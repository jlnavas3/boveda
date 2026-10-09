package com.jlnavas3.bovedalocal.ui.pantallas.salud

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
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
import androidx.compose.ui.graphics.Color
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
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada

/**
 * Microcomponente que muestra una fila interactiva para una entrada con problemas de salud
 * (débil, repetida, comprometida o antigua) con acciones de deslizamiento para cambiar clave o ignorar.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilaProblemaAgil(
    entrada: Entrada,
    etiquetaDetalle: String,
    colorDetalle: Color,
    alCambiarRapido: () -> Unit,
    alVerDetalle: () -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    alIgnorar: () -> Unit = {},
    mostrarIndicadores: Boolean = false,
    enGrupo: Boolean = true,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    alAlternarSeleccion: (() -> Unit)? = null,
    alPulsarLargo: (() -> Unit)? = null
) {
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

    val forma = if (enGrupo) RectangleShape else RoundedCornerShape(CurvaturaEsquinas)
    val fondo = if (seleccionado) ColorAcento.copy(alpha = 0.22f) else if (enGrupo) Color.Transparent else ColorTarjetaAjustes

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val ocultarUsuario = seguridadVisualActiva && (ajustes?.ocultarUsuario == true)
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }

    TemporizadorAutoOcultar(
        revelado = mostrarContrasena,
        tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
    ) {
        mostrarContrasena = false
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        enGrupo = enGrupo,
        accionIzquierda = AccionDeslizamiento(
            texto = "Cambiar\nClave",
            icono = Icons.Filled.AutoFixHigh,
            color = ColorAcento,
            alEjecutar = alCambiarRapido
        ),
        accionDerecha = AccionDeslizamiento(
            texto = "Ignorar\nAlerta",
            icono = Icons.Filled.Check,
            color = Borde,
            alEjecutar = alIgnorar
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
                    .padding(horizontal = 14.dp, vertical = 11.dp),
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
                        tamanoDp = 36.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .border(0.8.dp, ColorSeparadorAjustes, FormaPequena),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoTipo,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    ContenidoDetalleProblemaSalud(
                        entrada = entrada,
                        etiquetaDetalle = etiquetaDetalle,
                        ocultarUsuario = ocultarUsuario,
                        estiloOcultamiento = estiloOcultamiento,
                        seguridadVisualActiva = seguridadVisualActiva,
                        mostrarContrasena = mostrarContrasena,
                        alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena }
                    )
                }
            }
        }
    }
}
