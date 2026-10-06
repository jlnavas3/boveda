package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.reglas

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
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
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
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme

private enum class TipoPistaDialogo {
    USUARIO, CONTRASENA, OTP
}

@Composable
fun PantallaReglasAutocompletado(
    vm: VaultViewModel,
    alVolver: () -> Unit = { vm.volverAtras() }
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var dialogoAgregarTipo by remember { mutableStateOf<TipoPistaDialogo?>(null) }
    var mostrarDialogoAgregarMapeo by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarNavegador by remember { mutableStateOf(false) }
    var mostrarDialogoRestablecer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Reglas de Autocompletado",
            idEtiqueta = "04-HER-PSK-RGL",
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
            SeccionChipsPistasAutofill(
                titulo = "Campos de Usuario",
                descripcion = "Palabras clave o identificadores que activan el autocompletado del nombre de usuario o correo (ej. cédula, DNI, RUC, identificador).",
                icono = Icons.Filled.Person,
                idGrupo = "04-HER-PSK-RGL-G01",
                pistas = ajustes.autofillPistasUsuario,
                alAgregarClick = { dialogoAgregarTipo = TipoPistaDialogo.USUARIO },
                alEliminarPista = { vm.eliminarPistaUsuario(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionChipsPistasAutofill(
                titulo = "Campos de Contraseña",
                descripcion = "Palabras clave que identifican campos de contraseñas, PINs o claves de acceso secundarias.",
                icono = Icons.Filled.Password,
                idGrupo = "04-HER-PSK-RGL-G02",
                pistas = ajustes.autofillPistasContrasena,
                alAgregarClick = { dialogoAgregarTipo = TipoPistaDialogo.CONTRASENA },
                alEliminarPista = { vm.eliminarPistaContrasena(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionChipsPistasAutofill(
                titulo = "Campos 2FA / OTP",
                descripcion = "Términos que identifican casillas para códigos temporales de un solo uso o autenticación en dos factores.",
                icono = Icons.Filled.Security,
                idGrupo = "04-HER-PSK-RGL-G03",
                pistas = ajustes.autofillPistasOtp,
                alAgregarClick = { dialogoAgregarTipo = TipoPistaDialogo.OTP },
                alEliminarPista = { vm.eliminarPistaOtp(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionMapeoPaquetesApps(
                mapeos = ajustes.mapeoPaquetesPersonalizados,
                alAgregarClick = { mostrarDialogoAgregarMapeo = true },
                alEliminarMapeo = { vm.eliminarMapeoPaquete(it) },
                mostrarId = ajustes.mostrarIdsAjustes,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SeccionNavegadoresPersonalizados(
                navegadores = ajustes.navegadoresPersonalizados,
                alAgregarClick = { mostrarDialogoAgregarNavegador = true },
                alEliminarNavegador = { vm.eliminarNavegadorPersonalizado(it) },
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

    when (dialogoAgregarTipo) {
        TipoPistaDialogo.USUARIO -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "usuario",
                ejemploSugerido = "'cedula', 'dni', 'ruc', 'cuenta'",
                alConfirmar = {
                    vm.agregarPistaUsuario(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.CONTRASENA -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "contraseña",
                ejemploSugerido = "'pin', 'nip', 'clave_web'",
                alConfirmar = {
                    vm.agregarPistaContrasena(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.OTP -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "código 2FA / OTP",
                ejemploSugerido = "'token', 'coordenadas', 'clave_dinamica'",
                alConfirmar = {
                    vm.agregarPistaOtp(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        null -> {}
    }

    if (mostrarDialogoAgregarMapeo) {
        DialogoAgregarMapeoPaquete(
            alConfirmar = { paquete, dominio ->
                vm.agregarMapeoPaquete(paquete, dominio)
                mostrarDialogoAgregarMapeo = false
            },
            alDescartar = { mostrarDialogoAgregarMapeo = false }
        )
    }

    if (mostrarDialogoAgregarNavegador) {
        DialogoAgregarNavegadorPersonalizado(
            alConfirmar = { paquete ->
                vm.agregarNavegadorPersonalizado(paquete)
                mostrarDialogoAgregarNavegador = false
            },
            alDescartar = { mostrarDialogoAgregarNavegador = false }
        )
    }

    if (mostrarDialogoRestablecer) {
        DialogoConfirmacionBoveda(
            titulo = "¿Restablecer reglas de autocompletado?",
            mensaje = "Se restaurarán las listas predeterminadas de palabras clave (usuario, contraseña, 2FA/OTP), vinculaciones de apps a sitios web y navegadores web personalizados.",
            textoConfirmar = "Restablecer",
            textoCancelar = "Cancelar",
            alConfirmar = {
                vm.restablecerReglasAutocompletado()
                mostrarDialogoRestablecer = false
            },
            alDescartar = { mostrarDialogoRestablecer = false }
        )
    }
}

@BovedaPantallaPreview
@Composable
private fun PreviaPantallaReglasAutocompletado() {
    BovedaTheme {
        // Preview decorativa
    }
}
