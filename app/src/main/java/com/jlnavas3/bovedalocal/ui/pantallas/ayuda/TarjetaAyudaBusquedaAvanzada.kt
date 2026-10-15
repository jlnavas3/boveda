/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta explicativa con la guía completa de sintaxis y filtros de búsqueda avanzada.
 */
@Composable
fun TarjetaAyudaBusquedaAvanzada(modifier: Modifier = Modifier) {
    ComponenteGrupo(
        etiqueta = "Búsqueda avanzada por sintaxis",
        icono = Icons.Filled.Search,
        colorIcono = ColorAcento,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Puedes escribir modificadores directamente en la barra de búsqueda para filtrar de forma instantánea:",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(14.dp))

            SeccionComandos(
                titulo = "Por tipo de entrada",
                comandos = listOf(
                    "t:login" to "Contraseñas y accesos",
                    "t:tarjeta" to "Tarjetas de crédito / débito",
                    "t:wifi" to "Redes Wi-Fi",
                    "t:banco" to "Cuentas bancarias",
                    "t:servidor" to "Servidores y claves SSH",
                    "t:wallet" to "Billeteras de criptomonedas",
                    "t:passkey" to "Llaves de paso (FIDO2)",
                    "t:nota" to "Notas seguras",
                    "t:contacto" to "Contactos seguros"
                )
            )

            Spacer(Modifier.height(14.dp))

            SeccionComandos(
                titulo = "Por atributos y estado",
                comandos = listOf(
                    "has:totp" to "Entradas con código 2FA / TOTP",
                    "has:passkey" to "Entradas con llave Passkey",
                    "has:nota" to "Entradas que tienen notas",
                    "is:fav" to "Entradas marcadas como favorito",
                    "is:ignorado" to "Entradas excluidas de auditoría"
                )
            )

            Spacer(Modifier.height(14.dp))

            SeccionComandos(
                titulo = "Por etiquetas y categorías",
                comandos = listOf(
                    "tag:trabajo" to "Filtra por la etiqueta 'trabajo'",
                    "cat:finanzas" to "Filtra por la categoría 'finanzas'"
                )
            )

            Spacer(Modifier.height(14.dp))

            // Ejemplos combinados
            Text(
                text = "EJEMPLOS COMBINADOS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextoSecundario,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(6.dp))

            val formaEjemplo = RoundedCornerShape(CurvaturaEsquinas / 2)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaEjemplo)
                    .background(ColorTarjetaAjustes)
                    .border(1.dp, ColorBordeActual.copy(alpha = 0.4f), formaEjemplo)
                    .padding(10.dp)
            ) {
                Text(
                    text = "t:login has:totp google",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = ColorAcento,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Busca cuentas de Google que tengan contraseña y código 2FA.",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "is:fav tag:servidor",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = ColorAcento,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Muestra únicamente favoritos que tengan la etiqueta 'servidor'.",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}

@Composable
private fun SeccionComandos(
    titulo: String,
    comandos: List<Pair<String, String>>
) {
    Text(
        text = titulo.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextoSecundario,
        letterSpacing = 0.5.sp
    )
    Spacer(Modifier.height(6.dp))
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        comandos.forEach { (comando, descripcion) ->
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
                    modifier = Modifier.width(105.dp)
                )
                Text(
                    text = descripcion,
                    fontSize = 12.sp,
                    color = TextoPrincipal
                )
            }
        }
    }
}
