package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Pantalla del paso de creación de contraseña y perfil de derivación Argon2id ("Forjar la bóveda").
 */
@Composable
fun PasoCrearContrasena(
    perfilSeleccionado: PerfilArgon2,
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    alConfirmar: (String) -> Unit,
    alVolver: () -> Unit,
    alAbrirAcercaDe: () -> Unit
) {
    val contexto = LocalContext.current
    var contrasena by remember { mutableStateOf("") }
    var repetida by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarRepetida by remember { mutableStateOf(false) }

    var mostrarModalConsejo by remember { mutableStateOf(false) }
    var mostrarDialogoCompartir by remember { mutableStateOf(false) }

    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val coinciden = contrasena.isNotEmpty() && contrasena == repetida
    val valida = contrasena.length >= 10 && coinciden
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior con botón Volver y menú contextual
        BarraSuperiorCrearContrasena(
            alVolver = alVolver,
            conSeparador = scrollState.value > 0,
            alAbrirConsejo = { mostrarModalConsejo = true },
            alAbrirAcercaDe = alAbrirAcercaDe
        )

        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextoSubtitulo(
                texto = "Es la única llave de tu bóveda. Sin ella es matemáticamente imposible descifrar los datos.",
                alineacion = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Tarjeta 1: Credenciales maestras agrupadas
            TarjetaCredencialesMaestras(
                contrasena = contrasena,
                repetida = repetida,
                alCambiarContrasena = { contrasena = it },
                alCambiarRepetida = { repetida = it },
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                mostrarRepetida = mostrarRepetida,
                alAlternarMostrarRepetida = { mostrarRepetida = !mostrarRepetida },
                fuerza = fuerza,
                coinciden = coinciden,
                alCompartir = { mostrarDialogoCompartir = true }
            )

            // Tarjeta 2: Parámetros de derivación y botón "Forjar la bóveda"
            TarjetaParametrosDerivacion(
                perfilSeleccionado = perfilSeleccionado,
                alCambiarPerfil = alCambiarPerfil,
                validaParaForjar = valida,
                alForjarBoveda = { if (valida) alConfirmar(contrasena) }
            )
        }
    }

    if (mostrarModalConsejo) {
        DialogoConsejoTile(alCerrar = { mostrarModalConsejo = false })
    }

    if (mostrarDialogoCompartir) {
        DialogoAdvertenciaCompartir(
            contrasena = contrasena,
            contexto = contexto,
            alCerrar = { mostrarDialogoCompartir = false }
        )
    }
}
