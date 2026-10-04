package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo

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
