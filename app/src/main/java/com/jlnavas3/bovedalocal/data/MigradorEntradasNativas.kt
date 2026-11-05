/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Migrador puro y desacoplado encargado de transformar entradas heredadas
 * que almacenaban sus atributos base dentro de [CampoPersonalizado] hacia
 * los nuevos modelos de datos fuertemente tipados de primer nivel.
 */
object MigradorEntradasNativas {

    private fun buscarValor(campos: List<CampoPersonalizado>, vararg claves: String): String {
        for (clave in claves) {
            val directo = campos.firstOrNull { it.etiqueta.equals(clave, ignoreCase = true) }?.valor
            if (!directo.isNullOrBlank()) return directo
        }
        for (clave in claves) {
            val porInicio = campos.firstOrNull { it.etiqueta.startsWith(clave, ignoreCase = true) }?.valor
            if (!porInicio.isNullOrBlank()) return porInicio
        }
        return ""
    }

    private fun coincideAlguna(etiqueta: String, vararg claves: String): Boolean {
        val limpia = etiqueta.trim().lowercase()
        return claves.any { c ->
            val cLimpia = c.trim().lowercase()
            limpia == cLimpia || limpia.startsWith(cLimpia) || cLimpia.startsWith(limpia)
        }
    }

    fun extraerTarjeta(entrada: Entrada): DatosTarjetaBancaria {
        entrada.datosTarjeta?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosTarjetaBancaria(
            titular = buscarValor(campos, "Titular"),
            numero = buscarValor(campos, "Número de tarjeta", "Numero de tarjeta", "Tarjeta"),
            vencimiento = buscarValor(campos, "Vencimiento", "Fecha de vencimiento", "Expiración"),
            cvv = buscarValor(campos, "CVV", "CVC", "Código de seguridad"),
            pin = buscarValor(campos, "PIN de tarjeta", "PIN", "PIN del cajero")
        )
    }

    fun extraerWifi(entrada: Entrada): DatosRedWifi {
        entrada.datosWifi?.let { return it }
        val campos = entrada.camposPersonalizados
        val ssid = buscarValor(campos, "Nombre de red (SSID)", "SSID", "Red").ifBlank { entrada.titulo }
        val clave = buscarValor(campos, "Contraseña Wi-Fi", "Contraseña wifi", "Contraseña", "Password").ifBlank { entrada.contrasena }
        val seg = buscarValor(campos, "Tipo de seguridad", "Seguridad").ifBlank { "WPA" }
        val oculta = campos.any {
            coincideAlguna(it.etiqueta, "Red oculta", "Oculta") && it.valor.equals("true", ignoreCase = true)
        }
        return DatosRedWifi(
            ssid = ssid,
            contrasena = clave,
            tipoSeguridad = seg,
            oculta = oculta
        )
    }

    fun extraerCuentaBancaria(entrada: Entrada): DatosCuentaBancaria {
        entrada.datosCuentaBancaria?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosCuentaBancaria(
            banco = buscarValor(campos, "Banco / Entidad", "Banco", "Entidad"),
            titular = buscarValor(campos, "Titular", "Titular de la cuenta"),
            tipoCuenta = buscarValor(campos, "Tipo de cuenta", "Cuenta"),
            numeroCuenta = buscarValor(campos, "Número de cuenta / IBAN", "Número de cuenta", "IBAN", "Cuenta"),
            swiftBic = buscarValor(campos, "CBU / CLABE / SWIFT", "SWIFT / CBU / CLABE", "SWIFT", "BIC"),
            cbuClabe = buscarValor(campos, "CBU", "CLABE")
        )
    }

    fun extraerIdentidad(entrada: Entrada): DatosDocumentoIdentidad {
        entrada.datosIdentidad?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosDocumentoIdentidad(
            tipoDocumento = buscarValor(campos, "Tipo de documento", "Documento").ifBlank { "CI" },
            numero = buscarValor(campos, "Número de documento", "Número", "Documento", "Numero"),
            nombreCompleto = buscarValor(campos, "Nombre completo", "Titular", "Nombre").ifBlank { entrada.usuario },
            expedicion = buscarValor(campos, "Fecha de expedición", "Expedición", "Emisión"),
            caducidad = buscarValor(campos, "Fecha de caducidad", "Caducidad", "Vencimiento"),
            paisEmisor = buscarValor(campos, "País emisor", "País", "Pais")
        )
    }

    fun extraerServidor(entrada: Entrada): DatosServidorSsh {
        entrada.datosServidor?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosServidorSsh(
            host = buscarValor(campos, "Host o IP", "Host", "IP", "Servidor"),
            puerto = buscarValor(campos, "Puerto", "Port").ifBlank { "22" },
            usuario = buscarValor(campos, "Usuario SSH", "Usuario").ifBlank { entrada.usuario },
            clavePrivada = buscarValor(campos, "Clave privada / Contraseña", "Clave privada / Password", "Clave privada", "Password").ifBlank { entrada.contrasena },
            clavePublica = buscarValor(campos, "Clave pública", "Clave publica", "Public key")
        )
    }

    fun extraerWallet(entrada: Entrada): DatosCriptoWallet {
        entrada.datosWallet?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosCriptoWallet(
            red = buscarValor(campos, "Red / Blockchain", "Red", "Blockchain").ifBlank { "Bitcoin" },
            direccionPublica = buscarValor(campos, "Dirección pública", "Dirección", "Public address"),
            fraseSemilla = buscarValor(campos, "Frase semilla (Seed phrase)", "Frase semilla", "Seed phrase", "Mnemónico"),
            clavePrivada = buscarValor(campos, "Clave privada", "Private key")
        )
    }

    fun extraerContacto(entrada: Entrada): DatosContacto {
        entrada.datosContacto?.let { return it }
        val campos = entrada.camposPersonalizados
        return DatosContacto(
            telefonoSecundario = buscarValor(campos, "Teléfono secundario", "Teléfono 2", "Celular"),
            correo = buscarValor(campos, "Correo electrónico", "Correo", "Email"),
            direccion = buscarValor(campos, "Dirección", "Direccion", "Calle", "Domicilio"),
            codigoPostal = buscarValor(campos, "Código postal", "Codigo postal", "ZIP", "CP"),
            ciudad = buscarValor(campos, "Ciudad", "Localidad", "Municipio"),
            provincia = buscarValor(campos, "Provincia", "Estado", "Departamento", "Región"),
            pais = buscarValor(campos, "País", "Pais")
        )
    }

    fun normalizarTexto(texto: String): String {
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .trim()
            .lowercase()
    }

    /**
     * Determina si una etiqueta corresponde a un atributo base de uno de los 6 tipos
     * nativos de primer nivel, reconociendo sinónimos, abreviaturas y variaciones ortográficas.
     */
    fun esCampoBaseNativo(tipo: TipoEntrada, etiqueta: String): Boolean {
        val limpia = normalizarTexto(etiqueta)
        if (limpia.isBlank()) return false
        val sinonimos: List<String> = when (tipo) {
            TipoEntrada.TARJETA -> listOf(
                "titular", "nombre del titular", "titular de la tarjeta", "nombre en la tarjeta",
                "numero de tarjeta", "numero", "tarjeta", "pan",
                "vencimiento", "fecha de vencimiento", "expiracion", "fecha de expiracion", "caducidad", "validez",
                "cvv", "cvc", "codigo de seguridad", "cvv2", "cid",
                "pin de tarjeta", "pin", "pin del cajero"
            )
            TipoEntrada.WIFI -> listOf(
                "nombre de red (ssid)", "nombre de red", "ssid", "red",
                "contrasena wi-fi", "contrasena wifi", "contrasena", "password", "clave wifi", "clave",
                "tipo de seguridad", "seguridad", "cifrado", "encriptacion",
                "red oculta", "oculta", "hidden"
            )
            TipoEntrada.CUENTA_BANCARIA -> listOf(
                "banco / entidad", "banco", "entidad", "institucion financiera",
                "titular de la cuenta", "titular", "nombre del titular",
                "tipo de cuenta", "cuenta",
                "numero de cuenta / iban", "numero de cuenta", "iban", "cuenta bancaria", "no de cuenta",
                "cbu / clabe / swift", "swift / cbu / clabe", "swift", "bic", "swift/bic", "cbu", "clabe", "cci"
            )
            TipoEntrada.IDENTIDAD -> listOf(
                "tipo de documento", "tipo documento", "tipo de doc", "tipo doc", "documento",
                "numero de documento", "numero documento", "numero", "cedula", "dni", "pasaporte", "ruc",
                "nombre completo", "titular del documento", "titular",
                "fecha de expedicion", "expedicion", "fecha de emision", "emision",
                "fecha de caducidad", "caducidad", "fecha de vencimiento", "vencimiento", "fecha de expiracion", "expiracion",
                "pais emisor", "pais", "nacionalidad"
            )
            TipoEntrada.SERVIDOR -> listOf(
                "host o ip", "host / ip", "host", "ip", "servidor",
                "puerto", "port",
                "usuario ssh", "usuario", "user",
                "clave privada / contrasena", "clave privada / password", "clave privada", "private key",
                "clave publica", "public key"
            )
            TipoEntrada.WALLET -> listOf(
                "red / blockchain", "red", "blockchain", "network",
                "direccion publica", "direccion", "public address", "wallet address",
                "frase semilla (seed phrase)", "frase semilla", "seed phrase", "semilla", "mnemonico", "12 palabras", "24 palabras",
                "clave privada", "private key", "secret key"
            )
            TipoEntrada.CONTACTO -> listOf(
                "telefono secundario", "telefono 2", "celular",
                "correo electronico", "correo", "email",
                "direccion", "calle", "domicilio",
                "codigo postal", "zip", "cp",
                "ciudad", "localidad", "municipio",
                "provincia", "estado", "departamento", "region",
                "pais"
            )
            else -> return false
        }
        return sinonimos.any { s ->
            val sNorm = normalizarTexto(s)
            limpia == sNorm ||
            (sNorm.length >= 4 && limpia.startsWith(sNorm)) ||
            (limpia.length >= 4 && sNorm.startsWith(limpia))
        }
    }

    /**
     * Filtra los campos personalizados eliminando aquellos que formaban parte
     * del preset original del tipo de entrada, conservando únicamente los campos extra
     * que el usuario añadió deliberadamente.
     */
    fun depurarCamposPersonalizados(tipo: TipoEntrada, campos: List<CampoPersonalizado>): List<CampoPersonalizado> {
        return campos.filterNot { campo ->
            esCampoBaseNativo(tipo, campo.etiqueta)
        }
    }

    /**
     * Migra una [Entrada] con campos planos heredados a una entrada estructurada nativa
     * y purga incondicionalmente los campos personalizados residuales para evitar duplicados.
     */
    fun migrarEntrada(entrada: Entrada): Entrada {
        return when (entrada.tipo) {
            TipoEntrada.TARJETA -> {
                val datos = entrada.datosTarjeta ?: extraerTarjeta(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosTarjeta = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.WIFI -> {
                val datos = entrada.datosWifi ?: extraerWifi(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosWifi = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                val datos = entrada.datosCuentaBancaria ?: extraerCuentaBancaria(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosCuentaBancaria = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.IDENTIDAD -> {
                val datos = entrada.datosIdentidad ?: extraerIdentidad(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosIdentidad = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.SERVIDOR -> {
                val datos = entrada.datosServidor ?: extraerServidor(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosServidor = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.WALLET -> {
                val datos = entrada.datosWallet ?: extraerWallet(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosWallet = datos, camposPersonalizados = camposRestantes)
            }
            TipoEntrada.CONTACTO -> {
                val datos = entrada.datosContacto ?: extraerContacto(entrada)
                val camposRestantes = depurarCamposPersonalizados(entrada.tipo, entrada.camposPersonalizados)
                entrada.copy(datosContacto = datos, camposPersonalizados = camposRestantes)
            }
            else -> entrada
        }
    }
}
