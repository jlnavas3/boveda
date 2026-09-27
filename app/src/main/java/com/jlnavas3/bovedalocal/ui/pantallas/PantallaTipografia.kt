package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PrevisualizacionTipografia
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun PantallaTipografia(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val familiaEtiqueta = AlmacenAjustes.OPCIONES_FAMILIA_FUENTE.firstOrNull { it.first == ajustes.familiaFuente }?.second ?: "Predeterminada"
    val pesoEtiqueta = AlmacenAjustes.OPCIONES_PESO_TEXTO.firstOrNull { it.first == ajustes.pesoTexto }?.second ?: "Normal"

    ContenedorPrincipal(
        titulo = "Tipografía",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "TEXTO Y FUENTES",
            icono = Icons.Filled.TextFields,
            colorIcono = Color(0xFF26A69A),
            alRestablecer = {
                haptica.tic()
                vm.restablecerTipografia()
                vm.avisar("Tipografía restablecida")
            }
        ) {
            ComponenteNavegacion(
                titulo = "Estilos predefinidos",
                icono = null,
                alPulsar = { vm.ir(Pantalla.TipografiaPresets()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Tamaño de fuente",
                valorTexto = "${String.format(Locale.US, "%.2f", ajustes.escalaTexto)}x",
                icono = null,
                alPulsar = { vm.ir(Pantalla.TipografiaEscala()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Familia tipográfica",
                valorTexto = familiaEtiqueta,
                icono = null,
                alPulsar = { vm.ir(Pantalla.TipografiaFamilia()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Grosor y estilo",
                valorTexto = if (ajustes.cursivaTexto) "$pesoEtiqueta · Cursiva" else pesoEtiqueta,
                icono = null,
                alPulsar = { vm.ir(Pantalla.TipografiaPeso()) }
            )
            ComponenteSeparador()
            ComponenteNavegacion(
                titulo = "Espaciado y separación",
                valorTexto = "${String.format(Locale.US, "%.1f", ajustes.espaciadoLetrasSp)} sp",
                icono = null,
                alPulsar = { vm.ir(Pantalla.TipografiaEspaciado()) }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
