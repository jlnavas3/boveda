package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Sección de campos adicionales personalizados dentro de la pantalla de edición de entradas.
 */
@Composable
fun SeccionCamposPersonalizados(
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    etiquetasBase: Set<String> = emptySet(),
    ajustes: AjustesApp = AjustesApp(),
    haptica: Haptica
) {
    var mostrandoDialogoNuevoCampo by remember { mutableStateOf(false) }
    var mostrandoDialogoPresets by remember { mutableStateOf(false) }

    val camposVisibles = remember(camposPersonalizados, etiquetasBase) {
        if (etiquetasBase.isEmpty()) camposPersonalizados
        else camposPersonalizados.filterNot { campo ->
            etiquetasBase.any { base -> base.equals(campo.etiqueta, ignoreCase = true) }
        }
    }

    if (camposVisibles.isNotEmpty()) {
        camposVisibles.forEachIndexed { indice, campo ->
            TarjetaCampoPersonalizadoEdicion(
                numero = indice + 1,
                campo = campo,
                alModificar = { modificado ->
                    val actualizados = camposPersonalizados.toMutableList()
                    val idx = actualizados.indexOfFirst { it.id == campo.id }
                    if (idx >= 0) {
                        actualizados[idx] = modificado
                    }
                    alCambiarCampos(actualizados)
                },
                alEliminar = {
                    haptica.tic()
                    alCambiarCampos(camposPersonalizados.filterNot { it.id == campo.id })
                },
                ajustes = ajustes
            )
            Spacer(Modifier.height(10.dp))
        }
    }

    if (etiquetasBase.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonBorde(
                texto = "Añadir campo",
                icono = Icons.Filled.Add,
                modifier = Modifier.weight(1f),
                alPulsar = {
                    haptica.tic()
                    mostrandoDialogoNuevoCampo = true
                }
            )
            BotonColorido(
                texto = "Presets",
                icono = Icons.Filled.DynamicForm,
                color = ColorAcento,
                modifier = Modifier.weight(1f),
                alPulsar = {
                    haptica.tic()
                    mostrandoDialogoPresets = true
                }
            )
        }
    } else {
        BotonBorde(
            texto = "Añadir campo adicional",
            icono = Icons.Filled.Add,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = {
                haptica.tic()
                mostrandoDialogoNuevoCampo = true
            }
        )
    }

    if (mostrandoDialogoNuevoCampo) {
        DialogoNuevoCampo(
            alDescartar = { mostrandoDialogoNuevoCampo = false },
            alCrearCampo = { nuevoCampo ->
                alCambiarCampos(camposPersonalizados + nuevoCampo)
            }
        )
    }

    if (mostrandoDialogoPresets) {
        DialogoPresetsRapidos(
            alDescartar = { mostrandoDialogoPresets = false },
            alSeleccionarPreset = { camposPreset ->
                alCambiarCampos(camposPersonalizados + camposPreset)
            }
        )
    }
}
