package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.plantillas.DialogoEditorPlantillaCampos
import com.jlnavas3.bovedalocal.ui.pantallas.plantillas.DialogoImportarEntradaAPlantilla
import com.jlnavas3.bovedalocal.ui.pantallas.plantillas.GrupoPlantillasSistema
import com.jlnavas3.bovedalocal.ui.pantallas.plantillas.TarjetaPlantillaPersonalizada
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla de configuración para administrar plantillas de campos personalizadas y del sistema (03-LST-PLT).
 */
@Composable
fun PantallaPlantillasCampos(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estado by vm.estado.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val entradasConCampos = remember(entradas) {
        entradas.filter { it.camposPersonalizados.isNotEmpty() }
    }

    var plantillaAEditar by remember { mutableStateOf<PlantillaCamposPersonalizada?>(null) }
    var mostrandoCrear by remember { mutableStateOf(false) }
    var mostrandoImportar by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Plantillas de campos",
                idEtiqueta = "03-LST-PLT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-PLT-G01", "Personalizadas"),
                            AccionSaltoGrupo("03-LST-PLT-G02", "Del sistema")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerPlantillasCampos()
                            vm.avisar("Plantillas restablecidas")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Acciones rápidas de creación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotonColorido(
                        texto = "Nueva plantilla",
                        icono = Icons.Filled.Add,
                        color = ColorAcento,
                        modifier = Modifier.weight(1f),
                        alPulsar = {
                            haptica.tic()
                            mostrandoCrear = true
                        }
                    )
                    BotonBorde(
                        texto = "Desde entrada",
                        icono = Icons.Filled.FileDownload,
                        modifier = Modifier.weight(1f),
                        alPulsar = {
                            haptica.tic()
                            if (entradasConCampos.isEmpty()) {
                                vm.avisar("No hay entradas con campos personalizados para importar")
                            } else {
                                mostrandoImportar = true
                            }
                        }
                    )
                }

                // Grupo 1: Plantillas personalizadas
                ComponenteGrupo(
                    etiqueta = "Plantillas personalizadas (${ajustes.plantillasCamposPersonalizadas.size})",
                    icono = Icons.Filled.FolderSpecial,
                    idGrupo = "03-LST-PLT-G01",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = if (ajustes.plantillasCamposPersonalizadas.isNotEmpty()) {
                        {
                            haptica.tic()
                            vm.restablecerPlantillasCampos()
                            vm.avisar("Plantillas personalizadas eliminadas")
                        }
                    } else null
                ) {
                    if (ajustes.plantillasCamposPersonalizadas.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkBorder,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Sin plantillas personalizadas",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Crea un conjunto reusable o impórtalo desde una cuenta existente para aplicarlo con 1 clic al crear entradas.",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ajustes.plantillasCamposPersonalizadas.forEach { plantilla ->
                                TarjetaPlantillaPersonalizada(
                                    plantilla = plantilla,
                                    alEditar = {
                                        haptica.tic()
                                        plantillaAEditar = plantilla
                                    },
                                    alEliminar = {
                                        haptica.tic()
                                        vm.eliminarPlantillaCampos(plantilla.id)
                                        vm.avisar("Plantilla eliminada")
                                    }
                                )
                            }
                        }
                    }
                }

                // Grupo 2: Plantillas del sistema
                GrupoPlantillasSistema(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (mostrandoCrear) {
        DialogoEditorPlantillaCampos(
            plantillaExistente = null,
            alDescartar = { mostrandoCrear = false },
            alGuardar = { nueva ->
                vm.guardarPlantillaCampos(nueva)
                mostrandoCrear = false
                vm.avisar("Plantilla creada: ${nueva.titulo}")
            }
        )
    }

    if (plantillaAEditar != null) {
        DialogoEditorPlantillaCampos(
            plantillaExistente = plantillaAEditar,
            alDescartar = { plantillaAEditar = null },
            alGuardar = { editada ->
                vm.guardarPlantillaCampos(editada)
                plantillaAEditar = null
                vm.avisar("Plantilla actualizada: ${editada.titulo}")
            }
        )
    }

    if (mostrandoImportar) {
        DialogoImportarEntradaAPlantilla(
            entradasConCampos = entradasConCampos,
            alDescartar = { mostrandoImportar = false },
            alSeleccionarEntrada = { plantillaImportada ->
                vm.guardarPlantillaCampos(plantillaImportada)
                mostrandoImportar = false
                vm.avisar("Plantilla creada desde '${plantillaImportada.titulo}'")
            }
        )
    }
}

@BovedaPreview
@Composable
private fun PantallaPlantillasCamposPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .padding(16.dp)
    ) {
        ComponenteGrupo(
            etiqueta = "Plantillas personalizadas (1)",
            icono = Icons.Filled.FolderSpecial,
            idGrupo = "03-LST-PLT-G01",
            mostrarId = true
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                Text("Vista previa de plantillas", color = TextoPrincipal)
            }
        }
    }
}
