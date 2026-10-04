package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat

/**
 * Microcomponente que renderiza el scroll principal de la lista de Passkeys,
 * incluyendo la descripción del conteo, estado vacío o las filas individuales.
 */
@Composable
fun ContenidoListaPasskeys(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    passkeysFiltradas: List<Entrada>,
    todasLasPasskeys: List<Entrada>,
    espaciadoFilas: Dp,
    formato: SimpleDateFormat,
    haptica: Haptica,
    modoSeleccion: Boolean,
    seleccionados: Set<String>,
    ajustes: AjustesApp,
    alImportarPasskeys: () -> Unit,
    alPulsarLargo: (String) -> Unit,
    alAlternarSeleccion: (String) -> Unit,
    alPulsarEntrada: (Entrada) -> Unit,
    alAlternarFavorito: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        DescripcionPantalla(
            subtitulo = if (passkeysFiltradas.isEmpty()) {
                "Sin llaves registradas"
            } else {
                "${passkeysFiltradas.size} llave${if (passkeysFiltradas.size == 1) "" else "s"} de paso almacenada${if (passkeysFiltradas.size == 1) "" else "s"}"
            }
        )

        Spacer(Modifier.height(10.dp))

        if (passkeysFiltradas.isEmpty()) {
            EstadoVacioPasskeys(
                sinPasskeysEnTotal = todasLasPasskeys.isEmpty(),
                alImportarPasskeys = alImportarPasskeys
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
            ) {
                passkeysFiltradas.forEach { entrada ->
                    FilaPasskey(
                        entrada = entrada,
                        formato = formato,
                        haptica = haptica,
                        seleccionActiva = modoSeleccion,
                        seleccionado = seleccionados.contains(entrada.id),
                        alPulsarLargo = { alPulsarLargo(entrada.id) },
                        alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                        alPulsar = { alPulsarEntrada(entrada) },
                        alAlternarFavorito = { alAlternarFavorito(entrada.id) },
                        ajustes = ajustes,
                        mostrarIndicadores = ajustes.mostrarIndicadoresContenido
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
