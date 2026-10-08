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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoCambioMaestra
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla contenedora de Nivel 2 para Seguridad y Biometría.
 * Presenta una navegación fractal limpia, 100% tipográfica sin íconos squircle,
 * derivando a pantallas atómicas por grupo.
 */
@Composable
fun PantallaSeguridad(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var dialogoCambioMaestra by remember { mutableStateOf(false) }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Biometría y seguridad",
                idEtiqueta = "01-SEG-BIO",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y cifrado de datos")
                Spacer(Modifier.height(10.dp))

                // Grupo: Acceso y Bloqueo
                ComponenteGrupo(
                    etiqueta = "Acceso y autenticación",
                    idGrupo = "01-SEG-BIO-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Bloqueo y biometría",
                        icono = null,
                        idFila = "01-SEG-BIO-BLO",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.BloqueoBiometria()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Seguridad visual",
                        icono = null,
                        idFila = "01-SEG-BIO-VIS",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.SeguridadVisual()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Portapapeles",
                        icono = null,
                        idFila = "01-SEG-BIO-CLP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.Portapapeles()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Auditoría de contraseñas",
                        icono = null,
                        idFila = "01-SEG-BIO-AUD",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AuditoriaSeguridad()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Credenciales y Cifrado
                ComponenteGrupo(
                    etiqueta = "Credenciales y protección",
                    idGrupo = "01-SEG-BIO-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Clave maestra",
                        icono = null,
                        idFila = "01-SEG-PAS",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoCambioMaestra = true }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Modo señuelo",
                        icono = null,
                        idFila = "01-SEG-SEN",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AjustesSenuelo()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Autodestrucción por PIN",
                        icono = null,
                        idFila = "01-SEG-DES-PIN",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AjustesAutodestruccion()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Autodestrucción por intentos fallidos",
                        icono = null,
                        idFila = "01-SEG-DES-INT",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AutodestruccionIntentos()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Cifrado",
                        icono = null,
                        idFila = "01-SEG-CRY",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.Argon2id()) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoCambioMaestra) {
        DialogoCambioMaestra(
            alDescartar = { dialogoCambioMaestra = false },
            alConfirmar = { actual, nueva ->
                dialogoCambioMaestra = false
                vm.cambiarContrasenaMaestra(actual, nueva)
            }
        )
    }
}

@Preview(name = "Pantalla Seguridad Fractal", showBackground = true)
@Composable
private fun PantallaSeguridadPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Biometría y seguridad",
                idEtiqueta = "01-SEG-BIO",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y cifrado de datos")
        }
    }
}
