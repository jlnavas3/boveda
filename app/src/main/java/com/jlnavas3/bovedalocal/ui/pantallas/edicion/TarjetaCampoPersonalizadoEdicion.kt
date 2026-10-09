package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo

import com.jlnavas3.bovedalocal.ui.componentes.blindajeSemanticoSensible

/**
 * Fila compacta de edición para un campo personalizado presentada en una sola línea homogénea:
 * el campo muestra su etiqueta y valor directamente, con acciones de configuración (lápiz)
 * y eliminación (papelera) a la derecha.
 */
@Composable
fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit,
    ajustes: AjustesApp = AjustesApp(),
    modifier: Modifier = Modifier
) {
    var mostrarValor by remember { mutableStateOf(false) }
    var mostrandoDialogoConfiguracion by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .blindajeSemanticoSensible(esSensible, campo.etiqueta.ifBlank { "Campo #$numero" })
        ) {
            EntradaValorCampoPersonalizado(
                campo = campo,
                esSensible = esSensible,
                mostrarValor = mostrarValor,
                alAlternarMostrarValor = { mostrarValor = !mostrarValor },
                alModificar = alModificar,
                ajustes = ajustes,
                etiquetaPersonalizada = campo.etiqueta.ifBlank { "Campo #$numero" }
            )
        }

        AccionesCampoPersonalizadoCompacto(
            alEditar = { mostrandoDialogoConfiguracion = true },
            alEliminar = alEliminar
        )
    }

    if (mostrandoDialogoConfiguracion) {
        DialogoConfigurarCampoPersonalizado(
            campo = campo,
            alDescartar = { mostrandoDialogoConfiguracion = false },
            alConfirmar = { modificado ->
                alModificar(modificado)
                mostrandoDialogoConfiguracion = false
            }
        )
    }
}
