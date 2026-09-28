package com.jlnavas3.bovedalocal.ui

import kotlinx.coroutines.flow.MutableStateFlow

interface VaultNavegacionDelegate {
    val pantallaInterna: MutableStateFlow<Pantalla>
    val navegandoAtrasInterno: MutableStateFlow<Boolean>
    val pilaNavegacion: ArrayDeque<Pantalla>
    var ultimoWidgetAjustesSeleccionado: Int

    fun padreDe(pantalla: Pantalla): Pantalla? = when (pantalla) {
        // Nivel 3 -> Nivel 2
        is Pantalla.CalibracionAnimacion -> Pantalla.Tema("02-APA-THM-ANI")
        is Pantalla.ColoresDatos -> Pantalla.Tema("02-APA-THM-DAT")
        is Pantalla.CalibracionWidgetTotp -> {
            ultimoWidgetAjustesSeleccionado = 0
            Pantalla.AjustesWidget("04-HER-WGT-CAL")
        }
        is Pantalla.CalibracionWidget1x1 -> {
            ultimoWidgetAjustesSeleccionado = 1
            Pantalla.AjustesWidget("04-HER-WGT-1X1")
        }

        is Pantalla.SaludBoveda -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Duplicados -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista
        is Pantalla.Papelera -> if (!pantalla.seccionId.isNullOrBlank()) Pantalla.Ajustes(pantalla.seccionId) else Pantalla.Lista

        // Nivel 3: Subpáginas de Formas -> Formas (Nivel 2)
        is Pantalla.FormasPresets,
        is Pantalla.FormasCurvatura,
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
        is Pantalla.Detalle -> Pantalla.Lista
        else -> null
    }

    fun ir(pantalla: Pantalla) {
        navegandoAtrasInterno.value = false
        if (pantalla != pantallaInterna.value) {
            val origen = when {
                pantallaInterna.value is Pantalla.Ajustes -> {
                    val idHijo = when (pantalla) {
                        is Pantalla.Generador -> "04-HER-GEN"
                        is Pantalla.Passkeys -> "04-HER-PSK"
                        is Pantalla.Autenticador -> "04-HER-2FA"
                        else -> null
                    }
                    if (idHijo != null) {
                        Pantalla.Ajustes(idHijo)
                    } else {
                        val padre = padreDe(pantalla)
                        if (padre is Pantalla.Ajustes && !padre.seccionId.isNullOrBlank()) padre
                        else pantallaInterna.value
                    }
                }
                pantallaInterna.value is Pantalla.Lista -> {
                    val padre = padreDe(pantalla)
                    if (padre is Pantalla.Ajustes && !padre.seccionId.isNullOrBlank()) padre
                    else Pantalla.Lista
                }
                else -> {
                    val padre = padreDe(pantalla)
                    padre ?: pantallaInterna.value
                }
            }
            if (pantallaInterna.value is Pantalla.Lista && origen !is Pantalla.Lista) {
                pilaNavegacion.addLast(Pantalla.Lista)
            }
            pilaNavegacion.addLast(origen)
            if (pilaNavegacion.size > 20) pilaNavegacion.removeFirst()
        }
        pantallaInterna.value = pantalla
    }

    fun irDesdeMenuLateral(pantalla: Pantalla) {
        navegandoAtrasInterno.value = false
        pilaNavegacion.clear()
        pilaNavegacion.addLast(Pantalla.Lista)
        pantallaInterna.value = pantalla
    }

    fun irPorId(id: String) {
        val limpio = id.trim()
        val destino = when {
            limpio.startsWith("01-SEG-BIO") || limpio == "01" || limpio.startsWith("01.1") || limpio.startsWith("01.0") -> Pantalla.Seguridad(limpio)
            limpio.startsWith("01-SEG-SEN") || limpio.startsWith("01.3") -> Pantalla.AjustesSenuelo(limpio)
            limpio.startsWith("01-SEG-DES") || limpio.startsWith("01.4") -> Pantalla.AjustesAutodestruccion(limpio)
            limpio.startsWith("01-SEG-CRY") || limpio.startsWith("01.5") -> Pantalla.Argon2id(limpio)

            limpio.startsWith("02-APA-THM-ANI") || limpio.startsWith("02-APA-THM-CAL") || limpio.startsWith("03.2.G2") -> Pantalla.CalibracionAnimacion(limpio)
            limpio.startsWith("02-APA-THM-DAT") || limpio.startsWith("03.2.1") -> Pantalla.ColoresDatos(limpio)
            limpio.startsWith("02-APA-THM") || limpio.startsWith("02.1") || limpio.startsWith("09.2") -> Pantalla.Tema(limpio)

            limpio.startsWith("02-APA-GEO-PRE") -> Pantalla.FormasPresets(limpio)
            limpio.startsWith("02-APA-GEO-CRV") -> Pantalla.FormasCurvatura(limpio)
            limpio.startsWith("02-APA-GEO-GRO") -> Pantalla.FormasGrosor(limpio)
            limpio.startsWith("02-APA-GEO-EST") -> Pantalla.FormasEstilo(limpio)
            limpio.startsWith("02-APA-GEO-ESP") -> Pantalla.FormasEspaciado(limpio)
            limpio.startsWith("02-APA-GEO") || limpio.startsWith("02.2") -> Pantalla.Formas(limpio)

            limpio.startsWith("02-APA-TYP-PRE") -> Pantalla.TipografiaPresets(limpio)
            limpio.startsWith("02-APA-TYP-ESC") -> Pantalla.TipografiaEscala(limpio)
            limpio.startsWith("02-APA-TYP-FAM") -> Pantalla.TipografiaFamilia(limpio)
            limpio.startsWith("02-APA-TYP-PES") -> Pantalla.TipografiaPeso(limpio)
            limpio.startsWith("02-APA-TYP-ESP") -> Pantalla.TipografiaEspaciado(limpio)
            limpio.startsWith("02-APA-TYP") || limpio.startsWith("02.3") -> Pantalla.Tipografia(limpio)

            limpio.startsWith("03-LST-DES") || limpio.startsWith("03.1") || limpio.startsWith("09.6") -> Pantalla.OrganizacionLista(limpio)
            limpio.startsWith("03-LST-AZX-OLA") -> Pantalla.IndiceOla(limpio)
            limpio.startsWith("03-LST-AZX-CRE") -> Pantalla.IndiceCresta(limpio)
            limpio.startsWith("03-LST-AZX-HAP") -> Pantalla.IndiceHaptica(limpio)
            limpio.startsWith("03-LST-AZX-RES") -> Pantalla.IndiceResaltado(limpio)
            limpio.startsWith("03-LST-AZX") || limpio.startsWith("03.2") || limpio.startsWith("09.5") -> Pantalla.AjustesIndice(limpio)
            limpio.startsWith("03-LST-FMT") || limpio.startsWith("03.3") || limpio.startsWith("10") -> Pantalla.FormatosCampos(limpio)

            limpio.startsWith("04-HER-AUT") || limpio.startsWith("04.1") || limpio == "07" || limpio.startsWith("07.0") -> Pantalla.AjustesAutenticador(limpio)
            limpio.startsWith("04-HER-HST-CFG") -> Pantalla.AjustesHistorial(limpio)
            limpio.startsWith("04-HER-HST") || limpio.startsWith("04.2") || limpio == "04" || limpio.startsWith("04.0") -> Pantalla.HistorialClaves(limpio)
            limpio.startsWith("04-HER-CAM") || limpio.startsWith("04.3") || limpio == "05" || limpio.startsWith("05.0") -> Pantalla.AjustesCamara(limpio)
            limpio.startsWith("04-HER-WGT-TOT") || limpio.startsWith("04.4.1") -> Pantalla.WidgetTotpAjustes(limpio)
            limpio.startsWith("04-HER-WGT-CAL") || limpio.startsWith("04.4.2") -> Pantalla.CalibracionWidgetTotp(limpio)
            limpio.startsWith("04-HER-WGT-MOD") || limpio.startsWith("04.4.3") -> Pantalla.Widget1x1Modo(limpio)
            limpio.startsWith("04-HER-WGT-CMP") || limpio.startsWith("04.4.4") -> Pantalla.Widget1x1Comportamiento(limpio)
            limpio.startsWith("04-HER-WGT-1X1") || limpio.startsWith("04.4.5") -> Pantalla.CalibracionWidget1x1(limpio)
            limpio.startsWith("04-HER-WGT") || limpio.startsWith("04.4") || limpio.startsWith("09.4") -> Pantalla.AjustesWidget(limpio)
            limpio.startsWith("04-HER-MSK") || limpio.startsWith("04.5") -> Pantalla.TileRapido(limpio)
            limpio.startsWith("04-HER-PSK") || limpio.startsWith("04.6") -> Pantalla.Passkeys

            limpio.startsWith("05-COP-ATM") || limpio.startsWith("05.1.4") -> Pantalla.AjustesCopiaAutomatica(limpio)
            limpio.startsWith("05-COP-EXP") || limpio.startsWith("05.1.2") -> Pantalla.ExportarSelectivo(limpio)
            limpio.startsWith("05-COP-MAN") || limpio.startsWith("05.1") || limpio == "06" || limpio.startsWith("06.0") -> Pantalla.CopiaSeguridad(limpio)
            limpio.startsWith("05-COP-CSV") || limpio.startsWith("05.2") || limpio == "08" || limpio.startsWith("08.0") -> Pantalla.CsvGoogle(limpio)
            limpio.startsWith("05-COP-KIT") || limpio.startsWith("05.3") -> Pantalla.KitEmergencia(limpio)

            limpio.startsWith("06-SIS-AVZ-COL") || limpio.startsWith("06.1.2b") || limpio.contains("COL-IDS") -> Pantalla.ColoresIdentificadores(limpio)
            limpio.startsWith("06-SIS-AVZ") || limpio.startsWith("06.1") || limpio.startsWith("11.1") || limpio == "11" -> Pantalla.Avanzada(limpio)
            limpio.startsWith("06-SIS-LOG") || limpio.startsWith("06.2") -> Pantalla.Registro(limpio)
            limpio.startsWith("06-SIS-DGN") || limpio.startsWith("06.3") || limpio.startsWith("06.4") || limpio.startsWith("11.3.1") -> Pantalla.AcercaDe(limpio)
            else -> Pantalla.Ajustes(limpio)
        }
        navegandoAtrasInterno.value = false
        if (destino != pantallaInterna.value) {
            val origen = when {
                pantallaInterna.value is Pantalla.Ajustes -> Pantalla.Ajustes(limpio)
                else -> {
                    val padre = padreDe(destino)
                    padre ?: pantallaInterna.value
                }
            }
            if (pantallaInterna.value is Pantalla.Lista && origen !is Pantalla.Lista) {
                pilaNavegacion.addLast(Pantalla.Lista)
            }
            pilaNavegacion.addLast(origen)
            if (pilaNavegacion.size > 20) pilaNavegacion.removeFirst()
        }
        pantallaInterna.value = destino
    }

    fun irRaiz(pantalla: Pantalla) {
        navegandoAtrasInterno.value = true
        pilaNavegacion.clear()
        pantallaInterna.value = pantalla
    }

    fun volverALista() {
        irRaiz(Pantalla.Lista)
    }

    fun retroceder(): Boolean {
        val anterior = pilaNavegacion.removeLastOrNull()
        val padre = padreDe(pantallaInterna.value)

        val destino = when {
            pantallaInterna.value is Pantalla.Editar || pantallaInterna.value is Pantalla.Escaner -> {
                anterior ?: padre
            }
            anterior is Pantalla.Ajustes && !anterior.seccionId.isNullOrBlank() -> {
                anterior
            }
            anterior is Pantalla.Lista -> {
                Pantalla.Lista
            }
            padre != null -> {
                padre
            }
            anterior != null -> {
                anterior
            }
            else -> null
        }

        if (destino != null) {
            navegandoAtrasInterno.value = true
            pantallaInterna.value = destino
            return true
        }
        return false
    }

    fun volverAtras() {
        if (!retroceder()) irRaiz(Pantalla.Lista)
    }
}
