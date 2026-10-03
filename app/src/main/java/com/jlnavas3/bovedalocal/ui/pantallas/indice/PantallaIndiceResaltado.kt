package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaIndiceResaltado(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    ContenedorPrincipal(
        titulo = "Resaltado y selección",
        idEtiqueta = "03-LST-AZX-RES",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() },
        cabeceraFlotante = {
            VistaPreviaIndiceInteractiva(
                ajustes = ajustes,
                letraArrastrada = letraArrastrada
            )
        },
        overlayLateral = {
            IndiceAlfabeticoCalibracion(
                ajustes = ajustes,
                alCambiarLetra = { letraArrastrada = it },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 64.dp, bottom = 100.dp)
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "Resaltado alfabético",
            icono = Icons.Filled.Highlight,
            alRestablecer = {
                haptica.tic()
                vm.ajustarIndiceResaltarEntradas(AjustesDefaults.Indice.RESALTAR_ENTRADAS)
                vm.ajustarIndiceResaltarSoloPrimera(AjustesDefaults.Indice.RESALTAR_SOLO_PRIMERA)
                vm.ajustarIndiceAlinearConCresta(AjustesDefaults.Indice.ALINEAR_CON_CRESTA)
                vm.avisar("Valores de resaltado restablecidos")
            },
            idGrupo = "03-LST-AZX-RES-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Resaltar cuentas al arrastrar",
                activo = ajustes.indiceResaltarEntradas,
                idFila = "03-LST-AZX-RES-SWT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceResaltarEntradas(it)
                }
            )

            if (ajustes.indiceResaltarEntradas) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSwitch(
                    titulo = "Resaltar solo la primera cuenta",
                    activo = ajustes.indiceResaltarSoloPrimera,
                    idFila = "03-LST-AZX-RES-1ST",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceResaltarSoloPrimera(it)
                    }
                )
            }

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSwitch(
                titulo = "Alinear con la cresta de la ola",
                activo = ajustes.indiceAlinearConCresta,
                idFila = "03-LST-AZX-RES-ALI",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceAlinearConCresta(it)
                }
            )
        }
    }
}
