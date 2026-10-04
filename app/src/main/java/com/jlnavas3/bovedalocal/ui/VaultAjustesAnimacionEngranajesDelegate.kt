package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en calibración de animación de desbloqueo, engranajes y puerta de bóveda.
 */
interface VaultAjustesAnimacionEngranajesDelegate {
    val repositorio: VaultRepository

    fun ajustarAnimacionDesbloqueo(modo: String) {
        repositorio.ajustes.actualizar { it.copy(animacionDesbloqueo = modo) }
        Diagnostico.apuntar("apariencia", "Animación de desbloqueo configurada en: $modo")
    }

    fun restablecerAnimacionDesbloqueo() = ajustarAnimacionDesbloqueo(AjustesDefaults.Animacion.TIPO_DESBLOQUEO)

    // --- Personalización de Engranajes ---
    fun ajustarEngranajesVelocidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesVelocidad = valor) }
    }
    fun ajustarEngranajesGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesGrosorBorde = valor) }
    }
    fun ajustarEngranajesAlturaDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesAlturaDientes = valor) }
    }
    fun ajustarEngranajesAnchoDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesAnchoDientes = valor) }
    }
    fun ajustarEngranajesGrosorRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesGrosorRadios = valor) }
    }
    fun ajustarEngranajesCurvaturaRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesCurvaturaRadios = valor) }
    }
    fun ajustarEngranajesCantidadRadios(valor: Int) {
        repositorio.ajustes.actualizar { it.copy(engranajesCantidadRadios = valor) }
    }
    fun ajustarEngranajesRadioInterior(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesRadioInterior = valor) }
    }
    fun ajustarEngranajesTamanoEje(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesTamanoEje = valor) }
    }
    fun ajustarEngranajesSombraIntensidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesSombraIntensidad = valor) }
    }
    fun ajustarEngranajesColor(campo: String, hex: String) {
        repositorio.ajustes.actualizar {
            when (campo) {
                "brillo" -> it.copy(engranajesColorBrillo = hex)
                "principal" -> it.copy(engranajesColorPrincipal = hex)
                "sombra_medio" -> it.copy(engranajesColorSombraMedio = hex)
                "sombra_oscuro" -> it.copy(engranajesColorSombraOscuro = hex)
                "bisel" -> it.copy(engranajesColorBisel = hex)
                "interior" -> it.copy(engranajesColorInterior = hex)
                "cubo" -> it.copy(engranajesColorCubo = hex)
                "eje" -> it.copy(engranajesColorEje = hex)
                else -> it
            }
        }
    }
    fun restablecerAjustesEngranajes() {
        repositorio.ajustes.actualizar {
            it.copy(
                engranajesVelocidad = AjustesDefaults.Animacion.Engranajes.VELOCIDAD,
                engranajesGrosorBorde = AjustesDefaults.Animacion.Engranajes.GROSOR_BORDE,
                engranajesAlturaDientes = AjustesDefaults.Animacion.Engranajes.ALTURA_DIENTES,
                engranajesAnchoDientes = AjustesDefaults.Animacion.Engranajes.ANCHO_DIENTES,
                engranajesGrosorRadios = AjustesDefaults.Animacion.Engranajes.GROSOR_RADIOS,
                engranajesCurvaturaRadios = AjustesDefaults.Animacion.Engranajes.CURVATURA_RADIOS,
                engranajesCantidadRadios = AjustesDefaults.Animacion.Engranajes.CANTIDAD_RADIOS,
                engranajesRadioInterior = AjustesDefaults.Animacion.Engranajes.RADIO_INTERIOR,
                engranajesTamanoEje = AjustesDefaults.Animacion.Engranajes.TAMANO_EJE,
                engranajesSombraIntensidad = AjustesDefaults.Animacion.Engranajes.SOMBRA_INTENSIDAD,
                engranajesColorBrillo = AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
                engranajesColorPrincipal = AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
                engranajesColorSombraMedio = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
                engranajesColorSombraOscuro = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
                engranajesColorBisel = AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
                engranajesColorInterior = AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
                engranajesColorCubo = AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
                engranajesColorEje = AjustesDefaults.Animacion.Engranajes.COLOR_EJE
            )
        }
    }

    // --- Personalización de Puerta de Bóveda ---
    fun ajustarPuertaVelocidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaVelocidad = valor) }
    }
    fun ajustarPuertaGrosorAnillos(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaGrosorAnillos = valor) }
    }
    fun ajustarPuertaColor(hex: String) {
        repositorio.ajustes.actualizar { it.copy(puertaColor = hex) }
    }
    fun restablecerAjustesPuerta() {
        repositorio.ajustes.actualizar {
            it.copy(
                puertaVelocidad = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
                puertaGrosorAnillos = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS,
                puertaColor = AjustesDefaults.Animacion.Puerta.COLOR
            )
        }
    }

    fun restablecerGeometriaPuerta() {
        repositorio.ajustes.actualizar {
            it.copy(
                puertaVelocidad = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
                puertaGrosorAnillos = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS
            )
        }
    }

    fun restablecerColorPuerta() = ajustarPuertaColor(AjustesDefaults.Animacion.Puerta.COLOR)
}
