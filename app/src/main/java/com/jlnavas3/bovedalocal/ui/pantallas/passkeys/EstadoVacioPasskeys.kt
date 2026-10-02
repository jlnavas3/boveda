package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun EstadoVacioPasskeys(
    sinPasskeysEnTotal: Boolean,
    modifier: Modifier = Modifier,
    alImportarPasskeys: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(FormaPequena)
                .background(fondoBadgeParaTema(ColorPasskeys)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = null,
                tint = colorLegibleParaTema(ColorPasskeys),
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            if (sinPasskeysEnTotal) "Todavía no hay llaves de paso" else "No se encontraron llaves de paso",
            color = ColorTitulos,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            if (sinPasskeysEnTotal) {
                "Cuando una web o app te pida crear una llave de paso y elijas Bóveda local, aparecerá guardada aquí. También puedes importarlas directamente desde otro gestor."
            } else {
                "Ninguna llave de paso coincide con la búsqueda o filtro aplicado."
            },
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        if (sinPasskeysEnTotal && alImportarPasskeys != null) {
            Spacer(Modifier.height(16.dp))
            androidx.compose.material3.OutlinedButton(
                onClick = alImportarPasskeys,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = ColorPasskeys)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.VpnKey,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Importar llaves de paso", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun EstadoVacioPasskeysPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        EstadoVacioPasskeys(
            sinPasskeysEnTotal = true,
            alImportarPasskeys = {}
        )
    }
}

