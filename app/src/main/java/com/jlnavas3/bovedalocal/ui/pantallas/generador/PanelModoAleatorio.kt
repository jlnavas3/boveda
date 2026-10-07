package com.jlnavas3.bovedalocal.ui.pantallas.generador

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SwitchExcluirAmbiguos

@Composable
fun PanelModoAleatorio(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccion("Longitud: ${opciones.longitud} caracteres")
        SliderBoveda(
            value = opciones.longitud.toFloat(),
            onValueChange = {
                val nuevo = it.roundToInt().coerceIn(AjustesDefaults.Generador.LONGITUD_MIN, AjustesDefaults.Generador.LONGITUD_MAX)
                if (nuevo != opciones.longitud) {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(longitud = nuevo))
                }
            },
            valueRange = AjustesDefaults.Generador.LONGITUD_MIN.toFloat()..AjustesDefaults.Generador.LONGITUD_MAX.toFloat()
        )

        Spacer(Modifier.height(8.dp))
        SwitchExcluirAmbiguos(
            excluirAmbiguos = opciones.excluirAmbiguos,
            alCambiar = { alCambiarOpciones(opciones.copy(excluirAmbiguos = it)) },
            haptica = haptica
        )

        var mostrarCampoSimbolos by remember { mutableStateOf(false) }

        if (opciones.simbolos) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        haptica.tic()
                        mostrarCampoSimbolos = !mostrarCampoSimbolos
                    }
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (mostrarCampoSimbolos) "Ocultar símbolos personalizados" else "Personalizar símbolos permitidos",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Icon(
                    imageVector = if (mostrarCampoSimbolos) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (mostrarCampoSimbolos) "Ocultar" else "Mostrar",
                    tint = TextoSecundario,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (mostrarCampoSimbolos) {
                val contexto = LocalContext.current
                Spacer(Modifier.height(4.dp))
                CampoBoveda(
                    valor = opciones.simbolosPersonalizados,
                    etiqueta = "Símbolos permitidos (#$!)",
                    alCambiar = { alCambiarOpciones(opciones.copy(simbolosPersonalizados = it)) },
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
                                    alCambiarOpciones(opciones.copy(simbolosPersonalizados = PasswordGenerator.SIMBOLOS))
                                    Toast.makeText(contexto, "Símbolos por defecto restaurados", Toast.LENGTH_SHORT).show()
                                }
                        )
                    }
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Edita o excluye símbolos no soportados por ciertos servicios",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (!opciones.mayusculas && !opciones.minusculas && !opciones.digitos && !opciones.simbolos) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Activa al menos un tipo de carácter en el menú desplegable superior.",
                color = Peligro,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
