package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoSuperficie
import com.jlnavas3.bovedalocal.ui.componentes.EstadoValidacion
import com.jlnavas3.bovedalocal.ui.componentes.TextoValidacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
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
                val estadoValidacion = when {
                    repetida.isEmpty() -> EstadoValidacion.NEUTRO
                    coinciden -> EstadoValidacion.EXITO
                    else -> EstadoValidacion.ERROR
                }
                val mensajeValidacion = when {
                    repetida.isEmpty() -> "Mínimo 10 caracteres"
                    coinciden -> "✓ Las contraseñas coinciden"
                    else -> "✖ No coinciden"
                }

                TextoValidacion(
                    texto = mensajeValidacion,
                    estado = estadoValidacion
                )

                if (contrasena.isNotEmpty()) {
                    BotonIconoSuperficie(
                        icono = Icons.Filled.Share,
                        descripcion = "Compartir",
                        alPulsar = alCompartir
                    )
                }
            }
        }
    }
}
