package com.example.dela.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dela.R

enum class HomeSection(val route: String, @StringRes val title: Int, val image: ImageVector) {
    Tasks("home", R.string.tasks, Icons.Outlined.Check),
    Category("category", R.string.categories, Icons.Outlined.Star),
    Search("search", R.string.search, Icons.Outlined.Search);

    companion object {
        fun findTitleByRoute(route: String?): Int? {
            val title = values().find {
                it.route == route
            }?.title
            return title
        }
    }
}
