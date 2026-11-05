/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.data.CategoriaAvisoInterno
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla

/**
 * Calculador de recomendaciones y avisos internos de la bóveda.
 * Se ejecuta en segundo plano (Dispatchers.Default) para no bloquear la interfaz.
 */
object CalculadorAvisosInternos {

    fun esTituloComprometido(entrada: Entrada, tlds: List<String> = emptyList()): Boolean {
        val t = entrada.titulo.trim()
        if (t.isBlank()) return true
        val tBase = t.substringBefore('(').substringBefore('·').substringBefore('[').trim()
        if (tBase.isBlank()) return true
        if (tBase.equals("localhost", ignoreCase = true) || tBase.startsWith("localhost :", ignoreCase = true)) return false
        if (tBase.startsWith("http://", ignoreCase = true) ||
            tBase.startsWith("https://", ignoreCase = true) ||
            tBase.startsWith("android://", ignoreCase = true) ||
            tBase.startsWith("androidapp://", ignoreCase = true) ||
            tBase.startsWith("app://", ignoreCase = true)
        ) return true
        if (LanzadorEnlaces.esNombrePaquete(tBase) || LanzadorEnlaces.extraerPaquete(tBase) != null) return true
        if (!tBase.contains(' ') && (tBase.contains('/') || tBase.contains('?') || tBase.contains('#'))) return true
        if (tBase.contains(Regex("""(?<!\s):\d{2,5}"""))) return true
        if (tBase.matches(Regex("""^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}.*"""))) return true
        val tldList = if (tlds.isNotEmpty()) tlds else AjustesDefaults.NormalizacionTitulos.TLDS_DESCARTABLES
        val tldsConPunto = tldList.map { if (it.startsWith(".")) it.lowercase() else ".${it.lowercase()}" }
        if (tldsConPunto.any { tBase.endsWith(it, ignoreCase = true) }) return true
        if (!tBase.contains(' ') && tBase.contains('.')) {
            val esDominioValido = runCatching {
                val idn = com.google.common.net.InternetDomainName.from(tBase)
                idn.hasPublicSuffix()
            }.getOrDefault(false)
            if (esDominioValido) return true
        }
        val hostCoincidente = entrada.urls.firstOrNull { u ->
            val h = Dominios.host(u)
            h.isNotBlank() && h.contains('.') && (h.equals(tBase, ignoreCase = true) || h.removePrefix("www.").equals(tBase, ignoreCase = true))
        }
        return hostCoincidente != null
    }

    fun calcular(
        entradas: List<Entrada>,
        ajustes: AjustesApp,
        archivoBovedaModificado: Long,
        ahora: Long = System.currentTimeMillis(),
        contexto: android.content.Context? = null
    ): List<AvisoInterno> {
        val ignorados = ajustes.avisosIgnorados
        val resultado = mutableListOf<AvisoInterno>()

        // 1. Títulos sin normalizar / comprometidos
        if (ajustes.avisarTitulosSinNormalizar && TipoAvisoInterno.TITULOS_SIN_NORMALIZAR.name !in ignorados) {
            val totalComprometidos = entradas.count { esTituloComprometido(it, ajustes.tldsDescartables) }
            if (totalComprometidos > 0) {
                resultado.add(
                    AvisoInterno(
                        tipo = TipoAvisoInterno.TITULOS_SIN_NORMALIZAR,
                        categoria = CategoriaAvisoInterno.ORGANIZACION,
                        titulo = "Nombres no amigables",
                        descripcion = "Detectamos $totalComprometidos ${if (totalComprometidos == 1) "título con formato de enlace o paquete" else "títulos con formato de enlace o paquete"}.",
                        accionTexto = "Normalizar títulos",
                        pantallaDestino = Pantalla.NormalizadorTitulos,
                        cantidadAfectados = totalComprometidos
                    )
                )
            }
        }

        // 2. Duplicados
        if (ajustes.avisarDuplicados && TipoAvisoInterno.DUPLICADOS_DETECTADOS.name !in ignorados) {
            val gruposDup = AnalizadorDuplicados.analizar(entradas)
            val totalDup = gruposDup.sumOf { it.entradasSecundarias.size }
            if (totalDup > 0) {
                resultado.add(
                    AvisoInterno(
                        tipo = TipoAvisoInterno.DUPLICADOS_DETECTADOS,
                        categoria = CategoriaAvisoInterno.ORGANIZACION,
                        titulo = "Credenciales duplicadas",
                        descripcion = "Tienes $totalDup ${if (totalDup == 1) "cuenta potencialmente repetida" else "cuentas potencialmente repetidas"}.",
                        accionTexto = "Resolver duplicados",
                        pantallaDestino = Pantalla.Duplicados,
                        cantidadAfectados = totalDup
                    )
                )
            }
        }

        // 3. Salud de contraseñas
        if (ajustes.avisarSaludContrasenas && TipoAvisoInterno.CONTRASENAS_VULNERABLES.name !in ignorados) {
            val totalVulnerables = com.jlnavas3.bovedalocal.ui.pantallas.salud.contarProblemasSaludBoveda(
                entradas = entradas,
                contexto = contexto,
                anadidasComunes = ajustes.contrasenasComunesAnadidas,
                excluidasComunes = ajustes.contrasenasComunesExcluidas
            )
            if (totalVulnerables > 0) {
                resultado.add(
                    AvisoInterno(
                        tipo = TipoAvisoInterno.CONTRASENAS_VULNERABLES,
                        categoria = CategoriaAvisoInterno.SEGURIDAD,
                        titulo = "Auditoría de seguridad",
                        descripcion = "Detectamos $totalVulnerables ${if (totalVulnerables == 1) "contraseña vulnerable o repetida" else "contraseñas vulnerables o repetidas"}.",
                        accionTexto = "Revisar salud",
                        pantallaDestino = Pantalla.SaludBoveda,
                        cantidadAfectados = totalVulnerables
                    )
                )
            }
        }

        // 4. Respaldo pendiente
        if (ajustes.avisarRespaldoPendiente && TipoAvisoInterno.RESPALDO_PENDIENTE.name !in ignorados && ajustes.recordatorioExportacionDias != 0) {
            val config = ajustes.recordatorioExportacionDias
            val umbralMs = when (config) {
                -30 -> 30L * 60 * 1000
                in 1..Int.MAX_VALUE -> config.toLong() * 24 * 60 * 60 * 1000
                else -> 30L * 24 * 60 * 60 * 1000
            }
            val ultimaCopia = maxOf(ajustes.ultimaExportacionEn, ajustes.backupAutoUltimaEjecucion)
            if (ultimaCopia > 0L) {
                val transcurrido = ahora - ultimaCopia
                if (transcurrido >= umbralMs) {
                    val desc = if (config == -30) {
                        val minutos = (transcurrido / (60 * 1000)).coerceAtLeast(30)
                        if (minutos < 60) {
                            "Han pasado $minutos minutos desde tu última copia de seguridad (Modo de prueba)."
                        } else {
                            val horas = minutos / 60
                            "Han pasado $horas ${if (horas == 1L) "hora" else "horas"} desde tu última copia de seguridad."
                        }
                    } else {
                        val dias = (transcurrido / (24L * 60 * 60 * 1000)).coerceAtLeast(1)
                        "Han pasado $dias ${if (dias == 1L) "día" else "días"} desde tu última copia de seguridad."
                    }
                    resultado.add(
                        AvisoInterno(
                            tipo = TipoAvisoInterno.RESPALDO_PENDIENTE,
                            categoria = CategoriaAvisoInterno.MANTENIMIENTO,
                            titulo = "Copia de seguridad pendiente",
                            descripcion = desc,
                            accionTexto = "Hacer copia",
                            pantallaDestino = Pantalla.CopiaSeguridad("05-COP-MAN"),
                            cantidadAfectados = 1
                        )
                    )
                }
            } else {
                val tiempoBase = if (ajustes.fechaCreacionBovedaLocal > 0L) {
                    ajustes.fechaCreacionBovedaLocal
                } else if (archivoBovedaModificado > 0L) {
                    archivoBovedaModificado
                } else {
                    ahora
                }
                val transcurrido = ahora - tiempoBase
                if (entradas.isNotEmpty() && transcurrido >= umbralMs) {
                    resultado.add(
                        AvisoInterno(
                            tipo = TipoAvisoInterno.RESPALDO_PENDIENTE,
                            categoria = CategoriaAvisoInterno.MANTENIMIENTO,
                            titulo = "Primera copia recomendada",
                            descripcion = "Aún no has respaldado tu bóveda local. Te recomendamos crear una copia cifrada.",
                            accionTexto = "Crear respaldo",
                            pantallaDestino = Pantalla.CopiaSeguridad("05-COP-MAN"),
                            cantidadAfectados = 1
                        )
                    )
                }
            }
        }

        return resultado
    }
}
