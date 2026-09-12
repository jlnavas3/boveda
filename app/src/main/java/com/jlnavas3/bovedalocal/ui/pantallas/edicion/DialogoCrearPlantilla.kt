package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPlantilla
import com.jlnavas3.bovedalocal.data.PlantillaCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun DialogoCrearPlantilla(
    camposIniciales: List<CampoPlantilla> = emptyList(),
    nombreInicial: String = "",
    haptica: Haptica,
    alGuardar: (PlantillaCampos) -> Unit,
    alCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var descripcion by remember { mutableStateOf("") }
    val campos = remember {
        mutableStateListOf<CampoPlantilla>().apply {
            if (camposIniciales.isNotEmpty()) {
                addAll(camposIniciales)
            } else {
                add(CampoPlantilla("Campo 1", TipoCampo.TEXTO))
            }
        }
    }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = alCerrar,
        title = {
            Text(
                text = if (camposIniciales.isNotEmpty()) "Guardar campos como plantilla" else "Nueva plantilla de campos",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Define un nombre y los campos que formarán esta plantilla reusable.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(12.dp))

                CampoPepo(
                    valor = nombre,
                    etiqueta = "Nombre de la plantilla (ej. Cripto, Membresía)",
                    alCambiar = {
                        nombre = it
                        if (it.isNotBlank()) errorMensaje = null
                    }
                )
                Spacer(Modifier.height(8.dp))

                CampoPepo(
                    valor = descripcion,
                    etiqueta = "Descripción breve (opcional)",
                    alCambiar = { descripcion = it }
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    "Campos incluidos (${campos.size}):",
                    style = MaterialTheme.typography.titleSmall,
                    color = ColorTitulos
                )
                Spacer(Modifier.height(8.dp))

                campos.forEachIndexed { index, campo ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(FormaTarjeta)
                            .background(Superficie)
                            .then(
                                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                                else Modifier
                            )
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Campo #${index + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextoSecundario
                            )
                            if (campos.size > 1) {
                                IconButton(
                                    onClick = {
                                        haptica.tic()
                                        campos.removeAt(index)
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Quitar campo",
                                        tint = Peligro,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))

                        // Selector de tipo de campo (TEXTO, OCULTO, PIN)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TipoCampo.entries.forEach { tipo ->
                                val activo = campo.tipo == tipo
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(FormaPequena)
                                        .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
                                        .clickable {
                                            haptica.tic()
                                            campos[index] = campo.copy(tipo = tipo)
                                        }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        tipo.etiqueta,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (activo) ColorSobreAcento else TextoSecundario
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))

                        CampoPepo(
                            valor = campo.etiqueta,
                            etiqueta = "Etiqueta del campo",
                            alCambiar = { campos[index] = campo.copy(etiqueta = it) }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                BotonBorde(
                    texto = "+ Añadir otro campo",
                    icono = Icons.Filled.Add,
                    alPulsar = {
                        haptica.tic()
                        campos.add(CampoPlantilla("Campo ${campos.size + 1}", TipoCampo.TEXTO))
                    }
                )

                if (errorMensaje != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMensaje ?: "",
                        color = Peligro,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isBlank()) {
                        errorMensaje = "Escribe un nombre para la plantilla"
                        haptica.error()
                        return@TextButton
                    }
                    if (campos.isEmpty() || campos.any { it.etiqueta.isBlank() }) {
                        errorMensaje = "Todos los campos deben tener una etiqueta"
                        haptica.error()
                        return@TextButton
                    }
                    haptica.exito()
                    alGuardar(
                        PlantillaCampos(
                            nombre = nombre.trim(),
                            descripcion = descripcion.trim(),
                            esPredeterminada = false,
                            campos = campos.toList()
                        )
                    )
                }
            ) {
                Text("Guardar plantilla", color = Ambar, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}
