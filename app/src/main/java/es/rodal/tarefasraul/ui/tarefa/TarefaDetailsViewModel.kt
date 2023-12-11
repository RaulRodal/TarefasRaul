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

package es.rodal.tarefasraul.ui.tarefa

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rodal.tarefasraul.data.TarefasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an tarefa from the [TarefasRepository]'s data source.
 */
class TarefaDetailsViewModel(
    private val tarefasRepository: TarefasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tarefaId: Int = checkNotNull(savedStateHandle[TarefaDetailsDestination.tarefaIdArg])//recupera el id del savedState que se le pasa desde AppViewModelProvider

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<TarefaDetailsUiState> =
        tarefasRepository.getTarefaStream(tarefaId)
            .filterNotNull()
            .map { TarefaDetailsUiState( tarefaDetails = it.toTarefaDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TarefaDetailsUiState()
            )


    fun deleteTarefa() {
        viewModelScope.launch {
            tarefasRepository.deleteTarefa(uiState.value.tarefaDetails.toTarefa())
            }
    }



}

/**
 * UI state for TarefaDetailsScreen
 */
data class TarefaDetailsUiState(
    val outOfStock: Boolean = true,
    val tarefaDetails: TarefaDetails = TarefaDetails()
)
