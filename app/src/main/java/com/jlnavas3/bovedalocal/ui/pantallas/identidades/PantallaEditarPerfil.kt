/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.Perfil
import com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorPerfil
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlHacerScroll
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.cerrarTecladoAlTocarFuera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.scroll.rememberLazyListStatePersistente
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.UUID

/**
 * Pantalla dedicada completa para la creación y edición exhaustiva de un [Perfil]
 * con soporte para asociación múltiple de documentos, tarjetas bancarias, cuentas y contactos
 * para autocompletado en compras y formularios web.
 */
@Composable
fun PantallaEditarPerfil(
    vm: VaultViewModel,
    perfilId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val estado by vm.estado.collectAsStateWithLifecycle()
    val identidades = (estado as? EstadoBoveda.Desbloqueada)?.identidades ?: emptyList()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()

    val perfilActual = remember(identidades, perfilId) {
        if (perfilId != null) identidades.firstOrNull { it.id == perfilId } else null
    }

    val esEdicion = perfilId != null

    var nombre by remember(perfilActual) { mutableStateOf(perfilActual?.nombre ?: "") }
    var correoPrincipal by remember(perfilActual) { mutableStateOf(perfilActual?.correoPrincipal ?: "") }
    var correosSecundariosTexto by remember(perfilActual) {
        mutableStateOf(perfilActual?.correosSecundarios?.joinToString(", ") ?: "")
    }
    var preferenciaIdentificador by remember(perfilActual) {
        mutableStateOf(perfilActual?.preferenciaIdentificador ?: PreferenciaIdentificadorPerfil.CORREO)
    }
    var colorSeleccionadoHex by remember(perfilActual) {
        mutableStateOf(perfilActual?.colorHex ?: PaletaColoresIdentidad.first())
    }

    var documentosIds by remember(perfilActual) { mutableStateOf(perfilActual?.documentosIds ?: emptyList()) }
    var documentoPredeterminadoId by remember(perfilActual) { mutableStateOf(perfilActual?.documentoPredeterminadoId) }

    var tarjetasIds by remember(perfilActual) { mutableStateOf(perfilActual?.tarjetasIds ?: emptyList()) }
    var tarjetaPredeterminadaId by remember(perfilActual) { mutableStateOf(perfilActual?.tarjetaPredeterminadaId) }

    var cuentasIds by remember(perfilActual) { mutableStateOf(perfilActual?.cuentasIds ?: emptyList()) }
    var cuentaPredeterminadaId by remember(perfilActual) { mutableStateOf(perfilActual?.cuentaPredeterminadaId) }

    var contactosIds by remember(perfilActual) { mutableStateOf(perfilActual?.contactosIds ?: emptyList()) }
    var contactoPredeterminadoId by remember(perfilActual) { mutableStateOf(perfilActual?.contactoPredeterminadoId) }

    var sheetDocumentosAbierto by remember { mutableStateOf(false) }
    var sheetTarjetasAbierto by remember { mutableStateOf(false) }
    var sheetCuentasAbierto by remember { mutableStateOf(false) }
    var sheetContactosAbierto by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (!esEdicion) {
            focusRequester.requestFocus()
        }
    }

    val colorAcentoFinal = parsearColorO(colorSeleccionadoHex, ColorAcento)
    val correoValido = correoPrincipal.isBlank() || correoPrincipal.contains("@")
    val camposValidos = nombre.isNotBlank() && correoValido

    val listState = rememberLazyListStatePersistente("editar_perfil", vm.gestorScroll)
    CerrarTecladoAlHacerScroll(listState.isScrollInProgress)

    // Entradas disponibles en la bóveda por tipo
    val todosDocumentos = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.IDENTIDAD } }
    val todasTarjetas = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.TARJETA } }
    val todasCuentas = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.CUENTA_BANCARIA } }
    val todosContactos = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.CONTACTO } }

    // Entradas asociadas actualmente a este perfil
    val documentosAsociados = remember(todosDocumentos, documentosIds) {
        todosDocumentos.filter { documentosIds.contains(it.id) }
    }
    val tarjetasAsociadas = remember(todasTarjetas, tarjetasIds) {
        todasTarjetas.filter { tarjetasIds.contains(it.id) }
    }
    val cuentasAsociadas = remember(todasCuentas, cuentasIds) {
        todasCuentas.filter { cuentasIds.contains(it.id) }
    }
    val contactosAsociados = remember(todosContactos, contactosIds) {
        todosContactos.filter { contactosIds.contains(it.id) }
    }

    val docEfectivo = remember(documentosAsociados, documentoPredeterminadoId) {
        documentosAsociados.firstOrNull { it.id == documentoPredeterminadoId } ?: documentosAsociados.firstOrNull()
    }
    val tarjetaEfectiva = remember(tarjetasAsociadas, tarjetaPredeterminadaId) {
        tarjetasAsociadas.firstOrNull { it.id == tarjetaPredeterminadaId } ?: tarjetasAsociadas.firstOrNull()
    }
    val cuentaEfectiva = remember(cuentasAsociadas, cuentaPredeterminadaId) {
        cuentasAsociadas.firstOrNull { it.id == cuentaPredeterminadaId } ?: cuentasAsociadas.firstOrNull()
    }
    val contactoEfectivo = remember(contactosAsociados, contactoPredeterminadoId) {
        contactosAsociados.firstOrNull { it.id == contactoPredeterminadoId } ?: contactosAsociados.firstOrNull()
    }
    val siglaDoc = docEfectivo?.identidadEfectiva?.tipoDocumento?.ifBlank { null }
        ?: perfilActual?.tipoDocumento
        ?: "Documento"
    val numDoc = docEfectivo?.identidadEfectiva?.numero?.ifBlank { null }
        ?: docEfectivo?.usuario?.ifBlank { null }
        ?: perfilActual?.numeroDocumento.orEmpty()
    val tieneDocumentoAsociado = docEfectivo != null || !perfilActual?.numeroDocumento.isNullOrBlank()

    fun guardar() {
        if (!camposValidos) return
        haptica.tic()

        val secundarios = correosSecundariosTexto
            .split(",", "\n", ";")
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() && it.contains("@") }

        val numFinal = numDoc.ifBlank { perfilActual?.numeroDocumento.orEmpty() }
        val tipoFinal = docEfectivo?.identidadEfectiva?.tipoDocumento ?: perfilActual?.tipoDocumento

        val preferenciaFinal = when {
            correoPrincipal.isNotBlank() && numFinal.isNotBlank() -> preferenciaIdentificador
            numFinal.isNotBlank() -> PreferenciaIdentificadorPerfil.DOCUMENTO
            else -> PreferenciaIdentificadorPerfil.CORREO
        }

        val perfilAGuardar = Perfil(
            id = perfilActual?.id ?: UUID.randomUUID().toString(),
            nombre = nombre.trim(),
            correoPrincipal = correoPrincipal.trim().lowercase(),
            correosSecundarios = secundarios,
            tipoDocumento = tipoFinal,
            numeroDocumento = numFinal.ifBlank { null },
            preferenciaIdentificador = preferenciaFinal,
            colorHex = colorSeleccionadoHex,
            icono = "person",
            tarjetasIds = tarjetasIds,
            tarjetaPredeterminadaId = tarjetaPredeterminadaId ?: tarjetasIds.firstOrNull(),
            cuentasIds = cuentasIds,
            cuentaPredeterminadaId = cuentaPredeterminadaId ?: cuentasIds.firstOrNull(),
            contactosIds = contactosIds,
            contactoPredeterminadoId = contactoPredeterminadoId ?: contactosIds.firstOrNull(),
            documentosIds = documentosIds,
            documentoPredeterminadoId = documentoPredeterminadoId ?: documentosIds.firstOrNull(),
            creadaEn = perfilActual?.creadaEn ?: System.currentTimeMillis(),
            modificadaEn = System.currentTimeMillis()
        )

        vm.guardarPerfil(perfilAGuardar)
        vm.volverAtras()
    }

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = if (esEdicion) "Editar perfil" else "Nuevo perfil",
                idEtiqueta = "03-LST-IDE-EDT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = listState.firstVisibleItemScrollOffset > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonTextoBoveda(
                        texto = "Guardar",
                        tipo = TipoBotonTexto.PRIMARIO,
                        colorPersonalizado = colorAcentoFinal,
                        habilitado = camposValidos,
                        alPulsar = { guardar() }
                    )
                }
            )
        },
        containerColor = ColorAjustesFondo,
        modifier = Modifier.cerrarTecladoAlTocarFuera()
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .cerrarTecladoAlTocarFuera(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sección 1: Datos principales (Nombre y Color)
            item {
                SeccionDatosPrincipalesPerfil(
                    nombre = nombre,
                    alCambiarNombre = { nombre = it },
                    colorHex = colorSeleccionadoHex,
                    alCambiarColorHex = { colorSeleccionadoHex = it },
                    colorAcento = colorAcentoFinal,
                    focusRequester = focusRequester
                )
            }

            // Sección 2: Identificadores oficiales (Correo, Alias, Preferencia)
            item {
                SeccionIdentificadoresPerfil(
                    correoPrincipal = correoPrincipal,
                    alCambiarCorreoPrincipal = { correoPrincipal = it },
                    correosSecundariosTexto = correosSecundariosTexto,
                    alCambiarCorreosSecundariosTexto = { correosSecundariosTexto = it },
                    preferenciaIdentificador = preferenciaIdentificador,
                    alCambiarPreferenciaIdentificador = { preferenciaIdentificador = it },
                    tieneDocumento = tieneDocumentoAsociado,
                    siglaDocumento = siglaDoc,
                    colorAcento = colorAcentoFinal
                )
            }

            // Sección 3: Autocompletado y pagos unificados (Documentos, Tarjetas, Cuentas, Contactos)
            item {
                SeccionAutocompletadoUnificadaPerfil(
                    documentoPrincipal = docEfectivo,
                    cantidadDocumentos = documentosAsociados.size,
                    alAbrirDocumentos = { sheetDocumentosAbierto = true },
                    tarjetaPrincipal = tarjetaEfectiva,
                    cantidadTarjetas = tarjetasAsociadas.size,
                    alAbrirTarjetas = { sheetTarjetasAbierto = true },
                    cuentaPrincipal = cuentaEfectiva,
                    cantidadCuentas = cuentasAsociadas.size,
                    alAbrirCuentas = { sheetCuentasAbierto = true },
                    contactoPrincipal = contactoEfectivo,
                    cantidadContactos = contactosAsociados.size,
                    alAbrirContactos = { sheetContactosAbierto = true },
                    colorAcento = colorAcentoFinal
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    // Modal Bottom Sheet para asociar Documentos de Identidad
    BottomSheetVincularEntradaPerfil(
        visible = sheetDocumentosAbierto,
        titulo = "Asociar documento de identidad",
        icono = Icons.Filled.Badge,
        colorAcento = colorAcentoFinal,
        entradasDisponibles = todosDocumentos,
        idsSeleccionados = documentosIds,
        alCambiarSeleccion = { nuevas ->
            documentosIds = nuevas
            if (documentoPredeterminadoId !in nuevas) {
                documentoPredeterminadoId = nuevas.firstOrNull()
            }
        },
        alCrearNuevaEntrada = {
            vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.IDENTIDAD))
        },
        alCerrar = { sheetDocumentosAbierto = false }
    )

    // Modal Bottom Sheet para asociar Tarjetas
    BottomSheetVincularEntradaPerfil(
        visible = sheetTarjetasAbierto,
        titulo = "Asociar tarjetas bancarias",
        icono = Icons.Filled.CreditCard,
        colorAcento = colorAcentoFinal,
        entradasDisponibles = todasTarjetas,
        idsSeleccionados = tarjetasIds,
        alCambiarSeleccion = { nuevas ->
            tarjetasIds = nuevas
            if (tarjetaPredeterminadaId !in nuevas) {
                tarjetaPredeterminadaId = nuevas.firstOrNull()
            }
        },
        alCrearNuevaEntrada = {
            vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.TARJETA))
        },
        alCerrar = { sheetTarjetasAbierto = false }
    )

    // Modal Bottom Sheet para asociar Cuentas Bancarias
    BottomSheetVincularEntradaPerfil(
        visible = sheetCuentasAbierto,
        titulo = "Asociar cuentas bancarias",
        icono = Icons.Filled.AccountBalance,
        colorAcento = colorAcentoFinal,
        entradasDisponibles = todasCuentas,
        idsSeleccionados = cuentasIds,
        alCambiarSeleccion = { nuevas ->
            cuentasIds = nuevas
            if (cuentaPredeterminadaId !in nuevas) {
                cuentaPredeterminadaId = nuevas.firstOrNull()
            }
        },
        alCrearNuevaEntrada = {
            vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.CUENTA_BANCARIA))
        },
        alCerrar = { sheetCuentasAbierto = false }
    )

    // Modal Bottom Sheet para asociar Contactos / Direcciones
    BottomSheetVincularEntradaPerfil(
        visible = sheetContactosAbierto,
        titulo = "Asociar contacto / dirección",
        icono = Icons.Filled.LocationOn,
        colorAcento = colorAcentoFinal,
        entradasDisponibles = todosContactos,
        idsSeleccionados = contactosIds,
        alCambiarSeleccion = { nuevas ->
            contactosIds = nuevas
            if (contactoPredeterminadoId !in nuevas) {
                contactoPredeterminadoId = nuevas.firstOrNull()
            }
        },
        alCrearNuevaEntrada = {
            vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.CONTACTO))
        },
        alCerrar = { sheetContactosAbierto = false }
    )
}
