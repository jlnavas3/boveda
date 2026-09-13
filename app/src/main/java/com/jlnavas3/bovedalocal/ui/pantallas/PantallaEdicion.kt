package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GeneradorEnLineaEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionCamposPersonalizados
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionCuentaBancaria
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionIdentidad
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionServidor
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionTarjeta
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWallet
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWifi
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionEtiquetasEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SelectorTipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun PantallaEdicion(vm: VaultViewModel, id: String?, contrasenaInicial: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val original = remember(id) { id?.let { vm.entrada(it) } }

    var tipo by remember { mutableStateOf(original?.tipo ?: TipoEntrada.LOGIN) }
    var titulo by remember { mutableStateOf(original?.titulo ?: "") }
    var usuario by remember { mutableStateOf(original?.usuario ?: "") }
    var contrasena by remember { mutableStateOf(original?.contrasena ?: contrasenaInicial) }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var urls by remember { mutableStateOf(original?.urls?.joinToString(", ") ?: "") }
    var notas by remember { mutableStateOf(original?.notas ?: "") }
    var totp by remember { mutableStateOf(original?.secretoTotp ?: "") }
    var favorito by remember { mutableStateOf(original?.favorito ?: false) }
    var etiquetas by remember { mutableStateOf(original?.etiquetas ?: emptyList()) }
    var camposPersonalizados by remember { mutableStateOf(original?.camposPersonalizados ?: emptyList()) }
    var opcionesGenerador by remember { mutableStateOf(OpcionesGenerador()) }
    val etiquetasSugeridas = remember { vm.etiquetasUsadas() }

    val totpValido = totp.isBlank() || Base32.esValido(totp)
    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val esComun = remember(contrasena) { ContrasenasComunes.esComun(contexto, contrasena) }
    val puedeGuardar = titulo.isNotBlank() && totpValido

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = if (original == null) "Nueva Entrada" else "Editar Entrada",
            subtitulo = if (original == null) "Crea y cifra un registro en la bóveda" else "Modifica los datos del registro",
            alVolver = { vm.volverAtras() }
        )

        if (original == null) {
            EtiquetaSeccion("Tipo")
            Spacer(Modifier.height(8.dp))
            SelectorTipoEntrada(
                tipoActual = tipo,
                alSeleccionarTipo = { tipo = it; haptica.tic() }
            )
            Spacer(Modifier.height(16.dp))
        }

        CampoBoveda(valor = titulo, etiqueta = "Título", alCambiar = { titulo = it })
        Spacer(Modifier.height(12.dp))

        when (tipo) {
            TipoEntrada.LOGIN -> {
                CampoBoveda(valor = usuario, etiqueta = "Usuario o correo", alCambiar = { usuario = it })
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = contrasena,
                    etiqueta = "Contraseña",
                    alCambiar = { contrasena = it },
                    esContrasena = true,
                    mostrarContrasena = mostrarContrasena,
                    alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                    monoespaciada = true
                )
                Spacer(Modifier.height(10.dp))

                GeneradorEnLineaEdicion(
                    opcionesGenerador = opcionesGenerador,
                    alCambiarOpciones = { opcionesGenerador = it },
                    alGenerarContrasena = { contrasena = it },
                    haptica = haptica
                )

                if (contrasena.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    BarraFuerza(fuerza.fraccion, fuerza.etiqueta, fuerza.tiempo)
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
                CampoBoveda(
                    valor = urls,
                    etiqueta = "Sitios o paquetes (separados por comas)",
                    alCambiar = { urls = it }
                )
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = totp,
                    etiqueta = "Secreto TOTP en Base32 (opcional)",
                    alCambiar = { totp = it.uppercase() },
                    monoespaciada = true
                )
                if (!totpValido) {
                    Spacer(Modifier.height(6.dp))
                    Text("Ese secreto no es Base32 válido", color = Peligro, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(12.dp))
            }
            TipoEntrada.TARJETA -> {
                FormularioEdicionTarjeta(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it }
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.WIFI -> {
                FormularioEdicionWifi(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it }
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                FormularioEdicionCuentaBancaria(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it }
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.IDENTIDAD -> {
                FormularioEdicionIdentidad(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it },
                    ajustes = ajustes
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.SERVIDOR -> {
                FormularioEdicionServidor(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it }
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.WALLET -> {
                FormularioEdicionWallet(
                    campos = camposPersonalizados,
                    alCambiarCampos = { camposPersonalizados = it }
                )
                Spacer(Modifier.height(14.dp))
            }
            TipoEntrada.NOTA, TipoEntrada.PASSKEY -> {
                // Para nota y passkey, notas es el campo principal
            }
        }

        CampoBoveda(valor = notas, etiqueta = "Notas", alCambiar = { notas = it }, varias = true)
        Spacer(Modifier.height(16.dp))

        val etiquetasBase = remember(tipo) { GestorCamposBase.etiquetasBaseParaTipo(tipo) }
        SeccionCamposPersonalizados(
            camposPersonalizados = camposPersonalizados,
            alCambiarCampos = { camposPersonalizados = it },
            etiquetasBase = etiquetasBase,
            ajustes = ajustes,
            haptica = haptica
        )
        Spacer(Modifier.height(16.dp))

        SeccionEtiquetasEdicion(
            etiquetas = etiquetas,
            alCambiarEtiquetas = { etiquetas = it },
            etiquetasSugeridas = etiquetasSugeridas
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaTarjeta)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                        Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Favorito", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
                Text("Aparece arriba en la lista", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = favorito,
                onCheckedChange = { favorito = it; haptica.tic() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ColorSobreAcento,
                    checkedTrackColor = Ambar,
                    checkedBorderColor = Ambar,
                    uncheckedThumbColor = TextoSecundario,
                    uncheckedTrackColor = SuperficieAlta,
                    uncheckedBorderColor = TextoSecundario
                )
            )
        }

        Spacer(Modifier.height(20.dp))
        BotonColorido(
            texto = if (original == null) "Guardar entrada" else "Guardar cambios",
            color = ColorAcento,
            icono = Icons.Filled.Check,
            activo = puedeGuardar
        ) {
            haptica.exito()
            val entrada = Entrada(
                id = original?.id ?: vm.nuevoId(),
                tipo = original?.passkey?.let { TipoEntrada.PASSKEY } ?: tipo,
                titulo = titulo.trim(),
                usuario = usuario.trim(),
                contrasena = contrasena,
                urls = urls.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                notas = notas,
                secretoTotp = totp.trim().ifBlank { null },
                favorito = favorito,
                creadaEn = original?.creadaEn ?: 0L,
                etiquetas = etiquetas.map(::normalizarEtiqueta).filter { it.isNotEmpty() }.distinct(),
                passkey = original?.passkey,
                camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() }
            )
            vm.guardar(entrada)
            if (original == null) vm.volverAtras() else vm.ir(Pantalla.Detalle(entrada.id))
        }
        Spacer(Modifier.height(12.dp))
        BotonBorde(
            texto = "Cancelar",
            icono = Icons.Filled.Close
        ) {
            vm.volverAtras()
        }
        Spacer(Modifier.height(32.dp))
    }
}
