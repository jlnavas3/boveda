package com.jlnavas3.bovedalocal.data

enum class TipoDuplicado(val titulo: String, val descripcion: String) {
    IDENTICO(
        "Copia idéntica",
        "Mismo usuario, contraseña y servicio. Típico al re-importar el mismo archivo CSV."
    ),
    PASSKEY(
        "Llave de paso duplicada",
        "Entradas que comparten la misma credencial passkey o dominio RP."
    ),
    TOTP(
        "2FA / TOTP duplicado",
        "Entradas que comparten la misma clave secreta de verificación en dos pasos."
    ),
    MISMA_CUENTA_DISTINTA_CLAVE(
        "Misma cuenta (distinta clave)",
        "Mismo usuario y servicio, pero con contraseñas distintas."
    ),
    VARIANTE_USUARIO(
        "Variante de usuario",
        "Misma clave y servicio, pero con variantes en el nombre de usuario o correo."
    )
}

data class GrupoDuplicado(
    val idGrupo: String,
    val tipo: TipoDuplicado,
    val claveVisual: String,
    val entradas: List<Entrada>,
    val sugeridaPrincipal: Entrada,
    val esAppAndroid: Boolean = false
) {
    val entradasSecundarias: List<Entrada>
        get() = entradas.filterNot { it.id == sugeridaPrincipal.id }
}
