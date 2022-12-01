package com.example.dela

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.compose.ui.test.SemanticsMatcher
import com.example.dela.data.dao.CategoryDao
import com.example.dela.data.dao.TasksDao
import com.example.dela.data.entity.CategoryEntity
import com.example.dela.ui.theme.DelaTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val tasksDao: TasksDao by inject()
    private val categoryDao: CategoryDao by inject()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        runTest {
            categoryDao.cleanTable()
            tasksDao.cleanTable()
            categoryDao.addCategory(CategoryEntity(name = "Books", color = "#cc5a71"))
            categoryDao.addCategory(CategoryEntity(name = "Work", color = "#519872"))
        }

        composeTestRule.setContent {
            DelaTheme {
                NavGraph()
            }
        }
    }

    @Test
    fun check_task_creation() {
        val taskName = "Make an appointment"
        createTask(taskName)
        composeTestRule.onNodeWithText(context.getString(R.string.add)).performClick()
        composeTestRule.onNodeWithText(text = taskName, useUnmergedTree = true).assertExists()
    }


    private fun createTask(taskName: String) {
        with(composeTestRule) {
            onNodeWithContentDescription(
                context.getString(R.string.task_cd_add_task),
                useUnmergedTree = true
            ).performClick()
            onNode(matcher = hasSetTextAction()).performTextInput(taskName)
        }
    }

    @Test
    fun check_if_task_appears_in_the_category_it_was_created_in() {
        val taskName = "Make an assignment"
        with(composeTestRule) {
            createTask(taskName)
            onAllNodesWithText("Work")[1].performClick()
            onNodeWithText(context.getString(R.string.add)).performClick()
            waitUntilExists(hasText(taskName))
            onNodeWithText(taskName).assertExists()

            onNodeWithText("Work").performClick()
            waitUntilExists(hasText(taskName))
            onNodeWithText(taskName).assertExists()

            onNodeWithText("Books").performClick()
            waitUntilNotExists(hasText(taskName))
            onNodeWithText(taskName).assertDoesNotExist()
        }
    }


    @Test
    fun check_complete_task() {
        val taskName = "Finish assignment"
        with(composeTestRule){
            createTask(taskName)
            onAllNodesWithText("Work")[1].performClick()
            onNodeWithText(context.getString(R.string.add)).performClick()
            waitUntilExists(hasText(taskName))
            onNodeWithText(taskName).assertExists()
            onNodeWithTag(taskName).performTouchInput {
                swipeLeft()
            }
            waitUntilNotExists(hasText(taskName))
            onNodeWithText(taskName).assertDoesNotExist()
        }
    }



    private fun ComposeContentTestRule.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 3_000L
    ) = waitUntilNodeCount(matcher = matcher, count = 1, timeoutMillis = timeoutMillis)

    private fun ComposeContentTestRule.waitUntilNotExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 3_000L
    ) = waitUntilNodeCount(matcher = matcher, count = 0, timeoutMillis = timeoutMillis)

    private fun ComposeContentTestRule.waitUntilNodeCount(
        matcher: SemanticsMatcher,
        count: Int,
        timeoutMillis: Long = 3_000L
    ) = waitUntil(timeoutMillis) {
        onAllNodes(matcher).fetchSemanticsNodes().size == count
    }

}