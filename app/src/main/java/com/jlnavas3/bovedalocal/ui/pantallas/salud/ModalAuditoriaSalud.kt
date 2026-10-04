package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente del modal inferior de auditoría de salud de la bóveda.
 * Muestra el resumen estadístico global y accesos directos de optimización.
 */
@Composable
fun ModalAuditoriaSalud(
    abierto: Boolean,
    alCerrar: () -> Unit,
    clavesCount: Int,
    totalSobrantesDuplicadas: Int,
    repetidasCount: Int,
    muyComunesCount: Int,
    debilesCount: Int,
    antiguasCount: Int,
    ignoradasCount: Int,
    umbralDias: Int,
    alIrDuplicados: () -> Unit
) {
    ModalInferiorBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        titulo = "Auditoría de Salud",
        descripcion = "$clavesCount contraseñas analizadas",
        icono = Icons.Filled.Analytics,
        colorIcono = ColorAcento
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (totalSobrantesDuplicadas > 0) {
                GrupoAjustes(etiqueta = "Duplicados detectados") {
                    FilaAjusteMenu(
                        titulo = "Detectadas $totalSobrantesDuplicadas copias repetidas",
                        subtitulo = "Entradas idénticas de importación. Pulsa para limpiar con 1 toque",
                        icono = Icons.Filled.AutoFixHigh,
                        colorIcono = ColorAcento,
                        alPulsar = {
                            alCerrar()
                            alIrDuplicados()
                        }
                    )
                }
            }

            ResumenAuditoriaSalud(
                clavesCount = clavesCount,
                repetidasCount = repetidasCount,
                muyComunesCount = muyComunesCount,
                debilesCount = debilesCount,
                antiguasCount = antiguasCount,
                expandido = true,
                alAlternarExpandido = {},
                umbralAntiguedadDias = umbralDias,
                ignoradasCount = ignoradasCount
            )
        }
    }
}
