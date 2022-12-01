package com.example.dela.viewModels

import com.example.dela.domain.model.Category
import com.example.dela.domain.model.Task
import com.example.dela.domain.model.TaskWithCategory
import com.example.dela.domain.usecase.SearchTasksByName
import com.example.dela.ui.model.mapper.TaskSearchMapper
import com.example.dela.ui.search.SearchUiState
import com.example.dela.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val mockMapper = TaskSearchMapper()

    private val taskSearchFake = mock<SearchTasksByName>()

    private val viewModel = SearchViewModel(taskSearchFake, mockMapper)

    @Test
    fun `if there are tasks loaded state should be returned`() = runTest {
        whenever(taskSearchFake.invoke(any())).doReturn(flow {
            val task = Task(title = "Buy milk", dueDate = null)
            val category = Category(name = "Books", color = "#FF0000")
            emit(listOf(TaskWithCategory(task, category)))
        })
        val flow = viewModel.findTasksByName("test")
        val state = flow.first()
        assert(state is SearchUiState.Loaded)
    }

    @Test
    fun `if there is no any tasks empty state should be returned`() = runTest {
        whenever(taskSearchFake.invoke(any())).doReturn(flow { emit(emptyList()) })
        val flow = viewModel.findTasksByName("test")
        val state = flow.first()
        assert(state is SearchUiState.Empty)
    }


}