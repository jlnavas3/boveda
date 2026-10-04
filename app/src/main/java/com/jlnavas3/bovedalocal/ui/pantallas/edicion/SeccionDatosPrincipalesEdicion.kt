package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

@Composable
fun SeccionDatosPrincipalesEdicion(
    titulo: String,
    alCambiarTitulo: (String) -> Unit,
    tipo: TipoEntrada,
    original: Entrada?,
    usuario: String,
    alCambiarUsuario: (String) -> Unit,
    contrasena: String,
    alCambiarContrasena: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpcionesGenerador: (OpcionesGenerador) -> Unit,
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCamposPersonalizados: (List<CampoPersonalizado>) -> Unit,
    listaEnlaces: List<EnlaceEditable>,
    ajustes: AjustesApp,
    haptica: Haptica
) {
    val contexto = LocalContext.current

    GrupoAjustes(etiqueta = "Datos principales") {
        Column(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = titulo,
                etiqueta = "Título",
                alCambiar = alCambiarTitulo,
                colorBordeIzquierdo = ColorAcento,
                botonLimpiar = true
            )

            val urlsParaResolver = remember(listaEnlaces, original) {
                val reconstruidas = listaEnlaces.map { LanzadorEnlaces.reconstruirDesdeEdicion(it) }.filter { it.isNotBlank() }
                if (reconstruidas.isEmpty() && original != null) original.urls else reconstruidas
            }
            val entradaParaResolver = remember(urlsParaResolver, original, titulo) {
                Entrada(
                    id = original?.id ?: "",
                    titulo = titulo,
                    usuario = usuario,
                    contrasena = contrasena,
                    urls = urlsParaResolver,
                    passkey = original?.passkey
                )
            }
            val paqueteDetectado = remember(entradaParaResolver, contexto) {
                GestorAppsInstaladas.resolverPaqueteApp(contexto, entradaParaResolver)
            }
            val nombreAppDetectada = remember(paqueteDetectado, contexto) {
                if (paqueteDetectado != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDetectado)) {
                    LanzadorEnlaces.obtenerNombreApp(contexto, paqueteDetectado)
                } else null
            }
            if (nombreAppDetectada != null && !titulo.trim().equals(nombreAppDetectada, ignoreCase = true)) {
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ColorAcento.copy(alpha = 0.12f))
                        .clickable {
                            alCambiarTitulo(nombreAppDetectada)
                            haptica.tic()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoFixHigh,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Usar \"$nombreAppDetectada\"",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = ColorAcento,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            when (tipo) {
                TipoEntrada.LOGIN, TipoEntrada.PASSKEY -> {
                    SeccionCredencialesEdicion(
                        passkey = original?.passkey,
                        usuario = usuario,
                        alCambiarUsuario = alCambiarUsuario,
                        contrasena = contrasena,
                        alCambiarContrasena = alCambiarContrasena,
                        mostrarContrasena = mostrarContrasena,
                        alAlternarMostrarContrasena = alAlternarMostrarContrasena,
                        opcionesGenerador = opcionesGenerador,
                        alCambiarOpcionesGenerador = alCambiarOpcionesGenerador,
                        haptica = haptica
                    )
                }
                TipoEntrada.TARJETA -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionTarjeta(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados
                    )
                }
                TipoEntrada.WIFI -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionWifi(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados
                    )
                }
                TipoEntrada.CUENTA_BANCARIA -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionCuentaBancaria(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados
                    )
                }
                TipoEntrada.IDENTIDAD -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionIdentidad(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.SERVIDOR -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionServidor(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados
                    )
                }
                TipoEntrada.WALLET -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionWallet(
                        campos = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados
                    )
                }
                TipoEntrada.NOTA -> {
                    // Para nota, el campo principal es la nota
                }
            }
        }
    }
}
