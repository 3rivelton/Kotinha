package com.kotinha.ui.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kotinha.MainViewModel
import com.kotinha.repo.Repository
import com.kotinha.ui.HomePage
import com.kotinha.ui.ListPage
import com.kotinha.ui.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel : MainViewModel,
    repository : Repository,
    modifier: Modifier,
    context: Context
) {
    NavHost(navController, startDestination = BottomNavItem.HomePageKt.route) {
    // composable (route = NOME DESTA DESTINAÇÃO) { UI DA DESTINAÇÃO }
        composable(route = BottomNavItem.HomePageKt.route) {
            HomePage(viewModel, modifier)
        }
        composable(route = BottomNavItem.ListPageKt.route) {
            ListPage(viewModel, repository, context, navController)
        }
        composable(route = BottomNavItem.MapPageKt.route) {
            MapPage(viewModel, repository, context)
        }
    }
}