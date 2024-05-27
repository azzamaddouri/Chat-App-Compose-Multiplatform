package features.tabs
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import features.tabs.domain.UserRepository
import features.tabs.presentation.event.LogoutEvent
import features.tabs.presentation.state.LogoutState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TabsViewModel(private val userRepository: UserRepository) : ViewModel() {
   private val _logoutState = MutableStateFlow(LogoutState())
    val logoutState = _logoutState.asStateFlow()
    fun onLogoutEvent(event : LogoutEvent){
        when (event){
            is LogoutEvent.logout -> logout()
        }
    }

    private fun logout() {
        if (_logoutState.value.isLoading) return
        _logoutState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            userRepository.
            logout()
                .onSuccess {response ->
                    _logoutState.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                }
                .onFailure {e ->
                    _logoutState.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                }
        }
    }
}