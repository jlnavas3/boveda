package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun EstadoEntradaNoEncontrada(
    alVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Esta entrada ya no está en la bóveda", color = TextoSecundario)
        Spacer(Modifier.height(16.dp))
        BotonColorido(
            texto = "Volver",
            color = ColorAcento,
            icono = Icons.AutoMirrored.Filled.ArrowBack
        ) { alVolver() }
    }
}
