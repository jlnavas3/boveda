package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository

/**
 * Sub-delegado especializado en la configuración de la tarjeta rápida (Tile) y parámetros de TOTP manual.
 */
interface VaultAjustesTileTotpManualDelegate {
    val repositorio: VaultRepository

    fun ajustarTileModo(modo: String) = repositorio.ajustes.actualizar { it.copy(tileModo = modo) }

    fun ajustarTileLongitud(longitud: Int) = repositorio.ajustes.actualizar { it.copy(tileLongitud = longitud) }

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
        repositorio.ajustes.actualizar { it.copy(tileDicewarePalabras = palabras) }
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
}
