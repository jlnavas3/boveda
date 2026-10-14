/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Resuelve la jerarquía de navegación y determina la pantalla padre
 * para el flujo de retorno y retroceso de la aplicación.
 */
object MapeoJerarquiaPantallas {

    fun resolverPadre(
        pantalla: Pantalla,
        alRetrocederWidgetTotp: () -> Unit = {},
        alRetrocederWidget1x1: () -> Unit = {}
    ): Pantalla? = when (pantalla) {
        // Nivel 4 -> Nivel 3
        is Pantalla.ReglasNormalizacion -> Pantalla.NormalizadorTitulos(idsEspecificos = pantalla.idsEspecificos)
        is Pantalla.AjustesBiometria -> Pantalla.BloqueoBiometria("01-SEG-BIO-BIO")
        is Pantalla.BloqueoApp -> Pantalla.BloqueoBiometria("01-SEG-BIO-APP")
        is Pantalla.FuerzaBruta -> Pantalla.BloqueoBiometria("01-SEG-BIO-BRU")

        // Nivel 3 -> Nivel 2
        // Bloque 01: Seguridad
        is Pantalla.BloqueoBiometria -> Pantalla.Seguridad("01-SEG-BIO-BLO")
        is Pantalla.SeguridadVisual -> Pantalla.Seguridad("01-SEG-BIO-VIS")
        is Pantalla.SeguridadVisualMemoria -> Pantalla.Seguridad("01-SEG-BIO-VIS")
        is Pantalla.Portapapeles -> Pantalla.Seguridad("01-SEG-BIO-CLP")
        is Pantalla.AuditoriaSeguridad -> Pantalla.Seguridad("01-SEG-BIO-AUD")
        is Pantalla.AjustesSenuelo -> Pantalla.Seguridad("01-SEG-SEN")
        is Pantalla.AjustesAutodestruccion -> Pantalla.Seguridad("01-SEG-DES-PIN")
        is Pantalla.AutodestruccionIntentos -> Pantalla.Seguridad("01-SEG-DES-INT")
        is Pantalla.Argon2id -> Pantalla.Seguridad("01-SEG-CRY")

        // Bloque 02: Tema y apariencia
        is Pantalla.TemaPaletas -> Pantalla.Tema("02-APA-THM-PAL")
        is Pantalla.AnimacionDesbloqueo -> Pantalla.Tema("02-APA-THM-ANI")
        is Pantalla.CalibracionAnimacion -> Pantalla.AnimacionDesbloqueo("02-APA-THM-ANI")
        is Pantalla.LaboratorioTemas -> Pantalla.Tema("02-APA-THM-LAB")
        is Pantalla.PersonalizarMenuLateral -> Pantalla.Ajustes("02-APA-MNL")
        is Pantalla.AjustesAvisos -> Pantalla.Ajustes("02-APA-AVI")

        // Bloque 02: Apariencia
        is Pantalla.OrganizacionEstructura -> Pantalla.OrganizacionLista("02-APA-DES-EST")
        is Pantalla.OrganizacionJerarquia -> Pantalla.OrganizacionLista("02-APA-DES-JER")
        is Pantalla.OrganizacionIndicadores -> Pantalla.OrganizacionLista("02-APA-DES-IND")
        is Pantalla.NormalizadorTitulos -> Pantalla.OrganizacionLista("02-APA-DES-TIT")
        is Pantalla.ColoresDatos -> when {
            pantalla.seccionId == "02-APA-THM-G04" -> Pantalla.OrganizacionIndicadores("02-APA-THM-G04")
            !pantalla.seccionId.isNullOrBlank() -> Pantalla.Ajustes(pantalla.seccionId)
            else -> Pantalla.Ajustes("02-APA-COL")
        }

        // Bloque 04: Historial, Widgets y Tile
        is Pantalla.HistorialClavesGeneradas -> Pantalla.AjustesHistorial("04-HER-HST-GEN")
        is Pantalla.HistorialCredenciales -> Pantalla.AjustesHistorial("04-HER-HST-ENT")

        is Pantalla.AjustesWidgetTotpSub -> Pantalla.AjustesWidget("04-HER-WGT-TOT")
        is Pantalla.AjustesWidget1x1Sub -> Pantalla.AjustesWidget("04-HER-WGT-1X1")
        is Pantalla.WidgetTotpAjustes -> Pantalla.AjustesWidgetTotpSub("04-HER-WGT-TOT-CFG")
        is Pantalla.CalibracionWidgetTotp -> {
            alRetrocederWidgetTotp()
            Pantalla.AjustesWidgetTotpSub("04-HER-WGT-CAL")
        }
        is Pantalla.Widget1x1Modo -> Pantalla.AjustesWidget1x1Sub("04-HER-WGT-MOD")
        is Pantalla.Widget1x1Comportamiento -> Pantalla.AjustesWidget1x1Sub("04-HER-WGT-CMP")
        is Pantalla.CalibracionWidget1x1 -> {
            alRetrocederWidget1x1()
            Pantalla.AjustesWidget1x1Sub("04-HER-WGT-1X1-CAL")
        }

        is Pantalla.TileConfiguracion -> Pantalla.TileRapido("04-HER-MSK-CFG")
        is Pantalla.ReglasAutocompletado -> Pantalla.AjustesAutocompletado("04-HER-PSK-REG")

        // Bloque 05: Copias
        is Pantalla.CopiaManual -> Pantalla.CopiaSeguridad("05-COP-MAN-FIL")
        is Pantalla.CopiaAutomaticaLocalSub -> Pantalla.CopiaSeguridad("05-COP-AUT-FIL")
        is Pantalla.CopiaRecordatorios -> Pantalla.CopiaSeguridad("05-COP-REC-FIL")
        is Pantalla.AjustesCopiaAutomatica -> Pantalla.CopiaAutomaticaLocalSub("05-COP-ATM-G01")
        is Pantalla.ExportarSelectivo -> Pantalla.CopiaManual("05-COP-EXP")

        // Bloque 06: Sistema y Avanzada
        is Pantalla.AvanzadaDesarrollo -> Pantalla.Avanzada("06-SIS-AVZ-DES")
        is Pantalla.AvanzadaAlumbrado -> Pantalla.Avanzada("06-SIS-AVZ-LGT")
        is Pantalla.ReorganizarAjustes -> Pantalla.Avanzada("06-SIS-AVZ-ORG")
        is Pantalla.AvanzadaHaptica -> Pantalla.Avanzada("06-SIS-AVZ-HAP")
        is Pantalla.AvanzadaZonaPeligro -> Pantalla.Avanzada("06-SIS-AVZ-PEL")
        is Pantalla.ColoresIdentificadores -> Pantalla.AvanzadaDesarrollo("06-SIS-AVZ-COL")

        is Pantalla.SaludBoveda -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-SLD")
        is Pantalla.Duplicados -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-DUP")
        is Pantalla.Papelera -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-PAP")
        is Pantalla.Identidades -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-IDE")
        is Pantalla.Categorias -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-CAT")

        // Nivel 3: Subpáginas de Formas -> Formas (Nivel 2)
        is Pantalla.FormasPresets,
        is Pantalla.FormasCurvatura,
        is Pantalla.FormasBorde,
        is Pantalla.FormasGrosor,
        is Pantalla.FormasEstilo,
        is Pantalla.FormasEspaciado -> Pantalla.Formas("02-APA-GEO")

        // Nivel 3: Subpáginas de Tipografía -> Tipografía (Nivel 2)
        is Pantalla.TipografiaPresets,
        is Pantalla.TipografiaEscala,
        is Pantalla.TipografiaFamilia,
        is Pantalla.TipografiaPeso,
        is Pantalla.TipografiaEspaciado -> Pantalla.Tipografia("02-APA-TYP")

        // Nivel 2: Personalización -> Ajustes (Nivel 1)
        is Pantalla.IndiceOla,
        is Pantalla.IndiceCresta,
        is Pantalla.IndiceHaptica,
        is Pantalla.IndiceResaltado -> Pantalla.AjustesIndice("02-APA-AZX")

        is Pantalla.Tema -> Pantalla.Ajustes("02-APA-THM")
        is Pantalla.Formas -> Pantalla.Ajustes("02-APA-GEO")
        is Pantalla.Tipografia -> Pantalla.Ajustes("02-APA-TYP")
        is Pantalla.OrganizacionLista -> Pantalla.Ajustes("02-APA-DES")
        is Pantalla.AjustesIndice -> Pantalla.Ajustes("02-APA-AZX")
        is Pantalla.FormatosCampos -> Pantalla.Ajustes("02-APA-FMT")
        is Pantalla.PlantillasCampos -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("02-APA-PLT")
        is Pantalla.AjustesWidget -> Pantalla.Ajustes("04-HER-WGT")
        is Pantalla.EntradasTipo -> when (pantalla.tipo) {
            TipoEntrada.LOGIN -> Pantalla.Ajustes("03-LST-LOG")
            TipoEntrada.NOTA -> Pantalla.Ajustes("03-LST-NOT")
            TipoEntrada.TARJETA -> Pantalla.Ajustes("03-LST-TAR")
            TipoEntrada.CONTACTO -> Pantalla.Ajustes("03-LST-CON")
            TipoEntrada.WIFI -> Pantalla.Ajustes("03-LST-WIF")
            TipoEntrada.CUENTA_BANCARIA -> Pantalla.Ajustes("03-LST-BNC")
            TipoEntrada.IDENTIDAD -> Pantalla.Ajustes("03-LST-IDN")
            TipoEntrada.SERVIDOR -> Pantalla.Ajustes("03-LST-SRV")
            TipoEntrada.WALLET -> Pantalla.Ajustes("03-LST-WAL")
            TipoEntrada.PASSKEY -> Pantalla.Ajustes("03-LST-PAS")
        }

        // Nivel 2: Seguridad -> Ajustes (Nivel 1)
        is Pantalla.Seguridad -> Pantalla.Ajustes("01-SEG-BIO")
        is Pantalla.AuditoriaAccesibilidad -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("01-SEG-ACC")
        is Pantalla.ContrasenasComunes -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("01-SEG-COM")

        // Nivel 2: Copias y datos -> Ajustes (Nivel 1)
        is Pantalla.CopiaSeguridad -> Pantalla.Ajustes("05-COP-MAN")
        is Pantalla.CsvGoogle -> Pantalla.Ajustes("05-COP-CSV")
        is Pantalla.KitEmergencia -> Pantalla.Ajustes("05-COP-KIT")

        // Nivel 2: Funciones -> Ajustes (Nivel 1)
        is Pantalla.AjustesAutenticador -> Pantalla.Ajustes("04-HER-AUT")
        is Pantalla.AjustesHistorial -> Pantalla.Ajustes("04-HER-HST")
        is Pantalla.HistorialClaves -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.AjustesCamara -> Pantalla.Ajustes("04-HER-CAM")
        is Pantalla.AjustesAutocompletado -> Pantalla.Ajustes("04-HER-PSK")
        is Pantalla.TileRapido -> Pantalla.Ajustes("04-HER-MSK")
        is Pantalla.DiccionarioPalabras -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("04-HER-DIC")

        // Nivel 2: Sistema -> Ajustes (Nivel 1)
        is Pantalla.Avanzada -> Pantalla.Ajustes("06-SIS-AVZ")
        is Pantalla.Registro -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("06-SIS-LOG")
        is Pantalla.AcercaDe -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("06-SIS-DGN")
        is Pantalla.AcercaDeApp -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("06-SIS-APP")
        is Pantalla.Licencia -> Pantalla.AcercaDeApp("06-SIS-APP")

        // Nivel 1: Ajustes -> Lista (Nivel 0)
        is Pantalla.Ajustes -> Pantalla.Lista

        // Pantallas abiertas directamente desde Lista
        is Pantalla.Generador -> Pantalla.Lista
        is Pantalla.Passkeys -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Ajustes("03-LST-PAS")
        is Pantalla.Autenticador -> Pantalla.Lista
        is Pantalla.CamaraQr -> Pantalla.Autenticador
        is Pantalla.Detalle -> Pantalla.Lista
        else -> null
    }
}
