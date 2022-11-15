package com.example.dela.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.dela.MainDestinations
import com.example.dela.ui.HomeSection
import com.example.dela.ui.home.category.CategoryListSection
import com.example.dela.ui.sections.TasksListLoader

@Composable
fun Home(navController: NavController) {

    val (homeSection, setSection) = rememberSaveable {
        mutableStateOf(HomeSection.Tasks)
    }
    Crossfade(targetState = homeSection, animationSpec = tween(700)) { section ->
        DelaHomeScaffold(currentHomeSection = section, navController = navController, setSection)
    }
}

@Composable
private fun DelaBottomNav(
    currentSection: HomeSection,
    onSectionSelect: (HomeSection) -> Unit,
    items: Array<HomeSection>
) {
    BottomAppBar {
        items.forEach { section ->
            val selected = section == currentSection
            val colorState = animateColorAsState(
                if (selected) {
                    MaterialTheme.colors.onPrimary
                } else {
                    MaterialTheme.colors.onSecondary
                }
            )
            DelaBottomIcon(
                section = section,
                tint = colorState.value,
                onSectionSelect = onSectionSelect,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun DelaBottomIcon(
    section: HomeSection,
    tint: Color,
    onSectionSelect: (HomeSection) -> Unit,
    modifier: Modifier
) {
    val title = stringResource(section.title)
    IconButton(
        onClick = { onSectionSelect(section) },
        modifier = modifier
    ) {
        Icon(imageVector = section.image, tint = tint, contentDescription = title)
    }
}


@Composable
fun DelaHomeScaffold(
    currentHomeSection: HomeSection,
    navController: NavController,
    setSection: (HomeSection) -> Unit
) {
    Scaffold(
        bottomBar = {
            DelaBottomNav(currentSection = currentHomeSection, onSectionSelect = {
                setSection(it)
            }, items = HomeSection.values())
        },
        topBar = {
            TopAppBar {

                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProvideTextStyle(value = MaterialTheme.typography.h6) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.high,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = MaterialTheme.colors.onPrimary,
                                text = stringResource(id = currentHomeSection.title)
                            )
                        }
                    }
                }
            }
        }, content = { paddingValues ->
            DelaHomeContent(
                homeSection = currentHomeSection,
                navController,
                Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
fun DelaHomeContent(
    homeSection: HomeSection,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    when (homeSection) {
        HomeSection.Tasks -> {

            TasksListLoader(modifier = modifier) {
                navController.navigate(MainDestinations.show_task_bottom_sheet)
            }
        }
        HomeSection.Search -> {

        }
        HomeSection.Category -> {
            CategoryListSection(modifier = modifier, onShowBottomSheet = {
                val id = it ?: 0L
                navController.navigate(
                    "${MainDestinations.show_category_bottom_sheet}/$id"
                )
            })
        }
    }
}
