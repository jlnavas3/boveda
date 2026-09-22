package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.aplicarPaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia
import com.jlnavas3.bovedalocal.util.CambiadorIcono
import com.jlnavas3.bovedalocal.util.Diagnostico

interface VaultAjustesDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

    fun ajustarAutoBloqueo(segundos: Int) {
        repositorio.ajustes.actualizar { it.copy(autoBloqueoSegundos = segundos) }
        val desc = if (segundos == 0) "desactivado" else "${segundos}s"
        Diagnostico.apuntar("seguridad", "Tiempo de auto-bloqueo configurado en $desc")
    }

    fun ajustarPortapapeles(segundos: Int) {
        repositorio.ajustes.actualizar { it.copy(portapapelesSegundos = segundos) }
        val desc = if (segundos == 0) "desactivado" else "${segundos}s"
        Diagnostico.apuntar("seguridad", "Tiempo de limpieza de portapapeles configurado en $desc")
    }

    fun ajustarTileModo(modo: String) = repositorio.ajustes.actualizar { it.copy(tileModo = modo) }

    fun ajustarTileLongitud(longitud: Int) = repositorio.ajustes.actualizar { it.copy(tileLongitud = longitud) }

    fun ajustarTilePatron(patron: String) = repositorio.ajustes.actualizar { it.copy(tilePatron = patron) }

    fun ajustarTileCopiarPortapapeles(copiar: Boolean) = repositorio.ajustes.actualizar { it.copy(tileCopiarPortapapeles = copiar) }

    fun ajustarTileMostrarToast(toast: Boolean) = repositorio.ajustes.actualizar { it.copy(tileMostrarToast = toast) }

    fun ajustarTileHaptica(haptica: Boolean) = repositorio.ajustes.actualizar { it.copy(tileHaptica = haptica) }

    fun ajustarTileHapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(tileHapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun ajustarMotorCamara(clave: String) = repositorio.ajustes.actualizar { it.copy(motorCamara = clave) }

    fun ajustarNombrePersonalizado(nombre: String) =
        repositorio.ajustes.actualizar { it.copy(nombrePersonalizado = nombre) }

    /** Cambia el icono del launcher (las 20 variantes precompiladas de Android). */
    fun ajustarIconoLauncher(clave: String) {
        repositorio.ajustes.actualizar { it.copy(iconoLauncher = clave) }
        CambiadorIcono.aplicar(obtenerApp(), clave)
    }

    /** Cambia el acento del tema Y el color del icono del launcher (mismo dibujo, otro degradado). */
    fun ajustarColorApp(paleta: PaletaAcento) {
        aplicarPaletaAcento(paleta)
        repositorio.ajustes.actualizar {
            it.copy(
                colorAcento = paleta.clave,
                iconoLauncher = paleta.clave,
                colorDinamicoSistema = false
            )
        }
        CambiadorIcono.aplicar(obtenerApp(), paleta.clave)
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorAcento(hexOClave: String) {
        repositorio.ajustes.actualizar {
            it.copy(
                colorAcento = hexOClave,
                colorDinamicoSistema = false
            )
        }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarAnimacionDesbloqueo(modo: String) {
        repositorio.ajustes.actualizar { it.copy(animacionDesbloqueo = modo) }
        Diagnostico.apuntar("apariencia", "Animación de desbloqueo configurada en: $modo")
    }

    // --- Personalización de Engranajes ---
    fun ajustarEngranajesVelocidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesVelocidad = valor) }
    }
    fun ajustarEngranajesGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesGrosorBorde = valor) }
    }
    fun ajustarEngranajesAlturaDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesAlturaDientes = valor) }
    }
    fun ajustarEngranajesAnchoDientes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesAnchoDientes = valor) }
    }
    fun ajustarEngranajesGrosorRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesGrosorRadios = valor) }
    }
    fun ajustarEngranajesCurvaturaRadios(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesCurvaturaRadios = valor) }
    }
    fun ajustarEngranajesCantidadRadios(valor: Int) {
        repositorio.ajustes.actualizar { it.copy(engranajesCantidadRadios = valor) }
    }
    fun ajustarEngranajesRadioInterior(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesRadioInterior = valor) }
    }
    fun ajustarEngranajesTamanoEje(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesTamanoEje = valor) }
    }
    fun ajustarEngranajesSombraIntensidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(engranajesSombraIntensidad = valor) }
    }
    fun ajustarEngranajesColor(campo: String, hex: String) {
        repositorio.ajustes.actualizar {
            when (campo) {
                "brillo" -> it.copy(engranajesColorBrillo = hex)
                "principal" -> it.copy(engranajesColorPrincipal = hex)
                "sombra_medio" -> it.copy(engranajesColorSombraMedio = hex)
                "sombra_oscuro" -> it.copy(engranajesColorSombraOscuro = hex)
                "bisel" -> it.copy(engranajesColorBisel = hex)
                "interior" -> it.copy(engranajesColorInterior = hex)
                "cubo" -> it.copy(engranajesColorCubo = hex)
                "eje" -> it.copy(engranajesColorEje = hex)
                else -> it
            }
        }
    }
    fun restablecerAjustesEngranajes() {
        repositorio.ajustes.actualizar {
            it.copy(
                engranajesVelocidad = 24f,
                engranajesGrosorBorde = 0.7f,
                engranajesAlturaDientes = 0.76f,
                engranajesAnchoDientes = 1.00f,
                engranajesGrosorRadios = 1.40f,
                engranajesCurvaturaRadios = 1.00f,
                engranajesCantidadRadios = 6,
                engranajesRadioInterior = 0.80f,
                engranajesTamanoEje = 1.23f,
                engranajesSombraIntensidad = 0.95f,
                engranajesColorBrillo = "#ABA799",
                engranajesColorPrincipal = "#918D7E",
                engranajesColorSombraMedio = "#635C57",
                engranajesColorSombraOscuro = "#404038",
                engranajesColorBisel = "#A5A19D",
                engranajesColorInterior = "#00000000",
                engranajesColorCubo = "#D3D1C8",
                engranajesColorEje = "#141316"
            )
        }
    }

    // --- Personalización de Puerta de Bóveda ---
    fun ajustarPuertaVelocidad(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaVelocidad = valor) }
    }
    fun ajustarPuertaGrosorAnillos(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(puertaGrosorAnillos = valor) }
    }
    fun ajustarPuertaColor(hex: String) {
        repositorio.ajustes.actualizar { it.copy(puertaColor = hex) }
    }
    fun restablecerAjustesPuerta() {
        repositorio.ajustes.actualizar {
            it.copy(
                puertaVelocidad = 1.0f,
                puertaGrosorAnillos = 1.0f,
                puertaColor = ""
            )
        }
    }

    fun ajustarColorIconosInternos(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIconosInternos = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorTitulos(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorTitulos = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorTarjetas(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorTarjetas = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorSeguridad(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorSeguridad = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorArgon2(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorArgon2 = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorCamara(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorCamara = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColor2FA(hex: String) {
        repositorio.ajustes.actualizar { it.copy(color2FA = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorPasskeys(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorPasskeys = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorGenerador(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorGenerador = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorSalud(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorSalud = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorPapelera(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorPapelera = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorExportacion(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorExportacion = hex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun restablecerColoresTema() {
        repositorio.ajustes.actualizar {
            it.copy(
                colorAcento = "ambar",
                colorIconosInternos = "",
                colorTitulos = "",
                colorTarjetas = "",
                colorDinamicoSistema = true,
                colorSeguridad = "",
                colorArgon2 = "",
                colorCamara = "",
                color2FA = "",
                colorPasskeys = "",
                colorGenerador = "",
                colorSalud = "",
                colorPapelera = "",
                colorExportacion = ""
            )
        }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun alternarColorDinamicoSistema(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(colorDinamicoSistema = activo) }
        aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
    }

    // --- Personalización de Bordes y Formas ---
    fun ajustarCurvaturaEsquinas(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(curvaturaEsquinasDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(grosorBordeDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEstiloBorde(estilo: String) {
        repositorio.ajustes.actualizar { it.copy(estiloBorde = estilo) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoComponentes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoComponentesDp = valor) }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun aplicarPresetFormas(curvatura: Float, grosor: Float, estilo: String, espaciado: Float) {
        repositorio.ajustes.actualizar {
            it.copy(
                curvaturaEsquinasDp = curvatura,
                grosorBordeDp = grosor,
                estiloBorde = estilo,
                espaciadoComponentesDp = espaciado
            )
        }
        aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun restablecerFormas() {
        aplicarPresetFormas(curvatura = 6f, grosor = 0.8f, estilo = "marcado", espaciado = 14f)
    }

    // --- Personalización de Tipografía y Textos ---
    fun ajustarEscalaTexto(escala: Float) {
        repositorio.ajustes.actualizar { it.copy(escalaTexto = escala) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarPesoTexto(peso: String) {
        repositorio.ajustes.actualizar { it.copy(pesoTexto = peso) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarCursivaTexto(cursiva: Boolean) {
        repositorio.ajustes.actualizar { it.copy(cursivaTexto = cursiva) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoLetras(espaciado: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoLetrasSp = espaciado) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarInterlineadoFactor(factor: Float) {
        repositorio.ajustes.actualizar { it.copy(interlineadoFactor = factor) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarFamiliaFuente(familia: String) {
        repositorio.ajustes.actualizar { it.copy(familiaFuente = familia) }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun aplicarPresetTipografia(
        escala: Float,
        peso: String,
        cursiva: Boolean,
        kerning: Float,
        interlineado: Float,
        familia: String
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                escalaTexto = escala,
                pesoTexto = peso,
                cursivaTexto = cursiva,
                espaciadoLetrasSp = kerning,
                interlineadoFactor = interlineado,
                familiaFuente = familia
            )
        }
        aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun restablecerTipografia() {
        aplicarPresetTipografia(
            escala = 1.0f,
            peso = "normal",
            cursiva = false,
            kerning = 0.0f,
            interlineado = 1.0f,
            familia = "sans"
        )
    }

    fun ajustarTema(clave: String) {
        repositorio.ajustes.actualizar {
            if (it.temaApp != clave) {
                it.copy(
                    temaApp = clave,
                    colorIconosInternos = "",
                    colorTitulos = "",
                    colorTarjetas = "",
                    colorSeguridad = "",
                    colorArgon2 = "",
                    colorCamara = "",
                    color2FA = "",
                    colorPasskeys = "",
                    colorGenerador = "",
                    colorSalud = "",
                    colorPapelera = "",
                    colorExportacion = ""
                )
            } else {
                it.copy(temaApp = clave)
            }
        }
        aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
    }


    fun ajustarRecordatorioExportacion(dias: Int) =
        repositorio.ajustes.actualizar { it.copy(recordatorioExportacionDias = dias) }

    fun ajustarDensidadLista(clave: String) =
        repositorio.ajustes.actualizar { it.copy(densidadLista = clave) }

    fun ajustarAgruparPorSitio(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(agruparPorSitio = activo) }

    fun ajustarTotpManualDigitos(digitos: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualDigitos = digitos) }

    fun ajustarTotpManualPeriodo(periodo: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualPeriodo = periodo) }

    fun ajustarTotpManualAlgoritmo(algoritmo: String) =
        repositorio.ajustes.actualizar { it.copy(totpManualAlgoritmo = algoritmo) }

    fun ajustarTotpSepararDigitos(separar: Boolean) =
        repositorio.ajustes.actualizar { it.copy(totpSepararDigitos = separar) }

    fun ajustarMostrarIndiceAlfabetico(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(mostrarIndiceAlfabetico = activo) }

    fun ajustarIndiceEfectoOla(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceEfectoOla = activo) }

    fun ajustarIndiceAmplitudOlaDp(amplitud: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceAmplitudOlaDp = amplitud) }

    fun ajustarIndiceRadioOlaDp(radio: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceRadioOlaDp = radio) }

    fun ajustarIndiceEscalaLetras(escala: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceEscalaLetras = escala) }

    fun ajustarIndiceMostrarCirculo(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceMostrarCirculo = activo) }

    fun ajustarIndiceTamanoCirculoDp(tamano: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceTamanoCirculoDp = tamano) }

    fun ajustarIndiceOffsetCirculoDp(offset: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceOffsetCirculoDp = offset) }

    fun ajustarIndiceHaptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceHaptica = activo) }

    fun ajustarIndiceAnchoTactilDp(anchoDp: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceAnchoTactilDp = anchoDp) }

    fun ajustarIndiceTonoLetras(tono: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceTonoLetras = tono) }

    fun ajustarIndiceIncluirEnie(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceIncluirEnie = activo) }

    fun ajustarIndiceResaltarEntradas(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceResaltarEntradas = activo) }

    fun ajustarIndiceResaltarSoloPrimera(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceResaltarSoloPrimera = activo) }

    fun restablecerAjustesIndiceAlfabetico() {
        repositorio.ajustes.actualizar {
            it.copy(
                mostrarIndiceAlfabetico = true,
                indiceEfectoOla = true,
                indiceAmplitudOlaDp = 109f,
                indiceRadioOlaDp = 169f,
                indiceEscalaLetras = 1.5f,
                indiceMostrarCirculo = true,
                indiceTamanoCirculoDp = 50f,
                indiceOffsetCirculoDp = 136f,
                indiceHaptica = true,
                indiceAnchoTactilDp = 45f,
                indiceTonoLetras = 80f,
                indiceIncluirEnie = true,
                indiceResaltarEntradas = true,
                indiceResaltarSoloPrimera = true
            )
        }
    }

    fun ajustarFormatoFecha(formato: String) {
        repositorio.ajustes.actualizar { it.copy(formatoFecha = formato) }
        Diagnostico.apuntar("formatos", "Formato de fecha establecido en $formato")
    }

    fun ajustarFormatoHora(formato: String) {
        repositorio.ajustes.actualizar { it.copy(formatoHora = formato) }
        Diagnostico.apuntar("formatos", "Formato de hora establecido en $formato")
    }

    fun ajustarFormatoTelefono(formato: String) {
        repositorio.ajustes.actualizar { it.copy(formatoTelefono = formato) }
        Diagnostico.apuntar("formatos", "Formato de teléfono establecido en $formato")
    }

    fun ajustarSeparadorDecimal(separador: String) {
        repositorio.ajustes.actualizar { it.copy(separadorDecimal = separador) }
        Diagnostico.apuntar("formatos", "Separador decimal establecido en $separador")
    }

    fun ajustarProteccionPantalla(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(proteccionPantalla = activo) }
        val desc = if (activo) "activada" else "desactivada"
        Diagnostico.apuntar("seguridad", "Protección de pantalla (FLAG_SECURE) $desc por el usuario")
    }

    fun ajustarWidgetGrosorBorde(grosor: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetGrosorBordeDp = grosor) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetCurvaturaEsquinas(curvatura: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetCurvaturaEsquinasDp = curvatura) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTransparenciaFondo(transparencia: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTransparenciaFondo = transparencia) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorBorde(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorBorde = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorContador(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorContador = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorCodigo(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorCodigo = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetColorTituloIcono(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorTituloIcono = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetHaptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widgetHaptica = activo) }

    fun ajustarWidgetHapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(widgetHapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun ajustarWidget1x1Haptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Haptica = activo) }

    fun ajustarWidget1x1HapticaIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(widget1x1HapticaIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun ajustarWidget1x1Modo(modo: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Modo = modo) }

    fun ajustarWidget1x1Longitud(longitud: Int) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Longitud = longitud) }

    fun ajustarWidget1x1Patron(patron: String) =
        repositorio.ajustes.actualizar { it.copy(widget1x1Patron = patron) }

    fun ajustarWidget1x1Simbolos(simbolos: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1Simbolos = simbolos) }
    }

    fun ajustarWidget1x1CopiarPortapapeles(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1CopiarPortapapeles = activo) }

    fun ajustarWidget1x1MostrarToast(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(widget1x1MostrarToast = activo) }

    fun ajustarWidget1x1GrosorBorde(grosor: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1GrosorBordeDp = grosor) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1CurvaturaEsquinas(curvatura: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1CurvaturaEsquinasDp = curvatura) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1TransparenciaFondo(transparencia: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1TransparenciaFondo = transparencia) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1Tamano(tamano: Float) {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1TamanoDp = tamano,
                widget1x1AnchoDp = tamano,
                widget1x1AltoDp = tamano
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1Ancho(ancho: Float) {
        repositorio.ajustes.actualizar {
            if (it.widget1x1BloquearProporcion) {
                it.copy(widget1x1AnchoDp = ancho, widget1x1AltoDp = ancho, widget1x1TamanoDp = ancho)
            } else {
                it.copy(widget1x1AnchoDp = ancho, widget1x1TamanoDp = ancho)
            }
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1Alto(alto: Float) {
        repositorio.ajustes.actualizar {
            if (it.widget1x1BloquearProporcion) {
                it.copy(widget1x1AnchoDp = alto, widget1x1AltoDp = alto, widget1x1TamanoDp = alto)
            } else {
                it.copy(widget1x1AltoDp = alto)
            }
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1BloquearProporcion(bloquear: Boolean) {
        repositorio.ajustes.actualizar {
            if (bloquear) {
                it.copy(widget1x1BloquearProporcion = true, widget1x1AltoDp = it.widget1x1AnchoDp)
            } else {
                it.copy(widget1x1BloquearProporcion = false)
            }
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1OffsetX(offset: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1OffsetX = offset) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1OffsetY(offset: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1OffsetY = offset) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1PresetTamano(dp: Float) {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1AnchoDp = dp,
                widget1x1AltoDp = dp,
                widget1x1TamanoDp = dp
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1Alineamiento(alineamiento: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1Alineamiento = alineamiento) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1ColorBorde(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1ColorBorde = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1ColorIcono(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1ColorIcono = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1ColorFondo(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1ColorFondo = colorHex) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun aplicarPresetHonorWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1GrosorBordeDp = 0f,
                widget1x1CurvaturaEsquinasDp = 15f,
                widget1x1AnchoDp = 55f,
                widget1x1AltoDp = 51f,
                widget1x1TamanoDp = 55f,
                widget1x1BloquearProporcion = false,
                widget1x1OffsetX = 0f,
                widget1x1OffsetY = 4f,
                widget1x1ColorBorde = "#33332E",
                widget1x1ColorIcono = "#E6FCFF",
                widget1x1ColorFondo = "#2E3333",
                widget1x1Modo = "aleatoria"
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerAjustesWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1Haptica = true,
                widget1x1HapticaIntensidad = 0.20f,
                widget1x1Modo = "aleatoria",
                widget1x1Longitud = 20,
                widget1x1Patron = "XXXXX-XXXXX-XXXXX-XXXXX",
                widget1x1Simbolos = "!@#$%&*()_-=+[]{}?/,.:;",
                widget1x1CopiarPortapapeles = true,
                widget1x1MostrarToast = true,
                widget1x1GrosorBordeDp = 0f,
                widget1x1CurvaturaEsquinasDp = 15f,
                widget1x1TransparenciaFondo = 1.0f,
                widget1x1TamanoDp = 55f,
                widget1x1AnchoDp = 55f,
                widget1x1AltoDp = 51f,
                widget1x1BloquearProporcion = false,
                widget1x1OffsetX = 0f,
                widget1x1OffsetY = 4f,
                widget1x1Alineamiento = "arriba",
                widget1x1ColorBorde = "#33332E",
                widget1x1ColorIcono = "#E6FCFF",
                widget1x1ColorFondo = "#2E3333"
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerAjustesWidget() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetGrosorBordeDp = 0f,
                widgetCurvaturaEsquinasDp = 0f,
                widgetTransparenciaFondo = 0.50f,
                widgetColorBorde = "#FFB300",
                widgetColorContador = "#FFFFFF",
                widgetColorCodigo = "#FFB300",
                widgetColorTituloIcono = "#FFFFFF",
                widgetHaptica = true,
                widgetHapticaIntensidad = 0.20f
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarHistorialClavesMax(max: Int) {
        repositorio.ajustes.actualizar {
            val recortado = it.historialClaves.take(max.coerceAtLeast(1))
            it.copy(historialClavesMax = max, historialClaves = recortado)
        }
    }

    fun ajustarHistorialClavesVaciadoAuto(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(historialClavesVaciadoAuto = activo) }
    }

    fun ajustarHistorialClavesTiempoAutoDestruccion(ms: Long) {
        repositorio.ajustes.actualizar { it.copy(historialClavesTiempoAutoDestruccion = ms) }
    }

    fun eliminarDeHistorialClaves(id: String) {
        repositorio.ajustes.actualizar { actual ->
            actual.copy(historialClaves = actual.historialClaves.filterNot { it.id == id })
        }
    }

    fun vaciarHistorialClaves() {
        repositorio.ajustes.actualizar { it.copy(historialClaves = emptyList()) }
        Diagnostico.apuntar("generador", "Historial de contraseñas vaciado manualmente")
    }

    fun ajustarBackupAutoFrecuenciaDias(dias: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoFrecuenciaDias = dias) }
        val desc = if (dias == 0) "desactivado" else "cada $dias días"
        Diagnostico.apuntar("backup", "Frecuencia de backup automático establecida en $desc")
    }

    fun ajustarBackupAutoPassword(password: String) {
        repositorio.ajustes.actualizar { it.copy(backupAutoPasswordCifrado = password) }
    }

    fun ajustarBackupAutoPasswordCifrado(password: String) = ajustarBackupAutoPassword(password)

    fun ajustarBackupAutoMaxCopias(max: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoMaxCopias = max) }
    }

    fun ajustarBackupAutoPatronNombre(patron: String) {
        repositorio.ajustes.actualizar { it.copy(backupAutoPatronNombre = patron) }
    }

    fun ajustarBackupAutoSecuencia(secuencia: Int) {
        repositorio.ajustes.actualizar { it.copy(backupAutoSecuencia = secuencia) }
    }

    fun ajustarMostrarIdsAjustes(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(mostrarIdsAjustes = mostrar) }
    }

    fun ajustarAlumbradoActivo(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(alumbradoActivo = activo) }
        AlumbradoActivo = activo
    }

    fun ajustarAlumbradoIntensidad(intensidad: Float) {
        val valor = intensidad.coerceIn(0.1f, 1.0f)
        repositorio.ajustes.actualizar { it.copy(alumbradoIntensidad = valor) }
        AlumbradoIntensidad = valor
    }

    fun ajustarAlumbradoRepeticiones(repeticiones: Int) {
        val valor = repeticiones.coerceIn(1, 5)
        repositorio.ajustes.actualizar { it.copy(alumbradoRepeticiones = valor) }
        AlumbradoRepeticiones = valor
    }

    fun ajustarAlumbradoDuracionMs(duracionMs: Int) {
        val valor = duracionMs.coerceIn(300, 2000)
        repositorio.ajustes.actualizar { it.copy(alumbradoDuracionMs = valor) }
        AlumbradoDuracionMs = valor
    }

    fun ajustarHapticaApp(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(hapticaApp = activo) }

    fun ajustarHapticaAppIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(hapticaAppIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun recargarAjustes() {
        repositorio.ajustes.recargar()
    }
}
