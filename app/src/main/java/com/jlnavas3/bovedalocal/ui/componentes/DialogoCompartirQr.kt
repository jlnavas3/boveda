package com.jlnavas3.bovedalocal.ui.componentes

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.GeneradorQr
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.delay

private enum class ModoQr(val etiqueta: String) {
    TOTP("2FA / TOTP"),
    CONTRASENA("Contraseña"),
    CREDENCIAL("Completa")
}

@Composable
fun DialogoCompartirQr(
    entrada: Entrada,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val tieneTotp = !entrada.secretoTotp.isNullOrBlank()
    val tieneContrasena = entrada.contrasena.isNotBlank()

    val modosDisponibles = remember(entrada) {
        buildList {
            if (tieneTotp) add(ModoQr.TOTP)
            if (tieneContrasena) add(ModoQr.CONTRASENA)
            add(ModoQr.CREDENCIAL)
        }
    }

    var modoSeleccionado by remember {
        mutableStateOf(if (tieneTotp) ModoQr.TOTP else if (tieneContrasena) ModoQr.CONTRASENA else ModoQr.CREDENCIAL)
    }

    val textoQr = remember(entrada, modoSeleccionado) {
        when (modoSeleccionado) {
            ModoQr.TOTP -> GeneradorQr.uriTotp(entrada) ?: entrada.titulo
            ModoQr.CONTRASENA -> entrada.contrasena
            ModoQr.CREDENCIAL -> GeneradorQr.textoCredencialCompleta(entrada)
        }
    }

    val qrBitmap = remember(textoQr) {
        try {
            GeneradorQr.generarBitmap(textoQr, tamano = 640)
        } catch (_: Exception) {
            null
        }
    }

    var tiempoRestante by remember { mutableIntStateOf(60) }
    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(1000)
            tiempoRestante--
        }
        alCerrar()
    }

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(ColorTarjetas)
                .then(
                    if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                    else Modifier
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Cabecera con título y botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.QrCode,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Compartir por QR",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = entrada.titulo.ifBlank { "Credencial" },
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    IconButton(onClick = alCerrar) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = TextoSecundario)
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Selector de modo si hay más de 1
                if (modosDisponibles.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Superficie)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        modosDisponibles.forEach { modo ->
                            val seleccionado = modo == modoSeleccionado
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (seleccionado) ColorAcento else Color.Transparent)
                                    .clickable { modoSeleccionado = modo }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = modo.etiqueta,
                                    color = if (seleccionado) Obsidiana else TextoSecundario,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }

                // Marco del código QR de alto contraste
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (qrBitmap != null) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "Código QR offline",
                            modifier = Modifier.size(216.dp)
                        )
                    } else {
                        Text(
                            text = "No se pudo generar el código QR",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Nota informativa y advertencia de seguridad
                Text(
                    text = when (modoSeleccionado) {
                        ModoQr.TOTP -> "Escanea con cualquier app de autenticación para vincular este token 2FA."
                        ModoQr.CONTRASENA -> "Escanea pantalla a pantalla para transferir la contraseña sin internet."
                        ModoQr.CREDENCIAL -> "Transfiere usuario y clave de forma segura y sin conexión."
                    },
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                // Temporizador de cierre por privacidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                        tint = TextoSecundario,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Se cerrará en ${tiempoRestante}s por seguridad",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace)
                    )
                }
            }
        }
    }
}
