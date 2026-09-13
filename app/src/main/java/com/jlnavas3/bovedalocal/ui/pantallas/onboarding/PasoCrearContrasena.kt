package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SelectorPerfilArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun PasoCrearContrasena(
    perfilSeleccionado: PerfilArgon2,
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    alConfirmar: (String) -> Unit,
    alVolver: () -> Unit
) {
    val contexto = LocalContext.current
    var contrasena by remember { mutableStateOf("") }
    var repetida by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarRepetida by remember { mutableStateOf(false) }

    var expandirConsejoTile by remember { mutableStateOf(false) }
    var expandirArgon2 by remember { mutableStateOf(false) }
    var mostrarDialogoCompartir by remember { mutableStateOf(false) }

    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val coinciden = contrasena.isNotEmpty() && contrasena == repetida
    val valida = contrasena.length >= 10 && coinciden

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tu contraseña maestra",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTitulos
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Es la única llave de tu bóveda. Sin ella es matemáticamente imposible descifrar los datos.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(18.dp))

        // Tarjeta principal que alberga el consejo colapsable y los campos de contraseña
        ContenedorTarjeta(paddingInterno = 16.dp) {
            // Consejo colapsable para generar con el Tile de Android
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(ColorAcento.copy(alpha = 0.08f))
                    .clickable { expandirConsejoTile = !expandirConsejoTile }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Consejo: Genera con el Tile rápido",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos
                    )
                    Text(
                        text = "Usa el acceso rápido de la barra de estado",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
                Icon(
                    imageVector = if (expandirConsejoTile) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandirConsejoTile) "Colapsar" else "Expandir",
                    tint = TextoSecundario
                )
            }

            AnimatedVisibility(visible = expandirConsejoTile) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)) {
                    Text(
                        text = "Puedes deslizar hacia abajo la barra de estado de Android y pulsar el botón «Generador rápido» de Bóveda Local. Generará una clave ultra-segura al instante directamente en el portapapeles para pegarla aquí.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campo de contraseña maestra con ícono de ojo
            CampoBoveda(
                valor = contrasena,
                etiqueta = "Contraseña maestra",
                alCambiar = { contrasena = it },
                esContrasena = true,
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                monoespaciada = true
            )

            Spacer(Modifier.height(12.dp))

            // Campo de repetición con ícono de ojo
            CampoBoveda(
                valor = repetida,
                etiqueta = "Repite la contraseña",
                alCambiar = { repetida = it },
                esContrasena = true,
                mostrarContrasena = mostrarRepetida,
                alAlternarMostrarContrasena = { mostrarRepetida = !mostrarRepetida },
                monoespaciada = true
            )

            Spacer(Modifier.height(14.dp))
            BarraFuerza(fuerza.fraccion, fuerza.etiqueta, fuerza.tiempo)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (repetida.isEmpty()) "Mínimo 10 caracteres" else if (coinciden) "✓ Las contraseñas coinciden" else "✖ No coinciden",
                    color = if (repetida.isNotEmpty() && !coinciden) MaterialTheme.colorScheme.error else if (coinciden) Menta else TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Botón pequeño para compartir/respaldar contraseña
                if (contrasena.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorAcento.copy(alpha = 0.10f))
                            .clickable { mostrarDialogoCompartir = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Compartir",
                            tint = ColorAcento,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "Compartir",
                            color = ColorAcento,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Tarjeta colapsable para el Perfil de cifrado Argon2id (colapsada por defecto)
        ContenedorTarjeta(paddingInterno = 14.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandirArgon2 = !expandirArgon2 },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Memory,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Perfil de cifrado Argon2id",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos
                    )
                    Text(
                        text = perfilSeleccionado.titulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = Menta
                    )
                }
                Icon(
                    imageVector = if (expandirArgon2) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandirArgon2) "Colapsar" else "Expandir",
                    tint = TextoSecundario
                )
            }

            AnimatedVisibility(visible = expandirArgon2) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    SelectorPerfilArgon2(
                        perfilActual = perfilSeleccionado,
                        alSeleccionarPerfil = alCambiarPerfil
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        BotonAmbar(
            texto = "Forjar la bóveda",
            activo = valida,
            icono = Icons.Filled.Shield
        ) {
            alConfirmar(contrasena)
        }

        Spacer(Modifier.height(10.dp))

        BotonBorde("Volver", icono = Icons.AutoMirrored.Filled.ArrowBack) {
            alVolver()
        }
    }

    // Diálogo de advertencia antes de compartir la contraseña maestra
    if (mostrarDialogoCompartir) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCompartir = false },
            containerColor = ColorTarjetas,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = Peligro,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Advertencia de Seguridad", color = ColorTitulos)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Vas a compartir tu contraseña maestra mediante las opciones del sistema.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Si decides enviártela a ti mismo (por ejemplo en WhatsApp a tu propio chat, «Mensajes guardados» en Telegram o guardarla en un archivo de notas), te recomendamos fuertemente memorizarla y resguardarla en un lugar seguro fuera del alcance de terceros.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    Text(
                        text = "⚠️ La app no guarda tu contraseña. Si la pierdes, no hay forma matemática de recuperar tu bóveda.",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Peligro
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoCompartir = false
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Contraseña maestra - Bóveda Local")
                        putExtra(Intent.EXTRA_TEXT, contrasena)
                    }
                    contexto.startActivity(Intent.createChooser(intent, "Guardar o compartir contraseña"))
                }) {
                    Text("Entendido, Compartir", color = ColorAcento, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCompartir = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
