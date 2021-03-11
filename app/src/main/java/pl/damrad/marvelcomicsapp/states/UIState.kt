package pl.damrad.marvelcomicsapp.states

sealed class UIState {
    object Success : UIState()
    object Error : UIState()
    object Warning : UIState()
    data class ErrorResponse(val text: String? = null) : UIState()
}
