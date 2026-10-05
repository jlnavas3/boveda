package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.crypto.OtpAuth
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.CuentaGoogleAuth
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface VaultEntradasDelegate : VaultDuplicadosPapeleraDelegate {
    override val repositorio: VaultRepository
    override val avisoInterno: MutableStateFlow<String?>
    val errorInterno: MutableStateFlow<String?>
    val busquedaInterna: MutableStateFlow<String>
    val filtroTipoInterno: MutableStateFlow<TipoEntrada?>
    val soloFavoritosInterno: MutableStateFlow<Boolean>
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
        }
    }

    fun eliminar(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
            withContext(Dispatchers.IO) { repositorio.eliminarEntrada(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) enviada a la papelera")
            irRaiz(Pantalla.Lista)
            avisoInterno.value = "Movida a la papelera"
        }
    }

    fun eliminarVarias(ids: Set<String>) {
        if (ids.isEmpty()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(ids) }
            Diagnostico.apuntar("papelera", "${ids.size} entradas enviadas a la papelera")
            avisoInterno.value = if (ids.size == 1) "Entrada movida a la papelera" else "${ids.size} entradas movidas a la papelera"
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
}
