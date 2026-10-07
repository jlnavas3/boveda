package com.jlnavas3.bovedalocal.ui.pantallas.plantillas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.DialogoNuevoCampo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoEditorPlantillaCampos(
    plantillaExistente: PlantillaCamposPersonalizada? = null,
    alDescartar: () -> Unit,
    alGuardar: (PlantillaCamposPersonalizada) -> Unit
) {
    var titulo by remember { mutableStateOf(plantillaExistente?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(plantillaExistente?.descripcion ?: "") }
    val campos = remember {
        mutableStateListOf<CampoPersonalizado>().apply {
            if (plantillaExistente != null) addAll(plantillaExistente.campos)
        }
    }
    var mostrandoDialogoNuevoCampo by remember { mutableStateOf(false) }

    val puedeGuardar = titulo.isNotBlank() && campos.isNotEmpty()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = if (plantillaExistente == null) "Nueva plantilla de campos" else "Editar plantilla",
        icono = Icons.Filled.FolderSpecial,
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        },
        botonConfirmar = {
            BotonColorido(
                texto = "Guardar",
                icono = Icons.Filled.Save,
                color = ColorAcento,
                activo = puedeGuardar,
                alPulsar = {
                    if (puedeGuardar) {
                        alGuardar(
                            PlantillaCamposPersonalizada(
                                id = plantillaExistente?.id ?: java.util.UUID.randomUUID().toString(),
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim(),
                                campos = campos.toList()
                            )
                        )
                        alDescartar()
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = titulo,
                alCambiar = { titulo = it },
                etiqueta = "Nombre de la plantilla *",
                modifier = Modifier.fillMaxWidth()
            )

            CampoBoveda(
                valor = descripcion,
                alCambiar = { descripcion = it },
                etiqueta = "Descripción (opcional)",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Campos de la plantilla (${campos.size}):",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                BotonBorde(
                    texto = "Añadir campo",
                    icono = Icons.Filled.Add,
                    alPulsar = { mostrandoDialogoNuevoCampo = true }
                )
            }

            if (campos.isEmpty()) {
                Text(
                    text = "Debes añadir al menos un campo para guardar esta plantilla.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                campos.forEachIndexed { index, campo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .background(SuperficieAlta)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (campo.esSensibleEfectivo) {
                                Icon(
                                    imageVector = Icons.Filled.Security,
                                    contentDescription = "Sensible",
                                    tint = ColorAcento,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                            }
                            Column {
                                Text(
                                    text = campo.etiqueta.ifBlank { "Campo ${index + 1}" },
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    text = campo.tipo.etiqueta,
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { campos.removeAt(index) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DeleteOutline,
                                contentDescription = "Quitar campo",
                                tint = Peligro,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (mostrandoDialogoNuevoCampo) {
        DialogoNuevoCampo(
            alDescartar = { mostrandoDialogoNuevoCampo = false },
            alCrearCampo = { nuevoCampo ->
                campos.add(nuevoCampo)
                mostrandoDialogoNuevoCampo = false
            }
        )
    }
}

@BovedaPreview
@Composable
private fun DialogoEditorPlantillaCamposPreview() {
    DialogoEditorPlantillaCampos(
        plantillaExistente = null,
        alDescartar = {},
        alGuardar = {}
    )
}
