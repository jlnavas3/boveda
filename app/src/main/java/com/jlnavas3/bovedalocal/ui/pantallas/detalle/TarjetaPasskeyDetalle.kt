/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TarjetaPasskeyDetalle(
    passkey: DatosPasskey,
    usuarioEntrada: String = "",
    fechaRegistro: Long = 0L,
    mostrarDatosTecnicos: Boolean = false,
    vm: VaultViewModel? = null,
    haptica: Haptica? = null
) {
    val contexto = LocalContext.current
    val rpName = passkey.rpName.trim()
    val rpId = passkey.rpId.trim()
    val hayNombreDistinto = rpName.isNotBlank() && !rpName.equals(rpId, ignoreCase = true)

    val usuarioPasskey = passkey.usuario.trim()
    val usuarioBase = usuarioEntrada.trim()
    val mostrarCuenta = usuarioPasskey.isNotBlank() && !usuarioPasskey.equals(usuarioBase, ignoreCase = true)

    var expandido by remember { mutableStateOf(false) }
    var itemCopiado by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(itemCopiado) {
        if (itemCopiado != null) {
            delay(2000)
            itemCopiado = null
        }
    }

    fun copiarDato(etiqueta: String, valor: String, claveItem: String) {
        haptica?.exito()
        if (vm != null) {
            vm.copiar(etiqueta, valor, sensible = false)
        } else {
            Portapapeles.copiar(contexto, etiqueta, valor)
        }
        itemCopiado = claveItem
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Llave de acceso")
        TarjetaDatoDetalle(colorBorde = ColorDatosPasskey) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .animateContentSize()
            ) {
                // Fila superior: Título principal a la izquierda y fecha/hora a la derecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (hayNombreDistinto) rpName else rpId.ifBlank { "Llave de acceso WebAuthn" },
                        color = ColorDatosPasskey,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (fechaRegistro > 0L) {
                        val fechaCorta = remember(fechaRegistro) {
                            SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES")).format(Date(fechaRegistro))
                        }
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ColorDatosPasskey.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = fechaCorta,
                                color = ColorDatosPasskey,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Dominio (solo si difiere del título)
                if (hayNombreDistinto) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Dominio: $rpId",
                        color = ColorDatosPasskey.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }

                // Cuenta (solo si difiere de la mostrada arriba)
                if (mostrarCuenta) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Cuenta: $usuarioPasskey",
                        color = ColorDatosPasskey.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }

                Spacer(Modifier.height(2.dp))
                Text(
                    "Algoritmo: ${passkey.algoritmo} (P-256)",
                    color = ColorDatosPasskey.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                )



                if (mostrarDatosTecnicos) {
                    Spacer(Modifier.height(6.dp))

                    // Enlace interactivo en la parte inferior alineado a la derecha
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                haptica?.toque()
                                expandido = !expandido
                            }
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = ColorDatosPasskey,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        val flecha = if (expandido) "⌃" else "⌄"
                        Text(
                            text = "Datos técnicos $flecha",
                            color = ColorDatosPasskey,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }

                    if (expandido) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Información técnica para usuarios avanzados. Útil para identificar y contrastar esta llave en la configuración de seguridad de la página web.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, lineHeight = 15.sp)
                        )

                        if (passkey.credId.isNotBlank()) {
                            Spacer(Modifier.height(10.dp))
                            FilaDatoTecnicoPasskey(
                                etiqueta = "Credential ID:",
                                valor = passkey.credId.trim(),
                                esMonospace = true,
                                copiado = itemCopiado == "credId",
                                alCopiar = { copiarDato("Credential ID", passkey.credId.trim(), "credId") }
                            )
                        }

                        if (passkey.userHandle.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            FilaDatoTecnicoPasskey(
                                etiqueta = "User Handle:",
                                valor = passkey.userHandle.trim(),
                                esMonospace = true,
                                copiado = itemCopiado == "userHandle",
                                alCopiar = { copiarDato("User Handle", passkey.userHandle.trim(), "userHandle") }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaPasskeyDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaPasskeyDetalle(
            passkey = DatosPasskey(
                rpId = "google.com",
                rpName = "Google Accounts",
                userHandle = "user-12345",
                credId = "cred-passkey-abc-123456789-xyz",
                clavePrivada = "privkey-mock",
                usuario = "usuario@correo.com"
            ),
            fechaRegistro = 1728919800000L
        )
    }
}
