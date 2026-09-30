package com.jlnavas3.bovedalocal.ui.pantallas.copia

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun GrupoCopiaAutomaticaLocal(
    ajustes: AjustesApp,
    seccionDestino: String?,
    vm: VaultViewModel,
    modifier: Modifier = Modifier,
    haptica: Haptica? = null
) {
    val dias = ajustes.backupAutoFrecuenciaDias

    val textoFrecuencia = AlmacenAjustes.OPCIONES_FRECUENCIA_BACKUP_AUTO
        .find { it.first == dias }?.second ?: if (dias > 0) "$dias días" else "Desactivada"

    val valorTexto = if (dias <= 0) "Desactivada" else textoFrecuencia

    Column(modifier = modifier) {
        ComponenteGrupo(
            etiqueta = "Copia automática local",
            icono = Icons.Filled.Autorenew,
            colorIcono = ColorExportacion,
            idGrupo = "05-COP-ATM-G01",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Copias periódicas cifradas en Descargas con rotación"
        ) {
            ComponenteNavegacion(
                titulo = "Configurar respaldo",
                icono = null,
                idFila = "05-COP-ATM-CFG",
                mostrarId = ajustes.mostrarIdsAjustes,
                valorTexto = valorTexto,
                alPulsar = {
                    haptica?.tic()
                    vm.ir(Pantalla.AjustesCopiaAutomatica("05-COP-ATM"))
                }
            )
        }
    }
}
