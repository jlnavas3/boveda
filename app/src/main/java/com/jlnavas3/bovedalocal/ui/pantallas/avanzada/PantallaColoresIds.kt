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
import com.jlnavas3.bovedalocal.data.AjustesDefaults
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
                            vm.restablecerColoresIds()
                        },
                        mensajeToastRestablecer = "Colores de IDs restablecidos"
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
                val colorSeguridad = parsearColorO(ajustes.colorIdSeguridad, parsearColorO(AjustesDefaults.ColoresIds.SEGURIDAD, Color.Blue))
                ComponenteGrupo(
                    etiqueta = "01 Seguridad",
                    icono = Icons.Filled.Security,
                    colorIcono = colorSeguridad,
                    alRestablecer = {
                        vm.ajustarColorIdSeguridad(AjustesDefaults.ColoresIds.SEGURIDAD)
                    },
                    idGrupo = "06-SIS-AVZ-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "01-SEG-BIO", ajustes = ajustes)
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
                val colorApariencia = parsearColorO(ajustes.colorIdApariencia, parsearColorO(AjustesDefaults.ColoresIds.APARIENCIA, Color.Magenta))
                ComponenteGrupo(
                    etiqueta = "02 Apariencia",
                    icono = Icons.Filled.Palette,
                    colorIcono = colorApariencia,
                    alRestablecer = {
                        vm.ajustarColorIdApariencia(AjustesDefaults.ColoresIds.APARIENCIA)
                    },
                    idGrupo = "06-SIS-AVZ-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "02-APA-THM", ajustes = ajustes)
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
                val colorLista = parsearColorO(ajustes.colorIdLista, parsearColorO(AjustesDefaults.ColoresIds.LISTA, Color.Cyan))
                ComponenteGrupo(
                    etiqueta = "03 Lista de cuentas",
                    icono = Icons.Filled.Layers,
                    colorIcono = colorLista,
                    alRestablecer = {
                        vm.ajustarColorIdLista(AjustesDefaults.ColoresIds.LISTA)
                    },
                    idGrupo = "06-SIS-AVZ-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "03-LST-DES", ajustes = ajustes)
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
                val colorHerramientas = parsearColorO(ajustes.colorIdHerramientas, parsearColorO(AjustesDefaults.ColoresIds.HERRAMIENTAS, Color.Yellow))
                ComponenteGrupo(
                    etiqueta = "04 Herramientas",
                    icono = Icons.Filled.Build,
                    colorIcono = colorHerramientas,
                    alRestablecer = {
                        vm.ajustarColorIdHerramientas(AjustesDefaults.ColoresIds.HERRAMIENTAS)
                    },
                    idGrupo = "06-SIS-AVZ-G04",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "04-HER-WGT", ajustes = ajustes)
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
                val colorCopias = parsearColorO(ajustes.colorIdCopias, parsearColorO(AjustesDefaults.ColoresIds.COPIAS, Color.Blue))
                ComponenteGrupo(
                    etiqueta = "05 Copias y datos",
                    icono = Icons.Filled.Backup,
                    colorIcono = colorCopias,
                    alRestablecer = {
                        vm.ajustarColorIdCopias(AjustesDefaults.ColoresIds.COPIAS)
                    },
                    idGrupo = "06-SIS-AVZ-G05",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "05-COP-ATM", ajustes = ajustes)
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
                val colorSistema = parsearColorO(ajustes.colorIdSistema, parsearColorO(AjustesDefaults.ColoresIds.SISTEMA, Color.Gray))
                ComponenteGrupo(
                    etiqueta = "06 Sistema",
                    icono = Icons.Filled.Settings,
                    colorIcono = colorSistema,
                    alRestablecer = {
                        vm.ajustarColorIdSistema(AjustesDefaults.ColoresIds.SISTEMA)
                    },
                    idGrupo = "06-SIS-AVZ-G06",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InsigniaIdAjuste(id = "06-SIS-AVZ", ajustes = ajustes)
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
