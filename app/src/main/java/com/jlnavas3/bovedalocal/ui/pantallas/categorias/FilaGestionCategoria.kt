package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoSuperficie
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Tarjeta individual de una [Categoria] en la pantalla de gestion.
 */
@Composable
fun FilaGestionCategoria(
    categoria: Categoria,
    conteoCuentas: Int,
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorBase = parsearColorO(categoria.colorHex ?: "", ColorAcento)
    val iconoVector = IconosCategorias.obtenerIcono(categoria.icono)

    ContenedorTarjeta(
        modifier = modifier,
        colorFondo = ColorTarjetaAjustes,
        paddingInterno = 14.dp,
        alPulsar = alEditar
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContenedorIconoInsignia(
                icono = iconoVector,
                tamano = TamanoInsignia.MEDIANO,
                colorFondo = colorBase.copy(alpha = 0.16f),
                colorIcono = colorBase,
                conBorde = true
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextoTitulo(
                        texto = categoria.nombre,
                        estilo = EstiloTitulo.PEQUENO,
                        maxLineas = 1
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colorBase.copy(alpha = 0.12f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        TextoCuerpo(
                            texto = if (conteoCuentas == 1) "1 entrada" else "$conteoCuentas entradas",
                            tamano = TamanoCuerpo.MINI,
                            color = colorBase,
                            maxLineas = 1
                        )
                    }
                }

                TextoSubtitulo(
                    texto = "Categoría temática",
                    color = TextoSecundario,
                    maxLineas = 1
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonIconoSuperficie(
                    icono = Icons.Filled.Edit,
                    descripcion = "Editar categoría",
                    alPulsar = alEditar
                )

                BotonIconoSuperficie(
                    icono = Icons.Filled.Delete,
                    descripcion = "Eliminar categoría",
                    colorIcono = Peligro,
                    alPulsar = alEliminar
                )
            }
        }
    }
}
