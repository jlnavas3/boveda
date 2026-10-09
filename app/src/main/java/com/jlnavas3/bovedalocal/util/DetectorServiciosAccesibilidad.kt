package com.jlnavas3.bovedalocal.util

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.accessibility.AccessibilityManager

data class ServicioAccesibilidadDetectado(
    val nombrePaquete: String,
    val etiquetaApp: String,
    val esAppSistema: Boolean,
    val estaActivo: Boolean = true,
    val esEnListaBlanca: Boolean = false,
    val esSospechoso: Boolean
)

data class DiagnosticoAccesibilidad(
    val totalActivos: Int,
    val servicios: List<ServicioAccesibilidadDetectado>,
    val serviciosSospechosos: List<ServicioAccesibilidadDetectado>,
    val esSeguro: Boolean
)

/**
 * Microcomponente que audita los servicios de accesibilidad activos en el sistema operativo.
 * Identifica si existen aplicaciones de terceros (no del sistema) con permisos
 * para leer el árbol de vistas e inspeccionar la pantalla del usuario.
 */
object DetectorServiciosAccesibilidad {

    // Paquetes conocidos y legítimos de accesibilidad del sistema / suite de Google / fabricantes
    private val PAQUETES_ACCESIBILIDAD_CONOCIDOS = setOf(
        "com.google.android.marvin.talkback",
        "com.google.android.apps.accessibility.auditor",
        "com.google.android.accessibility.switchaccess",
        "com.google.audio.hearing.visualization.accessibility.scribe",
        "com.samsung.android.accessibility.talkback",
        "com.samsung.android.app.talkback",
        "com.hihonor.android.accessibility",
        "com.huawei.android.accessibility",
        "com.miui.accessibility",
        "com.oppo.accessibility",
        "com.coloros.accessibility",
        "com.vivo.accessibility"
    )

    fun evaluar(contexto: Context, listaBlanca: Set<String> = emptySet()): DiagnosticoAccesibilidad {
        return try {
            val am = contexto.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
            val habilitados = am?.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK) ?: emptyList()
            val instalados = am?.installedAccessibilityServiceList ?: emptyList()
            val pm = contexto.packageManager

            val paquetesHabilitados = habilitados.mapNotNull {
                it.resolveInfo?.serviceInfo?.packageName?.lowercase()
            }.toSet()

            // 1. Servicios habilitados / activos
            val serviciosHabilitados = habilitados.mapNotNull { info ->
                val paquete = info.resolveInfo?.serviceInfo?.packageName ?: return@mapNotNull null
                val etiqueta = try {
                    info.resolveInfo?.loadLabel(pm)?.toString() ?: paquete
                } catch (_: Exception) {
                    paquete
                }

                val esSistema = esAplicacionDeSistema(pm, paquete)
                val esConocido = PAQUETES_ACCESIBILIDAD_CONOCIDOS.contains(paquete.lowercase())
                val esEnListaBlanca = listaBlanca.contains(paquete.lowercase())
                val esSospechoso = !esSistema && !esConocido && !esEnListaBlanca

                ServicioAccesibilidadDetectado(
                    nombrePaquete = paquete,
                    etiquetaApp = etiqueta,
                    esAppSistema = esSistema || esConocido,
                    estaActivo = true,
                    esEnListaBlanca = esEnListaBlanca,
                    esSospechoso = esSospechoso
                )
            }

            // 2. Servicios del sistema instalados (incluso si están inactivos)
            val nombresYaProcesados = serviciosHabilitados.map { it.nombrePaquete.lowercase() }.toSet()

            val serviciosSistemaInstalados = instalados.mapNotNull { info ->
                val paquete = info.resolveInfo?.serviceInfo?.packageName ?: return@mapNotNull null
                if (nombresYaProcesados.contains(paquete.lowercase())) return@mapNotNull null

                val esSistema = esAplicacionDeSistema(pm, paquete)
                val esConocido = PAQUETES_ACCESIBILIDAD_CONOCIDOS.contains(paquete.lowercase())

                if (esSistema || esConocido) {
                    val etiqueta = try {
                        info.resolveInfo?.loadLabel(pm)?.toString() ?: paquete
                    } catch (_: Exception) {
                        paquete
                    }

                    ServicioAccesibilidadDetectado(
                        nombrePaquete = paquete,
                        etiquetaApp = etiqueta,
                        esAppSistema = true,
                        estaActivo = paquetesHabilitados.contains(paquete.lowercase()),
                        esEnListaBlanca = false,
                        esSospechoso = false
                    )
                } else {
                    null
                }
            }.distinctBy { it.etiquetaApp to it.nombrePaquete }

            val todos = (serviciosHabilitados + serviciosSistemaInstalados).distinctBy { it.etiquetaApp to it.nombrePaquete }
            val sospechosos = todos.filter { it.esSospechoso }

            DiagnosticoAccesibilidad(
                totalActivos = serviciosHabilitados.size,
                servicios = todos,
                serviciosSospechosos = sospechosos,
                esSeguro = sospechosos.isEmpty()
            )
        } catch (_: Exception) {
            DiagnosticoAccesibilidad(
                totalActivos = 0,
                servicios = emptyList(),
                serviciosSospechosos = emptyList(),
                esSeguro = true
            )
        }
    }

    private fun esAplicacionDeSistema(pm: PackageManager, nombrePaquete: String): Boolean {
        return try {
            val appInfo = pm.getApplicationInfo(nombrePaquete, 0)
            val flags = appInfo.flags
            (flags and ApplicationInfo.FLAG_SYSTEM) != 0 || (flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
        } catch (_: Exception) {
            false
        }
    }
}
