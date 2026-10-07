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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.plantillas.DialogoEditorPlantillaCampos
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.UUID

@Composable
fun DialogoPresetsRapidos(
    plantillasPersonalizadas: List<PlantillaCamposPersonalizada> = emptyList(),
    camposActuales: List<CampoPersonalizado> = emptyList(),
    alDescartar: () -> Unit,
    alSeleccionarPreset: (List<CampoPersonalizado>) -> Unit,
    alGuardarComoPlantilla: ((PlantillaCamposPersonalizada) -> Unit)? = null,
    alEliminarPlantilla: ((String) -> Unit)? = null
) {
    var mostrandoGuardarPlantilla by remember { mutableStateOf(false) }
    var mostrandoCrearNuevaPlantilla by remember { mutableStateOf(false) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Conjuntos de campos rápidos",
        icono = Icons.Filled.DynamicForm,
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cerrar", color = TextoSecundario)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Selecciona un conjunto sugerido para agregar sus campos a esta entrada:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

            if (camposActuales.isNotEmpty() && alGuardarComoPlantilla != null) {
                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BotonBorde(
                        texto = "Guardar actuales (${camposActuales.size})",
                        icono = Icons.Filled.BookmarkAdd,
                        modifier = Modifier.weight(1f),
                        alPulsar = { mostrandoGuardarPlantilla = true }
                    )
                    BotonBorde(
                        texto = "Crear plantilla",
                        icono = Icons.Filled.Add,
                        modifier = Modifier.weight(1f),
                        alPulsar = { mostrandoCrearNuevaPlantilla = true }
                    )
                }
                Spacer(Modifier.height(4.dp))
            } else if (alGuardarComoPlantilla != null) {
                Spacer(Modifier.height(2.dp))
                BotonBorde(
                    texto = "Crear nueva plantilla personalizada",
                    icono = Icons.Filled.Add,
                    modifier = Modifier.fillMaxWidth(),
                    alPulsar = { mostrandoCrearNuevaPlantilla = true }
                )
                Spacer(Modifier.height(4.dp))
            }

            // Sección: Plantillas personalizadas del usuario
            Text(
                text = "Plantillas personalizadas",
                color = ColorTitulos,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )

            if (plantillasPersonalizadas.isEmpty()) {
                Text(
                    text = "Aún no tienes plantillas propias creadas.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } else {
                plantillasPersonalizadas.forEach { plantilla ->
                    val fondoItem = ColorCampoAjustes
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaCampo)
                            .background(fondoItem)
                            .then(
                                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                    Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                                } else Modifier
                            )
                            .clickable {
                                val clonados = plantilla.campos.map { it.copy(id = UUID.randomUUID().toString()) }
                                alSeleccionarPreset(clonados)
                                alDescartar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(FormaPequena)
                                .background(ColorAcento),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FolderSpecial,
                                contentDescription = null,
                                tint = ColorSobreAcento,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = plantilla.titulo,
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp)
                            )
                            Text(
                                text = plantilla.descripcion.ifBlank { "${plantilla.campos.size} campo(s) guardado(s)" },
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                            )
                        }
                        if (alEliminarPlantilla != null) {
                            IconButton(
                                onClick = { alEliminarPlantilla(plantilla.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteOutline,
                                    contentDescription = "Eliminar plantilla",
                                    tint = Peligro,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Plantillas del sistema",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            val fondoItem = ColorCampoAjustes

            PresetsCampos.todos.forEach { preset ->
                val icono: ImageVector = when (preset.tipoEntradaSugerido) {
                    TipoEntrada.TARJETA -> Icons.Filled.CreditCard
                    TipoEntrada.WIFI -> Icons.Filled.Wifi
                    TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
                    TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
                    TipoEntrada.SERVIDOR -> Icons.Filled.Dns
                    TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
                    else -> Icons.Filled.DynamicForm
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(fondoItem)
                        .then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                            } else Modifier
                        )
                        .clickable {
                            alSeleccionarPreset(preset.generarCampos())
                            alDescartar()
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(FormaPequena)
                            .background(ColorAcento),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = preset.titulo,
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp)
                        )
                        Text(
                            text = preset.descripcion,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }

    if (mostrandoGuardarPlantilla && alGuardarComoPlantilla != null) {
        DialogoGuardarPlantillaCampos(
            campos = camposActuales,
            alDescartar = { mostrandoGuardarPlantilla = false },
            alGuardar = { nuevaPlantilla ->
                alGuardarComoPlantilla(nuevaPlantilla)
                mostrandoGuardarPlantilla = false
            }
        )
    }

    if (mostrandoCrearNuevaPlantilla && alGuardarComoPlantilla != null) {
        DialogoEditorPlantillaCampos(
            plantillaExistente = null,
            alDescartar = { mostrandoCrearNuevaPlantilla = false },
            alGuardar = { nueva ->
                alGuardarComoPlantilla(nueva)
                mostrandoCrearNuevaPlantilla = false
            }
        )
    }
}

@BovedaPreview
@Composable
private fun DialogoPresetsRapidosPreview() {
    DialogoPresetsRapidos(
        plantillasPersonalizadas = listOf(
            PlantillaCamposPersonalizada(
                titulo = "Mi Servidor Proxmox",
                descripcion = "IP, Node, Realm y Token"
            )
        ),
        camposActuales = emptyList(),
        alDescartar = {},
        alSeleccionarPreset = {}
    )
}
