package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import java.util.Calendar

/**
 * Tarjeta individual de edición para un campo personalizado con soporte para selección de fecha/hora,
 * formatos numéricos, máscaras telefónicas y alternancia de datos sensibles.
 */
@Composable
fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    var mostrarValor by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo

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

    val fondoTarjeta = ColorCampoAjustes
    val bordeTarjeta = ColorBordeActual

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(fondoTarjeta)
            .border(1.dp, bordeTarjeta, FormaCampo)
            .padding(12.dp)
    ) {
        // Cabecera del campo: Número, Badge de Tipo, Sensible y Eliminar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "#$numero",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )

                // Chip de tipo
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(SuperficieAlta)
                        .then(
                            if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                            else Modifier
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = campo.tipo.etiqueta,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                }

                // Chip de dato sensible
                if (esSensible) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(ColorAcento.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            Icons.Filled.Security,
                            contentDescription = "Sensible",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Sensible",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorIconosInternos
                        )
                    }
                }
            }

            IconButton(onClick = alEliminar, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar campo",
                    tint = Peligro,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Etiqueta del campo
        CampoBoveda(
            valor = campo.etiqueta,
            etiqueta = "Nombre del campo",
            alCambiar = { alModificar(campo.copy(etiqueta = it)) }
        )

        Spacer(Modifier.height(8.dp))

        // Valor del campo según su tipo específico
        when (campo.tipo) {
            TipoCampo.NOTAS -> {
                ComponenteCampoTexto(
                    valor = campo.valor,
                    etiqueta = "Notas / Contenido",
                    alCambiar = { alModificar(campo.copy(valor = it)) },
                    tipo = TipoCampoTexto.MULTILINEA,
                    esContrasena = esSensible,
                    mostrarContrasena = if (esSensible) mostrarValor else null,
                    alAlternarMostrarContrasena = if (esSensible) { { mostrarValor = !mostrarValor } } else null,
                    monoespaciada = esSensible
                )
            }
            TipoCampo.FECHA -> {
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Fecha (${ajustes.formatoFecha})",
                    alCambiar = {},
                    readOnly = true,
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
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Hora (${ajustes.formatoHora})",
                    alCambiar = {},
                    readOnly = true,
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
                    etiqueta = "Teléfono",
                    alCambiar = { nuevoValor ->
                        alModificar(campo.copy(valor = nuevoValor))
                    },
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
                    etiqueta = if (campo.tipo == TipoCampo.PIN) "PIN (solo números)" else "Número entero",
                    alCambiar = { raw ->
                        val sanitizado = FormateadorCampos.sanitizarNumero(raw)
                        alModificar(campo.copy(valor = sanitizado))
                    },
                    esContrasena = esSensible,
                    mostrarContrasena = mostrarValor,
                    alAlternarMostrarContrasena = if (esSensible) { { mostrarValor = !mostrarValor } } else null,
                    tecladoNumerico = true,
                    keyboardType = if (esSensible) KeyboardType.NumberPassword else KeyboardType.Number,
                    monoespaciada = true
                )
            }
            TipoCampo.DECIMAL -> {
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Número decimal (separador '${ajustes.separadorDecimal}')",
                    alCambiar = { raw ->
                        val sanitizado = FormateadorCampos.sanitizarDecimal(raw, ajustes.separadorDecimal)
                        alModificar(campo.copy(valor = sanitizado))
                    },
                    keyboardType = KeyboardType.Decimal,
                    monoespaciada = true
                )
            }
            TipoCampo.EMAIL -> {
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Correo electrónico",
                    alCambiar = { alModificar(campo.copy(valor = it)) },
                    keyboardType = KeyboardType.Email
                )
            }
            TipoCampo.URL -> {
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Dirección web (URL)",
                    alCambiar = { alModificar(campo.copy(valor = it)) },
                    keyboardType = KeyboardType.Uri
                )
            }
            else -> {
                CampoBoveda(
                    valor = campo.valor,
                    etiqueta = "Valor del campo",
                    alCambiar = { alModificar(campo.copy(valor = it)) },
                    esContrasena = esSensible,
                    mostrarContrasena = mostrarValor,
                    alAlternarMostrarContrasena = if (esSensible) { { mostrarValor = !mostrarValor } } else null
                )
            }
        }

        // Toggle rápido de sensibilidad
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .clip(FormaPequena)
                .clickable { alModificar(campo.copy(esSensible = !campo.esSensible)) }
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = null,
                tint = if (campo.esSensible) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (campo.esSensible) "Marcado como secreto (toca para desmarcar)" else "Marcar como secreto / sensible",
                style = MaterialTheme.typography.labelSmall,
                color = if (campo.esSensible) ColorIconosInternos else TextoSecundario
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaCampoPersonalizadoEdicionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TarjetaCampoPersonalizadoEdicion(
                numero = 1,
                campo = CampoPersonalizado(
                    etiqueta = "PIN de Acceso",
                    valor = "9942",
                    tipo = TipoCampo.PIN,
                    esSensible = true
                ),
                alModificar = {},
                alEliminar = {}
            )
            TarjetaCampoPersonalizadoEdicion(
                numero = 2,
                campo = CampoPersonalizado(
                    etiqueta = "Fecha de Emisión",
                    valor = "15/04/2025",
                    tipo = TipoCampo.FECHA,
                    esSensible = false
                ),
                alModificar = {},
                alEliminar = {}
            )
        }
    }
}

