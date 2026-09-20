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

data class RecordatorioExportacionInfo(
    val titulo: String,
    val descripcion: String
)

class VaultViewModel(app: Application) : AndroidViewModel(app), VaultAjustesDelegate, VaultBackupDelegate {

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

    /** true cuando la última transición fue un retroceso: la animación desliza al revés. */
    private val _navegandoAtras = MutableStateFlow(false)
    val navegandoAtras: StateFlow<Boolean> = _navegandoAtras

    /** Devuelve la pantalla padre a la que debe retornar una subsección jerárquica. */
    fun padreDe(pantalla: Pantalla): Pantalla? = when (pantalla) {
        // Grupo 01: Seguridad -> Ajustes
        is Pantalla.Seguridad -> Pantalla.Ajustes("01.1")
        is Pantalla.AjustesSenuelo -> Pantalla.Ajustes("01.2")
        is Pantalla.AjustesAutodestruccion -> Pantalla.Ajustes("01.3")
        is Pantalla.Argon2id -> Pantalla.Ajustes("01.4")

        // Grupo 02: Cuentas y Datos -> Ajustes
        is Pantalla.CopiaSeguridad -> Pantalla.Ajustes("02.1")
        is Pantalla.CsvGoogle -> Pantalla.Ajustes("02.2")
        is Pantalla.KitEmergencia -> Pantalla.Ajustes("02.3")

        // Grupo 03: Personalización -> Ajustes
        is Pantalla.Tema -> Pantalla.Ajustes("03.2")
        is Pantalla.CalibracionAnimacion -> Pantalla.Tema("03.2.G2")
        is Pantalla.AjustesWidget -> Pantalla.Ajustes("03.3")
        is Pantalla.Formas -> Pantalla.Ajustes("03.3")
        is Pantalla.AjustesIndice -> Pantalla.Ajustes("03.4")
        is Pantalla.Tipografia -> Pantalla.Ajustes("03.4")
        is Pantalla.OrganizacionLista -> Pantalla.Ajustes("03.5")
        is Pantalla.FormatosCampos -> Pantalla.Ajustes("03.6")

        // Grupo 04: Funciones -> Ajustes
        is Pantalla.AjustesAutenticador -> Pantalla.Ajustes("04.1")
        is Pantalla.AjustesCamara -> Pantalla.Ajustes("04.2")
        is Pantalla.TileRapido -> Pantalla.Ajustes("04.3")

        // Grupo 05: Sistema -> Ajustes
        is Pantalla.Avanzada -> Pantalla.Ajustes("05.1")

        // Pantallas de la barra lateral -> Retornan a la Lista principal
        is Pantalla.Generador -> Pantalla.Lista
        is Pantalla.HistorialClaves -> Pantalla.Lista
        is Pantalla.Passkeys -> Pantalla.Lista
        is Pantalla.Autenticador -> Pantalla.Lista
        is Pantalla.SaludBoveda -> Pantalla.Lista
        is Pantalla.Duplicados -> Pantalla.Lista
        is Pantalla.Papelera -> Pantalla.Lista
        is Pantalla.Registro -> Pantalla.Lista
        is Pantalla.AcercaDe -> Pantalla.Lista
        else -> null
    }

    fun ir(pantalla: Pantalla) {
        _navegandoAtras.value = false
        if (pantalla != _pantalla.value) {
            // Si salimos de Ajustes hacia una subpantalla hija con padre conocido,
            // guardamos en la pila el estado contextual con el ID de retorno para que
            // al volver atrás regrese a la tarjeta padre desplegada.
            val origen = if (_pantalla.value is Pantalla.Ajustes) {
                val padre = padreDe(pantalla)
                if (padre is Pantalla.Ajustes && !padre.seccionId.isNullOrBlank()) {
                    padre
                } else {
                    _pantalla.value
                }
            } else {
                _pantalla.value
            }
            pila.addLast(origen)
            if (pila.size > 20) pila.removeFirst()
        }
        _pantalla.value = pantalla
    }

    /** Navega directamente a la pantalla y tarjeta específica correspondiente al ID jerárquico. */
    fun irPorId(id: String) {
        val limpio = id.trim()
        val destino = when {
            // Grupo 01: Seguridad
            limpio == "01" || limpio.startsWith("01.1") || limpio.startsWith("01.0") -> Pantalla.Seguridad(limpio)
            limpio.startsWith("01.2") -> Pantalla.AjustesSenuelo(limpio)
            limpio.startsWith("01.3") -> Pantalla.AjustesAutodestruccion(limpio)
            limpio.startsWith("01.4") || limpio == "03" || limpio.startsWith("03.0") -> Pantalla.Argon2id(limpio)

            // Grupo 02: Cuentas y Datos
            limpio == "02" || limpio.startsWith("02.1") || limpio == "06" || limpio.startsWith("06.0") -> Pantalla.CopiaSeguridad(limpio)
            limpio.startsWith("02.2") || limpio == "08" || limpio.startsWith("08.0") -> Pantalla.CsvGoogle(limpio)
            limpio.startsWith("02.3") || limpio.startsWith("06.3") -> Pantalla.KitEmergencia(limpio)
            limpio.startsWith("02.4") || limpio.startsWith("06.1") -> Pantalla.SaludBoveda(limpio)
            limpio.startsWith("02.5") || limpio.startsWith("06.2") -> Pantalla.Duplicados(limpio)
            limpio.startsWith("02.6") || limpio.startsWith("06.4") -> Pantalla.Papelera(limpio)

            // Grupo 03: Personalización
            limpio.startsWith("03.1") || limpio.startsWith("09.1") -> Pantalla.Ajustes("03.1")
            limpio.startsWith("03.2") || limpio.startsWith("09.2") -> Pantalla.Tema(limpio)
            limpio.startsWith("03.3") || limpio.startsWith("09.4") -> Pantalla.AjustesWidget(limpio)
            limpio.startsWith("03.4") || limpio.startsWith("09.5") -> Pantalla.AjustesIndice(limpio)
            limpio.startsWith("03.5") || limpio.startsWith("09.6") || limpio.startsWith("09.7") -> Pantalla.OrganizacionLista(limpio)
            limpio.startsWith("03.6") || limpio.startsWith("10") -> Pantalla.FormatosCampos(limpio)

            // Grupo 04: Funciones y Herramientas
            limpio.startsWith("04.1") || limpio == "07" || limpio.startsWith("07.0") -> Pantalla.AjustesAutenticador(limpio)
            limpio.startsWith("04.2") || limpio == "05" || limpio.startsWith("05.0") -> Pantalla.AjustesCamara(limpio)
            limpio.startsWith("04.3") -> Pantalla.TileRapido(limpio)
            limpio.startsWith("04.4") -> Pantalla.Generador
            limpio.startsWith("04.5") || limpio == "04" || limpio.startsWith("04.0") -> Pantalla.HistorialClaves(limpio)
            limpio.startsWith("04.6") -> Pantalla.Passkeys
            limpio.startsWith("04.7") -> Pantalla.Autenticador

            // Grupo 05: Sistema
            limpio.startsWith("05.1") || limpio.startsWith("11.1") || limpio.startsWith("11.2") || limpio.startsWith("11.4") || limpio == "11" -> Pantalla.Avanzada(limpio)
            limpio.startsWith("05.2") || limpio.startsWith("11.3.2") -> Pantalla.Registro(limpio)
            limpio.startsWith("05.3") || limpio.startsWith("11.3.1") -> Pantalla.AcercaDe(limpio)

            else -> Pantalla.Ajustes(limpio)
        }
        _navegandoAtras.value = false
        if (destino != _pantalla.value) {
            // Si venimos de Ajustes, guardamos el origen contextual con el ID que lo disparó
            val origen = if (_pantalla.value is Pantalla.Ajustes) {
                Pantalla.Ajustes(limpio)
            } else {
                _pantalla.value
            }
            pila.addLast(origen)
            if (pila.size > 20) pila.removeFirst()
        }
        _pantalla.value = destino
    }

    /** Cambia de pantalla vaciando la pila: se usa al abrir, cerrar y bloquear. */
    private fun irRaiz(pantalla: Pantalla) {
        _navegandoAtras.value = true
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
        val anterior = pila.removeLastOrNull()
        if (anterior != null) {
            _navegandoAtras.value = true
            _pantalla.value = anterior
            return true
        }
        val fallback = padreDe(_pantalla.value)
        if (fallback != null) {
            _navegandoAtras.value = true
            _pantalla.value = fallback
            return true
        }
        return false
    }

    /**
     * Vuelve a la pantalla previa en la pila o a la lista principal si la pila está vacía.
     */
    fun volverAtras() {
        if (!retroceder()) irRaiz(Pantalla.Lista)
    }

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
            _aviso.value = "Guardado en la bóveda"
        }
    }

    fun eliminar(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
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

    fun renombrarVarias(ids: Set<String>, nuevoTitulo: String) {
        if (ids.isEmpty()) return
        val tituloLimpio = nuevoTitulo.trim()
        if (tituloLimpio.isBlank()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.renombrarEntradas(ids, tituloLimpio) }
            Diagnostico.apuntar("lista", "${ids.size} entradas renombradas a «$tituloLimpio»")
            _aviso.value = if (ids.size == 1) "Entrada renombrada a «$tituloLimpio»" else "${ids.size} entradas renombradas a «$tituloLimpio»"
        }
    }

    // ------------------------------------------------------------- duplicados y salud
    fun eliminarDuplicadasExactasMasivo(grupos: List<com.jlnavas3.bovedalocal.data.GrupoDuplicado>) {
        val idsABorrar = grupos
            .filter { it.tipo == com.jlnavas3.bovedalocal.data.TipoDuplicado.IDENTICO }
            .flatMap { it.entradasSecundarias.map { ent -> ent.id } }
            .toSet()

        if (idsABorrar.isEmpty()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(idsABorrar) }
            Diagnostico.apuntar("duplicados", "Limpieza masiva: ${idsABorrar.size} copias idénticas movidas a la papelera")
            _aviso.value = "${idsABorrar.size} copias idénticas movidas a la papelera"
        }
    }

    fun unificarEntradas(principal: Entrada, secundarias: List<Entrada>) {
        val idsSecundarias = secundarias.map { it.id }.toSet()
        if (idsSecundarias.isEmpty()) return
        ejecutar {
            val unificada = com.jlnavas3.bovedalocal.data.AnalizadorDuplicados.fusionar(principal, secundarias)
            withContext(Dispatchers.IO) {
                repositorio.guardarEntrada(unificada)
                repositorio.eliminarEntradas(idsSecundarias)
            }
            Diagnostico.apuntar("duplicados", "Entrada unificada y ${idsSecundarias.size} duplicadas enviadas a la papelera")
            _aviso.value = "Entradas unificadas con éxito"
        }
    }

    fun actualizarContrasenaRapida(id: String, nuevaClave: String) {
        if (nuevaClave.isBlank()) return
        ejecutar {
            val entrada = withContext(Dispatchers.IO) { repositorio.entrada(id) } ?: return@ejecutar
            val entradaActualizada = entrada.copy(
                contrasena = nuevaClave,
                modificadaEn = System.currentTimeMillis()
            )
            withContext(Dispatchers.IO) { repositorio.guardarEntrada(entradaActualizada) }
            Diagnostico.apuntar("salud", "Contraseña de \"${entrada.titulo}\" actualizada de forma rápida")
            _aviso.value = "Contraseña actualizada con éxito"
        }
    }

    // ------------------------------------------------------------- papelera

    fun restaurarDeLaPapelera(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
            withContext(Dispatchers.IO) { repositorio.restaurarDeLaPapelera(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) restaurada desde la papelera a la bóveda")
            _aviso.value = "Entrada restaurada"
        }
    }

    fun borrarDefinitivamente(id: String) {
        ejecutar {
            val ent = withContext(Dispatchers.IO) { repositorio.entrada(id) }
            val tipoDesc = ent?.tipo?.etiqueta?.lowercase() ?: "entrada"
            withContext(Dispatchers.IO) { repositorio.borrarDefinitivamente(id) }
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) eliminada definitivamente de la papelera")
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
