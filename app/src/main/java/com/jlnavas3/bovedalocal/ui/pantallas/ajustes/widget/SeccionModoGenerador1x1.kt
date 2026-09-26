package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorPlantillaPatron
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun SeccionModoGenerador1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Modo de generación",
        idGrupo = "03.3.G2",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Elige cómo se creará la clave al pulsar el widget",
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Generación aleatoria",
            icono = Icons.Filled.Refresh,
            colorIcono = ColorIconosInternos,
            idFila = "03.3.6",
            mostrarId = ajustes.mostrarIdsAjustes,
            seleccionado = ajustes.widget1x1Modo == "aleatoria",
            alSeleccionar = {
                haptica.tic()
                vm.ajustarWidget1x1Modo("aleatoria")
            }
        )

        ComponenteSeparador()

        ComponenteRadio(
            titulo = "Por patrón",
            icono = Icons.Filled.Pattern,
            colorIcono = ColorIconosInternos,
            idFila = "03.3.7",
            mostrarId = ajustes.mostrarIdsAjustes,
            seleccionado = ajustes.widget1x1Modo == "patron",
            alSeleccionar = {
                haptica.tic()
                vm.ajustarWidget1x1Modo("patron")
            }
        )

        if (ajustes.widget1x1Modo == "aleatoria") {
            ComponenteSeparador()

            ComponenteSlider(
                titulo = "Longitud de contraseña",
                valor = ajustes.widget1x1Longitud.toFloat(),
                valorTexto = "${ajustes.widget1x1Longitud} caracteres",
                rango = 4f..64f,
                pasos = 59,
                etiquetaMin = "4",
                etiquetaMax = "64",
                idFila = "03.3.8",
                mostrarId = ajustes.mostrarIdsAjustes,
                icono = Icons.Filled.Numbers,
                colorIcono = ColorIconosInternos,
                alCambiar = {
                    vm.ajustarWidget1x1Longitud(it.roundToInt())
                    haptica.tic()
                }
            )

            ComponenteSeparador()

            var mostrarCampoSimbolos1x1 by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            haptica.tic()
                            mostrarCampoSimbolos1x1 = !mostrarCampoSimbolos1x1
                        }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (mostrarCampoSimbolos1x1) "Ocultar símbolos personalizados" else "Personalizar símbolos permitidos",
                        color = ColorTitulos,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Icon(
                        imageVector = if (mostrarCampoSimbolos1x1) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (mostrarCampoSimbolos1x1) "Ocultar" else "Mostrar",
                        tint = TextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (mostrarCampoSimbolos1x1) {
                    Spacer(Modifier.height(6.dp))
                    CampoBoveda(
                        valor = ajustes.widget1x1Simbolos,
                        etiqueta = "Símbolos permitidos (#$!)",
                        alCambiar = { vm.ajustarWidget1x1Simbolos(it) },
                        monoespaciada = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.RestartAlt,
                                contentDescription = "Restaurar símbolos por defecto",
                                tint = ColorIconosInternos,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        haptica.exito()
                                        vm.ajustarWidget1x1Simbolos(PasswordGenerator.SIMBOLOS)
                                    }
                            )
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Edita o excluye símbolos no soportados por ciertos servicios",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        } else {
            ComponenteSeparador()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Plantillas rápidas:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(6.dp))
                SelectorPlantillaPatron(
                    patronActual = ajustes.widget1x1Patron,
                    alSeleccionarPlantilla = { nuevoPatron ->
                        haptica.tic()
                        vm.ajustarWidget1x1Patron(nuevoPatron)
                    }
                )
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = ajustes.widget1x1Patron,
                    etiqueta = "Patrón personalizado (C: mayús, c: minús, d: dígito, s: símb)",
                    alCambiar = { vm.ajustarWidget1x1Patron(it) },
                    monoespaciada = true
                )
            }
        }
    }
}
