package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.Pantalla

/**
 * Acciones especiales que abren diálogos modales o flujos específicos en vez de una pantalla.
 */
enum class AccionEspecialAjuste {
    NOMBRE_BOVEDA,
    PROVEEDOR_PASSKEYS,
    CAMBIAR_MAESTRA
}

/**
 * Nodo unitario en el árbol fractal de Ajustes.
 * En la raíz (Ajustes N1), [icono] y [colorIcono] están presentes con estilo squircle.
 * En todos los subniveles (N2, N3, etc.), [icono] es estrictamente null por diseño Pixel / iOS.
 */
data class NodoAjuste(
    val id: String,
    val titulo: String,
    val subtitulo: String,
    val ruta: String,
    val grupo: String,
    val pantallaDestino: Pantalla? = null,
    val padreId: String? = null,
    val esRaiz: Boolean = false,
    val icono: ImageVector? = null,
    val colorIcono: Color? = null,
    val palabrasClave: List<String> = emptyList(),
    val accionEspecial: AccionEspecialAjuste? = null
)

/**
 * Catálogo inmutable centralizado de todos los nodos de ajustes de la aplicación.
 */
object MapaAjustes {

    val NODOS_RAIZ: List<NodoAjuste> = listOf(
        // ==========================================
        // GRUPOS NIVEL 1 (Hijos directos de 00-AJU)
        // ==========================================
        NodoAjuste(
            id = "01-SEG",
            titulo = "Seguridad",
            subtitulo = "Políticas de acceso, biometría, claves y cifrado",
            ruta = "Seguridad",
            grupo = "Seguridad",
            esRaiz = true,
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFF1E88E5),
            palabrasClave = listOf("seguridad", "bloqueo", "huella", "biometria", "pin", "clave")
        ),
        NodoAjuste(
            id = "02-APA",
            titulo = "Apariencia",
            subtitulo = "Temas, colores, tipografía, bordes e identificadores",
            ruta = "Apariencia",
            grupo = "Apariencia",
            esRaiz = true,
            icono = Icons.Filled.Palette,
            colorIcono = Color(0xFF8E24AA),
            palabrasClave = listOf("apariencia", "tema", "colores", "fuentes", "bordes")
        ),
        NodoAjuste(
            id = "03-LST",
            titulo = "Lista de cuentas",
            subtitulo = "Diseño, títulos, identidades, índice A-Z y formatos",
            ruta = "Lista de cuentas",
            grupo = "Lista de cuentas",
            esRaiz = true,
            icono = Icons.Filled.Layers,
            colorIcono = Color(0xFF00ACC1),
            palabrasClave = listOf("lista", "cuentas", "diseño", "orden", "identidades", "indice")
        ),
        NodoAjuste(
            id = "04-HER",
            titulo = "Herramientas",
            subtitulo = "Autocompletado, 2FA, historial, cámara y widgets",
            ruta = "Herramientas",
            grupo = "Herramientas",
            esRaiz = true,
            icono = Icons.Filled.Build,
            colorIcono = Color(0xFF8B5CF6),
            palabrasClave = listOf("herramientas", "autocompletado", "2fa", "claves", "widgets")
        ),
        NodoAjuste(
            id = "05-COP",
            titulo = "Copias y datos",
            subtitulo = "Copias de seguridad, exportación e importación",
            ruta = "Copias y datos",
            grupo = "Copias y datos",
            esRaiz = true,
            icono = Icons.Filled.Backup,
            colorIcono = Color(0xFF43A047),
            palabrasClave = listOf("copias", "backup", "datos", "importar", "exportar")
        ),
        NodoAjuste(
            id = "06-SIS",
            titulo = "Sistema",
            subtitulo = "Opciones avanzadas, registro de eventos y diagnóstico",
            ruta = "Sistema",
            grupo = "Sistema",
            esRaiz = true,
            icono = Icons.Filled.Settings,
            colorIcono = Color(0xFFC2185B),
            palabrasClave = listOf("sistema", "avanzada", "registro", "diagnostico", "ajustes")
        )
    )

    // Subnodos (Nivel 2 y 3+)
    val SUBNODOS: List<NodoAjuste> = listOf(
        // ==========================================
        // NIVEL 2: Hijos de Seguridad (01-SEG)
        // ==========================================
        NodoAjuste(
            id = "01-SEG-BIO",
            titulo = "Biometría y seguridad",
            subtitulo = "Políticas de acceso, autenticación biométrica y cifrado de datos",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.Seguridad(),
            padreId = "01-SEG",
            icono = Icons.Filled.Fingerprint,
            colorIcono = Color(0xFF1E88E5),
            palabrasClave = listOf("seguridad", "huella", "biometria", "clave", "contrasena", "maestra", "pin", "bloqueo", "inactividad", "fuerza bruta", "portapapeles", "autodestruccion", "senuelo", "cifrado", "argon2id")
        ),

        // ==========================================
        // NIVEL 2: Hijos de Apariencia (02-APA)
        // ==========================================
        NodoAjuste(
            id = "02-APA-THM",
            titulo = "Tema y colores",
            subtitulo = "Modo oscuro, paleta y acento dinámico",
            ruta = "Apariencia > Tema y colores",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.Tema(),
            padreId = "02-APA",
            icono = Icons.Filled.Palette,
            colorIcono = Color(0xFF8E24AA),
            palabrasClave = listOf("tema", "colores", "paleta", "acento", "oscuro", "claro", "sistema")
        ),
        NodoAjuste(
            id = "02-APA-TYP",
            titulo = "Tipografía",
            subtitulo = "Familia, peso y escala tipográfica",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.Tipografia(),
            padreId = "02-APA",
            icono = Icons.Filled.TextFields,
            colorIcono = Color(0xFF5E35B1),
            palabrasClave = listOf("fuente", "tipografia", "letra", "mono", "sans", "tamano", "escala")
        ),
        NodoAjuste(
            id = "02-APA-GEO",
            titulo = "Formas y bordes",
            subtitulo = "Curvatura, grosor de líneas y separación",
            ruta = "Apariencia > Formas y bordes",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.Formas(),
            padreId = "02-APA",
            icono = Icons.Filled.SquareFoot,
            colorIcono = Color(0xFF3949AB),
            palabrasClave = listOf("formas", "bordes", "esquinas", "curvatura", "radio", "contorno")
        ),
        NodoAjuste(
            id = "02-APA-COL",
            titulo = "Identificadores de campos",
            subtitulo = "Pastillas de color por tipo de credencial",
            ruta = "Apariencia > Identificadores de campos",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.ColoresDatos(),
            padreId = "02-APA",
            icono = Icons.Filled.Badge,
            colorIcono = Color(0xFF1E88E5),
            palabrasClave = listOf("colores", "etiquetas", "tipos", "credenciales", "pastillas")
        ),
        NodoAjuste(
            id = "02-APA-LNC",
            titulo = "Nombre de la bóveda",
            subtitulo = "Personalizar el texto de la cabecera",
            ruta = "Apariencia > Nombre de la bóveda",
            grupo = "Apariencia",
            padreId = "02-APA",
            icono = Icons.Filled.Badge,
            colorIcono = Color(0xFF00897B),
            palabrasClave = listOf("nombre", "personalizada", "titulo", "encabezado"),
            accionEspecial = AccionEspecialAjuste.NOMBRE_BOVEDA
        ),
        NodoAjuste(
            id = "02-APA-MNL",
            titulo = "Personalizar menú lateral",
            subtitulo = "Visibilidad de cabecera, pie, botón bloquear, agrupación y accesos",
            ruta = "Apariencia > Personalizar menú lateral",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.PersonalizarMenuLateral(),
            padreId = "02-APA",
            icono = Icons.Filled.Menu,
            colorIcono = Color(0xFF6366F1),
            palabrasClave = listOf("menu", "lateral", "barra", "sidebar", "bloquear", "agrupar", "bordes", "accesos", "reorganizar")
        ),

        // ==========================================
        // NIVEL 2: Hijos de Lista de cuentas (03-LST)
        // ==========================================
        NodoAjuste(
            id = "03-LST-DES",
            titulo = "Diseño de lista",
            subtitulo = "Agrupamiento por sitio y densidad",
            ruta = "Lista de cuentas > Diseño de lista",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.OrganizacionLista(),
            padreId = "03-LST",
            icono = Icons.Filled.Layers,
            colorIcono = Color(0xFF00ACC1),
            palabrasClave = listOf("agrupar", "lista", "densidad", "compacta", "comoda", "sitio")
        ),
        NodoAjuste(
            id = "03-LST-DES-TIT",
            titulo = "Asistente de títulos",
            subtitulo = "Normalizar nombres web y reglas de URLs",
            ruta = "Lista de cuentas > Asistente de títulos",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.NormalizadorTitulos(),
            padreId = "03-LST",
            icono = Icons.Filled.Layers,
            colorIcono = Color(0xFF00ACC1),
            palabrasClave = listOf("asistente", "titulos", "normalizador", "urls", "reglas")
        ),
        NodoAjuste(
            id = "03-LST-IDE",
            titulo = "Identidades",
            subtitulo = "Perfiles de correo y vinculación inteligente",
            ruta = "Lista de cuentas > Identidades",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.Identidades(),
            padreId = "03-LST",
            icono = Icons.Filled.AccountCircle,
            colorIcono = Color(0xFF0284C7),
            palabrasClave = listOf("identidades", "cuentas", "perfiles", "correo", "email", "alias")
        ),
        NodoAjuste(
            id = "03-LST-AZX",
            titulo = "Índice A-Z",
            subtitulo = "Desplazamiento rápido alfabético lateral",
            ruta = "Lista de cuentas > Índice A-Z",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.AjustesIndice(),
            padreId = "03-LST",
            icono = Icons.AutoMirrored.Filled.Sort,
            colorIcono = Color(0xFF6A1B9A),
            palabrasClave = listOf("abecedario", "indice", "ola", "niagara", "scroll", "a-z")
        ),
        NodoAjuste(
            id = "03-LST-FMT",
            titulo = "Formatos",
            subtitulo = "Campos personalizados predeterminados",
            ruta = "Lista de cuentas > Formatos",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.FormatosCampos(),
            padreId = "03-LST",
            icono = Icons.AutoMirrored.Filled.FormatListBulleted,
            colorIcono = Color(0xFFFFA000),
            palabrasClave = listOf("formatos", "campos", "plantillas", "formulario")
        ),
        NodoAjuste(
            id = "03-LST-PLT",
            titulo = "Plantillas de campos",
            subtitulo = "Plantillas personalizadas y del sistema",
            ruta = "Lista de cuentas > Plantillas de campos",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.PlantillasCampos(),
            padreId = "03-LST",
            icono = Icons.Filled.DynamicForm,
            colorIcono = Color(0xFF10B981),
            palabrasClave = listOf("plantillas", "presets", "personalizados", "modelos")
        ),
        NodoAjuste(
            id = "03-LST-SLD",
            titulo = "Salud de la bóveda",
            subtitulo = "Auditoría de contraseñas débiles, repetidas y vulneradas",
            ruta = "Lista de cuentas > Salud de la bóveda",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.SaludBoveda(),
            padreId = "03-LST",
            icono = Icons.Filled.HealthAndSafety,
            colorIcono = Color(0xFF10B981),
            palabrasClave = listOf("salud", "seguridad", "debiles", "repetidas", "antiguas", "analisis", "auditoria")
        ),
        NodoAjuste(
            id = "03-LST-DUP",
            titulo = "Cuentas duplicadas",
            subtitulo = "Detección y resolución de entradas repetidas",
            ruta = "Lista de cuentas > Cuentas duplicadas",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.Duplicados(),
            padreId = "03-LST",
            icono = Icons.Filled.ContentCopy,
            colorIcono = Color(0xFFF59E0B),
            palabrasClave = listOf("duplicados", "repetidas", "clones", "limpieza")
        ),
        NodoAjuste(
            id = "03-LST-PAP",
            titulo = "Papelera de reciclaje",
            subtitulo = "Recuperación y purga definitiva de cuentas eliminadas",
            ruta = "Lista de cuentas > Papelera",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.Papelera(),
            padreId = "03-LST",
            icono = Icons.Filled.Delete,
            colorIcono = Color(0xFFEF4444),
            palabrasClave = listOf("papelera", "borrados", "recuperar", "restaurar", "eliminar", "purga")
        ),
        NodoAjuste(
            id = "03-LST-CAT",
            titulo = "Categorías de cuentas",
            subtitulo = "Organizar entradas por carpetas, etiquetas y grupos",
            ruta = "Lista de cuentas > Categorías",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.Categorias(),
            padreId = "03-LST",
            icono = Icons.Filled.Folder,
            colorIcono = Color(0xFF10B981),
            palabrasClave = listOf("categorias", "carpetas", "etiquetas", "clasificar", "grupos")
        ),

        // ==========================================
        // NIVEL 2: Hijos de Herramientas (04-HER)
        // ==========================================
        NodoAjuste(
            id = "04-HER-PSK",
            titulo = "Autocompletado y llaves",
            subtitulo = "Sugerencias en teclado y proveedor de credenciales",
            ruta = "Herramientas > Autocompletado y llaves",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesAutocompletado(),
            padreId = "04-HER",
            icono = Icons.Filled.Key,
            colorIcono = Color(0xFF8B5CF6),
            palabrasClave = listOf("autofill", "autocompletado", "passkeys", "llaves", "teclado")
        ),
        NodoAjuste(
            id = "04-HER-AUT",
            titulo = "Verificación en dos pasos",
            subtitulo = "Parámetros predeterminados de códigos de dos pasos",
            ruta = "Herramientas > Verificación en dos pasos",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesAutenticador(),
            padreId = "04-HER",
            icono = Icons.Filled.Timer,
            colorIcono = Color(0xFF3949AB),
            palabrasClave = listOf("2fa", "totp", "autenticador", "codigos", "periodo")
        ),
        NodoAjuste(
            id = "04-HER-HST",
            titulo = "Historial de claves",
            subtitulo = "Retención temporal y autodestrucción",
            ruta = "Herramientas > Historial de claves",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesHistorial(),
            padreId = "04-HER",
            icono = Icons.Filled.History,
            colorIcono = Color(0xFFFF9800),
            palabrasClave = listOf("historial", "contrasenas", "generadas", "retencion", "autodestruccion")
        ),
        NodoAjuste(
            id = "04-HER-CAM",
            titulo = "Cámara y escáner",
            subtitulo = "Motor óptico CameraX y compatibilidad",
            ruta = "Herramientas > Cámara y escáner",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesCamara(),
            padreId = "04-HER",
            icono = Icons.Filled.CameraAlt,
            colorIcono = Color(0xFF00897B),
            palabrasClave = listOf("camara", "escaner", "qr", "camerax", "lector")
        ),
        NodoAjuste(
            id = "04-HER-WGT",
            titulo = "Widgets",
            subtitulo = "Generador 1x1 y accesos directos TOTP",
            ruta = "Herramientas > Widgets",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesWidget(),
            padreId = "04-HER",
            icono = Icons.Filled.Widgets,
            colorIcono = Color(0xFF7CB342),
            palabrasClave = listOf("widgets", "escritorio", "inicio", "favoritos", "generador")
        ),
        NodoAjuste(
            id = "04-HER-MSK",
            titulo = "Mosaico rápido",
            subtitulo = "Generación desde barra de notificaciones",
            ruta = "Herramientas > Mosaico rápido",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.TileRapido(),
            padreId = "04-HER",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFFBC02D),
            palabrasClave = listOf("tile", "mosaico", "notificaciones", "rapido")
        ),
        NodoAjuste(
            id = "04-HER-GEN",
            titulo = "Generar contraseñas",
            subtitulo = "Generador aleatorio de contraseñas y claves robustas",
            ruta = "Herramientas > Generar contraseñas",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.Generador,
            padreId = "04-HER",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = Color(0xFF3B82F6),
            palabrasClave = listOf("generador", "crear", "claves", "longitud", "simbolos", "robustez")
        ),
        NodoAjuste(
            id = "04-HER-2FA",
            titulo = "Autenticador 2FA",
            subtitulo = "Códigos TOTP temporales de un solo uso",
            ruta = "Herramientas > Autenticador 2FA",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.Autenticador,
            padreId = "04-HER",
            icono = Icons.Filled.Timer,
            colorIcono = Color(0xFFF97316),
            palabrasClave = listOf("autenticador", "totp", "2fa", "codigos", "temporales", "doble factor")
        ),

        // ==========================================
        // NIVEL 2: Hijos de Copias y datos (05-COP)
        // ==========================================
        NodoAjuste(
            id = "05-COP-MAN",
            titulo = "Copia de seguridad",
            subtitulo = "Exportar, restaurar y copias automáticas",
            ruta = "Copias y datos > Copia de seguridad",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.CopiaSeguridad(),
            padreId = "05-COP",
            icono = Icons.Filled.Backup,
            colorIcono = Color(0xFF43A047),
            palabrasClave = listOf("copia", "seguridad", "backup", "exportar", "restaurar", "auto")
        ),
        NodoAjuste(
            id = "05-COP-CSV",
            titulo = "Importar de Google",
            subtitulo = "Importar archivo CSV de Google Passwords",
            ruta = "Copias y datos > Importar de Google",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.CsvGoogle(),
            padreId = "05-COP",
            icono = Icons.Filled.FileDownload,
            colorIcono = Color(0xFF0288D1),
            palabrasClave = listOf("google", "passwords", "csv", "importar", "chrome")
        ),
        NodoAjuste(
            id = "05-COP-KIT",
            titulo = "Kit de emergencia",
            subtitulo = "Ficha física imprimible con claves maestras",
            ruta = "Copias y datos > Kit de emergencia",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.KitEmergencia(),
            padreId = "05-COP",
            icono = Icons.Filled.Description,
            colorIcono = Color(0xFFFFB300),
            palabrasClave = listOf("kit", "emergencia", "papel", "pdf", "imprimir")
        ),

        // ==========================================
        // NIVEL 2: Hijos de Sistema (06-SIS)
        // ==========================================
        NodoAjuste(
            id = "06-SIS-AVZ",
            titulo = "Opciones avanzadas",
            subtitulo = "Respuesta táctil, desarrollo y peligro",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.Avanzada(),
            padreId = "06-SIS",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFC2185B),
            palabrasClave = listOf("avanzada", "desarrollo", "haptica", "reorganizar", "peligro")
        ),
        NodoAjuste(
            id = "06-SIS-LOG",
            titulo = "Registro de eventos",
            subtitulo = "Auditoría de acciones, accesos y seguridad",
            ruta = "Sistema > Registro de eventos",
            grupo = "Sistema",
            pantallaDestino = Pantalla.Registro(),
            padreId = "06-SIS",
            icono = Icons.Filled.History,
            colorIcono = Color(0xFF00897B),
            palabrasClave = listOf("registro", "eventos", "logs", "auditoria", "fallos")
        ),
        NodoAjuste(
            id = "06-SIS-DGN",
            titulo = "Diagnóstico de seguridad",
            subtitulo = "Auditoría de integridad, versión y licencias",
            ruta = "Sistema > Diagnóstico de seguridad",
            grupo = "Sistema",
            pantallaDestino = Pantalla.AcercaDe(),
            padreId = "06-SIS",
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFF607D8B),
            palabrasClave = listOf("diagnostico", "seguridad", "acerca de", "version", "licencias")
        ),
        // ==========================================
        // Hijos de Biometría y seguridad (01-SEG-BIO)
        // ==========================================
        NodoAjuste(
            id = "01-SEG-BIO-BLO",
            titulo = "Bloqueo y biometría",
            subtitulo = "Huella dactilar, bloqueo por inactividad y fuerza bruta",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.BloqueoBiometria(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("huella", "bloqueo", "inactividad", "fuerza bruta", "intentos", "tiempo", "espera")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-BIO",
            titulo = "Biometría",
            subtitulo = "Autenticación biométrica con huella dactilar y modos fuerte o compatible",
            ruta = "Seguridad > Biometría y seguridad > Bloqueo y biometría",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.AjustesBiometria(),
            padreId = "01-SEG-BIO-BLO",
            palabrasClave = listOf("huella", "biometria", "fingerprint", "sensor", "fuerte", "compatible")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-APP",
            titulo = "Bloqueo de aplicación",
            subtitulo = "Temporizadores de inactividad y protección de capturas de pantalla",
            ruta = "Seguridad > Biometría y seguridad > Bloqueo y biometría",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.BloqueoApp(),
            padreId = "01-SEG-BIO-BLO",
            palabrasClave = listOf("bloqueo", "inactividad", "temporizador", "minutos", "flag secure", "pantalla")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-BRU",
            titulo = "Protección contra fuerza bruta",
            subtitulo = "Defensa progresiva contra ataques de diccionario y limitación de reintentos",
            ruta = "Seguridad > Biometría y seguridad > Bloqueo y biometría",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.FuerzaBruta(),
            padreId = "01-SEG-BIO-BLO",
            palabrasClave = listOf("fuerza", "bruta", "freno", "intentos", "espera", "ataques")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-VIS",
            titulo = "Seguridad visual",
            subtitulo = "Ocultar contenido en aplicaciones recientes y capturas de pantalla",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.SeguridadVisual(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("visual", "flag secure", "privacidad", "recientes", "capturas")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-CLP",
            titulo = "Portapapeles",
            subtitulo = "Limpieza automática y retención en memoria",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.Portapapeles(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("portapapeles", "copiar", "pegar", "limpieza", "segundos", "memoria")
        ),
        NodoAjuste(
            id = "01-SEG-BIO-AUD",
            titulo = "Auditoría de seguridad",
            subtitulo = "Umbral de antigüedad y salud de contraseñas",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.AuditoriaSeguridad(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("auditoria", "salud", "antiguedad", "meses", "caducidad", "vencimiento")
        ),
        NodoAjuste(
            id = "01-SEG-PAS",
            titulo = "Clave maestra",
            subtitulo = "Cambiar la contraseña principal de la bóveda",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("clave", "contrasena", "maestra", "cambiar", "pass", "password"),
            accionEspecial = AccionEspecialAjuste.CAMBIAR_MAESTRA
        ),
        NodoAjuste(
            id = "01-SEG-SEN",
            titulo = "Modo señuelo",
            subtitulo = "PIN de coacción y cuentas simuladas",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.AjustesSenuelo(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("senuelo", "coaccion", "pin", "falso", "fake", "cuentas", "panico")
        ),
        NodoAjuste(
            id = "01-SEG-DES-PIN",
            titulo = "Autodestrucción por PIN",
            subtitulo = "Borrado irreversible inmediato por código de emergencia",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.AjustesAutodestruccion(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("autodestruccion", "pin", "emergencia", "borrado", "panico", "destruir")
        ),
        NodoAjuste(
            id = "01-SEG-DES-INT",
            titulo = "Autodestrucción por intentos fallidos",
            subtitulo = "Límite de claves erróneas antes del borrado irreversible",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.AutodestruccionIntentos(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("autodestruccion", "intentos fallidos", "claves erroneas", "umbral", "limite", "borrar")
        ),
        NodoAjuste(
            id = "01-SEG-CRY",
            titulo = "Cifrado Argon2id",
            subtitulo = "Parámetros KDF de resistencia criptográfica",
            ruta = "Seguridad > Biometría y seguridad",
            grupo = "Seguridad",
            pantallaDestino = Pantalla.Argon2id(),
            padreId = "01-SEG-BIO",
            palabrasClave = listOf("argon2id", "cifrado", "algoritmo", "kdf", "memoria", "hilos", "ram")
        ),

        // ==========================================
        // Hijos de Tema y colores (02-APA-THM)
        // ==========================================
        NodoAjuste(
            id = "02-APA-THM-PAL",
            titulo = "Paleta y estilo",
            subtitulo = "Modo claro u oscuro y colores de acento",
            ruta = "Apariencia > Tema y colores",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TemaPaletas(),
            padreId = "02-APA-THM",
            palabrasClave = listOf("paleta", "estilo", "tema", "color", "acento", "oscuro", "claro", "dinamico")
        ),
        NodoAjuste(
            id = "02-APA-THM-ANI",
            titulo = "Animación de desbloqueo",
            subtitulo = "Velocidad de engranajes, puerta y efectos luminosos",
            ruta = "Apariencia > Tema y colores",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.AnimacionDesbloqueo(),
            padreId = "02-APA-THM",
            palabrasClave = listOf("animaciones", "desbloqueo", "engranajes", "puerta", "glow", "calibracion", "velocidad")
        ),
        NodoAjuste(
            id = "02-APA-THM-LAB",
            titulo = "Laboratorio de temas",
            subtitulo = "Exportación e importación de paletas JSON",
            ruta = "Apariencia > Tema y colores",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.LaboratorioTemas(),
            padreId = "02-APA-THM",
            palabrasClave = listOf("laboratorio", "temas", "json", "exportar", "importar", "paletas")
        ),
        NodoAjuste(
            id = "02-APA-THM-CAL",
            titulo = "Calibración de animaciones",
            subtitulo = "Ajustes de duración, suavizado y rebote elástico",
            ruta = "Apariencia > Tema y colores > Animación de desbloqueo",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.CalibracionAnimacion(),
            padreId = "02-APA-THM-ANI",
            palabrasClave = listOf("calibracion", "animacion", "velocidad", "rebote", "puerta", "engranajes")
        ),

        // ==========================================
        // Hijos de Tipografía (02-APA-TYP)
        // ==========================================
        NodoAjuste(
            id = "02-APA-TYP-PRE",
            titulo = "Presets tipográficos",
            subtitulo = "Combinaciones predefinidas de fuente y escala",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TipografiaPresets(),
            padreId = "02-APA-TYP",
            palabrasClave = listOf("presets", "tipografia", "fuente", "modelos")
        ),
        NodoAjuste(
            id = "02-APA-TYP-ESC",
            titulo = "Escala de texto",
            subtitulo = "Tamaño y proporción de texto en la interfaz",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TipografiaEscala(),
            padreId = "02-APA-TYP",
            palabrasClave = listOf("escala", "tamano", "texto", "fuente")
        ),
        NodoAjuste(
            id = "02-APA-TYP-FAM",
            titulo = "Familia tipográfica",
            subtitulo = "Selección de fuente: Sans, Serif o Monospace",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TipografiaFamilia(),
            padreId = "02-APA-TYP",
            palabrasClave = listOf("familia", "fuente", "sans", "mono", "serif")
        ),
        NodoAjuste(
            id = "02-APA-TYP-PES",
            titulo = "Peso tipográfico",
            subtitulo = "Grosor regular, medium o bold",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TipografiaPeso(),
            padreId = "02-APA-TYP",
            palabrasClave = listOf("peso", "grosor", "bold", "regular", "medium")
        ),
        NodoAjuste(
            id = "02-APA-TYP-ESP",
            titulo = "Espaciado de texto",
            subtitulo = "Interletraje y altura de línea",
            ruta = "Apariencia > Tipografía",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.TipografiaEspaciado(),
            padreId = "02-APA-TYP",
            palabrasClave = listOf("espaciado", "interletraje", "linea", "texto")
        ),

        // ==========================================
        // Hijos de Formas y bordes (02-APA-GEO)
        // ==========================================
        NodoAjuste(
            id = "02-APA-GEO-PRE",
            titulo = "Presets de formas",
            subtitulo = "Estilos geométricos predefinidos",
            ruta = "Apariencia > Formas y bordes",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.FormasPresets(),
            padreId = "02-APA-GEO",
            palabrasClave = listOf("presets", "formas", "bordes", "estilos")
        ),
        NodoAjuste(
            id = "02-APA-GEO-CRV",
            titulo = "Curvatura de esquinas",
            subtitulo = "Radio de redondeo en dp para tarjetas y botones",
            ruta = "Apariencia > Formas y bordes",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.FormasCurvatura(),
            padreId = "02-APA-GEO",
            palabrasClave = listOf("curvatura", "esquinas", "radio", "dp", "redondeo")
        ),
        NodoAjuste(
            id = "02-APA-GEO-BOR",
            titulo = "Bordes y contornos",
            subtitulo = "Grosor de línea y estilo de borde",
            ruta = "Apariencia > Formas y bordes",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.FormasBorde(),
            padreId = "02-APA-GEO",
            palabrasClave = listOf("bordes", "contorno", "grosor", "linea")
        ),
        NodoAjuste(
            id = "02-APA-GEO-ESP",
            titulo = "Espaciado y márgenes",
            subtitulo = "Separación entre tarjetas y elementos de la lista",
            ruta = "Apariencia > Formas y bordes",
            grupo = "Apariencia",
            pantallaDestino = Pantalla.FormasEspaciado(),
            padreId = "02-APA-GEO",
            palabrasClave = listOf("espaciado", "margenes", "separacion", "tarjetas")
        ),

        // ==========================================
        // Hijos de Diseño de lista (03-LST-DES)
        // ==========================================
        NodoAjuste(
            id = "03-LST-DES-EST",
            titulo = "Estructura y visualización",
            subtitulo = "Densidad, modo compacto y vista de elementos",
            ruta = "Lista de cuentas > Diseño de lista",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.OrganizacionEstructura(),
            padreId = "03-LST-DES",
            palabrasClave = listOf("estructura", "densidad", "compacta", "comoda", "vista", "diseno")
        ),
        NodoAjuste(
            id = "03-LST-DES-JER",
            titulo = "Jerarquía y orden",
            subtitulo = "Agrupamiento por sitio y criterios de ordenación",
            ruta = "Lista de cuentas > Diseño de lista",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.OrganizacionJerarquia(),
            padreId = "03-LST-DES",
            palabrasClave = listOf("jerarquia", "agrupamiento", "sitio", "orden", "carpetas", "criterios")
        ),
        NodoAjuste(
            id = "03-LST-DES-IND",
            titulo = "Indicadores y badges",
            subtitulo = "Pastillas de seguridad, TOTP y tipo de cuenta",
            ruta = "Lista de cuentas > Diseño de lista",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.OrganizacionIndicadores(),
            padreId = "03-LST-DES",
            palabrasClave = listOf("indicadores", "badges", "pastillas", "totp", "etiquetas", "iconos")
        ),

        // ==========================================
        // Hijos de Índice A-Z (03-LST-AZX)
        // ==========================================
        NodoAjuste(
            id = "03-LST-AZX-OLA",
            titulo = "Efecto ola Niagara",
            subtitulo = "Animación expansiva al deslizar el índice lateral",
            ruta = "Lista de cuentas > Índice A-Z",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.IndiceOla(),
            padreId = "03-LST-AZX",
            palabrasClave = listOf("ola", "niagara", "animacion", "indice", "curva")
        ),
        NodoAjuste(
            id = "03-LST-AZX-CRE",
            titulo = "Curvatura de cresta",
            subtitulo = "Intensidad y amplitud del arco de la ola",
            ruta = "Lista de cuentas > Índice A-Z",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.IndiceCresta(),
            padreId = "03-LST-AZX",
            palabrasClave = listOf("cresta", "curvatura", "arco", "amplitud")
        ),
        NodoAjuste(
            id = "03-LST-AZX-HAP",
            titulo = "Respuesta háptica del índice",
            subtitulo = "Vibración al pasar por cada letra del alfabeto",
            ruta = "Lista de cuentas > Índice A-Z",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.IndiceHaptica(),
            padreId = "03-LST-AZX",
            palabrasClave = listOf("haptica", "vibracion", "indice", "letras")
        ),
        NodoAjuste(
            id = "03-LST-AZX-RES",
            titulo = "Resaltado de letra activa",
            subtitulo = "Color y tamaño de la pastilla flotante de la letra actual",
            ruta = "Lista de cuentas > Índice A-Z",
            grupo = "Lista de cuentas",
            pantallaDestino = Pantalla.IndiceResaltado(),
            padreId = "03-LST-AZX",
            palabrasClave = listOf("resaltado", "letra", "pastilla", "flotante")
        ),

        // ==========================================
        // Hijos de Autocompletado (04-HER-PSK)
        // ==========================================
        NodoAjuste(
            id = "04-HER-PSK-REG",
            titulo = "Reglas de autocompletado",
            subtitulo = "Mapeo de dominios, paquetes y URLs asociadas",
            ruta = "Herramientas > Autocompletado",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.ReglasAutocompletado,
            padreId = "04-HER-PSK",
            palabrasClave = listOf("reglas", "autocompletado", "autofill", "dominios", "paquetes")
        ),
        NodoAjuste(
            id = "04-HER-PSK-PRV",
            titulo = "Activar proveedor de credenciales",
            subtitulo = "Establecer como administrador de contraseñas de Android",
            ruta = "Herramientas > Autocompletado",
            grupo = "Herramientas",
            padreId = "04-HER-PSK",
            palabrasClave = listOf("proveedor", "credenciales", "android", "activar", "passkeys"),
            accionEspecial = AccionEspecialAjuste.PROVEEDOR_PASSKEYS
        ),

        // ==========================================
        // Hijos de Historial de claves (04-HER-HST)
        // ==========================================
        NodoAjuste(
            id = "04-HER-HST-GEN",
            titulo = "Claves generadas",
            subtitulo = "Historial de contraseñas creadas y retención temporal",
            ruta = "Herramientas > Historial de claves",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.HistorialClavesGeneradas(),
            padreId = "04-HER-HST",
            palabrasClave = listOf("generadas", "historial", "claves", "retencion", "tiempo", "autodestruccion")
        ),
        NodoAjuste(
            id = "04-HER-HST-ENT",
            titulo = "Historial de credenciales",
            subtitulo = "Auditoría de versiones anteriores de credenciales modificadas",
            ruta = "Herramientas > Historial de claves",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.HistorialCredenciales(),
            padreId = "04-HER-HST",
            palabrasClave = listOf("credenciales", "versiones", "modificadas", "anteriores", "auditoria")
        ),

        // ==========================================
        // Hijos de Widgets (04-HER-WGT)
        // ==========================================
        NodoAjuste(
            id = "04-HER-WGT-TOT",
            titulo = "Widget TOTP",
            subtitulo = "Acceso directo a códigos 2FA en pantalla de inicio",
            ruta = "Herramientas > Widgets",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesWidgetTotpSub(),
            padreId = "04-HER-WGT",
            palabrasClave = listOf("widget", "totp", "2fa", "inicio", "pantalla de inicio", "calibracion")
        ),
        NodoAjuste(
            id = "04-HER-WGT-1X1",
            titulo = "Widget 1x1",
            subtitulo = "Generador rápido de contraseñas en miniatura",
            ruta = "Herramientas > Widgets",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.AjustesWidget1x1Sub(),
            padreId = "04-HER-WGT",
            palabrasClave = listOf("widget", "1x1", "generador", "rapido", "miniatura", "boton")
        ),
        NodoAjuste(
            id = "04-HER-WGT-TOT-CFG",
            titulo = "Configuración Widget TOTP",
            subtitulo = "Cuentas favoritas y disposición de columnas",
            ruta = "Herramientas > Widgets > Widget TOTP",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.WidgetTotpAjustes(),
            padreId = "04-HER-WGT-TOT",
            palabrasClave = listOf("favoritos", "columnas", "disposicion", "totp")
        ),
        NodoAjuste(
            id = "04-HER-WGT-CAL",
            titulo = "Calibración Widget TOTP",
            subtitulo = "Ajuste de bordes, opacidad y escala del widget TOTP",
            ruta = "Herramientas > Widgets > Widget TOTP",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.CalibracionWidgetTotp(),
            padreId = "04-HER-WGT-TOT",
            palabrasClave = listOf("calibracion", "opacidad", "escala", "bordes", "totp")
        ),
        NodoAjuste(
            id = "04-HER-WGT-MOD",
            titulo = "Modo de generación Widget 1x1",
            subtitulo = "Longitud, caracteres y complejidad de claves",
            ruta = "Herramientas > Widgets > Widget 1x1",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.Widget1x1Modo(),
            padreId = "04-HER-WGT-1X1",
            palabrasClave = listOf("modo", "longitud", "complejidad", "caracteres")
        ),
        NodoAjuste(
            id = "04-HER-WGT-CMP",
            titulo = "Comportamiento Widget 1x1",
            subtitulo = "Acción al pulsar: copiar o abrir aplicación",
            ruta = "Herramientas > Widgets > Widget 1x1",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.Widget1x1Comportamiento(),
            padreId = "04-HER-WGT-1X1",
            palabrasClave = listOf("comportamiento", "pulsar", "copiar", "abrir")
        ),
        NodoAjuste(
            id = "04-HER-WGT-1X1-CAL",
            titulo = "Calibración Widget 1x1",
            subtitulo = "Personalización visual del botón 1x1",
            ruta = "Herramientas > Widgets > Widget 1x1",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.CalibracionWidget1x1(),
            padreId = "04-HER-WGT-1X1",
            palabrasClave = listOf("calibracion", "visual", "1x1", "diseno")
        ),

        // ==========================================
        // Hijos de Mosaico rápido (04-HER-MSK)
        // ==========================================
        NodoAjuste(
            id = "04-HER-MSK-CFG",
            titulo = "Configuración del mosaico",
            subtitulo = "Comportamiento del tile en la barra de notificaciones",
            ruta = "Herramientas > Mosaico rápido",
            grupo = "Herramientas",
            pantallaDestino = Pantalla.TileConfiguracion(),
            padreId = "04-HER-MSK",
            palabrasClave = listOf("tile", "mosaico", "notificaciones", "ajustes rapidos", "barra")
        ),

        // ==========================================
        // Hijos de Copia de seguridad (05-COP-MAN)
        // ==========================================
        NodoAjuste(
            id = "05-COP-MAN-FIL",
            titulo = "Copia manual",
            subtitulo = "Exportar bóveda cifrada a archivo o restaurar",
            ruta = "Copias y datos > Copia de seguridad",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.CopiaManual(),
            padreId = "05-COP-MAN",
            palabrasClave = listOf("copia", "manual", "exportar", "restaurar", "archivo", "bvda")
        ),
        NodoAjuste(
            id = "05-COP-AUT-FIL",
            titulo = "Copia automática local",
            subtitulo = "Respaldos periódicos automáticos en almacenamiento local",
            ruta = "Copias y datos > Copia de seguridad",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.CopiaAutomaticaLocalSub(),
            padreId = "05-COP-MAN",
            palabrasClave = listOf("copia", "automatica", "local", "periodica", "diaria", "semanal")
        ),
        NodoAjuste(
            id = "05-COP-REC-FIL",
            titulo = "Recordatorios de copia",
            subtitulo = "Avisos de advertencia si no se realizan copias periódicas",
            ruta = "Copias y datos > Copia de seguridad",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.CopiaRecordatorios(),
            padreId = "05-COP-MAN",
            palabrasClave = listOf("recordatorios", "avisos", "notificaciones", "frecuencia", "alertas")
        ),
        NodoAjuste(
            id = "05-COP-EXP",
            titulo = "Exportación selectiva",
            subtitulo = "Elegir qué cuentas incluir en la copia exportada",
            ruta = "Copias y datos > Copia de seguridad > Copia manual",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.ExportarSelectivo(),
            padreId = "05-COP-MAN-FIL",
            palabrasClave = listOf("exportacion", "selectiva", "cuentas", "categorias", "filtro")
        ),
        NodoAjuste(
            id = "05-COP-ATM-G01",
            titulo = "Frecuencia y retención automática",
            subtitulo = "Configurar periodicidad y número máximo de copias",
            ruta = "Copias y datos > Copia de seguridad > Copia automática",
            grupo = "Copias y datos",
            pantallaDestino = Pantalla.AjustesCopiaAutomatica(),
            padreId = "05-COP-AUT-FIL",
            palabrasClave = listOf("frecuencia", "retencion", "rotacion", "limite", "copias")
        ),

        // ==========================================
        // Hijos de Opciones avanzadas (06-SIS-AVZ)
        // ==========================================
        NodoAjuste(
            id = "06-SIS-AVZ-DES",
            titulo = "Modo desarrollador",
            subtitulo = "Opciones para depuración y desarrolladores",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.AvanzadaDesarrollo(),
            padreId = "06-SIS-AVZ",
            palabrasClave = listOf("desarrollo", "depuracion", "developer", "debug", "logs")
        ),
        NodoAjuste(
            id = "06-SIS-AVZ-LGT",
            titulo = "Alumbrado y resaltado",
            subtitulo = "Efectos luminosos al navegar entre opciones",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.AvanzadaAlumbrado(),
            padreId = "06-SIS-AVZ",
            palabrasClave = listOf("alumbrado", "resaltado", "glow", "brillo", "destello")
        ),
        NodoAjuste(
            id = "06-SIS-AVZ-ORG",
            titulo = "Reorganizar ajustes",
            subtitulo = "Personalizar el orden de las opciones arrastrando",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.ReorganizarAjustes,
            padreId = "06-SIS-AVZ",
            palabrasClave = listOf("reorganizar", "orden", "arrastrar", "mover", "personalizar", "filas")
        ),
        NodoAjuste(
            id = "06-SIS-AVZ-HAP",
            titulo = "Respuesta háptica",
            subtitulo = "Vibración táctil al pulsar botones y acciones",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.AvanzadaHaptica(),
            padreId = "06-SIS-AVZ",
            palabrasClave = listOf("haptica", "vibracion", "tactil", "feedback")
        ),
        NodoAjuste(
            id = "06-SIS-AVZ-PEL",
            titulo = "Zona de peligro",
            subtitulo = "Restablecimiento de fábrica y borrado completo",
            ruta = "Sistema > Opciones avanzadas",
            grupo = "Sistema",
            pantallaDestino = Pantalla.AvanzadaZonaPeligro(),
            padreId = "06-SIS-AVZ",
            palabrasClave = listOf("peligro", "restablecer", "fabrica", "borrar todo", "reset")
        ),
        NodoAjuste(
            id = "06-SIS-AVZ-COL",
            titulo = "Colores de identificadores",
            subtitulo = "Personalizar colores de las etiquetas de cada bloque",
            ruta = "Sistema > Opciones avanzadas > Modo desarrollador",
            grupo = "Sistema",
            pantallaDestino = Pantalla.ColoresIdentificadores(),
            padreId = "06-SIS-AVZ-DES",
            palabrasClave = listOf("colores", "identificadores", "ids", "bloques", "hex")
        )
    )

    val TODOS_LOS_NODOS: List<NodoAjuste> by lazy {
        NODOS_RAIZ + SUBNODOS
    }

    /**
     * Devuelve las opciones raíz respetando el orden personalizado si existe y el reparenting personalizado.
     */
    fun obtenerNodosRaiz(
        ordenPersonalizado: List<String> = emptyList(),
        reparenting: Map<String, String> = emptyMap()
    ): List<NodoAjuste> {
        val raices = TODOS_LOS_NODOS.filter { nodo ->
            val padreEfectivo = reparenting[nodo.id] ?: nodo.padreId ?: "00-AJU"
            padreEfectivo == "00-AJU"
        }
        if (ordenPersonalizado.isEmpty()) return raices
        val mapa = raices.associateBy { it.id }
        val ordenados = ordenPersonalizado.mapNotNull { mapa[it] }
        val noOrdenados = raices.filterNot { ordenPersonalizado.contains(it.id) }
        return ordenados + noOrdenados
    }

    /**
     * Devuelve los subnodos de una pantalla padre dada, considerando reparenting y orden personalizado.
     */
    fun obtenerHijosDe(
        padreId: String,
        ordenPersonalizado: Map<String, List<String>> = emptyMap(),
        reparenting: Map<String, String> = emptyMap()
    ): List<NodoAjuste> {
        val base = TODOS_LOS_NODOS.filter { nodo ->
            val padreEfectivo = reparenting[nodo.id] ?: (nodo.padreId ?: "00-AJU")
            padreEfectivo == padreId
        }
        val orden = ordenPersonalizado[padreId] ?: emptyList()
        if (orden.isEmpty()) return base
        val mapa = base.associateBy { it.id }
        val ordenados = orden.mapNotNull { mapa[it] }
        val noOrdenados = base.filterNot { orden.contains(it.id) }
        return ordenados + noOrdenados
    }

    /**
     * Devuelve la lista de nodos contenedores elegibles para reparentar (pantallas secundarias o raíz).
     */
    fun obtenerNodosContenedores(): List<NodoAjuste> {
        val idsConHijos = TODOS_LOS_NODOS.mapNotNull { it.padreId }.toSet()
        return TODOS_LOS_NODOS.filter { it.esRaiz || idsConHijos.contains(it.id) || it.pantallaDestino != null }
    }

    /**
     * Búsqueda en todos los nodos (raíz y subniveles).
     */
    fun buscarNodos(query: String): List<NodoAjuste> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        val palabras = q.split("\\s+".toRegex()).filter { it.isNotBlank() }
        return TODOS_LOS_NODOS.filter { nodo ->
            val corpus = "${nodo.titulo} ${nodo.subtitulo} ${nodo.ruta} ${nodo.id} ${nodo.palabrasClave.joinToString(" ")}".lowercase()
            palabras.all { corpus.contains(it) }
        }
    }

    /**
     * Busca un nodo por su ID.
     */
    fun buscarPorId(id: String): NodoAjuste? = TODOS_LOS_NODOS.firstOrNull { it.id == id }

    /**
     * Resuelve el icono y color representativo de un nodo (para el Hub o búsqueda)
     * a partir de su definición, asignaciones específicas o herencia de ancestro.
     */
    fun resolverIconoYColor(nodo: NodoAjuste): Pair<ImageVector, Color> {
        if (nodo.icono != null && nodo.colorIcono != null) {
            return nodo.icono to nodo.colorIcono
        }
        val iconoDedicado: Pair<ImageVector, Color>? = when (nodo.id) {
            "06-SIS-AVZ-DES" -> Icons.Filled.Build to Color(0xFFC2185B)
            "06-SIS-AVZ-COL" -> Icons.Filled.Palette to Color(0xFF00ACC1)
            "06-SIS-AVZ-LGT" -> Icons.Filled.AutoAwesome to Color(0xFFFFB300)
            "06-SIS-AVZ-ORG" -> Icons.AutoMirrored.Filled.Sort to Color(0xFF8B5CF6)
            "06-SIS-AVZ-HAP" -> Icons.Filled.Tune to Color(0xFF00897B)
            "06-SIS-AVZ-PEL" -> Icons.Filled.DeleteForever to Color(0xFFD32F2F)
            else -> null
        }
        if (iconoDedicado != null) return iconoDedicado

        var actual: NodoAjuste? = nodo
        while (actual?.padreId != null) {
            actual = buscarPorId(actual.padreId!!)
            if (actual?.icono != null && actual.colorIcono != null) {
                return actual.icono to actual.colorIcono
            }
        }
        return when (nodo.grupo) {
            "Seguridad" -> Icons.Filled.Fingerprint to Color(0xFF1E88E5)
            "Apariencia" -> Icons.Filled.Palette to Color(0xFF8E24AA)
            "Lista de cuentas" -> Icons.Filled.Layers to Color(0xFF00ACC1)
            "Herramientas" -> Icons.Filled.Tune to Color(0xFF8B5CF6)
            "Copias y datos" -> Icons.Filled.Backup to Color(0xFF43A047)
            else -> Icons.Filled.Tune to Color(0xFFC2185B)
        }
    }
}
