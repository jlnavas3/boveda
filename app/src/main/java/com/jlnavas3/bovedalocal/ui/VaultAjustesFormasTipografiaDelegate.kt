package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia

/**
 * Sub-delegado especializado en personalización de formas, bordes y tipografía de la interfaz.
 */
interface VaultAjustesFormasTipografiaDelegate {
    val repositorio: VaultRepository

    // --- Personalización de Bordes y Formas ---
    fun ajustarCurvaturaEsquinas(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(curvaturaEsquinasDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(grosorBordeDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEstiloBorde(estilo: String) {
        repositorio.ajustes.actualizar { it.copy(estiloBorde = estilo) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoComponentes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoComponentesDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun aplicarPresetFormas(curvatura: Float, grosor: Float, estilo: String, espaciado: Float) {
        repositorio.ajustes.actualizar {
            it.copy(
                curvaturaEsquinasDp = curvatura,
                grosorBordeDp = grosor,
                estiloBorde = estilo,
                espaciadoComponentesDp = espaciado
            )
        }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun restablecerFormas() {
        aplicarPresetFormas(
            curvatura = AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP,
            grosor = AjustesDefaults.Formas.GROSOR_BORDE_DP,
            estilo = AjustesDefaults.Formas.ESTILO_BORDE,
            espaciado = AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP
        )
    }

    fun restablecerCurvaturaEsquinas() = ajustarCurvaturaEsquinas(AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP)
    fun restablecerGrosorBorde() = ajustarGrosorBorde(AjustesDefaults.Formas.GROSOR_BORDE_DP)
    fun restablecerEstiloBorde() = ajustarEstiloBorde(AjustesDefaults.Formas.ESTILO_BORDE)
    fun restablecerEspaciadoComponentes() = ajustarEspaciadoComponentes(AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP)

    // --- Personalización de Tipografía y Textos ---
    fun ajustarEscalaTexto(escala: Float) {
        repositorio.ajustes.actualizar { it.copy(escalaTexto = escala) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarPesoTexto(peso: String) {
        repositorio.ajustes.actualizar { it.copy(pesoTexto = peso) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarCursivaTexto(cursiva: Boolean) {
        repositorio.ajustes.actualizar { it.copy(cursivaTexto = cursiva) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoLetras(espaciado: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoLetrasSp = espaciado) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarInterlineadoFactor(factor: Float) {
        repositorio.ajustes.actualizar { it.copy(interlineadoFactor = factor) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarFamiliaFuente(familia: String) {
        repositorio.ajustes.actualizar { it.copy(familiaFuente = familia) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun aplicarPresetTipografia(
        escala: Float,
        peso: String,
        cursiva: Boolean,
        kerning: Float,
        interlineado: Float,
        familia: String
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                escalaTexto = escala,
                pesoTexto = peso,
                cursivaTexto = cursiva,
                espaciadoLetrasSp = kerning,
                interlineadoFactor = interlineado,
                familiaFuente = familia
            )
        }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun restablecerTipografia() {
        aplicarPresetTipografia(
            escala = AjustesDefaults.Tipografia.ESCALA_TEXTO,
            peso = AjustesDefaults.Tipografia.PESO_TEXTO,
            cursiva = AjustesDefaults.Tipografia.CURSIVA_TEXTO,
            kerning = AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP,
            interlineado = AjustesDefaults.Tipografia.INTERLINEADO_FACTOR,
            familia = AjustesDefaults.Tipografia.FAMILIA_FUENTE
        )
    }

    fun restablecerEscalaTexto() = ajustarEscalaTexto(AjustesDefaults.Tipografia.ESCALA_TEXTO)
    fun restablecerKerning() = ajustarEspaciadoLetras(AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP)
    fun restablecerInterlineado() = ajustarInterlineadoFactor(AjustesDefaults.Tipografia.INTERLINEADO_FACTOR)
    fun restablecerPesoTexto() {
        ajustarPesoTexto(AjustesDefaults.Tipografia.PESO_TEXTO)
        ajustarCursivaTexto(AjustesDefaults.Tipografia.CURSIVA_TEXTO)
    }
    fun restablecerFamiliaFuente() = ajustarFamiliaFuente(AjustesDefaults.Tipografia.FAMILIA_FUENTE)
}
