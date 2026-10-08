package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
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
    listaEnlaces: SnapshotStateList<EnlaceEditable>,
    alSolicitarExplorarApp: (Int?) -> Unit,
    totp: String,
    alCambiarTotp: (String) -> Unit,
    mostrarSecretoTotp: Boolean,
    alAlternarMostrarSecretoTotp: () -> Unit,
    totpValido: Boolean,
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
    ignoradaEnSalud: Boolean,
    alAlternarIgnoradaEnSalud: (Boolean) -> Unit,
    ajustes: AjustesApp,
    haptica: Haptica,
    alGuardarPlantillaCampos: ((com.jlnavas3.bovedalocal.data.PlantillaCamposPersonalizada) -> Unit)? = null,
    alEliminarPlantillaCampos: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Cierre automático al iniciar desplazamiento
    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

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
            listaEnlaces = listaEnlaces,
            ajustes = ajustes,
            haptica = haptica
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
        }

        // Notas
        Spacer(Modifier.height(10.dp))
        SeccionNotasEdicion(
            notas = notas,
            alCambiarNotas = alCambiarNotas
        )

        // Campos adicionales
        val etiquetasBase = remember(tipo) { GestorCamposBase.etiquetasBaseParaTipo(tipo) }
        Spacer(Modifier.height(10.dp))
        SeccionCamposPersonalizados(
            camposPersonalizados = camposPersonalizados,
            alCambiarCampos = alCambiarCamposPersonalizados,
            etiquetasBase = etiquetasBase,
            ajustes = ajustes,
            haptica = haptica,
            alGuardarPlantilla = alGuardarPlantillaCampos,
            alEliminarPlantilla = alEliminarPlantillaCampos
        )

        // Organización (Identidad, Categorías, Etiquetas, Favorito, Ignorar en salud)
        Spacer(Modifier.height(10.dp))
        SeccionOrganizacionEdicion(
            categoriasDisponibles = categoriasDisponibles,
            categoriasSeleccionadas = categorias,
            alCambiarCategorias = alCambiarCategorias,
            alCrearNuevaCategoria = alCrearNuevaCategoria,
            identidadesDisponibles = identidadesDisponibles,
            identidadSeleccionadaId = identidadSeleccionadaId,
            alSeleccionarIdentidad = alSeleccionarIdentidad,
            etiquetas = etiquetas,
            alCambiarEtiquetas = alCambiarEtiquetas,
            etiquetasSugeridas = etiquetasSugeridas,
            favorito = favorito,
            alAlternarFavorito = alAlternarFavorito,
            ignoradaEnSalud = ignoradaEnSalud,
            alAlternarIgnoradaEnSalud = alAlternarIgnoradaEnSalud
        )

        Spacer(Modifier.height(24.dp))
    }
}
