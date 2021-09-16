package com.app.xarehub.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by PS Wang on 2021/8/4
 */
abstract class BaseViewModel : ViewModel() {

    private val _errorMsg = MutableStateFlow("")
    val errorMsg: StateFlow<String> = _errorMsg

    fun cleanErrorMsg() {
        _errorMsg.value = ""
    }

    protected fun setErrorMsg(msg: String) {
        _errorMsg.value = msg
    }

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    protected fun startLoading() {
        _loading.value = true
    }

    protected fun loadingDone() {
        _loading.value = false
    }
}