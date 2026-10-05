package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.data.FrenoIntentos
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.PinAutodestruccion
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
    var accionShortcutPendiente: String?

    fun registrarInteraccion()
    fun ejecutar(bloque: suspend () -> Unit)
    fun ir(pantalla: Pantalla)
    fun irRaiz(pantalla: Pantalla)

    val contextoApp: Application get() = obtenerApp()

    // ------------------------------------------- freno a los intentos de clave

    /** Segundos que faltan para poder volver a probar. 0 si se puede probar ya. */
    fun esperaPorIntentos(): Long = FrenoIntentos.esperaSegundos(contextoApp)

    suspend fun apuntarFallo() = withContext(Dispatchers.IO) {
        FrenoIntentos.apuntarFallo(contextoApp)
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
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) {
                    repositorio.reForjarBovedaConPerfil(chars, nuevoPerfil)
                }
                Diagnostico.apuntar("bóveda", "Bóveda re-forjada con perfil ${nuevoPerfil.titulo} (${nuevoPerfil.memoriaKiB / 1024} MiB RAM, ${nuevoPerfil.iteraciones} pasadas, ${nuevoPerfil.paralelismo} hilos)")
                registrarInteraccion()
                avisoInterno.value = "Bóveda re-cifrada con perfil ${nuevoPerfil.titulo}"
                alTerminar(true)
            } catch (e: Exception) {
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
                delay(520)
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
                delay(520)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                verificarBackupAutomatico()
            } catch (e: Exception) {
                apuntarFallo()
                Diagnostico.apuntar("bóveda", "Desbloqueo con contraseña rechazado")
                errorInterno.value = "Contraseña incorrecta"
                alTerminar(false)
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
                delay(520)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                verificarBackupAutomatico()
            } catch (e: Exception) {
                Diagnostico.apuntar("huella", "La clave desenvuelta no abrió la bóveda: ${e.javaClass.simpleName}")
                errorInterno.value = "No se pudo abrir la bóveda con la huella"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(clave)
            }
        }
    }

    fun solicitarAccionShortcut(accion: String) {
        if (repositorio.estaDesbloqueada) {
            ejecutarAccionShortcut(accion)
        } else {
            accionShortcutPendiente = accion
        }
    }

    fun procesarShortcutPendiente() {
        val pendiente = accionShortcutPendiente ?: return
        accionShortcutPendiente = null
        ejecutarAccionShortcut(pendiente)
    }

    fun ejecutarAccionShortcut(accion: String) {
        when (accion) {
            "nueva_entrada" -> ir(Pantalla.Editar(id = null))
            "buscar" -> irRaiz(Pantalla.Lista)
            "escanear_qr" -> ir(Pantalla.Escaner())
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
        if (estabaDesbloqueada) {
            val limite = repositorio.ajustes.actual.autoBloqueoSegundos
            val mensaje = if (porInactividad) "Bloqueada por caducidad de tiempo (${limite}s de inactividad)" else "Bloqueada manualmente"
            Diagnostico.apuntar("bóveda", mensaje)
        }
        registrarInteraccion()
        irRaiz(if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding)
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

            val tiempoBase = entradas.minOfOrNull { it.creadaEn }
                ?: repositorio.archivoBoveda.lastModified().takeIf { it > 0L }
                ?: ahora

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
}
