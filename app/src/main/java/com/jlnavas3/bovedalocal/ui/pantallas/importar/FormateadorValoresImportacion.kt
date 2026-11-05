/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ConfiguracionBovedaExportable
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Generador de textos informativos detallados para mostrar los valores contenidos
 * en el archivo .bvda y compararlos directamente con los ajustes actuales del dispositivo.
 *
 * Emplea estilos [AnnotatedString] para diferenciar con colores sutiles y elegantes
 * los datos del Respaldo (azul cielo / océano) de los del Dispositivo (ámbar / arena cálido).
 */
object FormateadorValoresImportacion {

    private const val TEXTO_NO_INCLUIDO = "No incluido en este respaldo"

    // Colores diferenciadores sobrios (no chillones) adaptados a tema claro y oscuro
    private val colorRespaldo: Color
        get() = if (esOscuroActivo) Color(0xFF81D4FA) else Color(0xFF0277BD)

    private val colorDispositivo: Color
        get() = if (esOscuroActivo) Color(0xFFFFCC80) else Color(0xFFB45309)

    private val colorNoIncluido: Color
        get() = TextoSecundario.copy(alpha = 0.7f)

    private val colorSeparador: Color
        get() = TextoSecundario.copy(alpha = 0.45f)

    private fun noIncluido(): AnnotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = colorNoIncluido, fontStyle = FontStyle.Italic)) {
            append(TEXTO_NO_INCLUIDO)
        }
    }

    private fun AnnotatedString.Builder.appendComparacion(
        etiqueta: String,
        valorRespaldo: String?,
        valorDispositivo: String
    ) {
        append(etiqueta)
        append(": ")
        if (valorRespaldo != null) {
            withStyle(SpanStyle(color = colorRespaldo, fontWeight = FontWeight.Medium)) {
                append("Respaldo ($valorRespaldo)")
            }
        } else {
            withStyle(SpanStyle(color = colorNoIncluido, fontStyle = FontStyle.Italic)) {
                append("Respaldo (No incluido)")
            }
        }
        withStyle(SpanStyle(color = colorSeparador)) {
            append(" | ")
        }
        withStyle(SpanStyle(color = colorDispositivo, fontWeight = FontWeight.Medium)) {
            append("Dispositivo ($valorDispositivo)")
        }
    }

    fun categorias(categorias: List<Categoria>): AnnotatedString {
        if (categorias.isEmpty()) return noIncluido()
        return buildAnnotatedString {
            append("En respaldo: ")
            withStyle(SpanStyle(color = colorRespaldo, fontWeight = FontWeight.Medium)) {
                append(categorias.joinToString(", ") { it.nombre })
            }
        }
    }

    fun identidades(identidades: List<Identidad>): AnnotatedString {
        if (identidades.isEmpty()) return noIncluido()
        return buildAnnotatedString {
            identidades.forEachIndexed { idx, iden ->
                if (idx > 0) append("\n")
                append("• ")
                withStyle(SpanStyle(color = colorRespaldo, fontWeight = FontWeight.Medium)) {
                    append(iden.nombre)
                }
                val correosExtras = if (iden.correosSecundarios.isNotEmpty()) " (+${iden.correosSecundarios.size} alias)" else ""
                val detalleIden = when {
                    iden.correoPrincipal.isNotBlank() && iden.tieneDocumento ->
                        "${iden.correoPrincipal}$correosExtras · ${iden.etiquetaDocumentoCompleta}"
                    iden.correoPrincipal.isNotBlank() ->
                        "${iden.correoPrincipal}$correosExtras"
                    iden.tieneDocumento ->
                        iden.etiquetaDocumentoCompleta
                    else ->
                        "Sin identificador"
                }
                append(" ($detalleIden)")
            }
        }
    }

    fun reglasUrls(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val prefCopia = config.prefijosSubdominios?.joinToString(", ")
        val tldsCopia = config.tldsDescartables?.joinToString(", ")
        val marcasCopia = config.marcasPersonalizadas?.size?.toString()
        val respTitCopia = config.respetarTitulosPersonalizados?.let { if (it) "Sí" else "No" }
        val respTitActual = if (actual.respetarTitulosPersonalizados) "Sí" else "No"

        return buildAnnotatedString {
            appendComparacion("Prefijos", prefCopia, actual.prefijosSubdominios.joinToString(", "))
            append("\n")
            appendComparacion("TLDs", tldsCopia, actual.tldsDescartables.joinToString(", "))
            append("\n")
            appendComparacion("Marcas", marcasCopia, actual.marcasPersonalizadas.size.toString())
            append("\n")
            appendComparacion("Respetar títulos", respTitCopia, respTitActual)
        }
    }

    fun reglasHomelab(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val puertosCopia = config.puertosServiciosLocales?.let { mapa ->
            val muestra = mapa.entries.take(3).joinToString(", ") { "${it.key}:${it.value}" }
            if (mapa.size > 3) "$muestra (+${mapa.size - 3})" else muestra
        }
        val octetosCopia = config.octetosRouter?.joinToString(", ")

        return buildAnnotatedString {
            appendComparacion("Puertos locales", puertosCopia, "${actual.puertosServiciosLocales.size} puertos")
            append("\n")
            appendComparacion("Octetos router", octetosCopia, actual.octetosRouter.joinToString(", "))
        }
    }

    fun autofillDeteccion(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val sugCopia = config.autofillSugerenciasTeclado?.let { if (it) "Sí" else "No" }
        val sugActual = if (actual.autocompletado.sugerenciasTeclado) "Sí" else "No"
        val pistasCopia = if (config.autofillPistasUsuario != null || config.autofillPistasContrasena != null || config.autofillPistasDocumento != null) {
            "${config.autofillPistasUsuario?.size ?: 0} user, ${config.autofillPistasContrasena?.size ?: 0} pass, ${config.autofillPistasDocumento?.size ?: 0} doc"
        } else null
        val pistasActual = "${actual.autocompletado.pistasUsuario.size} user, ${actual.autocompletado.pistasContrasena.size} pass, ${actual.autocompletado.pistasDocumento.size} doc"

        return buildAnnotatedString {
            appendComparacion("Sugerencias teclado", sugCopia, sugActual)
            append("\n")
            appendComparacion("Palabras clave", pistasCopia, pistasActual)
        }
    }

    fun mapeosApps(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val totalAppsCopia = config.mapeoPaquetesPersonalizados?.size?.toString()
        val totalNavCopia = config.navegadoresPersonalizados?.size?.toString()

        return buildAnnotatedString {
            appendComparacion("Apps vinculadas", totalAppsCopia, actual.autocompletado.mapeoPaquetesPersonalizados.size.toString())
            append("\n")
            appendComparacion("Navegadores", totalNavCopia, actual.autocompletado.navegadoresPersonalizados.size.toString())
        }
    }

    fun plantillas(config: ConfiguracionBovedaExportable?): AnnotatedString {
        val lista = config?.plantillasCamposPersonalizadas
        if (lista.isNullOrEmpty()) return noIncluido()
        return buildAnnotatedString {
            lista.forEachIndexed { idx, p ->
                if (idx > 0) append("\n")
                append("• ")
                withStyle(SpanStyle(color = colorRespaldo, fontWeight = FontWeight.Medium)) {
                    append(p.titulo)
                }
                append(" (${p.campos.size} campos)")
            }
        }
    }

    fun seguridadTiempos(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val bloqCopia = config.autoBloqueoSegundos?.let { "${it}s" }
        val portCopia = config.portapapelesSegundos?.let { "${it}s" }
        val protCopia = config.proteccionPantalla?.let { if (it) "Sí" else "No" }
        val protActual = if (actual.proteccionPantalla) "Sí" else "No"

        return buildAnnotatedString {
            appendComparacion("Autobloqueo", bloqCopia, "${actual.autoBloqueoSegundos}s")
            append("\n")
            appendComparacion("Portapapeles", portCopia, "${actual.portapapelesSegundos}s")
            append("\n")
            appendComparacion("Protección pantalla", protCopia, protActual)
        }
    }

    fun seguridadCripto(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val argonCopia = config.perfilArgon2
        val intentosCopia = config.frenoIntentosGratis?.toString()
        val autoDestCopia = config.autodestruccionIntentosFallidosMax?.let { if (it > 0) "$it fallos" else "Desactivada" }
        val autoDestActual = if (actual.autodestruccionIntentosFallidosMax > 0) "${actual.autodestruccionIntentosFallidosMax} fallos" else "Desactivada"

        return buildAnnotatedString {
            appendComparacion("Argon2id", argonCopia, actual.perfilArgon2)
            append("\n")
            appendComparacion("Freno de intentos", intentosCopia, actual.frenoIntentosGratis.toString())
            append("\n")
            appendComparacion("Autodestrucción", autoDestCopia, autoDestActual)
        }
    }

    fun seguridadVisual(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val estiloCopia = config.estiloOcultamientoVisual
        val tiempoCopia = config.tiempoAutoOcultarSegundos?.let { "${it}s" }

        return buildAnnotatedString {
            appendComparacion("Estilo puntos", estiloCopia, actual.estiloOcultamientoVisual)
            append("\n")
            appendComparacion("Auto-ocultar", tiempoCopia, "${actual.tiempoAutoOcultarSegundos}s")
        }
    }

    fun formatosLista(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val densCopia = config.densidadLista
        val ordCopia = config.criterioOrdenacion
        val fechaCopia = config.formatoFecha

        return buildAnnotatedString {
            appendComparacion("Densidad", densCopia, actual.densidadLista)
            append("\n")
            appendComparacion("Ordenación", ordCopia, actual.criterioOrdenacion)
            append("\n")
            appendComparacion("Formato fecha", fechaCopia, actual.formatoFecha)
        }
    }

    fun generadorTotp(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val longCopia = config.generadorLongitudMax?.let { "$it car." }
        val totpCopia = if (config.totpManualDigitos != null) {
            "${config.totpManualDigitos}d / ${config.totpManualPeriodo ?: 30}s"
        } else null
        val totpActual = "${actual.totpManualDigitos}d / ${actual.totpManualPeriodo}s"

        return buildAnnotatedString {
            appendComparacion("Longitud generador", longCopia, "${actual.generadorLongitudMax} car.")
            append("\n")
            appendComparacion("TOTP manual", totpCopia, totpActual)
        }
    }

    fun retencionPapelera(config: ConfiguracionBovedaExportable?, actual: AjustesApp): AnnotatedString {
        if (config == null) return noIncluido()
        val diasCopia = config.diasRetencionPapelera?.let { "$it días" }
        val histCopia = config.maxHistorialContrasenasPorEntrada?.toString()

        return buildAnnotatedString {
            appendComparacion("Retención papelera", diasCopia, "${actual.diasRetencionPapelera} días")
            append("\n")
            appendComparacion("Historial claves", histCopia, actual.maxHistorialContrasenasPorEntrada.toString())
        }
    }
}
