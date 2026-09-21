package ru.nto.storage.mobile.ui.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nto.storage.mobile.data.repo.AuthRepository
import ru.nto.storage.mobile.domain.auth.GetCodeUseCase
import ru.nto.storage.mobile.ui.nav.AppDestination
import ru.nto.storage.mobile.ui.nav.AuthScreenDestination
import ru.nto.storage.mobile.ui.nav.MainScreenDestination
import ru.nto.storage.mobile.ui.nav.TakeScreenDestination
import ru.nto.storage.mobile.ui.screen.auth.AuthScreen
import ru.nto.storage.mobile.ui.screen.main.MainScreen
import ru.nto.storage.mobile.ui.screen.take.TakeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var destination by remember { mutableStateOf<AppDestination?>(null) }
    LaunchedEffect(Unit) {
        val code = GetCodeUseCase(AuthRepository).invoke()
        destination = if (code == null) {
            AuthScreenDestination
        } else {
            MainScreenDestination
        }
    }
    if (destination != null) {
        NavHost(
            modifier = modifier,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            navController = navController,
            startDestination = destination as AppDestination,
        ) {
            composable<AuthScreenDestination> {
                AuthScreen(navController = navController)
            }
            composable<MainScreenDestination> {
                MainScreen(navController = navController)
            }
            composable<TakeScreenDestination> {
                TakeScreen(navController = navController)
            }
        }
    }
}
