package com.jlnavas3.bovedalocal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

data class RecordatorioExportacionInfo(
    val titulo: String,
    val descripcion: String
)

class VaultViewModel(app: Application) : AndroidViewModel(app),
    VaultAjustesDelegate,
    VaultBackupDelegate,
    VaultNavegacionDelegate,
    VaultEntradasDelegate {

    override val repositorio = VaultRepository.obtener(app)
    override fun obtenerApp(): Application = getApplication()

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
    override val errorInterno: MutableStateFlow<String?> get() = _error

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso
    override val avisoInterno: MutableStateFlow<String?> get() = _aviso

    private val _cuentaAtrasPortapapeles = MutableStateFlow(0)
    val cuentaAtrasPortapapeles: StateFlow<Int> = _cuentaAtrasPortapapeles

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda
    override val busquedaInterna: MutableStateFlow<String> get() = _busqueda

    private val _filtroTipo = MutableStateFlow<TipoEntrada?>(null)
    val filtroTipo: StateFlow<TipoEntrada?> = _filtroTipo
    override val filtroTipoInterno: MutableStateFlow<TipoEntrada?> get() = _filtroTipo

    private val _soloFavoritos = MutableStateFlow(false)
    val soloFavoritos: StateFlow<Boolean> = _soloFavoritos
    override val soloFavoritosInterno: MutableStateFlow<Boolean> get() = _soloFavoritos

    private val _filtroEtiqueta = MutableStateFlow<String?>(null)
    val filtroEtiqueta: StateFlow<String?> = _filtroEtiqueta
    override val filtroEtiquetaInterno: MutableStateFlow<String?> get() = _filtroEtiqueta

    private val _criterioOrdenacion = MutableStateFlow(
        CriterioOrdenacion.entries.firstOrNull { it.name == repositorio.ajustes.actual.criterioOrdenacion }
            ?: CriterioOrdenacion.NOMBRE_AZ
    )
    val criterioOrdenacion: StateFlow<CriterioOrdenacion> = _criterioOrdenacion
    override val criterioOrdenacionInterno: MutableStateFlow<CriterioOrdenacion> get() = _criterioOrdenacion

    private var trabajoPortapapeles: Job? = null

    // ------------------------------------------------------------- navegación (VaultNavegacionDelegate)

    override val pantallaInterna: MutableStateFlow<Pantalla> get() = _pantalla
    override val navegandoAtrasInterno: MutableStateFlow<Boolean> get() = _navegandoAtras
    override val pilaNavegacion: ArrayDeque<Pantalla> = ArrayDeque()
    override var ultimoWidgetAjustesSeleccionado: Int = 0

    /** true cuando la última transición fue un retroceso: la animación desliza al revés. */
    private val _navegandoAtras = MutableStateFlow(false)
    val navegandoAtras: StateFlow<Boolean> = _navegandoAtras

    // ------------------------------------------- freno a los intentos de clave

    private val contextoApp: Application get() = getApplication()

    /** Segundos que faltan para poder volver a probar. 0 si se puede probar ya. */
    fun esperaPorIntentos(): Long = FrenoIntentos.esperaSegundos(contextoApp)

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
                if (desbloqueada && !estabaDesbloqueada) registrarInteraccion()
                estabaDesbloqueada = desbloqueada
                if (_ofrecerBiometria.value) registrarInteraccion()
                if (limite > 0 && desbloqueada && !_ofrecerBiometria.value) {
                    val quieto = System.currentTimeMillis() - _ultimaInteraccion.value
                    if (quieto >= limite * 1000L) {
                        bloquear(porInactividad = true)
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

    override fun avisar(texto: String) {
        _aviso.value = texto
    }

    override fun ir(pantalla: Pantalla) = super<VaultNavegacionDelegate>.ir(pantalla)
    override fun irRaiz(pantalla: Pantalla) = super<VaultNavegacionDelegate>.irRaiz(pantalla)

    // ----------------------------------------------------------- bóveda: ciclo

    fun crearBoveda(password: String, alTerminar: () -> Unit = {}) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) { repositorio.crear(chars) }
                Diagnostico.apuntar("bóveda", "Bóveda creada y desbloqueada")
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                _ofrecerBiometria.value = true
                alTerminar()
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun reForjarBoveda(password: String, nuevoPerfil: com.jlnavas3.bovedalocal.crypto.PerfilArgon2, alTerminar: (Boolean) -> Unit = {}) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) {
                    repositorio.reForjarBovedaConPerfil(chars, nuevoPerfil)
                }
                Diagnostico.apuntar("bóveda", "Bóveda re-forjada con perfil ${nuevoPerfil.titulo} (${nuevoPerfil.memoriaKiB / 1024} MiB RAM, ${nuevoPerfil.iteraciones} pasadas, ${nuevoPerfil.paralelismo} hilos)")
                registrarInteraccion()
                avisar("Bóveda re-cifrada con perfil ${nuevoPerfil.titulo}")
                alTerminar(true)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al re-forjar bóveda: ${e.message}")
                _error.value = "Contraseña incorrecta o fallo al re-cifrar"
                alTerminar(false)
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
        if (com.jlnavas3.bovedalocal.data.PinAutodestruccion.esPinAutodestruccion(contextoApp, password)) {
            ejecutar {
                repositorio.borrarTodo()
                com.jlnavas3.bovedalocal.data.PinAutodestruccion.desactivar(contextoApp)
                com.jlnavas3.bovedalocal.data.BovedaSenuelo.desactivar(contextoApp)
                limpiarFallos()
                irRaiz(Pantalla.Onboarding)
                alTerminar(true)
            }
            return
        }
        if (com.jlnavas3.bovedalocal.data.BovedaSenuelo.esPinCoaccion(contextoApp, password)) {
            ejecutar {
                val senuelo = com.jlnavas3.bovedalocal.data.BovedaSenuelo.cargar(contextoApp).entradas
                repositorio.abrirSenuelo(senuelo)
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con contraseña maestra")
                registrarInteraccion()
                alTerminar(true)
                delay(520)
                irRaiz(Pantalla.Lista)
            }
            return
        }
        ejecutar {
            val chars = password.toCharArray()
            try {
                withContext(Dispatchers.Default) { repositorio.desbloquear(chars) }
                limpiarFallos()
                Diagnostico.apuntar("bóveda", "Desbloqueada con contraseña maestra")
                registrarInteraccion()
                alTerminar(true)
                delay(520)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                verificarBackupAutomatico()
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
                alTerminar(true)
                delay(520)
                irRaiz(Pantalla.Lista)
                procesarShortcutPendiente()
                verificarBackupAutomatico()
            } catch (e: Exception) {
                Diagnostico.apuntar("huella", "La clave desenvuelta no abrió la bóveda: ${e.javaClass.simpleName}")
                _error.value = "No se pudo abrir la bóveda con la huella"
                alTerminar(false)
            } finally {
                Zeroizar.borrar(clave)
            }
        }
    }

    private var accionShortcutPendiente: String? = null

    fun solicitarAccionShortcut(accion: String) {
        if (repositorio.estaDesbloqueada) {
            ejecutarAccionShortcut(accion)
        } else {
            accionShortcutPendiente = accion
        }
    }

    fun procesarShortcutPendiente() {
        val pendiente = accionShortcutPendiente ?: return
        accionShortcutPendiente = null
        ejecutarAccionShortcut(pendiente)
    }

    fun ejecutarAccionShortcut(accion: String) {
        when (accion) {
            "nueva_entrada" -> ir(Pantalla.Editar(id = null))
            "buscar" -> irRaiz(Pantalla.Lista)
            "escanear_qr" -> ir(Pantalla.Escaner())
        }
    }

    fun verificarBackupAutomatico() {
        if (com.jlnavas3.bovedalocal.data.GestorBackupAutomatico.debeEjecutar(repositorio.ajustes.actual)) {
            ejecutarBackupAutomatico(manual = false)
        }
    }

    fun ejecutarBackupAutomatico(manual: Boolean = false, alTerminar: (Boolean, String) -> Unit = { _, _ -> }) {
        ejecutar {
            val (exito, mensaje) = withContext(Dispatchers.IO) {
                com.jlnavas3.bovedalocal.data.GestorBackupAutomatico.ejecutar(contextoApp, repositorio)
            }
            if (manual || exito) {
                _aviso.value = mensaje
            }
            alTerminar(exito, mensaje)
        }
    }

    fun bloquear(porInactividad: Boolean = false) {
        val estabaDesbloqueada = repositorio.estaDesbloqueada
        repositorio.bloquear()
        if (estabaDesbloqueada) {
            val limite = repositorio.ajustes.actual.autoBloqueoSegundos
            val mensaje = if (porInactividad) "Bloqueada por caducidad de tiempo (${limite}s de inactividad)" else "Bloqueada manualmente"
            Diagnostico.apuntar("bóveda", mensaje)
        }
        registrarInteraccion()
        irRaiz(if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding)
    }

    /** Información del recordatorio de copia; null si no corresponde mostrarlo. */
    fun recordatorioExportacionInfo(): RecordatorioExportacionInfo? {
        val ajustes = repositorio.ajustes.actual
        val config = ajustes.recordatorioExportacionDias
        if (config == 0) return null

        val ahora = System.currentTimeMillis()
        val umbralMs = when (config) {
            -30 -> 30L * 60 * 1000 // 30 minutos
            in 1..Int.MAX_VALUE -> config.toLong() * 24 * 60 * 60 * 1000
            else -> return null
        }

        val ultimaCopia = maxOf(ajustes.ultimaExportacionEn, ajustes.backupAutoUltimaEjecucion)

        if (ultimaCopia > 0L) {
            val transcurridoMs = ahora - ultimaCopia
            if (transcurridoMs >= umbralMs) {
                return if (config == -30) {
                    val minutos = (transcurridoMs / (60 * 1000)).coerceAtLeast(30)
                    RecordatorioExportacionInfo(
                        titulo = "Hace $minutos min que no haces una copia",
                        descripcion = "Toca para ir a Copia de seguridad"
                    )
                } else {
                    val dias = (transcurridoMs / (24L * 60 * 60 * 1000)).coerceAtLeast(config.toLong())
                    RecordatorioExportacionInfo(
                        titulo = "Hace $dias ${if (dias == 1L) "día" else "días"} sin exportar una copia",
                        descripcion = "Toca para ir a Copia de seguridad"
                    )
                }
            }
            return null
        } else {
            // Nunca se ha realizado una copia ni exportación
            val entradas = repositorio.entradas()
            if (entradas.isEmpty()) return null

            val tiempoBase = entradas.minOfOrNull { it.creadaEn }
                ?: repositorio.archivoBoveda.lastModified().takeIf { it > 0L }
                ?: ahora

            val transcurridoMs = ahora - tiempoBase
            if (transcurridoMs >= umbralMs) {
                return RecordatorioExportacionInfo(
                    titulo = "Copia de seguridad recomendada",
                    descripcion = "Aún no has realizado ninguna copia de tu bóveda. Toca para hacer una."
                )
            }
            return null
        }
    }

    /** Días sin exportar la bóveda; null si nunca se exportó o el recordatorio está apagado. */
    fun diasSinExportar(): Long? {
        val info = recordatorioExportacionInfo() ?: return null
        val config = repositorio.ajustes.actual.recordatorioExportacionDias
        if (config == -30) return 0L
        val ultimaCopia = maxOf(repositorio.ajustes.actual.ultimaExportacionEn, repositorio.ajustes.actual.backupAutoUltimaEjecucion)
        return if (ultimaCopia > 0L) (System.currentTimeMillis() - ultimaCopia) / (24L * 60 * 60 * 1000) else config.toLong()
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

    // Ofertas iniciales tras crear bóveda
    private val _ofrecerBiometria = MutableStateFlow(false)
    val ofrecerBiometria: StateFlow<Boolean> = _ofrecerBiometria

    fun cerrarOfertaBiometria() {
        _ofrecerBiometria.value = false
        _ofrecerGestor.value = true
    }

    private val _ofrecerGestor = MutableStateFlow(false)
    val ofrecerGestor: StateFlow<Boolean> = _ofrecerGestor

    fun cerrarOfertaGestor() {
        _ofrecerGestor.value = false
    }

    override fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            _trabajando.value = true
            try {
                bloque()
            } catch (e: Exception) {
                Diagnostico.apuntar("app", "Operación fallida: ${e.javaClass.simpleName}")
                _error.value = e.message ?: "Algo salió mal"
            } finally {
                _trabajando.value = false
            }
        }
    }
}
