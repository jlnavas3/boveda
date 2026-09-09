package com.pepotech.pepoboveda.util

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
        "ambar" to "com.pepotech.pepoboveda.IconoAmbar",
        "menta" to "com.pepotech.pepoboveda.IconoMenta",
        "azul" to "com.pepotech.pepoboveda.IconoAzul",
        "rosa" to "com.pepotech.pepoboveda.IconoRosa",
        "violeta" to "com.pepotech.pepoboveda.IconoVioleta",
        "rojo" to "com.pepotech.pepoboveda.IconoRojo",
        "purpura" to "com.pepotech.pepoboveda.IconoPurpura",
        "purpura_oscuro" to "com.pepotech.pepoboveda.IconoPurpuraOscuro",
        "indigo" to "com.pepotech.pepoboveda.IconoIndigo",
        "celeste" to "com.pepotech.pepoboveda.IconoCeleste",
        "cian" to "com.pepotech.pepoboveda.IconoCian",
        "verde" to "com.pepotech.pepoboveda.IconoVerde",
        "verde_claro" to "com.pepotech.pepoboveda.IconoVerdeClaro",
        "lima" to "com.pepotech.pepoboveda.IconoLima",
        "amarillo" to "com.pepotech.pepoboveda.IconoAmarillo",
        "naranja" to "com.pepotech.pepoboveda.IconoNaranja",
        "naranja_oscuro" to "com.pepotech.pepoboveda.IconoNaranjaOscuro",
        "marron" to "com.pepotech.pepoboveda.IconoMarron",
        "gris" to "com.pepotech.pepoboveda.IconoGris",
        "gris_azulado" to "com.pepotech.pepoboveda.IconoGrisAzulado"
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

