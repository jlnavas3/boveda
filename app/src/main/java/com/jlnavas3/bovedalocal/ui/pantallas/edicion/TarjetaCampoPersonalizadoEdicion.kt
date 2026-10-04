package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo

/**
 * Tarjeta individual de edición para un campo personalizado con soporte para selección de fecha/hora,
 * formatos numéricos, máscaras telefónicas y alternancia de datos sensibles.
 */
@Composable
fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit,
    ajustes: AjustesApp = AjustesApp(),
    modifier: Modifier = Modifier
) {
    var mostrarValor by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(ColorCampoAjustes)
            .border(1.dp, ColorBordeActual, FormaCampo)
            .padding(12.dp)
    ) {
        // Cabecera: número, badge de tipo, badge de sensible y botón eliminar
        CabeceraTarjetaCampoPersonalizado(
            numero = numero,
            campo = campo,
            esSensible = esSensible,
            alEliminar = alEliminar
        )

        Spacer(Modifier.height(10.dp))

        // Etiqueta del campo
        CampoBoveda(
            valor = campo.etiqueta,
            etiqueta = "Nombre del campo",
            alCambiar = { alModificar(campo.copy(etiqueta = it)) }
        )

        Spacer(Modifier.height(8.dp))

        // Entrada de valor según el tipo específico
        EntradaValorCampoPersonalizado(
            campo = campo,
            esSensible = esSensible,
            mostrarValor = mostrarValor,
            alAlternarMostrarValor = { mostrarValor = !mostrarValor },
            alModificar = alModificar,
            ajustes = ajustes
        )

        // Toggle rápido de sensibilidad
        Spacer(Modifier.height(6.dp))
        ToggleSensibilidadCampo(
            esSensible = campo.esSensible,
            alAlternar = { alModificar(campo.copy(esSensible = !campo.esSensible)) }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaCampoPersonalizadoEdicionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TarjetaCampoPersonalizadoEdicion(
                numero = 1,
                campo = CampoPersonalizado(
                    etiqueta = "PIN de Acceso",
                    valor = "9942",
                    tipo = TipoCampo.PIN,
                    esSensible = true
                ),
                alModificar = {},
                alEliminar = {}
            )
            TarjetaCampoPersonalizadoEdicion(
                numero = 2,
                campo = CampoPersonalizado(
                    etiqueta = "Fecha de Emisión",
                    valor = "15/04/2025",
                    tipo = TipoCampo.FECHA,
                    esSensible = false
                ),
                alModificar = {},
                alEliminar = {}
            )
        }
    }
}
