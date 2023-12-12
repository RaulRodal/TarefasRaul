package es.rodal.tarefasraul.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.rodal.tarefasraul.ui.home.HomeDestination
import es.rodal.tarefasraul.ui.home.HomeScreen
import es.rodal.tarefasraul.ui.tarefa.TarefaDetailsDestination
import es.rodal.tarefasraul.ui.tarefa.TarefaDetailsScreen
import es.rodal.tarefasraul.ui.tarefa.TarefaEditDestination
import es.rodal.tarefasraul.ui.tarefa.TarefaEditScreen
import es.rodal.tarefasraul.ui.tarefa.TarefaEntryDestination
import es.rodal.tarefasraul.ui.tarefa.TarefaEntryScreen

@Composable
fun TarefasRaulNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navHostController.navigate(TarefaEntryDestination.route) },
                navigateToItemUpdate = { navHostController.navigate("${TarefaDetailsDestination.route}/${it}") }
            )
        }
        composable(route = TarefaEntryDestination.route) {
            TarefaEntryScreen(
                navigateBack = { navHostController.popBackStack() },
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
        composable(
            route = TarefaDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TarefaDetailsDestination.tarefaIdArg) {//argumentos que recibe de la ruta
                type = NavType.IntType
            })
        ) {
            TarefaDetailsScreen(
                navigateToEditTarefa = { navHostController.navigate("${TarefaEditDestination.route}/$it") },
                navigateBack = { navHostController.navigateUp() }
            )
        }
        composable(
            route = TarefaEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TarefaEditDestination.tarefaIdArg) {
                type = NavType.IntType
            })
        ) {
            TarefaEditScreen(
                navigateBack = { navHostController.popBackStack() },
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
    }
}