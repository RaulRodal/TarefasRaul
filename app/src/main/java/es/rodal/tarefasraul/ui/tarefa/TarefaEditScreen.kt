package es.rodal.tarefasraul.ui.tarefa

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import es.rodal.tarefasraul.R
import es.rodal.tarefasraul.TarefasRaulTopAppBar
import es.rodal.tarefasraul.ui.AppViewModelProvider
import es.rodal.tarefasraul.ui.navigation.NavigationDestination
import es.rodal.tarefasraul.ui.theme.TarefasRaulTheme

object TarefaEditDestination : NavigationDestination {
    override val route = "tarefa_edit"
    override val titleRes = R.string.edit_tarefa_title
    const val tarefaIdArg = "tarefaId"
    val routeWithArgs = "$route/{$tarefaIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarefaEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TarefaEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val tarefaUiState by viewModel.tarefaUiState.collectAsState() //Suscripcion

    Scaffold(
        topBar = {
            TarefasRaulTopAppBar(
                title = stringResource(id = TarefaEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        TarefaEntryBody(
            tarefaUiState = tarefaUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                viewModel.updateItem()
                navigateBack()
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TarefaEditScreenPreview() {
    TarefasRaulTheme {
        TarefaEditScreen(navigateBack = {}, onNavigateUp = {})
    }
}