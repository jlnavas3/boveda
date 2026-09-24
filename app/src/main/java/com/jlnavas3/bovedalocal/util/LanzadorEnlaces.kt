package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object LanzadorEnlaces {

    private val tldsWeb = setOf(
        "com", "org", "net", "edu", "gov", "mil", "int", "eu", "ar", "es", "mx", "co", "pe", "cl",
        "ec", "ve", "uy", "br", "uk", "de", "fr", "it", "ca", "us", "cn", "jp", "ru", "in", "info",
        "biz", "pro", "xyz", "online", "site", "website", "tech", "store", "shop", "blog", "app",
        "dev", "io", "ai", "tv", "me", "cc", "ws", "to", "ly", "local", "lan"
    )

    private val prefijosPaqueteComunes = setOf(
        "com", "org", "net", "edu", "gov", "mil", "android", "google", "io", "es", "de", "fr", "uk",
        "jp", "br", "ru", "in", "app", "me", "tech", "dev", "pl", "us", "ch", "nl", "se", "no", "fi"
    )

    /** Detecta si un texto parece un nombre de paquete Android válido (e.g. com.whatsapp, com.brakefield.painter). */
    fun esNombrePaquete(texto: String): Boolean {
        val limpio = texto.trim()
        if (limpio.contains("/") || limpio.contains(":") || limpio.contains("?") || limpio.contains(" ") || limpio.contains("@")) {
            return false
        }
        val partes = limpio.split('.')
        if (partes.size < 2) return false
        for (p in partes) {
            if (p.isEmpty() || (!p[0].isLetter() && p[0] != '_')) return false
            if (!p.all { it.isLetterOrDigit() || it == '_' }) return false
        }

        val primerParte = partes.first().lowercase()
        val ultimaParte = partes.last().lowercase()

        // Si tiene 2 partes y la última es un TLD web común (ej: github.com, google.es), es un dominio web
        if (partes.size == 2 && tldsWeb.contains(ultimaParte) && !tldsWeb.contains(primerParte)) {
            return false
        }

        // Si tiene 2 partes (ej: com.whatsapp), la primera parte debe ser un prefijo de paquete
        if (partes.size == 2) {
            return prefijosPaqueteComunes.contains(primerParte) && !tldsWeb.contains(ultimaParte)
        }

        // Para 3 o más partes (ej: com.brakefield.painter, org.mozilla.firefox):
        // Si termina en un TLD web y no empieza con un prefijo típico de paquete, es un subdominio web (ej: api.github.com)
        if (tldsWeb.contains(ultimaParte) && !prefijosPaqueteComunes.contains(primerParte)) {
            return false
        }

        return true
    }

    /**
     * Extrae el nombre de paquete de cualquier formato de credencial Android:
     * - 'https://android//<hash>@<package>/' (Google Password Manager / Autofill / Passkeys)
     * - 'android://<hash>@<package>/' (Bitwarden / Chrome Autofill)
     * - 'androidapp://<package>' o 'app://<package>'
     * - 'package:<package>'
     * - '<package>' directo (e.g. 'com.brakefield.painter')
     */
    fun extraerPaquete(texto: String): String? {
        val limpio = texto.trim()
        if (limpio.isBlank()) return null

        // Caso 1: Enlaces con esquema android explícito
        val esEsquemaAndroid = limpio.startsWith("android://", ignoreCase = true) ||
            limpio.startsWith("https://android//", ignoreCase = true) ||
            limpio.startsWith("http://android//", ignoreCase = true) ||
            limpio.startsWith("android-app://", ignoreCase = true) ||
            limpio.startsWith("androidapp://", ignoreCase = true) ||
            limpio.startsWith("app://", ignoreCase = true) ||
            limpio.startsWith("package:", ignoreCase = true)

        if (esEsquemaAndroid) {
            val despuesDeArroba = if (limpio.contains('@')) {
                limpio.substringAfterLast('@')
            } else {
                limpio
                    .removePrefix("https://android//")
                    .removePrefix("http://android//")
                    .removePrefix("android://")
                    .removePrefix("android-app://")
                    .removePrefix("androidapp://")
                    .removePrefix("app://")
                    .removePrefix("package:")
                    .removePrefix("//")
            }
            val candidato = despuesDeArroba
                .substringBefore('/')
                .substringBefore('?')
                .substringBefore('#')
                .substringBefore(':')
                .trim()
            if (candidato.isNotBlank()) {
                if (candidato.split('.').size >= 2) return candidato
                if (esNombrePaquete(candidato)) return candidato
            }
        }

        // Caso 2: Cualquier formato con hash@paquete (ej: DAL Digital Asset Links)
        if (limpio.contains('@')) {
            val candidato = limpio
                .substringAfterLast('@')
                .substringBefore('/')
                .substringBefore('?')
                .substringBefore('#')
                .substringBefore(':')
                .trim()
            if (candidato.split('.').size >= 2 && esNombrePaquete(candidato)) {
                return candidato
            }
        }

        // Caso 3: Nombre de paquete directo
        if (esNombrePaquete(limpio)) {
            return limpio
        }

        return null
    }

    /** Comprueba si el paquete de la aplicación está instalado en el dispositivo. */
    fun estaInstalada(contexto: Context, paquete: String): Boolean {
        return try {
            val intent = contexto.packageManager.getLaunchIntentForPackage(paquete)
            intent != null
        } catch (e: Exception) {
            false
        }
    }

    /** Obtiene el nombre amigable de la aplicación si está instalada. */
    fun obtenerNombreApp(contexto: Context, paquete: String): String? {
        return try {
            val pm = contexto.packageManager
            val appInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.getApplicationInfo(paquete, PackageManager.ApplicationInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getApplicationInfo(paquete, 0)
            }
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            null
        }
    }

    /** Añade https:// si el usuario ingresó un dominio sin esquema. */
    fun normalizarUrlWeb(texto: String): String {
        val limpio = texto.trim()
        if (limpio.contains("://")) return limpio
        return if (!limpio.startsWith("http://", ignoreCase = true) &&
            !limpio.startsWith("https://", ignoreCase = true)
        ) {
            "https://$limpio"
        } else {
            limpio
        }
    }

    /**
     * Abre el enlace en el navegador predeterminado o la app si está instalada (o Play Store si no).
     * No requiere permisos de red en Bóveda Local ya que delega la acción al sistema mediante Intents.
     */
    fun abrir(contexto: Context, entradaUrl: String, onAviso: (String) -> Unit = {}) {
        val limpio = entradaUrl.trim()
        if (limpio.isBlank()) return

        // Caso directo para esquemas de tiendas de aplicaciones (market://)
        if (limpio.startsWith("market://", ignoreCase = true)) {
            val intentMarket = Intent(Intent.ACTION_VIEW, Uri.parse(limpio)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                contexto.startActivity(intentMarket)
                Diagnostico.apuntar("enlaces", "Abriendo tienda de aplicaciones: $limpio")
                return
            } catch (e: Exception) {
                val urlFallback = if (limpio.contains("search?q=", ignoreCase = true)) {
                    val consulta = limpio.substringAfter("search?q=")
                    "https://play.google.com/store/search?q=$consulta&c=apps"
                } else if (limpio.contains("details?id=", ignoreCase = true)) {
                    val idPkg = limpio.substringAfter("details?id=")
                    "https://play.google.com/store/apps/details?id=$idPkg"
                } else {
                    "https://play.google.com/store"
                }
                try {
                    val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse(urlFallback)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    contexto.startActivity(intentWeb)
                    return
                } catch (e2: Exception) {
                    onAviso("No se encontró tienda de aplicaciones para abrir: $limpio")
                    return
                }
            }
        }

        val paquete = extraerPaquete(limpio)
        if (paquete != null) {
            // 1. Si está instalada, abrir la aplicación
            val intentApp = try {
                contexto.packageManager.getLaunchIntentForPackage(paquete)
            } catch (e: Exception) {
                null
            }
            if (intentApp != null) {
                intentApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    contexto.startActivity(intentApp)
                    Diagnostico.apuntar("enlaces", "Abriendo app instalada: $paquete")
                    return
                } catch (e: Exception) {
                    Diagnostico.apuntar("enlaces", "Fallo al iniciar actividad de $paquete: ${e.message}")
                }
            }

            // 2. Si no está instalada, abrir en Google Play Store
            Diagnostico.apuntar("enlaces", "App no instalada. Abriendo Play Store para: $paquete")
            val intentMarket = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$paquete")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                contexto.startActivity(intentMarket)
                return
            } catch (e: Exception) {
                // Si el dispositivo no tiene app de Google Play, abrir vía web en navegador
                val intentWebPlay = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$paquete")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    contexto.startActivity(intentWebPlay)
                    return
                } catch (e2: Exception) {
                    onAviso("No se encontró tienda ni aplicación para abrir $paquete")
                }
            }
            return
        }

        // 3. Es una URL web: abrir en el navegador predeterminado
        val urlFinal = normalizarUrlWeb(limpio)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFinal)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            contexto.startActivity(intent)
            Diagnostico.apuntar("enlaces", "Abriendo URL en navegador: $urlFinal")
        } catch (e: Exception) {
            Diagnostico.apuntar("enlaces", "Fallo al abrir URL $urlFinal: ${e.message}")
            onAviso("No se pudo abrir el navegador para: $urlFinal")
        }
    }

    /** Extrae el hash de certificado criptográfico de un URI Digital Asset Links de Android. */
    fun extraerHashCertificado(texto: String): String? {
        val limpio = texto.trim()
        if (!limpio.contains('@')) return null
        val antesDeArroba = limpio.substringBeforeLast('@')
        val hash = antesDeArroba
            .substringAfter("://", antesDeArroba)
            .removePrefix("android//")
            .removePrefix("android-app//")
            .removePrefix("//")
            .trim()
        return if (hash.isNotBlank() && hash.length >= 10) hash else null
    }

    /** Descompone una URL cruda para mostrarla limpia y editable en el formulario. */
    fun desglosarParaEdicion(url: String): EnlaceEditable {
        val limpio = url.trim()
        val paquete = extraerPaquete(limpio)
        if (paquete != null && limpio.contains('@')) {
            val hash = extraerHashCertificado(limpio)
            val esquema = when {
                limpio.startsWith("https://android//", ignoreCase = true) -> "https://android//"
                limpio.startsWith("http://android//", ignoreCase = true) -> "http://android//"
                limpio.startsWith("android://", ignoreCase = true) -> "android://"
                else -> "https://android//"
            }
            return EnlaceEditable(
                valor = paquete,
                hashOriginal = hash,
                esquemaOriginal = esquema
            )
        }
        return EnlaceEditable(valor = limpio)
    }

    /** Reconstruye el formato original al guardar conservando el hash DAL si corresponde. */
    fun reconstruirDesdeEdicion(enlace: EnlaceEditable): String {
        val valorLimpio = enlace.valor.trim()
        if (valorLimpio.isBlank()) return ""
        if (enlace.hashOriginal != null && esNombrePaquete(valorLimpio)) {
            val esquema = enlace.esquemaOriginal ?: "https://android//"
            return "$esquema${enlace.hashOriginal}@$valorLimpio/"
        }
        return valorLimpio
    }
}

/** Representa un enlace o app en el formulario de edición con su hash DAL original si existía. */
data class EnlaceEditable(
    val id: String = java.util.UUID.randomUUID().toString(),
    var valor: String,
    val hashOriginal: String? = null,
    val esquemaOriginal: String? = null
)
