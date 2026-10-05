package com.jlnavas3.bovedalocal.data

/**
 * Fuente única de la verdad para TODOS los valores predeterminados de la aplicación.
 * Ningún valor por defecto debe estar hardcodeado en ViewModels, pantallas ni componentes UI.
 */
object AjustesDefaults {

    // 1. Seguridad y Acceso
    object Seguridad {
        const val AUTO_BLOQUEO_SEGUNDOS = 60
        const val PORTAPAPELES_SEGUNDOS = 30
        const val MODO_GRABACION = false
        const val BIOMETRIA_ACTIVA = false
        const val BIOMETRIA_MODO = ""
        const val MOTOR_CAMARA = "auto"
        const val PROTECCION_PANTALLA = true
        const val PERFIL_ARGON2 = "estandar"
        const val UMBRAL_ANTIGUEDAD_DIAS = 180
    }

    // 1b. Seguridad Visual y Privacidad de Pantalla
    object SeguridadVisual {
        const val ACTIVA = false
        const val ESTILO = "desenfoque"
        const val AUTO_OCULTAR_SEGUNDOS = 10
        const val OCULTAR_USUARIO = true
        const val OCULTAR_CONTRASENA = true
        const val OCULTAR_TOTP = false
        const val OCULTAR_NOTAS = true
        const val OCULTAR_CAMPOS = true
    }

    // 2. Apariencia y Tema General
    object Tema {
        const val NOMBRE_PERSONALIZADO = ""
        const val ICONO_LAUNCHER = "gris"
        const val COLOR_ACENTO = ""
        const val COLOR_ICONOS_INTERNOS = ""
        const val COLOR_TITULOS = ""
        const val COLOR_TARJETAS = ""
        const val TEMA_APP = "sistema"
        const val COLOR_DINAMICO_SISTEMA = false
    }

    // 3. Colores Semánticos / Funcionales de Secciones
    object ColoresSecciones {
        const val SEGURIDAD = ""
        const val ARGON2 = ""
        const val CAMARA = ""
        const val DOS_FA = ""
        const val PASSKEYS = ""
        const val GENERADOR = ""
        const val SALUD = ""
        const val PAPELERA = ""
        const val EXPORTACION = ""
    }

    // 4. Colores Datos e Indicadores de Tarjetas
    object ColoresDatos {
        const val MOSTRAR_INDICADORES = true
        const val USUARIO = "#0284C7"
        const val CONTRASENA = "#0D9488"
        const val DOS_FA = "#F97316"
        const val PASSKEY = "#8B5CF6"
        const val WEB = "#06B6D4"
        const val APP = "#10B981"
    }

    // 5. Colores Bloques de Identificadores (IDs)
    object ColoresIds {
        const val SEGURIDAD = "#3F51B5"
        const val APARIENCIA = "#8E24AA"
        const val LISTA = "#00897B"
        const val HERRAMIENTAS = "#FB8C00"
        const val COPIAS = "#1E88E5"
        const val SISTEMA = "#607D8B"
    }

    // 6. Formas y Geometría
    object Formas {
        const val CURVATURA_ESQUINAS_DP = 16f
        const val GROSOR_BORDE_DP = 1.0f
        const val ESTILO_BORDE = "ninguno"
        const val ESPACIADO_COMPONENTES_DP = 14f
    }

    // 7. Tipografía y Textos
    object Tipografia {
        const val ESCALA_TEXTO = 1.0f
        const val PESO_TEXTO = "normal"
        const val CURSIVA_TEXTO = false
        const val ESPACIADO_LETRAS_SP = 0.0f
        const val INTERLINEADO_FACTOR = 1.0f
        const val FAMILIA_FUENTE = "sans"
    }

    // 8. Índice Alfabético Lateral (Ola Niagara)
    object Indice {
        const val MOSTRAR = true
        const val EFECTO_OLA = true
        const val AMPLITUD_OLA_DP = 109f
        const val RADIO_OLA_DP = 169f
        const val ESCALA_LETRAS = 1.5f
        const val MOSTRAR_CIRCULO = true
        const val TAMANO_CIRCULO_DP = 50f
        const val OFFSET_CIRCULO_DP = 136f
        const val HAPTICA = true
        const val ANCHO_TACTIL_DP = 45f
        const val TONO_LETRAS = 80f
        const val INCLUIR_ENIE = true
        const val RESALTAR_ENTRADAS = true
        const val RESALTAR_SOLO_PRIMERA = true
        const val ALINEAR_CON_CRESTA = true
    }

    // 9. Animación Desbloqueo: Engranajes y Puerta
    object Animacion {
        const val TIPO_DESBLOQUEO = "engranajes"

        object Engranajes {
            const val VELOCIDAD = 24f
            const val GROSOR_BORDE = 0.7f
            const val ALTURA_DIENTES = 0.76f
            const val ANCHO_DIENTES = 1.00f
            const val GROSOR_RADIOS = 1.40f
            const val CURVATURA_RADIOS = 1.00f
            const val CANTIDAD_RADIOS = 6
            const val RADIO_INTERIOR = 0.80f
            const val TAMANO_EJE = 1.23f
            const val SOMBRA_INTENSIDAD = 0.95f
            const val COLOR_BRILLO = "#ABA799"
            const val COLOR_PRINCIPAL = "#918D7E"
            const val COLOR_SOMBRA_MEDIO = "#635C57"
            const val COLOR_SOMBRA_OSCURO = "#404038"
            const val COLOR_BISEL = "#A5A19D"
            const val COLOR_INTERIOR = "#00000000"
            const val COLOR_CUBO = "#D3D1C8"
            const val COLOR_EJE = "#141316"
        }

        object Puerta {
            const val VELOCIDAD = 1.0f
            const val GROSOR_ANILLOS = 1.0f
            const val COLOR = ""
        }
    }

    // 10. Widgets
    object WidgetTotp {
        const val GROSOR_BORDE_DP = 0f
        const val CURVATURA_ESQUINAS_DP = 22f
        const val TRANSPARENCIA_FONDO = 0.85f
        const val COLOR_BORDE = "#38383A"
        const val COLOR_CONTADOR = "#FFFFFF"
        const val COLOR_CODIGO = "#FFFFFF"
        const val COLOR_TITULO_ICONO = "#FFFFFF"
        const val COLOR_FILAS = "#00000000"
        const val COLOR_FILAS_DEFECTO = "#1C1C1E"
        const val TRANSPARENCIA_FILAS = 0.0f
        const val VIDRIO_ESMERILADO = false
        const val ESMERILADO_INTENSIDAD = 0.60f
        const val ESMERILADO_LUZ = 0.40f
        const val HAPTICA = true
        const val HAPTICA_INTENSIDAD = 0.20f
    }

    object Widget1x1 {
        const val HAPTICA = true
        const val HAPTICA_INTENSIDAD = 0.20f
        const val MODO = "aleatoria"
        const val LONGITUD = 20
        const val PATRON = "XXXXX-XXXXX-XXXXX-XXXXX"
        const val SIMBOLOS = "!@#$%&*()_-=+[]{}?/,.:;"
        const val COPIAR_PORTAPAPELES = true
        const val MOSTRAR_TOAST = true
        const val GROSOR_BORDE_DP = 0f
        const val CURVATURA_ESQUINAS_DP = 15f
        const val TRANSPARENCIA_FONDO = 0.85f
        const val TAMANO_DP = 55f
        const val ANCHO_DP = 55f
        const val ALTO_DP = 51f
        const val BLOQUEAR_PROPORCION = false
        const val OFFSET_X = 0f
        const val OFFSET_Y = 4f
        const val ALINEAMIENTO = "arriba"
        const val COLOR_BORDE = "#38383A"
        const val COLOR_ICONO = "#FFFFFF"
        const val COLOR_FONDO = "#1C1C1E"
        const val VIDRIO_ESMERILADO = false
        const val ESMERILADO_INTENSIDAD = 0.60f
        const val ESMERILADO_LUZ = 0.40f
        const val DICEWARE_PALABRAS = 5
        const val DICEWARE_SEPARADOR = "-"
    }

    // 11. Quick Settings Tile
    object Tile {
        const val MODO = "longitud"
        const val LONGITUD = 20
        const val PATRON = "XXXXX-XXXXX-XXXXX-XXXXX"
        const val SIMBOLOS = "!@#$%&*()_-=+[]{}?/,.:;"
        const val DICEWARE_PALABRAS = 5
        const val DICEWARE_SEPARADOR = "-"
        const val COPIAR_PORTAPAPELES = true
        const val MOSTRAR_TOAST = true
        const val HAPTICA = true
        const val HAPTICA_INTENSIDAD = 0.8f
    }

    // 12. Lista y Formatos
    object ListaFormatos {
        const val DENSIDAD_LISTA = "predeterminada"
        const val CRITERIO_ORDENACION = "NOMBRE_AZ"
        const val AGRUPAR_POR_SITIO = true
        const val MODO_IDENTIDADES = "chips"
        const val JERARQUIA_ORGANIZACION = "identidad_sobre_coleccion"
        const val FORMATO_FECHA = "DD/MM/AAAA"
        const val FORMATO_HORA = "24h"
        const val FORMATO_TELEFONO = "### ### ####"
        const val SEPARADOR_DECIMAL = "."
    }

    // 13. Generador Manual 2FA
    object TotpManual {
        const val DIGITOS = 6
        const val PERIODO = 30
        const val ALGORITMO = "HmacSHA1"
        const val SEPARAR_DIGITOS = true
    }

    // 14. Historial, Copias y Respaldos
    object HistorialCopias {
        const val HISTORIAL_MAX = 15
        const val HISTORIAL_VACIADO_AUTO = true
        const val HISTORIAL_TIEMPO_AUTO_DESTRUCCION_MS = 30 * 60 * 1000L
        const val BACKUP_AUTO_FRECUENCIA_DIAS = 0
        const val BACKUP_AUTO_PASSWORD_CIFRADO = ""
        const val BACKUP_AUTO_ULTIMA_EJECUCION = 0L
        const val BACKUP_AUTO_MAX_COPIAS = 5
        const val BACKUP_AUTO_PATRON_NOMBRE = "{99}-backup-{FECHA}"
        const val BACKUP_AUTO_SECUENCIA = 0
        const val RECORDATORIO_EXPORTACION_DIAS = 30
        const val CSV_GOOGLE_RUTA = ""
        const val CSV_GOOGLE_URI = ""
        const val CSV_GOOGLE_CUENTAS = 0
        const val CSV_GOOGLE_ELIMINADO = false
    }

    // 15. UI / UX / Alumbrado / Háptica
    object Interaccion {
        const val ALUMBRADO_ACTIVO = true
        const val ALUMBRADO_INTENSIDAD = 0.5f
        const val ALUMBRADO_REPETICIONES = 2
        const val ALUMBRADO_DURACION_MS = 600
        const val HAPTICA_APP = true
        const val HAPTICA_APP_INTENSIDAD = 0.10f
        const val MOSTRAR_IDS_AJUSTES = false
    }
}
