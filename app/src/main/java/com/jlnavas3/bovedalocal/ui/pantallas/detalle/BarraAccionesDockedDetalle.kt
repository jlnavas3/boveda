package com.jlnavas3.bovedalocal.ui.pantallas.detalle

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
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
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Barra inferior anclada fija (Docked Bottom Bar) para la pantalla de detalle:
 * - A la izquierda: 3 botones compactos con icono y texto pequeño debajo:
 *     1. Eliminar (rojo peligro)
 *     2. QR (compartir)
 *     3. Favorito (destacado si activo)
 * - A la derecha: Botón principal elegante Editar con fondo de acento.
 *
 * Mantiene despejada el área superior y previene que ningún botón tape los controles o fechas.
 */
@Composable
fun BarraAccionesDockedDetalle(
    esFavorito: Boolean,
    alEliminar: () -> Unit,
    alCompartirQr: () -> Unit,
    alAlternarFavorito: () -> Unit,
    alEditar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.5f)
    }

    val formaBotonEditar = RoundedCornerShape(CurvaturaEsquinas)

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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tres acciones compactas a la izquierda
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Eliminar
                BotonAccionCompactoDetalle(
                    icono = Icons.Filled.Delete,
                    etiqueta = "Eliminar",
                    alPulsar = alEliminar,
                    colorIcono = Peligro,
                    colorTexto = Peligro
                )

                // 2. QR / Compartir
                BotonAccionCompactoDetalle(
                    icono = Icons.Filled.QrCode,
                    etiqueta = "QR",
                    alPulsar = alCompartirQr,
                    colorIcono = TextoPrincipal,
                    colorTexto = TextoSecundario
                )

                // 3. Favorito
                BotonAccionCompactoDetalle(
                    icono = Icons.Filled.Star,
                    etiqueta = "Favorito",
                    alPulsar = alAlternarFavorito,
                    colorIcono = if (esFavorito) ColorAcento else TextoSecundario,
                    colorTexto = if (esFavorito) ColorAcento else TextoSecundario
                )
            }

            // Botón principal Editar a la derecha
            Row(
                modifier = Modifier
                    .reboteElastico()
                    .height(44.dp)
                    .clip(formaBotonEditar)
                    .background(ColorAcento)
                    .then(
                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaBotonEditar)
                        } else Modifier
                    )
                    .clickable(onClick = alEditar)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = ColorSobreAcento,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Editar",
                    color = ColorSobreAcento,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun BotonAccionCompactoDetalle(
    icono: ImageVector,
    etiqueta: String,
    alPulsar: () -> Unit,
    colorIcono: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .reboteElastico()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = etiqueta,
            tint = colorIcono,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = etiqueta,
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )
    }
}
