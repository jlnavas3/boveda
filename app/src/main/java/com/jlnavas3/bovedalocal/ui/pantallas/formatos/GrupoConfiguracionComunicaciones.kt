/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal

/**
 * Grupo de configuración desacoplado para llamadas confidenciales, WhatsApp directo sin agenda,
 * mensajería efímera y opciones de purga de contactos de Android.
 */
@Composable
fun GrupoConfiguracionComunicaciones(
    mostrarIdsAjustes: Boolean,
    prefijoNumeroOculto: String,
    habilitarLlamadaOculta: Boolean,
    habilitarWhatsappDirecto: Boolean,
    modoEnvioMensajeSeguro: String,
    preguntarPurgaAgenda: Boolean,
    alCambiarPrefijoNumeroOculto: (String) -> Unit,
    alCambiarHabilitarLlamadaOculta: (Boolean) -> Unit,
    alCambiarHabilitarWhatsappDirecto: (Boolean) -> Unit,
    alCambiarModoEnvioMensajeSeguro: (String) -> Unit,
    alCambiarPreguntarPurgaAgenda: (Boolean) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Comunicaciones seguras y privacidad",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFF3F51B5),
        alRestablecer = alRestablecerGrupo,
        idGrupo = "02-APA-FMT-G04",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        // 1. Habilitar opción de llamada con número oculto
        ComponenteSwitch(
            titulo = "Llamada con número oculto",
            subtitulo = "Ofrece marcar con el prefijo de privacidad de operadora (#31#, *67...)",
            activo = habilitarLlamadaOculta,
            alCambiar = alCambiarHabilitarLlamadaOculta,
            icono = Icons.Filled.Call,
            idFila = "02-APA-FMT-LLOC",
            mostrarId = mostrarIdsAjustes
        )

        if (habilitarLlamadaOculta) {
            ComponenteSeparador(sangriaInicio = 16.dp)

            val opcionesPrefijo = remember {
                listOf(
                    OpcionSelectorModal(
                        valor = "#31#",
                        etiquetaFila = "#31# (Estándar GSM)",
                        etiquetaModal = "#31# — Estándar GSM mundial",
                        descripcionModal = "Utilizado en Europa, Latinoamérica y la mayoría de redes GSM",
                        icono = Icons.Filled.Lock
                    ),
                    OpcionSelectorModal(
                        valor = "*67",
                        etiquetaFila = "*67 (EE.UU. / Canadá)",
                        etiquetaModal = "*67 — Norteamérica NANP",
                        descripcionModal = "Prefijo para ocultar ID de llamada en Estados Unidos y Canadá",
                        icono = Icons.Filled.Lock
                    ),
                    OpcionSelectorModal(
                        valor = "*31#",
                        etiquetaFila = "*31# (Argentina / Otros)",
                        etiquetaModal = "*31# — Argentina y operadores específicos",
                        descripcionModal = "Formato alternativo para ciertas operadoras de telefonía",
                        icono = Icons.Filled.Lock
                    ),
                    OpcionSelectorModal(
                        valor = "141",
                        etiquetaFila = "141 (Reino Unido)",
                        etiquetaModal = "141 — Reino Unido",
                        descripcionModal = "Prefijo estándar en redes del Reino Unido",
                        icono = Icons.Filled.Lock
                    )
                )
            }

            ComponenteSelectorModal(
                titulo = "Prefijo de número oculto",
                descripcionModal = "Código que se antepone al número para ocultar tu identificador de llamada",
                icono = null,
                idFila = "02-APA-FMT-PREF",
                mostrarId = mostrarIdsAjustes,
                valorSeleccionado = prefijoNumeroOculto,
                opciones = opcionesPrefijo,
                alSeleccionar = alCambiarPrefijoNumeroOculto
            )
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        // 2. WhatsApp Directo
        ComponenteSwitch(
            titulo = "WhatsApp directo confidencial",
            subtitulo = "Abre chats sin necesidad de añadir el contacto a la agenda pública de Android",
            activo = habilitarWhatsappDirecto,
            alCambiar = alCambiarHabilitarWhatsappDirecto,
            icono = Icons.AutoMirrored.Filled.Chat,
            idFila = "02-APA-FMT-WAPP",
            mostrarId = mostrarIdsAjustes
        )

        if (habilitarWhatsappDirecto) {
            ComponenteSeparador(sangriaInicio = 16.dp)

            val opcionesModo = remember {
                listOf(
                    OpcionSelectorModal(
                        valor = "preguntar",
                        etiquetaFila = "Preguntar siempre",
                        etiquetaModal = "Preguntar modo de envío",
                        descripcionModal = "Muestra un diálogo para elegir entre texto directo o imagen efímera",
                        icono = Icons.Filled.Security
                    ),
                    OpcionSelectorModal(
                        valor = "texto",
                        etiquetaFila = "Texto directo",
                        etiquetaModal = "Abrir chat directamente",
                        descripcionModal = "Salta directo a la conversación de WhatsApp",
                        icono = Icons.AutoMirrored.Filled.Chat
                    ),
                    OpcionSelectorModal(
                        valor = "imagen",
                        etiquetaFila = "Imagen efímera",
                        etiquetaModal = "Compositor de imagen efímera (Vista única)",
                        descripcionModal = "Genera un Bitmap seguro con marca de tiempo sobre fondo oscuro",
                        icono = Icons.Filled.Lock
                    )
                )
            }

            ComponenteSelectorModal(
                titulo = "Modo de mensaje confidencial",
                descripcionModal = "Formato por defecto al iniciar una comunicación por WhatsApp",
                icono = null,
                idFila = "02-APA-FMT-MODM",
                mostrarId = mostrarIdsAjustes,
                valorSeleccionado = modoEnvioMensajeSeguro,
                opciones = opcionesModo,
                alSeleccionar = alCambiarModoEnvioMensajeSeguro
            )
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        // 3. Purga opcional tras importación
        ComponenteSwitch(
            titulo = "Sugerir purga tras importar",
            subtitulo = "Pregunta si deseas abrir la ficha del contacto en la agenda del sistema para eliminarlo de los contactos públicos de Android",
            activo = preguntarPurgaAgenda,
            alCambiar = alCambiarPreguntarPurgaAgenda,
            icono = Icons.Filled.DeleteSweep,
            idFila = "02-APA-FMT-PURG",
            mostrarId = mostrarIdsAjustes
        )
    }
}
