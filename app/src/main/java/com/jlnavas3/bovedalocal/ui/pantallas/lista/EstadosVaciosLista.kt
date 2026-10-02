package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.IlustracionVacio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun EstadoVacioLista(
    entradasVacias: Boolean,
    alImportarCopia: () -> Unit,
    alImportarCsvGoogle: () -> Unit,
    alImportarGoogleAuthenticator: () -> Unit,
    alImportarDirectoCxf: () -> Unit = {}
) {
    if (entradasVacias) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))
            IlustracionVacio(tamanoLupa = 115.dp)
            Spacer(Modifier.height(24.dp))

            ComponenteGrupo(
                etiqueta = "Importar",
                icono = Icons.Filled.FileDownload,
                colorIcono = ColorExportacion
            ) {
                ComponenteNavegacion(
                    titulo = "Copia de seguridad",
                    icono = Icons.Filled.Backup,
                    colorIcono = Color(0xFF26A69A),
                    alPulsar = alImportarCopia
                )
                ComponenteSeparador(sangriaInicio = 68.dp)
                ComponenteNavegacion(
                    titulo = "Contraseñas de Google",
                    icono = Icons.Filled.VpnKey,
                    colorIcono = Color(0xFF4285F4),
                    alPulsar = alImportarCsvGoogle
                )
                ComponenteSeparador(sangriaInicio = 68.dp)
                ComponenteNavegacion(
                    titulo = "Verificación en dos pasos de Google",
                    icono = Icons.Filled.QrCodeScanner,
                    colorIcono = Color(0xFFEA4335),
                    alPulsar = alImportarGoogleAuthenticator
                )
                ComponenteSeparador(sangriaInicio = 68.dp)
                ComponenteNavegacion(
                    titulo = "Importación directa de llaves de paso y contraseñas",
                    icono = androidx.compose.material.icons.Icons.Filled.VpnKey,
                    colorIcono = com.jlnavas3.bovedalocal.ui.theme.Ambar,
                    alPulsar = alImportarDirectoCxf
                )
            }

            Spacer(Modifier.height(88.dp))
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Nada coincide con esa búsqueda",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun EstadoVacioListaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        EstadoVacioLista(
            entradasVacias = true,
            alImportarCopia = {},
            alImportarCsvGoogle = {},
            alImportarGoogleAuthenticator = {},
            alImportarDirectoCxf = {}
        )
    }
}

