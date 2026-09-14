package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.OpcionAjuste
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SelectorAjuste
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.FormateadorCampos

@Composable
fun PantallaFormatosCampos(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsState()

    val ejemploFecha = FormateadorCampos.formatearFecha(2026, 11, 31, ajustes.formatoFecha)
    val ejemploHora = FormateadorCampos.formatearHora(19, 30, ajustes.formatoHora)
    val ejemploTel = FormateadorCampos.aplicarMascaraTelefono("612345678", ajustes.formatoTelefono)
    val ejemploDecimal = "1250${ajustes.separadorDecimal}50"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidiana)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        CabeceraPantalla(
            titulo = "Formatos de campos",
            subtitulo = "Personaliza máscaras de teléfono, fechas, horas y separadores numéricos",
            alVolver = { vm.volverAtras() }
        )

        Spacer(Modifier.height(16.dp))

        // Vista previa en vivo
        TarjetaBovedaDesplegable(
            titulo = "Vista previa en tiempo real",
            descripcion = "Previsualización instantánea de los formatos activos",
            icono = Icons.Filled.Tune,
            colorIcono = ColorSalud,
            inicialmenteAbierta = true
        ) {
            // Fila de ejemplos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaEjemploChip(
                    etiqueta = "Fecha",
                    valor = ejemploFecha,
                    modifier = Modifier.weight(1f)
                )
                FilaEjemploChip(
                    etiqueta = "Hora",
                    valor = ejemploHora,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilaEjemploChip(
                    etiqueta = "Teléfono",
                    valor = ejemploTel,
                    modifier = Modifier.weight(1.3f)
                )
                FilaEjemploChip(
                    etiqueta = "Decimal",
                    valor = ejemploDecimal,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Selectores de formato
        EtiquetaSeccion("Configuración de formatos", icono = Icons.Filled.Tune, colorIcono = ColorAcento)
        Spacer(Modifier.height(10.dp))

        SelectorAjuste(
            titulo = "Formato de fecha",
            icono = Icons.Filled.CalendarToday,
            colorIcono = ColorAcento,
            seleccionado = FormateadorCampos.OPCIONES_FECHA.firstOrNull { it.first == ajustes.formatoFecha }?.second ?: ajustes.formatoFecha,
            opciones = FormateadorCampos.OPCIONES_FECHA.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.CalendarToday)
            },
            alSeleccionar = {
                haptica.tic()
                vm.ajustarFormatoFecha(it)
            }
        )

        Spacer(Modifier.height(10.dp))

        SelectorAjuste(
            titulo = "Formato de hora",
            icono = Icons.Filled.Schedule,
            colorIcono = ColorGenerador,
            seleccionado = FormateadorCampos.OPCIONES_HORA.firstOrNull { it.first == ajustes.formatoHora }?.second ?: ajustes.formatoHora,
            opciones = FormateadorCampos.OPCIONES_HORA.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.Schedule)
            },
            alSeleccionar = {
                haptica.tic()
                vm.ajustarFormatoHora(it)
            }
        )

        Spacer(Modifier.height(10.dp))

        SelectorAjuste(
            titulo = "Máscara de teléfono",
            icono = Icons.Filled.Phone,
            colorIcono = ColorPasskeys,
            seleccionado = FormateadorCampos.OPCIONES_TELEFONO.firstOrNull { it.first == ajustes.formatoTelefono }?.second ?: ajustes.formatoTelefono,
            opciones = FormateadorCampos.OPCIONES_TELEFONO.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.Phone)
            },
            alSeleccionar = {
                haptica.tic()
                vm.ajustarFormatoTelefono(it)
            }
        )

        Spacer(Modifier.height(10.dp))

        SelectorAjuste(
            titulo = "Separador decimal",
            icono = Icons.Filled.Numbers,
            colorIcono = ColorSalud,
            seleccionado = FormateadorCampos.OPCIONES_DECIMAL.firstOrNull { it.first == ajustes.separadorDecimal }?.second ?: ajustes.separadorDecimal,
            opciones = FormateadorCampos.OPCIONES_DECIMAL.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.Numbers)
            },
            alSeleccionar = {
                haptica.tic()
                vm.ajustarSeparadorDecimal(it)
            }
        )
    }
}

@Composable
private fun FilaEjemploChip(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(FormaPequena)
            .background(SuperficieAlta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = etiqueta,
                color = TextoSecundario,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = valor,
                color = TextoPrincipal,
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
