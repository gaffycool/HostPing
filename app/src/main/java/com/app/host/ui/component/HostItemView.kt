package com.app.host.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.host.R
import com.app.host.ui.feature.home.HostUIModel
import com.app.host.ui.feature.home.PingStatus
import com.app.host.ui.theme.Green
import com.app.host.ui.theme.space2
import com.app.host.ui.theme.space4
import com.app.host.ui.theme.space6

/**
 * HostItemView is UI representation of host with its name, icon, url and its ping status
 */
@Composable
fun HostItemView(model: HostUIModel, retryClick: (HostUIModel) -> Unit) = Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    modifier = Modifier.clickable { retryClick(model) }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = model.icon,
                contentDescription = "",
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.width(space4))
            Column(Modifier.weight(1f)) {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = model.url,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(space2))
                Text(
                    text = stringResource(R.string.click_to_recheck_latency),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            when (model.pingStatus) {
                is PingStatus.Done -> Text(
                    text = stringResource(R.string.latency_millis, model.pingStatus.latency),
                    style = MaterialTheme.typography.titleSmall,
                    color = Green
                )

                PingStatus.Failed -> Text(
                    text = stringResource(R.string.failed),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )

                PingStatus.InProcess -> CircularProgressIndicator(modifier = Modifier.size(space6))
            }
        }
    }
}

@Preview
@Composable
fun BreedItemPreview() {
    HostItemView(
        HostUIModel(
            name = "eBay UK",
            url = "www.ebay.co.uk",
            icon = "https://pages.ebay.com/favicon.ico",
        ),
        retryClick = {}
    )
}
