package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    alNavegarColoresIds: () -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    ComponenteGrupo(
        etiqueta = "Desarrollo y referencia",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFFC2185B),
        alRestablecer = {
            haptica.tic()
            alCambiarMostrarIds(false)
            alCambiarAlumbradoActivo(true)
            alCambiarIntensidad(0.7f)
            alCambiarRepeticiones(2)
            alCambiarDuracion(600)
        },
        idGrupo = "06.1.G2",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Muestra una etiqueta con el código ID jerárquico de cada opción para facilitar soporte y automatización",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Identificadores de ajustes (IDs)",
            icono = null,
            activo = mostrarIdsAjustes,
            idFila = "06.1.2",
            mostrarId = mostrarIdsAjustes,
            alCambiar = {
                haptica.tic()
                alCambiarMostrarIds(it)
            }
        )
        if (mostrarIdsAjustes) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteNavegacion(
                titulo = "Colores de identificadores",
                subtitulo = "Personalizar paleta de los 6 bloques",
                icono = null,
                idFila = "06.1.2b",
                mostrarId = mostrarIdsAjustes,
                alPulsar = {
                    haptica.tic()
                    alNavegarColoresIds()
                }
            )
        }
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSwitch(
            titulo = "Alumbrado de navegación",
            icono = null,
            activo = alumbradoActivo,
            idFila = "06.1.3",
            mostrarId = mostrarIdsAjustes,
            alCambiar = {
                haptica.tic()
                alCambiarAlumbradoActivo(it)
            }
        )
        if (alumbradoActivo) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteSlider(
                titulo = "Intensidad del alumbrado",
                valor = alumbradoIntensidad,
                valorTexto = "${(alumbradoIntensidad * 100).roundToInt()}%",
                rango = 0.1f..1.0f,
                pasos = 8,
                etiquetaMin = "10%",
                etiquetaMax = "100%",
                idFila = "06.1.4",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                alCambiar = alCambiarIntensidad
            )
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteSlider(
                titulo = "Número de alumbrados",
                valor = alumbradoRepeticiones.toFloat(),
                valorTexto = if (alumbradoRepeticiones == 1) "1 destello" else "$alumbradoRepeticiones destellos",
                rango = 1f..5f,
                pasos = 3,
                etiquetaMin = "1",
                etiquetaMax = "5",
                idFila = "06.1.5",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                alCambiar = {
                    haptica.tic()
                    alCambiarRepeticiones(it.roundToInt())
                }
            )
            ComponenteSeparador(sangriaInicio = 16.dp)
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
                idFila = "06.1.6",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                alCambiar = {
                    alCambiarDuracion(it.roundToInt())
                }
            )
            ComponenteSeparador(sangriaInicio = 16.dp)
            val estadoPrueba = recordarEstadoAlumbrado("06.1.7")
            val colorAcentoPrueba = ColorAcento
            ComponenteNavegacion(
                titulo = "Probar efecto de alumbrado",
                icono = null,
                idFila = "06.1.7",
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
