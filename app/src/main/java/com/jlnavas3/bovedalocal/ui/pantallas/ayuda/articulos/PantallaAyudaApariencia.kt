/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
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
 * Artículo de ayuda exhaustivo sobre el Laboratorio de Temas, paletas cromáticas,
 * geometría de bordes, tipografías y animaciones de interfaz.
 */
@Composable
fun PantallaAyudaApariencia(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Temas y personalización",
        alVolver = alVolver,
        textoAccion = "Abrir Laboratorio de temas",
        iconoAccion = Icons.Filled.Palette,
        alEjecutarAccion = { vm.ir(Pantalla.LaboratorioTemas()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Control estético y ergonómico total") {
            Text(
                text = "Bóveda Local está construida sobre un motor de diseño adaptable que te permite calibrar cada elemento visual: desde el contraste de fondos en pantallas OLED hasta la curvatura exacta de esquinas y la cadencia de las animaciones.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El Laboratorio de temas (02-APA-THM-LAB)") {
            Text(
                text = "El Laboratorio es un entorno interactivo en tiempo real con panel flotante de previsualización que reúne:\n\n• Modo de color: Selecciona entre Automático (sigue el modo oscuro/claro del sistema Android), Oscuro permanente o Claro permanente.\n\n• Paletas sobrias y acentos: Diseñadas para no distraer. Puedes elegir paletas Obsidian pura, Carbón, Azul medianoche, Bosque o tonos claros neutros, combinándolos con tu color de acento favorito (Azul, Menta, Esmeralda, Violeta, Ámbar, etc.).\n\n• Escala de capas cromáticas (L1 a L4): Calibra el contraste entre el fondo de la pantalla (L1), las superficies de las tarjetas (L2), los campos de texto internos (L3) y los elementos interactivos elevados (L4).\n\n• Exportación e importación JSON: Guarda tus creaciones cromáticas en archivos JSON para compartirlas o restaurarlas en otros dispositivos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Geometría, bordes y tipografía") {
            Text(
                text = "• Curvatura de esquinas: Regulable en un deslizador continuo desde 0.dp (estilo industrial retro con esquinas vivas a 90°) hasta 32.dp (diseño orgánico ultra-redondeado estilo Google Material 3 / iOS).\n\n• Grosor y estilo de borde: Define contornos de tarjeta sutiles (0.5.dp a 2.dp) o sin bordes para un diseño plano minimalista.\n\n• Tipografía y tamaño de texto: Selecciona familias de fuentes del sistema o monoespaciadas y ajusta el escalado tipográfico global para garantizar legibilidad perfecta a cualquier distancia.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Animación de desbloqueo y destellos (Glow)") {
            Text(
                text = "• Animación de apertura: Elige entre desvanecimiento suave, escala elástica, cortina o revelación gradual al verificar tu contraseña o huella.\n\n• Calibración milimétrica: Regula la duración exacta en milisegundos y la curva de aceleración física de la transición.\n\n• Efecto de alumbrado y glow: Al saltar a una sección desde el buscador o el menú de 3 puntos, la tarjeta de destino emite un suave destello pulsante de color acento para ubicar tu vista al instante.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Máximo ahorro de batería en pantallas OLED",
            texto = "Si tu dispositivo cuenta con pantalla AMOLED u OLED, utiliza la paleta 'Obsidiana' (negro #000000 absoluto). Los píxeles negros apagan completamente los diodos emisores, reduciendo drásticamente el consumo de energía."
        )
    }
}
