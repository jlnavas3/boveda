package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Pantalla para personalizar integralmente la barra lateral:
 * conmutadores de estructura/diseño y reordenación interactiva de accesos directos.
 */
@Composable
fun PantallaPersonalizarMenuLateral(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var mostrarDialogoAnadir by remember { mutableStateOf(false) }

    val itemsNodos = remember(ajustes.menuLateralItemsVisibles) {
        ajustes.menuLateralItemsVisibles.mapNotNull { id ->
            MapaAjustes.buscarPorId(id)
        }
    }

    val itemsCandidatos = remember(ajustes.menuLateralItemsVisibles) {
        val yaVisibles = ajustes.menuLateralItemsVisibles.toSet()
        MapaAjustes.TODOS_LOS_NODOS.filter { nodo ->
            (nodo.pantallaDestino != null || nodo.accionEspecial != null || nodo.esRaiz) &&
                !yaVisibles.contains(nodo.id)
        }
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Personalizar barra lateral",
                idEtiqueta = "02-APA-MNL",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                DescripcionPantalla(
                    subtitulo = "Personaliza la visibilidad de cabecera y pie, el estilo de agrupación y reordena los accesos con el asa ≡."
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            vm.restablecerMenuLateral()
                            vm.avisar("Barra lateral restablecida a valores de fábrica")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restablecer",
                            modifier = Modifier.size(15.dp),
                            tint = ColorAcento
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Restablecer barra lateral",
                            color = ColorAcento,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Bloque 1: Conmutadores de estructura y estética
                GrupoEstructuraMenuLateral(
                    ajustes = ajustes,
                    vm = vm
                )

                Spacer(Modifier.height(16.dp))

                // Bloque 2: Accesos directos reordenables
                GrupoItemsMenuLateral(
                    itemsNodos = itemsNodos,
                    mostrarIds = ajustes.mostrarIdsAjustes,
                    alReordenar = { nuevaLista -> vm.ajustarMenuLateralItemsVisibles(nuevaLista) },
                    alEliminar = { idEliminar ->
                        vm.ajustarMenuLateralItemsVisibles(ajustes.menuLateralItemsVisibles.filterNot { it == idEliminar })
                    },
                    alAbrirSelector = { mostrarDialogoAnadir = true }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (mostrarDialogoAnadir) {
        DialogoAnadirItemMenuLateral(
            itemsDisponibles = itemsCandidatos,
            mostrarIds = ajustes.mostrarIdsAjustes,
            alSeleccionar = { nuevoNodo ->
                vm.ajustarMenuLateralItemsVisibles(ajustes.menuLateralItemsVisibles + nuevoNodo.id)
                vm.avisar("Añadido: ${nuevoNodo.titulo}")
            },
            alCerrar = { mostrarDialogoAnadir = false }
        )
    }
}
