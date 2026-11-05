/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.crypto.OtpAuth
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.TotpSecundario
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.CuentaGoogleAuth
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface VaultEntradasDelegate : VaultDuplicadosPapeleraDelegate {
    fun obtenerApp(): android.app.Application
    override val repositorio: VaultRepository
    override val avisoInterno: MutableStateFlow<String?>
    val errorInterno: MutableStateFlow<String?>
    val busquedaInterna: MutableStateFlow<String>
    val filtroTipoInterno: MutableStateFlow<TipoEntrada?>
    val soloFavoritosInterno: MutableStateFlow<Boolean>
    val solo2faInterno: MutableStateFlow<Boolean>
    val filtroEtiquetaInterno: MutableStateFlow<String?>
    val filtroCategoriaInterno: MutableStateFlow<String?>
    val filtroColeccionInterno: MutableStateFlow<String?> get() = filtroCategoriaInterno
    val filtroIdentidadInterno: MutableStateFlow<String?>
    val criterioOrdenacionInterno: MutableStateFlow<CriterioOrdenacion>
    override fun ejecutar(bloque: suspend () -> Unit)
    fun ir(pantalla: Pantalla)
    fun irRaiz(pantalla: Pantalla)

    fun avisar(texto: String) {
        avisoInterno.value = texto
    }

    // ---------------------------------------------------------------- entradas

    fun entradasVisibles(
        entradas: List<Entrada>,
        identidades: List<com.jlnavas3.bovedalocal.data.Identidad> = emptyList()
    ): List<Entrada> =
        FiltradorEntradas.filtrarYOrdenar(
            entradas = entradas,
            busqueda = busquedaInterna.value,
            filtroTipo = filtroTipoInterno.value,
            soloFavoritos = soloFavoritosInterno.value,
            solo2fa = solo2faInterno.value,
            filtroEtiqueta = filtroEtiquetaInterno.value,
            filtroCategoria = filtroCategoriaInterno.value,
            criterioOrdenacion = criterioOrdenacionInterno.value,
            filtroIdentidad = filtroIdentidadInterno.value,
            identidades = identidades
        )

    fun cambiarCriterioOrdenacion(criterio: CriterioOrdenacion) {
        criterioOrdenacionInterno.value = criterio
        repositorio.ajustes.actualizar { it.copy(criterioOrdenacion = criterio.name) }
    }

    fun buscar(texto: String) {
        busquedaInterna.value = texto
    }

    fun filtrarPorTipo(tipo: TipoEntrada?) {
        filtroTipoInterno.value = tipo
        if (tipo != null) {
            solo2faInterno.value = false
        }
    }

    fun filtrarPor2fa(activo: Boolean) {
        solo2faInterno.value = activo
        if (activo) {
            filtroTipoInterno.value = null
        }
    }

    fun alternarSolo2fa() {
        val nuevo = !solo2faInterno.value
        solo2faInterno.value = nuevo
        if (nuevo) {
            filtroTipoInterno.value = null
        }
    }

    fun alternarSoloFavoritos() {
        soloFavoritosInterno.value = !soloFavoritosInterno.value
    }

    fun filtrarPorEtiqueta(etiqueta: String?) {
        filtroEtiquetaInterno.value = etiqueta
    }

    /** Todas las etiquetas ya usadas en la bóveda, para los chips de filtro y las sugerencias al editar. */
    fun etiquetasUsadas(): List<String> = repositorio.etiquetasUsadas()

    fun entrada(id: String): Entrada? = repositorio.entrada(id)

    fun registrarUsoEntrada(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) {
                repositorio.registrarUsoEntrada(id)
            }
        }
    }

    fun guardar(entrada: Entrada) {
        ejecutar {
            val existente = withContext(Dispatchers.IO) { repositorio.entrada(entrada.id) }
            withContext(Dispatchers.IO) { repositorio.guardarEntrada(entrada) }
            val tipoDesc = entrada.tipo.etiqueta.lowercase()
            val nombre = entrada.titulo.ifBlank { "(sin título)" }
            if (existente == null) {
                Diagnostico.apuntar("bóveda", "Nueva entrada creada: \"$nombre\" ($tipoDesc)")
            } else {
                if (existente.tipo != entrada.tipo) {
                    Diagnostico.apuntar("bóveda", "Entrada editada: \"$nombre\" (${existente.tipo.etiqueta.lowercase()} → $tipoDesc)")
                } else {
                    Diagnostico.apuntar("bóveda", "Entrada editada: \"$nombre\" ($tipoDesc)")
                }
            }
            avisoInterno.value = "Guardado en la bóveda"
            refrescarTransferenciaCxfSiActiva()
        }
    }

    fun intercambiarPasskeyPrincipal(entradaId: String, passkeySecundaria: DatosPasskey) {
        ejecutar {
            val actual = withContext(Dispatchers.IO) { repositorio.entrada(entradaId) } ?: return@ejecutar
            val principalAnterior = actual.passkey?.let { p ->
                if (p.creadaEn > 0L) p else p.copy(creadaEn = if (actual.creadaEn > 0L) actual.creadaEn else System.currentTimeMillis())
            }
            val nuevasSecundarias = actual.passkeysSecundarias.toMutableList()
            nuevasSecundarias.removeAll { it.credId == passkeySecundaria.credId }
            if (principalAnterior != null && principalAnterior.credId != passkeySecundaria.credId) {
                nuevasSecundarias.add(0, principalAnterior)
            }
            val passkeyNuevaPrincipal = if (passkeySecundaria.creadaEn > 0L) {
                passkeySecundaria
            } else {
                passkeySecundaria.copy(creadaEn = if (actual.creadaEn > 0L) actual.creadaEn else System.currentTimeMillis())
            }
            val actualizada = actual.copy(
                passkey = passkeyNuevaPrincipal,
                passkeysSecundarias = nuevasSecundarias
            )
            guardar(actualizada)
            avisar("Llave de acceso principal actualizada")
        }
    }

    fun eliminarPasskeySecundaria(entradaId: String, passkeyAEliminar: DatosPasskey) {
        ejecutar {
            val actual = withContext(Dispatchers.IO) { repositorio.entrada(entradaId) } ?: return@ejecutar
            val nuevasSecundarias = actual.passkeysSecundarias.filterNot { it.credId == passkeyAEliminar.credId }
            val actualizada = actual.copy(passkeysSecundarias = nuevasSecundarias)
            guardar(actualizada)
            avisar("Llave de acceso secundaria eliminada")
        }
    }

    fun intercambiarTotpPrincipal(entradaId: String, totpSecundario: TotpSecundario) {
        ejecutar {
            val actual = withContext(Dispatchers.IO) { repositorio.entrada(entradaId) } ?: return@ejecutar
            val nuevasSecundarias = actual.totpsSecundarios.toMutableList()
            nuevasSecundarias.removeAll { it.id == totpSecundario.id }
            if (actual.tieneTotp) {
                nuevasSecundarias.add(
                    0,
                    TotpSecundario(
                        id = java.util.UUID.randomUUID().toString(),
                        secreto = actual.secretoTotp ?: "",
                        emisor = actual.totpEmisor.ifBlank { actual.titulo },
                        periodo = actual.totpPeriodo,
                        digitos = actual.totpDigitos,
                        algoritmo = actual.totpAlgoritmo
                    )
                )
            }
            val actualizada = actual.copy(
                secretoTotp = totpSecundario.secreto,
                totpEmisor = totpSecundario.emisor,
                totpPeriodo = totpSecundario.periodo,
                totpDigitos = totpSecundario.digitos,
                totpAlgoritmo = totpSecundario.algoritmo,
                totpsSecundarios = nuevasSecundarias
            )
            guardar(actualizada)
            avisar("Código 2FA principal actualizado")
        }
    }

    fun eliminarTotpSecundario(entradaId: String, idTotp: String) {
        ejecutar {
            val actual = withContext(Dispatchers.IO) { repositorio.entrada(entradaId) } ?: return@ejecutar
            val nuevasSecundarias = actual.totpsSecundarios.filterNot { it.id == idTotp }
            val actualizada = actual.copy(totpsSecundarios = nuevasSecundarias)
            guardar(actualizada)
            avisar("Código 2FA secundario eliminado")
        }
    }

    fun eliminar(id: String) {
        val ent = repositorio.entradas().firstOrNull { it.id == id }
        val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
        irRaiz(Pantalla.Lista)
        avisoInterno.value = "Movida a la papelera"
        avisar("Movida a la papelera")
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntrada(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) enviada a la papelera")
            refrescarTransferenciaCxfSiActiva()
        }
    }

    fun eliminarVarias(ids: Set<String>) {
        if (ids.isEmpty()) return
        val entradasOriginales: List<Entrada> = repositorio.entradas().filter { it.id in ids }
        val textoMsg = if (ids.size == 1) "Entrada movida a la papelera" else "${ids.size} entradas movidas a la papelera"
        if (entradasOriginales.isNotEmpty()) {
            avisarConDeshacer(
                mensaje = textoMsg,
                titulo = "Papelera",
                alDeshacer = {
                    ejecutar {
                        withContext(Dispatchers.IO) {
                            entradasOriginales.forEach { repositorio.guardarEntrada(it) }
                        }
                        avisoInterno.value = "Eliminación deshecha"
                    }
                }
            )
        } else {
            avisoInterno.value = textoMsg
        }
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(ids) }
            Diagnostico.apuntar("papelera", "${ids.size} entradas enviadas a la papelera")
        }
    }

    fun renombrarVarias(ids: Set<String>, nuevoTitulo: String) {
        if (ids.isEmpty()) return
        val tituloLimpio = nuevoTitulo.trim()
        if (tituloLimpio.isBlank()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.renombrarEntradas(ids, tituloLimpio) }
            Diagnostico.apuntar("lista", "${ids.size} entradas renombradas a «$tituloLimpio»")
            avisoInterno.value = if (ids.size == 1) "Entrada renombrada a «$tituloLimpio»" else "${ids.size} entradas renombradas a «$tituloLimpio»"
        }
    }

    fun alternarFavoritosVarias(ids: Set<String>, forzarMarcar: Boolean? = null) {
        if (ids.isEmpty()) return
        ejecutar {
            val entradasObjetivo = withContext(Dispatchers.IO) {
                repositorio.entradas().filter { it.id in ids }
            }
            val marcar = forzarMarcar ?: entradasObjetivo.any { !it.favorito }
            withContext(Dispatchers.IO) {
                repositorio.alternarFavoritos(ids, marcar)
            }
            val texto = if (marcar) {
                if (ids.size == 1) "Añadida a favoritos" else "${ids.size} añadidas a favoritos"
            } else {
                if (ids.size == 1) "Quitada de favoritos" else "${ids.size} quitadas de favoritos"
            }
            Diagnostico.apuntar("bóveda", "$texto (${ids.size} entradas)")
            avisoInterno.value = texto
        }
    }

    fun alternarAnclados(ids: Set<String>, forzarMarcar: Boolean? = null) {
        if (ids.isEmpty()) return
        ejecutar {
            val entradasObjetivo = withContext(Dispatchers.IO) {
                repositorio.entradas().filter { it.id in ids }
            }
            val marcar = forzarMarcar ?: entradasObjetivo.any { !it.anclado }
            withContext(Dispatchers.IO) {
                repositorio.alternarAnclados(ids, marcar)
            }
            val texto = if (marcar) {
                if (ids.size == 1) "Entrada anclada" else "${ids.size} entradas ancladas"
            } else {
                if (ids.size == 1) "Entrada desanclada" else "${ids.size} entradas desancladas"
            }
            Diagnostico.apuntar("bóveda", "$texto (${ids.size} entradas)")
            avisoInterno.value = texto
        }
    }

    // ------------------------------------------------------------- favoritos y acciones

    fun alternarFavorito(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) {
                repositorio.alternarFavorito(id)
                repositorio.entrada(id)
            }
            val estadoFav = if (ent?.favorito == true) "marcada como favorita" else "desmarcada de favoritos"
            Diagnostico.apuntar("bóveda", "Entrada $estadoFav")
        }
    }

    fun alternarAnclado(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) {
                repositorio.alternarAnclado(id)
                repositorio.entrada(id)
            }
            val estadoAnc = if (ent?.anclado == true) "anclada arriba" else "desanclada"
            Diagnostico.apuntar("bóveda", "Entrada $estadoAnc")
        }
    }

    fun alternarIgnorarSalud(id: String, forzar: Boolean? = null) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) {
                repositorio.alternarIgnorarSalud(id, forzar)
                repositorio.entrada(id)
            }
            val estado = if (ent?.ignoradaEnSalud == true) "ignorada en auditoría de salud" else "restaurada a la auditoría de salud"
            Diagnostico.apuntar("salud", "Entrada $estado")
            avisoInterno.value = if (ent?.ignoradaEnSalud == true) "Entrada ignorada en salud" else "Entrada incluida en salud"
        }
    }

    fun alternarIgnorarSaludVarias(ids: Set<String>, forzarMarcar: Boolean? = null) {
        if (ids.isEmpty()) return
        ejecutar {
            val entradasObjetivo = withContext(Dispatchers.IO) {
                repositorio.entradas().filter { it.id in ids }
            }
            val marcar = forzarMarcar ?: entradasObjetivo.any { !it.ignoradaEnSalud }
            withContext(Dispatchers.IO) {
                repositorio.alternarIgnorarSaludVarias(ids, marcar)
            }
            val texto = if (marcar) {
                if (ids.size == 1) "Excluida de la auditoría de salud" else "${ids.size} excluidas de la auditoría"
            } else {
                if (ids.size == 1) "Incluida en la auditoría de salud" else "${ids.size} incluidas en la auditoría"
            }
            Diagnostico.apuntar("salud", "$texto (${ids.size} entradas)")
            avisoInterno.value = texto
        }
    }

    fun nuevoId(): String = repositorio.nuevoId()

    // -------------------------------------------------------------- 2FA (TOTP)

    fun entradasConTotp(entradas: List<Entrada>): List<Entrada> =
        entradas.filter { !it.secretoTotp.isNullOrBlank() }
            .sortedBy { it.titulo.lowercase() }

    fun altaTotp(texto: String, entradaDestino: String? = null): Boolean {
        val ajustes = repositorio.ajustes.actual
        val semilla = OtpAuth.leer(
            texto = texto,
            digitosManual = ajustes.totpManualDigitos,
            periodoManual = ajustes.totpManualPeriodo,
            algoritmoManual = ajustes.totpManualAlgoritmo
        ) ?: return false
        val destino = entradaDestino?.let { repositorio.entrada(it) }
        if (destino != null) {
            guardar(
                destino.copy(
                    secretoTotp = semilla.secreto,
                    totpEmisor = semilla.emisor.ifBlank { destino.totpEmisor },
                    totpDigitos = semilla.digitos,
                    totpPeriodo = semilla.periodo,
                    totpAlgoritmo = semilla.algoritmo
                )
            )
            Diagnostico.apuntar("2fa", "Doble factor (TOTP) vinculado a entrada existente")
            ir(Pantalla.Detalle(destino.id))
            return true
        }
        val entrada = Entrada(
            id = repositorio.nuevoId(),
            tipo = TipoEntrada.LOGIN,
            titulo = semilla.titulo,
            usuario = semilla.cuenta,
            secretoTotp = semilla.secreto,
            totpEmisor = semilla.emisor,
            totpDigitos = semilla.digitos,
            totpPeriodo = semilla.periodo,
            totpAlgoritmo = semilla.algoritmo
        )
        guardar(entrada)
        Diagnostico.apuntar("2fa", "Nueva entrada creada con doble factor (TOTP)")
        ir(Pantalla.Autenticador)
        return true
    }

    fun importarCuentasGoogleAuth(cuentas: List<CuentaGoogleAuth>): Int {
        var procesadas = 0
        val entradasExistentes = repositorio.entradas()

        cuentas.filter { it.seleccionada }.forEach { cuenta ->
            val secretoB32 = cuenta.secretoBase32.uppercase().trim()
            val coincidencia = entradasExistentes.firstOrNull { e ->
                val eSecreto = e.secretoTotp?.uppercase()?.trim()
                (eSecreto != null && eSecreto == secretoB32) ||
                (e.usuario.equals(cuenta.cuenta, ignoreCase = true) && e.totpEmisor.equals(cuenta.emisor, ignoreCase = true) && cuenta.emisor.isNotBlank())
            }

            if (coincidencia != null) {
                val actualizada = coincidencia.copy(
                    secretoTotp = secretoB32,
                    totpEmisor = cuenta.emisor.ifBlank { coincidencia.totpEmisor },
                    totpDigitos = cuenta.digitos,
                    totpPeriodo = cuenta.periodo,
                    totpAlgoritmo = cuenta.algoritmo
                )
                repositorio.guardarEntrada(actualizada)
            } else {
                val nueva = Entrada(
                    id = repositorio.nuevoId(),
                    tipo = TipoEntrada.LOGIN,
                    titulo = cuenta.titulo,
                    usuario = cuenta.cuenta,
                    secretoTotp = secretoB32,
                    totpEmisor = cuenta.emisor,
                    totpDigitos = cuenta.digitos,
                    totpPeriodo = cuenta.periodo,
                    totpAlgoritmo = cuenta.algoritmo
                )
                repositorio.guardarEntrada(nueva)
            }
            procesadas++
        }

        if (procesadas > 0) {
            Diagnostico.apuntar("2fa", "Procesadas $procesadas cuentas de Google Authenticator")
            avisar("Se importaron $procesadas cuentas de Google Authenticator")
            ir(Pantalla.Autenticador)
        }
        return procesadas
    }

    fun resolverTituloDuplicado(entrada: Entrada): Entrada {
        val titulos = repositorio.entradas().map { it.titulo }
        return com.jlnavas3.bovedalocal.util.ResolverTituloDuplicado.resolverEntrada(entrada, titulos)
    }

    fun importarEntradasContactos(entradas: List<Entrada>): Int {
        if (entradas.isEmpty()) return 0
        ejecutar {
            var importadas = 0
            val total = entradas.size
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "importar_contactos",
                titulo = "Contactos del teléfono",
                faseInicial = "Importando $total contactos...",
                totalEstimado = total
            )
            val delayMs = (2000L / total.coerceAtLeast(1)).coerceIn(1L, 25L)
            withContext(Dispatchers.IO) {
                val titulosExistentes = repositorio.entradas().map { it.titulo }.toMutableList()
                entradas.forEachIndexed { index, e ->
                    val procesadas = index + 1
                    val ajustada = com.jlnavas3.bovedalocal.util.ResolverTituloDuplicado.resolverEntrada(e, titulosExistentes)
                    repositorio.guardarEntrada(ajustada)
                    titulosExistentes.add(ajustada.titulo)
                    importadas++
                    val item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                        id = ajustada.id,
                        titulo = ajustada.titulo.ifBlank { "Contacto" },
                        subtitulo = ajustada.camposPersonalizados.firstOrNull()?.valor ?: "",
                        progreso = 1.0f,
                        completado = true
                    )
                    com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                        item = item,
                        progresoGlobal = 0.1f + 0.85f * (procesadas.toFloat() / total),
                        procesadas = procesadas
                    )
                    if (delayMs > 0) {
                        kotlinx.coroutines.delay(delayMs)
                    }
                }
            }
            Diagnostico.apuntar("contactos", "$importadas contactos importados a la bóveda")
            val mensaje = if (importadas == 1) "1 contacto importado a la bóveda" else "$importadas contactos importados a la bóveda"
            avisar(mensaje)
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                mensajeExito = mensaje,
                totalCuentas = importadas,
                textoAccionResultado = "Ver en la lista",
                textoAccionSecundaria = null,
                accionResultado = {
                    (this as? com.jlnavas3.bovedalocal.ui.VaultNavegacionDelegate)?.irRaiz(Pantalla.Lista)
                }
            )
        }
        return entradas.size
    }

    fun refrescarTransferenciaCxfSiActiva() {
        val ajustes = repositorio.ajustes.actual
        if (com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.estaTransferenciaActiva(ajustes)) {
            ejecutar {
                withContext(Dispatchers.IO) {
                    try {
                        val app = obtenerApp()
                        if (com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.esSeleccionPersonalizada(app)) {
                            return@withContext
                        }
                        val entradas = repositorio.entradas().filter { it.eliminadaEn == 0L }
                        if (entradas.isNotEmpty()) {
                            if (ajustes.cxfModoTransferencia == "automatica") {
                                com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.sincronizarExportacionAutomatica(
                                    context = obtenerApp(),
                                    entradas = entradas,
                                    ajustes = ajustes
                                )
                            } else {
                                com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia.registrarExportacion(
                                    context = obtenerApp(),
                                    entradas = entradas,
                                    esSeleccionPersonalizada = false
                                )
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }
}
