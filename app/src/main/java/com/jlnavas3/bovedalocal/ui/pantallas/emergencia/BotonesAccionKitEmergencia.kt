package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun BotonesAccionKitEmergencia(
    alImprimirPdf: () -> Unit,
    alCopiarTexto: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Acciones del documento",
        icono = Icons.Filled.Print,
        colorIcono = ColorSeguridad,
        idGrupo = "05-COP-KIT-G04",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Imprimir / Exportar documento PDF",
            subtitulo = "Enviar a impresora o guardar archivo PDF local",
            icono = null,
            idFila = "05-COP-KIT-ACT-PRN",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alImprimirPdf
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteNavegacion(
            titulo = "Copiar texto al portapapeles",
            subtitulo = "Copiar contenido completo en formato texto",
            icono = null,
            idFila = "05-COP-KIT-ACT-CPY",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alCopiarTexto
        )
    }
}
