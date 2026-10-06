package com.jlnavas3.bovedalocal.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAcercaDe
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.titulos.PantallaNormalizadorTitulos
import com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas.PantallaReglasNormalizacion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesAutodestruccion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesCamara
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesIndice
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesSenuelo
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesWidget
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaArgon2id
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAvanzada
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionAnimacion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionWidgetTotp
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaColoresDatos
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaConfirmarMigracion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaCopiaSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaCsvGoogle
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDesbloqueo
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaEscaner
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaExportarSelectivo
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaFormas
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaFormatosCampos
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaHistorialClaves
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaKitEmergencia
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaLista
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaOnboarding
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaOrganizacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaRegistro
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaSaludBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaTema
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaTileRapido
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidget1x1Comportamiento
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidget1x1Modo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidgetTotpAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.PantallaAjustesAutocompletado
import com.jlnavas3.bovedalocal.ui.pantallas.avanzada.PantallaColoresIds
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.PantallaGestionCategorias
import com.jlnavas3.bovedalocal.ui.pantallas.copia.PantallaCopiaAutomatica
import com.jlnavas3.bovedalocal.ui.pantallas.cxf.PantallaConfirmarImportacionCxf
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.PantallaCamaraQr
import com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasBorde
import com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasCurvatura
import com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasEspaciado
import com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasPresets
import com.jlnavas3.bovedalocal.ui.pantallas.historial.PantallaAjustesHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.identidades.PantallaGestionIdentidades
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceCresta
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceHaptica
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceOla
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceResaltado
import com.jlnavas3.bovedalocal.ui.pantallas.tema.PantallaLaboratorioTemas
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaEscala
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaEspaciado
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaFamilia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaPeso
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaPresets
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria

@Composable
fun RaizBoveda(vm: VaultViewModel, actividad: FragmentActivity) {
    val pantalla by vm.pantalla.collectAsStateWithLifecycle()
    val estado by vm.estado.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val aviso by vm.aviso.collectAsStateWithLifecycle()
    val cuentaAtras by vm.cuentaAtrasPortapapeles.collectAsStateWithLifecycle()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val esRetroceso by vm.navegandoAtras.collectAsStateWithLifecycle()
    val anfitrion = remember { SnackbarHostState() }

    // Sin esto, atrás cerraba la app desde generador, passkeys o ajustes.
    // En lista, desbloqueo y onboarding no lo tocamos: ahí atrás sí sale de la app
    // (y desde el desbloqueo jamás debe entrar a la bóveda).
    val esRaiz = pantalla is Pantalla.Lista ||
        pantalla is Pantalla.Desbloqueo ||
        pantalla is Pantalla.Onboarding
    BackHandler(enabled = !esRaiz) {
        vm.volverAtras()
    }

    LaunchedEffect(Unit) { vm.vigilarInactividad() }

    val ofrecerBiometria by vm.ofrecerBiometria.collectAsStateWithLifecycle()
    // Se pregunta cuando toca ofrecerla, no al arrancar la app: así cuenta una huella
    // registrada hace un minuto, y un sensor ocupado en el arranque no la esconde para siempre.
    val modoOfrecido = remember(ofrecerBiometria) {
        if (ofrecerBiometria) FlujoBiometria.modoRecomendado(Biometria.capacidad(actividad)) else null
    }
    if (ofrecerBiometria && modoOfrecido != null) {
        DialogoOfrecerBiometria(vm, actividad, modoOfrecido)
    }
    // Sin huella ni PIN utilizables no hay nada que ofrecer: pasamos directo al
    // siguiente paso en vez de dejar la oferta colgada para siempre.
    LaunchedEffect(ofrecerBiometria, modoOfrecido) {
        if (ofrecerBiometria && modoOfrecido == null) vm.cerrarOfertaBiometria()
    }

    val ofrecerGestor by vm.ofrecerGestor.collectAsStateWithLifecycle()
    if (ofrecerGestor) {
        DialogoOfrecerGestor(vm, actividad)
    }

    val uriBvda by vm.uriBvdaPendiente.collectAsStateWithLifecycle()
    if (uriBvda != null && estado is EstadoBoveda.Desbloqueada) {
        DialogoContrasena(
            titulo = "Importar copia de seguridad",
            descripcion = "Se ha abierto una copia cifrada (.bvda). Escribe la contraseña con la que fue protegida para incorporar sus entradas en tu bóveda.",
            textoBoton = "Importar",
            alConfirmar = { clave ->
                val uri = uriBvda
                vm.descartarUriBvdaPendiente()
                if (uri != null) {
                    vm.importar(clave) {
                        actividad.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                            ?: throw IllegalStateException("No se pudo leer el archivo")
                    }
                }
            },
            alCancelar = {
                vm.descartarUriBvdaPendiente()
            }
        )
    }

    LaunchedEffect(estado) {
        if (estado is EstadoBoveda.Bloqueada && pantalla !is Pantalla.Desbloqueo) {
            vm.ir(Pantalla.Desbloqueo)
        }
    }

    LaunchedEffect(error) {
        error?.let {
            anfitrion.showSnackbar(it)
            vm.limpiarError()
        }
    }

    LaunchedEffect(aviso) {
        aviso?.let {
            anfitrion.showSnackbar(it)
            vm.limpiarAviso()
        }
    }

    Scaffold(
        containerColor = Obsidiana,
        snackbarHost = { SnackbarHost(anfitrion) }
    ) { relleno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .imePadding()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Initial)
                            vm.registrarInteraccion()
                        }
                    }
                }
        ) {
            AnimatedContent(
                targetState = pantalla,
                transitionSpec = {
                    // Adelante: la pantalla nueva entra desde la derecha; la actual sale por la izquierda.
                    // Atrás: la pantalla anterior entra desde la izquierda; la actual sale por la derecha.
                    val signo = if (esRetroceso) -1 else 1
                    val animOffset = tween<IntOffset>(durationMillis = 180, easing = FastOutSlowInEasing)
                    val animFade = tween<Float>(durationMillis = 180, easing = FastOutSlowInEasing)
                    val entrada = slideInHorizontally(
                        animationSpec = animOffset
                    ) { ancho -> signo * (ancho / 4) } + fadeIn(animationSpec = animFade)
                    val salida = slideOutHorizontally(
                        animationSpec = animOffset
                    ) { ancho -> signo * (-ancho / 6) } + fadeOut(animationSpec = animFade)
                    entrada togetherWith salida
                },
                label = "navegacion"
            ) { destino ->
                when (destino) {
                    Pantalla.Onboarding -> PantallaOnboarding(vm, actividad)
                    Pantalla.Desbloqueo -> PantallaDesbloqueo(vm, actividad)
                    Pantalla.Lista -> PantallaLista(vm, estado)
                    is Pantalla.Detalle -> PantallaDetalle(vm, destino.id, destino.idsContexto, destino.modoComparacion)
                    is Pantalla.Editar -> PantallaEdicion(
                        vm,
                        destino.id,
                        destino.contrasenaInicial,
                        destino.tituloInicial,
                        destino.urlInicial
                    )
                    Pantalla.Generador -> PantallaGenerador(vm)
                    Pantalla.Passkeys -> PantallaPasskeys(vm)
                    Pantalla.Autenticador -> PantallaAutenticador(vm, estado)
                    is Pantalla.Escaner -> PantallaEscaner(vm, actividad, destino.entradaDestino, destino.soloManual)
                    is Pantalla.CamaraQr -> PantallaCamaraQr(vm, actividad, destino.entradaDestino)
                    is Pantalla.Ajustes -> PantallaAjustes(vm, actividad, destino.seccionId)
                    is Pantalla.AjustesIndice -> PantallaAjustesIndice(vm, destino.seccionId)
                    is Pantalla.IndiceOla -> PantallaIndiceOla(vm)
                    is Pantalla.IndiceCresta -> PantallaIndiceCresta(vm)
                    is Pantalla.IndiceHaptica -> PantallaIndiceHaptica(vm)
                    is Pantalla.IndiceResaltado -> PantallaIndiceResaltado(vm)
                    is Pantalla.AjustesWidget -> PantallaAjustesWidget(vm, destino.seccionId)
                    is Pantalla.WidgetTotpAjustes -> PantallaWidgetTotpAjustes(vm, destino.seccionId)
                    is Pantalla.Widget1x1Modo -> PantallaWidget1x1Modo(vm, destino.seccionId)
                    is Pantalla.Widget1x1Comportamiento -> PantallaWidget1x1Comportamiento(vm, destino.seccionId)
                    is Pantalla.Tema -> PantallaTema(vm, destino.seccionId)
                    is Pantalla.LaboratorioTemas -> PantallaLaboratorioTemas(vm, destino.seccionId)
                    is Pantalla.CalibracionAnimacion -> PantallaCalibracionAnimacion(vm, destino.seccionId)
                    is Pantalla.CalibracionWidgetTotp -> PantallaCalibracionWidgetTotp(vm, destino.seccionId)
                    is Pantalla.CalibracionWidget1x1 -> PantallaCalibracionWidget1x1(vm, destino.seccionId)
                    is Pantalla.Formas -> PantallaFormas(vm, destino.seccionId)
                    is Pantalla.FormasPresets -> PantallaFormasPresets(vm)
                    is Pantalla.FormasCurvatura -> PantallaFormasCurvatura(vm)
                    is Pantalla.FormasBorde, is Pantalla.FormasGrosor, is Pantalla.FormasEstilo -> PantallaFormasBorde(vm)
                    is Pantalla.FormasEspaciado -> PantallaFormasEspaciado(vm)
                    is Pantalla.Tipografia -> PantallaTipografia(vm, destino.seccionId)
                    is Pantalla.TipografiaPresets -> PantallaTipografiaPresets(vm)
                    is Pantalla.TipografiaEscala -> PantallaTipografiaEscala(vm)
                    is Pantalla.TipografiaFamilia -> PantallaTipografiaFamilia(vm)
                    is Pantalla.TipografiaPeso -> PantallaTipografiaPeso(vm)
                    is Pantalla.TipografiaEspaciado -> PantallaTipografiaEspaciado(vm)
                    is Pantalla.OrganizacionLista -> PantallaOrganizacionLista(vm, destino.seccionId)
                    is Pantalla.AcercaDe -> PantallaAcercaDe(vm, destino.seccionId)
                    is Pantalla.Registro -> PantallaRegistro(vm, destino.seccionId)
                    is Pantalla.SaludBoveda -> PantallaSaludBoveda(vm, estado, destino.seccionId)
                    is Pantalla.Duplicados -> PantallaDuplicados(vm, estado, destino.seccionId)
                    is Pantalla.Papelera -> PantallaPapelera(vm, estado, destino.seccionId)
                    is Pantalla.Identidades -> PantallaGestionIdentidades(vm, destino.seccionId)
                    is Pantalla.Categorias -> PantallaGestionCategorias(vm, destino.seccionId)
                    is Pantalla.KitEmergencia -> PantallaKitEmergencia(vm, actividad, destino.seccionId)
                    is Pantalla.AjustesCopiaAutomatica -> PantallaCopiaAutomatica(vm, destino.seccionId)
                    is Pantalla.AjustesSenuelo -> PantallaAjustesSenuelo(vm, destino.seccionId)
                    is Pantalla.AjustesAutodestruccion -> PantallaAjustesAutodestruccion(vm, destino.seccionId)
                    is Pantalla.FormatosCampos -> PantallaFormatosCampos(vm, destino.seccionId)
                    is Pantalla.HistorialClaves -> PantallaHistorialClaves(vm, destino.seccionId)
                    is Pantalla.AjustesHistorial -> PantallaAjustesHistorial(vm, destino.seccionId)
                    is Pantalla.Seguridad -> PantallaSeguridad(vm, actividad, destino.seccionId)
                    is Pantalla.CopiaSeguridad -> PantallaCopiaSeguridad(vm, destino.seccionId)
                    is Pantalla.CsvGoogle -> PantallaCsvGoogle(vm, destino.seccionId)
                    is Pantalla.ConfirmarMigracion -> PantallaConfirmarMigracion(vm, destino.urlMigracion)
                    is Pantalla.ConfirmarImportacionCxf -> PantallaConfirmarImportacionCxf(vm, destino.jsonCxf)
                    is Pantalla.ExportarSelectivo -> PantallaExportarSelectivo(vm, destino.seccionInicial)
                    is Pantalla.ColoresDatos -> PantallaColoresDatos(vm, destino.seccionId)
                    is Pantalla.ColoresIdentificadores -> PantallaColoresIds(vm, destino.seccionId)
                    is Pantalla.Argon2id -> PantallaArgon2id(vm, destino.seccionId)
                    is Pantalla.AjustesAutenticador -> PantallaAjustesAutenticador(vm, destino.seccionId)
                    is Pantalla.AjustesCamara -> PantallaAjustesCamara(vm, destino.seccionId)
                    is Pantalla.AjustesAutocompletado -> PantallaAjustesAutocompletado(vm, actividad, destino.seccionId)
                    is Pantalla.TileRapido -> PantallaTileRapido(vm, destino.seccionId)
                    is Pantalla.Avanzada -> PantallaAvanzada(vm, destino.seccionId)
                    is Pantalla.NormalizadorTitulos -> PantallaNormalizadorTitulos(vm, destino.esPostImportacion)
                    Pantalla.ReglasNormalizacion -> PantallaReglasNormalizacion(vm)
                }
            }

            if (cuentaAtras > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.75f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(SuperficieAlta)
                ) {
                    val duracion = ajustes.portapapelesSegundos.coerceAtLeast(1)
                    val progreso = (cuentaAtras.toFloat() / duracion).coerceIn(0f, 1f)
                    val colorProgreso = Color(
                        red = 1f - progreso,
                        green = progreso,
                        blue = 0.12f,
                        alpha = 1f
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progreso)
                            .fillMaxSize()
                            .background(colorProgreso)
                    )
                }
            }
        }
    }
}

/**
 * Se ofrece una sola vez, justo al crear la bóveda. Si dice no, no vuelve a salir:
 * queda el interruptor de siempre en Ajustes.
 */
@Composable
private fun DialogoOfrecerBiometria(vm: VaultViewModel, actividad: FragmentActivity, modo: BiometricKeyStore.Modo) {
    val flujo = remember { FlujoBiometria(actividad, vm.repositorio) }
    val compatible = modo == BiometricKeyStore.Modo.COMPATIBLE

    fun activar() {
        flujo.activar(modo) { resultado ->
            when (resultado) {
                is FlujoBiometria.ResultadoActivacion.Activada ->
                    vm.avisar("Listo: la próxima vez entras con la huella")
                FlujoBiometria.ResultadoActivacion.Cancelada ->
                    vm.avisar("Huella cancelada. Puedes activarla en Ajustes.")
                is FlujoBiometria.ResultadoActivacion.FuerteRota ->
                    vm.avisar("Android acepta tu huella pero el Keystore la rechaza. En Ajustes > Seguridad puedes activar el modo compatible.")
                is FlujoBiometria.ResultadoActivacion.Error ->
                    vm.avisar(resultado.texto)
            }
            vm.cerrarOfertaBiometria()
        }
    }

    DialogoConfirmacionBoveda(
        titulo = if (compatible) "¿Abrir con tu huella o tu PIN?" else "¿Abrir con tu huella?",
        mensaje = if (compatible) {
            "Este móvil no ofrece huella de Clase 3, así que iría en modo compatible: Android comprueba " +
                "tu huella o el PIN y la app abre la bóveda. La clave maestra queda envuelta por el Keystore " +
                "y no sale del móvil, pero no queda atada al chip como en el modo fuerte. Tu contraseña " +
                "maestra sigue siendo la única llave real."
        } else {
            "Tu contraseña maestra seguirá siendo la única llave: la huella solo la desenvuelve, " +
                "guardada por el Keystore de Android y atada a este móvil. Si cambias la biometría del " +
                "dispositivo, deja de valer y toca escribir la contraseña."
        },
        textoConfirmar = "Activar",
        textoCancelar = "Ahora no",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.Fingerprint,
        alConfirmar = { activar() },
        alDescartar = { vm.cerrarOfertaBiometria() }
    )
}

/**
 * Segunda oferta de bienvenida: activarme como gestor del sistema. Sin esto no
 * salgo al rellenar contraseñas ni al crear una llave de acceso, y nadie
 * encuentra solo el ajuste.
 */
@Composable
private fun DialogoOfrecerGestor(vm: VaultViewModel, actividad: FragmentActivity) {
    val mensaje = remember {
        buildAnnotatedString {
            append("Android no deja que una app se ponga sola: lo tienes que activar tú. Te abro la pantalla de \"Contraseñas y llaves de acceso\" y marcas ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Bóveda local") }
            append(".\n\nSin esto no aparezco al rellenar contraseñas ni al crear una llave de acceso. Lo puedes hacer más tarde desde Ajustes.")
        }.text
    }

    DialogoConfirmacionBoveda(
        titulo = "¿Me pones como gestor?",
        mensaje = mensaje,
        textoConfirmar = "Abrir ajustes",
        textoCancelar = "Ahora no",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.Settings,
        alConfirmar = {
            if (!AjustesSistema.abrirProveedorCredenciales(actividad)) {
                vm.avisar("No encuentro esa pantalla en este móvil")
            }
            vm.cerrarOfertaGestor()
        },
        alDescartar = { vm.cerrarOfertaGestor() }
    )
}
