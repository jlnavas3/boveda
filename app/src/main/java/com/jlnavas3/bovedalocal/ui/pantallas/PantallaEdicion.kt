package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionCuentaBancaria
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionIdentidad
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionServidor
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionTarjeta
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWallet
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FormularioEdicionWifi
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionCamposPersonalizados
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionCredencialesEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionOrganizacionEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionSitiosYAppsEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SelectorTipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.DialogoCrearEditarColeccion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SeccionTotpEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.SelectorAppModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

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
        }
    }
    var mostrarSelectorApp by remember { mutableStateOf(false) }
    var indiceEnlaceSeleccionado by remember { mutableStateOf<Int?>(null) }
    var listaAppsInstaladas by remember { mutableStateOf<List<AppInstalada>>(emptyList()) }

    LaunchedEffect(Unit) {
        listaAppsInstaladas = GestorAppsInstaladas.obtenerAppsInstaladas(contexto)
    }
    var notas by remember { mutableStateOf(original?.notas ?: "") }
    var totp by remember { mutableStateOf(original?.secretoTotp ?: "") }
    var mostrarSecretoTotp by remember { mutableStateOf(false) }
    var favorito by remember { mutableStateOf(original?.favorito ?: false) }
    var ignoradaEnSalud by remember { mutableStateOf(original?.ignoradaEnSalud ?: false) }
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()
    val coleccionesDisponibles = remember(estadoBoveda) {
        (estadoBoveda as? EstadoBoveda.Desbloqueada)?.colecciones ?: emptyList()
    }
    var colecciones by remember { mutableStateOf(original?.colecciones ?: emptyList()) }
    var mostrarDialogoNuevaColeccion by remember { mutableStateOf(false) }

    var etiquetas by remember { mutableStateOf(original?.etiquetas ?: emptyList()) }
    var camposPersonalizados by remember { mutableStateOf(original?.camposPersonalizados ?: emptyList()) }
    var opcionesGenerador by remember { mutableStateOf(OpcionesGenerador()) }
    val etiquetasSugeridas = remember { vm.etiquetasUsadas() }

    val totpValido = totp.isBlank() || Base32.esValido(totp)
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
            camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() },
            ignoradaEnSalud = ignoradaEnSalud,
            colecciones = colecciones
        )
        vm.guardar(entrada)
        vm.volverAtras()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Cabecera plana nativa
            BarraSuperiorPantalla(
                titulo = if (original == null) "Nueva entrada" else "Editar entrada",
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {}
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .reboteElastico()
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
                        colorBordeIzquierdo = ColorAcento,
                        botonLimpiar = true
                    )

                    val urlsParaResolver = remember(listaEnlaces.toList(), original) {
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
                                    titulo = nombreAppDetectada
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
                                alCambiarUsuario = { usuario = it },
                                contrasena = contrasena,
                                alCambiarContrasena = { contrasena = it },
                                mostrarContrasena = mostrarContrasena,
                                alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena },
                                opcionesGenerador = opcionesGenerador,
                                alCambiarOpcionesGenerador = { opcionesGenerador = it },
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
                        TipoEntrada.NOTA -> {
                            // Para nota, el campo principal es la nota
                        }
                    }
                }
            }

            // Grupo: Sitios o aplicaciones
            if (tipo == TipoEntrada.LOGIN || tipo == TipoEntrada.PASSKEY) {
                Spacer(Modifier.height(16.dp))
                SeccionSitiosYAppsEdicion(
                    listaEnlaces = listaEnlaces,
                    alSolicitarExplorarApp = { idx ->
                        indiceEnlaceSeleccionado = idx
                        mostrarSelectorApp = true
                    }
                )

                Spacer(Modifier.height(16.dp))
                SeccionTotpEdicion(
                    totp = totp,
                    alCambiarTotp = { totp = it },
                    mostrarSecretoTotp = mostrarSecretoTotp,
                    alAlternarMostrarSecreto = {
                        haptica.tic()
                        mostrarSecretoTotp = !mostrarSecretoTotp
                    },
                    totpValido = totpValido
                )
            }

            // Grupo: Notas
            Spacer(Modifier.height(16.dp))
            GrupoAjustes(etiqueta = "Notas") {
                Column(modifier = Modifier.padding(14.dp)) {
                    ComponenteCampoTexto(
                        valor = notas,
                        etiqueta = "Notas y detalles",
                        alCambiar = { notas = it },
                        tipo = TipoCampoTexto.MULTILINEA,
                        colorBordeIzquierdo = ColorAcento
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
            SeccionOrganizacionEdicion(
                coleccionesDisponibles = coleccionesDisponibles,
                coleccionesSeleccionadas = colecciones,
                alCambiarColecciones = { colecciones = it },
                alCrearNuevaColeccion = { mostrarDialogoNuevaColeccion = true },
                etiquetas = etiquetas,
                alCambiarEtiquetas = { etiquetas = it },
                etiquetasSugeridas = etiquetasSugeridas,
                favorito = favorito,
                alAlternarFavorito = { favorito = it; haptica.tic() },
                ignoradaEnSalud = ignoradaEnSalud,
                alAlternarIgnoradaEnSalud = { ignoradaEnSalud = it; haptica.tic() }
            )

            Spacer(Modifier.height(80.dp))
        }
    }

    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    FloatingActionButton(
        onClick = {
            if (puedeGuardar) {
                guardarEntrada()
            } else {
                haptica.error()
                vm.avisar("Introduce un título para guardar")
            }
        },
        containerColor = ColorAcento,
        contentColor = ColorSobreAcento,
        shape = formaFab,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .navigationBarsPadding()
            .imePadding()
            .padding(20.dp)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Guardar",
            modifier = Modifier.size(24.dp)
        )
    }
}

    if (mostrarSelectorApp) {
        SelectorAppModal(
            alDescartar = { mostrarSelectorApp = false },
            alSeleccionarApp = { paquete ->
                val nombreApp = LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
                if (titulo.isBlank() || titulo.trim() == "Nueva entrada") {
                    if (!nombreApp.isNullOrBlank()) {
                        titulo = nombreApp
                    }
                }
                val urlApp = if (paquete.startsWith("android://")) paquete else "android://$paquete"
                val idx = indiceEnlaceSeleccionado
                if (idx != null && idx in listaEnlaces.indices) {
                    listaEnlaces[idx] = listaEnlaces[idx].copy(valor = urlApp)
                } else {
                    if (listaEnlaces.size == 1 && listaEnlaces[0].valor.isBlank()) {
                        listaEnlaces[0] = EnlaceEditable(valor = urlApp)
                    } else {
                        listaEnlaces.add(EnlaceEditable(valor = urlApp))
                    }
                }
                mostrarSelectorApp = false
            }
        )
    }

    if (mostrarDialogoNuevaColeccion) {
        DialogoCrearEditarColeccion(
            alGuardar = { nombreCol, iconoCol, colorHexCol ->
                val nueva = vm.crearColeccion(nombreCol, iconoCol, colorHexCol)
                colecciones = colecciones + nueva.id
                mostrarDialogoNuevaColeccion = false
            },
            alDescartar = { mostrarDialogoNuevaColeccion = false }
        )
    }
}
