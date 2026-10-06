package com.jlnavas3.bovedalocal.autofill

import android.app.assist.AssistStructure
import android.content.Context
import android.service.autofill.Dataset
import android.text.InputType
import android.view.View
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.widget.RemoteViews
import androidx.core.graphics.drawable.toBitmap
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.MapeadorPaquetesPopulares

data class CamposDetectados(
    val usuario: AutofillId? = null,
    val contrasena: AutofillId? = null,
    val dominioWeb: String? = null,
    val otp: AutofillId? = null
) {
    val hayAlgo: Boolean get() = usuario != null || contrasena != null || otp != null
}

object AutofillUtiles {

    private val PISTAS_USUARIO = listOf("username", "email", "user", "correo", "usuario", "login", "identifier", "phone")
    private val PISTAS_CONTRASENA = listOf("password", "contrasena", "contraseña", "passwd", "pwd", "clave")

    fun detectar(
        estructura: AssistStructure,
        pistasUsuario: List<String> = emptyList(),
        pistasContrasena: List<String> = emptyList(),
        pistasOtp: List<String> = emptyList()
    ): CamposDetectados {
        val listaUsuario = pistasUsuario.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_USUARIO }
        val listaContrasena = pistasContrasena.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_CONTRASENA }
        var usuario: AutofillId? = null
        var contrasena: AutofillId? = null
        var otp: AutofillId? = null
        var dominio: String? = null

        fun recorrer(nodo: AssistStructure.ViewNode) {
            nodo.webDomain?.takeIf { it.isNotBlank() }?.let { if (dominio == null) dominio = it }
            val id = nodo.autofillId
            val tipoValido = nodo.autofillType == View.AUTOFILL_TYPE_TEXT || nodo.autofillType == View.AUTOFILL_TYPE_NONE
            if (id != null && tipoValido) {
                var esContrasenaWeb = false
                var esUsuarioWeb = false
                var htmlAutocomplete: String? = null
                val pistasSistema = nodo.autofillHints?.map { it.lowercase() } ?: emptyList()
                val textoPistas = buildList {
                    addAll(pistasSistema)
                    nodo.hint?.lowercase()?.let { add(it) }
                    nodo.idEntry?.lowercase()?.let { add(it) }
                    nodo.text?.toString()?.lowercase()?.let { add(it) }
                    nodo.htmlInfo?.attributes?.forEach { par ->
                        val nombreAtributo = par.first.lowercase()
                        val valorAtributo = par.second?.lowercase() ?: ""
                        if (nombreAtributo == "type" && valorAtributo == "password") {
                            esContrasenaWeb = true
                        } else if (nombreAtributo == "autocomplete") {
                            htmlAutocomplete = valorAtributo
                            if (valorAtributo == "username" || valorAtributo == "email") {
                                esUsuarioWeb = true
                            }
                        } else if ((nombreAtributo == "name" || nombreAtributo == "id") && valorAtributo.isNotBlank()) {
                            add(valorAtributo)
                        }
                    }
                }
                val esOtp = AutofillOtpUtiles.esCampoOtp(pistasSistema, textoPistas, htmlAutocomplete, pistasOtp)
                val variacion = nodo.inputType and InputType.TYPE_MASK_VARIATION
                val esContrasenaPorTipo = variacion == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    variacion == InputType.TYPE_NUMBER_VARIATION_PASSWORD
                val esContrasena = esContrasenaWeb || esContrasenaPorTipo || textoPistas.any { pista ->
                    listaContrasena.any { pista.contains(it) }
                }
                val esUsuario = esUsuarioWeb || textoPistas.any { pista -> listaUsuario.any { pista.contains(it) } }
                if (esOtp && otp == null) {
                    otp = id
                } else if (esContrasena && contrasena == null) {
                    contrasena = id
                } else if (esUsuario && usuario == null) {
                    usuario = id
                }
            }
            for (i in 0 until nodo.childCount) recorrer(nodo.getChildAt(i))
        }

        for (i in 0 until estructura.windowNodeCount) {
            recorrer(estructura.getWindowNodeAt(i).rootViewNode)
        }
        return CamposDetectados(usuario = usuario, contrasena = contrasena, dominioWeb = dominio, otp = otp)
    }

    /** Devuelve el par (usuario, contraseña) escrito por la persona, para el flujo de guardado. */
    fun leerValores(
        estructura: AssistStructure,
        pistasUsuario: List<String> = emptyList(),
        pistasContrasena: List<String> = emptyList()
    ): Pair<String?, String?> {
        val listaUsuario = pistasUsuario.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_USUARIO }
        val listaContrasena = pistasContrasena.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_CONTRASENA }
        var usuario: String? = null
        var contrasena: String? = null

        fun texto(nodo: AssistStructure.ViewNode): String? {
            val valor = nodo.autofillValue
            return when {
                valor != null && valor.isText -> valor.textValue?.toString()
                else -> nodo.text?.toString()
            }?.takeIf { it.isNotBlank() }
        }

        fun recorrer(nodo: AssistStructure.ViewNode) {
            val tipoValido = nodo.autofillType == View.AUTOFILL_TYPE_TEXT || nodo.autofillType == View.AUTOFILL_TYPE_NONE
            if (tipoValido) {
                var esContrasenaWeb = false
                var esUsuarioWeb = false
                val pistas = buildList {
                    nodo.autofillHints?.forEach { add(it.lowercase()) }
                    nodo.hint?.lowercase()?.let { add(it) }
                    nodo.idEntry?.lowercase()?.let { add(it) }
                    nodo.htmlInfo?.attributes?.forEach { par ->
                        val nombreAtributo = par.first.lowercase()
                        val valorAtributo = par.second?.lowercase() ?: ""
                        if (nombreAtributo == "type" && valorAtributo == "password") {
                            esContrasenaWeb = true
                        } else if (nombreAtributo == "autocomplete" && (valorAtributo == "username" || valorAtributo == "email")) {
                            esUsuarioWeb = true
                        } else if ((nombreAtributo == "name" || nombreAtributo == "id") && valorAtributo.isNotBlank()) {
                            add(valorAtributo)
                        }
                    }
                }
                val variacion = nodo.inputType and InputType.TYPE_MASK_VARIATION
                val esContrasenaPorTipo = variacion == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                    variacion == InputType.TYPE_NUMBER_VARIATION_PASSWORD
                val esContrasena = esContrasenaWeb || esContrasenaPorTipo || pistas.any { p -> listaContrasena.any { p.contains(it) } }
                val esUsuario = esUsuarioWeb || pistas.any { p -> listaUsuario.any { p.contains(it) } }
                if (esContrasena && contrasena == null) contrasena = texto(nodo)
                else if (esUsuario && usuario == null) usuario = texto(nodo)
            }
            for (i in 0 until nodo.childCount) recorrer(nodo.getChildAt(i))
        }

        for (i in 0 until estructura.windowNodeCount) {
            recorrer(estructura.getWindowNodeAt(i).rootViewNode)
        }
        return usuario to contrasena
    }

    /**
     * Obtiene el bitmap del icono de la app con máscara circular recortada
     * y escala ampliada (1.22f), adecuado para mostrarse en RemoteViews.
     */
    fun obtenerBitmapIconoCircular(contexto: Context, paquete: String, tamanoPx: Int = 96): Bitmap? {
        return try {
            val pm = contexto.packageManager
            val appInfo = pm.getApplicationInfo(paquete, 0)
            val drawable = appInfo.loadIcon(pm) ?: return null
            val bitmap = Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val path = Path().apply {
                addCircle(tamanoPx / 2f, tamanoPx / 2f, tamanoPx / 2f, Path.Direction.CCW)
            }
            canvas.clipPath(path)
            drawable.setBounds(0, 0, tamanoPx, tamanoPx)
            canvas.scale(1.22f, 1.22f, tamanoPx / 2f, tamanoPx / 2f)
            drawable.draw(canvas)
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Obtiene el bitmap nativo del icono de la app (soporta AdaptiveIconDrawable y VectorDrawable),
     * adecuado para sugerencias en línea del teclado (InlinePresentation).
     */
    fun obtenerBitmapIcono(contexto: Context, paquete: String, tamanoPx: Int = 96): Bitmap? {
        return try {
            val pm = contexto.packageManager
            val appInfo = pm.getApplicationInfo(paquete, 0)
            val drawable = appInfo.loadIcon(pm) ?: return null
            drawable.toBitmap(width = tamanoPx, height = tamanoPx)
        } catch (_: Exception) {
            null
        }
    }

    fun presentacion(
        contexto: Context,
        titulo: String,
        subtitulo: String,
        iconoBitmap: Bitmap? = null
    ): RemoteViews =
        RemoteViews(contexto.packageName, R.layout.autofill_item).apply {
            if (iconoBitmap != null) {
                setImageViewBitmap(R.id.icono_autofill, iconoBitmap)
            } else {
                setImageViewResource(R.id.icono_autofill, R.drawable.ic_candado_boveda)
            }
            setTextViewText(R.id.titulo, titulo)
            setTextViewText(R.id.subtitulo, subtitulo)
        }

    @Suppress("DEPRECATION")
    fun dataset(
        contexto: Context,
        entrada: Entrada,
        campos: CamposDetectados,
        inlineSpec: android.widget.inline.InlinePresentationSpec? = null,
        paqueteSolicitante: String? = null
    ): Dataset? {
        if (!campos.hayAlgo) return null

        val dom = (campos.dominioWeb ?: entrada.urls.firstNotNullOfOrNull { Dominios.host(it) })?.let { Dominios.raiz(it) }
        val paqueteDeDominio = dom?.let { com.jlnavas3.bovedalocal.util.ResolverIconoAppDominio.resolverPaquete(contexto, it) }

        val paqueteDirecto = entrada.urls.firstNotNullOfOrNull { LanzadorEnlaces.extraerPaquete(it) }
            ?: entrada.passkey?.rpId?.let { LanzadorEnlaces.extraerPaquete(it) }

        val paquete = if (paqueteDirecto != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDirecto)) {
            paqueteDirecto
        } else if (paqueteDeDominio != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDeDominio)) {
            paqueteDeDominio
        } else {
            GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
                ?: (if (campos.dominioWeb.isNullOrBlank() && !paqueteSolicitante.isNullOrBlank() && LanzadorEnlaces.estaInstalada(contexto, paqueteSolicitante)) paqueteSolicitante else paqueteDirecto)
        }

        val nombreApp = if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
        } else null

        val iconoBitmapCirculo = if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            obtenerBitmapIconoCircular(contexto, paquete)
        } else null

        val iconoGlifoTeclado = ResolverIconoGlifoTeclado.resolver(
            contexto = contexto,
            entrada = entrada,
            paquete = paquete,
            dominioWeb = campos.dominioWeb ?: entrada.urls.firstNotNullOfOrNull { Dominios.host(it) }
        )

        val domPaquete = if (paquete != null) Dominios.dominioDePaquete(paquete) else null
        val tituloAlmacenado = entrada.titulo.trim()

        val tituloMostrar = when {
            !nombreApp.isNullOrBlank() && (tituloAlmacenado.isBlank() ||
                tituloAlmacenado == "Nueva entrada" ||
                tituloAlmacenado.equals(domPaquete, ignoreCase = true) ||
                tituloAlmacenado.equals(paquete, ignoreCase = true)) -> nombreApp
            tituloAlmacenado.isNotBlank() -> tituloAlmacenado
            entrada.usuario.isNotBlank() -> entrada.usuario
            else -> "Entrada"
        }

        val subtituloMostrar = when {
            entrada.usuario.isNotBlank() -> entrada.usuario
            !nombreApp.isNullOrBlank() -> "Contraseña guardada"
            else -> entrada.urls.firstOrNull() ?: "Bóveda local"
        }

        val vista = presentacion(
            contexto = contexto,
            titulo = tituloMostrar,
            subtitulo = subtituloMostrar,
            iconoBitmap = iconoBitmapCirculo
        )

        val inlineTitulo = if (entrada.usuario.isNotBlank()) entrada.usuario else tituloMostrar
        val inlineSubtitulo = if (entrada.usuario.isNotBlank()) {
            if (!nombreApp.isNullOrBlank()) nombreApp else (entrada.titulo.takeIf { it.isNotBlank() } ?: subtituloMostrar)
        } else {
            subtituloMostrar
        }

        val inlinePres = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && inlineSpec != null) {
            CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = inlineTitulo,
                subtitulo = inlineSubtitulo,
                iconoBitmap = iconoGlifoTeclado
            )
        } else null

        android.util.Log.i("BovedaAutofill", "Dataset para ${entrada.titulo}: inlinePres=$inlinePres, inlineSpec=$inlineSpec")
        val constructor = Dataset.Builder(vista)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && inlinePres != null) {
            constructor.setInlinePresentation(inlinePres)
            campos.usuario?.let {
                @Suppress("DEPRECATION")
                constructor.setValue(it, AutofillValue.forText(entrada.usuario), vista, inlinePres)
            }
            campos.contrasena?.let {
                @Suppress("DEPRECATION")
                constructor.setValue(it, AutofillValue.forText(entrada.contrasena), vista, inlinePres)
            }
            campos.otp?.let { idOtp ->
                AutofillOtpUtiles.obtenerCodigoTotp(entrada)?.let { codigo ->
                    @Suppress("DEPRECATION")
                    constructor.setValue(idOtp, AutofillValue.forText(codigo), vista, inlinePres)
                }
            }
        } else {
            campos.usuario?.let { constructor.setValue(it, AutofillValue.forText(entrada.usuario)) }
            campos.contrasena?.let { constructor.setValue(it, AutofillValue.forText(entrada.contrasena)) }
            campos.otp?.let { idOtp ->
                AutofillOtpUtiles.obtenerCodigoTotp(entrada)?.let { codigo ->
                    constructor.setValue(idOtp, AutofillValue.forText(codigo))
                }
            }
        }
        return try {
            constructor.build()
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /** Identificador del contexto que pide el relleno: dominio web o paquete de la app. */
    fun contextoSolicitante(paquete: String, dominioWeb: String?): String =
        if (!dominioWeb.isNullOrBlank()) Dominios.raiz(dominioWeb) else paquete

    fun entradasCompatibles(entradas: List<Entrada>, paquete: String, dominioWeb: String?): List<Entrada> {
        val objetivo = contextoSolicitante(paquete, dominioWeb)
        return entradas.filter { entrada ->
            (entrada.contrasena.isNotBlank() || !entrada.secretoTotp.isNullOrBlank()) && entrada.urls.any { guardado ->
                Dominios.coincide(guardado, objetivo)
            }
        }
    }
}
