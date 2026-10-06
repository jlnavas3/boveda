package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SelectorModoFormatoTitulos(
    modoActual: ModoFormatoTitulos,
    respetarManuales: Boolean,
    alCambiarModo: (ModoFormatoTitulos) -> Unit,
    alCambiarRespetarManuales: (Boolean) -> Unit,
    mostrarId: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Cuentas Múltiples del Mismo Sitio",
        icono = Icons.Filled.Tune,
        colorIcono = ColorAcento,
        idGrupo = "03-LST-TIT-G01",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "¿Cómo prefieres nombrar las cuentas cuando tienes más de una en la misma página?",
                color = TextoSecundario,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        ComponenteRadio(
            titulo = "Servicio (usuario)",
            subtitulo = "ej. Instagram (jolunavi), Google (cuenta@gmail.com)",
            icono = null,
            seleccionado = modoActual == ModoFormatoTitulos.EXPLICITO_PARENTESIS,
            idFila = "03-LST-TIT-PAR",
            mostrarId = mostrarId,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.EXPLICITO_PARENTESIS) }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteRadio(
            titulo = "Servicio · usuario",
            subtitulo = "ej. Instagram · jolunavi, Google · cuenta@gmail.com",
            icono = null,
            seleccionado = modoActual == ModoFormatoTitulos.EXPLICITO_SEPARADOR,
            idFila = "03-LST-TIT-SEP",
            mostrarId = mostrarId,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.EXPLICITO_SEPARADOR) }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteRadio(
            titulo = "Solo nombre del servicio (Minimalista)",
            subtitulo = "ej. Instagram, Google (el usuario se ve en el subtítulo)",
            icono = null,
            seleccionado = modoActual == ModoFormatoTitulos.MINIMALISTA,
            idFila = "03-LST-TIT-MIN",
            mostrarId = mostrarId,
            alSeleccionar = { alCambiarModo(ModoFormatoTitulos.MINIMALISTA) }
        )
        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Respetar títulos personalizados",
            subtitulo = "No sobrescribe entradas que ya hayas editado a mano con nombres propios",
            icono = null,
            activo = respetarManuales,
            idFila = "03-LST-TIT-MAN",
            mostrarId = mostrarId,
            alCambiar = alCambiarRespetarManuales
        )
    }
}

@BovedaPreview
@Composable
private fun PreviaSelectorModoFormatoTitulos() {
    BovedaTheme {
        SelectorModoFormatoTitulos(
            modoActual = ModoFormatoTitulos.EXPLICITO_PARENTESIS,
            respetarManuales = true,
            alCambiarModo = {},
            alCambiarRespetarManuales = {}
        )
    }
}
