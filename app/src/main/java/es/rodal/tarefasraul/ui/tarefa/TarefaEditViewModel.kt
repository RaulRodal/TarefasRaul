package es.rodal.tarefasraul.ui.tarefa

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.rodal.tarefasraul.data.TarefasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TarefaEditViewModel(
    private val tarefasRepository : TarefasRepository,
            savedStateHandle : SavedStateHandle
    ) : ViewModel(){

        private val tarefaId: Int = checkNotNull(savedStateHandle[TarefaEditDestination.tarefaIdArg])

        private var _tarefaUiState = MutableStateFlow(TarefaUiState())
        val tarefaUiState: StateFlow<TarefaUiState> = _tarefaUiState.asStateFlow()


    private fun getTarefa(tarefaId:Int){
        viewModelScope.launch {
            tarefasRepository.getTarefaStream(tarefaId)
                .filterNotNull()
                .map { it.toTarefaUiState(true) }
                .collect { item -> //transformar a mutable, se fose a stateFlow seria "state in" en vez de "collect"
                    _tarefaUiState.value = item
                }
        }
    }
    init {
        getTarefa(tarefaId)
    }
    private fun validateInput(uiState: TarefaDetails = _tarefaUiState.value.tarefaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank()
        }
    }


    fun updateUiState(tarefaDetails: TarefaDetails) {
        _tarefaUiState.update { currentState ->
            currentState.copy(
                tarefaDetails = tarefaDetails,
                isEntryValid = validateInput(tarefaDetails)
            )
        }
    }

    fun updateItem() {
        viewModelScope.launch {
            tarefasRepository.updateTarefa((_tarefaUiState.value.tarefaDetails.toTarefa()))
        }
    }


    }