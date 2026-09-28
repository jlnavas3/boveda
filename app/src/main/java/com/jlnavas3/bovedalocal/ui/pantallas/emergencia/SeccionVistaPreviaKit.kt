package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun SeccionVistaPreviaKit(
    textoPreview: String,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Vista previa del documento",
        icono = Icons.Filled.Description,
        colorIcono = ColorIconosInternos,
        idGrupo = "05-COP-KIT-G03",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Previsualización del formato de texto impreso",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(Superficie)
                    .then(
                        if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                        else Modifier
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(14.dp)
            ) {
                Text(
                    text = textoPreview,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                )
            }
        }
    }
}
