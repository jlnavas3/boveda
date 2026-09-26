package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoNombreBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoProveedorPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.VistaGruposAjustesHub
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.VistaResultadosBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.crearCatalogoAjustesHub

@Composable
fun PantallaAjustes(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var textoBusqueda by remember { mutableStateOf("") }
    var dialogoNombreBoveda by remember { mutableStateOf(false) }
    var dialogoProveedorPasskeys by remember { mutableStateOf(false) }

    val todosLosElementos = remember(ajustes) {
        crearCatalogoAjustesHub(
            ajustes = ajustes,
            vm = vm,
            alAbrirNombreBoveda = { dialogoNombreBoveda = true },
            alAbrirProveedorPasskeys = { dialogoProveedorPasskeys = true }
        )
    }

    val elementosFiltrados = remember(textoBusqueda, todosLosElementos) {
        val q = textoBusqueda.trim().lowercase()
        if (q.isEmpty()) emptyList()
        else todosLosElementos.filter {
            it.titulo.lowercase().contains(q) ||
            it.subtitulo.lowercase().contains(q) ||
            it.idEtiqueta.lowercase().contains(q) ||
            it.palabrasClave.lowercase().contains(q)
        }
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Ajustes",
                idEtiqueta = "00",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BarraBusquedaAjustes(
                    texto = textoBusqueda,
                    alCambiarTexto = { textoBusqueda = it },
                    placeholder = "Buscar en ajustes..."
                )

                Spacer(Modifier.height(14.dp))

                if (textoBusqueda.isNotBlank()) {
                    VistaResultadosBusquedaAjustes(
                        textoBusqueda = textoBusqueda,
                        elementosFiltrados = elementosFiltrados,
                        mostrarIds = ajustes.mostrarIdsAjustes
                    )
                } else {
                    VistaGruposAjustesHub(
                        todosLosElementos = todosLosElementos,
                        mostrarIds = ajustes.mostrarIdsAjustes
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoNombreBoveda) {
        DialogoNombreBoveda(
            nombreActual = ajustes.nombrePersonalizado,
            alCerrar = { dialogoNombreBoveda = false },
            alGuardar = { vm.ajustarNombrePersonalizado(it) }
        )
    }

    if (dialogoProveedorPasskeys) {
        DialogoProveedorPasskeys(
            actividad = actividad,
            vm = vm,
            alCerrar = { dialogoProveedorPasskeys = false }
        )
    }
}
