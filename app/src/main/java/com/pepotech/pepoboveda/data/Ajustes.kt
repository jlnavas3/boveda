package com.pepotech.pepoboveda.data

import android.content.Context
import android.content.SharedPreferences
import com.pepotech.pepoboveda.crypto.BiometricKeyStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** El modo de huella en uso, o null si está apagada. Única lectura de [AjustesApp.biometriaModo]. */
val AjustesApp.modoBiometriaActivo: BiometricKeyStore.Modo?
    get() = if (biometriaActiva) BiometricKeyStore.Modo.desde(biometriaModo) else null

data class AjustesApp(
    val autoBloqueoSegundos: Int = 60,
    val portapapelesSegundos: Int = 30,
    val modoGrabacion: Boolean = false,
    val biometriaActiva: Boolean = false,
    /** "fuerte" (Clase 3 + Keystore atado), "compatible" (huella o PIN comprobados por Android) o "" si no hay. */
    val biometriaModo: String = "",
    /** "auto", "camerax" o "compatible": qué motor usa el escáner de QR. */
    val motorCamara: String = "auto",
    /** Nombre que se ve dentro de la app (cabecera del menú). El launcher siempre muestra "Bóveda local": Android no permite un rótulo de icono libre en tiempo de ejecución. */
    val nombrePersonalizado: String = "",
    /** Clave de [com.pepotech.pepoboveda.ui.theme.PaletaAcento]: color del tema Y del icono del launcher a la vez. */
    val colorAcento: String = "ambar",
    /** "sistema", "claro" u "oscuro". */
    val temaApp: String = "sistema",
    /** 0 si nunca se ha exportado todavía. */
    val ultimaExportacionEn: Long = 0L,
    /** Días entre avisos de "haz una copia"; 0 = recordatorio apagado. */
    val recordatorioExportacionDias: Int = 30,
    /** "predeterminada", "comoda" o "compacta". */
    val densidadLista: String = "predeterminada"
)

class AlmacenAjustes(contexto: Context) {

    private val prefs: SharedPreferences =
        contexto.getSharedPreferences("ajustes_pepo_boveda", Context.MODE_PRIVATE)

    private val _ajustes = MutableStateFlow(leer())
    val ajustes: StateFlow<AjustesApp> = _ajustes

    val actual: AjustesApp get() = _ajustes.value

    private fun leer(): AjustesApp {
        val biometriaActiva = prefs.getBoolean("biometria", false)
        var modo = prefs.getString("biometria_modo", "") ?: ""
        // Quien activó la huella antes de existir los modos la tenía en el fuerte, el único que había.
        if (biometriaActiva && modo.isEmpty()) modo = "fuerte"
        return AjustesApp(
            autoBloqueoSegundos = prefs.getInt("auto_bloqueo", 60),
            portapapelesSegundos = prefs.getInt("portapapeles", 30),
            modoGrabacion = prefs.getBoolean("modo_grabacion", false),
            biometriaActiva = biometriaActiva,
            biometriaModo = modo,
            motorCamara = prefs.getString("motor_camara", "auto") ?: "auto",
            nombrePersonalizado = prefs.getString("nombre_personalizado", "") ?: "",
            colorAcento = prefs.getString("color_acento", "ambar") ?: "ambar",
            temaApp = prefs.getString("tema_app", "sistema") ?: "sistema",
            ultimaExportacionEn = prefs.getLong("ultima_exportacion", 0L),
            recordatorioExportacionDias = prefs.getInt("recordatorio_exportacion_dias", 30),
            densidadLista = prefs.getString("densidad_lista", "predeterminada") ?: "predeterminada"
        )
    }

    fun actualizar(bloque: (AjustesApp) -> AjustesApp) {
        val nuevo = bloque(_ajustes.value)
        prefs.edit()
            .putInt("auto_bloqueo", nuevo.autoBloqueoSegundos)
            .putInt("portapapeles", nuevo.portapapelesSegundos)
            .putBoolean("modo_grabacion", nuevo.modoGrabacion)
            .putBoolean("biometria", nuevo.biometriaActiva)
            .putString("biometria_modo", nuevo.biometriaModo)
            .putString("motor_camara", nuevo.motorCamara)
            .putString("nombre_personalizado", nuevo.nombrePersonalizado)
            .putString("color_acento", nuevo.colorAcento)
            .putString("tema_app", nuevo.temaApp)
            .putLong("ultima_exportacion", nuevo.ultimaExportacionEn)
            .putInt("recordatorio_exportacion_dias", nuevo.recordatorioExportacionDias)
            .putString("densidad_lista", nuevo.densidadLista)
            .apply()
        _ajustes.value = nuevo
    }

    companion object {
        val OPCIONES_AUTO_BLOQUEO = listOf(
            0 to "Al cerrar la app",
            30 to "30 segundos",
            60 to "1 minuto",
            300 to "5 minutos"
        )
        val OPCIONES_PORTAPAPELES = listOf(
            15 to "15 segundos",
            30 to "30 segundos",
            60 to "1 minuto"
        )
        val OPCIONES_TEMA = listOf(
            "sistema" to "Sistema",
            "claro" to "Claro",
            "oscuro" to "Oscuro"
        )
        val OPCIONES_RECORDATORIO_EXPORTACION = listOf(
            0 to "Nunca",
            30 to "Cada 30 días",
            60 to "Cada 60 días",
            90 to "Cada 90 días"
        )
        val OPCIONES_DENSIDAD_LISTA = listOf(
            "predeterminada" to "Predeterminada",
            "comoda" to "Cómoda",
            "compacta" to "Compacta"
        )
    }
}
