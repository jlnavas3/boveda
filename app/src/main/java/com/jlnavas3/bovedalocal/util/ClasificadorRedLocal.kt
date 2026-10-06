package com.jlnavas3.bovedalocal.util

import java.net.URI

object ClasificadorRedLocal {

    fun analizar(
        urlORaw: String,
        puertosConfigurados: Map<String, String> = emptyMap(),
        octetosRouter: List<Int> = listOf(1, 254)
    ): InfoRedLocal? {
        val limpio = urlORaw.trim().lowercase()
        if (limpio.isBlank()) return null

        val conEsquema = if (!limpio.startsWith("http://") && !limpio.startsWith("https://")) {
            "http://$limpio"
        } else {
            limpio
        }

        val uri = runCatching { URI(conEsquema) }.getOrNull()
        val host = uri?.host ?: limpio.substringAfter("://").substringBefore('/').substringBefore(':').removePrefix("www.")
        val puerto = if (uri?.port != null && uri.port != -1 && uri.port != 80 && uri.port != 443) {
            uri.port.toString()
        } else {
            val despuesHost = limpio.substringAfter("://").substringBefore('/')
            if (despuesHost.contains(':')) despuesHost.substringAfter(':') else null
        }
        val ruta = uri?.path?.lowercase() ?: limpio.substringAfter(host).substringBefore('?')

        val servicioRuta = when {
            ruta.contains("phpmyadmin") -> "phpMyAdmin"
            ruta.contains("web/guest") || ruta.contains("portal") -> "Portal Web"
            else -> null
        }
        val servicioPuerto = puerto?.let { puertosConfigurados[it] }
        val servicio = servicioRuta ?: servicioPuerto

        if (host == "localhost" || host == "127.0.0.1") {
            return InfoRedLocal(
                esRedPrivada = true,
                esRouterOPuertaEnlace = false,
                hostOIp = host,
                puerto = puerto,
                servicioDetectado = servicio ?: if (puerto != null) "Localhost :$puerto" else "Localhost"
            )
        }

        val partesIp = host.split('.')
        if (partesIp.size == 4 && partesIp.all { it.toIntOrNull() != null && it.toInt() in 0..255 }) {
            val octetos = partesIp.map { it.toInt() }
            val o1 = octetos[0]
            val o2 = octetos[1]
            val o4 = octetos[3]

            val esPrivada = (o1 == 10) ||
                    (o1 == 172 && o2 in 16..31) ||
                    (o1 == 192 && o2 == 168) ||
                    (o1 == 169 && o2 == 254)

            val listaOctetos = octetosRouter.ifEmpty { listOf(1, 254) }
            val esRouter = esPrivada && (o4 in listaOctetos)
            return InfoRedLocal(
                esRedPrivada = esPrivada,
                esRouterOPuertaEnlace = esRouter,
                hostOIp = host,
                puerto = puerto,
                servicioDetectado = servicio
            )
        }

        return null
    }

    fun formatearNombre(
        info: InfoRedLocal,
        plantillaRouter: String = "Router ({ip})",
        plantillaServidor: String = "Servidor ({ip})"
    ): String {
        val ipConPuerto = if (info.puerto != null && info.hostOIp != "localhost") {
            "${info.hostOIp}:${info.puerto}"
        } else {
            info.hostOIp
        }

        if (info.servicioDetectado != null) {
            return if (info.hostOIp == "localhost") {
                info.servicioDetectado
            } else {
                "${info.servicioDetectado} ($ipConPuerto)"
            }
        }

        return if (info.esRouterOPuertaEnlace) {
            plantillaRouter.replace("{ip}", ipConPuerto)
        } else {
            plantillaServidor.replace("{ip}", ipConPuerto)
        }
    }
}
