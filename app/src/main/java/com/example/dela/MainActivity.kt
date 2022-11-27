package com.example.dela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dela.ui.HomeSection
import com.example.dela.ui.home.Home
import com.example.dela.ui.home.category.CategoryBottomSheet
import com.example.dela.ui.home.tasks.TaskDetails
import com.example.dela.ui.sections.AddTaskLoader
import com.example.dela.ui.theme.DelaTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DelaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}

object MainDestinations {
    const val home_route = "home"
    const val show_task_bottom_sheet = "task_bottom_sheet"
    const val show_category_bottom_sheet = "category_bottom_sheet"
    const val task_details = "task_details"
}

object DestinationArgs {
    const val CategoryId = "category_id"
    const val TaskId = "task_id"
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun NavGraph() {

    val bottomSheetNavigator = rememberBottomSheetNavigator()

    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {

        NavHost(
            navController = navController,
            startDestination = HomeSection.Tasks.route
        ) {

            composable(MainDestinations.home_route) {
                Home(navController = navController)
            }

            composable(
                "${MainDestinations.task_details}/{${DestinationArgs.TaskId}}", arguments =
                listOf(navArgument(DestinationArgs.TaskId) {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                val taskId = requireNotNull(backStackEntry.arguments).getLong(DestinationArgs.TaskId)
                TaskDetails(taskId = taskId, onUp = {
                    navController.navigateUp()
                })
            }

            bottomSheet(MainDestinations.show_task_bottom_sheet) {
                AddTaskLoader {
                    navController.navigateUp()
                }
            }

            bottomSheet(
                "${MainDestinations.show_category_bottom_sheet}/{${DestinationArgs.CategoryId}}",
                arguments = listOf(navArgument(DestinationArgs.CategoryId) {
                    type = NavType.LongType
                }),
            ) { backstackEntry ->
                val categoryId = backstackEntry.arguments?.getLong(DestinationArgs.CategoryId) ?: 0L
                CategoryBottomSheet(categoryId = categoryId) {
                    navController.navigateUp()
                }
            }

        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DelaTheme {
        Greeting("Android")
    }
}