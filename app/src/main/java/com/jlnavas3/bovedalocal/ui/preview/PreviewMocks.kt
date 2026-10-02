package com.jlnavas3.bovedalocal.ui.preview

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada

object PreviewMocks {
    val ajustes = AjustesApp()

    val entradaEjemplo = Entrada(
        id = "mock-1",
        tipo = TipoEntrada.LOGIN,
        titulo = "Google Workspace",
        usuario = "usuario@correo.com",
        contrasena = "K8#mP9_xL2!vQ4zR",
        urls = listOf("https://accounts.google.com"),
        notas = "Cuenta de trabajo principal con 2FA",
        secretoTotp = "JBSWY3DPEHPK3PXP",
        totpEmisor = "Google",
        favorito = true,
        creadaEn = System.currentTimeMillis() - 86400000L * 30,
        modificadaEn = System.currentTimeMillis() - 3600000L * 2,
        etiquetas = listOf("Trabajo", "Principal"),
        camposPersonalizados = listOf(
            CampoPersonalizado(etiqueta = "PIN de Recuperación", valor = "4829", tipo = TipoCampo.PIN, esSensible = true)
        )
    )

    val entradaBancaria = Entrada(
        id = "mock-2",
        tipo = TipoEntrada.TARJETA,
        titulo = "Banco Santander Visa",
        usuario = "4550 1234 5678 9010",
        contrasena = "789",
        urls = listOf("https://bancosantander.es"),
        notas = "Tarjeta de débito preferente",
        favorito = false,
        creadaEn = System.currentTimeMillis() - 86400000L * 60,
        modificadaEn = System.currentTimeMillis() - 86400000L * 5,
        etiquetas = listOf("Finanzas")
    )
}
