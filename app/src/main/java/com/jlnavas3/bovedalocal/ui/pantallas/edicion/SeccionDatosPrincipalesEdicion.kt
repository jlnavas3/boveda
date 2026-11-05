/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.FormateadorCampos
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
    datosTarjeta: DatosTarjetaBancaria = DatosTarjetaBancaria(),
    alCambiarDatosTarjeta: (DatosTarjetaBancaria) -> Unit = {},
    datosWifi: DatosRedWifi = DatosRedWifi(),
    alCambiarDatosWifi: (DatosRedWifi) -> Unit = {},
    datosCuentaBancaria: DatosCuentaBancaria = DatosCuentaBancaria(),
    alCambiarDatosCuentaBancaria: (DatosCuentaBancaria) -> Unit = {},
    datosIdentidad: DatosDocumentoIdentidad = DatosDocumentoIdentidad(),
    alCambiarDatosIdentidad: (DatosDocumentoIdentidad) -> Unit = {},
    datosServidor: DatosServidorSsh = DatosServidorSsh(),
    alCambiarDatosServidor: (DatosServidorSsh) -> Unit = {},
    datosWallet: DatosCriptoWallet = DatosCriptoWallet(),
    alCambiarDatosWallet: (DatosCriptoWallet) -> Unit = {},
    datosContacto: com.jlnavas3.bovedalocal.data.DatosContacto = com.jlnavas3.bovedalocal.data.DatosContacto(),
    alCambiarDatosContacto: (com.jlnavas3.bovedalocal.data.DatosContacto) -> Unit = {},
    listaEnlaces: List<EnlaceEditable>,
    ajustes: AjustesApp,
    haptica: Haptica,
    alAbrirDiccionario: (() -> Unit)? = null,
    alAbrirContrasenasComunes: (() -> Unit)? = null,
    alImportarContacto: ((com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    Column(modifier = modifier.fillMaxWidth()) {
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
            val debeSugerirNombreApp = nombreAppDetectada != null &&
                !titulo.trim().equals(nombreAppDetectada, ignoreCase = true) &&
                (titulo.isBlank() || titulo == "Nueva entrada" || com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.esTituloTecnico(titulo, urlsParaResolver))
            if (debeSugerirNombreApp) {
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
                Spacer(Modifier.height(8.dp))
            }

            when (tipo) {
                TipoEntrada.LOGIN, TipoEntrada.PASSKEY -> {
                    TarjetaCredencialesUnificada(
                        titulo = titulo,
                        alCambiarTitulo = alCambiarTitulo,
                        usuario = usuario,
                        alCambiarUsuario = alCambiarUsuario,
                        contrasena = contrasena,
                        alCambiarContrasena = alCambiarContrasena,
                        mostrarContrasena = mostrarContrasena,
                        alAlternarMostrarContrasena = alAlternarMostrarContrasena,
                        opcionesGenerador = opcionesGenerador,
                        alCambiarOpcionesGenerador = alCambiarOpcionesGenerador,
                        passkey = original?.passkey,
                        haptica = haptica,
                        alAbrirDiccionario = alAbrirDiccionario,
                        alAbrirContrasenasComunes = alAbrirContrasenasComunes,
                        correoBasePredeterminado = ajustes.aliasCorreoBasePredeterminado,
                        delimitadorAlias = ajustes.aliasCorreoDelimitador,
                        longitudSufijoAlias = ajustes.aliasCorreoLongitudSufijo
                    )
                }
                TipoEntrada.CONTACTO -> {
                    ComponenteCampoTexto(
                        valor = titulo,
                        etiqueta = "Nombre y apellidos",
                        alCambiar = alCambiarTitulo,
                        colorBordeIzquierdo = ColorAcento,
                        botonLimpiar = true
                    )
                    Spacer(Modifier.height(8.dp))
                    CampoBoveda(
                        valor = usuario,
                        etiqueta = "Teléfono principal",
                        alCambiar = alCambiarUsuario,
                        formateadorMascara = { raw ->
                            FormateadorCampos.aplicarMascaraTelefono(raw, ajustes.formatoTelefono)
                        },
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
                        colorBordeIzquierdo = ColorDatosUsuario
                    )
                }
                else -> {
                    ComponenteCampoTexto(
                        valor = titulo,
                        etiqueta = "Título",
                        alCambiar = alCambiarTitulo,
                        colorBordeIzquierdo = ColorAcento,
                        botonLimpiar = true
                    )
                }
            }

            when (tipo) {
                TipoEntrada.LOGIN, TipoEntrada.PASSKEY -> {
                    // Ya gestionado por TarjetaCredencialesUnificada
                }
                TipoEntrada.TARJETA -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionTarjeta(
                        datos = datosTarjeta,
                        alCambiar = alCambiarDatosTarjeta,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.WIFI -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionWifi(
                        datos = datosWifi,
                        alCambiar = alCambiarDatosWifi,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.CUENTA_BANCARIA -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionCuentaBancaria(
                        datos = datosCuentaBancaria,
                        alCambiar = alCambiarDatosCuentaBancaria,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.IDENTIDAD -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionIdentidad(
                        datos = datosIdentidad,
                        alCambiar = alCambiarDatosIdentidad,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.SERVIDOR -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionServidor(
                        datos = datosServidor,
                        alCambiar = alCambiarDatosServidor,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.WALLET -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionWallet(
                        datos = datosWallet,
                        alCambiar = alCambiarDatosWallet,
                        ajustes = ajustes
                    )
                }
                TipoEntrada.CONTACTO -> {
                    Spacer(Modifier.height(12.dp))
                    FormularioEdicionContacto(
                        datos = datosContacto,
                        alCambiar = alCambiarDatosContacto,
                        ajustes = ajustes,
                        alImportarContacto = alImportarContacto
                    )
                }
                TipoEntrada.NOTA -> {
                    // Para nota, gestionado en sus campos correspondientes
                }
            }
        }
}
