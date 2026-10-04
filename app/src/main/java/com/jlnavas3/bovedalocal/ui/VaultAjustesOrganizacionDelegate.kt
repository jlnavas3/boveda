package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en organización de listas, índice alfabético y formatos numéricos/regionales.
 */
interface VaultAjustesOrganizacionDelegate {
    val repositorio: VaultRepository

    fun ajustarRecordatorioExportacion(dias: Int) =
        repositorio.ajustes.actualizar { it.copy(recordatorioExportacionDias = dias) }

    fun ajustarDensidadLista(clave: String) =
        repositorio.ajustes.actualizar { it.copy(densidadLista = clave) }

    fun ajustarAgruparPorSitio(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(agruparPorSitio = activo) }

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

    // --- Índice Alfabético ---
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

    // --- Formatos Regionales ---
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
}
