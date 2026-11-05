/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.MigradorEntradasNativas
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

/**
 * Contenido con scroll de la pantalla de detalle que ensambla de forma modular
 * todas las tarjetas informativas y de credenciales de una entrada.
 */
@Composable
fun ContenidoEntradaDetalle(
    entrada: Entrada,
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    alMostrarQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    var revelada by remember(entrada.id) { mutableStateOf(false) }
    var ultimaCopia by remember(entrada.id) { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    LaunchedEffect(revelada) {
        if (revelada) {
            val tipoDesc = entrada.tipo.etiqueta.lowercase()
            Diagnostico.apuntar("seguridad", "Contraseña revelada en pantalla ($tipoDesc)")
        }
    }

    LaunchedEffect(ultimaCopia) {
        if (ultimaCopia != null) {
            delay(1500)
            ultimaCopia = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .reboteElastico()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Cabecera Hero de identidad
        CabeceraHeroDetalle(
            entrada = entrada,
            alMostrarQr = alMostrarQr,
            alActualizarTitulo = { nuevoTitulo ->
                haptica.exito()
                vm.guardar(entrada.copy(titulo = nuevoTitulo, tituloPersonalizado = true, modificadaEn = System.currentTimeMillis()))
            },
            seguridadVisualActiva = ajustes.seguridadVisualActiva
        )

        Spacer(Modifier.height(8.dp))

        // Grupo 1: Datos principales según TipoEntrada
        when (entrada.tipo) {
            TipoEntrada.TARJETA -> {
                entrada.tarjetaEfectiva?.let { tarjeta ->
                    TarjetaDetalleTarjetaBancaria(
                        tarjeta = tarjeta,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.WIFI -> {
                entrada.wifiEfectivo?.let { wifi ->
                    TarjetaDetalleWifi(
                        wifi = wifi,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it },
                        alMostrarQr = alMostrarQr
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                entrada.cuentaBancariaEfectiva?.let { cuenta ->
                    TarjetaDetalleCuentaBancaria(
                        cuenta = cuenta,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.IDENTIDAD -> {
                entrada.identidadEfectiva?.let { iden ->
                    TarjetaDetalleIdentidad(
                        identidad = iden,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.SERVIDOR -> {
                entrada.servidorEfectivo?.let { serv ->
                    TarjetaDetalleServidor(
                        servidor = serv,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.WALLET -> {
                entrada.walletEfectiva?.let { wallet ->
                    TarjetaDetalleWallet(
                        wallet = wallet,
                        vm = vm,
                        ajustes = ajustes,
                        haptica = haptica,
                        ultimaCopia = ultimaCopia,
                        alCopiarCampo = { ultimaCopia = it },
                        alMostrarQr = alMostrarQr
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            TipoEntrada.CONTACTO -> {
                TarjetaDetalleContacto(
                    telefonoPrincipal = entrada.usuario,
                    contacto = entrada.contactoEfectivo ?: com.jlnavas3.bovedalocal.data.DatosContacto(),
                    vm = vm,
                    ajustes = ajustes,
                    haptica = haptica,
                    ultimaCopia = ultimaCopia,
                    alCopiarCampo = { ultimaCopia = it }
                )
                Spacer(Modifier.height(16.dp))
            }
            else -> {
                TarjetaCredencialesDetalle(
                    entrada = entrada,
                    revelada = revelada,
                    ultimaCopia = ultimaCopia,
                    alAlternarRevelada = {
                        haptica.toque()
                        revelada = !revelada
                    },
                    alCopiarUsuario = {
                        haptica.toque()
                        vm.copiar("Usuario", entrada.usuario, sensible = false)
                        vm.registrarUsoEntrada(entrada.id)
                        ultimaCopia = "usuario"
                    },
                    alCopiarContrasena = {
                        haptica.exito()
                        vm.copiar("Contraseña", entrada.contrasena, sensible = true)
                        vm.registrarUsoEntrada(entrada.id)
                        ultimaCopia = "contrasena"
                    },
                    ajustes = ajustes
                )
                if (entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        // Grupo 2: 2FA / TOTP modular
        val tieneTotp = !entrada.secretoTotp.isNullOrBlank() ||
            entrada.camposPersonalizados.any { it.etiqueta.contains("TOTP", ignoreCase = true) && it.valor.isNotBlank() }

        TarjetaTotpDetalle(
            entrada = entrada,
            ajustes = ajustes,
            haptica = haptica,
            alCopiarTotp = { codigo ->
                vm.copiar("Código TOTP", codigo, sensible = true)
                vm.registrarUsoEntrada(entrada.id)
            },
            alIntercambiarTotp = { idCampo ->
                haptica.exito()
                val campo = entrada.camposPersonalizados.firstOrNull { it.id == idCampo }
                if (campo != null) {
                    val actual = entrada.secretoTotp ?: ""
                    val nuevo = campo.valor.trim()
                    val nuevosCampos = entrada.camposPersonalizados.map { c ->
                        if (c.id == idCampo) {
                            if (actual.isNotBlank()) {
                                c.copy(valor = actual)
                            } else {
                                null
                            }
                        } else {
                            c
                        }
                    }.filterNotNull()
                    val actualizada = entrada.copy(
                        secretoTotp = nuevo,
                        camposPersonalizados = nuevosCampos,
                        modificadaEn = System.currentTimeMillis()
                    )
                    vm.guardar(actualizada)
                    vm.avisar("TOTP alternativo establecido como principal")
                }
            }
        )
        if (tieneTotp) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 3: Sitios y apps asociados (si existen)
        TarjetaSitiosYAppsDetalle(
            urls = entrada.urls,
            alCopiarUrl = { url ->
                vm.copiar("Enlace", url, sensible = false)
            },
            alAvisar = { vm.avisar(it) },
            alAbrirUrl = { vm.registrarUsoEntrada(entrada.id) }
        )
        if (entrada.urls.any { it.isNotBlank() }) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 4: Campos personalizados (excluyendo TOTP alternativo y campos base nativos)
        val camposVisibles = remember(entrada.camposPersonalizados, entrada.tipo) {
            val sinTotp = entrada.camposPersonalizados.filterNot {
                it.etiqueta.contains("TOTP", ignoreCase = true) && it.valor.isNotBlank()
            }
            MigradorEntradasNativas.depurarCamposPersonalizados(entrada.tipo, sinTotp)
        }
        TarjetaCamposDetalle(
            campos = camposVisibles,
            vm = vm,
            ajustes = ajustes,
            haptica = haptica,
            ultimaCopia = ultimaCopia,
            alCopiarCampo = { idCampo -> ultimaCopia = idCampo }
        )
        if (camposVisibles.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 5: Notas
        TarjetaNotasDetalle(
            notas = entrada.notas,
            titulo = if (entrada.tipo == TipoEntrada.NOTA) "Nota" else "Notas",
            ultimaCopia = ultimaCopia,
            alCopiarNotas = {
                haptica.toque()
                vm.copiar("Notas", entrada.notas, sensible = false)
                ultimaCopia = "notas"
            },
            ajustes = ajustes
        )
        if (entrada.notas.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 6: Etiquetas
        TarjetaEtiquetasDetalle(
            etiquetas = entrada.etiquetas,
            alFiltrarPorEtiqueta = { etiqueta ->
                vm.filtrarPorEtiqueta(etiqueta)
                vm.volverALista()
            }
        )
        if (entrada.etiquetas.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo: Archivos adjuntos cifrados
        TarjetaAdjuntosDetalle(
            adjuntos = entrada.adjuntos
        )
        if (entrada.adjuntos.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 7: Passkey
        entrada.passkey?.let { passkey ->
            TarjetaPasskeyDetalle(
                passkey = passkey,
                usuarioEntrada = entrada.usuario,
                fechaRegistro = if (passkey.creadaEn > 0L) passkey.creadaEn else entrada.creadaEn,
                mostrarDatosTecnicos = ajustes.mostrarDatosTecnicosPasskey,
                vm = vm,
                haptica = haptica
            )
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 8: Credenciales alternativas (contraseñas anteriores, passkeys y 2FA secundarios)
        TarjetaCredencialesSecundariasDetalle(
            entrada = entrada,
            vm = vm,
            haptica = haptica,
            ajustes = ajustes
        )
        val tieneAlternativas = entrada.historialContrasenas.isNotEmpty() ||
                entrada.passkeysSecundarias.isNotEmpty() ||
                entrada.totpsSecundarios.isNotEmpty()
        if (tieneAlternativas) {
            Spacer(Modifier.height(16.dp))
        }

        // Metadatos: Fechas de edición y último uso
        val tieneFechasPie = (entrada.modificadaEn > 0L && entrada.modificadaEn != entrada.creadaEn) ||
                (entrada.ultimoUsoEn > 0L && entrada.tipo != com.jlnavas3.bovedalocal.data.TipoEntrada.NOTA)
        if (tieneFechasPie) {
            Spacer(Modifier.height(4.dp))
            PieMetadatosDetalle(
                creadaEn = entrada.creadaEn,
                modificadaEn = entrada.modificadaEn,
                ultimoUsoEn = entrada.ultimoUsoEn,
                tipo = entrada.tipo
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(130.dp))
    }
}
