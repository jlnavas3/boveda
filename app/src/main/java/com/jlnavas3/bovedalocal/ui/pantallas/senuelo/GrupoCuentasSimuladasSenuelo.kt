package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoCuentasSimuladasSenuelo(
    mostrarIdsAjustes: Boolean,
    cantidadCuentas: Int,
    alRestablecerEjemplos: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Cuentas simuladas en señuelo",
        idGrupo = "01.2.G3",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Actualmente hay $cantidadCuentas cuentas simuladas almacenadas en la bóveda señuelo.",
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Estas cuentas se muestran al ingresar el PIN de coacción en el desbloqueo.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }

        SeparadorFilaSimple()

        ComponenteBotonFila(
            titulo = "Restablecer cuentas de ejemplo",
            icono = Icons.Filled.Refresh,
            colorIcono = ColorIconosInternos,
            idFila = "01.2.3",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alRestablecerEjemplos
        )
    }
}
