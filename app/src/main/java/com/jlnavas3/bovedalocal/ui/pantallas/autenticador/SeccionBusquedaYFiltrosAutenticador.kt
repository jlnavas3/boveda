package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo

@Composable
fun SeccionBusquedaYFiltrosAutenticador(
    busquedaVisible: Boolean,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alLimpiarFavoritos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = busquedaVisible || textoBusqueda.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = textoBusqueda,
                    alCambiar = alCambiarTextoBusqueda,
                    alCerrar = alCerrarBusqueda
                )
            }
        }

        if (soloFavoritos) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "★ Favoritos",
                    alLimpiar = alLimpiarFavoritos
                )
            }
        }
    }
}
