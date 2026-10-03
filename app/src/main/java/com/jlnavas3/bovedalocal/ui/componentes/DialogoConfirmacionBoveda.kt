package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo de confirmación estándar y elegante para la aplicación.
 * Sigue la estética unificada de ModalInferiorBoveda con squircle para el icono de cabecera,
 * tipografía cuidada, bordes y curvatura adaptativos y botones sólidos BotonBoveda.
 */
@Composable
fun DialogoConfirmacionBoveda(
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit,
    textoCancelar: String = "Cancelar",
    tipoConfirmacion: TipoBotonTexto = TipoBotonTexto.PRIMARIO,
    iconoHeader: ImageVector? = null,
    fijarAbajo: Boolean = false
) {
    val varianteConfirmar = when (tipoConfirmacion) {
        TipoBotonTexto.PELIGRO -> VarianteBoton.PELIGRO
        TipoBotonTexto.SECUNDARIO -> VarianteBoton.SECUNDARIO
        TipoBotonTexto.PRIMARIO -> VarianteBoton.PRIMARIO
    }

    val colorIcono = when (tipoConfirmacion) {
        TipoBotonTexto.PELIGRO -> Color.White
        TipoBotonTexto.SECUNDARIO -> ColorIconosInternos
        TipoBotonTexto.PRIMARIO -> ColorSobreAcento
    }

    val fondoIcono = when (tipoConfirmacion) {
        TipoBotonTexto.PELIGRO -> Peligro
        TipoBotonTexto.SECUNDARIO -> ColorAjusteGris.copy(alpha = 0.15f)
        TipoBotonTexto.PRIMARIO -> ColorAcento
    }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alDescartar,
        fijarAbajo = fijarAbajo,
        titulo = titulo,
        icono = iconoHeader,
        colorIcono = colorIcono,
        fondoIcono = fondoIcono,
        mostrarBotonCerrar = false
    ) {
        Text(
            text = mensaje,
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
        )

        Spacer(Modifier.height(20.dp))

        val apilarBotones = textoConfirmar.length > 14 || textoCancelar.length > 14

        if (apilarBotones) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BotonBoveda(
                    texto = textoConfirmar,
                    variante = varianteConfirmar,
                    icono = iconoHeader,
                    modifier = Modifier.fillMaxWidth(),
                    alPulsar = alConfirmar
                )
                BotonBoveda(
                    texto = textoCancelar,
                    variante = VarianteBoton.SECUNDARIO,
                    modifier = Modifier.fillMaxWidth(),
                    alPulsar = alDescartar
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonBoveda(
                    texto = textoCancelar,
                    variante = VarianteBoton.SECUNDARIO,
                    modifier = Modifier.weight(1f),
                    alPulsar = alDescartar
                )
                BotonBoveda(
                    texto = textoConfirmar,
                    variante = varianteConfirmar,
                    icono = iconoHeader,
                    modifier = Modifier.weight(1f),
                    alPulsar = alConfirmar
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun DialogoConfirmacionBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar credencial?",
            mensaje = "Esta acción moverá la credencial seleccionada a la papelera de reciclaje.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {},
            alDescartar = {}
        )
    }
}

