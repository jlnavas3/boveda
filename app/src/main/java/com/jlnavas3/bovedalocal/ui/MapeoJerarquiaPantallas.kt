package com.jlnavas3.bovedalocal.ui

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
        // Nivel 3 -> Nivel 2
        is Pantalla.CalibracionAnimacion -> Pantalla.Tema("02-APA-THM-ANI")
        is Pantalla.ColoresDatos -> Pantalla.OrganizacionLista("02-APA-THM-G04")
        is Pantalla.CalibracionWidgetTotp -> {
            alRetrocederWidgetTotp()
            Pantalla.AjustesWidget("04-HER-WGT-CAL")
        }
        is Pantalla.CalibracionWidget1x1 -> {
            alRetrocederWidget1x1()
            Pantalla.AjustesWidget("04-HER-WGT-1X1")
        }

        is Pantalla.SaludBoveda -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Duplicados -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Papelera -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Identidades -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Categorias -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista

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
        is Pantalla.IndiceResaltado -> Pantalla.AjustesIndice("03-LST-AZX")
        is Pantalla.ExportarSelectivo -> Pantalla.CopiaSeguridad("05-COP-EXP")

        is Pantalla.Tema -> Pantalla.Ajustes("02-APA-THM")
        is Pantalla.Formas -> Pantalla.Ajustes("02-APA-GEO")
        is Pantalla.Tipografia -> Pantalla.Ajustes("02-APA-TYP")
        is Pantalla.OrganizacionLista -> Pantalla.Ajustes("03-LST-DES")
        is Pantalla.AjustesIndice -> Pantalla.Ajustes("03-LST-AZX")
        is Pantalla.FormatosCampos -> Pantalla.Ajustes("03-LST-FMT")
        is Pantalla.WidgetTotpAjustes,
        is Pantalla.Widget1x1Modo,
        is Pantalla.Widget1x1Comportamiento -> Pantalla.AjustesWidget("04-HER-WGT")
        is Pantalla.AjustesWidget -> Pantalla.Ajustes("04-HER-WGT")

        // Nivel 2: Seguridad -> Ajustes (Nivel 1)
        is Pantalla.Seguridad -> Pantalla.Ajustes("01-SEG-BIO")
        is Pantalla.AjustesSenuelo -> Pantalla.Ajustes("01-SEG-SEN")
        is Pantalla.AjustesAutodestruccion -> Pantalla.Ajustes("01-SEG-DES")
        is Pantalla.Argon2id -> Pantalla.Ajustes("01-SEG-CRY")

        // Nivel 2: Copias y datos -> Ajustes (Nivel 1)
        is Pantalla.AjustesCopiaAutomatica -> Pantalla.CopiaSeguridad("05-COP-ATM-G01")
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

        // Nivel 2: Sistema -> Ajustes (Nivel 1)
        is Pantalla.ColoresIdentificadores -> Pantalla.Avanzada("06-SIS-AVZ-G01")
        is Pantalla.Avanzada -> Pantalla.Ajustes("06-SIS-AVZ")
        is Pantalla.Registro -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.AcercaDe -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista

        // Nivel 1: Ajustes -> Lista (Nivel 0)
        is Pantalla.Ajustes -> Pantalla.Lista

        // Pantallas abiertas directamente desde Lista
        is Pantalla.Generador -> Pantalla.Lista
        is Pantalla.Passkeys -> Pantalla.Lista
        is Pantalla.Autenticador -> Pantalla.Lista
        is Pantalla.CamaraQr -> Pantalla.Autenticador
        is Pantalla.Detalle -> Pantalla.Lista
        else -> null
    }
}
