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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.rodal.tarefasraul.TarefasRaulTopAppBar
import es.rodal.tarefasraul.R
import es.rodal.tarefasraul.data.Tarefa
import es.rodal.tarefasraul.ui.AppViewModelProvider
import es.rodal.tarefasraul.ui.navigation.NavigationDestination
import es.rodal.tarefasraul.ui.theme.TarefasRaulTheme

object TarefaDetailsDestination : NavigationDestination {
    override val route = "tarefa_details"
    override val titleRes = R.string.tarefa_detail_title
    const val tarefaIdArg = "tarefaId"
    val routeWithArgs = "$route/{$tarefaIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarefaDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToEditTarefa: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: TarefaDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TarefasRaulTopAppBar(
                title = stringResource(TarefaDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditTarefa(uiState.tarefaDetails.id) }, //id para que rellene os datos da vista edit
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_tarefa_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        TarefaDetailsBody(
            tarefaDetailsUiState = uiState,
            onCompletedButton = viewModel::reverseComplete,
            onDelete = {
                viewModel.deleteTarefa()
                navigateBack()
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun TarefaDetailsBody(
    tarefaDetailsUiState: TarefaDetailsUiState,
    onCompletedButton: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        TarefaDetails(
            tarefa = tarefaDetailsUiState.tarefaDetails.toTarefa(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onCompletedButton,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                if (tarefaDetailsUiState.tarefaDetails.completed) stringResource(R.string.discomplete)
                else stringResource(id = R.string.complete)
            )
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun TarefaDetails(
    tarefa: Tarefa, modifier: Modifier = Modifier
) {

    val color by animateColorAsState(//color para texto de completed dependiendo de si esta completada o no
        targetValue = if (tarefa.completed) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error, label = "color"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = tarefa.name,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = modifier) {
                Text(
                    text = if (tarefa.completed) stringResource(R.string.completed) else stringResource(R.string.noCompleted),
                    color = color,
                    fontWeight = FontWeight.Bold

                )
            }
            Row {
                Text(
                    text = stringResource(id = R.string.description)+":",
                    fontSize = 12.sp
                )
            }
            Row {
                Text(text = tarefa.description)
            }

        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun TarefaDetailsScreenPreview() {
    TarefasRaulTheme {
        TarefaDetailsBody(
            TarefaDetailsUiState(
                outOfStock = true,
                tarefaDetails = TarefaDetails(1, "Pen", "boligrafo azul asdkajsf lksdlgj knañjdgs najksd hngajsdghnja sdhflasjdf")
            ),
            onCompletedButton = {},
            onDelete = {}
        )
    }
}
