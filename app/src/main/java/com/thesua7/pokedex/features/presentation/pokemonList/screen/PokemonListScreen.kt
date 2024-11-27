package com.thesua7.pokedex.features.presentation.pokemonList.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thesua7.pokedex.R
import com.thesua7.pokedex.core.resources.CustomSnackBarHost
import com.thesua7.pokedex.core.resources.SnackBarManager
import com.thesua7.pokedex.core.resources.SnackBarType
import com.thesua7.pokedex.core.resources.showSnackBar
import com.thesua7.pokedex.features.domain.model.Pokemon
import com.thesua7.pokedex.features.presentation.pokemonList.event.PokemonListEvent
import com.thesua7.pokedex.features.presentation.pokemonList.viewModel.PokemonViewModel
import com.thesua7.pokedex.features.utils.PicassoImageView
import com.thesua7.pokedex.ui.theme.RobotoCondensed
import kotlinx.coroutines.delay


@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel(),
    snackBarManager: SnackBarManager
) {
    val state by viewModel.uiState.collectAsState()

    // State to track swipe gesture
    var isRefreshing by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }

    // Simulate a refresh action
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(2000) // Simulate network delay
            viewModel.refreshData()  // Trigger your refresh action (i.e., reload data)
            swipeOffset = 0f
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        snackbarHost = { CustomSnackBarHost(snackBarManager) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    swipeOffset += dragAmount

                    if (swipeOffset > 200f) {
                        if (!isRefreshing) {
                            isRefreshing = true
                        }
                    }
                }
            }
        ) {
            // Pull-to-refresh indicator
            if (swipeOffset > 0f) {
                LinearProgressIndicator(
                    progress = swipeOffset / 300f,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logo image
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )

            // Search bar UI
            SearchBar(
                hint = "Search..",
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Handle loading, error, or data states
            when {
                state.isLoading && !isRefreshing -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                state.error != null -> {
                    snackBarManager.showSnackBar(state.error?.message ?: "Unknown Error", SnackBarType.ERROR)
                }
                else -> {
                    PokemonList(
                        pokemonList = state.data,
                        navController = navController,
                        snackBarManager = snackBarManager,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemonList: List<Pokemon>,
    navController: NavController,
    snackBarManager: SnackBarManager,
    viewModel: PokemonViewModel
) {
    val endReached by viewModel.endReached
    val isLoading = viewModel.uiState.collectAsState().value.isLoading
    viewModel.uiState.collectAsState().value.error

    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState, contentPadding = PaddingValues(16.dp)
    ) {
        val itemCount = if (pokemonList.size % 2 == 0) pokemonList.size / 2 else pokemonList.size / 2 + 1

        items(count = itemCount, key = { index ->
            "${pokemonList.getOrNull(index * 2)?.pokemonId ?: ""}:${pokemonList.getOrNull(index * 2 + 1)?.pokemonId ?: ""}"
        }) { rowIndex ->
            val shouldLoadMore = remember(rowIndex, itemCount) {
                rowIndex >= itemCount - 2 && !endReached && !isLoading
            }

            LaunchedEffect(shouldLoadMore) {
                if (shouldLoadMore) {
                    snackBarManager.showSnackBar("Loading more data...", type = SnackBarType.SUCCESS)
                    viewModel.onEvent(PokemonListEvent.LoadPokemonList)
                }
            }

            PokeDexRow(
                rowIndex = rowIndex,
                items = pokemonList,
                navController = navController
            )
        }
    }

    if (isLoading && pokemonList.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }


    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String, onSearch: (String) -> Unit = {}) {
    // Local state to manage the search text and hint visibility
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it) // Call the search handler
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    // Show the hint text when the field is not focused and empty
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )

        // Display hint text when the input field is empty
        if (isHintDisplayed) {
            Text(
                text = hint,
                textAlign = TextAlign.Center,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}



@Composable
fun PokeDexItem(
    navController: NavController,
    modifier: Modifier = Modifier,
    pokemon: Pokemon,

) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    val dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                // Navigate to the Pokémon detail screen
                navController.navigate("pokemon_detail_screen/${dominantColor.toArgb()}/${pokemon.pokemonName}")
            }
    ) {
        Column {
            // Display Pokémon image
            PicassoImageView(
                url = pokemon.imageUrl,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Display Pokémon name
            Text(
                text = pokemon.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PokeDexRow(
    rowIndex: Int,
    items: List<Pokemon>,
    navController: NavController,
) {
    Column {
        Row {
            // Display the first Pokémon item in the row
            PokeDexItem(
                pokemon = items[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Display the second Pokémon item in the row (if available)
            if (items.size >= rowIndex * 2 + 2) {
                PokeDexItem(
                    pokemon = items[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                )
            } else {
                // Add space if there is no second Pokémon to show
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add space after each row
    }
}
