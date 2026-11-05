/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.csv

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente que ofrece la opción de importar contactos de la agenda nativa
 * del teléfono hacia la bóveda en la pantalla de transferencias e importaciones.
 */
@Composable
fun GrupoImportacionContactos(
    mostrarId: Boolean,
    alIniciarImportacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Contactos (vCard / CSV)",
        icono = Icons.Filled.Person,
        colorIcono = ColorAcento,
        idGrupo = "05-COP-CNT-G01",
        mostrarId = mostrarId,
        descripcion = "Importación segura con cero permisos mediante selector de archivos (.vcf / .csv)",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextoSubtitulo(
                texto = "Permite seleccionar un archivo vCard (.vcf) o CSV exportado de tu agenda. Cada contacto se previsualiza para que elijas cuáles cifrar en la bóveda, sin otorgar permisos de acceso a tu agenda."
            )
        }
        SeparadorFilaSimple()
        ComponenteBotonFila(
            titulo = "Seleccionar archivo de contactos (.vcf / .csv)",
            icono = null,
            idFila = "05-COP-CNT-IMP",
            mostrarId = mostrarId,
            alPulsar = alIniciarImportacion
        )
    }
}
