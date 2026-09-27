package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.BarraSuperiorDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.CabeceraHeroDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.EstadoEntradaNoEncontrada
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.PieMetadatosDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCamposDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCredencialesDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaEtiquetasDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaHistorialDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaNotasDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaPasskeyDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaSitiosYAppsDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaTotpDetalle
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

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

    if (entrada == null) {
        EstadoEntradaNoEncontrada(alVolver = { vm.volverAtras() })
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
        BarraSuperiorDetalle(
            esFavorito = entrada.favorito,
            conSeparador = scrollState.value > 0,
            alVolver = { vm.volverAtras() },
            alCompartirQr = {
                haptica.tic()
                mostrarDialogoQr = true
            },
            alAlternarFavorito = {
                haptica.tic()
                vm.alternarFavorito(entrada.id)
            },
            alEditar = {
                haptica.tic()
                vm.ir(Pantalla.Editar(entrada.id))
            },
            alMoverAPapelera = {
                haptica.tic()
                confirmarBorrado = true
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
            TarjetaEtiquetasDetalle(
                etiquetas = entrada.etiquetas,
                alFiltrarPorEtiqueta = { etiqueta ->
                    vm.filtrarPorEtiqueta(etiqueta)
                    vm.volverALista()
                }
            )
            if (entrada.etiquetas.isNotEmpty()) {
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
            PieMetadatosDetalle(
                creadaEn = entrada.creadaEn,
                modificadaEn = entrada.modificadaEn
            )
            if (entrada.creadaEn > 0L || entrada.modificadaEn > 0L) {
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
