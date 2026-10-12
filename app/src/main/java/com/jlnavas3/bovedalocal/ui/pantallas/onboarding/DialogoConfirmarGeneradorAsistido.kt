/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo de confirmación y advertencia de seguridad previo a abrir el Generador Asistido
 * para la creación de la contraseña maestra.
 */
@Composable
fun DialogoConfirmarGeneradorAsistido(
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alDescartar,
        fijarAbajo = false,
        titulo = "Generador Asistido",
        descripcion = "Creación de contraseña maestra",
        icono = Icons.Filled.AutoFixHigh,
        colorIcono = ColorSobreAcento,
        fondoIcono = ColorAcento,
        mostrarBotonCerrar = false
    ) {
        TextoCuerpo(
            texto = "¿Deseas generar una contraseña segura asistida para tu bóveda?",
            tamano = TamanoCuerpo.NORMAL,
            color = TextoPrincipal
        )

        Spacer(Modifier.height(8.dp))

        TextoCuerpo(
            texto = "Podrás elegir entre una contraseña aleatoria de alta entropía, una frase de paso memorable (Diceware) o una estructura por patrón.",
            tamano = TamanoCuerpo.PEQUENO,
            color = TextoSecundario
        )

        Spacer(Modifier.height(12.dp))

        ComponenteAlerta(
            tipo = TipoAlerta.WARNING,
            titulo = "Responsabilidad de resguardo",
            mensaje = "Esta será la única llave de acceso a tu bóveda. Deberás memorizarla o anotarla en un lugar físico seguro, ya que no existe recuperación de contraseña."
        )

        Spacer(Modifier.height(18.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonBoveda(
                texto = "Abrir Generador Asistido",
                variante = VarianteBoton.PRIMARIO,
                icono = Icons.Filled.AutoFixHigh,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = alConfirmar
            )

            BotonBoveda(
                texto = "Escribirla yo mismo",
                variante = VarianteBoton.SECUNDARIO,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = alDescartar
            )
        }
    }
}
