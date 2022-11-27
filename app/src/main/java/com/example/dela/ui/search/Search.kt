package com.example.dela.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dela.R
import com.example.dela.ui.home.category.DefaultIconTextContent
import com.example.dela.ui.model.task.TaskSearchItem
import com.example.dela.ui.sections.LoadingContent
import com.example.dela.ui.theme.DelaTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchSection(modifier: Modifier = Modifier, onItemClick: (Long) -> Unit) {
    SearchLoader(modifier = modifier, onItemClick = onItemClick)
}

@Composable
private fun SearchLoader(
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit,
    viewModel: SearchViewModel = getViewModel()
) {
    val (query, setQuery) = rememberSaveable { mutableStateOf("") }
    val viewState by remember(viewModel, query) {
        viewModel.findTasksByName(query)
    }.collectAsState(initial = SearchUiState.Loading)

    SearchScaffold(
        viewState = viewState,
        modifier = modifier,
        onItemClick = onItemClick,
        query = query,
        setQuery = setQuery
    )
}

@Composable
internal fun SearchScaffold(
    modifier: Modifier = Modifier,
    viewState: SearchUiState,
    onItemClick: (Long) -> Unit,
    query: String,
    setQuery: (String) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(padding)) {
            SearchTextField(query, onTextChange = setQuery)
            Crossfade(viewState) { state ->
                when (state) {
                    SearchUiState.Loading -> LoadingContent()
                    SearchUiState.Empty -> SearchEmptyContent()
                    is SearchUiState.Loaded -> SearchListContent(
                        taskList = state.taskList,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}


@Composable
private fun SearchTextField(text: String, onTextChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    )
}

@Composable
private fun SearchEmptyContent() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ExitToApp,
        iconContentDescription = R.string.search_cd_empty_list,
        header = R.string.search_header_empty
    )
}

@Composable
private fun SearchListContent(taskList: List<TaskSearchItem>, onItemClick: (Long) -> Unit) {
    LazyColumn {
        items(items = taskList, itemContent = { task -> SearchItem(task = task, onItemClick = onItemClick) }
        )
    }
}

@Composable
private fun SearchItem(task: TaskSearchItem, onItemClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable { onItemClick(task.id) },
        verticalArrangement = Arrangement.Center
    ) {
        val textDecoration: TextDecoration
        val circleColor: Color

        if (task.completed) {
            textDecoration = TextDecoration.LineThrough
            circleColor = MaterialTheme.colors.onSurface
        } else {
            textDecoration = TextDecoration.None
            circleColor = task.categoryColor ?: MaterialTheme.colors.onSurface
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )
            Text(
                text = task.title,
                textDecoration = textDecoration,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun SearchLoadedListPreview() {
    val task1 = TaskSearchItem(
        completed = true,
        title = "Call Me By Your Name",
        categoryColor = Color.Green,
        isRepeating = false
    )

    val task2 = TaskSearchItem(
        completed = false,
        title = "The Crown",
        categoryColor = Color.White,
        isRepeating = true
    )

    val taskList = listOf(task1, task2)

    DelaTheme {
        SearchScaffold(
            modifier = Modifier,
            viewState = SearchUiState.Loaded(taskList),
            onItemClick = {},
            query = "",
            setQuery = {}
        )
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun SearchEmptyListPreview() {
    DelaTheme {
        SearchScaffold(
            modifier = Modifier,
            viewState = SearchUiState.Empty,
            onItemClick = {},
            query = "",
            setQuery = {}
        )
    }
}
