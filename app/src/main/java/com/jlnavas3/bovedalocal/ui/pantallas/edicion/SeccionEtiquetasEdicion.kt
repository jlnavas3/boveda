package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

import androidx.compose.foundation.shape.RoundedCornerShape
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun ChipEtiqueta(texto: String, sugerida: Boolean = false, alPulsar: () -> Unit) {
    val forma = RoundedCornerShape(8.dp)
    val fondoChip = if (esOscuroActivo) Color(0xFF19181B) else Color(0xFFF0F0F3)
    val fondoSugerida = if (esOscuroActivo) Color(0xFF262529) else Color(0xFFE6E6EB)
    val bordeChip = if (esOscuroActivo) Color(0xFF38373C) else Color(0xFFDADAE0)

    Row(
        modifier = Modifier
            .clip(forma)
            .background(if (sugerida) fondoSugerida else fondoChip)
            .border(1.dp, bordeChip, forma)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#${normalizarEtiqueta(texto)}", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
        if (!sugerida) {
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Filled.Close, contentDescription = "Quitar etiqueta", tint = TextoSecundario, modifier = Modifier.size(16.dp))
        }
    }
}

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
