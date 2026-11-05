/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.DatosContacto
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import java.util.UUID

/**
 * Microconversor puro para transformar un [ContactoDispositivo]
 * en una [Entrada] de tipo [TipoEntrada.CONTACTO] con sus campos estructurados en [DatosContacto]
 * y campos adicionales en [CampoPersonalizado].
 */
object ConversorContactoEntrada {

    fun convertir(contacto: ContactoDispositivo): Entrada {
        val telefonosUnicos = mutableListOf<String>()
        for (tel in contacto.telefonos) {
            val idx = telefonosUnicos.indexOfFirst {
                FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, tel)
            }
            if (idx == -1) {
                telefonosUnicos.add(tel)
            } else {
                telefonosUnicos[idx] = FormateadorTelefonoInternacional.mejorRepresentacion(telefonosUnicos[idx], tel)
            }
        }

        val primerTelefono = telefonosUnicos.firstOrNull() ?: ""
        val segundoTelefono = telefonosUnicos.drop(1).firstOrNull {
            !FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, primerTelefono)
        } ?: ""
        val primerCorreo = contacto.correos.firstOrNull() ?: ""
        val primeraDireccion = contacto.direcciones.firstOrNull()
        val primerSitioWeb = contacto.sitiosWeb.firstOrNull() ?: ""

        val usuarioPrincipal = primerTelefono.ifBlank { primerCorreo }

        val datosContacto = DatosContacto(
            telefonoSecundario = if (usuarioPrincipal == primerTelefono && primerTelefono.isNotBlank()) segundoTelefono else "",
            correo = primerCorreo,
            direccion = primeraDireccion?.calle ?: "",
            codigoPostal = primeraDireccion?.codigoPostal ?: "",
            ciudad = primeraDireccion?.ciudad ?: "",
            provincia = primeraDireccion?.region ?: "",
            pais = primeraDireccion?.pais ?: ""
        )

        val camposPersonalizados = mutableListOf<CampoPersonalizado>()

        // Teléfonos adicionales (solo aquellos distintos al primario y secundario)
        val telefonosAdicionales = telefonosUnicos.filterNot {
            FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, primerTelefono) ||
                (segundoTelefono.isNotBlank() && FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, segundoTelefono))
        }

        telefonosAdicionales.forEachIndexed { i, tel ->
            camposPersonalizados.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Teléfono adicional ${i + 1}",
                    valor = tel,
                    tipo = TipoCampo.TELEFONO,
                    esSensible = false
                )
            )
        }

        // Correos adicionales (a partir del 2º si el 1º está en datosContacto)
        if (contacto.correos.size > 1) {
            contacto.correos.drop(1).forEachIndexed { i, email ->
                camposPersonalizados.add(
                    CampoPersonalizado(
                        id = UUID.randomUUID().toString(),
                        etiqueta = "Correo secundario ${if (i > 0) i + 2 else ""}".trim(),
                        valor = email,
                        tipo = TipoCampo.EMAIL,
                        esSensible = false
                    )
                )
            }
        }

        // Direcciones adicionales (a partir de la 2ª si la 1ª está en datosContacto)
        if (contacto.direcciones.size > 1) {
            contacto.direcciones.drop(1).forEachIndexed { i, dir ->
                val textoDir = listOf(dir.calle, dir.ciudad, dir.region, dir.codigoPostal, dir.pais)
                    .filter { it.isNotBlank() }
                    .joinToString(", ")
                if (textoDir.isNotBlank()) {
                    camposPersonalizados.add(
                        CampoPersonalizado(
                            id = UUID.randomUUID().toString(),
                            etiqueta = "Dirección (${dir.etiqueta})",
                            valor = textoDir,
                            tipo = TipoCampo.TEXTO,
                            esSensible = false
                        )
                    )
                }
            }
        }

        // Organización y cargo
        if (!contacto.organizacion.isNullOrBlank()) {
            camposPersonalizados.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Empresa / Organización",
                    valor = contacto.organizacion,
                    tipo = TipoCampo.TEXTO,
                    esSensible = false
                )
            )
        }

        if (!contacto.cargo.isNullOrBlank()) {
            camposPersonalizados.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Cargo / Puesto",
                    valor = contacto.cargo,
                    tipo = TipoCampo.TEXTO,
                    esSensible = false
                )
            )
        }

        // Sitios web adicionales
        if (contacto.sitiosWeb.size > 1) {
            contacto.sitiosWeb.drop(1).forEachIndexed { i, web ->
                camposPersonalizados.add(
                    CampoPersonalizado(
                        id = UUID.randomUUID().toString(),
                        etiqueta = "Sitio Web ${i + 2}",
                        valor = web,
                        tipo = TipoCampo.URL,
                        esSensible = false
                    )
                )
            }
        }

        // Campos adicionales no estándar capturados del archivo (estrategia Zero Data Loss)
        if (contacto.camposExtra.isNotEmpty()) {
            for ((etiqueta, valor) in contacto.camposExtra) {
                if (valor.isNotBlank() && camposPersonalizados.none { it.etiqueta.equals(etiqueta, ignoreCase = true) }) {
                    camposPersonalizados.add(
                        CampoPersonalizado(
                            id = UUID.randomUUID().toString(),
                            etiqueta = etiqueta,
                            valor = valor,
                            tipo = TipoCampo.TEXTO,
                            esSensible = false
                        )
                    )
                }
            }
        }

        val ahora = System.currentTimeMillis()
        return Entrada(
            id = UUID.randomUUID().toString(),
            titulo = contacto.nombre,
            usuario = usuarioPrincipal,
            urls = if (primerSitioWeb.isNotBlank()) listOf(primerSitioWeb) else emptyList(),
            tipo = TipoEntrada.CONTACTO,
            datosContacto = datosContacto,
            camposPersonalizados = camposPersonalizados,
            notas = contacto.notas ?: "",
            creadaEn = ahora,
            modificadaEn = ahora
        )
    }
}
