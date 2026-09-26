package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.IlustracionVacio
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun EstadoVacioLista(
    entradasVacias: Boolean,
    alImportarCopia: () -> Unit,
    alImportarCsvGoogle: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (entradasVacias) {
            IlustracionVacio()
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = alImportarCopia,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ambar,
                    contentColor = ColorSobreAcento
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            ) {
                Icon(Icons.Filled.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Importar copia de seguridad")
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = alImportarCsvGoogle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ambar,
                    contentColor = ColorSobreAcento
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            ) {
                Icon(Icons.Filled.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Importar contraseñas de Google")
            }
        } else {
            Text(
                "Nada coincide con esa búsqueda",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}
