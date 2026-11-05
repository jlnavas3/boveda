/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/**
 * Calculador puro y determinista de diferencias y presencia de campos entre entradas.
 * Espejo canónico de `src/sync/merge/diff_mask.rs` en Rust.
 */
object CalculadorMascaraDiff {

    private fun esUrlWeb(url: String): Boolean {
        if (LanzadorEnlaces.extraerPaquete(url) != null) return false
        val limpio = url.trim()
        return limpio.startsWith("http://", ignoreCase = true) ||
            limpio.startsWith("https://", ignoreCase = true) ||
            limpio.contains(".")
    }

    /**
     * Construye una máscara con todos los segmentos que contienen información activa
     * para una entrada clasificada como NUEVA.
     */
    fun deEntradaNueva(entrada: Entrada): MascaraCampos {
        var tieneWeb = false
        var tieneApp = false

        for (u in entrada.urls) {
            if (LanzadorEnlaces.extraerPaquete(u) != null) {
                tieneApp = true
            } else if (esUrlWeb(u)) {
                tieneWeb = true
            }
        }

        val tieneUsuario = entrada.usuario.isNotBlank() ||
            entrada.tarjetaEfectiva?.titular?.isNotBlank() == true ||
            entrada.cuentaBancariaEfectiva?.titular?.isNotBlank() == true ||
            entrada.identidadEfectiva?.nombreCompleto?.isNotBlank() == true ||
            entrada.servidorEfectivo?.usuario?.isNotBlank() == true

        val tieneClave = entrada.contrasena.isNotBlank() ||
            entrada.historialContrasenas.isNotEmpty() ||
            entrada.wifiEfectivo?.contrasena?.isNotBlank() == true ||
            entrada.tarjetaEfectiva?.cvv?.isNotBlank() == true ||
            entrada.tarjetaEfectiva?.pin?.isNotBlank() == true ||
            entrada.servidorEfectivo?.clavePrivada?.isNotBlank() == true ||
            entrada.walletEfectiva?.clavePrivada?.isNotBlank() == true

        val tieneTotp = !entrada.secretoTotp.isNullOrBlank() ||
            entrada.totpsSecundarios.isNotEmpty()

        val tienePasskey = entrada.passkey != null ||
            entrada.passkeysSecundarias.isNotEmpty() ||
            entrada.walletEfectiva?.fraseSemilla?.isNotBlank() == true ||
            entrada.servidorEfectivo?.clavePublica?.isNotBlank() == true

        if (!tieneWeb && (entrada.servidorEfectivo?.host?.isNotBlank() == true || entrada.walletEfectiva?.direccionPublica?.isNotBlank() == true)) {
            tieneWeb = true
        }

        val tieneSubmodelos = entrada.datosTarjeta != null ||
            entrada.datosWifi != null ||
            entrada.datosCuentaBancaria != null ||
            entrada.datosIdentidad != null ||
            entrada.datosServidor != null ||
            entrada.datosWallet != null ||
            entrada.datosContacto != null

        return MascaraCampos(
            usuario = tieneUsuario,
            clave = tieneClave,
            totp = tieneTotp,
            passkey = tienePasskey,
            web = tieneWeb,
            app = tieneApp,
            notas = entrada.notas.isNotBlank(),
            submodelos = tieneSubmodelos,
            camposExtra = entrada.camposPersonalizados.isNotEmpty()
        )
    }

    /**
     * Compara campo a campo dos versiones de una misma entrada y devuelve la máscara
     * con los segmentos exactamente modificados.
     */
    fun calcularDiff(previa: Entrada, nueva: Entrada): MascaraCampos {
        val claveModificada = (previa.contrasena != nueva.contrasena) ||
            (previa.historialContrasenas != nueva.historialContrasenas)

        val totpModificado = (previa.secretoTotp != nueva.secretoTotp) ||
            (previa.totpEmisor != nueva.totpEmisor) ||
            (previa.totpDigitos != nueva.totpDigitos) ||
            (previa.totpPeriodo != nueva.totpPeriodo) ||
            (previa.totpAlgoritmo != nueva.totpAlgoritmo) ||
            (previa.totpsSecundarios != nueva.totpsSecundarios)

        val passkeyModificado = (previa.passkey != nueva.passkey) ||
            (previa.passkeysSecundarias != nueva.passkeysSecundarias)

        val websPrevias = previa.urls.filter { esUrlWeb(it) }.toSet()
        val websNuevas = nueva.urls.filter { esUrlWeb(it) }.toSet()
        val webModificada = websPrevias != websNuevas

        val appsPrevias = previa.urls.mapNotNull { LanzadorEnlaces.extraerPaquete(it) }.toSet()
        val appsNuevas = nueva.urls.mapNotNull { LanzadorEnlaces.extraerPaquete(it) }.toSet()
        val appModificada = appsPrevias != appsNuevas

        val submodelosModificados = (previa.datosTarjeta != nueva.datosTarjeta) ||
            (previa.datosWifi != nueva.datosWifi) ||
            (previa.datosCuentaBancaria != nueva.datosCuentaBancaria) ||
            (previa.datosIdentidad != nueva.datosIdentidad) ||
            (previa.datosServidor != nueva.datosServidor) ||
            (previa.datosWallet != nueva.datosWallet) ||
            (previa.datosContacto != nueva.datosContacto)

        return MascaraCampos(
            usuario = previa.usuario != nueva.usuario,
            clave = claveModificada,
            totp = totpModificado,
            passkey = passkeyModificado,
            web = webModificada,
            app = appModificada,
            notas = previa.notas != nueva.notas,
            submodelos = submodelosModificados,
            camposExtra = previa.camposPersonalizados != nueva.camposPersonalizados
        )
    }

    /**
     * Compara una bóveda previa contra la resultante de la sincronización y genera
     * el [InformeSincronizacion] completo con máscaras de diff.
     */
    fun generarInforme(
        bovedaPrevia: com.jlnavas3.bovedalocal.data.ContenidoBoveda,
        bovedaFusionada: com.jlnavas3.bovedalocal.data.ContenidoBoveda,
        dispositivoRemoto: String? = null
    ): InformeSincronizacion {
        val todasPrevias = (bovedaPrevia.entradas + bovedaPrevia.papelera).associateBy { it.id }.toMutableMap()
        val todasFusionadas = (bovedaFusionada.entradas + bovedaFusionada.papelera).associateBy { it.id }

        val listaNuevas = mutableListOf<ItemCambioSync>()
        val listaActualizadas = mutableListOf<ItemCambioSync>()
        val listaPapelera = mutableListOf<ItemCambioSync>()
        var sinCambiosCount = 0

        for ((id, fusionada) in todasFusionadas) {
            val previa = todasPrevias.remove(id)
            if (previa == null) {
                val mascara = deEntradaNueva(fusionada)
                listaNuevas.add(ItemCambioSync(fusionada.id, fusionada.titulo, TipoCambioSync.NUEVA, mascara))
            } else {
                val mascara = calcularDiff(previa, fusionada)
                when {
                    previa.eliminadaEn > 0L && fusionada.eliminadaEn <= 0L -> {
                        listaPapelera.add(ItemCambioSync(fusionada.id, fusionada.titulo, TipoCambioSync.RESTAURADA_DE_PAPELERA, mascara))
                    }
                    previa.eliminadaEn <= 0L && fusionada.eliminadaEn > 0L -> {
                        listaPapelera.add(ItemCambioSync(fusionada.id, fusionada.titulo, TipoCambioSync.ENVIADA_A_PAPELERA, mascara))
                    }
                    mascara.hayCambios || previa.titulo != fusionada.titulo -> {
                        listaActualizadas.add(ItemCambioSync(fusionada.id, fusionada.titulo, TipoCambioSync.ACTUALIZADA, mascara))
                    }
                    else -> {
                        sinCambiosCount++
                    }
                }
            }
        }

        sinCambiosCount += todasPrevias.size

        return InformeSincronizacion(
            nuevas = listaNuevas,
            actualizadas = listaActualizadas,
            papelera = listaPapelera,
            sinCambiosCount = sinCambiosCount,
            dispositivoRemoto = dispositivoRemoto ?: bovedaFusionada.aliasDispositivo
        )
    }
}
