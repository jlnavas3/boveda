package com.pepotech.pepoboveda.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pepotech.pepoboveda.crypto.OtpAuth
import com.pepotech.pepoboveda.crypto.Zeroizar
import com.pepotech.pepoboveda.data.AjustesApp
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.EstadoBoveda
import com.pepotech.pepoboveda.data.FrenoIntentos
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.data.VaultRepository
import com.pepotech.pepoboveda.util.Diagnostico
import com.pepotech.pepoboveda.util.Portapapeles
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
    object AcercaDe : Pantalla
    object SaludBoveda : Pantalla
    object Papelera : Pantalla
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
                registrarInteraccion()
                irRaiz(Pantalla.Lista)
                alTerminar(true)
            } catch (e: Exception) {
                apuntarFallo()
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
        repositorio.bloquear()
        // El rato que pase bloqueada no cuenta como inactividad.
        registrarInteraccion()
        irRaiz(if (repositorio.existeBoveda) Pantalla.Desbloqueo else Pantalla.Onboarding)
    }

    // ---------------------------------------------------------------- entradas

    fun entradasVisibles(entradas: List<Entrada>): List<Entrada> {
        val texto = _busqueda.value.trim().lowercase()
        return entradas
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
            .sortedWith(compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() })
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
            withContext(Dispatchers.IO) { repositorio.guardarEntrada(entrada) }
            _aviso.value = "Guardado en la bóveda"
        }
    }

    fun eliminar(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntrada(id) }
            irRaiz(Pantalla.Lista)
            _aviso.value = "Movida a la papelera"
        }
    }

    fun eliminarVarias(ids: Set<String>) {
        if (ids.isEmpty()) return
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.eliminarEntradas(ids) }
            _aviso.value = if (ids.size == 1) "Entrada movida a la papelera" else "${ids.size} entradas movidas a la papelera"
        }
    }

    // ------------------------------------------------------------- papelera

    fun restaurarDeLaPapelera(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.restaurarDeLaPapelera(id) }
            _aviso.value = "Entrada restaurada"
        }
    }

    fun borrarDefinitivamente(id: String) {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.borrarDefinitivamente(id) }
            _aviso.value = "Borrada para siempre"
        }
    }

    fun vaciarPapelera() {
        ejecutar {
            withContext(Dispatchers.IO) { repositorio.vaciarPapelera() }
            _aviso.value = "Papelera vaciada"
        }
    }

    fun alternarFavorito(id: String) {
        ejecutar { withContext(Dispatchers.IO) { repositorio.alternarFavorito(id) } }
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
        ir(Pantalla.Autenticador)
        return true
    }

    // ------------------------------------------------------------ portapapeles

    fun copiar(etiqueta: String, valor: String, sensible: Boolean) {
        val contexto = getApplication<Application>()
        Portapapeles.copiarSensible(contexto, etiqueta, valor)
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
        }
    }

    // --------------------------------------------------------------- ajustes

    fun ajustarAutoBloqueo(segundos: Int) = repositorio.ajustes.actualizar { it.copy(autoBloqueoSegundos = segundos) }

    fun ajustarPortapapeles(segundos: Int) = repositorio.ajustes.actualizar { it.copy(portapapelesSegundos = segundos) }

    fun ajustarModoGrabacion(activo: Boolean) = repositorio.ajustes.actualizar { it.copy(modoGrabacion = activo) }

    fun ajustarMotorCamara(clave: String) = repositorio.ajustes.actualizar { it.copy(motorCamara = clave) }

    fun ajustarNombrePersonalizado(nombre: String) =
        repositorio.ajustes.actualizar { it.copy(nombrePersonalizado = nombre) }

    /** Cambia el acento del tema Y el color del icono del launcher (mismo dibujo, otro degradado). */
    fun ajustarColorApp(paleta: com.pepotech.pepoboveda.ui.theme.PaletaAcento) {
        com.pepotech.pepoboveda.ui.theme.aplicarPaletaAcento(paleta)
        repositorio.ajustes.actualizar { it.copy(colorAcento = paleta.clave) }
        com.pepotech.pepoboveda.util.CambiadorIcono.aplicar(contextoApp, paleta.clave)
    }

    fun ajustarTema(clave: String) = repositorio.ajustes.actualizar { it.copy(temaApp = clave) }

    fun ajustarRecordatorioExportacion(dias: Int) =
        repositorio.ajustes.actualizar { it.copy(recordatorioExportacionDias = dias) }

    fun ajustarDensidadLista(clave: String) =
        repositorio.ajustes.actualizar { it.copy(densidadLista = clave) }

    fun ajustarTotpManualDigitos(digitos: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualDigitos = digitos) }

    fun ajustarTotpManualPeriodo(periodo: Int) =
        repositorio.ajustes.actualizar { it.copy(totpManualPeriodo = periodo) }

    fun ajustarTotpManualAlgoritmo(algoritmo: String) =
        repositorio.ajustes.actualizar { it.copy(totpManualAlgoritmo = algoritmo) }

    fun ajustarTotpSepararDigitos(separar: Boolean) =
        repositorio.ajustes.actualizar { it.copy(totpSepararDigitos = separar) }

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
                _aviso.value = "Bóveda exportada y cifrada"
            } catch (e: Exception) {
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
                _aviso.value = "Importadas $nuevas entradas"
            } catch (e: Exception) {
                _error.value = "No se pudo importar: contraseña incorrecta o archivo inválido"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importarCsv(lector: () -> ByteArray) {
        ejecutar {
            try {
                val datos = withContext(Dispatchers.IO) { lector() }
                val nuevas = withContext(Dispatchers.Default) { repositorio.importarCsv(datos) }
                _aviso.value = "Importadas $nuevas entradas desde CSV"
            } catch (e: Exception) {
                _error.value = "No se pudo importar el CSV: ${e.message ?: "formato no reconocido"}"
            }
        }
    }

    fun cambiarContrasenaMaestra(actual: String, nueva: String) {
        ejecutar {
            val viejaChars = actual.toCharArray()
            val nuevaChars = nueva.toCharArray()
            try {
                val correcta = withContext(Dispatchers.Default) { repositorio.verificarContrasena(viejaChars) }
                if (!correcta) {
                    _error.value = "La contraseña actual no es correcta"
                    return@ejecutar
                }
                withContext(Dispatchers.Default) { repositorio.cambiarContrasenaMaestra(nuevaChars) }
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
