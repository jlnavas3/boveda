/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * Cliente TCP de sincronización LAN P2P para Bóveda Local Android.
 * Espejo canónico de `src/sync/p2p/tcp_client.rs` en Rust.
 *
 * Características:
 * - Algoritmo "Happy Eyeballs" (conexión paralela concurrente a las IPs candidatas del QR).
 * - Handshake 1-RTT con autenticación mutua mediante token PSK y X25519.
 * - Cifrado de transporte ChaCha20-Poly1305 con protección Anti-Replay.
 * - Confirmación atómica de recepción mediante Commit ACK.
 */
object ClienteSyncP2P {

    /**
     * Ejecuta el ciclo completo de sincronización P2P con el servidor Desktop.
     *
     * @param payloadQr Contenido verificado del código QR escaneado.
     * @param bovedaLocal Estado actual de la bóveda local de contraseñas.
     * @param timeoutSegundos Tiempo límite global para la operación (por defecto 60 segundos).
     * @return Bóveda fusionada devuelta por el servidor Desktop.
     */
    fun sincronizarConServidor(
        payloadQr: SyncQrPayload,
        bovedaLocal: ContenidoBoveda,
        timeoutSegundos: Long = 60L
    ): ContenidoBoveda {
        val socket = conectarCandidataGanadora(payloadQr.ips, payloadQr.puerto, timeoutSegundos)

        try {
            socket.soTimeout = (timeoutSegundos * 1000).toInt()
            val input = socket.getInputStream()
            val output = socket.getOutputStream()

            // 1. Generar par efímero X25519 del cliente Android y construir ClientHello (64 bytes)
            val skAndroid = X25519.generarClavePrivada()
            val (clientHello, keys) = HandshakeP2P.construirClientHello(
                skAndroid = skAndroid,
                pkDesktop = payloadQr.clavePublicaDesktop,
                pskToken = payloadQr.tokenPsk
            )
            skAndroid.fill(0)

            // 2. Enviar ClientHello
            WireProtocol.escribirTrama(output, clientHello)

            // 3. Leer y verificar ServerHello (32 bytes)
            val serverHelloRaw = WireProtocol.leerTrama(input, 1024)
            require(serverHelloRaw.size == 32) {
                "Trama ServerHello inválida: esperados 32 bytes, recibidos ${serverHelloRaw.size}."
            }

            val pkAndroid = clientHello.copyOfRange(0, 32)
            val serverValido = HandshakeP2P.verificarServerHello(
                serverHello = serverHelloRaw,
                pkDesktop = payloadQr.clavePublicaDesktop,
                pkAndroid = pkAndroid,
                handshakeKey = keys.handshakeKey
            )
            require(serverValido) {
                "Fallo de autenticación mutua: Desktop no posee el token secreto PSK."
            }

            // 4. Instanciar cifrador simétrico en rol Cliente
            val cipher = CifradorSesionP2P.paraCliente(keys)

            // 5. Cifrar y enviar la bóveda local con alias del dispositivo al Servidor
            val bovedaConAlias = bovedaLocal.copy(aliasDispositivo = IdentidadDispositivoLocal.obtenerNombre())
            val msgLocal = MensajeP2P.SyncPayload(bovedaConAlias)
            val plaintextLocal = WireProtocol.serializarMensaje(msgLocal)
            val frameLocal = cipher.seal(plaintextLocal)
            WireProtocol.escribirTrama(output, frameLocal)

            // 6. Recibir y descifrar la bóveda fusionada resultante
            val frameFusionada = WireProtocol.leerTrama(input, WireProtocol.MAX_TRAMA_TAMANO_DEFECTO)
            val plaintextFusionada = cipher.open(frameFusionada)
            val msgFusionada = WireProtocol.deserializarMensaje(plaintextFusionada)

            val bovedaFusionada = when (msgFusionada) {
                is MensajeP2P.SyncPayload -> msgFusionada.boveda
                is MensajeP2P.CommitAck -> throw IllegalStateException("Protocolo violado: recibido CommitAck antes de la bóveda.")
            }

            // 7. Enviar confirmación CommitAck al servidor
            val msgAck = MensajeP2P.CommitAck("OK")
            val plaintextAck = WireProtocol.serializarMensaje(msgAck)
            val frameAck = cipher.seal(plaintextAck)
            WireProtocol.escribirTrama(output, frameAck)

            // 8. Limpieza de claves y cierre
            cipher.destruir()
            keys.destruir()

            try {
                socket.shutdownOutput()
            } catch (_: Exception) {}

            return bovedaFusionada
        } finally {
            try {
                socket.close()
            } catch (_: Exception) {}
        }
    }

    /**
     * Intenta conectar concurrentemente a todas las IPs candidatas ("Happy Eyeballs").
     * La primera conexión TCP exitosa es adoptada y las restantes se cancelan de inmediato.
     */
    private fun conectarCandidataGanadora(
        ips: List<String>,
        puerto: Int,
        timeoutSegundos: Long
    ): Socket {
        require(ips.isNotEmpty()) { "Lista de IPs candidatas vacía." }

        if (ips.size == 1) {
            val s = Socket()
            s.connect(InetSocketAddress(ips[0], puerto), (timeoutSegundos * 1000).toInt())
            return s
        }

        val ganador = CompletableFuture<Socket>()
        val encontrado = AtomicBoolean(false)
        val fallosRestantes = AtomicInteger(ips.size)
        val ultimoError = AtomicReference<Throwable?>(null)
        val hilos = mutableListOf<Thread>()

        for (ip in ips) {
            val t = Thread {
                val s = Socket()
                try {
                    s.connect(InetSocketAddress(ip, puerto), 4000)
                    if (encontrado.compareAndSet(false, true)) {
                        ganador.complete(s)
                    } else {
                        s.close()
                    }
                } catch (e: Exception) {
                    try { s.close() } catch (_: Exception) {}
                    ultimoError.set(e)
                    if (fallosRestantes.decrementAndGet() == 0 && !encontrado.get()) {
                        ganador.completeExceptionally(
                            ultimoError.get() ?: java.io.IOException("Fallo conectando a las IPs candidatas")
                        )
                    }
                }
            }
            hilos.add(t)
            t.start()
        }

        try {
            return ganador.get(timeoutSegundos, TimeUnit.SECONDS)
        } catch (e: Exception) {
            for (t in hilos) {
                t.interrupt()
            }
            val causa = if (e is ExecutionException && e.cause != null) e.cause!! else e
            throw IllegalStateException(
                "No fue posible conectar con Desktop en ninguna de las IPs: $ips en el puerto $puerto (${causa.javaClass.simpleName}: ${causa.message ?: "Sin respuesta"})",
                causa
            )
        }
    }
}
