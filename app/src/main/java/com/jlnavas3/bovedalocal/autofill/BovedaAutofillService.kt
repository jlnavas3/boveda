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
        val campos = AutofillUtiles.detectar(estructura)
        if (!campos.hayAlgo) {
            callback.onSuccess(null)
            return
        }
        val descCampos = buildList {
            if (campos.usuario != null) add("usuario")
            if (campos.contrasena != null) add("contraseña")
            if (campos.otp != null) add("código de verificación")
        }.joinToString(" y ")
        Diagnostico.apuntar("autofill", "Formulario reconocido: $descCampos")
        val paquete = estructura.activityComponent?.packageName ?: ""
        val repositorio = VaultRepository.obtener(this)
        val respuesta = FillResponse.Builder()
        val ids: Array<AutofillId> = listOfNotNull(campos.usuario, campos.contrasena, campos.otp).toTypedArray()

        val ajustes = repositorio.ajustes.ajustes.value
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
            val compatibles = AutofillUtiles.entradasCompatibles(repositorio.entradas(), paquete, campos.dominioWeb)
            if (compatibles.isEmpty()) {
                val datosSugeridos = DetectorDatosSugeridosAutofill.detectar(this, paquete, campos.dominioWeb)
                val inlineSpec = specs.firstOrNull()
                CreadorAccionNuevaEntradaAutofill.aplicar(
                    contexto = this,
                    respuesta = respuesta,
                    ids = ids,
                    datosSugeridos = datosSugeridos,
                    inlineSpec = inlineSpec
                )
            } else {
                compatibles.forEachIndexed { indice, entrada ->
                    val spec = specs.getOrElse(indice) { specs.lastOrNull() }
                    AutofillUtiles.dataset(this, entrada, campos, spec, paquete)?.let { respuesta.addDataset(it) }
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
        val campos = AutofillUtiles.detectar(estructura)
        val (usuario, contrasena) = AutofillUtiles.leerValores(estructura)
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
            Dominios.raiz(campos.dominioWeb!!)
        }

        val urlGuardada = if (campos.dominioWeb.isNullOrBlank() && paquete.isNotBlank()) {
            "android://$paquete"
        } else {
            objetivo
        }

        val existente = repositorio.entradas().firstOrNull { entrada ->
            entrada.usuario == (usuario ?: "") && entrada.urls.any { Dominios.coincide(it, objetivo) }
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
