package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.registro.BarraSuperiorRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.CategoriaOpcion
import com.jlnavas3.bovedalocal.ui.pantallas.registro.CriterioOrdenRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.DialogoOrdenacionRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.EstadoVacioRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.EventoRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.ModalCategoriasRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.registro.TarjetaEventoRegistro
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@Composable
fun PantallaRegistro(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    var busquedaVisible by remember { mutableStateOf(false) }
    var filtroTexto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var criterioOrden by remember { mutableStateOf(CriterioOrdenRegistro.RECIENTES) }
    var mostrarModalCategorias by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    val categoriasOpciones = remember {
        listOf(
            CategoriaOpcion("Todos", "Todos los eventos del sistema", Icons.Filled.SelectAll, ColorAcento),
            CategoriaOpcion("Bóveda", "Apertura, cifrado, cambios de clave y entradas", Icons.Filled.Lock, Menta),
            CategoriaOpcion("Papelera", "Entradas eliminadas, restauradas y vaciado", Icons.Filled.Delete, ColorPapelera),
            CategoriaOpcion("Portapapeles", "Elementos copiados y vaciado automático", Icons.Filled.ContentCopy, Ambar),
            CategoriaOpcion("2FA", "Códigos TOTP, sincronización y doble factor", Icons.Filled.Password, Color2FA),
            CategoriaOpcion("Huella", "Autenticación biométrica y Keystore de Android", Icons.Filled.Fingerprint, ColorSeguridad),
            CategoriaOpcion("Cámara", "Escaneo de QR y motores de cámara", Icons.Filled.CameraAlt, ColorAcento),
            CategoriaOpcion("Autofill", "Autocompletado, Passkeys y Credential Manager", Icons.Filled.Description, ColorPasskeys),
            CategoriaOpcion("Errores", "Fallos, excepciones y anomalías", Icons.Filled.ErrorOutline, Peligro)
        )
    }

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }
    LaunchedEffect(refresco) {
        registro = Diagnostico.ultimas(Diagnostico.MAX_LINEAS_MEMORIA)
    }

    val eventosParseados = remember(registro) {
        registro.map { linea ->
            val timestamp = if (linea.length >= 14) linea.take(14) else ""
            val resto = if (linea.length > 15) linea.drop(15) else linea
            val area = if (resto.contains(':')) resto.substringBefore(':').trim() else "app"
            val mensaje = if (resto.contains(':')) resto.substringAfter(':').trim() else resto
            val esError = linea.contains("error", ignoreCase = true) ||
                linea.contains("fallo", ignoreCase = true) ||
                linea.contains("exception", ignoreCase = true) ||
                linea.contains("[NO]", ignoreCase = true)
            EventoRegistro(
                timestamp = timestamp,
                area = area,
                mensaje = mensaje,
                esError = esError,
                textoCompleto = linea
            )
        }
    }

    val eventosFiltrados = remember(eventosParseados, filtroTexto, categoriaSeleccionada) {
        eventosParseados.filter { ev ->
            val coincideCategoria = when (categoriaSeleccionada) {
                "Todos" -> true
                "Bóveda" -> ev.area.equals("bóveda", ignoreCase = true) ||
                    ev.area.equals("boveda", ignoreCase = true)
                "Papelera" -> ev.area.contains("papelera", ignoreCase = true) ||
                    ev.mensaje.contains("papelera", ignoreCase = true)
                "Portapapeles" -> ev.area.contains("portapapeles", ignoreCase = true) ||
                    ev.mensaje.contains("copiad", ignoreCase = true) ||
                    ev.mensaje.contains("portapapeles", ignoreCase = true)
                "2FA" -> ev.area.contains("2fa", ignoreCase = true) ||
                    ev.area.contains("totp", ignoreCase = true) ||
                    ev.mensaje.contains("2fa", ignoreCase = true) ||
                    ev.mensaje.contains("totp", ignoreCase = true) ||
                    ev.mensaje.contains("doble factor", ignoreCase = true)
                "Huella" -> ev.area.contains("huella", ignoreCase = true) ||
                    ev.area.contains("keystore", ignoreCase = true) ||
                    ev.mensaje.contains("biometr", ignoreCase = true)
                "Cámara" -> ev.area.contains("camara", ignoreCase = true) ||
                    ev.area.contains("cámara", ignoreCase = true) ||
                    ev.area.contains("qr", ignoreCase = true) ||
                    ev.mensaje.contains("motor", ignoreCase = true)
                "Autofill" -> ev.area.contains("autofill", ignoreCase = true) ||
                    ev.area.contains("credential", ignoreCase = true) ||
                    ev.area.contains("passkey", ignoreCase = true) ||
                    ev.mensaje.contains("relleno", ignoreCase = true) ||
                    ev.mensaje.contains("passkey", ignoreCase = true)
                "Errores" -> ev.esError
                else -> true
            }
            val coincideTexto = if (filtroTexto.isBlank()) true else {
                ev.textoCompleto.contains(filtroTexto, ignoreCase = true)
            }
            coincideCategoria && coincideTexto
        }
    }

    val eventosOrdenados = remember(eventosFiltrados, criterioOrden) {
        when (criterioOrden) {
            CriterioOrdenRegistro.RECIENTES -> eventosFiltrados.reversed()
            CriterioOrdenRegistro.ANTIGUOS -> eventosFiltrados
            CriterioOrdenRegistro.AREA_AZ -> eventosFiltrados.sortedWith(
                compareBy({ it.area.lowercase() }, { it.timestamp })
            )
            CriterioOrdenRegistro.AREA_ZA -> eventosFiltrados.sortedWith(
                compareByDescending<EventoRegistro> { it.area.lowercase() }.thenByDescending { it.timestamp }
            )
        }
    }

    fun textoRegistroFiltrado(): String = eventosOrdenados.joinToString("\n") { it.textoCompleto }

    val tieneFiltrosActivos = categoriaSeleccionada != "Todos" || criterioOrden != CriterioOrdenRegistro.RECIENTES || filtroTexto.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior moderna con acciones integradas
        BarraSuperiorRegistro(
            busquedaVisible = busquedaVisible,
            filtroTexto = filtroTexto,
            categoriaSeleccionada = categoriaSeleccionada,
            tieneFiltrosActivos = tieneFiltrosActivos,
            alVolver = { vm.volverAtras() },
            alAlternarBusqueda = {
                busquedaVisible = !busquedaVisible
                if (!busquedaVisible && filtroTexto.isNotBlank()) {
                    filtroTexto = ""
                }
            },
            alMostrarCategorias = { mostrarModalCategorias = true },
            alMostrarOrdenacion = { mostrarDialogoOrdenacion = true },
            alCopiarRegistro = {
                haptica.tic()
                Portapapeles.copiar(contexto, "Registro Bóveda local", textoRegistroFiltrado())
                vm.avisar("Registro copiado")
            },
            alCompartirRegistro = {
                haptica.tic()
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, "Registro Bóveda local")
                    .putExtra(Intent.EXTRA_TEXT, textoRegistroFiltrado())
                if (!AjustesSistema.abrir(contexto, Intent.createChooser(intent, "Compartir registro"))) {
                    vm.avisar("No hay ninguna app con la que compartirlo")
                }
            },
            alBorrarRegistro = {
                haptica.toque()
                Diagnostico.borrar()
                registro = emptyList()
                vm.avisar("Registro borrado")
            },
            alRestablecerFiltros = {
                haptica.tic()
                categoriaSeleccionada = "Todos"
                criterioOrden = CriterioOrdenRegistro.RECIENTES
                filtroTexto = ""
                busquedaVisible = false
            }
        )

        // Barra de búsqueda animada One UI
        AnimatedVisibility(
            visible = busquedaVisible || filtroTexto.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = filtroTexto,
                    alCambiar = { filtroTexto = it },
                    alCerrar = {
                        busquedaVisible = false
                        filtroTexto = ""
                    }
                )
            }
        }

        // Chip indicador de categoría activa
        if (categoriaSeleccionada != "Todos") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "Categoría: $categoriaSeleccionada",
                    alLimpiar = {
                        haptica.tic()
                        categoriaSeleccionada = "Todos"
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${eventosOrdenados.size} de ${registro.size} eventos",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = criterioOrden.etiqueta,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            // Lista de eventos que aprovecha todo el espacio vertical
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (eventosOrdenados.isEmpty()) {
                    EstadoVacioRegistro(estaVacio = registro.isEmpty())
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(eventosOrdenados) { ev ->
                            TarjetaEventoRegistro(ev = ev)
                        }
                    }
                }
            }
        }
    }

    // Modal de selección de categorías fijado abajo estilo One UI / MagicOS
    if (mostrarModalCategorias) {
        ModalCategoriasRegistro(
            categoriasOpciones = categoriasOpciones,
            categoriaSeleccionada = categoriaSeleccionada,
            alSeleccionarCategoria = { categoriaSeleccionada = it },
            alCerrar = { mostrarModalCategorias = false }
        )
    }

    // Diálogo de ordenación modal estilo One UI
    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionRegistro(
            criterioActual = criterioOrden,
            alSeleccionarCriterio = { criterioOrden = it },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }
}
