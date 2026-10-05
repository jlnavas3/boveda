package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera para subsecciones subordinadas (Nivel 2) en listas agrupadas jerárquicamente.
 * Presenta una sangría indentada y escala compacta que expresa subordinación visual clara.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubseccionGrupoLista(
    titulo: String,
    cantidad: Int,
    expandido: Boolean,
    colorBase: Color,
    icono: ImageVector,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    alPulsarLargo: (() -> Unit)? = null,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    parcialmenteSeleccionado: Boolean = false,
    alAlternarExpansion: (() -> Unit)? = null
) {
    val forma = FormaPequena
    val borde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp

    val modificadorInteraccion = if (alPulsarLargo != null) {
        Modifier.combinedClickable(
            onClick = alAlternar,
            onLongClick = alPulsarLargo
        )
    } else {
        Modifier.clickable(onClick = alAlternar)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 14.dp)
            .clip(forma)
            .background(if (seleccionado) ColorAcento.copy(alpha = 0.16f) else ColorCampoAjustes)
            .border(grosor, borde, forma)
            .then(modificadorInteraccion)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado || parcialmenteSeleccionado) ColorAcento else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Seleccionado",
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(15.dp)
                        )
                    } else if (parcialmenteSeleccionado) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Parcialmente seleccionado",
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
            }
            ContenedorIconoInsignia(
                icono = icono,
                tamano = TamanoInsignia.PEQUENO,
                colorFondo = colorBase.copy(alpha = 0.16f),
                colorIcono = colorBase,
                conBorde = true
            )

            Spacer(Modifier.width(10.dp))

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextoTitulo(
                    texto = titulo,
                    estilo = EstiloTitulo.PEQUENO,
                    maxLineas = 1
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorBase.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    TextoCuerpo(
                        texto = "$cantidad",
                        tamano = TamanoCuerpo.MINI,
                        color = colorBase,
                        maxLineas = 1
                    )
                }
            }

            IconButton(
                onClick = {
                    if (alAlternarExpansion != null) alAlternarExpansion() else alAlternar()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Plegar subsección" else "Desplegar subsección",
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
