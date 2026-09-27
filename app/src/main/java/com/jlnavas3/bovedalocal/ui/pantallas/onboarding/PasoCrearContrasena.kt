package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Pantalla del paso de creación de contraseña maestra y perfil de derivación Argon2id.
 */
@Composable
fun PasoCrearContrasena(
    perfilSeleccionado: PerfilArgon2,
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    alConfirmar: (String) -> Unit,
    alVolver: () -> Unit
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

    val opcionesArgon2 = remember {
        PerfilArgon2.entries.map { perfil ->
            OpcionSelectorModal(
                valor = perfil,
                etiquetaFila = perfil.titulo,
                etiquetaModal = perfil.titulo,
                descripcionModal = "${perfil.resumen}\n${perfil.detalle}",
                icono = Icons.Filled.Memory
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Tu contraseña maestra",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Es la única llave de tu bóveda. Sin ella es matemáticamente imposible descifrar los datos.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )
        }

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

        // Tarjeta 2: Parámetros y Asistencia agrupados
        ComponenteGrupo(
            etiqueta = "Parámetros y Asistencia"
        ) {
            ComponenteSelectorModal(
                titulo = "Perfil Argon2id",
                valorSeleccionado = perfilSeleccionado,
                opciones = opcionesArgon2,
                alSeleccionar = alCambiarPerfil,
                icono = Icons.Filled.Memory,
                colorIcono = ColorArgon2,
                colorTinteIcono = ColorSobreAcento,
                descripcionModal = "Parámetros KDF para la derivación de tu clave maestra"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Consejo: Generación rápida",
                alPulsar = { mostrarModalConsejo = true },
                icono = Icons.Filled.Lightbulb,
                colorIcono = ColorIconosInternos,
                colorTinteIcono = Color.White,
                valorTexto = "Tile / Widget"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Forjar la bóveda",
                alPulsar = { if (valida) alConfirmar(contrasena) },
                icono = Icons.Filled.Shield,
                colorIcono = if (valida) ColorAcento else ColorAjusteGris.copy(alpha = 0.35f),
                colorTinteIcono = if (valida) ColorSobreAcento else ColorAjusteGris,
                habilitado = valida
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Volver",
                alPulsar = alVolver,
                icono = Icons.AutoMirrored.Filled.ArrowBack,
                colorIcono = ColorAjusteGris.copy(alpha = 0.2f),
                colorTinteIcono = TextoPrincipal
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
