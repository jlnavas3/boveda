package com.jlnavas3.bovedalocal.ui.pantallas.historial

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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada al historial de claves del generador.
 */
@Composable
fun PantallaHistorialClavesGeneradas(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var mostrarDialogoVaciar by remember { mutableStateOf(false) }

    val opcionesAutodestruccion = remember {
        listOf(
            OpcionSelectorModal(30 * 60 * 1000L, "30 minutos", "30 minutos (predeterminado)", "Las contraseñas generadas expiran a la media hora", Icons.Filled.Timer),
            OpcionSelectorModal(60 * 60 * 1000L, "1 hora", "1 hora", "Expira a los 60 minutos de generación", Icons.Filled.Timer),
            OpcionSelectorModal(24 * 60 * 60 * 1000L, "24 horas", "24 horas (1 día)", "Expira tras un día completo", Icons.Filled.Timer),
            OpcionSelectorModal(7 * 24 * 60 * 60 * 1000L, "7 días", "7 días (1 semana)", "Mantiene las claves generadas durante 1 semana", Icons.Filled.Timer),
            OpcionSelectorModal(0L, "Nunca expirar", "Nunca expirar", "No borra las claves por tiempo (solo por límite de cantidad)", Icons.Filled.Timer)
        )
    }

    val opcionesCantidad = remember {
        listOf(
            OpcionSelectorModal(15, "15 claves", "15 claves", "Conserva hasta las últimas 15 contraseñas generadas", Icons.Filled.History),
            OpcionSelectorModal(25, "25 claves", "25 claves", "Conserva hasta las últimas 25 contraseñas generadas", Icons.Filled.History),
            OpcionSelectorModal(50, "50 claves", "50 claves", "Conserva hasta las últimas 50 contraseñas generadas", Icons.Filled.History),
            OpcionSelectorModal(100, "100 claves", "100 claves", "Conserva hasta las últimas 100 contraseñas generadas", Icons.Filled.History)
        )
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial de claves generadas",
                idEtiqueta = "04-HER-HST-GEN",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-HST-G01", "Retención y caducidad"),
                            AccionSaltoGrupo("04-HER-HST-G02", "Capacidad del historial"),
                            AccionSaltoGrupo("04-HER-HST-G04", "Zona de peligro")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerHistorialClavesConfig()
                            vm.avisar("Ajustes de claves generadas restablecidos")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Retención temporal, capacidad máxima y vaciado del historial de claves")
                Spacer(Modifier.height(10.dp))

                // Retención y caducidad
                ComponenteGrupo(
                    etiqueta = "Retención y caducidad",
                    idGrupo = "04-HER-HST-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Auto-destrucción temporal",
                        icono = null,
                        activo = ajustes.historialClavesVaciadoAuto,
                        idFila = "04-HER-HST-SWT",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarHistorialClavesVaciadoAuto(it)
                        }
                    )

                    if (ajustes.historialClavesVaciadoAuto) {
                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteSelectorModal(
                            titulo = "Tiempo de caducidad",
                            descripcionModal = "Tiempo tras el cual las claves generadas se eliminan del historial",
                            icono = null,
                            idFila = "04-HER-HST-EXP",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorSeleccionado = ajustes.historialClavesTiempoAutoDestruccion,
                            opciones = opcionesAutodestruccion,
                            alSeleccionar = { ms ->
                                haptica.tic()
                                vm.ajustarHistorialClavesTiempoAutoDestruccion(ms)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Capacidad del historial
                ComponenteGrupo(
                    etiqueta = "Capacidad del historial",
                    idGrupo = "04-HER-HST-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSelectorModal(
                        titulo = "Límite de contraseñas",
                        descripcionModal = "Cantidad máxima de claves registradas antes de descartar las más antiguas",
                        icono = null,
                        idFila = "04-HER-HER-MAX",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.historialClavesMax,
                        opciones = opcionesCantidad,
                        alSeleccionar = { max ->
                            haptica.tic()
                            vm.ajustarHistorialClavesMax(max)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Zona de peligro
                ComponenteGrupo(
                    etiqueta = "Zona de peligro",
                    idGrupo = "04-HER-HST-G04",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteBotonFila(
                        titulo = "Vaciar todo el historial ahora",
                        icono = Icons.Filled.Delete,
                        colorIcono = Peligro,
                        idFila = "04-HER-HST-DEL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.toque()
                            mostrarDialogoVaciar = true
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (mostrarDialogoVaciar) {
        DialogoVaciarHistorial(
            alDescartar = { mostrarDialogoVaciar = false },
            alConfirmar = {
                mostrarDialogoVaciar = false
                vm.vaciarHistorialClaves()
                vm.avisar("Historial vaciado")
            }
        )
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaHistorialClavesGeneradasPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial de claves generadas",
                idEtiqueta = "04-HER-HST-GEN",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Retención temporal, capacidad máxima y vaciado del historial de claves")
        }
    }
}
