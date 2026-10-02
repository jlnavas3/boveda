package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.AjustesSistema

@Composable
fun DialogoNombreBoveda(
    nombreActual: String,
    alCerrar: () -> Unit,
    alGuardar: (String) -> Unit
) {
    var nombreTemporal by remember(nombreActual) { mutableStateOf(nombreActual) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Nombre de la app",
        botonConfirmar = {
            TextButton(onClick = {
                alCerrar()
                alGuardar(nombreTemporal.trim())
            }) {
                Text("Guardar", color = ColorAcento)
            }
        },
        botonDescartar = {
            Row {
                TextButton(onClick = {
                    nombreTemporal = ""
                    alGuardar("")
                    alCerrar()
                }) {
                    Text("Restablecer", color = TextoSecundario)
                }
                TextButton(onClick = alCerrar) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        }
    ) {
        Column {
            Text(
                "Personaliza el nombre que se muestra en la cabecera del listado y en la barra lateral. Si se deja vacío se usará \"Bóveda local\".",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            CampoBoveda(
                valor = nombreTemporal,
                etiqueta = "Nombre de la bóveda",
                alCambiar = { nombreTemporal = it }
            )
        }
    }
}

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
        colorIcono = colorLegibleParaTema(ColorPasskeys),
        fondoIcono = fondoBadgeParaTema(ColorPasskeys),
        botonConfirmar = {
            TextButton(onClick = {
                alCerrar()
                if (!AjustesSistema.abrirProveedorCredenciales(actividad)) {
                    vm.avisar("Ajustes › Contraseñas y cuentas › Contraseñas y llaves de acceso")
                }
            }) {
                Text("Configurar proveedor", color = ColorPasskeys, fontWeight = FontWeight.Bold)
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
