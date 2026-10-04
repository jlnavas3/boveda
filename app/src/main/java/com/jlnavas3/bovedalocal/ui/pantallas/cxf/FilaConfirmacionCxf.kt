package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaPildora
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Fila individual para una credencial CXF que va a ser importada.
 * Muestra checkbox de selección, título, usuario, badge si ya existe y chips descriptivos.
 */
@Composable
fun FilaConfirmacionCxf(
    item: ItemSeleccionableCxf,
    alAlternar: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    ContenedorTarjeta(
        onClick = alAlternar,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckboxBoveda(
                checked = item.seleccionada,
                onCheckedChange = onCheckedChange
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextoTitulo(
                        texto = item.entrada.titulo.ifBlank { "Sin título" },
                        estilo = EstiloTitulo.PEQUENO,
                        maxLineas = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (item.yaExisteEnBoveda) {
                        Spacer(Modifier.width(6.dp))
                        InsigniaPildora(
                            texto = "Ya en bóveda",
                            colorAcento = Peligro,
                            mostrarPunto = false
                        )
                    }
                }

                if (item.entrada.usuario.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    TextoSubtitulo(
                        texto = item.entrada.usuario,
                        maxLineas = 1
                    )
                }

                Spacer(Modifier.height(6.dp))

                // Etiquetas de contenido adaptadas al tema
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (item.entrada.passkey != null) {
                        MiniChipCxf("Llave de paso", ColorPasskeys)
                    }
                    if (item.entrada.contrasena.isNotBlank()) {
                        MiniChipCxf("Contraseña", Color(0xFF2196F3))
                    }
                    if (!item.entrada.secretoTotp.isNullOrBlank()) {
                        MiniChipCxf("Dos pasos", Color2FA)
                    }
                }
            }
        }
    }
}
