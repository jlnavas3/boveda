package com.jlnavas3.bovedalocal.util

/**
 * Catálogo optimizado de equivalencias entre nombres de paquetes nativos de Android
 * y sus dominios web oficiales asociados, para apps de uso extendido.
 */
object MapeadorPaquetesPopulares {

    private val PAQUETES_A_DOMINIO: Map<String, String> = mapOf(
        // Meta
        "com.facebook.katana" to "facebook.com",
        "com.facebook.orca" to "facebook.com",
        "com.facebook.lite" to "facebook.com",
        "com.instagram.android" to "instagram.com",
        "com.whatsapp" to "whatsapp.com",
        "com.whatsapp.w4b" to "whatsapp.com",
        "com.threads.android" to "threads.net",

        // X / Twitter
        "com.twitter.android" to "x.com",
        "com.twitter.android.lite" to "x.com",

        // ByteDance / TikTok
        "com.zhiliaoapp.musically" to "tiktok.com",
        "com.ss.android.ugc.trill" to "tiktok.com",

        // Google
        "com.google.android.gm" to "google.com",
        "com.google.android.youtube" to "youtube.com",
        "com.google.android.apps.docs" to "google.com",

        // Streaming y Multimedia
        "com.spotify.music" to "spotify.com",
        "com.spotify.music.canary" to "spotify.com",
        "com.netflix.mediaclient" to "netflix.com",
        "com.disney.disneyplus" to "disneyplus.com",
        "com.amazon.avod.thirdpartyclient" to "primevideo.com",
        "tv.twitch.android.app" to "twitch.tv",

        // Comercio y Pagos
        "com.amazon.mShop.android.shopping" to "amazon.com",
        "com.paypal.android.p2pmobile" to "paypal.com",
        "com.ebay.mobile" to "ebay.com",
        "com.mercadolibre" to "mercadolibre.com",
        "com.mercadopago.wallet" to "mercadopago.com",
        "com.alibaba.aliexpresshd" to "aliexpress.com",

        // Comunicación y Redes
        "org.telegram.messenger" to "telegram.org",
        "org.thunderdog.challegram" to "telegram.org",
        "com.discord" to "discord.com",
        "com.reddit.frontpage" to "reddit.com",
        "com.linkedin.android" to "linkedin.com",
        "com.pinterest" to "pinterest.com",
        "com.snapchat.android" to "snapchat.com",

        // Microsoft
        "com.microsoft.office.outlook" to "outlook.com",
        "com.microsoft.teams" to "microsoft.com",
        "com.microsoft.skydrive" to "live.com",

        // Transporte y Viajes
        "com.ubercab" to "uber.com",
        "com.ubercab.eats" to "ubereats.com",
        "com.airbnb.android" to "airbnb.com",
        "com.booking" to "booking.com",

        // Productividad y Almacenamiento
        "com.dropbox.android" to "dropbox.com",
        "com.notion.id" to "notion.so"
    )

    /**
     * Obtiene el dominio web asociado al nombre de paquete si existe en las reglas personalizadas
     * o en el catálogo oficial de respaldo.
     */
    fun obtenerDominio(paquete: String, mapeoPersonalizado: Map<String, String> = emptyMap()): String? {
        val clave = paquete.trim().lowercase()
        return mapeoPersonalizado[clave] ?: PAQUETES_A_DOMINIO[clave]
    }

    /**
     * Obtiene el paquete de app Android asociado a un dominio si existe en las reglas personalizadas
     * o en el catálogo oficial de respaldo.
     */
    fun obtenerPaquete(dominio: String, mapeoPersonalizado: Map<String, String> = emptyMap()): String? {
        val clave = dominio.trim().lowercase()
        return mapeoPersonalizado.entries.firstOrNull { it.value.equals(clave, ignoreCase = true) }?.key
            ?: PAQUETES_A_DOMINIO.entries.firstOrNull { it.value == clave }?.key
    }
}
