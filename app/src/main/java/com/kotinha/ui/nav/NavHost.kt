package com.kotinha.ui.nav

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kotinha.MainViewModel
import com.kotinha.repo.Repository
import com.kotinha.ui.HomePage
import com.kotinha.ui.ListPage
import com.kotinha.ui.TicketPage
import com.kotinha.ui.UserPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    repository: Repository,
    modifier: Modifier,
    context: Context
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    Column(modifier = modifier) {
        NavHost(navController, startDestination = BottomNavItem.HomePageKt.route) {
            composable(route = BottomNavItem.HomePageKt.route) {
                HomePage(viewModel, modifier)
            }
            composable(route = BottomNavItem.ListPageKt.route) {
                ListPage(viewModel, repository, context, navController)
            }
            composable(route = BottomNavItem.UserPageKt.route) {
                UserPage(viewModel, context)
            }
            composable("ticket_page/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")
                ticketId?.let {
                    val ticket = viewModel.getTicketById(it)
                    if (ticket != null) {
                        TicketPage(ticket)
                    }
                }
            }
        }
        if (currentRoute != "ticket_page/{ticketId}") {
            BottomNavBar(navController)
        }
    }
}