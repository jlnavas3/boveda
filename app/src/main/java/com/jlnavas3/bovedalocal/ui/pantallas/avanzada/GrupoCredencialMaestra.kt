package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun GrupoCredencialMaestra(
    mostrarIdsAjustes: Boolean,
    alSolicitarCambio: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Credencial maestra",
        idGrupo = "05.1.G1",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Al cambiar la contraseña maestra, la base de datos se re-cifra en tiempo real con Argon2id",
        modifier = modifier
    ) {
        ComponenteNavegacion(
            titulo = "Cambiar contraseña maestra",
            icono = Icons.Filled.Lock,
            colorIcono = ColorSeguridad,
            idFila = "05.1.1",
            mostrarId = mostrarIdsAjustes,
            alPulsar = alSolicitarCambio
        )
    }
}
