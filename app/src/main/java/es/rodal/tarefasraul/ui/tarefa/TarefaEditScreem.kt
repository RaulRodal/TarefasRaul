package es.rodal.tarefasraul.ui.tarefa

import es.rodal.tarefasraul.R
import es.rodal.tarefasraul.ui.navigation.NavigationDestination

object TarefaEditDestination : NavigationDestination {
    override val route = "tarefa_edit"
    override val titleRes = R.string.edit_tarefa_title
    const val tarefaIdArg = "tarefaId"
    val routeWithArgs = "$route/{$tarefaIdArg}"
}
