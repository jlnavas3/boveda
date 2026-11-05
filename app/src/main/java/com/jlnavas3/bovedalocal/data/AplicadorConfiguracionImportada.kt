/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Aplica sobre los ajustes actuales únicamente aquellas secciones
 * de configuración que fueron seleccionadas por el usuario, registrando
 * cada bloque aplicado o con error en el historial de eventos de diagnóstico.
 */
fun AjustesApp.aplicarConfiguracionFiltrada(
    config: ConfiguracionBovedaExportable,
    opciones: OpcionesImportacionBvda
): AjustesApp {
    var resultado = this

    if (opciones.importarReglasUrls) {
        try {
            resultado = resultado.copy(
                prefijosSubdominios = config.prefijosSubdominios ?: resultado.prefijosSubdominios,
                tldsDescartables = config.tldsDescartables ?: resultado.tldsDescartables,
                marcasPersonalizadas = config.marcasPersonalizadas ?: resultado.marcasPersonalizadas,
                formatoColisionTitulos = config.formatoColisionTitulos ?: resultado.formatoColisionTitulos,
                respetarTitulosPersonalizados = config.respetarTitulosPersonalizados ?: resultado.respetarTitulosPersonalizados
            )
            Diagnostico.apuntar(
                "ajustes",
                "Reglas de URLs importadas (${config.prefijosSubdominios?.size ?: 0} prefijos, ${config.tldsDescartables?.size ?: 0} TLDs, ${config.marcasPersonalizadas?.size ?: 0} marcas)"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar reglas de URLs importadas", e)
        }
    }

    if (opciones.importarReglasHomelab) {
        try {
            resultado = resultado.copy(
                puertosServiciosLocales = config.puertosServiciosLocales ?: resultado.puertosServiciosLocales,
                rutasServiciosLocales = config.rutasServiciosLocales ?: resultado.rutasServiciosLocales,
                octetosRouter = config.octetosRouter ?: resultado.octetosRouter,
                plantillaRouterIp = config.plantillaRouterIp ?: resultado.plantillaRouterIp,
                plantillaServidorIp = config.plantillaServidorIp ?: resultado.plantillaServidorIp
            )
            Diagnostico.apuntar(
                "ajustes",
                "Reglas Homelab importadas (${config.puertosServiciosLocales?.size ?: 0} puertos, ${config.rutasServiciosLocales?.size ?: 0} rutas, ${config.octetosRouter?.size ?: 0} octetos)"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar reglas Homelab importadas", e)
        }
    }

    if (opciones.importarAutofillDeteccion) {
        try {
            resultado = resultado.copy(
                autocompletado = resultado.autocompletado.copy(
                    sugerenciasTeclado = config.autofillSugerenciasTeclado ?: resultado.autofillSugerenciasTeclado,
                    copiarTotpAutomatico = config.autofillCopiarTotpAutomatico ?: resultado.autofillCopiarTotpAutomatico,
                    pistasUsuario = config.autofillPistasUsuario ?: resultado.autofillPistasUsuario,
                    pistasContrasena = config.autofillPistasContrasena ?: resultado.autofillPistasContrasena,
                    pistasOtp = config.autofillPistasOtp ?: resultado.autofillPistasOtp,
                    pistasDocumento = config.autofillPistasDocumento ?: resultado.autofillPistasDocumento,
                    paquetesComunicacion = config.autofillPaquetesComunicacion ?: resultado.autofillPaquetesComunicacion,
                    pistasContactoTelefono = config.autofillPistasContactoTelefono ?: resultado.autofillPistasContactoTelefono,
                    pistasContactoNombre = config.autofillPistasContactoNombre ?: resultado.autofillPistasContactoNombre,
                    maxSugerenciasAutofill = config.maxSugerenciasAutofill ?: resultado.maxSugerenciasAutofill,
                    mostrarDatosTecnicosPasskey = config.mostrarDatosTecnicosPasskey ?: resultado.mostrarDatosTecnicosPasskey,
                    pistasTarjetaNumero = config.autofillPistasTarjetaNumero ?: resultado.autofillPistasTarjetaNumero,
                    pistasTarjetaCvv = config.autofillPistasTarjetaCvv ?: resultado.autofillPistasTarjetaCvv,
                    pistasTarjetaExpiracion = config.autofillPistasTarjetaExpiracion ?: resultado.autofillPistasTarjetaExpiracion,
                    pistasTarjetaTitular = config.autofillPistasTarjetaTitular ?: resultado.autofillPistasTarjetaTitular,
                    pistasDireccionCalle = config.autofillPistasDireccionCalle ?: resultado.autofillPistasDireccionCalle,
                    pistasDireccionCp = config.autofillPistasDireccionCp ?: resultado.autofillPistasDireccionCp,
                    pistasDireccionCiudad = config.autofillPistasDireccionCiudad ?: resultado.autofillPistasDireccionCiudad,
                    pistasDireccionProvincia = config.autofillPistasDireccionProvincia ?: resultado.autofillPistasDireccionProvincia,
                    pistasDireccionPais = config.autofillPistasDireccionPais ?: resultado.autofillPistasDireccionPais,
                    tiposDocumentoPersonalizados = config.tiposDocumentoPersonalizados ?: resultado.tiposDocumentoPersonalizados
                )
            )
            Diagnostico.apuntar("ajustes", "Detección de autocompletado importada con éxito")
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar detección de autocompletado", e)
        }
    }

    if (opciones.importarMapeosApps) {
        try {
            resultado = resultado.copy(
                autocompletado = resultado.autocompletado.copy(
                    mapeoPaquetesPersonalizados = config.mapeoPaquetesPersonalizados ?: resultado.mapeoPaquetesPersonalizados,
                    navegadoresPersonalizados = config.navegadoresPersonalizados ?: resultado.navegadoresPersonalizados
                )
            )
            Diagnostico.apuntar(
                "ajustes",
                "Mapeos de apps y navegadores importados (${config.mapeoPaquetesPersonalizados?.size ?: 0} apps mapeadas)"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar mapeos de apps y navegadores", e)
        }
    }

    if (opciones.importarPlantillas && config.plantillasCamposPersonalizadas != null) {
        try {
            val porId = resultado.plantillasCamposPersonalizadas.associateBy { it.id }.toMutableMap()
            config.plantillasCamposPersonalizadas.forEach { porId[it.id] = it }
            resultado = resultado.copy(plantillasCamposPersonalizadas = porId.values.toList())
            Diagnostico.apuntar(
                "ajustes",
                "Plantillas de campos importadas (${config.plantillasCamposPersonalizadas.size} plantillas)"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar plantillas de campos importadas", e)
        }
    }

    if (opciones.importarSeguridadTiempos) {
        try {
            resultado = resultado.copy(
                autoBloqueoSegundos = config.autoBloqueoSegundos ?: resultado.autoBloqueoSegundos,
                portapapelesSegundos = config.portapapelesSegundos ?: resultado.portapapelesSegundos,
                motorCamara = config.motorCamara ?: resultado.motorCamara,
                escanerQrTamanoVentanaDp = config.escanerQrTamanoVentanaDp ?: resultado.escanerQrTamanoVentanaDp,
                escanerQrVibracion = config.escanerQrVibracion ?: resultado.escanerQrVibracion,
                escanerQrSonido = config.escanerQrSonido ?: resultado.escanerQrSonido,
                proteccionPantalla = config.proteccionPantalla ?: resultado.proteccionPantalla,
                proteccionTapjacking = config.proteccionTapjacking ?: resultado.proteccionTapjacking
            )
            Diagnostico.apuntar(
                "seguridad",
                "Tiempos y cámara importados: autobloqueo=${resultado.autoBloqueoSegundos}s, portapapeles=${resultado.portapapelesSegundos}s, escanerQr=${resultado.escanerQrTamanoVentanaDp}dp"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("seguridad", "Error al aplicar tiempos de seguridad", e)
        }
    }

    if (opciones.importarSeguridadCripto) {
        try {
            // Nota: perfilArgon2 físico de la base de datos no se altera aquí sin re-forjar con contraseña maestra.
            resultado = resultado.copy(
                umbralAntiguedadDias = config.umbralAntiguedadDias ?: resultado.umbralAntiguedadDias,
                saludUmbralPuntuacionDebil = config.saludUmbralPuntuacionDebil ?: resultado.saludUmbralPuntuacionDebil,
                saludLongitudMinimaRecomendada = config.saludLongitudMinimaRecomendada ?: resultado.saludLongitudMinimaRecomendada,
                frenoIntentosGratis = config.frenoIntentosGratis ?: resultado.frenoIntentosGratis,
                frenoSegundosBase = config.frenoSegundosBase ?: resultado.frenoSegundosBase,
                frenoSegundosMax = config.frenoSegundosMax ?: resultado.frenoSegundosMax,
                autodestruccionIntentosFallidosMax = config.autodestruccionIntentosFallidosMax ?: resultado.autodestruccionIntentosFallidosMax
            )
            Diagnostico.apuntar(
                "seguridad",
                "Límites criptográficos importados: frenoIntentos=${resultado.frenoIntentosGratis}, autodestrucción=${resultado.autodestruccionIntentosFallidosMax}, umbralSalud=${resultado.saludUmbralPuntuacionDebil}"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("seguridad", "Error al aplicar límites criptográficos", e)
        }
    }

    if (opciones.importarSeguridadVisual) {
        try {
            resultado = resultado.copy(
                seguridadVisualActiva = config.seguridadVisualActiva ?: resultado.seguridadVisualActiva,
                estiloOcultamientoVisual = config.estiloOcultamientoVisual ?: resultado.estiloOcultamientoVisual,
                tiempoAutoOcultarSegundos = config.tiempoAutoOcultarSegundos ?: resultado.tiempoAutoOcultarSegundos,
                ocultarUsuario = config.ocultarUsuario ?: resultado.ocultarUsuario,
                ocultarContrasena = config.ocultarContrasena ?: resultado.ocultarContrasena,
                ocultarTotp = config.ocultarTotp ?: resultado.ocultarTotp,
                ocultarNotas = config.ocultarNotas ?: resultado.ocultarNotas,
                ocultarCampos = config.ocultarCampos ?: resultado.ocultarCampos
            )
            Diagnostico.apuntar(
                "seguridad",
                "Seguridad visual importada: estilo=${resultado.estiloOcultamientoVisual}"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("seguridad", "Error al aplicar seguridad visual", e)
        }
    }

    if (opciones.importarFormatosLista) {
        try {
            resultado = resultado.copy(
                densidadLista = config.densidadLista ?: resultado.densidadLista,
                criterioOrdenacion = config.criterioOrdenacion ?: resultado.criterioOrdenacion,
                agruparPorSitio = config.agruparPorSitio ?: resultado.agruparPorSitio,
                fijarAncladosArriba = config.fijarAncladosArriba ?: resultado.fijarAncladosArriba,
                modoIdentidades = config.modoIdentidades ?: resultado.modoIdentidades,
                jerarquiaOrganizacion = config.jerarquiaOrganizacion ?: resultado.jerarquiaOrganizacion,
                formatoFecha = config.formatoFecha ?: resultado.formatoFecha,
                formatoHora = config.formatoHora ?: resultado.formatoHora,
                formatoTelefono = config.formatoTelefono ?: resultado.formatoTelefono,
                separadorDecimal = config.separadorDecimal ?: resultado.separadorDecimal,
                csvDelimitadorPredeterminado = config.csvDelimitadorPredeterminado ?: resultado.csvDelimitadorPredeterminado,
                comunicaciones = resultado.comunicaciones.copy(
                    prefijoNumeroOculto = config.comunicacionesPrefijoNumeroOculto ?: resultado.comunicaciones.prefijoNumeroOculto,
                    habilitarLlamadaOculta = config.comunicacionesHabilitarLlamadaOculta ?: resultado.comunicaciones.habilitarLlamadaOculta,
                    habilitarWhatsappDirecto = config.comunicacionesHabilitarWhatsappDirecto ?: resultado.comunicaciones.habilitarWhatsappDirecto,
                    paisPredeterminadoTelefono = config.comunicacionesPaisPredeterminadoTelefono ?: resultado.comunicaciones.paisPredeterminadoTelefono,
                    modoEnvioMensajeSeguro = config.comunicacionesModoEnvioMensajeSeguro ?: resultado.comunicaciones.modoEnvioMensajeSeguro,
                    preguntarPurgaAgenda = config.comunicacionesPreguntarPurgaAgenda ?: resultado.comunicaciones.preguntarPurgaAgenda
                )
            )
            Diagnostico.apuntar(
                "ajustes",
                "Formatos de visualización y comunicaciones importados con éxito"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar formatos de visualización", e)
        }
    }

    if (opciones.importarGeneradorTotp) {
        try {
            resultado = resultado.copy(
                totpManualDigitos = config.totpManualDigitos ?: resultado.totpManualDigitos,
                totpManualPeriodo = config.totpManualPeriodo ?: resultado.totpManualPeriodo,
                totpManualAlgoritmo = config.totpManualAlgoritmo ?: resultado.totpManualAlgoritmo,
                totpSepararDigitos = config.totpSepararDigitos ?: resultado.totpSepararDigitos,
                generadorExcluirAmbiguos = config.generadorExcluirAmbiguos ?: resultado.generadorExcluirAmbiguos,
                generadorLongitudMax = config.generadorLongitudMax ?: resultado.generadorLongitudMax,
                generadorIdiomaFrases = config.generadorIdiomaFrases ?: resultado.generadorIdiomaFrases,
                generadorCapitalizarFrases = config.generadorCapitalizarFrases ?: resultado.generadorCapitalizarFrases,
                aliasCorreoBasePredeterminado = config.aliasCorreoBasePredeterminado ?: resultado.aliasCorreoBasePredeterminado,
                aliasCorreoLongitudSufijo = config.aliasCorreoLongitudSufijo ?: resultado.aliasCorreoLongitudSufijo,
                aliasCorreoDelimitador = config.aliasCorreoDelimitador ?: resultado.aliasCorreoDelimitador
            )
            Diagnostico.apuntar(
                "ajustes",
                "Generador y TOTP importados: longitud=${resultado.generadorLongitudMax}, totp=${resultado.totpManualDigitos}d/${resultado.totpManualPeriodo}s"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar generador y TOTP", e)
        }
    }

    if (opciones.importarRetencionPapelera) {
        try {
            resultado = resultado.copy(
                diasRetencionPapelera = config.diasRetencionPapelera ?: resultado.diasRetencionPapelera,
                maxHistorialContrasenasPorEntrada = config.maxHistorialContrasenasPorEntrada ?: resultado.maxHistorialContrasenasPorEntrada,
                recordatorioExportacionDias = config.recordatorioExportacionDias ?: resultado.recordatorioExportacionDias
            )
            Diagnostico.apuntar(
                "ajustes",
                "Retención de papelera importada: retención=${resultado.diasRetencionPapelera} días, historial=${resultado.maxHistorialContrasenasPorEntrada}"
            )
        } catch (e: Exception) {
            Diagnostico.apuntar("ajustes", "Error al aplicar retención de papelera", e)
        }
    }

    return resultado
}
