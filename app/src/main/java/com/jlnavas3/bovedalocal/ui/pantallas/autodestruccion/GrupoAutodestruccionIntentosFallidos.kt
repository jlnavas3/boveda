package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelector
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionItemSelector
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoAutodestruccionIntentosFallidos(
    intentosMax: Int,
    alCambiarIntentosMax: (Int) -> Unit,
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Autodestrucción por intentos fallidos",
        icono = Icons.Filled.LockReset,
        colorIcono = Peligro,
        idGrupo = "01-SEG-DES-G03",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Borra permanentemente la base de datos si se introduce una clave incorrecta sucesivamente esta cantidad de veces. Cero desactiva la autodestrucción por intentos.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
            Spacer(Modifier.height(8.dp))
        }

        ComponenteSelector(
            titulo = "Límite de intentos",
            valorSeleccionado = intentosMax,
            opciones = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_INTENTOS.map {
                OpcionItemSelector(valor = it.first, etiqueta = it.second)
            },
            alSeleccionar = alCambiarIntentosMax,
            icono = Icons.Filled.WarningAmber,
            colorIcono = Peligro,
            idFila = "01-SEG-DES-INT",
            mostrarId = mostrarIdsAjustes
        )
    }
}

@BovedaPreview
@Composable
private fun GrupoAutodestruccionIntentosFallidosPreview() {
    GrupoAutodestruccionIntentosFallidos(
        intentosMax = 10,
        alCambiarIntentosMax = {},
        mostrarIdsAjustes = false
    )
}
