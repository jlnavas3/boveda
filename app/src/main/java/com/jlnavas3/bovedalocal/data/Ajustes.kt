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

private fun deserializarPrefijosSubdominios(raw: String): List<String> = try {
    if (raw.isBlank()) AjustesDefaults.NormalizacionTitulos.PREFIJOS_SUBDOMINIOS
    else jsonAjustes.decodeFromString(raw)
} catch (_: Exception) {
    AjustesDefaults.NormalizacionTitulos.PREFIJOS_SUBDOMINIOS
}

/** El modo de huella en uso, o null si está apagada. Única lectura de [AjustesApp.biometriaModo]. */
val AjustesApp.modoBiometriaActivo: BiometricKeyStore.Modo?
    get() = if (biometriaActiva) BiometricKeyStore.Modo.desde(biometriaModo) else null

data class AjustesApp(
    val autoBloqueoSegundos: Int = AjustesDefaults.Seguridad.AUTO_BLOQUEO_SEGUNDOS,
    val portapapelesSegundos: Int = AjustesDefaults.Seguridad.PORTAPAPELES_SEGUNDOS,
    val modoGrabacion: Boolean = AjustesDefaults.Seguridad.MODO_GRABACION,
    val biometriaActiva: Boolean = AjustesDefaults.Seguridad.BIOMETRIA_ACTIVA,
    /** "fuerte" (Clase 3 + Keystore atado), "compatible" (huella o PIN comprobados por Android) o "" si no hay. */
    val biometriaModo: String = AjustesDefaults.Seguridad.BIOMETRIA_MODO,
    /** "auto", "camerax" o "compatible": qué motor usa el escáner de QR. */
    val motorCamara: String = AjustesDefaults.Seguridad.MOTOR_CAMARA,
    /** Nombre que se ve dentro de la app (cabecera del menú). El launcher siempre muestra "Bóveda local": Android no permite un rótulo de icono libre en tiempo de ejecución. */
    val nombrePersonalizado: String = AjustesDefaults.Tema.NOMBRE_PERSONALIZADO,
    /** Clave de icono en el launcher (las 20 variantes de activity-alias). */
    val iconoLauncher: String = AjustesDefaults.Tema.ICONO_LAUNCHER,
    /** Clave de [com.jlnavas3.bovedalocal.ui.theme.PaletaAcento] o hex: color de acento principal. */
    val colorAcento: String = AjustesDefaults.Tema.COLOR_ACENTO,
    /** Hex del color para los íconos internos (o vacío para seguir el acento). */
    val colorIconosInternos: String = AjustesDefaults.Tema.COLOR_ICONOS_INTERNOS,
    /** Hex del color para los títulos y cabeceras (o vacío para seguir el acento). */
    val colorTitulos: String = AjustesDefaults.Tema.COLOR_TITULOS,
    /** Hex del color para el fondo de las tarjetas (o vacío para seguir el tema). */
    val colorTarjetas: String = AjustesDefaults.Tema.COLOR_TARJETAS,
    /** "sistema", "claro" u "oscuro": tema visual de la aplicación. */
    val temaApp: String = AjustesDefaults.Tema.TEMA_APP,
    /** Momento (milisegundos epoch) de la última exportación de la bóveda; 0 si nunca se exportó. */
    val ultimaExportacionEn: Long = 0L,
    /** Días entre recordatorios para exportar la bóveda; 0 para no avisar nunca. */
    val recordatorioExportacionDias: Int = AjustesDefaults.HistorialCopias.RECORDATORIO_EXPORTACION_DIAS,
    /** "predeterminada", "comoda" o "compacta": espaciado entre elementos en la lista principal. */
    val densidadLista: String = AjustesDefaults.ListaFormatos.DENSIDAD_LISTA,
    /** Criterio de ordenación de la lista: NOMBRE_AZ, NOMBRE_ZA, MODIFICACION_RECIENTE, CREACION_RECIENTE, ANTIGUEDAD. */
    val criterioOrdenacion: String = AjustesDefaults.ListaFormatos.CRITERIO_ORDENACION,
    /** Si es true, las entradas con el mismo dominio/sitio se agrupan en un acordeón desplegable. */
    val agruparPorSitio: Boolean = AjustesDefaults.ListaFormatos.AGRUPAR_POR_SITIO,
    /** Modo de visualización de identidades: "chips", "secciones" o "desactivado". */
    val modoIdentidades: String = AjustesDefaults.ListaFormatos.MODO_IDENTIDADES,
    /** Relación jerárquica entre Identidades y Categorías: "identidad_sobre_categoria" o "categoria_sobre_identidad". */
    val jerarquiaOrganizacion: String = AjustesDefaults.ListaFormatos.JERARQUIA_ORGANIZACION,
    // Preferencias del generador manual de 2FA
    val totpManualDigitos: Int = AjustesDefaults.TotpManual.DIGITOS,
    val totpManualPeriodo: Int = AjustesDefaults.TotpManual.PERIODO,
    val totpManualAlgoritmo: String = AjustesDefaults.TotpManual.ALGORITMO,
    val totpSepararDigitos: Boolean = AjustesDefaults.TotpManual.SEPARAR_DIGITOS,
    // Personalización de formas y bordes
    val curvaturaEsquinasDp: Float = AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP,
    val grosorBordeDp: Float = AjustesDefaults.Formas.GROSOR_BORDE_DP,
    val estiloBorde: String = AjustesDefaults.Formas.ESTILO_BORDE,
    val espaciadoComponentesDp: Float = AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP,
    // Personalización del Widget de escritorio (2FA favoritos)
    val widgetGrosorBordeDp: Float = AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP,
    val widgetCurvaturaEsquinasDp: Float = AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP,
    val widgetTransparenciaFondo: Float = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO,
    val widgetColorBorde: String = AjustesDefaults.WidgetTotp.COLOR_BORDE,
    val widgetColorContador: String = AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
    val widgetColorCodigo: String = AjustesDefaults.WidgetTotp.COLOR_CODIGO,
    val widgetColorTituloIcono: String = AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
    val widgetHaptica: Boolean = AjustesDefaults.WidgetTotp.HAPTICA,
    val widgetHapticaIntensidad: Float = AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD,
    // Configuración Widget 1x1 ("Generador Rápido")
    val widget1x1Haptica: Boolean = AjustesDefaults.Widget1x1.HAPTICA,
    val widget1x1HapticaIntensidad: Float = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD,
    val widget1x1Modo: String = AjustesDefaults.Widget1x1.MODO,
    val widget1x1Longitud: Int = AjustesDefaults.Widget1x1.LONGITUD,
    val widget1x1Patron: String = AjustesDefaults.Widget1x1.PATRON,
    val widget1x1Simbolos: String = AjustesDefaults.Widget1x1.SIMBOLOS,
    val widget1x1CopiarPortapapeles: Boolean = AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES,
    val widget1x1MostrarToast: Boolean = AjustesDefaults.Widget1x1.MOSTRAR_TOAST,
    val widget1x1GrosorBordeDp: Float = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
    val widget1x1CurvaturaEsquinasDp: Float = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
    val widget1x1TransparenciaFondo: Float = AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO,
    val widget1x1TamanoDp: Float = AjustesDefaults.Widget1x1.TAMANO_DP,
    val widget1x1AnchoDp: Float = AjustesDefaults.Widget1x1.ANCHO_DP,
    val widget1x1AltoDp: Float = AjustesDefaults.Widget1x1.ALTO_DP,
    val widget1x1BloquearProporcion: Boolean = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
    val widget1x1OffsetX: Float = AjustesDefaults.Widget1x1.OFFSET_X,
    val widget1x1OffsetY: Float = AjustesDefaults.Widget1x1.OFFSET_Y,
    val widget1x1Alineamiento: String = AjustesDefaults.Widget1x1.ALINEAMIENTO, // "arriba", "centro", "abajo", "izquierda", "derecha"
    val widget1x1ColorBorde: String = AjustesDefaults.Widget1x1.COLOR_BORDE,
    val widget1x1ColorIcono: String = AjustesDefaults.Widget1x1.COLOR_ICONO,
    val widget1x1ColorFondo: String = AjustesDefaults.Widget1x1.COLOR_FONDO,
    val widget1x1DicewarePalabras: Int = AjustesDefaults.Widget1x1.DICEWARE_PALABRAS,
    val widget1x1DicewareSeparador: String = AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR,
    val widgetColorFilas: String = AjustesDefaults.WidgetTotp.COLOR_FILAS,
    val widgetTransparenciaFilas: Float = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS,
    val widgetTotpVidrioEsmerilado: Boolean = AjustesDefaults.WidgetTotp.VIDRIO_ESMERILADO,
    val widgetTotpEsmeriladoIntensidad: Float = AjustesDefaults.WidgetTotp.ESMERILADO_INTENSIDAD,
    val widgetTotpEsmeriladoLuz: Float = AjustesDefaults.WidgetTotp.ESMERILADO_LUZ,
    val widget1x1VidrioEsmerilado: Boolean = AjustesDefaults.Widget1x1.VIDRIO_ESMERILADO,
    val widget1x1EsmeriladoIntensidad: Float = AjustesDefaults.Widget1x1.ESMERILADO_INTENSIDAD,
    val widget1x1EsmeriladoLuz: Float = AjustesDefaults.Widget1x1.ESMERILADO_LUZ,
    // Personalización de tipografía y textos
    val escalaTexto: Float = AjustesDefaults.Tipografia.ESCALA_TEXTO,
    val pesoTexto: String = AjustesDefaults.Tipografia.PESO_TEXTO,
    val cursivaTexto: Boolean = AjustesDefaults.Tipografia.CURSIVA_TEXTO,
    val espaciadoLetrasSp: Float = AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP,
    val interlineadoFactor: Float = AjustesDefaults.Tipografia.INTERLINEADO_FACTOR,
    val familiaFuente: String = AjustesDefaults.Tipografia.FAMILIA_FUENTE,
    /** Sincronización con Material You (Monet) en Android 12+ (API 31+). */
    val colorDinamicoSistema: Boolean = AjustesDefaults.Tema.COLOR_DINAMICO_SISTEMA,
    // Configuración Quick Settings Tile ("Generador Rápido")
    val tileModo: String = AjustesDefaults.Tile.MODO,
    val tileLongitud: Int = AjustesDefaults.Tile.LONGITUD,
    val tilePatron: String = AjustesDefaults.Tile.PATRON,
    val tileSimbolos: String = AjustesDefaults.Tile.SIMBOLOS,
    val tileDicewarePalabras: Int = AjustesDefaults.Tile.DICEWARE_PALABRAS,
    val tileDicewareSeparador: String = AjustesDefaults.Tile.DICEWARE_SEPARADOR,
    val tileCopiarPortapapeles: Boolean = AjustesDefaults.Tile.COPIAR_PORTAPAPELES,
    val tileMostrarToast: Boolean = AjustesDefaults.Tile.MOSTRAR_TOAST,
    val tileHaptica: Boolean = AjustesDefaults.Tile.HAPTICA,
    val tileHapticaIntensidad: Float = AjustesDefaults.Tile.HAPTICA_INTENSIDAD,
    // Colores semánticos de secciones funcionales
    val colorSeguridad: String = AjustesDefaults.ColoresSecciones.SEGURIDAD,
    val colorArgon2: String = AjustesDefaults.ColoresSecciones.ARGON2,
    val colorCamara: String = AjustesDefaults.ColoresSecciones.CAMARA,
    val color2FA: String = AjustesDefaults.ColoresSecciones.DOS_FA,
    val colorPasskeys: String = AjustesDefaults.ColoresSecciones.PASSKEYS,
    val colorGenerador: String = AjustesDefaults.ColoresSecciones.GENERADOR,
    val colorSalud: String = AjustesDefaults.ColoresSecciones.SALUD,
    val colorPapelera: String = AjustesDefaults.ColoresSecciones.PAPELERA,
    val colorExportacion: String = AjustesDefaults.ColoresSecciones.EXPORTACION,
    // Registro del archivo CSV importado de Google
    val csvGoogleRuta: String = AjustesDefaults.HistorialCopias.CSV_GOOGLE_RUTA,
    val csvGoogleUri: String = AjustesDefaults.HistorialCopias.CSV_GOOGLE_URI,
    val csvGoogleCuentas: Int = AjustesDefaults.HistorialCopias.CSV_GOOGLE_CUENTAS,
    val csvGoogleEliminado: Boolean = AjustesDefaults.HistorialCopias.CSV_GOOGLE_ELIMINADO,
    // Índice Alfabético Lateral (Fast-scroller con ola estilo Niagara)
    val mostrarIndiceAlfabetico: Boolean = AjustesDefaults.Indice.MOSTRAR,
    val indiceEfectoOla: Boolean = AjustesDefaults.Indice.EFECTO_OLA,
    val indiceAmplitudOlaDp: Float = AjustesDefaults.Indice.AMPLITUD_OLA_DP,
    val indiceRadioOlaDp: Float = AjustesDefaults.Indice.RADIO_OLA_DP,
    val indiceEscalaLetras: Float = AjustesDefaults.Indice.ESCALA_LETRAS,
    val indiceMostrarCirculo: Boolean = AjustesDefaults.Indice.MOSTRAR_CIRCULO,
    val indiceTamanoCirculoDp: Float = AjustesDefaults.Indice.TAMANO_CIRCULO_DP,
    val indiceOffsetCirculoDp: Float = AjustesDefaults.Indice.OFFSET_CIRCULO_DP,
    val indiceHaptica: Boolean = AjustesDefaults.Indice.HAPTICA,
    val indiceAnchoTactilDp: Float = AjustesDefaults.Indice.ANCHO_TACTIL_DP,
    val indiceTonoLetras: Float = AjustesDefaults.Indice.TONO_LETRAS,
    val indiceIncluirEnie: Boolean = AjustesDefaults.Indice.INCLUIR_ENIE,
    val indiceResaltarEntradas: Boolean = AjustesDefaults.Indice.RESALTAR_ENTRADAS,
    val indiceResaltarSoloPrimera: Boolean = AjustesDefaults.Indice.RESALTAR_SOLO_PRIMERA,
    val indiceAlinearConCresta: Boolean = AjustesDefaults.Indice.ALINEAR_CON_CRESTA,
    val formatoFecha: String = AjustesDefaults.ListaFormatos.FORMATO_FECHA,
    val formatoHora: String = AjustesDefaults.ListaFormatos.FORMATO_HORA,
    val formatoTelefono: String = AjustesDefaults.ListaFormatos.FORMATO_TELEFONO,
    val separadorDecimal: String = AjustesDefaults.ListaFormatos.SEPARADOR_DECIMAL,
    /** Perfil de derivación Argon2id: "estandar", "reforzado" o "ultraseguro". */
    val perfilArgon2: String = AjustesDefaults.Seguridad.PERFIL_ARGON2,
    /** FLAG_SECURE: protección anti-captura de pantalla y anti-recientes. Activa por defecto. */
    val proteccionPantalla: Boolean = AjustesDefaults.Seguridad.PROTECCION_PANTALLA,
    /** Umbral en días para advertir sobre contraseñas antiguas en Salud (0 = desactivado). */
    val umbralAntiguedadDias: Int = AjustesDefaults.Seguridad.UMBRAL_ANTIGUEDAD_DIAS,
    // Seguridad visual (Privacidad de pantalla)
    val seguridadVisualActiva: Boolean = AjustesDefaults.SeguridadVisual.ACTIVA,
    val estiloOcultamientoVisual: String = AjustesDefaults.SeguridadVisual.ESTILO,
    val tiempoAutoOcultarSegundos: Int = AjustesDefaults.SeguridadVisual.AUTO_OCULTAR_SEGUNDOS,
    val ocultarUsuario: Boolean = AjustesDefaults.SeguridadVisual.OCULTAR_USUARIO,
    val ocultarContrasena: Boolean = AjustesDefaults.SeguridadVisual.OCULTAR_CONTRASENA,
    val ocultarTotp: Boolean = AjustesDefaults.SeguridadVisual.OCULTAR_TOTP,
    val ocultarNotas: Boolean = AjustesDefaults.SeguridadVisual.OCULTAR_NOTAS,
    val ocultarCampos: Boolean = AjustesDefaults.SeguridadVisual.OCULTAR_CAMPOS,
    // Historial temporal de contraseñas generadas
    val historialClavesMax: Int = AjustesDefaults.HistorialCopias.HISTORIAL_MAX,
    val historialClavesVaciadoAuto: Boolean = AjustesDefaults.HistorialCopias.HISTORIAL_VACIADO_AUTO,
    val historialClavesTiempoAutoDestruccion: Long = AjustesDefaults.HistorialCopias.HISTORIAL_TIEMPO_AUTO_DESTRUCCION_MS,
    val historialClaves: List<RegistroClaveGenerada> = emptyList(),
    // Copia de seguridad automática local rotativa
    val backupAutoFrecuenciaDias: Int = AjustesDefaults.HistorialCopias.BACKUP_AUTO_FRECUENCIA_DIAS,
    val backupAutoPasswordCifrado: String = AjustesDefaults.HistorialCopias.BACKUP_AUTO_PASSWORD_CIFRADO,
    val backupAutoUltimaEjecucion: Long = AjustesDefaults.HistorialCopias.BACKUP_AUTO_ULTIMA_EJECUCION,
    val backupAutoMaxCopias: Int = AjustesDefaults.HistorialCopias.BACKUP_AUTO_MAX_COPIAS,
    val backupAutoPatronNombre: String = AjustesDefaults.HistorialCopias.BACKUP_AUTO_PATRON_NOMBRE,
    val backupAutoSecuencia: Int = AjustesDefaults.HistorialCopias.BACKUP_AUTO_SECUENCIA,
    val mostrarIdsAjustes: Boolean = AjustesDefaults.Interaccion.MOSTRAR_IDS_AJUSTES,
    /** Animación de pantalla bloqueada: "engranajes" (mecanismo relojero) o "puerta" (anillos concéntricos). */
    val animacionDesbloqueo: String = AjustesDefaults.Animacion.TIPO_DESBLOQUEO,
    // Configuración visual y física de los engranajes
    val engranajesVelocidad: Float = AjustesDefaults.Animacion.Engranajes.VELOCIDAD,
    val engranajesGrosorBorde: Float = AjustesDefaults.Animacion.Engranajes.GROSOR_BORDE,
    val engranajesAlturaDientes: Float = AjustesDefaults.Animacion.Engranajes.ALTURA_DIENTES,
    val engranajesAnchoDientes: Float = AjustesDefaults.Animacion.Engranajes.ANCHO_DIENTES,
    val engranajesGrosorRadios: Float = AjustesDefaults.Animacion.Engranajes.GROSOR_RADIOS,
    val engranajesCurvaturaRadios: Float = AjustesDefaults.Animacion.Engranajes.CURVATURA_RADIOS,
    val engranajesCantidadRadios: Int = AjustesDefaults.Animacion.Engranajes.CANTIDAD_RADIOS,
    val engranajesRadioInterior: Float = AjustesDefaults.Animacion.Engranajes.RADIO_INTERIOR,
    val engranajesTamanoEje: Float = AjustesDefaults.Animacion.Engranajes.TAMANO_EJE,
    val engranajesSombraIntensidad: Float = AjustesDefaults.Animacion.Engranajes.SOMBRA_INTENSIDAD,
    val engranajesColorBrillo: String = AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
    val engranajesColorPrincipal: String = AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
    val engranajesColorSombraMedio: String = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
    val engranajesColorSombraOscuro: String = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
    val engranajesColorBisel: String = AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
    val engranajesColorInterior: String = AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
    val engranajesColorCubo: String = AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
    val engranajesColorEje: String = AjustesDefaults.Animacion.Engranajes.COLOR_EJE,
    // Configuración visual y física de la puerta de bóveda
    val puertaVelocidad: Float = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
    val puertaGrosorAnillos: Float = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS,
    val puertaColor: String = AjustesDefaults.Animacion.Puerta.COLOR,
    // Resaltado / Alumbrado visual de filas y grupos en navegación de ajustes
    val alumbradoActivo: Boolean = AjustesDefaults.Interaccion.ALUMBRADO_ACTIVO,
    val alumbradoIntensidad: Float = AjustesDefaults.Interaccion.ALUMBRADO_INTENSIDAD,
    val alumbradoRepeticiones: Int = AjustesDefaults.Interaccion.ALUMBRADO_REPETICIONES,
    val alumbradoDuracionMs: Int = AjustesDefaults.Interaccion.ALUMBRADO_DURACION_MS,
    // Vibración háptica global de la app
    val hapticaApp: Boolean = AjustesDefaults.Interaccion.HAPTICA_APP,
    val hapticaAppIntensidad: Float = AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD,
    // Indicadores superiores de contenido en tarjetas de la lista
    val mostrarIndicadoresContenido: Boolean = AjustesDefaults.ColoresDatos.MOSTRAR_INDICADORES,
    // Colores aislados exclusivos para datos e indicadores de tarjetas
    val colorDatosUsuario: String = AjustesDefaults.ColoresDatos.USUARIO,
    val colorDatosContrasena: String = AjustesDefaults.ColoresDatos.CONTRASENA,
    val colorDatos2FA: String = AjustesDefaults.ColoresDatos.DOS_FA,
    val colorDatosPasskey: String = AjustesDefaults.ColoresDatos.PASSKEY,
    val colorDatosWeb: String = AjustesDefaults.ColoresDatos.WEB,
    val colorDatosApp: String = AjustesDefaults.ColoresDatos.APP,
    // Colores por bloque de identificadores de ajustes
    val colorIdSeguridad: String = AjustesDefaults.ColoresIds.SEGURIDAD,
    val colorIdApariencia: String = AjustesDefaults.ColoresIds.APARIENCIA,
    val colorIdLista: String = AjustesDefaults.ColoresIds.LISTA,
    val colorIdHerramientas: String = AjustesDefaults.ColoresIds.HERRAMIENTAS,
    val colorIdCopias: String = AjustesDefaults.ColoresIds.COPIAS,
    val colorIdSistema: String = AjustesDefaults.ColoresIds.SISTEMA,
    // Autocompletado de Android e Inline Suggestions
    val autofillSugerenciasTeclado: Boolean = AjustesDefaults.Autocompletado.SUGERENCIAS_TECLADO,
    // Normalización de títulos y redes locales
    val prefijosSubdominios: List<String> = AjustesDefaults.NormalizacionTitulos.PREFIJOS_SUBDOMINIOS,
    val plantillaRouterIp: String = AjustesDefaults.NormalizacionTitulos.PLANTILLA_ROUTER_IP,
    val plantillaServidorIp: String = AjustesDefaults.NormalizacionTitulos.PLANTILLA_SERVIDOR_IP,
    val formatoColisionTitulos: String = AjustesDefaults.NormalizacionTitulos.FORMATO_COLISION_TITULOS,
    val respetarTitulosPersonalizados: Boolean = AjustesDefaults.NormalizacionTitulos.RESPETAR_TITULOS_PERSONALIZADOS
) {
    val modoVisualizacionIdentidades: ModoVisualizacionIdentidades
        get() = ModoVisualizacionIdentidades.desde(modoIdentidades)

    val jerarquiaOrganizacionEfectiva: JerarquiaOrganizacion
        get() = JerarquiaOrganizacion.desdeClave(jerarquiaOrganizacion)
}


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
        val biometriaActiva = prefs.getBoolean("biometria", AjustesDefaults.Seguridad.BIOMETRIA_ACTIVA)
        var modo = prefs.getString("biometria_modo", AjustesDefaults.Seguridad.BIOMETRIA_MODO) ?: AjustesDefaults.Seguridad.BIOMETRIA_MODO
        // Quien activó la huella antes de existir los modos la tenía en el fuerte, el único que había.
        if (biometriaActiva && modo.isEmpty()) modo = "fuerte"
        val rawAutoBloqueo = prefs.getInt("auto_bloqueo", AjustesDefaults.Seguridad.AUTO_BLOQUEO_SEGUNDOS)
        val autoBloqueo = if (rawAutoBloqueo < 5) AjustesDefaults.Seguridad.AUTO_BLOQUEO_SEGUNDOS else rawAutoBloqueo

        return AjustesApp(
            autoBloqueoSegundos = autoBloqueo,
            portapapelesSegundos = prefs.getInt("portapapeles", AjustesDefaults.Seguridad.PORTAPAPELES_SEGUNDOS),
            modoGrabacion = false,
            biometriaActiva = biometriaActiva,
            biometriaModo = modo,
            motorCamara = prefs.getString("motor_camara", AjustesDefaults.Seguridad.MOTOR_CAMARA) ?: AjustesDefaults.Seguridad.MOTOR_CAMARA,
            nombrePersonalizado = prefs.getString("nombre_personalizado", AjustesDefaults.Tema.NOMBRE_PERSONALIZADO) ?: AjustesDefaults.Tema.NOMBRE_PERSONALIZADO,
            iconoLauncher = run {
                val prefIcono = prefs.getString("icono_launcher", null)
                if (prefIcono.isNullOrBlank() || prefIcono.equals("ambar", ignoreCase = true)) {
                    if (prefIcono?.equals("ambar", ignoreCase = true) == true) {
                        prefs.edit().putString("icono_launcher", AjustesDefaults.Tema.ICONO_LAUNCHER).apply()
                    }
                    AjustesDefaults.Tema.ICONO_LAUNCHER
                } else {
                    prefIcono
                }
            },
            colorAcento = run {
                val prefAcento = prefs.getString("color_acento", AjustesDefaults.Tema.COLOR_ACENTO) ?: AjustesDefaults.Tema.COLOR_ACENTO
                if (prefAcento.equals("ambar", ignoreCase = true)) {
                    prefs.edit().putString("color_acento", AjustesDefaults.Tema.COLOR_ACENTO).apply()
                    AjustesDefaults.Tema.COLOR_ACENTO
                } else {
                    prefAcento
                }
            },
            colorIconosInternos = prefs.getString("color_iconos_internos", AjustesDefaults.Tema.COLOR_ICONOS_INTERNOS) ?: AjustesDefaults.Tema.COLOR_ICONOS_INTERNOS,
            colorTitulos = prefs.getString("color_titulos", AjustesDefaults.Tema.COLOR_TITULOS) ?: AjustesDefaults.Tema.COLOR_TITULOS,
            colorTarjetas = prefs.getString("color_tarjetas", AjustesDefaults.Tema.COLOR_TARJETAS) ?: AjustesDefaults.Tema.COLOR_TARJETAS,
            temaApp = prefs.getString("tema_app", AjustesDefaults.Tema.TEMA_APP) ?: AjustesDefaults.Tema.TEMA_APP,
            ultimaExportacionEn = prefs.getLong("ultima_exportacion", 0L),
            recordatorioExportacionDias = prefs.getInt("recordatorio_exportacion_dias", AjustesDefaults.HistorialCopias.RECORDATORIO_EXPORTACION_DIAS),
            densidadLista = prefs.getString("densidad_lista", AjustesDefaults.ListaFormatos.DENSIDAD_LISTA) ?: AjustesDefaults.ListaFormatos.DENSIDAD_LISTA,
            agruparPorSitio = if (!prefs.contains("agrupar_por_sitio_migrado_default")) {
                prefs.edit().putBoolean("agrupar_por_sitio_migrado_default", true).putBoolean("agrupar_por_sitio", true).apply()
                true
            } else {
                prefs.getBoolean("agrupar_por_sitio", AjustesDefaults.ListaFormatos.AGRUPAR_POR_SITIO)
            },
            modoIdentidades = prefs.getString("modo_identidades", AjustesDefaults.ListaFormatos.MODO_IDENTIDADES) ?: AjustesDefaults.ListaFormatos.MODO_IDENTIDADES,
            jerarquiaOrganizacion = prefs.getString("jerarquia_organizacion", AjustesDefaults.ListaFormatos.JERARQUIA_ORGANIZACION) ?: AjustesDefaults.ListaFormatos.JERARQUIA_ORGANIZACION,
            totpManualDigitos = prefs.getInt("totp_manual_digitos", AjustesDefaults.TotpManual.DIGITOS),
            totpManualPeriodo = prefs.getInt("totp_manual_periodo", AjustesDefaults.TotpManual.PERIODO),
            totpManualAlgoritmo = prefs.getString("totp_manual_algoritmo", AjustesDefaults.TotpManual.ALGORITMO) ?: AjustesDefaults.TotpManual.ALGORITMO,
            totpSepararDigitos = prefs.getBoolean("totp_separar_digitos", AjustesDefaults.TotpManual.SEPARAR_DIGITOS),
            curvaturaEsquinasDp = prefs.getFloat("curvatura_esquinas_dp", AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP),
            grosorBordeDp = prefs.getFloat("grosor_borde_dp", AjustesDefaults.Formas.GROSOR_BORDE_DP),
            estiloBorde = prefs.getString("estilo_borde", AjustesDefaults.Formas.ESTILO_BORDE) ?: AjustesDefaults.Formas.ESTILO_BORDE,
            espaciadoComponentesDp = prefs.getFloat("espaciado_componentes_dp", AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP),
            widgetGrosorBordeDp = prefs.getFloat("widget_grosor_borde_dp", AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP),
            widgetCurvaturaEsquinasDp = prefs.getFloat("widget_curvatura_esquinas_dp", AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP),
            widgetTransparenciaFondo = prefs.getFloat("widget_transparencia_fondo", AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO),
            widgetColorBorde = prefs.getString("widget_color_borde", AjustesDefaults.WidgetTotp.COLOR_BORDE) ?: AjustesDefaults.WidgetTotp.COLOR_BORDE,
            widgetColorContador = prefs.getString("widget_color_contador", AjustesDefaults.WidgetTotp.COLOR_CONTADOR) ?: AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
            widgetColorCodigo = prefs.getString("widget_color_codigo", AjustesDefaults.WidgetTotp.COLOR_CODIGO) ?: AjustesDefaults.WidgetTotp.COLOR_CODIGO,
            widgetColorTituloIcono = prefs.getString("widget_color_titulo_icono", AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO) ?: AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
            widgetHaptica = prefs.getBoolean("widget_haptica", AjustesDefaults.WidgetTotp.HAPTICA),
            widgetHapticaIntensidad = prefs.getFloat("widget_haptica_intensidad", AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD),
            widget1x1Haptica = prefs.getBoolean("widget_1x1_haptica", AjustesDefaults.Widget1x1.HAPTICA),
            widget1x1HapticaIntensidad = prefs.getFloat("widget_1x1_haptica_intensidad", AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD),
            widget1x1Modo = prefs.getString("widget_1x1_modo", AjustesDefaults.Widget1x1.MODO) ?: AjustesDefaults.Widget1x1.MODO,
            widget1x1Longitud = prefs.getInt("widget_1x1_longitud", AjustesDefaults.Widget1x1.LONGITUD),
            widget1x1Patron = prefs.getString("widget_1x1_patron", AjustesDefaults.Widget1x1.PATRON) ?: AjustesDefaults.Widget1x1.PATRON,
            widget1x1Simbolos = prefs.getString("widget_1x1_simbolos", AjustesDefaults.Widget1x1.SIMBOLOS) ?: AjustesDefaults.Widget1x1.SIMBOLOS,
            widget1x1CopiarPortapapeles = prefs.getBoolean("widget_1x1_copiar_portapapeles", AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES),
            widget1x1MostrarToast = prefs.getBoolean("widget_1x1_mostrar_toast", AjustesDefaults.Widget1x1.MOSTRAR_TOAST),
            widget1x1GrosorBordeDp = prefs.getFloat("widget_1x1_grosor_borde_dp", AjustesDefaults.Widget1x1.GROSOR_BORDE_DP),
            widget1x1CurvaturaEsquinasDp = prefs.getFloat("widget_1x1_curvatura_esquinas_dp", AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP),
            widget1x1TransparenciaFondo = prefs.getFloat("widget_1x1_transparencia_fondo", AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO),
            widget1x1TamanoDp = prefs.getFloat("widget_1x1_ancho_dp", prefs.getFloat("widget_1x1_tamano_dp", AjustesDefaults.Widget1x1.TAMANO_DP)),
            widget1x1AnchoDp = prefs.getFloat("widget_1x1_ancho_dp", prefs.getFloat("widget_1x1_tamano_dp", AjustesDefaults.Widget1x1.ANCHO_DP)),
            widget1x1AltoDp = prefs.getFloat("widget_1x1_alto_dp", prefs.getFloat("widget_1x1_tamano_dp", AjustesDefaults.Widget1x1.ALTO_DP)),
            widget1x1BloquearProporcion = prefs.getBoolean("widget_1x1_bloquear_proporcion", AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION),
            widget1x1OffsetX = prefs.getFloat("widget_1x1_offset_x", AjustesDefaults.Widget1x1.OFFSET_X),
            widget1x1OffsetY = prefs.getFloat("widget_1x1_offset_y", AjustesDefaults.Widget1x1.OFFSET_Y),
            widget1x1Alineamiento = prefs.getString("widget_1x1_alineamiento", AjustesDefaults.Widget1x1.ALINEAMIENTO) ?: AjustesDefaults.Widget1x1.ALINEAMIENTO,
            widget1x1ColorBorde = prefs.getString("widget_1x1_color_borde", AjustesDefaults.Widget1x1.COLOR_BORDE) ?: AjustesDefaults.Widget1x1.COLOR_BORDE,
            widget1x1ColorIcono = prefs.getString("widget_1x1_color_icono", AjustesDefaults.Widget1x1.COLOR_ICONO) ?: AjustesDefaults.Widget1x1.COLOR_ICONO,
            widget1x1ColorFondo = prefs.getString("widget_1x1_color_fondo", AjustesDefaults.Widget1x1.COLOR_FONDO) ?: AjustesDefaults.Widget1x1.COLOR_FONDO,
            widget1x1DicewarePalabras = prefs.getInt("widget_1x1_diceware_palabras", AjustesDefaults.Widget1x1.DICEWARE_PALABRAS),
            widget1x1DicewareSeparador = prefs.getString("widget_1x1_diceware_separador", AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR) ?: AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR,
            widgetColorFilas = prefs.getString("widget_color_filas", AjustesDefaults.WidgetTotp.COLOR_FILAS) ?: AjustesDefaults.WidgetTotp.COLOR_FILAS,
            widgetTransparenciaFilas = prefs.getFloat("widget_transparencia_filas", AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS),
            widgetTotpVidrioEsmerilado = prefs.getBoolean("widget_totp_vidrio_esmerilado", AjustesDefaults.WidgetTotp.VIDRIO_ESMERILADO),
            widgetTotpEsmeriladoIntensidad = prefs.getFloat("widget_totp_esmerilado_intensidad", AjustesDefaults.WidgetTotp.ESMERILADO_INTENSIDAD),
            widgetTotpEsmeriladoLuz = prefs.getFloat("widget_totp_esmerilado_luz", AjustesDefaults.WidgetTotp.ESMERILADO_LUZ),
            widget1x1VidrioEsmerilado = prefs.getBoolean("widget_1x1_vidrio_esmerilado", AjustesDefaults.Widget1x1.VIDRIO_ESMERILADO),
            widget1x1EsmeriladoIntensidad = prefs.getFloat("widget_1x1_esmerilado_intensidad", AjustesDefaults.Widget1x1.ESMERILADO_INTENSIDAD),
            widget1x1EsmeriladoLuz = prefs.getFloat("widget_1x1_esmerilado_luz", AjustesDefaults.Widget1x1.ESMERILADO_LUZ),
            tileSimbolos = prefs.getString("tile_simbolos", AjustesDefaults.Tile.SIMBOLOS) ?: AjustesDefaults.Tile.SIMBOLOS,
            tileDicewarePalabras = prefs.getInt("tile_diceware_palabras", AjustesDefaults.Tile.DICEWARE_PALABRAS),
            tileDicewareSeparador = prefs.getString("tile_diceware_separador", AjustesDefaults.Tile.DICEWARE_SEPARADOR) ?: AjustesDefaults.Tile.DICEWARE_SEPARADOR,
            escalaTexto = prefs.getFloat("escala_texto", AjustesDefaults.Tipografia.ESCALA_TEXTO),
            pesoTexto = prefs.getString("peso_texto", AjustesDefaults.Tipografia.PESO_TEXTO) ?: AjustesDefaults.Tipografia.PESO_TEXTO,
            cursivaTexto = prefs.getBoolean("cursiva_texto", AjustesDefaults.Tipografia.CURSIVA_TEXTO),
            espaciadoLetrasSp = prefs.getFloat("espaciado_letras_sp", AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP),
            interlineadoFactor = prefs.getFloat("interlineado_factor", AjustesDefaults.Tipografia.INTERLINEADO_FACTOR),
            familiaFuente = prefs.getString("familia_fuente", AjustesDefaults.Tipografia.FAMILIA_FUENTE) ?: AjustesDefaults.Tipografia.FAMILIA_FUENTE,
            colorDinamicoSistema = prefs.getBoolean("color_dinamico_sistema", AjustesDefaults.Tema.COLOR_DINAMICO_SISTEMA),
            tileModo = prefs.getString("tile_modo", AjustesDefaults.Tile.MODO) ?: AjustesDefaults.Tile.MODO,
            tileLongitud = prefs.getInt("tile_longitud", AjustesDefaults.Tile.LONGITUD),
            tilePatron = prefs.getString("tile_patron", AjustesDefaults.Tile.PATRON) ?: AjustesDefaults.Tile.PATRON,
            tileCopiarPortapapeles = prefs.getBoolean("tile_copiar", AjustesDefaults.Tile.COPIAR_PORTAPAPELES),
            tileMostrarToast = prefs.getBoolean("tile_toast", AjustesDefaults.Tile.MOSTRAR_TOAST),
            tileHaptica = prefs.getBoolean("tile_haptica", AjustesDefaults.Tile.HAPTICA),
            tileHapticaIntensidad = prefs.getFloat("tile_haptica_intensidad", AjustesDefaults.Tile.HAPTICA_INTENSIDAD),
            colorSeguridad = prefs.getString("color_seguridad", AjustesDefaults.ColoresSecciones.SEGURIDAD) ?: AjustesDefaults.ColoresSecciones.SEGURIDAD,
            colorArgon2 = prefs.getString("color_argon2", AjustesDefaults.ColoresSecciones.ARGON2) ?: AjustesDefaults.ColoresSecciones.ARGON2,
            colorCamara = prefs.getString("color_camara", AjustesDefaults.ColoresSecciones.CAMARA) ?: AjustesDefaults.ColoresSecciones.CAMARA,
            color2FA = prefs.getString("color_2fa", AjustesDefaults.ColoresSecciones.DOS_FA) ?: AjustesDefaults.ColoresSecciones.DOS_FA,
            colorPasskeys = prefs.getString("color_passkeys", AjustesDefaults.ColoresSecciones.PASSKEYS) ?: AjustesDefaults.ColoresSecciones.PASSKEYS,
            colorGenerador = prefs.getString("color_generador", AjustesDefaults.ColoresSecciones.GENERADOR) ?: AjustesDefaults.ColoresSecciones.GENERADOR,
            colorSalud = prefs.getString("color_salud", AjustesDefaults.ColoresSecciones.SALUD) ?: AjustesDefaults.ColoresSecciones.SALUD,
            colorPapelera = prefs.getString("color_papelera", AjustesDefaults.ColoresSecciones.PAPELERA) ?: AjustesDefaults.ColoresSecciones.PAPELERA,
            colorExportacion = prefs.getString("color_exportacion", AjustesDefaults.ColoresSecciones.EXPORTACION) ?: AjustesDefaults.ColoresSecciones.EXPORTACION,
            csvGoogleRuta = prefs.getString("csv_google_ruta", AjustesDefaults.HistorialCopias.CSV_GOOGLE_RUTA) ?: AjustesDefaults.HistorialCopias.CSV_GOOGLE_RUTA,
            csvGoogleUri = prefs.getString("csv_google_uri", AjustesDefaults.HistorialCopias.CSV_GOOGLE_URI) ?: AjustesDefaults.HistorialCopias.CSV_GOOGLE_URI,
            csvGoogleCuentas = prefs.getInt("csv_google_cuentas", AjustesDefaults.HistorialCopias.CSV_GOOGLE_CUENTAS),
            csvGoogleEliminado = prefs.getBoolean("csv_google_eliminado", AjustesDefaults.HistorialCopias.CSV_GOOGLE_ELIMINADO),
            mostrarIndiceAlfabetico = prefs.getBoolean("mostrar_indice_alfabetico", AjustesDefaults.Indice.MOSTRAR),
            indiceEfectoOla = prefs.getBoolean("indice_efecto_ola", AjustesDefaults.Indice.EFECTO_OLA),
            indiceAmplitudOlaDp = prefs.getFloat("indice_amplitud_ola_dp", AjustesDefaults.Indice.AMPLITUD_OLA_DP),
            indiceRadioOlaDp = prefs.getFloat("indice_radio_ola_dp", AjustesDefaults.Indice.RADIO_OLA_DP).let { if (it < 40f) AjustesDefaults.Indice.RADIO_OLA_DP else it },
            indiceEscalaLetras = prefs.getFloat("indice_escala_letras", AjustesDefaults.Indice.ESCALA_LETRAS),
            indiceMostrarCirculo = prefs.getBoolean("indice_mostrar_circulo", AjustesDefaults.Indice.MOSTRAR_CIRCULO),
            indiceTamanoCirculoDp = prefs.getFloat("indice_tamano_circulo_dp", AjustesDefaults.Indice.TAMANO_CIRCULO_DP),
            indiceOffsetCirculoDp = prefs.getFloat("indice_offset_circulo_dp", AjustesDefaults.Indice.OFFSET_CIRCULO_DP).let { if (it < 30f) AjustesDefaults.Indice.OFFSET_CIRCULO_DP else it },
            indiceHaptica = prefs.getBoolean("indice_haptica", AjustesDefaults.Indice.HAPTICA),
            indiceAnchoTactilDp = prefs.getFloat("indice_ancho_tactil_dp", AjustesDefaults.Indice.ANCHO_TACTIL_DP),
            indiceTonoLetras = prefs.getFloat("indice_tono_letras", AjustesDefaults.Indice.TONO_LETRAS).let { if (it <= 1.0f) AjustesDefaults.Indice.TONO_LETRAS else it },
            indiceIncluirEnie = prefs.getBoolean("indice_incluir_enie", AjustesDefaults.Indice.INCLUIR_ENIE),
            indiceResaltarEntradas = prefs.getBoolean("indice_resaltar_entradas", AjustesDefaults.Indice.RESALTAR_ENTRADAS),
            indiceResaltarSoloPrimera = prefs.getBoolean("indice_resaltar_solo_primera", AjustesDefaults.Indice.RESALTAR_SOLO_PRIMERA),
            indiceAlinearConCresta = prefs.getBoolean("indice_alinear_con_cresta", AjustesDefaults.Indice.ALINEAR_CON_CRESTA),
            formatoFecha = prefs.getString("formato_fecha", AjustesDefaults.ListaFormatos.FORMATO_FECHA) ?: AjustesDefaults.ListaFormatos.FORMATO_FECHA,
            formatoHora = prefs.getString("formato_hora", AjustesDefaults.ListaFormatos.FORMATO_HORA) ?: AjustesDefaults.ListaFormatos.FORMATO_HORA,
            formatoTelefono = prefs.getString("formato_telefono", AjustesDefaults.ListaFormatos.FORMATO_TELEFONO) ?: AjustesDefaults.ListaFormatos.FORMATO_TELEFONO,
            separadorDecimal = prefs.getString("separador_decimal", AjustesDefaults.ListaFormatos.SEPARADOR_DECIMAL) ?: AjustesDefaults.ListaFormatos.SEPARADOR_DECIMAL,
            perfilArgon2 = prefs.getString("perfil_argon2", AjustesDefaults.Seguridad.PERFIL_ARGON2) ?: AjustesDefaults.Seguridad.PERFIL_ARGON2,
            proteccionPantalla = prefs.getBoolean("proteccion_pantalla", AjustesDefaults.Seguridad.PROTECCION_PANTALLA),
            umbralAntiguedadDias = prefs.getInt("umbral_antiguedad_dias", AjustesDefaults.Seguridad.UMBRAL_ANTIGUEDAD_DIAS),
            seguridadVisualActiva = prefs.getBoolean("seguridad_visual_activa", AjustesDefaults.SeguridadVisual.ACTIVA),
            estiloOcultamientoVisual = prefs.getString("estilo_ocultamiento_visual", AjustesDefaults.SeguridadVisual.ESTILO) ?: AjustesDefaults.SeguridadVisual.ESTILO,
            tiempoAutoOcultarSegundos = prefs.getInt("tiempo_auto_ocultar_segundos", AjustesDefaults.SeguridadVisual.AUTO_OCULTAR_SEGUNDOS),
            ocultarUsuario = prefs.getBoolean("ocultar_usuario", AjustesDefaults.SeguridadVisual.OCULTAR_USUARIO),
            ocultarContrasena = prefs.getBoolean("ocultar_contrasena", AjustesDefaults.SeguridadVisual.OCULTAR_CONTRASENA),
            ocultarTotp = prefs.getBoolean("ocultar_totp", AjustesDefaults.SeguridadVisual.OCULTAR_TOTP),
            ocultarNotas = prefs.getBoolean("ocultar_notas", AjustesDefaults.SeguridadVisual.OCULTAR_NOTAS),
            ocultarCampos = prefs.getBoolean("ocultar_campos", AjustesDefaults.SeguridadVisual.OCULTAR_CAMPOS),
            historialClavesMax = prefs.getInt("historial_claves_max", AjustesDefaults.HistorialCopias.HISTORIAL_MAX),
            historialClavesVaciadoAuto = prefs.getBoolean("historial_claves_vaciado_auto", AjustesDefaults.HistorialCopias.HISTORIAL_VACIADO_AUTO),
            historialClavesTiempoAutoDestruccion = prefs.getLong("historial_claves_tiempo_autodestruccion", AjustesDefaults.HistorialCopias.HISTORIAL_TIEMPO_AUTO_DESTRUCCION_MS),
            historialClaves = deserializarHistorial(prefs.getString("historial_claves_json", "") ?: ""),
            backupAutoFrecuenciaDias = prefs.getInt("backup_auto_frecuencia_dias", AjustesDefaults.HistorialCopias.BACKUP_AUTO_FRECUENCIA_DIAS),
            backupAutoPasswordCifrado = prefs.getString("backup_auto_password", AjustesDefaults.HistorialCopias.BACKUP_AUTO_PASSWORD_CIFRADO) ?: AjustesDefaults.HistorialCopias.BACKUP_AUTO_PASSWORD_CIFRADO,
            backupAutoUltimaEjecucion = prefs.getLong("backup_auto_ultima_ejecucion", AjustesDefaults.HistorialCopias.BACKUP_AUTO_ULTIMA_EJECUCION),
            backupAutoMaxCopias = prefs.getInt("backup_auto_max_copias", AjustesDefaults.HistorialCopias.BACKUP_AUTO_MAX_COPIAS),
            backupAutoPatronNombre = run {
                val guardado = prefs.getString("backup_auto_patron_nombre", null)
                if (guardado == null || guardado == "boveda-auto-{FECHA}" || guardado == "{99}-boveda-auto-{FECHA}") {
                    AjustesDefaults.HistorialCopias.BACKUP_AUTO_PATRON_NOMBRE
                } else {
                    guardado
                }
            },
            backupAutoSecuencia = prefs.getInt("backup_auto_secuencia", AjustesDefaults.HistorialCopias.BACKUP_AUTO_SECUENCIA),
            mostrarIdsAjustes = prefs.getBoolean("mostrar_ids_ajustes", AjustesDefaults.Interaccion.MOSTRAR_IDS_AJUSTES),
            animacionDesbloqueo = prefs.getString("animacion_desbloqueo", AjustesDefaults.Animacion.TIPO_DESBLOQUEO) ?: AjustesDefaults.Animacion.TIPO_DESBLOQUEO,
            engranajesVelocidad = prefs.getFloat("engranajes_velocidad", AjustesDefaults.Animacion.Engranajes.VELOCIDAD),
            engranajesGrosorBorde = prefs.getFloat("engranajes_grosor_borde", AjustesDefaults.Animacion.Engranajes.GROSOR_BORDE),
            engranajesAlturaDientes = prefs.getFloat("engranajes_altura_dientes", AjustesDefaults.Animacion.Engranajes.ALTURA_DIENTES),
            engranajesAnchoDientes = prefs.getFloat("engranajes_ancho_dientes", AjustesDefaults.Animacion.Engranajes.ANCHO_DIENTES),
            engranajesGrosorRadios = prefs.getFloat("engranajes_grosor_radios", AjustesDefaults.Animacion.Engranajes.GROSOR_RADIOS),
            engranajesCurvaturaRadios = prefs.getFloat("engranajes_curvatura_radios", AjustesDefaults.Animacion.Engranajes.CURVATURA_RADIOS),
            engranajesCantidadRadios = prefs.getInt("engranajes_cantidad_radios", AjustesDefaults.Animacion.Engranajes.CANTIDAD_RADIOS),
            engranajesRadioInterior = prefs.getFloat("engranajes_radio_interior", AjustesDefaults.Animacion.Engranajes.RADIO_INTERIOR),
            engranajesTamanoEje = prefs.getFloat("engranajes_tamano_eje", AjustesDefaults.Animacion.Engranajes.TAMANO_EJE),
            engranajesSombraIntensidad = prefs.getFloat("engranajes_sombra_intensidad", AjustesDefaults.Animacion.Engranajes.SOMBRA_INTENSIDAD),
            engranajesColorBrillo = prefs.getString("engranajes_color_brillo", AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO) ?: AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
            engranajesColorPrincipal = prefs.getString("engranajes_color_principal", AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL) ?: AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
            engranajesColorSombraMedio = prefs.getString("engranajes_color_sombra_medio", AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO) ?: AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
            engranajesColorSombraOscuro = prefs.getString("engranajes_color_sombra_oscuro", AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO) ?: AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
            engranajesColorBisel = prefs.getString("engranajes_color_bisel", AjustesDefaults.Animacion.Engranajes.COLOR_BISEL) ?: AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
            engranajesColorInterior = prefs.getString("engranajes_color_interior", AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR) ?: AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
            engranajesColorCubo = prefs.getString("engranajes_color_cubo", AjustesDefaults.Animacion.Engranajes.COLOR_CUBO) ?: AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
            engranajesColorEje = prefs.getString("engranajes_color_eje", AjustesDefaults.Animacion.Engranajes.COLOR_EJE) ?: AjustesDefaults.Animacion.Engranajes.COLOR_EJE,
            puertaVelocidad = prefs.getFloat("puerta_velocidad", AjustesDefaults.Animacion.Puerta.VELOCIDAD),
            puertaGrosorAnillos = prefs.getFloat("puerta_grosor_anillos", AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS),
            puertaColor = prefs.getString("puerta_color", AjustesDefaults.Animacion.Puerta.COLOR) ?: AjustesDefaults.Animacion.Puerta.COLOR,
            alumbradoActivo = prefs.getBoolean("alumbrado_activo", AjustesDefaults.Interaccion.ALUMBRADO_ACTIVO),
            alumbradoIntensidad = prefs.getFloat("alumbrado_intensidad", AjustesDefaults.Interaccion.ALUMBRADO_INTENSIDAD),
            alumbradoRepeticiones = prefs.getInt("alumbrado_repeticiones", AjustesDefaults.Interaccion.ALUMBRADO_REPETICIONES),
            alumbradoDuracionMs = prefs.getInt("alumbrado_duracion_ms", AjustesDefaults.Interaccion.ALUMBRADO_DURACION_MS),
            hapticaApp = prefs.getBoolean("haptica_app", AjustesDefaults.Interaccion.HAPTICA_APP),
            hapticaAppIntensidad = prefs.getFloat("haptica_app_intensidad", AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD),
            mostrarIndicadoresContenido = prefs.getBoolean("mostrar_indicadores_contenido", AjustesDefaults.ColoresDatos.MOSTRAR_INDICADORES),
            colorDatosUsuario = prefs.getString("color_datos_usuario", AjustesDefaults.ColoresDatos.USUARIO) ?: AjustesDefaults.ColoresDatos.USUARIO,
            colorDatosContrasena = prefs.getString("color_datos_contrasena", AjustesDefaults.ColoresDatos.CONTRASENA) ?: AjustesDefaults.ColoresDatos.CONTRASENA,
            colorDatos2FA = prefs.getString("color_datos_2fa", AjustesDefaults.ColoresDatos.DOS_FA) ?: AjustesDefaults.ColoresDatos.DOS_FA,
            colorDatosPasskey = prefs.getString("color_datos_passkey", AjustesDefaults.ColoresDatos.PASSKEY) ?: AjustesDefaults.ColoresDatos.PASSKEY,
            colorDatosWeb = prefs.getString("color_datos_web", AjustesDefaults.ColoresDatos.WEB) ?: AjustesDefaults.ColoresDatos.WEB,
            colorDatosApp = prefs.getString("color_datos_app", AjustesDefaults.ColoresDatos.APP) ?: AjustesDefaults.ColoresDatos.APP,
            colorIdSeguridad = prefs.getString("color_id_seguridad", AjustesDefaults.ColoresIds.SEGURIDAD) ?: AjustesDefaults.ColoresIds.SEGURIDAD,
            colorIdApariencia = prefs.getString("color_id_apariencia", AjustesDefaults.ColoresIds.APARIENCIA) ?: AjustesDefaults.ColoresIds.APARIENCIA,
            colorIdLista = prefs.getString("color_id_lista", AjustesDefaults.ColoresIds.LISTA) ?: AjustesDefaults.ColoresIds.LISTA,
            colorIdHerramientas = prefs.getString("color_id_herramientas", AjustesDefaults.ColoresIds.HERRAMIENTAS) ?: AjustesDefaults.ColoresIds.HERRAMIENTAS,
            colorIdCopias = prefs.getString("color_id_copias", AjustesDefaults.ColoresIds.COPIAS) ?: AjustesDefaults.ColoresIds.COPIAS,
            colorIdSistema = prefs.getString("color_id_sistema", AjustesDefaults.ColoresIds.SISTEMA) ?: AjustesDefaults.ColoresIds.SISTEMA,
            autofillSugerenciasTeclado = prefs.getBoolean("autofill_sugerencias_teclado", AjustesDefaults.Autocompletado.SUGERENCIAS_TECLADO),
            prefijosSubdominios = deserializarPrefijosSubdominios(prefs.getString("prefijos_subdominios_json", "") ?: ""),
            plantillaRouterIp = prefs.getString("plantilla_router_ip", AjustesDefaults.NormalizacionTitulos.PLANTILLA_ROUTER_IP) ?: AjustesDefaults.NormalizacionTitulos.PLANTILLA_ROUTER_IP,
            plantillaServidorIp = prefs.getString("plantilla_servidor_ip", AjustesDefaults.NormalizacionTitulos.PLANTILLA_SERVIDOR_IP) ?: AjustesDefaults.NormalizacionTitulos.PLANTILLA_SERVIDOR_IP,
            formatoColisionTitulos = prefs.getString("formato_colision_titulos", AjustesDefaults.NormalizacionTitulos.FORMATO_COLISION_TITULOS) ?: AjustesDefaults.NormalizacionTitulos.FORMATO_COLISION_TITULOS,
            respetarTitulosPersonalizados = prefs.getBoolean("respetar_titulos_personalizados", AjustesDefaults.NormalizacionTitulos.RESPETAR_TITULOS_PERSONALIZADOS)
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
            .putString("modo_identidades", nuevo.modoIdentidades)
            .putString("jerarquia_organizacion", nuevo.jerarquiaOrganizacion)
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
            .putInt("widget_1x1_diceware_palabras", nuevo.widget1x1DicewarePalabras)
            .putString("widget_1x1_diceware_separador", nuevo.widget1x1DicewareSeparador)
            .putString("widget_color_filas", nuevo.widgetColorFilas)
            .putFloat("widget_transparencia_filas", nuevo.widgetTransparenciaFilas)
            .putBoolean("widget_totp_vidrio_esmerilado", nuevo.widgetTotpVidrioEsmerilado)
            .putFloat("widget_totp_esmerilado_intensidad", nuevo.widgetTotpEsmeriladoIntensidad)
            .putFloat("widget_totp_esmerilado_luz", nuevo.widgetTotpEsmeriladoLuz)
            .putBoolean("widget_1x1_vidrio_esmerilado", nuevo.widget1x1VidrioEsmerilado)
            .putFloat("widget_1x1_esmerilado_intensidad", nuevo.widget1x1EsmeriladoIntensidad)
            .putFloat("widget_1x1_esmerilado_luz", nuevo.widget1x1EsmeriladoLuz)
            .putString("tile_simbolos", nuevo.tileSimbolos)
            .putInt("tile_diceware_palabras", nuevo.tileDicewarePalabras)
            .putString("tile_diceware_separador", nuevo.tileDicewareSeparador)
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
            .putBoolean("indice_alinear_con_cresta", nuevo.indiceAlinearConCresta)
            .putString("formato_fecha", nuevo.formatoFecha)
            .putString("formato_hora", nuevo.formatoHora)
            .putString("formato_telefono", nuevo.formatoTelefono)
            .putString("separador_decimal", nuevo.separadorDecimal)
            .putString("perfil_argon2", nuevo.perfilArgon2)
            .putBoolean("proteccion_pantalla", nuevo.proteccionPantalla)
            .putInt("umbral_antiguedad_dias", nuevo.umbralAntiguedadDias)
            .putBoolean("seguridad_visual_activa", nuevo.seguridadVisualActiva)
            .putString("estilo_ocultamiento_visual", nuevo.estiloOcultamientoVisual)
            .putInt("tiempo_auto_ocultar_segundos", nuevo.tiempoAutoOcultarSegundos)
            .putBoolean("ocultar_usuario", nuevo.ocultarUsuario)
            .putBoolean("ocultar_contrasena", nuevo.ocultarContrasena)
            .putBoolean("ocultar_totp", nuevo.ocultarTotp)
            .putBoolean("ocultar_notas", nuevo.ocultarNotas)
            .putBoolean("ocultar_campos", nuevo.ocultarCampos)
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
            .putString("color_id_seguridad", nuevo.colorIdSeguridad)
            .putString("color_id_apariencia", nuevo.colorIdApariencia)
            .putString("color_id_lista", nuevo.colorIdLista)
            .putString("color_id_herramientas", nuevo.colorIdHerramientas)
            .putString("color_id_copias", nuevo.colorIdCopias)
            .putString("color_id_sistema", nuevo.colorIdSistema)
            .putBoolean("autofill_sugerencias_teclado", nuevo.autofillSugerenciasTeclado)
            .putString("prefijos_subdominios_json", jsonAjustes.encodeToString(nuevo.prefijosSubdominios))
            .putString("plantilla_router_ip", nuevo.plantillaRouterIp)
            .putString("plantilla_servidor_ip", nuevo.plantillaServidorIp)
            .putString("formato_colision_titulos", nuevo.formatoColisionTitulos)
            .putBoolean("respetar_titulos_personalizados", nuevo.respetarTitulosPersonalizados)
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
        val OPCIONES_UMBRAL_ANTIGUEDAD = listOf(
            0 to "Desactivado",
            90 to "3 meses",
            180 to "6 meses",
            270 to "9 meses",
            365 to "12 meses",
            455 to "15 meses",
            545 to "18 meses"
        )
        val OPCIONES_ESTILO_OCULTAMIENTO = listOf(
            "desenfoque" to "Desenfoque (Efecto cristal)",
            "puntos_fijos" to "Puntos de seguridad (Longitud fija)",
            "puntos_reales" to "Puntos tradicionales (Longitud real)"
        )
        val OPCIONES_TIEMPO_AUTO_OCULTAR = listOf(
            5 to "5 segundos",
            10 to "10 segundos (Recomendado)",
            15 to "15 segundos",
            30 to "30 segundos",
            0 to "Manual (hasta volver a tocar)"
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
            "ninguno" to "Sin borde",
            "sutil" to "Sutil",
            "acento" to "Acento",
            "marcado" to "Marcado"
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

