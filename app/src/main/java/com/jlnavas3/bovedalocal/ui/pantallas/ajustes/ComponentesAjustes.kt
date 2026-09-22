package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ColumnScope
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica

val ColorAjustesFondo: Color get() = if (esOscuroActivo) Color(0xFF000000) else Color(0xFFF2F2F7)
val ColorTarjetaAjustes: Color get() = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)
val ColorTextoAjustes: Color get() = if (esOscuroActivo) Color(0xFFFFFFFF) else Color(0xFF15171F)
val ColorAjusteGris: Color get() = if (esOscuroActivo) Color(0xFF99999B) else Color(0xFF8E8E93)
val ColorSeparadorAjustes: Color get() = if (esOscuroActivo) Color(0xFF333238) else Color(0xFFE5E5EA)

@Composable
fun BarraBusquedaAjustes(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    placeholder: String = "Buscar en ajustes...",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ColorTarjetaAjustes)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Buscar",
            tint = ColorAjusteGris,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (texto.isEmpty()) {
                Text(
                    text = placeholder,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            BasicTextField(
                value = texto,
                onValueChange = alCambiarTexto,
                singleLine = true,
                cursorBrush = SolidColor(ColorTextoAjustes),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = ColorTextoAjustes
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (texto.isNotEmpty()) {
            IconButton(
                onClick = { alCambiarTexto("") },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GrupoAjustes(
    etiqueta: String? = null,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!etiqueta.isNullOrBlank()) {
            Text(
                text = etiqueta.uppercase(),
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 4.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}

@Composable
fun FilaAjusteMenu(
    titulo: String,
    icono: ImageVector,
    colorIcono: Color,
    subtitulo: String? = null,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(
                horizontal = 16.dp,
                vertical = if (mostrarId && !idEtiqueta.isNullOrBlank()) 11.dp else 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "ID: $idEtiqueta",
                    color = ColorAjusteGris,
                    style = EstiloMono.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!valorTexto.isNullOrBlank()) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = valorTexto,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        Spacer(Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = ColorAjusteGris,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SeparadorFilaAjuste(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 68.dp)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}

@Composable
fun SeparadorFilaSimple(modifier: Modifier = Modifier, paddingInicio: androidx.compose.ui.unit.Dp = 16.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingInicio)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}

@Composable
fun FilaEntradaSubmenu(
    titulo: String,
    subtitulo: String? = null,
    valorTexto: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    esPeligro: Boolean = false,
    mostrarChevron: Boolean = true,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(
                horizontal = 16.dp,
                vertical = if (mostrarId && !idEtiqueta.isNullOrBlank()) 12.dp else 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono ?: (if (esPeligro) Peligro else ColorAjusteGris),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                color = if (esPeligro) Peligro else ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.5.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "ID: $idEtiqueta",
                    color = ColorAjusteGris,
                    style = EstiloMono.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!valorTexto.isNullOrBlank()) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = valorTexto,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        if (mostrarChevron) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun FilaOpcionRadio(
    titulo: String,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alSeleccionar()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono ?: (if (seleccionado) ColorAcento else ColorAjusteGris),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 15.5.sp
                )
            )
            if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = if (seleccionado) 6.dp else 1.5.dp,
                    color = if (seleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun EnlaceAjuste(texto: String, alPulsar: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text(
        texto,
        color = ColorAcento,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clickable { alPulsar() }
            .padding(vertical = 4.dp)
    )
}

@Composable
fun DialogoContrasena(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alConfirmar: (String) -> Unit,
    alCancelar: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
    AlertDialog(
        onDismissRequest = alCancelar,
        containerColor = colorDialogo,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(20.dp),
        title = { Text(titulo, color = TextoPrincipal) },
        text = {
            Column {
                Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = valor,
                    etiqueta = "Contraseña",
                    alCambiar = { valor = it },
                    esContrasena = true,
                    mostrarContrasena = mostrarContrasena,
                    alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena }
                )
            }
        },
        confirmButton = {
            TextButton(enabled = valor.length >= 8, onClick = { alConfirmar(valor) }) {
                Text(textoBoton, color = ColorAcento)
            }
        },
        dismissButton = { TextButton(onClick = alCancelar) { Text("Cancelar") } }
    )
}

/**
 * Switch estándar para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 * Sin bordes duros, con track redondeado y colores sólidos.
 */
@Composable
fun SwitchBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorActivo: Color = Ambar
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = ColorSobreAcento,
            checkedTrackColor = colorActivo,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = ColorAjusteGris,
            uncheckedTrackColor = if (esOscuroActivo) Color(0xFF333238) else Color(0xFFE5E5EA),
            uncheckedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun FilaAjuste(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean = true,
    colorActivo: Color = Ambar,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        SwitchBoveda(
            checked = activo,
            enabled = habilitado,
            colorActivo = colorActivo,
            onCheckedChange = alCambiar
        )
    }
}

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
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
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
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondoCaja = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
    val bordeCaja = if (abierto) ColorAcento else (if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3))

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
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val fondoSolido = if (esOscuroActivo) Color(0xFF2C2B30) else Color(0xFFEAEAEA)
    val forma = RoundedCornerShape(10.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier
                .clip(forma)
                .background(fondoSolido)
                .clickable {
                    haptica.tic()
                    alRestaurar()
                }
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                tint = Ambar,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = texto,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp
                )
            )
        }
    }
}
