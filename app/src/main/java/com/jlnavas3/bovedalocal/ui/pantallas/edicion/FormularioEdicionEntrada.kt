package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Contenido desplazable del formulario de creación y edición de entradas en la bóveda.
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
            GrupoAjustes(etiqueta = "Tipo de registro") {
                Column(modifier = Modifier.padding(14.dp)) {
                    SelectorTipoEntrada(
                        tipoActual = tipo,
                        alSeleccionarTipo = { alCambiarTipo(it); haptica.tic() }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Grupo: Datos principales
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

        // Grupo: Sitios o aplicaciones
        if (tipo == TipoEntrada.LOGIN || tipo == TipoEntrada.PASSKEY) {
            Spacer(Modifier.height(16.dp))
            SeccionSitiosYAppsEdicion(
                listaEnlaces = listaEnlaces,
                alSolicitarExplorarApp = alSolicitarExplorarApp
            )

            Spacer(Modifier.height(16.dp))
            SeccionTotpEdicion(
                totp = totp,
                alCambiarTotp = alCambiarTotp,
                mostrarSecretoTotp = mostrarSecretoTotp,
                alAlternarMostrarSecreto = alAlternarMostrarSecretoTotp,
                totpValido = totpValido
            )
        }

        // Grupo: Notas
        Spacer(Modifier.height(16.dp))
        SeccionNotasEdicion(
            notas = notas,
            alCambiarNotas = alCambiarNotas
        )

        // Grupo: Campos adicionales
        Spacer(Modifier.height(16.dp))
        val etiquetasBase = remember(tipo) { GestorCamposBase.etiquetasBaseParaTipo(tipo) }
        GrupoAjustes(etiqueta = if (etiquetasBase.isEmpty()) "Campos personalizados" else "Campos adicionales") {
            Column(modifier = Modifier.padding(14.dp)) {
                SeccionCamposPersonalizados(
                    camposPersonalizados = camposPersonalizados,
                    alCambiarCampos = alCambiarCamposPersonalizados,
                    etiquetasBase = etiquetasBase,
                    ajustes = ajustes,
                    haptica = haptica
                )
            }
        }

        // Grupo: Organización
        Spacer(Modifier.height(16.dp))
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

        Spacer(Modifier.height(80.dp))
    }
}
