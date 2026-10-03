package com.jlnavas3.bovedalocal.data

object AnalizadorDuplicados {

    fun esAppAndroid(entrada: Entrada): Boolean = NormalizadorDuplicados.esAppAndroid(entrada)

    fun extraerPaqueteAndroid(url: String): String = NormalizadorDuplicados.extraerPaqueteAndroid(url)

    fun normalizarServicio(entrada: Entrada): String = NormalizadorDuplicados.normalizarServicio(entrada)

    fun normalizarUsuario(usuario: String, servicio: String): String = NormalizadorDuplicados.normalizarUsuario(usuario, servicio)

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
                val serv = NormalizadorDuplicados.normalizarServicio(entrada)
                val usr = entrada.usuario.trim().lowercase()
                val pwd = entrada.contrasena
                "$serv|||$usr|||$pwd"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposIdenticos) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = NormalizadorDuplicados.armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "identico_${clave.hashCode()}",
                    tipo = TipoDuplicado.IDENTICO,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { NormalizadorDuplicados.esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 2. Detección de duplicados de Passkey (mismo credId o mismo RP ID + usuario)
        val restantesParaPasskey = activas.filterNot { it.id in idsProcesados }
        val gruposPasskey = restantesParaPasskey
            .filter { it.passkey != null && (it.passkey.credId.isNotBlank() || it.passkey.rpId.isNotBlank()) }
            .groupBy { entrada ->
                val pk = entrada.passkey!!
                if (pk.credId.isNotBlank()) {
                    "credId|||${pk.credId.trim()}"
                } else {
                    val serv = pk.rpId.ifBlank { NormalizadorDuplicados.normalizarServicio(entrada) }
                    val usr = pk.usuario.ifBlank { entrada.usuario }.trim().lowercase()
                    "rpId|||$serv|||$usr"
                }
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposPasskey) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = NormalizadorDuplicados.armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "passkey_${clave.hashCode()}",
                    tipo = TipoDuplicado.PASSKEY,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { NormalizadorDuplicados.esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 3. Detección de duplicados de TOTP (mismo secreto)
        val restantesParaTotp = activas.filterNot { it.id in idsProcesados }
        val gruposTotp = restantesParaTotp
            .filter { !it.secretoTotp.isNullOrBlank() }
            .groupBy { entrada ->
                entrada.secretoTotp!!.replace(" ", "").trim().uppercase()
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposTotp) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = NormalizadorDuplicados.armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "totp_${clave.hashCode()}",
                    tipo = TipoDuplicado.TOTP,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { NormalizadorDuplicados.esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 4. Detección de duplicados de la MISMA cuenta (mismo servicio y mismo usuario normalizado)
        // IMPORTANTE: Cuentas con usuarios diferentes NUNCA se agrupan aquí aunque compartan clave y servicio.
        val restantes = activas.filterNot { it.id in idsProcesados }
        val gruposPorCuenta = restantes
            .filter { it.usuario.isNotBlank() }
            .groupBy { entrada ->
                val serv = NormalizadorDuplicados.normalizarServicio(entrada)
                val usrNorm = NormalizadorDuplicados.normalizarUsuario(entrada.usuario, serv)
                "$serv|||$usrNorm"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposPorCuenta) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = NormalizadorDuplicados.armarClaveVisual(principal)
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
                    esAppAndroid = lista.any { NormalizadorDuplicados.esAppAndroid(it) }
                )
            )
            idsProcesados.addAll(lista.map { it.id })
        }

        // 3. Entradas sin usuario pero con misma contraseña y servicio (copias anónimas exactas)
        val restantesSinUsuario = activas.filterNot { it.id in idsProcesados }
        val gruposSinUsuario = restantesSinUsuario
            .filter { it.usuario.isBlank() && it.contrasena.isNotBlank() }
            .groupBy { entrada ->
                val serv = NormalizadorDuplicados.normalizarServicio(entrada)
                val pwd = entrada.contrasena
                "$serv|||__sin_usuario__|||$pwd"
            }
            .filter { it.value.size > 1 }

        for ((clave, lista) in gruposSinUsuario) {
            val principal = seleccionarMejorEntrada(lista)
            val claveVisual = NormalizadorDuplicados.armarClaveVisual(principal)
            resultado.add(
                GrupoDuplicado(
                    idGrupo = "anonimo_${clave.hashCode()}",
                    tipo = TipoDuplicado.IDENTICO,
                    claveVisual = claveVisual,
                    entradas = lista,
                    sugeridaPrincipal = principal,
                    esAppAndroid = lista.any { NormalizadorDuplicados.esAppAndroid(it) }
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
     * Delega la fusión segura de entradas secundarias a FusionadorEntradas
     */
    fun fusionar(principal: Entrada, secundarias: List<Entrada>): Entrada {
        return FusionadorEntradas.fusionar(principal, secundarias)
    }
}
