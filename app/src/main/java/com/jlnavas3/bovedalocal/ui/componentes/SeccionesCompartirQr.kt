package com.jlnavas3.bovedalocal.ui.componentes

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
internal fun CabeceraCompartirQr(
    esWifi: Boolean,
    modoSeleccionado: ModoCompartirQr,
    ssidWifi: String,
    tituloEntrada: String,
    onCopiar: () -> Unit,
    onCerrar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            val iconoCabecera = when (modoSeleccionado) {
                ModoCompartirQr.CONTACTO -> Icons.Filled.Person
                ModoCompartirQr.WIFI -> Icons.Filled.Wifi
                ModoCompartirQr.TOTP -> Icons.Filled.QrCode
                ModoCompartirQr.TRANSFERIR -> Icons.Filled.QrCode
            }

            val tituloCabecera = when (modoSeleccionado) {
                ModoCompartirQr.CONTACTO -> "Contacto (vCard)"
                ModoCompartirQr.WIFI -> "Conectar a Wi-Fi"
                ModoCompartirQr.TOTP -> "Vincular 2FA"
                ModoCompartirQr.TRANSFERIR -> "Transferir a Bóveda"
            }

            val subtituloCabecera = when (modoSeleccionado) {
                ModoCompartirQr.CONTACTO -> tituloEntrada.ifBlank { "Contacto" }
                ModoCompartirQr.WIFI -> if (ssidWifi.isNotBlank()) "Red: $ssidWifi" else tituloEntrada
                ModoCompartirQr.TOTP -> tituloEntrada.ifBlank { "Código 2FA" }
                ModoCompartirQr.TRANSFERIR -> tituloEntrada.ifBlank { "Credencial segura" }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(FormaPequena)
                    .background(ColorAcento),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoCabecera,
                    contentDescription = null,
                    tint = ColorSobreAcento,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = tituloCabecera,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(
                    text = subtituloCabecera,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onCopiar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copiar texto del QR",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(4.dp))
            IconButton(
                onClick = onCerrar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
internal fun SelectorModosCompartirQr(
    modosDisponibles: List<ModoCompartirQr>,
    modoSeleccionado: ModoCompartirQr,
    fondoSelector: Color,
    onSeleccionar: (ModoCompartirQr) -> Unit
) {
    if (modosDisponibles.size <= 1) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(FormaCampo)
            .background(fondoSelector)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                } else Modifier
            )
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        modosDisponibles.forEach { modo ->
            val seleccionado = modo == modoSeleccionado
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(FormaPequena)
                    .background(if (seleccionado) ColorAcento else Color.Transparent)
                    .clickable { onSeleccionar(modo) }
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = modo.etiqueta,
                    color = if (seleccionado) ColorSobreAcento else ColorAjusteGris,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.5.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
internal fun VisorCodigoQr(
    qrBitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(240.dp)
            .clip(FormaCampo)
            .background(Color.White)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                } else Modifier
            )
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        if (qrBitmap != null) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "Código QR offline",
                modifier = Modifier.size(212.dp)
            )
        } else {
            Text(
                text = "No se pudo generar el código QR",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
internal fun CapsulaTemporizadorSeguridad(
    tiempoRestante: Int,
    modifier: Modifier = Modifier
) {
    val esOscuro = esOscuroActivo
    val colorTextoEIcono = if (esOscuro) Color(0xFFFFB74D) else Color(0xFFB45309)
    val colorFondoCapsula = if (esOscuro) Color(0xFF2B2215) else Color(0xFFFFF3E0)

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(colorFondoCapsula)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Timer,
            contentDescription = null,
            tint = colorTextoEIcono,
            modifier = Modifier.size(15.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Se cerrará en ${tiempoRestante}s por seguridad",
            color = colorTextoEIcono,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        )
    }
}
