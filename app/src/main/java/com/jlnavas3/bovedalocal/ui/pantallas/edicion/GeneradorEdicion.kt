package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorCaracteresGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorPlantillaPatron
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import kotlin.math.roundToInt

@Composable
fun GeneradorEnLineaEdicion(
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    alGenerarContrasena: (String) -> Unit,
    haptica: Haptica
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonPrimario(
            texto = "Generar",
            icono = Icons.Filled.AutoAwesome,
            modifier = Modifier.weight(1f)
        ) {
            haptica.toque()
            alGenerarContrasena(PasswordGenerator.generar(opcionesGenerador))
        }
        SelectorModoEdicion(
            opciones = opcionesGenerador,
            alCambiarOpciones = {
                haptica.tic()
                alCambiarOpciones(it)
            },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(10.dp))

    when {
        opcionesGenerador.modoFrase -> {
            EtiquetaSeccion("Palabras Diceware: ${opcionesGenerador.palabras}")
            SliderBoveda(
                value = opcionesGenerador.palabras.toFloat(),
                onValueChange = {
                    val nuevo = it.roundToInt().coerceIn(3, 12)
                    if (nuevo != opcionesGenerador.palabras) {
                        haptica.tic()
                        alCambiarOpciones(opcionesGenerador.copy(palabras = nuevo))
                    }
                },
                valueRange = 3f..12f,
                steps = 8
            )
        }
        opcionesGenerador.modoPatron -> {
            SelectorPlantillaPatron(
                patronActual = opcionesGenerador.patron,
                alSeleccionarPlantilla = {
                    haptica.tic()
                    alCambiarOpciones(opcionesGenerador.copy(patron = it))
                }
            )
            Spacer(Modifier.height(8.dp))
            CampoBoveda(
                valor = opcionesGenerador.patron,
                etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX-XXXXX)",
                alCambiar = { alCambiarOpciones(opcionesGenerador.copy(patron = it)) },
                monoespaciada = true
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "X: alfanum | A: mayús | a: minús | 9: dígito | w: palabra Diceware",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 50% Slider de caracteres
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Longitud",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "${opcionesGenerador.longitud} car.",
                            color = ColorTitulos,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    SliderBoveda(
                        value = opcionesGenerador.longitud.toFloat(),
                        onValueChange = {
                            val nuevo = it.roundToInt().coerceIn(AjustesDefaults.Generador.LONGITUD_MIN, AjustesDefaults.Generador.LONGITUD_MAX)
                            if (nuevo != opcionesGenerador.longitud) {
                                haptica.tic()
                                alCambiarOpciones(opcionesGenerador.copy(longitud = nuevo))
                            }
                        },
                        valueRange = AjustesDefaults.Generador.LONGITUD_MIN.toFloat()..AjustesDefaults.Generador.LONGITUD_MAX.toFloat()
                    )
                }

                // 50% Menú desplegable con todos los switches (A-Z, a-z, 0-9, #$!)
                SelectorCaracteresGenerador(
                    opciones = opcionesGenerador,
                    alCambiarOpciones = {
                        haptica.tic()
                        alCambiarOpciones(it)
                    },
                    haptica = haptica,
                    modifier = Modifier.weight(1f)
                )
            }

            if (opcionesGenerador.simbolos) {
                SeccionSimbolosPersonalizadosEdicion(
                    opcionesGenerador = opcionesGenerador,
                    alCambiarOpciones = alCambiarOpciones,
                    haptica = haptica
                )
            }
        }
    }
}
