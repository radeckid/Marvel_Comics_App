package pl.damrad.marvelcomicsapp.other

import android.os.Bundle

sealed class UIState {
    object Initialized : UIState()
    object Refreshing : UIState()
    object InProgress : UIState()
    object Success : UIState()
    object Error : UIState()
    object Warning : UIState()
    data class ErrorResponse(val text: String? = null) : UIState()
    data class NavigateTo(val key: String?, val bundle: Bundle? = null) : UIState()
}
