/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.CerrarTecladoAlArrastrar
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.SeccionCategoriasEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.notas.CampoNotaEnriquecidaEdicion
import com.jlnavas3.bovedalocal.ui.pantallas.identidades.SeccionIdentidadEdicion
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Contenido desplazable del formulario de creación y edición de entradas en la bóveda,
 * con autocierre inteligente de teclado al pulsar en espacios vacíos o iniciar scroll.
 */
@Composable
fun FormularioEdicionEntrada(
    scrollState: ScrollState,
    original: Entrada?,
    tipo: TipoEntrada,
    alCambiarTipo: (TipoEntrada) -> Unit,
    titulo: String,
    alCambiarTitulo: (String) -> Unit,
    usuario: String,
    alCambiarUsuario: (String) -> Unit,
    contrasena: String,
    alCambiarContrasena: (String) -> Unit,
    mostrarContrasena: Boolean,
    alAlternarMostrarContrasena: () -> Unit,
    opcionesGenerador: OpcionesGenerador,
    alCambiarOpcionesGenerador: (OpcionesGenerador) -> Unit,
    camposPersonalizados: List<CampoPersonalizado>,
    alCambiarCamposPersonalizados: (List<CampoPersonalizado>) -> Unit,
    datosTarjeta: com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria = com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria(),
    alCambiarDatosTarjeta: (com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria) -> Unit = {},
    datosWifi: com.jlnavas3.bovedalocal.data.DatosRedWifi = com.jlnavas3.bovedalocal.data.DatosRedWifi(),
    alCambiarDatosWifi: (com.jlnavas3.bovedalocal.data.DatosRedWifi) -> Unit = {},
    datosCuentaBancaria: com.jlnavas3.bovedalocal.data.DatosCuentaBancaria = com.jlnavas3.bovedalocal.data.DatosCuentaBancaria(),
    alCambiarDatosCuentaBancaria: (com.jlnavas3.bovedalocal.data.DatosCuentaBancaria) -> Unit = {},
    datosIdentidad: com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad = com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad(),
    alCambiarDatosIdentidad: (com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad) -> Unit = {},
    datosServidor: com.jlnavas3.bovedalocal.data.DatosServidorSsh = com.jlnavas3.bovedalocal.data.DatosServidorSsh(),
    alCambiarDatosServidor: (com.jlnavas3.bovedalocal.data.DatosServidorSsh) -> Unit = {},
    datosWallet: com.jlnavas3.bovedalocal.data.DatosCriptoWallet = com.jlnavas3.bovedalocal.data.DatosCriptoWallet(),
    alCambiarDatosWallet: (com.jlnavas3.bovedalocal.data.DatosCriptoWallet) -> Unit = {},
    datosContacto: com.jlnavas3.bovedalocal.data.DatosContacto = com.jlnavas3.bovedalocal.data.DatosContacto(),
    alCambiarDatosContacto: (com.jlnavas3.bovedalocal.data.DatosContacto) -> Unit = {},
    adjuntos: List<com.jlnavas3.bovedalocal.data.AdjuntoBoveda> = emptyList(),
    alCambiarAdjuntos: (List<com.jlnavas3.bovedalocal.data.AdjuntoBoveda>) -> Unit = {},
    listaEnlaces: SnapshotStateList<EnlaceEditable>,
    alSolicitarExplorarApp: (Int?) -> Unit,
    totp: String,
    alCambiarTotp: (String) -> Unit,
    mostrarSecretoTotp: Boolean,
    alAlternarMostrarSecretoTotp: () -> Unit,
    totpValido: Boolean,
    fechaCaducidad: Long? = null,
    alCambiarFechaCaducidad: (Long?) -> Unit = {},
    notas: String,
    alCambiarNotas: (String) -> Unit,
    categoriasDisponibles: List<Categoria>,
    categorias: List<String>,
    alCambiarCategorias: (List<String>) -> Unit,
    alCrearNuevaCategoria: () -> Unit,
    identidadesDisponibles: List<com.jlnavas3.bovedalocal.data.Identidad> = emptyList(),
    identidadSeleccionadaId: String? = null,
    alSeleccionarIdentidad: (com.jlnavas3.bovedalocal.data.Identidad?) -> Unit = {},
    etiquetas: List<String>,
    alCambiarEtiquetas: (List<String>) -> Unit,
    etiquetasSugeridas: List<String>,
    favorito: Boolean,
    alAlternarFavorito: (Boolean) -> Unit,
    anclado: Boolean = false,
    alAlternarAnclado: (Boolean) -> Unit = {},
    ignoradaEnSalud: Boolean,
    alAlternarIgnoradaEnSalud: (Boolean) -> Unit,
    ajustes: AjustesApp,
    haptica: Haptica,
    alGuardarPlantillaCampos: ((com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada) -> Unit)? = null,
    alEliminarPlantillaCampos: ((String) -> Unit)? = null,
    alAbrirDiccionario: (() -> Unit)? = null,
    alAbrirContrasenasComunes: (() -> Unit)? = null,
    alImportarContacto: ((com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Cierre automático únicamente al iniciar desplazamiento manual por arrastre del usuario
    // (evita que el auto-scroll de Compose al enfocar campos multilínea largos cierre el teclado de inmediato)
    CerrarTecladoAlArrastrar(scrollState.interactionSource)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )
            }
            .reboteElastico()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        DescripcionPantalla(
            subtitulo = if (original == null) "Crea y cifra un registro seguro en la bóveda" else "Modifica los datos del registro"
        )
        Spacer(Modifier.height(12.dp))

        // Selector de tipo (solo para nuevas entradas)
        if (original == null) {
            SelectorTipoEntrada(
                tipoActual = tipo,
                alSeleccionarTipo = { alCambiarTipo(it); haptica.tic() }
            )
            Spacer(Modifier.height(12.dp))
        }

        // Datos principales (Tarjeta unificada)
        SeccionDatosPrincipalesEdicion(
            titulo = titulo,
            alCambiarTitulo = alCambiarTitulo,
            tipo = tipo,
            original = original,
            usuario = usuario,
            alCambiarUsuario = alCambiarUsuario,
            contrasena = contrasena,
            alCambiarContrasena = alCambiarContrasena,
            mostrarContrasena = mostrarContrasena,
            alAlternarMostrarContrasena = alAlternarMostrarContrasena,
            opcionesGenerador = opcionesGenerador,
            alCambiarOpcionesGenerador = alCambiarOpcionesGenerador,
            camposPersonalizados = camposPersonalizados,
            alCambiarCamposPersonalizados = alCambiarCamposPersonalizados,
            datosTarjeta = datosTarjeta,
            alCambiarDatosTarjeta = alCambiarDatosTarjeta,
            datosWifi = datosWifi,
            alCambiarDatosWifi = alCambiarDatosWifi,
            datosCuentaBancaria = datosCuentaBancaria,
            alCambiarDatosCuentaBancaria = alCambiarDatosCuentaBancaria,
            datosIdentidad = datosIdentidad,
            alCambiarDatosIdentidad = alCambiarDatosIdentidad,
            datosServidor = datosServidor,
            alCambiarDatosServidor = alCambiarDatosServidor,
            datosWallet = datosWallet,
            alCambiarDatosWallet = alCambiarDatosWallet,
            datosContacto = datosContacto,
            alCambiarDatosContacto = alCambiarDatosContacto,
            listaEnlaces = listaEnlaces,
            ajustes = ajustes,
            haptica = haptica,
            alAbrirDiccionario = alAbrirDiccionario,
            alAbrirContrasenasComunes = alAbrirContrasenasComunes,
            alImportarContacto = alImportarContacto
        )

        // Sitios o aplicaciones y 2FA
        if (tipo == TipoEntrada.LOGIN || tipo == TipoEntrada.PASSKEY) {
            Spacer(Modifier.height(10.dp))
            SeccionSitiosYAppsEdicion(
                listaEnlaces = listaEnlaces,
                alSolicitarExplorarApp = alSolicitarExplorarApp
            )

            Spacer(Modifier.height(10.dp))
            SeccionTotpEdicion(
                totp = totp,
                alCambiarTotp = alCambiarTotp,
                mostrarSecretoTotp = mostrarSecretoTotp,
                alAlternarMostrarSecreto = alAlternarMostrarSecretoTotp,
                totpValido = totpValido
            )

            Spacer(Modifier.height(10.dp))
            CampoCaducidadEntrada(
                fechaCaducidad = fechaCaducidad,
                alCambiarFecha = alCambiarFechaCaducidad,
                ajustes = ajustes
            )
        }

        // Campo de nota enriquecida directa para notas seguras (abierto sin acordeón)
        var interactuoConNotas by remember { mutableStateOf(notas.isNotBlank()) }
        if (tipo == TipoEntrada.NOTA) {
            Spacer(Modifier.height(10.dp))
            CampoNotaEnriquecidaEdicion(
                notas = notas,
                alCambiarNotas = {
                    interactuoConNotas = true
                    alCambiarNotas(it)
                }
            )
        }

        // --- Estado de divulgación progresiva (Más... / Menos...) ---
        var mostrarMas by remember { mutableStateOf(false) }

        val etiquetasBase = remember(tipo) { GestorCamposBase.etiquetasBaseParaTipo(tipo) }
        val tieneNotas = tipo != TipoEntrada.NOTA && (notas.isNotBlank() || interactuoConNotas)
        val tieneCampos = camposPersonalizados.isNotEmpty()
        val tieneAdjuntos = adjuntos.isNotEmpty()
        val tieneIdentidad = identidadesDisponibles.isNotEmpty() && !identidadSeleccionadaId.isNullOrBlank()
        val tieneCategorias = categorias.isNotEmpty()
        val tieneEtiquetas = etiquetas.isNotEmpty()
        val tieneSwitchesActivos = favorito || anclado || ignoradaEnSalud
        val tieneSwitchesInactivos = !favorito || !anclado || !ignoradaEnSalud

        val haySeccionesVacias = (tipo != TipoEntrada.NOTA && !tieneNotas) ||
            !tieneCampos ||
            !tieneAdjuntos ||
            (identidadesDisponibles.isNotEmpty() && !tieneIdentidad) ||
            !tieneCategorias ||
            !tieneEtiquetas ||
            tieneSwitchesInactivos

        // --- SECCIONES SECUNDARIAS ACTIVAS (Con datos configurados) ---
        if (tieneNotas) {
            Spacer(Modifier.height(10.dp))
            SeccionNotasEdicion(
                notas = notas,
                alCambiarNotas = {
                    interactuoConNotas = true
                    alCambiarNotas(it)
                },
                inicialmenteExpandido = true
            )
        }

        if (tieneCampos) {
            Spacer(Modifier.height(10.dp))
            SeccionCamposPersonalizados(
                camposPersonalizados = camposPersonalizados,
                alCambiarCampos = alCambiarCamposPersonalizados,
                tipo = tipo,
                etiquetasBase = etiquetasBase,
                ajustes = ajustes,
                haptica = haptica,
                alGuardarPlantilla = alGuardarPlantillaCampos,
                alEliminarPlantilla = alEliminarPlantillaCampos
            )
        }

        if (tieneAdjuntos) {
            Spacer(Modifier.height(10.dp))
            SeccionAdjuntosEdicion(
                adjuntos = adjuntos,
                alCambiarAdjuntos = alCambiarAdjuntos
            )
        }

        if (tieneIdentidad) {
            Spacer(Modifier.height(10.dp))
            SeccionIdentidadEdicion(
                identidadesDisponibles = identidadesDisponibles,
                identidadSeleccionadaId = identidadSeleccionadaId,
                alSeleccionarIdentidad = alSeleccionarIdentidad
            )
        }

        if (tieneCategorias) {
            Spacer(Modifier.height(10.dp))
            SeccionCategoriasEdicion(
                categoriasDisponibles = categoriasDisponibles,
                categoriasSeleccionadas = categorias,
                alCambiarCategorias = alCambiarCategorias,
                alCrearNuevaCategoria = alCrearNuevaCategoria
            )
        }

        if (tieneEtiquetas) {
            Spacer(Modifier.height(10.dp))
            SeccionEtiquetasEdicion(
                etiquetas = etiquetas,
                alCambiarEtiquetas = alCambiarEtiquetas,
                etiquetasSugeridas = etiquetasSugeridas
            )
        }

        if (tieneSwitchesActivos) {
            Spacer(Modifier.height(10.dp))
            SeccionSwitchesEdicion(
                favorito = favorito,
                alAlternarFavorito = alAlternarFavorito,
                anclado = anclado,
                alAlternarAnclado = alAlternarAnclado,
                ignoradaEnSalud = ignoradaEnSalud,
                alAlternarIgnoradaEnSalud = alAlternarIgnoradaEnSalud,
                modo = ModoFiltroSwitches.SOLO_ACTIVOS
            )
        }

        // --- BOTÓN DE ALTERNANCIA "Más..." / "Menos..." ---
        if (haySeccionesVacias) {
            Spacer(Modifier.height(10.dp))
            BotonAlternarMasOpciones(
                expandido = mostrarMas,
                alAlternar = {
                    mostrarMas = !mostrarMas
                },
                haptica = haptica
            )
        }

        // --- SECCIONES SECUNDARIAS VACÍAS (Desplegables tras pulsar "Más...") ---
        AnimatedVisibility(
            visible = mostrarMas,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.height(2.dp))

                if (tipo != TipoEntrada.NOTA && !tieneNotas) {
                    SeccionNotasEdicion(
                        notas = notas,
                        alCambiarNotas = {
                            interactuoConNotas = true
                            alCambiarNotas(it)
                        }
                    )
                }

                if (!tieneCampos) {
                    SeccionCamposPersonalizados(
                        camposPersonalizados = camposPersonalizados,
                        alCambiarCampos = alCambiarCamposPersonalizados,
                        tipo = tipo,
                        etiquetasBase = etiquetasBase,
                        ajustes = ajustes,
                        haptica = haptica,
                        alGuardarPlantilla = alGuardarPlantillaCampos,
                        alEliminarPlantilla = alEliminarPlantillaCampos
                    )
                }

                if (!tieneAdjuntos) {
                    SeccionAdjuntosEdicion(
                        adjuntos = adjuntos,
                        alCambiarAdjuntos = alCambiarAdjuntos
                    )
                }

                if (identidadesDisponibles.isNotEmpty() && !tieneIdentidad) {
                    SeccionIdentidadEdicion(
                        identidadesDisponibles = identidadesDisponibles,
                        identidadSeleccionadaId = identidadSeleccionadaId,
                        alSeleccionarIdentidad = alSeleccionarIdentidad
                    )
                }

                if (!tieneCategorias) {
                    SeccionCategoriasEdicion(
                        categoriasDisponibles = categoriasDisponibles,
                        categoriasSeleccionadas = categorias,
                        alCambiarCategorias = alCambiarCategorias,
                        alCrearNuevaCategoria = alCrearNuevaCategoria
                    )
                }

                if (!tieneEtiquetas) {
                    SeccionEtiquetasEdicion(
                        etiquetas = etiquetas,
                        alCambiarEtiquetas = alCambiarEtiquetas,
                        etiquetasSugeridas = etiquetasSugeridas
                    )
                }

                if (tieneSwitchesInactivos) {
                    SeccionSwitchesEdicion(
                        favorito = favorito,
                        alAlternarFavorito = alAlternarFavorito,
                        anclado = anclado,
                        alAlternarAnclado = alAlternarAnclado,
                        ignoradaEnSalud = ignoradaEnSalud,
                        alAlternarIgnoradaEnSalud = alAlternarIgnoradaEnSalud,
                        modo = ModoFiltroSwitches.SOLO_INACTIVOS
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
