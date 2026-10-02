package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

data class ElementoMenuAjustes(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val idEtiqueta: String,
    val grupo: String,
    val palabrasClave: String = "",
    val valorTexto: String? = null,
    val alPulsar: () -> Unit
)

fun crearCatalogoAjustesHub(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alAbrirNombreBoveda: () -> Unit,
    alAbrirProveedorPasskeys: () -> Unit,
    alAbrirCambioMaestra: () -> Unit
): List<ElementoMenuAjustes> {
    return listOf(
        // 🔒 Grupo 1: Seguridad
        ElementoMenuAjustes(
            titulo = "Biometría",
            subtitulo = "Huella dactilar, bloqueo de app y portapapeles",
            icono = Icons.Filled.Fingerprint,
            colorIcono = Color(0xFF1E88E5),
            idEtiqueta = "01-SEG-BIO",
            grupo = "Seguridad",
            palabrasClave = "huella biometria pin contrasena bloqueo inactividad flag secure pantalla portapapeles",
            alPulsar = { vm.ir(Pantalla.Seguridad("01-SEG-BIO")) }
        ),
        ElementoMenuAjustes(
            titulo = "Clave maestra",
            subtitulo = "Cambiar la contraseña principal de la bóveda",
            icono = Icons.Filled.Lock,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "01-SEG-PAS",
            grupo = "Seguridad",
            palabrasClave = "clave contrasena maestra cambiar pass principal",
            alPulsar = alAbrirCambioMaestra
        ),
        ElementoMenuAjustes(
            titulo = "Modo señuelo",
            subtitulo = "PIN de coacción y cuentas simuladas",
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFFFB8C00),
            idEtiqueta = "01-SEG-SEN",
            grupo = "Seguridad",
            palabrasClave = "senuelo coaccion pin falso fake simulado cuentas",
            alPulsar = { vm.ir(Pantalla.AjustesSenuelo("01-SEG-SEN")) }
        ),
        ElementoMenuAjustes(
            titulo = "Autodestrucción",
            subtitulo = "Borrado irreversible por PIN de emergencia",
            icono = Icons.Filled.DeleteForever,
            colorIcono = Color(0xFFE53935),
            idEtiqueta = "01-SEG-DES",
            grupo = "Seguridad",
            palabrasClave = "autodestruccion borrar destruir emergencia peligro pin panico",
            alPulsar = { vm.ir(Pantalla.AjustesAutodestruccion("01-SEG-DES")) }
        ),
        ElementoMenuAjustes(
            titulo = "Cifrado",
            subtitulo = "Parámetros Argon2id de resistencia KDF",
            icono = Icons.Filled.Memory,
            colorIcono = Color(0xFF5C6BC0),
            idEtiqueta = "01-SEG-CRY",
            grupo = "Seguridad",
            palabrasClave = "argon2id cifrado algoritmo hash ram memoria hilos kdf",
            alPulsar = { vm.ir(Pantalla.Argon2id("01-SEG-CRY")) }
        ),

        // 🎨 Grupo 2: Apariencia
        ElementoMenuAjustes(
            titulo = "Tema y colores",
            subtitulo = "Modo oscuro, paleta y acento dinámico",
            icono = Icons.Filled.Palette,
            colorIcono = Color(0xFF8E24AA),
            idEtiqueta = "02-APA-THM",
            grupo = "Apariencia",
            palabrasClave = "tema colores paleta acento apariencia aspecto oscuro claro sistema",
            valorTexto = when (ajustes.temaApp) {
                "claro" -> "Claro"
                "oscuro" -> "Oscuro"
                else -> "Sistema"
            },
            alPulsar = { vm.ir(Pantalla.Tema("02-APA-THM")) }
        ),
        ElementoMenuAjustes(
            titulo = "Laboratorio de temas y paleta",
            subtitulo = "Escala neutra de grises, luminancia y exportar",
            icono = Icons.Filled.Palette,
            colorIcono = Color(0xFF673AB7),
            idEtiqueta = "02-APA-LAB",
            grupo = "Apariencia",
            palabrasClave = "laboratorio temas paleta grises luminancia sobrio exportar copiar color",
            alPulsar = { vm.ir(Pantalla.LaboratorioTemas()) }
        ),
        ElementoMenuAjustes(
            titulo = "Formas y bordes",
            subtitulo = "Curvatura de esquinas y estilo de bordes",
            icono = Icons.Filled.SquareFoot,
            colorIcono = Color(0xFFE91E63),
            idEtiqueta = "02-APA-GEO",
            grupo = "Apariencia",
            palabrasClave = "formas bordes curvatura esquinas grosor estilo presets tarjetas",
            alPulsar = { vm.ir(Pantalla.Formas("02-APA-GEO")) }
        ),
        ElementoMenuAjustes(
            titulo = "Tipografía",
            subtitulo = "Escala de fuentes, peso y espaciado",
            icono = Icons.Filled.TextFields,
            colorIcono = Color(0xFF26A69A),
            idEtiqueta = "02-APA-TYP",
            grupo = "Apariencia",
            palabrasClave = "fuente tipografia letras texto tamano escala peso espaciado interlineado",
            alPulsar = { vm.ir(Pantalla.Tipografia("02-APA-TYP")) }
        ),
        ElementoMenuAjustes(
            titulo = "Nombre de la app",
            subtitulo = "Personalizar nombre visible en cabecera",
            icono = Icons.Filled.Badge,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "02-APA-LNC",
            grupo = "Apariencia",
            palabrasClave = "nombre personalizada titulo encabezado boveda local",
            valorTexto = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
            alPulsar = alAbrirNombreBoveda
        ),

        // 📋 Grupo 3: Lista de cuentas
        ElementoMenuAjustes(
            titulo = "Diseño de lista",
            subtitulo = "Agrupamiento por sitio y densidad",
            icono = Icons.Filled.Layers,
            colorIcono = Color(0xFF00ACC1),
            idEtiqueta = "03-LST-DES",
            grupo = "Lista de cuentas",
            palabrasClave = "agrupar agrupamiento lista densidad compacta comoda cuentas sitio dominio carpetas orden",
            valorTexto = if (ajustes.agruparPorSitio) "Agrupada" else "Individual",
            alPulsar = { vm.ir(Pantalla.OrganizacionLista("03-LST-DES")) }
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
            titulo = "Plantillas de campos",
            subtitulo = "Campos personalizados predeterminados",
            icono = Icons.AutoMirrored.Filled.FormatListBulleted,
            colorIcono = Color(0xFFFFA000),
            idEtiqueta = "03-LST-FMT",
            grupo = "Lista de cuentas",
            palabrasClave = "formatos campos plantillas autofill rellenar formulario",
            alPulsar = { vm.ir(Pantalla.FormatosCampos("03-LST-FMT")) }
        ),

        // ⚡ Grupo 4: Herramientas
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
        ),

        // 💾 Grupo 5: Copias y datos
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
            icono = Icons.Filled.FileUpload,
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
        ),

        // ⚙️ Grupo 6: Sistema
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
}
