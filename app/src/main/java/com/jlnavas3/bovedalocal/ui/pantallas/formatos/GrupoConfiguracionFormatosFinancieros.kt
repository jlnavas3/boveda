/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.util.FormateadorCampos

@Composable
fun GrupoConfiguracionFormatosFinancieros(
    mostrarIdsAjustes: Boolean,
    formatoTarjeta: String,
    formatoExpiracionTarjeta: String,
    formatoIban: String,
    alCambiarFormatoTarjeta: (String) -> Unit,
    alCambiarFormatoExpiracionTarjeta: (String) -> Unit,
    alCambiarFormatoIban: (String) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Formatos financieros y bancarios",
        icono = Icons.Filled.CreditCard,
        colorIcono = Color(0xFF26A69A),
        alRestablecer = alRestablecerGrupo,
        idGrupo = "02-APA-FMT-G03",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        val opcionesTarjeta = remember {
            listOf(
                OpcionSelectorModal(
                    valor = FormateadorCampos.TARJETA_4X4,
                    etiquetaFila = "Espaciado 4x4",
                    etiquetaModal = "Espaciado 4x4 (#### #### #### ####)",
                    descripcionModal = "Agrupa en bloques de 4 dígitos para facilitar lectura (ej. 4532 8910 2345 6789)",
                    icono = Icons.Filled.CreditCard
                ),
                OpcionSelectorModal(
                    valor = FormateadorCampos.TARJETA_SIN_ESPACIOS,
                    etiquetaFila = "Sin espacios",
                    etiquetaModal = "Continuo sin espacios (################)",
                    descripcionModal = "Cadena numérica continua sin separadores",
                    icono = Icons.Filled.CreditCard
                )
            )
        }
        ComponenteSelectorModal(
            titulo = "Número de tarjeta",
            descripcionModal = "Formato visual para números de tarjetas de débito y crédito",
            icono = null,
            idFila = "02-APA-FMT-TAR",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoTarjeta,
            opciones = opcionesTarjeta,
            alSeleccionar = alCambiarFormatoTarjeta
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        val opcionesExpiracion = remember {
            listOf(
                OpcionSelectorModal(
                    valor = FormateadorCampos.EXPIRACION_MMAA,
                    etiquetaFila = "MM/AA",
                    etiquetaModal = "Mes y Año corto (MM/AA)",
                    descripcionModal = "Estándar bancario universal (ej. 12/28)",
                    icono = Icons.Filled.CalendarToday
                ),
                OpcionSelectorModal(
                    valor = FormateadorCampos.EXPIRACION_MMAAAA,
                    etiquetaFila = "MM/AAAA",
                    etiquetaModal = "Mes y Año de 4 dígitos (MM/AAAA)",
                    descripcionModal = "Año completo (ej. 12/2028)",
                    icono = Icons.Filled.CalendarToday
                )
            )
        }
        ComponenteSelectorModal(
            titulo = "Vencimiento de tarjeta",
            descripcionModal = "Estándar de representación para fechas de caducidad de tarjetas",
            icono = null,
            idFila = "02-APA-FMT-VNC",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoExpiracionTarjeta,
            opciones = opcionesExpiracion,
            alSeleccionar = alCambiarFormatoExpiracionTarjeta
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        val opcionesIban = remember {
            listOf(
                OpcionSelectorModal(
                    valor = FormateadorCampos.IBAN_BLOQUES_4,
                    etiquetaFila = "Bloques de 4",
                    etiquetaModal = "Bloques de 4 caracteres (ES91 2100...)",
                    descripcionModal = "Separación estándar internacional de 4 en 4 (ej. ES91 2100 0418 4502)",
                    icono = Icons.Filled.AccountBalance
                ),
                OpcionSelectorModal(
                    valor = FormateadorCampos.IBAN_CONTINUO,
                    etiquetaFila = "Continuo",
                    etiquetaModal = "Continuo sin espacios (ES912100...)",
                    descripcionModal = "Texto continuo sin separadores de espacio",
                    icono = Icons.Filled.AccountBalance
                )
            )
        }
        ComponenteSelectorModal(
            titulo = "Formato IBAN",
            descripcionModal = "Agrupación visual para códigos internacionales de cuenta bancaria",
            icono = null,
            idFila = "02-APA-FMT-IBN",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoIban,
            opciones = opcionesIban,
            alSeleccionar = alCambiarFormatoIban
        )
    }
}
