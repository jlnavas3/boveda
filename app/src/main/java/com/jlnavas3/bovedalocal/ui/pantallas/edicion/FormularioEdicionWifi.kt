package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun FormularioEdicionWifi(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarClave by remember { mutableStateOf(false) }

    val ssid = GestorCamposBase.valorDeCampo(campos, "Nombre de red (SSID)")
    val clave = GestorCamposBase.valorDeCampo(campos, "Contraseña Wi-Fi")
    val seguridad = GestorCamposBase.valorDeCampo(campos, "Tipo de seguridad")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = ssid,
            etiqueta = "Nombre de red (SSID)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Nombre de red (SSID)", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = clave,
            etiqueta = "Contraseña Wi-Fi",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Contraseña Wi-Fi", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true
        )

        Column {
            CampoBoveda(
                valor = seguridad,
                etiqueta = "Tipo de seguridad (ej. WPA3, WPA2-Personal)",
                alCambiar = {
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de seguridad", it, TipoCampo.TEXTO))
                }
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("WPA2 / WPA3", "WPA", "WEP", "Sin contraseña (Abierta)").forEach { opcion ->
                    val seleccionado = seguridad.equals(opcion, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (seleccionado) ColorAcento else ColorCampoAjustes)
                            .clickable {
                                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de seguridad", opcion, TipoCampo.TEXTO))
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = opcion,
                            color = if (seleccionado) ColorSobreAcento else TextoSecundario,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}
