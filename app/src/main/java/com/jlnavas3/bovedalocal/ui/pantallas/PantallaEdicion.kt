/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.MigradorEntradasNativas
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.ModalesEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.herramientas.PantallaDiccionarioPalabras
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.PantallaContrasenasComunes
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

@Composable
fun PantallaEdicion(
    vm: VaultViewModel,
    id: String?,
    contrasenaInicial: String = "",
    tituloInicial: String = "",
    urlInicial: String = "",
    tipoInicial: TipoEntrada? = null,
    telefonoInicial: String = ""
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val original = remember(id) { id?.let { vm.entrada(it) } }

    var tipo by remember { mutableStateOf(original?.tipo ?: (tipoInicial ?: TipoEntrada.LOGIN)) }
    var titulo by remember { mutableStateOf(original?.titulo ?: tituloInicial) }
    var usuario by remember {
        mutableStateOf(
            original?.usuario
                ?: if (tipoInicial == TipoEntrada.CONTACTO && telefonoInicial.isNotBlank()) {
                    com.jlnavas3.bovedalocal.data.contactos.FormateadorTelefonoInternacional.formatear(telefonoInicial)
                } else ""
        )
    }
    var contrasena by remember { mutableStateOf(original?.contrasena ?: contrasenaInicial) }
    val ocultarContrasenaPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarContrasena
    var mostrarContrasena by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarContrasenaPorDefecto) }
    val listaEnlaces = remember(original) {
        val items = original?.urls?.map { LanzadorEnlaces.desglosarParaEdicion(it) }
            ?: if (urlInicial.isNotBlank()) listOf(LanzadorEnlaces.desglosarParaEdicion(urlInicial)) else emptyList()
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
    var mostrarSecretoTotp by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarContrasenaPorDefecto) }
    var favorito by remember { mutableStateOf(original?.favorito ?: false) }
    var anclado by remember { mutableStateOf(original?.anclado ?: false) }
    var fechaCaducidad by remember { mutableStateOf<Long?>(original?.fechaCaducidad) }
    var ignoradaEnSalud by remember { mutableStateOf(original?.ignoradaEnSalud ?: false) }
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()
    val categoriasDisponibles = remember(estadoBoveda) {
        (estadoBoveda as? EstadoBoveda.Desbloqueada)?.categorias ?: emptyList()
    }
    val identidadesDisponibles = remember(estadoBoveda) {
        (estadoBoveda as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList()
    }
    val identidadFiltroId by vm.identidadSeleccionadaId.collectAsStateWithLifecycle()
    var identidadId by remember {
        mutableStateOf(
            original?.identidadId ?: identidadFiltroId?.takeIf { it != "__SIN_IDENTIDAD__" }
        )
    }

    LaunchedEffect(identidadId) {
        if (original == null && usuario.isBlank() && identidadId != null) {
            val iden = identidadesDisponibles.firstOrNull { it.id == identidadId }
            if (iden != null) {
                usuario = iden.identificadorPreferido
            }
        }
    }

    var categorias by remember { mutableStateOf(original?.categorias ?: emptyList()) }
    var mostrarDialogoNuevaCategoria by remember { mutableStateOf(false) }

    val entradaMigradaInicial = remember(original) {
        original?.let { MigradorEntradasNativas.migrarEntrada(it) }
    }
    var datosTarjeta by remember { mutableStateOf(entradaMigradaInicial?.datosTarjeta ?: original?.datosTarjeta ?: DatosTarjetaBancaria()) }
    var datosWifi by remember { mutableStateOf(entradaMigradaInicial?.datosWifi ?: original?.datosWifi ?: DatosRedWifi()) }
    var datosCuentaBancaria by remember { mutableStateOf(entradaMigradaInicial?.datosCuentaBancaria ?: original?.datosCuentaBancaria ?: DatosCuentaBancaria()) }
    var datosIdentidad by remember { mutableStateOf(entradaMigradaInicial?.datosIdentidad ?: original?.datosIdentidad ?: DatosDocumentoIdentidad()) }
    var datosServidor by remember { mutableStateOf(entradaMigradaInicial?.datosServidor ?: original?.datosServidor ?: DatosServidorSsh()) }
    var datosWallet by remember { mutableStateOf(entradaMigradaInicial?.datosWallet ?: original?.datosWallet ?: DatosCriptoWallet()) }
    var datosContacto by remember { mutableStateOf(entradaMigradaInicial?.datosContacto ?: original?.datosContacto ?: com.jlnavas3.bovedalocal.data.DatosContacto()) }

    var etiquetas by remember { mutableStateOf(original?.etiquetas ?: emptyList()) }
    var camposPersonalizados by remember {
        mutableStateOf(entradaMigradaInicial?.camposPersonalizados ?: original?.camposPersonalizados ?: emptyList())
    }
    var adjuntos by remember { mutableStateOf(original?.adjuntos ?: emptyList()) }
    var opcionesGenerador by remember { mutableStateOf(OpcionesGenerador()) }
    var mostrarDiccionarioModal by remember { mutableStateOf(false) }
    var mostrarComunesModal by remember { mutableStateOf(false) }
    var contactoParaPurga by remember { mutableStateOf<com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo?>(null) }
    val etiquetasSugeridas = remember { vm.etiquetasUsadas() }

    val totpValido = totp.isBlank() || Base32.esValido(totp)
    val puedeGuardar = titulo.isNotBlank() && totpValido
    val scrollState = rememberScrollState()

    fun guardarEntrada() {
        if (!puedeGuardar) return
        haptica.exito()
        val urlsGuardadas = com.jlnavas3.bovedalocal.util.DeduplicadorUrls.deduplicar(
            listaEnlaces.flatMap { enlace ->
                val reconstruido = LanzadorEnlaces.reconstruirDesdeEdicion(enlace)
                if (enlace.hashOriginal != null) {
                    listOf(reconstruido)
                } else {
                    reconstruido.split(",", "\n", ";").map { it.trim() }
                }
            }.filter { it.isNotBlank() }
        )

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
            anclado = anclado,
            creadaEn = original?.creadaEn ?: 0L,
            etiquetas = etiquetas.map(::normalizarEtiqueta).filter { it.isNotEmpty() }.distinct(),
            historialContrasenas = original?.historialContrasenas ?: emptyList(),
            passkey = original?.passkey,
            camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() },
            datosTarjeta = if (tipo == TipoEntrada.TARJETA) datosTarjeta else null,
            datosWifi = if (tipo == TipoEntrada.WIFI) datosWifi else null,
            datosCuentaBancaria = if (tipo == TipoEntrada.CUENTA_BANCARIA) datosCuentaBancaria else null,
            datosIdentidad = if (tipo == TipoEntrada.IDENTIDAD) datosIdentidad else null,
            datosServidor = if (tipo == TipoEntrada.SERVIDOR) datosServidor else null,
            datosWallet = if (tipo == TipoEntrada.WALLET) datosWallet else null,
            datosContacto = if (tipo == TipoEntrada.CONTACTO) datosContacto else null,
            ignoradaEnSalud = ignoradaEnSalud,
            categorias = categorias,
            perfilId = identidadId,
            fechaCaducidad = fechaCaducidad,
            adjuntos = adjuntos,
            tituloPersonalizado = true
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
                acciones = {
                    BotonIconoCabecera(
                        onClick = {
                            if (puedeGuardar) {
                                guardarEntrada()
                            } else {
                                haptica.error()
                                vm.avisar("Introduce un título para guardar")
                            }
                        },
                        icono = Icons.Filled.Check,
                        descripcion = "Guardar",
                        colorFondo = if (puedeGuardar) ColorAcento else Color.Transparent,
                        tint = if (puedeGuardar) ColorSobreAcento else ColorIconosInternos
                    )
                }
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
                datosTarjeta = datosTarjeta,
                alCambiarDatosTarjeta = { datosTarjeta = it },
                datosWifi = datosWifi,
                alCambiarDatosWifi = { datosWifi = it },
                datosCuentaBancaria = datosCuentaBancaria,
                alCambiarDatosCuentaBancaria = { datosCuentaBancaria = it },
                datosIdentidad = datosIdentidad,
                alCambiarDatosIdentidad = { datosIdentidad = it },
                datosServidor = datosServidor,
                alCambiarDatosServidor = { datosServidor = it },
                datosWallet = datosWallet,
                alCambiarDatosWallet = { datosWallet = it },
                datosContacto = datosContacto,
                alCambiarDatosContacto = { datosContacto = it },
                alImportarContacto = { c ->
                    haptica.exito()
                    val convertida = com.jlnavas3.bovedalocal.data.contactos.ConversorContactoEntrada.convertir(c)
                    if (convertida.titulo.isNotBlank()) {
                        titulo = convertida.titulo
                    }
                    if (convertida.usuario.isNotBlank()) {
                        usuario = convertida.usuario
                    }
                    val datosCont = convertida.datosContacto ?: com.jlnavas3.bovedalocal.data.DatosContacto()
                    val telSec = datosCont.telefonoSecundario
                    val telSecLimpio = if (com.jlnavas3.bovedalocal.data.contactos.FormateadorTelefonoInternacional.sonNumerosEquivalentes(convertida.usuario, telSec)) {
                        ""
                    } else {
                        telSec
                    }
                    datosContacto = datosCont.copy(telefonoSecundario = telSecLimpio)
                    camposPersonalizados = (camposPersonalizados + convertida.camposPersonalizados).distinctBy { it.etiqueta }
                    if (convertida.notas.isNotBlank()) {
                        notas = if (notas.isBlank()) convertida.notas else "$notas\n${convertida.notas}"
                    }
                    if (convertida.urls.isNotEmpty()) {
                        listaEnlaces.addAll(convertida.urls.map { LanzadorEnlaces.desglosarParaEdicion(it) })
                    }
                },
                adjuntos = adjuntos,
                alCambiarAdjuntos = { adjuntos = it },
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
                fechaCaducidad = fechaCaducidad,
                alCambiarFechaCaducidad = { fechaCaducidad = it },
                notas = notas,
                alCambiarNotas = { notas = it },
                categoriasDisponibles = categoriasDisponibles,
                categorias = categorias,
                alCambiarCategorias = { categorias = it },
                alCrearNuevaCategoria = { mostrarDialogoNuevaCategoria = true },
                identidadesDisponibles = identidadesDisponibles,
                identidadSeleccionadaId = identidadId,
                alSeleccionarIdentidad = { iden ->
                    identidadId = iden?.id
                    if (iden != null && usuario.isBlank()) {
                        usuario = iden.identificadorPreferido
                    }
                },
                etiquetas = etiquetas,
                alCambiarEtiquetas = { etiquetas = it },
                etiquetasSugeridas = etiquetasSugeridas,
                favorito = favorito,
                alAlternarFavorito = { favorito = it; haptica.tic() },
                anclado = anclado,
                alAlternarAnclado = { anclado = it; haptica.tic() },
                ignoradaEnSalud = ignoradaEnSalud,
                alAlternarIgnoradaEnSalud = { ignoradaEnSalud = it; haptica.tic() },
                ajustes = ajustes,
                haptica = haptica,
                alGuardarPlantillaCampos = { vm.guardarPlantillaCampos(it) },
                alEliminarPlantillaCampos = { vm.eliminarPlantillaCampos(it) },
                alAbrirDiccionario = { mostrarDiccionarioModal = true },
                alAbrirContrasenasComunes = { mostrarComunesModal = true },
                modifier = Modifier.weight(1f)
            )
        }
    }

    ModalesEdicion(
        mostrarSelectorApp = mostrarSelectorApp,
        mostrarDialogoNuevaCategoria = mostrarDialogoNuevaCategoria,
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
        alDescartarNuevaCategoria = { mostrarDialogoNuevaCategoria = false },
        alGuardarNuevaCategoria = { nombreCat: String, iconoCat: String, colorHexCat: String? ->
            val nueva = vm.crearCategoria(nombreCat, iconoCat, colorHexCat)
            categorias = categorias + nueva.id
            mostrarDialogoNuevaCategoria = false
        }
    )

    if (mostrarDiccionarioModal) {
        Box(modifier = Modifier.fillMaxSize()) {
            PantallaDiccionarioPalabras(
                vm = vm,
                idiomaInicial = opcionesGenerador.idiomaFrase,
                alVolverManual = { mostrarDiccionarioModal = false }
            )
        }
    }

    if (mostrarComunesModal) {
        Box(modifier = Modifier.fillMaxSize()) {
            PantallaContrasenasComunes(
                vm = vm,
                alVolverManual = { mostrarComunesModal = false }
            )
        }
    }

    contactoParaPurga?.let { c ->
        com.jlnavas3.bovedalocal.ui.pantallas.contactos.DialogoConfirmarPurgaAgenda(
            abierto = true,
            alCerrar = { contactoParaPurga = null },
            nombreContacto = c.nombre,
            alAbrirFichaSistema = {
                com.jlnavas3.bovedalocal.util.comunicacion.LanzadorPurgaContactoAgenda.abrirFichaParaEliminar(contexto, c.id)
            }
        )
    }
}
