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
            subtitulo = "Comandos rápidos (t:, tag:, cat:, has:, is:) y chips interactivos",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("busqueda", "filtros", "sintaxis", "comandos", "chips", "totp", "tags", "encontrar")
        ),
        ArticuloAyuda(
            id = "identidades",
            titulo = "Identidades y perfiles",
            subtitulo = "Separación de credenciales por ámbito (Personal, Trabajo, etc.) y chips",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("identidades", "perfiles", "trabajo", "personal", "cuentas", "asociar")
        ),
        ArticuloAyuda(
            id = "categorias",
            titulo = "Categorías personalizadas",
            subtitulo = "Creación, edición, colores e iconos para organizar entradas",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("categorias", "grupos", "colores", "iconos", "clasificar", "secciones")
        ),
        ArticuloAyuda(
            id = "etiquetas",
            titulo = "Etiquetas (Tags)",
            subtitulo = "Etiquetado libre transversal y autocompletado en edición",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("etiquetas", "tags", "palabras", "clasificacion", "filtrar")
        ),
        ArticuloAyuda(
            id = "ordenacion",
            titulo = "Criterios de ordenación y grupos",
            subtitulo = "Orden alfabético, por uso, favoritos y agrupamiento por sitio web",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("orden", "ordenar", "alfabetico", "favoritos", "frecuente", "agrupar", "sitio")
        ),
        ArticuloAyuda(
            id = "campos_personalizados",
            titulo = "Campos personalizados y tipos ricos",
            subtitulo = "Interruptores Booleanos (Sí/No), PIN, fechas, formatos y plantillas reutilizables",
            grupo = "Búsqueda y organización",
            palabrasClave = listOf("campos", "personalizados", "booleano", "switch", "pin", "fecha", "plantillas", "oculto")
        ),

        // ==========================================
        // 2. HERRAMIENTAS INTEGRADAS
        // ==========================================
        ArticuloAyuda(
            id = "generador",
            titulo = "Generador de contraseñas y Diceware",
            subtitulo = "Contraseñas aleatorias seguras, frases memorables y entropía",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("generador", "crear", "contrasena", "diceware", "frases", "segura", "entropia")
        ),
        ArticuloAyuda(
            id = "claves_ssh",
            titulo = "Generador de claves SSH para servidores",
            subtitulo = "Pares Ed25519, RSA, ECDSA, claves OpenSSH, PEM y huellas digitales SHA-256",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("ssh", "claves", "servidor", "ed25519", "rsa", "ecdsa", "pem", "openssh", "huella", "fingerprint")
        ),
        ArticuloAyuda(
            id = "adjuntos",
            titulo = "Archivos adjuntos cifrados (.bvda)",
            subtitulo = "Almacenamiento cifrado de archivos (hasta 2 MB), visor offline y exportación SAF",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("adjuntos", "archivos", "fotos", "documentos", "pdf", "saf", "exportar", "visor", "2mb", "cifrado")
        ),
        ArticuloAyuda(
            id = "alias_correo",
            titulo = "Generador de alias de correo (@)",
            subtitulo = "Subdireccionamiento RFC 5233 y alias aleatorios para combatir el spam",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("alias", "correo", "email", "mascara", "rfc5233", "spam", "arroba")
        ),
        ArticuloAyuda(
            id = "autenticador",
            titulo = "Autenticador 2FA / TOTP integrado",
            subtitulo = "Códigos temporales de 6 dígitos con cuenta regresiva y algoritmos seguros",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("totp", "2fa", "autenticador", "segundo factor", "codigos", "verificacion")
        ),
        ArticuloAyuda(
            id = "escaner",
            titulo = "Escáner QR de códigos 2FA",
            subtitulo = "Captura inmediata con visor, linterna, zoom o ingreso manual",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("escaner", "qr", "camara", "lector", "captura", "linterna")
        ),
        ArticuloAyuda(
            id = "passkeys",
            titulo = "Llaves de paso (Passkeys / FIDO2)",
            subtitulo = "Inicio de sesión biométrico sin contraseñas con el estándar WebAuthn",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("passkeys", "llaves", "fido2", "webauthn", "biometria", "sin contrasena", "cxf")
        ),
        ArticuloAyuda(
            id = "historial",
            titulo = "Historial de contraseñas",
            subtitulo = "Consulta de versiones anteriores y recuperación de claves modificadas",
            grupo = "Herramientas integradas",
            palabrasClave = listOf("historial", "versiones", "anteriores", "recuperar", "cambios", "auditoria")
        ),

        // ==========================================
        // 3. SALUD, AUDITORÍA Y PRIVACIDAD
        // ==========================================
        ArticuloAyuda(
            id = "salud",
            titulo = "Salud de la bóveda e insignia global",
            subtitulo = "Detección de vulnerabilidades, claves débiles, reutilizadas o sin 2FA",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("salud", "auditoria", "escudo", "vulnerabilidades", "debiles", "seguridad")
        ),
        ArticuloAyuda(
            id = "duplicados",
            titulo = "Detección de credenciales duplicadas",
            subtitulo = "Identificación de contraseñas idénticas entre diferentes servicios",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("duplicados", "repetidas", "reutilizadas", "mismo usuario", "brechas")
        ),
        ArticuloAyuda(
            id = "ignorados",
            titulo = "Ignorar en auditoría",
            subtitulo = "Exclusión intencionada de cuentas secundarias, locales o de prueba",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("ignorar", "excluir", "auditoria", "salud", "prueba", "secundarias")
        ),
        ArticuloAyuda(
            id = "papelera",
            titulo = "Papelera de reciclaje y retención",
            subtitulo = "Recuperación de elementos borrados, vaciado y políticas de auto-purga",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("papelera", "reciclaje", "borrado", "eliminar", "restaurar", "purga")
        ),
        ArticuloAyuda(
            id = "autodestruccion",
            titulo = "Autodestrucción de emergencia",
            subtitulo = "Borrado seguro de datos ante intentos reiterados de intrusión física",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("autodestruccion", "peligro", "intentos", "bloqueo", "borrado", "ataque")
        ),
        ArticuloAyuda(
            id = "senuelo",
            titulo = "Bóveda señuelo bajo coacción",
            subtitulo = "Contraseña secundaria para mostrar una bóveda señuelo ficticia",
            grupo = "Salud, auditoría y privacidad",
            palabrasClave = listOf("senuelo", "coaccion", "duress", "ficticia", "falsa", "amenaza")
        ),

        // ==========================================
        // 4. SISTEMA, SEGURIDAD Y CONFIGURACIÓN
        // ==========================================
        ArticuloAyuda(
            id = "seguridad",
            titulo = "Seguridad biométrica y bloqueo",
            subtitulo = "Huella dactilar, desbloqueo facial, tiempo de inactividad y portapapeles",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("biometria", "huella", "rostro", "inactividad", "tiempo", "bloqueo", "portapapeles")
        ),
        ArticuloAyuda(
            id = "copias",
            titulo = "Copias de seguridad cifradas y Dry-Run",
            subtitulo = "Exportación .bvda con compresión BLZ1, inspección previa y copias automáticas",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("copia", "respaldo", "bvda", "boveda", "blz1", "compresion", "dry-run", "exportar", "restaurar")
        ),
        ArticuloAyuda(
            id = "kit_emergencia",
            titulo = "Kit de emergencia imprimible",
            subtitulo = "Ficha física de rescate con claves maestras e instrucciones offline",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("kit", "emergencia", "imprimir", "papel", "hoja", "rescate", "offline")
        ),
        ArticuloAyuda(
            id = "importacion",
            titulo = "Importación desde Google Passwords y CSV",
            subtitulo = "Migración sencilla desde navegadores web y gestores externos",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("importar", "google", "chrome", "csv", "passwords", "migracion")
        ),
        ArticuloAyuda(
            id = "apariencia",
            titulo = "Personalización, temas y tipografías",
            subtitulo = "Laboratorio de temas, curvatura de esquinas, estilos de borde y fuentes",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("apariencia", "temas", "colores", "formas", "bordes", "tipografia", "esquinas")
        ),
        ArticuloAyuda(
            id = "widgets",
            titulo = "Widgets de escritorio y accesos rápidos",
            subtitulo = "Widget 1x1, visualizador TOTP dinámico y Quick Settings Tile",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("widgets", "acceso directo", "pantalla de inicio", "tile", "notificaciones")
        ),
        ArticuloAyuda(
            id = "autocompletado",
            titulo = "Servicio de Autocompletado del sistema",
            subtitulo = "Relleno automático seguro de usuario y contraseña en apps y navegadores",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("autocompletado", "autofill", "rellenar", "navegador", "servicio", "accesibilidad")
        ),
        ArticuloAyuda(
            id = "arquitectura",
            titulo = "Arquitectura Zero-Knowledge y offline",
            subtitulo = "Garantía de privacidad absoluta: cero internet, cero telemetría y AES-256",
            grupo = "Sistema y ajustes",
            palabrasClave = listOf("arquitectura", "privacidad", "zero-knowledge", "offline", "cifrado", "aes", "gplv3")
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
