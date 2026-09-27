package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun GrupoConfiguracionPinAutodestruccion(
    mostrarIdsAjustes: Boolean,
    activo: Boolean,
    pinNuevo: String,
    pinConfirmar: String,
    verPin: Boolean,
    tieneHashGuardado: Boolean,
    alCambiarActivo: (Boolean) -> Unit,
    alCambiarPinNuevo: (String) -> Unit,
    alCambiarPinConfirmar: (String) -> Unit,
    alAlternarVerPin: () -> Unit,
    alGuardarPin: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Configuración del PIN",
        idGrupo = "01.3.G2",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Activar PIN de Autodestrucción",
            icono = Icons.Filled.DeleteForever,
            colorIcono = Peligro,
            activo = activo,
            idFila = "01.3.1",
            mostrarId = mostrarIdsAjustes,
            colorActivo = ColorAcento,
            alCambiar = alCambiarActivo
        )

        if (activo) {
            SeparadorFilaSimple()
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (tieneHashGuardado) "Cambiar PIN de Autodestrucción" else "Definir nuevo PIN de Autodestrucción",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                ComponenteCampoTexto(
                    valor = pinNuevo,
                    etiqueta = "PIN de autodestrucción (mínimo 4 caracteres)",
                    alCambiar = alCambiarPinNuevo,
                    tipo = TipoCampoTexto.NUMERICO,
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = alAlternarVerPin,
                    mostrarIcono = true,
                    colorIcono = Peligro
                )

                Spacer(Modifier.height(8.dp))

                ComponenteCampoTexto(
                    valor = pinConfirmar,
                    etiqueta = "Confirmar PIN de autodestrucción",
                    alCambiar = alCambiarPinConfirmar,
                    tipo = TipoCampoTexto.NUMERICO,
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = alAlternarVerPin,
                    mostrarIcono = true,
                    colorIcono = Peligro
                )

                if (tieneHashGuardado) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Peligro,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "PIN configurado y activo en el dispositivo",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            SeparadorFilaSimple()

            ComponenteBotonFila(
                titulo = "Guardar PIN de autodestrucción",
                icono = Icons.Filled.DeleteForever,
                colorIcono = Peligro,
                idFila = "01.3.2",
                mostrarId = mostrarIdsAjustes,
                alPulsar = alGuardarPin
            )
        }
    }
}
