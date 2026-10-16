/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.WindowManager
import android.view.autofill.AutofillManager
import com.jlnavas3.bovedalocal.data.VaultRepository

/**
 * Actividad trampolín transparente que intercepta la selección de una sugerencia de autocompletado
 * para copiar el código TOTP / 2FA al portapapeles y devolver inmediatamente el dataset a Android.
 */
class AutofillTrampolineActivity : Activity() {

    companion object {
        const val EXTRA_ENTRADA_ID = "boveda.trampoline.entrada_id"
        const val EXTRA_DATASET = "boveda.trampoline.dataset"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        val entradaId = intent.getStringExtra(EXTRA_ENTRADA_ID)
        if (!entradaId.isNullOrBlank()) {
            val repo = VaultRepository.obtener(this)
            if (repo.estaDesbloqueada) {
                val entrada = repo.entradas().firstOrNull { it.id == entradaId }
                CopiadorTotpAutofill.copiarSiAplica(this, entrada, repo.ajustes.actual)
            }
        }

        val dataset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATASET, Dataset::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATASET)
        }

        if (dataset != null) {
            val resultado = Intent().apply {
                putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, dataset)
            }
            setResult(Activity.RESULT_OK, resultado)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }
}
