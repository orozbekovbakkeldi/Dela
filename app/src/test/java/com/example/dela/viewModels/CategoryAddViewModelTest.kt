package com.example.dela.viewModels

import android.graphics.Color
import com.example.dela.rules.CoroutineTestRule
import com.example.dela.fake.AddCategoryFake
import com.example.dela.ui.home.category.CategoryAddViewModel
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.mapper.CategoryMapper
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryAddViewModelTest {
    private val addCategory = AddCategoryFake()
    private val viewModel = CategoryAddViewModel(addCategory, CategoryMapper())

    @get:Rule
    val rule = CoroutineTestRule()

    @Test
    fun `check if category was added`() = runTest {
        val color = Color.parseColor("#9CCC65")
        val category = Category(name = "Fake", color = color)
        viewModel.addCategory(category)
        assertTrue(addCategory.wasCategoryCreated(category.name))
    }

    @Test
    fun `check if category without name is not added`() = runTest {
        val color = Color.parseColor("#9CCC65")
        val category = Category(name = "", color = color)
        viewModel.addCategory(category)
        assertFalse(addCategory.wasCategoryCreated(category.name))
    }


}