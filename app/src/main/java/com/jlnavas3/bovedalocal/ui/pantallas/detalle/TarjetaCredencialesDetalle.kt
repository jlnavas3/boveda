package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Deprecated("Usar BotonCopiarDetalle", ReplaceWith("BotonCopiarDetalle(copiado, alPulsar)"))
@Composable
fun BotonCopiar(copiado: Boolean, alPulsar: () -> Unit) {
    BotonCopiarDetalle(copiado = copiado, alPulsar = alPulsar)
}

@Composable
fun TarjetaCredencialesDetalle(
    entrada: Entrada,
    revelada: Boolean,
    ultimaCopia: String?,
    alAlternarRevelada: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    ajustes: AjustesApp? = null
) {
    val tieneCredenciales = entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()
    if (!tieneCredenciales) return

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = if (seguridadVisualActiva) (ajustes?.tiempoAutoOcultarSegundos ?: 10) else 0

    // Estado y temporizador para usuario
    val protegerUsuario = seguridadVisualActiva && (ajustes?.ocultarUsuario == true)
    var usuarioRevelado by remember(entrada.id, protegerUsuario) { mutableStateOf(!protegerUsuario) }

    TemporizadorAutoOcultar(
        revelado = usuarioRevelado && protegerUsuario,
        tiempoSegundos = tiempoAutoOcultar,
        alAutoOcultar = { usuarioRevelado = false }
    )

    // Temporizador para contraseña
    TemporizadorAutoOcultar(
        revelado = revelada && seguridadVisualActiva,
        tiempoSegundos = tiempoAutoOcultar,
        alAutoOcultar = { if (revelada) alAlternarRevelada() }
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Credenciales")

        if (entrada.usuario.isNotBlank()) {
            TarjetaDatoDetalle(
                colorBorde = ColorDatosUsuario
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
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
                        TextoSeguroVisual(
                            texto = entrada.usuario,
                            oculto = protegerUsuario && !usuarioRevelado,
                            estilo = estiloOcultamiento,
                            estiloTexto = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            colorTexto = TextoPrincipal
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (protegerUsuario) {
                            BotonIconoDetalle(
                                icono = if (usuarioRevelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (usuarioRevelado) "Ocultar usuario" else "Mostrar usuario",
                                alPulsar = { usuarioRevelado = !usuarioRevelado }
                            )
                        }
                        BotonCopiarDetalle(copiado = ultimaCopia == "usuario", alPulsar = alCopiarUsuario)
                    }
                }
            }
        }

        if (entrada.usuario.isNotBlank() && entrada.contrasena.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
        }

        if (entrada.contrasena.isNotBlank()) {
            TarjetaDatoDetalle(
                colorBorde = ColorDatosContrasena
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
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
                            TextoSeguroVisual(
                                texto = entrada.contrasena,
                                oculto = true,
                                estilo = if (seguridadVisualActiva) estiloOcultamiento else "puntos_reales",
                                estiloTexto = EstiloMonoGrande.copy(letterSpacing = 2.sp),
                                colorTexto = TextoSecundario,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonIconoDetalle(
                            icono = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                            alPulsar = alAlternarRevelada
                        )
                        BotonCopiarDetalle(copiado = ultimaCopia == "contrasena", alPulsar = alCopiarContrasena)
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
private fun TarjetaCredencialesDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TarjetaCredencialesDetalle(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                revelada = false,
                ultimaCopia = null,
                alAlternarRevelada = {},
                alCopiarUsuario = {},
                alCopiarContrasena = {}
            )
            TarjetaCredencialesDetalle(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                revelada = true,
                ultimaCopia = "contrasena",
                alAlternarRevelada = {},
                alCopiarUsuario = {},
                alCopiarContrasena = {}
            )
        }
    }
}

