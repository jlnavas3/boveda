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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.CabeceraHeroDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCamposDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCredencialesDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaHistorialDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaNotasDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaPasskeyDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaSitiosYAppsDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaTotpDetalle
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaDetalle(vm: VaultViewModel, id: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val entrada = (estado as? EstadoBoveda.Desbloqueada)?.entradas?.find { it.id == id } ?: vm.entrada(id)
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var revelada by remember { mutableStateOf(false) }
    var confirmarBorrado by remember { mutableStateOf(false) }
    var mostrarDialogoQr by remember { mutableStateOf(false) }
    var ultimaCopia by remember { mutableStateOf<String?>(null) }

    val formatoFechaCompacta = remember {
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES"))
    }

    if (entrada == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Esta entrada ya no está en la bóveda", color = TextoSecundario)
            Spacer(Modifier.height(16.dp))
            BotonColorido(
                texto = "Volver",
                color = ColorAcento,
                icono = Icons.AutoMirrored.Filled.ArrowBack
            ) { vm.volverAtras() }
        }
        return
    }

    LaunchedEffect(entrada.id) {
        val tipoDesc = entrada.tipo.etiqueta.lowercase()
        Diagnostico.apuntar("bóveda", "Detalle de entrada consultado ($tipoDesc)")
    }

    LaunchedEffect(revelada) {
        if (revelada) {
            val tipoDesc = entrada.tipo.etiqueta.lowercase()
            Diagnostico.apuntar("seguridad", "Contraseña revelada en pantalla ($tipoDesc)")
        }
    }

    LaunchedEffect(ultimaCopia) {
        if (ultimaCopia != null) {
            delay(1500)
            ultimaCopia = null
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Barra superior plana nativa
        BarraSuperiorPantalla(
            titulo = "",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                BotonIconoCabecera(
                    onClick = { haptica.tic(); mostrarDialogoQr = true },
                    icono = Icons.Filled.QrCode,
                    descripcion = "Compartir por código QR"
                )
                BotonIconoCabecera(
                    onClick = { haptica.tic(); vm.alternarFavorito(entrada.id) },
                    icono = Icons.Filled.Star,
                    descripcion = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                    tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.4f)
                )
                BotonIconoCabecera(
                    onClick = { haptica.tic(); vm.ir(Pantalla.Editar(entrada.id)) },
                    icono = Icons.Filled.Edit,
                    descripcion = "Editar"
                )
                BotonIconoCabecera(
                    onClick = { haptica.tic(); confirmarBorrado = true },
                    icono = Icons.Filled.Delete,
                    descripcion = "Mover a papelera",
                    tint = Peligro
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cabecera Hero de identidad
            CabeceraHeroDetalle(
                entrada = entrada,
                alMostrarQr = {
                    haptica.tic()
                    mostrarDialogoQr = true
                }
            )

            Spacer(Modifier.height(14.dp))

            // Grupo 1: Credenciales principales
            TarjetaCredencialesDetalle(
                entrada = entrada,
                revelada = revelada,
                ultimaCopia = ultimaCopia,
                alAlternarRevelada = {
                    haptica.toque()
                    revelada = !revelada
                },
                alCopiarUsuario = {
                    haptica.toque()
                    vm.copiar("Usuario", entrada.usuario, sensible = false)
                    ultimaCopia = "usuario"
                },
                alCopiarContrasena = {
                    haptica.exito()
                    vm.copiar("Contraseña", entrada.contrasena, sensible = true)
                    ultimaCopia = "contrasena"
                }
            )
            if (entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 2: 2FA / TOTP modular
            TarjetaTotpDetalle(
                entrada = entrada,
                ajustes = ajustes,
                haptica = haptica,
                alCopiarTotp = { codigo ->
                    vm.copiar("Código TOTP", codigo, sensible = true)
                }
            )
            if (!entrada.secretoTotp.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 3: Sitios y apps asociados (si existen)
            TarjetaSitiosYAppsDetalle(
                urls = entrada.urls,
                alCopiarUrl = { url ->
                    vm.copiar("Enlace", url, sensible = false)
                },
                alAvisar = { vm.avisar(it) }
            )
            if (entrada.urls.any { it.isNotBlank() }) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 4: Campos personalizados
            TarjetaCamposDetalle(
                campos = entrada.camposPersonalizados,
                vm = vm,
                haptica = haptica,
                ultimaCopia = ultimaCopia,
                alCopiarCampo = { idCampo -> ultimaCopia = idCampo }
            )
            if (entrada.camposPersonalizados.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 5: Notas
            TarjetaNotasDetalle(
                notas = entrada.notas,
                ultimaCopia = ultimaCopia,
                alCopiarNotas = {
                    haptica.toque()
                    vm.copiar("Notas", entrada.notas, sensible = false)
                    ultimaCopia = "notas"
                }
            )
            if (entrada.notas.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 6: Etiquetas
            if (entrada.etiquetas.isNotEmpty()) {
                GrupoAjustes(etiqueta = "Etiquetas (${entrada.etiquetas.size})") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        entrada.etiquetas.forEach { etiqueta ->
                            val formaChip = FormaPequena
                            Box(
                                modifier = Modifier
                                    .clip(formaChip)
                                    .background(Borde)
                                    .then(
                                        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                                        else Modifier
                                    )
                                    .clickable {
                                        vm.filtrarPorEtiqueta(etiqueta)
                                        vm.volverALista()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("#${normalizarEtiqueta(etiqueta)}", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 7: Passkey
            entrada.passkey?.let { passkey ->
                TarjetaPasskeyDetalle(passkey = passkey, usuarioEntrada = entrada.usuario)
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 8: Historial de contraseñas anteriores
            TarjetaHistorialDetalle(
                entrada = entrada,
                vm = vm,
                haptica = haptica
            )
            if (entrada.historialContrasenas.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
            }

            // Metadatos: Fechas de creación y edición
            if (entrada.creadaEn > 0L || entrada.modificadaEn > 0L) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (entrada.creadaEn > 0L) {
                        Text(
                            text = "Creada: ${formatoFechaCompacta.format(Date(entrada.creadaEn))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }
                    if (entrada.modificadaEn > 0L && entrada.modificadaEn != entrada.creadaEn) {
                        Text(
                            text = "Editada: ${formatoFechaCompacta.format(Date(entrada.modificadaEn))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (confirmarBorrado) {
        DialogoConfirmacionBoveda(
            titulo = "¿Mover a la papelera?",
            mensaje = "Se puede restaurar desde Ajustes > Papelera durante 30 días; pasado ese tiempo se borra permanentemente.",
            textoConfirmar = "Mover a la papelera",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                confirmarBorrado = false
                haptica.error()
                vm.eliminar(entrada.id)
            },
            alDescartar = { confirmarBorrado = false }
        )
    }

    if (mostrarDialogoQr) {
        DialogoCompartirQr(
            entrada = entrada,
            alCerrar = { mostrarDialogoQr = false }
        )
    }
}
