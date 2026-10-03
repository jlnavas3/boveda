package com.jlnavas3.bovedalocal.ui.pantallas

import android.app.Activity
import android.os.Build
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
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
        val mostrarColorAcento = !ajustes.colorDinamicoSistema || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Tema y colores",
                idEtiqueta = "02-APA-THM",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = buildList {
                            add(AccionSaltoGrupo("02-APA-THM-G01", "Modo de tema"))
                            add(AccionSaltoGrupo("02-APA-THM-G02", "Pantalla de desbloqueo"))
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                add(AccionSaltoGrupo("02-APA-THM-G03", "Color dinámico"))
                            }
                            if (mostrarColorAcento) {
                                add(AccionSaltoGrupo("02-APA-THM-G05", "Color de acento"))
                            }
                            add(AccionSaltoGrupo("02-APA-THM-G06", "Ícono en el launcher"))
                        },
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerColoresTema()
                            vm.avisar("Tema y colores restablecidos")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 0. Modo de tema (Sistema, Claro, Oscuro)
                SeccionModoTema(
                    ajustes = ajustes,
                    alCambiarTema = { vm.ajustarTema(it) }
                )

                Spacer(Modifier.height(14.dp))

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

                // 2. Color de acento principal (solo si Material You está desactivado)
                if (mostrarColorAcento) {
                    Spacer(Modifier.height(14.dp))
                    SeccionColorAcento(
                        ajustes = ajustes,
                        reqAcento = reqAcento,
                        alAjustarColorAcento = { vm.ajustarColorAcento(it) }
                    )
                }

                // 4. Ícono de la app en el Launcher
                Spacer(Modifier.height(14.dp))
                SeccionIconoLauncher(
                    ajustes = ajustes,
                    haptica = haptica,
                    reqLauncher = reqLauncher,
                    alSolicitarCambioIcono = { paleta -> dialogoConfirmarIcono = paleta }
                )

                // 5. Laboratorio de Temas y Paleta Sobria
                Spacer(Modifier.height(14.dp))
                ComponenteGrupo(
                    etiqueta = "Laboratorio y personalización",
                    idGrupo = "02-APA-THM-G07",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Laboratorio de temas",
                        subtitulo = "Escala de grises, restricción de luminancia y exportar",
                        icono = Icons.Filled.Palette,
                        alPulsar = { vm.ir(Pantalla.LaboratorioTemas()) }
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
