package com.jlnavas3.bovedalocal.data

import java.util.UUID

object FusionadorEntradas {

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
                            id = UUID.randomUUID().toString(),
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
