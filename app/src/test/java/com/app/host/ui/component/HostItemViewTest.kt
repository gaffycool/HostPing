package com.app.host.ui.component

import app.cash.paparazzi.Paparazzi
import com.app.host.ui.feature.home.HostUIModel
import com.app.host.ui.feature.home.PingStatus
import org.junit.Rule
import org.junit.Test

class HostItemViewTest {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            HostItemView(
                model = HostUIModel(
                    icon = "icon",
                    name = "name",
                    url = "url",
                    pingStatus = PingStatus.InProcess
                ),
                retryClick = {}
            )
        }
    }
}
