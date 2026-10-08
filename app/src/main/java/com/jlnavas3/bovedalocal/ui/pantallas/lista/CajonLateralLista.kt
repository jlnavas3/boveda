package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.Superficie

/**
 * Envoltorio del cajón de navegación lateral (Drawer) para PantallaLista.
 */
@Composable
fun CajonLateralLista(
    estadoCajon: DrawerState,
    ajustes: AjustesApp,
    totalEntradas: Int,
    totalPapelera: Int,
    totalDuplicadas: Int,
    perfilArgon2: PerfilArgon2,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit,
    contenido: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(estadoCajon.targetValue, estadoCajon.isOpen) {
        if (estadoCajon.targetValue == DrawerValue.Open || estadoCajon.isOpen) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    val formaCajon = RectangleShape
    val colorLineaBordeCajon = if (ColorBordeActual != Color.Transparent) {
        ColorBordeActual.copy(alpha = 0.38f)
    } else {
        Color.White.copy(alpha = 0.12f)
    }

    ModalNavigationDrawer(
        drawerState = estadoCajon,
        scrimColor = Color.Black.copy(alpha = 0.68f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .cerrarTecladoAlTocarFuera()
                    .drawWithContent {
                        drawContent()
                        val strokeWidth = 2.5.dp.toPx()
                        drawLine(
                            color = colorLineaBordeCajon,
                            start = Offset(size.width - strokeWidth / 2, 0f),
                            end = Offset(size.width - strokeWidth / 2, size.height),
                            strokeWidth = strokeWidth
                        )
                    },
                drawerShape = formaCajon,
                drawerContainerColor = Superficie,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                MenuLateral(
                    nombreApp = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                    totalEntradas = totalEntradas,
                    totalPapelera = totalPapelera,
                    totalDuplicadas = totalDuplicadas,
                    perfilArgon2 = perfilArgon2,
                    mostrarIds = ajustes.mostrarIdsAjustes,
                    ajustes = ajustes,
                    alIr = alIr,
                    alBloquear = alBloquear
                )
            }
        },
        content = contenido
    )
}
