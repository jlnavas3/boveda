package com.jlnavas3.bovedalocal.ui.pantallas.csv

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente que ofrece la opción de importar contactos de la agenda nativa
 * del teléfono hacia la bóveda en la pantalla de transferencias e importaciones.
 */
@Composable
fun GrupoImportacionContactos(
    mostrarId: Boolean,
    alIniciarImportacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Contactos del teléfono",
        icono = Icons.Filled.Person,
        colorIcono = ColorAcento,
        idGrupo = "05-COP-CNT-G01",
        mostrarId = mostrarId,
        descripcion = "Importación segura y offline de contactos desde la agenda de Android",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextoSubtitulo(
                texto = "Permite seleccionar e importar contactos de tu agenda telefónica a la bóveda. Cada contacto se guardará cifrado con su nombre, teléfonos, correos y notas."
            )
        }
        SeparadorFilaSimple()
        ComponenteBotonFila(
            titulo = "Importar contactos a la bóveda",
            icono = null,
            idFila = "05-COP-CNT-IMP",
            mostrarId = mostrarId,
            alPulsar = alIniciarImportacion
        )
    }
}
