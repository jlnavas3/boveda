package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada

/**
 * Fila interactiva para cada entrada duplicada con acciones de deslizamiento,
 * soporte para selección múltiple y ocultamiento seguro de credenciales.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilaEntradaDuplicada(
    entrada: Entrada,
    esSugerida: Boolean,
    alConservar: () -> Unit,
    alVerDetalle: () -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    enGrupo: Boolean = false,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    alAlternarSeleccion: (() -> Unit)? = null,
    alPulsarLargo: (() -> Unit)? = null
) {
    val forma = if (enGrupo) RectangleShape else RoundedCornerShape(CurvaturaEsquinas)
    val fondo = if (seleccionado) ColorAcento.copy(alpha = 0.22f) else ColorTarjetaAjustes

    val iconoTipo = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
    }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val ocultarUsuario = seguridadVisualActiva && (ajustes?.ocultarUsuario == true)
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    var mostrarTotp by rememberSaveable { mutableStateOf(false) }

    TemporizadorAutoOcultar(
        revelado = mostrarContrasena,
        tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
    ) {
        mostrarContrasena = false
    }

    TemporizadorAutoOcultar(
        revelado = mostrarTotp,
        tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
    ) {
        mostrarTotp = false
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        enGrupo = enGrupo,
        accionIzquierda = AccionDeslizamiento(
            texto = "Conservar\nCopia",
            icono = Icons.Filled.DoneAll,
            color = Menta,
            alEjecutar = alConservar
        ),
        accionDerecha = AccionDeslizamiento(
            texto = "Ver\nDetalle",
            icono = Icons.AutoMirrored.Filled.ArrowForwardIos,
            color = ColorAcento,
            alEjecutar = alVerDetalle
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(fondo)
                .then(
                    if (seleccionado) Modifier.border(1.dp, ColorAcento.copy(alpha = 0.5f), forma)
                    else if (!enGrupo && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
        ) {
            if (mostrarIndicadores) {
                IndicadorContenidoTarjeta(
                    entrada = entrada,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            if (seleccionActiva) {
                                alAlternarSeleccion?.invoke()
                            } else {
                                alVerDetalle()
                            }
                        },
                        onLongClick = {
                            if (!seleccionActiva && alPulsarLargo != null) {
                                alPulsarLargo()
                            }
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (seleccionActiva) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (seleccionado) ColorAcento else Borde),
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorSobreAcento,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }

                val iconoApp = rememberIconoAppInstalada(entrada)
                if (iconoApp != null) {
                    IconoAppCircular(
                        bitmap = iconoApp,
                        descripcion = entrada.titulo,
                        tamanoDp = 34.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .border(0.8.dp, ColorSeparadorAjustes, FormaPequena),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoTipo,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Título y badge Sugerida
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = entrada.titulo.ifBlank { "Sin título" },
                            color = ColorTextoAjustes,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (esSugerida) {
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
                                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    "Sugerida",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    ),
                                    color = ColorAcento
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    // Usuario
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

                    // Contraseña
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
                                onClick = { mostrarContrasena = !mostrarContrasena },
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

                    // Passkey si existe
                    FilaPasskeyDuplicada(passkey = entrada.passkey)

                    // TOTP si existe
                    FilaTotpDuplicada(
                        entrada = entrada,
                        mostrarTotp = mostrarTotp,
                        alAlternarMostrarTotp = { mostrarTotp = !mostrarTotp },
                        seguridadVisualActiva = seguridadVisualActiva,
                        estiloOcultamiento = estiloOcultamiento
                    )

                    // URLs
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
            }
        }
    }
}
