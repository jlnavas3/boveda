package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

/**
 * Contenido con scroll de la pantalla de detalle que ensambla de forma modular
 * todas las tarjetas informativas y de credenciales de una entrada.
 */
@Composable
fun ContenidoEntradaDetalle(
    entrada: Entrada,
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    alMostrarQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    var revelada by remember(entrada.id) { mutableStateOf(false) }
    var ultimaCopia by remember(entrada.id) { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .reboteElastico()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Cabecera Hero de identidad
        CabeceraHeroDetalle(
            entrada = entrada,
            alMostrarQr = alMostrarQr,
            alActualizarTitulo = { nuevoTitulo ->
                haptica.exito()
                vm.guardar(entrada.copy(titulo = nuevoTitulo, modificadaEn = System.currentTimeMillis()))
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
                vm.registrarUsoEntrada(entrada.id)
                ultimaCopia = "usuario"
            },
            alCopiarContrasena = {
                haptica.exito()
                vm.copiar("Contraseña", entrada.contrasena, sensible = true)
                vm.registrarUsoEntrada(entrada.id)
                ultimaCopia = "contrasena"
            },
            ajustes = ajustes
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
                vm.registrarUsoEntrada(entrada.id)
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
            alAvisar = { vm.avisar(it) },
            alAbrirUrl = { vm.registrarUsoEntrada(entrada.id) }
        )
        if (entrada.urls.any { it.isNotBlank() }) {
            Spacer(Modifier.height(16.dp))
        }

        // Grupo 4: Campos personalizados
        TarjetaCamposDetalle(
            campos = entrada.camposPersonalizados,
            vm = vm,
            ajustes = ajustes,
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
            },
            ajustes = ajustes
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

        // Metadatos: Fechas de creación, edición y último uso
        PieMetadatosDetalle(
            creadaEn = entrada.creadaEn,
            modificadaEn = entrada.modificadaEn,
            ultimoUsoEn = entrada.ultimoUsoEn
        )
        if (entrada.creadaEn > 0L || entrada.modificadaEn > 0L || entrada.ultimoUsoEn > 0L) {
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(130.dp))
    }
}
