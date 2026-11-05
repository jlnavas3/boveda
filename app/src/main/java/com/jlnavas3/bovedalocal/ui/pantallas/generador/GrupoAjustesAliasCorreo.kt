/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Pin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun GrupoAjustesAliasCorreo(
    ajustes: AjustesApp,
    alCambiarCorreoBase: (String) -> Unit,
    alCambiarLongitudSufijo: (Int) -> Unit,
    alCambiarDelimitador: (String) -> Unit,
    alRestablecer: () -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    var mostrarDialogoCorreoBase by remember { mutableStateOf(false) }

    ComponenteGrupo(
        etiqueta = "Subdireccionamiento y Alias de Correo",
        icono = Icons.Filled.AlternateEmail,
        colorIcono = ColorAcento,
        idGrupo = "04-HER-GEN-G03",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = alRestablecer,
        mensajeToastRestablecer = "Ajustes de alias restablecidos",
        descripcion = "Enmascaramiento de identidad (RFC 5233 / Plus-Addressing) para registros y formularios",
        modifier = modifier
    ) {
        val correoSubtitulo = if (ajustes.aliasCorreoBasePredeterminado.isNotBlank()) {
            ajustes.aliasCorreoBasePredeterminado
        } else {
            "Sin configurar (se solicitará al generar en el formulario)"
        }

        ComponenteFila(
            titulo = "Correo electrónico base",
            subtitulo = correoSubtitulo,
            icono = Icons.Filled.Mail,
            idFila = "04-HER-GEN-EML",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = {
                haptica.tic()
                mostrarDialogoCorreoBase = true
            }
        )

        ComponenteSeparador()

        val opcionesDelimitador = remember {
            listOf(
                OpcionSelectorModal("+", "Signo más (+)", "+", "Estándar universal RFC 5233 (Gmail, Outlook, iCloud)", Icons.Filled.Code),
                OpcionSelectorModal("-", "Guion medio (-)", "-", "Compatible con Fastmail y servidores personalizados", Icons.Filled.Code),
                OpcionSelectorModal(".", "Punto (.)", ".", "Alternativa para sitios que rechazan signos especiales", Icons.Filled.Code)
            )
        }

        ComponenteSelectorModal(
            titulo = "Delimitador de alias",
            descripcionModal = "Carácter separador utilizado entre el usuario principal y la etiqueta o sufijo aleatorio",
            icono = Icons.Filled.Code,
            idFila = "04-HER-GEN-DEL",
            mostrarId = ajustes.mostrarIdsAjustes,
            valorSeleccionado = ajustes.aliasCorreoDelimitador,
            opciones = opcionesDelimitador,
            alSeleccionar = {
                haptica.toque()
                alCambiarDelimitador(it)
            }
        )

        ComponenteSeparador()

        val opcionesLongitud = remember {
            listOf(
                OpcionSelectorModal(4, "4 caracteres", "4", "Compacto y fácil de recordar", Icons.Filled.Pin),
                OpcionSelectorModal(6, "6 caracteres (Recomendado)", "6", "Equilibrio estándar entre privacidad y tamaño", Icons.Filled.Pin),
                OpcionSelectorModal(8, "8 caracteres", "8", "Mayor aleatoriedad contra correlación", Icons.Filled.Pin),
                OpcionSelectorModal(10, "10 caracteres", "10", "Alta entropía para máxima privacidad", Icons.Filled.Pin),
                OpcionSelectorModal(12, "12 caracteres", "12", "Longitud extendida", Icons.Filled.Pin)
            )
        }

        ComponenteSelectorModal(
            titulo = "Longitud de sufijo aleatorio",
            descripcionModal = "Número de caracteres generados al crear un alias aleatorio seguro",
            icono = Icons.Filled.Pin,
            idFila = "04-HER-GEN-LEN",
            mostrarId = ajustes.mostrarIdsAjustes,
            valorSeleccionado = ajustes.aliasCorreoLongitudSufijo,
            opciones = opcionesLongitud,
            alSeleccionar = {
                haptica.toque()
                alCambiarLongitudSufijo(it)
            }
        )
    }

    if (mostrarDialogoCorreoBase) {
        DialogoCorreoBaseAlias(
            correoActual = ajustes.aliasCorreoBasePredeterminado,
            alCerrar = { mostrarDialogoCorreoBase = false },
            alGuardar = { nuevo ->
                alCambiarCorreoBase(nuevo)
                haptica.exito()
            }
        )
    }
}
