package com.jlnavas3.bovedalocal.ui.componentes

/**
 * Lista de caracteres del abecedario ordenado con localización en español ('Ñ' opcional)
 * y símbolo '#' al inicio para números y caracteres especiales.
 */
fun obtenerLetrasIndice(incluirEnie: Boolean = true): List<Char> =
    listOf('#') + ('A'..'N').toList() + (if (incluirEnie) listOf('Ñ') else emptyList()) + ('O'..'Z').toList()

val LETRAS_INDICE: List<Char> get() = obtenerLetrasIndice(true)
