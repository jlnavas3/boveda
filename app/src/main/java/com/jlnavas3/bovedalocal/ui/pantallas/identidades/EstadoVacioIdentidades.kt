package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Estado vacío para la pantalla de gestión de identidades cuando aún no se ha creado ninguna.
 */
@Composable
fun EstadoVacioIdentidades(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ContenedorIconoInsignia(
                icono = Icons.Filled.AccountCircle,
                tamano = TamanoInsignia.HERO,
                colorFondo = ColorAcento.copy(alpha = 0.12f),
                colorIcono = ColorAcento,
                conBorde = true
            )
            Spacer(Modifier.height(18.dp))
            TextoTitulo(
                texto = "Sin identidades creadas",
                estilo = EstiloTitulo.MEDIANO,
                alineacion = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            TextoSubtitulo(
                texto = "Crea identidades (ej. Personal, Trabajo, Compras) asociadas a tus correos. Las credenciales existentes se vincularán de forma automática e inteligente.",
                alineacion = TextAlign.Center
            )
        }
    }
}
