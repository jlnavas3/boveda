/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
 * Artículo de ayuda exhaustivo sobre la papelera de reciclaje, borrado suave y restauración sin conflictos.
 */
@Composable
fun PantallaAyudaPapelera(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Papelera de reciclaje y retención",
        alVolver = alVolver,
        textoAccion = "Abrir Papelera",
        iconoAccion = Icons.Filled.Delete,
        alEjecutarAccion = { vm.ir(Pantalla.Papelera) }
    ) {
        TarjetaInformativaAyuda(titulo = "Borrado protegido contra accidentes") {
            Text(
                text = "Borrar una contraseña por error puede ser catastrófico si no tienes un respaldo a mano. Bóveda Local implementa una política de 'borrado suave' (Soft-Delete): cuando eliminas una credencial, tarjeta o nota, se mueve a la Papelera de Reciclaje manteniendo intacta su información, historial y adjuntos.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Opciones y gestión en la papelera") {
            Text(
                text = "Desde el menú lateral > Papelera puedes:\n\n• Restaurar cuentas: Recupera cualquier entrada devuelta a su estado activo original con un solo toque.\n\n• Resolución de conflictos al restaurar: Si creaste una nueva cuenta con idéntico nombre mientras la anterior estaba en la papelera, el diálogo de restauración te alertará para que elijas si deseas renombrarla, conservarla como copia o sobrescribirla.\n\n• Borrado definitivo individual: Elimina quirúrgicamente un registro sensible que no desees conservar en la papelera.\n\n• Vaciar papelera completa: Purga definitiva de todos los elementos archivados en un solo paso con confirmación de seguridad.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Cifrado permanente en la papelera") {
            Text(
                text = "Los elementos en la papelera no quedan en texto plano ni se almacenan en carpetas temporales desprotegidas: continúan cifrados con AES-256 dentro de la base de datos SQLCipher. Nadie puede acceder a tus claves borradas sin tu contraseña maestra.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaAdvertenciaAyuda(
            titulo = "El vaciado de papelera es irreversible",
            texto = "Al pulsar 'Vaciar papelera', los bloques de datos se truncan y borran físicamente de la base de datos. Solo un respaldo externo .bvda previo podrá recuperar las cuentas eliminadas tras vaciar la papelera."
        )

        TarjetaConsejoAyuda(
            titulo = "Revisa la papelera antes de vaciar",
            texto = "Echa un vistazo rápido a los nombres antes de pulsar 'Vaciar'. A veces un borrado rápido semanas atrás contenía apuntes o notas de recuperación que no recordabas."
        )
    }
}
