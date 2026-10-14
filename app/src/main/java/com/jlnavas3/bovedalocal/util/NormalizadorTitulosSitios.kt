/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
        "mega" to "MEGA",
        "brave" to "Brave",
        "sony" to "Sony",
        "sonyentertainmentnetwork" to "Sony",
        "tunemymusic" to "TuneMyMusic",
        "epicgames" to "Epic Games",
        "bitwarden" to "Bitwarden",
        "fastmail" to "Fastmail"
    )

    fun podarPrefijos(host: String, prefijos: List<String>): String {
        var h = host.trim().lowercase().removePrefix("www.")
        var huboCambio = true
        while (huboCambio) {
            huboCambio = false
            for (p in prefijos) {
                val prefijoConPunto = "${p.trim().lowercase()}."
                if (h.startsWith(prefijoConPunto) && h.length > prefijoConPunto.length) {
                    val restante = h.removePrefix(prefijoConPunto).removePrefix("www.")
                    // Salvaguarda: solo podar si lo que queda aún contiene un punto
                    // (es decir, preserva al menos 'marca.tld' y nunca reduce a solo el TLD)
                    if (restante.contains('.')) {
                        h = restante
                        huboCambio = true
                        break
                    }
                }
            }
        }
        return h
    }

    fun podarTlds(host: String, tlds: List<String>): String {
        var h = host.trim().lowercase()
        val listaTlds = tlds.ifEmpty { AjustesDefaults.NormalizacionTitulos.TLDS_DESCARTABLES }
        var huboCambio = true
        while (huboCambio) {
            huboCambio = false
            for (tld in listaTlds) {
                val tldConPunto = if (tld.startsWith(".")) tld.lowercase() else ".${tld.lowercase()}"
                if (h.endsWith(tldConPunto) && h.length > tldConPunto.length) {
                    h = h.removeSuffix(tldConPunto)
                    huboCambio = true
                    break
                }
            }
        }
        return h
    }

    private val PROVEEDORES_SSO = setOf(
        "accounts.google.com",
        "login.microsoftonline.com",
        "login.live.com",
        "appleid.apple.com",
        "auth0.com",
        "auth.atlassian.com",
        "oauth.telegram.org",
        "api.twitter.com"
    )

    fun esProveedorSso(urlODominio: String): Boolean {
        val host = Dominios.host(urlODominio).lowercase()
        return PROVEEDORES_SSO.any { host == it || host.endsWith(".$it") }
    }

    fun esNombreDeProveedorSso(titulo: String): Boolean {
        val t = titulo.trim().lowercase()
        return t.contains("google") ||
               t.contains("microsoft") ||
               t.contains("apple") ||
               t.contains("atlassian") ||
               t.contains("auth0") ||
               t.contains("twitter")
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
        val esHostSso = urlODominio.isNotBlank() && esProveedorSso(urlODominio)
        val tituloEsProveedor = esNombreDeProveedorSso(tituloLimpio)
        val entradaAnalisis = if (esHostSso && tituloLimpio.isNotBlank() && !tituloEsProveedor) {
            tituloLimpio
        } else if (urlODominio.isNotBlank()) {
            urlODominio
        } else {
            tituloLimpio
        }
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

        // 1b. Paquetes de apps de Android (ej: com.whatsapp, com.google.android.youtube)
        val paquete = LanzadorEnlaces.extraerPaquete(entradaAnalisis)
            ?: (if (!entradaAnalisis.startsWith("http://", ignoreCase = true) && !entradaAnalisis.startsWith("https://", ignoreCase = true)) {
                LanzadorEnlaces.extraerPaquete(tituloLimpio)
            } else null)
        if (paquete != null) {
            val partes = paquete.split('.').filter { it.isNotBlank() }
            val marcaPers = marcasPersonalizadas[paquete.lowercase()]
                ?: partes.firstNotNullOfOrNull { marcasPersonalizadas[it.lowercase()] }
            if (marcaPers != null) return marcaPers

            val marcaOficial = partes.reversed().firstNotNullOfOrNull { MAPA_MARCAS_OFICIALES[it.lowercase()] }
            if (marcaOficial != null) return marcaOficial

            val segmentosGenericos = setOf("com", "org", "net", "io", "es", "de", "fr", "android", "app", "mobile", "client", "lite", "free", "pro", "www")
            val segmentoSignificativo = partes.reversed().firstOrNull { it.lowercase() !in segmentosGenericos }
                ?: partes.lastOrNull()
                ?: paquete
            return capitalizarAmigable(segmentoSignificativo)
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
        val marcaKeyRaw = Dominios.marca(hostPodado).lowercase()
        val marcaKey = if (prefijosConfigurados.any { it.equals(marcaKeyRaw, ignoreCase = true) }) {
            val segmentosSinTld = Dominios.sitioAgrupacion(host)
                .split('.')
                .filter { it.isNotBlank() && !it.equals("www", ignoreCase = true) }
            val candidato = segmentosSinTld.lastOrNull { seg ->
                !prefijosConfigurados.any { it.equals(seg, ignoreCase = true) }
            } ?: segmentosSinTld.firstOrNull { seg ->
                !prefijosConfigurados.any { it.equals(seg, ignoreCase = true) }
            }
            candidato?.lowercase() ?: marcaKeyRaw
        } else {
            marcaKeyRaw
        }

        // 5. Diccionario de marcas personalizadas (prioridad sobre diccionario oficial)
        val marcaPers = marcasPersonalizadas[marcaKey] ?: marcasPersonalizadas[hostPodado.lowercase()]
        if (marcaPers != null) return marcaPers

        // 6. Diccionario oficial
        val nombreOficial = MAPA_MARCAS_OFICIALES[marcaKey]
        if (nombreOficial != null) return nombreOficial

        // 7. Usar la marca del dominio registrable
        if (marcaKey.isNotBlank()) {
            return capitalizarAmigable(marcaKey)
        }

        // 8. Fallback si no se pudo determinar la marca del dominio
        val hostSinTld = podarTlds(hostPodado, tldsConfigurados)
        val nombreLimpio = when {
            hostSinTld.contains('.') -> hostSinTld.substringAfterLast('.')
            else -> hostSinTld
        }

        return capitalizarAmigable(nombreLimpio)
    }

    fun esTituloTecnico(
        titulo: String,
        urls: List<String> = emptyList(),
        tldsConfigurados: List<String> = emptyList()
    ): Boolean {
        return CalculadorAvisosInternos.esTituloComprometido(
            entrada = com.jlnavas3.bovedalocal.data.Entrada(id = "", titulo = titulo, urls = urls),
            tlds = tldsConfigurados
        )
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
        tieneColision: Boolean,
        forzarFormato: Boolean = false
    ): String {
        if (usuario.isBlank() || (!tieneColision && !forzarFormato)) return nombreBase
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
