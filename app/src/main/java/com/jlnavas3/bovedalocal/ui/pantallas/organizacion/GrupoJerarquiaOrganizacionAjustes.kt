package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.layout.padding
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Grupo de ajustes moleculares para seleccionar la jerarquía estructural entre Identidades y Categorías.
 */
@Composable
fun GrupoJerarquiaOrganizacionAjustes(
    jerarquiaActual: JerarquiaOrganizacion,
    mostrarId: Boolean,
    alSeleccionarJerarquia: (JerarquiaOrganizacion) -> Unit,
    alGestionarCategorias: () -> Unit = {},
    habilitado: Boolean = true,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Jerarquía de organización",
        icono = Icons.AutoMirrored.Filled.AltRoute,
        colorIcono = Color(0xFF0D9488),
        idGrupo = "03-LST-JER",
        mostrarId = mostrarId,
        modifier = if (habilitado) modifier else modifier.alpha(0.55f)
    ) {
        if (!habilitado) {
            TextoCuerpo(
                texto = "Inactivo: las identidades están desactivadas en la lista principal.",
                tamano = TamanoCuerpo.MINI,
                color = TextoSecundario,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ComponenteSeparador(sangriaInicio = 16.dp)
        }
        ComponenteRadio(
            titulo = JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA.etiqueta,
            seleccionado = jerarquiaActual == JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
            alSeleccionar = { if (habilitado) alSeleccionarJerarquia(JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA) },
            idFila = "03-LST-JER-IDC",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteRadio(
            titulo = JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD.etiqueta,
            seleccionado = jerarquiaActual == JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD,
            alSeleccionar = { if (habilitado) alSeleccionarJerarquia(JerarquiaOrganizacion.CATEGORIA_SOBRE_IDENTIDAD) },
            idFila = "03-LST-JER-COI",
            mostrarId = mostrarId,
            colorAcento = ColorAcento
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteNavegacion(
            titulo = "Administrar categorías...",
            icono = null,
            idFila = "03-LST-CAT",
            mostrarId = mostrarId,
            alPulsar = alGestionarCategorias
        )
    }
}
