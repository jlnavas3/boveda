package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en ajustes de seguridad, bloqueo, ofuscación visual, cámara e historial de claves.
 */
interface VaultAjustesSeguridadDelegate {
    val repositorio: VaultRepository

    fun ajustarAutoBloqueo(segundos: Int) {
        repositorio.ajustes.actualizar { it.copy(autoBloqueoSegundos = segundos) }
        val desc = if (segundos == 0) "desactivado" else "${segundos}s"
        Diagnostico.apuntar("seguridad", "Tiempo de auto-bloqueo configurado en $desc")
    }

    fun ajustarPortapapeles(segundos: Int) {
        repositorio.ajustes.actualizar { it.copy(portapapelesSegundos = segundos) }
        val desc = if (segundos == 0) "desactivado" else "${segundos}s"
        Diagnostico.apuntar("seguridad", "Tiempo de limpieza de portapapeles configurado en $desc")
    }

    fun ajustarUmbralAntiguedad(dias: Int) {
        repositorio.ajustes.actualizar { it.copy(umbralAntiguedadDias = dias) }
        val desc = if (dias == 0) "desactivado" else "${dias} días"
        Diagnostico.apuntar("seguridad", "Umbral de contraseñas antiguas configurado en $desc")
    }

    fun ajustarSeguridadVisualActiva(activa: Boolean) {
        repositorio.ajustes.actualizar { it.copy(seguridadVisualActiva = activa) }
        Diagnostico.apuntar("seguridad", "Seguridad visual ${if (activa) "activada" else "desactivada"}")
    }

    fun ajustarEstiloOcultamientoVisual(estilo: String) = repositorio.ajustes.actualizar { it.copy(estiloOcultamientoVisual = estilo) }
    fun ajustarTiempoAutoOcultar(segundos: Int) = repositorio.ajustes.actualizar { it.copy(tiempoAutoOcultarSegundos = segundos) }
    fun ajustarOcultarUsuario(ocultar: Boolean) = repositorio.ajustes.actualizar { it.copy(ocultarUsuario = ocultar) }
    fun ajustarOcultarContrasena(ocultar: Boolean) = repositorio.ajustes.actualizar { it.copy(ocultarContrasena = ocultar) }
    fun ajustarOcultarTotp(ocultar: Boolean) = repositorio.ajustes.actualizar { it.copy(ocultarTotp = ocultar) }
    fun ajustarOcultarNotas(ocultar: Boolean) = repositorio.ajustes.actualizar { it.copy(ocultarNotas = ocultar) }
    fun ajustarOcultarCampos(ocultar: Boolean) = repositorio.ajustes.actualizar { it.copy(ocultarCampos = ocultar) }

    fun ajustarMotorCamara(clave: String) = repositorio.ajustes.actualizar { it.copy(motorCamara = clave) }

    fun ajustarProteccionPantalla(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(proteccionPantalla = activo) }
        val desc = if (activo) "activada" else "desactivada"
        Diagnostico.apuntar("seguridad", "Protección de pantalla (FLAG_SECURE) $desc por el usuario")
    }

    fun ajustarFrenoIntentosGratis(intentos: Int) {
        repositorio.ajustes.actualizar { it.copy(frenoIntentosGratis = intentos) }
        val desc = if (intentos <= 0) "desactivado" else "$intentos intentos"
        Diagnostico.apuntar("seguridad", "Tolerancia de intentos fallidos antes de penalización establecida en $desc")
    }

    fun ajustarFrenoSegundosMax(segundos: Long) {
        repositorio.ajustes.actualizar { it.copy(frenoSegundosMax = segundos) }
        Diagnostico.apuntar("seguridad", "Tiempo máximo de penalización establecido en ${segundos / 60} min")
    }

    fun restablecerBloqueoApp() {
        ajustarAutoBloqueo(AjustesDefaults.Seguridad.AUTO_BLOQUEO_SEGUNDOS)
        ajustarProteccionPantalla(AjustesDefaults.Seguridad.PROTECCION_PANTALLA)
    }

    fun restablecerFrenoIntentos() {
        repositorio.ajustes.actualizar {
            it.copy(
                frenoIntentosGratis = AjustesDefaults.Seguridad.FRENO_INTENTOS_GRATIS,
                frenoSegundosBase = AjustesDefaults.Seguridad.FRENO_SEGUNDOS_BASE,
                frenoSegundosMax = AjustesDefaults.Seguridad.FRENO_SEGUNDOS_MAX
            )
        }
    }

    fun restablecerPortapapeles() = ajustarPortapapeles(AjustesDefaults.Seguridad.PORTAPAPELES_SEGUNDOS)
    fun restablecerUmbralAntiguedad() = ajustarUmbralAntiguedad(AjustesDefaults.Seguridad.UMBRAL_ANTIGUEDAD_DIAS)

    fun restablecerSeguridadVisual() {
        repositorio.ajustes.actualizar {
            it.copy(
                seguridadVisualActiva = AjustesDefaults.SeguridadVisual.ACTIVA,
                estiloOcultamientoVisual = AjustesDefaults.SeguridadVisual.ESTILO,
                tiempoAutoOcultarSegundos = AjustesDefaults.SeguridadVisual.AUTO_OCULTAR_SEGUNDOS,
                ocultarUsuario = AjustesDefaults.SeguridadVisual.OCULTAR_USUARIO,
                ocultarContrasena = AjustesDefaults.SeguridadVisual.OCULTAR_CONTRASENA,
                ocultarTotp = AjustesDefaults.SeguridadVisual.OCULTAR_TOTP,
                ocultarNotas = AjustesDefaults.SeguridadVisual.OCULTAR_NOTAS,
                ocultarCampos = AjustesDefaults.SeguridadVisual.OCULTAR_CAMPOS
            )
        }
    }

    fun restablecerCamara() = ajustarMotorCamara(AjustesDefaults.Seguridad.MOTOR_CAMARA)
    fun restablecerArgon2() = repositorio.ajustes.actualizar { it.copy(perfilArgon2 = AjustesDefaults.Seguridad.PERFIL_ARGON2) }

    fun restablecerHistorialClavesConfig() {
        repositorio.ajustes.actualizar {
            it.copy(
                historialClavesMax = AjustesDefaults.HistorialCopias.HISTORIAL_MAX,
                historialClavesVaciadoAuto = AjustesDefaults.HistorialCopias.HISTORIAL_VACIADO_AUTO,
                historialClavesTiempoAutoDestruccion = AjustesDefaults.HistorialCopias.HISTORIAL_TIEMPO_AUTO_DESTRUCCION_MS
            )
        }
    }

    fun ajustarHistorialClavesMax(max: Int) {
        repositorio.ajustes.actualizar {
            val recortado = it.historialClaves.take(max.coerceAtLeast(1))
            it.copy(historialClavesMax = max, historialClaves = recortado)
        }
    }

    fun ajustarHistorialClavesVaciadoAuto(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(historialClavesVaciadoAuto = activo) }
    }

    fun ajustarHistorialClavesTiempoAutoDestruccion(ms: Long) {
        repositorio.ajustes.actualizar { it.copy(historialClavesTiempoAutoDestruccion = ms) }
    }

    fun eliminarDeHistorialClaves(id: String) {
        repositorio.ajustes.actualizar { actual ->
            actual.copy(historialClaves = actual.historialClaves.filterNot { it.id == id })
        }
    }

    fun vaciarHistorialClaves() {
        repositorio.ajustes.actualizar { it.copy(historialClaves = emptyList()) }
        Diagnostico.apuntar("generador", "Historial de contraseñas vaciado manualmente")
    }

    fun ajustarAutodestruccionIntentosFallidosMax(max: Int) {
        repositorio.ajustes.actualizar { it.copy(autodestruccionIntentosFallidosMax = max) }
        val desc = if (max <= 0) "desactivado" else "$max intentos fallidos"
        Diagnostico.apuntar("seguridad", "Autodestrucción por intentos fallidos configurada en $desc")
    }

    fun restablecerAutodestruccionIntentosFallidosMax() =
        ajustarAutodestruccionIntentosFallidosMax(AjustesDefaults.Seguridad.AUTODESTRUCCION_INTENTOS_FALLIDOS_MAX)
}
