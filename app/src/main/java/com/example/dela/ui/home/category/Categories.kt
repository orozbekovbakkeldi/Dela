package com.example.dela.ui.home.category

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dela.R
import com.example.dela.ui.model.CategorySheetState
import com.example.dela.ui.model.category.Category
import com.example.dela.ui.model.category.CategoryUIState
import com.example.dela.ui.sections.AddFloatingActionButton
import com.example.dela.ui.sections.LoadingContent
import kotlinx.coroutines.delay
import kotlinx.parcelize.IgnoredOnParcel
import org.koin.androidx.compose.getViewModel
import kotlin.math.max
import kotlin.math.roundToInt


@Composable
fun CategoryListSection(
    modifier: Modifier,
    onShowBottomSheet: (Long?) -> Unit
) {
    CategoryListLoader(
        modifier = modifier,
        onItemClick = onShowBottomSheet,
        onAddClick = { onShowBottomSheet(null) }
    )
}

@Composable
private fun CategoryListLoader(
    modifier: Modifier,
    viewModel: CategoryViewModel = getViewModel(),
    onItemClick: (Long?) -> Unit,
    onAddClick: () -> Unit
) {
    val viewState by remember(viewModel) {
        viewModel.getCategories()
    }.collectAsState(initial = CategoryUIState.Loading)

    CategoryListScaffold(
        modifier = modifier,
        viewState = viewState,
        onItemClick = onItemClick,
        onAddClick = onAddClick
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CategoryListScaffold(
    modifier: Modifier,
    viewState: CategoryUIState,
    onItemClick: (Long?) -> Unit,
    onAddClick: () -> Unit
) {
    BoxWithConstraints {
        val fabPosition = if (this.maxHeight > maxWidth) FabPosition.Center else FabPosition.End
        Scaffold(
            modifier = modifier.fillMaxSize(),
            floatingActionButton = {
                AddFloatingActionButton {
                    onAddClick()
                }
            },
            floatingActionButtonPosition = fabPosition
        ) {
            Crossfade(viewState) { state ->
                when (state) {
                    is CategoryUIState.Loading -> LoadingContent()
                    is CategoryUIState.Empty -> DefaultIconTextContent(
                        icon = Icons.Default.ThumbUp,
                        iconContentDescription = R.string.no_categories_content_description,
                        header = R.string.empty_categories_list
                    )
                    is CategoryUIState.Loaded -> CategoryListContent(state.categories, onItemClick)
                }
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun CategoryListContent(categoryList: List<Category>, onItemClick: (Long?) -> Unit) {
    BoxWithConstraints(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        val cellCount: Int = max(2F, maxWidth.value / 250).roundToInt()
        LazyVerticalGrid(columns = GridCells.Fixed(cellCount)) {
            items(
                items = categoryList,
                itemContent = { category ->
                    CategoryItem(category = category, onItemClick = onItemClick)
                }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onItemClick: (Long) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable { onItemClick(category.id) }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            CategoryItemIcon(category.color)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = category.name)
        }
    }
}

@Composable
private fun CategoryItemIcon(color: Int) {
    Box(contentAlignment = Alignment.Center) {
        CategoryCircleIndicator(size = 48.dp, color = color, alpha = 0.2F)
        CategoryCircleIndicator(size = 40.dp, color = color)
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colors.background
        )
    }
}

@Composable
private fun CategoryCircleIndicator(size: Dp, color: Int, alpha: Float = 1F) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .alpha(alpha)
            .background(Color(color))
    )
}


@Composable
fun DefaultIconTextContent(
    icon: ImageVector,
    @StringRes iconContentDescription: Int,
    @StringRes header: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = iconContentDescription),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colors.secondary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = header),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun CategoryBottomSheet(categoryId: Long, onHideBottomSheet: () -> Unit) {
    val colorList = CategoryColors.values().map { it.value }
    if (categoryId == 0L) {
        CategoryNewSheetLoader(
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet
        )
    } else {
        CategoryEditSheetLoader(
            categoryId = categoryId,
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet
        )
    }
}

@Composable
private fun CategoryEditSheetLoader(
    editViewModel: CategoryEditViewModel = getViewModel(),
    categoryId: Long,
    colorList: List<Color>,
    onHideBottomSheet: () -> Unit
) {
    val categoryState by remember(editViewModel, categoryId) {
        editViewModel.getCategory(categoryId = categoryId)
    }.collectAsState(initial = CategorySheetState.Empty)

    val category = when (categoryState) {
        is CategorySheetState.Empty -> emptyCategory()
        is CategorySheetState.Loaded -> (categoryState as CategorySheetState.Loaded).category
    }

    val sheetState by rememberSaveable(categoryState) {
        mutableStateOf(CategoryBottomSheetState(category))
    }

    CategorySheetContent(
        colorList = colorList,
        state = sheetState,
        onCategoryChange = { updatedState ->
            editViewModel.updateCategory(updatedState.toCategory())
            onHideBottomSheet()
        },
        onCategoryRemove = { cat ->
            editViewModel.deleteCategory(cat.id)
            onHideBottomSheet()
        }
    )
}


@Composable
private fun CategoryNewSheetLoader(
    addViewModel: CategoryAddViewModel = getViewModel(),
    colorList: List<Color>,
    onHideBottomSheet: () -> Unit
) {
    val sheetState by rememberSaveable(addViewModel) {
        mutableStateOf(CategoryBottomSheetState(emptyCategory()))
    }

    CategorySheetContent(
        colorList = colorList,
        state = sheetState,
        onCategoryChange = { updatedState ->
            addViewModel.addCategory(updatedState.toCategory())
            onHideBottomSheet()
        }
    )
}

@Composable
@Suppress("MagicNumber")
private fun CategorySheetContent(
    state: CategoryBottomSheetState,
    colorList: List<Color>,
    onCategoryChange: (CategoryBottomSheetState) -> Unit,
    onCategoryRemove: (Category) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .background(MaterialTheme.colors.surface) // Accompanist does not support M3 yet
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var openDialog by rememberSaveable { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                delay(300)
                focusRequester.requestFocus()
            }

            RemoveCategoryDialog(
                categoryName = state.name,
                isDialogOpen = openDialog,
                onCloseDialog = { openDialog = false },
                onActionConfirm = { onCategoryRemove(state.toCategory()) }
            )

            OutlinedTextField(
                label = {
                    Text(text = stringResource(id = R.string.category_add_label))
                },
                value = state.name,
                textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
                onValueChange = {
                    state.name = it
                },
                modifier = Modifier
                    .weight(5F)
                    .focusRequester(focusRequester)
            )
            if (state.isEditing()) {
                IconButton(
                    onClick = { openDialog = true },
                    modifier = Modifier
                        .height(64.dp)
                        .weight(1F)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.category_cd_remove_category)
                    )
                }
            }
        }

        CategoryColorSelector(
            colorList = colorList,
            value = Color(state.color),
            onColorChange = { state.color = it.toArgb() }
        )

        CategorySaveButton(
            currentColor = Color(state.color),
            onClick = { onCategoryChange(state) }
        )
    }
}

@Composable
private fun CategorySaveButton(currentColor: Color, onClick: () -> Unit) {
    val colorState = animateColorAsState(targetValue = currentColor)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(contentColor = colorState.value),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(id = R.string.category_sheet_save),
            color = MaterialTheme.colors.background
        )
    }
}

@Composable
private fun CategoryColorSelector(
    colorList: List<Color>,
    value: Color,
    onColorChange: (Color) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        items(
            items = colorList,
            itemContent = { color ->
                val optionSelected = color == value
                CategoryColorItem(color, optionSelected, onClick = { onColorChange(color) })
            }
        )
    }
}

private fun emptyCategory() = Category(
    name = "",
    color = CategoryColors.values()[0].value.toArgb()
)

@Composable
private fun CategoryColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = color)
                .selectable(
                    role = Role.RadioButton,
                    selected = isSelected,
                    onClick = onClick
                )
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.background)
            )
        }
    }
}

@Composable
private fun RemoveCategoryDialog(
    categoryName: String,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onActionConfirm: () -> Unit
) {
    val arguments = com.example.dela.ui.home.tasks.DialogArguments(
        title = stringResource(id = R.string.category_dialog_remove_title),
        text = stringResource(id = R.string.category_dialog_remove_text, categoryName),
        confirmText = stringResource(id = R.string.category_dialog_remove_confirm),
        dismissText = stringResource(id = R.string.category_dialog_remove_cancel),
        onConfirmAction = {
            onActionConfirm()
            onCloseDialog()
        }
    )
    DelaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog
    )
}

@Composable
fun DelaDialog(
    arguments: com.example.dela.ui.home.tasks.DialogArguments,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = arguments.title) },
            text = { Text(text = arguments.text) },
            confirmButton = {
                Button(onClick = arguments.onConfirmAction) {
                    Text(text = arguments.confirmText)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismissRequest) {
                    Text(text = arguments.dismissText)
                }
            }
        )
    }
}


@Stable
@kotlinx.parcelize.Parcelize
internal class CategoryBottomSheetState(
    private val category: Category
) : Parcelable {

    @IgnoredOnParcel
    var id by mutableStateOf(category.id)

    @IgnoredOnParcel
    var name by mutableStateOf(category.name)

    @IgnoredOnParcel
    var color by mutableStateOf(category.color)

    fun isEditing(): Boolean =
        id > -1L

    fun toCategory(): Category =
        Category(
            id = id,
            name = name,
            color = color
        )
}
