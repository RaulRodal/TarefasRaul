package es.rodal.tarefasraul.ui.tarefa

import android.telecom.Call.Details
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import es.rodal.tarefasraul.data.Tarefa
import es.rodal.tarefasraul.data.TarefasRepository


/**
 * ViewModel para validar e insertar tarefas na base de datos Room
 */
class TarefaEntryViewModel(private val tarefasRepository: TarefasRepository) : ViewModel() {

    var tarefaUiState by mutableStateOf(TarefaUiState())
        private set

    /**
     * Función que valida que os datos non esten vacios
     */
    private fun validateInput(uiState: TarefaDetails = tarefaUiState.tarefaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank()
        }
    }

    /**
     * Actualiza tarefaUiState cos valores que se lle pasen nos argumentos e realiza
     * a validacion dos datos de entrada
     */
    fun updateUiState(tarefaDetails: TarefaDetails) {
        tarefaUiState =
            TarefaUiState(
                tarefaDetails = tarefaDetails, isEntryValid = validateInput(tarefaDetails)
            )
    }

    /**
     * Funcion de suspension que chama a insertTarefa() do tarefasRepository para gardar a Tarefa
     */
    suspend fun saveTarefa() {
        if (validateInput()) {
            tarefasRepository.insertTarefa(tarefaUiState.tarefaDetails.toTarefa())
        }
    }
}




data class TarefaUiState(
    val tarefaDetails: TarefaDetails = TarefaDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Obxeto para manexar os detalles que terá a tarea para que coincidan coa base de datos
 */
data class TarefaDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val completed: Boolean = false
)

/**
 * Funcion de extension para poder transformar un TarefaDetails nun obxeto Tarefa
 */
fun TarefaDetails.toTarefa(): Tarefa = Tarefa(
    id = id,
    name = name,
    description = description,
    completed = completed
)

/**
 * Funcion de extension para transformar unha Tarefa nun obxeto TarefaDetails
 */
fun Tarefa.toTarefaDetails(): TarefaDetails = TarefaDetails(
    id = id,
    name = name,
    description = description,
    completed = completed
)

/**
 * Funcion de extension para transformar unha Tarefa nun obxeto TarefaUiState
 */
fun Tarefa.toTarefaUiState(isEntryValid: Boolean = false): TarefaUiState = TarefaUiState(
    tarefaDetails = this.toTarefaDetails(),
    isEntryValid = isEntryValid
)



