package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegablePepo
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
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

@Composable
fun BarraSeleccion(
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
fun BannerRecordatorioExportacion(dias: Long, alIr: () -> Unit) {
    val forma = FormaTarjeta
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
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
fun ChipFiltro(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
            .then(
                if (!activo && GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
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
fun CampoBusquedaLista(valor: String, alCambiar: (String) -> Unit) {
    val forma = FormaCampo
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, if (valor.isBlank()) ColorBordeActual else Ambar, forma)
                } else {
                    Modifier
                }
            )
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
            modifier = Modifier.weight(1f)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = TextoSecundario,
                    modifier = Modifier.size(18.dp)
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
    val forma = FormaCampo

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, if (desplegado || filtro != null || soloFavoritos) Ambar else ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
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

        MenuDesplegablePepo(
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
    val forma = FormaCampo

    Box {
        Row(
            modifier = Modifier
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, if (desplegado) ColorAcento else ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Ordenar lista",
                tint = if (desplegado) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegablePepo(
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
