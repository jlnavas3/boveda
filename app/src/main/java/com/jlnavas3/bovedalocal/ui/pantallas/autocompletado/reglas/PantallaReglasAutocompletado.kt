/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme

private enum class TipoPistaDialogo {
    USUARIO, CONTRASENA, OTP, DOCUMENTO,
    TARJETA_NUMERO, TARJETA_CVV, TARJETA_EXPIRACION, TARJETA_TITULAR,
    DIRECCION_CALLE, DIRECCION_CP, DIRECCION_CIUDAD, DIRECCION_PROVINCIA, DIRECCION_PAIS,
    CONTACTO_TELEFONO, CONTACTO_NOMBRE
}

@Composable
fun PantallaReglasAutocompletado(
    vm: VaultViewModel,
    seccionId: String? = null,
    alVolver: () -> Unit = { vm.volverAtras() }
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var dialogoAgregarTipo by remember { mutableStateOf<TipoPistaDialogo?>(null) }
    var mostrarDialogoAgregarTipoDocumento by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarPaqueteComunicacion by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarMapeo by remember { mutableStateOf(false) }
    var mostrarDialogoAgregarNavegador by remember { mutableStateOf(false) }
    var mostrarDialogoRestablecer by remember { mutableStateOf(false) }

    ProveedorResaltadoAjustes(seccionId, scrollState = scrollState) {
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
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-PSK-RGL-G01", "Campos de Usuario"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G02", "Campos de Contraseña"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G03", "Campos 2FA / OTP"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G04", "Documentos de Identidad"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G08", "Tarjetas de Pago"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G09", "Direcciones y Domicilios"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G05", "Contactos y Mensajería"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G06", "Vinculación Apps Nativas"),
                            AccionSaltoGrupo("04-HER-PSK-RGL-G07", "Navegadores Web")
                        ),
                        alRestablecerPantalla = { mostrarDialogoRestablecer = true },
                        mensajeToastRestablecer = "Reglas restablecidas"
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // ==================== 1. CREDENCIALES ====================
                SeccionChipsPistasAutofill(
                    titulo = "Campos de Usuario",
                    descripcion = "Palabras clave o identificadores que activan el autocompletado del nombre de usuario o correo (ej. email, login, account).",
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

                // ==================== 2. DOCUMENTOS DE IDENTIDAD ====================
                SeccionDocumentosAutofill(
                    pistas = ajustes.autofillPistasDocumento,
                    tiposPersonalizados = ajustes.tiposDocumentoPersonalizados,
                    alAgregarClick = { dialogoAgregarTipo = TipoPistaDialogo.DOCUMENTO },
                    alEliminarPista = { vm.eliminarPistaDocumento(it) },
                    alAgregarTipoClick = { mostrarDialogoAgregarTipoDocumento = true },
                    alEliminarTipoPersonalizado = { vm.eliminarTipoDocumentoPersonalizado(it) },
                    mostrarId = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ==================== 2b. TARJETAS DE PAGO ====================
                SeccionTarjetasAutofill(
                    pistasNumero = ajustes.autofillPistasTarjetaNumero,
                    pistasCvv = ajustes.autofillPistasTarjetaCvv,
                    pistasExpiracion = ajustes.autofillPistasTarjetaExpiracion,
                    pistasTitular = ajustes.autofillPistasTarjetaTitular,
                    alAgregarNumeroClick = { dialogoAgregarTipo = TipoPistaDialogo.TARJETA_NUMERO },
                    alEliminarNumero = { vm.eliminarPistaTarjetaNumero(it) },
                    alAgregarCvvClick = { dialogoAgregarTipo = TipoPistaDialogo.TARJETA_CVV },
                    alEliminarCvv = { vm.eliminarPistaTarjetaCvv(it) },
                    alAgregarExpiracionClick = { dialogoAgregarTipo = TipoPistaDialogo.TARJETA_EXPIRACION },
                    alEliminarExpiracion = { vm.eliminarPistaTarjetaExpiracion(it) },
                    alAgregarTitularClick = { dialogoAgregarTipo = TipoPistaDialogo.TARJETA_TITULAR },
                    alEliminarTitular = { vm.eliminarPistaTarjetaTitular(it) },
                    mostrarId = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ==================== 2c. DIRECCIONES Y DOMICILIOS ====================
                SeccionDireccionesAutofill(
                    pistasCalle = ajustes.autofillPistasDireccionCalle,
                    pistasCp = ajustes.autofillPistasDireccionCp,
                    pistasCiudad = ajustes.autofillPistasDireccionCiudad,
                    pistasProvincia = ajustes.autofillPistasDireccionProvincia,
                    pistasPais = ajustes.autofillPistasDireccionPais,
                    alAgregarCalleClick = { dialogoAgregarTipo = TipoPistaDialogo.DIRECCION_CALLE },
                    alEliminarCalle = { vm.eliminarPistaDireccionCalle(it) },
                    alAgregarCpClick = { dialogoAgregarTipo = TipoPistaDialogo.DIRECCION_CP },
                    alEliminarCp = { vm.eliminarPistaDireccionCp(it) },
                    alAgregarCiudadClick = { dialogoAgregarTipo = TipoPistaDialogo.DIRECCION_CIUDAD },
                    alEliminarCiudad = { vm.eliminarPistaDireccionCiudad(it) },
                    alAgregarProvinciaClick = { dialogoAgregarTipo = TipoPistaDialogo.DIRECCION_PROVINCIA },
                    alEliminarProvincia = { vm.eliminarPistaDireccionProvincia(it) },
                    alAgregarPaisClick = { dialogoAgregarTipo = TipoPistaDialogo.DIRECCION_PAIS },
                    alEliminarPais = { vm.eliminarPistaDireccionPais(it) },
                    mostrarId = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ==================== 3. CONTACTOS Y MENSAJERÍA ====================
                SeccionContactosAutofill(
                    paquetes = ajustes.paquetesComunicacion,
                    pistasTelefono = ajustes.autofillPistasContactoTelefono,
                    pistasNombre = ajustes.autofillPistasContactoNombre,
                    alAgregarPaqueteClick = { mostrarDialogoAgregarPaqueteComunicacion = true },
                    alEliminarPaquete = { vm.eliminarPaqueteComunicacion(it) },
                    alAgregarPistaTelefonoClick = { dialogoAgregarTipo = TipoPistaDialogo.CONTACTO_TELEFONO },
                    alEliminarPistaTelefono = { vm.eliminarPistaContactoTelefono(it) },
                    alAgregarPistaNombreClick = { dialogoAgregarTipo = TipoPistaDialogo.CONTACTO_NOMBRE },
                    alEliminarPistaNombre = { vm.eliminarPistaContactoNombre(it) },
                    mostrarId = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ==================== 4. MAPEO DE APPS Y NAVEGADORES ====================
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
    }

    when (dialogoAgregarTipo) {
        TipoPistaDialogo.USUARIO -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "usuario",
                ejemploSugerido = "'user', 'email', 'login', 'account'",
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
        TipoPistaDialogo.DOCUMENTO -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "documento de identidad",
                ejemploSugerido = "'cedula', 'dni', 'ruc', 'curp', 'rfc'",
                alConfirmar = {
                    vm.agregarPistaDocumento(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.TARJETA_NUMERO -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "número de tarjeta",
                ejemploSugerido = "'cardnumber', 'pan', 'numero_tarjeta'",
                alConfirmar = {
                    vm.agregarPistaTarjetaNumero(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.TARJETA_CVV -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "código de seguridad CVV",
                ejemploSugerido = "'cvv', 'cvc', 'cid', 'security_code'",
                alConfirmar = {
                    vm.agregarPistaTarjetaCvv(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.TARJETA_EXPIRACION -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "fecha de expiración",
                ejemploSugerido = "'exp', 'expiration', 'vencimiento', 'caducidad'",
                alConfirmar = {
                    vm.agregarPistaTarjetaExpiracion(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.TARJETA_TITULAR -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "titular de tarjeta",
                ejemploSugerido = "'cardholder', 'titular', 'nombre_tarjeta', 'owner'",
                alConfirmar = {
                    vm.agregarPistaTarjetaTitular(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.DIRECCION_CALLE -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "calle o domicilio",
                ejemploSugerido = "'address', 'street', 'calle', 'domicilio'",
                alConfirmar = {
                    vm.agregarPistaDireccionCalle(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.DIRECCION_CP -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "código postal",
                ejemploSugerido = "'zip', 'postal', 'codigo_postal', 'postcode'",
                alConfirmar = {
                    vm.agregarPistaDireccionCp(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.DIRECCION_CIUDAD -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "ciudad o localidad",
                ejemploSugerido = "'city', 'ciudad', 'localidad', 'municipio'",
                alConfirmar = {
                    vm.agregarPistaDireccionCiudad(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.DIRECCION_PROVINCIA -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "provincia o estado",
                ejemploSugerido = "'state', 'province', 'provincia', 'departamento'",
                alConfirmar = {
                    vm.agregarPistaDireccionProvincia(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.DIRECCION_PAIS -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "país o nación",
                ejemploSugerido = "'country', 'pais', 'nacion'",
                alConfirmar = {
                    vm.agregarPistaDireccionPais(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.CONTACTO_TELEFONO -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "teléfono de contacto",
                ejemploSugerido = "'phone', 'telefono', 'movil', 'celular'",
                alConfirmar = {
                    vm.agregarPistaContactoTelefono(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        TipoPistaDialogo.CONTACTO_NOMBRE -> {
            DialogoAgregarPistaAutofill(
                tipoCampo = "nombre de contacto",
                ejemploSugerido = "'contact_name', 'nombre_contacto', 'destinatario'",
                alConfirmar = {
                    vm.agregarPistaContactoNombre(it)
                    dialogoAgregarTipo = null
                },
                alDescartar = { dialogoAgregarTipo = null }
            )
        }
        null -> {}
    }

    if (mostrarDialogoAgregarTipoDocumento) {
        DialogoAgregarTipoDocumento(
            alConfirmar = { sigla ->
                vm.agregarTipoDocumentoPersonalizado(sigla)
                mostrarDialogoAgregarTipoDocumento = false
            },
            alDescartar = { mostrarDialogoAgregarTipoDocumento = false }
        )
    }

    if (mostrarDialogoAgregarPaqueteComunicacion) {
        DialogoAgregarPaqueteComunicacion(
            alConfirmar = { paquete ->
                vm.agregarPaqueteComunicacion(paquete)
                mostrarDialogoAgregarPaqueteComunicacion = false
            },
            alDescartar = { mostrarDialogoAgregarPaqueteComunicacion = false }
        )
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
            mensaje = "Se restaurarán las listas predeterminadas de palabras clave (credenciales, documentos, contactos), aplicaciones de comunicación, vinculaciones de apps a sitios web y navegadores web personalizados.",
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
