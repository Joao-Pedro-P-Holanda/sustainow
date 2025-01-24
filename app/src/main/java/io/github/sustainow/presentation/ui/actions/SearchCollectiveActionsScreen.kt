package io.github.sustainow.presentation.ui.actions

import CreateCollectiveAction
import ViewCollectiveAction
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.components.CollectiveActionCard
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.ui.utils.toLocalDate
import io.github.sustainow.presentation.viewmodel.SearchCollectiveActionsViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCollectiveActionsScreen(navController:NavController, viewModel:SearchCollectiveActionsViewModel,modifier: Modifier = Modifier) {
    val searchText by viewModel.searchText.collectAsState()
    val finished by viewModel.finished.collectAsState()
    val ascendingDate by viewModel.ascendingDate.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    val actions by viewModel.collectiveActions.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val dateRangePickerState = rememberDateRangePickerState()
    var showDate by  remember { mutableStateOf(false)}

    val onRefresh = {
        viewModel.searchCollectiveActions()
    }
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(floatingActionButton = {FloatingActionButton(onClick={navController.navigate(CreateCollectiveAction)}) {
        Icon(Icons.Filled.Add, contentDescription = "Adicionar ação coletiva")
    }}, content =  { innerPadding ->
    Column(modifier=modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally){
       Text(stringResource(id = R.string.collective_actions_search_title),style=MaterialTheme.typography.displaySmall)
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = { viewModel.setSearchText(it) },
                    onSearch = { Log.i("Collective","Executando busca"); viewModel.filterCollectiveActions()},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("Digite o nome de uma ação") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = false,
            onExpandedChange = { },
            content={}
        )
        // filtering
        Row(modifier = modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically){
            Icon(Icons.Default.FilterAlt,contentDescription=null)
            InputChip(
                selected = finished == true,
                onClick = { viewModel.setFinished(if (finished == true) null else true) },
                label = { Text("Finalizada") }
            )

            FilterChip(
                selected = startDate != null || endDate != null,
                onClick = { showDate = !showDate },
                label = {
                    // if the dates are not null show them
                    if(startDate != null && endDate != null) {
                        Text("${DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startDate?.toJavaLocalDate())}. - ${
                            DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endDate?.toJavaLocalDate())}")
                    }
                    else if(startDate != null){
                        Text("A partir de ${DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startDate?.toJavaLocalDate())}")
                    }
                    else if(endDate != null){
                        Text("Até ${DateTimeFormatter.ofPattern("dd/MM/yyyy").format(endDate?.toJavaLocalDate())}")
                    }
                    else{
                        Text("Data")
                    }
                }
            )

            if(showDate)
            DatePickerDialog(onDismissRequest = {showDate=false}, confirmButton = {
                TextButton(
                    onClick = {
                        dateRangePickerState.selectedStartDateMillis?.let { it -> viewModel.setStartDate(it.toLocalDate())}
                        dateRangePickerState.selectedEndDateMillis?.let { it ->  viewModel.setEndDate(it.toLocalDate())}
                        showDate =false
                    }
                            )
                 {
                    Text("OK")
                }
            },
                dismissButton = {
                    TextButton(onClick = {showDate=false}) {
                        Text("Cancelar")
                    }
                }
        ) {
                DateRangePicker(dateRangePickerState, showModeToggle = true)
            }
        }
        // ordering
        Row(modifier.padding(8.dp).fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
             Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null) 
            InputChip(
                selected = ascendingDate,
                onClick = { viewModel.reverseAscendingDate() },
                label = { Text("Data") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
            )
        }


            if(!loading) {
                if (actions.isNullOrEmpty()) {
                    Text("Nenhuma ação encontrada", style = MaterialTheme.typography.headlineSmall)
                }
                else{
                //results
                Text("Resultados: ${actions?.size}", style = MaterialTheme.typography.headlineSmall)
                LazyColumn(
                    modifier.padding(8.dp).pullToRefresh(isRefreshing = loading,onRefresh=onRefresh,state= pullToRefreshState)
                ) {
                    items(actions ?: emptyList()) {
                        CollectiveActionCard(
                            it,
                            { navController.navigate(ViewCollectiveAction(it.id)) })
                    }
                }
                }
            } else {
               LoadingModal()
            }
    }
    })
}