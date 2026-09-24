package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
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
}
