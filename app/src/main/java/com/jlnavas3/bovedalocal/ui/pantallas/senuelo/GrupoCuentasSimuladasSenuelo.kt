package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
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
        etiqueta = "Cuentas simuladas",
        icono = Icons.Filled.Group,
        colorIcono = ColorSeguridad,
        alRestablecer = alRestablecerEjemplos,
        idGrupo = "01.3.G3",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Actualmente hay $cantidadCuentas cuentas simuladas en la bóveda señuelo.",
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Se muestran al ingresar el PIN de coacción. Usa el ícono superior para restablecerlas a los ejemplos predeterminados.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
