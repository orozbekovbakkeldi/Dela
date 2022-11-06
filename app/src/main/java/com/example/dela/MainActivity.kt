package com.example.dela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.dela.ui.Screen
import com.example.dela.ui.sections.TaskListScaffold
import com.example.dela.ui.theme.DelaTheme

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

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val screens = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Category
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(screens = screens, navController = navController)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
            }
            composable(Screen.Category.route) {

            }
            composable(Screen.Search.route) {

            }
        }
    }


}

@Composable
fun BottomNavigation(screens: List<Screen>, navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(screen = screen, navController = navController, currentDestination = currentDestination)
        }
    }
}


@Composable
fun RowScope.BottomNavigationItem(
    screen: Screen,
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