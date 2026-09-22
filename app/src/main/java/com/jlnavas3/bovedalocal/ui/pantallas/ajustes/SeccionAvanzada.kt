package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

import androidx.compose.material.icons.filled.Tune

@Composable
fun SeccionAvanzada(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    alCambiarMaestra: () -> Unit,
    alBorrarBoveda: () -> Unit,
    seccionDestino: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TarjetaAjuste(
        titulo = "Abecedario lateral",
        icono = Icons.AutoMirrored.Filled.Sort,
        descripcion = "Navegación rápida con efecto de ola estilo Niagara y personalización completa.",
        inicialmenteAbierta = seccionDestino == "11.1" || seccionDestino == "11",
        colorIcono = ColorIconosInternos,
        idEtiqueta = "11.1",
        mostrarId = ajustes.mostrarIdsAjustes
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
            ) { vm.irPorId("11.1") }
        }
    }

    Spacer(Modifier.height(16.dp))

    TarjetaAjuste(
        titulo = "Contraseña maestra",
        icono = Icons.Filled.Lock,
        descripcion = "Cambia la clave que protege toda la bóveda.",
        inicialmenteAbierta = seccionDestino == "11.2",
        colorIcono = ColorSeguridad,
        idEtiqueta = "11.2",
        mostrarId = ajustes.mostrarIdsAjustes
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
        titulo = "Desarrollo y referencia",
        icono = Icons.Filled.Tune,
        descripcion = "Opciones técnicas y de identificación para atajos y diagnóstico.",
        inicialmenteAbierta = seccionDestino == "11.3",
        colorIcono = ColorIconosInternos,
        idEtiqueta = "11.3",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        FilaAjuste(
            titulo = "Identificadores de ajustes",
            descripcion = "Muestra una etiqueta con el código ID de cada sección debajo de su ícono.",
            activo = ajustes.mostrarIdsAjustes,
            alCambiar = {
                haptica.tic()
                vm.ajustarMostrarIdsAjustes(it)
            }
        )
    }

    Spacer(Modifier.height(16.dp))

    TarjetaAjuste(
        titulo = "Zona peligrosa",
        icono = Icons.Filled.Warning,
        descripcion = "Borra de forma irreversible la bóveda de este dispositivo.",
        inicialmenteAbierta = seccionDestino == "11.4",
        colorIcono = Peligro,
        idEtiqueta = "11.4",
        mostrarId = ajustes.mostrarIdsAjustes
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
}
