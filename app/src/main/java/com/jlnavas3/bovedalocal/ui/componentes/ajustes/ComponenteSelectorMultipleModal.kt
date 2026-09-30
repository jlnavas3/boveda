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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

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
        val colorAcentoFinal = colorIcono ?: ColorAcento

        val listaOpciones = @Composable {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                opciones.forEach { opcion ->
                    FilaOpcionModal(
                        titulo = opcion.etiquetaModal,
                        descripcion = opcion.descripcionModal,
                        icono = opcion.icono,
                        seleccionado = opcion.activo,
                        colorAcento = colorAcentoFinal,
                        alPulsar = {
                            haptica.tic()
                            alAlternar(opcion.valor)
                        },
                        controlFinal = {
                            SwitchBoveda(
                                checked = opcion.activo,
                                onCheckedChange = {
                                    haptica.tic()
                                    alAlternar(opcion.valor)
                                }
                            )
                        }
                    )
                }
            }
        }

        if (fijarAbajo) {
            ModalInferiorBoveda(
                abierto = abierto,
                alCerrar = { abierto = false },
                titulo = titulo,
                descripcion = descripcionModal,
                icono = icono,
                colorIcono = colorTinteIcono,
                fondoIcono = colorAcentoFinal,
                fijarAbajo = true,
                mostrarBotonCerrar = false
            ) {
                listaOpciones()
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { abierto = false }) {
                        Text(
                            text = textoBotonCerrar,
                            color = colorAcentoFinal,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        } else {
            DialogoBoveda(
                abierto = abierto,
                alCerrar = { abierto = false },
                titulo = titulo,
                icono = icono,
                colorIcono = colorTinteIcono,
                fondoIcono = colorAcentoFinal,
                botonConfirmar = {
                    TextButton(onClick = { abierto = false }) {
                        Text(
                            text = textoBotonCerrar,
                            color = colorAcentoFinal,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            ) {
                if (!descripcionModal.isNullOrBlank()) {
                    Text(
                        text = descripcionModal,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                }
                listaOpciones()
            }
        }
    }
}
