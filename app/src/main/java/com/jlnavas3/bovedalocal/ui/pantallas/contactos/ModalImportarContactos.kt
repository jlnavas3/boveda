package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.data.contactos.ConversorContactoEntrada
import com.jlnavas3.bovedalocal.data.contactos.LectorContactosAndroid
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente modal que orquesta la lectura, visualización interactiva y selección
 * masiva de contactos del teléfono para transformarlos en entradas seguras de la bóveda.
 */
@Composable
fun ModalImportarContactos(
    abierto: Boolean,
    alCerrar: () -> Unit,
    alImportar: (List<Entrada>) -> Unit
) {
    if (!abierto) return

    val contexto = LocalContext.current
    var cargando by remember { mutableStateOf(true) }
    var todosLosContactos by remember { mutableStateOf<List<ContactoDispositivo>>(emptyList()) }
    var consultaBusqueda by remember { mutableStateOf("") }
    var seleccionadosIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(Unit) {
        cargando = true
        todosLosContactos = LectorContactosAndroid.leerContactos(contexto)
        cargando = false
    }

    val contactosFiltrados = remember(todosLosContactos, consultaBusqueda) {
        if (consultaBusqueda.isBlank()) {
            todosLosContactos
        } else {
            val q = consultaBusqueda.trim().lowercase()
            todosLosContactos.filter { c ->
                c.nombre.lowercase().contains(q) ||
                c.telefonos.any { it.contains(q) } ||
                c.correos.any { it.lowercase().contains(q) }
            }
        }
    }

    val todosSeleccionados = contactosFiltrados.isNotEmpty() &&
        contactosFiltrados.all { seleccionadosIds.contains(it.id) }

    ModalInferiorBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        fijarAbajo = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            CabeceraImportarContactos(
                totalContactos = contactosFiltrados.size,
                seleccionadosCount = seleccionadosIds.size,
                todosSeleccionados = todosSeleccionados,
                alAlternarTodos = {
                    seleccionadosIds = if (todosSeleccionados) {
                        seleccionadosIds - contactosFiltrados.map { it.id }.toSet()
                    } else {
                        seleccionadosIds + contactosFiltrados.map { it.id }
                    }
                },
                alCerrar = alCerrar
            )

            BarraBusquedaContactos(
                consulta = consultaBusqueda,
                alCambiarConsulta = { consultaBusqueda = it }
            )

            when {
                cargando -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ColorAcento)
                    }
                }
                contactosFiltrados.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (todosLosContactos.isEmpty()) {
                                "No se encontraron contactos en el dispositivo o no se concedió el permiso de lectura."
                            } else {
                                "No hay contactos que coincidan con la búsqueda."
                            },
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(contactosFiltrados, key = { it.id }) { contacto ->
                            val estaSeleccionado = seleccionadosIds.contains(contacto.id)
                            FilaContactoImportacion(
                                contacto = contacto,
                                seleccionado = estaSeleccionado,
                                alAlternarSeleccion = {
                                    seleccionadosIds = if (estaSeleccionado) {
                                        seleccionadosIds - contacto.id
                                    } else {
                                        seleccionadosIds + contacto.id
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (seleccionadosIds.isNotEmpty()) {
                BotonConfirmarImportacionContactos(
                    cantidadSeleccionados = seleccionadosIds.size,
                    alConfirmar = {
                        val contactosAImportar = todosLosContactos.filter { seleccionadosIds.contains(it.id) }
                        val entradasGeneradas = contactosAImportar.map { ConversorContactoEntrada.convertir(it) }
                        alImportar(entradasGeneradas)
                        alCerrar()
                    }
                )
            } else {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
