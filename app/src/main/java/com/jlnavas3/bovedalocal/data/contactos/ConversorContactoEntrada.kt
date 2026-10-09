package com.jlnavas3.bovedalocal.data.contactos

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import java.util.UUID

/**
 * Microconversor puro para transformar un [ContactoDispositivo]
 * en una [Entrada] de tipo [TipoEntrada.CONTACTO] con sus campos estructurados.
 */
object ConversorContactoEntrada {

    fun convertir(contacto: ContactoDispositivo): Entrada {
        val primerTelefono = contacto.telefonos.firstOrNull() ?: ""
        val primerCorreo = contacto.correos.firstOrNull() ?: ""
        val usuarioPrincipal = primerTelefono.ifBlank { primerCorreo }

        val camposPersonalizados = mutableListOf<CampoPersonalizado>()

        val telefonosRestantes = if (usuarioPrincipal == primerTelefono && primerTelefono.isNotBlank()) {
            contacto.telefonos.drop(1)
        } else {
            contacto.telefonos
        }

        telefonosRestantes.forEachIndexed { i, tel ->
            camposPersonalizados.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = if (i == 0 && usuarioPrincipal != primerTelefono) "Teléfono" else "Teléfono ${i + 2}",
                    valor = tel,
                    tipo = TipoCampo.TELEFONO,
                    esSensible = false
                )
            )
        }

        val correosRestantes = if (usuarioPrincipal == primerCorreo && primerCorreo.isNotBlank()) {
            contacto.correos.drop(1)
        } else {
            contacto.correos
        }

        correosRestantes.forEachIndexed { i, email ->
            camposPersonalizados.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = if (i == 0) "Correo electrónico" else "Correo secundario",
                    valor = email,
                    tipo = TipoCampo.EMAIL,
                    esSensible = false
                )
            )
        }

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

        val ahora = System.currentTimeMillis()
        return Entrada(
            id = UUID.randomUUID().toString(),
            titulo = contacto.nombre,
            usuario = usuarioPrincipal,
            tipo = TipoEntrada.CONTACTO,
            camposPersonalizados = camposPersonalizados,
            notas = contacto.notas ?: "",
            creadaEn = ahora,
            modificadaEn = ahora
        )
    }
}
