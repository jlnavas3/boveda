/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import android.net.Uri
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

val uriBvdaGlobal = MutableStateFlow<Uri?>(null)

interface VaultBackupDelegate {
    val repositorio: VaultRepository
    val avisoInterno: MutableStateFlow<String?>
    val errorInterno: MutableStateFlow<String?>
    fun ejecutar(bloque: suspend () -> Unit)
    fun ir(pantalla: Pantalla) {}
    fun irRaiz(pantalla: Pantalla) {}
    fun prepararNormalizacionTitulos(entradasEspecificas: List<com.jlnavas3.bovedalocal.data.Entrada>?) {}

    val uriBvdaPendiente: MutableStateFlow<Uri?> get() = uriBvdaGlobal
    fun establecerUriBvdaPendiente(uri: Uri?) { uriBvdaGlobal.value = uri }
    fun descartarUriBvdaPendiente() { uriBvdaGlobal.value = null }

    fun exportar(password: String, escritor: (ByteArray) -> Unit) {
        ejecutar {
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "exportar_boveda",
                titulo = "Copia de seguridad",
                faseInicial = "Derivando clave segura Argon2id...",
                totalEstimado = 1
            )
            val chars = password.toCharArray()
            try {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Cifrando credenciales con AES-GCM...", 0.45f)
                val datos = withContext(Dispatchers.Default) { repositorio.exportar(chars) }
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Escribiendo archivo de seguridad...", 0.85f)
                withContext(Dispatchers.IO) { escritor(datos) }
                repositorio.ajustes.actualizar { it.copy(ultimaExportacionEn = System.currentTimeMillis()) }
                Diagnostico.apuntar("bóveda", "Copia de seguridad cifrada exportada correctamente")
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "Copia de seguridad exportada y cifrada",
                    totalCuentas = 1,
                    textoAccionResultado = null,
                    textoAccionSecundaria = "Aceptar"
                )
                avisoInterno.value = "Bóveda exportada y cifrada"
            } catch (e: Exception) {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("No se pudo exportar: ${e.message ?: "error desconocido"}")
                Diagnostico.apuntar("bóveda", "Fallo al exportar copia de seguridad: ${e.message ?: "error desconocido"}", e)
                errorInterno.value = "No se pudo exportar: ${e.message ?: "error desconocido"}"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun exportarSelectivo(idsEntradas: Set<String>, password: String, escritor: (ByteArray) -> Unit) {
        ejecutar {
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "exportar_selectivo",
                titulo = "Exportación selectiva",
                faseInicial = "Preparando ${idsEntradas.size} entradas seleccionadas...",
                totalEstimado = idsEntradas.size
            )
            val chars = password.toCharArray()
            try {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Cifrando datos con AES-GCM...", 0.50f)
                val datos = withContext(Dispatchers.Default) { repositorio.exportarSelectivo(idsEntradas, chars) }
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase("Escribiendo archivo de respaldo...", 0.85f)
                withContext(Dispatchers.IO) { escritor(datos) }
                Diagnostico.apuntar("bóveda", "Exportación selectiva cifrada (${idsEntradas.size} entradas) completada correctamente")
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "${idsEntradas.size} credenciales exportadas",
                    totalCuentas = idsEntradas.size,
                    textoAccionResultado = null,
                    textoAccionSecundaria = "Aceptar"
                )
                avisoInterno.value = "Exportadas ${idsEntradas.size} entradas seleccionadas"
            } catch (e: Exception) {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("No se pudo exportar: ${e.message ?: "error desconocido"}")
                Diagnostico.apuntar("bóveda", "Fallo al exportar copia selectiva: ${e.message ?: "error desconocido"}", e)
                errorInterno.value = "No se pudo exportar: ${e.message ?: "error desconocido"}"
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importar(
        password: String,
        onResultado: ((Int, Int) -> Unit)? = null,
        lector: () -> ByteArray
    ) {
        ejecutar {
            val chars = password.toCharArray()
            try {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                    id = "restaurar_boveda",
                    titulo = "Restaurar copia",
                    faseInicial = "Leyendo archivo..."
                )
                val datos = withContext(Dispatchers.IO) { lector() }

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Descifrando y restaurando...",
                    progresoGlobal = 0.50f
                )

                val nuevas = withContext(Dispatchers.Default) { repositorio.importar(datos, chars) }
                val duplicados = withContext(Dispatchers.Default) {
                    AnalizadorDuplicados.analizar(repositorio.entradas()).size
                }

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Finalizando restauración...",
                    progresoGlobal = 0.95f
                )
                kotlinx.coroutines.delay(150)

                Diagnostico.apuntar("bóveda", "Bóveda importada desde copia cifrada ($nuevas entradas incorporadas, $duplicados duplicados)")
                val mensaje = if (duplicados > 0) "Se restauraron $nuevas entradas ($duplicados duplicadas)" else "Se restauraron $nuevas entradas con éxito"
                avisoInterno.value = mensaje

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = mensaje,
                    totalCuentas = nuevas,
                    textoAccionResultado = "Ver listado",
                    textoAccionSecundaria = null,
                    accionResultado = { irRaiz(Pantalla.Lista) }
                )

                onResultado?.invoke(nuevas, duplicados)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo cifrado: contraseña incorrecta o archivo inválido", e)
                errorInterno.value = "No se pudo importar: contraseña incorrecta o archivo inválido"
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("Contraseña incorrecta o archivo inválido")
            } finally {
                Zeroizar.borrar(chars)
            }
        }
    }

    fun importarCsv(
        nombreArchivo: String = "Google Passwords.csv",
        lector: () -> ByteArray,
        onResultado: ((Int) -> Unit)? = null
    ) {
        ejecutar {
            try {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                    id = "importar_csv",
                    titulo = "Importación CSV",
                    faseInicial = "Leyendo archivo..."
                )
                val datos = withContext(Dispatchers.IO) { lector() }

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Analizando datos CSV...",
                    progresoGlobal = 0.10f
                )

                val entradasParseadas = withContext(Dispatchers.Default) {
                    com.jlnavas3.bovedalocal.data.ImportadorCsv.parsear(datos)
                }
                val total = entradasParseadas.size

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Importando $total cuentas...",
                    progresoGlobal = 0.15f
                )

                val delayMs = (2000L / total.coerceAtLeast(1)).coerceIn(1L, 25L)

                entradasParseadas.forEachIndexed { index, entrada ->
                    val procesadas = index + 1
                    val progreso = 0.15f + 0.70f * (procesadas.toFloat() / total)
                    val item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                        id = entrada.id,
                        titulo = entrada.titulo.ifBlank { "Cuenta" },
                        subtitulo = entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "" },
                        progreso = 1.0f,
                        completado = true
                    )
                    com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                        item = item,
                        progresoGlobal = progreso,
                        procesadas = procesadas
                    )
                    if (delayMs > 0) {
                        kotlinx.coroutines.delay(delayMs)
                    }
                }

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Cifrando bóveda con AES-256...",
                    progresoGlobal = 0.92f
                )

                val nuevas = withContext(Dispatchers.IO) {
                    repositorio.importarEntradas(entradasParseadas)
                }
                kotlinx.coroutines.delay(180)

                Diagnostico.apuntar("bóveda", "Importación CSV completada con éxito ($nuevas entradas incorporadas)")
                avisoInterno.value = "Importadas $nuevas entradas desde CSV"

                val comprometidas = entradasParseadas.count {
                    com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(it, repositorio.ajustes.actual.tldsDescartables)
                }
                val sugerirNormalizar = repositorio.ajustes.actual.sugerirNormalizarTrasImportar && comprometidas > 0

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "Se importaron $nuevas cuentas con éxito",
                    totalCuentas = nuevas,
                    textoAccionResultado = if (sugerirNormalizar) "Renombrar" else "Ver listado",
                    textoAccionSecundaria = if (sugerirNormalizar) "Ver listado" else null,
                    accionResultado = {
                        if (sugerirNormalizar) {
                            prepararNormalizacionTitulos(entradasParseadas)
                            irRaiz(Pantalla.Lista)
                            ir(Pantalla.NormalizadorTitulos(esPostImportacion = true))
                        } else {
                            irRaiz(Pantalla.Lista)
                        }
                    }
                )

                onResultado?.invoke(nuevas)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar archivo CSV: ${e.message ?: "formato no reconocido"}", e)
                errorInterno.value = "No se pudo importar el CSV: ${e.message ?: "formato no reconocido"}"
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar(e.message ?: "Formato no reconocido")
            }
        }
    }

    fun registrarCsvGoogleImportado(ruta: String, uri: String, cuentas: Int) {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = ruta,
                csvGoogleUri = uri,
                csvGoogleCuentas = cuentas,
                csvGoogleEliminado = false
            )
        }
    }

    fun marcarCsvGoogleEliminado(eliminado: Boolean) {
        repositorio.ajustes.actualizar {
            it.copy(csvGoogleEliminado = eliminado)
        }
        if (eliminado) {
            Diagnostico.apuntar("seguridad", "Archivo CSV de Google eliminado de forma segura del almacenamiento")
        }
    }

    fun descartarAvisoCsvGoogle() {
        repositorio.ajustes.actualizar {
            it.copy(
                csvGoogleRuta = "",
                csvGoogleUri = "",
                csvGoogleCuentas = 0,
                csvGoogleEliminado = false
            )
        }
    }

    fun importarEntradasCxf(
        entradas: List<com.jlnavas3.bovedalocal.data.Entrada>,
        origen: String = "Google Password Manager",
        onCompletado: ((Int) -> Unit)? = null
    ) {
        if (entradas.isEmpty()) {
            onCompletado?.invoke(0)
            return
        }
        ejecutar {
            try {
                val total = entradas.size
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                    id = "importar_cxf",
                    titulo = "Transferencia directa",
                    faseInicial = "Importando $total credenciales...",
                    totalEstimado = total
                )

                val delayMs = (2000L / total.coerceAtLeast(1)).coerceIn(1L, 25L)

                entradas.forEachIndexed { index, entrada ->
                    val procesadas = index + 1
                    val progreso = 0.05f + 0.82f * (procesadas.toFloat() / total)
                    val item = com.jlnavas3.bovedalocal.data.ItemProgresoEntrada(
                        id = entrada.id,
                        titulo = entrada.titulo.ifBlank { "Cuenta" },
                        subtitulo = entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "" },
                        progreso = 1.0f,
                        completado = true
                    )
                    com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.reportarItem(
                        item = item,
                        progresoGlobal = progreso,
                        procesadas = procesadas
                    )
                    if (delayMs > 0) {
                        kotlinx.coroutines.delay(delayMs)
                    }
                }

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    fase = "Cifrando bóveda con AES-256...",
                    progresoGlobal = 0.92f
                )

                val nuevas = withContext(Dispatchers.IO) {
                    repositorio.importarEntradas(entradas)
                }

                kotlinx.coroutines.delay(180)

                Diagnostico.apuntar("bóveda", "Importación directa CXF completada ($nuevas entradas guardadas)")
                avisoInterno.value = if (nuevas == 1) "1 credencial importada correctamente" else "$nuevas credenciales importadas correctamente"

                val comprometidas = entradas.count {
                    com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos.esTituloComprometido(it, repositorio.ajustes.actual.tldsDescartables)
                }
                val sugerirNormalizar = repositorio.ajustes.actual.sugerirNormalizarTrasImportar && comprometidas > 0

                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "Se importaron $nuevas credenciales con éxito",
                    totalCuentas = nuevas,
                    textoAccionResultado = if (sugerirNormalizar) "Renombrar" else "Ver listado",
                    textoAccionSecundaria = if (sugerirNormalizar) "Ver listado" else null,
                    accionResultado = {
                        if (sugerirNormalizar) {
                            prepararNormalizacionTitulos(entradas)
                            irRaiz(Pantalla.Lista)
                            ir(Pantalla.NormalizadorTitulos(esPostImportacion = true))
                        } else {
                            irRaiz(Pantalla.Lista)
                        }
                    }
                )

                onCompletado?.invoke(nuevas)
            } catch (e: Exception) {
                Diagnostico.apuntar("bóveda", "Fallo al importar credenciales CXF", e)
                errorInterno.value = "No se pudieron importar las credenciales: ${e.message ?: "error desconocido"}"
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar(e.message ?: "Error desconocido")
            }
        }
    }

    fun cambiarContrasenaMaestra(actual: String, nueva: String) {
        ejecutar {
            com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.iniciar(
                id = "cambiar_contrasena_maestra",
                titulo = "Contraseña maestra",
                faseInicial = "Verificando contraseña actual...",
                totalEstimado = 1
            )
            val viejaChars = actual.toCharArray()
            val nuevaChars = nueva.toCharArray()
            try {
                val correcta = withContext(Dispatchers.Default) { repositorio.verificarContrasena(viejaChars) }
                if (!correcta) {
                    com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("La contraseña actual no es correcta")
                    Diagnostico.apuntar("bóveda", "Intento de cambio de contraseña maestra rechazado (contraseña actual incorrecta)")
                    errorInterno.value = "La contraseña actual no es correcta"
                    return@ejecutar
                }
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    "Derivando nueva clave Argon2id...",
                    0.45f
                )
                withContext(Dispatchers.Default) { repositorio.cambiarContrasenaMaestra(nuevaChars) }
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.actualizarFase(
                    "Re-cifrando base de datos...",
                    0.85f
                )
                Diagnostico.apuntar("bóveda", "Contraseña maestra de la bóveda modificada exitosamente")
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.completar(
                    mensajeExito = "Contraseña maestra actualizada con éxito",
                    totalCuentas = 1,
                    textoAccionResultado = null,
                    textoAccionSecundaria = "Aceptar"
                )
                avisoInterno.value = "Contraseña maestra cambiada. Vuelve a activar la biometría."
            } catch (e: Exception) {
                com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones.fallar("Error: ${e.message ?: "al cambiar contraseña"}")
                errorInterno.value = "No se pudo cambiar la contraseña: ${e.message ?: "error desconocido"}"
            } finally {
                Zeroizar.borrar(viejaChars)
                Zeroizar.borrar(nuevaChars)
            }
        }
    }
}
