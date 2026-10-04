package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Pantalla inicial de bienvenida del flujo de onboarding.
 * Orquesta la cabecera, la animación de la bóveda, la insignia de seguridad y el inicio de creación.
 */
@Composable
fun PasoBienvenida(
    ajustes: AjustesApp = AjustesApp(),
    alIniciarCreacion: () -> Unit,
    alAbrirAcercaDe: () -> Unit,
    alSalir: () -> Unit
) {
    var mostrarModalGarantias by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior desacoplada con menú contextual de opciones
        BarraSuperiorBienvenida(
            alAbrirGarantias = { mostrarModalGarantias = true },
            alAbrirAcercaDe = alAbrirAcercaDe,
            alSalir = alSalir
        )

        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Visualizador mecánico de la bóveda
            AnimacionBienvenidaBoveda(ajustes = ajustes)

            // Encabezado con insignia de seguridad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InsigniaSeguridadOnboarding()

                Text(
                    text = "Bóveda Local",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = ColorTitulos,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Solo tú tienes la llave de acceso a tu información.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Tarjeta agrupada de inicio de configuración
            ComponenteGrupo(
                etiqueta = "Iniciar configuración"
            ) {
                ComponenteBotonFila(
                    titulo = "Crear mi bóveda",
                    alPulsar = alIniciarCreacion,
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorAcento,
                    colorTinteIcono = ColorSobreAcento
                )
            }

            Spacer(Modifier.height(18.dp))

            // Pie de página con versión y autoría
            PieVersionOnboarding()
        }
    }

    if (mostrarModalGarantias) {
        DialogoGarantiasSeguridad(alCerrar = { mostrarModalGarantias = false })
    }
}
