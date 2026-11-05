/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal para sugerir la purga o eliminación del contacto importado en la agenda pública
 * de Android, permitiendo mantenerlo exclusivamente protegido dentro de Bóveda Local.
 */
@Composable
fun DialogoConfirmarPurgaAgenda(
    abierto: Boolean,
    alCerrar: () -> Unit,
    nombreContacto: String,
    alAbrirFichaSistema: () -> Unit
) {
    if (!abierto) return

    DialogoBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        titulo = "Privacidad de agenda",
        icono = Icons.Filled.DeleteSweep,
        colorIcono = ColorAcento,
        botonConfirmar = {
            BotonTextoBoveda(
                texto = "Abrir ficha para eliminar",
                alPulsar = {
                    alCerrar()
                    alAbrirFichaSistema()
                },
                tipo = TipoBotonTexto.PRIMARIO
            )
        },
        botonDescartar = {
            BotonTextoBoveda(
                texto = "Conservar en ambos",
                alPulsar = alCerrar,
                tipo = TipoBotonTexto.SECUNDARIO
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "«$nombreContacto» ha sido resguardado con cifrado en tu Bóveda.",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextoPrincipal
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "¿Deseas abrir la ficha del contacto en la agenda del sistema para eliminarlo de los contactos públicos de Android y mantenerlo de forma confidencial únicamente aquí?",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}
