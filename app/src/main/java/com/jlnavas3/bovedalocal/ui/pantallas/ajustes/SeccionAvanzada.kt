package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
        titulo = "Abecedario lateral",
        icono = Icons.AutoMirrored.Filled.Sort,
        descripcion = "Navegación rápida con efecto de ola estilo Niagara y personalización completa.",
        inicialmenteAbierta = false,
        colorIcono = ColorAcento
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

    TarjetaAjuste(
        titulo = "Contraseña maestra",
        icono = Icons.Filled.Lock,
        descripcion = "Cambia la clave que protege toda la bóveda.",
        inicialmenteAbierta = false,
        colorIcono = ColorSeguridad
    ) {
        Text(
            text = "Al modificar la contraseña maestra, toda la base de datos se re-cifrará inmediatamente con una nueva clave derivada mediante Argon2id. Recuerda memorizarla o anotarla en tu kit físico.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
        )
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Cambiar contraseña maestra",
            color = ColorSeguridad,
            icono = Icons.Filled.Lock
        ) { alCambiarMaestra() }
    }

    Spacer(Modifier.height(16.dp))

    TarjetaAjuste(
        titulo = "Zona peligrosa",
        icono = Icons.Filled.Warning,
        descripcion = "Borra de forma irreversible la bóveda de este dispositivo.",
        inicialmenteAbierta = false,
        colorIcono = Peligro
    ) {
        Text(
            text = "Esta acción eliminará de forma permanente e irrecuperable todas tus contraseñas, notas, passkeys, configuraciones y registros de diagnóstico en este teléfono.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
        )
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Borrar bóveda definitivamente",
            color = ColorPapelera,
            icono = Icons.Filled.Delete
        ) { alBorrarBoveda() }
    }
}
