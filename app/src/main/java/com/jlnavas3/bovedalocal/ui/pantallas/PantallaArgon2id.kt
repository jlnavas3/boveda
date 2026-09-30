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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
                titulo = "Cifrado",
                idEtiqueta = "01-SEG-CRY",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01-SEG-CRY-G01", "Perfil de derivación"),
                            AccionSaltoGrupo("01-SEG-CRY-G02", "Detalles técnicos")
                        )
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
                val perfilActual = vm.repositorio.perfilArgon2Actual()

                ComponenteGrupo(
                    etiqueta = "Perfil de derivación",
                    icono = Icons.Filled.Memory,
                    colorIcono = ColorArgon2,
                    alRestablecer = {
                        if (perfilActual != PerfilArgon2.ESTANDAR) {
                            perfilPendiente = PerfilArgon2.ESTANDAR
                        } else {
                            vm.avisar("Ya estás usando el perfil Estándar recomendado")
                        }
                    },
                    idGrupo = "01-SEG-CRY-G01",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Resistencia computacional ante ataques de fuerza bruta y granjas GPU/ASIC"
                ) {
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
                        descripcionModal = "Elige el nivel de resistencia del algoritmo KDF",
                        icono = null,
                        idFila = "01-SEG-CRY-PRF",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = perfilActual,
                        opciones = opcionesArgon2,
                        alSeleccionar = { nuevoPerfil ->
                            if (nuevoPerfil != perfilActual) {
                                perfilPendiente = nuevoPerfil
                            }
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Tarjeta técnica informativa
                ComponenteGrupo(
                    etiqueta = "Detalles técnicos",
                    idGrupo = "01-SEG-CRY-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Memoria RAM: ${perfilActual.memoriaKiB / 1024} MB",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Iteraciones: ${perfilActual.iteraciones}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Paralelismo: ${perfilActual.paralelismo} hilos",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = perfilActual.detalle,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    perfilPendiente?.let { perfil ->
        DialogoContrasena(
            titulo = "Confirmar cambio de perfil Argon2id",
            descripcion = "Se re-cifrará toda la base de datos con el perfil ${perfil.titulo} (${perfil.memoriaKiB / 1024} MB RAM, ${perfil.iteraciones} iteraciones). Introduce tu contraseña maestra:",
            textoBoton = "Aplicar perfil",
            alCancelar = { perfilPendiente = null },
            alConfirmar = { clave ->
                vm.reForjarBoveda(clave, perfil) { exito ->
                    if (exito) {
                        haptica.exito()
                    } else {
                        haptica.error()
                    }
                }
                perfilPendiente = null
            }
        )
    }
}
