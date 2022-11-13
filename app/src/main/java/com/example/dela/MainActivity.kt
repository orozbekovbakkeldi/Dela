package com.example.dela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dela.ui.HomeSection
import com.example.dela.ui.sections.AddTaskLoader
import com.example.dela.ui.sections.TasksListLoader
import com.example.dela.ui.theme.DelaTheme
import kotlinx.coroutines.launch

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
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)

    ModalBottomSheetLayout(sheetState = state, sheetContent = {
        AddTaskLoader(closeSheet = {
            scope.launch {
                state.hide()
            }
        })
    }) {

        Scaffold(
            bottomBar = {
                BottomNavigation(homeSections = HomeSection.values(), navController = navController)
            },
            topBar = {
                TopAppBarDela(
                    (HomeSection.findTitleByRoute(currentRoute.value?.destination?.route))?.let {
                        getTopAppBarTitle(id = it)
                    } ?: ""
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = HomeSection.Tasks.route,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable(HomeSection.Tasks.route) {
                    TasksListLoader(addClick = {
                        scope.launch { state.show() }
                    })
                }
                composable(HomeSection.Category.route) {

                }
                composable(HomeSection.Search.route) {

                }

            }
        }
    }
}

@Composable
fun getTopAppBarTitle(id: Int) = stringResource(id = id)

@Composable
fun TopAppBarDela(title: String) {
    TopAppBar(title = {
        Text(text = title)
    })
}

@Composable
fun BottomNavigation(homeSections: Array<HomeSection>, navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        homeSections.forEach { screen ->
            BottomNavigationItem(
                screen = screen,
                navController = navController,
                currentDestination = currentDestination
            )
        }
    }
}


@Composable
fun RowScope.BottomNavigationItem(
    screen: HomeSection,
    navController: NavController,
    currentDestination: NavDestination?
) {
    BottomNavigationItem(selected =
    currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true, onClick = {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    },
        icon = {
            val title = stringResource(id = screen.title)
            Icon(imageVector = screen.image, contentDescription = title)
        })
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