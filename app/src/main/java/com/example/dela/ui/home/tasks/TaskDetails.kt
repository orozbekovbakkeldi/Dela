package com.example.dela.ui.home.tasks

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dela.R
import com.example.dela.ui.alarm.AlarmPermission
import com.example.dela.ui.alarm.AlarmSelectionState
import com.example.dela.ui.alarm.DateTimePickerDialog
import com.example.dela.ui.alarm.rememberAlarmSelectionState
import com.example.dela.ui.home.alarm.AlarmViewModel
import com.example.dela.ui.home.category.CategoryViewModel
import com.example.dela.ui.home.category.DelaDialog
import com.example.dela.ui.model.category.CategoryUIState
import com.example.dela.ui.model.task.AlarmInterval
import com.example.dela.ui.model.task.Task
import com.example.dela.ui.model.task.TaskDetailState
import com.example.dela.ui.sections.Categories
import com.example.dela.ui.sections.LoadingContent
import com.google.accompanist.permissions.*
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import java.text.DateFormat
import java.util.*


@Composable
fun TaskDetails(
    taskId: Long,
    modifier: Modifier = Modifier,
    onUp: () -> Unit,
    taskDetailsViewModel: TaskDetailsViewModel = getViewModel(),
    categoriesViewModel: CategoryViewModel = getViewModel(),
    alarmViewModel: AlarmViewModel = getViewModel(),
    alarmPermission: AlarmPermission = get()
) {


    val taskInfoState by remember(taskDetailsViewModel, taskId) {
        taskDetailsViewModel.getTaskInfo(taskId)
    }.collectAsState(initial = TaskDetailState.Loading)


    val categoriesState by remember(categoriesViewModel, taskId) {
        categoriesViewModel.getCategories()
    }.collectAsState(initial = CategoryUIState.Loading)

    val taskDetailsActions = TaskDetailsActions(
        onTitleChanged = { title ->
            taskDetailsViewModel.updateTitle(taskId, title)
        },
        onDescriptionChanged = { description ->
            taskDetailsViewModel.updateDescription(taskId, description)
        },
        onCategoryChanged = { categoryId ->
            taskDetailsViewModel.updateTaskCategory(taskId, categoryId)
        },
        onAlarmUpdate = { date ->
            alarmViewModel.updateAlarm(taskId, date)
        },
        onIntervalSelect = { interval ->
            alarmViewModel.setRepeating(taskId, interval)
        },
        hasAlarmPermission = {
            alarmPermission.hasExactAlarmPermission()
        },
        shouldCheckNotificationPermission = alarmPermission.shouldCheckNotificationPermission()
    )

    Scaffold(topBar = {
        TopAppBar(title = {}, modifier = modifier, navigationIcon = {
            IconButton(onClick = onUp) {
                Icon(Icons.Default.ArrowBack, stringResource(id = R.string.back_arrow))
            }
        })
    }) { paddingValues ->
        Crossfade(
            targetState = taskInfoState, modifier = Modifier.padding(paddingValues)
        ) { state ->
            when (state) {
                is TaskDetailState.Loading -> LoadingContent()
                is TaskDetailState.Error -> {}
                is TaskDetailState.Loaded -> {
                    TaskDetailsLoaded(
                        task = state.task,
                        categoryState = categoriesState,
                        categoryChanged = { categoryId ->
                            categoryId?.let {
                                taskDetailsViewModel.updateTaskCategory(taskId, categoryId)
                            }
                        },
                        taskDetailsActions = taskDetailsActions
                    )
                }
            }
        }

    }
}

@Composable
fun TaskDetailsLoaded(
    task: Task,
    categoryState: CategoryUIState,
    taskDetailsActions: TaskDetailsActions,
    categoryChanged: (Long?) -> Unit
) {
    Surface(color = Color.Transparent) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            TaskTitleTextField(text = task.title, onTitleChange = taskDetailsActions.onTitleChanged)
            TaskCategories {
                Categories(
                    categoryState = categoryState,
                    currentCategory = task.categoryId,
                    onCategoryChange = categoryChanged
                )
            }
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChange = taskDetailsActions.onDescriptionChanged
            )
            AlarmSelection(
                calendar = task.dueDate,
                interval = task.alarmInterval,
                onAlarmUpdate = taskDetailsActions.onAlarmUpdate,
                onIntervalSelect = taskDetailsActions.onIntervalSelect,
                hasAlarmPermission = taskDetailsActions.hasAlarmPermission,
                shouldCheckNotificationPermission = taskDetailsActions.shouldCheckNotificationPermission
            )
        }
    }
}

@Composable
fun TaskCategories(content: @Composable BoxScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = stringResource(id = R.string.categories)
        )
        Box(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}

@Composable
private fun TaskDescriptionTextField(text: String?, onDescriptionChange: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text ?: "")) }

    TextField(modifier = Modifier
        .fillMaxWidth()
        .clearFocusOnKeyboardDismiss(), leadingIcon = {
        LeadingIcon(
            imageVector = Icons.Default.List, contentDescription = R.string.task_description
        )
    }, value = textState.value, onValueChange = {
        onDescriptionChange(it.text)
        textState.value = it
    }, textStyle = MaterialTheme.typography.body1, colors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.background
    )
    )
}

fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect);
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom;
    return keypadHeight > screenHeight * 0.15
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}


@Composable
fun LeadingIcon(
    imageVector: ImageVector, @StringRes contentDescription: Int, modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = stringResource(id = contentDescription),
        tint = MaterialTheme.colors.onBackground,
        modifier = modifier
    )
}

@Composable
private fun TaskTitleTextField(text: String, onTitleChange: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text)) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(), value = textState.value, onValueChange = {
            onTitleChange(it.text)
            textState.value = it
        }, textStyle = MaterialTheme.typography.h5, colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background
        )
    )
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AlarmSelection(
    calendar: Calendar?,
    interval: AlarmInterval?,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    hasAlarmPermission: () -> Boolean,
    shouldCheckNotificationPermission: Boolean
) {
    val context = LocalContext.current
    val permissionState = if (shouldCheckNotificationPermission) {
        rememberPermissionState(permission = POST_NOTIFICATIONS)
    } else {
        getGrantedPermissionState(permission = POST_NOTIFICATIONS)
    }
    val state = rememberAlarmSelectionState(calendar = calendar, alarmInterval = interval)

    // Exact Alarm permission dialog
    AlarmPermissionDialog(
        context = context,
        isDialogOpen = state.showExactAlarmDialog,
        onCloseDialog = { state.showExactAlarmDialog = false }
    )

    // Notification permission dialog
    NotificationPermissionDialog(
        permissionState = permissionState,
        isDialogOpen = state.showNotificationDialog,
        onCloseDialog = { state.showNotificationDialog = false }
    )

    // Rationale permission dialog
    RationalePermissionDialog(
        context = context,
        isDialogOpen = state.showRationaleDialog,
        onCloseDialog = { state.showRationaleDialog = false }
    )

    AlarmSelectionContent(
        context = context,
        state = state,
        permissionState = permissionState,
        hasAlarmPermission = hasAlarmPermission,
        onAlarmUpdate = onAlarmUpdate,
        onIntervalSelect = onIntervalSelect
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionDialog(
    permissionState: PermissionState,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_notification_permission_dialog_title),
        text = stringResource(id = R.string.task_notification_permission_dialog_text),
        confirmText = stringResource(id = R.string.task_notification_permission_dialog_confirm),
        dismissText = stringResource(id = R.string.task_notification_permission_dialog_cancel),
        onConfirmAction = {
            permissionState.launchPermissionRequest()
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
fun RationalePermissionDialog(
    context: Context,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_notification_rationale_dialog_title),
        text = stringResource(id = R.string.task_notification_rationale_dialog_text),
        confirmText = stringResource(id = R.string.task_notification_rationale_dialog_confirm),
        dismissText = stringResource(id = R.string.task_notification_rationale_dialog_cancel),
        onConfirmAction = {
            val intent = Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
            onCloseDialog()
        }
    )
    DelaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog
    )
}


@Suppress("LongParameterList")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlarmSelectionContent(
    context: Context,
    state: AlarmSelectionState,
    permissionState: PermissionState,
    hasAlarmPermission: () -> Boolean,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    Column {
        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    when {
                        hasAlarmPermission() && permissionState.status.isGranted ->
                            DateTimePickerDialog(context) { calendar ->
                                state.date = calendar
                                onAlarmUpdate(calendar)
                            }.show()
                        permissionState.status.shouldShowRationale ->
                            state.showRationaleDialog = true
                        else -> {
                            state.showExactAlarmDialog = !hasAlarmPermission()
                            state.showNotificationDialog = !permissionState.status.isGranted
                        }
                    }
                },
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_alarm_24),
            contentDescription = R.string.task_detail_cd_icon_alarm
        ) {
            AlarmInfo(state.date) {
                state.date = null
                onAlarmUpdate(null)
            }
        }
        AlarmIntervalSelection(
            date = state.date,
            alarmInterval = state.alarmInterval,
            onIntervalSelect = { interval ->
                state.alarmInterval = interval
                onIntervalSelect(interval)
            }
        )
    }
}


@Composable
fun AlarmIntervalSelection(
    date: Calendar?,
    alarmInterval: AlarmInterval?,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    if (date != null) {
        AlarmIntervalDialog(showDialog) { interval -> onIntervalSelect(interval) }

        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable { showDialog.value = true },
            imageVector = Icons.Default.Refresh,
            contentDescription = R.string.task_detail_cd_icon_repeat_alarm
        ) {
            val index = alarmInterval?.index ?: 0
            Text(
                text = stringArrayResource(id = R.array.task_alarm_repeating)[index],
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun TaskDetailSectionContent(
    imageVector: ImageVector,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIcon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
        Box(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}

@Composable
private fun AlarmIntervalDialog(
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    if (showDialog.value.not()) {
        return
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            val intervalList = stringArrayResource(id = R.array.task_alarm_repeating)
            LazyColumn(modifier = Modifier.padding(24.dp)) {
                itemsIndexed(
                    items = intervalList,
                    itemContent = { index, title ->
                        AlarmListItem(
                            title = title,
                            index = index,
                            showDialog = showDialog,
                            onIntervalSelect = onIntervalSelect
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AlarmListItem(
    title: String,
    index: Int,
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val interval =
                    AlarmInterval
                        .values()
                        .find { it.index == index } ?: AlarmInterval.NEVER
                onIntervalSelect(interval)
                showDialog.value = false
            }
    )
}

@Composable
fun AlarmInfo(
    date: Calendar?,
    onRemoveDate: () -> Unit
) {
    Column {
        if (date == null) {
            NoAlarmSet()
        } else {
            AlarmSet(
                date = date,
                onRemoveClick = onRemoveDate
            )
        }
    }
}

@Composable
private fun AlarmSet(date: Calendar?, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date?.format() ?: "",
            color = MaterialTheme.colors.onSurface
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(
                    id = R.string.task_detail_cd_icon_remove_alarm
                )
            )
        }
    }

}

fun Calendar.format(): String {
    val dateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG,
        DateFormat.SHORT,
        Locale.getDefault()
    )
    return dateFormat.format(time)
}


@Composable
private fun NoAlarmSet() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.task_detail_alarm_no_alarm),
            color = MaterialTheme.colors.onSurface
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
fun getGrantedPermissionState(permission: String): PermissionState = object : PermissionState {
    override val permission: String
        get() = permission

    override val status: PermissionStatus
        get() = PermissionStatus.Granted

    override fun launchPermissionRequest() {
        // Do nothing
    }
}

@Composable
fun AlarmPermissionDialog(
    context: Context,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_alarm_permission_dialog_title),
        text = stringResource(id = R.string.task_alarm_permission_dialog_text),
        confirmText = stringResource(id = R.string.task_alarm_permission_dialog_confirm),
        dismissText = stringResource(id = R.string.task_alarm_permission_dialog_cancel),
        onConfirmAction = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }
                context.startActivity(intent)
                onCloseDialog()
            }
        }
    )
    DelaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog
    )
}

data class DialogArguments(
    val title: String,
    val text: String,
    val confirmText: String,
    val dismissText: String,
    val onConfirmAction: () -> Unit
)