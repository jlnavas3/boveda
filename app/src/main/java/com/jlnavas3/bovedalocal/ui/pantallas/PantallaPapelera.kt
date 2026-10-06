package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.BarraSuperiorPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.ContenidoListaPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogoAjustesRetencionPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.papelera.DialogosPapelera
import com.jlnavas3.bovedalocal.ui.theme.calcularEspaciadoFilas
import com.jlnavas3.bovedalocal.util.construirItemsAgrupadosPorTitulo

@Composable
fun PantallaPapelera(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val entradasActivas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var mostrarDialogoRetencion by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }
    var conflictoRestaurar by remember { mutableStateOf<Entrada?>(null) }
    var gruposExpandidos by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val espaciadoFilas = calcularEspaciadoFilas(ajustes.densidadLista)

    val ordenadas = remember(papelera) { papelera.sortedByDescending { it.eliminadaEn } }
    val itemsAgrupados = remember(ordenadas, ajustes.agruparPorSitio) {
        construirItemsAgrupadosPorTitulo(
            entradas = ordenadas,
            agrupar = ajustes.agruparPorSitio,
            expandido = { false }
        )
    }

    val alRestaurarEntrada = { entrada: Entrada ->
        val conflicto = entradasActivas.find {
            it.id == entrada.id || (it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && it.usuario.trim() == entrada.usuario.trim())
        }
        if (conflicto != null) {
            conflictoRestaurar = entrada
        } else {
            vm.restaurarDeLaPapelera(entrada.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPapelera(
            tieneElementos = papelera.isNotEmpty(),
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            alConfirmarVaciar = { confirmarVaciar = true },
            alIrADisenoLista = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES-GRP")) },
            alIrAAutodestruccion = { vm.ir(Pantalla.AjustesAutodestruccion("01-SEG-DES")) },
            alCambiarRetencion = { mostrarDialogoRetencion = true }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (papelera.isEmpty()) {
                    "Sin elementos en la papelera"
                } else if (ajustes.diasRetencionPapelera <= 0) {
                    "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Sin expiración automática"
                } else {
                    "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Se borran tras ${ajustes.diasRetencionPapelera} días"
                }
            )

            Spacer(Modifier.height(10.dp))

            ContenidoListaPapelera(
                papelera = papelera,
                ordenadas = ordenadas,
                itemsAgrupados = itemsAgrupados,
                gruposExpandidos = gruposExpandidos,
                ajustes = ajustes,
                ahora = ahora,
                espaciadoFilas = espaciadoFilas,
                alAlternarGrupo = { clave ->
                    gruposExpandidos = if (gruposExpandidos.contains(clave)) {
                        gruposExpandidos - clave
                    } else {
                        gruposExpandidos + clave
                    }
                },
                alRestaurar = alRestaurarEntrada,
                alBorrarDefinitivo = { aBorrarDefinitivo = it },
                modifier = Modifier.weight(1f)
            )
        }
    }

    DialogosPapelera(
        confirmarVaciar = confirmarVaciar,
        cantidadPapelera = papelera.size,
        aBorrarDefinitivo = aBorrarDefinitivo,
        conflictoRestaurar = conflictoRestaurar,
        alDescartarVaciar = { confirmarVaciar = false },
        alConfirmarVaciar = {
            confirmarVaciar = false
            vm.vaciarPapelera()
            vm.avisar("Papelera vaciada")
        },
        alDescartarBorrarDefinitivo = { aBorrarDefinitivo = null },
        alConfirmarBorrarDefinitivo = { entrada ->
            val id = entrada.id
            aBorrarDefinitivo = null
            vm.borrarDefinitivamente(id)
            vm.avisar("Entrada destruida")
        },
        alDescartarConflicto = { conflictoRestaurar = null },
        alSustituirConflicto = { entrada ->
            val id = entrada.id
            conflictoRestaurar = null
            vm.restaurarDeLaPapelera(id, sustituir = true)
        },
        alDuplicarConflicto = { entrada ->
            val id = entrada.id
            conflictoRestaurar = null
            vm.restaurarDeLaPapelera(id, sustituir = false)
        }
    )

    if (mostrarDialogoRetencion) {
        DialogoAjustesRetencionPapelera(
            diasActuales = ajustes.diasRetencionPapelera,
            alDescartar = { mostrarDialogoRetencion = false },
            alSeleccionarDias = { dias ->
                vm.ajustarDiasRetencionPapelera(dias)
                mostrarDialogoRetencion = false
                val desc = if (dias <= 0) "Sin expiración automática" else "Retención: $dias días"
                vm.avisar(desc)
            }
        )
    }
}
