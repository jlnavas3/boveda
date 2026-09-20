package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogoNuevoCampo(
    alDescartar: () -> Unit,
    alCrearCampo: (CampoPersonalizado) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf(TipoCampo.TEXTO) }
    var esSensible by remember { mutableStateOf(false) }

    val tiposDisponibles = remember {
        listOf(
            TipoCampo.TEXTO,
            TipoCampo.NUMERO,
            TipoCampo.DECIMAL,
            TipoCampo.PIN,
            TipoCampo.EMAIL,
            TipoCampo.URL,
            TipoCampo.TELEFONO,
            TipoCampo.FECHA,
            TipoCampo.HORA,
            TipoCampo.LISTA,
            TipoCampo.NOTAS
        )
    }

    val colorDialogo = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(20.dp),
        containerColor = colorDialogo,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = "Nuevo campo personalizado",
                color = ColorTitulos,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nombre del campo
                CampoBoveda(
                    valor = nombre,
                    alCambiar = { nombre = it },
                    etiqueta = "Nombre (ej. CVV, Titular, IP...)"
                )

                // Selector de tipo de campo (Chips compactos)
                Text(
                    text = "Tipo de campo",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tiposDisponibles.forEach { tipo ->
                        val seleccionado = tipo == tipoSeleccionado
                        val colorFondo = if (seleccionado) ColorAcento else if (esOscuroActivo) Color(0xFF2A292E) else Color(0xFFEFEFF3)
                        val colorTexto = if (seleccionado) com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento else TextoPrincipal

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorFondo)
                                .clickable {
                                    tipoSeleccionado = tipo
                                    if (tipo == TipoCampo.PIN) {
                                        esSensible = true
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = colorTexto,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                            }
                            Text(
                                text = tipo.etiqueta,
                                color = colorTexto,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 12.sp,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                // Switch de Sensibilidad
                val fondoSensible = if (esOscuroActivo) Color(0xFF18171A) else Color(0xFFF4F4F6)
                val bordeSensible = if (esOscuroActivo) Color(0xFF333238) else Color(0xFFDFDFE3)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fondoSensible)
                        .border(1.dp, bordeSensible, RoundedCornerShape(12.dp))
                        .clickable { esSensible = !esSensible }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = null,
                            tint = if (esSensible) ColorAcento else TextoSecundario,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Dato sensible (Secreto)",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            )
                            Text(
                                text = "Ocultar por defecto (••••)",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                            )
                        }
                    }

                    SwitchBoveda(
                        checked = esSensible,
                        onCheckedChange = { esSensible = it }
                    )
                }
            }
        },
        confirmButton = {
            BotonColorido(
                texto = "Añadir",
                icono = Icons.Filled.Add,
                color = ColorAcento,
                alPulsar = {
                    if (nombre.isNotBlank()) {
                        alCrearCampo(
                            CampoPersonalizado(
                                etiqueta = nombre.trim(),
                                tipo = tipoSeleccionado,
                                esSensible = esSensible || tipoSeleccionado == TipoCampo.PIN
                            )
                        )
                        alDescartar()
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}
