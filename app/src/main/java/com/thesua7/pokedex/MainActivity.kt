package com.thesua7.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thesua7.pokedex.core.resources.SnackBarManager
import com.thesua7.pokedex.core.resources.TransitionType
import com.thesua7.pokedex.core.resources.transitionIn
import com.thesua7.pokedex.core.resources.transitionOut
import com.thesua7.pokedex.core.splash.SplashScreen
import com.thesua7.pokedex.features.pokemonDetail.presentation.page.PokemonDetailScreen
import com.thesua7.pokedex.features.pokemonList.presentation.page.PokemonListScreen
import com.thesua7.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var snackBarManager: SnackBarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokeDexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash_screen",
                ) {
                    composable("splash_screen") {
                        SplashScreen(onNavigateToAuth = null, onNavigateToHome = {
                            navController.navigate("pokemon_list_screen") {
                                popUpTo("splash_screen") { inclusive = true }
                            }
                        })
                    }


                    composable("pokemon_list_screen",
                        enterTransition = { transitionIn(TransitionType.Slide, 500, 500) },
                        exitTransition = { transitionOut(TransitionType.Slide, 500, 500) }) {
                        PokemonListScreen(
                            navController = navController, snackBarManager = snackBarManager
                        )

                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                        enterTransition = { transitionIn(TransitionType.Slide, 500, 500) },
                        exitTransition = { transitionOut(TransitionType.Slide, 500, 500) },
                        arguments = listOf(navArgument("dominantColor") {
                            type = NavType.IntType
                        }, navArgument("pokemonName") {
                            type = NavType.StringType
                        })
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) ?: Color.White }
                        }

                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }

                        PokemonDetailScreen(
                            navController = navController,
                            dominantColor = dominantColor ?: Color.Transparent,
                            pokemonName = pokemonName?.toLowerCase(Locale.ROOT) ?: ""
                        )

                    }
                }
            }
        }
    }
}

