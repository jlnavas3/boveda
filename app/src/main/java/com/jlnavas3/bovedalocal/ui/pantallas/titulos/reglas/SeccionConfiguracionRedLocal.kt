package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SeccionConfiguracionRedLocal(
    plantillaRouter: String,
    plantillaServidor: String,
    alCambiarPlantillaRouter: (String) -> Unit,
    alCambiarPlantillaServidor: (String) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Redes Privadas e IPs (RFC 1918)",
        icono = Icons.Filled.Router,
        colorIcono = ColorAcento,
        idGrupo = "03-LST-RGL-G02",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Formato automático para direcciones IP privadas (192.168.*, 10.*, 172.16-31.*, localhost). Usa '{ip}' como comodín.",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = plantillaRouter,
                alCambiar = alCambiarPlantillaRouter,
                etiqueta = "Routers y Puertas de Enlace (.1 / .254)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            CampoBoveda(
                valor = plantillaServidor,
                alCambiar = alCambiarPlantillaServidor,
                etiqueta = "Servidores y Equipos Locales",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaSeccionConfiguracionRedLocal() {
    BovedaTheme {
        SeccionConfiguracionRedLocal(
            plantillaRouter = "Router ({ip})",
            plantillaServidor = "Servidor ({ip})",
            alCambiarPlantillaRouter = {},
            alCambiarPlantillaServidor = {}
        )
    }
}
