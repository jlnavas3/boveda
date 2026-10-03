package com.jlnavas3.bovedalocal.ui.pantallas.lista

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Menú lateral rediseñado estilo MagicOS / Samsung One UI:
 * - Tarjetas agrupadas con esquinas redondeadas y sin divisores duros.
 * - Iconos en contenedores redondeados con colores temáticos por sección.
 * - Badges numéricos modernos con píldoras de contraste suave.
 * - Acción de bloqueo dedicada y pie de página con badges de seguridad.
 */
@Composable
fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    totalDuplicadas: Int = 0,
    perfilArgon2: PerfilArgon2 = PerfilArgon2.ESTANDAR,
    mostrarIds: Boolean = false,
    ajustes: AjustesApp? = null,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit = {}
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val topAjusted = (topInset - 24.dp).coerceAtLeast(8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(start = 14.dp, end = 14.dp, top = topAjusted, bottom = 10.dp)
    ) {
        // Cabecera destacada estilo MagicOS / One UI
        CabeceraMenuLateral(nombreApp = nombreApp)

        Spacer(Modifier.height(10.dp))

        // Contenido scrolleable agrupado en tarjetas suaves
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Grupo 1: Herramientas
            GrupoMenuLateral(
                titulo = "Herramientas",
                idEtiqueta = "04-HER",
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) {
                ItemMenu(
                    texto = "Generar contraseñas",
                    icono = Icons.Filled.AutoAwesome,
                    colorIcono = ColorGenerador,
                    idEtiqueta = "04-HER-GEN",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.Generador) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Historial de contraseñas",
                    icono = Icons.Filled.History,
                    colorIcono = ColorGenerador,
                    idEtiqueta = "04-HER-HST",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.HistorialClaves) }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    SeparadorItemMenu()
                    ItemMenu(
                        texto = "Llaves de paso",
                        icono = Icons.Filled.Fingerprint,
                        colorIcono = ColorPasskeys,
                        idEtiqueta = "04-HER-PSK",
                        mostrarId = mostrarIds,
                        ajustes = ajustes
                    ) { alIr(Pantalla.Passkeys) }
                }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Verificación en dos pasos",
                    icono = Icons.Filled.Timer,
                    colorIcono = Color2FA,
                    idEtiqueta = "04-HER-2FA",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.Autenticador) }
            }

            // Grupo 2: Organización y auditoría
            GrupoMenuLateral(
                titulo = "Organización y auditoría",
                idEtiqueta = "03-LST",
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) {
                ItemMenu(
                    texto = "Salud",
                    icono = Icons.Filled.HealthAndSafety,
                    colorIcono = ColorSalud,
                    idEtiqueta = "03-LST-SLD",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.SaludBoveda) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Duplicados",
                    icono = Icons.Filled.ContentCopy,
                    colorIcono = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
                    badge = if (totalDuplicadas > 0) totalDuplicadas.toString() else null,
                    colorBadge = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
                    idEtiqueta = "03-LST-DUP",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.Duplicados) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Papelera",
                    icono = Icons.Filled.Delete,
                    colorIcono = if (totalPapelera > 0) ColorPapelera else ColorIconosInternos,
                    badge = if (totalPapelera > 0) totalPapelera.toString() else null,
                    colorBadge = ColorAcento,
                    idEtiqueta = "03-LST-PAP",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.Papelera) }
            }

            // Grupo 3: Sistema
            GrupoMenuLateral(
                titulo = "Sistema",
                idEtiqueta = "06-SIS",
                mostrarId = mostrarIds,
                ajustes = ajustes
            ) {
                ItemMenu(
                    texto = "Ajustes",
                    icono = Icons.Filled.Settings,
                    colorIcono = Color(0xFF546E7A),
                    idEtiqueta = "00-AJU",
                    mostrarId = mostrarIds,
                    ajustes = ajustes
                ) { alIr(Pantalla.Ajustes) }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Pie de Menú: Cápsulas de seguridad y versión
        PieMenuLateral(perfilArgon2 = perfilArgon2)

        Spacer(Modifier.height(10.dp))

        // Botón Bloquear aplicación
        BotonFilaBloquear(alBloquear = alBloquear)
    }
}
