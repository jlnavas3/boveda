package com.jlnavas3.bovedalocal.ui.pantallas

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.tema.DialogoConfirmacionIconoLauncher
import com.jlnavas3.bovedalocal.ui.pantallas.tema.SeccionAnimacionDesbloqueo
import com.jlnavas3.bovedalocal.ui.pantallas.tema.SeccionColorAcento
import com.jlnavas3.bovedalocal.ui.pantallas.tema.SeccionColorDinamicoYSistema
import com.jlnavas3.bovedalocal.ui.pantallas.tema.SeccionIconoLauncher
import com.jlnavas3.bovedalocal.ui.pantallas.tema.SeccionModoTema
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaTema(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoConfirmarIcono by remember { mutableStateOf<PaletaAcento?>(null) }

    val reqDinamico = remember { BringIntoViewRequester() }
    val reqAcento = remember { BringIntoViewRequester() }
    val reqLauncher = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.2.2" -> reqDinamico.bringIntoView()
                seccionDestino == "09.2.3" -> reqAcento.bringIntoView()
                seccionDestino == "09.2.8" -> reqLauncher.bringIntoView()
                seccionDestino.startsWith("09.2.") && seccionDestino != "09.2" -> reqAcento.bringIntoView()
            }
        }
    }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Tema y colores",
                idEtiqueta = "03.2",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Modo de tema, paleta visual y colores semánticos por módulo")
                Spacer(Modifier.height(10.dp))

                // 0. Modo de tema (Sistema, Claro, Oscuro)
                SeccionModoTema(
                    ajustes = ajustes,
                    alCambiarTema = { vm.ajustarTema(it) }
                )

                Spacer(Modifier.height(18.dp))

                // Animación de pantalla bloqueada
                SeccionAnimacionDesbloqueo(
                    ajustes = ajustes,
                    haptica = haptica,
                    alCambiarAnimacion = { vm.ajustarAnimacionDesbloqueo(it) },
                    alIrACalibracion = { vm.ir(Pantalla.CalibracionAnimacion()) }
                )

                // 1. Color dinámico del sistema (Material You - Android 12+)
                SeccionColorDinamicoYSistema(
                    ajustes = ajustes,
                    haptica = haptica,
                    reqDinamico = reqDinamico,
                    alAlternarColorDinamico = { vm.alternarColorDinamicoSistema(it) }
                )

                // Colores de campos y datos
                Spacer(Modifier.height(18.dp))
                ComponenteGrupo(
                    etiqueta = "Colores de campos y datos",
                    idGrupo = "03.2.G3.5",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Personaliza los colores individuales para Usuario, Contraseña, 2FA, Passkey, Web y Apps"
                ) {
                    ComponenteNavegacion(
                        titulo = "Colores de campos y datos",
                        icono = Icons.Filled.Palette,
                        colorIcono = Color(0xFF8E24AA),
                        idFila = "03.2.8",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.ColoresDatos()) }
                    )
                }

                // 2. Color de acento principal
                Spacer(Modifier.height(18.dp))
                SeccionColorAcento(
                    ajustes = ajustes,
                    reqAcento = reqAcento,
                    alAjustarColorAcento = { vm.ajustarColorAcento(it) }
                )

                // 4. Ícono de la app en el Launcher
                Spacer(Modifier.height(18.dp))
                SeccionIconoLauncher(
                    ajustes = ajustes,
                    haptica = haptica,
                    reqLauncher = reqLauncher,
                    alSolicitarCambioIcono = { paleta -> dialogoConfirmarIcono = paleta }
                )

                // 9. Botones de acción inferiores
                Spacer(Modifier.height(18.dp))
                ComponenteGrupo {
                    ComponenteBotonFila(
                        titulo = "Restablecer módulo",
                        alPulsar = {
                            vm.restablecerColoresTema()
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    dialogoConfirmarIcono?.let { paleta ->
        DialogoConfirmacionIconoLauncher(
            paleta = paleta,
            alConfirmar = { elegido ->
                dialogoConfirmarIcono = null
                vm.ajustarIconoLauncher(elegido.clave)
                actividad?.finishAffinity()
            },
            alDescartar = { dialogoConfirmarIcono = null }
        )
    }
}
