/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.GrupoTitulosSitio
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios
import kotlinx.coroutines.flow.MutableStateFlow

interface VaultTitulosDelegate {
    val repositorio: VaultRepository
    val gruposTitulosInterno: MutableStateFlow<List<GrupoTitulosSitio>>
    val modoFormatoTitulosInterno: MutableStateFlow<ModoFormatoTitulos>
    val respetarTitulosManualesInterno: MutableStateFlow<Boolean>
    val esSeleccionEspecificaTitulosInterno: MutableStateFlow<Boolean>
    fun ejecutar(bloque: suspend () -> Unit)
    fun avisar(texto: String)

    fun prepararNormalizacionTitulos(entradasEspecificas: List<Entrada>? = null) {
        if (repositorio.ajustes.actual.prefijosSubdominios.any { it.equals("acorn", ignoreCase = true) }) {
            eliminarPrefijoSubdominio("acorn")
        }
        val ajustes = repositorio.ajustes.actual
        val modo = ModoFormatoTitulos.desde(ajustes.formatoColisionTitulos)
        modoFormatoTitulosInterno.value = modo
        respetarTitulosManualesInterno.value = ajustes.respetarTitulosPersonalizados
        esSeleccionEspecificaTitulosInterno.value = entradasEspecificas != null

        val entradas = entradasEspecificas ?: when (val estado = repositorio.estado.value) {
            is EstadoBoveda.Desbloqueada -> estado.entradas
            else -> emptyList()
        }

        val grupos = mutableMapOf<String, MutableList<Entrada>>()
        for (e in entradas) {
            val primeraUrl = e.urls.firstOrNull() ?: ""
            val nombreBase = NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = primeraUrl,
                rawTitulo = e.titulo,
                prefijosConfigurados = ajustes.prefijosSubdominios,
                plantillaRouter = ajustes.plantillaRouterIp,
                plantillaServidor = ajustes.plantillaServidorIp,
                tldsConfigurados = ajustes.tldsDescartables,
                marcasPersonalizadas = ajustes.marcasPersonalizadas,
                puertosConfigurados = ajustes.puertosServiciosLocales,
                octetosRouter = ajustes.octetosRouter
            )
            val clave = nombreBase.lowercase()
            grupos.getOrPut(clave) { mutableListOf() }.add(e)
        }

        val resultadoGrupos = grupos.map { (claveAgrupacion, lista) ->
            val primeraUrl = lista.firstOrNull { it.urls.isNotEmpty() }?.urls?.firstOrNull() ?: ""
            val primerTitulo = lista.firstOrNull()?.titulo ?: ""
            val nombreBase = NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = primeraUrl,
                rawTitulo = primerTitulo,
                prefijosConfigurados = ajustes.prefijosSubdominios,
                plantillaRouter = ajustes.plantillaRouterIp,
                plantillaServidor = ajustes.plantillaServidorIp,
                tldsConfigurados = ajustes.tldsDescartables,
                marcasPersonalizadas = ajustes.marcasPersonalizadas,
                puertosConfigurados = ajustes.puertosServiciosLocales,
                octetosRouter = ajustes.octetosRouter
            )
            val domMostrar = if (primeraUrl.isNotBlank()) {
                val host = Dominios.host(primeraUrl)
                if (host.isNotBlank()) {
                    val conPuerto = primeraUrl.substringAfter("://").substringBefore('/').removePrefix("www.")
                    if (conPuerto.contains(':')) conPuerto else host
                } else {
                    primeraUrl
                }
            } else {
                primerTitulo
            }
            GrupoTitulosSitio(
                id = claveAgrupacion,
                dominioClave = domMostrar,
                nombreSugerido = nombreBase,
                nombrePersonalizado = nombreBase,
                entradas = lista,
                tieneColision = lista.size > 1
            )
        }.sortedBy { it.nombreEfectivo.lowercase() }

        gruposTitulosInterno.value = resultadoGrupos
    }

    fun esGrupoComprometido(grupo: GrupoTitulosSitio): Boolean {
        val tlds = repositorio.ajustes.actual.tldsDescartables
        return grupo.entradas.any { e ->
            com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(e, tlds)
        }
    }

    fun actualizarNombreGrupo(idGrupo: String, nuevoNombre: String) {
        gruposTitulosInterno.value = gruposTitulosInterno.value.map { g ->
            if (g.id == idGrupo) g.copy(nombrePersonalizado = nuevoNombre) else g
        }
    }

    fun cambiarModoFormatoTitulos(nuevoModo: ModoFormatoTitulos) {
        modoFormatoTitulosInterno.value = nuevoModo
        repositorio.ajustes.actualizar { it.copy(formatoColisionTitulos = nuevoModo.name) }
    }

    fun cambiarRespetarTitulosManuales(respetar: Boolean) {
        respetarTitulosManualesInterno.value = respetar
        repositorio.ajustes.actualizar { it.copy(respetarTitulosPersonalizados = respetar) }
    }

    fun aplicarNormalizacionTitulos(alTerminar: () -> Unit) {
        ejecutar {
            val grupos = gruposTitulosInterno.value
            val modo = modoFormatoTitulosInterno.value
            val respetarManuales = respetarTitulosManualesInterno.value
            val esSeleccionManual = esSeleccionEspecificaTitulosInterno.value
            val tldsActuales = repositorio.ajustes.actual.tldsDescartables

            var modificadas = 0
            val mapaReemplazo = mutableMapOf<String, Entrada>()

            for (grupo in grupos) {
                for (entrada in grupo.entradas) {
                    val esCambioManualGrupo = grupo.nombrePersonalizado != grupo.nombreSugerido
                    if (!esSeleccionManual && !esCambioManualGrupo && respetarManuales && !NormalizadorTitulosSitios.esTituloGeneradoOModificable(
                            titulo = entrada.titulo,
                            nombreBase = grupo.nombreEfectivo,
                            usuario = entrada.usuario,
                            urls = entrada.urls,
                            tldsConfigurados = tldsActuales
                        )
                    ) {
                        continue
                    }
                    val tituloFinal = NormalizadorTitulosSitios.generarTituloFinal(
                        nombreBase = grupo.nombreEfectivo,
                        usuario = entrada.usuario,
                        modo = modo,
                        tieneColision = grupo.tieneColision,
                        forzarFormato = esSeleccionManual
                    )
                    if (tituloFinal != entrada.titulo) {
                        mapaReemplazo[entrada.id] = entrada.copy(
                            titulo = tituloFinal,
                            modificadaEn = System.currentTimeMillis()
                        )
                        modificadas++
                    }
                }
            }

            if (mapaReemplazo.isNotEmpty()) {
                val entradasActuales = when (val estado = repositorio.estado.value) {
                    is EstadoBoveda.Desbloqueada -> estado.entradas
                    else -> emptyList()
                }
                val nuevas = entradasActuales.map { e -> mapaReemplazo[e.id] ?: e }
                repositorio.sincronizarLote(entradasAGuardar = nuevas)
            }

            avisar("Se han actualizado $modificadas títulos")
            alTerminar()
        }
    }

    fun estandarizarTitulosRapido(alTerminar: () -> Unit = {}) {
        ejecutar {
            val ajustes = repositorio.ajustes.actual
            val modo = ModoFormatoTitulos.desde(ajustes.formatoColisionTitulos)
            val tldsActuales = ajustes.tldsDescartables
            val entradasActuales = when (val estado = repositorio.estado.value) {
                is EstadoBoveda.Desbloqueada -> estado.entradas
                else -> emptyList()
            }

            val gruposPorSitio = entradasActuales.groupBy { e ->
                val u = e.urls.firstOrNull() ?: ""
                NormalizadorTitulosSitios.extraerNombreBase(
                    urlODominio = u,
                    rawTitulo = e.titulo,
                    prefijosConfigurados = ajustes.prefijosSubdominios,
                    plantillaRouter = ajustes.plantillaRouterIp,
                    plantillaServidor = ajustes.plantillaServidorIp,
                    tldsConfigurados = tldsActuales,
                    marcasPersonalizadas = ajustes.marcasPersonalizadas,
                    puertosConfigurados = ajustes.puertosServiciosLocales,
                    octetosRouter = ajustes.octetosRouter
                ).lowercase()
            }

            val comprometidas = entradasActuales.filter { entrada ->
                if (!com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(entrada, tldsActuales)) {
                    return@filter false
                }
                val primeraUrl = entrada.urls.firstOrNull() ?: ""
                val nombreBase = NormalizadorTitulosSitios.extraerNombreBase(
                    urlODominio = primeraUrl,
                    rawTitulo = entrada.titulo,
                    prefijosConfigurados = ajustes.prefijosSubdominios,
                    plantillaRouter = ajustes.plantillaRouterIp,
                    plantillaServidor = ajustes.plantillaServidorIp,
                    tldsConfigurados = tldsActuales,
                    marcasPersonalizadas = ajustes.marcasPersonalizadas,
                    puertosConfigurados = ajustes.puertosServiciosLocales,
                    octetosRouter = ajustes.octetosRouter
                )
                val tieneColision = (gruposPorSitio[nombreBase.lowercase()]?.size ?: 1) > 1
                val tituloFinal = NormalizadorTitulosSitios.generarTituloFinal(
                    nombreBase = nombreBase,
                    usuario = entrada.usuario,
                    modo = modo,
                    tieneColision = tieneColision
                )
                tituloFinal != entrada.titulo
            }

            if (comprometidas.isEmpty()) {
                avisar("No hay títulos pendientes por estandarizar")
                alTerminar()
                return@ejecutar
            }

            val mapaReemplazo = mutableMapOf<String, Entrada>()
            var modificadas = 0
            val totalComprometidas = comprometidas.size

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "renombrar_titulos_masivo",
                titulo = "Renombrando títulos",
                faseInicial = "Normalizando nombres no amigables...",
                totalEstimado = totalComprometidas
            )

            for ((indice, entrada) in comprometidas.withIndex()) {
                val primeraUrl = entrada.urls.firstOrNull() ?: ""
                val nombreBase = NormalizadorTitulosSitios.extraerNombreBase(
                    urlODominio = primeraUrl,
                    rawTitulo = entrada.titulo,
                    prefijosConfigurados = ajustes.prefijosSubdominios,
                    plantillaRouter = ajustes.plantillaRouterIp,
                    plantillaServidor = ajustes.plantillaServidorIp,
                    tldsConfigurados = tldsActuales,
                    marcasPersonalizadas = ajustes.marcasPersonalizadas,
                    puertosConfigurados = ajustes.puertosServiciosLocales,
                    octetosRouter = ajustes.octetosRouter
                )
                val tieneColision = (gruposPorSitio[nombreBase.lowercase()]?.size ?: 1) > 1
                val tituloFinal = NormalizadorTitulosSitios.generarTituloFinal(
                    nombreBase = nombreBase,
                    usuario = entrada.usuario,
                    modo = modo,
                    tieneColision = tieneColision
                )
                if (tituloFinal != entrada.titulo) {
                    val actualizada = entrada.copy(
                        titulo = tituloFinal,
                        modificadaEn = System.currentTimeMillis()
                    )
                    mapaReemplazo[entrada.id] = actualizada
                    modificadas++

                    val progreso = (indice + 1).toFloat() / totalComprometidas
                    com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                        item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                            id = entrada.id,
                            titulo = "${entrada.titulo} → $tituloFinal",
                            subtitulo = entrada.usuario,
                            progreso = 1f,
                            completado = true
                        ),
                        progresoGlobal = progreso,
                        procesadas = modificadas
                    )
                }
            }

            if (mapaReemplazo.isNotEmpty()) {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Escribiendo bóveda cifrada...", 0.95f)
                val nuevas = entradasActuales.map { e -> mapaReemplazo[e.id] ?: e }
                repositorio.sincronizarLote(entradasAGuardar = nuevas)
            }

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                mensajeExito = "Se han estandarizado $modificadas títulos",
                totalCuentas = modificadas,
                textoAccionResultado = null,
                textoAccionSecundaria = "Ver listado"
            )

            avisar("¡Listo! Se han estandarizado $modificadas títulos")
            prepararNormalizacionTitulos()
            alTerminar()
        }
    }

    fun obtenerEjemplosNormalizacion(limite: Int = 4): List<com.jlnavas3.bovedalocal.data.EjemploNormalizacion> {
        val ajustes = repositorio.ajustes.actual
        val modo = ModoFormatoTitulos.desde(ajustes.formatoColisionTitulos)
        val tldsActuales = ajustes.tldsDescartables
        val entradasActuales = when (val estado = repositorio.estado.value) {
            is EstadoBoveda.Desbloqueada -> estado.entradas
            else -> emptyList()
        }

        val gruposPorSitio = entradasActuales.groupBy { e ->
            val u = e.urls.firstOrNull() ?: ""
            NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = u,
                rawTitulo = e.titulo,
                prefijosConfigurados = ajustes.prefijosSubdominios,
                plantillaRouter = ajustes.plantillaRouterIp,
                plantillaServidor = ajustes.plantillaServidorIp,
                tldsConfigurados = tldsActuales,
                marcasPersonalizadas = ajustes.marcasPersonalizadas,
                puertosConfigurados = ajustes.puertosServiciosLocales,
                octetosRouter = ajustes.octetosRouter
            ).lowercase()
        }

        return entradasActuales.asSequence().filter { entrada ->
            com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(entrada, tldsActuales)
        }.mapNotNull { entrada ->
            val primeraUrl = entrada.urls.firstOrNull() ?: ""
            val nombreBase = NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = primeraUrl,
                rawTitulo = entrada.titulo,
                prefijosConfigurados = ajustes.prefijosSubdominios,
                plantillaRouter = ajustes.plantillaRouterIp,
                plantillaServidor = ajustes.plantillaServidorIp,
                tldsConfigurados = tldsActuales,
                marcasPersonalizadas = ajustes.marcasPersonalizadas,
                puertosConfigurados = ajustes.puertosServiciosLocales,
                octetosRouter = ajustes.octetosRouter
            )
            val tieneColision = (gruposPorSitio[nombreBase.lowercase()]?.size ?: 1) > 1
            val tituloFinal = NormalizadorTitulosSitios.generarTituloFinal(
                nombreBase = nombreBase,
                usuario = entrada.usuario,
                modo = modo,
                tieneColision = tieneColision
            )
            if (tituloFinal != entrada.titulo) {
                com.jlnavas3.bovedalocal.data.EjemploNormalizacion(
                    id = entrada.id,
                    tituloOriginal = entrada.titulo.ifBlank { "Sin título" },
                    tituloSugerido = tituloFinal,
                    subtitulo = entrada.usuario.ifBlank { primeraUrl }
                )
            } else null
        }.take(limite).toList()
    }

    fun agregarPrefijoSubdominio(prefijo: String) {
        val limpio = prefijo.trim().lowercase().removePrefix(".").removeSuffix(".")
        if (limpio.isNotBlank()) {
            if (gruposTitulosInterno.value.any { it.nombreEfectivo.equals(limpio, ignoreCase = true) }) {
                return
            }
            repositorio.ajustes.actualizar {
                if (!it.prefijosSubdominios.contains(limpio)) {
                    it.copy(prefijosSubdominios = it.prefijosSubdominios + limpio)
                } else it
            }
        }
    }

    fun alternarPrefijoSubdominio(prefijo: String) {
        val limpio = prefijo.trim().lowercase().removePrefix(".").removeSuffix(".")
        if (limpio.isBlank()) return
        if (gruposTitulosInterno.value.any { it.nombreEfectivo.equals(limpio, ignoreCase = true) }) {
            eliminarPrefijoSubdominio(limpio)
            return
        }
        val actuales = repositorio.ajustes.actual.prefijosSubdominios
        if (actuales.any { it.equals(limpio, ignoreCase = true) }) {
            eliminarPrefijoSubdominio(limpio)
        } else {
            agregarPrefijoSubdominio(limpio)
        }
    }

    fun agregarPrefijosSubdominios(prefijos: List<String>) {
        val nombresActivos = gruposTitulosInterno.value.map { it.nombreEfectivo.lowercase() }.toSet()
        val limpios = prefijos.map { it.trim().lowercase().removePrefix(".").removeSuffix(".") }
            .filter { it.isNotBlank() && !nombresActivos.contains(it) }
        if (limpios.isEmpty()) return
        repositorio.ajustes.actualizar {
            val nuevos = (it.prefijosSubdominios + limpios).distinct()
            it.copy(prefijosSubdominios = nuevos)
        }
    }

    fun eliminarPrefijoSubdominio(prefijo: String) {
        repositorio.ajustes.actualizar {
            it.copy(prefijosSubdominios = it.prefijosSubdominios.filterNot { p -> p.equals(prefijo, ignoreCase = true) })
        }
    }

    fun agregarTldDescartable(tld: String) {
        val limpio = tld.trim().lowercase().removePrefix(".").removeSuffix(".")
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.tldsDescartables.contains(limpio)) {
                    it.copy(tldsDescartables = it.tldsDescartables + limpio)
                } else it
            }
        }
    }

    fun eliminarTldDescartable(tld: String) {
        val limpio = tld.trim().lowercase().removePrefix(".").removeSuffix(".")
        repositorio.ajustes.actualizar {
            it.copy(tldsDescartables = it.tldsDescartables.filterNot { t -> t.equals(limpio, ignoreCase = true) })
        }
    }

    fun agregarMarcaPersonalizada(dominioOClave: String, nombreFormateado: String) {
        val claveLimpia = dominioOClave.trim().lowercase().removePrefix("https://").removePrefix("http://").removePrefix("www.").substringBefore('/')
        val nombreLimpio = nombreFormateado.trim()
        if (claveLimpia.isNotBlank() && nombreLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                it.copy(marcasPersonalizadas = it.marcasPersonalizadas + (claveLimpia to nombreLimpio))
            }
        }
    }

    fun eliminarMarcaPersonalizada(dominioOClave: String) {
        val claveLimpia = dominioOClave.trim().lowercase()
        repositorio.ajustes.actualizar {
            it.copy(marcasPersonalizadas = it.marcasPersonalizadas.filterKeys { k -> !k.equals(claveLimpia, ignoreCase = true) })
        }
    }

    fun agregarPuertoServicio(puerto: String, nombreServicio: String) {
        val puertoLimpio = puerto.trim().removePrefix(":")
        val servicioLimpio = nombreServicio.trim()
        if (puertoLimpio.isNotBlank() && servicioLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                it.copy(puertosServiciosLocales = it.puertosServiciosLocales + (puertoLimpio to servicioLimpio))
            }
        }
    }

    fun eliminarPuertoServicio(puerto: String) {
        val puertoLimpio = puerto.trim().removePrefix(":")
        repositorio.ajustes.actualizar {
            it.copy(puertosServiciosLocales = it.puertosServiciosLocales.filterKeys { k -> k != puertoLimpio })
        }
    }

    fun agregarOctetoRouter(octeto: Int) {
        if (octeto in 1..254) {
            repositorio.ajustes.actualizar {
                if (!it.octetosRouter.contains(octeto)) {
                    it.copy(octetosRouter = (it.octetosRouter + octeto).sorted())
                } else it
            }
        }
    }

    fun eliminarOctetoRouter(octeto: Int) {
        repositorio.ajustes.actualizar {
            it.copy(octetosRouter = it.octetosRouter.filter { o -> o != octeto })
        }
    }

    fun restablecerReglasNormalizacion() {
        repositorio.ajustes.actualizar {
            it.copy(
                prefijosSubdominios = AjustesDefaults.NormalizacionTitulos.PREFIJOS_SUBDOMINIOS,
                tldsDescartables = AjustesDefaults.NormalizacionTitulos.TLDS_DESCARTABLES,
                marcasPersonalizadas = AjustesDefaults.NormalizacionTitulos.MARCAS_PERSONALIZADAS,
                puertosServiciosLocales = AjustesDefaults.NormalizacionTitulos.PUERTOS_SERVICIOS_LOCALES,
                octetosRouter = AjustesDefaults.NormalizacionTitulos.OCTETOS_ROUTER,
                plantillaRouterIp = AjustesDefaults.NormalizacionTitulos.PLANTILLA_ROUTER_IP,
                plantillaServidorIp = AjustesDefaults.NormalizacionTitulos.PLANTILLA_SERVIDOR_IP,
                formatoColisionTitulos = AjustesDefaults.NormalizacionTitulos.FORMATO_COLISION_TITULOS,
                respetarTitulosPersonalizados = AjustesDefaults.NormalizacionTitulos.RESPETAR_TITULOS_PERSONALIZADOS
            )
        }
    }
}
