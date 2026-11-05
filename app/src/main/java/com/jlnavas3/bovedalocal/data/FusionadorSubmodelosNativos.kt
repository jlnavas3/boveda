/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Microcomponente encargado de fusionar submodelos nativos especializados (Tarjetas, Wi-Fi, Cuentas,
 * Documentos, Servidores, Wallets) de forma no destructiva:
 * - Enriquece los campos vacíos de la entrada principal con los datos de las secundarias.
 * - Si existen datos en conflicto (ej. números de tarjeta o claves distintas), genera bloques estructurados
 *   de respaldo en las notas para garantizar que no haya pérdida de información.
 */
object FusionadorSubmodelosNativos {

    data class ResultadoFusionNativa(
        val datosTarjeta: DatosTarjetaBancaria? = null,
        val datosWifi: DatosRedWifi? = null,
        val datosCuentaBancaria: DatosCuentaBancaria? = null,
        val datosIdentidad: DatosDocumentoIdentidad? = null,
        val datosServidor: DatosServidorSsh? = null,
        val datosWallet: DatosCriptoWallet? = null,
        val datosContacto: DatosContacto? = null,
        val notasDeRespaldo: List<String> = emptyList()
    )

    fun fusionar(principal: Entrada, secundarias: List<Entrada>): ResultadoFusionNativa {
        val notasRespaldo = mutableListOf<String>()

        var resTarjeta = principal.tarjetaEfectiva
        var resWifi = principal.wifiEfectivo
        var resCuenta = principal.cuentaBancariaEfectiva
        var resIdentidad = principal.identidadEfectiva
        var resServidor = principal.servidorEfectivo
        var resWallet = principal.walletEfectiva
        var resContacto = principal.contactoEfectivo

        for (sec in secundarias) {
            when (principal.tipo) {
                TipoEntrada.TARJETA -> {
                    val secT = sec.tarjetaEfectiva
                    if (secT != null) {
                        val base = resTarjeta ?: DatosTarjetaBancaria()
                        if (base.numero.isNotBlank() && secT.numero.isNotBlank() &&
                            base.numero.filter { it.isDigit() } != secT.numero.filter { it.isDigit() }
                        ) {
                            notasRespaldo.add(
                                "[Tarjeta secundaria respaldada al unificar: " +
                                "Titular: ${secT.titular.ifBlank { "N/A" }} | " +
                                "Número: ${secT.numero} | " +
                                "Vencimiento: ${secT.vencimiento.ifBlank { "N/A" }} | " +
                                "CVV: ${secT.cvv.ifBlank { "N/A" }} | " +
                                "PIN: ${secT.pin.ifBlank { "N/A" }}]"
                            )
                        } else {
                            resTarjeta = base.copy(
                                titular = base.titular.ifBlank { secT.titular },
                                numero = base.numero.ifBlank { secT.numero },
                                vencimiento = base.vencimiento.ifBlank { secT.vencimiento },
                                cvv = base.cvv.ifBlank { secT.cvv },
                                pin = base.pin.ifBlank { secT.pin },
                                marca = base.marca.ifBlank { secT.marca },
                                tipo = if (base.tipo.isNotBlank() && base.tipo != "Crédito") base.tipo else secT.tipo
                            )
                        }
                    }
                }
                TipoEntrada.WIFI -> {
                    val secW = sec.wifiEfectivo
                    if (secW != null) {
                        val base = resWifi ?: DatosRedWifi()
                        val claveBase = base.contrasena.ifBlank { principal.contrasena }
                        val claveSec = secW.contrasena.ifBlank { sec.contrasena }
                        if (claveBase.isNotBlank() && claveSec.isNotBlank() && claveBase != claveSec) {
                            notasRespaldo.add(
                                "[Contraseña Wi-Fi secundaria respaldada de red '${secW.ssid.ifBlank { principal.titulo }}': $claveSec]"
                            )
                        }
                        resWifi = base.copy(
                            ssid = base.ssid.ifBlank { secW.ssid },
                            contrasena = claveBase.ifBlank { claveSec },
                            tipoSeguridad = base.tipoSeguridad.ifBlank { secW.tipoSeguridad },
                            oculta = base.oculta || secW.oculta
                        )
                    }
                }
                TipoEntrada.CUENTA_BANCARIA -> {
                    val secC = sec.cuentaBancariaEfectiva
                    if (secC != null) {
                        val base = resCuenta ?: DatosCuentaBancaria()
                        val ibanBase = base.cbuClabe.ifBlank { base.numeroCuenta }
                        val ibanSec = secC.cbuClabe.ifBlank { secC.numeroCuenta }
                        if (ibanBase.isNotBlank() && ibanSec.isNotBlank() &&
                            ibanBase.filter { it.isLetterOrDigit() } != ibanSec.filter { it.isLetterOrDigit() }
                        ) {
                            notasRespaldo.add(
                                "[Cuenta bancaria secundaria respaldada al unificar: " +
                                "Banco: ${secC.banco.ifBlank { "N/A" }} | " +
                                "Titular: ${secC.titular.ifBlank { "N/A" }} | " +
                                "Número: ${secC.numeroCuenta.ifBlank { "N/A" }} | " +
                                "IBAN/CBU: ${secC.cbuClabe.ifBlank { "N/A" }} | " +
                                "SWIFT: ${secC.swiftBic.ifBlank { "N/A" }}]"
                            )
                        } else {
                            resCuenta = base.copy(
                                banco = base.banco.ifBlank { secC.banco },
                                titular = base.titular.ifBlank { secC.titular },
                                tipoCuenta = if (base.tipoCuenta.isNotBlank() && base.tipoCuenta != "Corriente") base.tipoCuenta else secC.tipoCuenta,
                                numeroCuenta = base.numeroCuenta.ifBlank { secC.numeroCuenta },
                                swiftBic = base.swiftBic.ifBlank { secC.swiftBic },
                                cbuClabe = base.cbuClabe.ifBlank { secC.cbuClabe }
                            )
                        }
                    }
                }
                TipoEntrada.IDENTIDAD -> {
                    val secI = sec.identidadEfectiva
                    if (secI != null) {
                        val base = resIdentidad ?: DatosDocumentoIdentidad()
                        if (base.numero.isNotBlank() && secI.numero.isNotBlank() &&
                            base.numero.filter { it.isLetterOrDigit() } != secI.numero.filter { it.isLetterOrDigit() }
                        ) {
                            notasRespaldo.add(
                                "[Documento de identidad secundario respaldado al unificar: " +
                                "Tipo: ${secI.tipoDocumento} | " +
                                "Número: ${secI.numero} | " +
                                "Titular: ${secI.nombreCompleto.ifBlank { "N/A" }} | " +
                                "País: ${secI.paisEmisor.ifBlank { "N/A" }} | " +
                                "Exp: ${secI.expedicion.ifBlank { "N/A" }} | " +
                                "Cad: ${secI.caducidad.ifBlank { "N/A" }}]"
                            )
                        } else {
                            resIdentidad = base.copy(
                                tipoDocumento = base.tipoDocumento.ifBlank { secI.tipoDocumento },
                                numero = base.numero.ifBlank { secI.numero },
                                nombreCompleto = base.nombreCompleto.ifBlank { secI.nombreCompleto },
                                expedicion = base.expedicion.ifBlank { secI.expedicion },
                                caducidad = base.caducidad.ifBlank { secI.caducidad },
                                paisEmisor = base.paisEmisor.ifBlank { secI.paisEmisor }
                            )
                        }
                    }
                }
                TipoEntrada.SERVIDOR -> {
                    val secS = sec.servidorEfectivo
                    if (secS != null) {
                        val base = resServidor ?: DatosServidorSsh()
                        if (base.host.isNotBlank() && secS.host.isNotBlank() &&
                            (base.host != secS.host || base.puerto != secS.puerto || base.usuario != secS.usuario)
                        ) {
                            notasRespaldo.add(
                                "[Servidor SSH secundario respaldado al unificar: " +
                                "Host: ${secS.host}:${secS.puerto} | " +
                                "Usuario: ${secS.usuario.ifBlank { "N/A" }} | " +
                                "Clave privada: ${if (secS.clavePrivada.isNotBlank()) "(Clave SSH resguardada)" else "Ninguna"}]"
                            )
                        } else {
                            resServidor = base.copy(
                                host = base.host.ifBlank { secS.host },
                                puerto = if (base.puerto.isNotBlank() && base.puerto != "22") base.puerto else secS.puerto,
                                usuario = base.usuario.ifBlank { secS.usuario },
                                clavePrivada = base.clavePrivada.ifBlank { secS.clavePrivada },
                                clavePublica = base.clavePublica.ifBlank { secS.clavePublica }
                            )
                        }
                    }
                }
                TipoEntrada.WALLET -> {
                    val secW = sec.walletEfectiva
                    if (secW != null) {
                        val base = resWallet ?: DatosCriptoWallet()
                        if (base.direccionPublica.isNotBlank() && secW.direccionPublica.isNotBlank() &&
                            base.direccionPublica.trim().lowercase() != secW.direccionPublica.trim().lowercase()
                        ) {
                            notasRespaldo.add(
                                "[Cripto Wallet secundaria respaldada al unificar: " +
                                "Red: ${secW.red} | " +
                                "Dirección: ${secW.direccionPublica} | " +
                                "Semilla/Clave: ${if (secW.fraseSemilla.isNotBlank() || secW.clavePrivada.isNotBlank()) "(Credenciales cripto resguardadas)" else "Ninguna"}]"
                            )
                        } else {
                            resWallet = base.copy(
                                red = if (base.red.isNotBlank() && base.red != "Bitcoin") base.red else secW.red,
                                direccionPublica = base.direccionPublica.ifBlank { secW.direccionPublica },
                                fraseSemilla = base.fraseSemilla.ifBlank { secW.fraseSemilla },
                                clavePrivada = base.clavePrivada.ifBlank { secW.clavePrivada }
                            )
                        }
                    }
                }
                TipoEntrada.CONTACTO -> {
                    val secCo = sec.contactoEfectivo
                    if (secCo != null) {
                        val base = resContacto ?: DatosContacto()
                        resContacto = base.copy(
                            telefonoSecundario = base.telefonoSecundario.ifBlank { secCo.telefonoSecundario },
                            correo = base.correo.ifBlank { secCo.correo },
                            direccion = base.direccion.ifBlank { secCo.direccion },
                            codigoPostal = base.codigoPostal.ifBlank { secCo.codigoPostal },
                            ciudad = base.ciudad.ifBlank { secCo.ciudad },
                            provincia = base.provincia.ifBlank { secCo.provincia },
                            pais = base.pais.ifBlank { secCo.pais }
                        )
                    }
                }
                else -> {
                    // Si la entrada principal no era especializada pero una secundaria sí, resguardamos sus datos
                    if (sec.tarjetaEfectiva != null) {
                        val t = sec.tarjetaEfectiva!!
                        notasRespaldo.add("[Tarjeta resguardada: Titular: ${t.titular} | Núm: ${t.numero} | Exp: ${t.vencimiento} | CVV: ${t.cvv}]")
                    }
                    if (sec.wifiEfectivo != null) {
                        val w = sec.wifiEfectivo!!
                        notasRespaldo.add("[Wi-Fi resguardado: SSID: ${w.ssid} | Clave: ${w.contrasena}]")
                    }
                    if (sec.cuentaBancariaEfectiva != null) {
                        val c = sec.cuentaBancariaEfectiva!!
                        notasRespaldo.add("[Cuenta bancaria resguardada: Banco: ${c.banco} | Núm: ${c.numeroCuenta} | IBAN: ${c.cbuClabe}]")
                    }
                    if (sec.identidadEfectiva != null) {
                        val i = sec.identidadEfectiva!!
                        notasRespaldo.add("[Documento resguardado: Tipo: ${i.tipoDocumento} | Núm: ${i.numero} | Titular: ${i.nombreCompleto}]")
                    }
                    if (sec.servidorEfectivo != null) {
                        val s = sec.servidorEfectivo!!
                        notasRespaldo.add("[Servidor resguardado: Host: ${s.host}:${s.puerto} | Usuario: ${s.usuario}]")
                    }
                    if (sec.walletEfectiva != null) {
                        val wl = sec.walletEfectiva!!
                        notasRespaldo.add("[Wallet resguardada: Red: ${wl.red} | Dirección: ${wl.direccionPublica}]")
                    }
                    if (sec.contactoEfectivo != null) {
                        val co = sec.contactoEfectivo!!
                        notasRespaldo.add("[Contacto resguardado: Tel: ${co.telefonoSecundario} | Email: ${co.correo} | Dir: ${co.direccionFormateada}]")
                    }
                }
            }
        }

        return ResultadoFusionNativa(
            datosTarjeta = resTarjeta,
            datosWifi = resWifi,
            datosCuentaBancaria = resCuenta,
            datosIdentidad = resIdentidad,
            datosServidor = resServidor,
            datosWallet = resWallet,
            datosContacto = resContacto,
            notasDeRespaldo = notasRespaldo
        )
    }
}
