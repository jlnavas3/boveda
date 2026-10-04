package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

/**
 * Tarjeta de ajustes para seleccionar el perfil criptográfico KDF (Argon2id)
 * y disparar la acción de "Forjar la bóveda".
 */
@Composable
fun TarjetaParametrosDerivacion(
    perfilSeleccionado: PerfilArgon2,
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    validaParaForjar: Boolean,
    alForjarBoveda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val opcionesArgon2 = remember {
        PerfilArgon2.entries.map { perfil ->
            OpcionSelectorModal(
                valor = perfil,
                etiquetaFila = perfil.nombreCorto,
                subetiquetaFila = perfil.memoriaTexto,
                etiquetaModal = perfil.titulo,
                descripcionModal = "${perfil.resumen}\n${perfil.detalle}",
                icono = Icons.Filled.Memory
            )
        }
    }

    ComponenteGrupo(
        etiqueta = "Parámetros de derivación",
        modifier = modifier
    ) {
        ComponenteSelectorModal(
            titulo = "Perfil Argon2id",
            valorSeleccionado = perfilSeleccionado,
            opciones = opcionesArgon2,
            alSeleccionar = alCambiarPerfil,
            icono = Icons.Filled.Memory,
            colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
            colorTinteIcono = ColorIconosInternos,
            descripcionModal = "Parámetros KDF para la derivación de tu clave maestra"
        )

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Forjar la bóveda",
            alPulsar = alForjarBoveda,
            icono = Icons.Filled.Shield,
            colorIcono = if (validaParaForjar) ColorAcento else ColorAjusteGris.copy(alpha = 0.35f),
            colorTinteIcono = if (validaParaForjar) ColorSobreAcento else ColorAjusteGris,
            habilitado = validaParaForjar
        )
    }
}
