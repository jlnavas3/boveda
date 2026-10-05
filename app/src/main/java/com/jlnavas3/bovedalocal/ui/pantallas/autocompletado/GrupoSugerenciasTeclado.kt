package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoSugerenciasTeclado(
    sugerenciasTeclado: Boolean,
    mostrarIdsAjustes: Boolean,
    alCambiarSugerenciasTeclado: (Boolean) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Sugerencias en teclado",
        icono = Icons.Filled.Keyboard,
        colorIcono = Color(0xFF3B82F6),
        alRestablecer = alRestablecer,
        idGrupo = "04-HER-PSK-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Sugerencias en el teclado",
            activo = sugerenciasTeclado,
            alCambiar = alCambiarSugerenciasTeclado,
            icono = Icons.Filled.Keyboard,
            colorIcono = Color(0xFF3B82F6),
            idFila = "04-HER-PSK-SUG",
            mostrarId = mostrarIdsAjustes
        )
        Text(
            text = "Muestra sugerencias de contraseñas y llaves como chips interactivos en la barra superior del teclado (Android 11+). Si se desactiva, se usará únicamente el menú desplegable flotante clásico sobre los campos (el teclado en pantalla no se mostrará automáticamente).",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
