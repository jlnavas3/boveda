package com.jlnavas3.bovedalocal.ui.pantallas.edicion

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun DialogoPresetsRapidos(
    alDescartar: () -> Unit,
    alSeleccionarPreset: (List<CampoPersonalizado>) -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Conjuntos de campos rápidos",
        icono = Icons.Filled.DynamicForm,
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cerrar", color = TextoSecundario)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Selecciona un conjunto sugerido para agregar sus campos a esta entrada:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

            Spacer(Modifier.height(2.dp))

            val fondoItem = if (esOscuroActivo) Color(0xFF2A292E) else Color(0xFFEFEFF3)

            PresetsCampos.todos.forEach { preset ->
                val icono: ImageVector = when (preset.tipoEntradaSugerido) {
                    TipoEntrada.TARJETA -> Icons.Filled.CreditCard
                    TipoEntrada.WIFI -> Icons.Filled.Wifi
                    TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
                    TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
                    TipoEntrada.SERVIDOR -> Icons.Filled.Dns
                    TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
                    else -> Icons.Filled.DynamicForm
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(fondoItem)
                        .then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                            } else Modifier
                        )
                        .clickable {
                            alSeleccionarPreset(preset.generarCampos())
                            alDescartar()
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(FormaPequena)
                            .background(ColorAcento),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = preset.titulo,
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp)
                        )
                        Text(
                            text = preset.descripcion,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}
