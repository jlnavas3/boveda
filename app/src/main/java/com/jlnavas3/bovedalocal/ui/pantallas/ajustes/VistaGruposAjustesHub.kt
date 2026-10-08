package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

@Composable
fun VistaGruposAjustesHub(
    todosLosElementos: List<ElementoMenuAjustes>,
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alAbrirNombreBoveda: () -> Unit = {},
    alAbrirProveedorPasskeys: () -> Unit = {},
    alAbrirCambioMaestra: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 1. Obtener los grupos Nivel 1 en el orden personalizado dinámico
    val gruposN1 = MapaAjustes.obtenerNodosRaiz(
        ordenPersonalizado = ajustes.ordenAjustesPersonalizado,
        reparenting = ajustes.reparentingPersonalizado
    )

    Column(modifier = modifier) {
        gruposN1.forEachIndexed { gIndex, grupo ->
            // 2. Obtener los hijos efectivos de este grupo (respetando reparenting y orden)
            val hijos = MapaAjustes.obtenerHijosDe(
                padreId = grupo.id,
                ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
                reparenting = ajustes.reparentingPersonalizado
            )

            val elementosDelGrupo = hijos.mapNotNull { hijoNodo ->
                val predefinido = todosLosElementos.firstOrNull { it.idEtiqueta == hijoNodo.id }
                if (predefinido != null) {
                    predefinido
                } else {
                    // Elemento adoptado o promovido dinámicamente desde subniveles
                    val (icono, color) = MapaAjustes.resolverIconoYColor(hijoNodo)
                    val accion: () -> Unit = when {
                        hijoNodo.accionEspecial == AccionEspecialAjuste.CAMBIAR_MAESTRA -> alAbrirCambioMaestra
                        hijoNodo.accionEspecial == AccionEspecialAjuste.NOMBRE_BOVEDA -> alAbrirNombreBoveda
                        hijoNodo.accionEspecial == AccionEspecialAjuste.PROVEEDOR_PASSKEYS -> alAbrirProveedorPasskeys
                        hijoNodo.pantallaDestino != null -> { { vm.ir(hijoNodo.pantallaDestino) } }
                        else -> { { vm.irPorId(hijoNodo.id) } }
                    }
                    ElementoMenuAjustes(
                        titulo = hijoNodo.titulo,
                        subtitulo = hijoNodo.subtitulo,
                        icono = icono,
                        colorIcono = color,
                        idEtiqueta = hijoNodo.id,
                        grupo = grupo.titulo,
                        palabrasClave = hijoNodo.palabrasClave.joinToString(" "),
                        valorTexto = null,
                        alPulsar = accion
                    )
                }
            }

            if (elementosDelGrupo.isNotEmpty()) {
                if (gIndex > 0) Spacer(Modifier.height(EspaciadoComponentes))
                ComponenteGrupo(
                    etiqueta = grupo.titulo,
                    icono = grupo.icono ?: Icons.Filled.Settings,
                    idGrupo = grupo.id,
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    elementosDelGrupo.forEachIndexed { index, elem ->
                        if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                        ComponenteNavegacion(
                            titulo = elem.titulo,
                            icono = elem.icono,
                            colorIcono = elem.colorIcono,
                            idFila = elem.idEtiqueta,
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorTexto = elem.valorTexto,
                            alPulsar = elem.alPulsar
                        )
                    }
                }
            }
        }
    }
}
