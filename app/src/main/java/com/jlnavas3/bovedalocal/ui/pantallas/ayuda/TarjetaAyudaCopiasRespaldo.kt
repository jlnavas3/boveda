/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta explicativa sobre copias de seguridad cifradas, vista previa de respaldo y kit de emergencia.
 */
@Composable
fun TarjetaAyudaCopiasRespaldo(modifier: Modifier = Modifier) {
    ComponenteGrupo(
        etiqueta = "Copias de seguridad y rescate",
        icono = Icons.Filled.Backup,
        colorIcono = Color(0xFF43A047),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Copias cifradas con inspección previa (Dry-Run):",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Al restaurar un archivo de respaldo (.bvda), la aplicación te mostrará primero un resumen con el número de entradas, categorías e identidades antes de aplicar cualquier cambio sobre tu base de datos actual.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Kit de emergencia:",
                fontSize = 13.sp,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Genera un documento imprimible con tus claves maestras y guárdalo en un lugar seguro bajo llave en tu hogar. Es tu salvavidas si alguna vez olvidas la clave maestra o pierdes el acceso biométrico.",
                fontSize = 12.sp,
                color = TextoSecundario,
                lineHeight = 17.sp
            )
        }
    }
}
