package com.kotinha

import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kotinha.repo.Repository
import com.kotinha.ui.TicketDialog
import com.kotinha.ui.nav.BottomNavBar
import com.kotinha.ui.nav.BottomNavItem
import com.kotinha.ui.nav.MainNavHost
import com.kotinha.ui.theme.KotinhaTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel : MainViewModel by viewModels()
        setContent {
            if (!viewModel.loggedIn) {
                this.finish()
            }
            val navController = rememberNavController()
            val showDialog = remember { mutableStateOf(false) }
            val context = LocalContext.current
            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.route != BottomNavItem.UserPageKt.route
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {} )
            val repo = remember { Repository (viewModel) }
            val modifier = Modifier
            KotinhaTheme {
                if (showDialog.value) TicketDialog(
                    onDismiss = { showDialog.value = false },
                    onConfirm = { ticket, uri ->
                        if (ticket.local.isNotBlank()) repo.addTicket(ticket, uri)
                        showDialog.value = false
                    })
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a ${viewModel.user.name}") },
                            actions = {
                                IconButton( onClick = {
                                    Firebase.auth.signOut()
                                }
                                ) {
                                    Icon( imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    },
                    floatingActionButton = {
                        if (showButton){
                            FloatingActionButton(onClick = { showDialog.value = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) {
                        innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        MainNavHost(
                            navController = navController,
                            viewModel = viewModel,
                            repository = repo,
                            modifier = modifier,
                            context = context
                        )
                    }
                }
            }
        }
    }
}