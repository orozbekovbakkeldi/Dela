package com.example.dela.ui.sections

import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dela.R
import com.example.dela.ui.model.Task
import com.example.dela.ui.model.TaskStateHandler
import com.example.dela.ui.model.TaskWithCategory
import com.example.dela.ui.model.TasksListUIState
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.category.CategoryStateHandler
import com.example.dela.ui.model.category.CategoryUIState
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun TaskListScaffold(
    taskHandler: TaskStateHandler,
    categoryHandler: CategoryStateHandler
) {

    val coroutineScope = rememberCoroutineScope()
    val snackBarState = remember {
        SnackbarHostState()
    }
    val snackBarTitle = stringResource(R.string.task_completed_snackbar)
    val undo = stringResource(id = R.string.undo)
    val showSnackBar: (TaskWithCategory) -> Unit = { task ->
        val message = snackBarTitle.format(task.task.title)
        coroutineScope.launch {
            if (snackBarState.showSnackbar(message, undo) == SnackbarResult.ActionPerformed) {
                taskHandler.onCheckedChange(task)
            }
        }
    }

    BoxWithConstraints {
        Scaffold(topBar = {
            TaskFilter(categoryHandler = categoryHandler)
        }, snackbarHost = {
            SnackbarHost(hostState = snackBarState)
        }) { innerPadding ->
            Crossfade(
                targetState = taskHandler.state,
                modifier = Modifier.padding(innerPadding)
            ) { tasksState ->
                when (tasksState) {
                    is TasksListUIState.Loaded -> {

                    }
                    is TasksListUIState.Loading -> {
                        LoadingContent()
                    }
                    is TasksListUIState.Empty -> {

                    }
                    is TasksListUIState.Error -> {

                    }
                }
            }
        }
    }


}

@Composable
fun TasksLoaded(tasks: List<TaskWithCategory>) {
    BoxWithConstraints {
        val cellCount = if (this.maxHeight > maxWidth) 1 else 2
        LazyVerticalGrid(columns = GridCells.Fixed(cellCount), content = {
            items(items = tasks) {

            }
        })
    }
}

@Composable
fun TaskItem(task: TaskWithCategory, onTaskClick: (Long) -> Unit) {
    Card(
        elevation = 4.dp, modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(74.dp)
    ) {
        Row {
            ColorForCategory(colorInt = task.category?.color)
            RadioButton(
                selected = task.task.completed,
                modifier = Modifier.fillMaxHeight(),
                onClick = { onTaskClick(task.task.id) })
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.task.title,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                DateText(calendar = task.task.dueDate)
            }
        }
    }
}

@Composable
fun ColorForCategory(colorInt: Int?) {
    val color = colorInt?.let {
        Color(it)
    } ?: MaterialTheme.colors.surface
    Spacer(
        modifier = Modifier
            .width(18.dp)
            .fillMaxHeight()
            .background(color)
    )
}

@Composable
fun DateText(calendar: Calendar?) {
    if (calendar == null) {
        return
    }
    val textTime = DateUtils.getRelativeDateTimeString(
        LocalContext.current,
        calendar.time.time,
        DateUtils.DAY_IN_MILLIS,
        DateUtils.DAY_IN_MILLIS,
        0
    ).toString()
    Text(
        text = textTime,
        style = MaterialTheme.typography.body2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
fun TaskItemPreview() {
    val task1 = Task(title = "Buy milk", dueDate = Calendar.getInstance())
    val category1 = Category(title = "Books", color = android.graphics.Color.BLUE)
    val taskWithCategory = TaskWithCategory(task = task1, category = category1)

    TaskItem(
        task = taskWithCategory,
        onTaskClick = {}
    )
}


@Composable
fun TaskFilter(categoryHandler: CategoryStateHandler) {
    Categories(
        categoryState = categoryHandler.state,
        currentCategory = categoryHandler.currentCategory,
        onCategoryChange = categoryHandler.onCategoryChange,
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
fun Categories(
    categoryState: CategoryUIState,
    currentCategory: Long?,
    onCategoryChange: (Long?) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.height(56.dp), contentAlignment = Alignment.CenterStart) {
        when (categoryState) {
            is CategoryUIState.Empty -> {
                EmptyCategories()
            }
            is CategoryUIState.Loaded -> {
                CategoriesLoaded(
                    currentCategory = currentCategory,
                    categories = categoryState.categories,
                    onCategoryChanged = onCategoryChange
                )
            }
            is CategoryUIState.Loading -> {
                LoadingContent()
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), content = {})
}


@Composable
fun EmptyCategories() {
    Text(
        text = stringResource(id = R.string.empty_categories_list),
        color = MaterialTheme.colors.surface
    )
}

@Composable
fun CategoriesLoaded(
    currentCategory: Long?,
    categories: List<Category>,
    onCategoryChanged: (Long?) -> Unit
) {
    val currentItem = categories.find {
        it.id == currentCategory
    }
    var selectedState by remember {
        mutableStateOf(currentItem?.id)
    }
    LazyRow {
        items(items = categories, itemContent = { category ->
            CategoryChip(
                category = category,
                isSelected = selectedState == currentItem?.id,
                onSelectChanged = { id ->
                    selectedState = id
                    onCategoryChanged(id)
                })
        })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryChip(isSelected: Boolean, category: Category, onSelectChanged: (Long?) -> Unit) {
    FilterChip(
        selected = isSelected, onClick = {
            val id = if (isSelected) null else category.id
            onSelectChanged(id)
        }, modifier = Modifier.padding(8.dp), colors = ChipDefaults.filterChipColors(
            selectedContentColor = Color(category.color)
        ), content = {
            Text(text = category.title)
        }
    )
}