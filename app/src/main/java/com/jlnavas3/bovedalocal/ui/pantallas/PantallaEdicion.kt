package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.BotonGuardarEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.ModalesEdicion
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

@Composable
fun PantallaEdicion(vm: VaultViewModel, id: String?, contrasenaInicial: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val original = remember(id) { id?.let { vm.entrada(it) } }

    var tipo by remember { mutableStateOf(original?.tipo ?: TipoEntrada.LOGIN) }
    var titulo by remember { mutableStateOf(original?.titulo ?: "") }
    var usuario by remember { mutableStateOf(original?.usuario ?: "") }
    var contrasena by remember { mutableStateOf(original?.contrasena ?: contrasenaInicial) }
    var mostrarContrasena by remember { mutableStateOf(false) }
    val listaEnlaces = remember(original) {
        val items = original?.urls?.map { LanzadorEnlaces.desglosarParaEdicion(it) } ?: emptyList()
        mutableStateListOf<EnlaceEditable>().apply {
            if (items.isNotEmpty()) addAll(items)
        }
    }
    var mostrarSelectorApp by remember { mutableStateOf(false) }
    var indiceEnlaceSeleccionado by remember { mutableStateOf<Int?>(null) }
    var listaAppsInstaladas by remember { mutableStateOf<List<AppInstalada>>(emptyList()) }

    LaunchedEffect(Unit) {
        listaAppsInstaladas = GestorAppsInstaladas.obtenerAppsInstaladas(contexto)
    }
    var notas by remember { mutableStateOf(original?.notas ?: "") }
    var totp by remember { mutableStateOf(original?.secretoTotp ?: "") }
    var mostrarSecretoTotp by remember { mutableStateOf(false) }
    var favorito by remember { mutableStateOf(original?.favorito ?: false) }
    var ignoradaEnSalud by remember { mutableStateOf(original?.ignoradaEnSalud ?: false) }
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()
    val coleccionesDisponibles = remember(estadoBoveda) {
        (estadoBoveda as? EstadoBoveda.Desbloqueada)?.colecciones ?: emptyList()
    }
    var colecciones by remember { mutableStateOf(original?.colecciones ?: emptyList()) }
    var mostrarDialogoNuevaColeccion by remember { mutableStateOf(false) }

    var etiquetas by remember { mutableStateOf(original?.etiquetas ?: emptyList()) }
    var camposPersonalizados by remember { mutableStateOf(original?.camposPersonalizados ?: emptyList()) }
    var opcionesGenerador by remember { mutableStateOf(OpcionesGenerador()) }
    val etiquetasSugeridas = remember { vm.etiquetasUsadas() }

    val totpValido = totp.isBlank() || Base32.esValido(totp)
    val puedeGuardar = titulo.isNotBlank() && totpValido
    val scrollState = rememberScrollState()

    fun guardarEntrada() {
        if (!puedeGuardar) return
        haptica.exito()
        val urlsGuardadas = listaEnlaces.flatMap { enlace ->
            val reconstruido = LanzadorEnlaces.reconstruirDesdeEdicion(enlace)
            if (enlace.hashOriginal != null) {
                listOf(reconstruido)
            } else {
                reconstruido.split(",", "\n", ";").map { it.trim() }
            }
        }.filter { it.isNotBlank() }

        val entrada = Entrada(
            id = original?.id ?: vm.nuevoId(),
            tipo = original?.passkey?.let { TipoEntrada.PASSKEY } ?: tipo,
            titulo = titulo.trim(),
            usuario = usuario.trim(),
            contrasena = contrasena,
            urls = urlsGuardadas,
            notas = notas,
            secretoTotp = totp.trim().ifBlank { null },
            favorito = favorito,
            creadaEn = original?.creadaEn ?: 0L,
            etiquetas = etiquetas.map(::normalizarEtiqueta).filter { it.isNotEmpty() }.distinct(),
            historialContrasenas = original?.historialContrasenas ?: emptyList(),
            passkey = original?.passkey,
            camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() },
            ignoradaEnSalud = ignoradaEnSalud,
            colecciones = colecciones
        )
        vm.guardar(entrada)
        vm.volverAtras()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BarraSuperiorPantalla(
                titulo = if (original == null) "Nueva entrada" else "Editar entrada",
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {}
            )

            FormularioEdicionEntrada(
                scrollState = scrollState,
                original = original,
                tipo = tipo,
                alCambiarTipo = { tipo = it },
                titulo = titulo,
                alCambiarTitulo = { titulo = it },
                usuario = usuario,
                alCambiarUsuario = { usuario = it },
                contrasena = contrasena,
                alCambiarContrasena = { contrasena = it },
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                opcionesGenerador = opcionesGenerador,
                alCambiarOpcionesGenerador = { opcionesGenerador = it },
                camposPersonalizados = camposPersonalizados,
                alCambiarCamposPersonalizados = { camposPersonalizados = it },
                listaEnlaces = listaEnlaces,
                alSolicitarExplorarApp = { idx ->
                    indiceEnlaceSeleccionado = idx
                    mostrarSelectorApp = true
                },
                totp = totp,
                alCambiarTotp = { totp = it },
                mostrarSecretoTotp = mostrarSecretoTotp,
                alAlternarMostrarSecretoTotp = {
                    haptica.tic()
                    mostrarSecretoTotp = !mostrarSecretoTotp
                },
                totpValido = totpValido,
                notas = notas,
                alCambiarNotas = { notas = it },
                coleccionesDisponibles = coleccionesDisponibles,
                colecciones = colecciones,
                alCambiarColecciones = { colecciones = it },
                alCrearNuevaColeccion = { mostrarDialogoNuevaColeccion = true },
                etiquetas = etiquetas,
                alCambiarEtiquetas = { etiquetas = it },
                etiquetasSugeridas = etiquetasSugeridas,
                favorito = favorito,
                alAlternarFavorito = { favorito = it; haptica.tic() },
                ignoradaEnSalud = ignoradaEnSalud,
                alAlternarIgnoradaEnSalud = { ignoradaEnSalud = it; haptica.tic() },
                ajustes = ajustes,
                haptica = haptica,
                modifier = Modifier.weight(1f)
            )
        }

        BotonGuardarEdicion(
            alGuardar = {
                if (puedeGuardar) {
                    guardarEntrada()
                } else {
                    haptica.error()
                    vm.avisar("Introduce un título para guardar")
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

    ModalesEdicion(
        mostrarSelectorApp = mostrarSelectorApp,
        mostrarDialogoNuevaColeccion = mostrarDialogoNuevaColeccion,
        alDescartarSelectorApp = { mostrarSelectorApp = false },
        alSeleccionarApp = { paquete ->
            val nombreApp = LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
            if (titulo.isBlank() || titulo.trim() == "Nueva entrada") {
                if (!nombreApp.isNullOrBlank()) {
                    titulo = nombreApp
                }
            }
            val urlApp = if (paquete.startsWith("android://")) paquete else "android://$paquete"
            val idx = indiceEnlaceSeleccionado
            if (idx != null && idx in listaEnlaces.indices) {
                listaEnlaces[idx] = listaEnlaces[idx].copy(valor = urlApp)
            } else {
                if (listaEnlaces.size == 1 && listaEnlaces[0].valor.isBlank()) {
                    listaEnlaces[0] = EnlaceEditable(valor = urlApp)
                } else {
                    listaEnlaces.add(EnlaceEditable(valor = urlApp))
                }
            }
            mostrarSelectorApp = false
        },
        alDescartarNuevaColeccion = { mostrarDialogoNuevaColeccion = false },
        alGuardarNuevaColeccion = { nombreCol, iconoCol, colorHexCol ->
            val nueva = vm.crearColeccion(nombreCol, iconoCol, colorHexCol)
            colecciones = colecciones + nueva.id
            mostrarDialogoNuevaColeccion = false
        }
    )
}
