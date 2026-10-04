package com.jlnavas3.bovedalocal.ui.pantallas.tema

import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria

fun construirPaletaDesdeEstado(
    estado: EstadoLaboratorioTemas,
    esOscuro: Boolean
): PaletaSobria {
    val sat = if (esOscuro) estado.saturacionTinteOscuro else estado.saturacionTinteClaro
    val tonoG = if (esOscuro) estado.tonoGlobalOscuro else estado.tonoGlobalClaro
    val effFondo = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoFondoOscuro else estado.tonoFondoClaro)
    val effTarjeta = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTarjetaOscuro else estado.tonoTarjetaClaro)
    val effCampo = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoCampoOscuro else estado.tonoCampoClaro)
    val effBorde = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoBordeOscuro else estado.tonoBordeClaro)
    val effTextoP = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTextoPrincipalOscuro else estado.tonoTextoPrincipalClaro)
    val effTextoS = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTextoSecundarioOscuro else estado.tonoTextoSecundarioClaro)

    val lumF = if (esOscuro) estado.lumFondoOscuro else estado.lumFondoClaro
    val lumT = if (esOscuro) estado.lumTarjetaOscuro else estado.lumTarjetaClaro
    val lumC = if (esOscuro) estado.lumCampoOscuro else estado.lumCampoClaro
    val lumB = if (esOscuro) estado.lumBordeOscuro else estado.lumBordeClaro
    val lumTP = if (esOscuro) estado.lumTextoPrincipalOscuro else estado.lumTextoPrincipalClaro
    val lumTS = if (esOscuro) estado.lumTextoSecundarioOscuro else estado.lumTextoSecundarioClaro

    return PaletaSobria(
        esOscuro = esOscuro,
        fondo = calcularColorCapaLab(effFondo, lumF, sat, esOscuro, true),
        tarjeta = calcularColorCapaLab(effTarjeta, lumT, sat, esOscuro, true),
        campo = calcularColorCapaLab(effCampo, lumC, sat, esOscuro, true),
        borde = calcularColorCapaLab(effBorde, lumB, sat, esOscuro, true),
        textoPrincipal = calcularColorCapaLab(effTextoP, lumTP, sat, esOscuro, false),
        textoSecundario = calcularColorCapaLab(effTextoS, lumTS, sat, esOscuro, false),
        acento = if (esOscuro) estado.colorAcentoOscuro else estado.colorAcentoClaro
    )
}
