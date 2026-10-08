package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.util.CambiadorIcono

/**
 * Sub-delegado especializado en interacción táctil (háptica), efectos de alumbrado, icono launcher y opciones de sistema.
 */
interface VaultAjustesInteraccionDelegate {
    val repositorio: VaultRepository
    fun obtenerApp(): Application

    fun ajustarNombrePersonalizado(nombre: String) =
        repositorio.ajustes.actualizar { it.copy(nombrePersonalizado = nombre) }

    /** Cambia el icono del launcher (las 20 variantes precompiladas de Android). */
    fun ajustarIconoLauncher(clave: String) {
        repositorio.ajustes.actualizar { it.copy(iconoLauncher = clave) }
        CambiadorIcono.aplicar(obtenerApp(), clave)
    }

    fun restablecerIconoLauncher() = ajustarIconoLauncher(AjustesDefaults.Tema.ICONO_LAUNCHER)

    fun ajustarMostrarIdsAjustes(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(mostrarIdsAjustes = mostrar) }
    }

    fun ajustarAutofillSugerenciasTeclado(activado: Boolean) {
        repositorio.ajustes.actualizar { it.copy(autofillSugerenciasTeclado = activado) }
    }

    fun agregarPistaUsuario(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasUsuario.contains(limpio)) {
                    it.copy(autofillPistasUsuario = it.autofillPistasUsuario + limpio)
                } else it
            }
        }
    }

    fun eliminarPistaUsuario(pista: String) {
        repositorio.ajustes.actualizar {
            it.copy(autofillPistasUsuario = it.autofillPistasUsuario.filterNot { p -> p.equals(pista, ignoreCase = true) })
        }
    }

    fun agregarPistaContrasena(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasContrasena.contains(limpio)) {
                    it.copy(autofillPistasContrasena = it.autofillPistasContrasena + limpio)
                } else it
            }
        }
    }

    fun eliminarPistaContrasena(pista: String) {
        repositorio.ajustes.actualizar {
            it.copy(autofillPistasContrasena = it.autofillPistasContrasena.filterNot { p -> p.equals(pista, ignoreCase = true) })
        }
    }

    fun agregarPistaOtp(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasOtp.contains(limpio)) {
                    it.copy(autofillPistasOtp = it.autofillPistasOtp + limpio)
                } else it
            }
        }
    }

    fun eliminarPistaOtp(pista: String) {
        repositorio.ajustes.actualizar {
            it.copy(autofillPistasOtp = it.autofillPistasOtp.filterNot { p -> p.equals(pista, ignoreCase = true) })
        }
    }

    fun agregarMapeoPaquete(paquete: String, dominio: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        val dominioLimpio = dominio.trim().lowercase().removePrefix("https://").removePrefix("http://").removePrefix("www.").substringBefore('/')
        if (paqueteLimpio.isNotBlank() && dominioLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                it.copy(mapeoPaquetesPersonalizados = it.mapeoPaquetesPersonalizados + (paqueteLimpio to dominioLimpio))
            }
        }
    }

    fun eliminarMapeoPaquete(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        repositorio.ajustes.actualizar {
            it.copy(mapeoPaquetesPersonalizados = it.mapeoPaquetesPersonalizados.filterKeys { k -> !k.equals(paqueteLimpio, ignoreCase = true) })
        }
    }

    fun agregarNavegadorPersonalizado(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        if (paqueteLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.navegadoresPersonalizados.contains(paqueteLimpio)) {
                    it.copy(navegadoresPersonalizados = (it.navegadoresPersonalizados + paqueteLimpio).sorted())
                } else it
            }
        }
    }

    fun eliminarNavegadorPersonalizado(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        repositorio.ajustes.actualizar {
            it.copy(navegadoresPersonalizados = it.navegadoresPersonalizados.filterNot { p -> p.equals(paqueteLimpio, ignoreCase = true) })
        }
    }

    fun ajustarMaxSugerenciasAutofill(max: Int) {
        repositorio.ajustes.actualizar {
            it.copy(maxSugerenciasAutofill = max)
        }
    }

    fun restablecerReglasAutocompletado() {
        repositorio.ajustes.actualizar {
            it.copy(
                autofillPistasUsuario = AjustesDefaults.Autocompletado.PISTAS_USUARIO,
                autofillPistasContrasena = AjustesDefaults.Autocompletado.PISTAS_CONTRASENA,
                autofillPistasOtp = AjustesDefaults.Autocompletado.PISTAS_OTP,
                autofillSugerenciasTeclado = AjustesDefaults.Autocompletado.SUGERENCIAS_TECLADO,
                mapeoPaquetesPersonalizados = AjustesDefaults.Autocompletado.MAPEO_PAQUETES_PERSONALIZADOS,
                navegadoresPersonalizados = AjustesDefaults.Autocompletado.NAVEGADORES_PERSONALIZADOS,
                maxSugerenciasAutofill = AjustesDefaults.Autocompletado.MAX_SUGERENCIAS_AUTOFILL
            )
        }
    }

    fun ajustarAlumbradoActivo(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(alumbradoActivo = activo) }
        AlumbradoActivo = activo
    }

    fun ajustarAlumbradoIntensidad(intensidad: Float) {
        val valor = intensidad.coerceIn(0.1f, 1.0f)
        repositorio.ajustes.actualizar { it.copy(alumbradoIntensidad = valor) }
        AlumbradoIntensidad = valor
    }

    fun ajustarAlumbradoRepeticiones(repeticiones: Int) {
        val valor = repeticiones.coerceIn(1, 5)
        repositorio.ajustes.actualizar { it.copy(alumbradoRepeticiones = valor) }
        AlumbradoRepeticiones = valor
    }

    fun ajustarAlumbradoDuracionMs(duracionMs: Int) {
        val valor = duracionMs.coerceIn(300, 2000)
        repositorio.ajustes.actualizar { it.copy(alumbradoDuracionMs = valor) }
        AlumbradoDuracionMs = valor
    }

    fun restablecerAlumbrado() {
        repositorio.ajustes.actualizar {
            it.copy(
                alumbradoActivo = AjustesDefaults.Interaccion.ALUMBRADO_ACTIVO,
                alumbradoIntensidad = AjustesDefaults.Interaccion.ALUMBRADO_INTENSIDAD,
                alumbradoRepeticiones = AjustesDefaults.Interaccion.ALUMBRADO_REPETICIONES,
                alumbradoDuracionMs = AjustesDefaults.Interaccion.ALUMBRADO_DURACION_MS
            )
        }
    }

    fun ajustarHapticaApp(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(hapticaApp = activo) }

    fun ajustarHapticaAppIntensidad(intensidad: Float) =
        repositorio.ajustes.actualizar { it.copy(hapticaAppIntensidad = intensidad.coerceIn(0.01f, 1.0f)) }

    fun restablecerHapticaApp() {
        repositorio.ajustes.actualizar {
            it.copy(
                hapticaApp = AjustesDefaults.Interaccion.HAPTICA_APP,
                hapticaAppIntensidad = AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD
            )
        }
    }

    fun ajustarDiagnosticoMaxEventos(max: Int) {
        val valor = max.coerceIn(50, 5000)
        repositorio.ajustes.actualizar { it.copy(diagnosticoMaxEventos = valor) }
        com.jlnavas3.bovedalocal.util.Diagnostico.configurar(valor)
    }

    fun ajustarOrdenAjustesPersonalizado(orden: List<String>) {
        repositorio.ajustes.actualizar { it.copy(ordenAjustesPersonalizado = orden) }
    }

    fun restablecerOrdenAjustesPersonalizado() {
        repositorio.ajustes.actualizar { it.copy(ordenAjustesPersonalizado = AjustesDefaults.Interaccion.ORDEN_AJUSTES_PERSONALIZADO) }
    }

    fun ajustarOrdenJerarquiaPersonalizado(orden: Map<String, List<String>>) {
        repositorio.ajustes.actualizar { it.copy(ordenJerarquiaPersonalizado = orden) }
    }

    fun ajustarReparentingPersonalizado(reparenting: Map<String, String>) {
        repositorio.ajustes.actualizar { it.copy(reparentingPersonalizado = reparenting) }
    }

    fun restablecerTodoArbolAjustes() {
        repositorio.ajustes.actualizar {
            it.copy(
                ordenAjustesPersonalizado = AjustesDefaults.Interaccion.ORDEN_AJUSTES_PERSONALIZADO,
                ordenJerarquiaPersonalizado = emptyMap(),
                reparentingPersonalizado = emptyMap()
            )
        }
    }

    fun ajustarMenuLateralMostrarCabecera(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(menuLateralMostrarCabecera = mostrar) }
    }

    fun ajustarMenuLateralMostrarPie(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(menuLateralMostrarPie = mostrar) }
    }

    fun ajustarMenuLateralMostrarBotonBloquear(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(menuLateralMostrarBotonBloquear = mostrar) }
    }

    fun ajustarMenuLateralAgruparItems(agrupar: Boolean) {
        repositorio.ajustes.actualizar { it.copy(menuLateralAgruparItems = agrupar) }
    }

    fun ajustarMenuLateralSinBordes(sinBordes: Boolean) {
        repositorio.ajustes.actualizar { it.copy(menuLateralSinBordes = sinBordes) }
    }

    fun ajustarMenuLateralItemsVisibles(items: List<String>) {
        repositorio.ajustes.actualizar { it.copy(menuLateralItemsVisibles = items) }
    }

    fun restablecerMenuLateral() {
        repositorio.ajustes.actualizar {
            it.copy(
                menuLateralMostrarCabecera = AjustesDefaults.MenuLateral.MOSTRAR_CABECERA,
                menuLateralMostrarPie = AjustesDefaults.MenuLateral.MOSTRAR_PIE,
                menuLateralMostrarBotonBloquear = AjustesDefaults.MenuLateral.MOSTRAR_BOTON_BLOQUEAR,
                menuLateralAgruparItems = AjustesDefaults.MenuLateral.AGRUPAR_ITEMS,
                menuLateralSinBordes = AjustesDefaults.MenuLateral.SIN_BORDES,
                menuLateralItemsVisibles = AjustesDefaults.MenuLateral.ITEMS_PREDETERMINADOS
            )
        }
    }

    fun recargarAjustes() {
        repositorio.ajustes.recargar()
    }
}
