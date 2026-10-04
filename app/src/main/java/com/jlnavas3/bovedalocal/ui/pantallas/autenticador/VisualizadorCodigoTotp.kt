package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente que renderiza el código TOTP numérico, su temporizador visual (tarta)
 * y el botón de alternar revelado/ocultamiento con seguridad visual.
 */
@Composable
fun VisualizadorCodigoTotp(
    codigo: String,
    codigoVisible: String,
    segundosRestantes: Long,
    periodo: Long,
    ocultarTotp: Boolean,
    codigoRevelado: Boolean,
    estiloOcultamiento: String,
    seleccionActiva: Boolean,
    haptica: Haptica,
    alCopiarCodigo: (String) -> Unit,
    alAlternarSeleccion: (() -> Unit)?,
    alAlternarRevelado: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .clip(FormaPequena)
                .clickable {
                    if (seleccionActiva) {
                        haptica.tic()
                        alAlternarSeleccion?.invoke()
                    } else {
                        haptica.exito()
                        alCopiarCodigo(codigo)
                    }
                }
                .padding(vertical = 2.dp)
        ) {
            TextoSeguroVisual(
                texto = codigoVisible,
                oculto = if (ocultarTotp) !codigoRevelado else false,
                estilo = estiloOcultamiento,
                estiloTexto = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                colorTexto = ColorTitulos
            )
            IndicadorTotpTarta(
                segundosRestantes = segundosRestantes,
                periodo = periodo,
                tamano = 18.dp
            )
        }
        if (ocultarTotp) {
            IconButton(
                onClick = {
                    haptica.toque()
                    alAlternarRevelado()
                },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (codigoRevelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (codigoRevelado) "Ocultar código" else "Mostrar código",
                    tint = TextoSecundario,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
