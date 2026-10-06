package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import java.util.Locale

object NormalizadorTitulosSitios {

    private val MAPA_MARCAS_OFICIALES = mapOf(
        "google" to "Google",
        "gmail" to "Google",
        "chrome" to "Google",
        "withgoogle" to "Google",
        "youtube" to "YouTube",
        "microsoft" to "Microsoft",
        "live" to "Microsoft",
        "msn" to "Microsoft",
        "outlook" to "Microsoft",
        "hotmail" to "Microsoft",
        "apple" to "Apple",
        "icloud" to "Apple",
        "github" to "GitHub",
        "gitlab" to "GitLab",
        "adobe" to "Adobe",
        "paypal" to "PayPal",
        "spotify" to "Spotify",
        "netflix" to "Netflix",
        "amazon" to "Amazon",
        "aws" to "Amazon AWS",
        "whatsapp" to "WhatsApp",
        "telegram" to "Telegram",
        "discord" to "Discord",
        "reddit" to "Reddit",
        "instagram" to "Instagram",
        "facebook" to "Facebook",
        "fb" to "Facebook",
        "twitter" to "X (Twitter)",
        "x" to "X (Twitter)",
        "linkedin" to "LinkedIn",
        "pinterest" to "Pinterest",
        "tiktok" to "TikTok",
        "twitch" to "Twitch",
        "steam" to "Steam",
        "steampowered" to "Steam",
        "playstation" to "PlayStation",
        "xbox" to "Xbox",
        "nintendo" to "Nintendo",
        "disney" to "Disney+",
        "disneyplus" to "Disney+",
        "max" to "Max (HBO)",
        "hbo" to "Max (HBO)",
        "hbomax" to "Max (HBO)",
        "starplus" to "Star+",
        "paramount" to "Paramount+",
        "paramountplus" to "Paramount+",
        "crunchyroll" to "Crunchyroll",
        "deezer" to "Deezer",
        "dropbox" to "Dropbox",
        "figma" to "Figma",
        "lenovo" to "Lenovo",
        "jetbrains" to "JetBrains",
        "envato" to "Envato",
        "codecanyon" to "Envato / CodeCanyon",
        "clarivate" to "Clarivate",
        "mercadolibre" to "Mercado Libre",
        "pichincha" to "Banco Pichincha",
        "banecuador" to "BanEcuador",
        "sri" to "SRI Ecuador",
        "iess" to "IESS",
        "utpl" to "UTPL",
        "cnt" to "CNT",
        "claro" to "Claro",
        "clarovideo" to "Claro Video",
        "movistar" to "Movistar",
        "tuenti" to "Tuenti",
        "xiaomi" to "Xiaomi",
        "huawei" to "Huawei",
        "samsung" to "Samsung",
        "uber" to "Uber",
        "dhl" to "DHL",
        "zoom" to "Zoom",
        "ebay" to "eBay",
        "aliexpress" to "AliExpress",
        "alibaba" to "Alibaba",
        "proton" to "Proton",
        "mega" to "MEGA"
    )

    fun podarPrefijos(host: String, prefijos: List<String>): String {
        var h = host.trim().lowercase().removePrefix("www.")
        var huboCambio = true
        while (huboCambio) {
            huboCambio = false
            for (p in prefijos) {
                val prefijoConPunto = "${p.trim().lowercase()}."
                if (h.startsWith(prefijoConPunto) && h.length > prefijoConPunto.length) {
                    h = h.removePrefix(prefijoConPunto).removePrefix("www.")
                    huboCambio = true
                    break
                }
            }
        }
        return h
    }

    fun podarTlds(host: String, tlds: List<String>): String {
        var h = host.trim().lowercase()
        val listaTlds = tlds.ifEmpty { AjustesDefaults.NormalizacionTitulos.TLDS_DESCARTABLES }
        for (tld in listaTlds) {
            val tldConPunto = if (tld.startsWith(".")) tld.lowercase() else ".${tld.lowercase()}"
            if (h.endsWith(tldConPunto) && h.length > tldConPunto.length) {
                h = h.removeSuffix(tldConPunto)
                break
            }
        }
        return h
    }

    fun extraerNombreBase(
        urlODominio: String,
        rawTitulo: String = "",
        prefijosConfigurados: List<String> = emptyList(),
        plantillaRouter: String = "Router ({ip})",
        plantillaServidor: String = "Servidor ({ip})",
        tldsConfigurados: List<String> = emptyList(),
        marcasPersonalizadas: Map<String, String> = emptyMap(),
        puertosConfigurados: Map<String, String> = emptyMap(),
        octetosRouter: List<Int> = listOf(1, 254)
    ): String {
        val tituloLimpio = rawTitulo.substringBefore('(').substringBefore('·').substringBefore('[').trim()
        val entradaAnalisis = if (urlODominio.isNotBlank()) urlODominio else tituloLimpio
        if (entradaAnalisis.isBlank()) return tituloLimpio.ifBlank { "Cuenta" }

        // 1. Redes locales / IPs (públicas o privadas) / Localhost
        val infoRed = ClasificadorRedLocal.analizar(
            urlORaw = entradaAnalisis,
            puertosConfigurados = puertosConfigurados,
            octetosRouter = octetosRouter
        )
        if (infoRed != null) {
            return ClasificadorRedLocal.formatearNombre(infoRed, plantillaRouter, plantillaServidor)
        }

        // 2. Extraer Host limpio
        var host = Dominios.host(entradaAnalisis)
        if (host.isBlank()) {
            host = entradaAnalisis.substringBefore('/').substringBefore(':').removePrefix("www.")
        }

        // Si no contiene punto y no es URL con protocolo, ya es un nombre limpio o marca
        if (!host.contains('.') && !entradaAnalisis.startsWith("http")) {
            val marcaKey = host.lowercase()
            val marcaPers = marcasPersonalizadas[marcaKey]
            if (marcaPers != null) return marcaPers
            return MAPA_MARCAS_OFICIALES[marcaKey] ?: capitalizarAmigable(host)
        }

        // 3. Podar prefijos de subdominios configurados
        val hostPodado = podarPrefijos(host, prefijosConfigurados)

        // 4. Marca o nombre raíz
        val marcaKey = Dominios.marca(hostPodado).lowercase()

        // 5. Diccionario de marcas personalizadas (prioridad sobre diccionario oficial)
        val marcaPers = marcasPersonalizadas[marcaKey] ?: marcasPersonalizadas[hostPodado.lowercase()]
        if (marcaPers != null) return marcaPers

        // 6. Diccionario oficial
        val nombreOficial = MAPA_MARCAS_OFICIALES[marcaKey]
        if (nombreOficial != null) return nombreOficial

        // 7. Si es un dominio genérico con extensiones compuestas (.gob.ec, .edu.ec, etc.)
        val hostSinTld = podarTlds(hostPodado, tldsConfigurados)
        val nombreLimpio = when {
            hostSinTld.contains('.') -> hostSinTld.substringBefore('.')
            else -> hostSinTld
        }

        return capitalizarAmigable(nombreLimpio)
    }

    fun esTituloTecnico(
        titulo: String,
        urls: List<String> = emptyList(),
        tldsConfigurados: List<String> = emptyList()
    ): Boolean {
        val t = titulo.trim().lowercase()
        if (t.startsWith("http://") || t.startsWith("https://") || t.startsWith("android://") || t.startsWith("androidapp://")) {
            return true
        }
        if (t.startsWith("192.168.") || t.startsWith("10.") || t.startsWith("172.") || t.startsWith("localhost")) {
            return true
        }
        if (t.matches(Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}(:\d+)?.*"""))) {
            return true
        }
        val listaTlds = tldsConfigurados.ifEmpty { AjustesDefaults.NormalizacionTitulos.TLDS_DESCARTABLES }
        val tlds = listaTlds.map { if (it.startsWith(".")) it.lowercase() else ".${it.lowercase()}" }
        if (tlds.any { t.contains(it) }) {
            return true
        }
        val hostCoincidente = urls.firstOrNull { u ->
            val h = Dominios.host(u)
            h.equals(t, ignoreCase = true) || h.removePrefix("www.").equals(t, ignoreCase = true)
        }
        return hostCoincidente != null
    }

    fun esTituloGeneradoOModificable(
        titulo: String,
        nombreBase: String,
        usuario: String,
        urls: List<String> = emptyList(),
        tldsConfigurados: List<String> = emptyList()
    ): Boolean {
        if (esTituloTecnico(titulo, urls, tldsConfigurados)) return true
        val t = titulo.trim()
        val base = nombreBase.trim()
        if (t.equals(base, ignoreCase = true)) return true
        if (usuario.isNotBlank()) {
            if (t.equals("$base ($usuario)", ignoreCase = true)) return true
            if (t.equals("$base · $usuario", ignoreCase = true)) return true
        }
        return false
    }

    fun generarTituloFinal(
        nombreBase: String,
        usuario: String,
        modo: ModoFormatoTitulos,
        tieneColision: Boolean
    ): String {
        if (!tieneColision || usuario.isBlank()) return nombreBase
        return when (modo) {
            ModoFormatoTitulos.MINIMALISTA -> nombreBase
            ModoFormatoTitulos.EXPLICITO_PARENTESIS -> "$nombreBase ($usuario)"
            ModoFormatoTitulos.EXPLICITO_SEPARADOR -> "$nombreBase · $usuario"
        }
    }

    private fun capitalizarAmigable(raw: String): String {
        val partes = raw.replace('-', ' ').replace('_', ' ').split(' ').filter { it.isNotBlank() }
        if (partes.isEmpty()) return raw
        return partes.joinToString(" ") { palabra ->
            palabra.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }
}
