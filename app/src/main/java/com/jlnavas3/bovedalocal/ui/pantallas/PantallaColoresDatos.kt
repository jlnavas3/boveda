package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteColorPicker
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
            idEtiqueta = "03.2.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo
        )

        // Tarjeta de Vista Previa FLOTANTE SUPERIOR FIXA (Sticky Top) - Limpia sin títulos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorAjustesFondo)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val modifierBordeTarjeta = if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
            } else {
                Modifier
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(FormaTarjeta)
                    .background(Superficie)
                    .then(modifierBordeTarjeta)
            ) {
                IndicadorContenidoTarjeta(
                    entrada = entradaPrueba,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

        // Scrollable Settings Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "Personaliza los colores aislados para cada tipo de dato. Estos colores identifican visualmente el contenido en las tarjetas del listado:"
            )

            Spacer(Modifier.height(12.dp))

            ComponenteGrupo(
                etiqueta = "Colores de datos e indicadores",
                idGrupo = "03.2.1.G1",
                mostrarId = ajustes.mostrarIdsAjustes
            ) {
                ComponenteColorPicker(
                    titulo = "Usuario / Correo",
                    colorActual = ColorDatosUsuario,
                    icono = Icons.Filled.Person,
                    colorIcono = ColorDatosUsuario,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatosUsuario
                        colorSeleccionando = "usuario"
                    }
                )

                ComponenteSeparador()

                ComponenteColorPicker(
                    titulo = "Contraseña",
                    colorActual = ColorDatosContrasena,
                    icono = Icons.Filled.Key,
                    colorIcono = ColorDatosContrasena,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatosContrasena
                        colorSeleccionando = "contrasena"
                    }
                )

                ComponenteSeparador()

                ComponenteColorPicker(
                    titulo = "Código 2FA (TOTP)",
                    colorActual = ColorDatos2FA,
                    icono = Icons.Filled.Timer,
                    colorIcono = ColorDatos2FA,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatos2FA
                        colorSeleccionando = "2fa"
                    }
                )

                ComponenteSeparador()

                ComponenteColorPicker(
                    titulo = "Passkey WebAuthn",
                    colorActual = ColorDatosPasskey,
                    icono = Icons.Filled.Fingerprint,
                    colorIcono = ColorDatosPasskey,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatosPasskey
                        colorSeleccionando = "passkey"
                    }
                )

                ComponenteSeparador()

                ComponenteColorPicker(
                    titulo = "Sitio Web (URL)",
                    colorActual = ColorDatosWeb,
                    icono = Icons.Filled.Language,
                    colorIcono = ColorDatosWeb,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatosWeb
                        colorSeleccionando = "web"
                    }
                )

                ComponenteSeparador()

                ComponenteColorPicker(
                    titulo = "App Android vinculada",
                    colorActual = ColorDatosApp,
                    icono = Icons.Filled.Android,
                    colorIcono = ColorDatosApp,
                    alPulsar = {
                        haptica.tic()
                        colorInicialModal = ColorDatosApp
                        colorSeleccionando = "app"
                    }
                )
            }

            Spacer(Modifier.height(18.dp))

            BotonBorde(
                texto = "Restablecer colores predeterminados",
                icono = Icons.Filled.Refresh,
                modifier = Modifier.fillMaxWidth()
            ) {
                haptica.tic()
                vm.restablecerColoresDatos()
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Modal Selector de Color
    colorSeleccionando?.let { clave ->
        val tituloModal = when (clave) {
            "usuario" -> "Usuario / Correo"
            "contrasena" -> "Contraseña"
            "2fa" -> "Código 2FA (TOTP)"
            "passkey" -> "Passkey WebAuthn"
            "web" -> "Sitio Web (URL)"
            else -> "App Android vinculada"
        }

        AlertDialog(
            onDismissRequest = { colorSeleccionando = null },
            shape = RoundedCornerShape(22.dp),
            containerColor = Superficie,
            title = {
                Text(
                    text = "Color de $tituloModal",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                SelectorColorEnTiempoReal(
                    colorInicial = colorInicialModal,
                    titulo = tituloModal
                ) { nuevoColor ->
                    val hex = nuevoColor.aHex()
                    when (clave) {
                        "usuario" -> vm.ajustarColorDatosUsuario(hex)
                        "contrasena" -> vm.ajustarColorDatosContrasena(hex)
                        "2fa" -> vm.ajustarColorDatos2FA(hex)
                        "passkey" -> vm.ajustarColorDatosPasskey(hex)
                        "web" -> vm.ajustarColorDatosWeb(hex)
                        "app" -> vm.ajustarColorDatosApp(hex)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { colorSeleccionando = null }) {
                    Text("Aceptar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
