package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun AccionesCalibracionEngranajes(
    ajustes: AjustesApp,
    vm: VaultViewModel
) {
    val contexto = LocalContext.current

    ComponenteGrupo(
        etiqueta = "Acciones del mecanismo",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        ComponenteNavegacion(
            titulo = "Copiar valores",
            icono = Icons.Filled.ContentCopy,
            colorIcono = ColorIconosInternos,
            alPulsar = {
                val textoConfig = buildString {
                    appendLine("=== CONFIGURACIÓN DE ENGRANAJES ===")
                    appendLine("velocidad = ${ajustes.engranajesVelocidad.roundToInt()}f")
                    appendLine("grosorBorde = ${String.format(Locale.US, "%.1f", ajustes.engranajesGrosorBorde)}f")
                    appendLine("alturaDientes = ${String.format(Locale.US, "%.2f", ajustes.engranajesAlturaDientes)}f")
                    appendLine("anchoDientes = ${String.format(Locale.US, "%.2f", ajustes.engranajesAnchoDientes)}f")
                    appendLine("grosorRadios = ${String.format(Locale.US, "%.2f", ajustes.engranajesGrosorRadios)}f")
                    appendLine("curvaturaRadios = ${String.format(Locale.US, "%.2f", ajustes.engranajesCurvaturaRadios)}f")
                    appendLine("cantidadRadios = ${if (ajustes.engranajesCantidadRadios <= 2) "0 (Auto)" else "${ajustes.engranajesCantidadRadios}"}")
                    appendLine("radioInterior = ${String.format(Locale.US, "%.2f", ajustes.engranajesRadioInterior)}f")
                    appendLine("tamanoEje = ${String.format(Locale.US, "%.2f", ajustes.engranajesTamanoEje)}f")
                    appendLine("sombraIntensidad = ${String.format(Locale.US, "%.2f", ajustes.engranajesSombraIntensidad)}f")
                    appendLine("colorBrillo = \"${ajustes.engranajesColorBrillo}\"")
                    appendLine("colorPrincipal = \"${ajustes.engranajesColorPrincipal}\"")
                    appendLine("colorSombraMedio = \"${ajustes.engranajesColorSombraMedio}\"")
                    appendLine("colorSombraOscuro = \"${ajustes.engranajesColorSombraOscuro}\"")
                    appendLine("colorBisel = \"${ajustes.engranajesColorBisel}\"")
                    appendLine("colorInterior = \"${ajustes.engranajesColorInterior}\"")
                    appendLine("colorCubo = \"${ajustes.engranajesColorCubo}\"")
                    appendLine("colorEje = \"${ajustes.engranajesColorEje}\"")
                }
                val clipboard = contexto.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Configuración Engranajes", textoConfig))
                Toast.makeText(contexto, "Configuración copiada al portapapeles", Toast.LENGTH_SHORT).show()
            }
        )
        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer módulo",
            alPulsar = {
                vm.restablecerAjustesEngranajes()
                Toast.makeText(contexto, "Engranajes restablecidos", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
