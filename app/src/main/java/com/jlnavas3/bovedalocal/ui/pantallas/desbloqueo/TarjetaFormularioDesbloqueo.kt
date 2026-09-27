package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

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
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(desplazamientoSacudidaX.toInt(), 0) },
        etiqueta = "Acceso a la bóveda",
        descripcion = mensajeBiometria,
        descripcionComoPie = true
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = contrasena,
                etiqueta = "Contraseña maestra",
                alCambiar = { if (!abriendo) alCambiarContrasena(it) },
                tipo = TipoCampoTexto.CONTRASENA,
                mostrarIcono = true,
                colorIcono = ColorIconosInternos,
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = { if (!abriendo) alAlternarMostrarContrasena() },
                monoespaciada = mostrarContrasena,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { alDesbloquear() }),
                readOnly = abriendo
            )
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        val botonActivo = contrasena.isNotEmpty() && !abriendo

        ComponenteBotonFila(
            titulo = if (abriendo) "Abriendo..." else "Abrir bóveda",
            alPulsar = { alDesbloquear() },
            icono = Icons.Filled.LockOpen,
            colorIcono = if (botonActivo) ColorIconosInternos else ColorAjusteGris.copy(alpha = 0.35f),
            colorTinteIcono = if (botonActivo) Color.White else ColorAjusteGris,
            habilitado = botonActivo
        )

        if (biometriaUsable) {
            ComponenteSeparador(sangriaInicio = 60.dp)
            ComponenteBotonFila(
                titulo = etiquetaBotonBiometria,
                alPulsar = { alLanzarBiometria() },
                icono = Icons.Filled.Fingerprint,
                colorIcono = Color(0xFF1E88E5),
                colorTinteIcono = Color.White,
                habilitado = !abriendo
            )
        }
    }
}
