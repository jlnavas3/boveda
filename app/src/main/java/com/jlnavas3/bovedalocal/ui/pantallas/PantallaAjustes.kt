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
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Badge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.BarraBusquedaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

private data class ElementoMenuAjustes(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val idEtiqueta: String,
    val grupo: String,
    val palabrasClave: String = "",
    val valorTexto: String? = null,
    val alPulsar: () -> Unit
)

@Composable
fun PantallaAjustes(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var textoBusqueda by remember { mutableStateOf("") }
    var dialogoNombreBoveda by remember { mutableStateOf(false) }
    var dialogoProveedorPasskeys by remember { mutableStateOf(false) }

    val todosLosElementos = remember(ajustes) {
        listOf(
            // Grupo: Seguridad
            ElementoMenuAjustes(
                titulo = "Datos biométricos y contraseña",
                subtitulo = "Huella dactilar, bloqueo automático y protección de pantalla",
                icono = Icons.Filled.Fingerprint,
                colorIcono = Color(0xFF1E88E5),
                idEtiqueta = "01.1",
                grupo = "Seguridad",
                palabrasClave = "huella biometria pin contrasena bloqueo inactividad flag secure pantalla",
                alPulsar = { vm.ir(Pantalla.Seguridad("01.1")) }
            ),
            ElementoMenuAjustes(
                titulo = "Bóveda señuelo",
                subtitulo = "Apertura ficticia transparente ante coacción o amenaza",
                icono = Icons.Filled.Shield,
                colorIcono = Color(0xFFFB8C00),
                idEtiqueta = "01.2",
                grupo = "Seguridad",
                palabrasClave = "senuelo coaccion pin falso fake simulado",
                alPulsar = { vm.ir(Pantalla.AjustesSenuelo("01.2")) }
            ),
            ElementoMenuAjustes(
                titulo = "Autodestrucción por PIN",
                subtitulo = "Borrado irreversible inmediato de la bóveda ante peligro extremo",
                icono = Icons.Filled.DeleteForever,
                colorIcono = Color(0xFFE53935),
                idEtiqueta = "01.3",
                grupo = "Seguridad",
                palabrasClave = "autodestruccion borrar destruir emergencia peligro pin",
                alPulsar = { vm.ir(Pantalla.AjustesAutodestruccion("01.3")) }
            ),
            ElementoMenuAjustes(
                titulo = "Cifrado Argon2id",
                subtitulo = "Memoria, iteraciones y resistencia a ataques de fuerza bruta",
                icono = Icons.Filled.Memory,
                colorIcono = Color(0xFF5C6BC0),
                idEtiqueta = "01.4",
                grupo = "Seguridad",
                palabrasClave = "argon2id cifrado algoritmo hash ram memoria hilos kdf",
                alPulsar = { vm.ir(Pantalla.Argon2id("01.4")) }
            ),

            // Grupo: Copias y Datos
            ElementoMenuAjustes(
                titulo = "Copia de seguridad",
                subtitulo = "Exportar, restaurar y copias automáticas locales rotativas",
                icono = Icons.Filled.Backup,
                colorIcono = Color(0xFF43A047),
                idEtiqueta = "02.1",
                grupo = "Cuentas y Datos",
                palabrasClave = "copia seguridad backup exportar importar restaurar auto automatica",
                alPulsar = { vm.ir(Pantalla.CopiaSeguridad("02.1")) }
            ),
            ElementoMenuAjustes(
                titulo = "Contraseñas de Google",
                subtitulo = "Importar archivo CSV descargado de Google Passwords",
                icono = Icons.Filled.FileUpload,
                colorIcono = Color(0xFF0288D1),
                idEtiqueta = "02.2",
                grupo = "Cuentas y Datos",
                palabrasClave = "google passwords csv importar chrome navegador",
                alPulsar = { vm.ir(Pantalla.CsvGoogle("02.2")) }
            ),
            ElementoMenuAjustes(
                titulo = "Kit de emergencia",
                subtitulo = "Generar documento impreso con claves y rescate físico",
                icono = Icons.Filled.Description,
                colorIcono = Color(0xFFFFB300),
                idEtiqueta = "02.3",
                grupo = "Cuentas y Datos",
                palabrasClave = "kit emergencia papel pdf imprimir hoja rescate",
                alPulsar = { vm.ir(Pantalla.KitEmergencia("02.3")) }
            ),

            // Grupo: Personalización
            ElementoMenuAjustes(
                titulo = "Nombre de la app",
                subtitulo = "Personalizar el nombre visible en la cabecera y el menú lateral",
                icono = Icons.Filled.Badge,
                colorIcono = Color(0xFF00897B),
                idEtiqueta = "03.1",
                grupo = "Personalización",
                palabrasClave = "nombre personalizada titulo encabezado boveda local",
                valorTexto = ajustes.nombrePersonalizado.ifBlank { "Bóveda local" },
                alPulsar = { dialogoNombreBoveda = true }
            ),
            ElementoMenuAjustes(
                titulo = "Tema y colores",
                subtitulo = "Personalización de paleta, color de acento y modo oscuro",
                icono = Icons.Filled.Palette,
                colorIcono = Color(0xFF8E24AA),
                idEtiqueta = "03.2",
                grupo = "Personalización",
                palabrasClave = "tema colores paleta acento apariencia aspecto oscuro claro sistema",
                valorTexto = when (ajustes.temaApp) {
                    "claro" -> "Claro"
                    "oscuro" -> "Oscuro"
                    else -> "Sistema"
                },
                alPulsar = { vm.ir(Pantalla.Tema("03.2")) }
            ),
            ElementoMenuAjustes(
                titulo = "Widgets de escritorio",
                subtitulo = "Generador rápido y accesos directos en pantalla de inicio",
                icono = Icons.Filled.Widgets,
                colorIcono = Color(0xFF7CB342),
                idEtiqueta = "03.3",
                grupo = "Personalización",
                palabrasClave = "widgets escritorio inicio favoritos totp generador 1x1",
                alPulsar = { vm.ir(Pantalla.AjustesWidget("03.3")) }
            ),
            ElementoMenuAjustes(
                titulo = "Abecedario lateral",
                subtitulo = "Navegación rápida con efecto de ola estilo Niagara y personalización",
                icono = Icons.AutoMirrored.Filled.Sort,
                colorIcono = Color(0xFF6A1B9A),
                idEtiqueta = "03.4",
                grupo = "Personalización",
                palabrasClave = "abecedario indice lateral ola niagara alfabeto scroll letras",
                valorTexto = if (ajustes.mostrarIndiceAlfabetico) "Activo" else "Oculto",
                alPulsar = { vm.ir(Pantalla.AjustesIndice("03.4")) }
            ),
            ElementoMenuAjustes(
                titulo = "Organización de lista",
                subtitulo = "Agrupamiento de cuentas por sitio, tamaño de filas y orden",
                icono = Icons.Filled.Layers,
                colorIcono = Color(0xFF00ACC1),
                idEtiqueta = "03.5",
                grupo = "Personalización",
                palabrasClave = "agrupar agrupamiento lista densidad compacta comoda cuentas sitio dominio carpetas orden",
                valorTexto = if (ajustes.agruparPorSitio) "Agrupada" else "Individual",
                alPulsar = { vm.ir(Pantalla.OrganizacionLista("03.5")) }
            ),
            ElementoMenuAjustes(
                titulo = "Formatos de campos",
                subtitulo = "Plantillas personalizadas y campos de autofill",
                icono = Icons.AutoMirrored.Filled.FormatListBulleted,
                colorIcono = Color(0xFFFFA000),
                idEtiqueta = "03.6",
                grupo = "Personalización",
                palabrasClave = "formatos campos plantillas autofill rellenar formulario",
                alPulsar = { vm.ir(Pantalla.FormatosCampos("03.6")) }
            ),

            // Grupo: Funciones
            ElementoMenuAjustes(
                titulo = "Autenticador 2FA",
                subtitulo = "Parámetros predeterminados de códigos temporales TOTP",
                icono = Icons.Filled.Timer,
                colorIcono = Color(0xFF3949AB),
                idEtiqueta = "04.1",
                grupo = "Funciones",
                palabrasClave = "2fa totp autenticador codigos periodo hmac digitos",
                alPulsar = { vm.ir(Pantalla.AjustesAutenticador("04.1")) }
            ),
            ElementoMenuAjustes(
                titulo = "Cámara y escáner QR",
                subtitulo = "Motor óptico CameraX y compatibilidad de escaneo",
                icono = Icons.Filled.CameraAlt,
                colorIcono = Color(0xFF00897B),
                idEtiqueta = "04.2",
                grupo = "Funciones",
                palabrasClave = "camara escaner qr camerax optico lector",
                alPulsar = { vm.ir(Pantalla.AjustesCamara("04.2")) }
            ),
            ElementoMenuAjustes(
                titulo = "Mosaico rápido de Android",
                subtitulo = "Generación instantánea desde la barra de notificaciones",
                icono = Icons.Filled.Tune,
                colorIcono = Color(0xFFFBC02D),
                idEtiqueta = "04.3",
                grupo = "Funciones",
                palabrasClave = "tile mosaico barra estado cortina notificaciones rapido",
                alPulsar = { vm.ir(Pantalla.TileRapido("04.3")) }
            ),
            ElementoMenuAjustes(
                titulo = "Proveedor de Passkeys",
                subtitulo = "Activar Bóveda local en \"Contraseñas y llaves de acceso\" de Android",
                icono = Icons.Filled.Key,
                colorIcono = Color(0xFF8B5CF6),
                idEtiqueta = "04.4",
                grupo = "Funciones",
                palabrasClave = "passkey passkeys proveedor credenciales llaves acceso android servicio activar",
                alPulsar = { dialogoProveedorPasskeys = true }
            ),

            // Grupo: Sistema
            ElementoMenuAjustes(
                titulo = "Opciones avanzadas",
                subtitulo = "Cambio de clave maestra, visualización de IDs y borrado",
                icono = Icons.Filled.Tune,
                colorIcono = Color(0xFFC2185B),
                idEtiqueta = "05.1",
                grupo = "Sistema",
                palabrasClave = "avanzada maestra clave cambiar borrar ids desarrollo",
                alPulsar = { vm.ir(Pantalla.Avanzada("05.1")) }
            )
        )
    }

    val elementosFiltrados = remember(textoBusqueda, todosLosElementos) {
        val q = textoBusqueda.trim().lowercase()
        if (q.isEmpty()) emptyList()
        else todosLosElementos.filter {
            it.titulo.lowercase().contains(q) ||
            it.subtitulo.lowercase().contains(q) ||
            it.idEtiqueta.lowercase().contains(q) ||
            it.palabrasClave.lowercase().contains(q)
        }
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Ajustes",
                idEtiqueta = "00",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Barra de búsqueda nativa en forma de píldora
                BarraBusquedaAjustes(
                    texto = textoBusqueda,
                    alCambiarTexto = { textoBusqueda = it },
                    placeholder = "Buscar en ajustes..."
                )

                Spacer(Modifier.height(14.dp))

                if (textoBusqueda.isNotBlank()) {
                    // Resultados de la búsqueda
                    if (elementosFiltrados.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "No se encontraron ajustes que coincidan con \"$textoBusqueda\"",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        ComponenteGrupo(etiqueta = "Resultados (${elementosFiltrados.size})") {
                            elementosFiltrados.forEachIndexed { index, elem ->
                                if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                                ComponenteNavegacion(
                                    titulo = elem.titulo,
                                    icono = elem.icono,
                                    colorIcono = elem.colorIcono,
                                    idFila = elem.idEtiqueta,
                                    mostrarId = ajustes.mostrarIdsAjustes,
                                    valorTexto = elem.valorTexto,
                                    alPulsar = elem.alPulsar
                                )
                            }
                        }
                    }
                } else {
                    // Vista normal estilo MagicOS / One UI: Tarjetas agrupadas con esquinas redondeadas
                    data class InfoGrupo(val nombre: String, val idGrupo: String)
                    val grupos = listOf(
                        InfoGrupo("Seguridad", "01"),
                        InfoGrupo("Cuentas y Datos", "02"),
                        InfoGrupo("Personalización", "03"),
                        InfoGrupo("Funciones", "04"),
                        InfoGrupo("Sistema", "05")
                    )
                    grupos.forEachIndexed { gIndex, grupo ->
                        val elementosDelGrupo = todosLosElementos.filter { it.grupo == grupo.nombre }
                        if (elementosDelGrupo.isNotEmpty()) {
                            if (gIndex > 0) Spacer(Modifier.height(14.dp))
                            ComponenteGrupo(
                                etiqueta = grupo.nombre,
                                idGrupo = grupo.idGrupo,
                                mostrarId = ajustes.mostrarIdsAjustes
                            ) {
                                elementosDelGrupo.forEachIndexed { index, elem ->
                                    if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                                    ComponenteNavegacion(
                                        titulo = elem.titulo,
                                        icono = elem.icono,
                                        colorIcono = elem.colorIcono,
                                        idFila = elem.idEtiqueta,
                                        mostrarId = ajustes.mostrarIdsAjustes,
                                        valorTexto = elem.valorTexto,
                                        alPulsar = elem.alPulsar
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoNombreBoveda) {
        var nombreTemporal by remember(ajustes.nombrePersonalizado) { mutableStateOf(ajustes.nombrePersonalizado) }
        val esOscuro = isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { dialogoNombreBoveda = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text("Nombre de la app", color = TextoPrincipal)
            },
            text = {
                Column {
                    Text(
                        "Personaliza el nombre que se muestra en la cabecera del listado y en la barra lateral. Si se deja vacío se usará \"Bóveda local\".",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(14.dp))
                    CampoBoveda(
                        valor = nombreTemporal,
                        etiqueta = "Nombre de la bóveda",
                        alCambiar = { nombreTemporal = it }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogoNombreBoveda = false
                    vm.ajustarNombrePersonalizado(nombreTemporal.trim())
                }) {
                    Text("Guardar", color = ColorAcento)
                }
            },
            dismissButton = {
                androidx.compose.foundation.layout.Row {
                    TextButton(onClick = {
                        nombreTemporal = ""
                        vm.ajustarNombrePersonalizado("")
                        dialogoNombreBoveda = false
                    }) {
                        Text("Restablecer", color = TextoSecundario)
                    }
                    TextButton(onClick = { dialogoNombreBoveda = false }) {
                        Text("Cancelar", color = TextoSecundario)
                    }
                }
            }
        )
    }

    if (dialogoProveedorPasskeys) {
        val esOscuro = isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { dialogoProveedorPasskeys = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema(com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = null,
                        tint = com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema(com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys),
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Text("Proveedor de credenciales", color = TextoPrincipal, style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Android requiere registrar a 'Bóveda local' como tu proveedor oficial de credenciales.",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "En \"Contraseñas y llaves de acceso\" activa Bóveda local para permitir el inicio de sesión automático con Passkeys.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Si tu fabricante personalizó el menú, busca \"Contraseñas\" en los ajustes de tu teléfono.",
                        color = TextoSecundario.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogoProveedorPasskeys = false
                    if (!com.jlnavas3.bovedalocal.util.AjustesSistema.abrirProveedorCredenciales(actividad)) {
                        vm.avisar("Ajustes › Contraseñas y cuentas › Contraseñas y llaves de acceso")
                    }
                }) {
                    Text("Configurar proveedor", color = com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogoProveedorPasskeys = false }) {
                    Text("Cerrar", color = TextoSecundario)
                }
            }
        )
    }
}
