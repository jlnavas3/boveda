/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre el Escudo de Salud de la bóveda, auditoría offline y resolución de riesgos.
 */
@Composable
fun PantallaAyudaSalud(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Salud de la bóveda e insignia global",
        alVolver = alVolver,
        textoAccion = "Auditar Salud de la Bóveda",
        iconoAccion = Icons.Filled.HealthAndSafety,
        alEjecutarAccion = { vm.ir(Pantalla.SaludBoveda) }
    ) {
        TarjetaInformativaAyuda(titulo = "El Escudo de Salud (0% a 100%)") {
            Text(
                text = "Tener un gestor de contraseñas no garantiza seguridad si utilizas la misma clave en 10 sitios distintos o si tienes claves obsoletas. El Panel de Salud de Bóveda Local audita matemáticamente tu base de datos y calcula un índice global de salud entre el 0% y el 100%:\n\n• Verde Óptimo (≥ 85%): Bóveda blindada con claves únicas y factores 2FA activos.\n• Amarillo Advertencia (50% - 84%): Existen duplicados o claves débiles que debes renovar.\n• Rojo Crítico (< 50%): Múltiples servicios comparten contraseñas vulnerables o filtradas.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Los 5 vectores de riesgo auditados") {
            Text(
                text = "1. Contraseñas duplicadas / reutilizadas: El peligro número uno en la red. Si una tienda online secundaria sufre una filtración, los atacantes probarán esa clave en tu correo y redes.\n\n2. Contraseñas débiles: Claves cortas (menos de 10 caracteres) o con patrones alfanuméricos predecibles.\n\n3. Claves comunes o en lista negra: Coincidencia con bases de datos de brechas históricas conocidas.\n\n4. Cuentas sin segundo factor (2FA): Te avisa si cuentas esenciales carecen de token TOTP configurado.\n\n5. Antigüedad prolongada: En Ajustes > Seguridad > Auditoría puedes fijar un umbral (ej. 6 o 12 meses) para que el escudo te recuerde rotar credenciales sensibles periódicamente.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Resolución ágil con un solo toque") {
            Text(
                text = "Desde el panel de Salud no tienes que memorizar qué cuenta cambiar:\n\n• Cada tarjeta de riesgo incluye un botón 'Abrir sitio web' para ir directo al formulario de cambio de clave.\n• Dispone del botón 'Editar en Bóveda' para generar y sustituir la contraseña en un segundo.\n• Si es una cuenta de prueba o intranet local donde no te preocupa el riesgo, puedes pulsar 'Ignorar' para no penalizar la puntuación del escudo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Auditoría 100% local y confidencial") {
            Text(
                text = "Toda la comprobación matemática se realiza en la memoria de tu procesador. Bóveda Local jamás envía listas de tus sitios ni hashes de tus contraseñas a servidores externos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Meta de salud recomendada",
            texto = "Apunta a mantener tu puntuación de salud por encima del 90%. Resolver los duplicados es la acción individual que más puntos otorga y la que mayor protección real brinda a tu vida digital."
        )
    }
}
