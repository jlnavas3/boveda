package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

data class ItemSeleccionableCxf(
    val entrada: Entrada,
    var seleccionada: Boolean,
    val yaExisteEnBoveda: Boolean
)

/**
 * Fila individual para una credencial CXF que va a ser importada.
 * Muestra checkbox de selección, título, usuario, badge si ya existe y chips descriptivos.
 */
@Composable
fun FilaConfirmacionCxf(
    item: ItemSeleccionableCxf,
    alAlternar: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val forma = FormaTarjeta
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (tieneBorde) {
                    val colorBorde = if (item.seleccionada) ColorAcento.copy(alpha = 0.65f) else ColorBordeActual
                    Modifier.border(GrosorBorde, colorBorde, forma)
                } else Modifier
            )
            .clickable(onClick = alAlternar)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckboxBoveda(
            checked = item.seleccionada,
            onCheckedChange = onCheckedChange
        )

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (item.yaExisteEnBoveda) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Ya en bóveda",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                        color = colorLegibleParaTema(Peligro),
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(Peligro))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (item.entrada.usuario.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.entrada.usuario,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(6.dp))

            // Etiquetas de contenido adaptadas al tema
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (item.entrada.passkey != null) {
                    MiniChipCxf("Llave de paso", ColorPasskeys)
                }
                if (item.entrada.contrasena.isNotBlank()) {
                    MiniChipCxf("Contraseña", Color(0xFF2196F3))
                }
                if (!item.entrada.secretoTotp.isNullOrBlank()) {
                    MiniChipCxf("Dos pasos", Color2FA)
                }
            }
        }
    }
}
