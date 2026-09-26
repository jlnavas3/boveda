package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionColorDinamicoYSistema(
    ajustes: AjustesApp,
    haptica: Haptica,
    reqDinamico: BringIntoViewRequester,
    alAlternarColorDinamico: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Spacer(Modifier.height(18.dp))
        ComponenteGrupo(
            etiqueta = "Color dinámico del sistema",
            idGrupo = "03.2.G3",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Adapta los colores automáticamente al fondo de pantalla de Android",
            modifier = Modifier.bringIntoViewRequester(reqDinamico)
        ) {
            ComponenteSwitch(
                titulo = "Material You",
                icono = Icons.Filled.AutoAwesome,
                colorIcono = Color(0xFF00897B),
                activo = ajustes.colorDinamicoSistema,
                idFila = "03.2.7",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    alAlternarColorDinamico(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionColorAcento(
    ajustes: AjustesApp,
    reqAcento: BringIntoViewRequester,
    alAjustarColorAcento: (String) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Color de acento principal",
        idGrupo = "03.2.G4",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = if (ajustes.colorDinamicoSistema) {
            "Material You está activo. Seleccionar un color manual desactivará el color dinámico del sistema."
        } else {
            "Afecta a los botones destacados, elementos activos y selectores"
        },
        modifier = Modifier.bringIntoViewRequester(reqAcento)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SelectorColorEnTiempoReal(
                colorInicial = ColorAcento,
                titulo = "Acento"
            ) { nuevoColor ->
                alAjustarColorAcento(nuevoColor.aHex())
            }
        }
        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                alAjustarColorAcento("ambar")
            }
        )
    }
}
