package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar

@Composable
fun FormularioEdicionIdentidad(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    var mostrarDoc by remember { mutableStateOf(false) }

    val tipoDoc = GestorCamposBase.valorDeCampo(campos, "Tipo de documento")
    val numero = GestorCamposBase.valorDeCampo(campos, "Número de documento")
    val nombre = GestorCamposBase.valorDeCampo(campos, "Nombre completo")
    val expedicion = GestorCamposBase.valorDeCampo(campos, "Expedición")
    val caducidad = GestorCamposBase.valorDeCampo(campos, "Caducidad")
    val pais = GestorCamposBase.valorDeCampo(campos, "País emisor")

    fun abrirSelectorFecha(clave: String, valorActual: String) {
        val parseado = FormateadorCampos.parsearFecha(valorActual, ajustes.formatoFecha)
        val cal = Calendar.getInstance()
        val y = parseado?.first ?: cal.get(Calendar.YEAR)
        val m = parseado?.second ?: cal.get(Calendar.MONTH)
        val d = parseado?.third ?: cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(contexto, { _, anio, mes, dia ->
            val fechaFormateada = FormateadorCampos.formatearFecha(anio, mes, dia, ajustes.formatoFecha)
            alCambiarCampos(GestorCamposBase.actualizarValor(campos, clave, fechaFormateada, TipoCampo.FECHA))
        }, y, m, d).show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = tipoDoc,
            etiqueta = "Tipo (DNI, Pasaporte, Licencia...)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de documento", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = numero,
            etiqueta = "Número de documento",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de documento", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarDoc,
            alAlternarMostrarContrasena = { mostrarDoc = !mostrarDoc },
            monoespaciada = true
        )

        CampoBoveda(
            valor = nombre,
            etiqueta = "Nombre completo del titular",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Nombre completo", it, TipoCampo.TEXTO))
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = expedicion,
                etiqueta = "Expedición (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha("Expedición", expedicion) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha("Expedición", expedicion) },
                modifier = Modifier.weight(1f)
            )

            CampoBoveda(
                valor = caducidad,
                etiqueta = "Caducidad (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha("Caducidad", caducidad) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha("Caducidad", caducidad) },
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = pais,
            etiqueta = "País emisor",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "País emisor", it, TipoCampo.TEXTO))
            }
        )
    }
}
