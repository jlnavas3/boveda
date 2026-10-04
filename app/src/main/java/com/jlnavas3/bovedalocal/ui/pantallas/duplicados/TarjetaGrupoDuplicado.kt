package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista

/**
 * Microcomponente contenedor de grupo de duplicados en acordeón expandible.
 * Delega la renderización de cada elemento a [FilaEntradaDuplicada].
 */
@Composable
fun TarjetaGrupoDuplicado(
    grupo: GrupoDuplicado,
    expandido: Boolean,
    alAlternar: () -> Unit,
    alConservar: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: (String) -> Unit = {},
    alPulsarLargo: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ComponenteGrupoLista(
        clave = grupo.claveVisual,
        entradas = grupo.entradas,
        expandido = expandido,
        alAlternar = alAlternar,
        modifier = modifier,
        contenidoEntrada = { entrada, _, _ ->
            FilaEntradaDuplicada(
                entrada = entrada,
                esSugerida = entrada.id == grupo.sugeridaPrincipal.id,
                alConservar = { alConservar(entrada) },
                alVerDetalle = { alVerDetalle(entrada.id) },
                ajustes = ajustes,
                mostrarIndicadores = mostrarIndicadores,
                enGrupo = true,
                seleccionActiva = seleccionActiva,
                seleccionado = seleccionados.contains(entrada.id),
                alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                alPulsarLargo = { alPulsarLargo(entrada.id) }
            )
        }
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaGrupoDuplicadoPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val entrada1 = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo
        val entrada2 = entrada1.copy(id = "mock-dup-2", titulo = "Google (Copia)")
        val grupo = GrupoDuplicado(
            idGrupo = "grupo-1",
            tipo = com.jlnavas3.bovedalocal.data.TipoDuplicado.IDENTICO,
            claveVisual = "google.com",
            entradas = listOf(entrada1, entrada2),
            sugeridaPrincipal = entrada1
        )
        TarjetaGrupoDuplicado(
            grupo = grupo,
            expandido = true,
            alAlternar = {},
            alConservar = {},
            alVerDetalle = {}
        )
    }
}
