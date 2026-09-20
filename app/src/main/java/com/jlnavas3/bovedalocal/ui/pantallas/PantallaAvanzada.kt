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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.Tune
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorrarBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoCambioMaestra
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAvanzada(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoCambio by remember { mutableStateOf(false) }
    var dialogoBorrar by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Opciones avanzadas",
                idEtiqueta = "05.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Gestión de credenciales maestras, diagnóstico y zona de peligro")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Credencial maestra",
                    idGrupo = "05.1.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Al cambiar la contraseña maestra, la base de datos se re-cifra en tiempo real con Argon2id"
                ) {
                    ComponenteNavegacion(
                        titulo = "Cambiar contraseña maestra",
                        icono = Icons.Filled.Lock,
                        colorIcono = ColorSeguridad,
                        idFila = "05.1.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoCambio = true }
                    )
                }

                Spacer(Modifier.height(18.dp))

                ComponenteGrupo(
                    etiqueta = "Desarrollo y referencia",
                    idGrupo = "05.1.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Muestra una etiqueta con el código ID jerárquico de cada opción para facilitar soporte y automatización"
                ) {
                    ComponenteSwitch(
                        titulo = "Identificadores de ajustes (IDs)",
                        icono = Icons.Filled.Tune,
                        colorIcono = Color(0xFFC2185B),
                        activo = ajustes.mostrarIdsAjustes,
                        idFila = "05.1.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarMostrarIdsAjustes(it)
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                ComponenteGrupo(
                    etiqueta = "Zona de peligro",
                    idGrupo = "05.1.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Eliminación irreversible e inmediata de todas las contraseñas, notas y configuraciones"
                ) {
                    ComponenteNavegacion(
                        titulo = "Borrar bóveda definitivamente",
                        icono = Icons.Filled.Delete,
                        colorIcono = ColorPapelera,
                        idFila = "05.1.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoBorrar = true }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoCambio) {
        DialogoCambioMaestra(
            alDescartar = { dialogoCambio = false },
            alConfirmar = { actual, nueva ->
                dialogoCambio = false
                vm.cambiarContrasenaMaestra(actual, nueva)
            }
        )
    }

    if (dialogoBorrar) {
        DialogoBorrarBoveda(
            alDescartar = { dialogoBorrar = false },
            alConfirmar = {
                dialogoBorrar = false
                vm.repositorio.borrarTodo()
                vm.ir(Pantalla.Onboarding)
            }
        )
    }
}
