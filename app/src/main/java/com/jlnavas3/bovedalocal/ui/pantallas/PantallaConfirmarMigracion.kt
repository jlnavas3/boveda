package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.CuentaGoogleAuth
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla interactiva que muestra el listado de cuentas extraídas de un QR de Google Authenticator,
 * permitiendo seleccionar/desmarcar con checkboxes antes de registrarlas en la bóveda.
 *
 * Incluye detección de cuentas ya existentes en la bóveda, desmarcándolas automáticamente por defecto.
 */
@Composable
fun PantallaConfirmarMigracion(
    vm: VaultViewModel,
    urlMigracion: String
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val entradasExistentes = (estadoBoveda as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val secretosEnBoveda = remember(entradasExistentes) {
        entradasExistentes.mapNotNull { it.secretoTotp?.uppercase()?.trim() }.toSet()
    }

    val cuentasExtraidas = remember(urlMigracion) {
        GoogleAuthMigration.decodificar(urlMigracion)
    }

    val listaCuentas = remember(cuentasExtraidas, secretosEnBoveda) {
        mutableStateListOf<CuentaGoogleAuth>().apply {
            val preparadas = cuentasExtraidas.map { c ->
                val yaExiste = secretosEnBoveda.contains(c.secretoBase32.uppercase().trim())
                c.copy(seleccionada = !yaExiste)
            }
            addAll(preparadas)
        }
    }

    val cuantasSeleccionadas = listaCuentas.count { it.seleccionada }
    val todasSonExistentes = remember(listaCuentas, secretosEnBoveda) {
        listaCuentas.isNotEmpty() && listaCuentas.all { secretosEnBoveda.contains(it.secretoBase32.uppercase().trim()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Importar de Google Authenticator",
            idEtiqueta = "2FA",
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
            DescripcionPantalla(
                subtitulo = if (listaCuentas.isEmpty())
                    "No se pudieron leer cuentas válidas en este código"
                else if (todasSonExistentes)
                    "Todas las cuentas de este código QR ya existen en tu Bóveda Local."
                else
                    "Se encontraron ${listaCuentas.size} cuentas. Las cuentas nuevas están seleccionadas automáticamente:"
            )

            Spacer(Modifier.height(10.dp))

            if (listaCuentas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "El código QR o enlace no contiene credenciales de migración válidas.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Barra de controles de selección rápida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$cuantasSeleccionadas de ${listaCuentas.size} seleccionadas",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ColorAcento
                    )

                    Row {
                        TextButton(
                            onClick = {
                                haptica.tic()
                                for (i in listaCuentas.indices) {
                                    val yaExiste = secretosEnBoveda.contains(listaCuentas[i].secretoBase32.uppercase().trim())
                                    listaCuentas[i] = listaCuentas[i].copy(seleccionada = !yaExiste)
                                }
                            }
                        ) {
                            Text("Solo nuevas", style = MaterialTheme.typography.bodySmall, color = ColorAcento)
                        }

                        TextButton(
                            onClick = {
                                haptica.tic()
                                for (i in listaCuentas.indices) {
                                    listaCuentas[i] = listaCuentas[i].copy(seleccionada = true)
                                }
                            }
                        ) {
                            Text("Todas", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                        }

                        TextButton(
                            onClick = {
                                haptica.tic()
                                for (i in listaCuentas.indices) {
                                    listaCuentas[i] = listaCuentas[i].copy(seleccionada = false)
                                }
                            }
                        ) {
                            Text("Ninguna", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Lista de cuentas con Checkboxes
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(listaCuentas, key = { _, item -> item.id }) { index, cuenta ->
                        val yaExisteEnBoveda = secretosEnBoveda.contains(cuenta.secretoBase32.uppercase().trim())

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FormaTarjeta)
                                .background(Superficie)
                                .then(
                                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                        Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                                    else Modifier
                                )
                                .clickable {
                                    haptica.tic()
                                    listaCuentas[index] = cuenta.copy(seleccionada = !cuenta.seleccionada)
                                }
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterStart)
                                    .background(ColorDatos2FA)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = cuenta.seleccionada,
                                    onCheckedChange = { checked ->
                                        haptica.tic()
                                        listaCuentas[index] = cuenta.copy(seleccionada = checked)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = ColorAcento,
                                        uncheckedColor = TextoSecundario
                                    )
                                )

                                Spacer(Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(ColorExportacion.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Timer,
                                        contentDescription = null,
                                        tint = ColorExportacion,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = cuenta.titulo,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextoPrincipal,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )

                                        if (yaExisteEnBoveda) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(fondoBadgeParaTema(TextoSecundario))
                                                    .padding(horizontal = 5.dp, vertical = 1.5.dp)
                                            ) {
                                                Text(
                                                    text = "En la bóveda",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                                    color = colorLegibleParaTema(TextoSecundario)
                                                )
                                            }
                                        }
                                    }

                                    if (cuenta.cuenta.isNotBlank() && cuenta.cuenta != cuenta.emisor) {
                                        Text(
                                            text = cuenta.cuenta,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextoSecundario,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Text(
                                        text = "${cuenta.digitos} dígitos • ${if (cuenta.esTotp) "TOTP ${cuenta.periodo}s" else "HOTP"}",
                                        style = EstiloMono.copy(fontSize = 10.sp),
                                        color = TextoSecundario.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botones fijos inferiores
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (listaCuentas.isNotEmpty()) {
                    BotonAmbar(
                        texto = if (cuantasSeleccionadas > 0)
                            "Importar $cuantasSeleccionadas ${if (cuantasSeleccionadas == 1) "cuenta" else "cuentas"} a la Bóveda"
                        else
                            "Selecciona al menos una cuenta",
                        icono = Icons.Filled.Check,
                        activo = cuantasSeleccionadas > 0
                    ) {
                        haptica.exito()
                        vm.importarCuentasGoogleAuth(listaCuentas)
                    }
                }

                BotonBorde(
                    texto = "Cancelar",
                    icono = Icons.Filled.Close
                ) {
                    vm.volverAtras()
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
