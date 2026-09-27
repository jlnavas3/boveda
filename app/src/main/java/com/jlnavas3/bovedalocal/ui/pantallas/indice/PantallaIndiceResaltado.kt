package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
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

    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar"
        )
    }

    ContenedorPrincipal(
        titulo = "Resaltado y selección",
        alVolver = { vm.volverAtras() },
        cabeceraFlotante = {
            VistaPreviaIndiceInteractiva(
                ajustes = ajustes,
                letraArrastrada = 'G',
                mockItems = mockItems
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "Resaltado en lista",
            icono = Icons.Filled.Highlight,
            idGrupo = "03.2.4",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Resaltar cuentas al arrastrar",
                activo = ajustes.indiceResaltarEntradas,
                idFila = "03.2.4.1",
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
                    idFila = "03.2.4.2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceResaltarSoloPrimera(it)
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer resaltado",
                alPulsar = {
                    haptica.tic()
                    vm.ajustarIndiceResaltarEntradas(true)
                    vm.ajustarIndiceResaltarSoloPrimera(true)
                    vm.avisar("Valores de resaltado restablecidos")
                }
            )
        }
    }
}
