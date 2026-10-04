package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.SelectAll
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

val CATEGORIAS_OPCIONES_REGISTRO: List<CategoriaOpcion> = listOf(
    CategoriaOpcion("Todos", "Todos los eventos del sistema", Icons.Filled.SelectAll, ColorAcento),
    CategoriaOpcion("Bóveda", "Apertura, cifrado, cambios de clave y entradas", Icons.Filled.Lock, Menta),
    CategoriaOpcion("Papelera", "Entradas eliminadas, restauradas y vaciado", Icons.Filled.Delete, ColorPapelera),
    CategoriaOpcion("Portapapeles", "Elementos copiados y vaciado automático", Icons.Filled.ContentCopy, Advertencia),
    CategoriaOpcion("2FA", "Códigos TOTP, sincronización y doble factor", Icons.Filled.Password, Color2FA),
    CategoriaOpcion("Huella", "Autenticación biométrica y Keystore de Android", Icons.Filled.Fingerprint, ColorSeguridad),
    CategoriaOpcion("Cámara", "Escaneo de QR y motores de cámara", Icons.Filled.CameraAlt, ColorAcento),
    CategoriaOpcion("Autofill", "Autocompletado, Passkeys y Credential Manager", Icons.Filled.Description, ColorPasskeys),
    CategoriaOpcion("Errores", "Fallos, excepciones y anomalías", Icons.Filled.ErrorOutline, Peligro)
)

/**
 * Parsea las líneas crudas del registro de eventos en objetos estructurados EventoRegistro.
 */
fun parsearEventosRegistro(lineas: List<String>): List<EventoRegistro> {
    return lineas.map { linea ->
        val timestamp = if (linea.length >= 14) linea.take(14) else ""
        val resto = if (linea.length > 15) linea.drop(15) else linea
        val area = if (resto.contains(':')) resto.substringBefore(':').trim() else "app"
        val mensaje = if (resto.contains(':')) resto.substringAfter(':').trim() else resto
        val esError = linea.contains("error", ignoreCase = true) ||
            linea.contains("fallo", ignoreCase = true) ||
            linea.contains("exception", ignoreCase = true) ||
            linea.contains("[NO]", ignoreCase = true)
        EventoRegistro(
            timestamp = timestamp,
            area = area,
            mensaje = mensaje,
            esError = esError,
            textoCompleto = linea
        )
    }
}

/**
 * Filtra y ordena los eventos según la categoría seleccionada, la consulta de texto y el orden.
 */
fun filtrarYOrdenarEventos(
    eventos: List<EventoRegistro>,
    filtroTexto: String,
    categoriaSeleccionada: String,
    criterioOrden: CriterioOrdenRegistro
): List<EventoRegistro> {
    val filtrados = eventos.filter { ev ->
        val coincideCategoria = when (categoriaSeleccionada) {
            "Todos" -> true
            "Bóveda" -> ev.area.equals("bóveda", ignoreCase = true) ||
                ev.area.equals("boveda", ignoreCase = true)
            "Papelera" -> ev.area.contains("papelera", ignoreCase = true) ||
                ev.mensaje.contains("papelera", ignoreCase = true)
            "Portapapeles" -> ev.area.contains("portapapeles", ignoreCase = true) ||
                ev.mensaje.contains("copiad", ignoreCase = true) ||
                ev.mensaje.contains("portapapeles", ignoreCase = true)
            "2FA" -> ev.area.contains("2fa", ignoreCase = true) ||
                ev.area.contains("totp", ignoreCase = true) ||
                ev.mensaje.contains("2fa", ignoreCase = true) ||
                ev.mensaje.contains("totp", ignoreCase = true) ||
                ev.mensaje.contains("doble factor", ignoreCase = true)
            "Huella" -> ev.area.contains("huella", ignoreCase = true) ||
                ev.area.contains("keystore", ignoreCase = true) ||
                ev.mensaje.contains("biometr", ignoreCase = true)
            "Cámara" -> ev.area.contains("camara", ignoreCase = true) ||
                ev.area.contains("cámara", ignoreCase = true) ||
                ev.area.contains("qr", ignoreCase = true) ||
                ev.mensaje.contains("motor", ignoreCase = true)
            "Autofill" -> ev.area.contains("autofill", ignoreCase = true) ||
                ev.area.contains("credential", ignoreCase = true) ||
                ev.area.contains("passkey", ignoreCase = true) ||
                ev.mensaje.contains("relleno", ignoreCase = true) ||
                ev.mensaje.contains("passkey", ignoreCase = true)
            "Errores" -> ev.esError
            else -> true
        }
        val coincideTexto = if (filtroTexto.isBlank()) true else {
            ev.textoCompleto.contains(filtroTexto, ignoreCase = true)
        }
        coincideCategoria && coincideTexto
    }

    return when (criterioOrden) {
        CriterioOrdenRegistro.RECIENTES -> filtrados.reversed()
        CriterioOrdenRegistro.ANTIGUOS -> filtrados
        CriterioOrdenRegistro.AREA_AZ -> filtrados.sortedWith(
            compareBy({ it.area.lowercase() }, { it.timestamp })
        )
        CriterioOrdenRegistro.AREA_ZA -> filtrados.sortedWith(
            compareByDescending<EventoRegistro> { it.area.lowercase() }.thenByDescending { it.timestamp }
        )
    }
}
