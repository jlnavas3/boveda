package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun GrupoOpcionesDocumento(
    mostrarIdsAjustes: Boolean,
    incluirContrasenas: Boolean,
    soloFavoritos: Boolean,
    incluirNotas: Boolean,
    alCambiarIncluirContrasenas: (Boolean) -> Unit,
    alCambiarSoloFavoritos: (Boolean) -> Unit,
    alCambiarIncluirNotas: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Opciones del documento",
        idGrupo = "02.3.G2",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Configuración del contenido que se incluirá en el PDF/impresión",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Incluir contraseñas en claro",
            icono = Icons.Filled.Shield,
            colorIcono = if (incluirContrasenas) Peligro else ColorAcento,
            activo = incluirContrasenas,
            idFila = "02.3.1",
            mostrarId = mostrarIdsAjustes,
            colorActivo = Peligro,
            alCambiar = alCambiarIncluirContrasenas
        )
        ComponenteSeparador()
        ComponenteSwitch(
            titulo = "Solo cuentas favoritas / esenciales",
            icono = Icons.Filled.Description,
            colorIcono = ColorSeguridad,
            activo = soloFavoritos,
            idFila = "02.3.2",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarSoloFavoritos
        )
        ComponenteSeparador()
        ComponenteSwitch(
            titulo = "Incluir notas seguras",
            icono = Icons.Filled.Description,
            colorIcono = ColorIconosInternos,
            activo = incluirNotas,
            idFila = "02.3.3",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarIncluirNotas
        )
    }
}
