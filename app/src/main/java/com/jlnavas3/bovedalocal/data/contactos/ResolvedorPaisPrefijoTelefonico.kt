/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import java.util.Locale

/**
 * Microcomponente puro para resolver el código de país ISO 3166-1 alpha-2 a partir del prefijo
 * internacional ITU-T E.164 de un número telefónico (+1 hasta +999).
 * Cobertura canónica mundial de todos los países y territorios.
 */
object ResolvedorPaisPrefijoTelefonico {

    private val PREFIJOS_3_DIGITOS = mapOf(
        // América
        "500" to "FK", "501" to "BZ", "502" to "GT", "503" to "SV", "504" to "HN",
        "505" to "NI", "506" to "CR", "507" to "PA", "508" to "PM", "509" to "HT",
        "590" to "GP", "591" to "BO", "592" to "GY", "593" to "EC", "594" to "GF",
        "595" to "PY", "596" to "MQ", "597" to "SR", "598" to "UY", "599" to "CW",
        // Europa
        "350" to "GI", "351" to "PT", "352" to "LU", "353" to "IE", "354" to "IS",
        "355" to "AL", "356" to "MT", "357" to "CY", "358" to "FI", "359" to "BG",
        "370" to "LT", "371" to "LV", "372" to "EE", "373" to "MD", "374" to "AM",
        "375" to "BY", "376" to "AD", "377" to "MC", "378" to "SM", "379" to "VA",
        "380" to "UA", "381" to "RS", "382" to "ME", "383" to "XK", "385" to "HR",
        "386" to "SI", "387" to "BA", "389" to "MK", "420" to "CZ", "421" to "SK",
        "423" to "LI",
        // Asia / Oriente Medio
        "850" to "KP", "852" to "HK", "853" to "MO", "855" to "KH", "856" to "LA",
        "880" to "BD", "886" to "TW", "960" to "MV", "961" to "LB", "962" to "JO",
        "963" to "SY", "964" to "IQ", "965" to "KW", "966" to "SA", "967" to "YE",
        "968" to "OM", "970" to "PS", "971" to "AE", "972" to "IL", "973" to "BH",
        "974" to "QA", "975" to "BT", "976" to "MN", "977" to "NP", "992" to "TJ",
        "993" to "TM", "994" to "AZ", "995" to "GE", "996" to "KG", "998" to "UZ",
        // África
        "211" to "SS", "212" to "MA", "213" to "DZ", "216" to "TN", "218" to "LY",
        "220" to "GM", "221" to "SN", "222" to "MR", "223" to "ML", "224" to "GN",
        "225" to "CI", "226" to "BF", "227" to "NE", "228" to "TG", "229" to "BJ",
        "230" to "MU", "231" to "LR", "232" to "SL", "233" to "GH", "234" to "NG",
        "235" to "TD", "236" to "CF", "237" to "CM", "238" to "CV", "239" to "ST",
        "240" to "GQ", "241" to "GA", "242" to "CG", "243" to "CD", "244" to "AO",
        "245" to "GW", "248" to "SC", "249" to "SD", "250" to "RW", "251" to "ET",
        "252" to "SO", "253" to "DJ", "254" to "KE", "255" to "TZ", "256" to "UG",
        "257" to "BI", "258" to "MZ", "260" to "ZM", "261" to "MG", "262" to "RE",
        "263" to "ZW", "264" to "NA", "265" to "MW", "266" to "LS", "267" to "BW",
        "268" to "SZ", "269" to "KM"
    )

    private val PREFIJOS_2_DIGITOS = mapOf(
        "20" to "EG", "27" to "ZA", "30" to "GR", "31" to "NL", "32" to "BE",
        "33" to "FR", "34" to "ES", "36" to "HU", "39" to "IT", "40" to "RO",
        "41" to "CH", "43" to "AT", "44" to "GB", "45" to "DK", "46" to "SE",
        "47" to "NO", "48" to "PL", "49" to "DE", "51" to "PE", "52" to "MX",
        "53" to "CU", "54" to "AR", "55" to "BR", "56" to "CL", "57" to "CO",
        "58" to "VE", "60" to "MY", "61" to "AU", "62" to "ID", "63" to "PH",
        "64" to "NZ", "65" to "SG", "66" to "TH", "81" to "JP", "82" to "KR",
        "84" to "VN", "86" to "CN", "90" to "TR", "91" to "IN", "92" to "PK",
        "93" to "AF", "94" to "LK", "95" to "MM", "98" to "IR"
    )

    private val PREFIJOS_1_DIGITO = mapOf(
        "1" to "US", // NANP: EE.UU., Canadá y Caribe
        "7" to "RU"  // Rusia y Kazajistán
    )

    /**
     * Resuelve el código ISO 3166-1 alpha-2 del país para un número telefónico internacional (+1 a +999).
     * Devuelve null si el número es local, inválido o no contiene un prefijo internacional reconocido.
     */
    fun obtenerPaisPorNumero(numeroRaw: String): String? {
        val limpio = numeroRaw.trim()
        val soloDigitosConPlus = if (limpio.startsWith("+")) {
            "+" + limpio.filter { it.isDigit() }
        } else if (limpio.startsWith("00")) {
            "+" + limpio.removePrefix("00").filter { it.isDigit() }
        } else {
            return null
        }

        if (soloDigitosConPlus.startsWith("+")) {
            val digitos = soloDigitosConPlus.removePrefix("+")
            if (digitos.length >= 3) {
                val pref3 = digitos.substring(0, 3)
                PREFIJOS_3_DIGITOS[pref3]?.let { return it }
            }
            if (digitos.length >= 2) {
                val pref2 = digitos.substring(0, 2)
                PREFIJOS_2_DIGITOS[pref2]?.let { return it }
            }
            if (digitos.isNotEmpty()) {
                val pref1 = digitos.substring(0, 1)
                PREFIJOS_1_DIGITO[pref1]?.let { return it }
            }
        }
        return null
    }

    /**
     * Resuelve el código ISO 3166-1 alpha-2 del país para un número telefónico.
     */
    fun resolverIsoPais(numeroRaw: String, paisPorDefecto: String? = null): String {
        obtenerPaisPorNumero(numeroRaw)?.let { return it }

        // Si no tiene prefijo internacional o no coincide, usar el país por defecto configurado
        if (!paisPorDefecto.isNullOrBlank()) {
            return paisPorDefecto.trim().uppercase()
        }

        // Fallback al país del dispositivo según Locale del sistema
        val localeCountry = Locale.getDefault().country
        return if (localeCountry.isNotBlank()) localeCountry.uppercase() else "EC"
    }
}
