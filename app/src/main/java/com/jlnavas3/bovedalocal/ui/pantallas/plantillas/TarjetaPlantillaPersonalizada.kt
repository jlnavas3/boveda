package com.jlnavas3.bovedalocal.ui.pantallas.plantillas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TarjetaPlantillaPersonalizada(
    plantilla: PlantillaCamposPersonalizada,
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = ColorCampoAjustes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(fondo)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                } else Modifier
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(FormaPequena)
                    .background(ColorAcento),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.FolderSpecial,
                    contentDescription = null,
                    tint = ColorSobreAcento,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plantilla.titulo,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
                if (plantilla.descripcion.isNotBlank()) {
                    Text(
                        text = plantilla.descripcion,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }
            }

            IconButton(
                onClick = alEditar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar plantilla",
                    tint = ColorAcento,
                    modifier = Modifier.size(19.dp)
                )
            }

            IconButton(
                onClick = alEliminar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = "Eliminar plantilla",
                    tint = Peligro,
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "${plantilla.campos.size} campos incluidos:",
            color = TextoSecundario,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
        )

        Spacer(Modifier.height(6.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            plantilla.campos.forEach { campo ->
                Row(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(SuperficieAlta)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (campo.esSensibleEfectivo) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        text = campo.etiqueta.ifBlank { "Sin etiqueta" },
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.5.sp
                        )
                    )
                    Text(
                        text = " • ${campo.tipo.etiqueta}",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                    )
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun TarjetaPlantillaPersonalizadaPreview() {
    TarjetaPlantillaPersonalizada(
        plantilla = PlantillaCamposPersonalizada(
            titulo = "Servidor Proxmox VE",
            descripcion = "Credenciales y tokens de acceso",
            campos = listOf(
                CampoPersonalizado(etiqueta = "Host / IP", tipo = TipoCampo.TEXTO),
                CampoPersonalizado(etiqueta = "Puerto", tipo = TipoCampo.NUMERO),
                CampoPersonalizado(etiqueta = "Token Secreto", tipo = TipoCampo.TEXTO, esSensible = true)
            )
        ),
        alEditar = {},
        alEliminar = {}
    )
}
