/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(velocidad = valor) } }
    }
    fun ajustarEngranajesGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(grosorBorde = valor) } }
    }
    fun ajustarEngranajesAlturaDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(alturaDientes = valor) } }
    }
    fun ajustarEngranajesAnchoDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(anchoDientes = valor) } }
    }
    fun ajustarEngranajesGrosorRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(grosorRadios = valor) } }
    }
    fun ajustarEngranajesCurvaturaRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(curvaturaRadios = valor) } }
    }
    fun ajustarEngranajesCantidadRadios(valor: Int) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(cantidadRadios = valor) } }
    }
    fun ajustarEngranajesRadioInterior(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(radioInterior = valor) } }
    }
    fun ajustarEngranajesTamanoEje(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(tamanoEje = valor) } }
    }
    fun ajustarEngranajesSombraIntensidad(valor: Float) {
        repositorio.ajustes.actualizar { it.actualizarEngranajes { e -> e.copy(sombraIntensidad = valor) } }
    }
    fun ajustarEngranajesColor(campo: String, hex: String, esOscuro: Boolean = true) {
        repositorio.ajustes.actualizar { actual ->
            actual.actualizarEngranajes { e ->
                if (esOscuro) {
                    val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(e.coloresOscuroJson, campo, hex)
                    when (campo) {
                        "brillo" -> e.copy(coloresOscuroJson = nuevoJson, colorBrillo = hex)
                        "principal" -> e.copy(coloresOscuroJson = nuevoJson, colorPrincipal = hex)
                        "sombra_medio" -> e.copy(coloresOscuroJson = nuevoJson, colorSombraMedio = hex)
                        "sombra_oscuro" -> e.copy(coloresOscuroJson = nuevoJson, colorSombraOscuro = hex)
                        "bisel" -> e.copy(coloresOscuroJson = nuevoJson, colorBisel = hex)
                        "interior" -> e.copy(coloresOscuroJson = nuevoJson, colorInterior = hex)
                        "cubo" -> e.copy(coloresOscuroJson = nuevoJson, colorCubo = hex)
                        "eje" -> e.copy(coloresOscuroJson = nuevoJson, colorEje = hex)
                        else -> e.copy(coloresOscuroJson = nuevoJson)
                    }
                } else {
                    val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(e.coloresClaroJson, campo, hex)
                    e.copy(coloresClaroJson = nuevoJson)
                }
            }
        }
    }

    fun restablecerColoresEngranajes(esOscuro: Boolean = true) {
        repositorio.ajustes.actualizar { actual ->
            actual.actualizarEngranajes { e ->
                if (esOscuro) {
                    e.copy(
                        coloresOscuroJson = "",
                        colorBrillo = AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
                        colorPrincipal = AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
                        colorSombraMedio = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
                        colorSombraOscuro = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
                        colorBisel = AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
                        colorInterior = AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
                        colorCubo = AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
                        colorEje = AjustesDefaults.Animacion.Engranajes.COLOR_EJE
                    )
                } else {
                    e.copy(coloresClaroJson = "")
                }
            }
        }
    }

    fun restablecerAjustesEngranajes() {
        repositorio.ajustes.actualizar {
            it.copy(engranajes = com.jlnavas3.bovedalocal.data.AjustesAnimacionEngranajes())
        }
    }

    // --- Personalización de Puerta de Bóveda ---
    fun ajustarPuertaVelocidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaVelocidad = valor) }
    }
    fun ajustarPuertaGrosorAnillos(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaGrosorAnillos = valor) }
    }
    fun ajustarPuertaColor(hex: String, esOscuro: Boolean = true) {
        repositorio.ajustes.actualizar {
            if (esOscuro) {
                val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(it.puertaColoresOscuroJson, "puerta", hex)
                it.copy(puertaColoresOscuroJson = nuevoJson, puertaColor = hex)
            } else {
                val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(it.puertaColoresClaroJson, "puerta", hex)
                it.copy(puertaColoresClaroJson = nuevoJson)
            }
        }
    }
    fun ajustarPuertaColorDestello(hex: String, esOscuro: Boolean = true) {
        repositorio.ajustes.actualizar {
            if (esOscuro) {
                val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(it.puertaColoresOscuroJson, "destello", hex)
                it.copy(puertaColoresOscuroJson = nuevoJson, puertaColorDestello = hex)
            } else {
                val nuevoJson = com.jlnavas3.bovedalocal.data.actualizarColorEnJson(it.puertaColoresClaroJson, "destello", hex)
                it.copy(puertaColoresClaroJson = nuevoJson)
            }
        }
    }
    fun restablecerAjustesPuerta() {
        repositorio.ajustes.actualizar {
            it.copy(
                puertaVelocidad = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
                puertaGrosorAnillos = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS,
                puertaColor = AjustesDefaults.Animacion.Puerta.COLOR,
                puertaColorDestello = "",
                puertaColoresClaroJson = "",
                puertaColoresOscuroJson = ""
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

    fun restablecerColorPuerta(esOscuro: Boolean = true) {
        repositorio.ajustes.actualizar {
            if (esOscuro) {
                it.copy(puertaColoresOscuroJson = "", puertaColor = "", puertaColorDestello = "")
            } else {
                it.copy(puertaColoresClaroJson = "")
            }
        }
    }
}
