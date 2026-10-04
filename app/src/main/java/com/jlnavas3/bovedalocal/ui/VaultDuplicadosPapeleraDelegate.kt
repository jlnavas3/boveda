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

    // ------------------------------------------------------------- duplicados y salud
    fun eliminarDuplicadasExactasMasivo(grupos: List<GrupoDuplicado>) {
        val idsABorrar = grupos
            .filter { it.tipo == TipoDuplicado.IDENTICO }
            .flatMap { it.entradasSecundarias.map { ent -> ent.id } }
            .toSet()

        if (idsABorrar.isEmpty()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(idsABorrar) }
            Diagnostico.apuntar("duplicados", "Limpieza masiva: ${idsABorrar.size} copias idénticas movidas a la papelera")
            avisoInterno.value = "${idsABorrar.size} copias idénticas movidas a la papelera"
        }
    }

    fun unificarEntradas(principal: Entrada, secundarias: List<Entrada>) {
        val idsSecundarias = secundarias.map { it.id }.toSet()
        if (idsSecundarias.isEmpty()) return
        ejecutar {
            val unificada = AnalizadorDuplicados.fusionar(principal, secundarias)
            withContext(Dispatchers.IO) {
                repositorio.guardarEntrada(unificada)
                repositorio.eliminarEntradas(idsSecundarias)
            }
            Diagnostico.apuntar("duplicados", "Entrada unificada y ${idsSecundarias.size} duplicadas enviadas a la papelera")
            avisoInterno.value = "Entradas unificadas con éxito"
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
