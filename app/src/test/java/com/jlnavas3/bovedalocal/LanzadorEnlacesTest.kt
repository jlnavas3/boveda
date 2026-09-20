package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LanzadorEnlacesTest {

    @Test
    fun esNombrePaquete_identificaPaquetesValidos() {
        assertTrue(LanzadorEnlaces.esNombrePaquete("com.whatsapp"))
        assertTrue(LanzadorEnlaces.esNombrePaquete("org.telegram.messenger"))
        assertTrue(LanzadorEnlaces.esNombrePaquete("com.google.android.youtube"))
        assertTrue(LanzadorEnlaces.esNombrePaquete("es.bancor.app_movil"))

        assertFalse(LanzadorEnlaces.esNombrePaquete("https://google.com"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("google.com/path"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("user@email.com"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("solo_una_palabra"))
    }

    @Test
    fun extraerPaquete_extraeCorrectamente() {
        assertEquals("com.whatsapp", LanzadorEnlaces.extraerPaquete("androidapp://com.whatsapp"))
        assertEquals("org.telegram.messenger", LanzadorEnlaces.extraerPaquete("app://org.telegram.messenger/login"))
        assertEquals("com.spotify.music", LanzadorEnlaces.extraerPaquete("com.spotify.music"))

        assertNull(LanzadorEnlaces.extraerPaquete("https://github.com/login"))
        assertNull(LanzadorEnlaces.extraerPaquete("http://192.168.1.1:8080"))
    }

    @Test
    fun normalizarUrlWeb_agregaHttpsSiFalta() {
        assertEquals("https://github.com", LanzadorEnlaces.normalizarUrlWeb("github.com"))
        assertEquals("https://github.com", LanzadorEnlaces.normalizarUrlWeb("https://github.com"))
        assertEquals("http://router.local", LanzadorEnlaces.normalizarUrlWeb("http://router.local"))
    }

    @Test
    fun extraerPaquete_digitalAssetLinksConHash() {
        val firefox = "https://android//2gCe6pR_AO_Q2Vu8Iep-4AsiKNnUHQxu0FaDHO_qa178GByKybdT_BuE8_dYk99G5Uvx_gdONXAOO2EaXidpVQ==@org.mozilla.firefox/"
        val chrome = "https://android//7fmduHKTdHHrlMvldlEqAIlSfii1tl35bxj1OXN5Ve8c4lU6URVu4xtSHc3BVZxS6WWJnxMDhIfQN0N0K2NDJg==@com.android.chrome/"
        val painter = "https://android//APYNp2XvKMgx_aoPi_hC2rzKGGOj37fSuEMR2N8x3yj05NOhX7bC6ZBN-sd6myxaQA8vbf95F95dibAWespkKg==@com.brakefield.painter/"

        assertEquals("org.mozilla.firefox", LanzadorEnlaces.extraerPaquete(firefox))
        assertEquals("com.android.chrome", LanzadorEnlaces.extraerPaquete(chrome))
        assertEquals("com.brakefield.painter", LanzadorEnlaces.extraerPaquete(painter))
    }

    @Test
    fun esNombrePaquete_distingueDominiosWeb() {
        assertFalse(LanzadorEnlaces.esNombrePaquete("github.com"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("google.com"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("amazon.es"))
        assertFalse(LanzadorEnlaces.esNombrePaquete("api.github.com"))

        assertTrue(LanzadorEnlaces.esNombrePaquete("com.brakefield.painter"))
        assertTrue(LanzadorEnlaces.esNombrePaquete("org.mozilla.firefox"))
        assertTrue(LanzadorEnlaces.esNombrePaquete("com.android.chrome"))
    }

    @Test
    fun desglosarYReconstruir_conservaCertificadosDAL() {
        val original = "https://android//2gCe6pR_AO_Q2Vu8Iep-4AsiKNnUHQxu0FaDHO_qa178GByKybdT_BuE8_dYk99G5Uvx_gdONXAOO2EaXidpVQ==@org.mozilla.firefox/"
        val editable = LanzadorEnlaces.desglosarParaEdicion(original)

        assertEquals("org.mozilla.firefox", editable.valor)
        assertEquals("2gCe6pR_AO_Q2Vu8Iep-4AsiKNnUHQxu0FaDHO_qa178GByKybdT_BuE8_dYk99G5Uvx_gdONXAOO2EaXidpVQ==", editable.hashOriginal)

        // Al guardar sin cambiar el paquete, se reconstruye idéntico al original
        val reconstruido = LanzadorEnlaces.reconstruirDesdeEdicion(editable)
        assertEquals(original, reconstruido)

        // Si el usuario cambia el paquete a otro válido, conserva el hash
        val editado = editable.copy(valor = "org.mozilla.fenix")
        val reconstruidoEditado = LanzadorEnlaces.reconstruirDesdeEdicion(editado)
        assertEquals("https://android//2gCe6pR_AO_Q2Vu8Iep-4AsiKNnUHQxu0FaDHO_qa178GByKybdT_BuE8_dYk99G5Uvx_gdONXAOO2EaXidpVQ==@org.mozilla.fenix/", reconstruidoEditado)
    }

    @Test
    fun desglosarYReconstruir_urlsWebNormales() {
        val web = "https://github.com/login"
        val editable = LanzadorEnlaces.desglosarParaEdicion(web)

        assertEquals("https://github.com/login", editable.valor)
        assertNull(editable.hashOriginal)

        val reconstruido = LanzadorEnlaces.reconstruirDesdeEdicion(editable)
        assertEquals("https://github.com/login", reconstruido)
    }
}
