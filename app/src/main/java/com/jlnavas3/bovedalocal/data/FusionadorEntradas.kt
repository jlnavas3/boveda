/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

object FusionadorEntradas {

    /**
     * Fusiona las secundarias en la principal sin perder información valiosa:
     * - Si una es Passkey y otra tiene contraseña, la entrada resultante conserva AMBAS (Passkey + contraseña).
     * - Si alguna tiene 2FA (TOTP), se preserva.
     * - Combina notas, URLs no repetidas y campos personalizados adicionales.
     * - Guarda las contraseñas alternativas de las secundarias en el historial de contraseñas ("contraseñas anteriores").
     */
    fun fusionar(principal: Entrada, secundarias: List<Entrada>, usuarioElegido: String? = null): Entrada {
        val urlsCombinadas = com.jlnavas3.bovedalocal.util.DeduplicadorUrls.deduplicar(
            principal.urls + secundarias.flatMap { it.urls }
        )

        val camposCombinados = principal.camposPersonalizados.toMutableList()
        val nuevoHistorial = (principal.historialContrasenas + secundarias.flatMap { it.historialContrasenas }).toMutableList()

        val notasNuevas = StringBuilder(principal.notas.trim())

        // Si la principal no tiene contraseña (ej. era de tipo PASSKEY), pero una secundaria sí, adoptamos esa contraseña
        var contrasenaFinal = principal.contrasena
        if (contrasenaFinal.isBlank()) {
            val primeraConClave = secundarias.firstOrNull { it.contrasena.isNotBlank() }
            if (primeraConClave != null) {
                contrasenaFinal = primeraConClave.contrasena
            }
        }

        // Conservar Passkey si la principal no tiene pero alguna secundaria sí
        var passkeyFinal = principal.passkey ?: secundarias.mapNotNull { it.passkey }.firstOrNull()
        val passkeysSecundariasCandidatas = (principal.passkeysSecundarias + secundarias.flatMap { it.passkeysSecundarias }).toMutableList()

        // Conservar TOTP (2FA) si la principal no tiene pero alguna secundaria sí
        val secConTotp = secundarias.firstOrNull { !it.secretoTotp.isNullOrBlank() }
        var totpFinal = principal.secretoTotp ?: secConTotp?.secretoTotp
        var emisorFinal = principal.totpEmisor.ifBlank { secConTotp?.totpEmisor ?: "" }
        val digitosFinal = if (principal.secretoTotp != null) principal.totpDigitos else (secConTotp?.totpDigitos ?: 6)
        val periodoFinal = if (principal.secretoTotp != null) principal.totpPeriodo else (secConTotp?.totpPeriodo ?: 30)
        val algoritmoFinal = if (principal.secretoTotp != null) principal.totpAlgoritmo else (secConTotp?.totpAlgoritmo ?: "HmacSHA1")
        val totpsSecundariosCandidatos = (principal.totpsSecundarios + secundarias.flatMap { it.totpsSecundarios }).toMutableList()

        var clavesAlternativasGuardadas = 0

        for ((indice, sec) in secundarias.withIndex()) {
            val n = sec.notas.trim()
            if (n.isNotBlank() && !notasNuevas.contains(n)) {
                if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
                notasNuevas.append(n)
            }

            // Si la contraseña de la secundaria es distinta a la contraseña final, pasa a contraseñas anteriores
            if (sec.contrasena.isNotBlank() && sec.contrasena != contrasenaFinal) {
                clavesAlternativasGuardadas++
                if (nuevoHistorial.none { it.contrasena == sec.contrasena }) {
                    val momento = if (sec.modificadaEn > 0) sec.modificadaEn else System.currentTimeMillis()
                    nuevoHistorial.add(CambioContrasena(contrasena = sec.contrasena, cambiadaEn = momento))
                }
            }

            // Control de TOTP (2FA): descartar si es idéntico al activo, enriquecer metadatos si hace falta
            val secTotp = sec.secretoTotp?.trim()
            if (!secTotp.isNullOrBlank()) {
                if (totpFinal.isNullOrBlank()) {
                    totpFinal = secTotp
                    emisorFinal = sec.totpEmisor.ifBlank { sec.titulo }
                } else if (secTotp.equals(totpFinal, ignoreCase = true)) {
                    // Es exactamente el mismo código/secreto: se descarta de alternativas
                    if (emisorFinal.isBlank() && sec.totpEmisor.isNotBlank()) {
                        emisorFinal = sec.totpEmisor
                    }
                } else {
                    // Es un factor 2FA distinto: candidata para credenciales alternativas
                    val momento = if (sec.modificadaEn > 0) sec.modificadaEn else System.currentTimeMillis()
                    totpsSecundariosCandidatos.add(
                        TotpSecundario(
                            secreto = secTotp,
                            emisor = sec.totpEmisor.ifBlank { sec.titulo },
                            digitos = sec.totpDigitos,
                            periodo = sec.totpPeriodo,
                            algoritmo = sec.totpAlgoritmo,
                            agregadoEn = momento
                        )
                    )
                }
            }

            // Control de Passkeys: descartar si es exactamente la misma llave, enriquecer datos y solo conservar si es diferente
            if (sec.passkey != null) {
                val secPasskey = sec.passkey
                val momento = if (secPasskey.creadaEn > 0L) {
                    secPasskey.creadaEn
                } else if (sec.creadaEn > 0L) {
                    sec.creadaEn
                } else if (sec.modificadaEn > 0L) {
                    sec.modificadaEn
                } else {
                    System.currentTimeMillis()
                }
                val secConFecha = if (secPasskey.creadaEn > 0L) secPasskey else secPasskey.copy(creadaEn = momento)

                if (passkeyFinal == null) {
                    passkeyFinal = secConFecha
                } else if (sonMismaPasskey(passkeyFinal, secConFecha)) {
                    // Es exactamente la misma llave: se descarta de alternativas y se consolidan metadatos en la principal
                    passkeyFinal = consolidarPasskey(passkeyFinal, secConFecha)
                } else {
                    // Es una llave de acceso distinta: candidata para credenciales alternativas
                    passkeysSecundariasCandidatas.add(secConFecha)
                }
            }

            // Conservar campos personalizados legítimos de las secundarias que no existan en la principal
            for (c in sec.camposPersonalizados) {
                if (camposCombinados.none { it.etiqueta.equals(c.etiqueta, ignoreCase = true) }) {
                    camposCombinados.add(c)
                }
            }
        }

        // Consolidación y deduplicación estricta de Passkeys secundarias
        val passkeysSecundariasFinales = mutableListOf<DatosPasskey>()
        for (cand in passkeysSecundariasCandidatas) {
            if (sonMismaPasskey(passkeyFinal, cand)) {
                passkeyFinal = passkeyFinal?.let { consolidarPasskey(it, cand) } ?: cand
                continue
            }
            val indexExistente = passkeysSecundariasFinales.indexOfFirst { sonMismaPasskey(it, cand) }
            if (indexExistente >= 0) {
                passkeysSecundariasFinales[indexExistente] = consolidarPasskey(passkeysSecundariasFinales[indexExistente], cand)
            } else {
                passkeysSecundariasFinales.add(cand)
            }
        }

        // Consolidación y deduplicación estricta de TOTPs secundarios
        val totpsSecundariosFinales = mutableListOf<TotpSecundario>()
        for (cand in totpsSecundariosCandidatos) {
            if (cand.secreto.trim().equals(totpFinal?.trim(), ignoreCase = true)) {
                continue
            }
            val indexExistente = totpsSecundariosFinales.indexOfFirst { it.secreto.trim().equals(cand.secreto.trim(), ignoreCase = true) }
            if (indexExistente < 0) {
                totpsSecundariosFinales.add(cand)
            }
        }

        if (clavesAlternativasGuardadas > 0) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("[$clavesAlternativasGuardadas clave(s) alternativa(s) respaldada(s) en el historial de contraseñas anteriores al unificar].")
        }

        val fusionNativa = FusionadorSubmodelosNativos.fusionar(principal, secundarias)
        for (notaNativa in fusionNativa.notasDeRespaldo) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append(notaNativa)
        }

        val etiquetasCombinadas = (principal.etiquetas + secundarias.flatMap { it.etiquetas }).distinct()
        val tipoFinal = when {
            ExtractorClaveDuplicadoNativo.esTipoEspecializado(principal.tipo) -> principal.tipo
            principal.tipo == TipoEntrada.NOTA -> principal.tipo
            principal.tipo == TipoEntrada.CONTACTO -> principal.tipo
            contrasenaFinal.isNotBlank() -> TipoEntrada.LOGIN
            else -> principal.tipo
        }

        val usuarioFinal = if (!usuarioElegido.isNullOrBlank()) usuarioElegido else principal.usuario
        val usuariosAlternativos = (listOf(principal.usuario) + secundarias.map { it.usuario })
            .filter { it.isNotBlank() && it != usuarioFinal }
            .distinct()

        if (usuariosAlternativos.isNotEmpty()) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("Usuario(s) alternativo(s) respaldado(s) al unificar: ${usuariosAlternativos.joinToString(", ")}.")
        }

        val notasSanitizadas = limpiarNotasAutogeneradas(notasNuevas.toString())

        return principal.copy(
            usuario = usuarioFinal,
            tipo = tipoFinal,
            contrasena = contrasenaFinal,
            passkey = passkeyFinal,
            passkeysSecundarias = passkeysSecundariasFinales,
            secretoTotp = totpFinal,
            totpEmisor = emisorFinal,
            totpDigitos = digitosFinal,
            totpPeriodo = periodoFinal,
            totpAlgoritmo = algoritmoFinal,
            totpsSecundarios = totpsSecundariosFinales,
            urls = urlsCombinadas,
            notas = notasSanitizadas,
            camposPersonalizados = camposCombinados,
            datosTarjeta = fusionNativa.datosTarjeta,
            datosWifi = fusionNativa.datosWifi,
            datosCuentaBancaria = fusionNativa.datosCuentaBancaria,
            datosIdentidad = fusionNativa.datosIdentidad,
            datosServidor = fusionNativa.datosServidor,
            datosWallet = fusionNativa.datosWallet,
            historialContrasenas = nuevoHistorial.distinctBy { it.contrasena }.filterNot { it.contrasena == contrasenaFinal },
            etiquetas = etiquetasCombinadas,
            modificadaEn = System.currentTimeMillis()
        )
    }

    /**
     * Determina si dos llaves de acceso representan criptográfica y técnicamente la misma credencial WebAuthn.
     */
    fun sonMismaPasskey(p1: DatosPasskey?, p2: DatosPasskey?): Boolean {
        if (p1 == null || p2 == null) return false
        // 1. Mismo ID de credencial WebAuthn (Base64Url)
        if (p1.credId.isNotBlank() && p2.credId.isNotBlank() && p1.credId.equals(p2.credId, ignoreCase = true)) {
            return true
        }
        // 2. Misma clave privada
        if (p1.clavePrivada.isNotBlank() && p2.clavePrivada.isNotBlank() && p1.clavePrivada == p2.clavePrivada) {
            return true
        }
        // 3. Mismo rpId y userHandle no vacío
        if (p1.rpId.isNotBlank() && p2.rpId.isNotBlank() && p1.rpId.equals(p2.rpId, ignoreCase = true) &&
            p1.userHandle.isNotBlank() && p2.userHandle.isNotBlank() && p1.userHandle == p2.userHandle) {
            return true
        }
        return false
    }

    /**
     * Enriquece la passkey base con metadatos adicionales más completos provenientes de otra instancia idéntica
     * (por ejemplo, si una tenía el nombre amigable de la app o el nombre de usuario y la otra no).
     */
    fun consolidarPasskey(base: DatosPasskey, otra: DatosPasskey): DatosPasskey {
        return base.copy(
            usuario = base.usuario.ifBlank { otra.usuario },
            rpName = if (base.rpName.isNotBlank() && !base.rpName.equals(base.rpId, ignoreCase = true)) base.rpName else otra.rpName.ifBlank { base.rpName },
            userHandle = base.userHandle.ifBlank { otra.userHandle },
            creadaEn = if (base.creadaEn > 0L) base.creadaEn else otra.creadaEn
        )
    }

    /**
     * Elimina notas de respaldo de passkey en texto plano generadas por versiones anteriores
     * para no ensuciar las notas del usuario, ya que las passkeys ahora se guardan estructuradas.
     */
    fun limpiarNotasAutogeneradas(texto: String): String {
        if (texto.isBlank()) return ""
        return texto
            .replace(Regex("""\[Passkey secundario respaldado de [^\]]+\]"""), "")
            .split(Regex("""(?:\r?\n){0,2}---\s*(?:\r?\n){1,2}"""))
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n\n---\n")
            .trim()
    }
}
