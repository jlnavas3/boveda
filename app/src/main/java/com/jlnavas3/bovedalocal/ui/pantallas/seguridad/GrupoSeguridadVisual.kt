package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun GrupoSeguridadVisual(
    seguridadVisualActiva: Boolean,
    estiloOcultamientoVisual: String,
    tiempoAutoOcultarSegundos: Int,
    ocultarUsuario: Boolean,
    ocultarContrasena: Boolean,
    ocultarTotp: Boolean,
    ocultarNotas: Boolean,
    ocultarCampos: Boolean,
    mostrarIdsAjustes: Boolean,
    alCambiarSeguridadVisualActiva: (Boolean) -> Unit,
    alCambiarEstiloOcultamiento: (String) -> Unit,
    alCambiarTiempoAutoOcultar: (Int) -> Unit,
    alCambiarOcultarUsuario: (Boolean) -> Unit,
    alCambiarOcultarContrasena: (Boolean) -> Unit,
    alCambiarOcultarTotp: (Boolean) -> Unit,
    alCambiarOcultarNotas: (Boolean) -> Unit,
    alCambiarOcultarCampos: (Boolean) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Seguridad visual",
        icono = Icons.Filled.VisibilityOff,
        colorIcono = ColorSeguridad,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-VIS-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Privacidad de pantalla",
            activo = seguridadVisualActiva,
            alCambiar = alCambiarSeguridadVisualActiva,
            idFila = "01-SEG-VIS-ACT",
            mostrarId = mostrarIdsAjustes
        )

        if (seguridadVisualActiva) {
            val opcionesEstilo = remember {
                AlmacenAjustes.OPCIONES_ESTILO_OCULTAMIENTO.map { (valor, etiqueta) ->
                    val desc = when (valor) {
                        "desenfoque" -> "Efecto vidrio esmerilado con revelado animado suave"
                        "puntos_fijos" -> "Longitud fija sin revelar número de caracteres"
                        else -> "Glifos estándar proporcionales a la longitud"
                    }
                    val icono = when (valor) {
                        "desenfoque" -> Icons.Filled.BlurOn
                        "puntos_fijos" -> Icons.Filled.MoreHoriz
                        else -> Icons.Filled.Pin
                    }
                    OpcionSelectorModal(valor, etiqueta, etiqueta, desc, icono)
                }
            }

            ComponenteSelectorModal(
                titulo = "Estilo de ocultamiento",
                descripcionModal = "Elige la apariencia visual aplicada a los campos protegidos",
                icono = null,
                idFila = "01-SEG-VIS-EST",
                mostrarId = mostrarIdsAjustes,
                valorSeleccionado = estiloOcultamientoVisual,
                opciones = opcionesEstilo,
                alSeleccionar = alCambiarEstiloOcultamiento
            )

            val opcionesTiempo = remember {
                AlmacenAjustes.OPCIONES_TIEMPO_AUTO_OCULTAR.map { (valor, etiqueta) ->
                    val desc = when (valor) {
                        0 -> "El dato se mantiene visible hasta volver a presionar el icono"
                        10 -> "Tiempo recomendado para consulta rápida"
                        else -> "Vuelve a ocultar automáticamente tras $etiqueta"
                    }
                    OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.Timer)
                }
            }

            ComponenteSelectorModal(
                titulo = "Auto-ocultar tras",
                descripcionModal = "Tiempo límite que un dato permanecerá visible tras presionar el ojo",
                icono = null,
                idFila = "01-SEG-VIS-TMP",
                mostrarId = mostrarIdsAjustes,
                valorSeleccionado = tiempoAutoOcultarSegundos,
                opciones = opcionesTiempo,
                alSeleccionar = alCambiarTiempoAutoOcultar
            )

            ComponenteSwitch(
                titulo = "Ocultar usuario o correo",
                activo = ocultarUsuario,
                alCambiar = alCambiarOcultarUsuario,
                idFila = "01-SEG-VIS-USR",
                mostrarId = mostrarIdsAjustes
            )

            ComponenteSwitch(
                titulo = "Ocultar contraseñas",
                activo = ocultarContrasena,
                alCambiar = alCambiarOcultarContrasena,
                idFila = "01-SEG-VIS-PWD",
                mostrarId = mostrarIdsAjustes
            )

            ComponenteSwitch(
                titulo = "Ocultar códigos 2FA (TOTP)",
                activo = ocultarTotp,
                alCambiar = alCambiarOcultarTotp,
                idFila = "01-SEG-VIS-OTP",
                mostrarId = mostrarIdsAjustes
            )

            ComponenteSwitch(
                titulo = "Ocultar notas seguras",
                activo = ocultarNotas,
                alCambiar = alCambiarOcultarNotas,
                idFila = "01-SEG-VIS-NOT",
                mostrarId = mostrarIdsAjustes
            )

            ComponenteSwitch(
                titulo = "Ocultar campos personalizados",
                activo = ocultarCampos,
                alCambiar = alCambiarOcultarCampos,
                idFila = "01-SEG-VIS-CMP",
                mostrarId = mostrarIdsAjustes
            )
        }
    }
}
