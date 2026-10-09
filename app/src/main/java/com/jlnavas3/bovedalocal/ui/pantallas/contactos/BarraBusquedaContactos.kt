package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente con buscador interactivo en tiempo real para filtrar la lista de contactos.
 */
@Composable
fun BarraBusquedaContactos(
    consulta: String,
    alCambiarConsulta: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteCampoTexto(
        valor = consulta,
        etiqueta = "Buscar contacto o teléfono...",
        alCambiar = alCambiarConsulta,
        icono = Icons.Filled.Search,
        trailingIcon = if (consulta.isNotEmpty()) {
            {
                IconButton(onClick = { alCambiarConsulta("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = TextoSecundario
                    )
                }
            }
        } else null,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}
