package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ChipsCategoriasExportacion(
    categorias: List<CategoriaExportacion>,
    categoriaActivaId: String,
    alSeleccionarCategoria: (String) -> Unit,
    totalPorCategoria: (CategoriaExportacion) -> Int,
    onTic: () -> Unit
) {
    if (categorias.size <= 1) return

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categorias, key = { it.id }) { cat ->
            val seleccionada = cat.id == categoriaActivaId
            val cantCat = totalPorCategoria(cat)

            Box(
                modifier = (if (!seleccionada && GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(width = GrosorBorde, color = ColorBordeActual, shape = FormaTarjeta as Shape)
                else
                    Modifier)
                    .clip(FormaTarjeta)
                    .background(if (seleccionada) ColorAcento else Superficie)
                    .clickable {
                        onTic()
                        alSeleccionarCategoria(cat.id)
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${cat.etiqueta} ($cantCat)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium),
                    color = if (seleccionada) Color.Black else TextoPrincipal
                )
            }
        }
    }
}

@Composable
fun BarraControlesSeleccionExportar(
    seleccionadas: Int,
    total: Int,
    alMarcarVisibles: () -> Unit,
    alDeseleccionarTodas: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$seleccionadas de $total seleccionadas",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = ColorAcento
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BotonIconoCabecera(
                onClick = alMarcarVisibles,
                icono = Icons.Filled.DoneAll,
                descripcion = "Marcar visibles",
                tint = ColorAcento
            )

            BotonIconoCabecera(
                onClick = alDeseleccionarTodas,
                icono = Icons.Filled.Clear,
                descripcion = "Deseleccionar todas",
                tint = TextoSecundario
            )
        }
    }
}

@Composable
fun FilaEntradaExportarSelectivo(
    entrada: Entrada,
    marcada: Boolean,
    alAlternarMarcado: (Boolean) -> Unit
) {
    val colorTipoEntrada = remember(entrada) {
        when {
            entrada.passkey != null -> ColorDatosPasskey
            !entrada.secretoTotp.isNullOrBlank() -> ColorDatos2FA
            entrada.contrasena.isNotBlank() -> ColorDatosContrasena
            entrada.usuario.isNotBlank() -> ColorDatosUsuario
            entrada.urls.any { it.startsWith("androidapp://", ignoreCase = true) || it.startsWith("androidapp:", ignoreCase = true) } -> ColorDatosApp
            entrada.urls.isNotEmpty() -> ColorDatosWeb
            else -> ColorAcento
        }
    }

    Box(
        modifier = (if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
            Modifier.border(width = GrosorBorde, color = ColorBordeActual, shape = FormaTarjeta as Shape)
        else
            Modifier)
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(Superficie)
            .clickable { alAlternarMarcado(!marcada) }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(colorTipoEntrada)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckboxBoveda(
                checked = marcada,
                onCheckedChange = alAlternarMarcado
            )

            Spacer(Modifier.width(6.dp))

            Monograma(
                titulo = entrada.titulo,
                semilla = entrada.urls.firstOrNull() ?: entrada.usuario,
                tamano = 36
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entrada.titulo,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entrada.usuario.isNotBlank()) {
                    Text(
                        text = entrada.usuario,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun BotonesAccionInferioresExportar(
    cantidadSeleccionada: Int,
    alExportar: () -> Unit,
    alCancelar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonAmbar(
            texto = if (cantidadSeleccionada > 0)
                "Exportar ($cantidadSeleccionada)"
            else
                "Selecciona",
            icono = Icons.Filled.Check,
            activo = cantidadSeleccionada > 0,
            modifier = Modifier.weight(1f),
            alPulsar = alExportar
        )

        BotonBorde(
            texto = "Cancelar",
            icono = Icons.Filled.Close,
            modifier = Modifier.weight(1f),
            alPulsar = alCancelar
        )
    }
}
