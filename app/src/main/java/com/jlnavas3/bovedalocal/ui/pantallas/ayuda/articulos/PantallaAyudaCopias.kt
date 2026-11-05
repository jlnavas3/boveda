/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaAdvertenciaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre copias de seguridad cifradas .bvda, compresión BLZ1 y modo Dry-Run.
 */
@Composable
fun PantallaAyudaCopias(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Copias de seguridad (.bvda)",
        alVolver = alVolver,
        textoAccion = "Gestionar Copias",
        iconoAccion = Icons.Filled.Backup,
        alEjecutarAccion = { vm.ir(Pantalla.CopiaSeguridad()) }
    ) {
        TarjetaInformativaAyuda(titulo = "El formato de respaldo oficial .bvda") {
            Text(
                text = "Una copia de seguridad de Bóveda Local (.bvda) es un contenedor criptográfico autónomo que empaqueta la totalidad de tus credenciales, llaves Passkeys, archivos adjuntos cifrados, perfiles, categorías, historial de cambios y preferencias de la aplicación.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Criptografía militar y compresión BLZ1") {
            Text(
                text = "• Derivación con Argon2id: Ganador de la Password Hashing Competition. Requiere una cantidad intensiva de memoria RAM y cómputo matemático, haciendo inviables los ataques de diccionario con hardware masivo (tarjetas gráficas o mineros ASIC).\n\n• Cifrado AES-256-GCM: Cifrado autenticado por bloques. Cualquier modificación externa accidental o maliciosa invalida el hash de autenticación (Tag GCM), rechazando el archivo antes de intentar descifrarlo.\n\n• Compresión BLZ1 integrada: Reduce drásticamente el peso del archivo resultante empaquetando eficientemente fotos de documentos o certificados adjuntos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modo Dry-Run (Inspección previa sin riesgo)") {
            Text(
                text = "Al seleccionar un archivo .bvda para restaurar, Bóveda Local no sobreescribe tus datos a ciegas. Se activa el modo Dry-Run:\n\n• Desencripta en memoria aislada y analiza el archivo.\n• Te muestra un desglose exacto: número de cuentas, tarjetas, tipos nativos, tamaño de adjuntos y fecha original del respaldo.\n• Te permite comparar con tu base de datos actual antes de confirmar la restauración definitiva.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Copias automáticas y recordatorios periódicos") {
            Text(
                text = "En Ajustes > Copias y datos puedes:\n\n• Programar copias automáticas locales: La aplicación genera respaldos periódicos en la memoria de tu móvil (vía Storage Access Framework de Android).\n\n• Recordatorios de respaldo: Si transcurren más de 15 o 30 días sin generar una copia manual, la aplicación te mostrará un aviso no invasivo para mantener tus datos a salvo.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaAdvertenciaAyuda(
            titulo = "Clave de cifrado del respaldo",
            texto = "Puedes cifrar la copia con tu contraseña maestra habitual o con una contraseña específica para ese archivo. Si olvidas dicha contraseña, es matemáticamente imposible recuperar los datos del archivo .bvda."
        )

        TarjetaConsejoAyuda(
            titulo = "Regla de respaldo 3-2-1",
            texto = "Guarda al menos tres copias de tu bóveda en dos medios diferentes (por ejemplo, en la memoria del teléfono y en un pendrive USB cifrado offline) y al menos una fuera de casa para máxima resiliencia."
        )
    }
}
