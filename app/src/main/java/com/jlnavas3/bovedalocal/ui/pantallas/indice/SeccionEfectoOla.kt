package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun SeccionEfectoOla(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    val amplitud = ajustes.indiceAmplitudOlaDp
    val radio = ajustes.indiceRadioOlaDp
    val escala = ajustes.indiceEscalaLetras
    val escalaStr = "%.1f".format(Locale.US, escala)

    ComponenteGrupo(
        etiqueta = "Efecto de ola Niagara",
        idGrupo = "03.4.G2",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Curvatura dinámica y escala que sigue el movimiento del dedo"
    ) {
        ComponenteSwitch(
            titulo = "Activar ola interactiva",
            icono = Icons.Filled.Animation,
            colorIcono = Color(0xFF6A1B9A),
            activo = ajustes.indiceEfectoOla,
            idFila = "03.4.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { haptica.tic(); vm.ajustarIndiceEfectoOla(it) }
        )

        if (ajustes.indiceEfectoOla) {
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Amplitud de la curvatura",
                valor = amplitud,
                valorTexto = "${amplitud.toInt()} dp",
                rango = 0f..130f,
                idFila = "03.4.2",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { vm.ajustarIndiceAmplitudOlaDp(it) }
            )

            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Alcance vertical",
                valor = radio,
                valorTexto = "${radio.toInt()} dp",
                rango = 80f..300f,
                idFila = "03.4.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { vm.ajustarIndiceRadioOlaDp(it) }
            )

            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Aumento de letras en cresta",
                valor = escala,
                valorTexto = "${escalaStr}x",
                rango = 1.0f..2.6f,
                idFila = "03.4.4",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { vm.ajustarIndiceEscalaLetras(it) }
            )
        }

        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = {
                vm.ajustarIndiceEfectoOla(true)
                vm.ajustarIndiceAmplitudOlaDp(109f)
                vm.ajustarIndiceRadioOlaDp(169f)
                vm.ajustarIndiceEscalaLetras(1.5f)
            }
        )
    }
}
