package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

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
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo

@Composable
fun PantallaReglasNormalizacion(
    vm: VaultViewModel,
    alVolver: () -> Unit = { vm.volverAtras() }
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var mostrarDialogoAgregarPrefijo by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarTld by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarMarca by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarPuerto by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarOcteto by remember { mutableStateOf(false) }
    var mostrarDialogoRestablecer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Reglas de URLs y Redes",
            idEtiqueta = "03-LST-RGL",
            mostrarId = ajustes.mostrarIdsAjustes,
            alVolver = alVolver,
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SeccionChipsPrefijos(
                prefijos = ajustes.prefijosSubdominios,
                alAgregarClick = { mostrarDialogoAgregarPrefijo = true },
                alEliminarPrefijo = { vm.eliminarPrefijoSubdominio(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionChipsTldsDescartables(
                tlds = ajustes.tldsDescartables,
                alAgregarClick = { mostrarDialogoAgregarTld = true },
                alEliminarTld = { vm.eliminarTldDescartable(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionMarcasPersonalizadas(
                marcasPersonalizadas = ajustes.marcasPersonalizadas,
                alAgregarClick = { mostrarDialogoAgregarMarca = true },
                alEliminarMarca = { vm.eliminarMarcaPersonalizada(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionPuertosRedLocal(
                puertosServicios = ajustes.puertosServiciosLocales,
                alAgregarClick = { mostrarDialogoAgregarPuerto = true },
                alEliminarPuerto = { vm.eliminarPuertoServicio(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionConfiguracionRedLocal(
                plantillaRouter = ajustes.plantillaRouterIp,
                plantillaServidor = ajustes.plantillaServidorIp,
                octetosRouter = ajustes.octetosRouter,
                alCambiarPlantillaRouter = { nueva ->
                    vm.repositorio.ajustes.actualizar { it.copy(plantillaRouterIp = nueva) }
                },
                alCambiarPlantillaServidor = { nueva ->
                    vm.repositorio.ajustes.actualizar { it.copy(plantillaServidorIp = nueva) }
                },
                alAgregarOctetoClick = { mostrarDialogoAgregarOcteto = true },
                alEliminarOcteto = { vm.eliminarOctetoRouter(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            BotonBoveda(
                texto = "Restablecer reglas por defecto",
                alPulsar = { mostrarDialogoRestablecer = true },
                variante = VarianteBoton.SECUNDARIO,
                icono = Icons.Filled.RestartAlt,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (mostrarDialogoAgregarPrefijo) {
        DialogoAgregarPrefijo(
            alConfirmar = { nuevo ->
                vm.agregarPrefijoSubdominio(nuevo)
                mostrarDialogoAgregarPrefijo = false
            },
            alDescartar = { mostrarDialogoAgregarPrefijo = false }
        )
    }

    if (mostrarDialogoAgregarTld) {
        DialogoAgregarTld(
            alConfirmar = { nuevo ->
                vm.agregarTldDescartable(nuevo)
                mostrarDialogoAgregarTld = false
            },
            alDescartar = { mostrarDialogoAgregarTld = false }
        )
    }

    if (mostrarDialogoAgregarMarca) {
        DialogoAgregarMarca(
            alConfirmar = { dominio, nombre ->
                vm.agregarMarcaPersonalizada(dominio, nombre)
                mostrarDialogoAgregarMarca = false
            },
            alDescartar = { mostrarDialogoAgregarMarca = false }
        )
    }

    if (mostrarDialogoAgregarPuerto) {
        DialogoAgregarPuertoServicio(
            alConfirmar = { puerto, servicio ->
                vm.agregarPuertoServicio(puerto, servicio)
                mostrarDialogoAgregarPuerto = false
            },
            alDescartar = { mostrarDialogoAgregarPuerto = false }
        )
    }

    if (mostrarDialogoAgregarOcteto) {
        DialogoAgregarOctetoRouter(
            alConfirmar = { octeto ->
                vm.agregarOctetoRouter(octeto)
                mostrarDialogoAgregarOcteto = false
            },
            alDescartar = { mostrarDialogoAgregarOcteto = false }
        )
    }

    if (mostrarDialogoRestablecer) {
        DialogoConfirmacionBoveda(
            titulo = "¿Restablecer reglas?",
            mensaje = "Se restaurará la configuración predeterminada de prefijos, extensiones TLD, marcas personalizadas, puertos de servicios locales y formato de IPs.",
            textoConfirmar = "Restablecer",
            textoCancelar = "Cancelar",
            alConfirmar = {
                vm.restablecerReglasNormalizacion()
                mostrarDialogoRestablecer = false
            },
            alDescartar = { mostrarDialogoRestablecer = false }
        )
    }
}
