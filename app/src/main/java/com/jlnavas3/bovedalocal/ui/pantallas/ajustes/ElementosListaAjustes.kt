package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Layers
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Define los elementos de menú del Hub de Ajustes para la lista de cuentas.
 */
fun crearElementosListaAjustes(
    ajustes: AjustesApp,
    vm: VaultViewModel
): List<ElementoMenuAjustes> = listOf(
    ElementoMenuAjustes(
        titulo = "Diseño de lista",
        subtitulo = "Agrupamiento por sitio y densidad",
        icono = Icons.Filled.Layers,
        colorIcono = Color(0xFF00ACC1),
        idEtiqueta = "03-LST-DES",
        grupo = "Lista de cuentas",
        palabrasClave = "agrupar agrupamiento lista densidad compacta comoda cuentas sitio dominio carpetas orden",
        valorTexto = if (ajustes.agruparPorSitio) "Agrupada" else "Individual",
        alPulsar = { vm.ir(Pantalla.OrganizacionLista(null)) }
    ),
    ElementoMenuAjustes(
        titulo = "Índice A-Z",
        subtitulo = "Desplazamiento rápido alfabético lateral",
        icono = Icons.AutoMirrored.Filled.Sort,
        colorIcono = Color(0xFF6A1B9A),
        idEtiqueta = "03-LST-AZX",
        grupo = "Lista de cuentas",
        palabrasClave = "abecedario indice lateral ola niagara alfabeto scroll letras a-z",
        valorTexto = if (ajustes.mostrarIndiceAlfabetico) "Activo" else "Oculto",
        alPulsar = { vm.ir(Pantalla.AjustesIndice("03-LST-AZX")) }
    ),
    ElementoMenuAjustes(
        titulo = "Formatos",
        subtitulo = "Campos personalizados predeterminados",
        icono = Icons.AutoMirrored.Filled.FormatListBulleted,
        colorIcono = Color(0xFFFFA000),
        idEtiqueta = "03-LST-FMT",
        grupo = "Lista de cuentas",
        palabrasClave = "formatos campos plantillas autofill rellenar formulario",
        alPulsar = { vm.ir(Pantalla.FormatosCampos("03-LST-FMT")) }
    )
)
