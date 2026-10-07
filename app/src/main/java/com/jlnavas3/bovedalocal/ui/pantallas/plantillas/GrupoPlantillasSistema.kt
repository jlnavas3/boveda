package com.jlnavas3.bovedalocal.ui.pantallas.plantillas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoPlantillasSistema(
    mostrarIdsAjustes: Boolean,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Plantillas predeterminadas del sistema",
        icono = Icons.Filled.DynamicForm,
        idGrupo = "03-LST-PLT-G02",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Esquemas nativos provistos por el sistema listos para aplicar en tus cuentas:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

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
                        .background(ColorCampoAjustes)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(FormaPequena)
                            .background(ColorAcento),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = preset.titulo,
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = preset.descripcion,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                        )
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun GrupoPlantillasSistemaPreview() {
    GrupoPlantillasSistema(mostrarIdsAjustes = false)
}
