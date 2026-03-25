package fpl.ph60001.task_managerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fpl.ph60001.task_managerapp.data.session.SessionManager
import fpl.ph60001.task_managerapp.ui.auth.AuthScreen
import fpl.ph60001.task_managerapp.ui.auth.AuthViewModel
import fpl.ph60001.task_managerapp.ui.main.MainScreen
import fpl.ph60001.task_managerapp.ui.theme.Task_ManagerAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            TaskManagerApp()
        }
    }
}

@Composable
fun TaskManagerApp() {
    val context = LocalContext.current
    var isDarkMode by remember { mutableStateOf(SessionManager.isDarkMode()) }
    val navController = rememberNavController()
    val startDestination = if (SessionManager.getToken().isNullOrBlank()) "auth" else "main"

    Task_ManagerAPPTheme(darkTheme = isDarkMode) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("auth") {
                val authViewModel: AuthViewModel = viewModel()
                AuthScreen(
                    viewModel = authViewModel,
                    onAuthSuccess = {
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }
            composable("main") {
                MainScreen(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { dark ->
                        isDarkMode = dark
                        SessionManager.setDarkMode(context, dark)
                    },
                    onLogout = {
                        SessionManager.clearToken(context)
                        navController.navigate("auth") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}