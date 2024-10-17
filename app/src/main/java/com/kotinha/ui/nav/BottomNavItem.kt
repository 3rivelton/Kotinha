package com.kotinha.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon:ImageVector, var route: String) {
    data object HomePageKt : BottomNavItem("Início",Icons.Default.Home, "home")
    data object ListPageKt : BottomNavItem("Compras",Icons.Default.ShoppingCart, "list")
    data object UserPageKt : BottomNavItem("Perfil",Icons.Default.AccountBox, "map")
}