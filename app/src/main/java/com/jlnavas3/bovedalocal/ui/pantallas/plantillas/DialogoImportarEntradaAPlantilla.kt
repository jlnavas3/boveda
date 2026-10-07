package com.jlnavas3.bovedalocal.ui.pantallas.plantillas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoImportarEntradaAPlantilla(
    entradasConCampos: List<Entrada>,
    alDescartar: () -> Unit,
    alSeleccionarEntrada: (PlantillaCamposPersonalizada) -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Crear desde entrada existente",
        icono = Icons.Filled.FileDownload,
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Selecciona una cuenta de tu bóveda para convertir sus campos personalizados en una nueva plantilla reutilizable:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

            Spacer(Modifier.height(4.dp))

            if (entradasConCampos.isEmpty()) {
                Text(
                    text = "No tienes cuentas en la bóveda con campos personalizados adicionales aún.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                entradasConCampos.forEach { entrada ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaCampo)
                            .background(ColorCampoAjustes)
                            .clickable {
                                alSeleccionarEntrada(
                                    PlantillaCamposPersonalizada(
                                        titulo = "Plantilla: ${entrada.titulo}",
                                        descripcion = "Creada a partir de '${entrada.titulo}'",
                                        campos = entrada.camposPersonalizados
                                    )
                                )
                                alDescartar()
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entrada.titulo,
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            val nombresCampos = entrada.camposPersonalizados.joinToString(", ") { it.etiqueta.ifBlank { "Campo" } }
                            Text(
                                text = "${entrada.camposPersonalizados.size} campos: $nombresCampos",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun DialogoImportarEntradaAPlantillaPreview() {
    DialogoImportarEntradaAPlantilla(
        entradasConCampos = listOf(
            Entrada(
                id = "preview-1",
                titulo = "Mi Servidor VPS",
                camposPersonalizados = listOf(
                    CampoPersonalizado(etiqueta = "IP Pública"),
                    CampoPersonalizado(etiqueta = "Puerto SSH")
                )
            )
        ),
        alDescartar = {},
        alSeleccionarEntrada = {}
    )
}
