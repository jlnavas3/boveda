package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun TarjetaAjuste(
    titulo: String,
    icono: ImageVector,
    descripcion: String,
    inicialmenteAbierta: Boolean = false,
    colorIcono: Color = ColorIconosInternos,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    abiertaControlada: Boolean? = null,
    alAlternarAbierta: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = titulo,
        icono = icono,
        descripcion = descripcion,
        inicialmenteAbierta = inicialmenteAbierta,
        colorIcono = colorIcono,
        idEtiqueta = idEtiqueta,
        mostrarId = mostrarId,
        abiertaControlada = abiertaControlada,
        alAlternarAbierta = alAlternarAbierta,
        modifier = modifier,
        contenido = contenido
    )
}

data class OpcionAjuste(
    val valor: String,
    val texto: String,
    val icono: ImageVector
)

@Composable
fun SelectorAjuste(
    titulo: String,
    icono: ImageVector,
    seleccionado: String,
    opciones: List<OpcionAjuste>,
    colorIcono: Color = ColorIconosInternos,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    alSeleccionar: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo
    val tieneBadge = mostrarId && !idEtiqueta.isNullOrBlank()
    val fondoCaja = ColorCampoAjustes

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (tieneBadge) 48.dp else 44.dp)
                .clip(forma)
                .background(fondoCaja)
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val colorFinalIcono = if (abierto) ColorAcento else colorIcono
            val colorLegible = colorLegibleParaTema(colorFinalIcono)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (tieneBadge) 30.dp else 34.dp)
                        .clip(FormaPequena)
                        .background(fondoBadgeParaTema(colorFinalIcono)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icono,
                        contentDescription = null,
                        tint = colorLegible,
                        modifier = Modifier.size(if (tieneBadge) 16.dp else 18.dp)
                    )
                }
                if (tieneBadge) {
                    Spacer(Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(fondoBadgeParaTema(colorFinalIcono))
                            .padding(horizontal = 3.dp, vertical = 0.5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = idEtiqueta!!,
                            style = EstiloMono.copy(fontSize = 7.5.sp, fontWeight = FontWeight.Bold),
                            color = colorLegible
                        )
                    }
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelMedium)
                Text(seleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir $titulo",
                tint = TextoSecundario
            )
        }
        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val esSeleccionado = opcion.texto == seleccionado
                DropdownMenuItem(
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(FormaPequena)
                                .background(ColorIconosInternos.copy(alpha = if (esSeleccionado) 0.16f else 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                opcion.icono,
                                contentDescription = null,
                                tint = if (esSeleccionado) ColorIconosInternos else TextoSecundario,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    text = {
                        Text(
                            opcion.texto,
                            color = if (opcion.texto == seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        if (opcion.texto == seleccionado) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp))
                        }
                    },
                    onClick = {
                        alSeleccionar(opcion.valor)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
fun SwatchColor(color: Color, seleccionado: Boolean, descripcion: String, alPulsar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color)
            .border(width = if (seleccionado) 3.dp else 0.dp, color = TextoPrincipal, shape = CircleShape)
            .clickable { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(Icons.Filled.Check, contentDescription = descripcion, tint = colorContraste(color))
        }
    }
}

@Composable
fun BotonRestablecerItem(
    texto: String = "Restablecer predefinido",
    modifier: Modifier = Modifier,
    alRestaurar: () -> Unit
) {
    ComponenteBotonFila(
        titulo = texto,
        icono = androidx.compose.material.icons.Icons.Filled.RestartAlt,
        colorIcono = ColorIconosInternos,
        alPulsar = alRestaurar,
        modifier = modifier
    )
}

@BovedaPreview
@Composable
private fun PreviewSelectoresAjustes() {
    PreviewTemaBoveda {
        Column(modifier = Modifier.padding(16.dp)) {
            SelectorAjuste(
                titulo = "Tema visual",
                icono = Icons.Filled.Refresh,
                seleccionado = "Oscuro",
                opciones = listOf(
                    OpcionAjuste("oscuro", "Oscuro", Icons.Filled.Refresh),
                    OpcionAjuste("claro", "Claro", Icons.Filled.Refresh)
                ),
                alSeleccionar = {}
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SwatchColor(color = Color(0xFFFF9800), seleccionado = true, descripcion = "Ámbar", alPulsar = {})
                SwatchColor(color = Color(0xFF2196F3), seleccionado = false, descripcion = "Azul", alPulsar = {})
                SwatchColor(color = Color(0xFF4CAF50), seleccionado = false, descripcion = "Verde", alPulsar = {})
            }
            Spacer(Modifier.height(16.dp))
            BotonRestablecerItem(alRestaurar = {})
        }
    }
}

