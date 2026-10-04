package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.aplicarPaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto
import com.jlnavas3.bovedalocal.util.CambiadorIcono

/**
 * Sub-delegado especializado en temas globales, acentos, paletas y personalización cromática.
 */
interface VaultAjustesColoresTemaDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

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

    fun alternarColorDinamicoSistema(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(colorDinamicoSistema = activo) }
        aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
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
}
