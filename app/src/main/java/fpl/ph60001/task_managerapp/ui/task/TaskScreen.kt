package fpl.ph60001.task_managerapp.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val state by viewModel.state.collectAsState()
    var title by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Quan ly cong viec", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Cong viec moi") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    viewModel.createTask(title)
                    title = ""
                }
            ) {
                Text("Them")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { viewModel.loadTasks() }) {
            Text("Tai lai")
        }
        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.loading -> CircularProgressIndicator()
            state.error != null -> Text(
                text = state.error ?: "Co loi khong xac dinh",
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.tasks) { task ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Trang thai: ${task.status} | Uu tien: ${task.priority}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
