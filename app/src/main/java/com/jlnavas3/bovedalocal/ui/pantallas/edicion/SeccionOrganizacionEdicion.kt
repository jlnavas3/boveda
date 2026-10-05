package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.SeccionColeccionesEdicion
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SeccionOrganizacionEdicion(
    coleccionesDisponibles: List<Coleccion> = emptyList(),
    coleccionesSeleccionadas: List<String> = emptyList(),
    alCambiarColecciones: (List<String>) -> Unit = {},
    alCrearNuevaColeccion: () -> Unit = {},
    identidadesDisponibles: List<com.jlnavas3.bovedalocal.data.Identidad> = emptyList(),
    identidadSeleccionadaId: String? = null,
    alSeleccionarIdentidad: (com.jlnavas3.bovedalocal.data.Identidad?) -> Unit = {},
    etiquetas: List<String>,
    alCambiarEtiquetas: (List<String>) -> Unit,
    etiquetasSugeridas: List<String>,
    favorito: Boolean,
    alAlternarFavorito: (Boolean) -> Unit,
    ignoradaEnSalud: Boolean = false,
    alAlternarIgnoradaEnSalud: (Boolean) -> Unit = {}
) {
    GrupoAjustes(etiqueta = "Organización") {
        Column(modifier = Modifier.padding(14.dp)) {
            if (identidadesDisponibles.isNotEmpty()) {
                com.jlnavas3.bovedalocal.ui.pantallas.identidades.SeccionIdentidadEdicion(
                    identidadesDisponibles = identidadesDisponibles,
                    identidadSeleccionadaId = identidadSeleccionadaId,
                    alSeleccionarIdentidad = alSeleccionarIdentidad
                )
                Spacer(Modifier.height(14.dp))
                SeparadorFilaSimple()
                Spacer(Modifier.height(10.dp))
            }

            SeccionColeccionesEdicion(
                coleccionesDisponibles = coleccionesDisponibles,
                coleccionesSeleccionadas = coleccionesSeleccionadas,
                alCambiarColecciones = alCambiarColecciones,
                alCrearNuevaColeccion = alCrearNuevaColeccion
            )

            Spacer(Modifier.height(14.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(10.dp))

            SeccionEtiquetasEdicion(
                etiquetas = etiquetas,
                alCambiarEtiquetas = alCambiarEtiquetas,
                etiquetasSugeridas = etiquetasSugeridas
            )

            Spacer(Modifier.height(14.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Favorito", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text("Aparece fijado arriba en la lista", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                }
                SwitchBoveda(
                    checked = favorito,
                    onCheckedChange = alAlternarFavorito
                )
            }

            Spacer(Modifier.height(10.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Ignorar en salud", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text("Excluir de la auditoría de seguridad y avisos", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                }
                SwitchBoveda(
                    checked = ignoradaEnSalud,
                    onCheckedChange = alAlternarIgnoradaEnSalud
                )
            }
        }
    }
}
