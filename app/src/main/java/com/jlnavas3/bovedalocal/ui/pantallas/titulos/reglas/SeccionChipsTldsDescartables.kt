package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionChipsTldsDescartables(
    tlds: List<String>,
    alAgregarClick: () -> Unit,
    alEliminarTld: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Extensiones de Dominio (TLDs)",
        icono = Icons.Filled.Public,
        colorIcono = ColorAcento,
        idGrupo = "03-LST-RGL-G03",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Extensiones de dominio descartables que se recortarán automáticamente para mostrar un nombre limpio y amigable.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                tlds.forEach { tld ->
                    ChipTldDescartable(
                        tld = tld,
                        alEliminar = alEliminarTld
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .clickable { alAgregarClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar extensión",
                            tint = ColorAcento,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Agregar",
                            color = ColorAcento,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionChipsTldsDescartables() {
    BovedaTheme {
        SeccionChipsTldsDescartables(
            tlds = listOf("com", "net", "org", "co", "tech", "io"),
            alAgregarClick = {},
            alEliminarTld = {}
        )
    }
}
