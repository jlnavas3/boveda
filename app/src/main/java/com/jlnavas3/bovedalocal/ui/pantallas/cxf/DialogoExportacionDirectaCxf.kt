package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.cxf.CxfExportador
import com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia
import com.jlnavas3.bovedalocal.cxf.ResultadoLimpiezaExportacion
import com.jlnavas3.bovedalocal.cxf.ResultadoRegistroExportacion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import kotlinx.coroutines.launch

/**
 * Diálogo orquestador para la transferencia directa de credenciales mediante el estándar
 * FIDO CXF (Credential Exchange Format). Admite tanto exportación completa como selectiva.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogoExportacionDirectaCxf(
    entradas: List<Entrada>,
    esSeleccionPersonalizada: Boolean = false,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    var enProgreso by remember { mutableStateOf(false) }
    var estadoHabilitado by remember { mutableStateOf<Boolean?>(null) }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }

    val activas = remember(entradas) { entradas.filter { it.eliminadaEn == 0L } }
    val passkeysCount = remember(activas) { activas.count { it.passkey != null } }
    val contrasenasCount = remember(activas) { activas.count { it.contrasena.isNotBlank() } }
    val totpCount = remember(activas) { activas.count { !it.secretoTotp.isNullOrBlank() } }

    DialogoBoveda(
        onDismissRequest = alCerrar,
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ColorAcento.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.VpnKey,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                text = if (esSeleccionPersonalizada) {
                    "Transferir selección (${activas.size})"
                } else {
                    "Transferencia directa de credenciales"
                },
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (esSeleccionPersonalizada) {
                        "Transfiere únicamente las credenciales seleccionadas directamente a otro gestor o dispositivo Android de forma local, cifrada y sin usar internet."
                    } else {
                        "Transfiere tus llaves de paso, contraseñas y códigos de verificación directamente a otro gestor o dispositivo Android de forma local, cifrada y sin usar internet."
                    },
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Resumen de credenciales
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (passkeysCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.VpnKey,
                            texto = "$passkeysCount llaves de paso",
                            color = ColorPasskeys
                        )
                    }
                    if (contrasenasCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.Password,
                            texto = "$contrasenasCount contraseñas",
                            color = ColorDatosContrasena
                        )
                    }
                    if (totpCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.QrCode,
                            texto = "$totpCount verificación",
                            color = Color2FA
                        )
                    }
                }

                // Banner de estado modular
                if (mensajeEstado != null) {
                    BannerEstadoExportacionCxf(
                        mensaje = mensajeEstado ?: "",
                        estaHabilitado = estadoHabilitado == true
                    )
                }

                if (enProgreso) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp), color = ColorPasskeys)
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Botón compartir archivo CXF
                BotonBoveda(
                    texto = "Compartir archivo FIDO (.cxf)",
                    icono = Icons.Filled.Share,
                    variante = VarianteBoton.SECUNDARIO,
                    alPulsar = {
                        val json = CxfExportador.exportarAJson(activas)
                        CompartidorArchivoCxf.compartir(contexto, json)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            if (estadoHabilitado != true) {
                BotonTextoBoveda(
                    texto = "Habilitar transferencia",
                    colorPersonalizado = ColorAcento,
                    alPulsar = {
                        scope.launch {
                            enProgreso = true
                            when (val res = CxfGestorTransferencia.registrarExportacion(
                                context = contexto,
                                entradas = activas,
                                esSeleccionPersonalizada = esSeleccionPersonalizada
                            )) {
                                is ResultadoRegistroExportacion.Exito -> {
                                    estadoHabilitado = true
                                    mensajeEstado = "Listo para transferir: Abre el otro dispositivo o gestor e inicia la importación."
                                }
                                is ResultadoRegistroExportacion.Error -> {
                                    estadoHabilitado = false
                                    mensajeEstado = res.mensaje
                                }
                            }
                            enProgreso = false
                        }
                    }
                )
            } else {
                BotonTextoBoveda(
                    texto = "Desactivar",
                    colorPersonalizado = ColorAcento,
                    alPulsar = {
                        scope.launch {
                            enProgreso = true
                            when (val res = CxfGestorTransferencia.limpiarExportacion(contexto)) {
                                is ResultadoLimpiezaExportacion.Exito -> {
                                    estadoHabilitado = false
                                    mensajeEstado = "Transferencia directa desactivada."
                                }
                                is ResultadoLimpiezaExportacion.Error -> {
                                    mensajeEstado = res.mensaje
                                }
                            }
                            enProgreso = false
                        }
                    }
                )
            }
        },
        dismissButton = {
            BotonTextoBoveda(
                texto = "Cerrar",
                alPulsar = alCerrar
            )
        }
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun DialogoExportacionDirectaCxfPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        DialogoExportacionDirectaCxf(
            entradas = listOf(
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaBancaria
            ),
            alCerrar = {}
        )
    }
}

