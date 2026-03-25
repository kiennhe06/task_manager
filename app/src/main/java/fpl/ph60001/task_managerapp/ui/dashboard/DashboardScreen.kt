package fpl.ph60001.task_managerapp.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpl.ph60001.task_managerapp.R
import fpl.ph60001.task_managerapp.ui.theme.CardDone
import fpl.ph60001.task_managerapp.ui.theme.CardInProgress
import fpl.ph60001.task_managerapp.ui.theme.CardOverdue
import fpl.ph60001.task_managerapp.ui.theme.CardTodo
import fpl.ph60001.task_managerapp.ui.theme.CardTotal
import fpl.ph60001.task_managerapp.ui.theme.GradientEnd
import fpl.ph60001.task_managerapp.ui.theme.GradientStart

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Modern Header with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                .padding(24.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Xin chào!",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.title_dashboard),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .offset(y = (-40).dp) // Overlap with header
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GradientStart)
                }
            }

            state.error?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = msg, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.weight(1f))
                        TextButton(onClick = { viewModel.loadStats() }) {
                            Text(stringResource(R.string.btn_reload))
                        }
                    }
                }
            }

            if (!state.isLoading && state.error == null) {
                val stats = state.stats
                
                // Highlight Card
                StatCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    title = stringResource(R.string.stat_total),
                    count = stats.total,
                    icon = Icons.Filled.Assignment,
                    color = CardTotal,
                    isLarge = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_todo),
                        count = stats.todo,
                        icon = Icons.Filled.ListAlt,
                        color = CardTodo
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_in_progress),
                        count = stats.inProgress,
                        icon = Icons.Filled.Schedule,
                        color = CardInProgress
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_done),
                        count = stats.done,
                        icon = Icons.Filled.CheckCircle,
                        color = CardDone
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.stat_overdue),
                        count = stats.overdue,
                        icon = Icons.Filled.Warning,
                        color = CardOverdue
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    isLarge: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isLarge) Arrangement.Start else Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (isLarge) 56.dp else 48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(if (isLarge) 32.dp else 24.dp)
                )
            }
            
            if (isLarge) {
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$count công việc",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = count.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

