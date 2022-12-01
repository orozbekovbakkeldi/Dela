package com.example.dela.viewModels

import com.example.dela.rules.CoroutineTestRule
import com.example.dela.fake.AddTaskFake
import com.example.dela.ui.home.tasks.AddTaskViewModel
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTaskViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()
    private val addTaskUseCase = AddTaskFake()

    private val viewModel = AddTaskViewModel(addTaskUseCase)


    @Before
    fun setup() {
        addTaskUseCase.clear()
    }

    @Test
    fun `check if new task is created`() = runTest {
        val taskName = "Hello World!!!"
        viewModel.addTask(taskName)
        assertTrue(addTaskUseCase.wasTaskCreated(taskName))
    }

    @Test
    fun `task should not be created when title is empty`() = runTest {
        val taskName = ""
        viewModel.addTask(taskName)
        assertFalse(addTaskUseCase.wasTaskCreated(taskName))
    }

}