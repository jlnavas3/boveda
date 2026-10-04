package com.jlnavas3.bovedalocal.ui.pantallas.salud

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.MedidorFuerza

data class DatosSaludBoveda(
    val claves: List<Entrada>,
    val ignoradas: List<Entrada>,
    val duplicadas: List<List<Entrada>>,
    val debiles: List<Entrada>,
    val muyComunes: List<Entrada>,
    val antiguas: List<Entrada>,
    val totalSobrantesDuplicadas: Int,
    val pestanasConDatos: List<PestanaSalud>
)

/**
 * Agrupa y memoriza los cálculos analíticos de contraseñas de la bóveda para la pantalla de salud.
 */
@Composable
fun rememberDatosSaludBoveda(
    entradas: List<Entrada>,
    umbralDias: Int,
    ahora: Long,
    contexto: Context
): DatosSaludBoveda {
    val claves = remember(entradas) {
        entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() && !it.ignoradaEnSalud }
    }
    val ignoradas = remember(entradas) {
        entradas.filter { it.ignoradaEnSalud }
    }
    val duplicadas = remember(claves) {
        claves.groupBy { it.contrasena }.values.filter { it.size > 1 }
    }
    val debiles = remember(claves) {
        claves.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }
            .sortedBy { MedidorFuerza.medir(it.contrasena).puntuacion }
    }
    val muyComunes = remember(claves) {
        claves.filter { ContrasenasComunes.esComun(contexto, it.contrasena) }
    }
    val antiguas = remember(claves, umbralDias) {
        if (umbralDias <= 0) emptyList()
        else {
            claves.filter { it.modificadaEn > 0 && diasDesde(it.modificadaEn, ahora) >= umbralDias.toLong() }
                .sortedBy { it.modificadaEn }
        }
    }
    val gruposDuplicados = remember(entradas) { AnalizadorDuplicados.analizar(entradas) }
    val totalSobrantesDuplicadas = remember(gruposDuplicados) { gruposDuplicados.sumOf { it.entradasSecundarias.size } }

    val pestanasConDatos = remember(duplicadas.size, muyComunes.size, debiles.size, antiguas.size, ignoradas.size) {
        buildList {
            if (duplicadas.isNotEmpty()) add(PestanaSalud.REPETIDAS)
            if (muyComunes.isNotEmpty()) add(PestanaSalud.COMUNES)
            if (debiles.isNotEmpty()) add(PestanaSalud.DEBILES)
            if (antiguas.isNotEmpty()) add(PestanaSalud.ANTIGUAS)
            if (ignoradas.isNotEmpty()) add(PestanaSalud.IGNORADAS)
        }
    }

    return remember(claves, ignoradas, duplicadas, debiles, muyComunes, antiguas, totalSobrantesDuplicadas, pestanasConDatos) {
        DatosSaludBoveda(
            claves = claves,
            ignoradas = ignoradas,
            duplicadas = duplicadas,
            debiles = debiles,
            muyComunes = muyComunes,
            antiguas = antiguas,
            totalSobrantesDuplicadas = totalSobrantesDuplicadas,
            pestanasConDatos = pestanasConDatos
        )
    }
}
