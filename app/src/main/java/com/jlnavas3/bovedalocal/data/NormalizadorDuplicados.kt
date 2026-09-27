package com.jlnavas3.bovedalocal.data

import com.jlnavas3.bovedalocal.util.Dominios

object NormalizadorDuplicados {

    fun esAppAndroid(entrada: Entrada): Boolean {
        return entrada.urls.any { it.trim().startsWith("android://", ignoreCase = true) }
    }

    fun extraerPaqueteAndroid(url: String): String {
        val u = url.trim()
        if (!u.startsWith("android://", ignoreCase = true)) return ""
        val sinEsquema = u.substringAfter("://", u)
        val sinRuta = sinEsquema.substringBefore('/').substringBefore('?').substringBefore('#')
        val sinCredenciales = sinRuta.substringAfterLast('@')
        return sinCredenciales.substringBefore(':').trim().lowercase()
    }

    /**
     * Normaliza el servicio / sitio web o app de Android de la entrada:
     * - Si es App de Android, extrae el paquete o el dominio del título (ej. "disneyplus.com", "com.xiaomi.account").
     *   NUNCA agrupa diferentes apps bajo "android".
     * - Si es Web, extrae el dominio raíz registrable (ej. "google.com", "amazon.com").
     */
    fun normalizarServicio(entrada: Entrada): String {
        val urlAndroid = entrada.urls.firstOrNull { it.trim().startsWith("android://", ignoreCase = true) }
        if (urlAndroid != null) {
            val paquete = extraerPaqueteAndroid(urlAndroid)
            val tit = entrada.titulo.trim().lowercase()

            // Si el título es un dominio web o contiene punto (ej. "disneyplus.com", "account.xiaomi.com", "netflix.com")
            if (tit.contains('.') && !tit.contains(' ') && !tit.equals("android", ignoreCase = true)) {
                val raiz = Dominios.raiz(tit)
                if (raiz.isNotBlank()) return "app:$raiz"
            }

            // Si el paquete es conocido y tiene forma de paquete Java (ej. com.xiaomi.account -> xiaomi.com)
            if (paquete.isNotBlank()) {
                val domPaquete = Dominios.dominioDePaquete(paquete)
                val raizPaquete = Dominios.raiz(domPaquete)
                if (raizPaquete.isNotBlank() && raizPaquete.contains('.')) return "app:$raizPaquete"
                return "app:$paquete"
            }

            // Si el título es el nombre propio de la app (ej. "Oral-B", "Solid Explorer File Manager")
            if (tit.isNotBlank() && !tit.equals("android", ignoreCase = true)) {
                return "app:$tit"
            }
        }

        // Si tiene URL Web tradicional:
        val urlWeb = entrada.urls.firstOrNull { !it.trim().startsWith("android://", ignoreCase = true) }
        if (!urlWeb.isNullOrBlank()) {
            val raiz = Dominios.raiz(urlWeb)
            if (raiz.isNotBlank()) return "web:$raiz"
            val host = Dominios.host(urlWeb)
            if (host.isNotBlank()) return "web:$host"
        }

        // Si no tiene URLs, usar título:
        val tit = entrada.titulo.trim().lowercase()
        if (tit.isNotBlank()) {
            val raiz = Dominios.raiz(tit)
            if (raiz.isNotBlank()) return "web:$raiz"
            return "web:$tit"
        }

        return "desconocido"
    }

    fun armarClaveVisual(entrada: Entrada): String {
        val esApp = esAppAndroid(entrada)
        val nombreServicio = if (esApp) {
            val urlAndroid = entrada.urls.firstOrNull { it.trim().startsWith("android://", ignoreCase = true) } ?: ""
            val paquete = extraerPaqueteAndroid(urlAndroid)
            val tit = entrada.titulo.trim()
            if (tit.isNotBlank() && !tit.equals("android", ignoreCase = true)) tit
            else if (paquete.isNotBlank()) paquete
            else "App Android"
        } else {
            val urlWeb = entrada.urls.firstOrNull()
            if (!urlWeb.isNullOrBlank()) {
                val r = Dominios.raiz(urlWeb)
                if (r.isNotBlank()) r else entrada.titulo.ifBlank { "Sin título" }
            } else {
                entrada.titulo.ifBlank { "Sin título" }
            }
        }
        val usuario = entrada.usuario.ifBlank { "Sin usuario" }
        return if (esApp) "$nombreServicio (App Android) · $usuario" else "$nombreServicio · $usuario"
    }

    /**
     * Normaliza el nombre de usuario según el servicio para detectar variantes de la misma cuenta:
     * - En Google / Gmail: "usuario@gmail.com" y "usuario" representan la misma cuenta de Google.
     * - En Yahoo: "usuario@yahoo.com" y "usuario" representan la misma cuenta.
     * - En Microsoft / Outlook: "usuario@outlook.com" y "usuario" representan la misma cuenta.
     * - Si el correo termina en @<dominioServicio> (ej. en spotify.com "juan@spotify.com" y "juan").
     * - Números telefónicos: unifica formato internacional y local (ej. +593991234567 y 0991234567).
     *
     * IMPORTANTE: Cuentas con usuarios completamente distintos (ej. "curtain.cross" vs "fregenavi")
     * o correos con dominios institucionales distintos (ej. "jlnavas3@utpl.edu.ec" vs "jlnavas3.utpl.edu.ec@gmail.com")
     * NUNCA son equivalentes y se mantienen estrictamente separadas.
     */
    fun normalizarUsuario(usuario: String, servicio: String): String {
        val u = usuario.trim().lowercase()
        if (u.isBlank()) return ""

        val servLower = servicio.lowercase()

        // Google / Gmail: "@gmail.com" y "@googlemail.com" son equivalentes al usuario sin dominio
        if (servLower.contains("google") || servLower.contains("gmail")) {
            if (u.endsWith("@gmail.com")) return u.removeSuffix("@gmail.com")
            if (u.endsWith("@googlemail.com")) return u.removeSuffix("@googlemail.com")
        }

        // Yahoo
        if (servLower.contains("yahoo")) {
            if (u.endsWith("@yahoo.com")) return u.removeSuffix("@yahoo.com")
            if (u.endsWith("@yahoo.es")) return u.removeSuffix("@yahoo.es")
        }

        // Microsoft / Outlook / Hotmail / Live
        if (servLower.contains("microsoft") || servLower.contains("live.com") || servLower.contains("outlook") || servLower.contains("hotmail")) {
            if (u.endsWith("@outlook.com")) return u.removeSuffix("@outlook.com")
            if (u.endsWith("@hotmail.com")) return u.removeSuffix("@hotmail.com")
            if (u.endsWith("@live.com")) return u.removeSuffix("@live.com")
        }

        // Si el usuario termina en @<dominioServicio> (ej. en spotify.com usuario es 'juan@spotify.com' y otro 'juan')
        val dominioServ = servLower.substringAfter("web:").substringAfter("app:")
        if (dominioServ.isNotBlank() && dominioServ.contains('.') && u.endsWith("@$dominioServ")) {
            return u.removeSuffix("@$dominioServ")
        }

        // Normalización de números telefónicos (quitar espacios, guiones, paréntesis)
        if (u.matches(Regex("""^\+?[\d\s\-()]+$""")) && u.count { it.isDigit() } >= 7) {
            var digitos = u.filter { it.isDigit() }
            if (digitos.startsWith("593") && digitos.length == 12) {
                digitos = "0" + digitos.removePrefix("593")
            }
            return digitos
        }

        return u
    }
}
