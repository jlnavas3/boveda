package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun TarjetaFormularioDesbloqueo(
    contrasena: String,
    alCambiarContrasena: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    abriendo: Boolean,
    desplazamientoSacudidaX: Float,
    mensajeBiometria: String?,
    biometriaUsable: Boolean,
    etiquetaBotonBiometria: String,
    alDesbloquear: () -> Unit,
    alLanzarBiometria: () -> Unit,
    segundosBloqueo: Long = 0L,
    esUltimoIntentoAntesAutodestruccion: Boolean = false,
    modifier: Modifier = Modifier
) {
    val minutos = segundosBloqueo / 60
    val segs = segundosBloqueo % 60
    val tiempoFormateado = if (minutos > 0) String.format("%02d:%02d", minutos, segs) else "${segundosBloqueo}s"
    val estaBloqueado = segundosBloqueo > 0

    ComponenteGrupo(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(desplazamientoSacudidaX.toInt(), 0) },
        etiqueta = "Acceso a la bóveda",
        descripcion = mensajeBiometria,
        descripcionComoPie = true
    ) {
        if (estaBloqueado) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clip(FormaCampo)
                    .background(Peligro.copy(alpha = 0.12f))
                    .border(1.dp, Peligro.copy(alpha = 0.5f), FormaCampo)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LockClock,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Bóveda bloqueada por demasiados intentos.\nPodrás intentar de nuevo en $tiempoFormateado",
                    color = Peligro,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp
                    )
                )
            }
        }

        if (esUltimoIntentoAntesAutodestruccion && !estaBloqueado) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clip(FormaCampo)
                    .background(Peligro.copy(alpha = 0.16f))
                    .border(1.5.dp, Peligro, FormaCampo)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "⚠️ ATENCIÓN: Este es tu ÚLTIMO intento permitido. Si fallas la contraseña, se autodestruirán todos los datos de la bóveda.",
                    color = Peligro,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
            }
        }

        Box(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = contrasena,
                etiqueta = if (estaBloqueado) "Bloqueado temporalmente" else "Contraseña maestra",
                alCambiar = { if (!abriendo && !estaBloqueado) alCambiarContrasena(it) },
                tipo = TipoCampoTexto.CONTRASENA,
                mostrarIcono = true,
                colorIcono = if (estaBloqueado) Peligro else ColorIconosInternos,
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = { if (!abriendo && !estaBloqueado) alAlternarMostrarContrasena() },
                monoespaciada = mostrarContrasena,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { if (!estaBloqueado) alDesbloquear() }),
                readOnly = abriendo || estaBloqueado
            )
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        val botonActivo = contrasena.isNotEmpty() && !abriendo && !estaBloqueado

        ComponenteBotonFila(
            titulo = when {
                abriendo -> "Abriendo..."
                estaBloqueado -> "Espera $tiempoFormateado"
                else -> "Abrir bóveda"
            },
            alPulsar = { if (!estaBloqueado) alDesbloquear() },
            icono = if (estaBloqueado) Icons.Filled.LockClock else Icons.Filled.LockOpen,
            colorIcono = if (botonActivo) ColorIconosInternos else ColorAjusteGris.copy(alpha = 0.35f),
            colorTinteIcono = if (botonActivo) Color.White else ColorAjusteGris,
            habilitado = botonActivo
        )

        if (biometriaUsable && !estaBloqueado) {
            BotonDesbloqueoBiometrico(
                etiqueta = etiquetaBotonBiometria,
                habilitado = !abriendo,
                alPulsar = alLanzarBiometria
            )
        }
    }
}
