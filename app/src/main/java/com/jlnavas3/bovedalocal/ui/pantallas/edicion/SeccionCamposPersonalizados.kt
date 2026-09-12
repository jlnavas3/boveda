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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Layers
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.CampoPlantilla
import com.jlnavas3.bovedalocal.data.PlantillaCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
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
fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit
) {
    var mostrarValor by remember { mutableStateOf(false) }
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
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Campo #$numero",
                style = MaterialTheme.typography.titleSmall,
                color = ColorTitulos
            )
            IconButton(onClick = alEliminar, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar campo", tint = Peligro, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoCampo.entries.forEach { t ->
                val activo = campo.tipo == t
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(FormaPequena)
                        .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
                        .then(
                            if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, FormaPequena)
                            else Modifier
                        )
                        .clickable { alModificar(campo.copy(tipo = t)) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        t.etiqueta,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (activo) ColorSobreAcento else TextoSecundario
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        CampoPepo(
            valor = campo.etiqueta,
            etiqueta = "Nombre (ej. PIN Cajero, Pregunta de seguridad)",
            alCambiar = { alModificar(campo.copy(etiqueta = it)) }
        )
        Spacer(Modifier.height(8.dp))
        CampoPepo(
            valor = campo.valor,
            etiqueta = if (campo.tipo == TipoCampo.PIN) "Valor del PIN" else "Valor del campo",
            alCambiar = { alModificar(campo.copy(valor = it)) },
            esContrasena = campo.tipo != TipoCampo.TEXTO,
            mostrarContrasena = mostrarValor,
            alAlternarMostrarContrasena = if (campo.tipo != TipoCampo.TEXTO) { { mostrarValor = !mostrarValor } } else null,
            monoespaciada = campo.tipo != TipoCampo.TEXTO
        )
    }
}

@Composable
fun SeccionCamposPersonalizados(
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    plantillasPersonalizadasRaw: String = "",
    alGuardarPlantillaNueva: ((PlantillaCampos) -> Unit)? = null,
    alEliminarPlantilla: ((String) -> Unit)? = null,
    haptica: Haptica
) {
    var mostrandoDialogoPlantillas by remember { mutableStateOf(false) }
    var mostrandoGuardarComoPlantilla by remember { mutableStateOf(false) }

    EtiquetaSeccion("Campos personalizados")
    Spacer(Modifier.height(8.dp))
    if (camposPersonalizados.isEmpty()) {
        Text(
            "Añade datos extra como tarjetas, redes Wi-Fi, cuentas bancarias o preguntas de seguridad.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(8.dp))
    } else {
        camposPersonalizados.forEachIndexed { indice, campo ->
            TarjetaCampoPersonalizadoEdicion(
                numero = indice + 1,
                campo = campo,
                alModificar = { modificado ->
                    alCambiarCampos(camposPersonalizados.toMutableList().apply {
                        set(indice, modificado)
                    })
                },
                alEliminar = {
                    haptica.tic()
                    alCambiarCampos(camposPersonalizados.toMutableList().apply {
                        removeAt(indice)
                    })
                }
            )
            Spacer(Modifier.height(10.dp))
        }
    }

    // Botones de acción: Añadir campo manual y Abrir plantillas
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonBorde(
            texto = "+ Añadir campo",
            modifier = Modifier.weight(1f),
            alPulsar = {
                haptica.tic()
                alCambiarCampos(camposPersonalizados + CampoPersonalizado(
                    etiqueta = "",
                    valor = "",
                    tipo = TipoCampo.TEXTO
                ))
            }
        )
        BotonColorido(
            texto = "Plantillas",
            icono = Icons.Filled.Layers,
            color = ColorAcento,
            modifier = Modifier.weight(1f),
            alPulsar = {
                haptica.tic()
                mostrandoDialogoPlantillas = true
            }
        )
    }

    // Opción para guardar los campos actuales como una nueva plantilla
    if (camposPersonalizados.isNotEmpty() && alGuardarPlantillaNueva != null) {
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    haptica.tic()
                    mostrandoGuardarComoPlantilla = true
                }
            ) {
                Icon(
                    Icons.Filled.BookmarkAdd,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Guardar campos como plantilla reutilizable",
                    color = Ambar,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    if (mostrandoDialogoPlantillas) {
        DialogoPlantillasCampos(
            plantillasPersonalizadasRaw = plantillasPersonalizadasRaw,
            haptica = haptica,
            alSeleccionarPlantilla = { nuevos ->
                alCambiarCampos(camposPersonalizados + nuevos)
            },
            alGuardarPlantillaNueva = { alGuardarPlantillaNueva?.invoke(it) },
            alEliminarPlantilla = { alEliminarPlantilla?.invoke(it) },
            alCerrar = { mostrandoDialogoPlantillas = false }
        )
    }

    if (mostrandoGuardarComoPlantilla && alGuardarPlantillaNueva != null) {
        DialogoCrearPlantilla(
            camposIniciales = camposPersonalizados.map { CampoPlantilla(it.etiqueta.ifBlank { "Campo" }, it.tipo) },
            haptica = haptica,
            alGuardar = { nueva ->
                mostrandoGuardarComoPlantilla = false
                alGuardarPlantillaNueva(nueva)
            },
            alCerrar = { mostrandoGuardarComoPlantilla = false }
        )
    }
}
