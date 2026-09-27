package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Fuerza

/**
 * Tarjeta de entrada y validación interactiva de la contraseña maestra durante el onboarding.
 */
@Composable
fun TarjetaCredencialesMaestras(
    contrasena: String,
    repetida: String,
    alCambiarContrasena: (String) -> Unit,
    alCambiarRepetida: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    mostrarRepetida: Boolean,
    alAlternarMostrarRepetida: () -> Unit,
    fuerza: Fuerza,
    coinciden: Boolean,
    alCompartir: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Credenciales maestras",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = contrasena,
                etiqueta = "Contraseña maestra",
                alCambiar = alCambiarContrasena,
                tipo = TipoCampoTexto.CONTRASENA,
                mostrarIcono = true,
                icono = Icons.Filled.Lock,
                colorIcono = ColorIconosInternos,
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = alAlternarMostrarContrasena,
                monoespaciada = mostrarContrasena
            )

            Spacer(Modifier.height(10.dp))

            ComponenteCampoTexto(
                valor = repetida,
                etiqueta = "Repite la contraseña",
                alCambiar = alCambiarRepetida,
                tipo = TipoCampoTexto.CONTRASENA,
                mostrarIcono = true,
                icono = Icons.Filled.Lock,
                colorIcono = ColorIconosInternos,
                mostrarContrasena = mostrarRepetida,
                alAlternarMostrarContrasena = alAlternarMostrarRepetida,
                monoespaciada = mostrarRepetida
            )

            if (contrasena.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                BarraFuerza(
                    fraccion = fuerza.fraccion,
                    etiqueta = fuerza.etiqueta,
                    tiempo = fuerza.tiempo,
                    bits = fuerza.bits
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (repetida.isEmpty()) "Mínimo 10 caracteres" else if (coinciden) "✓ Las contraseñas coinciden" else "✖ No coinciden",
                    color = if (repetida.isNotEmpty() && !coinciden) Peligro else if (coinciden) Menta else TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )

                if (contrasena.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorAcento.copy(alpha = 0.12f))
                            .clickable(onClick = alCompartir),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Compartir",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
