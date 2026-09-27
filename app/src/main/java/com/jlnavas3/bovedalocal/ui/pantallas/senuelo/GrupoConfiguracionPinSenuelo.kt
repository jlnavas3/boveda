package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
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
        etiqueta = "Configuración del PIN de coacción",
        idGrupo = "01.2.G2",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Activar Bóveda Señuelo",
            icono = Icons.Filled.Security,
            colorIcono = ColorSeguridad,
            activo = activo,
            idFila = "01.2.1",
            mostrarId = mostrarIdsAjustes,
            colorActivo = ColorAcento,
            alCambiar = alCambiarActivo
        )

        if (activo) {
            SeparadorFilaSimple()
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (tieneHashGuardado) "Cambiar PIN / Clave de coacción" else "Definir PIN de coacción",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                ComponenteCampoTexto(
                    valor = pinCoaccion,
                    etiqueta = "PIN o clave de coacción (ej. 1984)",
                    alCambiar = alCambiarPinCoaccion,
                    tipo = TipoCampoTexto.NUMERICO,
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = alAlternarVerPin,
                    mostrarIcono = true,
                    colorIcono = ColorSeguridad
                )
            }

            SeparadorFilaSimple()

            ComponenteBotonFila(
                titulo = "Guardar PIN de coacción",
                icono = Icons.Filled.Check,
                colorIcono = ColorSeguridad,
                idFila = "01.2.2",
                mostrarId = mostrarIdsAjustes,
                alPulsar = alGuardarPin
            )
        }
    }
}
