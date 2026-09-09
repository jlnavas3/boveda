package com.pepotech.pepoboveda.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.EstadoBoveda
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.ui.Pantalla
import com.pepotech.pepoboveda.ui.VaultViewModel
import com.pepotech.pepoboveda.ui.componentes.BotonBorde
import com.pepotech.pepoboveda.ui.componentes.EtiquetaSeccion
import com.pepotech.pepoboveda.ui.componentes.TarjetaPepo
import com.pepotech.pepoboveda.ui.theme.Ambar
import com.pepotech.pepoboveda.ui.theme.Menta
import com.pepotech.pepoboveda.ui.theme.Peligro
import com.pepotech.pepoboveda.ui.theme.Superficie
import com.pepotech.pepoboveda.ui.theme.TextoPrincipal
import com.pepotech.pepoboveda.ui.theme.TextoSecundario
import com.pepotech.pepoboveda.util.ItemAgrupado
import com.pepotech.pepoboveda.util.MedidorFuerza
import com.pepotech.pepoboveda.util.ContrasenasComunes
import com.pepotech.pepoboveda.util.construirItemsAgrupadosPorSitio
import java.util.concurrent.TimeUnit

/** A partir de cuántos días sin cambiar se avisa de que una contraseña "lleva tiempo igual". */
private const val DIAS_AVISO_ANTIGUEDAD = 180L

@Composable
fun PantallaSaludBoveda(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val claves = remember(entradas) { entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() } }

    val duplicadas = remember(claves) {
        claves.groupBy { it.contrasena }.values.filter { it.size > 1 }
    }
    val debiles = remember(claves) {
        claves.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }
            .sortedBy { MedidorFuerza.medir(it.contrasena).puntuacion }
    }
    val muyComunes = remember(claves) {
        claves.filter { ContrasenasComunes.esComun(contexto, it.contrasena) }
    }
    val ahora = remember { System.currentTimeMillis() }
    val antiguas = remember(claves) {
        claves.filter { it.modificadaEn > 0 && diasDesde(it.modificadaEn, ahora) >= DIAS_AVISO_ANTIGUEDAD }
            .sortedBy { it.modificadaEn }
    }

    // Grupos por sitio expandidos a mano, uno por sección para no mezclar estados.
    var sitiosDuplicadas by remember { mutableStateOf(setOf<String>()) }
    var sitiosDebiles by remember { mutableStateOf(setOf<String>()) }
    var sitiosComunes by remember { mutableStateOf(setOf<String>()) }
    var sitiosAntiguas by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Salud de la bóveda", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
        Text(
            "Todo esto se calcula en el móvil, comparando tus propias entradas y una lista de contraseñas filtradas que va dentro de la app. Nada sale de aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(18.dp))

        TarjetaPepo {
            EtiquetaSeccion("Resumen")
            Spacer(Modifier.height(8.dp))
            FilaResumen("Contraseñas guardadas", claves.size.toString(), Ambar)
            FilaResumen("Repetidas en más de una entrada", duplicadas.sumOf { it.size }.toString(), if (duplicadas.isEmpty()) Menta else Peligro)
            FilaResumen("Muy comunes o filtradas", muyComunes.size.toString(), if (muyComunes.isEmpty()) Menta else Peligro)
            FilaResumen("Débiles", debiles.size.toString(), if (debiles.isEmpty()) Menta else Peligro)
            FilaResumen("Sin cambiar hace más de $DIAS_AVISO_ANTIGUEDAD días", antiguas.size.toString(), if (antiguas.isEmpty()) Menta else Ambar)
        }

        if (duplicadas.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            SeccionColapsable(
                titulo = "Contraseñas repetidas",
                contador = duplicadas.sumOf { it.size },
                colorContador = Peligro,
                descripcion = "Usar la misma contraseña en varias cuentas significa que si una se filtra, las demás caen con ella."
            ) {
                duplicadas.forEachIndexed { indice, grupo ->
                    Text(
                        "Grupo ${indice + 1} · ${grupo.size} entradas",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = if (indice == 0) 0.dp else 10.dp, bottom = 4.dp)
                    )
                    FilaProblemasAgrupados(
                        entradas = grupo,
                        sitiosExpandidos = sitiosDuplicadas,
                        alAlternarSitio = { clave ->
                            sitiosDuplicadas = if (sitiosDuplicadas.contains(clave)) sitiosDuplicadas - clave else sitiosDuplicadas + clave
                        },
                        alAbrir = { vm.ir(Pantalla.Detalle(it)) }
                    )
                }
            }
        }

        if (muyComunes.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            SeccionColapsable(
                titulo = "Contraseñas muy comunes",
                contador = muyComunes.size,
                colorContador = Peligro,
                descripcion = "Están en la lista de las contraseñas más repetidas en filtraciones conocidas: son las primeras que prueba cualquiera."
            ) {
                FilaProblemasAgrupados(
                    entradas = muyComunes,
                    sitiosExpandidos = sitiosComunes,
                    alAlternarSitio = { clave ->
                        sitiosComunes = if (sitiosComunes.contains(clave)) sitiosComunes - clave else sitiosComunes + clave
                    },
                    alAbrir = { vm.ir(Pantalla.Detalle(it)) }
                )
            }
        }

        if (debiles.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            SeccionColapsable(
                titulo = "Contraseñas débiles",
                contador = debiles.size,
                colorContador = Peligro,
                descripcion = "Se adivinan rápido: cortas, muy comunes o con patrones previsibles."
            ) {
                FilaProblemasAgrupados(
                    entradas = debiles,
                    sitiosExpandidos = sitiosDebiles,
                    alAlternarSitio = { clave ->
                        sitiosDebiles = if (sitiosDebiles.contains(clave)) sitiosDebiles - clave else sitiosDebiles + clave
                    },
                    alAbrir = { vm.ir(Pantalla.Detalle(it)) }
                )
            }
        }

        if (antiguas.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            SeccionColapsable(
                titulo = "Sin cambiar hace tiempo",
                contador = antiguas.size,
                colorContador = Ambar,
                descripcion = "No está mal por sí solo, pero rotarlas de vez en cuando limita el daño de una filtración que no sepas que ocurrió."
            ) {
                FilaProblemasAgrupados(
                    entradas = antiguas,
                    sitiosExpandidos = sitiosAntiguas,
                    alAlternarSitio = { clave ->
                        sitiosAntiguas = if (sitiosAntiguas.contains(clave)) sitiosAntiguas - clave else sitiosAntiguas + clave
                    },
                    alAbrir = { vm.ir(Pantalla.Detalle(it)) },
                    detalle = { entrada -> "hace ${diasDesde(entrada.modificadaEn, ahora)} días" }
                )
            }
        }

        if (claves.isNotEmpty() && duplicadas.isEmpty() && muyComunes.isEmpty() && debiles.isEmpty() && antiguas.isEmpty()) {
            Spacer(Modifier.height(14.dp))
            TarjetaPepo {
                Text("Nada que avisar: tus contraseñas no se repiten, no son débiles y las has rotado hace poco.", color = Menta, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(20.dp))
        BotonBorde("Volver") { vm.volverALista() }
        Spacer(Modifier.height(40.dp))
    }
}

/** Tarjeta con cabecera tocable que pliega su contenido: las listas largas no invaden la pantalla. */
@Composable
private fun SeccionColapsable(
    titulo: String,
    contador: Int,
    colorContador: Color,
    descripcion: String,
    contenido: @Composable () -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    TarjetaPepo {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandido = !expandido },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EtiquetaSeccion(titulo)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$contador", color = colorContador, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(6.dp))
                Icon(
                    if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Contraer" else "Expandir",
                    tint = TextoSecundario
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        if (expandido) {
            Spacer(Modifier.height(10.dp))
            contenido()
        }
    }
}

/** La misma lista, pero agrupando por sitio como en la lista principal: nada de ver
 * "accounts.google.com" repetido doce veces si son doce cuentas del mismo sitio. */
@Composable
private fun FilaProblemasAgrupados(
    entradas: List<Entrada>,
    sitiosExpandidos: Set<String>,
    alAlternarSitio: (String) -> Unit,
    alAbrir: (String) -> Unit,
    detalle: (Entrada) -> String? = { null }
) {
    val items = construirItemsAgrupadosPorSitio(entradas) { clave -> sitiosExpandidos.contains(clave) }
    Column {
        items.forEach { item ->
            when (item) {
                is ItemAgrupado.Grupo -> FilaGrupoProblema(
                    clave = item.clave,
                    cantidad = item.entradas.size,
                    expandido = sitiosExpandidos.contains(item.clave),
                    alAlternar = { alAlternarSitio(item.clave) }
                )
                is ItemAgrupado.Suelto -> FilaProblema(item.entrada, detalle(item.entrada)) { alAbrir(item.entrada.id) }
                is ItemAgrupado.Hijo -> Column(modifier = Modifier.padding(start = 16.dp)) {
                    FilaProblema(item.entrada, detalle(item.entrada)) { alAbrir(item.entrada.id) }
                }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun FilaGrupoProblema(clave: String, cantidad: Int, expandido: Boolean, alAlternar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Superficie)
            .clickable { alAlternar() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(clave, color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            Text("$cantidad cuentas", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(
            if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = TextoSecundario
        )
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        Text(valor, color = color, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun FilaProblema(entrada: Entrada, detalle: String? = null, alAbrir: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Superficie)
            .clickable { alAbrir() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(entrada.titulo.ifBlank { "Sin título" }, color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            Text(
                entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" },
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (detalle != null) {
            Text(detalle, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))

