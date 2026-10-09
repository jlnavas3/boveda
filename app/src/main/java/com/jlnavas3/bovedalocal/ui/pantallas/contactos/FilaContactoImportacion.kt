package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente composable que renderiza una fila de contacto del dispositivo
 * con selector individual para ser importado hacia la bóveda.
 */
@Composable
fun FilaContactoImportacion(
    contacto: ContactoDispositivo,
    seleccionado: Boolean,
    alAlternarSeleccion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detalle = contacto.telefonos.firstOrNull() ?: contacto.correos.firstOrNull() ?: "Sin número registrado"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { alAlternarSeleccion() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = seleccionado,
            onCheckedChange = { alAlternarSeleccion() },
            colors = CheckboxDefaults.colors(
                checkedColor = ColorAcento,
                checkmarkColor = androidx.compose.ui.graphics.Color.Black
            )
        )

        Spacer(Modifier.width(8.dp))

        Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
            Monograma(
                titulo = contacto.nombre.ifBlank { "?" },
                semilla = contacto.nombre,
                tamano = 36
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contacto.nombre,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = detalle,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
