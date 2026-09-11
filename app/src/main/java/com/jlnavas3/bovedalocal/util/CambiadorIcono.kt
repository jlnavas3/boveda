package com.jlnavas3.bovedalocal.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

/**
 * Alterna qué color de icono ve el usuario en el launcher del sistema (al estilo
 * Fossify): mismo dibujo, mismo nombre, solo cambia el degradado. Cada color es un
 * activity-alias que apunta a MainActivity; solo cambia componentes de la propia
 * app (no requiere ningún permiso ni red). Android tarda un momento en refrescar
 * el icono tras el cambio.
 */
object CambiadorIcono {

    private val aliasPorClave = mapOf(
        "ambar" to "com.jlnavas3.bovedalocal.IconoAmbar",
        "menta" to "com.jlnavas3.bovedalocal.IconoMenta",
        "azul" to "com.jlnavas3.bovedalocal.IconoAzul",
        "rosa" to "com.jlnavas3.bovedalocal.IconoRosa",
        "violeta" to "com.jlnavas3.bovedalocal.IconoVioleta",
        "rojo" to "com.jlnavas3.bovedalocal.IconoRojo",
        "purpura" to "com.jlnavas3.bovedalocal.IconoPurpura",
        "purpura_oscuro" to "com.jlnavas3.bovedalocal.IconoPurpuraOscuro",
        "indigo" to "com.jlnavas3.bovedalocal.IconoIndigo",
        "celeste" to "com.jlnavas3.bovedalocal.IconoCeleste",
        "cian" to "com.jlnavas3.bovedalocal.IconoCian",
        "verde" to "com.jlnavas3.bovedalocal.IconoVerde",
        "verde_claro" to "com.jlnavas3.bovedalocal.IconoVerdeClaro",
        "lima" to "com.jlnavas3.bovedalocal.IconoLima",
        "amarillo" to "com.jlnavas3.bovedalocal.IconoAmarillo",
        "naranja" to "com.jlnavas3.bovedalocal.IconoNaranja",
        "naranja_oscuro" to "com.jlnavas3.bovedalocal.IconoNaranjaOscuro",
        "marron" to "com.jlnavas3.bovedalocal.IconoMarron",
        "gris" to "com.jlnavas3.bovedalocal.IconoGris",
        "gris_azulado" to "com.jlnavas3.bovedalocal.IconoGrisAzulado"
    )

    /**
     * Deja habilitado solo el alias del color elegido; los otros cuatro, apagados.
     * MainActivity (el "targetActivity" de los cinco) nunca se toca: si se deshabilita
     * el componente real al que apuntan los alias, Android deja de poder arrancarlo
     * también desde ellos y desaparecen los cinco iconos del launcher a la vez.
     */
    fun aplicar(contexto: Context, clave: String) {
        val pm = contexto.packageManager
        val componentes = aliasPorClave.mapValues { ComponentName(contexto.packageName, it.value) }
        val elegido = componentes[clave] ?: componentes.getValue("ambar")

        // Primero se enciende el elegido y solo después se apagan los otros cuatro: así
        // nunca hay una ventana sin ningún icono de lanzador visible.
        pm.setComponentEnabledSetting(elegido, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        componentes.filterValues { it != elegido }.values.forEach { componente ->
            pm.setComponentEnabledSetting(componente, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        }
    }
}

