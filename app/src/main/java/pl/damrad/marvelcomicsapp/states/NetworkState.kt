package pl.damrad.marvelcomicsapp.states

sealed class NetworkState {
    object Connected: NetworkState()
    object Disconnected: NetworkState()
    object Timeout: NetworkState()
    object Warning : NetworkState()
}