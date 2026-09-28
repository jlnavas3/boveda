package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
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
        icono = Icons.Filled.Tune,
        colorIcono = ColorSeguridad,
        idGrupo = "05-COP-KIT-G02",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Configuración del contenido que se incluirá en el PDF/impresión",
        alRestablecer = {
            alCambiarIncluirContrasenas(false)
            alCambiarSoloFavoritos(false)
            alCambiarIncluirNotas(false)
        },
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Incluir contraseñas en claro",
            icono = null,
            activo = incluirContrasenas,
            idFila = "05-COP-KIT-PWD",
            mostrarId = mostrarIdsAjustes,
            colorActivo = Peligro,
            alCambiar = alCambiarIncluirContrasenas
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSwitch(
            titulo = "Solo cuentas favoritas / esenciales",
            icono = null,
            activo = soloFavoritos,
            idFila = "05-COP-KIT-FAV",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarSoloFavoritos
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSwitch(
            titulo = "Incluir notas seguras",
            icono = null,
            activo = incluirNotas,
            idFila = "05-COP-KIT-NTS",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarIncluirNotas
        )
    }
}
