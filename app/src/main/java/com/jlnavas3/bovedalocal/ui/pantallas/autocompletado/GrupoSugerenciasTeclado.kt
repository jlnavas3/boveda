package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoSugerenciasTeclado(
    sugerenciasTeclado: Boolean,
    maxSugerencias: Int,
    mostrarIdsAjustes: Boolean,
    alCambiarSugerenciasTeclado: (Boolean) -> Unit,
    alCambiarMaxSugerencias: (Int) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val opcionesMax = AlmacenAjustes.OPCIONES_MAX_SUGERENCIAS_AUTOFILL.map { (valor, etiqueta) ->
        val desc = when (valor) {
            0 -> "Muestra todas las cuentas que coincidan sin limitar la cantidad."
            3 -> "Muestra un máximo de 3 credenciales para mantener la barra compacta."
            5 -> "Muestra hasta 5 credenciales (recomendado para la mayoría de pantallas)."
            10 -> "Muestra hasta 10 credenciales antes de requerir búsqueda manual."
            else -> "Muestra hasta $valor credenciales."
        }
        OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.FilterList)
    }

    ComponenteGrupo(
        etiqueta = "Sugerencias y Presentación",
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
            text = "Muestra sugerencias de contraseñas y llaves como chips interactivos en la barra superior del teclado (Android 11+). Si se desactiva, se usará únicamente el menú desplegable flotante clásico sobre los campos.",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        ComponenteSelectorModal(
            titulo = "Límite de sugerencias simultáneas",
            valorSeleccionado = maxSugerencias,
            opciones = opcionesMax,
            alSeleccionar = alCambiarMaxSugerencias,
            icono = Icons.Filled.FilterList,
            colorIcono = Color(0xFF3B82F6),
            idFila = "04-HER-PSK-MAX",
            mostrarId = mostrarIdsAjustes
        )
    }
}

@BovedaPreview
@Composable
private fun PreviaGrupoSugerenciasTeclado() {
    BovedaTheme {
        GrupoSugerenciasTeclado(
            sugerenciasTeclado = true,
            maxSugerencias = 5,
            mostrarIdsAjustes = false,
            alCambiarSugerenciasTeclado = {},
            alCambiarMaxSugerencias = {},
            alRestablecer = {}
        )
    }
}
