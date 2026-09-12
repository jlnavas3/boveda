package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepoDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import java.util.concurrent.TimeUnit

/** A partir de cuántos días sin cambiar se avisa de que una contraseña lleva tiempo igual. */
private const val DIAS_AVISO_ANTIGUEDAD = 180L

@Composable
fun PantallaSaludBoveda(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
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

    LaunchedEffect(claves.size) {
        val resumen = "Auditoría de salud ejecutada: ${claves.size} claves analizadas (${debiles.size} débiles, ${duplicadas.size} grupos repetidos, ${muyComunes.size} comunes, ${antiguas.size} antiguas)"
        Diagnostico.apuntar("salud", resumen)
    }

    val espaciado = EspaciadoComponentes.coerceAtLeast(10.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        CabeceraPantalla(
            titulo = "Salud de la Bóveda",
            subtitulo = "Auditoría de contraseñas repetidas, débiles o antiguas",
            alVolver = { vm.volverAtras() }
        )

        Spacer(Modifier.height(8.dp))

        // Tarjeta de Resumen General
        TarjetaPepo {
            EtiquetaSeccion("Resumen")
            Spacer(Modifier.height(8.dp))
            FilaResumen("Contraseñas analizadas", claves.size.toString(), ColorTitulos)
            FilaResumen("Repetidas (en grupos)", duplicadas.sumOf { it.size }.toString(), if (duplicadas.isEmpty()) Menta else Peligro)
            FilaResumen("Muy comunes o filtradas", muyComunes.size.toString(), if (muyComunes.isEmpty()) Menta else Peligro)
            FilaResumen("Débiles", debiles.size.toString(), if (debiles.isEmpty()) Menta else Peligro)
            FilaResumen("Sin actualizar (> $DIAS_AVISO_ANTIGUEDAD días)", antiguas.size.toString(), if (antiguas.isEmpty()) Menta else ColorAcento)
        }

        // 1. Sección: Contraseñas repetidas con Tarjetas Desplegables anidadas
        if (duplicadas.isNotEmpty()) {
            Spacer(Modifier.height(espaciado))
            TarjetaPepoDesplegable(
                titulo = "Contraseñas repetidas (${duplicadas.sumOf { it.size }})",
                descripcion = "${duplicadas.size} grupos de cuentas con la misma clave",
                icono = Icons.Filled.LockReset,
                colorIcono = Peligro,
                inicialmenteAbierta = false
            ) {
                Text(
                    text = "Usar la misma contraseña en varias cuentas significa que si una se filtra, todas las demás se ven comprometidas de inmediato.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))

                duplicadas.forEachIndexed { indice, grupo ->
                    val nombresGrupo = grupo.take(2).joinToString { it.titulo.ifBlank { "Sin título" } } +
                        if (grupo.size > 2) ", +${grupo.size - 2} más" else ""

                    TarjetaPepoDesplegable(
                        titulo = "Grupo ${indice + 1} (${grupo.size} entradas)",
                        descripcion = nombresGrupo,
                        icono = Icons.Filled.Key,
                        colorIcono = Peligro,
                        inicialmenteAbierta = false
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            grupo.forEach { entrada ->
                                FilaProblema(entrada = entrada) {
                                    vm.ir(Pantalla.Detalle(entrada.id))
                                }
                            }
                        }
                    }
                    if (indice < duplicadas.size - 1) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }

        // 2. Sección: Contraseñas muy comunes o filtradas
        if (muyComunes.isNotEmpty()) {
            Spacer(Modifier.height(espaciado))
            TarjetaPepoDesplegable(
                titulo = "Contraseñas muy comunes (${muyComunes.size})",
                descripcion = "Coinciden con filtraciones públicas globales",
                icono = Icons.Filled.Warning,
                colorIcono = Peligro,
                inicialmenteAbierta = false
            ) {
                Text(
                    text = "Están presentes en los listados mundiales de contraseñas filtradas más frecuentes. Son las primeras que un atacante prueba.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    muyComunes.forEach { entrada ->
                        FilaProblema(
                            entrada = entrada,
                            detalle = "Frecuente",
                            colorDetalle = Peligro
                        ) {
                            vm.ir(Pantalla.Detalle(entrada.id))
                        }
                    }
                }
            }
        }

        // 3. Sección: Contraseñas débiles
        if (debiles.isNotEmpty()) {
            Spacer(Modifier.height(espaciado))
            TarjetaPepoDesplegable(
                titulo = "Contraseñas débiles (${debiles.size})",
                descripcion = "Fáciles de descifrar por baja entropía o longitud",
                icono = Icons.Filled.LockOpen,
                colorIcono = Peligro,
                inicialmenteAbierta = false
            ) {
                Text(
                    text = "Se pueden descifrar rápidamente por fuerza bruta o coincidir con patrones sencillos y predecibles.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    debiles.forEach { entrada ->
                        val fuerza = MedidorFuerza.medir(entrada.contrasena)
                        FilaProblema(
                            entrada = entrada,
                            detalle = fuerza.etiqueta,
                            colorDetalle = Peligro
                        ) {
                            vm.ir(Pantalla.Detalle(entrada.id))
                        }
                    }
                }
            }
        }

        // 4. Sección: Sin cambiar hace tiempo
        if (antiguas.isNotEmpty()) {
            Spacer(Modifier.height(espaciado))
            TarjetaPepoDesplegable(
                titulo = "Sin cambiar hace tiempo (${antiguas.size})",
                descripcion = "Más de $DIAS_AVISO_ANTIGUEDAD días sin actualizarse",
                icono = Icons.Filled.History,
                colorIcono = ColorAcento,
                inicialmenteAbierta = false
            ) {
                Text(
                    text = "Rotar credenciales periódicamente limita el alcance de brechas de seguridad silenciosas no divulgadas.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    antiguas.forEach { entrada ->
                        val dias = diasDesde(entrada.modificadaEn, ahora)
                        FilaProblema(
                            entrada = entrada,
                            detalle = "hace $dias d",
                            colorDetalle = ColorAcento
                        ) {
                            vm.ir(Pantalla.Detalle(entrada.id))
                        }
                    }
                }
            }
        }

        // Mensaje de estado perfecto si no hay ninguna advertencia
        if (claves.isNotEmpty() && duplicadas.isEmpty() && muyComunes.isEmpty() && debiles.isEmpty() && antiguas.isEmpty()) {
            Spacer(Modifier.height(espaciado))
            TarjetaPepo {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = null,
                        tint = Menta,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "¡Excelente salud de la bóveda! Tus contraseñas no se repiten, tienen alta fortaleza y se han mantenido actualizadas.",
                        color = Menta,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        Text(valor, color = color, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun FilaProblema(
    entrada: Entrada,
    detalle: String? = null,
    colorDetalle: Color = TextoSecundario,
    alAbrir: () -> Unit
) {
    val forma = FormaCampo
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alAbrir() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entrada.titulo.ifBlank { "Sin título" },
                color = ColorTitulos,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1
            )
            val subtitulo = entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
            Text(
                text = subtitulo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
        if (detalle != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = detalle,
                color = colorDetalle,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Ver entrada",
            tint = TextoSecundario,
            modifier = Modifier.size(14.dp)
        )
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))
