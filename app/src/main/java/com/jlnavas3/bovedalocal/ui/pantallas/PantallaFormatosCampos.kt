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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaFormatosCampos(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val ejemploFecha = FormateadorCampos.formatearFecha(2026, 11, 31, ajustes.formatoFecha)
    val ejemploHora = FormateadorCampos.formatearHora(19, 30, ajustes.formatoHora)
    val ejemploTel = FormateadorCampos.aplicarMascaraTelefono("612345678", ajustes.formatoTelefono)
    val ejemploDecimal = "1250${ajustes.separadorDecimal}50"
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Formatos de campos",
                idEtiqueta = "03.6",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Personaliza máscaras de teléfono, fechas, horas y separadores numéricos")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Vista previa en tiempo real
                ComponenteGrupo(
                    etiqueta = "Vista previa en tiempo real",
                    idGrupo = "03.6.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Ejemplos de visualización con los formatos seleccionados"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Configuración de formatos
                ComponenteGrupo(
                    etiqueta = "Configuración de formatos",
                    idGrupo = "03.6.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Formatos aplicados al registrar o consultar credenciales"
                ) {
                    val opcionesFecha = remember {
                        listOf(
                            OpcionSelectorModal(FormateadorCampos.FECHA_DD_MM_AAAA, "DD/MM/AAAA", "DD/MM/AAAA", "Día / Mes / Año (ej. 31/12/2026)", Icons.Filled.CalendarToday),
                            OpcionSelectorModal(FormateadorCampos.FECHA_AAAA_MM_DD, "AAAA-MM-DD", "AAAA-MM-DD", "Año-Mes-Día (ISO, ej. 2026-12-31)", Icons.Filled.CalendarToday),
                            OpcionSelectorModal(FormateadorCampos.FECHA_MM_DD_AAAA, "MM/DD/AAAA", "MM/DD/AAAA", "Mes / Día / Año (EE. UU., ej. 12/31/2026)", Icons.Filled.CalendarToday),
                            OpcionSelectorModal(FormateadorCampos.FECHA_DD_GUION_MM_AAAA, "DD-MM-AAAA", "DD-MM-AAAA", "Día-Mes-Año con guiones (ej. 31-12-2026)", Icons.Filled.CalendarToday)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Formato de fecha",
                        descripcionModal = "Elige cómo visualizar las fechas registradas en tus cuentas",
                        icono = Icons.Filled.CalendarToday,
                        colorIcono = ColorIconosInternos,
                        idFila = "03.6.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.formatoFecha,
                        opciones = opcionesFecha,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarFormatoFecha(it)
                        }
                    )

                    ComponenteSeparador()

                    val opcionesHora = remember {
                        listOf(
                            OpcionSelectorModal(FormateadorCampos.HORA_24H, "24 horas", "Formato 24 horas", "Estándar militar / internacional (ej. 19:30)", Icons.Filled.Schedule),
                            OpcionSelectorModal(FormateadorCampos.HORA_12H, "12 horas", "Formato 12 horas (AM/PM)", "Formato con sufijo de meridiano (ej. 07:30 PM)", Icons.Filled.Schedule)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Formato de hora",
                        descripcionModal = "Selecciona el estándar de representación horaria",
                        icono = Icons.Filled.Schedule,
                        colorIcono = ColorGenerador,
                        idFila = "03.6.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.formatoHora,
                        opciones = opcionesHora,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarFormatoHora(it)
                        }
                    )

                    ComponenteSeparador()

                    val opcionesTelefono = remember {
                        listOf(
                            OpcionSelectorModal(FormateadorCampos.TEL_ESPACIOS, "612 345 678", "Espacios (### ### ####)", "Ejemplo: 612 345 678", Icons.Filled.Phone),
                            OpcionSelectorModal(FormateadorCampos.TEL_GUIONES, "612-345-678", "Guiones (###-###-####)", "Ejemplo: 612-345-678", Icons.Filled.Phone),
                            OpcionSelectorModal(FormateadorCampos.TEL_PARENTESIS, "(612) 345-678", "Paréntesis ((###) ###-####)", "Ejemplo: (612) 345-678", Icons.Filled.Phone),
                            OpcionSelectorModal(FormateadorCampos.TEL_INTERNACIONAL, "+## ### ###", "Internacional (+## ### ### ####)", "Ejemplo: +34 612 345 678", Icons.Filled.Phone),
                            OpcionSelectorModal(FormateadorCampos.TEL_SIN_MASCARA, "Sin máscara", "Sin formato (libre)", "Permite introducir texto numérico libre", Icons.Filled.Phone)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Máscara de teléfono",
                        descripcionModal = "Máscara visual aplicada automáticamente en campos telefónicos",
                        icono = Icons.Filled.Phone,
                        colorIcono = ColorPasskeys,
                        idFila = "03.6.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.formatoTelefono,
                        opciones = opcionesTelefono,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarFormatoTelefono(it)
                        }
                    )

                    ComponenteSeparador()

                    val opcionesDecimal = remember {
                        listOf(
                            OpcionSelectorModal(".", "Punto (.)", "Punto decimal (.)", "Estándar anglosajón e internacional (ej. 1250.50)", Icons.Filled.Numbers),
                            OpcionSelectorModal(",", "Coma (,)", "Coma decimal (,)", "Estándar hispano y europeo (ej. 1250,50)", Icons.Filled.Numbers)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Separador decimal",
                        descripcionModal = "Símbolo numérico para fracciones decimales en importes y notas",
                        icono = Icons.Filled.Numbers,
                        colorIcono = ColorSalud,
                        idFila = "03.6.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.separadorDecimal,
                        opciones = opcionesDecimal,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarSeparadorDecimal(it)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer grupo",
                        alPulsar = {
                            vm.ajustarFormatoFecha("DD/MM/AAAA")
                            vm.ajustarFormatoHora("24h")
                            vm.ajustarFormatoTelefono("### ### ####")
                            vm.ajustarSeparadorDecimal(".")
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
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
