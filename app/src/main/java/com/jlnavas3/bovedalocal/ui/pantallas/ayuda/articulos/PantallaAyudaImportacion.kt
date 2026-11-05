/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaAdvertenciaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaPasoAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre importación desde Google Passwords, delimitadores CSV y prevención de duplicados.
 */
@Composable
fun PantallaAyudaImportacion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Importación de contraseñas y CSV",
        alVolver = alVolver,
        textoAccion = "Importar de Google / CSV",
        iconoAccion = Icons.Filled.FileDownload,
        alEjecutarAccion = { vm.ir(Pantalla.CsvGoogle()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Migra toda tu vida digital en segundos") {
            Text(
                text = "Traer tus contraseñas acumuladas durante años en Google Chrome, Mozilla Firefox, Microsoft Edge, Bitwarden o KeePass a Bóveda Local es un proceso rápido, guiado y 100% offline sin que tus claves pasen por ningún servidor intermedio.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Soporte de delimitadores dinámicos (05-COP-CSV)") {
            Text(
                text = "Los archivos CSV generados por distintos sistemas operativos o versiones de Excel utilizan separadores de columna muy variados. Bóveda Local cuenta con detección automática y selector manual para:\n\n• Coma (,): Estándar por defecto en navegadores en inglés y Google Passwords.\n• Punto y coma (;): Habitual en exportaciones de Excel en español y sistemas europeos.\n• Tabulación (\\t): Archivos TSV de exportaciones técnicas.\n• Pleca / Barra vertical (|): Formato delimitado por pipes para evitar conflictos con comas dentro de notas o contraseñas complejas.\n\nEl importador analiza las primeras líneas y selecciona automáticamente el delimitador correcto, permitiéndote cambiarlo manualmente si fuera necesario.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Detección inteligente de credenciales vs contactos") {
            Text(
                text = "El motor de análisis semántico clasifica cada fila del archivo:\n\n• Si detecta campos de URL, usuario y contraseña, la importa como cuenta de acceso (Login).\n• Si identifica nombres personales, números de teléfono o direcciones postales sin URL, te ofrece clasificarla automáticamente como 'Contacto / Dirección'.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Pasos para migrar desde Google Passwords") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TarjetaPasoAyuda(
                    numero = 1,
                    titulo = "Descarga tu archivo CSV",
                    descripcion = "En Chrome (en tu PC o móvil), abre passwords.google.com > Ajustes (⚙️) > 'Exportar contraseñas' y descarga el archivo .csv."
                )
                TarjetaPasoAyuda(
                    numero = 2,
                    titulo = "Abre el Importador en Bóveda Local",
                    descripcion = "Ve a Ajustes > Copias y datos > 'Importar de Google' y pulsa 'Seleccionar archivo CSV'."
                )
                TarjetaPasoAyuda(
                    numero = 3,
                    titulo = "Inspecciona y confirma",
                    descripcion = "Revisa el número de cuentas detectadas, la ausencia de duplicados y pulsa 'Importar a la bóveda'."
                )
            }
        }

        TarjetaAdvertenciaAyuda(
            titulo = "Elimina de inmediato el archivo CSV descargado",
            texto = "Los archivos CSV exportados por los navegadores están en TEXTO PLANO sin ningún tipo de cifrado. Cualquier app instalada en tu teléfono con permiso de almacenamiento podría leer tus contraseñas si lo dejas en la carpeta 'Descargas'. Bórralo y vacía la papelera de descargas de inmediato tras importar."
        )

        TarjetaConsejoAyuda(
            titulo = "Ejecuta el Normalizador de Títulos tras importar",
            texto = "Las exportaciones de Chrome suelen contener títulos desordenados con URLs kilométricas. Tras importar, ve a Ajustes > Lista de cuentas > Normalizador de títulos para limpiar todos los nombres en un solo clic."
        )
    }
}
