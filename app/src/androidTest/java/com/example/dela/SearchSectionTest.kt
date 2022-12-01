package com.example.dela

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.example.dela.ui.model.task.TaskSearchItem
import com.example.dela.ui.search.SearchScaffold
import com.example.dela.ui.search.SearchUiState
import com.example.dela.ui.theme.DelaTheme
import org.junit.Rule
import org.junit.Test

class SearchSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()


    @Test
    fun test_empty_search_result() {
        val state = SearchUiState.Empty
        loadSearchContent(state)
        val header = context.getString(R.string.search_header_empty)
        val content = context.getString(R.string.search_cd_empty_list)
        composeRule.onNodeWithText(header).assertExists()
        composeRule.onNodeWithContentDescription(content).assertExists()
    }

    @Test
    fun test_loaded_search_result() {

        val item1 = TaskSearchItem(title = "Complete assignment", categoryColor = Color.Yellow, completed = false, isRepeating = false)
        val item2 = TaskSearchItem(title = "Complete project", categoryColor = Color.Green, completed = true, isRepeating = false)

        val state = SearchUiState.Loaded(listOf(item2, item1))
        loadSearchContent(state)
        composeRule.onNodeWithText(item1.title).assertExists()
        composeRule.onNodeWithText(item2.title).assertExists()

    }


    private fun loadSearchContent(state: SearchUiState) {
        composeRule.setContent {
            DelaTheme {
                SearchScaffold(viewState = state, onItemClick = {}, query = "", setQuery = {})
            }
        }
    }


}