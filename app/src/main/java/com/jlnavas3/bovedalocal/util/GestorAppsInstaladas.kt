package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Representa una aplicación instalada en el dispositivo con su nombre amigable,
 * nombre de paquete, icono pre-renderizado como ImageBitmap e indicador de app de sistema.
 */
data class AppInstalada(
    val nombre: String,
    val paquete: String,
    val iconoBitmap: ImageBitmap? = null,
    val esDeSistema: Boolean = false
)

/**
 * Utilidad encargada de consultar y almacenar en caché las aplicaciones instaladas en Android.
 */
object GestorAppsInstaladas {

    private var cacheAppsLanzables: List<AppInstalada>? = null
    private var cacheAppsTodas: List<AppInstalada>? = null
    @Volatile
    private var cachePaquetesInstalados: Set<String>? = null
    private val cacheNombresApp = java.util.concurrent.ConcurrentHashMap<String, String>()
    private val cacheIconosIndividuales = java.util.concurrent.ConcurrentHashMap<String, ImageBitmap>()
    private val cachePaqueteResueltoPorEntrada = java.util.concurrent.ConcurrentHashMap<String, String>()

    /**
     * Limpia todas las cachés en memoria si se fuerza una recarga.
     */
    fun limpiarCache() {
        cacheAppsLanzables = null
        cacheAppsTodas = null
        cachePaquetesInstalados = null
        cacheNombresApp.clear()
        cacheIconosIndividuales.clear()
        cachePaqueteResueltoPorEntrada.clear()
    }

    /**
     * Comprueba de manera instantánea si un paquete está instalado usando un Set en memoria.
     * Cero llamadas Binder IPC repetitivas a PackageManager.
     */
    fun estaInstalada(contexto: Context, paquete: String): Boolean {
        val paqLimpio = paquete.trim().lowercase()
        if (paqLimpio.isBlank()) return false
        val cache = cachePaquetesInstalados
        if (cache != null) {
            return cache.contains(paqLimpio)
        }
        return try {
            val pm = contexto.packageManager
            val paquetes = pm.getInstalledApplications(0).map { it.packageName.lowercase() }.toSet()
            cachePaquetesInstalados = paquetes
            paquetes.contains(paqLimpio)
        } catch (e: Exception) {
            LanzadorEnlaces.estaInstaladaDirecta(contexto, paquete)
        }
    }

    /**
     * Obtiene el nombre amigable de la aplicación con caché en memoria sin bloquear el hilo UI.
     */
    fun obtenerNombreApp(contexto: Context, paquete: String): String? {
        val paqLimpio = paquete.trim()
        if (paqLimpio.isBlank()) return null
        val paqKey = paqLimpio.lowercase()
        val enCache = cacheNombresApp[paqKey]
        if (enCache != null) return enCache

        val deLanzables = cacheAppsLanzables?.firstOrNull { it.paquete.equals(paqLimpio, ignoreCase = true) }?.nombre
        if (deLanzables != null) {
            cacheNombresApp[paqKey] = deLanzables
            return deLanzables
        }

        return try {
            val pm = contexto.packageManager
            val appInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.getApplicationInfo(paqLimpio, android.content.pm.PackageManager.ApplicationInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getApplicationInfo(paqLimpio, 0)
            }
            val nombre = pm.getApplicationLabel(appInfo).toString()
            cacheNombresApp[paqKey] = nombre
            nombre
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Precarga en segundo plano la lista de aplicaciones instaladas para que el hilo de la UI
     * nunca experimente caídas de cuadros (jank) al hacer scroll o usar el índice lateral.
     */
    suspend fun precargar(contexto: Context) = withContext(Dispatchers.IO) {
        try {
            val pm = contexto.packageManager
            val todosPaquetes = pm.getInstalledApplications(0).map { it.packageName.lowercase() }.toSet()
            cachePaquetesInstalados = todosPaquetes

            obtenerAppsInstaladas(contexto, incluirSistema = false)
        } catch (_: Exception) {}
    }

    /**
     * Obtiene la lista de aplicaciones instaladas en el dispositivo,
     * ordenadas alfabéticamente por su nombre amigable.
     *
     * @param incluirSistema Si es true, consulta todas las aplicaciones instaladas (incluyendo servicios de sistema como Xiaomi Account, Google Play Services, etc.).
     */
    suspend fun obtenerAppsInstaladas(
        contexto: Context,
        incluirSistema: Boolean = false,
        forzarRecarga: Boolean = false
    ): List<AppInstalada> {
        if (!forzarRecarga) {
            if (incluirSistema && cacheAppsTodas != null) return cacheAppsTodas!!
            if (!incluirSistema && cacheAppsLanzables != null) return cacheAppsLanzables!!
        } else {
            limpiarCache()
        }

        return withContext(Dispatchers.IO) {
            val pm = contexto.packageManager
            val miPaquete = contexto.packageName

            val lista = if (!incluirSistema) {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }
                val actividades = try {
                    pm.queryIntentActivities(intent, 0)
                } catch (e: Exception) {
                    emptyList()
                }

                actividades.mapNotNull { resolveInfo ->
                    val appInfo = resolveInfo.activityInfo?.applicationInfo ?: return@mapNotNull null
                    val paquete = appInfo.packageName ?: return@mapNotNull null
                    if (paquete == miPaquete) return@mapNotNull null

                    val label = resolveInfo.loadLabel(pm)
                    val nombre = (label?.toString() ?: paquete).trim().ifBlank { paquete }
                    val drawable = try {
                        resolveInfo.loadIcon(pm) ?: appInfo.loadIcon(pm)
                    } catch (e: Exception) {
                        null
                    }
                    val bitmap = try {
                        drawable?.toBitmapOrNull(width = 96, height = 96)?.asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                    val esSistema = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                        (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0

                    AppInstalada(
                        nombre = nombre,
                        paquete = paquete,
                        iconoBitmap = bitmap,
                        esDeSistema = esSistema
                    )
                }
            } else {
                val aplicaciones = try {
                    pm.getInstalledApplications(0)
                } catch (e: Exception) {
                    emptyList()
                }

                aplicaciones.mapNotNull { appInfo ->
                    val paquete = appInfo.packageName ?: return@mapNotNull null
                    if (paquete == miPaquete) return@mapNotNull null

                    val label = appInfo.loadLabel(pm)
                    val nombre = label.toString().trim().ifBlank { paquete }
                    val drawable = try {
                        appInfo.loadIcon(pm)
                    } catch (e: Exception) {
                        null
                    }
                    val bitmap = try {
                        drawable?.toBitmapOrNull(width = 96, height = 96)?.asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                    val esSistema = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                        (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0

                    AppInstalada(
                        nombre = nombre,
                        paquete = paquete,
                        iconoBitmap = bitmap,
                        esDeSistema = esSistema
                    )
                }
            }

            val listaOrdenada = lista.distinctBy { it.paquete }.sortedBy { it.nombre.lowercase() }

            listaOrdenada.forEach { app ->
                cacheNombresApp[app.paquete.lowercase()] = app.nombre
                if (app.iconoBitmap != null) {
                    cacheIconosIndividuales[app.paquete] = app.iconoBitmap
                }
            }

            if (incluirSistema) {
                cacheAppsTodas = listaOrdenada
            } else {
                cacheAppsLanzables = listaOrdenada
            }

            listaOrdenada
        }
    }

    /**
     * Filtra la lista de aplicaciones por un término de búsqueda en nombre o paquete.
     */
    fun filtrarApps(apps: List<AppInstalada>, consulta: String): List<AppInstalada> {
        val q = consulta.trim().lowercase()
        if (q.isBlank()) return apps
        return apps.filter {
            it.nombre.lowercase().contains(q) || it.paquete.lowercase().contains(q)
        }
    }

    private val paquetesExcluidos = setOf(
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.opera.browser",
        "com.brave.browser",
        "com.microsoft.emmx",
        "com.sec.android.app.sbrowser",
        "com.duckduckgo.mobile.android",
        "com.android.vending",
        "com.google.android.gms",
        "com.google.android.googlequicksearchbox",
        "com.android.settings",
        "com.android.systemui",
        "com.android.shell"
    )

    private val marcasGenericasNoDifusas = setOf(
        "google",
        "android",
        "microsoft",
        "apple",
        "wordpress",
        "blogger",
        "medium"
    )

    /**
     * Resuelve el paquete de la aplicación instalada asociada a una entrada:
     * 1. Si la entrada tiene un paquete Android explícito en sus URLs o passkey:
     *    - Si está instalada en el dispositivo: devuelve ese paquete.
     *    - Si NO está instalada: devuelve null (NUNCA adivina otra app).
     * 2. Si no tiene paquete explícito, busca con alta precisión si alguna app instalada
     *    corresponde a la marca del dominio web (ej. mercadolibre.com -> com.mercadolibre).
     */
    fun resolverPaqueteApp(contexto: Context, entrada: Entrada): String? {
        val claveCache = "${entrada.id}_${entrada.modificadaEn}_${entrada.titulo}"
        val enCache = cachePaqueteResueltoPorEntrada[claveCache]
        if (enCache != null) {
            return enCache.takeIf { it.isNotEmpty() }
        }

        val res = resolverPaqueteAppInterno(contexto, entrada)
        cachePaqueteResueltoPorEntrada[claveCache] = res ?: ""
        return res
    }

    private fun resolverPaqueteAppInterno(contexto: Context, entrada: Entrada): String? {
        // 1. Paquete explícito en URLs o passkey
        val paqueteExplicito = entrada.urls.firstNotNullOfOrNull { LanzadorEnlaces.extraerPaquete(it) }
            ?: entrada.passkey?.rpId?.let { LanzadorEnlaces.extraerPaquete(it) }
        if (paqueteExplicito != null) {
            return if (estaInstalada(contexto, paqueteExplicito)) {
                paqueteExplicito
            } else {
                null
            }
        }

        // 2. Coincidencia estricta por dominio web
        val itemsAAnalizar = (entrada.urls + listOf(entrada.titulo))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val marcasCandidatas = itemsAAnalizar
            .mapNotNull { item ->
                val raiz = Dominios.raiz(item)
                if (raiz.isNotBlank() && raiz.contains('.')) {
                    val marca = Dominios.marca(raiz).lowercase()
                    if (marca.length >= 3 && marca !in marcasGenericasNoDifusas) {
                        marca to raiz.lowercase()
                    } else null
                } else null
            }
            .distinct()

        if (marcasCandidatas.isEmpty()) return null

        val apps: List<AppInstalada> = cacheAppsLanzables ?: synchronized(this) {
            cacheAppsLanzables ?: run {
                try {
                    val pm = contexto.packageManager
                    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
                    val cargadas = pm.queryIntentActivities(intent, 0).mapNotNull { resolveInfo ->
                        val appInfo = resolveInfo.activityInfo?.applicationInfo ?: return@mapNotNull null
                        val paq = appInfo.packageName ?: return@mapNotNull null
                        if (paq in paquetesExcluidos || paq == contexto.packageName) return@mapNotNull null
                        val label = resolveInfo.loadLabel(pm)?.toString() ?: paq
                        cacheNombresApp[paq.lowercase()] = label
                        AppInstalada(nombre = label, paquete = paq)
                    }
                    cacheAppsLanzables = cargadas
                    cargadas
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }

        for ((marca, _) in marcasCandidatas) {
            val coincidente = apps.firstOrNull { app ->
                val paqLower = app.paquete.lowercase()
                if (paqLower in paquetesExcluidos || paqLower.startsWith("com.android.")) return@firstOrNull false
                if (paqLower.startsWith("io.github.") || paqLower.startsWith("com.gitlab.")) return@firstOrNull false

                val segmentos = paqLower.split('.')
                val tieneSegmentoMarca = segmentos.contains(marca)

                val nombreLimpio = app.nombre.lowercase().replace(" ", "").replace("-", "").replace("_", "")
                val marcaLimpia = marca.replace(" ", "").replace("-", "").replace("_", "")

                val nombreCoincide = nombreLimpio == marcaLimpia ||
                    (marcaLimpia.length >= 4 && (nombreLimpio.startsWith(marcaLimpia) || nombreLimpio.contains(marcaLimpia)))

                tieneSegmentoMarca && nombreCoincide
            }?.paquete

            if (coincidente != null && estaInstalada(contexto, coincidente)) {
                return coincidente
            }
        }

        return null
    }

    /**
     * Obtiene el icono como ImageBitmap para una aplicación instalada dada por su nombre de paquete.
     * Consulta primero las listas en caché si están disponibles; en caso contrario, lo carga
     * directamente del PackageManager de Android.
     */
    fun obtenerIconoApp(contexto: Context, paquete: String): ImageBitmap? {
        val paqueteLimpio = paquete.trim()
        if (paqueteLimpio.isBlank()) return null

        val enCacheIndiv = cacheIconosIndividuales[paqueteLimpio]
        if (enCacheIndiv != null) return enCacheIndiv

        val enCache = cacheAppsLanzables?.firstOrNull { it.paquete.equals(paqueteLimpio, ignoreCase = true) }?.iconoBitmap
            ?: cacheAppsTodas?.firstOrNull { it.paquete.equals(paqueteLimpio, ignoreCase = true) }?.iconoBitmap
        if (enCache != null) {
            cacheIconosIndividuales[paqueteLimpio] = enCache
            return enCache
        }

        return try {
            val pm = contexto.packageManager
            val appInfo = pm.getApplicationInfo(paqueteLimpio, 0)
            val drawable = appInfo.loadIcon(pm)
            val bmp = drawable?.toBitmapOrNull(width = 96, height = 96)?.asImageBitmap()
            if (bmp != null) {
                cacheIconosIndividuales[paqueteLimpio] = bmp
            }
            bmp
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Hook de Compose para recordar de manera eficiente el ImageBitmap del icono de la app
 * instalada asociada a una entrada (analizando URLs, dominios web, títulos y passkeys).
 */
@Composable
fun rememberIconoAppInstalada(entrada: Entrada): ImageBitmap? {
    val contexto = LocalContext.current
    return remember(entrada.id, entrada.modificadaEn) {
        val paquete = GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
        if (paquete != null) {
            GestorAppsInstaladas.obtenerIconoApp(contexto, paquete)
        } else null
    }
}

/**
 * Renderiza el icono de una aplicación instalada con máscara circular recortada
 * y escala ampliada (1.22f) para destacar el logotipo sin bordes excesivos.
 */
@Composable
fun IconoAppCircular(
    bitmap: ImageBitmap,
    descripcion: String,
    tamanoDp: Dp,
    modifier: Modifier = Modifier
) {
    val forma = CircleShape
    Box(
        modifier = modifier
            .size(tamanoDp)
            .clip(forma),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.22f)
        )
        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(GrosorBorde, ColorBordeActual.copy(alpha = 0.35f), forma)
            )
        }
    }
}
