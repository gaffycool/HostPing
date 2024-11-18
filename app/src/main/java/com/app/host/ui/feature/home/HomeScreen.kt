package com.app.host.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.host.R
import com.app.host.ui.Screen
import com.app.host.ui.component.HostItemView
import com.app.host.ui.theme.space4
import com.app.host.ui.theme.space6

/**
 * homeScreen extension is used to handle viewmodel and viewstate related operations at single page
 * so we can do preview for pure composable view
 */
fun NavGraphBuilder.homeScreen() {
    composable(Screen.Home.routeName) {
        val viewModel: HomeViewModel = hiltViewModel()
        val vmState by viewModel.uiState.collectAsStateWithLifecycle()
        HomeView(
            vmState = vmState,
            onRefresh = viewModel::onRefresh,
            sortClick = viewModel::sortClick,
            retryLatencyCheckClick = viewModel::retryLatencyCheckClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun HomeView(
    @PreviewParameter(HomeViewParameterProvider::class) vmState: HomeViewState,
    onRefresh: () -> Unit = {},
    sortClick: () -> Unit = {},
    retryLatencyCheckClick: (HostUIModel) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = sortClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_list),
                            contentDescription = ""
                        )
                    }
                }
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = space4)
                .pullToRefresh(
                    isRefreshing = vmState.isRefreshing,
                    state = rememberPullToRefreshState(),
                    onRefresh = onRefresh
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(space4)) {
                itemsIndexed(items = vmState.hosts) { _, host ->
                    HostItemView(model = host, retryClick = retryLatencyCheckClick)
                }
            }
            if (vmState.isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(space6)
                        .padding(top = space4),
                    strokeWidth = 2.dp
                )
            }
        }
    }
    val snackBarMessage = if (vmState.isSorted)
        stringResource(R.string.sorted_by_highest_latency)
    else
        stringResource(R.string.sorted_by_lowest_latency)
    LaunchedEffect(vmState.showSnackBarMessage) {
        if (vmState.showSnackBarMessage) {
            snackbarHostState.showSnackbar(snackBarMessage)
        }
    }
}

class HomeViewParameterProvider : PreviewParameterProvider<HomeViewState> {
    override val values = sequenceOf(
        HomeViewState()
    )
}
