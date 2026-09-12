package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.GestorPlantillasCampos
import com.jlnavas3.bovedalocal.data.PlantillaCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun DialogoPlantillasCampos(
    plantillasPersonalizadasRaw: String,
    haptica: Haptica,
    alSeleccionarPlantilla: (List<CampoPersonalizado>) -> Unit,
    alGuardarPlantillaNueva: (PlantillaCampos) -> Unit,
    alEliminarPlantilla: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val plantillasPersonalizadas = remember(plantillasPersonalizadasRaw) {
        GestorPlantillasCampos.decodificarPersonalizadas(plantillasPersonalizadasRaw)
    }
    var pestañaSeleccionada by remember { mutableIntStateOf(0) } // 0: Predeterminadas, 1: Mis plantillas
    var mostrandoCrearNueva by remember { mutableStateOf(false) }
    var plantillaAEliminar by remember { mutableStateOf<PlantillaCampos?>(null) }

    AlertDialog(
        onDismissRequest = alCerrar,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Layers,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Plantillas de campos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                // Selector de pestañas: Predeterminadas vs Personalizadas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaBoton)
                        .background(SuperficieAlta)
                        .padding(4.dp)
                ) {
                    val pestanas = listOf(
                        "Predeterminadas (${GestorPlantillasCampos.PREDETERMINADAS.size})",
                        "Mis plantillas (${plantillasPersonalizadas.size})"
                    )
                    pestanas.forEachIndexed { i, texto ->
                        val activa = pestañaSeleccionada == i
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(FormaBoton)
                                .background(if (activa) Ambar else Color.Transparent)
                                .clickable {
                                    haptica.tic()
                                    pestañaSeleccionada = i
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = texto,
                                color = if (activa) Color.Black else TextoSecundario,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Lista de plantillas según la pestaña
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    val listaAMostrar = if (pestañaSeleccionada == 0) {
                        GestorPlantillasCampos.PREDETERMINADAS
                    } else {
                        plantillasPersonalizadas
                    }

                    if (listaAMostrar.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No tienes plantillas personalizadas aún.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Pulsa \"+ Crear plantilla\" para crear una con los campos que sueles utilizar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextoSecundario.copy(alpha = 0.8f)
                            )
                        }
                    } else {
                        listaAMostrar.forEach { plantilla ->
                            TarjetaPlantillaItem(
                                plantilla = plantilla,
                                haptica = haptica,
                                alInsertar = {
                                    haptica.exito()
                                    alSeleccionarPlantilla(plantilla.aCamposPersonalizados())
                                    alCerrar()
                                },
                                alEliminar = if (!plantilla.esPredeterminada) {
                                    { plantillaAEliminar = plantilla }
                                } else null
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Botón "+ Crear nueva plantilla"
                BotonBorde(
                    texto = "+ Crear nueva plantilla",
                    icono = Icons.Filled.Add,
                    alPulsar = {
                        haptica.tic()
                        mostrandoCrearNueva = true
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar")
            }
        }
    )

    if (mostrandoCrearNueva) {
        DialogoCrearPlantilla(
            haptica = haptica,
            alGuardar = { nueva ->
                mostrandoCrearNueva = false
                alGuardarPlantillaNueva(nueva)
                pestañaSeleccionada = 1 // Cambiar a la pestaña de personalizadas para verla
            },
            alCerrar = { mostrandoCrearNueva = false }
        )
    }

    if (plantillaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { plantillaAEliminar = null },
            title = { Text("¿Eliminar plantilla?") },
            text = { Text("¿Seguro que deseas eliminar \"${plantillaAEliminar?.nombre}\"? Las entradas existentes que usaron esta plantilla no se verán afectadas.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = plantillaAEliminar?.id
                        plantillaAEliminar = null
                        if (id != null) {
                            haptica.tic()
                            alEliminarPlantilla(id)
                        }
                    }
                ) {
                    Text("Eliminar", color = Peligro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { plantillaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaPlantillaItem(
    plantilla: PlantillaCampos,
    haptica: Haptica,
    alInsertar: () -> Unit,
    alEliminar: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plantilla.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
                if (plantilla.descripcion.isNotBlank()) {
                    Text(
                        text = plantilla.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }

            if (alEliminar != null) {
                IconButton(
                    onClick = alEliminar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar plantilla",
                        tint = Peligro,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Lista de chips con los nombres de campos que incluye
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            plantilla.campos.forEach { campo ->
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(SuperficieAlta)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${campo.etiqueta}${if (campo.tipo != TipoCampo.TEXTO) " (${campo.tipo.etiqueta})" else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (campo.tipo == TipoCampo.PIN) Ambar else TextoPrincipal
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Botón para aplicar esta plantilla
        BotonColorido(
            texto = "Usar plantilla",
            color = ColorAcento,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alInsertar
        )
    }
}
