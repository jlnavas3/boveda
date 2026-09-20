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
import androidx.compose.ui.text.style.TextOverflow
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
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Wifi
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.util.Haptica

private enum class ModoQr(val etiqueta: String) {
    WIFI("Conectar Wi-Fi"),
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
    val haptica = remember { Haptica(contexto) }

    val ssidWifi = remember(entrada) {
        GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre de red (SSID)")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "SSID") }
            .ifBlank { entrada.titulo }
            .trim()
    }
    val claveWifi = remember(entrada) {
        GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña Wi-Fi")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña") }
            .ifBlank { entrada.contrasena }
    }
    val esWifi = remember(entrada, ssidWifi) {
        entrada.tipo == TipoEntrada.WIFI ||
            GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre de red (SSID)").isNotBlank()
    }

    val tieneTotp = !entrada.secretoTotp.isNullOrBlank()
    val tieneContrasena = entrada.contrasena.isNotBlank() || (esWifi && claveWifi.isNotBlank())

    val modosDisponibles = remember(entrada, esWifi, tieneTotp, tieneContrasena) {
        buildList {
            if (esWifi) add(ModoQr.WIFI)
            if (tieneTotp) add(ModoQr.TOTP)
            if (tieneContrasena) add(ModoQr.CONTRASENA)
            add(ModoQr.CREDENCIAL)
        }
    }

    var modoSeleccionado by remember {
        mutableStateOf(
            when {
                esWifi -> ModoQr.WIFI
                tieneTotp -> ModoQr.TOTP
                tieneContrasena -> ModoQr.CONTRASENA
                else -> ModoQr.CREDENCIAL
            }
        )
    }

    val textoQr = remember(entrada, modoSeleccionado, esWifi, claveWifi) {
        when (modoSeleccionado) {
            ModoQr.WIFI -> GeneradorQr.textoWifiDesdeEntrada(entrada)
            ModoQr.TOTP -> GeneradorQr.uriTotp(entrada) ?: entrada.titulo
            ModoQr.CONTRASENA -> if (esWifi) claveWifi else entrada.contrasena
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
        val fondoDialogo = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
        val fondoSelector = if (esOscuroActivo) Color(0xFF161518) else Color(0xFFEFEFF2)

        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(fondoDialogo)
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Cabecera limpia con ícono sólido estilo MagicOS, título, botón de copiar y cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(ColorAcento),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (esWifi && modoSeleccionado == ModoQr.WIFI) Icons.Filled.Wifi else Icons.Filled.QrCode,
                                contentDescription = null,
                                tint = com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (esWifi && modoSeleccionado == ModoQr.WIFI) "Conectar a Wi-Fi" else "Compartir por QR",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            )
                            Text(
                                text = if (esWifi && ssidWifi.isNotBlank()) "Red: $ssidWifi" else entrada.titulo.ifBlank { "Credencial" },
                                color = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                haptica.toque()
                                if (modoSeleccionado == ModoQr.CONTRASENA) {
                                    Portapapeles.copiarSensible(contexto, "Contraseña", textoQr)
                                } else {
                                    Portapapeles.copiar(contexto, "Contenido QR", textoQr)
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copiar texto del QR",
                                tint = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        IconButton(
                            onClick = alCerrar,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cerrar",
                                tint = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Selector de modo tipo pastilla segmentada nativa (sin bordes)
                if (modosDisponibles.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(fondoSelector)
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        modosDisponibles.forEach { modo ->
                            val seleccionado = modo == modoSeleccionado
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(9.dp))
                                    .background(if (seleccionado) ColorAcento else Color.Transparent)
                                    .clickable { haptica.tic(); modoSeleccionado = modo }
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = modo.etiqueta,
                                    color = if (seleccionado) com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento else com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.5.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Marco del código QR estilizado con esquinas redondeadas y sin bordes
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
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

                Spacer(Modifier.height(14.dp))

                // Nota informativa y advertencia de seguridad
                Text(
                    text = when (modoSeleccionado) {
                        ModoQr.WIFI -> "Escanea este código con la cámara de otro teléfono para conectarte automáticamente a la red Wi-Fi."
                        ModoQr.TOTP -> "Escanea con tu aplicación de autenticación para vincular este token 2FA."
                        ModoQr.CONTRASENA -> "Escanea pantalla a pantalla para transferir únicamente la contraseña."
                        ModoQr.CREDENCIAL -> "Transfiere los datos de la cuenta de forma segura y 100% offline."
                    },
                    color = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(14.dp))

                // Cápsula de temporizador en tono ámbar / seguridad
                Row(
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(if (esOscuroActivo) Color(0xFF2B2215) else Color(0xFFFFF3E0))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = null,
                        tint = if (esOscuroActivo) Color(0xFFFFB74D) else Color(0xFFB45309),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Se cerrará en ${tiempoRestante}s por seguridad",
                        color = if (esOscuroActivo) Color(0xFFFFB74D) else Color(0xFFB45309),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
