package com.jlnavas3.bovedalocal.ui.pantallas.categorias

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoAsignarCategorias(
    entradasSeleccionadas: List<Entrada>,
    categoriasDisponibles: List<Categoria>,
    alCrearNuevaCategoria: () -> Unit,
    alGuardar: (idsAgregar: Set<String>, idsQuitar: Set<String>) -> Unit,
    alDescartar: () -> Unit
) {
    val categoriasIniciales = remember(entradasSeleccionadas) {
        val mapa = mutableSetOf<String>()
        entradasSeleccionadas.forEach { entrada ->
            mapa.addAll(entrada.categorias)
        }
        mapa
    }

    var seleccionadas by remember { mutableStateOf(categoriasIniciales.toSet()) }

    val cant = entradasSeleccionadas.size
    val titulo = if (cant == 1) "Asignar Categorías" else "Asignar Categorías ($cant)"

    DialogoBoveda(
        alCerrar = alDescartar,
        titulo = titulo,
        icono = Icons.Filled.Folder,
        colorIcono = ColorAcento,
        botonConfirmar = {
            BotonTextoBoveda(
                texto = "Guardar",
                tipo = TipoBotonTexto.PRIMARIO,
                alPulsar = {
                    val idsAgregar = seleccionadas - categoriasIniciales
                    val idsQuitar = categoriasIniciales - seleccionadas
                    alGuardar(idsAgregar, idsQuitar)
                }
            )
        },
        botonDescartar = {
            BotonTextoBoveda(
                texto = "Cancelar",
                tipo = TipoBotonTexto.SECUNDARIO,
                alPulsar = alDescartar
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (categoriasDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextoCuerpo(
                        texto = "Aún no tienes categorías creadas.",
                        color = TextoSecundario
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(categoriasDisponibles, key = { it.id }) { cat ->
                        val estaMarcada = seleccionadas.contains(cat.id)
                        val colorPropio = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                        val icono = IconosCategorias.obtenerIcono(cat.icono)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FormaPequena)
                                .background(if (estaMarcada) ColorCampoAjustes else Color.Transparent)
                                .clickable {
                                    seleccionadas = if (estaMarcada) {
                                        seleccionadas - cat.id
                                    } else {
                                        seleccionadas + cat.id
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckboxBoveda(
                                checked = estaMarcada,
                                onCheckedChange = { chk ->
                                    seleccionadas = if (chk) {
                                        seleccionadas + cat.id
                                    } else {
                                        seleccionadas - cat.id
                                    }
                                },
                                colorAcento = ColorAcento
                            )
                            Spacer(Modifier.width(10.dp))
                            Icon(
                                imageVector = icono,
                                contentDescription = null,
                                tint = colorPropio,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            TextoCuerpo(
                                texto = cat.nombre,
                                color = TextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Botón "+ Crear nueva categoría"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaPequena)
                    .background(ColorCampoAjustes)
                    .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
                    .clickable { alCrearNuevaCategoria() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                TextoCuerpo(
                    texto = "Crear nueva categoría",
                    color = ColorAcento
                )
            }
        }
    }
}
