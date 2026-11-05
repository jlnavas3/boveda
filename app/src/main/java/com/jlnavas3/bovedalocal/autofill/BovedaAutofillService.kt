/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.PendingIntent
import android.content.Intent
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveInfo
import android.service.autofill.SaveRequest
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

import android.os.Build
import android.widget.inline.InlinePresentationSpec
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BovedaAutofillService : AutofillService() {

    companion object {
        const val EXTRA_USUARIO_ID = "boveda.usuario.id"
        const val EXTRA_CONTRASENA_ID = "boveda.contrasena.id"
        const val EXTRA_PAQUETE = "boveda.paquete"
        const val EXTRA_DOMINIO = "boveda.dominio"
        const val EXTRA_INLINE_REQUEST = "boveda.inline_request"
    }

    private val servicioScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onDestroy() {
        super.onDestroy()
        servicioScope.cancel()
    }

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        val job = servicioScope.launch {
            try {
                procesarFillRequest(request, callback)
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Diagnostico.apuntar("autofill", "Error procesando solicitud: ${e.message}")
                    callback.onFailure("Error al autocompletar")
                }
            }
        }
        cancellationSignal.setOnCancelListener {
            job.cancel()
        }
    }

    private fun procesarFillRequest(request: FillRequest, callback: FillCallback) {
        val contexto = request.fillContexts.lastOrNull()
        if (contexto == null) {
            callback.onSuccess(null)
            return
        }
        val estructura = contexto.structure
        val ajustes = AlmacenAjustes(this).ajustes.value
        val campos = AutofillUtiles.detectar(
            estructura = estructura,
            pistasUsuario = ajustes.autofillPistasUsuario,
            pistasContrasena = ajustes.autofillPistasContrasena,
            pistasOtp = ajustes.autofillPistasOtp,
            pistasDocumento = ajustes.autofillPistasDocumento,
            pistasTarjetaNumero = ajustes.autofillPistasTarjetaNumero,
            pistasTarjetaCvv = ajustes.autofillPistasTarjetaCvv,
            pistasTarjetaExpiracion = ajustes.autofillPistasTarjetaExpiracion,
            pistasTarjetaTitular = ajustes.autofillPistasTarjetaTitular,
            pistasDireccionCalle = ajustes.autofillPistasDireccionCalle,
            pistasDireccionCp = ajustes.autofillPistasDireccionCp,
            pistasDireccionCiudad = ajustes.autofillPistasDireccionCiudad,
            pistasDireccionProvincia = ajustes.autofillPistasDireccionProvincia,
            pistasDireccionPais = ajustes.autofillPistasDireccionPais,
            pistasContactoTelefono = ajustes.autofillPistasContactoTelefono,
            pistasContactoNombre = ajustes.autofillPistasContactoNombre
        )
        if (!campos.hayAlgo) {
            callback.onSuccess(null)
            return
        }
        val descCampos = buildList {
            if (campos.usuario != null) add("usuario")
            if (campos.contrasena != null) add("contraseña")
            if (campos.otp != null) add("código de verificación")
            if (campos.tarjeta.hayAlgo) add("tarjeta bancaria")
            if (campos.direccion.hayAlgo) add("dirección postal")
            if (campos.documento.hayAlgo) add("documento de identidad")
        }.joinToString(" y ")
        Diagnostico.apuntar("autofill", "Formulario reconocido: $descCampos")
        val paquete = estructura.activityComponent?.packageName ?: ""
        val repositorio = VaultRepository.obtener(this)
        val respuesta = FillResponse.Builder()
        val ids: Array<AutofillId> = buildList {
            if (campos.usuario != null) add(campos.usuario)
            if (campos.contrasena != null) add(campos.contrasena)
            if (campos.otp != null) add(campos.otp)
            // Tarjeta
            if (campos.tarjeta.numeroTarjeta != null) add(campos.tarjeta.numeroTarjeta)
            if (campos.tarjeta.cvv != null) add(campos.tarjeta.cvv)
            if (campos.tarjeta.titular != null) add(campos.tarjeta.titular)
            if (campos.tarjeta.vencimiento != null) add(campos.tarjeta.vencimiento)
            if (campos.tarjeta.vencimientoMes != null) add(campos.tarjeta.vencimientoMes)
            if (campos.tarjeta.vencimientoAno != null) add(campos.tarjeta.vencimientoAno)
            // Dirección
            if (campos.direccion.calle != null) add(campos.direccion.calle)
            if (campos.direccion.ciudad != null) add(campos.direccion.ciudad)
            if (campos.direccion.provincia != null) add(campos.direccion.provincia)
            if (campos.direccion.codigoPostal != null) add(campos.direccion.codigoPostal)
            if (campos.direccion.pais != null) add(campos.direccion.pais)
            if (campos.direccion.nombreCompleto != null) add(campos.direccion.nombreCompleto)
            if (campos.direccion.telefono != null) add(campos.direccion.telefono)
            if (campos.direccion.correo != null) add(campos.direccion.correo)
            // Documento
            if (campos.documento.numero != null) add(campos.documento.numero)
            if (campos.documento.nombreCompleto != null) add(campos.documento.nombreCompleto)
        }.distinct().toTypedArray()

        val inlineReq = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) request.inlineSuggestionsRequest else null
        android.util.Log.i("BovedaAutofill", "onFillRequest: paquete=$paquete, inlineReq=$inlineReq, specsCount=${inlineReq?.inlinePresentationSpecs?.size}, switchActivo=${ajustes.autofillSugerenciasTeclado}")
        val specs: List<InlinePresentationSpec> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && ajustes.autofillSugerenciasTeclado) {
            inlineReq?.inlinePresentationSpecs.orEmpty()
        } else {
            emptyList()
        }

        if (!repositorio.estaDesbloqueada) {
            val intent = Intent(this, AutofillAuthActivity::class.java).apply {
                putExtra(EXTRA_USUARIO_ID, campos.usuario)
                putExtra(EXTRA_CONTRASENA_ID, campos.contrasena)
                putExtra(EXTRA_PAQUETE, paquete)
                putExtra(EXTRA_DOMINIO, campos.dominioWeb)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && request.inlineSuggestionsRequest != null) {
                    putExtra(EXTRA_INLINE_REQUEST, request.inlineSuggestionsRequest)
                }
            }
            val pendiente = PendingIntent.getActivity(
                this,
                1001,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            val presentacion = AutofillUtiles.presentacion(this, "Bóveda local está cerrada", "Toca para desbloquearla")
            val inlineSpec = specs.firstOrNull()
            val inlineAuth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineSpec != null) {
                CreadorInlineSuggestion.crear(
                    contexto = this,
                    spec = inlineSpec,
                    titulo = "Desbloquear Bóveda",
                    intencionPendiente = pendiente,
                    fijado = false
                )
            } else null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val presBuilder = android.service.autofill.Presentations.Builder().apply {
                    setMenuPresentation(presentacion)
                    inlineAuth?.let { setInlinePresentation(it) }
                }
                respuesta.setAuthentication(ids, pendiente.intentSender, presBuilder.build())
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineAuth != null) {
                @Suppress("DEPRECATION")
                respuesta.setAuthentication(ids, pendiente.intentSender, presentacion, inlineAuth)
            } else {
                @Suppress("DEPRECATION")
                respuesta.setAuthentication(ids, pendiente.intentSender, presentacion)
            }
        } else {
            com.jlnavas3.bovedalocal.BovedaApp.refrescarActividadSegundoPlano(this)
            val elementosPerfil = ResolvedorElementosPerfilAutofill.resolver(
                entradas = repositorio.entradas(),
                perfiles = repositorio.perfiles(),
                paquete = paquete,
                dominioWeb = campos.dominioWeb,
                mapeoPersonalizado = ajustes.mapeoPaquetesPersonalizados
            )

            // Autocompletado de Tarjetas de Crédito / Pago
            if (campos.tarjeta.hayAlgo && elementosPerfil.tarjetas.isNotEmpty()) {
                elementosPerfil.tarjetas.forEachIndexed { indice, tarjetaEntrada ->
                    val spec = specs.getOrElse(indice) { specs.lastOrNull() }
                    CreadorDatasetTarjetaAutofill.crear(this, tarjetaEntrada, campos.tarjeta, spec)?.let {
                        respuesta.addDataset(it)
                    }
                }
            }

            // Autocompletado de Direcciones / Contactos
            if (campos.direccion.hayAlgo && elementosPerfil.contactos.isNotEmpty()) {
                elementosPerfil.contactos.forEachIndexed { indice, contactoEntrada ->
                    val spec = specs.getOrElse(indice) { specs.lastOrNull() }
                    CreadorDatasetDireccionAutofill.crear(this, contactoEntrada, campos.direccion, spec)?.let {
                        respuesta.addDataset(it)
                    }
                }
            }

            // Autocompletado de Documentos de Identidad (Cédula / DNI)
            if (campos.documento.hayAlgo) {
                val spec = specs.firstOrNull()
                val docPredet = elementosPerfil.documentos.firstOrNull()
                if (docPredet != null) {
                    CreadorDatasetDocumentoAutofill.crear(this, docPredet, campos.documento, spec)?.let {
                        respuesta.addDataset(it)
                    }
                } else if (elementosPerfil.perfil != null && !elementosPerfil.perfil.numeroDocumento.isNullOrBlank()) {
                    CreadorDatasetDocumentoAutofill.crearDesdePerfil(this, elementosPerfil.perfil, campos.documento, spec)?.let {
                        respuesta.addDataset(it)
                    }
                }
            }

            // Autocompletado de Credenciales de Inicio de Sesión
            val compatibles = AutofillUtiles.entradasCompatibles(
                repositorio.entradas(),
                paquete,
                campos.dominioWeb,
                ajustes.mapeoPaquetesPersonalizados
            )
            val sugerencias = if (ajustes.maxSugerenciasAutofill > 0) {
                compatibles.take(ajustes.maxSugerenciasAutofill)
            } else {
                compatibles
            }
            if (compatibles.isEmpty()) {
                val contextoContacto = DetectorContextoContactoAutofill.detectar(
                    paquete = paquete,
                    campos = campos,
                    estructura = estructura,
                    paquetesComunicacion = ajustes.autofillPaquetesComunicacion,
                    pistasTelefono = ajustes.autofillPistasContactoTelefono,
                    pistasNombre = ajustes.autofillPistasContactoNombre
                )
                if (campos.usuario != null || campos.contrasena != null || contextoContacto.esContacto) {
                    val datosSugeridos = DetectorDatosSugeridosAutofill.detectar(
                        this,
                        paquete,
                        campos.dominioWeb,
                        ajustes.navegadoresPersonalizados,
                        ajustes.mapeoPaquetesPersonalizados
                    )
                    val inlineSpec = specs.firstOrNull()
                    CreadorAccionNuevaEntradaAutofill.aplicar(
                        contexto = this,
                        respuesta = respuesta,
                        ids = ids,
                        datosSugeridos = datosSugeridos,
                        inlineSpec = inlineSpec,
                        esContacto = contextoContacto.esContacto,
                        telefonoInicial = contextoContacto.telefonoInicial,
                        nombreInicial = contextoContacto.nombreInicial
                    )
                }
            } else {
                sugerencias.forEachIndexed { indice, entrada ->
                    val spec = specs.getOrElse(indice) { specs.lastOrNull() }
                    AutofillUtiles.dataset(this, entrada, campos, spec, paquete, ajustes.autofillCopiarTotpAutomatico)?.let { respuesta.addDataset(it) }
                    if (campos.otp != null && !entrada.secretoTotp.isNullOrBlank()) {
                        AutofillOtpUtiles.datasetTotp(this, entrada, campos.otp, spec, paquete)?.let { respuesta.addDataset(it) }
                    }
                }
            }
        }

        if (ids.isNotEmpty()) {
            var tipos = 0
            if (campos.usuario != null) tipos = tipos or SaveInfo.SAVE_DATA_TYPE_USERNAME
            if (campos.contrasena != null) tipos = tipos or SaveInfo.SAVE_DATA_TYPE_PASSWORD
            if (campos.tarjeta.hayAlgo) tipos = tipos or SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD
            if (campos.direccion.hayAlgo) tipos = tipos or SaveInfo.SAVE_DATA_TYPE_ADDRESS
            if (tipos != 0) {
                Diagnostico.apuntar("autofill", "Android recibió la opción de guardar")
                respuesta.setSaveInfo(
                    SaveInfo.Builder(tipos, ids)
                        .setFlags(SaveInfo.FLAG_SAVE_ON_ALL_VIEWS_INVISIBLE)
                        .build()
                )
            }
        }

        callback.onSuccess(respuesta.build())
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        val repositorio = VaultRepository.obtener(this)
        if (!repositorio.estaDesbloqueada) {
            Diagnostico.apuntar("autofill", "Guardado rechazado: bóveda bloqueada")
            callback.onFailure("Abre Bóveda local para guardar esta contraseña")
            return
        }
        val contexto = request.fillContexts.lastOrNull()
        if (contexto == null) {
            Diagnostico.apuntar("autofill", "Guardado rechazado: Android no entregó el formulario")
            callback.onFailure("No se pudo leer el formulario")
            return
        }
        val estructura = contexto.structure
        val ajustes = AlmacenAjustes(this).ajustes.value
        val campos = AutofillUtiles.detectar(
            estructura = estructura,
            pistasUsuario = ajustes.autofillPistasUsuario,
            pistasContrasena = ajustes.autofillPistasContrasena,
            pistasOtp = ajustes.autofillPistasOtp,
            pistasDocumento = ajustes.autofillPistasDocumento
        )
        val (usuario, contrasena) = AutofillUtiles.leerValores(
            estructura = estructura,
            pistasUsuario = ajustes.autofillPistasUsuario,
            pistasContrasena = ajustes.autofillPistasContrasena
        )
        if (contrasena.isNullOrBlank()) {
            Diagnostico.apuntar("autofill", "Guardado rechazado: Android no expuso una contraseña")
            callback.onFailure("No se encontró ninguna contraseña que guardar")
            return
        }
        val paquete = estructura.activityComponent?.packageName ?: ""
        val objetivo = AutofillUtiles.contextoSolicitante(paquete, campos.dominioWeb)
        val nombreApp = if (campos.dominioWeb.isNullOrBlank() && paquete.isNotBlank()) {
            LanzadorEnlaces.obtenerNombreApp(this, paquete)
        } else null

        val titulo = if (campos.dominioWeb.isNullOrBlank()) {
            nombreApp?.ifBlank { null } ?: if (paquete.isNotBlank()) Dominios.dominioDePaquete(paquete) else "Nueva entrada"
        } else {
            Dominios.raiz(campos.dominioWeb)
        }

        val urlGuardada = if (campos.dominioWeb.isNullOrBlank() && paquete.isNotBlank()) {
            "android://$paquete"
        } else {
            objetivo
        }

        val existente = repositorio.entradas().firstOrNull { entrada ->
            entrada.usuario == (usuario ?: "") && entrada.urls.any { Dominios.coincide(it, objetivo, ajustes.mapeoPaquetesPersonalizados) }
        }
        val entrada = existente?.let { exist ->
            val domPaquete = if (paquete.isNotBlank()) Dominios.dominioDePaquete(paquete) else ""
            val debeActualizarTitulo = exist.titulo.isBlank() ||
                exist.titulo == "Nueva entrada" ||
                (domPaquete.isNotBlank() && exist.titulo.equals(domPaquete, ignoreCase = true)) ||
                (paquete.isNotBlank() && exist.titulo.equals(paquete, ignoreCase = true))

            val urlsActualizadas = if (campos.dominioWeb.isNullOrBlank() && paquete.isNotBlank() && !exist.urls.any { LanzadorEnlaces.extraerPaquete(it) == paquete }) {
                exist.urls + urlGuardada
            } else exist.urls

            exist.copy(
                contrasena = contrasena,
                titulo = if (debeActualizarTitulo) titulo.ifBlank { exist.titulo } else exist.titulo,
                urls = urlsActualizadas
            )
        } ?: Entrada(
            id = repositorio.nuevoId(),
            tipo = TipoEntrada.LOGIN,
            titulo = titulo.ifBlank { "Nueva entrada" },
            usuario = usuario ?: "",
            contrasena = contrasena,
            urls = listOf(urlGuardada)
        )
        try {
            repositorio.guardarEntrada(entrada)
            Diagnostico.apuntar("autofill", "Contraseña guardada correctamente")
            callback.onSuccess()
        } catch (e: Exception) {
            Diagnostico.apuntar("autofill", "Guardado falló: ${e.javaClass.simpleName}")
            callback.onFailure("No se pudo guardar en la bóveda")
        }
    }
}
