package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Pantalla del paso de creación de contraseña y perfil de derivación Argon2id.
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
    var menuAbierto by remember { mutableStateOf(false) }

    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val coinciden = contrasena.isNotEmpty() && contrasena == repetida
    val valida = contrasena.length >= 10 && coinciden
    val scrollState = rememberScrollState()

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
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior con botón Volver y menú contextual de opciones
        BarraSuperiorPantalla(
            titulo = "Contraseña",
            alVolver = alVolver,
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                Box {
                    BotonIconoCabecera(
                        onClick = { menuAbierto = true },
                        icono = Icons.Default.MoreVert,
                        descripcion = "Más opciones"
                    )

                    MenuDesplegableBoveda(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false },
                        modifier = Modifier.widthIn(min = 200.dp, max = 260.dp)
                    ) {
                        ElementoMenuCompacto(
                            texto = "Consejo",
                            icono = Icons.Filled.Lightbulb,
                            colorIcono = ColorIconosInternos,
                            colorTexto = TextoPrincipal,
                            onClick = {
                                menuAbierto = false
                                mostrarModalConsejo = true
                            }
                        )

                        ElementoMenuCompacto(
                            texto = "Acerca de",
                            icono = Icons.Filled.Info,
                            colorIcono = ColorIconosInternos,
                            colorTexto = TextoPrincipal,
                            onClick = {
                                menuAbierto = false
                                alAbrirAcercaDe()
                            }
                        )
                    }
                }
            }
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
            Text(
                text = "Es la única llave de tu bóveda. Sin ella es matemáticamente imposible descifrar los datos.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
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

            // Tarjeta 2: Parámetros de derivación
            ComponenteGrupo(
                etiqueta = "Parámetros de derivación"
            ) {
                ComponenteSelectorModal(
                    titulo = "Perfil Argon2id",
                    valorSeleccionado = perfilSeleccionado,
                    opciones = opcionesArgon2,
                    alSeleccionar = alCambiarPerfil,
                    icono = Icons.Filled.Memory,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos,
                    descripcionModal = "Parámetros KDF para la derivación de tu clave maestra"
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
            }
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
