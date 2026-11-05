/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Renderiza un ítem interactivo del menú lateral a partir de su identificador.
 */
@Composable
fun FilaItemMenuLateral(
    id: String,
    totalDuplicadas: Int = 0,
    totalPapelera: Int = 0,
    conteosListaCuentas: Map<String, Int> = emptyMap(),
    mostrarIds: Boolean = false,
    ajustes: AjustesApp? = null,
    alIr: (Pantalla) -> Unit
) {
    if ((id == "04-HER-PSK" || id == "04-HER-PAS" || id == "03-LST-PAS") && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        return
    }

    when (id) {
        "04-HER-GEN" -> ItemMenu(
            texto = "Generar contraseñas",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Generador) }

        "04-HER-HST" -> ItemMenu(
            texto = "Historial de contraseñas",
            icono = Icons.Filled.History,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.HistorialClaves) }

        "03-LST-PAS", "04-HER-PAS", "04-HER-PSK" -> {
            val passkeysCount = conteosListaCuentas["03-LST-PAS"] ?: conteosListaCuentas["04-HER-PAS"] ?: 0
            ItemMenu(
                texto = "Llaves de acceso",
                icono = Icons.Filled.Fingerprint,
                colorIcono = ColorIconosInternos,
                badge = if (passkeysCount > 0) passkeysCount.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Passkeys("03-LST-PAS")) }
        }

        "03-LST-2FA", "04-HER-2FA" -> {
            val totpCount = conteosListaCuentas["03-LST-2FA"] ?: conteosListaCuentas["04-HER-2FA"] ?: 0
            ItemMenu(
                texto = "Verificación en dos pasos",
                icono = Icons.Filled.Timer,
                colorIcono = ColorIconosInternos,
                badge = if (totpCount > 0) totpCount.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Autenticador) }
        }

        "04-HER-QRS" -> ItemMenu(
            texto = "Escáner QR",
            icono = Icons.Filled.QrCodeScanner,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.CamaraQr(null)) }

        "03-LST-SLD" -> {
            val problemasSalud = conteosListaCuentas["03-LST-SLD"] ?: 0
            ItemMenu(
                texto = "Salud",
                icono = Icons.Filled.HealthAndSafety,
                colorIcono = ColorIconosInternos,
                badge = if (problemasSalud > 0) problemasSalud.toString() else null,
                colorBadge = Peligro,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.SaludBoveda) }
        }

        "03-LST-DUP" -> {
            val duplicadas = conteosListaCuentas["03-LST-DUP"] ?: totalDuplicadas
            ItemMenu(
                texto = "Duplicados",
                icono = Icons.Filled.ContentCopy,
                colorIcono = ColorIconosInternos,
                badge = if (duplicadas > 0) duplicadas.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Duplicados) }
        }

        "03-LST-PAP" -> {
            val papelera = conteosListaCuentas["03-LST-PAP"] ?: totalPapelera
            ItemMenu(
                texto = "Papelera",
                icono = Icons.Filled.Delete,
                colorIcono = ColorIconosInternos,
                badge = if (papelera > 0) papelera.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Papelera) }
        }

        "03-LST-IDE" -> {
            val identidadesCount = conteosListaCuentas["03-LST-IDE"] ?: 0
            ItemMenu(
                texto = "Perfiles",
                icono = Icons.Filled.AccountCircle,
                colorIcono = ColorIconosInternos,
                badge = if (identidadesCount > 0) identidadesCount.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Perfiles()) }
        }

        "03-LST-CAT" -> {
            val categoriasCount = conteosListaCuentas["03-LST-CAT"] ?: 0
            ItemMenu(
                texto = "Categorías",
                icono = Icons.Filled.Folder,
                colorIcono = ColorIconosInternos,
                badge = if (categoriasCount > 0) categoriasCount.toString() else null,
                idEtiqueta = id,
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) { alIr(Pantalla.Categorias()) }
        }

        "00-AJU" -> ItemMenu(
            texto = "Ajustes",
            icono = Icons.Filled.Settings,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.Ajustes) }

        "01-SEG-ACC" -> ItemMenu(
            texto = "Auditoría de accesibilidad",
            icono = Icons.Filled.Security,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.AuditoriaAccesibilidad()) }

        "06-SIS-AVZ-TST" -> ItemMenu(
            texto = "Avisos emergentes",
            icono = Icons.Filled.Notifications,
            colorIcono = ColorIconosInternos,
            idEtiqueta = id,
            mostrarId = mostrarIds,
            ajustes = ajustes
        ) { alIr(Pantalla.AjustesAvisosEmergentes()) }

        else -> {
            val nodo = MapaAjustes.buscarPorId(id)
            if (nodo != null) {
                val (icono, _) = MapaAjustes.resolverIconoYColor(nodo)
                val conteo = conteosListaCuentas[id]
                ItemMenu(
                    texto = nodo.titulo,
                    icono = icono,
                    colorIcono = ColorIconosInternos,
                    badge = if (conteo != null && conteo > 0) conteo.toString() else null,
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
