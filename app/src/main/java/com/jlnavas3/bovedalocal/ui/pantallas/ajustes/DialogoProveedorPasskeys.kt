package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema

@Composable
fun DialogoProveedorPasskeys(
    actividad: FragmentActivity,
    vm: VaultViewModel,
    alCerrar: () -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Proveedor de credenciales",
        icono = Icons.Filled.Key,
        colorIcono = ColorAcento,
        fondoIcono = ColorAcento.copy(alpha = 0.15f),
        botonConfirmar = {
            TextButton(onClick = {
                alCerrar()
                if (!AjustesSistema.abrirProveedorCredenciales(actividad)) {
                    vm.avisar("Ajustes › Contraseñas y cuentas › Contraseñas y llaves de acceso")
                }
            }) {
                Text("Configurar proveedor", color = ColorAcento, fontWeight = FontWeight.Bold)
            }
        },
        botonDescartar = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = TextoSecundario)
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Android requiere registrar a 'Bóveda local' como tu proveedor oficial de credenciales.",
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "En \"Contraseñas y llaves de acceso\" activa Bóveda local para permitir el inicio de sesión automático con llaves de paso.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Si tu fabricante personalizó el menú, busca \"Contraseñas\" en los ajustes de tu teléfono.",
                color = TextoSecundario.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
