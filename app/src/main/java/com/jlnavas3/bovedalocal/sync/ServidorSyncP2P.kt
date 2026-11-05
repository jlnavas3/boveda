/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import java.io.Closeable
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.security.SecureRandom
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Resultado estructurado de una sincronización atendida con éxito en el rol Servidor/Anfitrión.
 */
data class ResultadoSyncServidor(
    val bovedaFusionada: ContenidoBoveda,
    val informeSync: InformeSincronizacion,
    val ipCliente: String
)

/**
 * Servidor TCP efímero de sincronización P2P para Android.
 * Réplica matemática y simétrica de `src/sync/p2p/tcp_server.rs` de Desktop.
 *
 * Permite que un teléfono Android actúe como anfitrión para:
 * 1. Sincronizar directamente con otro teléfono Android (`Android <-> Android`).
 * 2. Sincronizar con una app Desktop que se conecte como cliente (`Desktop -> Android`).
 */
class ServidorSyncP2P private constructor(
    val ips: List<String>,
    private val serverSocket: ServerSocket,
    val payloadQr: SyncQrPayload,
    val huellaVisual: String,
    private val skServidor: ByteArray,
    private val pskToken: ByteArray,
    private val permitirLoopback: Boolean = false
) : Closeable {

    val puerto: Int get() = serverSocket.localPort
    val puertoLocal: Int get() = serverSocket.localPort

    /**
     * Espera la conexión del cliente y ejecuta la sincronización completa.
     *
     * @param bovedaLocal Estado local de la bóveda antes de fusionar.
     * @param timeoutSegundos Tiempo límite global de espera y transferencia (por defecto 180s).
     * @param cancelado Bandera reactiva para abortar la escucha instantáneamente.
     * @param onClienteConectado Callback invocado en el instante exacto en que el cliente se conecta.
     * @param guardarCommitLocal Callback para persistir atómicamente la bóveda fusionada a disco.
     */
    fun atenderSincronizacion(
        bovedaLocal: ContenidoBoveda,
        timeoutSegundos: Long = 180L,
        cancelado: AtomicBoolean = AtomicBoolean(false),
        onClienteConectado: () -> Unit = {},
        guardarCommitLocal: (ContenidoBoveda) -> Unit
    ): ResultadoSyncServidor {
        val inicio = System.currentTimeMillis()
        val timeoutMs = timeoutSegundos * 1000L
        serverSocket.soTimeout = 1000 // Polling de 1 segundo para comprobar 'cancelado'

        var socketCliente: Socket? = null

        try {
            while (!cancelado.get()) {
                if (System.currentTimeMillis() - inicio > timeoutMs) {
                    throw SocketTimeoutException("Tiempo de espera agotado (${timeoutSegundos}s) esperando conexión móvil.")
                }

                try {
                    val socket = serverSocket.accept()
                    val ipRemota = socket.inetAddress.hostAddress ?: ""
                    val esValida = ValidadorRfc1918.esIpPrivadaRfc1918(ipRemota) ||
                        (permitirLoopback && (ipRemota == "127.0.0.1" || ipRemota == "::1"))
                    if (!esValida) {
                        socket.close()
                        continue
                    }
                    socketCliente = socket
                    break
                } catch (_: SocketTimeoutException) {
                    // Timeout del ciclo de 1s, continúa evaluando 'cancelado'
                } catch (e: SocketException) {
                    // Si el socket se cerró voluntariamente por cancelación del anfitrión
                    if (cancelado.get() || serverSocket.isClosed) {
                        break
                    }
                    val msg = e.message?.lowercase(Locale.ROOT) ?: ""
                    if (msg.contains("socket closed") || msg.contains("einval") || msg.contains("ebadf")) {
                        break
                    }
                    throw e
                }
            }

            if (cancelado.get() || serverSocket.isClosed || socketCliente == null) {
                throw kotlinx.coroutines.CancellationException("Sincronización P2P cancelada por el usuario.")
            }

            // Notificar conexión inmediata para restaurar brillo/energía en la UI
            onClienteConectado()

            socketCliente.soTimeout = (timeoutSegundos * 1000).toInt()
            val input = socketCliente.getInputStream()
            val output = socketCliente.getOutputStream()

            // 1. Leer trama ClientHello (64 bytes)
            val clientHello = WireProtocol.leerTrama(input, 1024)
            require(clientHello.size == 64) {
                "Trama ClientHello inválida: esperados 64 bytes, recibidos ${clientHello.size}."
            }

            // 2. Responder ServerHello y derivar claves de sesión
            val pkServidor = payloadQr.clavePublicaDesktop
            val respServer = HandshakeP2P.responderServerHello(
                clientHello = clientHello,
                pkDesktop = pkServidor,
                pskToken = pskToken,
                skDesktop = skServidor
            ) ?: throw IllegalStateException("Fallo de autenticación mutua: Token PSK incorrecto o corrupto.")

            val (serverHello, keys) = respServer
            WireProtocol.escribirTrama(output, serverHello)

            // 3. Inicializar cifrador en rol Servidor (Desktop/Host)
            val cipher = CifradorSesionP2P.paraServidor(keys)

            // 4. Recibir y descifrar la bóveda remota del cliente
            val frameRemoto = WireProtocol.leerTrama(input, WireProtocol.MAX_TRAMA_TAMANO_DEFECTO)
            val plaintextRemoto = cipher.open(frameRemoto)
            val msgRemoto = WireProtocol.deserializarMensaje(plaintextRemoto)
            require(msgRemoto is MensajeP2P.SyncPayload) {
                "Protocolo violado: esperada trama SyncPayload del cliente."
            }

            // 5. Ejecutar fusión determinista con emisión de informe en un pase
            val (bovedaFusionada, informe) = FusionadorBovedas.fusionarBovedasConInforme(
                bovedaLocal = bovedaLocal,
                bovedaRemota = msgRemoto.boveda
            )

            // 6. Cifrar y enviar la bóveda fusionada de vuelta al cliente con nuestro alias de anfitrión
            val bovedaParaCliente = bovedaFusionada.copy(aliasDispositivo = IdentidadDispositivoLocal.obtenerNombre())
            val msgFusionado = MensajeP2P.SyncPayload(bovedaParaCliente)
            val plaintextFusionado = WireProtocol.serializarMensaje(msgFusionado)
            val frameFusionado = cipher.seal(plaintextFusionado)
            WireProtocol.escribirTrama(output, frameFusionado)

            // 7. Esperar confirmación CommitAck del cliente
            val frameAck = WireProtocol.leerTrama(input, 1024)
            val plaintextAck = cipher.open(frameAck)
            val msgAck = WireProtocol.deserializarMensaje(plaintextAck)
            require(msgAck is MensajeP2P.CommitAck) {
                "Protocolo violado: esperado CommitAck del cliente."
            }

            // 8. Commit atómico local de la bóveda
            guardarCommitLocal(bovedaFusionada)

            // Limpieza de claves
            cipher.destruir()
            keys.destruir()

            val ipFinal = socketCliente.inetAddress.hostAddress ?: "Desconocida"
            return ResultadoSyncServidor(
                bovedaFusionada = bovedaFusionada,
                informeSync = informe,
                ipCliente = ipFinal
            )
        } finally {
            skServidor.fill(0)
            try { socketCliente?.close() } catch (_: Exception) {}
            close()
        }
    }

    override fun close() {
        try {
            if (!serverSocket.isClosed) {
                serverSocket.close()
            }
        } catch (_: Exception) {}
    }

    companion object {

        /**
         * Enlaza un nuevo servidor TCP efímero en un puerto disponible y genera claves y QR.
         */
        fun enlazar(
            ipsCandidatas: List<String> = DetectorIpLanAndroid.obtenerIpsLanPrivadas(),
            puertoDeseado: Int = 0
        ): ServidorSyncP2P {
            require(ipsCandidatas.isNotEmpty()) {
                "No se detectaron interfaces de red local privadas activas (Wi-Fi, Hotspot o Ethernet)."
            }

            val skServidor = X25519.generarClavePrivada()
            val pkServidor = X25519.derivarClavePublica(skServidor)

            val pskToken = ByteArray(32)
            SecureRandom().nextBytes(pskToken)

            val huellaVisual = HuellaVisualP2P.calcular(pkServidor, pskToken)

            val serverSocket = ServerSocket(puertoDeseado, 1, InetAddress.getByName("0.0.0.0"))
            val puerto = serverSocket.localPort

            val payload = SyncQrPayload(
                version = 2,
                ips = ipsCandidatas,
                puerto = puerto,
                clavePublicaDesktop = pkServidor,
                tokenPsk = pskToken,
                aliasDispositivo = IdentidadDispositivoLocal.obtenerNombre()
            )

            return ServidorSyncP2P(
                ips = ipsCandidatas,
                serverSocket = serverSocket,
                payloadQr = payload,
                huellaVisual = huellaVisual,
                skServidor = skServidor,
                pskToken = pskToken,
                permitirLoopback = false
            )
        }

        /**
         * Método de conveniencia para tests de integración y entornos con claves predeterminadas.
         */
        fun bind(
            skDesktop: ByteArray,
            pskToken: ByteArray,
            puerto: Int = 0,
            permitirLoopback: Boolean = true
        ): ServidorSyncP2P {
            val pkDesktop = X25519.derivarClavePublica(skDesktop)
            val huellaVisual = HuellaVisualP2P.calcular(pkDesktop, pskToken)
            val serverSocket = ServerSocket(puerto, 1, InetAddress.getByName("0.0.0.0"))
            val payload = SyncQrPayload(
                version = 2,
                ips = listOf("127.0.0.1"),
                puerto = serverSocket.localPort,
                clavePublicaDesktop = pkDesktop,
                tokenPsk = pskToken
            )
            return ServidorSyncP2P(
                ips = listOf("127.0.0.1"),
                serverSocket = serverSocket,
                payloadQr = payload,
                huellaVisual = huellaVisual,
                skServidor = skDesktop.clone(),
                pskToken = pskToken.clone(),
                permitirLoopback = permitirLoopback
            )
        }
    }
}
