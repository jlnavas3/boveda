package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes

/**
 * Contenedor modal inferior / flotante estándar para toda la aplicación.
 *
 * Elimina la duplicación de código modal y garantiza que el contenedor respete
 * estrictamente las configuraciones de `CurvaturaEsquinas`, `GrosorBorde`, `ColorBordeActual` y `EstiloBorde`.
 */
@Composable
fun ModalInferiorBoveda(
    abierto: Boolean,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier,
    titulo: String? = null,
    descripcion: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorAcento,
    fondoIcono: Color = colorIcono.copy(alpha = 0.15f),
    mostrarBotonCerrar: Boolean = false,
    fijarAbajo: Boolean = true,
    fondo: Color = ColorTarjetaAjustes,
    contenido: @Composable ColumnScope.() -> Unit
) {
    if (!abierto) return

    val focusManager = LocalFocusManager.current
    val fondoModal = fondo
    val formaModal = RoundedCornerShape(CurvaturaEsquinas)

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alCerrar() },
            contentAlignment = if (fijarAbajo) Alignment.BottomCenter else Alignment.Center
        ) {
            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = if (fijarAbajo) 0.dp else 32.dp,
                        bottom = if (fijarAbajo) 16.dp else 32.dp
                    )
                    .fillMaxWidth()
                    .widthIn(max = 440.dp)
                    .clip(formaModal)
                    .background(fondoModal)
                    .then(
                        if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaModal)
                        } else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { focusManager.clearFocus() }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Cabecera opcional
                    if (titulo != null || icono != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (icono != null) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(FormaPequena)
                                        .background(fondoIcono),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icono,
                                        contentDescription = null,
                                        tint = colorIcono,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                if (titulo != null) {
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
                                }
                                if (!descripcion.isNullOrBlank()) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = descripcion,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                                    )
                                }
                            }

                            if (mostrarBotonCerrar) {
                                IconButton(
                                    onClick = alCerrar,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Cerrar",
                                        tint = TextoSecundario,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    // Contenido dinámico
                    contenido()
                }
            }
        }
    }
}
