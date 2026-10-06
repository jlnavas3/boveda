package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoReglasDeteccionAutofill(
    mostrarIdsAjustes: Boolean,
    alAbrirReglas: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Reglas y Detección de Campos",
        icono = Icons.Filled.Tune,
        colorIcono = ColorAcento,
        idGrupo = "04-HER-PSK-G03",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Palabras clave de formularios",
            subtitulo = "Personalizar términos de usuario, contraseña y 2FA/OTP",
            icono = Icons.Filled.Tune,
            colorIcono = ColorAcento,
            idFila = "04-HER-PSK-RGL",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alAbrirReglas
        )
        Text(
            text = "Permite que Bóveda local detecte campos de acceso en webs o aplicaciones bancarias e institucionales con términos regionales (cédula, DNI, RUC, NIP).",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@BovedaPreview
@Composable
private fun PreviaGrupoReglasDeteccionAutofill() {
    BovedaTheme {
        GrupoReglasDeteccionAutofill(
            mostrarIdsAjustes = false,
            alAbrirReglas = {}
        )
    }
}
