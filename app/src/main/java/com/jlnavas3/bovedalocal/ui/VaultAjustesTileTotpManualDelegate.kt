/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository

/**
 * Sub-delegado especializado en la configuración de la tarjeta rápida (Tile) y parámetros de TOTP manual.
 */
interface VaultAjustesTileTotpManualDelegate {
    val repositorio: VaultRepository

    fun ajustarTileModo(modo: String) = repositorio.ajustes.actualizar { it.copy(tileModo = modo) }

    fun ajustarTileLongitud(longitud: Int) = repositorio.ajustes.actualizar {
        it.copy(
            tileLongitud = longitud.coerceIn(
                AjustesDefaults.Tile.LONGITUD_MIN,
                AjustesDefaults.Tile.LONGITUD_MAX
            )
        )
    }

    fun ajustarTilePatron(patron: String) = repositorio.ajustes.actualizar { it.copy(tilePatron = patron) }

    fun ajustarTileCopiarPortapapeles(copiar: Boolean) = repositorio.ajustes.actualizar { it.copy(tileCopiarPortapapeles = copiar) }

    fun ajustarTileMostrarToast(toast: Boolean) = repositorio.ajustes.actualizar { it.copy(tileMostrarToast = toast) }

    fun ajustarTileHaptica(haptica: Boolean) = repositorio.ajustes.actualizar { it.copy(tileHaptica = haptica) }

    fun ajustarTileHapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(tileHapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun ajustarTileSimbolos(simbolos: String) {
        repositorio.ajustes.actualizar { it.copy(tileSimbolos = simbolos) }
    }

    fun ajustarTileDicewarePalabras(palabras: Int) {
        repositorio.ajustes.actualizar {
            it.copy(
                tileDicewarePalabras = palabras.coerceIn(
                    AjustesDefaults.Tile.DICEWARE_PALABRAS_MIN,
                    AjustesDefaults.Tile.DICEWARE_PALABRAS_MAX
                )
            )
        }
    }

    fun ajustarTileDicewareSeparador(separador: String) {
        repositorio.ajustes.actualizar { it.copy(tileDicewareSeparador = separador) }
    }

    fun restablecerTile() {
        repositorio.ajustes.actualizar {
            it.copy(
                tileModo = AjustesDefaults.Tile.MODO,
                tileLongitud = AjustesDefaults.Tile.LONGITUD,
                tilePatron = AjustesDefaults.Tile.PATRON,
                tileSimbolos = AjustesDefaults.Tile.SIMBOLOS,
                tileDicewarePalabras = AjustesDefaults.Tile.DICEWARE_PALABRAS,
                tileDicewareSeparador = AjustesDefaults.Tile.DICEWARE_SEPARADOR,
                tileCopiarPortapapeles = AjustesDefaults.Tile.COPIAR_PORTAPAPELES,
                tileMostrarToast = AjustesDefaults.Tile.MOSTRAR_TOAST,
                tileHaptica = AjustesDefaults.Tile.HAPTICA,
                tileHapticaIntensidad = AjustesDefaults.Tile.HAPTICA_INTENSIDAD
            )
        }
    }

    fun ajustarTotpManualDigitos(digitos: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualDigitos = digitos) }

    fun ajustarTotpManualPeriodo(periodo: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualPeriodo = periodo) }

    fun ajustarTotpManualAlgoritmo(algoritmo: String) =
        repositorio.ajustes.actualizar { it.copy(totpManualAlgoritmo = algoritmo) }

    fun ajustarTotpSepararDigitos(separar: Boolean) =
        repositorio.ajustes.actualizar { it.copy(totpSepararDigitos = separar) }

    fun restablecerTotpManual() {
        repositorio.ajustes.actualizar {
            it.copy(
                totpManualDigitos = AjustesDefaults.TotpManual.DIGITOS,
                totpManualPeriodo = AjustesDefaults.TotpManual.PERIODO,
                totpManualAlgoritmo = AjustesDefaults.TotpManual.ALGORITMO,
                totpSepararDigitos = AjustesDefaults.TotpManual.SEPARAR_DIGITOS
            )
        }
    }

    fun ajustarAliasCorreoBasePredeterminado(correo: String) {
        val limpio = correo.trim().lowercase()
        repositorio.ajustes.actualizar { it.copy(aliasCorreoBasePredeterminado = limpio) }
    }

    fun ajustarAliasCorreoLongitudSufijo(longitud: Int) {
        val valor = longitud.coerceIn(AjustesDefaults.Generador.ALIAS_CORREO_LONGITUD_MIN, AjustesDefaults.Generador.ALIAS_CORREO_LONGITUD_MAX)
        repositorio.ajustes.actualizar { it.copy(aliasCorreoLongitudSufijo = valor) }
    }

    fun ajustarAliasCorreoDelimitador(delimitador: String) {
        val limpio = if (delimitador in listOf("+", "-", ".")) delimitador else AjustesDefaults.Generador.ALIAS_CORREO_DELIMITADOR
        repositorio.ajustes.actualizar { it.copy(aliasCorreoDelimitador = limpio) }
    }

    fun restablecerAliasCorreo() {
        repositorio.ajustes.actualizar {
            it.copy(
                aliasCorreoBasePredeterminado = AjustesDefaults.Generador.ALIAS_CORREO_BASE_PREDETERMINADO,
                aliasCorreoLongitudSufijo = AjustesDefaults.Generador.ALIAS_CORREO_LONGITUD_SUFIJO,
                aliasCorreoDelimitador = AjustesDefaults.Generador.ALIAS_CORREO_DELIMITADOR
            )
        }
    }
}
