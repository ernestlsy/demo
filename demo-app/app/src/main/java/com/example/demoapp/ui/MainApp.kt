package com.example.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.ui.screens.input.InputScreen
import com.example.demoapp.ui.screens.input.InputViewModel
import com.example.demoapp.ui.screens.output.OutputScreen
import com.example.demoapp.ui.screens.output.OutputViewModel
import com.example.demoapp.ui.theme.FooterBar
import com.example.demoapp.ui.theme.Pink40

enum class Screen(val title: String) {
    Input(title = "Input Text"),
    Output(title = "Summary")
}

@Composable
fun MainAppTopBar(
    currentScreen: Screen,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = currentScreen.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Pink40,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun MainAppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        containerColor = FooterBar,
        tonalElevation = 4.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screen.Input.name) }
            ) {
                Text("Input")
            }
            Button(
                onClick = { navController.navigate(Screen.Output.name) }
            ) {
                Text("Summary")
            }
        }
    }
}

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Input.name
    )

    Scaffold(
        topBar = {
            MainAppTopBar(
                currentScreen = currentScreen
            )
        },
        bottomBar = {
            MainAppBottomBar(
                navController = navController,
                modifier = Modifier
            )
        }
    ) { innerPadding ->
        val inputVM: InputViewModel = viewModel(factory = InputViewModel.Factory)
        val outputVM: OutputViewModel = viewModel(factory = OutputViewModel.Factory)

        NavHost(
            navController = navController,
            startDestination = Screen.Input.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = Screen.Input.name) {
                InputScreen(
                    viewModel = inputVM,
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = Screen.Output.name) {
                OutputScreen(
                    viewModel = outputVM,
                    navController = navController,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}