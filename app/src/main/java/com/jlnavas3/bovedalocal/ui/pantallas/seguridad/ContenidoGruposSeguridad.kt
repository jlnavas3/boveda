package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Contenido desplazable con los grupos de configuración de seguridad, biometría, bloqueo y portapapeles.
 */
@Composable
fun ContenidoGruposSeguridad(
    scrollState: ScrollState,
    ajustes: AjustesApp,
    esSenuelo: Boolean,
    nivel: Biometria.Nivel,
    capacidad: Biometria.Capacidad,
    modoActivo: BiometricKeyStore.Modo?,
    vm: VaultViewModel,
    haptica: Haptica,
    alCambiarBiometria: (Boolean) -> Unit,
    alRegistrarHuellaAndroid: () -> Unit,
    alOfrecerCompatible: () -> Unit,
    alActivarFuerte: () -> Unit,
    alPedirDesactivarSecure: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        DescripcionPantalla(subtitulo = "Autenticación, bloqueo automático y portapapeles")
        Spacer(Modifier.height(10.dp))

        // Grupo 1: Biometría
        GrupoBiometria(
            esSenuelo = esSenuelo,
            biometriaActiva = ajustes.biometriaActiva,
            nivel = nivel,
            capacidad = capacidad,
            modoActivo = modoActivo,
            mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
            alCambiarBiometria = alCambiarBiometria,
            alRegistrarHuellaAndroid = alRegistrarHuellaAndroid,
            alOfrecerCompatible = alOfrecerCompatible,
            alActivarFuerte = alActivarFuerte
        )

        Spacer(Modifier.height(18.dp))

        // Grupo 2: Bloqueo de aplicación
        GrupoBloqueoApp(
            autoBloqueoSegundos = ajustes.autoBloqueoSegundos,
            proteccionPantalla = ajustes.proteccionPantalla,
            mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
            alAjustarAutoBloqueo = { valor ->
                haptica.tic()
                vm.ajustarAutoBloqueo(valor)
            },
            alCambiarProteccionPantalla = { activar ->
                if (activar) {
                    vm.ajustarProteccionPantalla(true)
                    haptica.tic()
                } else {
                    alPedirDesactivarSecure()
                }
            },
            alRestablecer = {
                haptica.tic()
                vm.restablecerBloqueoApp()
                vm.avisar("Valores de bloqueo restablecidos")
            }
        )

        Spacer(Modifier.height(18.dp))

        // Grupo: Protección contra fuerza bruta
        GrupoFrenoFuerzaBruta(
            frenoIntentosGratis = ajustes.frenoIntentosGratis,
            frenoSegundosMax = ajustes.frenoSegundosMax,
            mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
            alAjustarIntentos = { valor ->
                haptica.tic()
                vm.ajustarFrenoIntentosGratis(valor)
            },
            alAjustarMaxTiempo = { segundos ->
                haptica.tic()
                vm.ajustarFrenoSegundosMax(segundos)
            },
            alRestablecer = {
                haptica.tic()
                vm.restablecerFrenoIntentos()
                vm.avisar("Protección contra fuerza bruta restablecida")
            }
        )

        Spacer(Modifier.height(18.dp))

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

        // Grupo 3: Portapapeles
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

        // Grupo 4: Auditoría de contraseñas (Salud)
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
