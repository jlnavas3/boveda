package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.FocusDirection
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.IconoAlternarContrasena
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Tarjeta continua y compacta (estilo One UI / iOS) que agrupa Título, Usuario y Contraseña,
 * reduciendo sustancialmente el consumo de espacio vertical con generador rápido [✨]
 * y panel avanzado desplegable bajo demanda.
 */
@Composable
fun TarjetaCredencialesUnificada(
    titulo: String,
    alCambiarTitulo: (String) -> Unit,
    usuario: String,
    alCambiarUsuario: (String) -> Unit,
    contrasena: String,
    alCambiarContrasena: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpcionesGenerador: (OpcionesGenerador) -> Unit,
    passkey: DatosPasskey? = null,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var mostrarOpcionesAvanzadas by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Insignia Passkey si está asociada
        if (passkey != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaPequena)
                    .background(ColorCampoAjustes)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Fingerprint,
                        contentDescription = "Llave de paso activa",
                        tint = colorLegibleParaTema(ColorDatosPasskey),
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Llave de paso registrada",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = colorLegibleParaTema(ColorDatosPasskey)
                        )
                        Text(
                            text = "rpId: ${passkey.rpId}",
                            style = EstiloMono.copy(fontSize = 11.sp),
                            color = colorLegibleParaTema(ColorDatosPasskey).copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(modifier = Modifier.matchParentSize()) {
                    Box(
                        modifier = Modifier
                            .width(4.5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .background(ColorDatosPasskey)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }

        // Tarjeta continua unificada
        val formaTarjeta = FormaCampo
        val bordeColor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes.copy(alpha = 0.4f)
        val grosorEfectivo = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(formaTarjeta)
                .background(ColorCampoAjustes)
                .border(grosorEfectivo, bordeColor, formaTarjeta)
        ) {
            // Fila 1: Título
            ComponenteCampoTexto(
                valor = titulo,
                etiqueta = "Título",
                alCambiar = alCambiarTitulo,
                sinFondo = true,
                mostrarIcono = true,
                icono = Icons.Filled.Title,
                colorIcono = ColorAcento,
                botonLimpiar = true,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            // Separador fino
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(0.6.dp)
                    .background(ColorSeparadorAjustes.copy(alpha = 0.5f))
            )

            // Fila 2: Usuario o correo
            ComponenteCampoTexto(
                valor = usuario,
                etiqueta = "Usuario o correo",
                alCambiar = alCambiarUsuario,
                sinFondo = true,
                mostrarIcono = true,
                icono = Icons.Filled.Person,
                colorIcono = ColorDatosUsuario,
                botonLimpiar = true,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            // Separador fino
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(0.6.dp)
                    .background(ColorSeparadorAjustes.copy(alpha = 0.5f))
            )

            // Fila 3: Contraseña con Varita mágica integrada y Alternador de visibilidad
            ComponenteCampoTexto(
                valor = contrasena,
                etiqueta = "Contraseña",
                alCambiar = alCambiarContrasena,
                tipo = TipoCampoTexto.CONTRASENA,
                sinFondo = true,
                mostrarIcono = true,
                icono = Icons.Filled.Lock,
                colorIcono = ColorDatosContrasena,
                mostrarContrasena = mostrarContrasena,
                alAlternarMostrarContrasena = alAlternarMostrarContrasena,
                monoespaciada = true,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Varita mágica rápida para generar
                        IconButton(
                            onClick = {
                                haptica.toque()
                                if (!mostrarContrasena) alAlternarMostrarContrasena()
                                alCambiarContrasena(PasswordGenerator.generar(opcionesGenerador))
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "Generar contraseña rápidamente",
                                tint = ColorAcento,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Alternar visibilidad (ojito)
                        IconoAlternarContrasena(
                            esVisible = mostrarContrasena,
                            onToggle = alAlternarMostrarContrasena
                        )
                    }
                }
            )
        }

        // Fuerza de contraseña (si hay contraseña ingresada)
        if (contrasena.isNotEmpty()) {
            val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
            val esComun = remember(contrasena) { ContrasenasComunes.esComun(contexto, contrasena) }

            Spacer(Modifier.height(8.dp))
            BarraFuerza(
                fraccion = fuerza.fraccion,
                etiqueta = fuerza.etiqueta,
                tiempo = fuerza.tiempo,
                bits = fuerza.bits
            )
            if (esComun) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Está entre las contraseñas más repetidas en filtraciones conocidas: cualquiera la prueba primero.",
                    color = Peligro,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Botón desplegable para opciones avanzadas del generador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    haptica.tic()
                    mostrarOpcionesAvanzadas = !mostrarOpcionesAvanzadas
                }
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = TextoSecundario,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = if (mostrarOpcionesAvanzadas) "Ocultar opciones del generador" else "Personalizar generador (${opcionesGenerador.longitud} car.)",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextoSecundario
                )
            }
            Icon(
                imageVector = if (mostrarOpcionesAvanzadas) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        AnimatedVisibility(
            visible = mostrarOpcionesAvanzadas,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                GeneradorEnLineaEdicion(
                    opcionesGenerador = opcionesGenerador,
                    alCambiarOpciones = { nuevas ->
                        alCambiarOpcionesGenerador(nuevas)
                        if (!mostrarContrasena) alAlternarMostrarContrasena()
                        alCambiarContrasena(PasswordGenerator.generar(nuevas))
                    },
                    alGenerarContrasena = { nueva ->
                        if (!mostrarContrasena) alAlternarMostrarContrasena()
                        alCambiarContrasena(nueva)
                    },
                    haptica = haptica
                )
            }
        }
    }
}
