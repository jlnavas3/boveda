package com.jlnavas3.bovedalocal.ui.pantallas.csv

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoImportacionCsvGoogle(
    mostrarId: Boolean,
    alIniciarImportacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Importación",
        icono = Icons.Filled.FileDownload,
        colorIcono = Color(0xFF0288D1),
        idGrupo = "05-COP-CSV-G01",
        mostrarId = mostrarId,
        descripcion = "Carga de archivo 'Google Passwords.csv' desde el almacenamiento",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Puedes exportar tu archivo CSV 'Google Passwords.csv' desde el administrador de contraseñas de Google (https://passwords.google.com/) e importarlo directamente aquí.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        SeparadorFilaSimple()
        ComponenteBotonFila(
            titulo = "Importar contraseñas de Google",
            colorIcono = ColorExportacion,
            icono = Icons.Filled.FileDownload,
            idFila = "05-COP-CSV-IMP",
            mostrarId = mostrarId,
            alPulsar = alIniciarImportacion
        )
    }
}
