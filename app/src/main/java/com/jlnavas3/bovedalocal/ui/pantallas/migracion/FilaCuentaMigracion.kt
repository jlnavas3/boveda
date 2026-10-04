package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaPildora
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.CuentaGoogleAuth

/**
 * Fila individual para representar una cuenta 2FA en la pantalla de migración.
 * Utiliza ContenedorTarjeta, ContenedorIconoInsignia y tipografía estandarizada.
 */
@Composable
fun FilaCuentaMigracion(
    cuenta: CuentaGoogleAuth,
    yaExisteEnBoveda: Boolean,
    alAlternarSeleccion: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ContenedorTarjeta(
        onClick = alAlternarSeleccion,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(ColorDatos2FA)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckboxBoveda(
                    checked = cuenta.seleccionada,
                    onCheckedChange = onCheckedChange
                )

                Spacer(Modifier.width(8.dp))

                ContenedorIconoInsignia(
                    icono = Icons.Filled.Timer,
                    color = ColorExportacion,
                    tamano = TamanoInsignia.MEDIANO
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TextoTitulo(
                            texto = cuenta.titulo,
                            estilo = EstiloTitulo.PEQUENO,
                            maxLineas = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        if (yaExisteEnBoveda) {
                            InsigniaPildora(
                                texto = "En la bóveda",
                                colorAcento = TextoSecundario,
                                mostrarPunto = false
                            )
                        }
                    }

                    if (cuenta.cuenta.isNotBlank() && cuenta.cuenta != cuenta.emisor) {
                        TextoSubtitulo(
                            texto = cuenta.cuenta,
                            maxLineas = 1
                        )
                    }

                    TextoPiePagina(
                        texto = "${cuenta.digitos} dígitos • ${if (cuenta.esTotp) "TOTP ${cuenta.periodo}s" else "HOTP"}",
                        monoespaciada = true
                    )
                }
            }
        }
    }
}
