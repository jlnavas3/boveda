package com.jlnavas3.bovedalocal.data

import com.jlnavas3.bovedalocal.util.Dominios

enum class TipoDuplicado(val titulo: String, val descripcion: String) {
    IDENTICO(
        "Copia idéntica",
        "Mismo usuario, contraseña y servicio. Típico al re-importar el mismo archivo CSV."
    ),
    MISMA_CUENTA_DISTINTA_CLAVE(
        "Misma cuenta (distinta clave)",
        "Mismo usuario y servicio, pero con contraseñas distintas."
    ),
    VARIANTE_USUARIO(
        "Variante de usuario",
        "Misma clave y servicio, pero con variantes en el nombre de usuario o correo."
    )
}

data class GrupoDuplicado(
    val idGrupo: String,
    val tipo: TipoDuplicado,
    val claveVisual: String,
    val entradas: List<Entrada>,
    val sugeridaPrincipal: Entrada,
    val esAppAndroid: Boolean = false
) {
    val entradasSecundarias: List<Entrada>
        get() = entradas.filterNot { it.id == sugeridaPrincipal.id }
}

object AnalizadorDuplicados {

    fun esAppAndroid(entrada: Entrada): Boolean {
        return entrada.urls.any { it.trim().startsWith("android://", ignoreCase = true) }
    }

    fun extraerPaqueteAndroid(url: String): String {
        val u = url.trim()
        if (!u.startsWith("android://", ignoreCase = true)) return ""
        val sinEsquema = u.substringAfter("://", u)
        val sinRuta = sinEsquema.substringBefore('/').substringBefore('?').substringBefore('#')
        val sinCredenciales = sinRuta.substringAfterLast('@')
        return sinCredenciales.substringBefore(':').trim().lowercase()
    }

    /**
     * Normaliza el servicio / sitio web o app de Android de la entrada:
     * - Si es App de Android, extrae el paquete o el dominio del título (ej. "disneyplus.com", "com.xiaomi.account").
     *   NUNCA agrupa diferentes apps bajo "android".
     * - Si es Web, extrae el dominio raíz registrable (ej. "google.com", "amazon.com").
     */
    fun normalizarServicio(entrada: Entrada): String {
        val urlAndroid = entrada.urls.firstOrNull { it.trim().startsWith("android://", ignoreCase = true) }
        if (urlAndroid != null) {
            val paquete = extraerPaqueteAndroid(urlAndroid)
            val tit = entrada.titulo.trim().lowercase()

            // Si el título es un dominio web o contiene punto (ej. "disneyplus.com", "account.xiaomi.com", "netflix.com")
            if (tit.contains('.') && !tit.contains(' ') && !tit.equals("android", ignoreCase = true)) {
                val raiz = Dominios.raiz(tit)
                if (raiz.isNotBlank()) return "app:$raiz"
            }

            // Si el paquete es conocido y tiene forma de paquete Java (ej. com.xiaomi.account -> xiaomi.com)
            if (paquete.isNotBlank()) {
                val domPaquete = Dominios.dominioDePaquete(paquete)
                val raizPaquete = Dominios.raiz(domPaquete)
                if (raizPaquete.isNotBlank() && raizPaquete.contains('.')) return "app:$raizPaquete"
                return "app:$paquete"
            }

            // Si el título es el nombre propio de la app (ej. "Oral-B", "Solid Explorer File Manager")
            if (tit.isNotBlank() && !tit.equals("android", ignoreCase = true)) {
                return "app:$tit"
            }
        }

        // Si tiene URL Web tradicional:
        val urlWeb = entrada.urls.firstOrNull { !it.trim().startsWith("android://", ignoreCase = true) }
        if (!urlWeb.isNullOrBlank()) {
            val raiz = Dominios.raiz(urlWeb)
            if (raiz.isNotBlank()) return "web:$raiz"
            val host = Dominios.host(urlWeb)
            if (host.isNotBlank()) return "web:$host"
        }

        // Si no tiene URLs, usar título:
        val tit = entrada.titulo.trim().lowercase()
        if (tit.isNotBlank()) {
            val raiz = Dominios.raiz(tit)
            if (raiz.isNotBlank()) return "web:$raiz"
            return "web:$tit"
        }

        return "desconocido"
    }

    private fun armarClaveVisual(entrada: Entrada): String {
        val esApp = esAppAndroid(entrada)
        val nombreServicio = if (esApp) {
            val urlAndroid = entrada.urls.firstOrNull { it.trim().startsWith("android://", ignoreCase = true) } ?: ""
            val paquete = extraerPaqueteAndroid(urlAndroid)
            val tit = entrada.titulo.trim()
            if (tit.isNotBlank() && !tit.equals("android", ignoreCase = true)) tit
            else if (paquete.isNotBlank()) paquete
            else "App Android"
        } else {
            val urlWeb = entrada.urls.firstOrNull()
            if (!urlWeb.isNullOrBlank()) {
                val r = Dominios.raiz(urlWeb)
                if (r.isNotBlank()) r else entrada.titulo.ifBlank { "Sin título" }
            } else {
                entrada.titulo.ifBlank { "Sin título" }
            }
        }
        val usuario = entrada.usuario.ifBlank { "Sin usuario" }
        return if (esApp) "$nombreServicio (App Android) · $usuario" else "$nombreServicio · $usuario"
    }

    /**
     * Normaliza el nombre de usuario según el servicio para detectar variantes de la misma cuenta:
     * - En Google / Gmail: "usuario@gmail.com" y "usuario" representan la misma cuenta de Google.
     * - En Yahoo: "usuario@yahoo.com" y "usuario" representan la misma cuenta.
     * - En Microsoft / Outlook: "usuario@outlook.com" y "usuario" representan la misma cuenta.
     * - Si el correo termina en @<dominioServicio> (ej. en spotify.com "juan@spotify.com" y "juan").
     * - Números telefónicos: unifica formato internacional y local (ej. +593991234567 y 0991234567).
     *
     * IMPORTANTE: Cuentas con usuarios completamente distintos (ej. "curtain.cross" vs "fregenavi")
     * o correos con dominios institucionales distintos (ej. "jlnavas3@utpl.edu.ec" vs "jlnavas3.utpl.edu.ec@gmail.com")
     * NUNCA son equivalentes y se mantienen estrictamente separadas.
     */
    fun normalizarUsuario(usuario: String, servicio: String): String {
        val u = usuario.trim().lowercase()
        if (u.isBlank()) return ""

        val servLower = servicio.lowercase()

        // Google / Gmail: "@gmail.com" y "@googlemail.com" son equivalentes al usuario sin dominio
        if (servLower.contains("google") || servLower.contains("gmail")) {
            if (u.endsWith("@gmail.com")) return u.removeSuffix("@gmail.com")
            if (u.endsWith("@googlemail.com")) return u.removeSuffix("@googlemail.com")
        }

        // Yahoo
        if (servLower.contains("yahoo")) {
            if (u.endsWith("@yahoo.com")) return u.removeSuffix("@yahoo.com")
            if (u.endsWith("@yahoo.es")) return u.removeSuffix("@yahoo.es")
        }

        // Microsoft / Outlook / Hotmail / Live
        if (servLower.contains("microsoft") || servLower.contains("live.com") || servLower.contains("outlook") || servLower.contains("hotmail")) {
            if (u.endsWith("@outlook.com")) return u.removeSuffix("@outlook.com")
            if (u.endsWith("@hotmail.com")) return u.removeSuffix("@hotmail.com")
            if (u.endsWith("@live.com")) return u.removeSuffix("@live.com")
        }

        // Si el usuario termina en @<dominioServicio> (ej. en spotify.com usuario es 'juan@spotify.com' y otro 'juan')
        val dominioServ = servLower.substringAfter("web:").substringAfter("app:")
        if (dominioServ.isNotBlank() && dominioServ.contains('.') && u.endsWith("@$dominioServ")) {
            return u.removeSuffix("@$dominioServ")
        }

        // Normalización de números telefónicos (quitar espacios, guiones, paréntesis)
        if (u.matches(Regex("""^\+?[\d\s\-()]+$""")) && u.count { it.isDigit() } >= 7) {
            var digitos = u.filter { it.isDigit() }
            if (digitos.startsWith("593") && digitos.length == 12) {
                digitos = "0" + digitos.removePrefix("593")
            }
            return digitos
        }

        return u
    }

    /**
     * Analiza una lista de entradas activas y devuelve todos los grupos de duplicados encontrados.
     */
    fun analizar(entradas: List<Entrada>): List<GrupoDuplicado> {
        val activas = entradas.filter { it.eliminadaEn == 0L }
        val resultado = mutableListOf<GrupoDuplicado>()
        val idsProcesados = mutableSetOf<String>()

        // 1. Detección de copias idénticas exactas (mismo servicio, mismo usuario literal y misma contraseña)
        // Esto captura de forma infalible las importaciones repetidas del mismo archivo CSV.
        val gruposIdenticos = activas
            .groupBy { entrada ->
                val serv = normalizarServicio(entrada)
                val usr = entrada.usuario.trim().lowercase()
                val pwd = entrada.contrasena
                "$serv|||$usr|||$pwd"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposIdenticos) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "identico_${clave.hashCode()}",
                    tipo = TipoDuplicado.IDENTICO,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 2. Detección de duplicados de la MISMA cuenta (mismo servicio y mismo usuario normalizado)
        // IMPORTANTE: Cuentas con usuarios diferentes NUNCA se agrupan aquí aunque compartan clave y servicio.
        val restantes = activas.filterNot { it.id in idsProcesados }
        val gruposPorCuenta = restantes
            .filter { it.usuario.isNotBlank() }
            .groupBy { entrada ->
                val serv = normalizarServicio(entrada)
                val usrNorm = normalizarUsuario(entrada.usuario, serv)
                "$serv|||$usrNorm"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposPorCuenta) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = armarClaveVisual(principal)
            val todasMismaClave = lista.map { it.contrasena }.distinct().size == 1

            // Si tienen la misma clave pero llegaron aquí, es porque su usuario literal tiene variantes
            // (ej. "jlnavas3.utpl.edu.ec" vs "jlnavas3.utpl.edu.ec@gmail.com")
            val tipo = if (todasMismaClave) {
                TipoDuplicado.VARIANTE_USUARIO
            } else {
                TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE
            }

            resultado.add(
                GrupoDuplicado(
                    idGrupo = "cuenta_${clave.hashCode()}",
                    tipo = tipo,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 3. Entradas sin usuario pero con misma contraseña y servicio (copias anónimas exactas)
        val restantesSinUsuario = activas.filterNot { it.id in idsProcesados }
        val gruposSinUsuario = restantesSinUsuario
            .filter { it.usuario.isBlank() && it.contrasena.isNotBlank() }
            .groupBy { entrada ->
                val serv = normalizarServicio(entrada)
                val pwd = entrada.contrasena
                "$serv|||__sin_usuario__|||$pwd"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposSinUsuario) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "anonimo_${clave.hashCode()}",
                    tipo = TipoDuplicado.IDENTICO,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        return resultado.sortedWith(
            compareBy<GrupoDuplicado> { it.tipo.ordinal }
                .thenByDescending { it.entradas.size }
        )
    }

    /**
     * Determina la mejor entrada de un conjunto duplicado para conservarla como principal:
     * Prioriza la que tenga TOTP, passkey, más campos personalizados, más notas, correo completo (@), longitud o fecha más reciente.
     */
    fun seleccionarMejorEntrada(entradas: List<Entrada>): Entrada {
        return entradas.maxWithOrNull(
            compareBy<Entrada> { if (it.favorito) 1 else 0 }
                .thenBy { if (!it.secretoTotp.isNullOrBlank()) 1 else 0 }
                .thenBy { if (it.passkey != null) 1 else 0 }
                .thenBy { it.camposPersonalizados.size }
                .thenBy { it.notas.length }
                .thenBy { if (it.usuario.contains("@")) 1 else 0 }
                .thenBy { it.usuario.length }
                .thenBy { it.modificadaEn }
                .thenBy { it.creadaEn }
        ) ?: entradas.first()
    }

    /**
     * Fusiona las secundarias en la principal sin perder información valiosa:
     * - Si una es Passkey y otra tiene contraseña, la entrada resultante conserva AMBAS (Passkey + contraseña).
     * - Si alguna tiene 2FA (TOTP), se preserva.
     * - Combina notas, URLs no repetidas y campos personalizados adicionales.
     * - Guarda las contraseñas alternativas adicionales de las secundarias en campos personalizados sensibles
     *   y en el historial de contraseñas para que el usuario pueda probarlas luego sin perderlas.
     */
    fun fusionar(principal: Entrada, secundarias: List<Entrada>): Entrada {
        val urlsCombinadas = (principal.urls + secundarias.flatMap { it.urls }).distinct().filter { it.isNotBlank() }

        val camposCombinados = principal.camposPersonalizados.toMutableList()
        val nuevoHistorial = (principal.historialContrasenas + secundarias.flatMap { it.historialContrasenas }).toMutableList()

        val notasNuevas = StringBuilder(principal.notas.trim())
        var clavesAlternativasGuardadas = 0

        // Si la principal no tiene contraseña (ej. era de tipo PASSKEY), pero una secundaria sí, adoptamos esa contraseña
        var contrasenaFinal = principal.contrasena
        if (contrasenaFinal.isBlank()) {
            val primeraConClave = secundarias.firstOrNull { it.contrasena.isNotBlank() }
            if (primeraConClave != null) {
                contrasenaFinal = primeraConClave.contrasena
            }
        }

        // Conservar Passkey si la principal no tiene pero alguna secundaria sí
        val passkeyFinal = principal.passkey ?: secundarias.mapNotNull { it.passkey }.firstOrNull()

        // Conservar TOTP (2FA) si la principal no tiene pero alguna secundaria sí
        val secConTotp = secundarias.firstOrNull { !it.secretoTotp.isNullOrBlank() }
        val totpFinal = principal.secretoTotp ?: secConTotp?.secretoTotp
        val emisorFinal = principal.totpEmisor.ifBlank { secConTotp?.totpEmisor ?: "" }
        val digitosFinal = if (principal.secretoTotp != null) principal.totpDigitos else (secConTotp?.totpDigitos ?: 6)
        val periodoFinal = if (principal.secretoTotp != null) principal.totpPeriodo else (secConTotp?.totpPeriodo ?: 30)
        val algoritmoFinal = if (principal.secretoTotp != null) principal.totpAlgoritmo else (secConTotp?.totpAlgoritmo ?: "HmacSHA1")

        for ((indice, sec) in secundarias.withIndex()) {
            val n = sec.notas.trim()
            if (n.isNotBlank() && !notasNuevas.contains(n)) {
                if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
                notasNuevas.append(n)
            }

            // Si la contraseña de la secundaria es distinta a la contraseña final, la guardamos como alternativa
            if (sec.contrasena.isNotBlank() && sec.contrasena != contrasenaFinal) {
                clavesAlternativasGuardadas++
                val etiquetaClave = if (sec.titulo.isNotBlank() && sec.titulo != principal.titulo) {
                    "Clave alternativa (${sec.titulo})"
                } else if (secundarias.size > 1) {
                    "Clave alternativa ${indice + 1}"
                } else {
                    "Clave alternativa"
                }

                // Guardar en campo personalizado sensible (visible y copiable en la ficha de la entrada)
                if (camposCombinados.none { it.etiqueta.equals(etiquetaClave, ignoreCase = true) }) {
                    camposCombinados.add(
                        CampoPersonalizado(
                            id = java.util.UUID.randomUUID().toString(),
                            etiqueta = etiquetaClave,
                            valor = sec.contrasena,
                            esSensible = true,
                            tipo = TipoCampo.TEXTO
                        )
                    )
                }

                // Guardar en el historial de contraseñas de la entrada
                if (nuevoHistorial.none { it.contrasena == sec.contrasena }) {
                    val momento = if (sec.modificadaEn > 0) sec.modificadaEn else System.currentTimeMillis()
                    nuevoHistorial.add(CambioContrasena(contrasena = sec.contrasena, cambiadaEn = momento))
                }
            }

            // Conservar campos personalizados de las secundarias que no existan
            for (c in sec.camposPersonalizados) {
                if (camposCombinados.none { it.etiqueta.equals(c.etiqueta, ignoreCase = true) }) {
                    camposCombinados.add(c)
                }
            }
        }

        if (clavesAlternativasGuardadas > 0) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("[$clavesAlternativasGuardadas clave(s) alternativa(s) guardada(s) en campos personalizados e historial al unificar cuentas].")
        }

        val etiquetasCombinadas = (principal.etiquetas + secundarias.flatMap { it.etiquetas }).distinct()
        val tipoFinal = if (contrasenaFinal.isNotBlank()) TipoEntrada.LOGIN else principal.tipo

        return principal.copy(
            tipo = tipoFinal,
            contrasena = contrasenaFinal,
            passkey = passkeyFinal,
            secretoTotp = totpFinal,
            totpEmisor = emisorFinal,
            totpDigitos = digitosFinal,
            totpPeriodo = periodoFinal,
            totpAlgoritmo = algoritmoFinal,
            urls = urlsCombinadas,
            notas = notasNuevas.toString(),
            camposPersonalizados = camposCombinados,
            historialContrasenas = nuevoHistorial.distinctBy { it.contrasena }.filterNot { it.contrasena == contrasenaFinal },
            etiquetas = etiquetasCombinadas,
            modificadaEn = System.currentTimeMillis()
        )
    }
}
