package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoProveedorCredenciales(
    mostrarIdsAjustes: Boolean,
    alConfigurarProveedor: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Proveedor de credenciales",
        icono = Icons.Filled.Key,
        colorIcono = Color(0xFF8B5CF6),
        idGrupo = "04-HER-PSK-G02",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Ajustes del sistema",
            subtitulo = "Activar Bóveda local como proveedor oficial",
            icono = Icons.Filled.Key,
            colorIcono = Color(0xFF8B5CF6),
            idFila = "04-HER-PSK-SYS",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alConfigurarProveedor
        )
        Text(
            text = "Android requiere registrar a Bóveda local en \"Contraseñas y cuentas\" para permitir el autocompletado y llaves de paso en otras aplicaciones.",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
