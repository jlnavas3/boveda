package com.jlnavas3.bovedalocal.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.R

/**
 * Información de presentación de una marca:
 * @param drawableRes Recurso vectorial de la marca.
 * @param colorOficial Color corporativo oficial. Si es null y esMulticolor es true, se preservan los colores del vector.
 * @param esAdaptativaBlancoNegro Si la marca usa un tono neutro (como Apple o GitHub), se renderiza en blanco en modo oscuro y en negro en modo claro.
 * @param esMulticolor Si el vector tiene sus propios colores internos (como Google de 4 colores o Microsoft).
 */
data class InfoMarca(
    @get:DrawableRes val drawableRes: Int,
    val colorOficial: Color? = null,
    val esAdaptativaBlancoNegro: Boolean = false,
    val esMulticolor: Boolean = false
)

/**
 * Catálogo offline de iconos vectoriales para marcas y servicios populares.
 * Identifica automáticamente la marca a partir del dominio registrable o del título de la entrada.
 */
object IconosMarcas {

    private val mapaMarcas: Map<String, InfoMarca> = mapOf(
        // Google (Multicolor oficial)
        "google" to InfoMarca(R.drawable.ic_marca_google, esMulticolor = true),
        "gmail" to InfoMarca(R.drawable.ic_marca_google, esMulticolor = true),
        "chrome" to InfoMarca(R.drawable.ic_marca_google, esMulticolor = true),
        "withgoogle" to InfoMarca(R.drawable.ic_marca_google, esMulticolor = true),
        "youtube" to InfoMarca(R.drawable.ic_marca_youtube, colorOficial = Color(0xFFFF0000)),

        // Microsoft (Multicolor oficial / Outlook)
        "microsoft" to InfoMarca(R.drawable.ic_marca_microsoft, esMulticolor = true),
        "live" to InfoMarca(R.drawable.ic_marca_microsoft, esMulticolor = true),
        "msn" to InfoMarca(R.drawable.ic_marca_microsoft, esMulticolor = true),
        "outlook" to InfoMarca(R.drawable.ic_marca_outlook, colorOficial = Color(0xFF0078D4)),
        "hotmail" to InfoMarca(R.drawable.ic_marca_outlook, colorOficial = Color(0xFF0078D4)),

        // Apple & GitHub (Adaptativos Blanco / Negro)
        "apple" to InfoMarca(R.drawable.ic_marca_apple, esAdaptativaBlancoNegro = true),
        "icloud" to InfoMarca(R.drawable.ic_marca_apple, esAdaptativaBlancoNegro = true),
        "github" to InfoMarca(R.drawable.ic_marca_github, esAdaptativaBlancoNegro = true),
        "gitlab" to InfoMarca(R.drawable.ic_marca_gitlab, colorOficial = Color(0xFFFC6D26)),

        // Servicios Clave
        "adobe" to InfoMarca(R.drawable.ic_marca_adobe, colorOficial = Color(0xFFFA0F00)),
        "paypal" to InfoMarca(R.drawable.ic_marca_paypal, colorOficial = Color(0xFF003087)),
        "spotify" to InfoMarca(R.drawable.ic_marca_spotify, colorOficial = Color(0xFF1DB954)),
        "netflix" to InfoMarca(R.drawable.ic_marca_netflix, colorOficial = Color(0xFFE50914)),
        "amazon" to InfoMarca(R.drawable.ic_marca_amazon, colorOficial = Color(0xFFFF9900)),
        "aws" to InfoMarca(R.drawable.ic_marca_amazon, colorOficial = Color(0xFFFF9900)),

        // Mensajería y Redes Sociales
        "whatsapp" to InfoMarca(R.drawable.ic_marca_whatsapp, colorOficial = Color(0xFF25D366)),
        "telegram" to InfoMarca(R.drawable.ic_marca_telegram, colorOficial = Color(0xFF229ED9)),
        "discord" to InfoMarca(R.drawable.ic_marca_discord, colorOficial = Color(0xFF5865F2)),
        "reddit" to InfoMarca(R.drawable.ic_marca_reddit, colorOficial = Color(0xFFFF4500)),
        "instagram" to InfoMarca(R.drawable.ic_marca_instagram, colorOficial = Color(0xFFE4405F)),
        "facebook" to InfoMarca(R.drawable.ic_marca_facebook, colorOficial = Color(0xFF1877F2)),
        "fb" to InfoMarca(R.drawable.ic_marca_facebook, colorOficial = Color(0xFF1877F2)),
        "twitter" to InfoMarca(R.drawable.ic_marca_x, esAdaptativaBlancoNegro = true),
        "x" to InfoMarca(R.drawable.ic_marca_x, esAdaptativaBlancoNegro = true),
        "linkedin" to InfoMarca(R.drawable.ic_marca_linkedin, colorOficial = Color(0xFF0A66C2)),
        "pinterest" to InfoMarca(R.drawable.ic_marca_pinterest, colorOficial = Color(0xFFBD081C)),
        "tiktok" to InfoMarca(R.drawable.ic_marca_tiktok, esAdaptativaBlancoNegro = true),
        "twitch" to InfoMarca(R.drawable.ic_marca_twitch, colorOficial = Color(0xFF9146FF)),
        "mastodon" to InfoMarca(R.drawable.ic_marca_mastodon, colorOficial = Color(0xFF6364FF)),
        "snapchat" to InfoMarca(R.drawable.ic_marca_snapchat, colorOficial = Color(0xFFFFFC00)),
        "tumblr" to InfoMarca(R.drawable.ic_marca_tumblr, colorOficial = Color(0xFF36465D)),
        "flickr" to InfoMarca(R.drawable.ic_marca_flickr, colorOficial = Color(0xFF0063DC)),
        "wattpad" to InfoMarca(R.drawable.ic_marca_wattpad, colorOficial = Color(0xFFFF6122)),
        "vk" to InfoMarca(R.drawable.ic_marca_vk, colorOficial = Color(0xFF0077FF)),

        // Streaming, Videojuegos y Entretenimiento
        "disney" to InfoMarca(R.drawable.ic_marca_disney, colorOficial = Color(0xFF113CCF)),
        "disneyplus" to InfoMarca(R.drawable.ic_marca_disney, colorOficial = Color(0xFF113CCF)),
        "max" to InfoMarca(R.drawable.ic_marca_max, colorOficial = Color(0xFF002BE7)),
        "hbo" to InfoMarca(R.drawable.ic_marca_max, colorOficial = Color(0xFF002BE7)),
        "hbomax" to InfoMarca(R.drawable.ic_marca_max, colorOficial = Color(0xFF002BE7)),
        "starplus" to InfoMarca(R.drawable.ic_marca_starplus, colorOficial = Color(0xFFFF2041)),
        "paramount" to InfoMarca(R.drawable.ic_marca_paramountplus, colorOficial = Color(0xFF0064FF)),
        "paramountplus" to InfoMarca(R.drawable.ic_marca_paramountplus, colorOficial = Color(0xFF0064FF)),
        "hulu" to InfoMarca(R.drawable.ic_marca_hulu, colorOficial = Color(0xFF1CE783)),
        "crunchyroll" to InfoMarca(R.drawable.ic_marca_crunchyroll, colorOficial = Color(0xFFF47521)),
        "deezer" to InfoMarca(R.drawable.ic_marca_deezer, colorOficial = Color(0xFFA238FF)),
        "lastfm" to InfoMarca(R.drawable.ic_marca_lastfm, colorOficial = Color(0xFFD51007)),
        "last.fm" to InfoMarca(R.drawable.ic_marca_lastfm, colorOficial = Color(0xFFD51007)),
        "steam" to InfoMarca(R.drawable.ic_marca_steam, colorOficial = Color(0xFF1A9FFF)),
        "steampowered" to InfoMarca(R.drawable.ic_marca_steam, colorOficial = Color(0xFF1A9FFF)),
        "epicgames" to InfoMarca(R.drawable.ic_marca_epicgames, esAdaptativaBlancoNegro = true),
        "playstation" to InfoMarca(R.drawable.ic_marca_playstation, colorOficial = Color(0xFF003791)),
        "xbox" to InfoMarca(R.drawable.ic_marca_xbox, colorOficial = Color(0xFF107C10)),
        "nintendo" to InfoMarca(R.drawable.ic_marca_nintendo, colorOficial = Color(0xFFE60012)),
        "vix" to InfoMarca(R.drawable.ic_marca_vix, colorOficial = Color(0xFFFF3C00)),
        "viki" to InfoMarca(R.drawable.ic_marca_viki, colorOficial = Color(0xFF00A3E0)),
        "pornhub" to InfoMarca(R.drawable.ic_marca_pornhub, colorOficial = Color(0xFFFFA31A)),

        // Hardware, Fabricantes & Móviles
        "xiaomi" to InfoMarca(R.drawable.ic_marca_xiaomi, colorOficial = Color(0xFFFF6900)),
        "samsung" to InfoMarca(R.drawable.ic_marca_samsung, colorOficial = Color(0xFF1428A0)),
        "huawei" to InfoMarca(R.drawable.ic_marca_huawei, colorOficial = Color(0xFFCF0A2C)),
        "sony" to InfoMarca(R.drawable.ic_marca_sony, esAdaptativaBlancoNegro = true),
        "lenovo" to InfoMarca(R.drawable.ic_marca_lenovo, colorOficial = Color(0xFFE2231A)),
        "dell" to InfoMarca(R.drawable.ic_marca_dell, colorOficial = Color(0xFF0076CE)),
        "logitech" to InfoMarca(R.drawable.ic_marca_logitech, colorOficial = Color(0xFF00B8FC)),
        "logi.com" to InfoMarca(R.drawable.ic_marca_logitech, colorOficial = Color(0xFF00B8FC)),
        "tcl" to InfoMarca(R.drawable.ic_marca_tcl, colorOficial = Color(0xFFE20613)),

        // Navegadores
        "firefox" to InfoMarca(R.drawable.ic_marca_firefox, colorOficial = Color(0xFFFF7139)),
        "mozilla" to InfoMarca(R.drawable.ic_marca_firefox, colorOficial = Color(0xFFFF7139)),
        "brave" to InfoMarca(R.drawable.ic_marca_brave, colorOficial = Color(0xFFFB542B)),

        // Desarrollo, Programación & Cloud
        "jetbrains" to InfoMarca(R.drawable.ic_marca_jetbrains, esAdaptativaBlancoNegro = true),
        "stackoverflow" to InfoMarca(R.drawable.ic_marca_stackoverflow, colorOficial = Color(0xFFF58025)),
        "postman" to InfoMarca(R.drawable.ic_marca_postman, colorOficial = Color(0xFFFF6C37)),
        "getpostman" to InfoMarca(R.drawable.ic_marca_postman, colorOficial = Color(0xFFFF6C37)),
        "sublime" to InfoMarca(R.drawable.ic_marca_sublimetext, colorOficial = Color(0xFFFF9800)),
        "sublimetext" to InfoMarca(R.drawable.ic_marca_sublimetext, colorOficial = Color(0xFFFF9800)),
        "termius" to InfoMarca(R.drawable.ic_marca_termius, colorOficial = Color(0xFF2B3B48)),
        "mongodb" to InfoMarca(R.drawable.ic_marca_mongodb, colorOficial = Color(0xFF47A248)),
        "heroku" to InfoMarca(R.drawable.ic_marca_heroku, colorOficial = Color(0xFF430098)),
        "oracle" to InfoMarca(R.drawable.ic_marca_oracle, colorOficial = Color(0xFFF80000)),
        "cisco" to InfoMarca(R.drawable.ic_marca_cisco, colorOficial = Color(0xFF1BA0D7)),
        "redhat" to InfoMarca(R.drawable.ic_marca_redhat, colorOficial = Color(0xFFEE0000)),
        "ubuntu" to InfoMarca(R.drawable.ic_marca_ubuntu, colorOficial = Color(0xFFE95420)),
        "ibm" to InfoMarca(R.drawable.ic_marca_ibm, colorOficial = Color(0xFF052FAD)),
        "wordpress" to InfoMarca(R.drawable.ic_marca_wordpress, colorOficial = Color(0xFF21759B)),
        "siteground" to InfoMarca(R.drawable.ic_marca_siteground, colorOficial = Color(0xFF1472B7)),
        "godaddy" to InfoMarca(R.drawable.ic_marca_godaddy, colorOficial = Color(0xFF1BDBDB)),
        "envato" to InfoMarca(R.drawable.ic_marca_envato, colorOficial = Color(0xFF81B441)),
        "codecanyon" to InfoMarca(R.drawable.ic_marca_envato, colorOficial = Color(0xFF81B441)),
        "teamviewer" to InfoMarca(R.drawable.ic_marca_teamviewer, colorOficial = Color(0xFF0E80E5)),
        "replit" to InfoMarca(R.drawable.ic_marca_replit, colorOficial = Color(0xFFF26207)),
        "ollama" to InfoMarca(R.drawable.ic_marca_ollama, esAdaptativaBlancoNegro = true),
        "linux" to InfoMarca(R.drawable.ic_marca_linux, colorOficial = Color(0xFFFCC624)),
        "linuxfoundation" to InfoMarca(R.drawable.ic_marca_linux, colorOficial = Color(0xFFFCC624)),

        // Productividad, IA, Notas & Educación
        "openai" to InfoMarca(R.drawable.ic_marca_openai, colorOficial = Color(0xFF10A37F)),
        "chatgpt" to InfoMarca(R.drawable.ic_marca_openai, colorOficial = Color(0xFF10A37F)),
        "claude" to InfoMarca(R.drawable.ic_marca_claude, colorOficial = Color(0xFFCC785C)),
        "anthropic" to InfoMarca(R.drawable.ic_marca_claude, colorOficial = Color(0xFFCC785C)),
        "slack" to InfoMarca(R.drawable.ic_marca_slack, esMulticolor = true),
        "notion" to InfoMarca(R.drawable.ic_marca_notion, esAdaptativaBlancoNegro = true),
        "trello" to InfoMarca(R.drawable.ic_marca_trello, colorOficial = Color(0xFF0079BF)),
        "figma" to InfoMarca(R.drawable.ic_marca_figma, colorOficial = Color(0xFFF24E1E)),
        "canva" to InfoMarca(R.drawable.ic_marca_canva, colorOficial = Color(0xFF00C4CC)),
        "dropbox" to InfoMarca(R.drawable.ic_marca_dropbox, colorOficial = Color(0xFF0061FF)),
        "cloudflare" to InfoMarca(R.drawable.ic_marca_cloudflare, colorOficial = Color(0xFFF38020)),
        "bitwarden" to InfoMarca(R.drawable.ic_marca_bitwarden, colorOficial = Color(0xFF175DDC)),
        "proton" to InfoMarca(R.drawable.ic_marca_proton, colorOficial = Color(0xFF6D4AFF)),
        "protonmail" to InfoMarca(R.drawable.ic_marca_proton, colorOficial = Color(0xFF6D4AFF)),
        "protonvpn" to InfoMarca(R.drawable.ic_marca_proton, colorOficial = Color(0xFF6D4AFF)),
        "docker" to InfoMarca(R.drawable.ic_marca_docker, colorOficial = Color(0xFF2496ED)),
        "zoom" to InfoMarca(R.drawable.ic_marca_zoom, colorOficial = Color(0xFF0B5CFF)),
        "duolingo" to InfoMarca(R.drawable.ic_marca_duolingo, colorOficial = Color(0xFF58CC02)),
        "wikipedia" to InfoMarca(R.drawable.ic_marca_wikipedia, esAdaptativaBlancoNegro = true),
        "yahoo" to InfoMarca(R.drawable.ic_marca_yahoo, colorOficial = Color(0xFF6001D2)),
        "ebay" to InfoMarca(R.drawable.ic_marca_ebay, colorOficial = Color(0xFFE53238)),
        "uber" to InfoMarca(R.drawable.ic_marca_uber, esAdaptativaBlancoNegro = true),
        "airbnb" to InfoMarca(R.drawable.ic_marca_airbnb, colorOficial = Color(0xFFFF5A5F)),
        "evernote" to InfoMarca(R.drawable.ic_marca_evernote, colorOficial = Color(0xFF00A82D)),
        "mega" to InfoMarca(R.drawable.ic_marca_mega, colorOficial = Color(0xFFD9272E)),
        "mediafire" to InfoMarca(R.drawable.ic_marca_mediafire, colorOficial = Color(0xFF1299F3)),
        "monday" to InfoMarca(R.drawable.ic_marca_monday, colorOficial = Color(0xFF00CA72)),
        "loom" to InfoMarca(R.drawable.ic_marca_loom, colorOficial = Color(0xFF625DF5)),
        "waze" to InfoMarca(R.drawable.ic_marca_waze, colorOficial = Color(0xFF33CCFF)),
        "deepl" to InfoMarca(R.drawable.ic_marca_deepl, colorOficial = Color(0xFF0F2B46)),
        "camscanner" to InfoMarca(R.drawable.ic_marca_camscanner, colorOficial = Color(0xFF1B8164)),
        "autodesk" to InfoMarca(R.drawable.ic_marca_autodesk, colorOficial = Color(0xFF0696D7)),
        "zotero" to InfoMarca(R.drawable.ic_marca_zotero, colorOficial = Color(0xFFCC2B2B)),
        "openstreetmap" to InfoMarca(R.drawable.ic_marca_openstreetmap, colorOficial = Color(0xFF7EBC6F)),
        "udemy" to InfoMarca(R.drawable.ic_marca_udemy, colorOficial = Color(0xFFA435F0)),
        "edx" to InfoMarca(R.drawable.ic_marca_edx, colorOficial = Color(0xFFD42728)),
        "lichess" to InfoMarca(R.drawable.ic_marca_lichess, esAdaptativaBlancoNegro = true),

        // Ciberseguridad, VPN & Redes
        "eset" to InfoMarca(R.drawable.ic_marca_eset, colorOficial = Color(0xFF008080)),
        "kaspersky" to InfoMarca(R.drawable.ic_marca_kaspersky, colorOficial = Color(0xFF006D55)),
        "norton" to InfoMarca(R.drawable.ic_marca_norton, colorOficial = Color(0xFFFFCC00)),
        "expressvpn" to InfoMarca(R.drawable.ic_marca_expressvpn, colorOficial = Color(0xFFDA3945)),
        "nord" to InfoMarca(R.drawable.ic_marca_nordvpn, colorOficial = Color(0xFF4687FF)),
        "nordvpn" to InfoMarca(R.drawable.ic_marca_nordvpn, colorOficial = Color(0xFF4687FF)),
        "nordaccount" to InfoMarca(R.drawable.ic_marca_nordvpn, colorOficial = Color(0xFF4687FF)),
        "hackthebox" to InfoMarca(R.drawable.ic_marca_hackthebox, colorOficial = Color(0xFF9FEF00)),
        "nextdns" to InfoMarca(R.drawable.ic_marca_nextdns, colorOficial = Color(0xFF0072CE)),
        "tplink" to InfoMarca(R.drawable.ic_marca_tplink, colorOficial = Color(0xFF3FD3D4)),
        "tplinkcloud" to InfoMarca(R.drawable.ic_marca_tplink, colorOficial = Color(0xFF3FD3D4)),
        "wyze" to InfoMarca(R.drawable.ic_marca_wyze, colorOficial = Color(0xFF19B6A6)),
        "noip" to InfoMarca(R.drawable.ic_marca_noip, colorOficial = Color(0xFF86B300)),

        // Comercio & Envíos
        "mercadolibre" to InfoMarca(R.drawable.ic_marca_mercadolibre, colorOficial = Color(0xFFFFE600)),
        "mercadopago" to InfoMarca(R.drawable.ic_marca_mercadopago, colorOficial = Color(0xFF009EE3)),
        "aliexpress" to InfoMarca(R.drawable.ic_marca_aliexpress, colorOficial = Color(0xFFFF4747)),
        "alibaba" to InfoMarca(R.drawable.ic_marca_alibaba, colorOficial = Color(0xFFFF6A00)),
        "dhl" to InfoMarca(R.drawable.ic_marca_dhl, colorOficial = Color(0xFFD40511)),

        // Banca, Fintech & Cripto
        "bbva" to InfoMarca(R.drawable.ic_marca_bbva, colorOficial = Color(0xFF004481)),
        "santander" to InfoMarca(R.drawable.ic_marca_santander, colorOficial = Color(0xFFEC0000)),
        "nequi" to InfoMarca(R.drawable.ic_marca_nequi, colorOficial = Color(0xFF200020)),
        "binance" to InfoMarca(R.drawable.ic_marca_binance, colorOficial = Color(0xFFF0B90B)),
        "coinbase" to InfoMarca(R.drawable.ic_marca_coinbase, colorOficial = Color(0xFF0052FF)),
        "wise" to InfoMarca(R.drawable.ic_marca_wise, colorOficial = Color(0xFF9FE870)),
        "revolut" to InfoMarca(R.drawable.ic_marca_revolut, colorOficial = Color(0xFF0075FF)),

        // Telecomunicaciones, Banca e Instituciones (Ecuador & LATAM)
        "pichincha" to InfoMarca(R.drawable.ic_marca_pichincha, colorOficial = Color(0xFFFFDD00)),
        "banecuador" to InfoMarca(R.drawable.ic_marca_banecuador, colorOficial = Color(0xFF006747)),
        "sri" to InfoMarca(R.drawable.ic_marca_sri, colorOficial = Color(0xFF003882)),
        "iess" to InfoMarca(R.drawable.ic_marca_iess, colorOficial = Color(0xFF004F9F)),
        "cnt" to InfoMarca(R.drawable.ic_marca_cnt, colorOficial = Color(0xFF005BAB)),
        "micnt" to InfoMarca(R.drawable.ic_marca_cnt, colorOficial = Color(0xFF005BAB)),
        "claro" to InfoMarca(R.drawable.ic_marca_claro, colorOficial = Color(0xFFDA291C)),
        "miclaro" to InfoMarca(R.drawable.ic_marca_claro, colorOficial = Color(0xFFDA291C)),
        "clarovideo" to InfoMarca(R.drawable.ic_marca_claro, colorOficial = Color(0xFFDA291C)),
        "movistar" to InfoMarca(R.drawable.ic_marca_movistar, colorOficial = Color(0xFF019DF4)),
        "directv" to InfoMarca(R.drawable.ic_marca_directv, colorOficial = Color(0xFF002F6C)),
        "directvgo" to InfoMarca(R.drawable.ic_marca_directv, colorOficial = Color(0xFF002F6C)),
        "diners" to InfoMarca(R.drawable.ic_marca_dinersclub, colorOficial = Color(0xFF004B87)),
        "dinersclub" to InfoMarca(R.drawable.ic_marca_dinersclub, colorOficial = Color(0xFF004B87)),
        "interdin" to InfoMarca(R.drawable.ic_marca_dinersclub, colorOficial = Color(0xFF004B87))
    )

    private val tldsIgnorados = setOf(
        "com", "org", "net", "edu", "gob", "fin", "ec", "it", "es", "app", "io", "tv", "me", "online",
        "cloud", "ar", "co", "mx", "us", "uk", "de", "fr", "ru", "jp", "cn", "in", "br", "android",
        "apps", "intl", "mobile", "services"
    )

    // Marcas de 1 a 3 letras o nombres comunes que sólo deben coincidir si el título es exacto o es un dominio específico
    private val clavesSoloDominioOExactas = setOf("live", "x", "fb", "vk", "max", "starplus")

    fun buscarInfo(semillaOUrl: String, titulo: String = ""): InfoMarca? {
        val entradaLimpia = semillaOUrl.trim().lowercase()
        val titLimpio = titulo.trim().lowercase()

        // 1. Extraer el host o paquete limpio (sin path, sin parámetros query, sin hash)
        val host = when {
            entradaLimpia.startsWith("android://") -> {
                entradaLimpia.substringAfterLast('@')
                    .substringBefore('/')
                    .substringBefore('?')
                    .substringBefore('#')
                    .substringBefore(':')
            }
            entradaLimpia.isNotBlank() -> Dominios.host(entradaLimpia)
            else -> ""
        }

        // Si el host es una dirección IP pura (ej: 192.168.1.1 o 186.4.146.197), no es una marca pública
        val esIp = host.isNotEmpty() && host.split('.').all { it.toIntOrNull() != null }

        if (host.isNotEmpty() && !esIp) {
            // 2. Coincidencia exacta con el host completo o marca del dominio
            mapaMarcas[host]?.let { return it }

            val marca = Dominios.marca(host)
            if (marca.isNotEmpty()) {
                mapaMarcas[marca]?.let { return it }
            }

            // 3. Coincidencia por segmento significativo del host o paquete de Android
            // ej: "bancaweb.pichincha.com" -> pichincha
            // ej: "com.yellowpepper.pichincha" -> pichincha
            // ej: "com.xiaomi.account" -> xiaomi
            val segmentos = host.split('.').filter { it !in tldsIgnorados && it.length >= 3 }
            for (seg in segmentos.asReversed()) {
                mapaMarcas[seg]?.let { return it }
            }
        }

        // 4. Búsqueda por título
        if (titLimpio.isNotEmpty()) {
            // Coincidencia exacta de todo el título (ej: "Google", "Netflix", "X", "FB")
            mapaMarcas[titLimpio]?.let { return it }

            // Coincidencia por palabra completa en el título (ej: "Banco Pichincha" -> "pichincha", "Cuenta Google" -> "google")
            // Delimitamos por espacios y signos de puntuación.
            // Excluimos palabras ambiguas o de 1-2 letras que podrían coincidir por error fuera de contexto.
            val palabras = titLimpio.split(Regex("[\\s._\\-:,;()/|\\[\\]]+")).filter { it.length >= 3 }
            for (palabra in palabras) {
                if (palabra !in clavesSoloDominioOExactas) {
                    mapaMarcas[palabra]?.let { return it }
                }
            }
        }

        return null
    }

    @DrawableRes
    fun buscar(semillaOUrl: String, titulo: String = ""): Int? {
        return buscarInfo(semillaOUrl, titulo)?.drawableRes
    }
}