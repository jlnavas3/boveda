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
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
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
    alAbrirProveedorPasskeys: () -> Unit
): List<ElementoMenuAjustes> {
    return listOf(
        // Grupo: Seguridad
        ElementoMenuAjustes(
            titulo = "Datos biométricos y contraseña",
            subtitulo = "Huella dactilar, bloqueo automático y protección de pantalla",
            icono = Icons.Filled.Fingerprint,
            colorIcono = Color(0xFF1E88E5),
            idEtiqueta = "01.1",
            grupo = "Seguridad",
            palabrasClave = "huella biometria pin contrasena bloqueo inactividad flag secure pantalla",
            alPulsar = { vm.ir(Pantalla.Seguridad("01.1")) }
        ),
        ElementoMenuAjustes(
            titulo = "Bóveda señuelo",
            subtitulo = "Apertura ficticia transparente ante coacción o amenaza",
            icono = Icons.Filled.Shield,
            colorIcono = Color(0xFFFB8C00),
            idEtiqueta = "01.2",
            grupo = "Seguridad",
            palabrasClave = "senuelo coaccion pin falso fake simulado",
            alPulsar = { vm.ir(Pantalla.AjustesSenuelo("01.2")) }
        ),
        ElementoMenuAjustes(
            titulo = "Autodestrucción por PIN",
            subtitulo = "Borrado irreversible inmediato de la bóveda ante peligro extremo",
            icono = Icons.Filled.DeleteForever,
            colorIcono = Color(0xFFE53935),
            idEtiqueta = "01.3",
            grupo = "Seguridad",
            palabrasClave = "autodestruccion borrar destruir emergencia peligro pin",
            alPulsar = { vm.ir(Pantalla.AjustesAutodestruccion("01.3")) }
        ),
        ElementoMenuAjustes(
            titulo = "Cifrado Argon2id",
            subtitulo = "Memoria, iteraciones y resistencia a ataques de fuerza bruta",
            icono = Icons.Filled.Memory,
            colorIcono = Color(0xFF5C6BC0),
            idEtiqueta = "01.4",
            grupo = "Seguridad",
            palabrasClave = "argon2id cifrado algoritmo hash ram memoria hilos kdf",
            alPulsar = { vm.ir(Pantalla.Argon2id("01.4")) }
        ),

        // Grupo: Cuentas y Datos
        ElementoMenuAjustes(
            titulo = "Copia de seguridad",
            subtitulo = "Exportar, restaurar y copias automáticas locales rotativas",
            icono = Icons.Filled.Backup,
            colorIcono = Color(0xFF43A047),
            idEtiqueta = "02.1",
            grupo = "Cuentas y Datos",
            palabrasClave = "copia seguridad backup exportar importar restaurar auto automatica",
            alPulsar = { vm.ir(Pantalla.CopiaSeguridad("02.1")) }
        ),
        ElementoMenuAjustes(
            titulo = "Contraseñas de Google",
            subtitulo = "Importar archivo CSV descargado de Google Passwords",
            icono = Icons.Filled.FileUpload,
            colorIcono = Color(0xFF0288D1),
            idEtiqueta = "02.2",
            grupo = "Cuentas y Datos",
            palabrasClave = "google passwords csv importar chrome navegador",
            alPulsar = { vm.ir(Pantalla.CsvGoogle("02.2")) }
        ),
        ElementoMenuAjustes(
            titulo = "Kit de emergencia",
            subtitulo = "Generar documento impreso con claves y rescate físico",
            icono = Icons.Filled.Description,
            colorIcono = Color(0xFFFFB300),
            idEtiqueta = "02.3",
            grupo = "Cuentas y Datos",
            palabrasClave = "kit emergencia papel pdf imprimir hoja rescate",
            alPulsar = { vm.ir(Pantalla.KitEmergencia("02.3")) }
        ),

        // Grupo: Personalización
        ElementoMenuAjustes(
            titulo = "Nombre de la app",
            subtitulo = "Personalizar el nombre visible en la cabecera y el menú lateral",
            icono = Icons.Filled.Badge,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "03.1",
            grupo = "Personalización",
            palabrasClave = "nombre personalizada titulo encabezado boveda local",
            valorTexto = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
            alPulsar = alAbrirNombreBoveda
        ),
        ElementoMenuAjustes(
            titulo = "Tema y colores",
            subtitulo = "Personalización de paleta, color de acento y modo oscuro",
            icono = Icons.Filled.Palette,
            colorIcono = Color(0xFF8E24AA),
            idEtiqueta = "03.2",
            grupo = "Personalización",
            palabrasClave = "tema colores paleta acento apariencia aspecto oscuro claro sistema",
            valorTexto = when (ajustes.temaApp) {
                "claro" -> "Claro"
                "oscuro" -> "Oscuro"
                else -> "Sistema"
            },
            alPulsar = { vm.ir(Pantalla.Tema("03.2")) }
        ),
        ElementoMenuAjustes(
            titulo = "Widgets de escritorio",
            subtitulo = "Generador rápido y accesos directos en pantalla de inicio",
            icono = Icons.Filled.Widgets,
            colorIcono = Color(0xFF7CB342),
            idEtiqueta = "03.3",
            grupo = "Personalización",
            palabrasClave = "widgets escritorio inicio favoritos totp generador 1x1",
            alPulsar = { vm.ir(Pantalla.AjustesWidget("03.3")) }
        ),
        ElementoMenuAjustes(
            titulo = "Abecedario lateral",
            subtitulo = "Navegación rápida con efecto de ola estilo Niagara y personalización",
            icono = Icons.AutoMirrored.Filled.Sort,
            colorIcono = Color(0xFF6A1B9A),
            idEtiqueta = "03.4",
            grupo = "Personalización",
            palabrasClave = "abecedario indice lateral ola niagara alfabeto scroll letras",
            valorTexto = if (ajustes.mostrarIndiceAlfabetico) "Activo" else "Oculto",
            alPulsar = { vm.ir(Pantalla.AjustesIndice("03.4")) }
        ),
        ElementoMenuAjustes(
            titulo = "Organización de lista",
            subtitulo = "Agrupamiento de cuentas por sitio, tamaño de filas y orden",
            icono = Icons.Filled.Layers,
            colorIcono = Color(0xFF00ACC1),
            idEtiqueta = "03.5",
            grupo = "Personalización",
            palabrasClave = "agrupar agrupamiento lista densidad compacta comoda cuentas sitio dominio carpetas orden",
            valorTexto = if (ajustes.agruparPorSitio) "Agrupada" else "Individual",
            alPulsar = { vm.ir(Pantalla.OrganizacionLista("03.5")) }
        ),
        ElementoMenuAjustes(
            titulo = "Formatos de campos",
            subtitulo = "Plantillas personalizadas y campos de autofill",
            icono = Icons.AutoMirrored.Filled.FormatListBulleted,
            colorIcono = Color(0xFFFFA000),
            idEtiqueta = "03.6",
            grupo = "Personalización",
            palabrasClave = "formatos campos plantillas autofill rellenar formulario",
            alPulsar = { vm.ir(Pantalla.FormatosCampos("03.6")) }
        ),
        ElementoMenuAjustes(
            titulo = "Formas y bordes",
            subtitulo = "Curvatura de esquinas, grosor y estilo de bordes",
            icono = Icons.Filled.SquareFoot,
            colorIcono = Color(0xFFE91E63),
            idEtiqueta = "03.7",
            grupo = "Personalización",
            palabrasClave = "formas bordes curvatura esquinas grosor estilo presets tarjetas",
            alPulsar = { vm.ir(Pantalla.Formas("03.7")) }
        ),
        ElementoMenuAjustes(
            titulo = "Tipografía y textos",
            subtitulo = "Tamaño de fuente, peso, espaciado e interlineado",
            icono = Icons.Filled.TextFields,
            colorIcono = Color(0xFF26A69A),
            idEtiqueta = "03.8",
            grupo = "Personalización",
            palabrasClave = "fuente tipografia letras texto tamano escala peso espaciado interlineado",
            alPulsar = { vm.ir(Pantalla.Tipografia("03.8")) }
        ),

        // Grupo: Funciones
        ElementoMenuAjustes(
            titulo = "Autenticador 2FA",
            subtitulo = "Parámetros predeterminados de códigos temporales TOTP",
            icono = Icons.Filled.Timer,
            colorIcono = Color(0xFF3949AB),
            idEtiqueta = "04.1",
            grupo = "Funciones",
            palabrasClave = "2fa totp autenticador codigos periodo hmac digitos",
            alPulsar = { vm.ir(Pantalla.AjustesAutenticador("04.1")) }
        ),
        ElementoMenuAjustes(
            titulo = "Cámara y escáner QR",
            subtitulo = "Motor óptico CameraX y compatibilidad de escaneo",
            icono = Icons.Filled.CameraAlt,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "04.2",
            grupo = "Funciones",
            palabrasClave = "camara escaner qr camerax optico lector",
            alPulsar = { vm.ir(Pantalla.AjustesCamara("04.2")) }
        ),
        ElementoMenuAjustes(
            titulo = "Mosaico rápido de Android",
            subtitulo = "Generación instantánea desde la barra de notificaciones",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFFBC02D),
            idEtiqueta = "04.3",
            grupo = "Funciones",
            palabrasClave = "tile mosaico barra estado cortina notificaciones rapido",
            alPulsar = { vm.ir(Pantalla.TileRapido("04.3")) }
        ),
        ElementoMenuAjustes(
            titulo = "Proveedor de Passkeys",
            subtitulo = "Activar Bóveda local en \"Contraseñas y llaves de acceso\" de Android",
            icono = Icons.Filled.Key,
            colorIcono = Color(0xFF8B5CF6),
            idEtiqueta = "04.4",
            grupo = "Funciones",
            palabrasClave = "passkey passkeys proveedor credenciales llaves acceso android servicio activar",
            alPulsar = alAbrirProveedorPasskeys
        ),
        ElementoMenuAjustes(
            titulo = "Historial de contraseñas",
            subtitulo = "Retención temporal, autodestrucción y registro de claves",
            icono = Icons.Filled.History,
            colorIcono = Color(0xFFFF9800),
            idEtiqueta = "04.5",
            grupo = "Funciones",
            palabrasClave = "historial contrasenas generadas retencion autodestruccion claves temporal tiempo",
            alPulsar = { vm.ir(Pantalla.HistorialClaves("04.5")) }
        ),

        // Grupo: Sistema
        ElementoMenuAjustes(
            titulo = "Opciones avanzadas",
            subtitulo = "Cambio de clave maestra, visualización de IDs y borrado",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFC2185B),
            idEtiqueta = "05.1",
            grupo = "Sistema",
            palabrasClave = "avanzada maestra clave cambiar borrar ids desarrollo",
            alPulsar = { vm.ir(Pantalla.Avanzada("05.1")) }
        )
    )
}
