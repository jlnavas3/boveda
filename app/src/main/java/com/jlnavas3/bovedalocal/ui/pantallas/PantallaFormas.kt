package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SimuladorTarjetaInteractiva
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PantallaFormas(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val estiloEtiqueta = AlmacenAjustes.OPCIONES_ESTILO_BORDE.firstOrNull { it.first == ajustes.estiloBorde }?.second ?: "Personalizado"
    val grosorTexto = if (ajustes.grosorBordeDp == 0f) "Sin borde" else "${String.format(Locale.US, "%.1f", ajustes.grosorBordeDp)} dp"

    ContenedorPrincipal(
        titulo = "Formas y bordes",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            SimuladorTarjetaInteractiva(
                ajustes = ajustes,
                haptica = haptica
            )
        },
        acciones = {
            BotonMenuOpcionesPantalla(
                grupos = listOf(
                    AccionSaltoGrupo("02-APA-GEO-G01", "Geometría y bordes")
                ),
                alRestablecerPantalla = {
                    haptica.tic()
                    vm.restablecerFormas()
                    vm.avisar("Geometría y bordes restablecidos")
                }
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "GEOMETRÍA Y BORDES",
            icono = Icons.Filled.SquareFoot,
            colorIcono = Color(0xFFE91E63),
            alRestablecer = {
                haptica.tic()
                vm.restablecerFormas()
                vm.avisar("Geometría y bordes restablecidos")
            },
            idGrupo = "02-APA-GEO-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteNavegacion(
                titulo = "Estilos predefinidos",
                icono = null,
                alPulsar = { vm.ir(Pantalla.FormasPresets()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Curvatura de esquinas",
                valorTexto = "${ajustes.curvaturaEsquinasDp.roundToInt()} dp",
                icono = null,
                alPulsar = { vm.ir(Pantalla.FormasCurvatura()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Grosor del borde",
                valorTexto = grosorTexto,
                icono = null,
                alPulsar = { vm.ir(Pantalla.FormasGrosor()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Tono y estilo del borde",
                valorTexto = estiloEtiqueta,
                icono = null,
                alPulsar = { vm.ir(Pantalla.FormasEstilo()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Espaciado y separación",
                valorTexto = "${ajustes.espaciadoComponentesDp.roundToInt()} dp",
                icono = null,
                alPulsar = { vm.ir(Pantalla.FormasEspaciado()) }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
