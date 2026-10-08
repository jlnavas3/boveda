package com.jlnavas3.bovedalocal.ui.pantallas.lista

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Renderiza un ítem interactivo del menú lateral a partir de su identificador.
 */
@Composable
fun FilaItemMenuLateral(
    id: String,
    totalDuplicadas: Int = 0,
    totalPapelera: Int = 0,
    mostrarIds: Boolean = false,
    ajustes: AjustesApp? = null,
    alIr: (Pantalla) -> Unit
) {
    if (id == "04-HER-PSK" && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        return
    }

    when (id) {
        "04-HER-GEN" -> ItemMenu(
            texto = "Generar contraseñas",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorGenerador,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Generador) }

        "04-HER-HST" -> ItemMenu(
            texto = "Historial de contraseñas",
            icono = Icons.Filled.History,
            colorIcono = ColorGenerador,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.HistorialClaves) }

        "04-HER-PSK" -> ItemMenu(
            texto = "Llaves de paso",
            icono = Icons.Filled.Fingerprint,
            colorIcono = ColorPasskeys,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Passkeys) }

        "04-HER-2FA" -> ItemMenu(
            texto = "Verificación en dos pasos",
            icono = Icons.Filled.Timer,
            colorIcono = Color2FA,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Autenticador) }

        "03-LST-SLD" -> ItemMenu(
            texto = "Salud",
            icono = Icons.Filled.HealthAndSafety,
            colorIcono = ColorSalud,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.SaludBoveda) }

        "03-LST-DUP" -> ItemMenu(
            texto = "Duplicados",
            icono = Icons.Filled.ContentCopy,
            colorIcono = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
            badge = if (totalDuplicadas > 0) totalDuplicadas.toString() else null,
            colorBadge = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Duplicados) }

        "03-LST-PAP" -> ItemMenu(
            texto = "Papelera",
            icono = Icons.Filled.Delete,
            colorIcono = if (totalPapelera > 0) ColorPapelera else ColorIconosInternos,
            badge = if (totalPapelera > 0) totalPapelera.toString() else null,
            colorBadge = ColorAcento,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Papelera) }

        "03-LST-IDE" -> ItemMenu(
            texto = "Identidades",
            icono = Icons.Filled.AccountCircle,
            colorIcono = Color(0xFF0284C7),
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Identidades()) }

        "03-LST-CAT" -> ItemMenu(
            texto = "Categorías",
            icono = Icons.Filled.Folder,
            colorIcono = Color(0xFF10B981),
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Categorias()) }

        "00-AJU" -> ItemMenu(
            texto = "Ajustes",
            icono = Icons.Filled.Settings,
            colorIcono = Color(0xFF546E7A),
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Ajustes) }

        else -> {
            val nodo = MapaAjustes.buscarPorId(id)
            if (nodo != null) {
                val (icono, color) = MapaAjustes.resolverIconoYColor(nodo)
                ItemMenu(
                    texto = nodo.titulo,
                    icono = icono,
                    colorIcono = color,
                    idEtiqueta = id,
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) {
                    alIr(nodo.pantallaDestino ?: Pantalla.Ajustes(nodo.id))
                }
            }
        }
    }
}
