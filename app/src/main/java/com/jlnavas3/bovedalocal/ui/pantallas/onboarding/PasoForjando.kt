package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.BarraProgresoForja
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun PasoForjando(
    perfil: PerfilArgon2,
    ajustes: AjustesApp = AjustesApp(),
    alIniciarForja: () -> Unit
) {
    LaunchedEffect(Unit) {
        alIniciarForja()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (ajustes.animacionDesbloqueo) {
            "engranajes" -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EngranajesBoveda(
                        abierta = false,
                        modifier = Modifier.fillMaxSize(),
                        config = ajustes.aEngranajesConfig()
                    )
                }
            }
            "puerta" -> {
                PuertaBoveda(abierta = false, tamano = 210)
            }
            else -> {
                ContenedorIconoInsignia(
                    icono = Icons.Filled.Lock,
                    tamano = TamanoInsignia.HERO,
                    colorFondo = ColorTarjetaAjustes,
                    colorIcono = ColorAcento
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        TextoTitulo(
            texto = "Forjando tu bóveda",
            estilo = EstiloTitulo.GRANDE,
            alineacion = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        TextoSubtitulo(
            texto = "Argon2id está derivando tu clave con ${perfil.resumen}. Esta barrera intensiva de cálculo y memoria hace que un ataque por fuerza bruta sea computacional y físicamente inviable.",
            alineacion = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))
        BarraProgresoForja()
    }
}
