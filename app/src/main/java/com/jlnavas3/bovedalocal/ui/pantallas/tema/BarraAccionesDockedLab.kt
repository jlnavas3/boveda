package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

/**
 * Barra de acciones anclada inferior fija (Docked Bottom Bar estilo One UI / Material 3)
 * para el Laboratorio de Temas.
 * Libera 100% el área de scroll evitando que botones flotantes tapen los controles y números.
 */
@Composable
fun BarraAccionesDockedLab(
    colorAcentoActual: Color,
    alRestablecer: () -> Unit,
    alCopiarPaleta: () -> Unit,
    alGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes.copy(alpha = 0.5f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorTarjetaAjustes)
    ) {
        // Línea divisoria superior sutil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.8.dp)
                .background(colorBorde)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Acciones secundarias a la izquierda (Restablecer y Copiar)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonAccionCompactoLab(
                    icono = Icons.Filled.RestartAlt,
                    etiqueta = "Restablecer",
                    alPulsar = alRestablecer,
                    colorIcono = ColorAjusteGris,
                    colorTexto = ColorAjusteGris
                )

                BotonAccionCompactoLab(
                    icono = Icons.Filled.ContentCopy,
                    etiqueta = "Copiar",
                    alPulsar = alCopiarPaleta,
                    colorIcono = ColorAjusteGris,
                    colorTexto = ColorAjusteGris
                )
            }

            // Acción primaria destacada a la derecha: Guardar tema
            val colorContenidoBoton = colorContraste(colorAcentoActual)
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(FormaBoton)
                    .background(colorAcentoActual)
                    .then(
                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.5f), FormaBoton)
                        } else Modifier
                    )
                    .clickable { alGuardar() }
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = null,
                        tint = colorContenidoBoton,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Guardar tema",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = colorContenidoBoton
                    )
                }
            }
        }
    }
}

@Composable
private fun BotonAccionCompactoLab(
    icono: ImageVector,
    etiqueta: String,
    alPulsar: () -> Unit,
    colorIcono: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(8.dp)
    Column(
        modifier = modifier
            .clip(forma)
            .background(ColorCampoAjustes)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = etiqueta,
            tint = colorIcono,
            modifier = Modifier.size(17.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Medium
            ),
            color = colorTexto
        )
    }
}
