package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionApariencia(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica
) {
    TarjetaAjuste(
        titulo = "Apariencia",
        icono = Icons.Filled.Palette,
        descripcion = "Tema, color, nombre, densidad y organización de la lista.",
        colorIcono = ColorAcento,
        inicialmenteAbierta = false
    ) {
        Spacer(Modifier.height(8.dp))

        // 1. Nombre dentro de la app
        TarjetaAjuste(
            titulo = "Nombre dentro de la app",
            icono = Icons.Filled.Badge,
            descripcion = "Nombre personalizado visible únicamente dentro de la aplicación.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            Text(
                "El icono del cajón de aplicaciones siempre se llama \"Bóveda local\": Android no deja poner ahí un texto libre. Este nombre solo se ve dentro de la app.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            var nombreLocal by remember(ajustes.nombrePersonalizado) { mutableStateOf(ajustes.nombrePersonalizado) }
            CampoBoveda(
                valor = nombreLocal,
                etiqueta = "Nombre (solo dentro de la app)",
                alCambiar = { nombreLocal = it }
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Guardar nombre",
                icono = Icons.Filled.Save,
                color = ColorAcento
            ) {
                vm.ajustarNombrePersonalizado(nombreLocal.trim())
                haptica.tic()
            }
        }

        Spacer(Modifier.height(12.dp))

        // 2. Personalización de colores y tema
        TarjetaAjuste(
            titulo = "Colores y Tema",
            icono = Icons.Filled.Palette,
            descripcion = "Modo claro/oscuro y personalización completa de paleta cromática.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            Text(
                "Modo de tema",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))
            SelectorAjuste(
                titulo = "Tema",
                icono = Icons.Filled.Palette,
                seleccionado = AlmacenAjustes.OPCIONES_TEMA.first { it.first == ajustes.temaApp }.second,
                opciones = AlmacenAjustes.OPCIONES_TEMA.map { (valor, etiqueta) ->
                    OpcionAjuste(valor, etiqueta, Icons.Filled.Palette)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTema(valor) }
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Configura independientemente los colores de íconos internos, títulos, tarjetas, acento y el ícono del launcher con selectores interactivos en tiempo real.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar colores",
                color = ColorAcento,
                icono = Icons.Filled.Palette
            ) {
                haptica.tic()
                vm.ir(Pantalla.Tema)
            }
        }

        Spacer(Modifier.height(12.dp))

        // 3. Personalización de bordes y formas
        TarjetaAjuste(
            titulo = "Bordes y Formas",
            icono = Icons.Filled.SquareFoot,
            descripcion = "Curvatura de esquinas, grosores de trazo y espaciados entre componentes.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            Text(
                "Configura en tiempo real curvatura de esquinas, grosores de trazo, estilos perimetrales y espaciados entre componentes con controles deslizantes.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar bordes y formas",
                color = ColorAcento,
                icono = Icons.Filled.SquareFoot
            ) {
                haptica.tic()
                vm.ir(Pantalla.Formas)
            }
        }

        Spacer(Modifier.height(12.dp))

        // 4. Personalización de tipografía y textos
        TarjetaAjuste(
            titulo = "Tipografía y Textos",
            icono = Icons.Filled.TextFields,
            descripcion = "Tamaño de fuentes, peso, interlineado y familias tipográficas.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            Text(
                "Ajusta en tiempo real tamaño de fuentes, peso de texto, inclinación cursiva, kerning, interlineado y familias tipográficas a tu gusto.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar tipografía y textos",
                color = ColorAcento,
                icono = Icons.Filled.TextFields
            ) {
                haptica.tic()
                vm.ir(Pantalla.Tipografia)
            }
        }

        Spacer(Modifier.height(12.dp))

        // 5. Densidad de lista
        TarjetaAjuste(
            titulo = "Densidad de lista",
            icono = Icons.Filled.Tune,
            descripcion = "Altura y compactación de las filas en la pantalla principal.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            Text(
                "Predeterminada tiene la altura normal, cómoda la reduce algo y compacta hace las filas mucho más estrechas para ver más entradas a la vez.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            SelectorAjuste(
                titulo = "Densidad de lista",
                icono = Icons.Filled.Tune,
                seleccionado = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.first { it.first == ajustes.densidadLista }.second,
                opciones = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.map { (valor, etiqueta) ->
                    OpcionAjuste(valor, etiqueta, Icons.Filled.Tune)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarDensidadLista(valor) }
            )
        }

        Spacer(Modifier.height(12.dp))

        // 6. Agrupar cuentas por sitio
        TarjetaAjuste(
            titulo = "Agrupar cuentas por sitio",
            icono = Icons.Filled.Layers,
            descripcion = "Organización y agrupamiento automático de cuentas del mismo dominio.",
            inicialmenteAbierta = false,
            colorIcono = ColorAcento
        ) {
            FilaAjuste(
                titulo = "Agrupar cuentas por sitio",
                descripcion = "Combina en un grupo plegable las cuentas que pertenecen al mismo servicio o subdominio.",
                activo = ajustes.agruparPorSitio,
                alCambiar = { haptica.tic(); vm.ajustarAgruparPorSitio(it) }
            )
        }
    }
}
