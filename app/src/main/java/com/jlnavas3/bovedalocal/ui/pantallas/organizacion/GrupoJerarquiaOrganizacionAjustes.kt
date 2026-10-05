package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Grupo de ajustes moleculares para seleccionar la jerarquia estructural entre Identidades y Colecciones.
 */
@Composable
fun GrupoJerarquiaOrganizacionAjustes(
    jerarquiaActual: JerarquiaOrganizacion,
    mostrarId: Boolean,
    alSeleccionarJerarquia: (JerarquiaOrganizacion) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Jerarquía de organización",
        icono = Icons.AutoMirrored.Filled.AltRoute,
        colorIcono = Color(0xFF0D9488),
        idGrupo = "03-LST-JER",
        mostrarId = mostrarId,
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION.etiqueta,
            seleccionado = jerarquiaActual == JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION,
            alSeleccionar = { alSeleccionarJerarquia(JerarquiaOrganizacion.IDENTIDAD_SOBRE_COLECCION) },
            idFila = "03-LST-JER-IDC",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = JerarquiaOrganizacion.COLECCION_SOBRE_IDENTIDAD.etiqueta,
            seleccionado = jerarquiaActual == JerarquiaOrganizacion.COLECCION_SOBRE_IDENTIDAD,
            alSeleccionar = { alSeleccionarJerarquia(JerarquiaOrganizacion.COLECCION_SOBRE_IDENTIDAD) },
            idFila = "03-LST-JER-COI",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
    }
}
