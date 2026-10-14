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
        val passkeyFinal = principal.passkey ?: secundarias.mapNotNull { it.passkey }.firstOrNull()

        // Conservar TOTP (2FA) si la principal no tiene pero alguna secundaria sí
        val secConTotp = secundarias.firstOrNull { !it.secretoTotp.isNullOrBlank() }
        val totpFinal = principal.secretoTotp ?: secConTotp?.secretoTotp
        val emisorFinal = principal.totpEmisor.ifBlank { secConTotp?.totpEmisor ?: "" }
        val digitosFinal = if (principal.secretoTotp != null) principal.totpDigitos else (secConTotp?.totpDigitos ?: 6)
        val periodoFinal = if (principal.secretoTotp != null) principal.totpPeriodo else (secConTotp?.totpPeriodo ?: 30)
        val algoritmoFinal = if (principal.secretoTotp != null) principal.totpAlgoritmo else (secConTotp?.totpAlgoritmo ?: "HmacSHA1")

        var clavesAlternativasGuardadas = 0
        var totpsAlternativosGuardados = 0
        var passkeysAlternativosGuardados = 0

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

            // Conservar TOTP secundario si es distinto al final
            val secTotp = sec.secretoTotp?.trim()
            if (!secTotp.isNullOrBlank() && secTotp != totpFinal) {
                totpsAlternativosGuardados++
                val etiquetaTotp = if (sec.titulo.isNotBlank() && sec.titulo != principal.titulo) {
                    "TOTP Alternativo (${sec.titulo})"
                } else if (totpsAlternativosGuardados > 1) {
                    "TOTP Alternativo $totpsAlternativosGuardados"
                } else {
                    "TOTP Alternativo"
                }
                if (camposCombinados.none { it.valor == secTotp }) {
                    camposCombinados.add(
                        CampoPersonalizado(
                            id = java.util.UUID.randomUUID().toString(),
                            etiqueta = etiquetaTotp,
                            valor = secTotp,
                            esSensible = true,
                            tipo = TipoCampo.TEXTO
                        )
                    )
                }
            }

            // Respaldo de Passkey alternativo si difiere del final
            if (sec.passkey != null && sec.passkey != passkeyFinal) {
                passkeysAlternativosGuardados++
                if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
                notasNuevas.append("[Passkey secundario respaldado de '${sec.titulo}': Usuario=${sec.passkey.usuario.ifBlank { sec.passkey.userHandle }}, CredId=${sec.passkey.credId}]")
            }

            // Conservar campos personalizados legítimos de las secundarias que no existan en la principal
            for (c in sec.camposPersonalizados) {
                if (camposCombinados.none { it.etiqueta.equals(c.etiqueta, ignoreCase = true) }) {
                    camposCombinados.add(c)
                }
            }
        }

        if (clavesAlternativasGuardadas > 0) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("[$clavesAlternativasGuardadas clave(s) alternativa(s) respaldada(s) en el historial de contraseñas anteriores al unificar].")
        }
        if (totpsAlternativosGuardados > 0) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("[$totpsAlternativosGuardados código(s) TOTP alternativo(s) guardado(s) en campos personalizados al unificar cuentas].")
        }

        val etiquetasCombinadas = (principal.etiquetas + secundarias.flatMap { it.etiquetas }).distinct()
        val tipoFinal = if (contrasenaFinal.isNotBlank()) TipoEntrada.LOGIN else principal.tipo

        val usuarioFinal = if (!usuarioElegido.isNullOrBlank()) usuarioElegido else principal.usuario
        val usuariosAlternativos = (listOf(principal.usuario) + secundarias.map { it.usuario })
            .filter { it.isNotBlank() && it != usuarioFinal }
            .distinct()

        if (usuariosAlternativos.isNotEmpty()) {
            if (notasNuevas.isNotEmpty()) notasNuevas.append("\n\n---\n")
            notasNuevas.append("Usuario(s) alternativo(s) respaldado(s) al unificar: ${usuariosAlternativos.joinToString(", ")}.")
        }

        return principal.copy(
            usuario = usuarioFinal,
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
