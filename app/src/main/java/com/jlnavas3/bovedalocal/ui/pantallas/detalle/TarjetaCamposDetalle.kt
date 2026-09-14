package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.FormatListBulleted
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Diagnostico

@Composable
fun TarjetaCamposDetalle(
    campos: List<CampoPersonalizado>,
    vm: VaultViewModel,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    if (campos.isEmpty()) return

    TarjetaBovedaDesplegable(
        titulo = "Campos personalizados",
        descripcion = "${campos.size} campo${if (campos.size == 1) "" else "s"}",
        icono = Icons.Filled.FormatListBulleted,
        colorIcono = ColorSalud,
        inicialmenteAbierta = true
    ) {
        campos.forEachIndexed { index, campo ->
            if (index > 0) {
                Spacer(Modifier.height(10.dp))
            }
            FilaCampoPersonalizadoDetalle(
                campo = campo,
                vm = vm,
                haptica = haptica,
                copiado = ultimaCopia == "campo_${campo.id}",
                alCopiar = { alCopiarCampo("campo_${campo.id}") }
            )
        }
    }
}

@Composable
private fun FilaCampoPersonalizadoDetalle(
    campo: CampoPersonalizado,
    vm: VaultViewModel,
    haptica: Haptica,
    copiado: Boolean,
    alCopiar: () -> Unit
) {
    var revelado by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(SuperficieAlta)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                campo.etiqueta.ifBlank { "Campo adicional" },
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (esSensible) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(ColorAcento.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Security,
                                contentDescription = "Sensible",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                "Sensible",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorIconosInternos
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(Ambar.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        campo.tipo.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        color = Ambar
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = when {
                !esSensible -> campo.valor
                revelado -> campo.valor
                campo.tipo == TipoCampo.PIN -> "• ".repeat(campo.valor.length).trim()
                else -> "•".repeat(campo.valor.length.coerceIn(8, 20))
            },
            style = if (esSensible && !revelado) EstiloMonoGrande.copy(letterSpacing = 2.sp) else if (esSensible) EstiloMono else MaterialTheme.typography.bodyLarge,
            color = if (esSensible && !revelado) TextoSecundario else TextoPrincipal,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (esSensible) {
                IconButton(onClick = {
                    haptica.toque()
                    revelado = !revelado
                    if (revelado) {
                        Diagnostico.apuntar("seguridad", "Dato sensible revelado (${campo.etiqueta.ifBlank { "campo personalizado" }})")
                    }
                }) {
                    Icon(
                        imageVector = if (revelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revelado) "Ocultar" else "Revelar",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            IconButton(onClick = {
                haptica.exito()
                vm.copiar(campo.etiqueta.ifBlank { "Campo personalizado" }, campo.valor, sensible = esSensible)
                alCopiar()
            }) {
                AnimatedVisibility(
                    visible = copiado,
                    enter = scaleIn(spring(dampingRatio = 0.5f)),
                    exit = scaleOut(spring(dampingRatio = 0.6f))
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Copiado", tint = Menta)
                }
                AnimatedVisibility(
                    visible = !copiado,
                    enter = scaleIn(spring(dampingRatio = 0.5f)),
                    exit = scaleOut(spring(dampingRatio = 0.6f))
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copiar", tint = ColorIconosInternos)
                }
            }
        }
    }
}
