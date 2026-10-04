package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SeccionEtiquetasEdicion(
    etiquetas: List<String>,
    alCambiarEtiquetas: (List<String>) -> Unit,
    etiquetasSugeridas: List<String>
) {
    var nuevaEtiqueta by remember { mutableStateOf("") }

    if (etiquetas.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            etiquetas.forEach { etiqueta ->
                ChipEtiqueta(etiqueta) { alCambiarEtiquetas(etiquetas - etiqueta) }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(1f)) {
            CampoBoveda(
                valor = nuevaEtiqueta,
                etiqueta = "Nueva etiqueta",
                alCambiar = { nuevaEtiqueta = it }
            )
        }
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.width(112.dp)) {
            BotonBorde(
                texto = "Añadir",
                icono = Icons.Filled.Add
            ) {
                val limpia = normalizarEtiqueta(nuevaEtiqueta)
                if (limpia.isNotEmpty() && !etiquetas.contains(limpia)) {
                    alCambiarEtiquetas(etiquetas + limpia)
                }
                nuevaEtiqueta = ""
            }
        }
    }
    val sugerenciasRestantes = etiquetasSugeridas.filterNot { etiquetas.contains(it) }
    if (sugerenciasRestantes.isNotEmpty()) {
        Spacer(Modifier.height(10.dp))
        Text("Ya usadas en otras entradas", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sugerenciasRestantes.forEach { etiqueta ->
                ChipEtiqueta(etiqueta, sugerida = true) {
                    val normalizada = normalizarEtiqueta(etiqueta)
                    if (normalizada.isNotEmpty() && !etiquetas.contains(normalizada)) {
                        alCambiarEtiquetas(etiquetas + normalizada)
                    }
                }
            }
        }
    }
}
