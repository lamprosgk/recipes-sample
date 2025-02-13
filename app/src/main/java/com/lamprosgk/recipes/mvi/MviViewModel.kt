package com.lamprosgk.recipes.mvi

import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<M : MviModel, I : MviIntent> {
    val state: StateFlow<M>
    fun onIntent(intent: I)
}