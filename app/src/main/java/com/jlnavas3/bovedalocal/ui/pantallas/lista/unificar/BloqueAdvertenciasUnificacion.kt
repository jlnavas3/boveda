/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.unificar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.DiscrepanciasUnificacion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Agrupa y visualiza todas las advertencias y detalles de discrepanicas detectadas para la unificación.
 */
@Composable
fun BloqueAdvertenciasUnificacion(
    discrepancias: DiscrepanciasUnificacion,
    principal: Entrada,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (discrepancias.hayServiciosDiferentes) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.WarningAmber,
                colorIcono = Peligro,
                fondo = Peligro.copy(alpha = 0.12f),
                borde = Peligro.copy(alpha = 0.35f),
                titulo = "Servicios diferentes detectados",
                descripcion = "Las entradas seleccionadas pertenecen a sitios o apps distintos (${discrepancias.servicios.joinToString(", ")}). Al unificarlas, la entrada principal conservará su nombre y enlaces, mientras que los datos de las secundarias se respaldarán en notas y campos."
            )
        }

        if (discrepancias.hayUsuariosDiferentes) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.Person,
                colorIcono = ColorIconosInternos,
                fondo = ColorCampoAjustes.copy(alpha = 0.6f),
                titulo = "Usuarios diferentes (${discrepancias.usuarios.size})",
                descripcion = "Se conservará el usuario '${principal.usuario.ifBlank { "Sin usuario" }}' como principal. Los demás usuarios se respaldarán en notas."
            )
        }

        if (discrepancias.hayContrasenasDiferentes) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.Key,
                colorIcono = ColorIconosInternos,
                fondo = ColorCampoAjustes.copy(alpha = 0.6f),
                titulo = "Contraseñas distintas (${discrepancias.totalContrasenas})",
                descripcion = "Se mantendrá la contraseña de la entrada principal. Las contraseñas secundarias se guardarán en el historial de contraseñas anteriores."
            )
        }

        if (discrepancias.hayTotpsMultiples || discrepancias.hayPasskeysMultiples) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.Security,
                colorIcono = ColorIconosInternos,
                fondo = ColorCampoAjustes.copy(alpha = 0.6f),
                titulo = "Múltiples factores de seguridad",
                descripcion = "Los códigos 2FA y Llaves de acceso (Passkeys) secundarios se conservarán estructurados en 'Credenciales alternativas' sin pérdida."
            )
        }

        if (discrepancias.hayTiposDiferentes) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.WarningAmber,
                colorIcono = Peligro,
                fondo = Peligro.copy(alpha = 0.12f),
                borde = Peligro.copy(alpha = 0.35f),
                titulo = "Tipos de entrada diferentes",
                descripcion = "Estás unificando entradas de distintos tipos (${discrepancias.tiposDiferentes.joinToString(", ") { it.etiqueta }}). Se mantendrá el tipo '${principal.tipo.etiqueta}' como principal y los datos especializados de las secundarias se respaldarán en notas."
            )
        }

        discrepancias.advertenciasNativas.forEach { adv ->
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.WarningAmber,
                colorIcono = ColorIconosInternos,
                fondo = ColorCampoAjustes.copy(alpha = 0.6f),
                titulo = "Discrepancia en datos especializados",
                descripcion = adv
            )
        }

        if (!discrepancias.tieneAlgunaAdvertencia) {
            TarjetaAdvertenciaUnificacion(
                icono = Icons.Filled.CheckCircle,
                colorIcono = ColorIconosInternos,
                fondo = ColorCampoAjustes.copy(alpha = 0.6f),
                titulo = "Entradas idénticas",
                descripcion = "Las entradas seleccionadas son copias exactas. Se conservará la entrada principal y se eliminarán las redundancias de forma segura."
            )
        }
    }
}
