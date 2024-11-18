package com.app.host.ui

sealed class Screen(val routeName: String) {
    data object Home : Screen("Home")
}