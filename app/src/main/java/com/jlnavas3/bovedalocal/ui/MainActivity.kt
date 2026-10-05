package com.jlnavas3.bovedalocal.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import android.net.Uri
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoContrasena
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAcercaDe
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesIndice
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaAutenticador
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDesbloqueo
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaEscaner
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.PantallaCamaraQr
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaLista
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaOnboarding
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaPasskeys
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaSaludBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaDuplicados
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaPapelera
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaRegistro
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Diagnostico
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.ui.pantallas.PantallaConfirmarMigracion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidget1x1Comportamiento
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidget1x1Modo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.PantallaWidgetTotpAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.historial.PantallaAjustesHistorial
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceCresta
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceHaptica
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceOla
import com.jlnavas3.bovedalocal.ui.pantallas.indice.PantallaIndiceResaltado
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private val vm: VaultViewModel by viewModels()

    /** Detecta capturas de pantalla en Android 14+ y las registra en el log de eventos. */
    private val capturaCallback: Any? = if (android.os.Build.VERSION.SDK_INT >= 34) {
        android.app.Activity.ScreenCaptureCallback {
            Diagnostico.apuntar("seguridad", "Captura de pantalla detectada por el sistema")
        }
    } else null

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT >= 34 && capturaCallback != null) {
            try {
                @Suppress("NewApi")
                registerScreenCaptureCallback(mainExecutor, capturaCallback as android.app.Activity.ScreenCaptureCallback)
            } catch (_: SecurityException) {
                // Algunas ROMs (Honor/HarmonyOS) exigen un permiso no público para esta API
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (android.os.Build.VERSION.SDK_INT >= 34 && capturaCallback != null) {
            try {
                @Suppress("NewApi")
                unregisterScreenCaptureCallback(capturaCallback as android.app.Activity.ScreenCaptureCallback)
            } catch (_: Exception) {
                // Ignorar si no se pudo registrar en onStart
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.recargarAjustes()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        manejarAccionShortcut(intent)
        manejarIntentArchivoBvda(intent)
    }

    private fun manejarIntentArchivoBvda(intent: Intent?) {
        if (intent == null) return
        val uri = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM) ?: intent.data
            }
            else -> null
        } ?: return

        val path = uri.path?.lowercase() ?: ""
        val lastSegment = uri.lastPathSegment?.lowercase() ?: ""
        val mime = intent.type?.lowercase() ?: ""
        val esBvda = path.endsWith(".bvda") || lastSegment.endsWith(".bvda") ||
                mime.contains("boveda") || mime.contains("bvda") ||
                intent.action == Intent.ACTION_VIEW

        if (esBvda) {
            vm.establecerUriBvdaPendiente(uri)
        }
    }

    private fun actualizarShortcutsDinamicos() {
        try {
            val shortcutNueva = ShortcutInfoCompat.Builder(this, "nueva_entrada")
                .setShortLabel(getString(R.string.shortcut_nueva_entrada))
                .setLongLabel(getString(R.string.shortcut_nueva_entrada))
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_nueva_entrada))
                .setIntent(Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("accion_shortcut", "nueva_entrada")
                })
                .build()

            val shortcutBuscar = ShortcutInfoCompat.Builder(this, "buscar")
                .setShortLabel(getString(R.string.shortcut_buscar))
                .setLongLabel(getString(R.string.shortcut_buscar))
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_buscar))
                .setIntent(Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("accion_shortcut", "buscar")
                })
                .build()

            val shortcutEscanear = ShortcutInfoCompat.Builder(this, "escanear_qr")
                .setShortLabel(getString(R.string.shortcut_escanear))
                .setLongLabel(getString(R.string.shortcut_escanear))
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_escanear))
                .setIntent(Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("accion_shortcut", "escanear_qr")
                })
                .build()

            val shortcutGenerador = ShortcutInfoCompat.Builder(this, "generador_rapido")
                .setShortLabel(getString(R.string.shortcut_generador))
                .setLongLabel(getString(R.string.shortcut_generador))
                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_generador))
                .setIntent(Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("accion_shortcut", "generador_rapido")
                })
                .build()

            ShortcutManagerCompat.setDynamicShortcuts(this, listOf(shortcutNueva, shortcutBuscar, shortcutEscanear, shortcutGenerador))
        } catch (_: Exception) {}
    }

    private fun manejarAccionShortcut(intent: Intent?) {
        val accion = intent?.getStringExtra("accion_shortcut") ?: return
        intent.removeExtra("accion_shortcut")
        if (accion == "generador_rapido") {
            GeneradorRapidoHelper.generar(this, "App Shortcut")
        } else {
            vm.solicitarAccionShortcut(accion)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actualizarShortcutsDinamicos()
        manejarAccionShortcut(intent)
        manejarIntentArchivoBvda(intent)
        // FLAG_SECURE activa por defecto; se gestiona dinámicamente según la preferencia del usuario
        // Cargar paleta sobria personalizada si existe
        val (guardadaOsc, guardadaCla) = com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria.cargar(this)
        if (guardadaOsc != null) com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura = guardadaOsc
        if (guardadaCla != null) com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara = guardadaCla
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(vm.repositorio.ajustes.actual)
        setContent {
            val ajustes by vm.ajustes.collectAsStateWithLifecycle()
            LaunchedEffect(ajustes) {
                com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(ajustes)
            }
            // FLAG_SECURE dinámico: respeta la preferencia del usuario en tiempo real
            LaunchedEffect(ajustes.proteccionPantalla) {
                if (ajustes.proteccionPantalla) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
            BovedaTheme(temaApp = ajustes.temaApp) {
                RaizBoveda(vm, this)
            }
        }
    }
}

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
                    val animOffset = tween<androidx.compose.ui.unit.IntOffset>(durationMillis = 180, easing = FastOutSlowInEasing)
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
                    is Pantalla.Editar -> PantallaEdicion(vm, destino.id, destino.contrasenaInicial)
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
                    is Pantalla.AjustesWidget -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesWidget(vm, destino.seccionId)
                    is Pantalla.WidgetTotpAjustes -> PantallaWidgetTotpAjustes(vm, destino.seccionId)
                    is Pantalla.Widget1x1Modo -> PantallaWidget1x1Modo(vm, destino.seccionId)
                    is Pantalla.Widget1x1Comportamiento -> PantallaWidget1x1Comportamiento(vm, destino.seccionId)
                    is Pantalla.Tema -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaTema(vm, destino.seccionId)
                    is Pantalla.LaboratorioTemas -> com.jlnavas3.bovedalocal.ui.pantallas.tema.PantallaLaboratorioTemas(vm, destino.seccionId)
                    is Pantalla.CalibracionAnimacion -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionAnimacion(vm, destino.seccionId)
                    is Pantalla.CalibracionWidgetTotp -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionWidgetTotp(vm, destino.seccionId)
                    is Pantalla.CalibracionWidget1x1 -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaCalibracionWidget1x1(vm, destino.seccionId)
                    is Pantalla.Formas -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaFormas(vm, destino.seccionId)
                    is Pantalla.FormasPresets -> com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasPresets(vm)
                    is Pantalla.FormasCurvatura -> com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasCurvatura(vm)
                    is Pantalla.FormasGrosor -> com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasGrosor(vm)
                    is Pantalla.FormasEstilo -> com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasEstilo(vm)
                    is Pantalla.FormasEspaciado -> com.jlnavas3.bovedalocal.ui.pantallas.formas.PantallaFormasEspaciado(vm)
                    is Pantalla.Tipografia -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaTipografia(vm, destino.seccionId)
                    is Pantalla.TipografiaPresets -> com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaPresets(vm)
                    is Pantalla.TipografiaEscala -> com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaEscala(vm)
                    is Pantalla.TipografiaFamilia -> com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaFamilia(vm)
                    is Pantalla.TipografiaPeso -> com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaPeso(vm)
                    is Pantalla.TipografiaEspaciado -> com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PantallaTipografiaEspaciado(vm)
                    is Pantalla.OrganizacionLista -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaOrganizacionLista(vm, destino.seccionId)
                    is Pantalla.AcercaDe -> PantallaAcercaDe(vm, destino.seccionId)
                    is Pantalla.Registro -> PantallaRegistro(vm, destino.seccionId)
                    is Pantalla.SaludBoveda -> PantallaSaludBoveda(vm, estado, destino.seccionId)
                    is Pantalla.Duplicados -> PantallaDuplicados(vm, estado, destino.seccionId)
                    is Pantalla.Papelera -> PantallaPapelera(vm, estado, destino.seccionId)
                    is Pantalla.Identidades -> com.jlnavas3.bovedalocal.ui.pantallas.identidades.PantallaGestionIdentidades(vm, destino.seccionId)
                    is Pantalla.Categorias -> com.jlnavas3.bovedalocal.ui.pantallas.categorias.PantallaGestionCategorias(vm, destino.seccionId)
                    is Pantalla.KitEmergencia -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaKitEmergencia(vm, actividad, destino.seccionId)
                    is Pantalla.AjustesCopiaAutomatica -> com.jlnavas3.bovedalocal.ui.pantallas.copia.PantallaCopiaAutomatica(vm, destino.seccionId)
                    is Pantalla.AjustesSenuelo -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesSenuelo(vm, destino.seccionId)
                    is Pantalla.AjustesAutodestruccion -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesAutodestruccion(vm, destino.seccionId)
                    is Pantalla.FormatosCampos -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaFormatosCampos(vm, destino.seccionId)
                    is Pantalla.HistorialClaves -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaHistorialClaves(vm, destino.seccionId)
                    is Pantalla.AjustesHistorial -> PantallaAjustesHistorial(vm, destino.seccionId)
                    is Pantalla.Seguridad -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaSeguridad(vm, actividad, destino.seccionId)
                    is Pantalla.CopiaSeguridad -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaCopiaSeguridad(vm, destino.seccionId)
                    is Pantalla.CsvGoogle -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaCsvGoogle(vm, destino.seccionId)
                    is Pantalla.ConfirmarMigracion -> PantallaConfirmarMigracion(vm, destino.urlMigracion)
                    is Pantalla.ConfirmarImportacionCxf -> com.jlnavas3.bovedalocal.ui.pantallas.cxf.PantallaConfirmarImportacionCxf(vm, destino.jsonCxf)
                    is Pantalla.ExportarSelectivo -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaExportarSelectivo(vm, destino.seccionInicial)
                    is Pantalla.ColoresDatos -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaColoresDatos(vm, destino.seccionId)
                    is Pantalla.ColoresIdentificadores -> com.jlnavas3.bovedalocal.ui.pantallas.avanzada.PantallaColoresIds(vm, destino.seccionId)
                    is Pantalla.Argon2id -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaArgon2id(vm, destino.seccionId)
                    is Pantalla.AjustesAutenticador -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesAutenticador(vm, destino.seccionId)
                    is Pantalla.AjustesCamara -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAjustesCamara(vm, destino.seccionId)
                    is Pantalla.TileRapido -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaTileRapido(vm, destino.seccionId)
                    is Pantalla.Avanzada -> com.jlnavas3.bovedalocal.ui.pantallas.PantallaAvanzada(vm, destino.seccionId)
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
