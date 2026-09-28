package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaColoresIds(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Colores de IDs",
                idEtiqueta = "06-SIS-AVZ-COL",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("06-SIS-AVZ-G01", "01 Seguridad"),
                            AccionSaltoGrupo("06-SIS-AVZ-G02", "02 Apariencia"),
                            AccionSaltoGrupo("06-SIS-AVZ-G03", "03 Lista de cuentas"),
                            AccionSaltoGrupo("06-SIS-AVZ-G04", "04 Herramientas"),
                            AccionSaltoGrupo("06-SIS-AVZ-G05", "05 Copias y datos"),
                            AccionSaltoGrupo("06-SIS-AVZ-G06", "06 Sistema")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerColoresIds()
                            vm.avisar("Colores de IDs restablecidos")
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
                // Bloque 01: Seguridad
                val colorSeguridad = parsearColorO(ajustes.colorIdSeguridad, Color(0xFF3F51B5))
                ComponenteGrupo(
                    etiqueta = "01 Seguridad",
                    icono = Icons.Filled.Security,
                    colorIcono = colorSeguridad,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdSeguridad("#3F51B5")
                    },
                    idGrupo = "06-SIS-AVZ-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "01-BIO-TIM-SEC", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorSeguridad,
                            titulo = "Color Seguridad (01)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdSeguridad(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Bloque 02: Apariencia
                val colorApariencia = parsearColorO(ajustes.colorIdApariencia, Color(0xFF8E24AA))
                ComponenteGrupo(
                    etiqueta = "02 Apariencia",
                    icono = Icons.Filled.Palette,
                    colorIcono = colorApariencia,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdApariencia("#8E24AA")
                    },
                    idGrupo = "06-SIS-AVZ-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "02-THM-MOD-DRK", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorApariencia,
                            titulo = "Color Apariencia (02)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdApariencia(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Bloque 03: Lista de cuentas
                val colorLista = parsearColorO(ajustes.colorIdLista, Color(0xFF00897B))
                ComponenteGrupo(
                    etiqueta = "03 Lista de cuentas",
                    icono = Icons.Filled.Layers,
                    colorIcono = colorLista,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdLista("#00897B")
                    },
                    idGrupo = "06-SIS-AVZ-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "03-LST-DEN-PAD", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorLista,
                            titulo = "Color Lista (03)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdLista(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Bloque 04: Herramientas
                val colorHerramientas = parsearColorO(ajustes.colorIdHerramientas, Color(0xFFFB8C00))
                ComponenteGrupo(
                    etiqueta = "04 Herramientas",
                    icono = Icons.Filled.Build,
                    colorIcono = colorHerramientas,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdHerramientas("#FB8C00")
                    },
                    idGrupo = "06-SIS-AVZ-G04",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "04-WGT-1X1-MOD", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorHerramientas,
                            titulo = "Color Herramientas (04)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdHerramientas(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Bloque 05: Copias y datos
                val colorCopias = parsearColorO(ajustes.colorIdCopias, Color(0xFF1E88E5))
                ComponenteGrupo(
                    etiqueta = "05 Copias y datos",
                    icono = Icons.Filled.Backup,
                    colorIcono = colorCopias,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdCopias("#1E88E5")
                    },
                    idGrupo = "06-SIS-AVZ-G05",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "05-BAK-ATM-PAS", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorCopias,
                            titulo = "Color Copias (05)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdCopias(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Bloque 06: Sistema
                val colorSistema = parsearColorO(ajustes.colorIdSistema, Color(0xFF607D8B))
                ComponenteGrupo(
                    etiqueta = "06 Sistema",
                    icono = Icons.Filled.Settings,
                    colorIcono = colorSistema,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarColorIdSistema("#607D8B")
                    },
                    idGrupo = "06-SIS-AVZ-G06",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "06-SYS-HAP-INT", ajustes = ajustes)
                            Spacer(Modifier.width(8.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectorColorEnTiempoReal(
                            colorInicial = colorSistema,
                            titulo = "Color Sistema (06)"
                        ) { nuevoColor ->
                            vm.ajustarColorIdSistema(nuevoColor.aHex())
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
