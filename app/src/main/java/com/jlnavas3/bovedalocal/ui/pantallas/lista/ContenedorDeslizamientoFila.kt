package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Menta

/**
 * Contenedor interactivo que gestiona el gesto de deslizamiento horizontal (swipe-to-action)
 * para copiar usuario o contraseña, con animaciones de resorte y retroalimentación háptica.
 */
@Composable
fun ContenedorDeslizamientoFila(
    entradaId: String,
    alturaFila: Dp,
    forma: Shape,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    modifier: Modifier = Modifier,
    enGrupo: Boolean = false,
    contenido: @Composable () -> Unit
) {
    ContenedorDeslizamientoBoveda(
        idItem = entradaId,
        forma = forma,
        alturaFila = alturaFila,
        enGrupo = enGrupo,
        accionIzquierda = AccionDeslizamiento(
            texto = "Copiar\nUsuario",
            icono = Icons.Filled.Person,
            color = Menta,
            alEjecutar = alCopiarUsuario
        ),
        accionDerecha = AccionDeslizamiento(
            texto = "Copiar\nContraseña",
            icono = Icons.Filled.Key,
            color = Ambar,
            alEjecutar = alCopiarContrasena
        ),
        modifier = modifier,
        contenido = contenido
    )
}
