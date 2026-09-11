package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Slider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import kotlin.math.roundToInt

@Composable
fun PantallaEdicion(vm: VaultViewModel, id: String?, contrasenaInicial: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val original = remember(id) { id?.let { vm.entrada(it) } }

    var tipo by remember { mutableStateOf(original?.tipo ?: TipoEntrada.LOGIN) }
    var titulo by remember { mutableStateOf(original?.titulo ?: "") }
    var usuario by remember { mutableStateOf(original?.usuario ?: "") }
    var contrasena by remember { mutableStateOf(original?.contrasena ?: contrasenaInicial) }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var urls by remember { mutableStateOf(original?.urls?.joinToString(", ") ?: "") }
    var notas by remember { mutableStateOf(original?.notas ?: "") }
    var totp by remember { mutableStateOf(original?.secretoTotp ?: "") }
    var favorito by remember { mutableStateOf(original?.favorito ?: false) }
    var etiquetas by remember { mutableStateOf(original?.etiquetas ?: emptyList()) }
    var nuevaEtiqueta by remember { mutableStateOf("") }
    var camposPersonalizados by remember { mutableStateOf(original?.camposPersonalizados ?: emptyList()) }
    var opcionesGenerador by remember { mutableStateOf(OpcionesGenerador()) }
    val etiquetasSugeridas = remember { vm.etiquetasUsadas() }

    val totpValido = totp.isBlank() || Base32.esValido(totp)
    val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
    val esComun = remember(contrasena) { ContrasenasComunes.esComun(contexto, contrasena) }
    val puedeGuardar = titulo.isNotBlank() && totpValido

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = if (original == null) "Nueva Entrada" else "Editar Entrada",
            subtitulo = if (original == null) "Crea y cifra un registro en la bóveda" else "Modifica los datos del registro",
            alVolver = { vm.volverAtras() }
        )

        if (original?.passkey == null) {
            EtiquetaSeccion("Tipo")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SelectorTipo("Contraseña", tipo == TipoEntrada.LOGIN) { tipo = TipoEntrada.LOGIN }
                SelectorTipo("Nota segura", tipo == TipoEntrada.NOTA) { tipo = TipoEntrada.NOTA }
            }
            Spacer(Modifier.height(16.dp))
        }

        CampoPepo(valor = titulo, etiqueta = "Título", alCambiar = { titulo = it })
        Spacer(Modifier.height(12.dp))

        if (tipo != TipoEntrada.NOTA) {
            CampoPepo(valor = usuario, etiqueta = "Usuario o correo", alCambiar = { usuario = it })
            Spacer(Modifier.height(12.dp))
            CampoPepo(
                valor = contrasena,
                etiqueta = "Contraseña",
                alCambiar = { contrasena = it },
                esContrasena = true,
                mostrarContrasena = mostrarContrasena,
                monoespaciada = true
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { mostrarContrasena = !mostrarContrasena },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(
                    onClick = {
                        haptica.toque()
                        contrasena = PasswordGenerator.generar(opcionesGenerador)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = "Generar contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(
                    onClick = { vm.ir(Pantalla.Generador) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = "Abrir generador completo",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonModoCompacto("Aleatoria", !opcionesGenerador.modoFrase && !opcionesGenerador.modoPatron) {
                    opcionesGenerador = opcionesGenerador.copy(modoFrase = false, modoPatron = false)
                }
                BotonModoCompacto("Diceware (${opcionesGenerador.palabras} pal.)", opcionesGenerador.modoFrase) {
                    opcionesGenerador = opcionesGenerador.copy(modoFrase = true, modoPatron = false)
                }
                BotonModoCompacto("Por patrón", opcionesGenerador.modoPatron) {
                    opcionesGenerador = opcionesGenerador.copy(modoFrase = false, modoPatron = true)
                }
            }
            Spacer(Modifier.height(8.dp))

            when {
                opcionesGenerador.modoFrase -> {
                    EtiquetaSeccion("Palabras Diceware: ${opcionesGenerador.palabras}")
                    Slider(
                        value = opcionesGenerador.palabras.toFloat(),
                        onValueChange = {
                            val nuevo = it.roundToInt().coerceIn(3, 12)
                            if (nuevo != opcionesGenerador.palabras) {
                                haptica.tic()
                                opcionesGenerador = opcionesGenerador.copy(palabras = nuevo)
                            }
                        },
                        valueRange = 3f..12f,
                        steps = 8,
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = Ambar,
                            activeTrackColor = Ambar,
                            inactiveTrackColor = Borde
                        )
                    )
                }
                opcionesGenerador.modoPatron -> {
                    CampoPepo(
                        valor = opcionesGenerador.patron,
                        etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX-XXXXX)",
                        alCambiar = { opcionesGenerador = opcionesGenerador.copy(patron = it) },
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "X: alfanum | A: mayús | a: minús | 9: dígito | w: palabra Diceware",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                else -> {
                    EtiquetaSeccion("Ajuste rápido: ${opcionesGenerador.longitud} caracteres")
                    Slider(
                        value = opcionesGenerador.longitud.toFloat(),
                        onValueChange = { opcionesGenerador = opcionesGenerador.copy(longitud = it.roundToInt().coerceIn(8, 64)) },
                        valueRange = 8f..64f,
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = Ambar,
                            activeTrackColor = Ambar,
                            inactiveTrackColor = Borde
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OpcionGeneradorCompacta("A-Z", opcionesGenerador.mayusculas) { opcionesGenerador = opcionesGenerador.copy(mayusculas = it) }
                        OpcionGeneradorCompacta("a-z", opcionesGenerador.minusculas) { opcionesGenerador = opcionesGenerador.copy(minusculas = it) }
                        OpcionGeneradorCompacta("0-9", opcionesGenerador.digitos) { opcionesGenerador = opcionesGenerador.copy(digitos = it) }
                        OpcionGeneradorCompacta("Símbolos", opcionesGenerador.simbolos) { opcionesGenerador = opcionesGenerador.copy(simbolos = it) }
                    }
                }
            }
            if (contrasena.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                BarraFuerza(fuerza.fraccion, fuerza.etiqueta, fuerza.tiempo)
                if (esComun) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Está entre las contraseñas más repetidas en filtraciones conocidas: cualquiera la prueba primero.",
                        color = Peligro,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            CampoPepo(
                valor = urls,
                etiqueta = "Sitios o paquetes (separados por comas)",
                alCambiar = { urls = it }
            )
            Spacer(Modifier.height(12.dp))
            CampoPepo(
                valor = totp,
                etiqueta = "Secreto TOTP en Base32 (opcional)",
                alCambiar = { totp = it.uppercase() },
                monoespaciada = true
            )
            if (!totpValido) {
                Spacer(Modifier.height(6.dp))
                Text("Ese secreto no es Base32 válido", color = Peligro, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(12.dp))
        }

        CampoPepo(valor = notas, etiqueta = "Notas", alCambiar = { notas = it }, varias = true)
        Spacer(Modifier.height(16.dp))

        EtiquetaSeccion("Campos personalizados")
        Spacer(Modifier.height(8.dp))
        if (camposPersonalizados.isEmpty()) {
            Text(
                "Añade datos extra como PIN secundario, preguntas de seguridad o claves de recuperación.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Spacer(Modifier.height(8.dp))
        } else {
            camposPersonalizados.forEachIndexed { indice, campo ->
                TarjetaCampoPersonalizadoEdicion(
                    numero = indice + 1,
                    campo = campo,
                    alModificar = { modificado ->
                        camposPersonalizados = camposPersonalizados.toMutableList().apply {
                            set(indice, modificado)
                        }
                    },
                    alEliminar = {
                        haptica.tic()
                        camposPersonalizados = camposPersonalizados.toMutableList().apply {
                            removeAt(indice)
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
            }
        }

        BotonBorde("+ Añadir campo personalizado") {
            haptica.tic()
            camposPersonalizados = camposPersonalizados + CampoPersonalizado(
                etiqueta = "",
                valor = "",
                tipo = TipoCampo.TEXTO
            )
        }
        Spacer(Modifier.height(16.dp))

        EtiquetaSeccion("Etiquetas")
        Spacer(Modifier.height(8.dp))
        if (etiquetas.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                etiquetas.forEach { etiqueta ->
                    ChipEtiqueta(etiqueta) { etiquetas = etiquetas - etiqueta }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                CampoPepo(
                    valor = nuevaEtiqueta,
                    etiqueta = "Nueva etiqueta",
                    alCambiar = { nuevaEtiqueta = it },
                    modifier = Modifier.height(56.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.width(96.dp)) {
                BotonBorde("Añadir") {
                    val limpia = normalizarEtiqueta(nuevaEtiqueta)
                    if (limpia.isNotEmpty() && !etiquetas.contains(limpia)) {
                        etiquetas = etiquetas + limpia
                    }
                    nuevaEtiqueta = ""
                }
            }
        }
        val sugerenciasRestantes = etiquetasSugeridas.filterNot { etiquetas.contains(it) }
        if (sugerenciasRestantes.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text("Ya usadas en otras entradas", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sugerenciasRestantes.forEach { etiqueta ->
                    ChipEtiqueta(etiqueta, sugerida = true) {
                        val normalizada = normalizarEtiqueta(etiqueta)
                        if (normalizada.isNotEmpty() && !etiquetas.contains(normalizada)) {
                            etiquetas = etiquetas + normalizada
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaTarjeta)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                        Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                    else Modifier
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Favorito", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
                Text("Aparece arriba en la lista", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = favorito,
                onCheckedChange = { favorito = it; haptica.tic() },
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

        Spacer(Modifier.height(20.dp))
        BotonColorido(
            texto = if (original == null) "Guardar en la bóveda" else "Guardar cambios",
            color = ColorAcento,
            activo = puedeGuardar
        ) {
            haptica.exito()
            val entrada = Entrada(
                id = original?.id ?: vm.nuevoId(),
                tipo = original?.passkey?.let { TipoEntrada.PASSKEY } ?: tipo,
                titulo = titulo.trim(),
                usuario = usuario.trim(),
                contrasena = contrasena,
                urls = urls.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                notas = notas,
                secretoTotp = totp.trim().ifBlank { null },
                favorito = favorito,
                creadaEn = original?.creadaEn ?: 0L,
                etiquetas = etiquetas.map(::normalizarEtiqueta).filter { it.isNotEmpty() }.distinct(),
                passkey = original?.passkey,
                camposPersonalizados = camposPersonalizados.filter { it.etiqueta.isNotBlank() || it.valor.isNotBlank() }
            )
            vm.guardar(entrada)
            if (original == null) vm.volverAtras() else vm.ir(Pantalla.Detalle(entrada.id))
        }
        Spacer(Modifier.height(12.dp))
        BotonBorde("Cancelar") {
            vm.volverAtras()
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ChipEtiqueta(texto: String, sugerida: Boolean = false, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Row(
        modifier = Modifier
            .clip(forma)
            .background(if (sugerida) Superficie else Borde)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#${normalizarEtiqueta(texto)}", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
        if (!sugerida) {
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Filled.Close, contentDescription = "Quitar etiqueta", tint = TextoSecundario, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun OpcionGeneradorCompacta(
    texto: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(78.dp)) {
        Text(texto, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        Switch(
            checked = activo,
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
private fun SelectorTipo(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaBoton
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(texto, color = if (activo) ColorSobreAcento else TextoSecundario, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun BotonModoCompacto(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelMedium,
            color = if (activo) ColorSobreAcento else TextoSecundario
        )
    }
}

@Composable
private fun TarjetaCampoPersonalizadoEdicion(
    numero: Int,
    campo: CampoPersonalizado,
    alModificar: (CampoPersonalizado) -> Unit,
    alEliminar: () -> Unit
) {
    var mostrarValor by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Campo #$numero",
                style = MaterialTheme.typography.titleSmall,
                color = ColorTitulos
            )
            IconButton(onClick = alEliminar, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar campo", tint = Peligro, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoCampo.entries.forEach { t ->
                val activo = campo.tipo == t
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(FormaPequena)
                        .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
                        .then(
                            if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, FormaPequena)
                            else Modifier
                        )
                        .clickable { alModificar(campo.copy(tipo = t)) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        t.etiqueta,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (activo) ColorSobreAcento else TextoSecundario
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        CampoPepo(
            valor = campo.etiqueta,
            etiqueta = "Nombre (ej. PIN Cajero, Pregunta de seguridad)",
            alCambiar = { alModificar(campo.copy(etiqueta = it)) }
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                CampoPepo(
                    valor = campo.valor,
                    etiqueta = if (campo.tipo == TipoCampo.PIN) "Valor del PIN" else "Valor del campo",
                    alCambiar = { alModificar(campo.copy(valor = it)) },
                    esContrasena = campo.tipo != TipoCampo.TEXTO,
                    mostrarContrasena = mostrarValor,
                    monoespaciada = campo.tipo != TipoCampo.TEXTO
                )
            }
            if (campo.tipo != TipoCampo.TEXTO) {
                IconButton(onClick = { mostrarValor = !mostrarValor }) {
                    Icon(
                        if (mostrarValor) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Mostrar/ocultar valor",
                        tint = ColorIconosInternos
                    )
                }
            }
        }
    }
}
