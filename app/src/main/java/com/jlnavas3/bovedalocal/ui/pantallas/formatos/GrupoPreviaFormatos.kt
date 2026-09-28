package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoPreviaFormatos(
    mostrarIdsAjustes: Boolean,
    ejemploFecha: String,
    ejemploHora: String,
    ejemploTel: String,
    ejemploDecimal: String,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Vista previa",
        icono = Icons.Filled.Visibility,
        colorIcono = Color(0xFFFFA000),
        idGrupo = "03-LST-FMT-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaEjemploChip(
                    etiqueta = "Fecha",
                    valor = ejemploFecha,
                    modifier = Modifier.weight(1f)
                )
                FilaEjemploChip(
                    etiqueta = "Hora",
                    valor = ejemploHora,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaEjemploChip(
                    etiqueta = "Teléfono",
                    valor = ejemploTel,
                    modifier = Modifier.weight(1.3f)
                )
                FilaEjemploChip(
                    etiqueta = "Decimal",
                    valor = ejemploDecimal,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
    }
}

@Composable
fun FilaEjemploChip(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(FormaPequena)
            .background(SuperficieAlta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = etiqueta,
                color = TextoSecundario,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = valor,
                color = TextoPrincipal,
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
