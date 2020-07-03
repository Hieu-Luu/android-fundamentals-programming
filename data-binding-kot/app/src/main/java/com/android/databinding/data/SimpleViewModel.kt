package com.android.databinding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class SimpleViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _likes =  MutableLiveData<Int>()

    val name: LiveData<String> = _name
    val lastName : LiveData<String> = _lastName
    val likes : LiveData<Int> = _likes

    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }

    /**
     *  Make LiveData popularity depend on _likes using Transformations
    */
    val popularity : LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}