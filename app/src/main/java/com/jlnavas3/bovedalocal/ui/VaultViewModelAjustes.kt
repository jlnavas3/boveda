package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesDefaults
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
import com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos

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
                engranajesVelocidad = AjustesDefaults.Animacion.Engranajes.VELOCIDAD,
                engranajesGrosorBorde = AjustesDefaults.Animacion.Engranajes.GROSOR_BORDE,
                engranajesAlturaDientes = AjustesDefaults.Animacion.Engranajes.ALTURA_DIENTES,
                engranajesAnchoDientes = AjustesDefaults.Animacion.Engranajes.ANCHO_DIENTES,
                engranajesGrosorRadios = AjustesDefaults.Animacion.Engranajes.GROSOR_RADIOS,
                engranajesCurvaturaRadios = AjustesDefaults.Animacion.Engranajes.CURVATURA_RADIOS,
                engranajesCantidadRadios = AjustesDefaults.Animacion.Engranajes.CANTIDAD_RADIOS,
                engranajesRadioInterior = AjustesDefaults.Animacion.Engranajes.RADIO_INTERIOR,
                engranajesTamanoEje = AjustesDefaults.Animacion.Engranajes.TAMANO_EJE,
                engranajesSombraIntensidad = AjustesDefaults.Animacion.Engranajes.SOMBRA_INTENSIDAD,
                engranajesColorBrillo = AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
                engranajesColorPrincipal = AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
                engranajesColorSombraMedio = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
                engranajesColorSombraOscuro = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
                engranajesColorBisel = AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
                engranajesColorInterior = AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
                engranajesColorCubo = AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
                engranajesColorEje = AjustesDefaults.Animacion.Engranajes.COLOR_EJE
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
                puertaVelocidad = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
                puertaGrosorAnillos = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS,
                puertaColor = AjustesDefaults.Animacion.Puerta.COLOR
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
                colorAcento = AjustesDefaults.Tema.COLOR_ACENTO,
                colorIconosInternos = AjustesDefaults.Tema.COLOR_ICONOS_INTERNOS,
                colorTitulos = AjustesDefaults.Tema.COLOR_TITULOS,
                colorTarjetas = AjustesDefaults.Tema.COLOR_TARJETAS,
                colorDinamicoSistema = AjustesDefaults.Tema.COLOR_DINAMICO_SISTEMA,
                colorSeguridad = AjustesDefaults.ColoresSecciones.SEGURIDAD,
                colorArgon2 = AjustesDefaults.ColoresSecciones.ARGON2,
                colorCamara = AjustesDefaults.ColoresSecciones.CAMARA,
                color2FA = AjustesDefaults.ColoresSecciones.DOS_FA,
                colorPasskeys = AjustesDefaults.ColoresSecciones.PASSKEYS,
                colorGenerador = AjustesDefaults.ColoresSecciones.GENERADOR,
                colorSalud = AjustesDefaults.ColoresSecciones.SALUD,
                colorPapelera = AjustesDefaults.ColoresSecciones.PAPELERA,
                colorExportacion = AjustesDefaults.ColoresSecciones.EXPORTACION
            )
        }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun alternarColorDinamicoSistema(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(colorDinamicoSistema = activo) }
        aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
    }

    // --- Personalización de Widgets 1x1, TOTP y Tile ---
    fun ajustarWidget1x1DicewarePalabras(palabras: Int) {
        repositorio.ajustes.actualizar { it.copy(widget1x1DicewarePalabras = palabras) }
    }
    fun ajustarWidget1x1DicewareSeparador(separador: String) {
        repositorio.ajustes.actualizar { it.copy(widget1x1DicewareSeparador = separador) }
    }
    fun ajustarWidgetColorFilas(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(widgetColorFilas = colorHex) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }
    fun ajustarWidgetTransparenciaFilas(transparencia: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTransparenciaFilas = transparencia) }
        WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }
    fun ajustarTileSimbolos(simbolos: String) {
        repositorio.ajustes.actualizar { it.copy(tileSimbolos = simbolos) }
    }
    fun ajustarTileDicewarePalabras(palabras: Int) {
        repositorio.ajustes.actualizar { it.copy(tileDicewarePalabras = palabras) }
    }
    fun ajustarTileDicewareSeparador(separador: String) {
        repositorio.ajustes.actualizar { it.copy(tileDicewareSeparador = separador) }
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
        aplicarPresetFormas(
            curvatura = AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP,
            grosor = AjustesDefaults.Formas.GROSOR_BORDE_DP,
            estilo = AjustesDefaults.Formas.ESTILO_BORDE,
            espaciado = AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP
        )
    }

    fun restablecerCurvaturaEsquinas() = ajustarCurvaturaEsquinas(AjustesDefaults.Formas.CURVATURA_ESQUINAS_DP)
    fun restablecerGrosorBorde() = ajustarGrosorBorde(AjustesDefaults.Formas.GROSOR_BORDE_DP)
    fun restablecerEstiloBorde() = ajustarEstiloBorde(AjustesDefaults.Formas.ESTILO_BORDE)
    fun restablecerEspaciadoComponentes() = ajustarEspaciadoComponentes(AjustesDefaults.Formas.ESPACIADO_COMPONENTES_DP)

    // --- Personalización de Colores de Datos e Indicadores ---
    fun ajustarMostrarIndicadoresContenido(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(mostrarIndicadoresContenido = mostrar) }
    }

    fun ajustarColorDatosUsuario(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatosUsuario = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorDatosContrasena(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatosContrasena = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorDatos2FA(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatos2FA = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorDatosPasskey(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatosPasskey = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorDatosWeb(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatosWeb = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorDatosApp(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorDatosApp = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun restablecerColoresDatos() {
        repositorio.ajustes.actualizar {
            it.copy(
                mostrarIndicadoresContenido = AjustesDefaults.ColoresDatos.MOSTRAR_INDICADORES,
                colorDatosUsuario = AjustesDefaults.ColoresDatos.USUARIO,
                colorDatosContrasena = AjustesDefaults.ColoresDatos.CONTRASENA,
                colorDatos2FA = AjustesDefaults.ColoresDatos.DOS_FA,
                colorDatosPasskey = AjustesDefaults.ColoresDatos.PASSKEY,
                colorDatosWeb = AjustesDefaults.ColoresDatos.WEB,
                colorDatosApp = AjustesDefaults.ColoresDatos.APP
            )
        }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    // --- Personalización de Colores por Bloque de Identificadores ---
    fun ajustarColorIdSeguridad(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdSeguridad = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIdApariencia(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdApariencia = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIdLista(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdLista = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIdHerramientas(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdHerramientas = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIdCopias(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdCopias = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIdSistema(colorHex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIdSistema = colorHex) }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun restablecerColoresIds() {
        repositorio.ajustes.actualizar {
            it.copy(
                colorIdSeguridad = AjustesDefaults.ColoresIds.SEGURIDAD,
                colorIdApariencia = AjustesDefaults.ColoresIds.APARIENCIA,
                colorIdLista = AjustesDefaults.ColoresIds.LISTA,
                colorIdHerramientas = AjustesDefaults.ColoresIds.HERRAMIENTAS,
                colorIdCopias = AjustesDefaults.ColoresIds.COPIAS,
                colorIdSistema = AjustesDefaults.ColoresIds.SISTEMA
            )
        }
        aplicarPersonalizacionColores(repositorio.ajustes.actual)
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
            escala = AjustesDefaults.Tipografia.ESCALA_TEXTO,
            peso = AjustesDefaults.Tipografia.PESO_TEXTO,
            cursiva = AjustesDefaults.Tipografia.CURSIVA_TEXTO,
            kerning = AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP,
            interlineado = AjustesDefaults.Tipografia.INTERLINEADO_FACTOR,
            familia = AjustesDefaults.Tipografia.FAMILIA_FUENTE
        )
    }

    fun restablecerEscalaTexto() = ajustarEscalaTexto(AjustesDefaults.Tipografia.ESCALA_TEXTO)
    fun restablecerKerning() = ajustarEspaciadoLetras(AjustesDefaults.Tipografia.ESPACIADO_LETRAS_SP)
    fun restablecerInterlineado() = ajustarInterlineadoFactor(AjustesDefaults.Tipografia.INTERLINEADO_FACTOR)
    fun restablecerPesoTexto() {
        ajustarPesoTexto(AjustesDefaults.Tipografia.PESO_TEXTO)
        ajustarCursivaTexto(AjustesDefaults.Tipografia.CURSIVA_TEXTO)
    }
    fun restablecerFamiliaFuente() = ajustarFamiliaFuente(AjustesDefaults.Tipografia.FAMILIA_FUENTE)

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

    fun ajustarIndiceAlinearConCresta(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceAlinearConCresta = activo) }

    fun restablecerAjustesIndiceAlfabetico() {
        repositorio.ajustes.actualizar {
            it.copy(
                mostrarIndiceAlfabetico = AjustesDefaults.Indice.MOSTRAR,
                indiceEfectoOla = AjustesDefaults.Indice.EFECTO_OLA,
                indiceAmplitudOlaDp = AjustesDefaults.Indice.AMPLITUD_OLA_DP,
                indiceRadioOlaDp = AjustesDefaults.Indice.RADIO_OLA_DP,
                indiceEscalaLetras = AjustesDefaults.Indice.ESCALA_LETRAS,
                indiceMostrarCirculo = AjustesDefaults.Indice.MOSTRAR_CIRCULO,
                indiceTamanoCirculoDp = AjustesDefaults.Indice.TAMANO_CIRCULO_DP,
                indiceOffsetCirculoDp = AjustesDefaults.Indice.OFFSET_CIRCULO_DP,
                indiceHaptica = AjustesDefaults.Indice.HAPTICA,
                indiceAnchoTactilDp = AjustesDefaults.Indice.ANCHO_TACTIL_DP,
                indiceTonoLetras = AjustesDefaults.Indice.TONO_LETRAS,
                indiceIncluirEnie = AjustesDefaults.Indice.INCLUIR_ENIE,
                indiceResaltarEntradas = AjustesDefaults.Indice.RESALTAR_ENTRADAS,
                indiceResaltarSoloPrimera = AjustesDefaults.Indice.RESALTAR_SOLO_PRIMERA,
                indiceAlinearConCresta = AjustesDefaults.Indice.ALINEAR_CON_CRESTA
            )
        }
    }

    fun restablecerAmplitudOla() = ajustarIndiceAmplitudOlaDp(AjustesDefaults.Indice.AMPLITUD_OLA_DP)
    fun restablecerRadioOla() = ajustarIndiceRadioOlaDp(AjustesDefaults.Indice.RADIO_OLA_DP)
    fun restablecerEscalaLetrasIndice() = ajustarIndiceEscalaLetras(AjustesDefaults.Indice.ESCALA_LETRAS)
    fun restablecerAnchoTactilIndice() = ajustarIndiceAnchoTactilDp(AjustesDefaults.Indice.ANCHO_TACTIL_DP)
    fun restablecerTonoLetrasIndice() = ajustarIndiceTonoLetras(AjustesDefaults.Indice.TONO_LETRAS)
    fun restablecerTamanoCirculoIndice() = ajustarIndiceTamanoCirculoDp(AjustesDefaults.Indice.TAMANO_CIRCULO_DP)
    fun restablecerOffsetCirculoIndice() = ajustarIndiceOffsetCirculoDp(AjustesDefaults.Indice.OFFSET_CIRCULO_DP)

    fun restablecerFormatos() {
        repositorio.ajustes.actualizar {
            it.copy(
                formatoFecha = AjustesDefaults.ListaFormatos.FORMATO_FECHA,
                formatoHora = AjustesDefaults.ListaFormatos.FORMATO_HORA,
                formatoTelefono = AjustesDefaults.ListaFormatos.FORMATO_TELEFONO,
                separadorDecimal = AjustesDefaults.ListaFormatos.SEPARADOR_DECIMAL
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

    fun ajustarWidgetTotpVidrioEsmerilado(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpVidrioEsmerilado = activo) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTotpEsmeriladoIntensidad(intensidad: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpEsmeriladoIntensidad = intensidad) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun ajustarWidgetTotpEsmeriladoLuz(luz: Float) {
        repositorio.ajustes.actualizar { it.copy(widgetTotpEsmeriladoLuz = luz) }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun aplicarPresetEstiloWidgetTotp(
        curvaturaDp: Float,
        grosorDp: Float,
        transparenciaFondo: Float,
        transparenciaFilas: Float,
        colorBorde: String,
        colorCodigo: String,
        colorContador: String,
        colorTitulo: String,
        colorFilas: String,
        vidrioEsmerilado: Boolean = false,
        esmeriladoIntensidad: Float = 0.60f
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetCurvaturaEsquinasDp = curvaturaDp,
                widgetGrosorBordeDp = grosorDp,
                widgetTransparenciaFondo = transparenciaFondo,
                widgetTransparenciaFilas = transparenciaFilas,
                widgetColorBorde = colorBorde,
                widgetColorCodigo = colorCodigo,
                widgetColorContador = colorContador,
                widgetColorTituloIcono = colorTitulo,
                widgetColorFilas = colorFilas,
                widgetTotpVidrioEsmerilado = vidrioEsmerilado,
                widgetTotpEsmeriladoIntensidad = esmeriladoIntensidad
            )
        }
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

    fun ajustarWidget1x1VidrioEsmerilado(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(widget1x1VidrioEsmerilado = activo) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1EsmeriladoIntensidad(intensidad: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1EsmeriladoIntensidad = intensidad) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun ajustarWidget1x1EsmeriladoLuz(luz: Float) {
        repositorio.ajustes.actualizar { it.copy(widget1x1EsmeriladoLuz = luz) }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun aplicarPresetEstiloWidget1x1(
        curvaturaDp: Float,
        grosorDp: Float,
        transparenciaFondo: Float,
        colorFondo: String,
        colorBorde: String,
        colorIcono: String,
        vidrioEsmerilado: Boolean = false,
        esmeriladoIntensidad: Float = 0.60f,
        bloquearProporcion: Boolean? = null
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1CurvaturaEsquinasDp = curvaturaDp,
                widget1x1GrosorBordeDp = grosorDp,
                widget1x1TransparenciaFondo = transparenciaFondo,
                widget1x1ColorFondo = colorFondo,
                widget1x1ColorBorde = colorBorde,
                widget1x1ColorIcono = colorIcono,
                widget1x1VidrioEsmerilado = vidrioEsmerilado,
                widget1x1EsmeriladoIntensidad = esmeriladoIntensidad,
                widget1x1BloquearProporcion = bloquearProporcion ?: it.widget1x1BloquearProporcion
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun aplicarPresetHonorWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
                widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
                widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
                widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
                widget1x1TamanoDp = AjustesDefaults.Widget1x1.TAMANO_DP,
                widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
                widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X,
                widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
                widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
                widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
                widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO,
                widget1x1Modo = AjustesDefaults.Widget1x1.MODO
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerAjustesWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1Haptica = AjustesDefaults.Widget1x1.HAPTICA,
                widget1x1HapticaIntensidad = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD,
                widget1x1Modo = AjustesDefaults.Widget1x1.MODO,
                widget1x1Longitud = AjustesDefaults.Widget1x1.LONGITUD,
                widget1x1Patron = AjustesDefaults.Widget1x1.PATRON,
                widget1x1Simbolos = AjustesDefaults.Widget1x1.SIMBOLOS,
                widget1x1CopiarPortapapeles = AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES,
                widget1x1MostrarToast = AjustesDefaults.Widget1x1.MOSTRAR_TOAST,
                widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
                widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
                widget1x1TransparenciaFondo = AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO,
                widget1x1TamanoDp = AjustesDefaults.Widget1x1.TAMANO_DP,
                widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
                widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
                widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
                widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X,
                widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
                widget1x1Alineamiento = AjustesDefaults.Widget1x1.ALINEAMIENTO,
                widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
                widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
                widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO,
                widget1x1DicewarePalabras = AjustesDefaults.Widget1x1.DICEWARE_PALABRAS,
                widget1x1DicewareSeparador = AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerAjustesWidget() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetGrosorBordeDp = AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP,
                widgetCurvaturaEsquinasDp = AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP,
                widgetTransparenciaFondo = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO,
                widgetColorBorde = AjustesDefaults.WidgetTotp.COLOR_BORDE,
                widgetColorContador = AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
                widgetColorCodigo = AjustesDefaults.WidgetTotp.COLOR_CODIGO,
                widgetColorTituloIcono = AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
                widgetColorFilas = AjustesDefaults.WidgetTotp.COLOR_FILAS,
                widgetTransparenciaFilas = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS,
                widgetHaptica = AjustesDefaults.WidgetTotp.HAPTICA,
                widgetHapticaIntensidad = AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun restablecerColoresWidgetTotp() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetColorBorde = AjustesDefaults.WidgetTotp.COLOR_BORDE,
                widgetColorContador = AjustesDefaults.WidgetTotp.COLOR_CONTADOR,
                widgetColorCodigo = AjustesDefaults.WidgetTotp.COLOR_CODIGO,
                widgetColorTituloIcono = AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO,
                widgetColorFilas = AjustesDefaults.WidgetTotp.COLOR_FILAS
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun restablecerFormaWidgetTotp() {
        repositorio.ajustes.actualizar {
            it.copy(
                widgetGrosorBordeDp = AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP,
                widgetCurvaturaEsquinasDp = AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP,
                widgetTransparenciaFondo = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO,
                widgetTransparenciaFilas = AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetTotpFavoritos.actualizarTodos(obtenerApp())
    }

    fun restablecerAspectoWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1GrosorBordeDp = AjustesDefaults.Widget1x1.GROSOR_BORDE_DP,
                widget1x1CurvaturaEsquinasDp = AjustesDefaults.Widget1x1.CURVATURA_ESQUINAS_DP,
                widget1x1TransparenciaFondo = AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerDimensionesWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1AnchoDp = AjustesDefaults.Widget1x1.ANCHO_DP,
                widget1x1AltoDp = AjustesDefaults.Widget1x1.ALTO_DP,
                widget1x1BloquearProporcion = AjustesDefaults.Widget1x1.BLOQUEAR_PROPORCION,
                widget1x1Alineamiento = AjustesDefaults.Widget1x1.ALINEAMIENTO,
                widget1x1OffsetY = AjustesDefaults.Widget1x1.OFFSET_Y,
                widget1x1OffsetX = AjustesDefaults.Widget1x1.OFFSET_X
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerModoGeneracionWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1Modo = AjustesDefaults.Widget1x1.MODO,
                widget1x1Longitud = AjustesDefaults.Widget1x1.LONGITUD,
                widget1x1Simbolos = AjustesDefaults.Widget1x1.SIMBOLOS,
                widget1x1DicewarePalabras = AjustesDefaults.Widget1x1.DICEWARE_PALABRAS,
                widget1x1DicewareSeparador = AjustesDefaults.Widget1x1.DICEWARE_SEPARADOR,
                widget1x1Patron = AjustesDefaults.Widget1x1.PATRON
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerComportamientoWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1CopiarPortapapeles = AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES,
                widget1x1MostrarToast = AjustesDefaults.Widget1x1.MOSTRAR_TOAST,
                widget1x1Haptica = AjustesDefaults.Widget1x1.HAPTICA,
                widget1x1HapticaIntensidad = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerColoresWidget1x1() {
        repositorio.ajustes.actualizar {
            it.copy(
                widget1x1ColorBorde = AjustesDefaults.Widget1x1.COLOR_BORDE,
                widget1x1ColorIcono = AjustesDefaults.Widget1x1.COLOR_ICONO,
                widget1x1ColorFondo = AjustesDefaults.Widget1x1.COLOR_FONDO
            )
        }
        com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido.actualizarTodos(obtenerApp())
    }

    fun restablecerGeometriaPuerta() {
        repositorio.ajustes.actualizar {
            it.copy(
                puertaVelocidad = AjustesDefaults.Animacion.Puerta.VELOCIDAD,
                puertaGrosorAnillos = AjustesDefaults.Animacion.Puerta.GROSOR_ANILLOS
            )
        }
    }

    fun restablecerColorPuerta() = ajustarPuertaColor(AjustesDefaults.Animacion.Puerta.COLOR)

    fun restablecerOrganizacionLista() {
        repositorio.ajustes.actualizar {
            it.copy(
                agruparPorSitio = AjustesDefaults.ListaFormatos.AGRUPAR_POR_SITIO,
                mostrarIndicadoresContenido = AjustesDefaults.ColoresDatos.MOSTRAR_INDICADORES,
                densidadLista = AjustesDefaults.ListaFormatos.DENSIDAD_LISTA,
                criterioOrdenacion = AjustesDefaults.ListaFormatos.CRITERIO_ORDENACION
            )
        }
    }

    fun restablecerAjustesBackup() {
        repositorio.ajustes.actualizar {
            it.copy(
                backupAutoFrecuenciaDias = AjustesDefaults.HistorialCopias.BACKUP_AUTO_FRECUENCIA_DIAS,
                backupAutoMaxCopias = AjustesDefaults.HistorialCopias.BACKUP_AUTO_MAX_COPIAS,
                backupAutoPatronNombre = AjustesDefaults.HistorialCopias.BACKUP_AUTO_PATRON_NOMBRE
            )
        }
    }

    fun restablecerTotpManual() {
        repositorio.ajustes.actualizar {
            it.copy(
                totpManualDigitos = AjustesDefaults.TotpManual.DIGITOS,
                totpManualPeriodo = AjustesDefaults.TotpManual.PERIODO,
                totpManualAlgoritmo = AjustesDefaults.TotpManual.ALGORITMO,
                totpSepararDigitos = AjustesDefaults.TotpManual.SEPARAR_DIGITOS
            )
        }
    }

    fun restablecerTile() {
        repositorio.ajustes.actualizar {
            it.copy(
                tileModo = AjustesDefaults.Tile.MODO,
                tileLongitud = AjustesDefaults.Tile.LONGITUD,
                tilePatron = AjustesDefaults.Tile.PATRON,
                tileSimbolos = AjustesDefaults.Tile.SIMBOLOS,
                tileDicewarePalabras = AjustesDefaults.Tile.DICEWARE_PALABRAS,
                tileDicewareSeparador = AjustesDefaults.Tile.DICEWARE_SEPARADOR,
                tileCopiarPortapapeles = AjustesDefaults.Tile.COPIAR_PORTAPAPELES,
                tileMostrarToast = AjustesDefaults.Tile.MOSTRAR_TOAST,
                tileHaptica = AjustesDefaults.Tile.HAPTICA,
                tileHapticaIntensidad = AjustesDefaults.Tile.HAPTICA_INTENSIDAD
            )
        }
    }

    fun restablecerHapticaApp() {
        repositorio.ajustes.actualizar {
            it.copy(
                hapticaApp = AjustesDefaults.Interaccion.HAPTICA_APP,
                hapticaAppIntensidad = AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD
            )
        }
    }

    fun restablecerAlumbrado() {
        repositorio.ajustes.actualizar {
            it.copy(
                alumbradoActivo = AjustesDefaults.Interaccion.ALUMBRADO_ACTIVO,
                alumbradoIntensidad = AjustesDefaults.Interaccion.ALUMBRADO_INTENSIDAD,
                alumbradoRepeticiones = AjustesDefaults.Interaccion.ALUMBRADO_REPETICIONES,
                alumbradoDuracionMs = AjustesDefaults.Interaccion.ALUMBRADO_DURACION_MS
            )
        }
    }

    fun restablecerBloqueoApp() {
        ajustarAutoBloqueo(AjustesDefaults.Seguridad.AUTO_BLOQUEO_SEGUNDOS)
        ajustarProteccionPantalla(AjustesDefaults.Seguridad.PROTECCION_PANTALLA)
    }
    fun restablecerPortapapeles() = ajustarPortapapeles(AjustesDefaults.Seguridad.PORTAPAPELES_SEGUNDOS)
    fun restablecerCamara() = ajustarMotorCamara(AjustesDefaults.Seguridad.MOTOR_CAMARA)
    fun restablecerArgon2() = repositorio.ajustes.actualizar { it.copy(perfilArgon2 = AjustesDefaults.Seguridad.PERFIL_ARGON2) }
    fun restablecerIconoLauncher() = ajustarIconoLauncher(AjustesDefaults.Tema.ICONO_LAUNCHER)
    fun restablecerAnimacionDesbloqueo() = ajustarAnimacionDesbloqueo(AjustesDefaults.Animacion.TIPO_DESBLOQUEO)

    fun restablecerHistorialClavesConfig() {
        repositorio.ajustes.actualizar {
            it.copy(
                historialClavesMax = AjustesDefaults.HistorialCopias.HISTORIAL_MAX,
                historialClavesVaciadoAuto = AjustesDefaults.HistorialCopias.HISTORIAL_VACIADO_AUTO,
                historialClavesTiempoAutoDestruccion = AjustesDefaults.HistorialCopias.HISTORIAL_TIEMPO_AUTO_DESTRUCCION_MS
            )
        }
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
