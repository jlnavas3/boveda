package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.recordarEstadoAlumbrado
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun GrupoDesarrolloReferencia(
    mostrarIdsAjustes: Boolean,
    alumbradoActivo: Boolean,
    alumbradoIntensidad: Float,
    alumbradoRepeticiones: Int,
    alumbradoDuracionMs: Int,
    alCambiarMostrarIds: (Boolean) -> Unit,
    alCambiarAlumbradoActivo: (Boolean) -> Unit,
    alCambiarIntensidad: (Float) -> Unit,
    alCambiarRepeticiones: (Int) -> Unit,
    alCambiarDuracion: (Int) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    ComponenteGrupo(
        etiqueta = "Desarrollo y referencia",
        idGrupo = "05.1.G2",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Muestra una etiqueta con el código ID jerárquico de cada opción para facilitar soporte y automatización",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Identificadores de ajustes (IDs)",
            icono = Icons.Filled.Tune,
            colorIcono = Color(0xFFC2185B),
            activo = mostrarIdsAjustes,
            idFila = "05.1.2",
            mostrarId = mostrarIdsAjustes,
            alCambiar = {
                haptica.tic()
                alCambiarMostrarIds(it)
            }
        )
        ComponenteSeparador()
        ComponenteSwitch(
            titulo = "Alumbrado de navegación",
            icono = Icons.Filled.Highlight,
            colorIcono = Color(0xFFE91E63),
            activo = alumbradoActivo,
            idFila = "05.1.4",
            mostrarId = mostrarIdsAjustes,
            alCambiar = {
                haptica.tic()
                alCambiarAlumbradoActivo(it)
            }
        )
        if (alumbradoActivo) {
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Intensidad del alumbrado",
                valor = alumbradoIntensidad,
                valorTexto = "${(alumbradoIntensidad * 100).roundToInt()}%",
                rango = 0.1f..1.0f,
                pasos = 8,
                etiquetaMin = "10%",
                etiquetaMax = "100%",
                idFila = "05.1.5",
                mostrarId = mostrarIdsAjustes,
                alCambiar = alCambiarIntensidad
            )
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Número de alumbrados",
                valor = alumbradoRepeticiones.toFloat(),
                valorTexto = if (alumbradoRepeticiones == 1) "1 destello" else "$alumbradoRepeticiones destellos",
                rango = 1f..5f,
                pasos = 3,
                etiquetaMin = "1",
                etiquetaMax = "5",
                idFila = "05.1.6",
                mostrarId = mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    alCambiarRepeticiones(it.roundToInt())
                }
            )
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Duración por alumbrado",
                valor = alumbradoDuracionMs.toFloat(),
                valorTexto = if (alumbradoDuracionMs >= 1000) {
                    String.format(Locale.US, "%.1f s", alumbradoDuracionMs / 1000f)
                } else {
                    "${alumbradoDuracionMs} ms"
                },
                rango = 300f..1500f,
                pasos = 11,
                etiquetaMin = "300 ms",
                etiquetaMax = "1.5 s",
                idFila = "05.1.7",
                mostrarId = mostrarIdsAjustes,
                alCambiar = {
                    alCambiarDuracion(it.roundToInt())
                }
            )
            ComponenteSeparador()
            val estadoPrueba = recordarEstadoAlumbrado("05.1.8")
            val colorAcentoPrueba = ColorAcento
            ComponenteNavegacion(
                titulo = "Probar efecto de alumbrado",
                icono = Icons.Filled.Highlight,
                idFila = "05.1.8",
                mostrarId = mostrarIdsAjustes,
                estadoAlumbrado = estadoPrueba,
                alPulsar = {
                    coroutineScope.launch {
                        estadoPrueba.dispararEfectoAlumbrado(
                            colorAcento = colorAcentoPrueba,
                            activo = true,
                            intensidad = alumbradoIntensidad,
                            repeticiones = alumbradoRepeticiones,
                            duracionMs = alumbradoDuracionMs
                        )
                    }
                }
            )
        }
    }
}
