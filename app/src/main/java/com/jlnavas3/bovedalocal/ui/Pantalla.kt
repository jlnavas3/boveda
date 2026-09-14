package com.jlnavas3.bovedalocal.ui

sealed interface Pantalla {
    object Onboarding : Pantalla
    object Desbloqueo : Pantalla
    object Lista : Pantalla
    data class Detalle(val id: String) : Pantalla
    data class Editar(val id: String?, val contrasenaInicial: String = "") : Pantalla
    object Generador : Pantalla
    object Passkeys : Pantalla
    object Autenticador : Pantalla
    /** Si [entradaDestino] es null, el QR crea una entrada nueva de 2FA. */
    data class Escaner(
        val entradaDestino: String? = null,
        /** true = entrar directo a escribir la clave a mano, sin cámara. */
        val soloManual: Boolean = false
    ) : Pantalla
    object Ajustes : Pantalla
    object AjustesIndice : Pantalla
    object Tema : Pantalla
    object Formas : Pantalla
    object Tipografia : Pantalla
    object AcercaDe : Pantalla
    object Registro : Pantalla
    object SaludBoveda : Pantalla
    object Papelera : Pantalla
    object KitEmergencia : Pantalla
    object AjustesSenuelo : Pantalla
    object AjustesAutodestruccion : Pantalla
    object FormatosCampos : Pantalla
}

enum class CriterioOrdenacion(val etiqueta: String) {
    NOMBRE_AZ("Nombre (A-Z)"),
    NOMBRE_ZA("Nombre (Z-A)"),
    MODIFICACION_RECIENTE("Modificado recientemente"),
    CREACION_RECIENTE("Añadido recientemente"),
    ANTIGUEDAD("Más antiguos primero")
}
