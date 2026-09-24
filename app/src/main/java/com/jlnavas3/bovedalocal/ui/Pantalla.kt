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
    open class Ajustes(val seccionId: String? = null) : Pantalla {
        companion object : Ajustes(null)
        override fun equals(other: Any?): Boolean = other is Ajustes && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesIndice(val seccionId: String? = null) : Pantalla {
        companion object : AjustesIndice(null)
        override fun equals(other: Any?): Boolean = other is AjustesIndice && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesWidget(val seccionId: String? = null) : Pantalla {
        companion object : AjustesWidget(null)
        override fun equals(other: Any?): Boolean = other is AjustesWidget && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Tema(val seccionId: String? = null) : Pantalla {
        companion object : Tema(null)
        override fun equals(other: Any?): Boolean = other is Tema && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class CalibracionAnimacion(val seccionId: String? = null) : Pantalla {
        companion object : CalibracionAnimacion(null)
        override fun equals(other: Any?): Boolean = other is CalibracionAnimacion && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class CalibracionWidgetTotp(val seccionId: String? = null) : Pantalla {
        companion object : CalibracionWidgetTotp(null)
        override fun equals(other: Any?): Boolean = other is CalibracionWidgetTotp && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class CalibracionWidget1x1(val seccionId: String? = null) : Pantalla {
        companion object : CalibracionWidget1x1(null)
        override fun equals(other: Any?): Boolean = other is CalibracionWidget1x1 && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Formas(val seccionId: String? = null) : Pantalla {
        companion object : Formas(null)
        override fun equals(other: Any?): Boolean = other is Formas && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Tipografia(val seccionId: String? = null) : Pantalla {
        companion object : Tipografia(null)
        override fun equals(other: Any?): Boolean = other is Tipografia && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class OrganizacionLista(val seccionId: String? = null) : Pantalla {
        companion object : OrganizacionLista(null)
        override fun equals(other: Any?): Boolean = other is OrganizacionLista && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AcercaDe(val seccionId: String? = null) : Pantalla {
        companion object : AcercaDe(null)
        override fun equals(other: Any?): Boolean = other is AcercaDe && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Registro(val seccionId: String? = null) : Pantalla {
        companion object : Registro(null)
        override fun equals(other: Any?): Boolean = other is Registro && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class SaludBoveda(val seccionId: String? = null) : Pantalla {
        companion object : SaludBoveda(null)
        override fun equals(other: Any?): Boolean = other is SaludBoveda && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Duplicados(val seccionId: String? = null) : Pantalla {
        companion object : Duplicados(null)
        override fun equals(other: Any?): Boolean = other is Duplicados && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Papelera(val seccionId: String? = null) : Pantalla {
        companion object : Papelera(null)
        override fun equals(other: Any?): Boolean = other is Papelera && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class KitEmergencia(val seccionId: String? = null) : Pantalla {
        companion object : KitEmergencia(null)
        override fun equals(other: Any?): Boolean = other is KitEmergencia && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesSenuelo(val seccionId: String? = null) : Pantalla {
        companion object : AjustesSenuelo(null)
        override fun equals(other: Any?): Boolean = other is AjustesSenuelo && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesAutodestruccion(val seccionId: String? = null) : Pantalla {
        companion object : AjustesAutodestruccion(null)
        override fun equals(other: Any?): Boolean = other is AjustesAutodestruccion && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class FormatosCampos(val seccionId: String? = null) : Pantalla {
        companion object : FormatosCampos(null)
        override fun equals(other: Any?): Boolean = other is FormatosCampos && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class HistorialClaves(val seccionId: String? = null) : Pantalla {
        companion object : HistorialClaves(null)
        override fun equals(other: Any?): Boolean = other is HistorialClaves && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Seguridad(val seccionId: String? = null) : Pantalla {
        companion object : Seguridad(null)
        override fun equals(other: Any?): Boolean = other is Seguridad && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class CopiaSeguridad(val seccionId: String? = null) : Pantalla {
        companion object : CopiaSeguridad(null)
        override fun equals(other: Any?): Boolean = other is CopiaSeguridad && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class CsvGoogle(val seccionId: String? = null) : Pantalla {
        companion object : CsvGoogle(null)
        override fun equals(other: Any?): Boolean = other is CsvGoogle && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    data class ConfirmarMigracion(val urlMigracion: String) : Pantalla
    open class ExportarSelectivo(val seccionInicial: String = "todos") : Pantalla {
        companion object : ExportarSelectivo("todos")
        override fun equals(other: Any?): Boolean = other is ExportarSelectivo && other.seccionInicial == seccionInicial
        override fun hashCode(): Int = seccionInicial.hashCode()
    }
    open class ColoresDatos(val seccionId: String? = null) : Pantalla {
        companion object : ColoresDatos(null)
        override fun equals(other: Any?): Boolean = other is ColoresDatos && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Argon2id(val seccionId: String? = null) : Pantalla {
        companion object : Argon2id(null)
        override fun equals(other: Any?): Boolean = other is Argon2id && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesAutenticador(val seccionId: String? = null) : Pantalla {
        companion object : AjustesAutenticador(null)
        override fun equals(other: Any?): Boolean = other is AjustesAutenticador && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class AjustesCamara(val seccionId: String? = null) : Pantalla {
        companion object : AjustesCamara(null)
        override fun equals(other: Any?): Boolean = other is AjustesCamara && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class TileRapido(val seccionId: String? = null) : Pantalla {
        companion object : TileRapido(null)
        override fun equals(other: Any?): Boolean = other is TileRapido && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
    open class Avanzada(val seccionId: String? = null) : Pantalla {
        companion object : Avanzada(null)
        override fun equals(other: Any?): Boolean = other is Avanzada && other.seccionId == seccionId
        override fun hashCode(): Int = seccionId?.hashCode() ?: 0
    }
}

enum class CriterioOrdenacion(val etiqueta: String) {
    NOMBRE_AZ("Nombre (A-Z)"),
    NOMBRE_ZA("Nombre (Z-A)"),
    MODIFICACION_RECIENTE("Modificado recientemente"),
    CREACION_RECIENTE("Añadido recientemente"),
    ANTIGUEDAD("Más antiguos primero")
}
