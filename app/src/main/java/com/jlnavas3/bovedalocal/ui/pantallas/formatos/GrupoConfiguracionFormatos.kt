package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
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
fun GrupoConfiguracionFormatos(
    mostrarIdsAjustes: Boolean,
    formatoFecha: String,
    formatoHora: String,
    formatoTelefono: String,
    separadorDecimal: String,
    alCambiarFormatoFecha: (String) -> Unit,
    alCambiarFormatoHora: (String) -> Unit,
    alCambiarFormatoTelefono: (String) -> Unit,
    alCambiarSeparadorDecimal: (String) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Configuración de formatos",
        icono = Icons.AutoMirrored.Filled.FormatListBulleted,
        colorIcono = Color(0xFFFFA000),
        alRestablecer = alRestablecerGrupo,
        idGrupo = "03-LST-FMT-G02",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
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
            icono = null,
            idFila = "03-LST-FMT-FCH",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoFecha,
            opciones = opcionesFecha,
            alSeleccionar = alCambiarFormatoFecha
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        val opcionesHora = remember {
            listOf(
                OpcionSelectorModal(FormateadorCampos.HORA_24H, "24 horas", "Formato 24 horas", "Estándar militar / internacional (ej. 19:30)", Icons.Filled.Schedule),
                OpcionSelectorModal(FormateadorCampos.HORA_12H, "12 horas", "Formato 12 horas (AM/PM)", "Formato con sufijo de meridiano (ej. 07:30 PM)", Icons.Filled.Schedule)
            )
        }
        ComponenteSelectorModal(
            titulo = "Formato de hora",
            descripcionModal = "Selecciona el estándar de representación horaria",
            icono = null,
            idFila = "03-LST-FMT-HOR",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoHora,
            opciones = opcionesHora,
            alSeleccionar = alCambiarFormatoHora
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

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
            icono = null,
            idFila = "03-LST-FMT-TEL",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = formatoTelefono,
            opciones = opcionesTelefono,
            alSeleccionar = alCambiarFormatoTelefono
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        val opcionesDecimal = remember {
            listOf(
                OpcionSelectorModal(".", "Punto (.)", "Punto decimal (.)", "Estándar anglosajón e internacional (ej. 1250.50)", Icons.Filled.Numbers),
                OpcionSelectorModal(",", "Coma (,)", "Coma decimal (,)", "Estándar hispano y europeo (ej. 1250,50)", Icons.Filled.Numbers)
            )
        }
        ComponenteSelectorModal(
            titulo = "Separador decimal",
            descripcionModal = "Símbolo numérico para fracciones decimales en importes y notas",
            icono = null,
            idFila = "03-LST-FMT-DEC",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = separadorDecimal,
            opciones = opcionesDecimal,
            alSeleccionar = alCambiarSeparadorDecimal
        )
    }
}
