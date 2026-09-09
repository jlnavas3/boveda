package com.pepotech.pepoboveda.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pepotech.pepoboveda.PepoBovedaApp
import com.pepotech.pepoboveda.camara.MotorCamara
import com.pepotech.pepoboveda.crypto.BiometricKeyStore
import com.pepotech.pepoboveda.data.AlmacenAjustes
import com.pepotech.pepoboveda.data.modoBiometriaActivo
import com.pepotech.pepoboveda.ui.FlujoBiometria
import com.pepotech.pepoboveda.ui.Pantalla
import com.pepotech.pepoboveda.ui.VaultViewModel
import com.pepotech.pepoboveda.ui.componentes.BotonAmbar
import com.pepotech.pepoboveda.ui.componentes.BotonBorde
import com.pepotech.pepoboveda.ui.componentes.CampoPepo
import com.pepotech.pepoboveda.ui.componentes.EtiquetaSeccion
import com.pepotech.pepoboveda.ui.componentes.TarjetaPepo
import com.pepotech.pepoboveda.ui.theme.Ambar
import com.pepotech.pepoboveda.ui.theme.Borde
import com.pepotech.pepoboveda.ui.theme.Obsidiana
import com.pepotech.pepoboveda.ui.theme.Peligro
import com.pepotech.pepoboveda.ui.theme.Superficie
import com.pepotech.pepoboveda.ui.theme.SuperficieAlta
import com.pepotech.pepoboveda.ui.theme.TextoPrincipal
import com.pepotech.pepoboveda.ui.theme.TextoSecundario
import com.pepotech.pepoboveda.util.AjustesSistema
import com.pepotech.pepoboveda.util.Biometria
import com.pepotech.pepoboveda.util.Haptica

@Composable
fun PantallaAjustes(vm: VaultViewModel, actividad: FragmentActivity) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var passwordExportacion by remember { mutableStateOf("") }
    var dialogoExportar by remember { mutableStateOf(false) }
    var dialogoImportar by remember { mutableStateOf(false) }
    var dialogoCambio by remember { mutableStateOf(false) }
    var dialogoBorrar by remember { mutableStateOf(false) }
    var dialogoImportarCsv by remember { mutableStateOf(false) }
    var actualMaestra by remember { mutableStateOf("") }
    var nuevaMaestra by remember { mutableStateOf("") }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }
    var dialogoConfirmarIcono by remember { mutableStateOf<com.pepotech.pepoboveda.ui.theme.PaletaAcento?>(null) }

    val lanzadorCrear = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            val clave = passwordExportacion
            passwordExportacion = ""
            vm.exportar(clave) { datos ->
                contexto.contentResolver.openOutputStream(uri)?.use { it.write(datos) }
            }
        }
    }

    val lanzadorAbrir = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            uriPendiente = uri
            dialogoImportar = true
        }
    }

    val lanzadorAbrirCsv = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            vm.importarCsv {
                contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("No se pudo leer el archivo")
            }
        }
    }

    // --------------------------------------------------------------- huella
    val flujo = remember { FlujoBiometria(actividad, vm.repositorio) }
    // La capacidad se vuelve a preguntar al volver a esta pantalla: si el usuario acaba
    // de registrar una huella en Android, aquí tiene que aparecer sin reiniciar nada.
    var capacidad by remember { mutableStateOf(Biometria.capacidad(contexto)) }
    LifecycleResumeEffect(Unit) {
        capacidad = Biometria.capacidad(contexto)
        onPauseOrDispose { }
    }
    val nivel = Biometria.decidirNivel(capacidad)
    val modoActivo = ajustes.modoBiometriaActivo
    var dialogoCompatible by remember { mutableStateOf<String?>(null) }

    fun tratarActivacion(resultado: FlujoBiometria.ResultadoActivacion) {
        when (resultado) {
            is FlujoBiometria.ResultadoActivacion.Activada -> {
                haptica.exito()
                vm.avisar(
                    if (resultado.modo == BiometricKeyStore.Modo.FUERTE) "Huella activada en modo fuerte"
                    else "Huella activada en modo compatible"
                )
            }
            FlujoBiometria.ResultadoActivacion.Cancelada -> vm.avisar("Huella cancelada")
            is FlujoBiometria.ResultadoActivacion.FuerteRota -> {
                haptica.error()
                dialogoCompatible = "Android acepta tu huella, pero el Keystore de este móvil la rechaza al usarla " +
                    "(fallo típico de ROMs personalizadas). Detalle técnico: ${resultado.detalle}."
            }
            is FlujoBiometria.ResultadoActivacion.Error -> {
                haptica.error()
                vm.avisar(resultado.texto)
            }
        }
    }

    // La comprobación de "bóveda abierta" la hace FlujoBiometria.activar; aquí no se repite.
    fun activarFuerte() = flujo.activar(BiometricKeyStore.Modo.FUERTE, ::tratarActivacion)

    fun ofrecerCompatible(motivo: String) {
        dialogoCompatible = motivo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
        Spacer(Modifier.height(18.dp))

        TarjetaAjuste("Seguridad", Icons.Filled.Security) {
            Spacer(Modifier.height(10.dp))
            FilaAjuste(
                titulo = "Abrir con huella",
                descripcion = when {
                    modoActivo != null -> "Activa en modo ${modoActivo.etiqueta}."
                    nivel == Biometria.Nivel.FUERTE ->
                        "La clave maestra se guarda envuelta por el Keystore, atada a tu huella de Clase 3."
                    nivel == Biometria.Nivel.COMPATIBLE ->
                        "${Biometria.explicarFaltaDeFuerte(capacidad)} Hay un modo compatible: Android comprueba la huella o el PIN y la app abre la bóveda."
                    else ->
                        "Sin huella ni PIN utilizables ahora mismo: ${Biometria.explicar(capacidad.compatible)}."
                },
                activo = ajustes.biometriaActiva,
                habilitado = nivel != Biometria.Nivel.NINGUNO || ajustes.biometriaActiva,
                alCambiar = { activar ->
                    if (activar) {
                        when (nivel) {
                            Biometria.Nivel.FUERTE -> activarFuerte()
                            Biometria.Nivel.COMPATIBLE -> ofrecerCompatible(Biometria.explicarFaltaDeFuerte(capacidad))
                            Biometria.Nivel.NINGUNO -> vm.avisar("Este móvil no ofrece huella ni PIN utilizables ahora mismo")
                        }
                    } else {
                        flujo.desactivar()
                        haptica.tic()
                        vm.avisar("Huella desactivada")
                    }
                }
            )
            if (capacidad.fuerte == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED &&
                capacidad.debil != BiometricManager.BIOMETRIC_SUCCESS
            ) {
                Spacer(Modifier.height(10.dp))
                BotonBorde("Registrar una huella en Android") {
                    if (!AjustesSistema.abrirRegistroHuella(contexto)) vm.avisar("No encuentro esa pantalla en este móvil")
                }
            }
            when {
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) ->
                    EnlaceAjuste("Cambiar a modo compatible") {
                        ofrecerCompatible("Si la huella te falla en este móvil aunque Android la acepte, el modo compatible suele funcionar.")
                    }
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) ->
                    EnlaceAjuste("Volver al modo fuerte") { activarFuerte() }
                !ajustes.biometriaActiva && nivel == Biometria.Nivel.FUERTE && Biometria.hayCompatible(capacidad) ->
                    EnlaceAjuste("Activar en modo compatible") {
                        ofrecerCompatible("Para quien ya sabe que la huella de Clase 3 le falla en este móvil.")
                    }
            }
            Spacer(Modifier.height(14.dp))
            SelectorAjuste(
                titulo = "Bloqueo automático",
                icono = Icons.Filled.Lock,
                seleccionado = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.first { it.first == ajustes.autoBloqueoSegundos }.second,
                opciones = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { (valor, etiqueta) ->
                    OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Lock)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarAutoBloqueo(valor.toInt()) }
            )
            Spacer(Modifier.height(14.dp))
            SelectorAjuste(
                titulo = "Borrado del portapapeles",
                icono = Icons.Filled.Backup,
                seleccionado = AlmacenAjustes.OPCIONES_PORTAPAPELES.first { it.first == ajustes.portapapelesSegundos }.second,
                opciones = AlmacenAjustes.OPCIONES_PORTAPAPELES.map { (valor, etiqueta) ->
                    OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Timer)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarPortapapeles(valor.toInt()) }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Cámara del escáner", Icons.Filled.CameraAlt) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Automático prueba CameraX y, si falla, pasa solo al motor compatible. Si la imagen sale negra o no lee nada, fuerza el compatible: usa la API antigua de cámara, que funciona hasta en los móviles más raros. Y si nada va, siempre puedes leer el QR desde una captura.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            SelectorAjuste(
                titulo = "Motor de cámara",
                icono = Icons.Filled.CameraAlt,
                seleccionado = MotorCamara.entries.first { it.clave == ajustes.motorCamara }.etiqueta,
                opciones = MotorCamara.entries.map { motor ->
                    OpcionAjuste(motor.clave, motor.etiqueta, Icons.Filled.CameraAlt)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarMotorCamara(valor) }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Copia de seguridad", Icons.Filled.Backup) {
            Spacer(Modifier.height(8.dp))
            Text(
                "El archivo exportado va cifrado con su propia contraseña y con Argon2id. Sin esa contraseña es ruido.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            BotonBorde("Exportar bóveda cifrada") { dialogoExportar = true }
            Spacer(Modifier.height(10.dp))
            BotonBorde("Importar bóveda cifrada") {
                PepoBovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorAbrir.launch(arrayOf("*/*"))
                } catch (e: Exception) {
                    PepoBovedaApp.salidaTerminada(contexto)
                    vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(
                "Recordarme hacer una copia",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Un aviso arriba de la lista si pasa este tiempo sin exportar. Todo se calcula en el móvil.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            SelectorAjuste(
                titulo = "Frecuencia del recordatorio",
                icono = Icons.Filled.Backup,
                seleccionado = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.first { it.first == ajustes.recordatorioExportacionDias }.second,
                opciones = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.map { (valor, etiqueta) ->
                    OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Backup)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarRecordatorioExportacion(valor.toInt()) }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Passwords de Google", Icons.Filled.Key, inicialmenteAbierta = false) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Importar archivo CSV 'Google Passwords.csv' exportado y descargado desde 'https://passwords.google.com/'.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            BotonBorde("Importar") { dialogoImportarCsv = true }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Apariencia", Icons.Filled.Palette) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Tema",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            SelectorAjuste(
                titulo = "Tema",
                icono = Icons.Filled.Palette,
                seleccionado = AlmacenAjustes.OPCIONES_TEMA.first { it.first == ajustes.temaApp }.second,
                opciones = AlmacenAjustes.OPCIONES_TEMA.map { (valor, etiqueta) ->
                    OpcionAjuste(valor, etiqueta, Icons.Filled.Palette)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTema(valor) }
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Nombre dentro de la app",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "El icono del cajón de aplicaciones siempre se llama \"Bóveda local\": Android no deja poner ahí un texto libre. Este nombre solo se ve dentro de la app.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            var nombreLocal by remember(ajustes.nombrePersonalizado) { mutableStateOf(ajustes.nombrePersonalizado) }
            CampoPepo(
                valor = nombreLocal,
                etiqueta = "Nombre (solo dentro de la app)",
                alCambiar = { nombreLocal = it }
            )
            Spacer(Modifier.height(8.dp))
            BotonBorde("Guardar nombre") {
                vm.ajustarNombrePersonalizado(nombreLocal.trim())
                haptica.tic()
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Color de la app y del icono",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "El mismo color se usa dentro de la app y en el icono del launcher, igual que en Fossify. Android cierra la app un instante al cambiar el icono; ya queda guardado antes de cerrarse.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                com.pepotech.pepoboveda.ui.theme.PaletaAcento.entries.forEach { paleta ->
                    SwatchColor(
                        color = paleta.base,
                        seleccionado = ajustes.colorAcento == paleta.clave,
                        descripcion = paleta.etiqueta
                    ) {
                        if (ajustes.colorAcento != paleta.clave) {
                            haptica.tic()
                            dialogoConfirmarIcono = paleta
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            EtiquetaSeccion("Densidad de lista")
            Spacer(Modifier.height(8.dp))
            Text(
                "Predeterminada tiene la altura normal, cómoda la reduce algo y compacta hace las filas mucho más estrechas para ver más entradas a la vez.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            SelectorAjuste(
                titulo = "Densidad de lista",
                icono = Icons.Filled.Tune,
                seleccionado = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.first { it.first == ajustes.densidadLista }.second,
                opciones = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA.map { (valor, etiqueta) ->
                    OpcionAjuste(valor, etiqueta, Icons.Filled.Tune)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarDensidadLista(valor) }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Contraseña maestra", Icons.Filled.Lock, inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            BotonBorde("Cambiar contraseña maestra") { dialogoCambio = true }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Passkeys", Icons.Filled.Fingerprint, inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Text(
                    "Tu Android admite passkeys. Activa Pepo Bóveda como proveedor de credenciales en los ajustes del sistema y gestiónalas desde la sección Passkeys.",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
                BotonBorde("Ver mis passkeys") { vm.ir(Pantalla.Passkeys) }
            } else {
                Text(
                    "Esta sección está oculta porque tu Android es anterior al 14. La API que permite a una app ser proveedora de passkeys del sistema (CredentialProviderService) llegó en Android 14; sin ella nadie puede ofrecerte passkeys de verdad, así que preferimos no fingirlo. Todo lo demás funciona igual.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Transparencia", Icons.Filled.Info, inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            BotonBorde("Audítame") { vm.ir(Pantalla.AcercaDe) }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Zona peligrosa", Icons.Filled.Warning, inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            BotonBorde("Borrar la bóveda de este dispositivo", color = Peligro) { dialogoBorrar = true }
        }

        Spacer(Modifier.height(20.dp))
        BotonBorde("Volver") { vm.volverALista() }
        Spacer(Modifier.height(40.dp))
    }

    dialogoCompatible?.let { motivo ->
        AlertDialog(
            onDismissRequest = { dialogoCompatible = null },
            containerColor = SuperficieAlta,
            title = { Text("Modo compatible", color = TextoPrincipal) },
            text = {
                Text(
                    motivo + "\n\nEn este modo la huella o el PIN los comprueba Android y la app abre la bóveda. " +
                        "La clave maestra sigue envuelta por el Keystore y no sale del móvil, pero no queda atada " +
                        "al chip como en el modo fuerte: es algo más débil. Tu contraseña maestra sigue siendo la " +
                        "única llave real, y puedes volver al modo fuerte cuando quieras.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogoCompatible = null
                    flujo.activar(BiometricKeyStore.Modo.COMPATIBLE, ::tratarActivacion)
                }) { Text("Activar modo compatible", color = Ambar) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoCompatible = null }) { Text("Ahora no", color = TextoSecundario) }
            }
        )
    }

    if (dialogoExportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Elige una contraseña solo para este archivo. Apúntala donde toque: sin ella la copia no se abre.",
            textoBoton = "Exportar",
            alConfirmar = { clave ->
                passwordExportacion = clave
                dialogoExportar = false
                PepoBovedaApp.salidaPendiente(contexto)
                try {
                    lanzadorCrear.launch("pepo-boveda-${System.currentTimeMillis()}.bvda")
                } catch (e: Exception) {
                    PepoBovedaApp.salidaTerminada(contexto)
                    passwordExportacion = ""
                    vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
                }
            },
            alCancelar = { dialogoExportar = false }
        )
    }

    if (dialogoImportar) {
        DialogoContrasena(
            titulo = "Contraseña de la copia",
            descripcion = "Escribe la contraseña con la que cifraste ese archivo.",
            textoBoton = "Importar",
            alConfirmar = { clave ->
                val uri = uriPendiente
                dialogoImportar = false
                uriPendiente = null
                if (uri != null) {
                    vm.importar(clave) {
                        contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                            ?: throw IllegalStateException("No se pudo leer el archivo")
                    }
                }
            },
            alCancelar = { dialogoImportar = false; uriPendiente = null }
        )
    }

    if (dialogoCambio) {
        AlertDialog(
            onDismissRequest = { dialogoCambio = false },
            title = { Text("Cambiar contraseña maestra") },
            text = {
                Column {
                    CampoPepo(valor = actualMaestra, etiqueta = "Contraseña actual", alCambiar = { actualMaestra = it }, esContrasena = true)
                    Spacer(Modifier.height(10.dp))
                    CampoPepo(valor = nuevaMaestra, etiqueta = "Nueva contraseña", alCambiar = { nuevaMaestra = it }, esContrasena = true)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Se vuelve a cifrar toda la bóveda y se desactiva la huella.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = nuevaMaestra.length >= 10 && actualMaestra.isNotEmpty(),
                    onClick = {
                        dialogoCambio = false
                        vm.cambiarContrasenaMaestra(actualMaestra, nuevaMaestra)
                        actualMaestra = ""
                        nuevaMaestra = ""
                    }
                ) { Text("Cambiar", color = Ambar) }
            },
            dismissButton = { TextButton(onClick = { dialogoCambio = false }) { Text("Cancelar") } }
        )
    }

    if (dialogoImportarCsv) {
        AlertDialog(
            onDismissRequest = { dialogoImportarCsv = false },
            containerColor = SuperficieAlta,
            title = { Text("Importar CSV", color = TextoPrincipal) },
            text = {
                Text(
                    "El CSV que exportan Google, Chrome, Bitwarden o LastPass va sin cifrar: cualquiera que " +
                        "lo abra ve las contraseñas en claro. Elige el archivo, se cifra al entrar en la bóveda, " +
                        "y después borra ese CSV de donde lo tengas guardado.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogoImportarCsv = false
                    PepoBovedaApp.salidaPendiente(contexto)
                    try {
                        lanzadorAbrirCsv.launch(arrayOf("text/csv", "text/comma-separated-values", "text/plain", "*/*"))
                    } catch (e: Exception) {
                        PepoBovedaApp.salidaTerminada(contexto)
                        vm.avisar("Este móvil no tiene ningún selector de archivos que pueda abrir")
                    }
                }) { Text("Elegir archivo", color = Ambar) }
            },
            dismissButton = { TextButton(onClick = { dialogoImportarCsv = false }) { Text("Cancelar", color = TextoSecundario) } }
        )
    }

    dialogoConfirmarIcono?.let { paleta ->
        AlertDialog(
            onDismissRequest = { dialogoConfirmarIcono = null },
            containerColor = SuperficieAlta,
            title = { Text("Cambiar a color \"${paleta.etiqueta}\"", color = TextoPrincipal) },
            text = {
                Text(
                    "Android cierra la app en cuanto cambia el color de su propio icono: es el sistema, no un fallo. " +
                        "Ya queda guardado antes de cerrarse. Para volver a abrirla, toca de nuevo el icono en tu launcher.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val elegido = paleta
                    dialogoConfirmarIcono = null
                    vm.ajustarColorApp(elegido)
                    actividad.finishAffinity()
                }) { Text("Cambiar y cerrar", color = Ambar) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoConfirmarIcono = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }

    if (dialogoBorrar) {
        AlertDialog(
            onDismissRequest = { dialogoBorrar = false },
            title = { Text("¿Borrar la bóveda entera?") },
            text = { Text("Se elimina el archivo cifrado y la clave de la huella. Si no tienes copia, no hay vuelta atrás.") },
            confirmButton = {
                TextButton(onClick = {
                    dialogoBorrar = false
                    vm.repositorio.borrarTodo()
                    vm.ir(Pantalla.Onboarding)
                }) { Text("Borrar todo", color = Peligro) }
            },
            dismissButton = { TextButton(onClick = { dialogoBorrar = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun EnlaceAjuste(texto: String, alPulsar: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text(
        texto,
        color = Ambar,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clickable { alPulsar() }
            .padding(vertical = 4.dp)
    )
}

@Composable
private fun DialogoContrasena(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alConfirmar: (String) -> Unit,
    alCancelar: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text(titulo) },
        text = {
            Column {
                Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                CampoPepo(valor = valor, etiqueta = "Contraseña", alCambiar = { valor = it }, esContrasena = true)
            }
        },
        confirmButton = {
            TextButton(enabled = valor.length >= 8, onClick = { alConfirmar(valor) }) {
                Text(textoBoton, color = Ambar)
            }
        },
        dismissButton = { TextButton(onClick = alCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun FilaAjuste(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean = true,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(
            checked = activo,
            enabled = habilitado,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Obsidiana,
                checkedTrackColor = Ambar,
                checkedBorderColor = Ambar,
                uncheckedThumbColor = TextoSecundario,
                uncheckedTrackColor = SuperficieAlta,
                uncheckedBorderColor = TextoSecundario
            )
        )
    }
}

@Composable
private fun TarjetaAjuste(
    titulo: String,
    icono: ImageVector,
    inicialmenteAbierta: Boolean = false,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    var abierta by remember { mutableStateOf(inicialmenteAbierta) }
    TarjetaPepo {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { abierta = !abierta }
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Icon(icono, contentDescription = titulo, tint = Ambar, modifier = Modifier.size(20.dp))
            Spacer(Modifier.size(8.dp))
            Icon(
                if (abierta) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (abierta) "Contraer" else "Expandir",
                tint = TextoSecundario
            )
        }
        if (abierta) {
            Spacer(Modifier.height(10.dp))
            contenido()
        }
    }
}

private data class OpcionAjuste(
    val valor: String,
    val texto: String,
    val icono: ImageVector
)

@Composable
private fun SelectorAjuste(
    titulo: String,
    icono: ImageVector,
    seleccionado: String,
    opciones: List<OpcionAjuste>,
    alSeleccionar: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(16.dp)
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(forma)
                .background(Superficie)
                .border(1.dp, if (abierto) Ambar else Borde, forma)
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = if (abierto) Ambar else TextoSecundario, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = if (abierto) Ambar else TextoSecundario, style = MaterialTheme.typography.labelMedium)
                Text(seleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir $titulo",
                tint = TextoSecundario
            )
        }
        DropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false },
            modifier = Modifier.background(SuperficieAlta)
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            opcion.icono,
                            contentDescription = null,
                            tint = if (opcion.texto == seleccionado) Ambar else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            opcion.texto,
                            color = if (opcion.texto == seleccionado) Ambar else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        if (opcion.texto == seleccionado) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = Ambar, modifier = Modifier.size(18.dp))
                        }
                    },
                    onClick = {
                        alSeleccionar(opcion.valor)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SwatchColor(color: Color, seleccionado: Boolean, descripcion: String, alPulsar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color)
            .border(width = if (seleccionado) 3.dp else 0.dp, color = TextoPrincipal, shape = CircleShape)
            .clickable { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(Icons.Filled.Check, contentDescription = descripcion, tint = Obsidiana)
        }
    }
}
