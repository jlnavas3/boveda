package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica de configuración de seguridad visual (privacidad de pantalla),
 * limpieza de portapapeles y auditoría de antigüedad de contraseñas.
 */
@Composable
fun PantallaSeguridadVisualMemoria(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Seguridad visual y portapapeles",
                idEtiqueta = "01-SEG-BIO-VIS",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01-SEG-BIO-G06", "Seguridad visual"),
                            AccionSaltoGrupo("01-SEG-BIO-G03", "Portapapeles"),
                            AccionSaltoGrupo("01-SEG-BIO-G04", "Auditoría de contraseñas")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerSeguridadVisual()
                            vm.restablecerPortapapeles()
                            vm.restablecerUmbralAntiguedad()
                            vm.avisar("Ajustes de privacidad restablecidos")
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
                DescripcionPantalla(subtitulo = "Privacidad de campos en pantalla, desenfoque visual y persistencia en memoria")
                Spacer(Modifier.height(10.dp))

                // Grupo: Seguridad visual (Privacidad de pantalla)
                GrupoSeguridadVisual(
                    seguridadVisualActiva = ajustes.seguridadVisualActiva,
                    estiloOcultamientoVisual = ajustes.estiloOcultamientoVisual,
                    tiempoAutoOcultarSegundos = ajustes.tiempoAutoOcultarSegundos,
                    ocultarUsuario = ajustes.ocultarUsuario,
                    ocultarContrasena = ajustes.ocultarContrasena,
                    ocultarTotp = ajustes.ocultarTotp,
                    ocultarNotas = ajustes.ocultarNotas,
                    ocultarCampos = ajustes.ocultarCampos,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alCambiarSeguridadVisualActiva = { activar ->
                        haptica.tic()
                        vm.ajustarSeguridadVisualActiva(activar)
                    },
                    alCambiarEstiloOcultamiento = { estilo ->
                        haptica.tic()
                        vm.ajustarEstiloOcultamientoVisual(estilo)
                    },
                    alCambiarTiempoAutoOcultar = { segundos ->
                        haptica.tic()
                        vm.ajustarTiempoAutoOcultar(segundos)
                    },
                    alCambiarOcultarUsuario = { activar ->
                        haptica.tic()
                        vm.ajustarOcultarUsuario(activar)
                    },
                    alCambiarOcultarContrasena = { activar ->
                        haptica.tic()
                        vm.ajustarOcultarContrasena(activar)
                    },
                    alCambiarOcultarTotp = { activar ->
                        haptica.tic()
                        vm.ajustarOcultarTotp(activar)
                    },
                    alCambiarOcultarNotas = { activar ->
                        haptica.tic()
                        vm.ajustarOcultarNotas(activar)
                    },
                    alCambiarOcultarCampos = { activar ->
                        haptica.tic()
                        vm.ajustarOcultarCampos(activar)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerSeguridadVisual()
                        vm.avisar("Valores de seguridad visual restablecidos")
                    }
                )

                Spacer(Modifier.height(18.dp))

                // Grupo: Portapapeles
                GrupoPortapapeles(
                    portapapelesSegundos = ajustes.portapapelesSegundos,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarPortapapeles = { valor ->
                        haptica.tic()
                        vm.ajustarPortapapeles(valor)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerPortapapeles()
                        vm.avisar("Tiempo de portapapeles restablecido")
                    }
                )

                Spacer(Modifier.height(18.dp))

                // Grupo: Auditoría de contraseñas (Salud)
                GrupoAntiguedadSalud(
                    umbralAntiguedadDias = ajustes.umbralAntiguedadDias,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarUmbral = { valor ->
                        haptica.tic()
                        vm.ajustarUmbralAntiguedad(valor)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerUmbralAntiguedad()
                        vm.avisar("Umbral de antigüedad restablecido")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Preview(name = "Pantalla Seguridad Visual y Memoria", showBackground = true)
@Composable
private fun PantallaSeguridadVisualMemoriaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Seguridad visual y portapapeles",
                idEtiqueta = "01-SEG-BIO-VIS",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Privacidad de campos en pantalla, desenfoque visual y persistencia en memoria")
        }
    }
}
