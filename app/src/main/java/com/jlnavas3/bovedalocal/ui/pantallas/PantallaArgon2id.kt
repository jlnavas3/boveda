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
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaArgon2id(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var perfilPendiente by remember { mutableStateOf<PerfilArgon2?>(null) }
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Cifrado Argon2id",
                idEtiqueta = "01.4",
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
                DescripcionPantalla(subtitulo = "Parámetros de derivación de claves contra ataques de fuerza bruta y hardware GPU/ASIC")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Perfil de derivación de clave",
                    idGrupo = "01.4.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Resistencia computacional ante ataques de fuerza bruta y granjas GPU/ASIC"
                ) {
                    val perfilActual = vm.repositorio.perfilArgon2Actual()
                    val opcionesArgon2 = remember {
                        PerfilArgon2.entries.map { perfil ->
                            val icono = if (perfil == PerfilArgon2.ULTRASEGURO) Icons.Filled.Shield else Icons.Filled.Memory
                            OpcionSelectorModal(
                                valor = perfil,
                                etiquetaFila = perfil.titulo,
                                etiquetaModal = perfil.titulo,
                                descripcionModal = "${perfil.resumen}\n${perfil.detalle}",
                                icono = icono
                            )
                        }
                    }

                    ComponenteSelectorModal(
                        titulo = "Perfil Argon2id",
                        descripcionModal = "Selecciona el perfil criptográfico para proteger la bóveda",
                        icono = Icons.Filled.Memory,
                        colorIcono = ColorArgon2,
                        idFila = "01.4.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = perfilActual,
                        opciones = opcionesArgon2,
                        alSeleccionar = { nuevo ->
                            if (nuevo != perfilActual) {
                                if (vm.repositorio.estaDesbloqueada) {
                                    perfilPendiente = nuevo
                                } else {
                                    vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = nuevo.clave) }
                                    Diagnostico.apuntar("bóveda", "Perfil Argon2id predeterminado establecido en ${nuevo.titulo}")
                                    vm.avisar("Perfil de cifrado predeterminado: ${nuevo.titulo}")
                                }
                            }
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer",
                        alPulsar = {
                            val perfilDef = PerfilArgon2.ESTANDAR
                            if (perfilActual != perfilDef) {
                                if (vm.repositorio.estaDesbloqueada) {
                                    perfilPendiente = perfilDef
                                } else {
                                    vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = perfilDef.clave) }
                                    Diagnostico.apuntar("bóveda", "Perfil Argon2id predeterminado establecido en ${perfilDef.titulo}")
                                    vm.avisar("Perfil de cifrado predeterminado: ${perfilDef.titulo}")
                                }
                            }
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    perfilPendiente?.let { objetivo ->
        DialogoContrasena(
            titulo = "Aplicar ${objetivo.titulo}",
            descripcion = "Para re-cifrar la bóveda con ${objetivo.resumen}, introduce tu contraseña maestra.\n\nNota criptográfica: La huella dactilar no conoce tu contraseña (solo custodia la llave en Keystore). Se requiere tu contraseña maestra para re-derivar la clave con ${objetivo.memoriaKiB / 1024} MiB de memoria.",
            textoBoton = "Re-cifrar bóveda",
            alConfirmar = { pass ->
                vm.reForjarBoveda(pass, objetivo) { exito ->
                    if (exito) {
                        haptica.exito()
                        perfilPendiente = null
                    }
                }
            },
            alCancelar = { perfilPendiente = null }
        )
    }
}
