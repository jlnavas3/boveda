package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionCuentaBancaria
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionIdentidad
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionServidor
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionTarjeta
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWallet
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWifi
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GeneradorEnLineaEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionCamposPersonalizados
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionEtiquetasEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SelectorTipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
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
    val listaEnlaces = remember(original) {
        val items = original?.urls?.map { LanzadorEnlaces.desglosarParaEdicion(it) } ?: emptyList()
        mutableStateListOf<EnlaceEditable>().apply {
            if (items.isNotEmpty()) addAll(items)
            else add(EnlaceEditable(valor = ""))
        }
    }
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
    val scrollState = rememberScrollState()

    fun guardarEntrada() {
        if (!puedeGuardar) return
        haptica.exito()
        val urlsGuardadas = listaEnlaces.flatMap { enlace ->
            val reconstruido = LanzadorEnlaces.reconstruirDesdeEdicion(enlace)
            if (enlace.hashOriginal != null) {
                listOf(reconstruido)
            } else {
                reconstruido.split(",", "\n", ";").map { it.trim() }
            }
        }.filter { it.isNotBlank() }

        val entrada = Entrada(
            id = original?.id ?: vm.nuevoId(),
            tipo = original?.passkey?.let { TipoEntrada.PASSKEY } ?: tipo,
            titulo = titulo.trim(),
            usuario = usuario.trim(),
            contrasena = contrasena,
            urls = urlsGuardadas,
            notas = notas,
            secretoTotp = totp.trim().ifBlank { null },
            favorito = favorito,
            creadaEn = original?.creadaEn ?: 0L,
            etiquetas = etiquetas.map(::normalizarEtiqueta).filter { it.isNotEmpty() }.distinct(),
            historialContrasenas = original?.historialContrasenas ?: emptyList(),
            passkey = original?.passkey,
            camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() }
        )
        vm.guardar(entrada)
        vm.volverAtras()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera plana nativa
        BarraSuperiorPantalla(
            titulo = if (original == null) "Nueva entrada" else "Editar entrada",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                IconButton(onClick = { guardarEntrada() }, enabled = puedeGuardar) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Guardar",
                        tint = if (puedeGuardar) ColorAcento else ColorIconosInternos.copy(alpha = 0.3f)
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (original == null) "Crea y cifra un registro seguro en la bóveda" else "Modifica los datos del registro"
            )
            Spacer(Modifier.height(12.dp))

            // Selector de tipo (solo para nuevas entradas)
            if (original == null) {
                GrupoAjustes(etiqueta = "Tipo de registro") {
                    Column(modifier = Modifier.padding(14.dp)) {
                        SelectorTipoEntrada(
                            tipoActual = tipo,
                            alSeleccionarTipo = { tipo = it; haptica.tic() }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo: Datos principales
            GrupoAjustes(etiqueta = "Datos principales") {
                Column(modifier = Modifier.padding(14.dp)) {
                    ComponenteCampoTexto(
                        valor = titulo,
                        etiqueta = "Título",
                        alCambiar = { titulo = it },
                        botonLimpiar = true
                    )

                    when (tipo) {
                        TipoEntrada.LOGIN -> {
                            Spacer(Modifier.height(12.dp))
                            ComponenteCampoTexto(
                                valor = usuario,
                                etiqueta = "Usuario o correo",
                                alCambiar = { usuario = it },
                                botonLimpiar = true
                            )
                            Spacer(Modifier.height(12.dp))
                            ComponenteCampoTexto(
                                valor = contrasena,
                                etiqueta = "Contraseña",
                                alCambiar = { contrasena = it },
                                tipo = TipoCampoTexto.CONTRASENA,
                                mostrarContrasena = mostrarContrasena,
                                alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                                monoespaciada = true
                            )

                            if (contrasena.isNotEmpty()) {
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
                                    opcionesGenerador = nuevas
                                    mostrarContrasena = true
                                    contrasena = PasswordGenerator.generar(nuevas)
                                },
                                alGenerarContrasena = { nueva ->
                                    mostrarContrasena = true
                                    contrasena = nueva
                                },
                                haptica = haptica
                            )
                        }
                        TipoEntrada.TARJETA -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionTarjeta(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it }
                            )
                        }
                        TipoEntrada.WIFI -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionWifi(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it }
                            )
                        }
                        TipoEntrada.CUENTA_BANCARIA -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionCuentaBancaria(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it }
                            )
                        }
                        TipoEntrada.IDENTIDAD -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionIdentidad(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it },
                                ajustes = ajustes
                            )
                        }
                        TipoEntrada.SERVIDOR -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionServidor(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it }
                            )
                        }
                        TipoEntrada.WALLET -> {
                            Spacer(Modifier.height(12.dp))
                            FormularioEdicionWallet(
                                campos = camposPersonalizados,
                                alCambiarCampos = { camposPersonalizados = it }
                            )
                        }
                        TipoEntrada.NOTA, TipoEntrada.PASSKEY -> {
                            // Para nota y passkey, notas es el campo principal
                        }
                    }
                }
            }

            // Grupo: Sitios o aplicaciones (solo para Login)
            if (tipo == TipoEntrada.LOGIN) {
                Spacer(Modifier.height(16.dp))
                GrupoAjustes(etiqueta = "Sitios o aplicaciones") {
                    Column(modifier = Modifier.padding(14.dp)) {
                        listaEnlaces.forEachIndexed { index, enlace ->
                            val paquete = remember(enlace.valor) { LanzadorEnlaces.extraerPaquete(enlace.valor) }
                            val esApp = paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)
                            val nombreApp = remember(enlace.valor, contexto) {
                                if (paquete != null && esApp) LanzadorEnlaces.obtenerNombreApp(contexto, paquete) else null
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            ) {
                                ComponenteCampoTexto(
                                    valor = enlace.valor,
                                    etiqueta = if (listaEnlaces.size > 1) "Sitio web o app #${index + 1}" else "Sitio web o app",
                                    alCambiar = { nuevoTexto ->
                                        listaEnlaces[index] = enlace.copy(valor = nuevoTexto)
                                    },
                                    tipo = TipoCampoTexto.ENLACE,
                                    trailingIcon = {
                                        if (listaEnlaces.size > 1 || enlace.valor.isNotEmpty()) {
                                            IconButton(
                                                onClick = {
                                                    if (listaEnlaces.size > 1) {
                                                        listaEnlaces.removeAt(index)
                                                    } else {
                                                        listaEnlaces[0] = EnlaceEditable(valor = "")
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Close,
                                                    contentDescription = "Eliminar sitio o app",
                                                    tint = TextoSecundario,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                )

                                if (enlace.hashOriginal != null) {
                                    Spacer(Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(FormaPequena)
                                            .background(fondoBadgeParaTema(ColorPasskeys))
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Security,
                                            contentDescription = "Certificado DAL",
                                            tint = ColorPasskeys,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "Certificado DAL: ${enlace.hashOriginal}",
                                            style = EstiloMono.copy(fontSize = 11.sp),
                                            color = colorLegibleParaTema(ColorPasskeys),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                if (esApp && nombreApp != null) {
                                    Spacer(Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(FormaPequena)
                                            .background(fondoBadgeParaTema(Menta))
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Android,
                                            contentDescription = "App detectada",
                                            tint = Menta,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "App detectada: $nombreApp",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                            color = colorLegibleParaTema(Menta),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        BotonBorde(
                            texto = "Añadir otro sitio o app",
                            icono = Icons.Filled.Add,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listaEnlaces.add(EnlaceEditable(valor = ""))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                GrupoAjustes(etiqueta = "Autenticador 2FA (Opcional)") {
                    Column(modifier = Modifier.padding(14.dp)) {
                        ComponenteCampoTexto(
                            valor = totp,
                            etiqueta = "Secreto TOTP en Base32",
                            alCambiar = { totp = it.uppercase() },
                            monoespaciada = true
                        )
                        if (!totpValido) {
                            Spacer(Modifier.height(6.dp))
                            Text("Ese secreto no es Base32 válido", color = Peligro, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Grupo: Notas
            Spacer(Modifier.height(16.dp))
            GrupoAjustes(etiqueta = "Notas") {
                Column(modifier = Modifier.padding(14.dp)) {
                    ComponenteCampoTexto(
                        valor = notas,
                        etiqueta = "Notas y detalles",
                        alCambiar = { notas = it },
                        tipo = TipoCampoTexto.MULTILINEA
                    )
                }
            }

            // Grupo: Campos adicionales
            Spacer(Modifier.height(16.dp))
            val etiquetasBase = remember(tipo) { GestorCamposBase.etiquetasBaseParaTipo(tipo) }
            GrupoAjustes(etiqueta = if (etiquetasBase.isEmpty()) "Campos personalizados" else "Campos adicionales") {
                Column(modifier = Modifier.padding(14.dp)) {
                    SeccionCamposPersonalizados(
                        camposPersonalizados = camposPersonalizados,
                        alCambiarCampos = { camposPersonalizados = it },
                        etiquetasBase = etiquetasBase,
                        ajustes = ajustes,
                        haptica = haptica
                    )
                }
            }

            // Grupo: Organización
            Spacer(Modifier.height(16.dp))
            GrupoAjustes(etiqueta = "Organización") {
                Column(modifier = Modifier.padding(14.dp)) {
                    SeccionEtiquetasEdicion(
                        etiquetas = etiquetas,
                        alCambiarEtiquetas = { etiquetas = it },
                        etiquetasSugeridas = etiquetasSugeridas
                    )

                    Spacer(Modifier.height(14.dp))
                    SeparadorFilaSimple()
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Favorito", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Aparece fijado arriba en la lista", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                        }
                        SwitchBoveda(
                            checked = favorito,
                            onCheckedChange = { favorito = it; haptica.tic() }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
