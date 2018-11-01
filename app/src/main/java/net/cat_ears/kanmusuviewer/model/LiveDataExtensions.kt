package net.cat_ears.kanmusuviewer.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T, R> MutableLiveData<T>.map(converter: (T) -> R): MutableLiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) { result.postValue(converter(it!!)) }
    return result
}

fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}
