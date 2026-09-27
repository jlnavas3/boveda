package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos

@Composable
fun GrupoConfiguracionPinSenuelo(
    mostrarIdsAjustes: Boolean,
    activo: Boolean,
    pinCoaccion: String,
    verPin: Boolean,
    tieneHashGuardado: Boolean,
    alCambiarActivo: (Boolean) -> Unit,
    alCambiarPinCoaccion: (String) -> Unit,
    alAlternarVerPin: () -> Unit,
    alGuardarPin: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "PIN de coacción",
        icono = Icons.Filled.Password,
        colorIcono = ColorSeguridad,
        idGrupo = "01.3.G2",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Activar modo señuelo",
            icono = null,
            activo = activo,
            idFila = "01.3.1",
            mostrarId = mostrarIdsAjustes,
            colorActivo = ColorAcento,
            alCambiar = alCambiarActivo
        )

        if (activo) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (tieneHashGuardado) "Cambiar PIN de coacción" else "Definir PIN de coacción",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                ComponenteCampoTexto(
                    valor = pinCoaccion,
                    etiqueta = "PIN de coacción (ej. 1984)",
                    alCambiar = alCambiarPinCoaccion,
                    tipo = TipoCampoTexto.NUMERICO,
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = alAlternarVerPin,
                    mostrarIcono = false
                )

                Spacer(Modifier.height(12.dp))

                BotonBoveda(
                    texto = "Guardar PIN de coacción",
                    alPulsar = alGuardarPin,
                    activo = pinCoaccion.isNotBlank()
                )
            }
        }
    }
}
