package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

object GestorCamposBase {
    fun etiquetasBaseParaTipo(tipo: TipoEntrada): Set<String> = when (tipo) {
        TipoEntrada.TARJETA -> setOf("Titular", "Número de tarjeta", "Vencimiento", "CVV", "PIN de tarjeta")
        TipoEntrada.WIFI -> setOf("Nombre de red (SSID)", "Contraseña Wi-Fi", "Tipo de seguridad")
        TipoEntrada.CUENTA_BANCARIA -> setOf("Banco / Entidad", "Titular de la cuenta", "Número de cuenta / IBAN", "SWIFT / CBU / CLABE")
        TipoEntrada.IDENTIDAD -> setOf("Tipo de documento", "Número de documento", "Nombre completo", "Expedición", "Caducidad", "País emisor")
        TipoEntrada.SERVIDOR -> setOf("Host o IP", "Puerto", "Usuario SSH", "Clave privada / Password")
        TipoEntrada.WALLET -> setOf("Red / Blockchain", "Dirección pública", "Frase semilla (Seed phrase)", "Clave privada")
        else -> emptySet()
    }

    fun valorDeCampo(campos: List<CampoPersonalizado>, clave: String): String {
        return campos.firstOrNull { it.etiqueta.equals(clave, ignoreCase = true) }?.valor
            ?: campos.firstOrNull { it.etiqueta.startsWith(clave, ignoreCase = true) }?.valor
            ?: campos.firstOrNull {
                val c = clave.lowercase()
                val e = it.etiqueta.lowercase()
                (c.contains("seguridad") && e.contains("seguridad")) ||
                (c.contains("ssid") && e.contains("ssid")) ||
                (c.contains("red") && e.contains("red"))
            }?.valor
            ?: ""
    }

    fun actualizarValor(
        campos: List<CampoPersonalizado>,
        etiqueta: String,
        nuevoValor: String,
        tipo: TipoCampo,
        sensible: Boolean = false,
        formato: String? = null
    ): List<CampoPersonalizado> {
        val lista = campos.toMutableList()
        val index = lista.indexOfFirst {
            it.etiqueta.equals(etiqueta, ignoreCase = true) ||
            it.etiqueta.startsWith(etiqueta, ignoreCase = true) ||
            (etiqueta.contains("seguridad", ignoreCase = true) && it.etiqueta.contains("seguridad", ignoreCase = true)) ||
            (etiqueta.contains("ssid", ignoreCase = true) && it.etiqueta.contains("ssid", ignoreCase = true))
        }
        if (index >= 0) {
            lista[index] = lista[index].copy(etiqueta = etiqueta, valor = nuevoValor, tipo = tipo, esSensible = sensible, formato = formato)
        } else {
            lista.add(
                CampoPersonalizado(
                    etiqueta = etiqueta,
                    valor = nuevoValor,
                    tipo = tipo,
                    esSensible = sensible,
                    formato = formato
                )
            )
        }
        return lista
    }
}

@Composable
fun FormularioEdicionTarjeta(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarNumero by remember { mutableStateOf(false) }
    var mostrarCvv by remember { mutableStateOf(false) }
    var mostrarPin by remember { mutableStateOf(false) }

    val titular = GestorCamposBase.valorDeCampo(campos, "Titular")
    val numero = GestorCamposBase.valorDeCampo(campos, "Número de tarjeta")
    val vencimiento = GestorCamposBase.valorDeCampo(campos, "Vencimiento")
    val cvv = GestorCamposBase.valorDeCampo(campos, "CVV")
    val pin = GestorCamposBase.valorDeCampo(campos, "PIN de tarjeta")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = titular,
            etiqueta = "Titular (nombre como figura en el plástico)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Titular", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = numero,
            etiqueta = "Número de tarjeta",
            alCambiar = { raw ->
                val sanitizado = raw.filter { it.isDigit() || it == ' ' }
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de tarjeta", sanitizado, TipoCampo.TEXTO, sensible = true, formato = "TARJETA_CREDITO"))
            },
            esContrasena = true,
            mostrarContrasena = mostrarNumero,
            alAlternarMostrarContrasena = { mostrarNumero = !mostrarNumero },
            monoespaciada = true,
            tecladoNumerico = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = vencimiento,
                etiqueta = "Vencimiento (MM/AA)",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() || it == '/' }.take(5)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Vencimiento", sanitizado, TipoCampo.FECHA, formato = "MM/AA"))
                },
                modifier = Modifier.weight(1f),
                tecladoNumerico = true
            )

            CampoBoveda(
                valor = cvv,
                etiqueta = "CVV / CVC",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(4)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "CVV", sanitizado, TipoCampo.NUMERO, sensible = true))
                },
                esContrasena = true,
                mostrarContrasena = mostrarCvv,
                alAlternarMostrarContrasena = { mostrarCvv = !mostrarCvv },
                monoespaciada = true,
                tecladoNumerico = true,
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = pin,
            etiqueta = "PIN del cajero (opcional)",
            alCambiar = { raw ->
                val sanitizado = raw.filter { it.isDigit() }.take(12)
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "PIN de tarjeta", sanitizado, TipoCampo.PIN, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarPin,
            alAlternarMostrarContrasena = { mostrarPin = !mostrarPin },
            monoespaciada = true,
            tecladoNumerico = true
        )
    }
}

@Composable
fun FormularioEdicionWifi(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarClave by remember { mutableStateOf(false) }

    val ssid = GestorCamposBase.valorDeCampo(campos, "Nombre de red (SSID)")
    val clave = GestorCamposBase.valorDeCampo(campos, "Contraseña Wi-Fi")
    val seguridad = GestorCamposBase.valorDeCampo(campos, "Tipo de seguridad")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = ssid,
            etiqueta = "Nombre de red (SSID)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Nombre de red (SSID)", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = clave,
            etiqueta = "Contraseña Wi-Fi",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Contraseña Wi-Fi", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true
        )

        Column {
            CampoBoveda(
                valor = seguridad,
                etiqueta = "Tipo de seguridad (ej. WPA3, WPA2-Personal)",
                alCambiar = {
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de seguridad", it, TipoCampo.TEXTO))
                }
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("WPA2 / WPA3", "WPA", "WEP", "Sin contraseña (Abierta)").forEach { opcion ->
                    val seleccionado = seguridad.equals(opcion, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (seleccionado) ColorAcento else Color(0xFF161518))
                            .clickable {
                                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de seguridad", opcion, TipoCampo.TEXTO))
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = opcion,
                            color = if (seleccionado) Obsidiana else TextoSecundario,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormularioEdicionCuentaBancaria(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarIban by remember { mutableStateOf(false) }

    val banco = GestorCamposBase.valorDeCampo(campos, "Banco / Entidad")
    val titular = GestorCamposBase.valorDeCampo(campos, "Titular de la cuenta")
    val iban = GestorCamposBase.valorDeCampo(campos, "Número de cuenta / IBAN")
    val swift = GestorCamposBase.valorDeCampo(campos, "SWIFT / CBU / CLABE")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = banco,
            etiqueta = "Banco / Entidad financiera",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Banco / Entidad", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = titular,
            etiqueta = "Titular de la cuenta",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Titular de la cuenta", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = iban,
            etiqueta = "Número de cuenta / IBAN",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de cuenta / IBAN", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarIban,
            alAlternarMostrarContrasena = { mostrarIban = !mostrarIban },
            monoespaciada = true
        )

        CampoBoveda(
            valor = swift,
            etiqueta = "SWIFT / BIC / CLABE / CBU",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "SWIFT / CBU / CLABE", it, TipoCampo.TEXTO))
            },
            monoespaciada = true
        )
    }
}

@Composable
fun FormularioEdicionIdentidad(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val contexto = LocalContext.current
    var mostrarDoc by remember { mutableStateOf(false) }

    val tipoDoc = GestorCamposBase.valorDeCampo(campos, "Tipo de documento")
    val numero = GestorCamposBase.valorDeCampo(campos, "Número de documento")
    val nombre = GestorCamposBase.valorDeCampo(campos, "Nombre completo")
    val expedicion = GestorCamposBase.valorDeCampo(campos, "Expedición")
    val caducidad = GestorCamposBase.valorDeCampo(campos, "Caducidad")
    val pais = GestorCamposBase.valorDeCampo(campos, "País emisor")

    fun abrirSelectorFecha(clave: String, valorActual: String) {
        val parseado = FormateadorCampos.parsearFecha(valorActual, ajustes.formatoFecha)
        val cal = Calendar.getInstance()
        val y = parseado?.first ?: cal.get(Calendar.YEAR)
        val m = parseado?.second ?: cal.get(Calendar.MONTH)
        val d = parseado?.third ?: cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(contexto, { _, anio, mes, dia ->
            val fechaFormateada = FormateadorCampos.formatearFecha(anio, mes, dia, ajustes.formatoFecha)
            alCambiarCampos(GestorCamposBase.actualizarValor(campos, clave, fechaFormateada, TipoCampo.FECHA))
        }, y, m, d).show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = tipoDoc,
            etiqueta = "Tipo (DNI, Pasaporte, Licencia...)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Tipo de documento", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = numero,
            etiqueta = "Número de documento",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Número de documento", it, TipoCampo.TEXTO, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarDoc,
            alAlternarMostrarContrasena = { mostrarDoc = !mostrarDoc },
            monoespaciada = true
        )

        CampoBoveda(
            valor = nombre,
            etiqueta = "Nombre completo del titular",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Nombre completo", it, TipoCampo.TEXTO))
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = expedicion,
                etiqueta = "Expedición (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha("Expedición", expedicion) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha("Expedición", expedicion) },
                modifier = Modifier.weight(1f)
            )

            CampoBoveda(
                valor = caducidad,
                etiqueta = "Caducidad (${ajustes.formatoFecha})",
                alCambiar = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { abrirSelectorFecha("Caducidad", caducidad) }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Elegir fecha",
                            tint = ColorIconosInternos
                        )
                    }
                },
                alPulsar = { abrirSelectorFecha("Caducidad", caducidad) },
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = pais,
            etiqueta = "País emisor",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "País emisor", it, TipoCampo.TEXTO))
            }
        )
    }
}

@Composable
fun FormularioEdicionServidor(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarClave by remember { mutableStateOf(false) }

    val host = GestorCamposBase.valorDeCampo(campos, "Host o IP")
    val puerto = GestorCamposBase.valorDeCampo(campos, "Puerto")
    val usuario = GestorCamposBase.valorDeCampo(campos, "Usuario SSH")
    val clave = GestorCamposBase.valorDeCampo(campos, "Clave privada / Password")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = host,
                etiqueta = "Host o IP",
                alCambiar = {
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Host o IP", it, TipoCampo.TEXTO))
                },
                modifier = Modifier.weight(2.2f)
            )

            CampoBoveda(
                valor = puerto,
                etiqueta = "Puerto",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(5)
                    alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Puerto", sanitizado, TipoCampo.NUMERO))
                },
                tecladoNumerico = true,
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = usuario,
            etiqueta = "Usuario SSH (ej. root, ubuntu)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Usuario SSH", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = clave,
            etiqueta = "Clave privada SSH o Contraseña",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Clave privada / Password", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true,
            varias = true
        )
    }
}

@Composable
fun FormularioEdicionWallet(
    campos: List<CampoPersonalizado>,
    alCambiarCampos: (List<CampoPersonalizado>) -> Unit
) {
    var mostrarSemilla by remember { mutableStateOf(false) }
    var mostrarClavePrivada by remember { mutableStateOf(false) }

    val red = GestorCamposBase.valorDeCampo(campos, "Red / Blockchain")
    val direccion = GestorCamposBase.valorDeCampo(campos, "Dirección pública")
    val semilla = GestorCamposBase.valorDeCampo(campos, "Frase semilla (Seed phrase)")
    val clavePrivada = GestorCamposBase.valorDeCampo(campos, "Clave privada")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = red,
            etiqueta = "Red / Blockchain (ej. Ethereum, Bitcoin, Solana)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Red / Blockchain", it, TipoCampo.TEXTO))
            }
        )

        CampoBoveda(
            valor = direccion,
            etiqueta = "Dirección pública (clave pública)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Dirección pública", it, TipoCampo.TEXTO))
            },
            monoespaciada = true
        )

        CampoBoveda(
            valor = semilla,
            etiqueta = "Frase semilla (12 o 24 palabras mnemónicas)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Frase semilla (Seed phrase)", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarSemilla,
            alAlternarMostrarContrasena = { mostrarSemilla = !mostrarSemilla },
            monoespaciada = true,
            varias = true
        )

        CampoBoveda(
            valor = clavePrivada,
            etiqueta = "Clave privada (Private key)",
            alCambiar = {
                alCambiarCampos(GestorCamposBase.actualizarValor(campos, "Clave privada", it, TipoCampo.NOTAS, sensible = true))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClavePrivada,
            alAlternarMostrarContrasena = { mostrarClavePrivada = !mostrarClavePrivada },
            monoespaciada = true,
            varias = true
        )
    }
}
