package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys

@Composable
fun PantallaPasskeys(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val passkeys = remember(vm.repositorio.entradas()) { vm.repositorio.passkeys() }
    val formato = remember { SimpleDateFormat("d MMM yyyy", Locale("es", "ES")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Passkeys",
            subtitulo = "Llaves de acceso criptográficas ES256 en hardware local",
            alVolver = { vm.volverAtras() }
        )

        TarjetaPepo {
            EtiquetaSeccion("Cómo activarlas")
            Spacer(Modifier.height(8.dp))
            Text(
                buildAnnotatedString {
                    append("Android tiene que saber que ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("'Bóveda local'")
                    }
                    append(" es tu gestor. El botón te deja en la pantalla de \"Contraseñas y llaves de acceso\": ahí marca ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Bóveda local")
                    }
                    append(".")
                },
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            BotonColorido(
                texto = "Abrir proveedor en ajustes del sistema",
                color = ColorPasskeys,
                icono = Icons.Filled.Key
            ) {
                if (!AjustesSistema.abrirProveedorCredenciales(contexto)) {
                    vm.avisar("Tu móvil no deja abrirla directa: Ajustes › Contraseñas y cuentas › Contraseñas y llaves de acceso")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Cada fabricante la coloca en un sitio distinto. Si el botón te deja en un menú de ajustes, busca \"Contraseñas\" en su buscador.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(16.dp))

        if (passkeys.isEmpty()) {
            TarjetaPepo {
                Text("Todavía no hay passkeys", color = ColorTitulos, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    buildAnnotatedString {
                        append("Cuando una web o app te pida crear una passkey y elijas ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Bóveda local") }
                        append(", aparecerá en esta lista.")
                    },
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            passkeys.forEach { entrada ->
                val datos = entrada.passkey ?: return@forEach
                TarjetaPepo(alPulsar = { vm.ir(Pantalla.Detalle(entrada.id)) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Monograma(titulo = datos.rpName.ifBlank { datos.rpId }, semilla = datos.rpId, tamano = 42)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(datos.rpName.ifBlank { datos.rpId }, color = ColorTitulos, style = MaterialTheme.typography.titleMedium)
                            Text(
                                datos.usuario.ifBlank { entrada.usuario.ifBlank { datos.rpId } },
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Creada el ${formato.format(Date(entrada.creadaEn))}",
                                color = Menta,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
