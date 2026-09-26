package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

data class CategoriaExportacion(
    val id: String,
    val etiqueta: String,
    val filtro: (Entrada) -> Boolean
)

object ProveedorCategoriasExportacion {
    fun obtenerTodas(): List<CategoriaExportacion> = listOf(
        CategoriaExportacion("todos", "Todos") { true },
        CategoriaExportacion("passkeys", "Passkeys") { it.tipo == TipoEntrada.PASSKEY || it.passkey != null },
        CategoriaExportacion("2fa", "2FA (TOTP)") { !it.secretoTotp.isNullOrBlank() },
        CategoriaExportacion("login", "Cuentas") { it.tipo == TipoEntrada.LOGIN },
        CategoriaExportacion("tarjetas", "Tarjetas") { it.tipo == TipoEntrada.TARJETA },
        CategoriaExportacion("identidades", "Identidades") { it.tipo == TipoEntrada.IDENTIDAD },
        CategoriaExportacion("wifi", "Wi-Fi") { it.tipo == TipoEntrada.WIFI },
        CategoriaExportacion("servidores", "Servidores") { it.tipo == TipoEntrada.SERVIDOR },
        CategoriaExportacion("wallet", "Crypto Wallets") { it.tipo == TipoEntrada.WALLET },
        CategoriaExportacion("notas", "Notas") { it.tipo == TipoEntrada.NOTA }
    )
}
