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
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
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
                titulo = "Cámara y escáner QR",
                idEtiqueta = "04.2",
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
                DescripcionPantalla(subtitulo = "Configuración del motor óptico para escanear códigos QR y 2FA")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Motor de captura",
                    idGrupo = "04.2.G1",
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
                        icono = Icons.Filled.CameraAlt,
                        colorIcono = ColorCamara,
                        idFila = "04.2.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.motorCamara,
                        opciones = opcionesCamara,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarMotorCamara(valor)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer",
                        alPulsar = {
                            vm.ajustarMotorCamara(MotorCamara.AUTOMATICO.clave)
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
