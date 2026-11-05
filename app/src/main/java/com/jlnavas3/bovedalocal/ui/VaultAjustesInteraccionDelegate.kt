/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.AjustesAutocompletado
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.util.CambiadorIcono
import com.jlnavas3.bovedalocal.util.Diagnostico

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

    fun ajustarAvisosEmergentesHabilitados(habilitados: Boolean) {
        repositorio.ajustes.actualizar { it.copy(avisosEmergentesHabilitados = habilitados) }
    }

    fun ajustarToastDescriptivo(descriptivo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(toastDescriptivo = descriptivo) }
    }

    fun ajustarPosicionToast(posicion: com.jlnavas3.bovedalocal.data.PosicionToast) {
        repositorio.ajustes.actualizar { it.copy(posicionToast = posicion) }
    }

    fun ajustarAutofillSugerenciasTeclado(activado: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarAutocompletado { a -> a.copy(sugerenciasTeclado = activado) } }
        Diagnostico.apuntar("autofill", "Sugerencias en teclado (inline) ${if (activado) "activadas" else "desactivadas"}")
    }

    fun ajustarAutofillCopiarTotp(activado: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarAutocompletado { a -> a.copy(copiarTotpAutomatico = activado) } }
        Diagnostico.apuntar("autofill", "Copia automática de TOTP en autofill ${if (activado) "activada" else "desactivada"}")
    }

    fun ajustarMostrarDatosTecnicosPasskey(mostrar: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarAutocompletado { a -> a.copy(mostrarDatosTecnicosPasskey = mostrar) } }
        Diagnostico.apuntar("passkey", "Visualización de datos técnicos de llaves ${if (mostrar) "activada" else "desactivada"}")
    }

    fun agregarPistaUsuario(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasUsuario.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasUsuario = a.pistasUsuario + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaUsuario(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasUsuario = a.pistasUsuario.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaContrasena(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasContrasena.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasContrasena = a.pistasContrasena + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaContrasena(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasContrasena = a.pistasContrasena.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaOtp(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasOtp.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasOtp = a.pistasOtp + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaOtp(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasOtp = a.pistasOtp.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDocumento(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDocumento.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDocumento = a.pistasDocumento + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDocumento(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDocumento = a.pistasDocumento.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaTarjetaNumero(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasTarjetaNumero.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasTarjetaNumero = a.pistasTarjetaNumero + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaTarjetaNumero(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasTarjetaNumero = a.pistasTarjetaNumero.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaTarjetaCvv(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasTarjetaCvv.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasTarjetaCvv = a.pistasTarjetaCvv + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaTarjetaCvv(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasTarjetaCvv = a.pistasTarjetaCvv.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaTarjetaExpiracion(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasTarjetaExpiracion.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasTarjetaExpiracion = a.pistasTarjetaExpiracion + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaTarjetaExpiracion(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasTarjetaExpiracion = a.pistasTarjetaExpiracion.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaTarjetaTitular(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasTarjetaTitular.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasTarjetaTitular = a.pistasTarjetaTitular + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaTarjetaTitular(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasTarjetaTitular = a.pistasTarjetaTitular.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDireccionCalle(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDireccionCalle.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDireccionCalle = a.pistasDireccionCalle + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDireccionCalle(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDireccionCalle = a.pistasDireccionCalle.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDireccionCp(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDireccionCp.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDireccionCp = a.pistasDireccionCp + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDireccionCp(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDireccionCp = a.pistasDireccionCp.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDireccionCiudad(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDireccionCiudad.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDireccionCiudad = a.pistasDireccionCiudad + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDireccionCiudad(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDireccionCiudad = a.pistasDireccionCiudad.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDireccionProvincia(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDireccionProvincia.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDireccionProvincia = a.pistasDireccionProvincia + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDireccionProvincia(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDireccionProvincia = a.pistasDireccionProvincia.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaDireccionPais(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasDireccionPais.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasDireccionPais = a.pistasDireccionPais + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaDireccionPais(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasDireccionPais = a.pistasDireccionPais.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarTipoDocumentoPersonalizado(sigla: String) {
        val limpia = sigla.trim().uppercase()
        if (limpia.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.tiposDocumentoPersonalizados.contains(limpia)) {
                    it.actualizarAutocompletado { a -> a.copy(tiposDocumentoPersonalizados = a.tiposDocumentoPersonalizados + limpia) }
                } else it
            }
        }
    }

    fun eliminarTipoDocumentoPersonalizado(sigla: String) {
        val limpia = sigla.trim().uppercase()
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(tiposDocumentoPersonalizados = a.tiposDocumentoPersonalizados.filterNot { t -> t.equals(limpia, ignoreCase = true) }) }
        }
    }

    fun agregarPaqueteComunicacion(paquete: String) {
        val limpio = paquete.trim().lowercase().removePrefix("android://")
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                it.actualizarAutocompletado { a -> a.copy(paquetesComunicacion = a.paquetesComunicacion + limpio) }
            }
        }
    }

    fun eliminarPaqueteComunicacion(paquete: String) {
        val limpio = paquete.trim().lowercase().removePrefix("android://")
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(paquetesComunicacion = a.paquetesComunicacion.filterNot { p -> p.equals(limpio, ignoreCase = true) }.toSet()) }
        }
    }

    fun agregarPistaContactoTelefono(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasContactoTelefono.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasContactoTelefono = a.pistasContactoTelefono + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaContactoTelefono(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasContactoTelefono = a.pistasContactoTelefono.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarPistaContactoNombre(pista: String) {
        val limpio = pista.trim().lowercase()
        if (limpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.autofillPistasContactoNombre.contains(limpio)) {
                    it.actualizarAutocompletado { a -> a.copy(pistasContactoNombre = a.pistasContactoNombre + limpio) }
                } else it
            }
        }
    }

    fun eliminarPistaContactoNombre(pista: String) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(pistasContactoNombre = a.pistasContactoNombre.filterNot { p -> p.equals(pista, ignoreCase = true) }) }
        }
    }

    fun agregarMapeoPaquete(paquete: String, dominio: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        val dominioLimpio = dominio.trim().lowercase().removePrefix("https://").removePrefix("http://").removePrefix("www.").substringBefore('/')
        if (paqueteLimpio.isNotBlank() && dominioLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                it.actualizarAutocompletado { a -> a.copy(mapeoPaquetesPersonalizados = a.mapeoPaquetesPersonalizados + (paqueteLimpio to dominioLimpio)) }
            }
        }
    }

    fun eliminarMapeoPaquete(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(mapeoPaquetesPersonalizados = a.mapeoPaquetesPersonalizados.filterKeys { k -> !k.equals(paqueteLimpio, ignoreCase = true) }) }
        }
    }

    fun agregarNavegadorPersonalizado(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        if (paqueteLimpio.isNotBlank()) {
            repositorio.ajustes.actualizar {
                if (!it.navegadoresPersonalizados.contains(paqueteLimpio)) {
                    it.actualizarAutocompletado { a -> a.copy(navegadoresPersonalizados = (a.navegadoresPersonalizados + paqueteLimpio).sorted()) }
                } else it
            }
        }
    }

    fun eliminarNavegadorPersonalizado(paquete: String) {
        val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(navegadoresPersonalizados = a.navegadoresPersonalizados.filterNot { p -> p.equals(paqueteLimpio, ignoreCase = true) }) }
        }
    }

    fun ajustarMaxSugerenciasAutofill(max: Int) {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { a -> a.copy(maxSugerenciasAutofill = max) }
        }
        val desc = if (max == 0) "todas las compatibles" else "$max"
        Diagnostico.apuntar("autofill", "Límite de sugerencias simultáneas configurado en: $desc")
    }

    fun restablecerReglasAutocompletado() {
        repositorio.ajustes.actualizar {
            it.actualizarAutocompletado { AjustesAutocompletado() }
        }
        Diagnostico.apuntar("autofill", "Reglas y opciones de autocompletado restablecidas a valores de fábrica")
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
