/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Insignia sutil y sobria para mostrar el conteo de elementos asociados a un perfil
 * (documentos, tarjetas, cuentas, contactos) con icono vectorial y texto en color secundario.
 */
@Composable
fun InsigniaConteoAsociacionPerfil(
    icono: ImageVector,
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    if (cantidad <= 0) return

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(ColorCampoAjustes)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = TextoSecundario,
            modifier = Modifier.size(12.dp)
        )
        TextoCuerpo(
            texto = "$cantidad",
            tamano = TamanoCuerpo.MINI,
            color = TextoSecundario,
            maxLineas = 1
        )
    }
}
