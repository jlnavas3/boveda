package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.Extension
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
 * Sección de campos adicionales personalizados dentro de la pantalla de edición de entradas,
 * presentada como fila colapsable homogénea.
 */
@Composable
fun SeccionCamposPersonalizados(
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    etiquetasBase: Set<String> = emptySet(),
    ajustes: AjustesApp = AjustesApp(),
    haptica: Haptica,
    alGuardarPlantilla: ((com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada) -> Unit)? = null,
    alEliminarPlantilla: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var mostrandoDialogoNuevoCampo by remember { mutableStateOf(false) }
    var mostrandoDialogoPresets by remember { mutableStateOf(false) }
    var mostrandoDialogoGuardarPlantilla by remember { mutableStateOf(false) }

    val camposVisibles = remember(camposPersonalizados, etiquetasBase) {
        if (etiquetasBase.isEmpty()) camposPersonalizados
        else camposPersonalizados.filterNot { campo ->
            etiquetasBase.any { base -> base.equals(campo.etiqueta, ignoreCase = true) }
        }
    }

    var expandido by remember { mutableStateOf(camposVisibles.isNotEmpty()) }

    val tituloSeccion = if (etiquetasBase.isEmpty()) "Campos personalizados" else "Campos adicionales"
    val resumenSeccion = if (camposVisibles.isEmpty()) "Sin campos adicionales" else "${camposVisibles.size} configurado(s)"

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.Extension,
        colorIcono = ColorAcento,
        titulo = tituloSeccion,
        resumen = resumenSeccion,
        insigniaTexto = if (camposVisibles.isNotEmpty()) "${camposVisibles.size}" else null,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonBorde(
            texto = if (etiquetasBase.isEmpty()) "Añadir campo" else "Añadir campo adicional",
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

        if (camposPersonalizados.isNotEmpty() && alGuardarPlantilla != null) {
            Spacer(Modifier.height(8.dp))
            BotonBorde(
                texto = "Guardar como plantilla (${camposPersonalizados.size} campos)",
                icono = Icons.Filled.BookmarkAdd,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = {
                    haptica.tic()
                    mostrandoDialogoGuardarPlantilla = true
                }
            )
        }
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
            plantillasPersonalizadas = ajustes.plantillasCamposPersonalizadas,
            camposActuales = camposPersonalizados,
            alDescartar = { mostrandoDialogoPresets = false },
            alSeleccionarPreset = { camposPreset ->
                alCambiarCampos(camposPersonalizados + camposPreset)
            },
            alGuardarComoPlantilla = alGuardarPlantilla,
            alEliminarPlantilla = alEliminarPlantilla
        )
    }

    if (mostrandoDialogoGuardarPlantilla && alGuardarPlantilla != null) {
        DialogoGuardarPlantillaCampos(
            campos = camposPersonalizados,
            alDescartar = { mostrandoDialogoGuardarPlantilla = false },
            alGuardar = { nuevaPlantilla ->
                alGuardarPlantilla(nuevaPlantilla)
                mostrandoDialogoGuardarPlantilla = false
            }
        )
    }
}
