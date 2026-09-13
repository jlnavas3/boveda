package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.Diagnostico
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class RegistroCrudTest {

    @get:Rule
    val carpeta = TemporaryFolder()

    private fun preparar(): File {
        val archivo = File(carpeta.root, "diagnostico_crud.log")
        Diagnostico.iniciar(archivo)
        return archivo
    }

    @After
    fun limpiar() {
        Diagnostico.borrar()
        Diagnostico.esperarEscrituras()
    }

    @Test
    fun `eventos CRUD registran adecuadamente todos los tipos antiguos y nuevos`() {
        preparar()

        val tipos = TipoEntrada.entries

        tipos.forEach { tipo ->
            val tipoDesc = tipo.etiqueta.lowercase()

            // 1. Create (C)
            Diagnostico.apuntar("bóveda", "Nueva entrada creada ($tipoDesc)")

            // 2. Read (R)
            Diagnostico.apuntar("bóveda", "Detalle de entrada consultado ($tipoDesc)")
            Diagnostico.apuntar("seguridad", "Contraseña revelada en pantalla ($tipoDesc)")

            // 3. Update (U)
            Diagnostico.apuntar("bóveda", "Entrada modificada ($tipoDesc)")

            // 4. Delete (D)
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) enviada a la papelera")
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) restaurada desde la papelera a la bóveda")
            Diagnostico.apuntar("papelera", "Entrada ($tipoDesc) eliminada definitivamente de la papelera")
        }

        Diagnostico.esperarEscrituras()
        val lineas = Diagnostico.ultimas(200)

        // Verificación de tipos tradicionales
        assertTrue(lineas.any { it.contains("Nueva entrada creada (contraseña)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (nota segura)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (passkey)") })
        assertTrue(lineas.any { it.contains("Entrada (contraseña) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (nota segura) enviada a la papelera") })

        // Verificación de nuevos tipos
        assertTrue(lineas.any { it.contains("Nueva entrada creada (tarjeta bancaria)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (red wi-fi)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (cuenta bancaria)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (documento de identidad)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (servidor / ssh)") })
        assertTrue(lineas.any { it.contains("Nueva entrada creada (cripto wallet)") })

        assertTrue(lineas.any { it.contains("Entrada (tarjeta bancaria) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (red wi-fi) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (cuenta bancaria) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (documento de identidad) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (servidor / ssh) enviada a la papelera") })
        assertTrue(lineas.any { it.contains("Entrada (cripto wallet) enviada a la papelera") })
    }
}
