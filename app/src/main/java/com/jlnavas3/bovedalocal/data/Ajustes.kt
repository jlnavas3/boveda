package com.jlnavas3.bovedalocal.data

import android.content.Context
import android.content.SharedPreferences
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
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
    /** Clave de icono en el launcher (las 20 variantes de activity-alias). */
    val iconoLauncher: String = "ambar",
    /** Clave de [com.jlnavas3.bovedalocal.ui.theme.PaletaAcento] o hex: color de acento principal. */
    val colorAcento: String = "ambar",
    /** Hex del color para los íconos internos (o vacío para seguir el acento). */
    val colorIconosInternos: String = "",
    /** Hex del color para los títulos y cabeceras (o vacío para seguir el acento). */
    val colorTitulos: String = "",
    /** Hex del color para las tarjetas / superficies (o vacío para usar el predeterminado del tema). */
    val colorTarjetas: String = "",
    /** "sistema", "claro" u "oscuro". */
    val temaApp: String = "sistema",
    /** 0 si nunca se ha exportado todavía. */
    val ultimaExportacionEn: Long = 0L,
    /** Días entre avisos de "haz una copia"; 0 = recordatorio apagado. */
    val recordatorioExportacionDias: Int = 30,
    /** "predeterminada", "comoda" o "compacta". */
    val densidadLista: String = "predeterminada",
    /** Criterio de ordenación activo en la lista principal (CriterioOrdenacion.name). */
    val criterioOrdenacion: String = "NOMBRE_AZ",
    /** Agrupa cuentas del mismo servicio/sitio en un bloque plegable. */
    val agruparPorSitio: Boolean = true,
    /** Defaults for manually entered TOTP secrets; otpauth QR parameters override them. */
    val totpManualDigitos: Int = 6,
    val totpManualPeriodo: Int = 30,
    val totpManualAlgoritmo: String = "HmacSHA1",
    val totpSepararDigitos: Boolean = true,
    // Personalización de bordes y formas
    val curvaturaEsquinasDp: Float = 6f,
    val grosorBordeDp: Float = 0.8f,
    val estiloBorde: String = "marcado",
    val espaciadoComponentesDp: Float = 14f,
    // Personalización de tipografía y textos
    val escalaTexto: Float = 1.0f,
    val pesoTexto: String = "normal",
    val cursivaTexto: Boolean = false,
    val espaciadoLetrasSp: Float = 0.0f,
    val interlineadoFactor: Float = 1.0f,
    val familiaFuente: String = "sans",
    /** Sincronización con Material You (Monet) en Android 12+ (API 31+). */
    val colorDinamicoSistema: Boolean = false,
    // Configuración Quick Settings Tile ("Generador Rápido")
    val tileModo: String = "longitud",
    val tileLongitud: Int = 20,
    val tilePatron: String = "XXXXX-XXXXX-XXXXX-XXXXX",
    val tileCopiarPortapapeles: Boolean = true,
    val tileMostrarToast: Boolean = true,
    val tileHaptica: Boolean = true,
    // Colores semánticos de secciones funcionales
    val colorSeguridad: String = "",
    val color2FA: String = "",
    val colorPasskeys: String = "",
    val colorGenerador: String = "",
    val colorSalud: String = "",
    val colorPapelera: String = "",
    val colorExportacion: String = "",
    // Registro del archivo CSV importado de Google
    val csvGoogleRuta: String = "",
    val csvGoogleUri: String = "",
    val csvGoogleCuentas: Int = 0,
    val csvGoogleEliminado: Boolean = false,
    // Índice Alfabético Lateral (Fast-scroller con ola estilo Niagara)
    val mostrarIndiceAlfabetico: Boolean = true,
    val indiceEfectoOla: Boolean = true,
    val indiceAmplitudOlaDp: Float = 95f,
    val indiceRadioOlaDp: Float = 220f,
    val indiceEscalaLetras: Float = 1.9f,
    val indiceMostrarCirculo: Boolean = true,
    val indiceOffsetCirculoDp: Float = 145f,
    val indiceHaptica: Boolean = true,
    val indiceAnchoTactilDp: Float = 50f
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
        val rawAutoBloqueo = prefs.getInt("auto_bloqueo", 30)
        val autoBloqueo = if (rawAutoBloqueo < 5) 30 else rawAutoBloqueo

        // Migración única si el usuario tenía los valores por defecto previos (18dp / 1dp / sutil)
        if (!prefs.getBoolean("v1_formas_defecto_v2", false)) {
            val curvaturaPrevia = prefs.getFloat("curvatura_esquinas_dp", 18f)
            val grosorPrevio = prefs.getFloat("grosor_borde_dp", 1f)
            val estiloPrevio = prefs.getString("estilo_borde", "sutil") ?: "sutil"
            val nuevaCurvatura = if (curvaturaPrevia == 18f) 6f else curvaturaPrevia
            val nuevoGrosor = if (grosorPrevio == 1f) 0.8f else grosorPrevio
            val nuevoEstilo = if (estiloPrevio == "sutil") "marcado" else estiloPrevio
            prefs.edit()
                .putBoolean("v1_formas_defecto_v2", true)
                .putFloat("curvatura_esquinas_dp", nuevaCurvatura)
                .putFloat("grosor_borde_dp", nuevoGrosor)
                .putString("estilo_borde", nuevoEstilo)
                .apply()
        }

        return AjustesApp(
            autoBloqueoSegundos = autoBloqueo,
            portapapelesSegundos = prefs.getInt("portapapeles", 30),
            modoGrabacion = false,
            biometriaActiva = biometriaActiva,
            biometriaModo = modo,
            motorCamara = prefs.getString("motor_camara", "auto") ?: "auto",
            nombrePersonalizado = prefs.getString("nombre_personalizado", "") ?: "",
            iconoLauncher = prefs.getString("icono_launcher", prefs.getString("color_acento", "ambar")) ?: "ambar",
            colorAcento = prefs.getString("color_acento", "ambar") ?: "ambar",
            colorIconosInternos = prefs.getString("color_iconos_internos", "") ?: "",
            colorTitulos = prefs.getString("color_titulos", "") ?: "",
            colorTarjetas = prefs.getString("color_tarjetas", "") ?: "",
            temaApp = prefs.getString("tema_app", "sistema") ?: "sistema",
            ultimaExportacionEn = prefs.getLong("ultima_exportacion", 0L),
            recordatorioExportacionDias = prefs.getInt("recordatorio_exportacion_dias", 30),
            densidadLista = prefs.getString("densidad_lista", "predeterminada") ?: "predeterminada",
            criterioOrdenacion = prefs.getString("criterio_ordenacion", "NOMBRE_AZ") ?: "NOMBRE_AZ",
            agruparPorSitio = prefs.getBoolean("agrupar_por_sitio", true),
            totpManualDigitos = prefs.getInt("totp_manual_digitos", 6),
            totpManualPeriodo = prefs.getInt("totp_manual_periodo", 30),
            totpManualAlgoritmo = prefs.getString("totp_manual_algoritmo", "HmacSHA1") ?: "HmacSHA1",
            totpSepararDigitos = prefs.getBoolean("totp_separar_digitos", true),
            curvaturaEsquinasDp = prefs.getFloat("curvatura_esquinas_dp", 6f),
            grosorBordeDp = prefs.getFloat("grosor_borde_dp", 0.8f),
            estiloBorde = prefs.getString("estilo_borde", "marcado") ?: "marcado",
            espaciadoComponentesDp = prefs.getFloat("espaciado_componentes_dp", 14f),
            escalaTexto = prefs.getFloat("escala_texto", 1.0f),
            pesoTexto = prefs.getString("peso_texto", "normal") ?: "normal",
            cursivaTexto = prefs.getBoolean("cursiva_texto", false),
            espaciadoLetrasSp = prefs.getFloat("espaciado_letras_sp", 0.0f),
            interlineadoFactor = prefs.getFloat("interlineado_factor", 1.0f),
            familiaFuente = prefs.getString("familia_fuente", "sans") ?: "sans",
            colorDinamicoSistema = prefs.getBoolean("color_dinamico_sistema", false),
            tileModo = prefs.getString("tile_modo", "longitud") ?: "longitud",
            tileLongitud = prefs.getInt("tile_longitud", 20),
            tilePatron = prefs.getString("tile_patron", "XXXXX-XXXXX-XXXXX-XXXXX") ?: "XXXXX-XXXXX-XXXXX-XXXXX",
            tileCopiarPortapapeles = prefs.getBoolean("tile_copiar", true),
            tileMostrarToast = prefs.getBoolean("tile_toast", true),
            tileHaptica = prefs.getBoolean("tile_haptica", true),
            colorSeguridad = prefs.getString("color_seguridad", "") ?: "",
            color2FA = prefs.getString("color_2fa", "") ?: "",
            colorPasskeys = prefs.getString("color_passkeys", "") ?: "",
            colorGenerador = prefs.getString("color_generador", "") ?: "",
            colorSalud = prefs.getString("color_salud", "") ?: "",
            colorPapelera = prefs.getString("color_papelera", "") ?: "",
            colorExportacion = prefs.getString("color_exportacion", "") ?: "",
            csvGoogleRuta = prefs.getString("csv_google_ruta", "") ?: "",
            csvGoogleUri = prefs.getString("csv_google_uri", "") ?: "",
            csvGoogleCuentas = prefs.getInt("csv_google_cuentas", 0),
            csvGoogleEliminado = prefs.getBoolean("csv_google_eliminado", false),
            mostrarIndiceAlfabetico = prefs.getBoolean("mostrar_indice_alfabetico", true),
            indiceEfectoOla = prefs.getBoolean("indice_efecto_ola", true),
            indiceAmplitudOlaDp = prefs.getFloat("indice_amplitud_ola_dp", 95f),
            indiceRadioOlaDp = prefs.getFloat("indice_radio_ola_dp", 220f),
            indiceEscalaLetras = prefs.getFloat("indice_escala_letras", 1.9f),
            indiceMostrarCirculo = prefs.getBoolean("indice_mostrar_circulo", true),
            indiceOffsetCirculoDp = prefs.getFloat("indice_offset_circulo_dp", 145f),
            indiceHaptica = prefs.getBoolean("indice_haptica", true),
            indiceAnchoTactilDp = prefs.getFloat("indice_ancho_tactil_dp", 50f)
        )
    }

    fun actualizar(transformar: (AjustesApp) -> AjustesApp) {
        val nuevo = transformar(_ajustes.value)
        prefs.edit()
            .putInt("auto_bloqueo", nuevo.autoBloqueoSegundos)
            .putInt("portapapeles", nuevo.portapapelesSegundos)
            .putBoolean("modo_grabacion", false)
            .putBoolean("biometria", nuevo.biometriaActiva)
            .putString("biometria_modo", nuevo.biometriaModo)
            .putString("motor_camara", nuevo.motorCamara)
            .putString("nombre_personalizado", nuevo.nombrePersonalizado)
            .putString("icono_launcher", nuevo.iconoLauncher)
            .putString("color_acento", nuevo.colorAcento)
            .putString("color_iconos_internos", nuevo.colorIconosInternos)
            .putString("color_titulos", nuevo.colorTitulos)
            .putString("color_tarjetas", nuevo.colorTarjetas)
            .putString("tema_app", nuevo.temaApp)
            .putLong("ultima_exportacion", nuevo.ultimaExportacionEn)
            .putInt("recordatorio_exportacion_dias", nuevo.recordatorioExportacionDias)
            .putString("densidad_lista", nuevo.densidadLista)
            .putString("criterio_ordenacion", nuevo.criterioOrdenacion)
            .putBoolean("agrupar_por_sitio", nuevo.agruparPorSitio)
            .putInt("totp_manual_digitos", nuevo.totpManualDigitos)
            .putInt("totp_manual_periodo", nuevo.totpManualPeriodo)
            .putString("totp_manual_algoritmo", nuevo.totpManualAlgoritmo)
            .putBoolean("totp_separar_digitos", nuevo.totpSepararDigitos)
            .putFloat("curvatura_esquinas_dp", nuevo.curvaturaEsquinasDp)
            .putFloat("grosor_borde_dp", nuevo.grosorBordeDp)
            .putString("estilo_borde", nuevo.estiloBorde)
            .putFloat("espaciado_componentes_dp", nuevo.espaciadoComponentesDp)
            .putFloat("escala_texto", nuevo.escalaTexto)
            .putString("peso_texto", nuevo.pesoTexto)
            .putBoolean("cursiva_texto", nuevo.cursivaTexto)
            .putFloat("espaciado_letras_sp", nuevo.espaciadoLetrasSp)
            .putFloat("interlineado_factor", nuevo.interlineadoFactor)
            .putString("familia_fuente", nuevo.familiaFuente)
            .putBoolean("color_dinamico_sistema", nuevo.colorDinamicoSistema)
            .putString("tile_modo", nuevo.tileModo)
            .putInt("tile_longitud", nuevo.tileLongitud)
            .putString("tile_patron", nuevo.tilePatron)
            .putBoolean("tile_copiar", nuevo.tileCopiarPortapapeles)
            .putBoolean("tile_toast", nuevo.tileMostrarToast)
            .putBoolean("tile_haptica", nuevo.tileHaptica)
            .putString("color_seguridad", nuevo.colorSeguridad)
            .putString("color_2fa", nuevo.color2FA)
            .putString("color_passkeys", nuevo.colorPasskeys)
            .putString("color_generador", nuevo.colorGenerador)
            .putString("color_salud", nuevo.colorSalud)
            .putString("color_papelera", nuevo.colorPapelera)
            .putString("color_exportacion", nuevo.colorExportacion)
            .putString("csv_google_ruta", nuevo.csvGoogleRuta)
            .putString("csv_google_uri", nuevo.csvGoogleUri)
            .putInt("csv_google_cuentas", nuevo.csvGoogleCuentas)
            .putBoolean("csv_google_eliminado", nuevo.csvGoogleEliminado)
            .putBoolean("mostrar_indice_alfabetico", nuevo.mostrarIndiceAlfabetico)
            .putBoolean("indice_efecto_ola", nuevo.indiceEfectoOla)
            .putFloat("indice_amplitud_ola_dp", nuevo.indiceAmplitudOlaDp)
            .putFloat("indice_radio_ola_dp", nuevo.indiceRadioOlaDp)
            .putFloat("indice_escala_letras", nuevo.indiceEscalaLetras)
            .putBoolean("indice_mostrar_circulo", nuevo.indiceMostrarCirculo)
            .putFloat("indice_offset_circulo_dp", nuevo.indiceOffsetCirculoDp)
            .putBoolean("indice_haptica", nuevo.indiceHaptica)
            .putFloat("indice_ancho_tactil_dp", nuevo.indiceAnchoTactilDp)
            .apply()
        _ajustes.value = nuevo
    }

    companion object {
        val OPCIONES_AUTO_BLOQUEO = listOf(
            5 to "5 segundos",
            10 to "10 segundos",
            15 to "15 segundos",
            20 to "20 segundos",
            25 to "25 segundos",
            30 to "30 segundos",
            40 to "40 segundos",
            50 to "50 segundos",
            60 to "1 minuto",
            120 to "2 minutos",
            300 to "5 minutos"
        )
        val OPCIONES_TILE_LONGITUD = listOf(
            10 to "10 caracteres",
            15 to "15 caracteres",
            20 to "20 caracteres",
            30 to "30 caracteres"
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
        val OPCIONES_ESTILO_BORDE = listOf(
            "sutil" to "Sutil",
            "acento" to "Acento",
            "marcado" to "Marcado",
            "ninguno" to "Sin borde"
        )
        val OPCIONES_PESO_TEXTO = listOf(
            "fino" to "Fino",
            "normal" to "Normal",
            "medio" to "Medio",
            "seminegrita" to "Seminegrita",
            "negrita" to "Negrita"
        )
        val OPCIONES_FAMILIA_FUENTE = listOf(
            "sans" to "Sans-Serif",
            "mono" to "Monospace (Terminal)",
            "serif" to "Serif (Editorial)",
            "cursiva" to "Cursiva"
        )
    }
}
