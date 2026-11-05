/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

/**
 * Catálogo inmutable de todos los temas de ayuda del sistema clasificados por categorías.
 */
object CatalogoArticulosAyuda {

    val TODOS: List<ArticuloAyuda> = listOf(
        // ==========================================
        // 1. BÚSQUEDA Y ORGANIZACIÓN
        // ==========================================
        ArticuloAyuda(
            id = "busqueda",
            titulo = "Búsqueda avanzada por sintaxis",
            subtitulo = "Comandos rápidos (t:, tag:, cat:, has:, is:), píldoras y sugerencias inteligentes",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("busqueda", "filtros", "sintaxis", "comandos", "chips", "pildoras", "2fa", "totp", "tags", "encontrar", "sugerencias", "operadores", "has:totp", "has:passkey", "is:fav", "is:ignorado")
        ),
        ArticuloAyuda(
            id = "identidades",
            titulo = "Identidades y perfiles",
            subtitulo = "Ficha de autocompletado y pagos (tarjetas, cuentas, direcciones) y paleta de 19 colores",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("identidades", "perfiles", "trabajo", "personal", "cuentas", "asociar", "pagos", "tarjetas", "envios", "documentos", "colores", "fiscal", "curp", "rfc", "iban", "pasaporte", "avatares")
        ),
        ArticuloAyuda(
            id = "tipos_entrada",
            titulo = "Tipos de entradas y datos nativos",
            subtitulo = "Tarjetas bancarias, cuentas IBAN, identificaciones, Wi-Fi, servidores, cripto y notas",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("tipos", "tarjetas", "cuentas", "iban", "wifi", "servidores", "wallets", "cripto", "identidad", "dni", "pasaporte", "contactos", "luhn", "swift", "bic", "wpa2", "wpa3", "bip39", "semilla")
        ),
        ArticuloAyuda(
            id = "normalizador_titulos",
            titulo = "Normalización de títulos y reglas de URLs",
            subtitulo = "Limpieza de URLs caóticas, TLDs descartables, dominios homelab y marcas reconocidas",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("normalizador", "titulos", "urls", "dominios", "subdominios", "marcas", "tld", "homelab", "router", "puertos", "subrutas", "ip", "pihole", "proxmox", "portainer")
        ),
        ArticuloAyuda(
            id = "notas_markdown",
            titulo = "Notas seguras y formato Markdown",
            subtitulo = "Tablas estructuradas, texto enriquecido, listas de tareas y bloques de código cifrados",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("notas", "markdown", "tablas", "formato", "negrita", "listas", "codigo", "cifrado", "seguras", "encabezados", "tareas", "syntax", "previsualizacion")
        ),
        ArticuloAyuda(
            id = "categorias",
            titulo = "Categorías personalizadas",
            subtitulo = "Creación, edición, colores e iconos temáticos para estructurar la bóveda",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("categorias", "grupos", "colores", "iconos", "clasificar", "secciones", "taxonomia", "carpetas", "reasignacion", "cat:")
        ),
        ArticuloAyuda(
            id = "etiquetas",
            titulo = "Etiquetas (Tags) transversales",
            subtitulo = "Etiquetado multidimensional libre, autocompletado inteligente y filtros #tag",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("etiquetas", "tags", "palabras", "clasificacion", "filtrar", "transversal", "tag:", "hashtag", "chips", "autocompletado")
        ),
        ArticuloAyuda(
            id = "ordenacion",
            titulo = "Criterios de ordenación y grupos",
            subtitulo = "Orden alfabético, por uso, favoritos y agrupamiento inteligente por sitio web",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("orden", "ordenar", "alfabetico", "favoritos", "frecuente", "agrupar", "sitio", "recientes", "frecuencia", "dominios", "is:fav")
        ),
        ArticuloAyuda(
            id = "campos_personalizados",
            titulo = "Campos personalizados y tipos ricos",
            subtitulo = "Interruptores Booleanos (Sí/No), PIN, fechas, formatos y plantillas reutilizables",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("campos", "personalizados", "booleano", "switch", "pin", "fecha", "plantillas", "oculto", "mascara", "formatos")
        ),

        // ==========================================
        // 2. HERRAMIENTAS INTEGRADAS
        // ==========================================
        ArticuloAyuda(
            id = "generador",
            titulo = "Generador de contraseñas y frases Diceware",
            subtitulo = "Contraseñas aleatorias fuertes, frases de palabras, medidor de entropía y tiempo de crackeo",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("generador", "crear", "contrasena", "diceware", "frases", "segura", "entropia", "bits", "crackeo", "longitud", "simbolos")
        ),
        ArticuloAyuda(
            id = "diccionarios_comunes",
            titulo = "Diccionarios Diceware y contraseñas comunes",
            subtitulo = "Vocabulario Diceware personalizado y comprobación offline contra lista negra de brechas",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("diccionario", "palabras", "diceware", "comunes", "blacklist", "vulnerables", "brechas", "seguridad", "offline", "rockyou", "filtradas")
        ),
        ArticuloAyuda(
            id = "claves_ssh",
            titulo = "Generador de claves SSH para servidores",
            subtitulo = "Pares asimétricos Ed25519, RSA, ECDSA, formatos OpenSSH/PEM y huellas SHA-256",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("ssh", "claves", "servidor", "ed25519", "rsa", "ecdsa", "pem", "openssh", "huella", "fingerprint", "sha256", "terminal")
        ),
        ArticuloAyuda(
            id = "adjuntos",
            titulo = "Archivos adjuntos cifrados en base de datos",
            subtitulo = "Almacenamiento cifrado de archivos (hasta 2 MB), visor offline seguro y exportación SAF",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("adjuntos", "archivos", "fotos", "documentos", "pdf", "saf", "exportar", "visor", "2mb", "cifrado", "sqlcipher")
        ),
        ArticuloAyuda(
            id = "alias_correo",
            titulo = "Generador de alias de correo y subdireccionamiento",
            subtitulo = "Subdireccionamiento RFC 5233 y alias aleatorios para combatir el spam",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("alias", "correo", "email", "mascara", "rfc5233", "spam", "arroba", "subdireccionamiento", "plus addressing", "aleatorio")
        ),
        ArticuloAyuda(
            id = "autenticador",
            titulo = "Autenticador 2FA / TOTP integrado",
            subtitulo = "Códigos de 6 dígitos RFC 6238, soporte para múltiples TOTP secundarios y cuenta atrás",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("totp", "2fa", "autenticador", "segundo factor", "codigos", "verificacion", "secundario", "multiples", "mfa", "rfc6238", "sha1", "sha256", "sha512")
        ),
        ArticuloAyuda(
            id = "escaner",
            titulo = "Escáner QR de códigos 2FA y Base32",
            subtitulo = "Captura inmediata con visor regulable, linterna, zoom o ingreso manual Base32",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("escaner", "qr", "camara", "lector", "captura", "linterna", "zoom", "ventana", "base32", "semilla", "otpauth")
        ),
        ArticuloAyuda(
            id = "passkeys",
            titulo = "Llaves de acceso (Passkeys / FIDO2)",
            subtitulo = "Autenticación biométrica WebAuthn, algoritmos ES256/EdDSA y passkeys secundarias",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("passkeys", "llaves", "fido2", "webauthn", "biometria", "sin contrasena", "cxf", "aaguid", "es256", "eddsa", "anti-phishing", "rp id")
        ),
        ArticuloAyuda(
            id = "transferencia_cxf",
            titulo = "Intercambio de Passkeys (Estándar CXF)",
            subtitulo = "Protocolo oficial FIDO Alliance para migrar passkeys entre Google, Apple, Bitwarden y Bóveda",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("cxf", "fido", "transferencia", "migracion", "intercambio", "passkeys", "google", "apple", "bitwarden", "ecosistema", "exportar passkeys", "importar passkeys")
        ),
        ArticuloAyuda(
            id = "historial",
            titulo = "Historial de contraseñas y versiones",
            subtitulo = "Auditoría de versiones anteriores con fecha/hora, restauración inmediata y purga segura",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("historial", "versiones", "anteriores", "recuperar", "cambios", "auditoria", "restaurar", "purga", "registro")
        ),

        // ==========================================
        // 3. SALUD, AUDITORÍA Y PRIVACIDAD
        // ==========================================
        ArticuloAyuda(
            id = "salud",
            titulo = "Salud de la bóveda e inspección de seguridad",
            subtitulo = "Escudo de seguridad (0-100%), auditoría local de 5 vectores y resolución ágil de riesgos",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("salud", "auditoria", "escudo", "vulnerabilidades", "debiles", "seguridad", "reutilizadas", "antiguas", "sin 2fa", "comprometidas")
        ),
        ArticuloAyuda(
            id = "duplicados",
            titulo = "Detección de duplicados multimodelo",
            subtitulo = "Detección de contraseñas repetidas, tarjetas bancarias, cuentas IBAN e identificaciones",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("duplicados", "repetidas", "reutilizadas", "mismo usuario", "brechas", "pan", "iban", "documentos", "analisis")
        ),
        ArticuloAyuda(
            id = "unificacion",
            titulo = "Unificación y fusión inteligente sin pérdida",
            subtitulo = "Comparador campo a campo de discrepancias, combinación sin pérdidas y traslado a papelera",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("unificacion", "fusion", "discrepancias", "duplicados", "unificar", "resolver", "fusionar", "lossless", "combinar")
        ),
        ArticuloAyuda(
            id = "ignorados",
            titulo = "Exclusión intencionada en auditoría",
            subtitulo = "Marcado de cuentas de prueba o intranets sin penalizar el escudo de salud",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("ignorar", "excluir", "auditoria", "salud", "prueba", "secundarias", "intranet", "is:ignorado")
        ),
        ArticuloAyuda(
            id = "papelera",
            titulo = "Papelera de reciclaje y retención cifrada",
            subtitulo = "Borrado suave (Soft-Delete), restauración individual o en lote y vaciado permanente",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("papelera", "reciclaje", "borrado", "eliminar", "restaurar", "purga", "soft delete", "vaciar", "conflictos")
        ),
        ArticuloAyuda(
            id = "autodestruccion",
            titulo = "Autodestrucción y borrado de emergencia",
            subtitulo = "Protección física por exceso de intentos o PIN de coacción con sobreescritura de claves",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("autodestruccion", "peligro", "intentos", "bloqueo", "borrado", "ataque", "emergencia", "pin panico", "borrado seguro", "purga", "ataque fisico")
        ),
        ArticuloAyuda(
            id = "senuelo",
            titulo = "Bóveda señuelo bajo coacción",
            subtitulo = "Contraseña alternativa con denegación plausible y cuentas ficticias creíbles",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("senuelo", "coaccion", "duress", "ficticia", "falsa", "amenaza", "denegacion plausible", "boveda secundaria")
        ),

        // ==========================================
        // 4. SISTEMA, SEGURIDAD Y CONFIGURACIÓN
        // ==========================================
        ArticuloAyuda(
            id = "seguridad",
            titulo = "Seguridad biométrica, bloqueo y protecciones",
            subtitulo = "Biometría Clase 3 vs Compatible, inactividad, anti-tapjacking, FLAG_SECURE y fuerza bruta",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("biometria", "huella", "rostro", "inactividad", "tiempo", "bloqueo", "portapapeles", "tapjacking", "flag_secure", "fuerza bruta", "retraso incremental")
        ),
        ArticuloAyuda(
            id = "copias",
            titulo = "Copias de seguridad cifradas (.bvda) y Dry-Run",
            subtitulo = "Cifrado Argon2id + AES-256-GCM, compresión BLZ1, modo Dry-Run y recordatorios",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("copia", "respaldo", "bvda", "boveda", "blz1", "compresion", "dry-run", "exportar", "restaurar", "argon2id", "aes-gcm", "automaticas", "backup")
        ),
        ArticuloAyuda(
            id = "exportar_selectivo",
            titulo = "Exportación e importación selectiva",
            subtitulo = "Filtrado por categorías, tipos de entrada, perfiles, exclusión de adjuntos e historial",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("exportar", "selectivo", "parcial", "tipos", "categorias", "adjuntos", "historial", "transferir", "filtrado", "importar")
        ),
        ArticuloAyuda(
            id = "kit_emergencia",
            titulo = "Kit de emergencia imprimible (PDF)",
            subtitulo = "Hoja física de rescate con credenciales maestras para contingencias y cajas fuertes",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("kit", "emergencia", "imprimir", "papel", "hoja", "rescate", "offline", "pdf", "contingencia", "caja fuerte")
        ),
        ArticuloAyuda(
            id = "importacion",
            titulo = "Importación desde CSV y otros gestores",
            subtitulo = "Soporte para múltiples delimitadores (, ; tab |), credenciales vs contactos y desduplicación",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("importar", "google", "chrome", "csv", "passwords", "migracion", "importacion", "delimitador", "bitwarden", "keepass", "contactos")
        ),
        ArticuloAyuda(
            id = "menu_lateral",
            titulo = "Menú lateral y organización de ajustes",
            subtitulo = "Catálogo de accesos directos (+ / ✓), reorganización fractal drag & drop de ajustes",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("menu", "lateral", "personalizar", "ajustes", "cajon", "reordenar", "visibilidad", "accesos directos", "reorganizacion", "drag and drop", "reparenting")
        ),
        ArticuloAyuda(
            id = "apariencia",
            titulo = "Personalización, temas visuales y tipografías",
            subtitulo = "Laboratorio de temas, capas L1-L4, paletas, esquinas (0-32 dp), destellos y animaciones",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("apariencia", "temas", "colores", "formas", "bordes", "tipografia", "esquinas", "laboratorio", "glow", "animacion", "l1", "l2")
        ),
        ArticuloAyuda(
            id = "widgets",
            titulo = "Widgets de escritorio y accesos rápidos",
            subtitulo = "Widget 1x1 generador, widget dinámico TOTP protegido y Quick Settings Tile",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("widgets", "acceso directo", "pantalla de inicio", "tile", "notificaciones", "escritorio", "totp widget", "generador widget", "quick settings")
        ),
        ArticuloAyuda(
            id = "autocompletado",
            titulo = "Servicio de Autocompletado del sistema (Autofill)",
            subtitulo = "Relleno automático de credenciales, sugerencias inline en teclado, tarjetas y direcciones",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("autocompletado", "autofill", "rellenar", "navegador", "tarjetas", "compras", "envios", "direcciones", "totp", "teclado", "inline", "ime", "pagos", "heuristica")
        ),
        ArticuloAyuda(
            id = "arquitectura",
            titulo = "Arquitectura Zero-Knowledge y código abierto",
            subtitulo = "Garantía de privacidad absoluta: cero internet, cero telemetría, SQLCipher y licencia GPLv3",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("arquitectura", "privacidad", "zero-knowledge", "offline", "cifrado", "aes", "gplv3", "sin internet", "cero telemetria", "sqlcipher", "codigo abierto")
        )
    )

    fun agruparPorCategoria(): Map<String, List<ArticuloAyuda>> =
        TODOS.groupBy { it.grupo }

    fun buscar(consulta: String): List<ArticuloAyuda> {
        val limpia = consulta.trim().lowercase()
        if (limpia.isBlank()) return TODOS
        val tokens = limpia.split(Regex("\\s+")).filter { it.isNotBlank() }
        return TODOS.filter { art ->
            val corpus = "${art.titulo} ${art.subtitulo} ${art.grupo} ${art.palabrasClave.joinToString(" ")}".lowercase()
            tokens.all { corpus.contains(it) }
        }
    }
}
