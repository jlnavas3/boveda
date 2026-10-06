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
    fun ejecutar(bloque: suspend () -> Unit)
    fun avisar(texto: String)

    fun prepararNormalizacionTitulos(entradasEspecificas: List<Entrada>? = null) {
        val ajustes = repositorio.ajustes.actual
        val modo = ModoFormatoTitulos.desde(ajustes.formatoColisionTitulos)
        modoFormatoTitulosInterno.value = modo
        respetarTitulosManualesInterno.value = ajustes.respetarTitulosPersonalizados

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
            val tldsActuales = repositorio.ajustes.actual.tldsDescartables

            var modificadas = 0
            val mapaReemplazo = mutableMapOf<String, Entrada>()

            for (grupo in grupos) {
                for (entrada in grupo.entradas) {
                    if (respetarManuales && !NormalizadorTitulosSitios.esTituloGeneradoOModificable(
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
                        tieneColision = grupo.tieneColision
                    )
                    if (tituloFinal != entrada.titulo) {
                        mapaReemplazo[entrada.id] = entrada.copy(titulo = tituloFinal)
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
                repositorio.importarEntradas(nuevas)
            }

            avisar("Se han actualizado $modificadas títulos")
            alTerminar()
        }
    }

    fun agregarPrefijoSubdominio(prefijo: String) {
        val limpio = prefijo.trim().lowercase().removePrefix(".").removeSuffix(".")
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.prefijosSubdominios.contains(limpio)) {
                    it.copy(prefijosSubdominios = it.prefijosSubdominios + limpio)
                } else it
            }
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
