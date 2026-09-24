package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.ContentValues
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.io.File

private data class CategoriaExportacion(
    val id: String,
    val etiqueta: String,
    val filtro: (Entrada) -> Boolean
)

/**
 * Pantalla de exportación selectiva que permite marcar entradas por categorías y
 * exportarlas a un archivo .bvda cifrado con contraseña.
 */
@Composable
fun PantallaExportarSelectivo(
    vm: VaultViewModel,
    seccionInicial: String = "todos"
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val estadoBoveda by vm.estado.collectAsStateWithLifecycle()

    val entradasTotales = remember(estadoBoveda) {
        val desbloqueada = estadoBoveda as? EstadoBoveda.Desbloqueada ?: return@remember emptyList()
        desbloqueada.entradas.filter { it.eliminadaEn == 0L }
    }

    // Definición de categorías
    val todasCategorias = remember {
        listOf(
            CategoriaExportacion("todos", "Todos") { true },
            CategoriaExportacion("passkeys", "Passkeys") { it.tipo == TipoEntrada.PASSKEY || it.passkey != null },
            CategoriaExportacion("2fa", "2FA (TOTP)") { !it.secretoTotp.isNullOrBlank() },
            CategoriaExportacion("login", "Cuentas") { it.tipo == TipoEntrada.LOGIN },
            CategoriaExportacion("tarjetas", "Tarjetas") { it.tipo == TipoEntrada.TARJETA },
            CategoriaExportacion("identidades", "Identidades") { it.tipo == TipoEntrada.IDENTIDAD },
            CategoriaExportacion("wifi", "Wi-Fi") { it.tipo == TipoEntrada.WIFI },
            CategoriaExportacion("servidores", "Servidores") { it.tipo == TipoEntrada.SERVIDOR },
            CategoriaExportacion("wallet", "Crypto Wallets") { it.tipo == TipoEntrada.WALLET },
            CategoriaExportacion("notas", "Notas") { it.tipo == TipoEntrada.NOTA }
        )
    }

    // Filtro dinámico: solo mostrar categorías que tengan al menos 1 entrada
    val categoriasDisponibles = remember(entradasTotales, todasCategorias) {
        todasCategorias.filter { cat ->
            if (cat.id == "todos") entradasTotales.isNotEmpty()
            else entradasTotales.any(cat.filtro)
        }
    }

    var seccionActiva by remember(seccionInicial, categoriasDisponibles) {
        val existe = categoriasDisponibles.any { it.id == seccionInicial }
        mutableStateOf(if (existe) seccionInicial else (categoriasDisponibles.firstOrNull()?.id ?: "todos"))
    }

    // Estado para campo de búsqueda en la barra superior
    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Persistencia de IDs seleccionados en memoria a través de todas las pestañas
    val idsSeleccionados = remember { mutableStateListOf<String>() }

    var dialogoExportar by remember { mutableStateOf(false) }
    var passwordAUsar by remember { mutableStateOf("") }

    val lanzadorGuardarBvda = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri != null) {
            vm.exportarSelectivo(idsSeleccionados.toSet(), passwordAUsar) { bytes ->
                contexto.contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
            }
        }
    }

    fun sanearNombreArchivo(nombre: String): String {
        val limpio = nombre.replace(Regex("[\\\\/:*?\"<>|]"), "_").trim()
        val conExt = if (limpio.endsWith(".bvda", ignoreCase = true)) limpio else "$limpio.bvda"
        return conExt.ifBlank { "01-selectivo-backup.bvda" }
    }

    fun guardarDirectoEnAutoBackup(nombreBruto: String, password: String) {
        val nombreArchivo = sanearNombreArchivo(nombreBruto)
        vm.exportarSelectivo(idsSeleccionados.toSet(), password) { bytes ->
            var guardadoExitoso = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val values = android.content.ContentValues().apply {
                        put(android.provider.MediaStore.Downloads.DISPLAY_NAME, nombreArchivo)
                        put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
                        put(android.provider.MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/BovedaLocal/Backups")
                        put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
                    }
                    val uri = contexto.contentResolver.insert(
                        android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        values
                    )
                    if (uri != null) {
                        contexto.contentResolver.openOutputStream(uri)?.use { stream ->
                            stream.write(bytes)
                        }
                        values.clear()
                        values.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
                        contexto.contentResolver.update(uri, values, null, null)
                        guardadoExitoso = true
                    }
                } catch (e: Exception) {
                    com.jlnavas3.bovedalocal.util.Diagnostico.apuntar("backup", "Fallo al guardar copia selectiva con MediaStore: ${e.message}")
                    guardadoExitoso = false
                }
            }

            if (!guardadoExitoso) {
                try {
                    val carpetaAuto = GestorBackupAutomatico.obtenerDirectorioBackups(contexto)
                    val destino = File(carpetaAuto, nombreArchivo)
                    VaultCrypto.escribirAtomico(destino, bytes)
                    MediaScannerConnection.scanFile(contexto, arrayOf(destino.absolutePath), null, null)
                    guardadoExitoso = true
                } catch (e: Exception) {
                    com.jlnavas3.bovedalocal.util.Diagnostico.apuntar("backup", "Fallo al guardar copia selectiva con File: ${e.message}")
                }
            }

            if (guardadoExitoso) {
                vm.avisar("Guardado en Descargas/BovedaLocal/Backups/$nombreArchivo")
            } else {
                vm.avisar("No se pudo guardar automáticamente. Usa el explorador de archivos.")
            }
        }
    }

    val categoriaActual = remember(seccionActiva, todasCategorias) {
        todasCategorias.firstOrNull { it.id == seccionActiva } ?: todasCategorias.first()
    }

    val entradasPagina = remember(entradasTotales, categoriaActual) {
        entradasTotales.filter(categoriaActual.filtro)
    }

    val entradasPaginaBusqueda = remember(entradasPagina, textoBusqueda) {
        val q = textoBusqueda.trim().lowercase()
        if (q.isBlank()) entradasPagina
        else entradasPagina.filter {
            it.titulo.lowercase().contains(q) || it.usuario.lowercase().contains(q)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Exportación selectiva",
            idEtiqueta = "02.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                BotonIconoCabecera(
                    onClick = {
                        haptica.tic()
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    icono = Icons.Filled.Search,
                    descripcion = "Buscar entradas",
                    tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Ambar else ColorIconosInternos
                )

                BotonIconoCabecera(
                    onClick = {
                        if (idsSeleccionados.isNotEmpty()) {
                            haptica.tic()
                            dialogoExportar = true
                        }
                    },
                    icono = Icons.Filled.Check,
                    descripcion = "Confirmar exportación",
                    tint = if (idsSeleccionados.isNotEmpty()) ColorAcento else ColorIconosInternos.copy(alpha = 0.3f),
                    habilitado = idsSeleccionados.isNotEmpty()
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = "Marca las entradas específicas que deseas guardar en un archivo .bvda cifrado:"
            )

            if (busquedaVisible) {
                Spacer(Modifier.height(8.dp))
                ComponenteCampoTexto(
                    valor = textoBusqueda,
                    etiqueta = "Buscar en esta categoría",
                    alCambiar = { textoBusqueda = it },
                    icono = Icons.Filled.Search,
                    mostrarIcono = true,
                    botonLimpiar = true
                )
            }

            Spacer(Modifier.height(10.dp))

            // Chips dinámicos de categorías con FormaTarjeta y texto negro cuando activo
            if (categoriasDisponibles.size > 1) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoriasDisponibles, key = { it.id }) { cat ->
                        val seleccionada = cat.id == seccionActiva
                        val cantCat = remember(entradasTotales) {
                            if (cat.id == "todos") entradasTotales.size
                            else entradasTotales.count(cat.filtro)
                        }

                        Box(
                            modifier = (if (!seleccionada && GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                Modifier.border(width = GrosorBorde, color = ColorBordeActual, shape = FormaTarjeta as Shape)
                            else
                                Modifier)
                                .clip(FormaTarjeta)
                                .background(if (seleccionada) ColorAcento else Superficie)
                                .clickable {
                                    haptica.tic()
                                    seccionActiva = cat.id
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "${cat.etiqueta} ($cantCat)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium),
                                color = if (seleccionada) Color.Black else TextoPrincipal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
            }

            // Barra de controles de selección rápida con Íconos Circulares
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${idsSeleccionados.size} de ${entradasTotales.size} seleccionadas",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAcento
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BotonIconoCabecera(
                        onClick = {
                            haptica.tic()
                            entradasPaginaBusqueda.forEach { if (!idsSeleccionados.contains(it.id)) idsSeleccionados.add(it.id) }
                        },
                        icono = Icons.Filled.DoneAll,
                        descripcion = "Marcar visibles",
                        tint = ColorAcento
                    )

                    BotonIconoCabecera(
                        onClick = {
                            haptica.tic()
                            idsSeleccionados.clear()
                        },
                        icono = Icons.Filled.Clear,
                        descripcion = "Deseleccionar todas",
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Lista de entradas filtradas por categoría
            if (entradasPaginaBusqueda.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay entradas registradas en esta categoría.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(entradasPaginaBusqueda, key = { it.id }) { entrada ->
                        val marcada = idsSeleccionados.contains(entrada.id)
                        val colorTipoEntrada = when {
                            entrada.passkey != null -> ColorDatosPasskey
                            !entrada.secretoTotp.isNullOrBlank() -> ColorDatos2FA
                            entrada.contrasena.isNotBlank() -> ColorDatosContrasena
                            entrada.usuario.isNotBlank() -> ColorDatosUsuario
                            entrada.urls.any { it.startsWith("androidapp://", ignoreCase = true) || it.startsWith("androidapp:", ignoreCase = true) } -> ColorDatosApp
                            entrada.urls.isNotEmpty() -> ColorDatosWeb
                            else -> ColorAcento
                        }

                        Box(
                            modifier = (if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                Modifier.border(width = GrosorBorde, color = ColorBordeActual, shape = FormaTarjeta as Shape)
                            else
                                Modifier)
                                .fillMaxWidth()
                                .clip(FormaTarjeta)
                                .background(Superficie)
                                .clickable {
                                    haptica.tic()
                                    if (marcada) idsSeleccionados.remove(entrada.id)
                                    else idsSeleccionados.add(entrada.id)
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterStart)
                                    .background(colorTipoEntrada)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = marcada,
                                    onCheckedChange = { checked ->
                                        haptica.tic()
                                        if (checked) idsSeleccionados.add(entrada.id)
                                        else idsSeleccionados.remove(entrada.id)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = ColorAcento,
                                        uncheckedColor = TextoSecundario
                                    )
                                )

                                Spacer(Modifier.width(6.dp))

                                Monograma(
                                    titulo = entrada.titulo,
                                    semilla = entrada.urls.firstOrNull() ?: entrada.usuario,
                                    tamano = 36
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entrada.titulo,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextoPrincipal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (entrada.usuario.isNotBlank()) {
                                        Text(
                                            text = entrada.usuario,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextoSecundario,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botones inferiores en Fila Horizontal (50% - 50%)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonAmbar(
                    texto = if (idsSeleccionados.isNotEmpty())
                        "Exportar (${idsSeleccionados.size})"
                    else
                        "Selecciona",
                    icono = Icons.Filled.Check,
                    activo = idsSeleccionados.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    haptica.tic()
                    dialogoExportar = true
                }

                BotonBorde(
                    texto = "Cancelar",
                    icono = Icons.Filled.Close,
                    modifier = Modifier.weight(1f)
                ) {
                    vm.volverAtras()
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (dialogoExportar) {
        DialogoClaveExportarSelectivo(
            cantidad = idsSeleccionados.size,
            autoPassword = ajustes.backupAutoPasswordCifrado,
            alDescartar = { dialogoExportar = false },
            alConfirmar = { nombreArchivoResolved, clavePass, directoAuto ->
                dialogoExportar = false
                passwordAUsar = clavePass
                if (directoAuto) {
                    guardarDirectoEnAutoBackup(nombreArchivoResolved, clavePass)
                } else {
                    BovedaApp.salidaPendiente(contexto)
                    try {
                        lanzadorGuardarBvda.launch(nombreArchivoResolved)
                    } catch (e: Exception) {
                        BovedaApp.salidaTerminada(contexto)
                        vm.avisar("No se encontró ningún selector de archivos para guardar")
                    }
                }
            }
        )
    }
}

/**
 * Diálogo modal para ingresar la contraseña de cifrado del archivo .bvda, patrón de nombre
 * y la opción de guardado directo en la carpeta de copias de seguridad automáticas.
 */
@Composable
private fun DialogoClaveExportarSelectivo(
    cantidad: Int,
    autoPassword: String,
    alDescartar: () -> Unit,
    alConfirmar: (String, String, Boolean) -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val esOscuro = isSystemInDarkTheme()
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

    var patronNombre by remember { mutableStateOf("{99}-selectivo-{FECHA}") }
    var clavePassword by remember { mutableStateOf("") }
    var mostrarClave by remember { mutableStateOf(false) }
    var usarAutoPassword by remember { mutableStateOf(false) }
    var guardarEnDirectorioAuto by remember { mutableStateOf(true) }

    val tieneAutoPassword = autoPassword.isNotBlank()
    val nombreFinalResuelto = remember(patronNombre) {
        val base = GestorBackupAutomatico.resolverNombreArchivo(patronNombre)
        if (base.endsWith(".bvda", ignoreCase = true)) base else "$base.bvda"
    }

    val claveValida = clavePassword.isNotBlank()

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(22.dp),
        containerColor = fondoModal,
        title = {
            Text(
                text = "Exportación selectiva (.bvda)",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Se exportarán $cantidad ${if (cantidad == 1) "entrada cifrada" else "entradas cifradas"}. Configura el nombre del archivo y la clave de cifrado:",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(14.dp))

                // Campo Patrón de Nombre de Archivo
                ComponenteCampoTexto(
                    valor = patronNombre,
                    etiqueta = "Patrón del nombre de archivo",
                    alCambiar = { patronNombre = it },
                    placeholder = "{99}-selectivo-{FECHA}"
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Nombre final: $nombreFinalResuelto",
                    style = EstiloMono.copy(fontSize = 10.sp),
                    color = ColorAcento
                )

                Spacer(Modifier.height(12.dp))

                // Campo Contraseña
                ComponenteCampoTexto(
                    valor = clavePassword,
                    etiqueta = "Contraseña de cifrado",
                    alCambiar = {
                        clavePassword = it
                        usarAutoPassword = (it == autoPassword)
                    },
                    esContrasena = true,
                    mostrarContrasena = mostrarClave,
                    alAlternarMostrarContrasena = {
                        haptica.tic()
                        mostrarClave = !mostrarClave
                    }
                )

                Spacer(Modifier.height(10.dp))

                // Fila Switch Guardar Directamente en Carpeta de Copias Automáticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .clickable {
                            haptica.tic()
                            guardarEnDirectorioAuto = !guardarEnDirectorioAuto
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Guardar en carpeta de copias automáticas",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = TextoPrincipal
                        )
                        Text(
                            text = if (guardarEnDirectorioAuto) "Guardará directamente en Descargas/BovedaLocalBackups" else "Solicitará ruta con el explorador de archivos",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }

                    SwitchBoveda(
                        checked = guardarEnDirectorioAuto,
                        onCheckedChange = { checked ->
                            haptica.tic()
                            guardarEnDirectorioAuto = checked
                        }
                    )
                }

                Spacer(Modifier.height(6.dp))

                // Fila Switch Contraseña Automática
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .clickable(enabled = tieneAutoPassword) {
                            if (tieneAutoPassword) {
                                haptica.tic()
                                usarAutoPassword = !usarAutoPassword
                                clavePassword = if (usarAutoPassword) autoPassword else ""
                            }
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usar clave de copia automática",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = if (tieneAutoPassword) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f)
                        )
                        if (!tieneAutoPassword) {
                            Text(
                                text = "No configurada en Ajustes",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextoSecundario.copy(alpha = 0.5f)
                            )
                        }
                    }

                    SwitchBoveda(
                        checked = usarAutoPassword && tieneAutoPassword,
                        onCheckedChange = { checked ->
                            if (tieneAutoPassword) {
                                haptica.tic()
                                usarAutoPassword = checked
                                clavePassword = if (checked) autoPassword else ""
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (claveValida) {
                        haptica.exito()
                        alConfirmar(nombreFinalResuelto, clavePassword, guardarEnDirectorioAuto)
                    }
                },
                enabled = claveValida
            ) {
                Text("Exportar .bvda", color = if (claveValida) ColorAcento else TextoSecundario, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}
