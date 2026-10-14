/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

/**
 * Delegado especializado en la gestión de duplicados, resolución de salud rápida y operaciones de papelera.
 */
interface VaultDuplicadosPapeleraDelegate {
    val repositorio: VaultRepository
    val avisoInterno: MutableStateFlow<String?>
    fun ejecutar(bloque: suspend () -> Unit)
    fun avisarConDeshacer(
        mensaje: String,
        titulo: String? = null,
        accionTexto: String = "Deshacer",
        alDeshacer: () -> Unit
    ) {
        avisoInterno.value = mensaje
    }

    // ------------------------------------------------------------- duplicados y salud
    fun unificarDuplicadasExactasMasivo(grupos: List<GrupoDuplicado>) {
        val gruposIdenticos = grupos.filter { it.tipo == TipoDuplicado.IDENTICO && it.entradasSecundarias.isNotEmpty() }
        if (gruposIdenticos.isEmpty()) return

        ejecutar {
            val totalGrupos = gruposIdenticos.size
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "unificar_duplicados_masivo",
                titulo = "Unificación de duplicados",
                faseInicial = "Consolidando credenciales idénticas...",
                totalEstimado = totalGrupos
            )

            val originalesPrincipales = mutableListOf<Entrada>()
            val originalesSecundarias = mutableListOf<Entrada>()
            val unificadas = mutableListOf<Entrada>()
            val idsABorrar = mutableSetOf<String>()

            for ((indice, grupo) in gruposIdenticos.withIndex()) {
                originalesPrincipales.add(grupo.sugeridaPrincipal)
                originalesSecundarias.addAll(grupo.entradasSecundarias)
                val unificada = AnalizadorDuplicados.fusionar(grupo.sugeridaPrincipal, grupo.entradasSecundarias)
                unificadas.add(unificada)
                idsABorrar.addAll(grupo.entradasSecundarias.map { it.id })

                val progreso = (indice + 1).toFloat() / totalGrupos
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                    item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                        id = unificada.id,
                        titulo = unificada.titulo,
                        subtitulo = unificada.usuario,
                        progreso = 1f,
                        completado = true
                    ),
                    progresoGlobal = progreso,
                    procesadas = indice + 1
                )
            }

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Escribiendo bóveda cifrada...", 0.95f)

            withContext(Dispatchers.IO) {
                repositorio.sincronizarLote(
                    entradasAGuardar = unificadas,
                    idsAEliminar = idsABorrar
                )
            }

            Diagnostico.apuntar("duplicados", "Unificación inteligente masiva: ${unificadas.size} grupos consolidados (${idsABorrar.size} duplicados unificados)")

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                mensajeExito = "${unificadas.size} grupos unificados (${idsABorrar.size} duplicados)",
                totalCuentas = totalGrupos,
                textoAccionResultado = "Deshacer",
                textoAccionSecundaria = "Aceptar",
                accionResultado = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            repositorio.sincronizarLote(
                                entradasAGuardar = originalesPrincipales + originalesSecundarias
                            )
                        }
                        avisoInterno.value = "Unificación masiva deshecha"
                    }
                }
            )

            avisarConDeshacer(
                mensaje = "${unificadas.size} grupo${if (unificadas.size > 1) "s" else ""} unificado${if (unificadas.size > 1) "s" else ""}",
                titulo = "Unificación masiva",
                alDeshacer = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            repositorio.sincronizarLote(
                                entradasAGuardar = originalesPrincipales + originalesSecundarias
                            )
                        }
                        avisoInterno.value = "Unificación masiva deshecha"
                    }
                }
            )
        }
    }

    fun eliminarDuplicadasExactasMasivo(grupos: List<GrupoDuplicado>) {
        unificarDuplicadasExactasMasivo(grupos)
    }

    fun unificarEntradas(
        principal: Entrada,
        secundarias: List<Entrada>,
        usuarioElegido: String? = null
    ) {
        val idsSecundarias = secundarias.map { it.id }.toSet()
        if (idsSecundarias.isEmpty()) return
        ejecutar {
            val principalOriginal = principal
            val secundariasOriginales = secundarias
            val unificada = AnalizadorDuplicados.fusionar(principal, secundarias, usuarioElegido)
            withContext(Dispatchers.IO) {
                repositorio.sincronizarLote(
                    entradasAGuardar = listOf(unificada),
                    idsAEliminar = idsSecundarias
                )
            }
            Diagnostico.apuntar("duplicados", "Entrada unificada y ${idsSecundarias.size} duplicadas enviadas a la papelera")
            avisarConDeshacer(
                mensaje = "Entradas unificadas con éxito",
                titulo = "Unificación",
                alDeshacer = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            repositorio.sincronizarLote(
                                entradasAGuardar = listOf(principalOriginal) + secundariasOriginales
                            )
                        }
                        avisoInterno.value = "Unificación deshecha"
                    }
                }
            )
        }
    }

    fun unificarMultiplesGrupos(gruposAUnificar: List<Pair<Entrada, List<Entrada>>>) {
        if (gruposAUnificar.isEmpty()) return
        ejecutar {
            val totalGrupos = gruposAUnificar.size
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "unificar_multiples_grupos",
                titulo = "Unificación de duplicados",
                faseInicial = "Fusionando grupos seleccionados...",
                totalEstimado = totalGrupos
            )

            val originalesPrincipales = mutableListOf<Entrada>()
            val originalesSecundarias = mutableListOf<Entrada>()
            val unificadas = mutableListOf<Entrada>()
            val idsABorrar = mutableSetOf<String>()

            for ((indice, par) in gruposAUnificar.withIndex()) {
                val (principal, secundarias) = par
                if (secundarias.isEmpty()) continue
                originalesPrincipales.add(principal)
                originalesSecundarias.addAll(secundarias)
                val unificada = AnalizadorDuplicados.fusionar(principal, secundarias)
                unificadas.add(unificada)
                idsABorrar.addAll(secundarias.map { it.id })

                val progreso = (indice + 1).toFloat() / totalGrupos
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                    item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                        id = unificada.id,
                        titulo = unificada.titulo,
                        subtitulo = unificada.usuario,
                        progreso = 1f,
                        completado = true
                    ),
                    progresoGlobal = progreso,
                    procesadas = indice + 1
                )
            }

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Escribiendo bóveda cifrada...", 0.95f)

            withContext(Dispatchers.IO) {
                repositorio.sincronizarLote(
                    entradasAGuardar = unificadas,
                    idsAEliminar = idsABorrar
                )
            }

            val cantGrupos = unificadas.size
            Diagnostico.apuntar("duplicados", "Unificación múltiple por grupos: $cantGrupos grupos consolidados (${idsABorrar.size} duplicados unificados)")

            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                mensajeExito = "$cantGrupos grupos consolidados (${idsABorrar.size} duplicados)",
                totalCuentas = cantGrupos,
                textoAccionResultado = "Deshacer",
                textoAccionSecundaria = "Aceptar",
                accionResultado = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            repositorio.sincronizarLote(
                                entradasAGuardar = originalesPrincipales + originalesSecundarias
                            )
                        }
                        avisoInterno.value = "Unificación deshecha"
                    }
                }
            )

            avisarConDeshacer(
                mensaje = "$cantGrupos grupo${if (cantGrupos > 1) "s" else ""} unificado${if (cantGrupos > 1) "s" else ""} con éxito",
                titulo = "Unificación",
                alDeshacer = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            repositorio.sincronizarLote(
                                entradasAGuardar = originalesPrincipales + originalesSecundarias
                            )
                        }
                        avisoInterno.value = "Unificación deshecha"
                    }
                }
            )
        }
    }

    fun actualizarContrasenaRapida(id: String, nuevaClave: String) {
        if (nuevaClave.isBlank()) return
        ejecutar {
            val entrada = withContext(Dispatchers.IO) { repositorio.entrada(id) } ?: return@ejecutar
            val entradaActualizada = entrada.copy(
                contrasena = nuevaClave,
                modificadaEn = System.currentTimeMillis()
            )
            withContext(Dispatchers.IO) { repositorio.guardarEntrada(entradaActualizada) }
            Diagnostico.apuntar("salud", "Contraseña de \"${entrada.titulo}\" actualizada de forma rápida")
            avisoInterno.value = "Contraseña actualizada con éxito"
        }
    }

    // ------------------------------------------------------------- papelera

    fun restaurarDeLaPapelera(id: String, sustituir: Boolean = false) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.papelera().firstOrNull { it.id == id } }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
            withContext(Dispatchers.IO) { repositorio.restaurarDeLaPapelera(id, sustituir) }
            val mensajeAccion = if (sustituir) "sustituida" else "restaurada"
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) $mensajeAccion desde la papelera a la bóveda")
            avisoInterno.value = if (sustituir) "Entrada sustituida" else "Entrada restaurada"
        }
    }

    fun borrarDefinitivamente(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
            withContext(Dispatchers.IO) { repositorio.borrarDefinitivamente(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) eliminada definitivamente de la papelera")
            avisoInterno.value = "Borrada para siempre"
        }
    }

    fun vaciarPapelera() {
        ejecutar {
            val cant = repositorio.papelera().size
            withContext(Dispatchers.IO) { repositorio.vaciarPapelera() }
            Diagnostico.apuntar("papelera", "Papelera vaciada por completo ($cant entradas eliminadas definitivamente)")
            avisoInterno.value = "Papelera vaciada"
        }
    }
}
