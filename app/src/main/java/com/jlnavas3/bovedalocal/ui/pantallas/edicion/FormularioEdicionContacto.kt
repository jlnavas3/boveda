/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosContacto
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonRellenarDesdeAgenda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.util.FormateadorCampos

/**
 * Microcomponente desacoplado para edición directa de datos estructurados de contacto,
 * incluyendo dirección postal, código postal, localidad y país para envíos y facturación,
 * con opción para importar y autorrellenar datos desde la agenda de contactos de Android.
 */
@Composable
fun FormularioEdicionContacto(
    datos: DatosContacto,
    alCambiar: (DatosContacto) -> Unit,
    ajustes: AjustesApp = AjustesApp(),
    alImportarContacto: ((ContactoDispositivo) -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (alImportarContacto != null) {
            BotonRellenarDesdeAgenda(
                alContactoSeleccionado = alImportarContacto
            )
            Spacer(Modifier.height(2.dp))
        }

        CampoBoveda(
            valor = datos.telefonoSecundario,
            etiqueta = "Teléfono secundario (opcional)",
            alCambiar = { alCambiar(datos.copy(telefonoSecundario = it)) },
            formateadorMascara = { raw ->
                FormateadorCampos.aplicarMascaraTelefono(raw, ajustes.formatoTelefono)
            },
            keyboardType = KeyboardType.Phone,
            colorBordeIzquierdo = ColorDatosUsuario
        )

        CampoBoveda(
            valor = datos.correo,
            etiqueta = "Correo electrónico (opcional)",
            alCambiar = { alCambiar(datos.copy(correo = it)) },
            keyboardType = KeyboardType.Email,
            colorBordeIzquierdo = ColorDatosWeb
        )

        CampoBoveda(
            valor = datos.direccion,
            etiqueta = "Dirección física (calle, número, depto)",
            alCambiar = { alCambiar(datos.copy(direccion = it)) },
            colorBordeIzquierdo = ColorAcento
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = datos.codigoPostal,
                etiqueta = "Código postal (ZIP)",
                alCambiar = { alCambiar(datos.copy(codigoPostal = it)) },
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorAcento
            )

            CampoBoveda(
                valor = datos.ciudad,
                etiqueta = "Ciudad / Localidad",
                alCambiar = { alCambiar(datos.copy(ciudad = it)) },
                modifier = Modifier.weight(1.3f),
                colorBordeIzquierdo = ColorDatosWeb
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = datos.provincia,
                etiqueta = "Provincia / Estado",
                alCambiar = { alCambiar(datos.copy(provincia = it)) },
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorDatosWeb
            )

            CampoBoveda(
                valor = datos.pais,
                etiqueta = "País",
                alCambiar = { alCambiar(datos.copy(pais = it)) },
                modifier = Modifier.weight(1f),
                colorBordeIzquierdo = ColorDatosWeb
            )
        }
    }
}
