package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
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

    var mostrarModalConsejo by remember { mutableStateOf(false) }
    var mostrarDialogoCompartir by remember { mutableStateOf(false) }

    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val coinciden = contrasena.isNotEmpty() && contrasena == repetida
    val valida = contrasena.length >= 10 && coinciden

    val opcionesArgon2 = remember {
        PerfilArgon2.entries.map { perfil ->
            OpcionSelectorModal(
                valor = perfil,
                etiquetaFila = perfil.titulo,
                etiquetaModal = perfil.titulo,
                descripcionModal = "${perfil.resumen}\n${perfil.detalle}",
                icono = Icons.Filled.Memory
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Tu contraseña maestra",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Es la única llave de tu bóveda. Sin ella es matemáticamente imposible descifrar los datos.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )
        }

        // Tarjeta 1: Credenciales maestras agrupadas
        ComponenteGrupo(
            etiqueta = "Credenciales maestras"
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                ComponenteCampoTexto(
                    valor = contrasena,
                    etiqueta = "Contraseña maestra",
                    alCambiar = { contrasena = it },
                    tipo = TipoCampoTexto.CONTRASENA,
                    mostrarIcono = true,
                    icono = Icons.Filled.Lock,
                    colorIcono = ColorIconosInternos,
                    mostrarContrasena = mostrarContrasena,
                    alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                    monoespaciada = mostrarContrasena
                )

                Spacer(Modifier.height(10.dp))

                ComponenteCampoTexto(
                    valor = repetida,
                    etiqueta = "Repite la contraseña",
                    alCambiar = { repetida = it },
                    tipo = TipoCampoTexto.CONTRASENA,
                    mostrarIcono = true,
                    icono = Icons.Filled.Lock,
                    colorIcono = ColorIconosInternos,
                    mostrarContrasena = mostrarRepetida,
                    alAlternarMostrarContrasena = { mostrarRepetida = !mostrarRepetida },
                    monoespaciada = mostrarRepetida
                )

                if (contrasena.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    BarraFuerza(
                        fraccion = fuerza.fraccion,
                        etiqueta = fuerza.etiqueta,
                        tiempo = fuerza.tiempo,
                        bits = fuerza.bits
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (repetida.isEmpty()) "Mínimo 10 caracteres" else if (coinciden) "✓ Las contraseñas coinciden" else "✖ No coinciden",
                        color = if (repetida.isNotEmpty() && !coinciden) Peligro else if (coinciden) Menta else TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )

                    if (contrasena.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorAcento.copy(alpha = 0.12f))
                                .clickable { mostrarDialogoCompartir = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Tarjeta 2: Parámetros y Asistencia agrupados
        ComponenteGrupo(
            etiqueta = "Parámetros y Asistencia"
        ) {
            ComponenteSelectorModal(
                titulo = "Perfil Argon2id",
                valorSeleccionado = perfilSeleccionado,
                opciones = opcionesArgon2,
                alSeleccionar = alCambiarPerfil,
                icono = Icons.Filled.Memory,
                colorIcono = ColorArgon2,
                colorTinteIcono = ColorSobreAcento,
                descripcionModal = "Parámetros KDF para la derivación de tu clave maestra"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Consejo: Generación rápida",
                alPulsar = { mostrarModalConsejo = true },
                icono = Icons.Filled.Lightbulb,
                colorIcono = ColorIconosInternos,
                colorTinteIcono = Color.White,
                valorTexto = "Tile / Widget"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Forjar la bóveda",
                alPulsar = { if (valida) alConfirmar(contrasena) },
                icono = Icons.Filled.Shield,
                colorIcono = if (valida) ColorAcento else ColorAjusteGris.copy(alpha = 0.35f),
                colorTinteIcono = if (valida) ColorSobreAcento else ColorAjusteGris,
                habilitado = valida
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Volver",
                alPulsar = alVolver,
                icono = Icons.AutoMirrored.Filled.ArrowBack,
                colorIcono = ColorAjusteGris.copy(alpha = 0.2f),
                colorTinteIcono = TextoPrincipal
            )
        }
    }

    if (mostrarModalConsejo) {
        DialogoConsejoTile(alCerrar = { mostrarModalConsejo = false })
    }

    if (mostrarDialogoCompartir) {
        DialogoAdvertenciaCompartir(
            contrasena = contrasena,
            contexto = contexto,
            alCerrar = { mostrarDialogoCompartir = false }
        )
    }
}

@Composable
private fun DialogoConsejoTile(alCerrar: () -> Unit) {
    val esOscuro = esOscuroActivo
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alCerrar() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(fondoModal)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Evita cerrar al pulsar dentro */ }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorAcento),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lightbulb,
                                contentDescription = null,
                                tint = ColorSobreAcento,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Generador Rápido",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.5.sp
                                )
                            )
                            Text(
                                text = "Tile de ajustes rápidos o Widget 1x1",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Puedes deslizar hacia abajo la barra de estado de Android y pulsar el botón «Generador rápido», o colocar el widget 1x1 «Generador Rápido» en tu pantalla de inicio.\n\nGenerará una clave ultra-segura al instante directamente en el portapapeles para pegarla aquí.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = TextoSecundario
                    )

                    Spacer(Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = alCerrar) {
                            Text(
                                text = "Entendido",
                                color = ColorAcento,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogoAdvertenciaCompartir(
    contrasena: String,
    contexto: Context,
    alCerrar: () -> Unit
) {
    val esOscuro = esOscuroActivo
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alCerrar() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(fondoModal)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Evita cerrar al pulsar dentro */ }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Peligro),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Advertencia de Seguridad",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.5.sp
                                )
                            )
                            Text(
                                text = "Resguardo de contraseña maestra",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Vas a compartir tu contraseña maestra mediante las opciones del sistema.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Si decides enviártela a ti mismo (por ejemplo en WhatsApp a tu propio chat, «Mensajes guardados» en Telegram o en tus notas), te recomendamos fuertemente memorizarla y resguardarla en un lugar seguro fuera del alcance de terceros.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = TextoSecundario
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "⚠️ La app no guarda tu contraseña. Si la pierdes, no hay forma matemática de recuperar tu bóveda.",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Peligro
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = alCerrar) {
                            Text("Cancelar", color = TextoSecundario)
                        }
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                alCerrar()
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Contraseña maestra - Bóveda Local")
                                    putExtra(Intent.EXTRA_TEXT, contrasena)
                                }
                                contexto.startActivity(Intent.createChooser(intent, "Guardar o compartir contraseña"))
                            }
                        ) {
                            Text("Entendido, Compartir", color = ColorAcento, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

