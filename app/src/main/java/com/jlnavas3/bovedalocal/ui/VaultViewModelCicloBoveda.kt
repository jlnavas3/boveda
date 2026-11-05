/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.data.FrenoIntentos
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.PinAutodestruccion
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

data class RecordatorioExportacionInfo(
    val titulo: String,
    val descripcion: String
)

interface VaultCicloBovedaDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application
    val avisoInterno: MutableStateFlow<String?>
    val errorInterno: MutableStateFlow<String?>
    val ofrecerBiometriaInterno: MutableStateFlow<Boolean>
    var accionShortcutPendiente: AccionShortcutPendiente?

    fun registrarInteraccion()
    fun ejecutar(bloque: suspend () -> Unit)
    fun ir(pantalla: Pantalla)
    fun irRaiz(pantalla: Pantalla)
    fun limpiarMemoriaScroll() {}
    fun procesarImportacionQrPendiente() {}

    val contextoApp: Application get() = obtenerApp()

    // ------------------------------------------- freno a los intentos de clave

    /** Segundos que faltan para poder volver a probar. 0 si se puede probar ya. */
    fun esperaPorIntentos(): Long = FrenoIntentos.esperaSegundos(
        contexto = contextoApp,
        maxCastigoSegundos = repositorio.ajustes.actual.frenoSegundosMax
    )

    fun intentosFallidosActuales(): Int = FrenoIntentos.obtenerIntentosFallidos(contextoApp)

    suspend fun apuntarFallo(): Int = withContext(Dispatchers.IO) {
        val ajustes = repositorio.ajustes.actual
        FrenoIntentos.apuntarFallo(
            contexto = contextoApp,
            gratis = ajustes.frenoIntentosGratis,
            castigoBaseSegundos = ajustes.frenoSegundosBase,
            maxCastigoSegundos = ajustes.frenoSegundosMax
        )
    }

    suspend fun limpiarFallos() = withContext(Dispatchers.IO) {
        FrenoIntentos.limpiar(contextoApp)
    }

    // ----------------------------------------------------------- bóveda: ciclo

    fun crearBoveda(password: String, identidadInicial: Identidad? = null, alTerminar: () -> Unit = {}) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                val identidades = if (identidadInicial != null) listOf(identidadInicial) else emptyList()
                withContext(Dispatchers.Default) { repositorio.crear(chars, identidades) }
                Diagnostico.apuntar("bóveda", "Bóveda creada y desbloqueada${if (identidadInicial != null) " con identidad '${identidadInicial.nombre}'" else ""}")
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                ofrecerBiometriaInterno.value = true
                alTerminar()
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun reForjarBoveda(password: String, nuevoPerfil: PerfilArgon2, alTerminar: (Boolean) -> Unit = {}) {
        ejecutar {
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "reforjar_argon2",
                titulo = "Ajustando seguridad Argon2id",
                faseInicial = "Verificando contraseña actual...",
                totalEstimado = 1
            )
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                "Derivando clave KDF (${nuevoPerfil.titulo})...",
                0.35f
            )

            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) {
                    repositorio.reForjarBovedaConPerfil(chars, nuevoPerfil)
                }
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    "Re-cifrando bóveda...",
                    0.85f
                )
                Diagnostico.apuntar("bóveda", "Bóveda re-forjada con perfil ${nuevoPerfil.titulo} (${nuevoPerfil.memoriaKiB / 1024} MiB RAM, ${nuevoPerfil.iteraciones} pasadas, ${nuevoPerfil.paralelismo} hilos)")
                registrarInteraccion()
                avisoInterno.value = "Bóveda re-cifrada con perfil ${nuevoPerfil.titulo}"
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "Perfil ${nuevoPerfil.titulo} aplicado con éxito",
                    totalCuentas = 1,
                    textoAccionResultado = "Aceptar",
                    textoAccionSecundaria = null
                )
                alTerminar(true)
            } catch (e: Exception) {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("Contraseña incorrecta o fallo al re-cifrar")
                Diagnostico.apuntar("bóveda", "Fallo al re-forjar bóveda: ${e.message}")
                errorInterno.value = "Contraseña incorrecta o fallo al re-cifrar"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun desbloquear(password: String, alTerminar: (Boolean) -> Unit = {}) {
        val espera = esperaPorIntentos()
        if (espera > 0) {
            errorInterno.value = "Demasiados intentos. Espera ${espera}s."
            alTerminar(false)
            return
        }
        if (PinAutodestruccion.esPinAutodestruccion(contextoApp, password)) {
            ejecutar {
                repositorio.borrarTodo()
                PinAutodestruccion.desactivar(contextoApp)
                BovedaSenuelo.desactivar(contextoApp)
                limpiarFallos()
                irRaiz(Pantalla.Onboarding)
                alTerminar(true)
            }
            return
        }
        if (BovedaSenuelo.esPinCoaccion(contextoApp, password)) {
            ejecutar {
                val senuelo = BovedaSenuelo.cargar(contextoApp).entradas
                repositorio.abrirSenuelo(senuelo)
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con contraseña maestra")
                registrarInteraccion()
                alTerminar(true)
                val esperaAnim = if (repositorio.ajustes.actual.animacionDesbloqueo == "ninguna") 0L else 320L
                if (esperaAnim > 0L) delay(esperaAnim)
                irRaiz(Pantalla.Lista)
            }
            return
        }
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) { repositorio.desbloquear(chars) }
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con contraseña maestra")
                registrarInteraccion()
                alTerminar(true)
                val esperaAnim = if (repositorio.ajustes.actual.animacionDesbloqueo == "ninguna") 0L else 320L
                if (esperaAnim > 0L) delay(esperaAnim)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                procesarImportacionQrPendiente()
                verificarBackupAutomatico()
                sincronizarTransferenciaCxfAlDesbloquear()
            } catch (e: Exception) {
                val fallos = apuntarFallo()
                Diagnostico.apuntar("bóveda", "Desbloqueo con contraseña rechazado (intento $fallos)")
                val maxFallos = repositorio.ajustes.actual.autodestruccionIntentosFallidosMax
                if (maxFallos > 0 && fallos >= maxFallos) {
                    Diagnostico.apuntar("seguridad", "Autodestrucción ejecutada tras alcanzar $fallos intentos fallidos consecutivos (límite: $maxFallos)")
                    repositorio.borrarTodo()
                    PinAutodestruccion.desactivar(contextoApp)
                    BovedaSenuelo.desactivar(contextoApp)
                    limpiarFallos()
                    irRaiz(Pantalla.Onboarding)
                    alTerminar(false)
                } else {
                    errorInterno.value = "Contraseña incorrecta"
                    alTerminar(false)
                }
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun desbloquearConClave(clave: ByteArray, alTerminar: (Boolean) -> Unit = {}) {
        ejecutar {
            try {
                withContext(Dispatchers.Default) { repositorio.desbloquearConClaveMaestra(clave) }
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con huella")
                registrarInteraccion()
                alTerminar(true)
                val esperaAnim = if (repositorio.ajustes.actual.animacionDesbloqueo == "ninguna") 0L else 320L
                if (esperaAnim > 0L) delay(esperaAnim)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                procesarImportacionQrPendiente()
                verificarBackupAutomatico()
                sincronizarTransferenciaCxfAlDesbloquear()
            } catch (e: Exception) {
                Diagnostico.apuntar("huella", "La clave desenvuelta no abrió la bóveda: ${e.javaClass.simpleName}")
                errorInterno.value = "No se pudo abrir la bóveda con la huella"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(clave)
            }
        }
    }

    fun solicitarAccionShortcut(
        accion: String,
        tituloInicial: String = "",
        urlInicial: String = "",
        tipoInicial: TipoEntrada? = null,
        telefonoInicial: String = ""
    ) {
        if (repositorio.estaDesbloqueada) {
            ejecutarAccionShortcut(accion, tituloInicial, urlInicial, tipoInicial, telefonoInicial)
        } else {
            accionShortcutPendiente = AccionShortcutPendiente(accion, tituloInicial, urlInicial, tipoInicial, telefonoInicial)
        }
    }

    fun procesarShortcutPendiente() {
        val pendiente = accionShortcutPendiente ?: return
        accionShortcutPendiente = null
        ejecutarAccionShortcut(
            pendiente.accion,
            pendiente.tituloInicial,
            pendiente.urlInicial,
            pendiente.tipoInicial,
            pendiente.telefonoInicial
        )
    }

    fun ejecutarAccionShortcut(
        accion: String,
        tituloInicial: String = "",
        urlInicial: String = "",
        tipoInicial: TipoEntrada? = null,
        telefonoInicial: String = ""
    ) {
        when (accion) {
            "nuevo_contacto" -> ir(
                Pantalla.Editar(
                    id = null,
                    tituloInicial = tituloInicial,
                    tipoInicial = TipoEntrada.CONTACTO,
                    telefonoInicial = telefonoInicial
                )
            )
            "nueva_entrada" -> ir(
                Pantalla.Editar(
                    id = null,
                    tituloInicial = tituloInicial,
                    urlInicial = urlInicial,
                    tipoInicial = tipoInicial,
                    telefonoInicial = telefonoInicial
                )
            )
            "buscar" -> irRaiz(Pantalla.Lista)
            "escanear_qr" -> ir(Pantalla.CamaraQr(null))
        }
    }

    fun verificarBackupAutomatico() {
        if (GestorBackupAutomatico.debeEjecutar(repositorio.ajustes.actual)) {
            ejecutarBackupAutomatico(manual = false)
        }
    }

    fun ejecutarBackupAutomatico(manual: Boolean = false, alTerminar: (Boolean, String) -> Unit = { _, _ -> }) {
        ejecutar {
            val (exito, mensaje) = withContext(Dispatchers.IO) {
                GestorBackupAutomatico.ejecutar(contextoApp, repositorio)
            }
            if (manual || exito) {
                avisoInterno.value = mensaje
            }
            alTerminar(exito, mensaje)
        }
    }

    fun bloquear(porInactividad: Boolean = false) {
        val estabaDesbloqueada = repositorio.estaDesbloqueada
        repositorio.bloquear()
        limpiarMemoriaScroll()
        com.jlnavas3.bovedalocal.util.GestorAppsInstaladas.limpiarCachesSensibles()
        com.jlnavas3.bovedalocal.util.ResolverIconoAppDominio.limpiarCaches()
        com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.limpiarMemoriaExportacion(contextoApp)
        com.jlnavas3.bovedalocal.util.comunicacion.PurgadorArchivosEfimeros.purgar(contextoApp)
        ejecutar {
            withContext(Dispatchers.IO) {
                com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.limpiarExportacion(contextoApp)
                com.jlnavas3.bovedalocal.util.comunicacion.PurgadorArchivosEfimeros.purgar(contextoApp)
            }
        }
        if (estabaDesbloqueada) {
            val limite = repositorio.ajustes.actual.autoBloqueoSegundos
            val mensaje = if (porInactividad) "Bloqueada por caducidad de tiempo (${limite}s de inactividad)" else "Bloqueada manualmente"
            Diagnostico.apuntar("bóveda", mensaje)
        }
        registrarInteraccion()
        irRaiz(if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding)
    }

    /**
     * Verifica de forma síncrona si la aplicación estuvo en segundo plano más tiempo del límite
     * configurado por el usuario. Si caducó, bloquea la bóveda y navega de inmediato a Pantalla.Desbloqueo.
     */
    fun verificarBloqueoSegundoPlano() {
        val app = contextoApp as? com.jlnavas3.bovedalocal.BovedaApp ?: return
        if (app.deberiaBloquearsePorInactividad()) {
            bloquear(porInactividad = true)
        }
    }

    /** Información del recordatorio de copia; null si no corresponde mostrarlo. */
    fun recordatorioExportacionInfo(): RecordatorioExportacionInfo? {
        val ajustes = repositorio.ajustes.actual
        val config = ajustes.recordatorioExportacionDias
        if (config == 0) return null

        val ahora = System.currentTimeMillis()
        val umbralMs = when (config) {
            -30 -> 30L * 60 * 1000 // 30 minutos
            in 1..Int.MAX_VALUE -> config.toLong() * 24 * 60 * 60 * 1000
            else -> return null
        }

        val ultimaCopia = maxOf(ajustes.ultimaExportacionEn, ajustes.backupAutoUltimaEjecucion)

        if (ultimaCopia > 0L) {
            val transcurridoMs = ahora - ultimaCopia
            if (transcurridoMs >= umbralMs) {
                return if (config == -30) {
                    val minutos = (transcurridoMs / (60 * 1000)).coerceAtLeast(30)
                    RecordatorioExportacionInfo(
                        titulo = "Hace $minutos min que no haces una copia",
                        descripcion = "Toca para ir a Copia de seguridad"
                    )
                } else {
                    val dias = (transcurridoMs / (24L * 60 * 60 * 1000)).coerceAtLeast(config.toLong())
                    RecordatorioExportacionInfo(
                        titulo = "Hace $dias ${if (dias == 1L) "día" else "días"} sin exportar una copia",
                        descripcion = "Toca para ir a Copia de seguridad"
                    )
                }
            }
            return null
        } else {
            // Nunca se ha realizado una copia ni exportación
            val entradas = repositorio.entradas()
            if (entradas.isEmpty()) return null

            val tiempoBase = if (ajustes.fechaCreacionBovedaLocal > 0L) {
                ajustes.fechaCreacionBovedaLocal
            } else {
                repositorio.archivoBoveda.lastModified().takeIf { it > 0L } ?: ahora
            }

            val transcurridoMs = ahora - tiempoBase
            if (transcurridoMs >= umbralMs) {
                return RecordatorioExportacionInfo(
                    titulo = "Copia de seguridad recomendada",
                    descripcion = "Aún no has realizado ninguna copia de tu bóveda. Toca para hacer una."
                )
            }
            return null
        }
    }

    /** Días sin exportar la bóveda; null si nunca se exportó o el recordatorio está apagado. */
    fun diasSinExportar(): Long? {
        val info = recordatorioExportacionInfo() ?: return null
        val config = repositorio.ajustes.actual.recordatorioExportacionDias
        if (config == -30) return 0L
        val ultimaCopia = maxOf(repositorio.ajustes.actual.ultimaExportacionEn, repositorio.ajustes.actual.backupAutoUltimaEjecucion)
        return if (ultimaCopia > 0L) (System.currentTimeMillis() - ultimaCopia) / (24L * 60 * 60 * 1000) else config.toLong()
    }

    fun sincronizarTransferenciaCxfAlDesbloquear() {
        val ajustes = repositorio.ajustes.actual
        if (ajustes.cxfModoTransferencia == "automatica") {
            ejecutar {
                withContext(Dispatchers.IO) {
                    try {
                        val entradas = repositorio.entradas().filter { it.eliminadaEn == 0L }
                        if (entradas.isNotEmpty()) {
                            com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.sincronizarExportacionAutomatica(
                                context = contextoApp,
                                entradas = entradas,
                                ajustes = ajustes
                            )
                        }
                    } catch (e: Exception) {
                        Diagnostico.apuntar("cxf", "Fallo al sincronizar exportación automática CXF: ${e.message}", e)
                    }
                }
            }
        }
    }

    /**
     * Obtiene una instantánea del estado actual de la bóveda para sincronización P2P.
     */
    fun obtenerContenidoActual(): com.jlnavas3.bovedalocal.data.ContenidoBoveda {
        return repositorio.obtenerContenidoActual()
    }

    /**
     * Aplica la bóveda fusionada resultante de la sincronización P2P en el repositorio local.
     */
    fun aplicarBovedaFusionada(bovedaFusionada: com.jlnavas3.bovedalocal.data.ContenidoBoveda) {
        repositorio.aplicarBovedaFusionada(bovedaFusionada)
    }
}
