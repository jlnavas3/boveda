package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
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
        TipoEntrada.CONTACTO -> Icons.Filled.Person
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
                    ContenidoDetalleFilaDuplicada(
                        entrada = entrada,
                        esSugerida = esSugerida,
                        ocultarUsuario = ocultarUsuario,
                        estiloOcultamiento = estiloOcultamiento,
                        seguridadVisualActiva = seguridadVisualActiva,
                        mostrarContrasena = mostrarContrasena,
                        mostrarTotp = mostrarTotp,
                        alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                        alAlternarMostrarTotp = { mostrarTotp = !mostrarTotp }
                    )
                }
            }
        }
    }
}
