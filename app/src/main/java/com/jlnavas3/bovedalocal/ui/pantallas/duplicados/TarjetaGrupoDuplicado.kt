package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual

private fun obtenerPrimerGrupoPasskey(credId: String): String {
    val limpio = credId.trim()
    return when {
        limpio.isBlank() -> "—"
        limpio.contains("-") -> limpio.substringBefore("-")
        limpio.contains(":") -> limpio.substringBefore(":")
        limpio.length > 10 -> limpio.take(10)
        else -> limpio
    }
}

@Composable
fun TarjetaGrupoDuplicado(
    grupo: GrupoDuplicado,
    expandido: Boolean,
    alAlternar: () -> Unit,
    alConservar: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: (String) -> Unit = {},
    alPulsarLargo: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ComponenteGrupoLista(
        clave = grupo.claveVisual,
        entradas = grupo.entradas,
        expandido = expandido,
        alAlternar = alAlternar,
        modifier = modifier,
        contenidoEntrada = { entrada, _, _ ->
            FilaEntradaDuplicada(
                entrada = entrada,
                esSugerida = entrada.id == grupo.sugeridaPrincipal.id,
                alConservar = { alConservar(entrada) },
                alVerDetalle = { alVerDetalle(entrada.id) },
                ajustes = ajustes,
                mostrarIndicadores = mostrarIndicadores,
                enGrupo = true,
                seleccionActiva = seleccionActiva,
                seleccionado = seleccionados.contains(entrada.id),
                alAlternarSeleccion = { alAlternarSeleccion(entrada.id) },
                alPulsarLargo = { alPulsarLargo(entrada.id) }
            )
        }
    )
}

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

    val (iconoTipo, colorTipo) = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock to ColorSeguridad
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint to ColorPasskeys
        TipoEntrada.NOTA -> Icons.Filled.Description to ColorAcento
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard to ColorGenerador
        TipoEntrada.WIFI -> Icons.Filled.Wifi to ColorSalud
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance to ColorSeguridad
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge to ColorExportacion
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns to ColorIconosInternos
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet to ColorAcento
    }
    val colorLegible = colorLegibleParaTema(colorTipo)
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

                    // Usuario (sin prefijo "Usuario: ")
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

                    // Contraseña (oculta con ícono de ojo a la derecha)
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

                    // Llave de paso (Passkey) si está presente
                    if (entrada.passkey != null) {
                        Spacer(Modifier.height(2.dp))
                        val primerGrupoPasskey = remember(entrada.passkey.credId) {
                            obtenerPrimerGrupoPasskey(entrada.passkey.credId)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Fingerprint,
                                contentDescription = null,
                                tint = ColorPasskeys,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Passkey: $primerGrupoPasskey...",
                                color = ColorTextoAjustes.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Verificación de dos pasos (TOTP) si está presente
                    if (!entrada.secretoTotp.isNullOrBlank()) {
                        val codigoTotp = remember(entrada.secretoTotp, entrada.totpDigitos, entrada.totpPeriodo, entrada.totpAlgoritmo) {
                            try {
                                val secretoBytes = Base32.decodificar(entrada.secretoTotp.replace(" ", "").uppercase())
                                val ahora = System.currentTimeMillis() / 1000
                                val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(1L)
                                val digitos = entrada.totpDigitos.coerceIn(6, 8)
                                val algo = when (entrada.totpAlgoritmo.uppercase()) {
                                    "SHA256", "HMACSHA256" -> "HmacSHA256"
                                    "SHA512", "HMACSHA512" -> "HmacSHA512"
                                    else -> "HmacSHA1"
                                }
                                Totp.codigo(secretoBytes, ahora, digitos, periodo, algo)
                            } catch (_: Exception) {
                                "------"
                            }
                        }

                        Spacer(Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val textoTotp = if (codigoTotp.length == 6) "${codigoTotp.take(3)} ${codigoTotp.drop(3)}" else codigoTotp
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Text(
                                    text = "TOTP: ",
                                    color = ColorAjusteGris,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                                TextoSeguroVisual(
                                    texto = textoTotp,
                                    oculto = !mostrarTotp,
                                    estilo = if (seguridadVisualActiva) estiloOcultamiento else "puntos_fijos",
                                    estiloTexto = if (mostrarTotp) {
                                        MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp
                                        )
                                    } else {
                                        MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                                    },
                                    colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { mostrarTotp = !mostrarTotp },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Icon(
                                    imageVector = if (mostrarTotp) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (mostrarTotp) "Ocultar TOTP" else "Mostrar TOTP",
                                    tint = ColorAjusteGris,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    // URLs (uno por línea debajo de la contraseña)
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

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaGrupoDuplicadoPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val entrada1 = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo
        val entrada2 = entrada1.copy(id = "mock-dup-2", titulo = "Google (Copia)")
        val grupo = GrupoDuplicado(
            idGrupo = "grupo-1",
            tipo = com.jlnavas3.bovedalocal.data.TipoDuplicado.IDENTICO,
            claveVisual = "google.com",
            entradas = listOf(entrada1, entrada2),
            sugeridaPrincipal = entrada1
        )
        TarjetaGrupoDuplicado(
            grupo = grupo,
            expandido = true,
            alAlternar = {},
            alConservar = {},
            alVerDetalle = {}
        )
    }
}

