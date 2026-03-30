package fpl.ph60001.task_managerapp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fpl.ph60001.task_managerapp.R
import fpl.ph60001.task_managerapp.ui.dashboard.DashboardScreen
import fpl.ph60001.task_managerapp.ui.dashboard.DashboardViewModel
import fpl.ph60001.task_managerapp.ui.profile.ProfileScreen
import fpl.ph60001.task_managerapp.ui.profile.ProfileViewModel
import fpl.ph60001.task_managerapp.ui.task.TaskScreen
import fpl.ph60001.task_managerapp.ui.task.TaskViewModel
import fpl.ph60001.task_managerapp.ui.theme.GradientStart

@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    val dashboardViewModel: DashboardViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val tabs = listOf(
        BottomNavItem(stringResource(R.string.tab_dashboard), Icons.Filled.Dashboard),
        BottomNavItem(stringResource(R.string.tab_tasks), Icons.Filled.ListAlt),
        BottomNavItem(null, Icons.Filled.Add), // Placeholder for FAB space
        BottomNavItem("Thông báo", Icons.Filled.Notifications), // Added to make 5 items total for symmetry
        BottomNavItem(stringResource(R.string.tab_profile), Icons.Filled.Person)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GradientStart,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .size(64.dp)
                    .offset(y = 50.dp) // Proper alignment with navigation bar
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Thêm mới", modifier = Modifier.size(32.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.height(80.dp)
            ) {
                tabs.forEachIndexed { index, item ->
                    if (index == 2) {
                        // Spacer for FAB
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                when (index) {
                                    0 -> dashboardViewModel.loadStats()
                                    1 -> taskViewModel.loadTasks()
                                    4 -> profileViewModel.loadProfile()
                                }
                            },
                            icon = { 
                                item.icon?.let { 
                                    Icon(it, contentDescription = item.label) 
                                } 
                            },
                            label = { 
                                item.label?.let { 
                                    Text(it, style = MaterialTheme.typography.labelSmall) 
                                } 
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (selectedTab) {
            0 -> DashboardScreen(viewModel = dashboardViewModel)
            1 -> TaskScreen(viewModel = taskViewModel)
            3 -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
                Text("Tính năng đang phát triển", color = Color.Gray) 
            }
            4 -> ProfileScreen(
                viewModel = profileViewModel,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onLogout = onLogout
            )
        }
    }

    if (showAddDialog) {
        fpl.ph60001.task_managerapp.ui.task.AddFileOrTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, desc, priority, dueDate ->
                taskViewModel.createTask(title, desc, priority, dueDate)
                showAddDialog = false
                // If we're on dashboard, refresh stats
                if (selectedTab == 0) dashboardViewModel.loadStats()
            }
        )
    }
}

// Updated BottomNavItem to support null labels/icons for the spacer
data class BottomNavItem(
    val label: String?,
    val icon: ImageVector?
)
