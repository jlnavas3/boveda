package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.ParserBovedaQr
import com.jlnavas3.bovedalocal.util.ResolverTituloDuplicado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val entradaQrImportacionGlobal = MutableStateFlow<Entrada?>(null)
var textoQrPendienteGlobal: String? = null

interface VaultImportacionQrDelegate {
    val repositorio: VaultRepository
    val entradaQrImportacionPendiente: StateFlow<Entrada?> get() = entradaQrImportacionGlobal

    fun descartarEntradaQrImportacionPendiente() {
        entradaQrImportacionGlobal.value = null
    }

    fun solicitarImportacionQr(textoOUrl: String) {
        val limpio = textoOUrl.trim()
        if (!ParserBovedaQr.esBovedaTransfer(limpio)) return

        if (repositorio.estaDesbloqueada) {
            val entrada = ParserBovedaQr.parsear(limpio)
            if (entrada != null) {
                val titulos = repositorio.entradas().map { it.titulo }
                val ajustada = ResolverTituloDuplicado.resolverEntrada(entrada, titulos)
                Diagnostico.apuntar("qr", "Enlace QR externo recibido para importar: «${ajustada.titulo}»")
                entradaQrImportacionGlobal.value = ajustada
                textoQrPendienteGlobal = null
            }
        } else {
            Diagnostico.apuntar("qr", "Enlace QR externo en cola pendiente de desbloqueo")
            textoQrPendienteGlobal = limpio
        }
    }

    fun procesarImportacionQrPendiente() {
        val texto = textoQrPendienteGlobal ?: return
        textoQrPendienteGlobal = null
        if (!repositorio.estaDesbloqueada) return

        val entrada = ParserBovedaQr.parsear(texto) ?: return
        val titulos = repositorio.entradas().map { it.titulo }
        val ajustada = ResolverTituloDuplicado.resolverEntrada(entrada, titulos)
        Diagnostico.apuntar("qr", "Enlace QR externo procesado tras desbloquear: «${ajustada.titulo}»")
        entradaQrImportacionGlobal.value = ajustada
    }
}
