package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.colores.DialogoSelectorColorDato
import com.jlnavas3.bovedalocal.ui.pantallas.colores.GrupoSelectoresColoresDatos
import com.jlnavas3.bovedalocal.ui.pantallas.colores.TarjetaStickyPreviaColores
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Pantalla dedicada a la personalización de los colores para los 6 tipos de datos
 * presentes en las entradas (Usuario, Contraseña, 2FA, Passkey, Web y Apps).
 */
@Composable
fun PantallaColoresDatos(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Entrada de prueba con todos los 6 campos activos para la vista previa
    val entradaPrueba = remember {
        Entrada(
            id = "preview_test",
            tipo = TipoEntrada.LOGIN,
            titulo = "Cuenta de Prueba",
            usuario = "usuario@ejemplo.com",
            contrasena = "SuperClave123!",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            passkey = DatosPasskey("ejemplo.com", "Ejemplo", "user", "cred123", "key123"),
            urls = listOf("https://ejemplo.com", "androidapp://com.ejemplo.app")
        )
    }

    var alturaTemporal by remember(ajustes.alturaIndicadoresDp) { mutableFloatStateOf(ajustes.alturaIndicadoresDp) }
    var opacidadTemporal by remember(ajustes.opacidadIndicadores) { mutableFloatStateOf(ajustes.opacidadIndicadores) }

    var colorSeleccionando by remember { mutableStateOf<String?>(null) }
    var colorInicialModal by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Colores de campos y datos",
            idEtiqueta = "02-APA-THM-DAT",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                BotonMenuOpcionesPantalla(
                    alRestablecerPantalla = {
                        haptica.tic()
                        alturaTemporal = AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP
                        opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                        vm.restablecerColoresDatos()
                    },
                    mensajeToastRestablecer = "Colores de datos restablecidos"
                )
            }
        )

        // Tarjeta de Vista Previa FLOTANTE SUPERIOR FIJA (Sticky Top con reactividad instantánea)
        TarjetaStickyPreviaColores(
            entradaPrueba = entradaPrueba,
            alturaIndicadores = alturaTemporal.dp,
            opacidadIndicadores = opacidadTemporal
        )

        // Scrollable Settings Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            GrupoSelectoresColoresDatos(
                mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                onSeleccionarColor = { clave, color ->
                    haptica.tic()
                    colorInicialModal = color
                    colorSeleccionando = clave
                },
                alRestablecer = {
                    haptica.tic()
                    vm.restablecerColoresDatos()
                    vm.avisar("Colores de datos restablecidos")
                }
            )

            Spacer(Modifier.height(EspaciadoComponentes))

            // Grupo 2: Dimensiones y estilo de fondo / línea
            ComponenteGrupo(
                etiqueta = "Dimensiones y apariencia de indicadores",
                idGrupo = "02-APA-THM-DAT-G02",
                mostrarId = ajustes.mostrarIdsAjustes,
                alRestablecer = {
                    haptica.tic()
                    alturaTemporal = AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP
                    opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                    vm.ajustarAlturaIndicadores(AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP)
                    vm.ajustarOpacidadIndicadores(AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES)
                }
            ) {
                val textoAltura = "${String.format(Locale.US, "%.1f", alturaTemporal)} dp"

                ComponenteSlider(
                    titulo = "Altura de las líneas",
                    valor = alturaTemporal.coerceIn(1f, 80f),
                    valorTexto = textoAltura,
                    rango = 1f..80f,
                    etiquetaMin = "1 dp",
                    etiquetaMax = "80 dp",
                    idFila = "02-APA-THM-DAT-ALT",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alCambiar = {
                        alturaTemporal = it
                        vm.ajustarAlturaIndicadores(it)
                    },
                    alRestablecer = {
                        haptica.tic()
                        alturaTemporal = AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP
                        vm.ajustarAlturaIndicadores(AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP)
                    }
                )

                ComponenteSeparador()

                val porcentajeOpacidad = (opacidadTemporal * 100).roundToInt()
                ComponenteSlider(
                    titulo = "Opacidad de los colores",
                    valor = opacidadTemporal.coerceIn(0.05f, 1.0f),
                    valorTexto = "$porcentajeOpacidad%",
                    rango = 0.05f..1.0f,
                    etiquetaMin = "5%",
                    etiquetaMax = "100%",
                    idFila = "02-APA-THM-DAT-OPC",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alCambiar = {
                        opacidadTemporal = it
                        vm.ajustarOpacidadIndicadores(it)
                    },
                    alRestablecer = {
                        haptica.tic()
                        opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                        vm.ajustarOpacidadIndicadores(AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES)
                    }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // Modal Selector de Color
    colorSeleccionando?.let { clave ->
        DialogoSelectorColorDato(
            claveColor = clave,
            colorInicial = colorInicialModal,
            alCerrar = { colorSeleccionando = null },
            alCambiarColor = { k, nuevoColor ->
                val hex = nuevoColor.aHex()
                when (k) {
                    "usuario" -> vm.ajustarColorDatosUsuario(hex)
                    "contrasena" -> vm.ajustarColorDatosContrasena(hex)
                    "2fa" -> vm.ajustarColorDatos2FA(hex)
                    "passkey" -> vm.ajustarColorDatosPasskey(hex)
                    "web" -> vm.ajustarColorDatosWeb(hex)
                    "app" -> vm.ajustarColorDatosApp(hex)
                }
            }
        )
    }
}
