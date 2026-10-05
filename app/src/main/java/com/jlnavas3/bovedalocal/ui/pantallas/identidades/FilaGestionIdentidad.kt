package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
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
 * Tarjeta individual de una [Identidad] en la pantalla de gestión.
 */
@Composable
fun FilaGestionIdentidad(
    identidad: Identidad,
    conteoCuentas: Int,
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorBase = parsearColorO(identidad.colorHex ?: "", ColorAcento)

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
            // Avatar de la identidad con componente insignia
            ContenedorIconoInsignia(
                icono = Icons.Filled.Person,
                tamano = TamanoInsignia.MEDIANO,
                colorFondo = colorBase.copy(alpha = 0.16f),
                colorIcono = colorBase,
                conBorde = true
            )

            Spacer(Modifier.width(14.dp))

            // Bloque central: Nombre, correo principal y badges
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextoTitulo(
                        texto = identidad.nombre,
                        estilo = EstiloTitulo.PEQUENO,
                        maxLineas = 1
                    )

                    // Píldora de conteo de cuentas asociadas
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colorBase.copy(alpha = 0.12f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        TextoCuerpo(
                            texto = if (conteoCuentas == 1) "1 cuenta" else "$conteoCuentas cuentas",
                            tamano = TamanoCuerpo.MINI,
                            color = colorBase,
                            maxLineas = 1
                        )
                    }
                }

                Spacer(Modifier.height(3.dp))

                TextoSubtitulo(
                    texto = identidad.correoPrincipal,
                    maxLineas = 1
                )

                if (identidad.correosSecundarios.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    TextoCuerpo(
                        texto = "+${identidad.correosSecundarios.size} alias adicionales",
                        tamano = TamanoCuerpo.MINI,
                        color = TextoSecundario.copy(alpha = 0.75f),
                        maxLineas = 1
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // Acciones: Editar y Eliminar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BotonIconoSuperficie(
                    icono = Icons.Filled.Edit,
                    alPulsar = alEditar,
                    colorIcono = TextoSecundario,
                    colorFondo = colorBase.copy(alpha = 0.10f),
                    tamano = 34.dp,
                    tamanoIcono = 18.dp,
                    descripcion = "Editar identidad"
                )

                BotonIconoSuperficie(
                    icono = Icons.Filled.Delete,
                    alPulsar = alEliminar,
                    colorIcono = Peligro,
                    colorFondo = Peligro.copy(alpha = 0.10f),
                    tamano = 34.dp,
                    tamanoIcono = 18.dp,
                    descripcion = "Eliminar identidad"
                )
            }
        }
    }
}
