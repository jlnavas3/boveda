/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaBusqueda(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Búsqueda avanzada",
        alVolver = alVolver,
        textoAccion = "Ir al listado principal",
        iconoAccion = Icons.Filled.Search,
        alEjecutarAccion = { vm.ir(Pantalla.Lista) }
    ) {
        TarjetaInformativaAyuda(titulo = "¿Cómo funciona la búsqueda avanzada?") {
            Text(
                text = "Además de buscar por texto libre, puedes escribir comandos con prefijo para filtrar instantáneamente entre cientos de entradas en tu bóveda local.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Comandos por tipo de entrada") {
            val tipos = listOf(
                "t:login" to "Contraseñas y cuentas de usuario",
                "t:tarjeta" to "Tarjetas bancarias de débito y crédito",
                "t:wifi" to "Redes Wi-Fi y claves de acceso",
                "t:banco" to "Cuentas bancarias (IBAN / BIC)",
                "t:servidor" to "Servidores, IPs y claves SSH",
                "t:wallet" to "Billeteras criptográficas y semillas",
                "t:passkey" to "Llaves de paso FIDO2 / WebAuthn",
                "t:nota" to "Notas seguras cifradas",
                "t:contacto" to "Contactos de emergencia"
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                tipos.forEach { (cmd, desc) ->
                    FilaComando(cmd, desc)
                }
            }
        }

        TarjetaInformativaAyuda(titulo = "Comandos por propiedades y estado") {
            val props = listOf(
                "has:totp" to "Entradas con autenticador 2FA activo",
                "has:passkey" to "Entradas con llave Passkey vinculada",
                "has:nota" to "Entradas que contienen notas adicionales",
                "is:fav" to "Entradas marcadas como favorito",
                "is:ignorado" to "Entradas excluidas de la auditoría de salud"
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                props.forEach { (cmd, desc) ->
                    FilaComando(cmd, desc)
                }
            }
        }

        TarjetaInformativaAyuda(titulo = "Comandos por taxonomía") {
            val tax = listOf(
                "tag:nombre" to "Filtra por la etiqueta especificada (ej. tag:trabajo)",
                "cat:nombre" to "Filtra por la categoría especificada (ej. cat:finanzas)"
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                tax.forEach { (cmd, desc) ->
                    FilaComando(cmd, desc)
                }
            }
        }

        TarjetaInformativaAyuda(titulo = "Ejemplos combinados") {
            Text(
                text = "Los modificadores se pueden combinar libremente entre sí y con texto general:",
                fontSize = 13.sp,
                color = TextoSecundario
            )
            Spacer(Modifier.height(8.dp))
            BloqueEjemplo(
                comando = "t:login has:totp google",
                explicacion = "Cuentas de Google que tengan contraseña y código 2FA activo."
            )
            Spacer(Modifier.height(6.dp))
            BloqueEjemplo(
                comando = "is:fav tag:servidor",
                explicacion = "Entradas favoritas etiquetadas con 'servidor'."
            )
        }
    }
}

@Composable
private fun FilaComando(comando: String, descripcion: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = comando,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = ColorAcento,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = descripcion,
            fontSize = 12.sp,
            color = TextoPrincipal
        )
    }
}

@Composable
private fun BloqueEjemplo(comando: String, explicacion: String) {
    val forma = RoundedCornerShape(CurvaturaEsquinas / 2)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(0.8.dp, ColorBordeActual.copy(alpha = 0.5f), forma)
            .padding(10.dp)
    ) {
        Text(
            text = comando,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = ColorAcento,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = explicacion,
            fontSize = 11.5.sp,
            color = TextoSecundario
        )
    }
}
