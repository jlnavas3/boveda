package com.jlnavas3.bovedalocal.util

import androidx.annotation.DrawableRes
import com.jlnavas3.bovedalocal.R

/**
 * Catálogo offline de iconos vectoriales para marcas y servicios populares.
 * Identifica automáticamente la marca a partir del dominio registrable o del título de la entrada.
 */
object IconosMarcas {

    private val mapaMarcas: Map<String, Int> = mapOf(
        "google" to R.drawable.ic_marca_google,
        "gmail" to R.drawable.ic_marca_google,
        "youtube" to R.drawable.ic_marca_youtube,
        "apple" to R.drawable.ic_marca_apple,
        "icloud" to R.drawable.ic_marca_apple,
        "github" to R.drawable.ic_marca_github,
        "microsoft" to R.drawable.ic_marca_microsoft,
        "live" to R.drawable.ic_marca_microsoft,
        "msn" to R.drawable.ic_marca_microsoft,
        "outlook" to R.drawable.ic_marca_outlook,
        "hotmail" to R.drawable.ic_marca_outlook,
        "amazon" to R.drawable.ic_marca_amazon,
        "aws" to R.drawable.ic_marca_amazon,
        "spotify" to R.drawable.ic_marca_spotify,
        "netflix" to R.drawable.ic_marca_netflix,
        "steam" to R.drawable.ic_marca_steam,
        "steampowered" to R.drawable.ic_marca_steam,
        "paypal" to R.drawable.ic_marca_paypal,
        "discord" to R.drawable.ic_marca_discord,
        "reddit" to R.drawable.ic_marca_reddit,
        "telegram" to R.drawable.ic_marca_telegram,
        "whatsapp" to R.drawable.ic_marca_whatsapp,
        "instagram" to R.drawable.ic_marca_instagram,
        "facebook" to R.drawable.ic_marca_facebook,
        "fb" to R.drawable.ic_marca_facebook,
        "twitter" to R.drawable.ic_marca_x,
        "x" to R.drawable.ic_marca_x,
        "dropbox" to R.drawable.ic_marca_dropbox,
        "linkedin" to R.drawable.ic_marca_linkedin,
        "mercadolibre" to R.drawable.ic_marca_mercadolibre,
        "mercadopago" to R.drawable.ic_marca_mercadolibre,
        "uber" to R.drawable.ic_marca_uber,
        "airbnb" to R.drawable.ic_marca_airbnb,
        "gitlab" to R.drawable.ic_marca_gitlab,
        "bitwarden" to R.drawable.ic_marca_bitwarden,
        "proton" to R.drawable.ic_marca_proton,
        "protonmail" to R.drawable.ic_marca_proton,
        "protonvpn" to R.drawable.ic_marca_proton,
        "twitch" to R.drawable.ic_marca_twitch,
        "epicgames" to R.drawable.ic_marca_epicgames,
        "tiktok" to R.drawable.ic_marca_tiktok,
        "pinterest" to R.drawable.ic_marca_pinterest,
        "ebay" to R.drawable.ic_marca_ebay,
        "adobe" to R.drawable.ic_marca_adobe,
        "cloudflare" to R.drawable.ic_marca_cloudflare,
        "openai" to R.drawable.ic_marca_openai,
        "chatgpt" to R.drawable.ic_marca_openai,
        "wikipedia" to R.drawable.ic_marca_wikipedia,
        "yahoo" to R.drawable.ic_marca_yahoo,
        "notion" to R.drawable.ic_marca_notion,
        "slack" to R.drawable.ic_marca_slack,
        "zoom" to R.drawable.ic_marca_zoom,
        "mastodon" to R.drawable.ic_marca_mastodon
    )

    @DrawableRes
    fun buscar(semillaOUrl: String, titulo: String = ""): Int? {
        val marca = Dominios.marca(semillaOUrl).lowercase().trim()
        mapaMarcas[marca]?.let { return it }

        val tit = titulo.lowercase().trim()
        if (tit.isNotBlank()) {
            mapaMarcas[tit]?.let { return it }
            // Búsqueda por subcadena para marcas comunes en títulos ("Cuenta de Google", "Netflix Personal", etc.)
            for ((clave, drawable) in mapaMarcas) {
                if (clave.length >= 4 && (tit.contains(clave) || marca.contains(clave))) {
                    return drawable
                }
            }
        }
        return null
    }
}
