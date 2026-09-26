package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun BotonCopiar(copiado: Boolean, alPulsar: () -> Unit) {
    IconButton(onClick = alPulsar) {
        AnimatedVisibility(
            visible = copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Copiado", tint = Menta)
        }
        AnimatedVisibility(
            visible = !copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(Icons.Filled.ContentCopy, contentDescription = "Copiar", tint = ColorIconosInternos)
        }
    }
}

@Composable
fun TarjetaCredencialesDetalle(
    entrada: Entrada,
    revelada: Boolean,
    ultimaCopia: String?,
    alAlternarRevelada: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit
) {
    val tieneCredenciales = entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()
    if (!tieneCredenciales) return

    GrupoAjustes(etiqueta = "Credenciales") {
        if (entrada.usuario.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondoBadgeParaTema(ColorDatosUsuario))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usuario o correo",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = entrada.usuario,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = TextoPrincipal
                        )
                    }
                    BotonCopiar(copiado = ultimaCopia == "usuario", alPulsar = alCopiarUsuario)
                }
                Box(modifier = Modifier.matchParentSize()) {
                    Box(
                        modifier = Modifier
                            .width(4.5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .background(ColorDatosUsuario)
                    )
                }
            }
        }

        if (entrada.usuario.isNotBlank() && entrada.contrasena.isNotBlank()) {
            SeparadorFilaSimple()
        }

        if (entrada.contrasena.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondoBadgeParaTema(ColorDatosContrasena))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Contraseña · ${entrada.contrasena.length} caracteres",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        if (revelada) {
                            Text(
                                text = contrasenaColoreada(entrada.contrasena),
                                style = EstiloMono,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = "•".repeat(entrada.contrasena.length.coerceIn(8, 24)),
                                style = EstiloMonoGrande.copy(letterSpacing = 2.sp),
                                color = TextoSecundario,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Clip,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = alAlternarRevelada) {
                            Icon(
                                imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        BotonCopiar(copiado = ultimaCopia == "contrasena", alPulsar = alCopiarContrasena)
                    }
                }
                Box(modifier = Modifier.matchParentSize()) {
                    Box(
                        modifier = Modifier
                            .width(4.5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .background(ColorDatosContrasena)
                    )
                }
            }
        }
    }
}
