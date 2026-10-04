package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
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
        Spacer(Modifier.height(14.dp))
        ComponenteGrupo(
            etiqueta = "COLOR DINÁMICO",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = Color(0xFF00897B),
            alRestablecer = { alAlternarColorDinamico(AjustesDefaults.Tema.COLOR_DINAMICO_SISTEMA) },
            idGrupo = "02-APA-THM-G03",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqDinamico)
        ) {
            ComponenteSwitch(
                titulo = "Material You",
                icono = null,
                activo = ajustes.colorDinamicoSistema,
                idFila = "02-APA-THM-DYN",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    alAlternarColorDinamico(it)
                }
            )
        }
    }
}
