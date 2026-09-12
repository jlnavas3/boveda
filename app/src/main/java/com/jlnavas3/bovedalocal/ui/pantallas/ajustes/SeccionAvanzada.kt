package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionAvanzada(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    alCambiarMaestra: () -> Unit,
    alBorrarBoveda: () -> Unit
) {
    TarjetaAjuste(
        "Abecedario lateral",
        Icons.AutoMirrored.Filled.Sort,
        "Navegación rápida con efecto de ola estilo Niagara y personalización completa.",
        inicialmenteAbierta = false
    ) {
        FilaAjuste(
            titulo = "Mostrar abecedario en la lista",
            descripcion = "Franja de navegación rápida por letras en el extremo derecho del listado.",
            activo = ajustes.mostrarIndiceAlfabetico,
            alCambiar = { haptica.tic(); vm.ajustarMostrarIndiceAlfabetico(it) }
        )

        if (ajustes.mostrarIndiceAlfabetico) {
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar abecedario y ola",
                color = ColorAcento,
                icono = Icons.AutoMirrored.Filled.Sort
            ) { vm.ir(Pantalla.AjustesIndice) }
        }
    }

    Spacer(Modifier.height(16.dp))

    TarjetaAjuste("Contraseña maestra", Icons.Filled.Lock, "Cambia la clave que protege toda la bóveda.", inicialmenteAbierta = false) {
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Cambiar contraseña maestra",
            color = ColorSeguridad,
            icono = Icons.Filled.Lock
        ) { alCambiarMaestra() }
    }

    Spacer(Modifier.height(16.dp))

    TarjetaAjuste("Zona peligrosa", Icons.Filled.Warning, "Borra de forma irreversible la bóveda de este dispositivo.", inicialmenteAbierta = false) {
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Borrar bóveda definitivamente",
            color = ColorPapelera,
            icono = Icons.Filled.Delete
        ) { alBorrarBoveda() }
    }
}
