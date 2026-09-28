package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SeccionGeometriaEngranajes(
    ajustes: AjustesApp,
    vm: VaultViewModel
) {
    val contexto = LocalContext.current

    ComponenteGrupo(
        etiqueta = "Geometría y movimiento",
        idGrupo = "02-APA-THM-ANI-G01",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            vm.restablecerAjustesEngranajes()
            Toast.makeText(contexto, "Geometría restablecida", Toast.LENGTH_SHORT).show()
        }
    ) {
        ComponenteSlider(
            titulo = "Velocidad de rotación",
            valor = ajustes.engranajesVelocidad,
            valorTexto = "${ajustes.engranajesVelocidad.roundToInt()} °/s",
            rango = 0f..120f,
            alCambiar = { vm.ajustarEngranajesVelocidad(it) },
            etiquetaMin = "0°/s (Detenido)",
            etiquetaMax = "120°/s (Rápido)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Grosor de borde y bisel",
            valor = ajustes.engranajesGrosorBorde,
            valorTexto = if (ajustes.engranajesGrosorBorde == 0f) "0 dp (Sin borde)" else "${String.format(Locale.US, "%.1f", ajustes.engranajesGrosorBorde)} dp",
            rango = 0f..6f,
            alCambiar = { vm.ajustarEngranajesGrosorBorde(it) },
            etiquetaMin = "0 dp (Plano)",
            etiquetaMax = "6.0 dp (Grueso)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Altura de los dientes",
            valor = ajustes.engranajesAlturaDientes,
            valorTexto = "${String.format(Locale.US, "%.2f", ajustes.engranajesAlturaDientes)}x",
            rango = 0.2f..2.5f,
            alCambiar = { vm.ajustarEngranajesAlturaDientes(it) },
            etiquetaMin = "0.2x (Cortos)",
            etiquetaMax = "2.5x (Profundos)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Ancho de los dientes",
            valor = ajustes.engranajesAnchoDientes,
            valorTexto = "${String.format(Locale.US, "%.2f", ajustes.engranajesAnchoDientes)}x",
            rango = 0.3f..2.0f,
            alCambiar = { vm.ajustarEngranajesAnchoDientes(it) },
            etiquetaMin = "0.3x (Finos)",
            etiquetaMax = "2.0x (Anchos)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Grosor de radios",
            valor = ajustes.engranajesGrosorRadios,
            valorTexto = if (ajustes.engranajesGrosorRadios == 0f) "Sin radios" else "${String.format(Locale.US, "%.2f", ajustes.engranajesGrosorRadios)}x",
            rango = 0f..4.0f,
            alCambiar = { vm.ajustarEngranajesGrosorRadios(it) },
            etiquetaMin = "0x (Ciego)",
            etiquetaMax = "4.0x (Robustos)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Cantidad de radios",
            valor = if (ajustes.engranajesCantidadRadios in 3..12) ajustes.engranajesCantidadRadios.toFloat() else 2f,
            valorTexto = if (ajustes.engranajesCantidadRadios <= 2) "Auto (según tamaño)" else "${ajustes.engranajesCantidadRadios} radios",
            rango = 2f..12f,
            pasos = 9,
            alCambiar = { vm.ajustarEngranajesCantidadRadios(if (it.roundToInt() <= 2) 0 else it.roundToInt()) },
            etiquetaMin = "Auto (3-6)",
            etiquetaMax = "12 radios"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Curvatura de radios",
            valor = ajustes.engranajesCurvaturaRadios,
            valorTexto = "${String.format(Locale.US, "%.2f", ajustes.engranajesCurvaturaRadios)}x",
            rango = 0.0f..1.0f,
            alCambiar = { vm.ajustarEngranajesCurvaturaRadios(it) },
            etiquetaMin = "0.0x (Recto)",
            etiquetaMax = "1.0x (Redondeado)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Cavidad interior (radio)",
            valor = ajustes.engranajesRadioInterior,
            valorTexto = String.format(Locale.US, "%.2f", ajustes.engranajesRadioInterior),
            rango = 0.20f..0.95f,
            alCambiar = { vm.ajustarEngranajesRadioInterior(it) },
            etiquetaMin = "0.20 (Pequeña)",
            etiquetaMax = "0.95 (Amplia)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Tamaño eje y cubo central",
            valor = ajustes.engranajesTamanoEje,
            valorTexto = "${String.format(Locale.US, "%.2f", ajustes.engranajesTamanoEje)}x",
            rango = 0.2f..2.5f,
            alCambiar = { vm.ajustarEngranajesTamanoEje(it) },
            etiquetaMin = "0.2x (Minúsculo)",
            etiquetaMax = "2.5x (Grande)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Intensidad de sombra y relieve",
            valor = ajustes.engranajesSombraIntensidad,
            valorTexto = String.format(Locale.US, "%.2f", ajustes.engranajesSombraIntensidad),
            rango = 0.0f..1.0f,
            alCambiar = { vm.ajustarEngranajesSombraIntensidad(it) },
            etiquetaMin = "0.0 (Sin sombra)",
            etiquetaMax = "1.0 (Contraste alto)"
        )
    }
}
