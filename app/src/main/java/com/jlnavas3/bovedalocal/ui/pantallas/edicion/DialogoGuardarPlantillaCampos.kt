package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoGuardarPlantillaCampos(
    campos: List<CampoPersonalizado>,
    alDescartar: () -> Unit,
    alGuardar: (PlantillaCamposPersonalizada) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val puedeGuardar = titulo.isNotBlank() && campos.isNotEmpty()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Guardar como plantilla",
        icono = Icons.Filled.BookmarkAdd,
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        },
        botonConfirmar = {
            BotonColorido(
                texto = "Guardar plantilla",
                icono = Icons.Filled.Save,
                color = ColorAcento,
                activo = puedeGuardar,
                alPulsar = {
                    if (puedeGuardar) {
                        alGuardar(
                            PlantillaCamposPersonalizada(
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim(),
                                campos = campos
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
            Text(
                text = "Guarda los ${campos.size} campos de esta entrada como una plantilla reutilizable para futuras entradas.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

            Spacer(Modifier.height(4.dp))

            CampoBoveda(
                valor = titulo,
                alCambiar = { titulo = it },
                etiqueta = "Nombre de la plantilla *",
                modifier = Modifier.fillMaxWidth()
            )

            CampoBoveda(
                valor = descripcion,
                alCambiar = { descripcion = it },
                etiqueta = "Descripción breve (opcional)",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@BovedaPreview
@Composable
private fun DialogoGuardarPlantillaCamposPreview() {
    DialogoGuardarPlantillaCampos(
        campos = listOf(
            CampoPersonalizado(etiqueta = "Servidor IP"),
            CampoPersonalizado(etiqueta = "Puerto SSH")
        ),
        alDescartar = {},
        alGuardar = {}
    )
}
