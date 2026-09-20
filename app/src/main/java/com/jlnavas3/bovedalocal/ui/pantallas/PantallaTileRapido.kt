package com.jlnavas3.bovedalocal.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaTileRapido(
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
                titulo = "Mosaico rápido de Android",
                idEtiqueta = "04.3",
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
                DescripcionPantalla(subtitulo = "Genera contraseñas desde los ajustes rápidos de la cortina de Android")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Modo de generación
                ComponenteGrupo(
                    etiqueta = "Modo de generación",
                    idGrupo = "04.3.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Añade el mosaico en la barra rápida de Android para generar con un toque"
                ) {
                    val opcionesModo = remember {
                        listOf(
                            OpcionSelectorModal("longitud", "Por longitud", "Longitud de caracteres", "Genera una contraseña aleatoria de longitud fija", Icons.Filled.Key),
                            OpcionSelectorModal("patron", "Por patrón", "Por patrón personalizado", "Genera según máscara de caracteres (XXXX-XXXX)", Icons.Filled.Tune)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Modo de generación",
                        descripcionModal = "Elige la estrategia de generación al pulsar el mosaico del sistema",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorGenerador,
                        idFila = "04.3.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.tileModo,
                        opciones = opcionesModo,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarTileModo(valor)
                        }
                    )

                    ComponenteSeparador()

                    if (ajustes.tileModo == "longitud") {
                        val opcionesLongitud = remember {
                            AlmacenAjustes.OPCIONES_TILE_LONGITUD.map { (valor, etiqueta) ->
                                OpcionSelectorModal(
                                    valor = valor,
                                    etiquetaFila = "$valor car.",
                                    etiquetaModal = etiqueta,
                                    descripcionModal = "Clave de $valor caracteres aleatorios",
                                    icono = Icons.Filled.Key
                                )
                            }
                        }
                        ComponenteSelectorModal(
                            titulo = "Longitud de la clave",
                            descripcionModal = "Cantidad de caracteres generados para la nueva clave",
                            icono = Icons.Filled.Key,
                            colorIcono = ColorGenerador,
                            idFila = "04.3.2",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorSeleccionado = ajustes.tileLongitud,
                            opciones = opcionesLongitud,
                            alSeleccionar = { valor ->
                                haptica.tic()
                                vm.ajustarTileLongitud(valor)
                            }
                        )
                    } else {
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            Column {
                                com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto(
                                    valor = ajustes.tilePatron,
                                    etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX)",
                                    alCambiar = { vm.ajustarTilePatron(it) },
                                    monoespaciada = true
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "X: alfanum. mayúscula | A: letra mayúscula | a: minúscula | 9: dígito | w: palabra Diceware",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    ComponenteSeparador()

                    ComponenteSwitch(
                        titulo = "Copiar al portapapeles",
                        icono = Icons.Filled.ContentCopy,
                        colorIcono = ColorGenerador,
                        idFila = "04.3.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        activo = ajustes.tileCopiarPortapapeles,
                        alCambiar = {
                            haptica.toque()
                            vm.ajustarTileCopiarPortapapeles(it)
                            Toast.makeText(
                                contexto,
                                if (it) "Copia al portapapeles activada" else "Copia al portapapeles desactivada",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    ComponenteSeparador()

                    ComponenteSwitch(
                        titulo = "Aviso emergente (Toast)",
                        icono = Icons.Filled.Notifications,
                        colorIcono = ColorGenerador,
                        idFila = "04.3.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        activo = ajustes.tileMostrarToast,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarTileMostrarToast(it)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer grupo",
                        alPulsar = {
                            vm.ajustarTileModo("longitud")
                            vm.ajustarTileLongitud(20)
                            vm.ajustarTilePatron("XXXXX-XXXXX-XXXXX-XXXXX")
                            vm.ajustarTileCopiarPortapapeles(true)
                            vm.ajustarTileMostrarToast(true)
                            vm.ajustarTileHaptica(true)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Widgets de inicio
                ComponenteGrupo(
                    etiqueta = "Widgets de inicio",
                    idGrupo = "04.3.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Acceso directo en la pantalla de inicio"
                ) {
                    ComponenteNavegacion(
                        titulo = "Personalizar widgets de escritorio",
                        icono = Icons.Filled.Widgets,
                        colorIcono = ColorGenerador,
                        idFila = "04.3.5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.AjustesWidget("03.3"))
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
