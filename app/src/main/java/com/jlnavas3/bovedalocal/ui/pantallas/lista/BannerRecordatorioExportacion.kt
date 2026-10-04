package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.RecordatorioExportacionInfo
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Banner de advertencia que recuerda al usuario realizar una copia de seguridad
 * periódica cuando han pasado varios días desde el último respaldo.
 */
@Composable
fun BannerRecordatorioExportacion(
    info: RecordatorioExportacionInfo,
    alIr: () -> Unit
) {
    ContenedorTarjeta(
        onClick = alIr,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContenedorIconoInsignia(
                icono = Icons.Filled.Backup,
                color = Color(0xFFFB8C00),
                tamano = TamanoInsignia.MEDIANO
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                TextoTitulo(
                    texto = info.titulo,
                    estilo = EstiloTitulo.PEQUENO
                )
                Spacer(Modifier.height(2.dp))
                TextoSubtitulo(
                    texto = info.descripcion
                )
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
