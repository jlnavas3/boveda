package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.Color2FA

@Composable
fun GrupoAccesosRapidosTotp(
    mostrarId: Boolean,
    alNavegarAjustesWidget: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Accesos rápidos",
        icono = Icons.Filled.Widgets,
        colorIcono = Color2FA,
        idGrupo = "04.1.G2",
        mostrarId = mostrarId,
        descripcion = "Atajos de integración directa en pantalla",
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Personalizar widgets de escritorio",
            icono = null,
            idFila = "04.1.5",
            mostrarId = mostrarId,
            alPulsar = alNavegarAjustesWidget
        )
    }
}
