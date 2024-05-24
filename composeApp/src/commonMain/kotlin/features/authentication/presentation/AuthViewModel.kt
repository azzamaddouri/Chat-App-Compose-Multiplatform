package features.authentication.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import features.authentication.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository ) :  ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun onEvent(event: AuthEvent){
        when (event){
           is AuthEvent.SignUp-> signUp(event)
            is AuthEvent.SignIn -> signIn(event)
        }
    }

    private fun signUp(event:AuthEvent.SignUp){
        if (_state.value.isLoading) return
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            authRepository.
            signUp(event.email,event.password,event.confirmPassword)
                .onSuccess {response ->
                    _state.update {
                        it.copy(isLoading = false, isSuccess = true,token=response.idToken)
                    }
                }
                .onFailure {e ->
                    _state.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                }
        }
    }

    private fun signIn(event:AuthEvent.SignIn){
        if (_state.value.isLoading) return
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            authRepository.
            signIn(event.email,event.password)
                .onSuccess {response ->
                    _state.update {
                        it.copy(isLoading = false, isSuccess = true,token=response.idToken)
                    }
                }
                .onFailure {e ->
                    _state.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                }
        }
    }
}
