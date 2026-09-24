package com.jlnavas3.bovedalocal.data

import android.content.Context
import android.content.SharedPreferences
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val jsonAjustes = Json { ignoreUnknownKeys = true }

private fun deserializarHistorial(raw: String): List<RegistroClaveGenerada> = try {
    if (raw.isBlank()) emptyList()
    else jsonAjustes.decodeFromString(raw)
} catch (_: Exception) {
    emptyList()
}

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
    /** Hex del color para el fondo de las tarjetas (o vacío para seguir el tema). */
    val colorTarjetas: String = "",
    /** "sistema", "claro" u "oscuro": tema visual de la aplicación. */
    val temaApp: String = "sistema",
    /** Momento (milisegundos epoch) de la última exportación de la bóveda; 0 si nunca se exportó. */
    val ultimaExportacionEn: Long = 0L,
    /** Días entre recordatorios para exportar la bóveda; 0 para no avisar nunca. */
    val recordatorioExportacionDias: Int = 30,
    /** "predeterminada", "comoda" o "compacta": espaciado entre elementos en la lista principal. */
    val densidadLista: String = "predeterminada",
    /** Criterio de ordenación de la lista: NOMBRE_AZ, NOMBRE_ZA, MODIFICACION_RECIENTE, CREACION_RECIENTE, ANTIGUEDAD. */
    val criterioOrdenacion: String = "NOMBRE_AZ",
    /** Si es true, las entradas con el mismo dominio/sitio se agrupan en un acordeón desplegable. */
    val agruparPorSitio: Boolean = false,
    // Preferencias del generador manual de 2FA
    val totpManualDigitos: Int = 6,
    val totpManualPeriodo: Int = 30,
    val totpManualAlgoritmo: String = "HmacSHA1",
    val totpSepararDigitos: Boolean = true,
    // Personalización de formas y bordes
    val curvaturaEsquinasDp: Float = 16f,
    val grosorBordeDp: Float = 1.0f,
    val estiloBorde: String = "acento",
    val espaciadoComponentesDp: Float = 14f,
    // Personalización del Widget de escritorio (2FA favoritos)
    val widgetGrosorBordeDp: Float = 0f,
    val widgetCurvaturaEsquinasDp: Float = 0f,
    val widgetTransparenciaFondo: Float = 0.50f,
    val widgetColorBorde: String = "#FFB300",
    val widgetColorContador: String = "#FFFFFF",
    val widgetColorCodigo: String = "#FFB300",
    val widgetColorTituloIcono: String = "#FFFFFF",
    val widgetHaptica: Boolean = true,
    val widgetHapticaIntensidad: Float = 0.20f,
    // Configuración Widget 1x1 ("Generador Rápido")
    val widget1x1Haptica: Boolean = true,
    val widget1x1HapticaIntensidad: Float = 0.20f,
    val widget1x1Modo: String = "aleatoria",
    val widget1x1Longitud: Int = 20,
    val widget1x1Patron: String = "XXXXX-XXXXX-XXXXX-XXXXX",
    val widget1x1Simbolos: String = "!@#$%&*()_-=+[]{}?/,.:;",
    val widget1x1CopiarPortapapeles: Boolean = true,
    val widget1x1MostrarToast: Boolean = true,
    val widget1x1GrosorBordeDp: Float = 0f,
    val widget1x1CurvaturaEsquinasDp: Float = 15f,
    val widget1x1TransparenciaFondo: Float = 1.0f,
    val widget1x1TamanoDp: Float = 55f,
    val widget1x1AnchoDp: Float = 55f,
    val widget1x1AltoDp: Float = 51f,
    val widget1x1BloquearProporcion: Boolean = false,
    val widget1x1OffsetX: Float = 0f,
    val widget1x1OffsetY: Float = 4f,
    val widget1x1Alineamiento: String = "arriba", // "arriba", "centro", "abajo", "izquierda", "derecha"
    val widget1x1ColorBorde: String = "#33332E",
    val widget1x1ColorIcono: String = "#E6FCFF",
    val widget1x1ColorFondo: String = "#2E3333",
    // Personalización de tipografía y textos
    val escalaTexto: Float = 1.0f,
    val pesoTexto: String = "normal",
    val cursivaTexto: Boolean = false,
    val espaciadoLetrasSp: Float = 0.0f,
    val interlineadoFactor: Float = 1.0f,
    val familiaFuente: String = "sans",
    /** Sincronización con Material You (Monet) en Android 12+ (API 31+). */
    val colorDinamicoSistema: Boolean = true,
    // Configuración Quick Settings Tile ("Generador Rápido")
    val tileModo: String = "longitud",
    val tileLongitud: Int = 20,
    val tilePatron: String = "XXXXX-XXXXX-XXXXX-XXXXX",
    val tileCopiarPortapapeles: Boolean = true,
    val tileMostrarToast: Boolean = true,
    val tileHaptica: Boolean = true,
    val tileHapticaIntensidad: Float = 0.8f,
    // Colores semánticos de secciones funcionales
    val colorSeguridad: String = "",
    val colorArgon2: String = "",
    val colorCamara: String = "",
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
    val indiceAmplitudOlaDp: Float = 109f,
    val indiceRadioOlaDp: Float = 169f,
    val indiceEscalaLetras: Float = 1.5f,
    val indiceMostrarCirculo: Boolean = true,
    val indiceTamanoCirculoDp: Float = 50f,
    val indiceOffsetCirculoDp: Float = 136f,
    val indiceHaptica: Boolean = true,
    val indiceAnchoTactilDp: Float = 45f,
    val indiceTonoLetras: Float = 80f,
    val indiceIncluirEnie: Boolean = true,
    val indiceResaltarEntradas: Boolean = true,
    val indiceResaltarSoloPrimera: Boolean = true,
    val formatoFecha: String = "DD/MM/AAAA",
    val formatoHora: String = "24h",
    val formatoTelefono: String = "### ### ####",
    val separadorDecimal: String = ".",
    /** Perfil de derivación Argon2id: "estandar", "reforzado" o "ultraseguro". */
    val perfilArgon2: String = "estandar",
    /** FLAG_SECURE: protección anti-captura de pantalla y anti-recientes. Activa por defecto. */
    val proteccionPantalla: Boolean = true,
    // Historial temporal de contraseñas generadas
    val historialClavesMax: Int = 15,
    val historialClavesVaciadoAuto: Boolean = true,
    val historialClavesTiempoAutoDestruccion: Long = 30 * 60 * 1000L,
    val historialClaves: List<RegistroClaveGenerada> = emptyList(),
    // Copia de seguridad automática local rotativa
    val backupAutoFrecuenciaDias: Int = 0,
    val backupAutoPasswordCifrado: String = "",
    val backupAutoUltimaEjecucion: Long = 0L,
    val backupAutoMaxCopias: Int = 5,
    val backupAutoPatronNombre: String = "{99}-backup-{FECHA}",
    val backupAutoSecuencia: Int = 0,
    val mostrarIdsAjustes: Boolean = false,
    /** Animación de pantalla bloqueada: "engranajes" (mecanismo relojero) o "puerta" (anillos concéntricos). */
    val animacionDesbloqueo: String = "engranajes",
    // Configuración visual y física de los engranajes
    val engranajesVelocidad: Float = 24f,
    val engranajesGrosorBorde: Float = 0.7f,
    val engranajesAlturaDientes: Float = 0.76f,
    val engranajesAnchoDientes: Float = 1.00f,
    val engranajesGrosorRadios: Float = 1.40f,
    val engranajesCurvaturaRadios: Float = 1.00f,
    val engranajesCantidadRadios: Int = 6,
    val engranajesRadioInterior: Float = 0.80f,
    val engranajesTamanoEje: Float = 1.23f,
    val engranajesSombraIntensidad: Float = 0.95f,
    val engranajesColorBrillo: String = "#ABA799",
    val engranajesColorPrincipal: String = "#918D7E",
    val engranajesColorSombraMedio: String = "#635C57",
    val engranajesColorSombraOscuro: String = "#404038",
    val engranajesColorBisel: String = "#A5A19D",
    val engranajesColorInterior: String = "#00000000",
    val engranajesColorCubo: String = "#D3D1C8",
    val engranajesColorEje: String = "#141316",
    // Configuración visual y física de la puerta de bóveda
    val puertaVelocidad: Float = 1.0f,
    val puertaGrosorAnillos: Float = 1.0f,
    val puertaColor: String = "",
    // Resaltado / Alumbrado visual de filas y grupos en navegación de ajustes
    val alumbradoActivo: Boolean = true,
    val alumbradoIntensidad: Float = 0.5f,
    val alumbradoRepeticiones: Int = 2,
    val alumbradoDuracionMs: Int = 600,
    // Vibración háptica global de la app
    val hapticaApp: Boolean = true,
    val hapticaAppIntensidad: Float = 0.10f,
    // Indicadores superiores de contenido en tarjetas de la lista
    val mostrarIndicadoresContenido: Boolean = true,
    // Colores aislados exclusivos para datos e indicadores de tarjetas
    val colorDatosUsuario: String = "#0284C7",
    val colorDatosContrasena: String = "#0D9488",
    val colorDatos2FA: String = "#F97316",
    val colorDatosPasskey: String = "#8B5CF6",
    val colorDatosWeb: String = "#06B6D4",
    val colorDatosApp: String = "#10B981"
)


class AlmacenAjustes(contexto: Context) {

    private val prefs: SharedPreferences = run {
        val actual = contexto.getSharedPreferences("ajustes_boveda", Context.MODE_PRIVATE)
        val antigua = contexto.getSharedPreferences("ajustes_pepo_boveda", Context.MODE_PRIVATE)
        if (actual.all.isEmpty() && antigua.all.isNotEmpty()) {
            val ed = actual.edit()
            antigua.all.forEach { (k, v) ->
                when (v) {
                    is Boolean -> ed.putBoolean(k, v)
                    is Int -> ed.putInt(k, v)
                    is Long -> ed.putLong(k, v)
                    is Float -> ed.putFloat(k, v)
                    is String -> ed.putString(k, v)
                }
            }
            ed.apply()
            antigua.edit().clear().apply()
        }
        actual
    }

    private val _ajustes = MutableStateFlow(leer())
    val ajustes: StateFlow<AjustesApp> = _ajustes

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        val nuevo = leer()
        _ajustes.value = nuevo
        com.jlnavas3.bovedalocal.util.Haptica.sincronizar(nuevo)
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
        com.jlnavas3.bovedalocal.util.Haptica.sincronizar(_ajustes.value)
    }

    fun recargar() {
        _ajustes.value = leer()
    }

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

        // Migración única a los valores predefinidos elegidos por el usuario (110dp / 250dp / 1.6x / 45dp)
        if (!prefs.getBoolean("v2_indice_defecto_usuario", false)) {
            val ampPrev = prefs.getFloat("indice_amplitud_ola_dp", 95f)
            val radPrev = prefs.getFloat("indice_radio_ola_dp", 220f)
            val escPrev = prefs.getFloat("indice_escala_letras", 1.9f)
            val ancPrev = prefs.getFloat("indice_ancho_tactil_dp", 50f)
            prefs.edit()
                .putBoolean("v2_indice_defecto_usuario", true)
                .putFloat("indice_amplitud_ola_dp", if (ampPrev == 95f) 110f else ampPrev)
                .putFloat("indice_radio_ola_dp", if (radPrev == 220f) 250f else radPrev)
                .putFloat("indice_escala_letras", if (escPrev == 1.9f) 1.6f else escPrev)
                .putFloat("indice_ancho_tactil_dp", if (ancPrev == 50f) 45f else ancPrev)
                .apply()
        }

        // Migración a los valores predefinidos del widget elegidos por el usuario (0dp / 0dp / 50%)
        if (!prefs.getBoolean("v1_widget_defecto_usuario", false)) {
            val grosorPrev = prefs.getFloat("widget_grosor_borde_dp", 1.0f)
            val radPrev = prefs.getFloat("widget_curvatura_esquinas_dp", 16f)
            val transPrev = prefs.getFloat("widget_transparencia_fondo", 0.90f)
            prefs.edit()
                .putBoolean("v1_widget_defecto_usuario", true)
                .putFloat("widget_grosor_borde_dp", if (grosorPrev == 1.0f) 0f else grosorPrev)
                .putFloat("widget_curvatura_esquinas_dp", if (radPrev == 16f) 0f else radPrev)
                .putFloat("widget_transparencia_fondo", if (transPrev == 0.90f) 0.50f else transPrev)
                .apply()
        }

        if (!prefs.getBoolean("v1_engranajes_interior_hueco", false)) {
            val colorIntPrevio = prefs.getString("engranajes_color_interior", "#19181C") ?: "#19181C"
            prefs.edit()
                .putBoolean("v1_engranajes_interior_hueco", true)
                .putString("engranajes_color_interior", if (colorIntPrevio == "#19181C") "#00000000" else colorIntPrevio)
                .apply()
        }

        if (!prefs.getBoolean("v2_widget_contador_blanco", false)) {
            val colorContadorPrev = prefs.getString("widget_color_contador", "#FFB300")
            prefs.edit()
                .putBoolean("v2_widget_contador_blanco", true)
                .putString("widget_color_contador", if (colorContadorPrev == "#FFB300") "#FFFFFF" else colorContadorPrev)
                .apply()
        }

        if (!prefs.getBoolean("v3_animacion_engranajes_defecto", false)) {
            val animPrev = prefs.getString("animacion_desbloqueo", "puerta")
            prefs.edit()
                .putBoolean("v3_animacion_engranajes_defecto", true)
                .putString("animacion_desbloqueo", if (animPrev == "puerta") "engranajes" else animPrev)
                .apply()
        }

        if (!prefs.getBoolean("v2_engranajes_defecto_usuario", false)) {
            val editor = prefs.edit().putBoolean("v2_engranajes_defecto_usuario", true)
            if (prefs.getFloat("engranajes_velocidad", 35f) == 35f) editor.putFloat("engranajes_velocidad", 24f)
            if (prefs.getFloat("engranajes_grosor_borde", 1.4f) == 1.4f) editor.putFloat("engranajes_grosor_borde", 0.7f)
            if (prefs.getFloat("engranajes_altura_dientes", 1.0f) == 1.0f) editor.putFloat("engranajes_altura_dientes", 0.76f)
            if (prefs.getFloat("engranajes_ancho_dientes", 1.0f) == 1.0f) editor.putFloat("engranajes_ancho_dientes", 1.00f)
            if (prefs.getFloat("engranajes_grosor_radios", 1.0f) == 1.0f) editor.putFloat("engranajes_grosor_radios", 1.40f)
            if (prefs.getFloat("engranajes_curvatura_radios", 0.55f) == 0.55f) editor.putFloat("engranajes_curvatura_radios", 1.00f)
            if (prefs.getInt("engranajes_cantidad_radios", 0) == 0) editor.putInt("engranajes_cantidad_radios", 6)
            if (prefs.getFloat("engranajes_radio_interior", 0.72f) == 0.72f) editor.putFloat("engranajes_radio_interior", 0.80f)
            if (prefs.getFloat("engranajes_tamano_eje", 1.0f) == 1.0f) editor.putFloat("engranajes_tamano_eje", 1.23f)
            if (prefs.getFloat("engranajes_sombra_intensidad", 0.85f) == 0.85f) editor.putFloat("engranajes_sombra_intensidad", 0.95f)
            if (prefs.getString("engranajes_color_brillo", "#FFE082") == "#FFE082") editor.putString("engranajes_color_brillo", "#ABA799")
            if (prefs.getString("engranajes_color_principal", "#FFB300") == "#FFB300") editor.putString("engranajes_color_principal", "#918D7E")
            if (prefs.getString("engranajes_color_sombra_medio", "#FF8F00") == "#FF8F00") editor.putString("engranajes_color_sombra_medio", "#635C57")
            if (prefs.getString("engranajes_color_sombra_oscuro", "#2E2207") == "#2E2207") editor.putString("engranajes_color_sombra_oscuro", "#404038")
            if (prefs.getString("engranajes_color_bisel", "#FFD54F") == "#FFD54F") editor.putString("engranajes_color_bisel", "#A5A19D")
            if (prefs.getString("engranajes_color_cubo", "#FFECB3") == "#FFECB3") editor.putString("engranajes_color_cubo", "#D3D1C8")
            if (prefs.getString("engranajes_color_eje", "#141316") == "#141316") editor.putString("engranajes_color_eje", "#141316")
            editor.apply()
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
            widgetGrosorBordeDp = prefs.getFloat("widget_grosor_borde_dp", 0f),
            widgetCurvaturaEsquinasDp = prefs.getFloat("widget_curvatura_esquinas_dp", 0f),
            widgetTransparenciaFondo = prefs.getFloat("widget_transparencia_fondo", 0.50f),
            widgetColorBorde = prefs.getString("widget_color_borde", "#FFB300") ?: "#FFB300",
            widgetColorContador = prefs.getString("widget_color_contador", "#FFFFFF") ?: "#FFFFFF",
            widgetColorCodigo = prefs.getString("widget_color_codigo", "#FFB300") ?: "#FFB300",
            widgetColorTituloIcono = prefs.getString("widget_color_titulo_icono", "#FFFFFF") ?: "#FFFFFF",
            widgetHaptica = prefs.getBoolean("widget_haptica", true),
            widgetHapticaIntensidad = prefs.getFloat("widget_haptica_intensidad", 0.20f),
            widget1x1Haptica = prefs.getBoolean("widget_1x1_haptica", true),
            widget1x1HapticaIntensidad = prefs.getFloat("widget_1x1_haptica_intensidad", 0.20f),
            widget1x1Modo = prefs.getString("widget_1x1_modo", "aleatoria") ?: "aleatoria",
            widget1x1Longitud = prefs.getInt("widget_1x1_longitud", 20),
            widget1x1Patron = prefs.getString("widget_1x1_patron", "XXXXX-XXXXX-XXXXX-XXXXX") ?: "XXXXX-XXXXX-XXXXX-XXXXX",
            widget1x1Simbolos = prefs.getString("widget_1x1_simbolos", "!@#$%&*()_-=+[]{}?/,.:;") ?: "!@#$%&*()_-=+[]{}?/,.:;",
            widget1x1CopiarPortapapeles = prefs.getBoolean("widget_1x1_copiar_portapapeles", true),
            widget1x1MostrarToast = prefs.getBoolean("widget_1x1_mostrar_toast", true),
            widget1x1GrosorBordeDp = prefs.getFloat("widget_1x1_grosor_borde_dp", 0f),
            widget1x1CurvaturaEsquinasDp = prefs.getFloat("widget_1x1_curvatura_esquinas_dp", 15f),
            widget1x1TransparenciaFondo = prefs.getFloat("widget_1x1_transparencia_fondo", 1.0f),
            widget1x1TamanoDp = prefs.getFloat("widget_1x1_ancho_dp", prefs.getFloat("widget_1x1_tamano_dp", 55f)),
            widget1x1AnchoDp = prefs.getFloat("widget_1x1_ancho_dp", prefs.getFloat("widget_1x1_tamano_dp", 55f)),
            widget1x1AltoDp = prefs.getFloat("widget_1x1_alto_dp", prefs.getFloat("widget_1x1_tamano_dp", 51f)),
            widget1x1BloquearProporcion = prefs.getBoolean("widget_1x1_bloquear_proporcion", false),
            widget1x1OffsetX = prefs.getFloat("widget_1x1_offset_x", 0f),
            widget1x1OffsetY = prefs.getFloat("widget_1x1_offset_y", 4f),
            widget1x1Alineamiento = prefs.getString("widget_1x1_alineamiento", "arriba") ?: "arriba",
            widget1x1ColorBorde = prefs.getString("widget_1x1_color_borde", "#33332E") ?: "#33332E",
            widget1x1ColorIcono = prefs.getString("widget_1x1_color_icono", "#E6FCFF") ?: "#E6FCFF",
            widget1x1ColorFondo = prefs.getString("widget_1x1_color_fondo", "#2E3333") ?: "#2E3333",
            escalaTexto = prefs.getFloat("escala_texto", 1.0f),
            pesoTexto = prefs.getString("peso_texto", "normal") ?: "normal",
            cursivaTexto = prefs.getBoolean("cursiva_texto", false),
            espaciadoLetrasSp = prefs.getFloat("espaciado_letras_sp", 0.0f),
            interlineadoFactor = prefs.getFloat("interlineado_factor", 1.0f),
            familiaFuente = prefs.getString("familia_fuente", "sans") ?: "sans",
            colorDinamicoSistema = prefs.getBoolean("color_dinamico_sistema", true),
            tileModo = prefs.getString("tile_modo", "longitud") ?: "longitud",
            tileLongitud = prefs.getInt("tile_longitud", 20),
            tilePatron = prefs.getString("tile_patron", "XXXXX-XXXXX-XXXXX-XXXXX") ?: "XXXXX-XXXXX-XXXXX-XXXXX",
            tileCopiarPortapapeles = prefs.getBoolean("tile_copiar", true),
            tileMostrarToast = prefs.getBoolean("tile_toast", true),
            tileHaptica = prefs.getBoolean("tile_haptica", true),
            tileHapticaIntensidad = prefs.getFloat("tile_haptica_intensidad", 0.8f),
            colorSeguridad = prefs.getString("color_seguridad", "") ?: "",
            colorArgon2 = prefs.getString("color_argon2", "") ?: "",
            colorCamara = prefs.getString("color_camara", "") ?: "",
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
            indiceAmplitudOlaDp = prefs.getFloat("indice_amplitud_ola_dp", 109f),
            indiceRadioOlaDp = prefs.getFloat("indice_radio_ola_dp", 169f),
            indiceEscalaLetras = prefs.getFloat("indice_escala_letras", 1.5f),
            indiceMostrarCirculo = prefs.getBoolean("indice_mostrar_circulo", true),
            indiceTamanoCirculoDp = prefs.getFloat("indice_tamano_circulo_dp", 50f),
            indiceOffsetCirculoDp = prefs.getFloat("indice_offset_circulo_dp", 136f),
            indiceHaptica = prefs.getBoolean("indice_haptica", true),
            indiceAnchoTactilDp = prefs.getFloat("indice_ancho_tactil_dp", 45f),
            indiceTonoLetras = prefs.getFloat("indice_tono_letras", 80f),
            indiceIncluirEnie = prefs.getBoolean("indice_incluir_enie", true),
            indiceResaltarEntradas = prefs.getBoolean("indice_resaltar_entradas", true),
            indiceResaltarSoloPrimera = prefs.getBoolean("indice_resaltar_solo_primera", true),
            formatoFecha = prefs.getString("formato_fecha", "DD/MM/AAAA") ?: "DD/MM/AAAA",
            formatoHora = prefs.getString("formato_hora", "24h") ?: "24h",
            formatoTelefono = prefs.getString("formato_telefono", "### ### ####") ?: "### ### ####",
            separadorDecimal = prefs.getString("separador_decimal", ".") ?: ".",
            perfilArgon2 = prefs.getString("perfil_argon2", "estandar") ?: "estandar",
            proteccionPantalla = prefs.getBoolean("proteccion_pantalla", true),
            historialClavesMax = prefs.getInt("historial_claves_max", 15),
            historialClavesVaciadoAuto = prefs.getBoolean("historial_claves_vaciado_auto", true),
            historialClavesTiempoAutoDestruccion = prefs.getLong("historial_claves_tiempo_autodestruccion", 30 * 60 * 1000L),
            historialClaves = deserializarHistorial(prefs.getString("historial_claves_json", "") ?: ""),
            backupAutoFrecuenciaDias = prefs.getInt("backup_auto_frecuencia_dias", 0),
            backupAutoPasswordCifrado = prefs.getString("backup_auto_password", "") ?: "",
            backupAutoUltimaEjecucion = prefs.getLong("backup_auto_ultima_ejecucion", 0L),
            backupAutoMaxCopias = prefs.getInt("backup_auto_max_copias", 5),
            backupAutoPatronNombre = run {
                val guardado = prefs.getString("backup_auto_patron_nombre", null)
                if (guardado == null || guardado == "boveda-auto-{FECHA}" || guardado == "{99}-boveda-auto-{FECHA}") {
                    "{99}-backup-{FECHA}"
                } else {
                    guardado
                }
            },
            backupAutoSecuencia = prefs.getInt("backup_auto_secuencia", 0),
            mostrarIdsAjustes = prefs.getBoolean("mostrar_ids_ajustes", false),
            animacionDesbloqueo = prefs.getString("animacion_desbloqueo", "engranajes") ?: "engranajes",
            engranajesVelocidad = prefs.getFloat("engranajes_velocidad", 24f),
            engranajesGrosorBorde = prefs.getFloat("engranajes_grosor_borde", 0.7f),
            engranajesAlturaDientes = prefs.getFloat("engranajes_altura_dientes", 0.76f),
            engranajesAnchoDientes = prefs.getFloat("engranajes_ancho_dientes", 1.00f),
            engranajesGrosorRadios = prefs.getFloat("engranajes_grosor_radios", 1.40f),
            engranajesCurvaturaRadios = prefs.getFloat("engranajes_curvatura_radios", 1.00f),
            engranajesCantidadRadios = prefs.getInt("engranajes_cantidad_radios", 6),
            engranajesRadioInterior = prefs.getFloat("engranajes_radio_interior", 0.80f),
            engranajesTamanoEje = prefs.getFloat("engranajes_tamano_eje", 1.23f),
            engranajesSombraIntensidad = prefs.getFloat("engranajes_sombra_intensidad", 0.95f),
            engranajesColorBrillo = prefs.getString("engranajes_color_brillo", "#ABA799") ?: "#ABA799",
            engranajesColorPrincipal = prefs.getString("engranajes_color_principal", "#918D7E") ?: "#918D7E",
            engranajesColorSombraMedio = prefs.getString("engranajes_color_sombra_medio", "#635C57") ?: "#635C57",
            engranajesColorSombraOscuro = prefs.getString("engranajes_color_sombra_oscuro", "#404038") ?: "#404038",
            engranajesColorBisel = prefs.getString("engranajes_color_bisel", "#A5A19D") ?: "#A5A19D",
            engranajesColorInterior = prefs.getString("engranajes_color_interior", "#00000000") ?: "#00000000",
            engranajesColorCubo = prefs.getString("engranajes_color_cubo", "#D3D1C8") ?: "#D3D1C8",
            engranajesColorEje = prefs.getString("engranajes_color_eje", "#141316") ?: "#141316",
            puertaVelocidad = prefs.getFloat("puerta_velocidad", 1.0f),
            puertaGrosorAnillos = prefs.getFloat("puerta_grosor_anillos", 1.0f),
            puertaColor = prefs.getString("puerta_color", "") ?: "",
            alumbradoActivo = prefs.getBoolean("alumbrado_activo", true),
            alumbradoIntensidad = prefs.getFloat("alumbrado_intensidad", 0.5f),
            alumbradoRepeticiones = prefs.getInt("alumbrado_repeticiones", 2),
            alumbradoDuracionMs = prefs.getInt("alumbrado_duracion_ms", 600),
            hapticaApp = prefs.getBoolean("haptica_app", true),
            hapticaAppIntensidad = prefs.getFloat("haptica_app_intensidad", 0.10f),
            mostrarIndicadoresContenido = prefs.getBoolean("mostrar_indicadores_contenido", true),
            colorDatosUsuario = prefs.getString("color_datos_usuario", "#0284C7") ?: "#0284C7",
            colorDatosContrasena = prefs.getString("color_datos_contrasena", "#0D9488") ?: "#0D9488",
            colorDatos2FA = prefs.getString("color_datos_2fa", "#F97316") ?: "#F97316",
            colorDatosPasskey = prefs.getString("color_datos_passkey", "#8B5CF6") ?: "#8B5CF6",
            colorDatosWeb = prefs.getString("color_datos_web", "#06B6D4") ?: "#06B6D4",
            colorDatosApp = prefs.getString("color_datos_app", "#10B981") ?: "#10B981"
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
            .putFloat("widget_grosor_borde_dp", nuevo.widgetGrosorBordeDp)
            .putFloat("widget_curvatura_esquinas_dp", nuevo.widgetCurvaturaEsquinasDp)
            .putFloat("widget_transparencia_fondo", nuevo.widgetTransparenciaFondo)
            .putString("widget_color_borde", nuevo.widgetColorBorde)
            .putString("widget_color_contador", nuevo.widgetColorContador)
            .putString("widget_color_codigo", nuevo.widgetColorCodigo)
            .putString("widget_color_titulo_icono", nuevo.widgetColorTituloIcono)
            .putBoolean("widget_haptica", nuevo.widgetHaptica)
            .putFloat("widget_haptica_intensidad", nuevo.widgetHapticaIntensidad)
            .putBoolean("widget_1x1_haptica", nuevo.widget1x1Haptica)
            .putFloat("widget_1x1_haptica_intensidad", nuevo.widget1x1HapticaIntensidad)
            .putString("widget_1x1_modo", nuevo.widget1x1Modo)
            .putInt("widget_1x1_longitud", nuevo.widget1x1Longitud)
            .putString("widget_1x1_patron", nuevo.widget1x1Patron)
            .putString("widget_1x1_simbolos", nuevo.widget1x1Simbolos)
            .putBoolean("widget_1x1_copiar_portapapeles", nuevo.widget1x1CopiarPortapapeles)
            .putBoolean("widget_1x1_mostrar_toast", nuevo.widget1x1MostrarToast)
            .putFloat("widget_1x1_grosor_borde_dp", nuevo.widget1x1GrosorBordeDp)
            .putFloat("widget_1x1_curvatura_esquinas_dp", nuevo.widget1x1CurvaturaEsquinasDp)
            .putFloat("widget_1x1_transparencia_fondo", nuevo.widget1x1TransparenciaFondo)
            .putFloat("widget_1x1_tamano_dp", nuevo.widget1x1AnchoDp)
            .putFloat("widget_1x1_ancho_dp", nuevo.widget1x1AnchoDp)
            .putFloat("widget_1x1_alto_dp", nuevo.widget1x1AltoDp)
            .putBoolean("widget_1x1_bloquear_proporcion", nuevo.widget1x1BloquearProporcion)
            .putFloat("widget_1x1_offset_x", nuevo.widget1x1OffsetX)
            .putFloat("widget_1x1_offset_y", nuevo.widget1x1OffsetY)
            .putString("widget_1x1_alineamiento", nuevo.widget1x1Alineamiento)
            .putString("widget_1x1_color_borde", nuevo.widget1x1ColorBorde)
            .putString("widget_1x1_color_icono", nuevo.widget1x1ColorIcono)
            .putString("widget_1x1_color_fondo", nuevo.widget1x1ColorFondo)
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
            .putFloat("tile_haptica_intensidad", nuevo.tileHapticaIntensidad)
            .putString("color_seguridad", nuevo.colorSeguridad)
            .putString("color_argon2", nuevo.colorArgon2)
            .putString("color_camara", nuevo.colorCamara)
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
            .putFloat("indice_tamano_circulo_dp", nuevo.indiceTamanoCirculoDp)
            .putFloat("indice_offset_circulo_dp", nuevo.indiceOffsetCirculoDp)
            .putBoolean("indice_haptica", nuevo.indiceHaptica)
            .putFloat("indice_ancho_tactil_dp", nuevo.indiceAnchoTactilDp)
            .putFloat("indice_tono_letras", nuevo.indiceTonoLetras)
            .putBoolean("indice_incluir_enie", nuevo.indiceIncluirEnie)
            .putBoolean("indice_resaltar_entradas", nuevo.indiceResaltarEntradas)
            .putBoolean("indice_resaltar_solo_primera", nuevo.indiceResaltarSoloPrimera)
            .putString("formato_fecha", nuevo.formatoFecha)
            .putString("formato_hora", nuevo.formatoHora)
            .putString("formato_telefono", nuevo.formatoTelefono)
            .putString("separador_decimal", nuevo.separadorDecimal)
            .putString("perfil_argon2", nuevo.perfilArgon2)
            .putBoolean("proteccion_pantalla", nuevo.proteccionPantalla)
            .putInt("historial_claves_max", nuevo.historialClavesMax)
            .putBoolean("historial_claves_vaciado_auto", nuevo.historialClavesVaciadoAuto)
            .putLong("historial_claves_tiempo_autodestruccion", nuevo.historialClavesTiempoAutoDestruccion)
            .putString("historial_claves_json", jsonAjustes.encodeToString(nuevo.historialClaves))
            .putInt("backup_auto_frecuencia_dias", nuevo.backupAutoFrecuenciaDias)
            .putString("backup_auto_password", nuevo.backupAutoPasswordCifrado)
            .putLong("backup_auto_ultima_ejecucion", nuevo.backupAutoUltimaEjecucion)
            .putInt("backup_auto_max_copias", nuevo.backupAutoMaxCopias)
            .putString("backup_auto_patron_nombre", nuevo.backupAutoPatronNombre)
            .putInt("backup_auto_secuencia", nuevo.backupAutoSecuencia)
            .putBoolean("mostrar_ids_ajustes", nuevo.mostrarIdsAjustes)
            .putString("animacion_desbloqueo", nuevo.animacionDesbloqueo)
            .putFloat("engranajes_velocidad", nuevo.engranajesVelocidad)
            .putFloat("engranajes_grosor_borde", nuevo.engranajesGrosorBorde)
            .putFloat("engranajes_altura_dientes", nuevo.engranajesAlturaDientes)
            .putFloat("engranajes_ancho_dientes", nuevo.engranajesAnchoDientes)
            .putFloat("engranajes_grosor_radios", nuevo.engranajesGrosorRadios)
            .putFloat("engranajes_curvatura_radios", nuevo.engranajesCurvaturaRadios)
            .putInt("engranajes_cantidad_radios", nuevo.engranajesCantidadRadios)
            .putFloat("engranajes_radio_interior", nuevo.engranajesRadioInterior)
            .putFloat("engranajes_tamano_eje", nuevo.engranajesTamanoEje)
            .putFloat("engranajes_sombra_intensidad", nuevo.engranajesSombraIntensidad)
            .putString("engranajes_color_brillo", nuevo.engranajesColorBrillo)
            .putString("engranajes_color_principal", nuevo.engranajesColorPrincipal)
            .putString("engranajes_color_sombra_medio", nuevo.engranajesColorSombraMedio)
            .putString("engranajes_color_sombra_oscuro", nuevo.engranajesColorSombraOscuro)
            .putString("engranajes_color_bisel", nuevo.engranajesColorBisel)
            .putString("engranajes_color_interior", nuevo.engranajesColorInterior)
            .putString("engranajes_color_cubo", nuevo.engranajesColorCubo)
            .putString("engranajes_color_eje", nuevo.engranajesColorEje)
            .putFloat("puerta_velocidad", nuevo.puertaVelocidad)
            .putFloat("puerta_grosor_anillos", nuevo.puertaGrosorAnillos)
            .putString("puerta_color", nuevo.puertaColor)
            .putBoolean("alumbrado_activo", nuevo.alumbradoActivo)
            .putFloat("alumbrado_intensidad", nuevo.alumbradoIntensidad)
            .putInt("alumbrado_repeticiones", nuevo.alumbradoRepeticiones)
            .putInt("alumbrado_duracion_ms", nuevo.alumbradoDuracionMs)
            .putBoolean("haptica_app", nuevo.hapticaApp)
            .putFloat("haptica_app_intensidad", nuevo.hapticaAppIntensidad)
            .putBoolean("mostrar_indicadores_contenido", nuevo.mostrarIndicadoresContenido)
            .putString("color_datos_usuario", nuevo.colorDatosUsuario)
            .putString("color_datos_contrasena", nuevo.colorDatosContrasena)
            .putString("color_datos_2fa", nuevo.colorDatos2FA)
            .putString("color_datos_passkey", nuevo.colorDatosPasskey)
            .putString("color_datos_web", nuevo.colorDatosWeb)
            .putString("color_datos_app", nuevo.colorDatosApp)
            .apply()
        _ajustes.value = nuevo
        com.jlnavas3.bovedalocal.util.Haptica.sincronizar(nuevo)
    }

    companion object {
        val OPCIONES_ANIMACION_DESBLOQUEO = listOf(
            "puerta" to "Puerta de bóveda",
            "engranajes" to "Mecanismo de engranajes"
        )
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
            -30 to "Cada 30 minutos (prueba)",
            7 to "Cada 7 días",
            30 to "Cada 30 días",
            60 to "Cada 60 días",
            90 to "Cada 90 días"
        )
        val OPCIONES_MAX_COPIAS_BACKUP_AUTO = listOf(
            5 to "5 copias",
            10 to "10 copias",
            20 to "20 copias",
            100 to "100 copias",
            0 to "Infinitas"
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
        val OPCIONES_HISTORIAL_MAX = listOf(
            5 to "5 contraseñas",
            10 to "10 contraseñas",
            15 to "15 contraseñas",
            20 to "20 contraseñas",
            25 to "25 contraseñas",
            30 to "30 contraseñas"
        )
        val OPCIONES_AUTODESTRUCCION_HISTORIAL = listOf(
            5 * 60 * 1000L to "5 minutos",
            10 * 60 * 1000L to "10 minutos",
            20 * 60 * 1000L to "20 minutos",
            30 * 60 * 1000L to "30 minutos",
            60 * 60 * 1000L to "1 hora",
            2 * 60 * 60 * 1000L to "2 horas",
            3 * 60 * 60 * 1000L to "3 horas",
            24 * 60 * 60 * 1000L to "1 día",
            2 * 24 * 60 * 60 * 1000L to "2 días",
            3 * 24 * 60 * 60 * 1000L to "3 días",
            7 * 24 * 60 * 60 * 1000L to "1 semana"
        )
        val OPCIONES_FRECUENCIA_BACKUP_AUTO = listOf(
            0 to "Nunca",
            1 to "Cada día",
            7 to "Cada semana",
            14 to "Cada 2 semanas",
            30 to "Cada mes",
            60 to "Cada 2 meses",
            90 to "Cada 3 meses"
        )
    }
}

