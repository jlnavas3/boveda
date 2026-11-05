/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Máscara booleana inmutable que representa los 9 segmentos de campos de una entrada
 * para reflejar qué datos fueron modificados o incorporados durante una sincronización.
 *
 * @property usuario Cambios en nombre de usuario o identidad de inicio de sesión.
 * @property clave Cambios en la contraseña actual o absorción de credenciales en el historial.
 * @property totp Cambios en semilla TOTP, emisor, algoritmo o códigos 2FA secundarios.
 * @property passkey Cambios en la llave de acceso principal o llaves secundarias.
 * @property web Cambios en direcciones URL de sitios web o dominios asociados.
 * @property app Cambios en paquetes de aplicaciones Android vinculadas.
 * @property notas Cambios en el texto de notas seguras.
 * @property submodelos Cambios en datos nativos de tarjeta, wifi, banco, identidad, etc.
 * @property camposExtra Cambios en campos personalizados o metadatos adicionales.
 */
data class MascaraCampos(
    val usuario: Boolean = false,
    val clave: Boolean = false,
    val totp: Boolean = false,
    val passkey: Boolean = false,
    val web: Boolean = false,
    val app: Boolean = false,
    val notas: Boolean = false,
    val submodelos: Boolean = false,
    val camposExtra: Boolean = false
) {
    /** True si al menos uno de los 9 segmentos refleja cambios activos. */
    val hayCambios: Boolean
        get() = usuario || clave || totp || passkey || web || app || notas || submodelos || camposExtra

    companion object {
        val VACIA = MascaraCampos()
    }
}
