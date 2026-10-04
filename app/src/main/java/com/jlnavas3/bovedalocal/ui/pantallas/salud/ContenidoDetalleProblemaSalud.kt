package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena

/**
 * Contenido textual y credenciales de la fila de un problema de salud de la bóveda.
 */
@Composable
fun ColumnScope.ContenidoDetalleProblemaSalud(
    entrada: Entrada,
    etiquetaDetalle: String,
    ocultarUsuario: Boolean,
    estiloOcultamiento: String,
    seguridadVisualActiva: Boolean,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit
) {
    // Fila 1: Título y Badge de diagnóstico
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = entrada.titulo.ifBlank { "Sin título" },
            color = ColorTextoAjustes,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .clip(FormaPequena)
                .background(ColorCampoAjustes)
                .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
                .padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            Text(
                text = etiquetaDetalle,
                color = ColorAcento,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }

    Spacer(Modifier.height(2.dp))

    // Fila 2: Usuario / Correo
    val usuarioTexto = entrada.usuario.ifBlank { "Sin usuario" }
    if (entrada.usuario.isNotBlank() && ocultarUsuario) {
        TextoSeguroVisual(
            texto = entrada.usuario,
            oculto = true,
            estilo = estiloOcultamiento,
            estiloTexto = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            colorTexto = ColorTextoAjustes.copy(alpha = 0.85f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        Text(
            text = usuarioTexto,
            color = if (entrada.usuario.isNotBlank()) ColorTextoAjustes.copy(alpha = 0.85f) else ColorAjusteGris,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Spacer(Modifier.height(2.dp))

    // Fila 3: Contraseña (oculta por puntos + ojo)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (entrada.contrasena.isBlank()) {
            Text(
                text = "Sin contraseña",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        } else {
            TextoSeguroVisual(
                texto = entrada.contrasena,
                oculto = !mostrarContrasena,
                estilo = if (seguridadVisualActiva) estiloOcultamiento else "puntos_reales",
                estiloTexto = if (mostrarContrasena) {
                    MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                } else {
                    MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                },
                colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
        if (entrada.contrasena.isNotBlank()) {
            IconButton(
                onClick = alAlternarMostrarContrasena,
                modifier = Modifier.size(22.dp)
            ) {
                Icon(
                    imageVector = if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }

    // Fila 4+: Enlaces web
    val urlsLimpias = remember(entrada.urls) { entrada.urls.filter { it.isNotBlank() } }
    if (urlsLimpias.isNotEmpty()) {
        Spacer(Modifier.height(2.dp))
        urlsLimpias.forEach { url ->
            Text(
                text = url,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
