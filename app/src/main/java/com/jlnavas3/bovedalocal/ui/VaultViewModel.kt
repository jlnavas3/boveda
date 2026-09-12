package com.jlnavas3.bovedalocal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jlnavas3.bovedalocal.crypto.OtpAuth
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.FrenoIntentos
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface Pantalla {
    object Onboarding : Pantalla
    object Desbloqueo : Pantalla
    object Lista : Pantalla
    data class Detalle(val id: String) : Pantalla
    data class Editar(val id: String?, val contrasenaInicial: String = "") : Pantalla
    object Generador : Pantalla
    object Passkeys : Pantalla
    object Autenticador : Pantalla
    /** Si [entradaDestino] es null, el QR crea una entrada nueva de 2FA. */
    data class Escaner(
        val entradaDestino: String? = null,
        /** true = entrar directo a escribir la clave a mano, sin cámara. */
        val soloManual: Boolean = false
    ) : Pantalla
    object Ajustes : Pantalla
    object Tema : Pantalla
    object Formas : Pantalla
    object Tipografia : Pantalla
    object AcercaDe : Pantalla
    object Registro : Pantalla
    object SaludBoveda : Pantalla
    object Papelera : Pantalla
}

enum class CriterioOrdenacion(val etiqueta: String) {
    NOMBRE_AZ("Nombre (A-Z)"),
    NOMBRE_ZA("Nombre (Z-A)"),
    MODIFICACION_RECIENTE("Modificado recientemente"),
    CREACION_RECIENTE("Añadido recientemente"),
    ANTIGUEDAD("Más antiguos primero")
}

class VaultViewModel(app: Application) : AndroidViewModel(app) {

    val repositorio = VaultRepository.obtener(app)

    val estado: StateFlow<EstadoBoveda> = repositorio.estado
    val ajustes: StateFlow<AjustesApp> = repositorio.ajustes.ajustes

    private val _pantalla = MutableStateFlow<Pantalla>(
        if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding
    )
    val pantalla: StateFlow<Pantalla> = _pantalla

    private val _trabajando = MutableStateFlow(false)
    val trabajando: StateFlow<Boolean> = _trabajando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso

    private val _cuentaAtrasPortapapeles = MutableStateFlow(0)
    val cuentaAtrasPortapapeles: StateFlow<Int> = _cuentaAtrasPortapapeles

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val _filtroTipo = MutableStateFlow<TipoEntrada?>(null)
    val filtroTipo: StateFlow<TipoEntrada?> = _filtroTipo

    private val _soloFavoritos = MutableStateFlow(false)
    val soloFavoritos: StateFlow<Boolean> = _soloFavoritos

    private val _filtroEtiqueta = MutableStateFlow<String?>(null)
    val filtroEtiqueta: StateFlow<String?> = _filtroEtiqueta

    private val _criterioOrdenacion = MutableStateFlow(
        CriterioOrdenacion.entries.firstOrNull { it.name == repositorio.ajustes.actual.criterioOrdenacion }
            ?: CriterioOrdenacion.NOMBRE_AZ
    )
    val criterioOrdenacion: StateFlow<CriterioOrdenacion> = _criterioOrdenacion

    private var trabajoPortapapeles: Job? = null

    // ------------------------------------------------------------- navegación

    /** Pila de navegación: sin esto, el botón atrás cerraba la app. */
    private val pila = ArrayDeque<Pantalla>()

    fun ir(pantalla: Pantalla) {
        if (pantalla != _pantalla.value) {
            pila.addLast(_pantalla.value)
            if (pila.size > 20) pila.removeFirst()
        }
        _pantalla.value = pantalla
    }

    /** Cambia de pantalla vaciando la pila: se usa al abrir, cerrar y bloquear. */
    private fun irRaiz(pantalla: Pantalla) {
        pila.clear()
        _pantalla.value = pantalla
    }

    fun volverALista() {
        irRaiz(Pantalla.Lista)
    }

    /**
     * Devuelve true si consumió el gesto. Si devuelve false, que salga la app:
     * ya estamos en la lista, en el desbloqueo o en el onboarding.
     */
    fun retroceder(): Boolean {
        val anterior = pila.removeLastOrNull() ?: return false
        _pantalla.value = anterior
        return true
    }

    /**
     * Vuelve a la pantalla previa en la pila o a la lista principal si la pila está vacía.
     */
    fun volverAtras() {
        if (!retroceder()) irRaiz(Pantalla.Lista)
    }

    // ------------------------------------------- freno a los intentos de clave

    // El contador vive en disco (ver FrenoIntentos): antes eran dos campos de aquí y
    // cerrar la app desde recientes lo reseteaba, que es justo lo que haría alguien
    // probando claves a mano.
    private val contextoApp: Application get() = getApplication()

    /** Segundos que faltan para poder volver a probar. 0 si se puede probar ya. */
    fun esperaPorIntentos(): Long = FrenoIntentos.esperaSegundos(contextoApp)

    // suspend y en IO: FrenoIntentos escribe con commit(), que es sincrono a
    // proposito, y estas dos se llaman desde dentro de ejecutar{}, que corre en
    // el hilo principal. Sin esto seria escritura a disco en el hilo de la UI.
    private suspend fun apuntarFallo() = withContext(Dispatchers.IO) {
        FrenoIntentos.apuntarFallo(contextoApp)
    }

    private suspend fun limpiarFallos() = withContext(Dispatchers.IO) {
        FrenoIntentos.limpiar(contextoApp)
    }

    // ------------------------------------- bloqueo por inactividad en pantalla

    private val _ultimaInteraccion = MutableStateFlow(System.currentTimeMillis())

    fun registrarInteraccion() {
        _ultimaInteraccion.value = System.currentTimeMillis()
    }

    private var vigilante: Job? = null

    /**
     * Cierra la bóveda si el móvil se queda abierto encima de la mesa.
     * Un solo vigilante: si la actividad se recrea, no se apilan más.
     */
    fun vigilarInactividad() {
        if (vigilante?.isActive == true) return
        vigilante = viewModelScope.launch {
            var estabaDesbloqueada = repositorio.estaDesbloqueada
            while (true) {
                delay(5_000)
                val limite = repositorio.ajustes.actual.autoBloqueoSegundos
                val desbloqueada = repositorio.estaDesbloqueada
                // Al abrirse la bóveda el contador empieza de cero, venga del camino que venga
                // (contraseña, huella o autofill). Sin esto, el tiempo que pasó bloqueada
                // contaba como inactividad y la volvía a cerrar en el siguiente tic.
                if (desbloqueada && !estabaDesbloqueada) registrarInteraccion()
                estabaDesbloqueada = desbloqueada
                // Mientras la oferta de huella está en pantalla el reloj no corre: es la primera
                // vez que el usuario ve la app y estará leyendo, no ignorándola.
                if (_ofrecerBiometria.value) registrarInteraccion()
                if (limite > 0 && desbloqueada && !_ofrecerBiometria.value) {
                    val quieto = System.currentTimeMillis() - _ultimaInteraccion.value
                    if (quieto >= limite * 1000L) {
                        bloquear()
                        _aviso.value = "Bóveda cerrada por inactividad"
                    }
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun limpiarAviso() {
        _aviso.value = null
    }

    fun avisar(texto: String) {
        _aviso.value = texto
    }

    // ----------------------------------------------------------- bóveda: ciclo

    fun crearBoveda(password: String, alTerminar: () -> Unit = {}) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) { repositorio.crear(chars) }
                Diagnostico.apuntar("bóveda", "Bóveda creada y desbloqueada")
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                // Mucha gente no llega nunca a Ajustes: se lo ofrecemos aquí, una vez.
                _ofrecerBiometria.value = true
                alTerminar()
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun desbloquear(password: String, alTerminar: (Boolean) -> Unit = {}) {
        val espera = esperaPorIntentos()
        if (espera > 0) {
            _error.value = "Demasiados intentos. Espera ${espera}s."
            alTerminar(false)
            return
        }
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) { repositorio.desbloquear(chars) }
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con contraseña maestra")
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                alTerminar(true)
            } catch (e: Exception) {
                apuntarFallo()
                Diagnostico.apuntar("bóveda", "Desbloqueo con contraseña rechazado")
                _error.value = "Contraseña incorrecta"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun desbloquearConClave(clave: ByteArray, alTerminar: (Boolean) -> Unit = {}) {
        ejecutar {
            try {
                withContext(Dispatchers.Default) { repositorio.desbloquearConClaveMaestra(clave) }
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con huella")
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                alTerminar(true)
            } catch (e: Exception) {
                Diagnostico.apuntar("huella", "La clave desenvuelta no abrió la bóveda: ${e.javaClass.simpleName}")
                _error.value = "No se pudo abrir la bóveda con la huella"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(clave)
            }
        }
    }

    fun bloquear() {
        val estabaDesbloqueada = repositorio.estaDesbloqueada
        repositorio.bloquear()
        if (estabaDesbloqueada) Diagnostico.apuntar("bóveda", "Bloqueada manualmente")
        // El rato que pase bloqueada no cuenta como inactividad.
        registrarInteraccion()
        irRaiz(if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding)
    }

    // ---------------------------------------------------------------- entradas

    fun entradasVisibles(entradas: List<Entrada>): List<Entrada> {
        val texto = _busqueda.value.trim().lowercase()
        val filtradas = entradas
            .filter { entrada ->
                (_filtroTipo.value == null || entrada.tipo == _filtroTipo.value) &&
                    (!_soloFavoritos.value || entrada.favorito) &&
                    (_filtroEtiqueta.value == null || entrada.etiquetas.contains(_filtroEtiqueta.value)) &&
                    (texto.isEmpty() ||
                        entrada.titulo.lowercase().contains(texto) ||
                        entrada.usuario.lowercase().contains(texto) ||
                        entrada.urls.any { it.lowercase().contains(texto) } ||
                        entrada.etiquetas.any { it.lowercase().contains(texto) })
            }
        val comparador: Comparator<Entrada> = when (_criterioOrdenacion.value) {
            CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() }
            CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<Entrada> { it.titulo.lowercase() }.thenByDescending { it.favorito }
            CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<Entrada> { it.modificadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.ANTIGUEDAD -> compareBy<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
        }
        return filtradas.sortedWith(comparador)
    }

    fun cambiarCriterioOrdenacion(criterio: CriterioOrdenacion) {
        _criterioOrdenacion.value = criterio
        repositorio.ajustes.actualizar { it.copy(criterioOrdenacion = criterio.name) }
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun filtrarPorTipo(tipo: TipoEntrada?) {
        _filtroTipo.value = tipo
    }

    fun alternarSoloFavoritos() {
        _soloFavoritos.value = !_soloFavoritos.value
    }

    fun filtrarPorEtiqueta(etiqueta: String?) {
        _filtroEtiqueta.value = etiqueta
    }

    /** Todas las etiquetas ya usadas en la bóveda, para los chips de filtro y las sugerencias al editar. */
    fun etiquetasUsadas(): List<String> = repositorio.etiquetasUsadas()

    fun entrada(id: String): Entrada? = repositorio.entrada(id)

    fun guardar(entrada: Entrada) {
        ejecutar {
            val existente = withContext(Dispatchers.IO) { repositorio.entrada(entrada.id) }
            withContext(Dispatchers.IO) { repositorio.guardarEntrada(entrada) }
            val tipoDesc = when (entrada.tipo) {
                TipoEntrada.LOGIN -> "login / credencial"
                TipoEntrada.NOTA -> "nota segura"
                TipoEntrada.PASSKEY -> "passkey"
            }
            if (existente == null) {
                Diagnostico.apuntar("bóveda", "Nueva entrada creada ($tipoDesc)")
            } else {
                Diagnostico.apuntar("bóveda", "Entrada modificada ($tipoDesc)")
            }
            _aviso.value = "Guardado en la bóveda"
        }
    }

    fun eliminar(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = when (ent?.tipo) {
                TipoEntrada.NOTA -> "nota segura"
                TipoEntrada.PASSKEY -> "passkey"
                else -> "credencial"
            }
            withContext(Dispatchers.IO) { repositorio.eliminarEntrada(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) enviada a la papelera")
            irRaiz(Pantalla.Lista)
            _aviso.value = "Movida a la papelera"
        }
    }

    fun eliminarVarias(ids: Set<String>) {
        if (ids.isEmpty()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(ids) }
            Diagnostico.apuntar("papelera", "${ids.size} entradas enviadas a la papelera")
            _aviso.value = if (ids.size == 1) "Entrada movida a la papelera" else "${ids.size} entradas movidas a la papelera"
        }
    }

    // ------------------------------------------------------------- papelera

    fun restaurarDeLaPapelera(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.restaurarDeLaPapelera(id) }
            Diagnostico.apuntar("papelera", "Entrada restaurada desde la papelera a la bóveda")
            _aviso.value = "Entrada restaurada"
        }
    }

    fun borrarDefinitivamente(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.borrarDefinitivamente(id) }
            Diagnostico.apuntar("papelera", "Entrada eliminada definitivamente de la papelera")
            _aviso.value = "Borrada para siempre"
        }
    }

    fun vaciarPapelera() {
        ejecutar {
            val cant = repositorio.papelera().size
            withContext(Dispatchers.IO) { repositorio.vaciarPapelera() }
            Diagnostico.apuntar("papelera", "Papelera vaciada por completo ($cant entradas eliminadas definitivamente)")
            _aviso.value = "Papelera vaciada"
        }
    }

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

    fun nuevoId(): String = repositorio.nuevoId()

    // -------------------------------------------------------------- 2FA (TOTP)

    fun entradasConTotp(entradas: List<Entrada>): List<Entrada> =
        entradas.filter { !it.secretoTotp.isNullOrBlank() }
            .sortedBy { it.titulo.lowercase() }

    /**
     * Alta de un doble factor a partir de un QR o de un código escrito a mano.
     * Devuelve false si el texto no sirve, para que la pantalla lo diga sin salir.
     */
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

    // ------------------------------------------------------------ portapapeles

    fun copiar(etiqueta: String, valor: String, sensible: Boolean) {
        val contexto = getApplication<Application>()
        Portapapeles.copiarSensible(contexto, etiqueta, valor)
        val accionDesc = when (etiqueta.lowercase()) {
            "usuario" -> "Usuario copiado al portapapeles"
            "contraseña" -> "Contraseña copiada al portapapeles"
            "código", "código totp", "código 2fa" -> "Código 2FA copiado al portapapeles"
            "contraseña anterior" -> "Contraseña anterior copiada al portapapeles"
            else -> "$etiqueta copiado/a al portapapeles"
        }
        Diagnostico.apuntar("portapapeles", accionDesc)
        trabajoPortapapeles?.cancel()
        // Todas las copias de datos de la bóveda usan la misma barra y limpieza automática.
        // Cancelar el job anterior no ejecuta su limpieza final: primero se resetea la barra.
        _cuentaAtrasPortapapeles.value = 0
        val segundos = repositorio.ajustes.actual.portapapelesSegundos
        trabajoPortapapeles = viewModelScope.launch {
            for (restante in segundos downTo 1) {
                _cuentaAtrasPortapapeles.value = restante
                delay(1_000)
            }
            _cuentaAtrasPortapapeles.value = 0
            Portapapeles.limpiarSiCoincide(contexto, valor)
            Diagnostico.apuntar("portapapeles", "Portapapeles limpiado automáticamente ($segundos s)")
        }
    }

    // --------------------------------------------------------------- ajustes

    fun ajustarAutoBloqueo(segundos: Int) = repositorio.ajustes.actualizar { it.copy(autoBloqueoSegundos = segundos) }

    fun ajustarPortapapeles(segundos: Int) = repositorio.ajustes.actualizar { it.copy(portapapelesSegundos = segundos) }

    fun ajustarTileModo(modo: String) = repositorio.ajustes.actualizar { it.copy(tileModo = modo) }

    fun ajustarTileLongitud(longitud: Int) = repositorio.ajustes.actualizar { it.copy(tileLongitud = longitud) }

    fun ajustarTilePatron(patron: String) = repositorio.ajustes.actualizar { it.copy(tilePatron = patron) }

    fun ajustarTileCopiarPortapapeles(copiar: Boolean) = repositorio.ajustes.actualizar { it.copy(tileCopiarPortapapeles = copiar) }

    fun ajustarTileMostrarToast(toast: Boolean) = repositorio.ajustes.actualizar { it.copy(tileMostrarToast = toast) }

    fun ajustarTileHaptica(haptica: Boolean) = repositorio.ajustes.actualizar { it.copy(tileHaptica = haptica) }

    fun ajustarMotorCamara(clave: String) = repositorio.ajustes.actualizar { it.copy(motorCamara = clave) }

    fun ajustarNombrePersonalizado(nombre: String) =
        repositorio.ajustes.actualizar { it.copy(nombrePersonalizado = nombre) }

    /** Cambia el icono del launcher (las 20 variantes precompiladas de Android). */
    fun ajustarIconoLauncher(clave: String) {
        repositorio.ajustes.actualizar { it.copy(iconoLauncher = clave) }
        com.jlnavas3.bovedalocal.util.CambiadorIcono.aplicar(contextoApp, clave)
    }

    /** Cambia el acento del tema Y el color del icono del launcher (mismo dibujo, otro degradado). */
    fun ajustarColorApp(paleta: com.jlnavas3.bovedalocal.ui.theme.PaletaAcento) {
        com.jlnavas3.bovedalocal.ui.theme.aplicarPaletaAcento(paleta)
        repositorio.ajustes.actualizar { it.copy(colorAcento = paleta.clave, iconoLauncher = paleta.clave) }
        com.jlnavas3.bovedalocal.util.CambiadorIcono.aplicar(contextoApp, paleta.clave)
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorAcento(hexOClave: String) {
        repositorio.ajustes.actualizar { it.copy(colorAcento = hexOClave) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorIconosInternos(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorIconosInternos = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorTitulos(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorTitulos = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorTarjetas(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorTarjetas = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorSeguridad(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorSeguridad = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColor2FA(hex: String) {
        repositorio.ajustes.actualizar { it.copy(color2FA = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorPasskeys(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorPasskeys = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorGenerador(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorGenerador = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorSalud(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorSalud = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorPapelera(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorPapelera = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun ajustarColorExportacion(hex: String) {
        repositorio.ajustes.actualizar { it.copy(colorExportacion = hex) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun restablecerColoresTema() {
        repositorio.ajustes.actualizar {
            it.copy(
                colorAcento = "ambar",
                colorIconosInternos = "",
                colorTitulos = "",
                colorTarjetas = "",
                colorDinamicoSistema = false,
                colorSeguridad = "",
                color2FA = "",
                colorPasskeys = "",
                colorGenerador = "",
                colorSalud = "",
                colorPapelera = "",
                colorExportacion = ""
            )
        }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionColores(repositorio.ajustes.actual)
    }

    fun alternarColorDinamicoSistema(activo: Boolean) {
        repositorio.ajustes.actualizar { it.copy(colorDinamicoSistema = activo) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
    }

    // --- Personalización de Bordes y Formas ---
    fun ajustarCurvaturaEsquinas(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(curvaturaEsquinasDp = valor) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarGrosorBorde(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(grosorBordeDp = valor) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEstiloBorde(estilo: String) {
        repositorio.ajustes.actualizar { it.copy(estiloBorde = estilo) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoComponentes(valor: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoComponentesDp = valor) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun aplicarPresetFormas(curvatura: Float, grosor: Float, estilo: String, espaciado: Float) {
        repositorio.ajustes.actualizar {
            it.copy(
                curvaturaEsquinasDp = curvatura,
                grosorBordeDp = grosor,
                estiloBorde = estilo,
                espaciadoComponentesDp = espaciado
            )
        }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionFormas(repositorio.ajustes.actual)
    }

    fun restablecerFormas() {
        aplicarPresetFormas(curvatura = 6f, grosor = 0.8f, estilo = "marcado", espaciado = 14f)
    }

    // --- Personalización de Tipografía y Textos ---
    fun ajustarEscalaTexto(escala: Float) {
        repositorio.ajustes.actualizar { it.copy(escalaTexto = escala) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarPesoTexto(peso: String) {
        repositorio.ajustes.actualizar { it.copy(pesoTexto = peso) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarCursivaTexto(cursiva: Boolean) {
        repositorio.ajustes.actualizar { it.copy(cursivaTexto = cursiva) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarEspaciadoLetras(espaciado: Float) {
        repositorio.ajustes.actualizar { it.copy(espaciadoLetrasSp = espaciado) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarInterlineadoFactor(factor: Float) {
        repositorio.ajustes.actualizar { it.copy(interlineadoFactor = factor) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun ajustarFamiliaFuente(familia: String) {
        repositorio.ajustes.actualizar { it.copy(familiaFuente = familia) }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun aplicarPresetTipografia(
        escala: Float,
        peso: String,
        cursiva: Boolean,
        kerning: Float,
        interlineado: Float,
        familia: String
    ) {
        repositorio.ajustes.actualizar {
            it.copy(
                escalaTexto = escala,
                pesoTexto = peso,
                cursivaTexto = cursiva,
                espaciadoLetrasSp = kerning,
                interlineadoFactor = interlineado,
                familiaFuente = familia
            )
        }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTipografia(repositorio.ajustes.actual)
    }

    fun restablecerTipografia() {
        aplicarPresetTipografia(
            escala = 1.0f,
            peso = "normal",
            cursiva = false,
            kerning = 0.0f,
            interlineado = 1.0f,
            familia = "sans"
        )
    }

    fun ajustarTema(clave: String) {
        repositorio.ajustes.actualizar {
            if (it.temaApp != clave) {
                it.copy(
                    temaApp = clave,
                    colorIconosInternos = "",
                    colorTitulos = "",
                    colorTarjetas = "",
                    colorSeguridad = "",
                    color2FA = "",
                    colorPasskeys = "",
                    colorGenerador = "",
                    colorSalud = "",
                    colorPapelera = "",
                    colorExportacion = ""
                )
            } else {
                it.copy(temaApp = clave)
            }
        }
        com.jlnavas3.bovedalocal.ui.theme.aplicarPersonalizacionTemaCompleto(repositorio.ajustes.actual)
    }

    fun ajustarRecordatorioExportacion(dias: Int) =
        repositorio.ajustes.actualizar { it.copy(recordatorioExportacionDias = dias) }

    fun ajustarDensidadLista(clave: String) =
        repositorio.ajustes.actualizar { it.copy(densidadLista = clave) }

    fun ajustarAgruparPorSitio(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(agruparPorSitio = activo) }

    fun ajustarTotpManualDigitos(digitos: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualDigitos = digitos) }

    fun ajustarTotpManualPeriodo(periodo: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualPeriodo = periodo) }

    fun ajustarTotpManualAlgoritmo(algoritmo: String) =
        repositorio.ajustes.actualizar { it.copy(totpManualAlgoritmo = algoritmo) }

    fun ajustarTotpSepararDigitos(separar: Boolean) =
        repositorio.ajustes.actualizar { it.copy(totpSepararDigitos = separar) }

    fun ajustarMostrarIndiceAlfabetico(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(mostrarIndiceAlfabetico = activo) }

    fun ajustarIndiceEfectoOla(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceEfectoOla = activo) }

    fun ajustarIndiceAmplitudOlaDp(amplitud: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceAmplitudOlaDp = amplitud) }

    fun ajustarIndiceRadioOlaDp(radio: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceRadioOlaDp = radio) }

    fun ajustarIndiceEscalaLetras(escala: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceEscalaLetras = escala) }

    fun ajustarIndiceMostrarCirculo(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceMostrarCirculo = activo) }

    fun ajustarIndiceOffsetCirculoDp(offset: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceOffsetCirculoDp = offset) }

    fun ajustarIndiceHaptica(activo: Boolean) =
        repositorio.ajustes.actualizar { it.copy(indiceHaptica = activo) }

    fun ajustarIndiceAnchoTactilDp(anchoDp: Float) =
        repositorio.ajustes.actualizar { it.copy(indiceAnchoTactilDp = anchoDp) }

    fun restablecerAjustesIndiceAlfabetico() {
        repositorio.ajustes.actualizar {
            it.copy(
                mostrarIndiceAlfabetico = true,
                indiceEfectoOla = true,
                indiceAmplitudOlaDp = 95f,
                indiceRadioOlaDp = 220f,
                indiceEscalaLetras = 1.9f,
                indiceMostrarCirculo = true,
                indiceOffsetCirculoDp = 145f,
                indiceHaptica = true,
                indiceAnchoTactilDp = 50f
            )
        }
    }

    /** Días sin exportar la bóveda; null si nunca se exportó o el recordatorio está apagado. */
    fun diasSinExportar(): Long? {
        val ajustes = repositorio.ajustes.actual
        if (ajustes.recordatorioExportacionDias <= 0) return null
        if (ajustes.ultimaExportacionEn <= 0L) return null
        val transcurridos = (System.currentTimeMillis() - ajustes.ultimaExportacionEn) / (24L * 60 * 60 * 1000)
        return if (transcurridos >= ajustes.recordatorioExportacionDias) transcurridos else null
    }

    /** Se pone a true justo al crear la bóveda, para ofrecer la huella sin pasar por Ajustes. */
    private val _ofrecerBiometria = MutableStateFlow(false)
    val ofrecerBiometria: StateFlow<Boolean> = _ofrecerBiometria

    fun cerrarOfertaBiometria() {
        _ofrecerBiometria.value = false
        // Encadenamos con la otra cosa que hay que hacer una sola vez: ponerme
        // como gestor de contraseñas del sistema. Si no, nadie lo encuentra.
        _ofrecerGestor.value = true
    }

    /** Oferta, una sola vez, de activar Bóveda local como gestor del sistema. */
    private val _ofrecerGestor = MutableStateFlow(false)
    val ofrecerGestor: StateFlow<Boolean> = _ofrecerGestor

    fun cerrarOfertaGestor() {
        _ofrecerGestor.value = false
    }

    // ------------------------------------------------------- exportar/importar

    fun exportar(password: String, escritor: (ByteArray) -> Unit) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                val datos = withContext(Dispatchers.Default) { repositorio.exportar(chars) }
                withContext(Dispatchers.IO) { escritor(datos) }
                repositorio.ajustes.actualizar { it.copy(ultimaExportacionEn = System.currentTimeMillis()) }
                Diagnostico.apuntar("bóveda", "Copia de seguridad cifrada exportada correctamente")
                _aviso.value = "Bóveda exportada y cifrada"
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al exportar copia de seguridad: ${e.message ?: "error desconocido"}", e)
                _error.value = "No se pudo exportar: ${e.message ?: "error desconocido"}"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importar(password: String, lector: () -> ByteArray) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                val datos = withContext(Dispatchers.IO) { lector() }
                val nuevas = withContext(Dispatchers.Default) { repositorio.importar(datos, chars) }
                Diagnostico.apuntar("bóveda", "Bóveda importada desde copia cifrada ($nuevas entradas incorporadas)")
                _aviso.value = "Importadas $nuevas entradas"
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo cifrado: contraseña incorrecta o archivo inválido", e)
                _error.value = "No se pudo importar: contraseña incorrecta o archivo inválido"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importarCsv(lector: () -> ByteArray, onResultado: ((Int) -> Unit)? = null) {
        ejecutar {
            try {
                val datos = withContext(Dispatchers.IO) { lector() }
                val nuevas = withContext(Dispatchers.Default) { repositorio.importarCsv(datos) }
                Diagnostico.apuntar("bóveda", "Importación CSV completada con éxito ($nuevas entradas incorporadas)")
                _aviso.value = "Importadas $nuevas entradas desde CSV"
                onResultado?.invoke(nuevas)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo CSV: ${e.message ?: "formato no reconocido"}", e)
                _error.value = "No se pudo importar el CSV: ${e.message ?: "formato no reconocido"}"
            }
        }
    }

    fun registrarCsvGoogleImportado(ruta: String, uri: String, cuentas: Int) {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = ruta,
                csvGoogleUri = uri,
                csvGoogleCuentas = cuentas,
                csvGoogleEliminado = false
            )
        }
    }

    fun marcarCsvGoogleEliminado(eliminado: Boolean) {
        repositorio.ajustes.actualizar {
            it.copy(csvGoogleEliminado = eliminado)
        }
        if (eliminado) {
            Diagnostico.apuntar("seguridad", "Archivo CSV de Google eliminado de forma segura del almacenamiento")
        }
    }

    fun descartarAvisoCsvGoogle() {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = "",
                csvGoogleUri = "",
                csvGoogleCuentas = 0,
                csvGoogleEliminado = false
            )
        }
    }


    fun cambiarContrasenaMaestra(actual: String, nueva: String) {
        ejecutar {
            val viejaChars = actual.toCharArray()
            val nuevaChars = nueva.toCharArray()
            try {
                val correcta = withContext(Dispatchers.Default) { repositorio.verificarContrasena(viejaChars) }
                if (!correcta) {
                    Diagnostico.apuntar("bóveda", "Intento de cambio de contraseña maestra rechazado (contraseña actual incorrecta)")
                    _error.value = "La contraseña actual no es correcta"
                    return@ejecutar
                }
                withContext(Dispatchers.Default) { repositorio.cambiarContrasenaMaestra(nuevaChars) }
                Diagnostico.apuntar("bóveda", "Contraseña maestra de la bóveda modificada exitosamente")
                _aviso.value = "Contraseña maestra cambiada. Vuelve a activar la biometría."
            } finally {
                Zeroizar.borrar(viejaChars)
                Zeroizar.borrar(nuevaChars)
            }
        }
    }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _trabajando.value = true
            try {
                bloque()
            } catch (e: Exception) {
                // Solo la clase: el mensaje de estas excepciones puede llevar rutas o contenido de la bóveda.
                Diagnostico.apuntar("app", "Operación fallida: ${e.javaClass.simpleName}")
                _error.value = e.message ?: "Algo salió mal"
            } finally {
                _trabajando.value = false
            }
        }
    }
}
