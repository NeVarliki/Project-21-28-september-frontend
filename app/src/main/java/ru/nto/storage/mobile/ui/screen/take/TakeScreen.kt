package ru.nto.storage.mobile.ui.screen.take

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.nto.storage.mobile.R
import ru.nto.storage.mobile.core.TestIds
import ru.nto.storage.mobile.ui.screen.main.MainResult
import java.time.LocalDate

@Composable
fun TakeScreen(
    viewModel: TakeViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is TakeAction.Back -> navController.popBackStack()
                is TakeAction.BackWithSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(MainResult.REFRESH_KEY, true)
                    navController.popBackStack()
                }
            }
        }
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.testTag(TestIds.Take.BACK_BUTTON),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.take_back)
                )
            }
            Text(
                text = stringResource(R.string.take_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        when (val currentState = state) {
            is TakeState.Data -> ContentState(viewModel, currentState)
            is TakeState.Error -> ErrorState(viewModel, currentState)
            is TakeState.Loading -> LoadingState()
            is TakeState.Empty -> EmptyState()
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.testTag(TestIds.Take.EMPTY),
            text = stringResource(R.string.take_empty),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
        )
    }
}

@Composable
private fun ErrorState(
    viewModel: TakeViewModel,
    state: TakeState.Error
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag(TestIds.Take.ERROR),
            text = state.error,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.testTag(TestIds.Take.REFRESH_BUTTON).fillMaxWidth(),
            onClick = {
                viewModel.onIntent(TakeIntent.Refresh)
            },
        ) {
            Text(stringResource(R.string.main_refresh))
        }
    }
}

@Composable
private fun ContentState(
    viewModel: TakeViewModel,
    state: TakeState.Data
) {
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedEquipmentId by remember {
        mutableStateOf<Long?>(null)
    }
    var returnDate by remember {
        mutableStateOf(LocalDate.now().plusDays(7).toString())
    }
    Box {
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
            ) {
                state.groups.forEachIndexed { index, group ->
                    Tab(
                        modifier = Modifier
                            .testTag(TestIds.Take.getIdCategoryItemByPosition(index)),
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            selectedEquipmentId = null
                        },
                        text = {
                            Text(
                                modifier = Modifier.testTag(TestIds.Take.ITEM_CATEGORY),
                                text = group.category,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            TextField(
                modifier = Modifier
                    .testTag(TestIds.Take.DATE_INPUT)
                    .fillMaxWidth()
                    .padding(16.dp),
                value = returnDate,
                onValueChange = { returnDate = it },
                label = { Text(stringResource(R.string.take_return_date)) },
                singleLine = true,
            )
            val group = state.groups[selectedTab]
            Column(
                Modifier
                    .selectableGroup()
                    .verticalScroll(rememberScrollState())
            ) {
                group.items.forEachIndexed { index, item ->
                    val isSelected = item.id == selectedEquipmentId
                    Row(
                        Modifier
                            .testTag(TestIds.Take.getIdEquipmentItemByPosition(index))
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = isSelected,
                                onClick = {
                                    selectedEquipmentId = item.id
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.testTag(TestIds.Take.ITEM_EQUIPMENT_SELECTOR),
                            selected = isSelected,
                            onClick = null
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                modifier = Modifier.testTag(TestIds.Take.ITEM_EQUIPMENT_TEXT),
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = item.inventoryCode,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(all = 24.dp)
                .align(Alignment.BottomEnd),
        ) {
            FloatingActionButton(
                modifier = Modifier.testTag(TestIds.Take.TAKE_BUTTON),
                onClick = {
                    val id = selectedEquipmentId
                    if (id != null) {
                        viewModel.onIntent(
                            TakeIntent.Take(
                                equipmentId = id,
                                returnDate = returnDate
                            )
                        )
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.take_confirm)
                )
            }
        }
    }
}
