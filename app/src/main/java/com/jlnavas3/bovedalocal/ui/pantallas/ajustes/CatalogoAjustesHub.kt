package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel

data class ElementoMenuAjustes(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val idEtiqueta: String,
    val grupo: String,
    val palabrasClave: String = "",
    val valorTexto: String? = null,
    val alPulsar: () -> Unit
)

fun crearCatalogoAjustesHub(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alAbrirNombreBoveda: () -> Unit,
    alAbrirProveedorPasskeys: () -> Unit,
    alAbrirCambioMaestra: () -> Unit
): List<ElementoMenuAjustes> {
    val seguridad = crearElementosSeguridadAjustes(vm, alAbrirCambioMaestra)
    val apariencia = crearElementosAparienciaAjustes(ajustes, vm, alAbrirNombreBoveda)
    val lista = crearElementosListaAjustes(ajustes, vm)
    val herramientas = crearElementosHerramientasAjustes(vm, alAbrirProveedorPasskeys)
    val (copias, sistema) = crearElementosCopiasYSistemaAjustes(vm)

    val base = seguridad + apariencia + lista + herramientas + copias + sistema
    val orden = ajustes.ordenAjustesPersonalizado
    if (orden.isEmpty()) return base

    val porId = base.associateBy { it.idEtiqueta }
    val ordenados = orden.mapNotNull { porId[it] }
    val noOrdenados = base.filterNot { orden.contains(it.idEtiqueta) }
    return ordenados + noOrdenados
}
