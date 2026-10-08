package com.jlnavas3.bovedalocal.ui

import kotlinx.coroutines.flow.MutableStateFlow

interface VaultNavegacionDelegate {
    val pantallaInterna: MutableStateFlow<Pantalla>
    val navegandoAtrasInterno: MutableStateFlow<Boolean>
    val pilaNavegacion: ArrayDeque<Pantalla>
    var ultimoWidgetAjustesSeleccionado: Int
    var ultimoScrollAjustes: Int
    var navegoDesdeMenuLateral: Boolean
    val abrirMenuLateralAlVolverALista: MutableStateFlow<Boolean>

    fun padreDe(pantalla: Pantalla): Pantalla? = MapeoJerarquiaPantallas.resolverPadre(
        pantalla = pantalla,
        alRetrocederWidgetTotp = { ultimoWidgetAjustesSeleccionado = 0 },
        alRetrocederWidget1x1 = { ultimoWidgetAjustesSeleccionado = 1 }
    )


    fun ir(pantalla: Pantalla) {
        navegandoAtrasInterno.value = false
        if (pantallaInterna.value is Pantalla.Lista) {
            navegoDesdeMenuLateral = false
            abrirMenuLateralAlVolverALista.value = false
            if (pantalla is Pantalla.Ajustes) {
                ultimoScrollAjustes = 0
            }
        }
        if (pantalla != pantallaInterna.value) {
            val origen = when {
                pantallaInterna.value is Pantalla.Ajustes -> {
                    val idHijo = when (pantalla) {
                        is Pantalla.Generador -> "04-HER-GEN"
                        is Pantalla.AjustesAutocompletado -> "04-HER-PSK"
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
                pantallaInterna.value is Pantalla.Lista -> Pantalla.Lista
                else -> {
                    val padre = padreDe(pantalla)
                    if (padre != null && padre::class == pantallaInterna.value::class) {
                        padre
                    } else {
                        pantallaInterna.value
                    }
                }
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
        navegoDesdeMenuLateral = true
        abrirMenuLateralAlVolverALista.value = false
        if (pantalla is Pantalla.Ajustes) {
            ultimoScrollAjustes = 0
        }
        pantallaInterna.value = pantalla
    }

    fun irPorId(id: String) {
        val limpio = id.trim()
        val destino = when {
            limpio.startsWith("01-SEG-BIO-BLO") -> Pantalla.BloqueoBiometria(limpio)
            limpio.startsWith("01-SEG-BIO-VIS") -> Pantalla.SeguridadVisual(limpio)
            limpio.startsWith("01-SEG-BIO-CLP") -> Pantalla.Portapapeles(limpio)
            limpio.startsWith("01-SEG-BIO-AUD") -> Pantalla.AuditoriaSeguridad(limpio)
            limpio.startsWith("01-SEG-BIO") || limpio == "01" || limpio.startsWith("01.1") || limpio.startsWith("01.0") -> Pantalla.Seguridad(limpio)
            limpio.startsWith("01-SEG-SEN") || limpio.startsWith("01.3") -> Pantalla.AjustesSenuelo(limpio)
            limpio.startsWith("01-SEG-DES-INT") -> Pantalla.AutodestruccionIntentos(limpio)
            limpio.startsWith("01-SEG-DES") || limpio.startsWith("01.4") -> Pantalla.AjustesAutodestruccion(limpio)
            limpio.startsWith("01-SEG-CRY") || limpio.startsWith("01.5") -> Pantalla.Argon2id(limpio)

            limpio.startsWith("02-APA-THM-PAL") -> Pantalla.TemaPaletas(limpio)
            limpio.startsWith("02-APA-THM-ANI") -> Pantalla.AnimacionDesbloqueo(limpio)
            limpio.startsWith("02-APA-THM-CAL") || limpio.startsWith("03.2.G2") -> Pantalla.CalibracionAnimacion(limpio)
            limpio.startsWith("02-APA-THM-DAT") || limpio.startsWith("02-APA-THM-G04") || limpio.startsWith("03.2.1") -> Pantalla.ColoresDatos(limpio)
            limpio.startsWith("02-APA-THM") || limpio.startsWith("02.1") || limpio.startsWith("09.2") -> Pantalla.Tema(limpio)
            limpio.startsWith("02-APA-MNL") -> Pantalla.PersonalizarMenuLateral(limpio)

            limpio.startsWith("02-APA-GEO-PRE") -> Pantalla.FormasPresets(limpio)
            limpio.startsWith("02-APA-GEO-CRV") -> Pantalla.FormasCurvatura(limpio)
            limpio.startsWith("02-APA-GEO-BOR") || limpio.startsWith("02-APA-GEO-GRO") || limpio.startsWith("02-APA-GEO-EST") -> Pantalla.FormasBorde(limpio)
            limpio.startsWith("02-APA-GEO-ESP") -> Pantalla.FormasEspaciado(limpio)
            limpio.startsWith("02-APA-GEO") || limpio.startsWith("02.2") -> Pantalla.Formas(limpio)

            limpio.startsWith("02-APA-TYP-PRE") -> Pantalla.TipografiaPresets(limpio)
            limpio.startsWith("02-APA-TYP-ESC") -> Pantalla.TipografiaEscala(limpio)
            limpio.startsWith("02-APA-TYP-FAM") -> Pantalla.TipografiaFamilia(limpio)
            limpio.startsWith("02-APA-TYP-PES") -> Pantalla.TipografiaPeso(limpio)
            limpio.startsWith("02-APA-TYP-ESP") -> Pantalla.TipografiaEspaciado(limpio)
            limpio.startsWith("02-APA-TYP") || limpio.startsWith("02.3") -> Pantalla.Tipografia(limpio)

            limpio.startsWith("03-LST-DES-EST") -> Pantalla.OrganizacionEstructura(limpio)
            limpio.startsWith("03-LST-DES-JER") -> Pantalla.OrganizacionJerarquia(limpio)
            limpio.startsWith("03-LST-DES-IND") -> Pantalla.OrganizacionIndicadores(limpio)
            limpio.startsWith("03-LST-DES") || limpio.startsWith("03.1") || limpio.startsWith("09.6") -> Pantalla.OrganizacionLista(limpio)
            limpio.startsWith("03-LST-AZX-OLA") -> Pantalla.IndiceOla(limpio)
            limpio.startsWith("03-LST-AZX-CRE") -> Pantalla.IndiceCresta(limpio)
            limpio.startsWith("03-LST-AZX-HAP") -> Pantalla.IndiceHaptica(limpio)
            limpio.startsWith("03-LST-AZX-RES") -> Pantalla.IndiceResaltado(limpio)
            limpio.startsWith("03-LST-AZX") || limpio.startsWith("03.2") || limpio.startsWith("09.5") -> Pantalla.AjustesIndice(limpio)
            limpio.startsWith("03-LST-FMT") || limpio.startsWith("03.3") || limpio.startsWith("10") -> Pantalla.FormatosCampos(limpio)
            limpio.startsWith("03-LST-PLT") -> Pantalla.PlantillasCampos(limpio)

            limpio.startsWith("04-HER-AUT") || limpio.startsWith("04.1") || limpio == "07" || limpio.startsWith("07.0") -> Pantalla.AjustesAutenticador(limpio)
            limpio.startsWith("04-HER-HST-GEN") -> Pantalla.HistorialClavesGeneradas(limpio)
            limpio.startsWith("04-HER-HST-ENT") -> Pantalla.HistorialCredenciales(limpio)
            limpio.startsWith("04-HER-HST-CFG") -> Pantalla.AjustesHistorial(limpio)
            limpio.startsWith("04-HER-HST") || limpio.startsWith("04.2") || limpio == "04" || limpio.startsWith("04.0") -> Pantalla.HistorialClaves(limpio)
            limpio.startsWith("04-HER-CAM") || limpio.startsWith("04.3") || limpio == "05" || limpio.startsWith("05.0") -> Pantalla.AjustesCamara(limpio)
            limpio.startsWith("04-HER-WGT-TOT") -> Pantalla.AjustesWidgetTotpSub(limpio)
            limpio.startsWith("04-HER-WGT-1X1") -> Pantalla.AjustesWidget1x1Sub(limpio)
            limpio.startsWith("04-HER-WGT") || limpio.startsWith("04.4") || limpio.startsWith("09.4") -> Pantalla.AjustesWidget(limpio)
            limpio.startsWith("04-HER-MSK-CFG") -> Pantalla.TileConfiguracion(limpio)
            limpio.startsWith("04-HER-MSK") || limpio.startsWith("04.5") -> Pantalla.TileRapido(limpio)
            limpio.startsWith("04-HER-PSK") || limpio.startsWith("04.6") -> Pantalla.AjustesAutocompletado(limpio)

            limpio.startsWith("05-COP-MAN-FIL") -> Pantalla.CopiaManual(limpio)
            limpio.startsWith("05-COP-AUT-FIL") || limpio.startsWith("05-COP-ATM") || limpio.startsWith("05.1.4") -> Pantalla.CopiaAutomaticaLocalSub(limpio)
            limpio.startsWith("05-COP-REC-FIL") -> Pantalla.CopiaRecordatorios(limpio)
            limpio.startsWith("05-COP-EXP") || limpio.startsWith("05.1.2") -> Pantalla.ExportarSelectivo(limpio)
            limpio.startsWith("05-COP-MAN") || limpio.startsWith("05.1") || limpio == "06" || limpio.startsWith("06.0") -> Pantalla.CopiaSeguridad(limpio)
            limpio.startsWith("05-COP-CSV") || limpio.startsWith("05.2") || limpio == "08" || limpio.startsWith("08.0") -> Pantalla.CsvGoogle(limpio)
            limpio.startsWith("05-COP-KIT") || limpio.startsWith("05.3") -> Pantalla.KitEmergencia(limpio)

            limpio.startsWith("06-SIS-AVZ-DES") -> Pantalla.AvanzadaDesarrollo(limpio)
            limpio.startsWith("06-SIS-AVZ-LGT") -> Pantalla.AvanzadaAlumbrado(limpio)
            limpio.startsWith("06-SIS-AVZ-ORG") -> Pantalla.ReorganizarAjustes
            limpio.startsWith("06-SIS-AVZ-HAP") -> Pantalla.AvanzadaHaptica(limpio)
            limpio.startsWith("06-SIS-AVZ-PEL") -> Pantalla.AvanzadaZonaPeligro(limpio)
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
        if (pantalla is Pantalla.Lista) {
            ultimoScrollAjustes = 0
            if (navegoDesdeMenuLateral) {
                navegoDesdeMenuLateral = false
                abrirMenuLateralAlVolverALista.value = true
            }
        }
        navegandoAtrasInterno.value = true
        pilaNavegacion.clear()
        pantallaInterna.value = pantalla
    }

    fun volverALista() {
        ultimoScrollAjustes = 0
        if (navegoDesdeMenuLateral) {
            navegoDesdeMenuLateral = false
            abrirMenuLateralAlVolverALista.value = true
        }
        irRaiz(Pantalla.Lista)
    }

    fun retroceder(): Boolean {
        val anterior = pilaNavegacion.removeLastOrNull()
        val padre = padreDe(pantallaInterna.value)

        val destino = when {
            pantallaInterna.value is Pantalla.Editar || pantallaInterna.value is Pantalla.Escaner || pantallaInterna.value is Pantalla.CamaraQr -> {
                anterior ?: padre
            }
            anterior != null && anterior != pantallaInterna.value -> {
                anterior
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
            if (destino is Pantalla.Lista) {
                ultimoScrollAjustes = 0
                if (navegoDesdeMenuLateral) {
                    navegoDesdeMenuLateral = false
                    abrirMenuLateralAlVolverALista.value = true
                }
            }
            navegandoAtrasInterno.value = true
            pantallaInterna.value = destino
            return true
        }
        return false
    }

    fun volverAtras() {
        if (!retroceder()) {
            if (navegoDesdeMenuLateral) {
                navegoDesdeMenuLateral = false
                abrirMenuLateralAlVolverALista.value = true
            }
            ultimoScrollAjustes = 0
            irRaiz(Pantalla.Lista)
        }
    }
}
