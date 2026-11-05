/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.data.contactos.LectorArchivoContactos
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Microbotón desacoplado para autorrellenar un contacto seguro a partir de un archivo (.vcf / .csv)
 * mediante el selector del sistema SAF (Storage Access Framework) con Cero Permisos.
 */
@Composable
fun BotonRellenarDesdeAgenda(
    alContactoSeleccionado: (ContactoDispositivo) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var contactosDisponibles by remember { mutableStateOf<List<ContactoDispositivo>>(emptyList()) }
    var mostrandoModalSeleccion by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        BovedaApp.salidaTerminada(context)
        if (uri != null) {
            scope.launch {
                try {
                    val texto = withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.use {
                            it.bufferedReader().readText()
                        }
                    }
                    if (texto.isNullOrBlank()) {
                        Toast.makeText(context, "El archivo seleccionado está vacío", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val contactos = withContext(Dispatchers.Default) {
                        LectorArchivoContactos.parsear(texto)
                    }

                    if (contactos.isEmpty()) {
                        Toast.makeText(context, "No se encontraron contactos en formato vCard (.vcf) o CSV", Toast.LENGTH_LONG).show()
                    } else if (contactos.size == 1) {
                        alContactoSeleccionado(contactos.first())
                    } else {
                        contactosDisponibles = contactos
                        mostrandoModalSeleccion = true
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al leer archivo: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val modBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, ColorBordeActual, forma)
    } else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .then(modBorde)
            .background(ColorTarjetaAjustes)
            .clickable {
                BovedaApp.salidaPendiente(context)
                try {
                    launcher.launch(arrayOf(
                        "text/vcard",
                        "text/x-vcard",
                        "text/csv",
                        "text/comma-separated-values",
                        "text/plain",
                        "*/*"
                    ))
                } catch (_: Exception) {
                    BovedaApp.salidaTerminada(context)
                    Toast.makeText(context, "No se encontró ningún selector de archivos disponible", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ContactPhone,
            contentDescription = null,
            tint = ColorAcento,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Rellenar desde archivo de contactos (.vcf / .csv)",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TextoPrincipal
        )
    }

    if (mostrandoModalSeleccion) {
        ModalSeleccionContactoUnico(
            abierto = true,
            contactos = contactosDisponibles,
            alSeleccionar = { contacto ->
                alContactoSeleccionado(contacto)
                mostrandoModalSeleccion = false
                contactosDisponibles = emptyList()
            },
            alCerrar = {
                mostrandoModalSeleccion = false
                contactosDisponibles = emptyList()
            }
        )
    }
}
