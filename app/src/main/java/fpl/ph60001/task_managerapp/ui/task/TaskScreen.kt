package fpl.ph60001.task_managerapp.ui.task

import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpl.ph60001.task_managerapp.R
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val state by viewModel.state.collectAsState()
    var editingTask by remember { mutableStateOf<TaskItem?>(null) }
    var taskToDelete by remember { mutableStateOf<TaskItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Premium Header with Stats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                .padding(24.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Công việc của bạn",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Quản lý mục tiêu",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Simple Stats inside Header
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HeaderMiniStat(label = "Chờ", count = state.tasks.count { it.status == "todo" })
                    HeaderMiniStat(label = "Đang làm", count = state.tasks.count { it.status == "in_progress" })
                    HeaderMiniStat(label = "Xong", count = state.tasks.count { it.status == "done" })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .offset(y = (-30).dp)
        ) {
            // Interactive Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text(stringResource(R.string.label_search), color = Color.Gray) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = GradientStart) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = GradientStart.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Refined Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    null to "Tất cả",
                    "todo" to "Chờ làm",
                    "in_progress" to "Đang làm",
                    "done" to "Hoàn thành"
                )
                filters.forEach { (statusValue, label) ->
                    val isSelected = state.filterStatus == statusValue
                    Surface(
                        onClick = { viewModel.onFilterStatusChanged(statusValue) },
                        color = if (isSelected) GradientStart else MaterialTheme.colorScheme.surface,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 2.dp,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                            Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isFetching && state.tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GradientStart)
                }
            } else if (state.tasks.isEmpty()) {
                EmptyState(onAddClick = { /* Handled by global FAB in MainScreen */ })
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.tasks, key = { it._id }) { task ->
                        TaskCard(
                            task = task,
                            onToggleStatus = {
                                val nextStatus = when(task.status) {
                                    "done" -> "todo"
                                    else -> "done"
                                }
                                viewModel.updateTask(task.copy(status = nextStatus))
                            },
                            onEdit = { editingTask = task },
                            onDelete = { taskToDelete = task }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }

    // Dialogs
    editingTask?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { editingTask = null },
            onSave = { updatedTask ->
                viewModel.updateTask(updatedTask)
                editingTask = null
            }
        )
    }

    taskToDelete?.let { task ->
        DeleteConfirmDialog(
            task = task,
            onDismiss = { taskToDelete = null },
            onConfirm = {
                viewModel.deleteTask(task._id)
                taskToDelete = null
            }
        )
    }
}

@Composable
fun HeaderMiniStat(label: String, count: Int) {
    Column {
        Text(text = count.toString(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun EmptyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Assignment,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chưa có công việc nào",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Thêm công việc đầu tiên ngay hôm nay!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Thêm ngay")
        }
    }
}

@Composable
fun TaskCard(
    task: TaskItem,
    onToggleStatus: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isDone = task.status == "done"
    val priorityColor = when (task.priority) {
        "high" -> PriorityHigh
        "medium" -> PriorityMedium
        else -> PriorityLow
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quick Status Toggle (Actionable!)
            IconButton(
                onClick = onToggleStatus,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isDone) StatusDone else Color.Transparent)
                    .border(2.dp, if (isDone) StatusDone else Color.LightGray, CircleShape)
            ) {
                if (isDone) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(priorityColor))
                    Text(text = task.priority.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = priorityColor)
                    
                    if (task.dueDate != null) {
                        VerticalDivider(modifier = Modifier.height(10.dp).width(1.dp), color = Color.LightGray)
                        Text(text = formatDateDisplay(task.dueDate), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
            }

            // Explicit Function Buttons (User asked for "functions")
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Sửa", tint = GradientStart, modifier = Modifier.size(22.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Xóa", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

@Composable
fun AddFileOrTaskDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo công việc mới", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tiêu đề công việc") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(title) }, enabled = title.isNotBlank(), shape = RoundedCornerShape(12.dp)) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun DeleteConfirmDialog(task: TaskItem, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận xóa", fontWeight = FontWeight.Bold) },
        text = { Text("Bạn có chắc chắn muốn xóa công việc \"${task.title}\" không?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Xóa", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(task: TaskItem, onDismiss: () -> Unit, onSave: (TaskItem) -> Unit) {
    val context = LocalContext.current
    var editTitle by remember(task._id) { mutableStateOf(task.title) }
    var editDesc by remember(task._id) { mutableStateOf(task.description) }
    var editStatus by remember(task._id) { mutableStateOf(task.status) }
    var editPriority by remember(task._id) { mutableStateOf(task.priority) }
    var editDueDate by remember(task._id) { mutableStateOf(task.dueDate ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = { 
            Text(
                "Chỉnh sửa công việc",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = editTitle,
                    onValueChange = { editTitle = it },
                    label = { Text("Tiêu đề") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = editDesc,
                    onValueChange = { editDesc = it },
                    label = { Text("Mô tả") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Column {
                    Text("Trạng thái", style = MaterialTheme.typography.labelLarge, color = GradientStart, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                        listOf("todo", "in_progress", "done").forEach { s ->
                            FilterChip(
                                selected = editStatus == s,
                                onClick = { editStatus = s },
                                label = { Text(formatStatus(s)) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                Column {
                    Text("Độ ưu tiên", style = MaterialTheme.typography.labelLarge, color = GradientStart, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                        listOf("low", "medium", "high").forEach { p ->
                            FilterChip(
                                selected = editPriority == p,
                                onClick = { editPriority = p },
                                label = { Text(p.uppercase()) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = editDueDate.let { if (it.isNotBlank()) formatDateDisplay(it) else "" },
                    onValueChange = { },
                    label = { Text("Hạn chót") },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    editDueDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = GradientStart)
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(task.copy(
                        title = editTitle,
                        description = editDesc,
                        status = editStatus,
                        priority = editPriority,
                        dueDate = editDueDate.takeIf { it.isNotBlank() }
                    ))
                },
                enabled = editTitle.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu thay đổi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

// Helper functions (same as before but ensured they are available)
private fun formatStatus(status: String): String = when (status) {
    "todo" -> "Chờ làm"
    "in_progress" -> "Đang làm"
    "done" -> "Hoàn thành"
    else -> status
}

private fun formatDateDisplay(dateStr: String): String {
    return try {
        val inputFormats = listOf("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        for (pattern in inputFormats) {
            try {
                val date = SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr)
                if (date != null) return outputFormat.format(date)
            } catch (_: Exception) {}
        }
        dateStr
    } catch (_: Exception) {
        dateStr
    }
}

private fun isTaskOverdue(task: TaskItem): Boolean {
    if (task.status == "done" || task.dueDate == null) return false
    return try {
        val inputFormats = listOf("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd")
        val now = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        for (pattern in inputFormats) {
            try {
                val date = SimpleDateFormat(pattern, Locale.getDefault()).parse(task.dueDate)
                if (date != null) return date.before(now)
            } catch (_: Exception) {}
        }
        false
    } catch (_: Exception) {
        false
    }
}

