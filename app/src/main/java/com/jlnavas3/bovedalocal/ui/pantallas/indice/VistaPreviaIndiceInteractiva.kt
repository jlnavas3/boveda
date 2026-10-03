package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun VistaPreviaIndiceInteractiva(
    ajustes: AjustesApp,
    letraArrastrada: Char? = null,
    mockItems: List<Pair<String, String>> = emptyList()
) {
    val itemsEfectivos = remember(mockItems) {
        if (mockItems.isNotEmpty()) mockItems else listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar"
        )
    }

    val primerIndiceCoincidente = remember(itemsEfectivos, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
        if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
        else if (ajustes.indiceResaltarSoloPrimera) {
            itemsEfectivos.indexOfFirst { (nombre, _) ->
                letraInicialIndice(nombre, ajustes.indiceIncluirEnie) == letraArrastrada
            }.takeIf { it >= 0 }
        } else null
    }

    ContenedorTarjeta(paddingInterno = 0.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsEfectivos.take(4).forEachIndexed { indice, (nombre, detalle) ->
                    val coincide = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                        false
                    } else if (ajustes.indiceResaltarSoloPrimera) {
                        indice == primerIndiceCoincidente
                    } else {
                        letraInicialIndice(nombre, ajustes.indiceIncluirEnie) == letraArrastrada
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .background(if (coincide) Ambar.copy(alpha = 0.18f) else SuperficieAlta)
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(if (coincide) Ambar else Borde),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                nombre.take(1),
                                color = if (coincide) ColorSobreAcento else TextoPrincipal,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                nombre,
                                color = if (coincide) Ambar else TextoPrincipal,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1
                            )
                            Text(detalle, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}
