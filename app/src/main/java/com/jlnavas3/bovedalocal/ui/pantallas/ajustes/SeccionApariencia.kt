package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
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
    TarjetaAjuste("Apariencia", Icons.Filled.Palette, "Tema, color, nombre, densidad y organización de la lista.") {
        Spacer(Modifier.height(8.dp))
        Text(
            "Tema",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        SelectorAjuste(
            titulo = "Tema",
            icono = Icons.Filled.Palette,
            seleccionado = AlmacenAjustes.OPCIONES_TEMA.first { it.first == ajustes.temaApp }.second,
            opciones = AlmacenAjustes.OPCIONES_TEMA.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.Palette)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarTema(valor) }
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Nombre dentro de la app",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "El icono del cajón de aplicaciones siempre se llama \"Bóveda local\": Android no deja poner ahí un texto libre. Este nombre solo se ve dentro de la app.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        var nombreLocal by remember(ajustes.nombrePersonalizado) { mutableStateOf(ajustes.nombrePersonalizado) }
        CampoBoveda(
            valor = nombreLocal,
            etiqueta = "Nombre (solo dentro de la app)",
            alCambiar = { nombreLocal = it }
        )
        Spacer(Modifier.height(8.dp))
        BotonColorido(
            texto = "Guardar nombre",
            icono = Icons.Filled.Save,
            color = ColorAcento
        ) {
            vm.ajustarNombrePersonalizado(nombreLocal.trim())
            haptica.tic()
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Personalización de colores y tema",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
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

        Spacer(Modifier.height(16.dp))
        Text(
            "Personalización de bordes y formas",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
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

        Spacer(Modifier.height(16.dp))
        Text(
            "Personalización de tipografía y textos",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
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

        Spacer(Modifier.height(14.dp))
        EtiquetaSeccion("Densidad de lista")
        Spacer(Modifier.height(8.dp))
        Text(
            "Predeterminada tiene la altura normal, cómoda la reduce algo y compacta hace las filas mucho más estrechas para ver más entradas a la vez.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        SelectorAjuste(
            titulo = "Densidad de lista",
            icono = Icons.Filled.Tune,
            seleccionado = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.first { it.first == ajustes.densidadLista }.second,
            opciones = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.map { (valor, etiqueta) ->
                OpcionAjuste(valor, etiqueta, Icons.Filled.Tune)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarDensidadLista(valor) }
        )
        Spacer(Modifier.height(16.dp))
        FilaAjuste(
            titulo = "Agrupar cuentas por sitio",
            descripcion = "Combina en un grupo plegable las cuentas que pertenecen al mismo servicio o subdominio.",
            activo = ajustes.agruparPorSitio,
            alCambiar = { haptica.tic(); vm.ajustarAgruparPorSitio(it) }
        )
    }
}
