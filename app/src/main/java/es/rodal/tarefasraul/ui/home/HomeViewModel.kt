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

package es.rodal.tarefasraul.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rodal.tarefasraul.data.Tarefa
import es.rodal.tarefasraul.data.TarefasRepository
import es.rodal.tarefasraul.ui.tarefa.toTarefa
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(private val tarefasRepository: TarefasRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

     val homeUiState: StateFlow<HomeUiState>  =
         tarefasRepository.getAllTarefasStream()//aqui temos flow<List<HomeUiState>>
             .map { HomeUiState(it) }//aqui temos flow<HomeUiState>
             .stateIn(
                 scope = viewModelScope,
                 started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), //cando usamos o viewModel e cando estamos suscritos(se non hay suscritor en 5 segundos cortase)
                 initialValue = HomeUiState()
             )//aqui temos StateFlow<HomeUiState> que é o que queriamos

    fun reverseCompleted(tarefa: Tarefa) {
        viewModelScope.launch {
            tarefasRepository.updateTarefa(tarefa.copy(completed = !tarefa.completed))
        }
    }

}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val tarefaList: List<Tarefa> = listOf())
