package com.jlnavas3.bovedalocal.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura
import com.jlnavas3.bovedalocal.util.Diagnostico

class MainActivity : FragmentActivity() {

    private val vm: VaultViewModel by viewModels()

    /** Detecta capturas de pantalla en Android 14+ y las registra en el log de eventos. */
    private val capturaCallback: Any? = if (Build.VERSION.SDK_INT >= 34) {
        ScreenCaptureCallback {
            Diagnostico.apuntar("seguridad", "Captura de pantalla detectada por el sistema")
        }
    } else null

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 34 && capturaCallback != null) {
            try {
                @Suppress("NewApi")
                registerScreenCaptureCallback(mainExecutor, capturaCallback as ScreenCaptureCallback)
            } catch (_: SecurityException) {
                // Algunas ROMs (Honor/HarmonyOS) exigen un permiso no público para esta API
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= 34 && capturaCallback != null) {
            try {
                @Suppress("NewApi")
                unregisterScreenCaptureCallback(capturaCallback as ScreenCaptureCallback)
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
        val tituloInicial = intent.getStringExtra("titulo_inicial") ?: ""
        val urlInicial = intent.getStringExtra("url_inicial") ?: ""
        intent.removeExtra("accion_shortcut")
        intent.removeExtra("titulo_inicial")
        intent.removeExtra("url_inicial")
        if (accion == "generador_rapido") {
            GeneradorRapidoHelper.generar(this, "App Shortcut")
        } else {
            vm.solicitarAccionShortcut(accion, tituloInicial, urlInicial)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actualizarShortcutsDinamicos()
        manejarAccionShortcut(intent)
        manejarIntentArchivoBvda(intent)
        // FLAG_SECURE activa por defecto; se gestiona dinámicamente según la preferencia del usuario
        // Cargar paleta sobria personalizada si existe
        val (guardadaOsc, guardadaCla) = GestorPaletaSobria.cargar(this)
        if (guardadaOsc != null) paletaSobriaGuardadaOscura = guardadaOsc
        if (guardadaCla != null) paletaSobriaGuardadaClara = guardadaCla
        aplicarPersonalizacionTemaCompleto(vm.repositorio.ajustes.actual)
        setContent {
            val ajustes by vm.ajustes.collectAsStateWithLifecycle()
            LaunchedEffect(ajustes) {
                aplicarPersonalizacionTemaCompleto(ajustes)
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
