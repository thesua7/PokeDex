package com.thesua7.pokedex.core.resources

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

// Enum for transition types
enum class TransitionType {
    Slide, // Slide and fade transition
    Expand // Expand and shrink transition
}

// CustomTransitions function that returns pairs of enter and exit transitions
fun transitions(transitionType: TransitionType, enterDuration: Int, exitDuration: Int) =
    when (transitionType) {
        TransitionType.Slide -> {
            // Slide and fade transitions
            Pair(
                { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() }
            )
        }

        TransitionType.Expand -> {
            // Expand and shrink transitions
            Pair(
                { expandIn(animationSpec = tween(durationMillis = enterDuration)) },
                { shrinkOut(animationSpec = tween(durationMillis = exitDuration)) }
            )
        }
    }

// Refactored reusable functions for enter and exit transitions with parameters
fun transitionIn(transitionType: TransitionType, enterDuration: Int, exitDuration: Int) =
    transitions(transitionType, enterDuration, exitDuration).first()

fun transitionOut(transitionType: TransitionType, enterDuration: Int, exitDuration: Int) =
    transitions(transitionType, enterDuration, exitDuration).second()
