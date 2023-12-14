package es.rodal.tarefasraul.ui.tarefa

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.rodal.tarefasraul.R
import es.rodal.tarefasraul.TarefasRaulTopAppBar
import es.rodal.tarefasraul.ui.AppViewModelProvider
import es.rodal.tarefasraul.ui.navigation.NavigationDestination
import es.rodal.tarefasraul.ui.theme.TarefasRaulTheme
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale


object TarefaEntryDestination : NavigationDestination {
    override val route = "tarefa_entry" //Preguntar porqué las rutas no se pueden poner en el strings.xml
    override val titleRes = R.string.tarefa_entry_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarefaEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TarefaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TarefasRaulTopAppBar(
                title = stringResource(TarefaEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TarefaEntryBody(
            tarefaUiState = viewModel.tarefaUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTarefa()
                    navigateBack()
                }   //probar con viewModel::saveItem
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TarefaEntryBody(
    tarefaUiState: TarefaUiState,
    onItemValueChange: (TarefaDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TarefaInputForm(
            tarefaDetails = tarefaUiState.tarefaDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = tarefaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarefaInputForm(
    tarefaDetails: TarefaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TarefaDetails) -> Unit = {},
    enabled: Boolean = true
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = tarefaDetails.name,
            onValueChange = { onValueChange(tarefaDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.tarefa_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = tarefaDetails.description,
            onValueChange = { onValueChange(tarefaDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.tarefa_price_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    TarefasRaulTheme {
        TarefaEntryBody(tarefaUiState = TarefaUiState(
            TarefaDetails(
                name = "Item name", description = "10.00123123123123"
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}