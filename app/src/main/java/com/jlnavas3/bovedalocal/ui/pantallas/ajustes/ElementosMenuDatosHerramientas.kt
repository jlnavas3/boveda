package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Microcomponente que define los elementos de menú del Hub de Ajustes para:
 * - Organización y formato de listas de cuentas
 * - Herramientas (2FA/TOTP, escáner, widgets, accesos rápidos, passkeys)
 * - Copias de seguridad y diagnóstico del sistema
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

fun crearElementosHerramientasAjustes(
    vm: VaultViewModel,
    alAbrirProveedorPasskeys: () -> Unit
): List<ElementoMenuAjustes> = listOf(
    ElementoMenuAjustes(
        titulo = "Verificación en dos pasos",
        subtitulo = "Parámetros predeterminados de códigos de dos pasos",
        icono = Icons.Filled.Timer,
        colorIcono = Color(0xFF3949AB),
        idEtiqueta = "04-HER-AUT",
        grupo = "Herramientas",
        palabrasClave = "2fa totp autenticador codigos periodo hmac digitos verificacion dos pasos",
        alPulsar = { vm.ir(Pantalla.AjustesAutenticador("04-HER-AUT")) }
    ),
    ElementoMenuAjustes(
        titulo = "Historial de claves",
        subtitulo = "Retención temporal y autodestrucción",
        icono = Icons.Filled.History,
        colorIcono = Color(0xFFFF9800),
        idEtiqueta = "04-HER-HST",
        grupo = "Herramientas",
        palabrasClave = "historial contrasenas generadas retencion autodestruccion claves temporal tiempo",
        alPulsar = { vm.ir(Pantalla.AjustesHistorial("04-HER-HST")) }
    ),
    ElementoMenuAjustes(
        titulo = "Cámara y escáner",
        subtitulo = "Motor óptico CameraX y compatibilidad",
        icono = Icons.Filled.CameraAlt,
        colorIcono = Color(0xFF00897B),
        idEtiqueta = "04-HER-CAM",
        grupo = "Herramientas",
        palabrasClave = "camara escaner qr camerax optico lector",
        alPulsar = { vm.ir(Pantalla.AjustesCamara("04-HER-CAM")) }
    ),
    ElementoMenuAjustes(
        titulo = "Widgets",
        subtitulo = "Generador 1x1 y accesos directos TOTP",
        icono = Icons.Filled.Widgets,
        colorIcono = Color(0xFF7CB342),
        idEtiqueta = "04-HER-WGT",
        grupo = "Herramientas",
        palabrasClave = "widgets escritorio inicio favoritos totp generador 1x1",
        alPulsar = { vm.ir(Pantalla.AjustesWidget("04-HER-WGT")) }
    ),
    ElementoMenuAjustes(
        titulo = "Mosaico rápido",
        subtitulo = "Generación desde barra de notificaciones",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFFFBC02D),
        idEtiqueta = "04-HER-MSK",
        grupo = "Herramientas",
        palabrasClave = "tile mosaico barra estado cortina notificaciones rapido",
        alPulsar = { vm.ir(Pantalla.TileRapido("04-HER-MSK")) }
    ),
    ElementoMenuAjustes(
        titulo = "Llaves de paso",
        subtitulo = "Proveedor de llaves de paso en Android",
        icono = Icons.Filled.Key,
        colorIcono = Color(0xFF8B5CF6),
        idEtiqueta = "04-HER-PSK",
        grupo = "Herramientas",
        palabrasClave = "passkey passkeys proveedor credenciales llaves paso acceso android servicio activar",
        alPulsar = alAbrirProveedorPasskeys
    )
)

fun crearElementosCopiasYSistemaAjustes(
    vm: VaultViewModel
): Pair<List<ElementoMenuAjustes>, List<ElementoMenuAjustes>> {
    val copias = listOf(
        ElementoMenuAjustes(
            titulo = "Copia de seguridad",
            subtitulo = "Exportar, restaurar y copias automáticas",
            icono = Icons.Filled.Backup,
            colorIcono = Color(0xFF43A047),
            idEtiqueta = "05-COP-MAN",
            grupo = "Copias y datos",
            palabrasClave = "copia seguridad backup exportar importar restaurar auto automatica",
            alPulsar = { vm.ir(Pantalla.CopiaSeguridad("05-COP-MAN")) }
        ),
        ElementoMenuAjustes(
            titulo = "Importar de Google",
            subtitulo = "Importar archivo CSV de Google Passwords",
            icono = Icons.Filled.FileDownload,
            colorIcono = Color(0xFF0288D1),
            idEtiqueta = "05-COP-CSV",
            grupo = "Copias y datos",
            palabrasClave = "google passwords csv importar chrome navegador",
            alPulsar = { vm.ir(Pantalla.CsvGoogle("05-COP-CSV")) }
        ),
        ElementoMenuAjustes(
            titulo = "Kit de emergencia",
            subtitulo = "Ficha física imprimible con claves maestras",
            icono = Icons.Filled.Description,
            colorIcono = Color(0xFFFFB300),
            idEtiqueta = "05-COP-KIT",
            grupo = "Copias y datos",
            palabrasClave = "kit emergencia papel pdf imprimir hoja rescate",
            alPulsar = { vm.ir(Pantalla.KitEmergencia("05-COP-KIT")) }
        )
    )

    val sistema = listOf(
        ElementoMenuAjustes(
            titulo = "Opciones avanzadas",
            subtitulo = "Respuesta táctil, desarrollo y peligro",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFC2185B),
            idEtiqueta = "06-SIS-AVZ",
            grupo = "Sistema",
            palabrasClave = "avanzada maestra clave cambiar borrar ids desarrollo haptica vibracion",
            alPulsar = { vm.ir(Pantalla.Avanzada("06-SIS-AVZ")) }
        ),
        ElementoMenuAjustes(
            titulo = "Registro de eventos",
            subtitulo = "Auditoría de acciones, accesos y seguridad",
            icono = Icons.Filled.History,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "06-SIS-LOG",
            grupo = "Sistema",
            palabrasClave = "registro eventos logs historial auditoria fallos accesos",
            alPulsar = { vm.ir(Pantalla.Registro("06-SIS-LOG")) }
        ),
        ElementoMenuAjustes(
            titulo = "Diagnóstico de seguridad",
            subtitulo = "Auditoría de integridad, versión y licencias",
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFF607D8B),
            idEtiqueta = "06-SIS-DGN",
            grupo = "Sistema",
            palabrasClave = "diagnostico seguridad acerca de version info auditoria integridad",
            alPulsar = { vm.ir(Pantalla.AcercaDe("06-SIS-DGN")) }
        )
    )

    return Pair(copias, sistema)
}
