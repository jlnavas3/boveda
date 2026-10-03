package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun SeccionCredencialesEdicion(
    passkey: DatosPasskey?,
    usuario: String,
    alCambiarUsuario: (String) -> Unit,
    contrasena: String,
    alCambiarContrasena: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpcionesGenerador: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    val contexto = LocalContext.current

    if (passkey != null) {
        Spacer(Modifier.height(12.dp))
        val colorFondoCampo = ColorCampoAjustes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaPequena)
                .background(colorFondoCampo)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Fingerprint,
                    contentDescription = "Llave de paso activa",
                    tint = colorLegibleParaTema(ColorDatosPasskey),
                    modifier = Modifier.size(22.dp)
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
                    Text(
                        text = "Algoritmo: ${passkey.algoritmo}",
                        style = EstiloMono.copy(fontSize = 11.sp),
                        color = colorLegibleParaTema(ColorDatosPasskey).copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(
                modifier = Modifier.matchParentSize()
            ) {
                Box(
                    modifier = Modifier
                        .width(4.5.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .background(ColorDatosPasskey)
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))
    ComponenteCampoTexto(
        valor = usuario,
        etiqueta = "Usuario o correo",
        alCambiar = alCambiarUsuario,
        mostrarIcono = true,
        icono = Icons.Filled.Person,
        colorBordeIzquierdo = ColorDatosUsuario,
        botonLimpiar = true
    )

    Spacer(Modifier.height(12.dp))
    ComponenteCampoTexto(
        valor = contrasena,
        etiqueta = "Contraseña",
        alCambiar = alCambiarContrasena,
        tipo = TipoCampoTexto.CONTRASENA,
        mostrarIcono = true,
        icono = Icons.Filled.Lock,
        colorBordeIzquierdo = ColorDatosContrasena,
        mostrarContrasena = mostrarContrasena,
        alAlternarMostrarContrasena = alAlternarMostrarContrasena,
        monoespaciada = true
    )

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
            Spacer(Modifier.height(6.dp))
            Text(
                "Está entre las contraseñas más repetidas en filtraciones conocidas: cualquiera la prueba primero.",
                color = Peligro,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    Spacer(Modifier.height(12.dp))

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

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun SeccionCredencialesEdicionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        SeccionCredencialesEdicion(
            passkey = null,
            usuario = "usuario@correo.com",
            alCambiarUsuario = {},
            contrasena = "K8#mP9_xL2!vQ4zR",
            alCambiarContrasena = {},
            mostrarContrasena = false,
            alAlternarMostrarContrasena = {},
            opcionesGenerador = OpcionesGenerador(),
            alCambiarOpcionesGenerador = {},
            haptica = remember { Haptica(contexto) }
        )
    }
}

