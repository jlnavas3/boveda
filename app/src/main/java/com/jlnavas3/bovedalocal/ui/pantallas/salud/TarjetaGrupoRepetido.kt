package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Microcomponente contenedor de grupo de contraseñas repetidas.
 * Delega cada elemento a [FilaProblemaAgil].
 */
@Composable
fun TarjetaGrupoRepetido(
    grupo: List<Entrada>,
    totalEnGrupo: Int = grupo.size,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    colapsable: Boolean = true,
    expandido: Boolean = false,
    alAlternar: () -> Unit = {},
    alIgnorar: (Entrada) -> Unit = {},
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: ((String) -> Unit)? = null,
    alPulsarLargo: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val claveTitulo = grupo.firstOrNull()?.titulo?.ifBlank { "Clave repetida" } ?: "Clave repetida"
    ComponenteGrupoLista(
        clave = claveTitulo,
        entradas = grupo,
        expandido = expandido,
        alAlternar = alAlternar,
        modifier = modifier,
        contenidoEntrada = { entrada, _, _ ->
            FilaProblemaAgil(
                entrada = entrada,
                etiquetaDetalle = "Repetida",
                colorDetalle = Peligro,
                alCambiarRapido = { alCambiarClave(entrada) },
                alVerDetalle = { alVerDetalle(entrada.id) },
                ajustes = ajustes,
                alIgnorar = { alIgnorar(entrada) },
                mostrarIndicadores = mostrarIndicadores,
                enGrupo = true,
                seleccionActiva = seleccionActiva,
                seleccionado = seleccionados.contains(entrada.id),
                alAlternarSeleccion = { alAlternarSeleccion?.invoke(entrada.id) },
                alPulsarLargo = { alPulsarLargo?.invoke(entrada.id) }
            )
        }
    )
}
