/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.rodal.tarefasraul.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import es.rodal.tarefasraul.TarefasRaulApplication
import es.rodal.tarefasraul.ui.home.HomeViewModel
import es.rodal.tarefasraul.ui.tarefa.TarefaDetailsViewModel
import es.rodal.tarefasraul.ui.tarefa.TarefaEditViewModel
import es.rodal.tarefasraul.ui.tarefa.TarefaEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(tarefasRaulApplication().container.tarefasRepository)
        }

        // Initializer for TarefaEntryViewModel
        initializer {
            TarefaEntryViewModel(tarefasRaulApplication().container.tarefasRepository)
        }

        // Initializer for TarefaDetailsViewModel
        initializer {
            TarefaDetailsViewModel(
                tarefasRaulApplication().container.tarefasRepository,
                this.createSavedStateHandle()
            )
        }

        // Initializer for TarefaEditViewModel
        initializer {//creationExtras contiene el contexto de la app en ese momento
            TarefaEditViewModel(
                tarefasRaulApplication().container.tarefasRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TarefasRaulApplication].
 */
fun CreationExtras.tarefasRaulApplication(): TarefasRaulApplication = /////////-------------------------
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TarefasRaulApplication)
