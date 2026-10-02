package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.cxf.CxfConvertidor
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.Haptica

private data class ItemSeleccionableCxf(
    val entrada: Entrada,
    var seleccionada: Boolean,
    val yaExisteEnBoveda: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaConfirmarImportacionCxf(
    vm: VaultViewModel,
    jsonCxf: String
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val entradasExistentes = (estadoBoveda as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val resultadoCxf = remember(jsonCxf) {
        CxfConvertidor.convertir(jsonCxf)
    }

    val items = remember(resultadoCxf, entradasExistentes) {
        mutableStateListOf<ItemSeleccionableCxf>().apply {
            val transformados = resultadoCxf.entradas.map { ent ->
                val yaExiste = entradasExistentes.any { existente ->
                    (existente.usuario.equals(ent.usuario, ignoreCase = true) || ent.usuario.isBlank()) &&
                        (
                            (ent.passkey != null && existente.passkey?.rpId.equals(ent.passkey?.rpId, ignoreCase = true)) ||
                            ent.urls.any { u -> existente.urls.any { eu -> Dominios.coincide(u, eu) } } ||
                            (ent.titulo.isNotBlank() && existente.titulo.equals(ent.titulo, ignoreCase = true))
                        )
                }
                ItemSeleccionableCxf(
                    entrada = ent,
                    seleccionada = !yaExiste,
                    yaExisteEnBoveda = yaExiste
                )
            }
            addAll(transformados)
        }
    }

    val cuantasSeleccionadas = items.count { it.seleccionada }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Importar credenciales",
            idEtiqueta = "05-COP-CXF-IMP",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cabecera informativa y origen
            val exportadorNombre = resultadoCxf.exportador?.ifBlank { "Proveedor del sistema" } ?: "Proveedor del sistema"
            Text(
                text = "Transferencia directa desde $exportadorNombre",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Se encontraron ${items.size} credenciales listas para incorporar a tu bóveda:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )

            Spacer(Modifier.height(10.dp))

            // Resumen de insignias con estadísticas
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (resultadoCxf.totalPasskeys > 0) {
                    InsigniaResumenCxf(
                        icono = Icons.Filled.VpnKey,
                        texto = "${resultadoCxf.totalPasskeys} llaves de paso",
                        color = Ambar
                    )
                }
                if (resultadoCxf.totalContrasenas > 0) {
                    InsigniaResumenCxf(
                        icono = Icons.Filled.Lock,
                        texto = "${resultadoCxf.totalContrasenas} contraseñas",
                        color = Color(0xFF64B5F6)
                    )
                }
                if (resultadoCxf.totalTotp > 0) {
                    InsigniaResumenCxf(
                        icono = Icons.Filled.QrCode,
                        texto = "${resultadoCxf.totalTotp} verificación en dos pasos",
                        color = Color(0xFF81C784)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botones rápidos de selección
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonSeleccionRapida(
                    texto = "Solo nuevas",
                    alPulsar = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = !items[i].yaExisteEnBoveda)
                        }
                    }
                )
                BotonSeleccionRapida(
                    texto = "Todas (${items.size})",
                    alPulsar = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = true)
                        }
                    }
                )
                BotonSeleccionRapida(
                    texto = "Ninguna",
                    alPulsar = {
                        haptica.tic()
                        for (i in items.indices) {
                            items[i] = items[i].copy(seleccionada = false)
                        }
                    }
                )
            }

            Spacer(Modifier.height(10.dp))

            // Listado de credenciales extraídas
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(items, key = { _, item -> item.entrada.id }) { index, item ->
                    FilaItemCxf(
                        item = item,
                        alAlternar = {
                            haptica.tic()
                            items[index] = item.copy(seleccionada = !item.seleccionada)
                        },
                        onCheckedChange = { checked ->
                            items[index] = item.copy(seleccionada = checked)
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botones inferiores de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda(
                    texto = "Cancelar",
                    variante = com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton.SECUNDARIO,
                    alPulsar = { vm.volverAtras() },
                    modifier = Modifier.weight(1f)
                )

                com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda(
                    texto = if (cuantasSeleccionadas == 0) "Ninguna seleccionada" else "Importar ($cuantasSeleccionadas)",
                    variante = com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton.PRIMARIO,
                    activo = cuantasSeleccionadas > 0,
                    alPulsar = {
                        val seleccionadas = items.filter { it.seleccionada }.map { it.entrada }
                        if (seleccionadas.isNotEmpty()) {
                            haptica.exito()
                            vm.importarEntradasCxf(seleccionadas)
                        }
                    },
                    modifier = Modifier.weight(1.5f)
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun InsigniaResumenCxf(
    icono: ImageVector,
    texto: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = color
        )
    }
}

@Composable
private fun BotonSeleccionRapida(
    texto: String,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ColorTarjetaAjustes)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            color = ColorIconosInternos
        )
    }
}

@Composable
private fun FilaItemCxf(
    item: ItemSeleccionableCxf,
    alAlternar: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val forma = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
    val colorBorde = if (item.seleccionada) com.jlnavas3.bovedalocal.ui.theme.ColorAcento.copy(alpha = 0.65f) else ColorBordeActual

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(GrosorBorde, colorBorde, forma)
            .clickable(onClick = alAlternar)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda(
            checked = item.seleccionada,
            onCheckedChange = onCheckedChange
        )

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (item.yaExisteEnBoveda) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Ya en bóveda",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Peligro,
                        modifier = Modifier
                            .clip(com.jlnavas3.bovedalocal.ui.theme.FormaPequena)
                            .background(Peligro.copy(alpha = 0.12f))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            if (item.entrada.usuario.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.entrada.usuario,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(6.dp))

            // Etiquetas de contenido
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (item.entrada.passkey != null) {
                    MiniChipCxf("Llave de paso", Ambar)
                }
                if (item.entrada.contrasena.isNotBlank()) {
                    MiniChipCxf("Contraseña", Color(0xFF64B5F6))
                }
                if (!item.entrada.secretoTotp.isNullOrBlank()) {
                    MiniChipCxf("Dos pasos", Color(0xFF81C784))
                }
            }
        }
    }
}

@Composable
private fun MiniChipCxf(texto: String, color: Color) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold),
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    )
}
