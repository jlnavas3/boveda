package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar

/**
 * Microcomponente que gestiona la entrada de valor según el TipoCampo específico,
 * incluyendo teclados especializados, formateadores de máscara y selectores nativos de fecha/hora.
 */
@Composable
fun EntradaValorCampoPersonalizado(
    campo: CampoPersonalizado,
    esSensible: Boolean,
    mostrarValor: Boolean,
    alAlternarMostrarValor: () -> Unit,
    alModificar: (CampoPersonalizado) -> Unit,
    ajustes: AjustesApp,
    modifier: Modifier = Modifier,
    etiquetaPersonalizada: String? = null
) {
    val contexto = LocalContext.current

    fun abrirSelectorFecha() {
        val parseado = FormateadorCampos.parsearFecha(campo.valor, ajustes.formatoFecha)
        val cal = Calendar.getInstance()
        val y = parseado?.first ?: cal.get(Calendar.YEAR)
        val m = parseado?.second ?: cal.get(Calendar.MONTH)
        val d = parseado?.third ?: cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(contexto, { _, anio, mes, dia ->
            val fechaFormateada = FormateadorCampos.formatearFecha(anio, mes, dia, ajustes.formatoFecha)
            alModificar(campo.copy(valor = fechaFormateada))
        }, y, m, d).show()
    }

    fun abrirSelectorHora() {
        val parseado = FormateadorCampos.parsearHora(campo.valor, ajustes.formatoHora)
        val cal = Calendar.getInstance()
        val h = parseado?.first ?: cal.get(Calendar.HOUR_OF_DAY)
        val min = parseado?.second ?: cal.get(Calendar.MINUTE)
        val es24h = ajustes.formatoHora == FormateadorCampos.HORA_24H

        TimePickerDialog(contexto, { _, hora, minuto ->
            val horaFormateada = FormateadorCampos.formatearHora(hora, minuto, ajustes.formatoHora)
            alModificar(campo.copy(valor = horaFormateada))
        }, h, min, es24h).show()
    }

    val etiquetaFinal = etiquetaPersonalizada?.takeIf { it.isNotBlank() }

    when (campo.tipo) {
        TipoCampo.NOTAS -> {
            ComponenteCampoTexto(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Notas / Contenido",
                alCambiar = { alModificar(campo.copy(valor = it)) },
                tipo = TipoCampoTexto.MULTILINEA,
                esContrasena = esSensible,
                mostrarContrasena = if (esSensible) mostrarValor else null,
                alAlternarMostrarContrasena = if (esSensible) alAlternarMostrarValor else null,
                monoespaciada = esSensible,
                modifier = modifier
            )
        }
        TipoCampo.FECHA -> {
            val etiquetaFecha = if (etiquetaFinal != null) "$etiquetaFinal (${ajustes.formatoFecha})" else "Fecha (${ajustes.formatoFecha})"
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFecha,
                alCambiar = {},
                readOnly = true,
                modifier = modifier,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha() }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha() }
            )
        }
        TipoCampo.HORA -> {
            val etiquetaHora = if (etiquetaFinal != null) "$etiquetaFinal (${ajustes.formatoHora})" else "Hora (${ajustes.formatoHora})"
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaHora,
                alCambiar = {},
                readOnly = true,
                modifier = modifier,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorHora() }) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Elegir hora",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorHora() }
            )
        }
        TipoCampo.TELEFONO -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Teléfono",
                alCambiar = { nuevoValor ->
                    alModificar(campo.copy(valor = nuevoValor))
                },
                modifier = modifier,
                formateadorMascara = { raw ->
                    FormateadorCampos.aplicarMascaraTelefono(raw, ajustes.formatoTelefono)
                },
                keyboardType = KeyboardType.Phone,
                monoespaciada = true
            )
        }
        TipoCampo.NUMERO, TipoCampo.PIN -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: if (campo.tipo == TipoCampo.PIN) "PIN (solo números)" else "Número entero",
                alCambiar = { raw ->
                    val sanitizado = FormateadorCampos.sanitizarNumero(raw)
                    alModificar(campo.copy(valor = sanitizado))
                },
                modifier = modifier,
                esContrasena = esSensible,
                mostrarContrasena = mostrarValor,
                alAlternarMostrarContrasena = if (esSensible) alAlternarMostrarValor else null,
                tecladoNumerico = true,
                keyboardType = if (esSensible) KeyboardType.NumberPassword else KeyboardType.Number,
                monoespaciada = true
            )
        }
        TipoCampo.DECIMAL -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Número decimal (${ajustes.separadorDecimal})",
                alCambiar = { raw ->
                    val sanitizado = FormateadorCampos.sanitizarDecimal(raw, ajustes.separadorDecimal)
                    alModificar(campo.copy(valor = sanitizado))
                },
                modifier = modifier,
                keyboardType = KeyboardType.Decimal,
                monoespaciada = true
            )
        }
        TipoCampo.EMAIL -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Correo electrónico",
                alCambiar = { alModificar(campo.copy(valor = it)) },
                modifier = modifier,
                keyboardType = KeyboardType.Email
            )
        }
        TipoCampo.URL -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Dirección web (URL)",
                alCambiar = { alModificar(campo.copy(valor = it)) },
                modifier = modifier,
                keyboardType = KeyboardType.Uri
            )
        }
        else -> {
            CampoBoveda(
                valor = campo.valor,
                etiqueta = etiquetaFinal ?: "Valor del campo",
                alCambiar = { alModificar(campo.copy(valor = it)) },
                modifier = modifier,
                esContrasena = esSensible,
                mostrarContrasena = mostrarValor,
                alAlternarMostrarContrasena = if (esSensible) alAlternarMostrarValor else null
            )
        }
    }
}
