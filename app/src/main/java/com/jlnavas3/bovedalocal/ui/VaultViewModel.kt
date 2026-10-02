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
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VaultViewModel(app: Application) : AndroidViewModel(app),
    VaultAjustesDelegate,
    VaultBackupDelegate,
    VaultNavegacionDelegate,
    VaultEntradasDelegate,
    VaultCicloBovedaDelegate {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            GestorAppsInstaladas.precargar(app)
        }
    }

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

    fun mostrarAviso(mensaje: String) { _aviso.value = mensaje }
    fun mostrarError(mensaje: String) { _error.value = mensaje }

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
    override var ultimoScrollAjustes: Int = 0
    override var navegoDesdeMenuLateral: Boolean = false
    override val abrirMenuLateralAlVolverALista = MutableStateFlow(false)

    /** true cuando la última transición fue un retroceso: la animación desliza al revés. */
    private val _navegandoAtras = MutableStateFlow(false)
    val navegandoAtras: StateFlow<Boolean> = _navegandoAtras

    override var accionShortcutPendiente: String? = null

    // ------------------------------------- bloqueo por inactividad en pantalla

    private val _ultimaInteraccion = MutableStateFlow(System.currentTimeMillis())

    override fun registrarInteraccion() {
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

    // ------------------------------------------------------------ portapapeles

    fun copiar(etiqueta: String, valor: String, sensible: Boolean) {
        val contexto = getApplication<Application>()
        Portapapeles.copiarSensible(contexto, etiqueta, valor)
        val accionDesc = when (etiqueta.lowercase()) {
            "usuario" -> "Usuario copiado al portapapeles"
            "contraseña" -> "Contraseña copiada al portapapeles"
            "código", "código totp", "código 2fa", "código de verificación" -> "Código de verificación copiado al portapapeles"
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
    override val ofrecerBiometriaInterno: MutableStateFlow<Boolean> get() = _ofrecerBiometria

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
