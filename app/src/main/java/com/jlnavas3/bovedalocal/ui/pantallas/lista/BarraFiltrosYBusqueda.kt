package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.RecordatorioExportacionInfo
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun BarraSeleccion(
    cantidad: Int,
    todoSeleccionado: Boolean,
    alCancelar: () -> Unit,
    alRenombrar: () -> Unit = {},
    alSeleccionarTodo: () -> Unit,
    alBorrar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ColorTarjetaAjustes)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = alCancelar, modifier = Modifier.size(38.dp)) {
            Icon(Icons.Filled.Close, contentDescription = "Cancelar selección", tint = TextoPrincipal, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = "$cantidad ${if (cantidad == 1) "seleccionada" else "seleccionadas"}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = ColorTitulos,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = alRenombrar, enabled = cantidad > 0, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Renombrar título",
                tint = if (cantidad > 0) TextoPrincipal else TextoSecundario.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = alSeleccionarTodo, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.SelectAll,
                contentDescription = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                tint = if (todoSeleccionado) Ambar else TextoPrincipal,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = alBorrar, enabled = cantidad > 0, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Borrar seleccionadas",
                tint = if (cantidad > 0) Peligro else Peligro.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BannerRecordatorioExportacion(
    info: RecordatorioExportacionInfo,
    alIr: () -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .clickable { alIr() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFB8C00)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Backup,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = info.titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = info.descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.5.sp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = ColorIconosInternos,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun BannerRecordatorioExportacion(dias: Long, alIr: () -> Unit) {
    BannerRecordatorioExportacion(
        info = RecordatorioExportacionInfo(
            titulo = "Hace $dias ${if (dias == 1L) "día" else "días"} sin exportar una copia",
            descripcion = "Toca para ir a Copia de seguridad"
        ),
        alIr = alIr
    )
}

@Composable
fun ChipFiltro(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(ColorTarjetaAjustes, ColorTarjetaAjustes)))
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            texto,
            color = if (activo) ColorSobreAcento else TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BarraBusquedaAnimada(
    valor: String,
    alCambiar: (String) -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
    val forma = RoundedCornerShape(20.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = if (valor.isBlank()) TextoSecundario else Ambar,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextoPrincipal, fontSize = 15.sp),
            cursorBrush = SolidColor(Ambar),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = TextoSecundario,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        IconButton(
            onClick = alCerrar,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cerrar búsqueda",
                tint = ColorIconosInternos,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ChipFiltroActivo(
    texto: String,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Ambar.copy(alpha = 0.16f))
            .clickable { alLimpiar() }
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            color = Ambar,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Quitar filtro",
            tint = Ambar,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun DialogoFiltrosLista(
    filtroActual: TipoEntrada?,
    alSeleccionarTipo: (TipoEntrada?) -> Unit,
    alCerrar: () -> Unit
) {
    val tipos = listOf(
        null to ("Todo" to Icons.Filled.SelectAll),
        TipoEntrada.LOGIN to ("Claves / Logins" to Icons.Filled.Lock),
        TipoEntrada.PASSKEY to ("Passkeys" to Icons.Filled.Fingerprint),
        TipoEntrada.NOTA to ("Notas seguras" to Icons.Filled.Description),
        TipoEntrada.TARJETA to ("Tarjetas bancarias" to Icons.Filled.CreditCard),
        TipoEntrada.WIFI to ("Redes Wi-Fi" to Icons.Filled.Wifi),
        TipoEntrada.CUENTA_BANCARIA to ("Cuentas bancarias" to Icons.Filled.AccountBalance),
        TipoEntrada.IDENTIDAD to ("Identidad" to Icons.Filled.Badge),
        TipoEntrada.SERVIDOR to ("Servidores" to Icons.Filled.Dns),
        TipoEntrada.WALLET to ("Cripto Wallets" to Icons.Filled.AccountBalanceWallet)
    )

    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = ColorTarjetaAjustes,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                "Filtrar por tipo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tipos.forEach { (tipo, par) ->
                    val (nombre, icono) = par
                    val seleccionado = filtroActual == tipo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) Ambar.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                alSeleccionarTipo(tipo)
                                alCerrar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = if (seleccionado) Ambar else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (seleccionado) Ambar else TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    )
}

@Composable
fun DialogoOrdenacionLista(
    criterioActual: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = ColorTarjetaAjustes,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                "Ordenar por",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CriterioOrdenacion.entries.forEach { criterio ->
                    val seleccionado = criterio == criterioActual
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) Ambar.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                alSeleccionarCriterio(criterio)
                                alCerrar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (seleccionado) Ambar else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = criterio.etiqueta,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (seleccionado) Ambar else TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    )
}

@Composable
fun CampoBusquedaLista(valor: String, alCambiar: (String) -> Unit) {
    val forma = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = if (valor.isBlank()) TextoSecundario else Ambar, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(Ambar),
            modifier = Modifier.weight(1f)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = TextoSecundario,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun SelectorFiltros(
    filtro: TipoEntrada?,
    soloFavoritos: Boolean,
    alSeleccionarTipo: (TipoEntrada?) -> Unit,
    alFavoritos: () -> Unit,
    alTodo: () -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val etiqueta = when {
        filtro == null && !soloFavoritos -> "Todo"
        filtro == TipoEntrada.LOGIN && soloFavoritos -> "Claves + ★"
        filtro == TipoEntrada.PASSKEY && soloFavoritos -> "Passkeys + ★"
        filtro == TipoEntrada.NOTA && soloFavoritos -> "Notas + ★"
        filtro == TipoEntrada.TARJETA && soloFavoritos -> "Tarjetas + ★"
        filtro == TipoEntrada.WIFI && soloFavoritos -> "Wi-Fi + ★"
        filtro == TipoEntrada.CUENTA_BANCARIA && soloFavoritos -> "Cuentas + ★"
        filtro == TipoEntrada.IDENTIDAD && soloFavoritos -> "Identidad + ★"
        filtro == TipoEntrada.SERVIDOR && soloFavoritos -> "Servidores + ★"
        filtro == TipoEntrada.WALLET && soloFavoritos -> "Wallets + ★"
        filtro == TipoEntrada.LOGIN -> "Claves"
        filtro == TipoEntrada.PASSKEY -> "Passkeys"
        filtro == TipoEntrada.NOTA -> "Notas"
        filtro == TipoEntrada.TARJETA -> "Tarjetas"
        filtro == TipoEntrada.WIFI -> "Redes Wi-Fi"
        filtro == TipoEntrada.CUENTA_BANCARIA -> "Cuentas bancarias"
        filtro == TipoEntrada.IDENTIDAD -> "Identidad"
        filtro == TipoEntrada.SERVIDOR -> "Servidores"
        filtro == TipoEntrada.WALLET -> "Cripto Wallets"
        soloFavoritos -> "Favoritos"
        else -> "Filtros"
    }
    val forma = RoundedCornerShape(12.dp)

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .clickable { desplegado = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Tune,
                contentDescription = null,
                tint = if (desplegado || filtro != null || soloFavoritos) Ambar else TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(etiqueta, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium, maxLines = 1, modifier = Modifier.weight(1f))
            Icon(
                if (desplegado) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir filtros",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = desplegado,
            onDismissRequest = { desplegado = false },
            modifier = Modifier.widthIn(min = 180.dp)
        ) {
            OpcionFiltro("Todo", Icons.Filled.SelectAll, filtro == null && !soloFavoritos) {
                alTodo()
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Claves", Icons.Filled.Lock, filtro == TipoEntrada.LOGIN) {
                alSeleccionarTipo(TipoEntrada.LOGIN)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Passkeys", Icons.Filled.Fingerprint, filtro == TipoEntrada.PASSKEY) {
                alSeleccionarTipo(TipoEntrada.PASSKEY)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Notas", Icons.Filled.Description, filtro == TipoEntrada.NOTA) {
                alSeleccionarTipo(TipoEntrada.NOTA)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Tarjetas bancarias", Icons.Filled.CreditCard, filtro == TipoEntrada.TARJETA) {
                alSeleccionarTipo(TipoEntrada.TARJETA)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Redes Wi-Fi", Icons.Filled.Wifi, filtro == TipoEntrada.WIFI) {
                alSeleccionarTipo(TipoEntrada.WIFI)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Cuentas bancarias", Icons.Filled.AccountBalance, filtro == TipoEntrada.CUENTA_BANCARIA) {
                alSeleccionarTipo(TipoEntrada.CUENTA_BANCARIA)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Documentos de identidad", Icons.Filled.Badge, filtro == TipoEntrada.IDENTIDAD) {
                alSeleccionarTipo(TipoEntrada.IDENTIDAD)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Servidores / SSH", Icons.Filled.Dns, filtro == TipoEntrada.SERVIDOR) {
                alSeleccionarTipo(TipoEntrada.SERVIDOR)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Cripto Wallets", Icons.Filled.AccountBalanceWallet, filtro == TipoEntrada.WALLET) {
                alSeleccionarTipo(TipoEntrada.WALLET)
                desplegado = false
            }
            SeparadorOpcionMenu()
            OpcionFiltro("Solo favoritos", Icons.Filled.Star, soloFavoritos) {
                alFavoritos()
                desplegado = false
            }
        }
    }
}

@Composable
fun OpcionFiltro(texto: String, icono: ImageVector, activo: Boolean, alPulsar: () -> Unit) {
    DropdownMenuItem(
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = if (activo) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        },
        text = {
            Text(
                texto,
                color = if (activo) ColorAcento else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingIcon = {
            if (activo) Icon(Icons.Filled.Check, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(18.dp))
        },
        onClick = alPulsar
    )
}

@Composable
fun SelectorOrdenacion(
    criterio: CriterioOrdenacion,
    alCambiar: (CriterioOrdenacion) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(12.dp)

    Box {
        Row(
            modifier = Modifier
                .height(42.dp)
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .clickable { desplegado = true }
                .padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Ordenar lista",
                tint = if (desplegado) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            CriterioOrdenacion.entries.forEachIndexed { index, op ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val activo = op == criterio
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (activo) ColorAcento else TextoSecundario
                        )
                    },
                    trailingIcon = if (activo) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(18.dp)) }
                    } else null,
                    text = {
                        Text(
                            op.etiqueta,
                            color = if (activo) ColorTitulos else TextoPrincipal,
                            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        alCambiar(op)
                        desplegado = false
                    }
                )
            }
        }
    }
}
