package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DynamicForm
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
        alPulsar = { vm.ir(Pantalla.OrganizacionLista()) }
    ),
    ElementoMenuAjustes(
        titulo = "Identidades",
        subtitulo = "Perfiles de correo y vinculación inteligente",
        icono = Icons.Filled.AccountCircle,
        colorIcono = Color(0xFF0284C7),
        idEtiqueta = "03-LST-IDE",
        grupo = "Lista de cuentas",
        palabrasClave = "identidades cuentas perfiles correo email alias inteligente vincular personas",
        alPulsar = { vm.ir(Pantalla.Identidades()) }
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
        alPulsar = { vm.ir(Pantalla.AjustesIndice()) }
    ),
    ElementoMenuAjustes(
        titulo = "Formatos",
        subtitulo = "Campos personalizados predeterminados",
        icono = Icons.AutoMirrored.Filled.FormatListBulleted,
        colorIcono = Color(0xFFFFA000),
        idEtiqueta = "03-LST-FMT",
        grupo = "Lista de cuentas",
        palabrasClave = "formatos campos plantillas autofill rellenar formulario",
        alPulsar = { vm.ir(Pantalla.FormatosCampos()) }
    ),
    ElementoMenuAjustes(
        titulo = "Plantillas de campos",
        subtitulo = "Plantillas personalizadas y del sistema",
        icono = Icons.Filled.DynamicForm,
        colorIcono = Color(0xFF10B981),
        idEtiqueta = "03-LST-PLT",
        grupo = "Lista de cuentas",
        palabrasClave = "plantillas campos presets personalizados formulas formularios modelos estructuras",
        valorTexto = "${ajustes.plantillasCamposPersonalizadas.size} pers.",
        alPulsar = { vm.ir(Pantalla.PlantillasCampos()) }
    )
)
