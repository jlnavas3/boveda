/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorCamara
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesCamara(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Cámara y escáner",
                idEtiqueta = "04-HER-CAM",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-CAM-G01", "Motor de captura"),
                            AccionSaltoGrupo("04-HER-CAM-G02", "Experiencia de escaneo")
                        ),
                        alRestablecerPantalla = {
                            vm.restablecerCamara()
                        },
                        mensajeToastRestablecer = "Ajustes de cámara y escáner restablecidos"
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ComponenteGrupo(
                    etiqueta = "Motor de captura",
                    icono = Icons.Filled.CameraAlt,
                    colorIcono = ColorCamara,
                    alRestablecer = {
                        vm.restablecerCamara()
                    },
                    mensajeToastRestablecer = "Motor de cámara restablecido",
                    idGrupo = "04-HER-CAM-G01",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "CameraX conmutación automática o API Camera clásica compatible"
                ) {
                    val opcionesCamara = remember {
                        listOf(
                            OpcionSelectorModal(MotorCamara.AUTOMATICO.clave, MotorCamara.AUTOMATICO.etiqueta, MotorCamara.AUTOMATICO.etiqueta, "Prueba CameraX y conmuta a Compatible si no responde", Icons.Filled.CameraAlt),
                            OpcionSelectorModal(MotorCamara.CAMERAX.clave, MotorCamara.CAMERAX.etiqueta, MotorCamara.CAMERAX.etiqueta, "Motor moderno de Android Jetpack de alto rendimiento", Icons.Filled.CameraAlt),
                            OpcionSelectorModal(MotorCamara.COMPATIBLE.clave, MotorCamara.COMPATIBLE.etiqueta, MotorCamara.COMPATIBLE.etiqueta, "API clásica de cámara; máxima compatibilidad en cualquier modelo", Icons.Filled.CameraAlt)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Motor de cámara",
                        descripcionModal = "Elige la tecnología de captura óptica para lectura de códigos QR y TOTP",
                        icono = null,
                        idFila = "04-HER-CAM-MOD",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.motorCamara,
                        opciones = opcionesCamara,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarMotorCamara(valor)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                ComponenteGrupo(
                    etiqueta = "Experiencia de escaneo",
                    icono = Icons.Filled.QrCodeScanner,
                    colorIcono = ColorCamara,
                    alRestablecer = {
                        vm.restablecerCamara()
                    },
                    mensajeToastRestablecer = "Ajustes de escaneo restablecidos",
                    idGrupo = "04-HER-CAM-G02",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Respuesta sensorial y tamaño del visor al detectar códigos QR"
                ) {
                    ComponenteSwitch(
                        titulo = "Vibración al detectar",
                        subtitulo = "Confirmación háptica inmediata al reconocer un código QR",
                        activo = ajustes.escanerQrVibracion,
                        icono = Icons.Filled.Vibration,
                        idFila = "04-HER-CAM-VIB",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { nuevo ->
                            haptica.tic()
                            vm.ajustarEscanerQrVibracion(nuevo)
                        }
                    )

                    com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador()

                    ComponenteSwitch(
                        titulo = "Pitido sonoro de éxito",
                        subtitulo = "Emite un tono breve al capturar satisfactoriamente un código",
                        activo = ajustes.escanerQrSonido,
                        icono = Icons.Filled.VolumeUp,
                        idFila = "04-HER-CAM-SND",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { nuevo ->
                            haptica.tic()
                            vm.ajustarEscanerQrSonido(nuevo)
                        }
                    )

                    com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador()

                    val opcionesTamano = remember {
                        listOf(
                            OpcionSelectorModal("200.0", "Compacta (200 dp)", "200 dp", "Área pequeña para códigos densos o pantallas pequeñas", Icons.Filled.CropFree),
                            OpcionSelectorModal("260.0", "Estándar (260 dp)", "260 dp", "Tamaño predeterminado equilibrado", Icons.Filled.CropFree),
                            OpcionSelectorModal("300.0", "Amplia (300 dp)", "300 dp", "Área de enfoque amplia para lectura a mayor distancia", Icons.Filled.CropFree),
                            OpcionSelectorModal("340.0", "Máxima (340 dp)", "340 dp", "Ocupa la mayor parte del visor", Icons.Filled.CropFree)
                        )
                    }

                    ComponenteSelectorModal(
                        titulo = "Tamaño inicial del visor",
                        descripcionModal = "Elige la dimensión de la ventana de enfoque predeterminada (también puedes pellizcar para cambiarla en el visor)",
                        icono = Icons.Filled.CropFree,
                        idFila = "04-HER-CAM-TAM",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.escanerQrTamanoVentanaDp.toString(),
                        opciones = opcionesTamano,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            valor.toFloatOrNull()?.let { vm.ajustarEscanerQrTamanoVentanaDp(it) }
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
