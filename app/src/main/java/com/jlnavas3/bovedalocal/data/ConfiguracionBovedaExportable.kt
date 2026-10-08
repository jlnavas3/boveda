package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/**
 * Modelo de datos que encapsula todas las configuraciones funcionales, reglas de URLs,
 * homelab, puertos, plantillas de campos, autocompletado y seguridad de la aplicación
 * que se incluyen en los respaldos y archivos de bóveda '.bvda'.
 *
 * Se excluyen estrictamente las preferencias de temas visuales (colores, paletas, tipografías,
 * formas de bordes, iconos del launcher y animaciones de reloj/puerta).
 */
@Serializable
data class ConfiguracionBovedaExportable(
    // 1. Reglas de URLs, Redes y Normalización de Títulos
    val prefijosSubdominios: List<String>? = null,
    val tldsDescartables: List<String>? = null,
    val marcasPersonalizadas: Map<String, String>? = null,
    val puertosServiciosLocales: Map<String, String>? = null,
    val octetosRouter: List<Int>? = null,
    val plantillaRouterIp: String? = null,
    val plantillaServidorIp: String? = null,
    val formatoColisionTitulos: String? = null,
    val respetarTitulosPersonalizados: Boolean? = null,

    // 2. Autocompletado y Detección
    val autofillSugerenciasTeclado: Boolean? = null,
    val autofillPistasUsuario: List<String>? = null,
    val autofillPistasContrasena: List<String>? = null,
    val autofillPistasOtp: List<String>? = null,
    val mapeoPaquetesPersonalizados: Map<String, String>? = null,
    val navegadoresPersonalizados: List<String>? = null,
    val maxSugerenciasAutofill: Int? = null,

    // 3. Plantillas de Campos Personalizadas
    val plantillasCamposPersonalizadas: List<PlantillaCamposPersonalizada>? = null,

    // 4. Seguridad Funcional (sin biometría atada a Keystore de hardware)
    val autoBloqueoSegundos: Int? = null,
    val portapapelesSegundos: Int? = null,
    val motorCamara: String? = null,
    val perfilArgon2: String? = null,
    val proteccionPantalla: Boolean? = null,
    val umbralAntiguedadDias: Int? = null,
    val frenoIntentosGratis: Int? = null,
    val frenoSegundosBase: Long? = null,
    val frenoSegundosMax: Long? = null,
    val autodestruccionIntentosFallidosMax: Int? = null,

    // 5. Privacidad y Seguridad Visual
    val seguridadVisualActiva: Boolean? = null,
    val estiloOcultamientoVisual: String? = null,
    val tiempoAutoOcultarSegundos: Int? = null,
    val ocultarUsuario: Boolean? = null,
    val ocultarContrasena: Boolean? = null,
    val ocultarTotp: Boolean? = null,
    val ocultarNotas: Boolean? = null,
    val ocultarCampos: Boolean? = null,

    // 6. Lista y Formatos
    val densidadLista: String? = null,
    val criterioOrdenacion: String? = null,
    val agruparPorSitio: Boolean? = null,
    val modoIdentidades: String? = null,
    val jerarquiaOrganizacion: String? = null,
    val formatoFecha: String? = null,
    val formatoHora: String? = null,
    val formatoTelefono: String? = null,
    val separadorDecimal: String? = null,

    // 7. Generador de Contraseñas y TOTP Manual
    val totpManualDigitos: Int? = null,
    val totpManualPeriodo: Int? = null,
    val totpManualAlgoritmo: String? = null,
    val totpSepararDigitos: Boolean? = null,
    val generadorExcluirAmbiguos: Boolean? = null,
    val generadorLongitudMax: Int? = null,
    val generadorIdiomaFrases: String? = null,
    val generadorCapitalizarFrases: Boolean? = null,

    // 8. Retención y Ciclo de Vida
    val diasRetencionPapelera: Int? = null,
    val maxHistorialContrasenasPorEntrada: Int? = null,
    val recordatorioExportacionDias: Int? = null,

    // 9. Reorganización de Ajustes
    val ordenAjustesPersonalizado: List<String>? = null
)

fun AjustesApp.aConfiguracionExportable(): ConfiguracionBovedaExportable {
    return ConfiguracionBovedaExportable(
        prefijosSubdominios = prefijosSubdominios,
        tldsDescartables = tldsDescartables,
        marcasPersonalizadas = marcasPersonalizadas,
        puertosServiciosLocales = puertosServiciosLocales,
        octetosRouter = octetosRouter,
        plantillaRouterIp = plantillaRouterIp,
        plantillaServidorIp = plantillaServidorIp,
        formatoColisionTitulos = formatoColisionTitulos,
        respetarTitulosPersonalizados = respetarTitulosPersonalizados,
        autofillSugerenciasTeclado = autofillSugerenciasTeclado,
        autofillPistasUsuario = autofillPistasUsuario,
        autofillPistasContrasena = autofillPistasContrasena,
        autofillPistasOtp = autofillPistasOtp,
        mapeoPaquetesPersonalizados = mapeoPaquetesPersonalizados,
        navegadoresPersonalizados = navegadoresPersonalizados,
        maxSugerenciasAutofill = maxSugerenciasAutofill,
        plantillasCamposPersonalizadas = plantillasCamposPersonalizadas,
        autoBloqueoSegundos = autoBloqueoSegundos,
        portapapelesSegundos = portapapelesSegundos,
        motorCamara = motorCamara,
        perfilArgon2 = perfilArgon2,
        proteccionPantalla = proteccionPantalla,
        umbralAntiguedadDias = umbralAntiguedadDias,
        frenoIntentosGratis = frenoIntentosGratis,
        frenoSegundosBase = frenoSegundosBase,
        frenoSegundosMax = frenoSegundosMax,
        autodestruccionIntentosFallidosMax = autodestruccionIntentosFallidosMax,
        seguridadVisualActiva = seguridadVisualActiva,
        estiloOcultamientoVisual = estiloOcultamientoVisual,
        tiempoAutoOcultarSegundos = tiempoAutoOcultarSegundos,
        ocultarUsuario = ocultarUsuario,
        ocultarContrasena = ocultarContrasena,
        ocultarTotp = ocultarTotp,
        ocultarNotas = ocultarNotas,
        ocultarCampos = ocultarCampos,
        densidadLista = densidadLista,
        criterioOrdenacion = criterioOrdenacion,
        agruparPorSitio = agruparPorSitio,
        modoIdentidades = modoIdentidades,
        jerarquiaOrganizacion = jerarquiaOrganizacion,
        formatoFecha = formatoFecha,
        formatoHora = formatoHora,
        formatoTelefono = formatoTelefono,
        separadorDecimal = separadorDecimal,
        totpManualDigitos = totpManualDigitos,
        totpManualPeriodo = totpManualPeriodo,
        totpManualAlgoritmo = totpManualAlgoritmo,
        totpSepararDigitos = totpSepararDigitos,
        generadorExcluirAmbiguos = generadorExcluirAmbiguos,
        generadorLongitudMax = generadorLongitudMax,
        generadorIdiomaFrases = generadorIdiomaFrases,
        generadorCapitalizarFrases = generadorCapitalizarFrases,
        diasRetencionPapelera = diasRetencionPapelera,
        maxHistorialContrasenasPorEntrada = maxHistorialContrasenasPorEntrada,
        recordatorioExportacionDias = recordatorioExportacionDias,
        ordenAjustesPersonalizado = ordenAjustesPersonalizado
    )
}

fun AjustesApp.aplicarConfiguracionExportable(config: ConfiguracionBovedaExportable): AjustesApp {
    return this.copy(
        prefijosSubdominios = config.prefijosSubdominios ?: this.prefijosSubdominios,
        tldsDescartables = config.tldsDescartables ?: this.tldsDescartables,
        marcasPersonalizadas = config.marcasPersonalizadas ?: this.marcasPersonalizadas,
        puertosServiciosLocales = config.puertosServiciosLocales ?: this.puertosServiciosLocales,
        octetosRouter = config.octetosRouter ?: this.octetosRouter,
        plantillaRouterIp = config.plantillaRouterIp ?: this.plantillaRouterIp,
        plantillaServidorIp = config.plantillaServidorIp ?: this.plantillaServidorIp,
        formatoColisionTitulos = config.formatoColisionTitulos ?: this.formatoColisionTitulos,
        respetarTitulosPersonalizados = config.respetarTitulosPersonalizados ?: this.respetarTitulosPersonalizados,
        autofillSugerenciasTeclado = config.autofillSugerenciasTeclado ?: this.autofillSugerenciasTeclado,
        autofillPistasUsuario = config.autofillPistasUsuario ?: this.autofillPistasUsuario,
        autofillPistasContrasena = config.autofillPistasContrasena ?: this.autofillPistasContrasena,
        autofillPistasOtp = config.autofillPistasOtp ?: this.autofillPistasOtp,
        mapeoPaquetesPersonalizados = config.mapeoPaquetesPersonalizados ?: this.mapeoPaquetesPersonalizados,
        navegadoresPersonalizados = config.navegadoresPersonalizados ?: this.navegadoresPersonalizados,
        maxSugerenciasAutofill = config.maxSugerenciasAutofill ?: this.maxSugerenciasAutofill,
        plantillasCamposPersonalizadas = if (config.plantillasCamposPersonalizadas != null) {
            val porId = this.plantillasCamposPersonalizadas.associateBy { it.id }.toMutableMap()
            config.plantillasCamposPersonalizadas.forEach { porId[it.id] = it }
            porId.values.toList()
        } else this.plantillasCamposPersonalizadas,
        autoBloqueoSegundos = config.autoBloqueoSegundos ?: this.autoBloqueoSegundos,
        portapapelesSegundos = config.portapapelesSegundos ?: this.portapapelesSegundos,
        motorCamara = config.motorCamara ?: this.motorCamara,
        perfilArgon2 = config.perfilArgon2 ?: this.perfilArgon2,
        proteccionPantalla = config.proteccionPantalla ?: this.proteccionPantalla,
        umbralAntiguedadDias = config.umbralAntiguedadDias ?: this.umbralAntiguedadDias,
        frenoIntentosGratis = config.frenoIntentosGratis ?: this.frenoIntentosGratis,
        frenoSegundosBase = config.frenoSegundosBase ?: this.frenoSegundosBase,
        frenoSegundosMax = config.frenoSegundosMax ?: this.frenoSegundosMax,
        autodestruccionIntentosFallidosMax = config.autodestruccionIntentosFallidosMax ?: this.autodestruccionIntentosFallidosMax,
        seguridadVisualActiva = config.seguridadVisualActiva ?: this.seguridadVisualActiva,
        estiloOcultamientoVisual = config.estiloOcultamientoVisual ?: this.estiloOcultamientoVisual,
        tiempoAutoOcultarSegundos = config.tiempoAutoOcultarSegundos ?: this.tiempoAutoOcultarSegundos,
        ocultarUsuario = config.ocultarUsuario ?: this.ocultarUsuario,
        ocultarContrasena = config.ocultarContrasena ?: this.ocultarContrasena,
        ocultarTotp = config.ocultarTotp ?: this.ocultarTotp,
        ocultarNotas = config.ocultarNotas ?: this.ocultarNotas,
        ocultarCampos = config.ocultarCampos ?: this.ocultarCampos,
        densidadLista = config.densidadLista ?: this.densidadLista,
        criterioOrdenacion = config.criterioOrdenacion ?: this.criterioOrdenacion,
        agruparPorSitio = config.agruparPorSitio ?: this.agruparPorSitio,
        modoIdentidades = config.modoIdentidades ?: this.modoIdentidades,
        jerarquiaOrganizacion = config.jerarquiaOrganizacion ?: this.jerarquiaOrganizacion,
        formatoFecha = config.formatoFecha ?: this.formatoFecha,
        formatoHora = config.formatoHora ?: this.formatoHora,
        formatoTelefono = config.formatoTelefono ?: this.formatoTelefono,
        separadorDecimal = config.separadorDecimal ?: this.separadorDecimal,
        totpManualDigitos = config.totpManualDigitos ?: this.totpManualDigitos,
        totpManualPeriodo = config.totpManualPeriodo ?: this.totpManualPeriodo,
        totpManualAlgoritmo = config.totpManualAlgoritmo ?: this.totpManualAlgoritmo,
        totpSepararDigitos = config.totpSepararDigitos ?: this.totpSepararDigitos,
        generadorExcluirAmbiguos = config.generadorExcluirAmbiguos ?: this.generadorExcluirAmbiguos,
        generadorLongitudMax = config.generadorLongitudMax ?: this.generadorLongitudMax,
        generadorIdiomaFrases = config.generadorIdiomaFrases ?: this.generadorIdiomaFrases,
        generadorCapitalizarFrases = config.generadorCapitalizarFrases ?: this.generadorCapitalizarFrases,
        diasRetencionPapelera = config.diasRetencionPapelera ?: this.diasRetencionPapelera,
        maxHistorialContrasenasPorEntrada = config.maxHistorialContrasenasPorEntrada ?: this.maxHistorialContrasenasPorEntrada,
        recordatorioExportacionDias = config.recordatorioExportacionDias ?: this.recordatorioExportacionDias,
        ordenAjustesPersonalizado = config.ordenAjustesPersonalizado ?: this.ordenAjustesPersonalizado
    )
}
