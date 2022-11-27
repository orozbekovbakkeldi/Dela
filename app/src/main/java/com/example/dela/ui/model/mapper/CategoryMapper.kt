package com.example.dela.ui.model.mapper

import android.graphics.Color
import com.example.dela.ui.model.category.Category
import com.example.dela.domain.model.Category as DomainCategory

class CategoryMapper {

    companion object{
        private const val HexFormat = "#%06X"
        private const val HexWhite = 0xFFFFFF
    }

    fun toUI(category: DomainCategory) = Category(
        id = category.id,
        name = category.name,
        color = Color.parseColor(category.color)
    )

    fun toDomain(category: Category) = DomainCategory(
        category.id,
        category.name,
        category.color.toStringColor()
    )

    private fun Int.toStringColor() =
        String.format(HexFormat, HexWhite and this)


}