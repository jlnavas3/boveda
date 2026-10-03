package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema

@Composable
fun TarjetaPermisoCamara(
    estadoPermiso: EstadoPermiso,
    alPedirPermiso: () -> Unit,
    alAvisar: (String) -> Unit
) {
    val contexto = LocalContext.current

    TarjetaBoveda {
        Text("Escanear el QR", color = ColorTitulos, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            "Para leerlo, Android tiene que darme la cámara. Se usa solo aquí, para descifrar ese QR, y no hay permiso de red con el que enviar nada.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        when (estadoPermiso) {
            EstadoPermiso.DENEGADO_PARA_SIEMPRE -> {
                Text(
                    "Android ya no me deja pedirlo desde aquí. Ábrelo tú en la ficha de la app (Permisos > Cámara) y vuelve: la cámara arrancará sola.",
                    color = Peligro,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
                BotonPrimario("Abrir la ficha de la app", icono = Icons.Filled.Settings) {
                    if (!AjustesSistema.abrirFichaApp(contexto)) alAvisar("No encuentro la ficha de la app en este móvil")
                }
            }
            EstadoPermiso.DENEGADO -> {
                BotonPrimario("Usar la cámara", icono = Icons.Filled.CameraAlt) { alPedirPermiso() }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Sin permiso no hay cámara, y no pasa nada: lee el QR desde una captura o escribe el código a mano aquí abajo.",
                    color = Peligro,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            EstadoPermiso.NO_PEDIDO -> BotonPrimario("Usar la cámara", icono = Icons.Filled.CameraAlt) { alPedirPermiso() }
        }
    }
}
