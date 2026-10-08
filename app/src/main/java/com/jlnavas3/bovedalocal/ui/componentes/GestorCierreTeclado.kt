package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController

/**
 * Modificador para cerrar el teclado y limpiar el foco al tocar en cualquier espacio libre
 * fuera de un campo de texto interactivo.
 */
fun Modifier.cerrarTecladoAlTocarFuera(
    focusManager: FocusManager? = null,
    keyboardController: SoftwareKeyboardController? = null
): Modifier = composed {
    val fm = focusManager ?: LocalFocusManager.current
    val kc = keyboardController ?: LocalSoftwareKeyboardController.current
    this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                fm.clearFocus()
                kc?.hide()
            }
        )
    }
}

/**
 * Efecto reactivo que oculta el teclado de forma fluida y limpia el foco
 * cuando se inicia el desplazamiento en cualquier lista o contenedor scrolleable.
 */
@Composable
fun CerrarTecladoAlHacerScroll(isScrollInProgress: Boolean) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
}
