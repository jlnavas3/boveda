package com.jlnavas3.bovedalocal.ui.pantallas

import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.jlnavas3.bovedalocal.data.AjustesApp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

import androidx.compose.material.icons.automirrored.filled.Sort
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.PepoBovedaApp
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegablePepo
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepoDesplegable
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

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
    var mostrarDialogoBorradoManual by remember { mutableStateOf(false) }
    var actualMaestra by remember { mutableStateOf("") }
    var nuevaMaestra by remember { mutableStateOf("") }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }
    var dialogoConfirmarIcono by remember { mutableStateOf<com.jlnavas3.bovedalocal.ui.theme.PaletaAcento?>(null) }

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
            var nombreArchivo = uri.lastPathSegment ?: "Google Passwords.csv"
            try {
                contexto.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1 && cursor.moveToFirst()) {
                        nombreArchivo = cursor.getString(nameIndex)
                    }
                }
            } catch (_: Exception) {}

            vm.importarCsv({
                contexto.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("No se pudo leer el archivo")
            }) { nuevas ->
                if (nuevas > 0) {
                    vm.registrarCsvGoogleImportado(
                        ruta = nombreArchivo,
                        uri = uri.toString(),
                        cuentas = nuevas
                    )
                }
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
        CabeceraPantalla(
            titulo = "Ajustes",
            subtitulo = "Configuración y personalización de la bóveda",
            alVolver = { vm.volverAtras() }
        )

        TarjetaAjuste("Seguridad", Icons.Filled.Security, "Huella, bloqueo automático y borrado del portapapeles.") {
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
                BotonColorido(
                    texto = "Registrar una huella en Android",
                    color = ColorSeguridad,
                    icono = Icons.Filled.Fingerprint
                ) {
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
            Spacer(Modifier.height(8.dp))
            androidx.compose.material3.Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                shape = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Protección de pantalla permanente (FLAG_SECURE)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = TextoPrincipal
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "La bóveda bloquea permanentemente las capturas de pantalla y oculta la vista previa en aplicaciones recientes de Android para garantizar la máxima privacidad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            SelectorAjuste(
                titulo = "Bloqueo automático",
                icono = Icons.Filled.Lock,
                seleccionado = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.firstOrNull { it.first == ajustes.autoBloqueoSegundos }?.second ?: "30 segundos",
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

        TarjetaAjuste(
            "Atajo en Ajustes Rápidos (Tile)",
            Icons.Filled.Key,
            "Genera credenciales seguras al instante desde la cortina de notificaciones de Android."
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Añade el tile 'Generador Rápido' editando los botones de la barra de notificaciones para generar contraseñas con un solo toque sin abrir la bóveda.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            SelectorAjuste(
                titulo = "Modo de generación",
                icono = Icons.Filled.Tune,
                seleccionado = if (ajustes.tileModo == "patron") "Por patrón personalizado" else "Longitud de caracteres",
                opciones = listOf(
                    OpcionAjuste("longitud", "Longitud de caracteres", Icons.Filled.Key),
                    OpcionAjuste("patron", "Por patrón personalizado", Icons.Filled.Tune)
                ),
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTileModo(valor) }
            )
            Spacer(Modifier.height(12.dp))
            if (ajustes.tileModo == "longitud") {
                SelectorAjuste(
                    titulo = "Longitud de la clave",
                    icono = Icons.Filled.Key,
                    seleccionado = "${ajustes.tileLongitud} caracteres",
                    opciones = AlmacenAjustes.OPCIONES_TILE_LONGITUD.map { (valor, etiqueta) ->
                        OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Key)
                    },
                    alSeleccionar = { valor -> haptica.tic(); vm.ajustarTileLongitud(valor.toInt()) }
                )
            } else {
                CampoPepo(
                    valor = ajustes.tilePatron,
                    etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX)",
                    alCambiar = { vm.ajustarTilePatron(it) },
                    monoespaciada = true
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "X: alfanum. mayúscula | A: letra mayúscula | a: minúscula | 9: dígito | w: palabra Diceware",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(12.dp))
            FilaAjuste(
                titulo = "Copiar al portapapeles",
                descripcion = "Copia la clave generada directamente al pulsar el tile",
                activo = ajustes.tileCopiarPortapapeles,
                alCambiar = {
                    haptica.toque()
                    vm.ajustarTileCopiarPortapapeles(it)
                    Toast.makeText(
                        contexto,
                        if (it) "Copia al portapapeles activada para el tile" else "Copia al portapapeles desactivada para el tile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            Spacer(Modifier.height(10.dp))
            FilaAjuste(
                titulo = "Mostrar aviso emergente (Toast)",
                descripcion = "Muestra una confirmación rápida al generar",
                activo = ajustes.tileMostrarToast,
                alCambiar = {
                    haptica.toque()
                    vm.ajustarTileMostrarToast(it)
                    Toast.makeText(
                        contexto,
                        if (it) "Avisos Toast activados para el tile" else "Avisos Toast desactivados para el tile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            Spacer(Modifier.height(10.dp))
            FilaAjuste(
                titulo = "Microvibración háptica",
                descripcion = "Confirma con una vibración táctil al generar",
                activo = ajustes.tileHaptica,
                alCambiar = {
                    haptica.toque()
                    vm.ajustarTileHaptica(it)
                    Toast.makeText(
                        contexto,
                        if (it) "Microvibración háptica activada para el tile" else "Microvibración háptica desactivada para el tile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Cámara del escáner", Icons.Filled.CameraAlt, "Elige cómo se leen los códigos QR de autenticación.") {
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

        TarjetaAjuste("Copia de seguridad", Icons.Filled.Backup, "Exporta, importa y configura avisos para no olvidar tus copias.") {
            Spacer(Modifier.height(8.dp))
            Text(
                "El archivo exportado va cifrado con su propia contraseña y con Argon2id. Sin esa contraseña es ruido.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            BotonColorido(
                texto = "Exportar bóveda cifrada",
                color = ColorExportacion,
                icono = Icons.Filled.FileDownload
            ) { dialogoExportar = true }
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Importar bóveda cifrada",
                color = ColorExportacion,
                icono = Icons.Filled.FileUpload
            ) {
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

        TarjetaAjuste("Autenticador 2FA", Icons.Filled.Timer, "Configura los valores usados al introducir una clave TOTP manualmente.") {
            Spacer(Modifier.height(8.dp))
            Text(
                "Los QR otpauth traen sus propios parámetros y siempre tienen prioridad. Estos valores solo se usan cuando pegas una clave Base32 sin QR.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            SelectorAjuste(
                titulo = "Dígitos predeterminados",
                icono = Icons.Filled.Timer,
                seleccionado = "${ajustes.totpManualDigitos} dígitos",
                opciones = listOf(6, 7, 8).map { valor ->
                    OpcionAjuste(valor.toString(), "$valor dígitos", Icons.Filled.Timer)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualDigitos(valor.toInt()) }
            )
            Spacer(Modifier.height(10.dp))
            SelectorAjuste(
                titulo = "Período predeterminado",
                icono = Icons.Filled.Timer,
                seleccionado = "${ajustes.totpManualPeriodo} segundos",
                opciones = listOf(30, 60, 90).map { valor ->
                    OpcionAjuste(valor.toString(), "$valor segundos", Icons.Filled.Timer)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualPeriodo(valor.toInt()) }
            )
            Spacer(Modifier.height(10.dp))
            SelectorAjuste(
                titulo = "Algoritmo predeterminado",
                icono = Icons.Filled.Security,
                seleccionado = ajustes.totpManualAlgoritmo.removePrefix("Hmac"),
                opciones = listOf("HmacSHA1", "HmacSHA256", "HmacSHA512").map { valor ->
                    OpcionAjuste(valor, valor.removePrefix("Hmac"), Icons.Filled.Security)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualAlgoritmo(valor) }
            )
            Spacer(Modifier.height(10.dp))
            FilaAjuste(
                titulo = "Separar códigos de 6 dígitos",
                descripcion = "Muestra 123 456 en lugar de 123456.",
                activo = ajustes.totpSepararDigitos,
                alCambiar = { haptica.tic(); vm.ajustarTotpSepararDigitos(it) }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Los códigos dependen de la hora del teléfono. Activa fecha y hora automáticas de Android si un código no es aceptado.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Passwords de Google", Icons.Filled.Key, "Importa un CSV exportado desde Google Password Manager.", inicialmenteAbierta = ajustes.csvGoogleRuta.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Importar archivo CSV 'Google Passwords.csv' exportado y descargado desde 'https://passwords.google.com/'.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            BotonColorido(
                texto = "Importar contraseñas de Google",
                color = ColorExportacion,
                icono = Icons.Filled.FileUpload
            ) { dialogoImportarCsv = true }

            if (ajustes.csvGoogleRuta.isNotBlank()) {
                Spacer(Modifier.height(14.dp))
                if (ajustes.csvGoogleEliminado) {
                    androidx.compose.material3.Surface(
                        color = ColorSalud.copy(alpha = 0.08f),
                        shape = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ColorSalud.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = ColorSalud,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Archivo CSV eliminado de forma segura",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = TextoPrincipal
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        "El archivo original '${ajustes.csvGoogleRuta}' ha sido eliminado. Se encuentran seguras y cifradas ${ajustes.csvGoogleCuentas} contraseñas en tu bóveda.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundario
                                    )
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            BotonColorido(
                                texto = "Entendido / Cerrar aviso",
                                color = ColorSalud,
                                icono = Icons.Filled.Check
                            ) {
                                haptica.tic()
                                vm.descartarAvisoCsvGoogle()
                            }
                        }
                    }
                } else {
                    androidx.compose.material3.Surface(
                        color = Peligro.copy(alpha = 0.08f),
                        shape = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Peligro.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Filled.Security,
                                    contentDescription = null,
                                    tint = Peligro,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "Archivo CSV sin cifrar en el almacenamiento",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = TextoPrincipal
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "Se importaron con éxito ${ajustes.csvGoogleCuentas} contraseñas a la bóveda. El archivo original sin cifrar sigue guardado en tu teléfono:\n\n📁 ${ajustes.csvGoogleRuta}\n\nCualquier persona o app con acceso a tus archivos puede leer tus contraseñas en texto claro. Se recomienda encarecidamente eliminarlo ahora.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextoSecundario
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            BotonColorido(
                                texto = "Eliminar archivo CSV",
                                color = Peligro,
                                icono = Icons.Filled.Delete
                            ) {
                                haptica.toque()
                                var borrado = false
                                try {
                                    if (ajustes.csvGoogleUri.isNotBlank()) {
                                        val docUri = Uri.parse(ajustes.csvGoogleUri)
                                        borrado = DocumentsContract.deleteDocument(contexto.contentResolver, docUri)
                                    }
                                } catch (_: Exception) {
                                    borrado = false
                                }
                                if (borrado) {
                                    haptica.exito()
                                    vm.marcarCsvGoogleEliminado(true)
                                    Toast.makeText(contexto, "Archivo CSV eliminado del almacenamiento", Toast.LENGTH_SHORT).show()
                                } else {
                                    haptica.error()
                                    mostrarDialogoBorradoManual = true
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { vm.descartarAvisoCsvGoogle() }) {
                                    Text("Descartar aviso", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Apariencia", Icons.Filled.Palette, "Tema, color, nombre, densidad y organización de la lista.") {
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
            BotonColorido(
                texto = "Guardar nombre",
                color = ColorAcento
            ) {
                vm.ajustarNombrePersonalizado(nombreLocal.trim())
                haptica.tic()
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Personalización de colores y tema",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Configura independientemente los colores de íconos internos, títulos, tarjetas, acento y el ícono del launcher con selectores interactivos en tiempo real.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar colores",
                color = ColorAcento,
                icono = Icons.Filled.Palette
            ) {
                haptica.tic()
                vm.ir(Pantalla.Tema)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Personalización de bordes y formas",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Configura en tiempo real curvatura de esquinas, grosores de trazo, estilos perimetrales y espaciados entre componentes con controles deslizantes.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar bordes y formas",
                color = ColorAcento,
                icono = Icons.Filled.SquareFoot
            ) {
                haptica.tic()
                vm.ir(Pantalla.Formas)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Personalización de tipografía y textos",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Ajusta en tiempo real tamaño de fuentes, peso de texto, inclinación cursiva, kerning, interlineado y familias tipográficas a tu gusto.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Personalizar tipografía y textos",
                color = ColorAcento,
                icono = Icons.Filled.TextFields
            ) {
                haptica.tic()
                vm.ir(Pantalla.Tipografia)
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
            Spacer(Modifier.height(16.dp))
            FilaAjuste(
                titulo = "Agrupar cuentas por sitio",
                descripcion = "Combina en un grupo plegable las cuentas que pertenecen al mismo servicio o subdominio.",
                activo = ajustes.agruparPorSitio,
                alCambiar = { haptica.tic(); vm.ajustarAgruparPorSitio(it) }
            )
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste(
            "Abecedario lateral",
            Icons.AutoMirrored.Filled.Sort,
            "Ola interactiva estilo Niagara, escala de letras y vista previa en tiempo real.",
            inicialmenteAbierta = false
        ) {
            FilaAjuste(
                titulo = "Mostrar abecedario en la lista",
                descripcion = "Franja de navegación rápida por letras en el extremo derecho del listado.",
                activo = ajustes.mostrarIndiceAlfabetico,
                alCambiar = { haptica.tic(); vm.ajustarMostrarIndiceAlfabetico(it) }
            )

            if (ajustes.mostrarIndiceAlfabetico) {
                Spacer(Modifier.height(14.dp))
                VistaPreviaIndice(ajustes)
                Spacer(Modifier.height(14.dp))

                FilaAjuste(
                    titulo = "Efecto de ola Niagara",
                    descripcion = "Curvatura dinámica continua que sigue el movimiento del dedo.",
                    activo = ajustes.indiceEfectoOla,
                    alCambiar = { haptica.tic(); vm.ajustarIndiceEfectoOla(it) }
                )

                if (ajustes.indiceEfectoOla) {
                    Spacer(Modifier.height(10.dp))
                    val amplitud = ajustes.indiceAmplitudOlaDp
                    val textoAmplitud = when {
                        amplitud <= 0f -> "Recto (sin ola)"
                        amplitud < 45f -> "Curva sutil"
                        amplitud < 85f -> "Equilibrado"
                        amplitud <= 115f -> "Ola amplia (predeterminado)"
                        else -> "Super exagerado"
                    }
                    SliderConEtiqueta(
                        titulo = "Amplitud de la curvatura: ${amplitud.toInt()} dp",
                        subtitulo = textoAmplitud,
                        valor = amplitud,
                        rango = 0f..130f,
                        alCambiar = { vm.ajustarIndiceAmplitudOlaDp(it) }
                    )

                    Spacer(Modifier.height(8.dp))
                    val radio = ajustes.indiceRadioOlaDp
                    val textoRadio = when {
                        radio < 140f -> "Concentrado"
                        radio < 220f -> "Arco medio"
                        radio <= 265f -> "Arco amplio (predeterminado)"
                        else -> "Abarca todo el abecedario"
                    }
                    SliderConEtiqueta(
                        titulo = "Alcance vertical: ${radio.toInt()} dp",
                        subtitulo = textoRadio,
                        valor = radio,
                        rango = 80f..300f,
                        alCambiar = { vm.ajustarIndiceRadioOlaDp(it) }
                    )

                    Spacer(Modifier.height(8.dp))
                    val escala = ajustes.indiceEscalaLetras
                    val textoEscala = when {
                        escala <= 1.05f -> "Sin aumento"
                        escala < 1.45f -> "Crecimiento suave"
                        escala <= 1.8f -> "Letras destacadas (predeterminado)"
                        escala < 2.3f -> "Letras grandes en cresta"
                        else -> "Letras gigantescas"
                    }
                    val escalaStr = "%.1f".format(java.util.Locale.US, escala)
                    SliderConEtiqueta(
                        titulo = "Aumento de letras en la cresta: ${escalaStr}x",
                        subtitulo = textoEscala,
                        valor = escala,
                        rango = 1.0f..2.6f,
                        alCambiar = { vm.ajustarIndiceEscalaLetras(it) }
                    )
                }

                Spacer(Modifier.height(12.dp))
                FilaAjuste(
                    titulo = "Círculo aumentado en la cresta",
                    descripcion = "Muestra la letra activa sin borde proyectada hacia el centro.",
                    activo = ajustes.indiceMostrarCirculo,
                    alCambiar = { haptica.tic(); vm.ajustarIndiceMostrarCirculo(it) }
                )

                if (ajustes.indiceMostrarCirculo) {
                    Spacer(Modifier.height(8.dp))
                    val offset = ajustes.indiceOffsetCirculoDp
                    val textoOffset = when {
                        offset < 80f -> "Cerca de la franja"
                        offset < 130f -> "Proyección flotante"
                        else -> "Casi a media pantalla"
                    }
                    SliderConEtiqueta(
                        titulo = "Desplazamiento del círculo: ${offset.toInt()} dp",
                        subtitulo = textoOffset,
                        valor = offset,
                        rango = 50f..160f,
                        alCambiar = { vm.ajustarIndiceOffsetCirculoDp(it) }
                    )
                }

                Spacer(Modifier.height(12.dp))
                FilaAjuste(
                    titulo = "Vibración háptica al deslizar",
                    descripcion = "Respuesta táctil sutil en cada letra seleccionada.",
                    activo = ajustes.indiceHaptica,
                    alCambiar = { haptica.tic(); vm.ajustarIndiceHaptica(it) }
                )

                Spacer(Modifier.height(10.dp))
                val anchoTactil = ajustes.indiceAnchoTactilDp
                val textoAnchoTactil = when {
                    anchoTactil < 35f -> "Estrecho (solo sobre letras)"
                    anchoTactil <= 55f -> "Estándar cómodo (predeterminado)"
                    anchoTactil < 75f -> "Área amplia"
                    else -> "Extremadamente amplio"
                }
                SliderConEtiqueta(
                    titulo = "Zona táctil de arrastre: ${anchoTactil.toInt()} dp",
                    subtitulo = textoAnchoTactil,
                    valor = anchoTactil,
                    rango = 26f..90f,
                    alCambiar = { vm.ajustarIndiceAnchoTactilDp(it) }
                )

                Spacer(Modifier.height(10.dp))
                val tono = ajustes.indiceTonoLetras
                val textoTono = when {
                    tono < 25f -> "Muy oscuro / discreto"
                    tono < 45f -> "Oscuro suave"
                    tono <= 65f -> "Equilibrado (predeterminado)"
                    tono < 85f -> "Claro y nítido"
                    else -> "Máximo brillo / blanco puro"
                }
                SliderConEtiqueta(
                    titulo = "Color de las letras: ${tono.toInt()}%",
                    subtitulo = textoTono,
                    valor = tono,
                    rango = 10f..100f,
                    alCambiar = { vm.ajustarIndiceTonoLetras(it) }
                )

                Spacer(Modifier.height(14.dp))
                BotonBorde(
                    texto = "Restablecer valores por defecto",
                    icono = Icons.Filled.Refresh,
                    alPulsar = {
                        haptica.toque()
                        vm.restablecerAjustesIndiceAlfabetico()
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Contraseña maestra", Icons.Filled.Lock, "Cambia la clave que protege toda la bóveda.", inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Cambiar contraseña maestra",
                color = ColorSeguridad,
                icono = Icons.Filled.Lock
            ) { dialogoCambio = true }
        }

        Spacer(Modifier.height(16.dp))

        TarjetaAjuste("Zona peligrosa", Icons.Filled.Warning, "Borra de forma irreversible la bóveda de este dispositivo.", inicialmenteAbierta = false) {
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Borrar bóveda definitivamente",
                color = ColorPapelera,
                icono = Icons.Filled.Delete
            ) { dialogoBorrar = true }
        }

        Spacer(Modifier.height(32.dp))
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
                }) { Text("Activar modo compatible", color = ColorAcento) }
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
                ) { Text("Cambiar", color = ColorAcento) }
            },
            dismissButton = { TextButton(onClick = { dialogoCambio = false }) { Text("Cancelar") } }
        )
    }

    if (dialogoImportarCsv) {
        AlertDialog(
            onDismissRequest = { dialogoImportarCsv = false },
            containerColor = ColorTarjetas,
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
                }) { Text("Elegir archivo", color = ColorAcento) }
            },
            dismissButton = { TextButton(onClick = { dialogoImportarCsv = false }) { Text("Cancelar", color = TextoSecundario) } }
        )
    }

    if (mostrarDialogoBorradoManual) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoBorradoManual = false },
            containerColor = ColorTarjetas,
            icon = { Icon(Icons.Filled.Warning, contentDescription = null, tint = Peligro) },
            title = { Text("Eliminar archivo sin cifrar", color = TextoPrincipal, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
            text = {
                Text(
                    "Por directivas de seguridad de Android, la aplicación no cuenta con permisos directos del sistema de archivos para borrar el documento automáticamente.\n\n" +
                    "Te recomendamos FUERTEMENTE abrir la app 'Archivos' o 'Descargas' de tu teléfono y eliminar manualmente el archivo:\n\n" +
                    "📁 ${ajustes.csvGoogleRuta}\n\n" +
                    "Este archivo contiene todas tus contraseñas de Google en texto plano y no debe permanecer en el almacenamiento del dispositivo.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoBorradoManual = false
                    vm.descartarAvisoCsvGoogle()
                }) {
                    Text("Ya lo he eliminado / Entendido", color = ColorAcento)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoBorradoManual = false }) {
                    Text("Cerrar", color = TextoSecundario)
                }
            }
        )
    }

    dialogoConfirmarIcono?.let { paleta ->
        AlertDialog(
            onDismissRequest = { dialogoConfirmarIcono = null },
            containerColor = ColorTarjetas,
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
                }) { Text("Cambiar y cerrar", color = ColorAcento) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoConfirmarIcono = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }

    if (dialogoBorrar) {
        AlertDialog(
            onDismissRequest = { dialogoBorrar = false },
            containerColor = ColorTarjetas,
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
        color = ColorAcento,
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
        containerColor = ColorTarjetas,
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
                Text(textoBoton, color = ColorAcento)
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
                checkedThumbColor = ColorSobreAcento,
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
    descripcion: String,
    inicialmenteAbierta: Boolean = false,
    contenido: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    TarjetaPepoDesplegable(
        titulo = titulo,
        icono = icono,
        descripcion = descripcion,
        inicialmenteAbierta = inicialmenteAbierta,
        contenido = contenido
    )
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
    val forma = FormaCampo
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = if (abierto) ColorIconosInternos else TextoSecundario, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelMedium)
                Text(seleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir $titulo",
                tint = TextoSecundario
            )
        }
        MenuDesplegablePepo(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            opcion.icono,
                            contentDescription = null,
                            tint = if (opcion.texto == seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Text(
                            opcion.texto,
                            color = if (opcion.texto == seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        if (opcion.texto == seleccionado) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp))
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
            Icon(Icons.Filled.Check, contentDescription = descripcion, tint = colorContraste(color))
        }
    }
}

@Composable
private fun VistaPreviaIndice(ajustes: AjustesApp) {
    var letraSeleccionada by remember { mutableStateOf<Char?>('G') }
    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar",
            "Spotify" to "Música y podcasts",
            "Twitter" to "@usuario_pepo"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(SuperficieAlta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "VISTA PREVIA EN VIVO",
                color = Ambar,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Toca y desliza en el borde 👉",
                color = TextoSecundario,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(FormaCampo)
                .background(Superficie)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // Entradas simuladas a la izquierda
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 46.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                mockItems.forEach { (nombre, detalle) ->
                    val coincide = letraSeleccionada != null && nombre.first().uppercaseChar() == letraSeleccionada
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaPequena)
                            .background(if (coincide) Ambar.copy(alpha = 0.18f) else SuperficieAlta)
                            .then(
                                if (coincide) Modifier.border(1.dp, Ambar, FormaPequena) else Modifier
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (coincide) Ambar else Borde),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                nombre.take(1),
                                color = if (coincide) ColorSobreAcento else TextoPrincipal,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                nombre,
                                color = if (coincide) Ambar else TextoPrincipal,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1
                            )
                            Text(detalle, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                    }
                }
            }

            // Indice interactivo con la configuracion en tiempo real
            IndiceAlfabetico(
                alSeleccionarLetra = { letra -> letraSeleccionada = letra },
                efectoOla = ajustes.indiceEfectoOla,
                amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
                radioOlaDp = ajustes.indiceRadioOlaDp,
                escalaMaximaLetras = ajustes.indiceEscalaLetras,
                mostrarCirculo = ajustes.indiceMostrarCirculo,
                offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
                hapticaActiva = ajustes.indiceHaptica,
                anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
                tonoLetras = ajustes.indiceTonoLetras,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun SliderConEtiqueta(
    titulo: String,
    subtitulo: String,
    valor: Float,
    rango: ClosedFloatingPointRange<Float>,
    pasos: Int = 0,
    alCambiar: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)) {
        Text(
            text = titulo,
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        if (subtitulo.isNotBlank()) {
            Spacer(Modifier.height(3.dp))
            Text(
                text = subtitulo,
                color = Ambar,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }
        Spacer(Modifier.height(2.dp))
        Slider(
            value = valor,
            onValueChange = alCambiar,
            valueRange = rango,
            steps = pasos,
            colors = SliderDefaults.colors(
                thumbColor = Ambar,
                activeTrackColor = Ambar,
                inactiveTrackColor = SuperficieAlta
            )
        )
    }
}
