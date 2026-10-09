package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun ResumenAuditoriaSalud(
    clavesCount: Int,
    repetidasCount: Int,
    muyComunesCount: Int,
    debilesCount: Int,
    antiguasCount: Int,
    expandido: Boolean,
    alAlternarExpandido: () -> Unit,
    modifier: Modifier = Modifier,
    umbralAntiguedadDias: Int = 180,
    ignoradasCount: Int = 0,
    diagnosticoAccesibilidad: com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { alAlternarExpandido() }
                .padding(vertical = 4.dp, horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RESUMEN DE AUDITORÍA",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (expandido) "Ocultar" else "Mostrar",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.labelSmall
                )
                Icon(
                    imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expandido) "Plegar resumen" else "Desplegar resumen",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (expandido) {
            // Tarjeta completa de auditoría
            GrupoAjustes {
                FilaMetricaSalud(
                    etiqueta = "Contraseñas analizadas",
                    valor = clavesCount.toString(),
                    color = ColorTextoAjustes
                )
                SeparadorFilaSimple()
                FilaMetricaSalud(
                    etiqueta = "Repetidas (en grupos)",
                    valor = repetidasCount.toString(),
                    color = if (repetidasCount == 0) Menta else Peligro
                )
                SeparadorFilaSimple()
                FilaMetricaSalud(
                    etiqueta = "Muy comunes o filtradas",
                    valor = muyComunesCount.toString(),
                    color = if (muyComunesCount == 0) Menta else Peligro
                )
                SeparadorFilaSimple()
                FilaMetricaSalud(
                    etiqueta = "Débiles (baja entropía)",
                    valor = debilesCount.toString(),
                    color = if (debilesCount == 0) Menta else Peligro
                )
                SeparadorFilaSimple()
                val etiquetaAntiguas = if (umbralAntiguedadDias <= 0) {
                    "Sin actualizar (Desactivado)"
                } else {
                    "Sin actualizar (> $umbralAntiguedadDias días)"
                }
                FilaMetricaSalud(
                    etiqueta = etiquetaAntiguas,
                    valor = antiguasCount.toString(),
                    color = if (antiguasCount == 0) Menta else ColorAcento
                )
                if (ignoradasCount > 0) {
                    SeparadorFilaSimple()
                    FilaMetricaSalud(
                        etiqueta = "Ignoradas de la auditoría",
                        valor = ignoradasCount.toString(),
                        color = ColorAjusteGris
                    )
                }
                if (diagnosticoAccesibilidad != null) {
                    SeparadorFilaSimple()
                    FilaAuditoriaAccesibilidad(diagnostico = diagnosticoAccesibilidad)
                }
            }
        } else {
            // Modo compacto: cápsula horizontal estilo Honor MagicOS / One UI
            val textoCompacto = if (diagnosticoAccesibilidad != null && !diagnosticoAccesibilidad.esSeguro) {
                "$clavesCount analizadas · ⚠️ Accesibilidad no autorizada"
            } else {
                "$clavesCount analizadas · $repetidasCount repetidas · $muyComunesCount comunes · $debilesCount débiles"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ColorTarjetaAjustes)
                    .clickable { alAlternarExpandido() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textoCompacto,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = if (diagnosticoAccesibilidad != null && !diagnosticoAccesibilidad.esSeguro) Peligro else ColorTextoAjustes,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
