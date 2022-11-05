package com.example.dela.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dela.R

enum class Screen(val route: String, @StringRes val title: Int, val image: ImageVector){
    Home("home", R.string.tasks, Icons.Outlined.Check),
    Category("category", R.string.categories, Icons.Outlined.Info),
    Search("search", R.string.search, Icons.Outlined.Search)
}
