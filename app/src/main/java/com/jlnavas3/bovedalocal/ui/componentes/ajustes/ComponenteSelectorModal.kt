package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.navigationBarsPadding
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda

/**
 * Modelo de opción para el selector modal estilo Honor MagicOS / Samsung One UI.
 *
 * @param valor Valor subyacente.
 * @param etiquetaFila Texto conciso mostrado en la fila principal (para evitar truncamientos).
 * @param etiquetaModal Título de la opción en el diálogo modal (por defecto igual a [etiquetaFila]).
 * @param descripcionModal Subtítulo o ejemplo detallado mostrado dentro del modal.
 * @param icono Icono descriptivo opcional para la opción.
 */
data class OpcionSelectorModal<T>(
    val valor: T,
    val etiquetaFila: String,
    val etiquetaModal: String = etiquetaFila,
    val descripcionModal: String? = null,
    val icono: ImageVector? = null
)

/**
 * Componente reutilizable de fila con selector modal emergente estilo Samsung One UI & Honor MagicOS.
 *
 * - En la fila: Icono squircle, título limpio, valor actual y flechas arriba/abajo (UnfoldMore).
 * - Al pulsar: Abre un diálogo modal (centrado o fijado abajo) con esquinas pronunciadas (26.dp),
 *   sin bordes pesados ni sombras pero con fondo contrastado y scrim, con radio buttons One UI de acento.
 */
@Composable
fun <T> ComponenteSelectorModal(
    titulo: String,
    valorSeleccionado: T,
    opciones: List<OpcionSelectorModal<T>>,
    alSeleccionar: (T) -> Unit,
    modifier: Modifier = Modifier,
    descripcionModal: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true,
    fijarAbajo: Boolean = false,
    tituloFila: String = titulo
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val scope = rememberCoroutineScope()
    var abierto by remember { mutableStateOf(false) }

    val opcionActual = opciones.firstOrNull { it.valor == valorSeleccionado }
    val textoFila = opcionActual?.etiquetaFila ?: valorSeleccionado.toString()

    ComponenteFila(
        titulo = tituloFila,
        modifier = modifier,
        icono = icono,
        colorIcono = colorIcono,
        colorTinteIcono = colorTinteIcono,
        idFila = idFila,
        mostrarId = mostrarId,
        valorTexto = textoFila,
        habilitado = habilitado,
        alPulsar = { if (habilitado) abierto = true },
        contenidoFinal = {
            Icon(
                imageVector = Icons.Filled.UnfoldMore,
                contentDescription = "Desplegar opciones",
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    )

    if (abierto) {
        val esOscuro = isSystemInDarkTheme()
        // Superficie con contraste suave sin bordes ni sombras pesadas
        val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White
        val colorAcentoFinal = colorIcono ?: ColorAcento

        Dialog(
            onDismissRequest = { abierto = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { abierto = false },
                contentAlignment = if (fijarAbajo) Alignment.BottomCenter else Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = if (fijarAbajo) 0.dp else 32.dp,
                            bottom = if (fijarAbajo) 16.dp else 32.dp
                        )
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(fondoModal)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Evita cerrar al pulsar dentro de la tarjeta */ }
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Cabecera del modal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (icono != null) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(colorAcentoFinal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = null,
                                        tint = colorTinteIcono,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = titulo,
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.5.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (!descripcionModal.isNullOrBlank()) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = descripcionModal,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Lista de opciones con radio buttons
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 380.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            opciones.forEach { opcion ->
                                val esSeleccionado = opcion.valor == valorSeleccionado

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (esSeleccionado) colorAcentoFinal.copy(alpha = 0.12f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            haptica.tic()
                                            alSeleccionar(opcion.valor)
                                            scope.launch {
                                                delay(120)
                                                abierto = false
                                            }
                                        }
                                        .padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (opcion.icono != null) {
                                        Icon(
                                            imageVector = opcion.icono,
                                            contentDescription = null,
                                            tint = if (esSeleccionado) colorAcentoFinal else TextoSecundario,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(12.dp))
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = opcion.etiquetaModal,
                                            color = if (esSeleccionado) colorAcentoFinal else TextoPrincipal,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (esSeleccionado) FontWeight.SemiBold else FontWeight.Normal,
                                                fontSize = 15.sp
                                            )
                                        )
                                        if (!opcion.descripcionModal.isNullOrBlank()) {
                                            Spacer(Modifier.height(2.dp))
                                            Text(
                                                text = opcion.descripcionModal,
                                                color = TextoSecundario,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    // Indicador Radio One UI
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = if (esSeleccionado) 6.dp else 1.5.dp,
                                                color = if (esSeleccionado) colorAcentoFinal else ColorAjusteGris.copy(alpha = 0.45f),
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Botón de cierre
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { abierto = false }
                            ) {
                                Text(
                                    text = "Cancelar",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modelo de opción para el selector múltiple modal (con switches) estilo Honor MagicOS / Samsung One UI.
 *
 * @param valor Valor identificador único.
 * @param etiquetaFila Texto conciso.
 * @param etiquetaModal Título mostrado en el diálogo modal.
 * @param descripcionModal Subtítulo o ejemplo detallado mostrado dentro del modal.
 * @param icono Icono descriptivo opcional para la opción.
 * @param activo Estado actual del interruptor.
 */
data class OpcionSelectorMultipleModal<T>(
    val valor: T,
    val etiquetaFila: String,
    val etiquetaModal: String = etiquetaFila,
    val descripcionModal: String? = null,
    val icono: ImageVector? = null,
    val activo: Boolean = false
)

/**
 * Componente reutilizable de fila con selector múltiple modal (con switches) emergente.
 * Permite seleccionar varios elementos sin cerrar el modal al pulsar cada interruptor.
 */
@Composable
fun <T> ComponenteSelectorMultipleModal(
    titulo: String,
    valorTexto: String,
    opciones: List<OpcionSelectorMultipleModal<T>>,
    alAlternar: (T) -> Unit,
    modifier: Modifier = Modifier,
    descripcionModal: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true,
    fijarAbajo: Boolean = false,
    textoBotonCerrar: String = "Listo",
    tituloFila: String = titulo
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var abierto by remember { mutableStateOf(false) }

    ComponenteFila(
        titulo = tituloFila,
        modifier = modifier,
        icono = icono,
        colorIcono = colorIcono,
        colorTinteIcono = colorTinteIcono,
        idFila = idFila,
        mostrarId = mostrarId,
        valorTexto = valorTexto,
        habilitado = habilitado,
        alPulsar = { if (habilitado) abierto = true },
        contenidoFinal = {
            Icon(
                imageVector = Icons.Filled.UnfoldMore,
                contentDescription = "Desplegar opciones",
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    )

    if (abierto) {
        val esOscuro = isSystemInDarkTheme()
        val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White
        val colorAcentoFinal = colorIcono ?: ColorAcento

        Dialog(
            onDismissRequest = { abierto = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { abierto = false },
                contentAlignment = if (fijarAbajo) Alignment.BottomCenter else Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = if (fijarAbajo) 0.dp else 32.dp,
                            bottom = if (fijarAbajo) 16.dp else 32.dp
                        )
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(fondoModal)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Evita cerrar al pulsar dentro */ }
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Cabecera del modal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (icono != null) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(colorAcentoFinal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = null,
                                        tint = colorTinteIcono,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = titulo,
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.5.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (!descripcionModal.isNullOrBlank()) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = descripcionModal,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Lista de opciones con SwitchBoveda
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 380.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            opciones.forEach { opcion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (opcion.activo) colorAcentoFinal.copy(alpha = 0.12f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            haptica.tic()
                                            alAlternar(opcion.valor)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (opcion.icono != null) {
                                        Icon(
                                            imageVector = opcion.icono,
                                            contentDescription = null,
                                            tint = if (opcion.activo) colorAcentoFinal else TextoSecundario,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(12.dp))
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = opcion.etiquetaModal,
                                            color = if (opcion.activo) colorAcentoFinal else TextoPrincipal,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (opcion.activo) FontWeight.SemiBold else FontWeight.Normal,
                                                fontSize = 15.sp
                                            )
                                        )
                                        if (!opcion.descripcionModal.isNullOrBlank()) {
                                            Spacer(Modifier.height(2.dp))
                                            Text(
                                                text = opcion.descripcionModal,
                                                color = TextoSecundario,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    SwitchBoveda(
                                        checked = opcion.activo,
                                        onCheckedChange = {
                                            haptica.tic()
                                            alAlternar(opcion.valor)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Botón de cierre
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { abierto = false }
                            ) {
                                Text(
                                    text = textoBotonCerrar,
                                    color = colorAcentoFinal,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

