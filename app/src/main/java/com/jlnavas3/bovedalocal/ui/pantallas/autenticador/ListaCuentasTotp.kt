package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun ListaCuentasTotp(
    totpFiltrados: List<Entrada>,
    totalTotp: Int,
    ahora: Long,
    separarDigitos: Boolean,
    haptica: Haptica,
    alCopiarCodigo: (String) -> Unit,
    alAlternarFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val etiquetaGrupo = if (totpFiltrados.size == totalTotp) {
        "Códigos activos (${totpFiltrados.size})"
    } else {
        "Códigos activos (${totpFiltrados.size} de $totalTotp)"
    }

    GrupoAjustes(
        etiqueta = etiquetaGrupo,
        modifier = modifier
    ) {
        if (totpFiltrados.isEmpty()) {
            EstadoVacioAutenticador(hayCuentasTotp = totalTotp > 0)
        } else {
            totpFiltrados.forEachIndexed { index, entrada ->
                if (index > 0) SeparadorFilaSimple()
                TarjetaCuentaTotp(
                    entrada = entrada,
                    ahora = ahora,
                    separarDigitos = separarDigitos,
                    haptica = haptica,
                    alCopiarCodigo = alCopiarCodigo,
                    alAlternarFavorito = { alAlternarFavorito(entrada.id) }
                )
            }
        }
    }
}
