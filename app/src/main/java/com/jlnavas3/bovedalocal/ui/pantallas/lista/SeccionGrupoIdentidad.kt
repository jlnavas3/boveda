package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera plegable para un bloque o sección de Identidad en la lista principal.
 */
@Composable
fun SeccionGrupoIdentidad(
    nombre: String,
    cantidad: Int,
    expandido: Boolean,
    colorBase: Color,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String = "",
    icono: ImageVector = Icons.Filled.Person,
    alPulsarLargo: (() -> Unit)? = null,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    parcialmenteSeleccionado: Boolean = false
) {
    ContenedorTarjeta(
        modifier = modifier,
        colorFondo = if (seleccionado) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes,
        paddingInterno = 12.dp,
        alPulsar = alAlternar,
        alPulsarProlongado = alPulsarLargo
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
            // Avatar del grupo usando el componente estándar de insignias
            ContenedorIconoInsignia(
                icono = icono,
                tamano = TamanoInsignia.PEQUENO,
                colorFondo = colorBase.copy(alpha = 0.16f),
                colorIcono = colorBase,
                conBorde = true
            )

            Spacer(Modifier.width(12.dp))

            // Textos del grupo (sin exponer correo electrónico)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextoTitulo(
                    texto = nombre,
                    estilo = EstiloTitulo.PEQUENO,
                    maxLineas = 1
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorBase.copy(alpha = 0.12f))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    TextoCuerpo(
                        texto = "$cantidad",
                        tamano = TamanoCuerpo.MINI,
                        color = colorBase,
                        maxLineas = 1
                    )
                }
            }

            // Chevron expandir/plegar
            Icon(
                imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expandido) "Plegar grupo" else "Desplegar grupo",
                tint = TextoSecundario,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
