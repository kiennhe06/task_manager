package fpl.ph60001.task_managerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fpl.ph60001.task_managerapp.ui.theme.Task_ManagerAPPTheme
import fpl.ph60001.task_managerapp.ui.task.TaskScreen
import fpl.ph60001.task_managerapp.ui.task.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task_ManagerAPPTheme {
                val viewModel: TaskViewModel = viewModel()
                TaskScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting() {
    val viewModel: TaskViewModel = viewModel()
    TaskScreen(viewModel = viewModel)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Task_ManagerAPPTheme {
        Greeting()
    }
}