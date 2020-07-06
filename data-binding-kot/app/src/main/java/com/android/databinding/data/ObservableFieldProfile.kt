package com.android.databinding.data

import androidx.databinding.ObservableInt

/**
 * Used as a layout variable to provide static properties (name and lastName) and
 * an observable on (likes)
 * */
data class ObservableFieldProfile(
    val name: String,
    val lastName: String,
    val likes: ObservableInt
)
