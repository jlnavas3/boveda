package com.jlnavas3.bovedalocal.ui.pantallas.detalle

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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun TarjetaCamposDetalle(
    campos: List<CampoPersonalizado>,
    vm: VaultViewModel,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    if (campos.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Campos personalizados (${campos.size})")

        campos.forEachIndexed { index, campo ->
            if (index > 0) {
                Spacer(Modifier.height(EspaciadoDetalle))
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

    TarjetaDatoDetalle(colorBorde = Ambar) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    campo.etiqueta.ifBlank { "Campo adicional" },
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextoSecundario
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
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
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
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Ambar
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        !esSensible -> campo.valor
                        revelado -> campo.valor
                        campo.tipo == TipoCampo.PIN -> "• ".repeat(campo.valor.length).trim()
                        else -> "•".repeat(campo.valor.length.coerceIn(8, 20))
                    },
                    style = if (esSensible && !revelado) EstiloMonoGrande.copy(letterSpacing = 2.sp) else if (esSensible) EstiloMono else MaterialTheme.typography.bodyLarge,
                    color = if (esSensible && !revelado) TextoSecundario else TextoPrincipal,
                    maxLines = if (esSensible && !revelado) 1 else Int.MAX_VALUE,
                    softWrap = !esSensible || revelado,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (esSensible) {
                        BotonIconoDetalle(
                            icono = if (revelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (revelado) "Ocultar" else "Revelar",
                            alPulsar = {
                                haptica.toque()
                                revelado = !revelado
                                if (revelado) {
                                    Diagnostico.apuntar("seguridad", "Dato sensible revelado (${campo.etiqueta.ifBlank { "campo personalizado" }})")
                                }
                            }
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = copiado,
                        alPulsar = {
                            haptica.exito()
                            vm.copiar(campo.etiqueta.ifBlank { "Campo personalizado" }, campo.valor, sensible = esSensible)
                            alCopiar()
                        }
                    )
                }
            }
        }
    }
}
