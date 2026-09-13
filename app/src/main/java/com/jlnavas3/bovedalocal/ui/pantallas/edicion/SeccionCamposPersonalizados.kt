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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
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
fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit
) {
    var mostrarValor by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo


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
        // Cabecera del campo: Número, Badge de Tipo, Sensible y Eliminar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "#$numero",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )

                // Chip de tipo
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(SuperficieAlta)
                        .then(
                            if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                            else Modifier
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = campo.tipo.etiqueta,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                }

                // Chip de dato sensible
                if (esSensible) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(ColorAcento.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            Icons.Filled.Security,
                            contentDescription = "Sensible",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Sensible",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorIconosInternos
                        )
                    }
                }
            }

            IconButton(onClick = alEliminar, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar campo",
                    tint = Peligro,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Etiqueta del campo
        CampoPepo(
            valor = campo.etiqueta,
            etiqueta = "Nombre del campo",
            alCambiar = { alModificar(campo.copy(etiqueta = it)) }
        )

        Spacer(Modifier.height(8.dp))

        // Valor del campo
        if (campo.tipo == TipoCampo.NOTAS) {
            OutlinedTextField(
                value = campo.valor,
                onValueChange = { alModificar(campo.copy(valor = it)) },
                label = { Text("Notas / Contenido", color = TextoSecundario) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = FormaBoton,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorTitulos,
                    unfocusedBorderColor = ColorBordeActual,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal,
                    focusedContainerColor = SuperficieAlta,
                    unfocusedContainerColor = SuperficieAlta
                ),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = if (esSensible && !mostrarValor) FontFamily.Monospace else FontFamily.Default
                ),
                visualTransformation = if (esSensible && !mostrarValor) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = if (esSensible) {
                    {
                        IconButton(onClick = { mostrarValor = !mostrarValor }) {
                            Icon(
                                imageVector = if (mostrarValor) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (mostrarValor) "Ocultar" else "Mostrar",
                                tint = ColorIconosInternos
                            )
                        }
                    }
                } else null
            )
        } else {
            CampoPepo(
                valor = campo.valor,
                etiqueta = if (campo.tipo == TipoCampo.PIN) "Valor del PIN" else "Valor del campo",
                alCambiar = { alModificar(campo.copy(valor = it)) },
                esContrasena = esSensible,
                mostrarContrasena = mostrarValor,
                alAlternarMostrarContrasena = if (esSensible) { { mostrarValor = !mostrarValor } } else null,
                tecladoNumerico = campo.tipo == TipoCampo.NUMERO || campo.tipo == TipoCampo.PIN
            )
        }

        // Toggle rápido de sensibilidad
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .clip(FormaPequena)
                .clickable { alModificar(campo.copy(esSensible = !campo.esSensible)) }
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = null,
                tint = if (campo.esSensible) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (campo.esSensible) "Marcado como secreto (toca para desmarcar)" else "Marcar como secreto / sensible",
                style = MaterialTheme.typography.labelSmall,
                color = if (campo.esSensible) ColorIconosInternos else TextoSecundario
            )
        }
    }
}

@Composable
fun SeccionCamposPersonalizados(
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    etiquetasBase: Set<String> = emptySet(),
    haptica: Haptica
) {
    var mostrandoDialogoNuevoCampo by remember { mutableStateOf(false) }
    var mostrandoDialogoPresets by remember { mutableStateOf(false) }

    val camposVisibles = remember(camposPersonalizados, etiquetasBase) {
        if (etiquetasBase.isEmpty()) camposPersonalizados
        else camposPersonalizados.filterNot { campo ->
            etiquetasBase.any { base -> base.equals(campo.etiqueta, ignoreCase = true) }
        }
    }

    EtiquetaSeccion(if (etiquetasBase.isEmpty()) "Campos personalizados" else "Campos adicionales")
    Spacer(Modifier.height(8.dp))

    if (camposVisibles.isNotEmpty()) {
        camposVisibles.forEachIndexed { indice, campo ->
            TarjetaCampoPersonalizadoEdicion(
                numero = indice + 1,
                campo = campo,
                alModificar = { modificado ->
                    val actualizados = camposPersonalizados.toMutableList()
                    val idx = actualizados.indexOfFirst { it.id == campo.id }
                    if (idx >= 0) {
                        actualizados[idx] = modificado
                    }
                    alCambiarCampos(actualizados)
                },
                alEliminar = {
                    haptica.tic()
                    alCambiarCampos(camposPersonalizados.filterNot { it.id == campo.id })
                }
            )
            Spacer(Modifier.height(10.dp))
        }
    }

    if (etiquetasBase.isEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonBorde(
                texto = "+ Campo",
                icono = Icons.Filled.Add,
                modifier = Modifier.weight(1f),
                alPulsar = {
                    haptica.tic()
                    mostrandoDialogoNuevoCampo = true
                }
            )
            BotonColorido(
                texto = "Presets",
                icono = Icons.Filled.DynamicForm,
                color = ColorAcento,
                modifier = Modifier.weight(1f),
                alPulsar = {
                    haptica.tic()
                    mostrandoDialogoPresets = true
                }
            )
        }
    } else {
        BotonBorde(
            texto = "+ Añadir campo adicional",
            icono = Icons.Filled.Add,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = {
                haptica.tic()
                mostrandoDialogoNuevoCampo = true
            }
        )
    }

    if (mostrandoDialogoNuevoCampo) {
        DialogoNuevoCampo(
            alDescartar = { mostrandoDialogoNuevoCampo = false },
            alCrearCampo = { nuevoCampo ->
                alCambiarCampos(camposPersonalizados + nuevoCampo)
            }
        )
    }

    if (mostrandoDialogoPresets) {
        DialogoPresetsRapidos(
            alDescartar = { mostrandoDialogoPresets = false },
            alSeleccionarPreset = { camposPreset ->
                alCambiarCampos(camposPersonalizados + camposPreset)
            }
        )
    }
}
