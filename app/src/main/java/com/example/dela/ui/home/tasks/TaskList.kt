package com.example.dela.ui.sections

import android.text.format.DateUtils
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dela.R
import com.example.dela.ui.home.category.CategoryViewModel
import com.example.dela.ui.home.tasks.AddTaskViewModel
import com.example.dela.ui.home.tasks.TasksListViewModel
import com.example.dela.ui.model.task.Task
import com.example.dela.ui.model.task.TaskStateHandler
import com.example.dela.ui.model.task.TaskWithCategory
import com.example.dela.ui.model.task.TasksListUIState
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.category.CategoryStateHandler
import com.example.dela.ui.model.category.CategoryUIState
import com.example.dela.ui.theme.DelaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.*


@Composable
fun TaskListScaffold(
    categoryStateHandler: CategoryStateHandler,
    tasksStateHandler: TaskStateHandler,
    modifier: Modifier
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
            when (snackBarState.showSnackbar(message, undo)) {
                SnackbarResult.ActionPerformed -> tasksStateHandler.onCheckedChange(task)
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    BoxWithConstraints {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TaskFilter(categoryHandler = categoryStateHandler)
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarState)
            },
            floatingActionButton = {
                AddFloatingActionButton {
                    tasksStateHandler.onAddClick.invoke()
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            Crossfade(
                targetState = tasksStateHandler.state, modifier = Modifier.padding(innerPadding)
            ) { tasksState ->
                when (tasksState) {
                    is TasksListUIState.Loaded -> {
                        TasksLoaded(
                            tasks = tasksState.items,
                            onItemClicked = tasksStateHandler.onItemClick,
                            onCheckedChanged = { taskWithCategory ->
                                tasksStateHandler.onCheckedChange(taskWithCategory)
                                showSnackBar(taskWithCategory)
                            }
                        )
                    }
                    is TasksListUIState.Loading -> {
                        LoadingContent()
                    }
                    is TasksListUIState.Empty -> {
                        NoTask()
                    }
                    is TasksListUIState.Error -> {
                        TasksLoadingError(message = tasksState.error.localizedMessage!!)
                    }
                }
            }
        }
    }
}

@Composable
fun TasksLoadingError(message: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(Icons.Default.Close, null)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = message)
    }
}

@Preview
@Composable
fun NoTask() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Icon(
            painterResource(id = R.drawable.tasks_list),
            null,
            Modifier.size(70.dp),
            tint = MaterialTheme.colors.secondary
        )
        Text(
            text = stringResource(id = R.string.no_new_tasks),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.no_new_tasks_description),
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp
        )

    }
}

@Composable
fun TasksLoaded(
    tasks: List<TaskWithCategory>,
    onCheckedChanged: (TaskWithCategory) -> Unit,
    onItemClicked: (Long) -> Unit
) {
    BoxWithConstraints {
        LazyColumn(content = {
            items(items = tasks, { taskWithCategory ->
                taskWithCategory.task.id
            }) { task ->
                DismissibleTaskItem(task, onItemClicked, onCheckedChanged)
            }
        })
    }
}

@Composable
fun AddFloatingActionButton(addTask: () -> Unit) {
    FloatingActionButton(
        onClick = {
            addTask()
        }
    ) {
        Icon(Icons.Outlined.Add, null)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskLoader(
    addTaskViewModel: AddTaskViewModel = getViewModel(),
    categoryViewModel: CategoryViewModel = getViewModel(),
    closeSheet: () -> Unit
) {

    var text by rememberSaveable {
        mutableStateOf("")
    }

    val categoriesState by remember {
        categoryViewModel
    }.getCategories().collectAsState(initial = CategoryUIState.Empty)

    var currentCategory by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300L)
        focusRequester.requestFocus()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .background(MaterialTheme.colors.background)
            .padding(16.dp), verticalArrangement = Arrangement.SpaceAround
    ) {

        OutlinedTextField(
            value = text,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onValueChange = {
                text = it
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            label = {
                Text(text = stringResource(id = R.string.task_title))
            })

        Categories(
            categoryState = categoriesState,
            currentCategory = currentCategory,
            onCategoryChange = { categoryId ->
                currentCategory = categoryId
            }
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp), onClick = {
            addTaskViewModel.addTask(text, currentCategory)
            closeSheet()
        }) {
            Text(text = stringResource(id = R.string.add))
        }
    }
}


@Composable
fun TaskItem(
    task: TaskWithCategory, onTaskClick: (Long) -> Unit
) {
    Card(elevation = 4.dp,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(74.dp)
            .clickable {
                onTaskClick(task.task.id)
            }) {
        Row {
            ColorForCategory(colorInt = task.category?.color)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissibleTaskItem(
    task: TaskWithCategory,
    onTaskClick: (Long) -> Unit,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onCheckedChanged.invoke(task)
        }
        true
    })

    SwipeToDismiss(state = dismissState, background = {
        SwipeToDismissBackground(dismissState.dismissDirection)
    }, dismissContent = {
        TaskItem(task = task, onTaskClick = onTaskClick)
    }, directions = setOf(DismissDirection.EndToStart)
    )

}

@Preview
@Composable
fun DismissibleTaskItemPreview() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val category1 = Category(name = "Books", color = android.graphics.Color.BLUE)
    val taskWithCategory = TaskWithCategory(task = task1, category = category1)
    DismissibleTaskItem(taskWithCategory, {}, {})
}

@Composable
fun SwipeToDismissBackground(dismissDirection: DismissDirection?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (dismissDirection == DismissDirection.EndToStart) Color.Red else Color.Transparent),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
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
    val category1 = Category(name = "Books", color = android.graphics.Color.BLUE)
    val taskWithCategory = TaskWithCategory(task = task1, category = category1)

    TaskItem(task = taskWithCategory, onTaskClick = {})
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
@Preview
fun TaskFilterLoadedPreview() {
    val categoryOne = Category(id = 0, name = "Dom", color = android.graphics.Color.BLUE)
    val categoryTwo = Category(id = 1, name = "Life", color = android.graphics.Color.GREEN)
    val categoryThree = Category(id = 2, name = "Work", color = android.graphics.Color.MAGENTA)
    val categories = listOf(categoryOne, categoryTwo, categoryThree)
    val state = CategoryUIState.Loaded(categories)
    val categoryHandler = CategoryStateHandler(state = state)
    TaskFilter(categoryHandler = categoryHandler)
}


@Composable
fun Categories(
    categoryState: CategoryUIState,
    currentCategory: Long?,
    onCategoryChange: (Long?) -> Unit,
    modifier: Modifier = Modifier
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
        text = stringResource(id = R.string.empty_categories_list)
    )
}

@Composable
fun CategoriesLoaded(
    currentCategory: Long?, categories: List<Category>, onCategoryChanged: (Long?) -> Unit
) {
    val currentItem = categories.find {
        it.id == currentCategory
    }
    var selectedState by remember {
        mutableStateOf(currentItem?.id)
    }
    LazyRow {
        items(items = categories, itemContent = { category ->
            CategoryChip(category = category,
                isSelected = selectedState == category.id,
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
    FilterChip(selected = isSelected, onClick = {
        val id = if (isSelected) null else category.id
        onSelectChanged(id)
    }, modifier = Modifier.padding(8.dp), colors = ChipDefaults.filterChipColors(
        selectedContentColor = MaterialTheme.colors.background,
        selectedBackgroundColor = Color(category.color)
    ), shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp)), content = {
        Text(text = category.name)
    })
}

@Composable
fun TasksListLoader(
    taskViewModel: TasksListViewModel = getViewModel(),
    categoryViewModel: CategoryViewModel = getViewModel(),
    modifier: Modifier,
    addClick: () -> Unit,
    onTaskClick: (Long) -> Unit
) {
    val (categoryId, setCategory) = rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    val taskState = remember(taskViewModel, categoryId) {
        taskViewModel.getTasksList(categoryId)
    }.collectAsState(initial = TasksListUIState.Loading)


    val categoryState = remember(categoryViewModel) {
        categoryViewModel.getCategories()
    }.collectAsState(initial = CategoryUIState.Loading)

    val taskStateHandler = TaskStateHandler(
        taskState.value,
        taskViewModel::updateTaskStatus,
        onAddClick = addClick,
        onItemClick = onTaskClick
    )
    val categoryStateHandler = CategoryStateHandler(
        state = categoryState.value,
        currentCategory = categoryId,
        onCategoryChange = setCategory
    )
    TaskListScaffold(categoryStateHandler, taskStateHandler, modifier)
}

@Preview
@Composable
fun TasksListScaffoldLoaded() {
    val task1 = Task(id = 0, title = "Buy milk", dueDate = null)
    val task2 = Task(id = 1, title = "Call Mark", dueDate = Calendar.getInstance())
    val task3 = Task(id = 2, title = "Watch Moonlight", dueDate = Calendar.getInstance())

    val category1 = Category(name = "Books", color = android.graphics.Color.GREEN)
    val category2 = Category(name = "Reminders", color = android.graphics.Color.MAGENTA)

    val listState = remember {
        mutableStateListOf(
            TaskWithCategory(task = task1, category = category1),
            TaskWithCategory(task = task2, category = category2),
            TaskWithCategory(task = task3, category = null)
        )
    }

    val state = TasksListUIState.Loaded(items = listState)

    DelaTheme {
        TaskListScaffold(
            tasksStateHandler = TaskStateHandler(state = state, onCheckedChange = {
                listState.remove(listState.find { taskWithCategory ->
                    taskWithCategory.task.id == it.task.id
                })
            }),
            categoryStateHandler = CategoryStateHandler(), modifier = Modifier
        )
    }
}