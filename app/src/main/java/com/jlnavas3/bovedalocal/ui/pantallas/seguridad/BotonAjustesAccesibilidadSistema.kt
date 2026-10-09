package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas

/**
 * Microcomponente que permite al usuario saltar directamente a los ajustes de accesibilidad
 * del sistema operativo Android para desactivar o desinstalar aplicaciones no reconocidas.
 */
@Composable
fun BotonAjustesAccesibilidadSistema(
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    Button(
        onClick = {
            try {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                contexto.startActivity(intent)
            } catch (_: Exception) {}
        },
        shape = RoundedCornerShape(CurvaturaEsquinas),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorAcento,
            contentColor = ColorSobreAcento
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "Abrir Ajustes de Accesibilidad de Android",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.5.sp
        )
    }
}
