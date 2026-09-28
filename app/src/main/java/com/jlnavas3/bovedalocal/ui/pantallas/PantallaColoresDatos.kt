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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.colores.DialogoSelectorColorDato
import com.jlnavas3.bovedalocal.ui.pantallas.colores.GrupoSelectoresColoresDatos
import com.jlnavas3.bovedalocal.ui.pantallas.colores.TarjetaStickyPreviaColores
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

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
            colorFondo = ColorAjustesFondo
        )

        // Tarjeta de Vista Previa FLOTANTE SUPERIOR FIJA (Sticky Top)
        TarjetaStickyPreviaColores(entradaPrueba = entradaPrueba)

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
