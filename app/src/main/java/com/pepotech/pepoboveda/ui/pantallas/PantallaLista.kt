package com.pepotech.pepoboveda.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pepotech.pepoboveda.BuildConfig
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.EstadoBoveda
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.ui.Pantalla
import com.pepotech.pepoboveda.ui.VaultViewModel
import com.pepotech.pepoboveda.crypto.Base32
import com.pepotech.pepoboveda.crypto.Totp
import com.pepotech.pepoboveda.ui.componentes.AnilloTotp
import com.pepotech.pepoboveda.ui.componentes.IlustracionVacio
import com.pepotech.pepoboveda.ui.componentes.Monograma
import com.pepotech.pepoboveda.ui.theme.Ambar
import com.pepotech.pepoboveda.ui.theme.Borde
import com.pepotech.pepoboveda.ui.theme.DegradadoAmbar
import com.pepotech.pepoboveda.ui.theme.Menta
import com.pepotech.pepoboveda.ui.theme.Obsidiana
import com.pepotech.pepoboveda.ui.theme.Peligro
import com.pepotech.pepoboveda.ui.theme.Superficie
import com.pepotech.pepoboveda.ui.theme.SuperficieAlta
import com.pepotech.pepoboveda.ui.theme.TextoPrincipal
import com.pepotech.pepoboveda.ui.theme.TextoSecundario
import com.pepotech.pepoboveda.util.Haptica
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.filtroTipo.collectAsStateWithLifecycle()
    val soloFavoritos by vm.soloFavoritos.collectAsStateWithLifecycle()
    val filtroEtiqueta by vm.filtroEtiqueta.collectAsStateWithLifecycle()
    val densidad = ajustes.densidadLista
    val densidadAltura = when (densidad) {
        "compacta" -> 48.dp
        "comoda" -> 60.dp
        else -> 74.dp
    }
    val densidadMonograma = when (densidad) {
        "compacta" -> 34
        "comoda" -> 40
        else -> 46
    }
    val espaciadoFilas = when (densidad) {
        "compacta" -> 4.dp
        "comoda" -> 7.dp
        else -> 10.dp
    }
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val visibles = remember(entradas, busqueda, filtro, soloFavoritos, filtroEtiqueta) { vm.entradasVisibles(entradas) }
    val etiquetasDisponibles = remember(entradas) { vm.etiquetasUsadas() }

    val estadoCajon = rememberDrawerState(DrawerValue.Closed)
    val ambitoCorutina = rememberCoroutineScope()
    fun abrirMenu() = ambitoCorutina.launch { estadoCajon.open() }
    fun cerrarMenu() = ambitoCorutina.launch { estadoCajon.close() }

    // ------------------------------------------------------- selección múltiple
    var modoSeleccion by remember { mutableStateOf(false) }
    var seleccionados by remember { mutableStateOf(setOf<String>()) }
    var dialogoBorrarSeleccion by remember { mutableStateOf(false) }

    // Claves "categoria|sitio" de los grupos por sitio que el usuario ha desplegado a mano.
    var gruposExpandidos by remember { mutableStateOf(setOf<String>()) }

    fun salirDeSeleccion() {
        modoSeleccion = false
        seleccionados = emptySet()
    }

    fun alternarSeleccion(id: String) {
        seleccionados = if (seleccionados.contains(id)) seleccionados - id else seleccionados + id
        if (seleccionados.isEmpty()) modoSeleccion = false
    }

    fun entrarEnSeleccion(id: String) {
        modoSeleccion = true
        seleccionados = setOf(id)
        haptica.tic()
    }

    // Selecciona/deselecciona todo lo que se ve ahora mismo (respeta búsqueda y filtros).
    val todoSeleccionado = visibles.isNotEmpty() && seleccionados.containsAll(visibles.map { it.id })
    fun alternarSeleccionarTodo() {
        haptica.tic()
        if (todoSeleccionado) {
            seleccionados = emptySet()
            modoSeleccion = false
        } else {
            modoSeleccion = true
            seleccionados = visibles.map { it.id }.toSet()
        }
    }

    // Si la lista cambia de raíz (por ejemplo, tras borrar) no queremos ids fantasma.
    LaunchedEffect(entradas) {
        if (modoSeleccion) {
            val vivos = entradas.map { it.id }.toSet()
            seleccionados = seleccionados.intersect(vivos)
            if (seleccionados.isEmpty()) modoSeleccion = false
        }
    }

    ModalNavigationDrawer(
        drawerState = estadoCajon,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.8f),
                drawerShape = androidx.compose.ui.graphics.RectangleShape,
                drawerContainerColor = Superficie
            ) {
                MenuLateral(
                    nombreApp = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                    totalEntradas = entradas.size,
                    totalPapelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera?.size ?: 0,
                    alIr = { destino -> cerrarMenu(); vm.ir(destino) },
                    alBloquear = { cerrarMenu(); haptica.toque(); vm.bloquear() }
                )
            }
        }
    ) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (modoSeleccion) {
                BarraSeleccion(
                    cantidad = seleccionados.size,
                    todoSeleccionado = todoSeleccionado,
                    alCancelar = { salirDeSeleccion() },
                    alSeleccionarTodo = { alternarSeleccionarTodo() },
                    alBorrar = { dialogoBorrarSeleccion = true }
                )
            } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { haptica.toque(); abrirMenu() }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = TextoPrincipal)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextoPrincipal
                    )
                    Text(
                        "${entradas.size} ${if (entradas.size == 1) "entrada" else "entradas"} cifradas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
            }

            val diasSinExportar = remember(ajustes) { vm.diasSinExportar() }
            if (diasSinExportar != null) {
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    BannerRecordatorioExportacion(
                        dias = diasSinExportar,
                        alIr = { vm.ir(Pantalla.Ajustes) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CampoBusquedaLista(valor = busqueda, alCambiar = { vm.buscar(it) })
                }
                Box(modifier = Modifier.weight(1f)) {
                    SelectorFiltros(
                        filtro = filtro,
                        soloFavoritos = soloFavoritos,
                        alTodo = {
                            vm.filtrarPorTipo(null)
                            if (soloFavoritos) vm.alternarSoloFavoritos()
                        },
                        alClaves = { vm.filtrarPorTipo(if (filtro == TipoEntrada.LOGIN) null else TipoEntrada.LOGIN) },
                        alPasskeys = { vm.filtrarPorTipo(if (filtro == TipoEntrada.PASSKEY) null else TipoEntrada.PASSKEY) },
                        alNotas = { vm.filtrarPorTipo(if (filtro == TipoEntrada.NOTA) null else TipoEntrada.NOTA) },
                        alFavoritos = { vm.alternarSoloFavoritos() }
                    )
                }
            }
            if (etiquetasDisponibles.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    etiquetasDisponibles.forEach { etiqueta ->
                        ChipFiltro("# $etiqueta", filtroEtiqueta == etiqueta) {
                            vm.filtrarPorEtiqueta(if (filtroEtiqueta == etiqueta) null else etiqueta)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (visibles.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    if (entradas.isEmpty()) {
                        IlustracionVacio()
                    } else {
                        Text(
                            "Nada coincide con esa búsqueda",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoSecundario,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 20.dp, end = 20.dp, bottom = 110.dp),
                    verticalArrangement = Arrangement.spacedBy(espaciadoFilas)
                ) {
                    // Orden fijo: primero lo que caduca en 30 segundos, luego claves,
                    // luego passkeys y notas.
                    val conDobleFactor = visibles.filter { !it.secretoTotp.isNullOrBlank() }
                    val claves = visibles.filter {
                        it.secretoTotp.isNullOrBlank() && it.tipo == TipoEntrada.LOGIN
                    }
                    val passkeys = visibles.filter {
                        it.secretoTotp.isNullOrBlank() && it.tipo == TipoEntrada.PASSKEY
                    }
                    val notas = visibles.filter {
                        it.secretoTotp.isNullOrBlank() && it.tipo == TipoEntrada.NOTA
                    }

                    listOf(
                        "Doble factor" to conDobleFactor,
                        "Contraseñas" to claves,
                        "Llaves de acceso" to passkeys,
                        "Notas" to notas
                    ).forEach { (titulo, grupo) ->
                        if (grupo.isEmpty()) return@forEach
                        item(key = "cabecera-$titulo") {
                            Text(
                                titulo.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = Ambar,
                                modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                            )
                        }
                        // Agrupa por sitio (mismo host, o mismo rpId en passkeys) cuando hay
                        // más de una cuenta: se ve una sola fila plegada, como en
                        // passwords.google.com, y se despliega tocándola.
                        val expandidoEnCategoria: (String) -> Boolean = { clave ->
                            modoSeleccion || gruposExpandidos.contains("$titulo|$clave")
                        }
                        val itemsAMostrar = com.pepotech.pepoboveda.util.construirItemsAgrupadosPorSitio(grupo, expandidoEnCategoria)
                        items(itemsAMostrar, key = { item ->
                            when (item) {
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Suelto -> item.entrada.id
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Grupo -> "grupo-$titulo-${item.clave}"
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Hijo -> "hijo-${item.entrada.id}"
                            }
                        }) { item ->
                            when (item) {
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Grupo -> FilaGrupoSitio(
                                    clave = item.clave,
                                    cantidad = item.entradas.size,
                                    expandido = expandidoEnCategoria(item.clave),
                                    alturaFila = densidadAltura,
                                    tamanoMonograma = densidadMonograma,
                                    alAlternar = {
                                        haptica.tic()
                                        val llave = "$titulo|${item.clave}"
                                        gruposExpandidos = if (gruposExpandidos.contains(llave)) {
                                            gruposExpandidos - llave
                                        } else {
                                            gruposExpandidos + llave
                                        }
                                    }
                                )
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Suelto -> FilaEntrada(
                                    entrada = item.entrada,
                                    seleccionActiva = modoSeleccion,
                                    seleccionado = seleccionados.contains(item.entrada.id),
                                    alAbrir = { vm.ir(Pantalla.Detalle(item.entrada.id)) },
                                    alCopiarUsuario = {
                                        haptica.toque()
                                        vm.copiar("Usuario", item.entrada.usuario, sensible = false)
                                    },
                                    alCopiarContrasena = {
                                        haptica.exito()
                                        vm.copiar("Contraseña", item.entrada.contrasena, sensible = true)
                                    },
                                    alFavorito = { haptica.tic(); vm.alternarFavorito(item.entrada.id) },
                                    alCopiarCodigo = { codigo ->
                                        haptica.exito()
                                        vm.copiar("Código", codigo, sensible = true)
                                    },
                                    alPulsarLargo = { entrarEnSeleccion(item.entrada.id) },
                                    alAlternarSeleccion = { alternarSeleccion(item.entrada.id) },
                                    alturaFila = densidadAltura,
                                    tamanoMonograma = densidadMonograma
                                )
                                is com.pepotech.pepoboveda.util.ItemAgrupado.Hijo -> Box(modifier = Modifier.padding(start = 16.dp)) {
                                    FilaEntrada(
                                        entrada = item.entrada,
                                        seleccionActiva = modoSeleccion,
                                        seleccionado = seleccionados.contains(item.entrada.id),
                                        alAbrir = { vm.ir(Pantalla.Detalle(item.entrada.id)) },
                                        alCopiarUsuario = {
                                            haptica.toque()
                                            vm.copiar("Usuario", item.entrada.usuario, sensible = false)
                                        },
                                        alCopiarContrasena = {
                                            haptica.exito()
                                            vm.copiar("Contraseña", item.entrada.contrasena, sensible = true)
                                        },
                                        alFavorito = { haptica.tic(); vm.alternarFavorito(item.entrada.id) },
                                        alCopiarCodigo = { codigo ->
                                            haptica.exito()
                                            vm.copiar("Código", codigo, sensible = true)
                                        },
                                        alPulsarLargo = { entrarEnSeleccion(item.entrada.id) },
                                        alAlternarSeleccion = { alternarSeleccion(item.entrada.id) },
                                        alturaFila = densidadAltura,
                                        tamanoMonograma = densidadMonograma
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!modoSeleccion) {
        FloatingActionButton(
            onClick = { haptica.toque(); vm.ir(Pantalla.Editar(null)) },
            containerColor = Ambar,
            contentColor = Obsidiana,
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nueva entrada")
        }
        }
    }
    }

    if (dialogoBorrarSeleccion) {
        AlertDialog(
            onDismissRequest = { dialogoBorrarSeleccion = false },
            containerColor = SuperficieAlta,
            title = { Text("¿Mover ${seleccionados.size} entradas a la papelera?", color = TextoPrincipal) },
            text = { Text("Se pueden restaurar desde la papelera durante 30 días.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    dialogoBorrarSeleccion = false
                    val idsABorrar = seleccionados
                    salirDeSeleccion()
                    vm.eliminarVarias(idsABorrar)
                }) { Text("Mover a la papelera", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoBorrarSeleccion = false }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}

/**
 * Menú lateral al estilo Solid Explorer: se abre deslizando desde el borde
 * izquierdo. Cabecera con el nombre de la app, cuerpo con los accesos que antes
 * vivían en la barra superior, y en el pie la versión compilada.
 */
@Composable
private fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(nombreApp, style = MaterialTheme.typography.headlineSmall, color = TextoPrincipal)
            Spacer(Modifier.height(4.dp))
            Text(
                "$totalEntradas ${if (totalEntradas == 1) "entrada guardada" else "entradas guardadas"}",
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = TextoSecundario
            )
        }
        Spacer(Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Borde))
        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            ItemMenu("Generar contraseñas", Icons.Filled.AutoAwesome) { alIr(Pantalla.Generador) }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ItemMenu("Passkeys", Icons.Filled.Fingerprint) { alIr(Pantalla.Passkeys) }
            }
            ItemMenu("Autenticador 2FA", Icons.Filled.Timer) { alIr(Pantalla.Autenticador) }
            ItemMenu("Salud de la bóveda", Icons.Filled.HealthAndSafety) { alIr(Pantalla.SaludBoveda) }
            ItemMenu(
                if (totalPapelera > 0) "Papelera ($totalPapelera)" else "Papelera",
                Icons.Filled.Delete
            ) { alIr(Pantalla.Papelera) }
            ItemMenu("Configuración", Icons.Filled.Settings) { alIr(Pantalla.Ajustes) }
            ItemMenu("Bloquear bóveda", Icons.Filled.Lock, colorTexto = Peligro) { alBloquear() }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Borde))
        Spacer(Modifier.height(12.dp))
        Text(
            "Bóveda local",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            "versión ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun ItemMenu(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorTexto: androidx.compose.ui.graphics.Color = TextoPrincipal,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alPulsar() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = if (colorTexto == Peligro) Peligro else Ambar)
        Spacer(Modifier.width(16.dp))
        Text(texto, style = MaterialTheme.typography.titleMedium, color = colorTexto)
    }
}

@Composable
private fun BarraSeleccion(
    cantidad: Int,
    todoSeleccionado: Boolean,
    alCancelar: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alBorrar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = alCancelar) {
            Icon(Icons.Filled.Close, contentDescription = "Cancelar selección", tint = TextoPrincipal)
        }
        Text(
            "$cantidad ${if (cantidad == 1) "seleccionada" else "seleccionadas"}",
            style = MaterialTheme.typography.titleMedium,
            color = TextoPrincipal,
            modifier = Modifier.weight(1f).padding(start = 4.dp)
        )
        IconButton(onClick = alSeleccionarTodo) {
            Icon(
                Icons.Filled.SelectAll,
                contentDescription = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                tint = if (todoSeleccionado) Ambar else TextoPrincipal
            )
        }
        IconButton(onClick = alBorrar, enabled = cantidad > 0) {
            Icon(Icons.Filled.Delete, contentDescription = "Borrar seleccionadas", tint = Peligro)
        }
    }
}

@Composable
private fun BannerRecordatorioExportacion(dias: Long, alIr: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Superficie)
            .clickable { alIr() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Hace $dias días que no exportas una copia",
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Toca para ir a Ajustes > Copia de seguridad",
                color = TextoSecundario,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ChipFiltro(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            texto,
            color = if (activo) Obsidiana else TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CampoBusquedaLista(valor: String, alCambiar: (String) -> Unit) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(forma)
            .background(Superficie)
            .border(1.dp, if (valor.isBlank()) Borde else Ambar, forma)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = if (valor.isBlank()) TextoSecundario else Ambar, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(Ambar),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SelectorFiltros(
    filtro: TipoEntrada?,
    soloFavoritos: Boolean,
    alTodo: () -> Unit,
    alClaves: () -> Unit,
    alPasskeys: () -> Unit,
    alNotas: () -> Unit,
    alFavoritos: () -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val etiqueta = when {
        filtro == null && !soloFavoritos -> "Todo"
        filtro == TipoEntrada.LOGIN && soloFavoritos -> "Claves + ★"
        filtro == TipoEntrada.PASSKEY && soloFavoritos -> "Passkeys + ★"
        filtro == TipoEntrada.NOTA && soloFavoritos -> "Notas + ★"
        filtro == TipoEntrada.LOGIN -> "Claves"
        filtro == TipoEntrada.PASSKEY -> "Passkeys"
        filtro == TipoEntrada.NOTA -> "Notas"
        soloFavoritos -> "Favoritos"
        else -> "Filtros"
    }
    val forma = RoundedCornerShape(18.dp)

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(forma)
                .background(Superficie)
                .border(1.dp, if (desplegado || filtro != null || soloFavoritos) Ambar else Borde, forma)
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Tune,
                contentDescription = null,
                tint = if (desplegado || filtro != null || soloFavoritos) Ambar else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(etiqueta, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1, modifier = Modifier.weight(1f))
            Icon(
                if (desplegado) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir filtros",
                tint = TextoSecundario
            )
        }

        DropdownMenu(
            expanded = desplegado,
            onDismissRequest = { desplegado = false },
            modifier = Modifier
                .background(SuperficieAlta)
                .fillMaxWidth(0.5f)
        ) {
            OpcionFiltro("Todo", Icons.Filled.SelectAll, filtro == null && !soloFavoritos) { alTodo(); desplegado = false }
            OpcionFiltro("Claves", Icons.Filled.Lock, filtro == TipoEntrada.LOGIN) { alClaves(); desplegado = false }
            OpcionFiltro("Passkeys", Icons.Filled.Fingerprint, filtro == TipoEntrada.PASSKEY) { alPasskeys(); desplegado = false }
            OpcionFiltro("Notas", Icons.Filled.Menu, filtro == TipoEntrada.NOTA) { alNotas(); desplegado = false }
            OpcionFiltro("Favoritos", Icons.Filled.Star, soloFavoritos) { alFavoritos(); desplegado = false }
        }
    }
}

@Composable
private fun OpcionFiltro(texto: String, icono: ImageVector, activo: Boolean, alPulsar: () -> Unit) {
    DropdownMenuItem(
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = if (activo) Ambar else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        },
        text = {
            Text(
                texto,
                color = if (activo) Ambar else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingIcon = {
            if (activo) Icon(Icons.Filled.Check, contentDescription = null, tint = Ambar, modifier = Modifier.size(18.dp))
        },
        onClick = alPulsar
    )
}

@Composable
private fun FilaGrupoSitio(
    clave: String,
    cantidad: Int,
    expandido: Boolean,
    alturaFila: androidx.compose.ui.unit.Dp = 74.dp,
    tamanoMonograma: Int = 46,
    alAlternar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(RoundedCornerShape(22.dp))
            .background(Superficie)
            .clickable { alAlternar() }
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Monograma(titulo = clave.ifBlank { "?" }, semilla = clave, tamano = tamanoMonograma)
        Spacer(Modifier.width(if (alturaFila.value <= 48f) 10.dp else 14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                clave,
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                color = TextoPrincipal,
                maxLines = 1
            )
            Text(
                "$cantidad ${if (cantidad == 1) "cuenta" else "cuentas"}",
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
        Icon(
            if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = TextoSecundario
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun FilaEntrada(
    entrada: Entrada,
    seleccionActiva: Boolean,
    seleccionado: Boolean,
    alAbrir: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    alFavorito: () -> Unit,
    alCopiarCodigo: (String) -> Unit = {},
    alPulsarLargo: () -> Unit,
    alAlternarSeleccion: () -> Unit,
    alturaFila: androidx.compose.ui.unit.Dp = 74.dp,
    tamanoMonograma: Int = 46
) {
    val compacta = alturaFila.value <= 48f
    val contenidoFila: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .clip(RoundedCornerShape(22.dp))
                .background(if (seleccionado) SuperficieAlta else Superficie)
                .combinedClickable(
                    onClick = { if (seleccionActiva) alAlternarSeleccion() else alAbrir() },
                    onLongClick = { if (!seleccionActiva) alPulsarLargo() }
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) Ambar else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = Obsidiana, modifier = Modifier.size(if (compacta) 16.dp else 18.dp))
                    }
                }
            } else {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                    tamano = tamanoMonograma
                )
            }
            Spacer(Modifier.width(if (compacta) 10.dp else 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entrada.titulo.ifBlank { "Sin título" },
                    style = if (compacta) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal,
                    maxLines = 1
                )
                Text(
                    when (entrada.tipo) {
                        TipoEntrada.PASSKEY -> "Passkey · ${entrada.passkey?.rpId ?: ""}"
                        TipoEntrada.NOTA -> "Nota segura"
                        TipoEntrada.LOGIN -> entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                    },
                    style = if (compacta) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    maxLines = 1
                )
            }
            val secreto = entrada.secretoTotp
            if (!seleccionActiva && !secreto.isNullOrBlank()) {
                var ahora by remember { mutableStateOf(System.currentTimeMillis() / 1000) }
                LaunchedEffect(secreto) {
                    while (true) {
                        ahora = System.currentTimeMillis() / 1000
                        kotlinx.coroutines.delay(1000)
                    }
                }
                val periodo = entrada.totpPeriodo.toLong()
                val codigo = remember(ahora / periodo, secreto) {
                    try {
                        Totp.codigo(
                            secreto = Base32.decodificar(secreto),
                            segundosUnix = ahora,
                            digitos = entrada.totpDigitos,
                            periodo = periodo
                        )
                    } catch (e: Exception) {
                        "------"
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { alCopiarCodigo(codigo) }.padding(horizontal = 4.dp)
                ) {
                    Text(codigo, style = if (compacta) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium, color = Ambar)
                    Spacer(Modifier.width(if (compacta) 4.dp else 8.dp))
                    // 34dp es el minimo en el que el numero de dentro se lee de un vistazo.
                    AnilloTotp(
                        codigo = "",
                        segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                        tamano = if (compacta) 28 else 34,
                        periodo = periodo
                    )
                }
            }
            if (!seleccionActiva) {
                IconButton(onClick = alFavorito, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Favorito",
                        tint = if (entrada.favorito) Ambar else Borde
                    )
                }
            }
        }
    }

    // El deslizar para copiar solo tiene sentido fuera del modo de selección: si no,
    // un swipe accidental copiaría una contraseña mientras se intenta marcar varias.
    if (seleccionActiva) {
        contenidoFila()
    } else {
        val estadoSwipe = rememberSwipeToDismissBoxState(
            confirmValueChange = { valor ->
                when (valor) {
                    SwipeToDismissBoxValue.StartToEnd -> alCopiarUsuario()
                    SwipeToDismissBoxValue.EndToStart -> alCopiarContrasena()
                    else -> Unit
                }
                false
            }
        )
        SwipeToDismissBox(
            state = estadoSwipe,
            backgroundContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Borde)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Copiar usuario", color = Menta, style = MaterialTheme.typography.bodyMedium)
                    Text("Copiar contraseña", color = Ambar, style = MaterialTheme.typography.bodyMedium)
                }
            },
            content = { contenidoFila() }
        )
    }
}

