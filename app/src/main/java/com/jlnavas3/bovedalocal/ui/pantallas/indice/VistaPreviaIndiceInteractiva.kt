package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun VistaPreviaIndiceInteractiva(
    ajustes: AjustesApp,
    letraArrastrada: Char?,
    mockItems: List<Pair<String, String>>
) {
    ComponenteGrupo(
        etiqueta = "Vista previa interactiva",
        idGrupo = "03.4.G1",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Desliza en el borde derecho para calibrar en tiempo real"
    ) {
        val primerIndiceCoincidente = remember(mockItems, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
            if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
            else if (ajustes.indiceResaltarSoloPrimera) {
                mockItems.indexOfFirst { (nombre, _) ->
                    letraInicialIndice(nombre, ajustes.indiceIncluirEnie) == letraArrastrada
                }.takeIf { it >= 0 }
            } else null
        }

        Column(modifier = Modifier.padding(14.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(Superficie)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                mockItems.forEachIndexed { indice, (nombre, detalle) ->
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
                            .then(
                                if (coincide) Modifier.border(1.5.dp, Ambar, FormaPequena) else Modifier
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
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
