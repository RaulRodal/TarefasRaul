package es.rodal.tarefasraul.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.rodal.tarefasraul.R
import es.rodal.tarefasraul.TarefasRaulTopAppBar
import es.rodal.tarefasraul.data.Tarefa
import es.rodal.tarefasraul.ui.AppViewModelProvider
import es.rodal.tarefasraul.ui.navigation.NavigationDestination
import es.rodal.tarefasraul.ui.tarefa.DeleteConfirmationDialog
import es.rodal.tarefasraul.ui.theme.TarefasRaulTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        {
            TarefasRaulTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.tarefa_entry_title)
                )
            }
        }) { innerPadding ->
        HomeBody(
            tarefaList = homeUiState.tarefaList, //Pestaña App Insprection abajo para añadir valores a la BBDD
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    tarefaList: List<Tarefa>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (tarefaList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            TarefasList(
                tarefaList = tarefaList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun TarefasList(
    tarefaList: List<Tarefa>,
    onItemClick: (Tarefa) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }


    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        // sortedBy para que aparezan primeiro as que non estan completed
        items(items = tarefaList.sortedBy { it.completed }, key = { it.id }) { tarefa ->

            val dismissState = rememberDismissState(
                initialValue = DismissValue.Default,
                positionalThreshold = { swipeActivationFloat -> swipeActivationFloat / 3 }
            )
            // icono para mostrar en el background del dismiss to end
            val iconCheck = if (tarefa.completed) Icons.Default.Close else Icons.Default.Check

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                background = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error
                            DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.secondaryContainer
                            else -> Color.Transparent
                        }, label = "color background dismiss"
                    )
                    val colorIcon by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.DismissedToStart -> MaterialTheme.colorScheme.onError
                            DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> Color.Transparent
                        }, label = "color Icon dismiss"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clip(MaterialTheme.shapes.extraLarge),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            IconButton(
                                modifier = Modifier.weight(3f),
                                onClick = { scope.launch { dismissState.reset() } }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = colorIcon
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (dismissState.targetValue == DismissValue.DismissedToStart)
                                IconButton(
                                    modifier = Modifier.weight(3f),
                                    onClick = {
                                        deleteConfirmationRequired = true
                                        scope.launch { dismissState.reset() }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = colorIcon
                                    )
                                }
                            if (dismissState.targetValue == DismissValue.DismissedToEnd)
                                IconButton(
                                    modifier = Modifier.weight(3f),
                                    onClick = {
                                        viewModel.reverseCompleted(tarefa)
                                        scope.launch { dismissState.reset() }
                                    }
                                ) {
                                    Icon(
                                        imageVector = iconCheck,
                                        contentDescription = "Complete"
                                    )
                                }

                        }
                    }
                },
                dismissContent = {
                    TarefaItem(tarefa = tarefa,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(tarefa) })

                })
            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        viewModel.deleteTarefa(tarefa)
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }

    }
}


////


@Composable
private fun TarefaItem(
    tarefa: Tarefa,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val color by animateColorAsState(//color cambiante dependiendo de expanded
        targetValue = if (tarefa.completed) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.errorContainer, label = "color"
    )
    val borderColor by animateColorAsState(//color cambiante dependiendo de expanded
        targetValue = if (tarefa.completed) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onErrorContainer, label = "color"
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = tarefa.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.weight(4f)
                )
                Checkbox(
                    checked = tarefa.completed,
                    onCheckedChange = { viewModel.reverseCompleted(tarefa) }
                )

            }
            Text(
                text = tarefa.description,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HomePreview() {
    TarefasRaulTheme {
        TarefaItem(
            tarefa =
            Tarefa(1, "Game", "100.0")
        )
    }
}