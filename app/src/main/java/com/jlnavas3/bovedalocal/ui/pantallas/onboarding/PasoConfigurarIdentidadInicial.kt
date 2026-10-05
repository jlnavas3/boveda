package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.identidades.PaletaColoresIdentidad
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Paso del onboarding para configurar opcionalmente la primera [Identidad] del usuario
 * antes de forjar la bóveda cifrada.
 */
@Composable
fun PasoConfigurarIdentidadInicial(
    alConfirmarIdentidad: (Identidad?) -> Unit,
    alVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("Personal") }
    var correo by remember { mutableStateOf("") }
    var colorHex by remember { mutableStateOf(PaletaColoresIdentidad.first()) }

    val correoValido = correo.isNotBlank() && correo.contains("@") && correo.contains(".")
    val colorAcentoFinal = parsearColorO(colorHex, ColorAcento)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = "Tu primera identidad",
                idEtiqueta = "01-ONB-IDE",
                alVolver = alVolver,
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )
        },
        containerColor = ColorAjustesFondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContenedorIconoInsignia(
                icono = Icons.Filled.AccountCircle,
                tamano = TamanoInsignia.HERO,
                colorFondo = colorAcentoFinal.copy(alpha = 0.14f),
                colorIcono = colorAcentoFinal,
                conBorde = true
            )

            TextoTitulo(
                texto = "Protege tu privacidad",
                estilo = EstiloTitulo.MEDIANO,
                alineacion = TextAlign.Center
            )

            TextoSubtitulo(
                texto = "Asocia tu cuenta de correo habitual a esta identidad. Así tus credenciales se vincularán de forma automática y tu correo real no quedará expuesto a miradas indiscretas.",
                alineacion = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            ContenedorTarjeta(
                paddingInterno = 16.dp
            ) {
                CampoBoveda(
                    valor = nombre,
                    etiqueta = "Nombre del perfil (ej. Personal, Trabajo)",
                    alCambiar = { nombre = it }
                )

                CampoBoveda(
                    valor = correo,
                    etiqueta = "Correo electrónico principal",
                    alCambiar = { correo = it }
                )

                Spacer(Modifier.height(4.dp))

                TextoSubtitulo(
                    texto = "Color temático",
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaletaColoresIdentidad.forEach { hex ->
                        val colorActual = parsearColorO(hex, ColorAcento)
                        val seleccionado = colorHex.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(colorActual)
                                .border(
                                    width = if (seleccionado) 2.5.dp else 1.dp,
                                    color = if (seleccionado) TextoPrincipal else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { colorHex = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            BotonPrimario(
                texto = "Continuar y forjar bóveda",
                icono = Icons.Filled.Shield,
                activo = nombre.isNotBlank() && correoValido,
                alPulsar = {
                    if (nombre.isNotBlank() && correoValido) {
                        val identidad = Identidad(
                            nombre = nombre.trim(),
                            correoPrincipal = correo.trim(),
                            colorHex = colorHex,
                            icono = "person"
                        )
                        alConfirmarIdentidad(identidad)
                    }
                }
            )

            BotonTextoBoveda(
                texto = "Omitir por ahora",
                tipo = TipoBotonTexto.SECUNDARIO,
                alPulsar = {
                    alConfirmarIdentidad(null)
                }
            )
        }
    }
}
