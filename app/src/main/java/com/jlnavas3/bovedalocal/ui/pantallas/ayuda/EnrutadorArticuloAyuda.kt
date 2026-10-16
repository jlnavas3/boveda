/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaAdjuntos
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaAliasCorreo
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaApariencia
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaArquitectura
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaAutocompletado
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaAutodestruccion
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaBusqueda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaCamposPersonalizados
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaCategorias
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaClavesSsh
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaCopias
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaEscaner
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaEtiquetas
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaIdentidades
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaIgnorados
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaImportacion
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaKitEmergencia
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaOrdenacion
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaSalud
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaSenuelo
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos.PantallaAyudaWidgets

/**
 * Enrutador desacoplado para redirigir al composable específico de cada artículo de ayuda.
 */
@Composable
fun EnrutadorArticuloAyuda(
    idArticulo: String,
    vm: VaultViewModel,
    alVolver: () -> Unit
) {
    when (idArticulo) {
        "busqueda" -> PantallaAyudaBusqueda(vm, alVolver)
        "identidades" -> PantallaAyudaIdentidades(vm, alVolver)
        "categorias" -> PantallaAyudaCategorias(vm, alVolver)
        "etiquetas" -> PantallaAyudaEtiquetas(vm, alVolver)
        "ordenacion" -> PantallaAyudaOrdenacion(vm, alVolver)
        "generador" -> PantallaAyudaGenerador(vm, alVolver)
        "claves_ssh" -> PantallaAyudaClavesSsh(vm, alVolver)
        "alias_correo" -> PantallaAyudaAliasCorreo(vm, alVolver)
        "autenticador" -> PantallaAyudaAutenticador(vm, alVolver)
        "escaner" -> PantallaAyudaEscaner(vm, alVolver)
        "passkeys" -> PantallaAyudaPasskeys(vm, alVolver)
        "adjuntos" -> PantallaAyudaAdjuntos(vm, alVolver)
        "campos_personalizados" -> PantallaAyudaCamposPersonalizados(vm, alVolver)
        "historial" -> PantallaAyudaHistorial(vm, alVolver)
        "salud" -> PantallaAyudaSalud(vm, alVolver)
        "duplicados" -> PantallaAyudaDuplicados(vm, alVolver)
        "ignorados" -> PantallaAyudaIgnorados(vm, alVolver)
        "papelera" -> PantallaAyudaPapelera(vm, alVolver)
        "autodestruccion" -> PantallaAyudaAutodestruccion(vm, alVolver)
        "senuelo" -> PantallaAyudaSenuelo(vm, alVolver)
        "seguridad" -> PantallaAyudaSeguridad(vm, alVolver)
        "copias" -> PantallaAyudaCopias(vm, alVolver)
        "kit_emergencia" -> PantallaAyudaKitEmergencia(vm, alVolver)
        "importacion" -> PantallaAyudaImportacion(vm, alVolver)
        "apariencia" -> PantallaAyudaApariencia(vm, alVolver)
        "widgets" -> PantallaAyudaWidgets(vm, alVolver)
        "autocompletado" -> PantallaAyudaAutocompletado(vm, alVolver)
        "arquitectura" -> PantallaAyudaArquitectura(vm, alVolver)
        else -> PantallaAyudaBusqueda(vm, alVolver)
    }
}
