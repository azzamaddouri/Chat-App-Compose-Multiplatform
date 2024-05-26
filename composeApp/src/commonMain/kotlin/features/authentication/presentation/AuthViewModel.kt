package features.authentication.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import features.authentication.domain.AuthRepository
import features.authentication.presentation.event.CheckSessionEvent
import features.authentication.presentation.event.SignInEvent
import features.authentication.presentation.event.SignUpEvent
import features.authentication.presentation.state.CheckSessionState
import features.authentication.presentation.state.SignInState
import features.authentication.presentation.state.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository ) :  ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    private val _signInState = MutableStateFlow(SignInState())
    private val _checkSessionState = MutableStateFlow(CheckSessionState())
    val signUpState = _signUpState.asStateFlow()
    val signInState = _signInState.asStateFlow()
    val checkSessionState = _checkSessionState.asStateFlow()

    fun onSignUpEvent(event: SignUpEvent){
        when (event){
           is SignUpEvent.SignUp-> signUp(event)
        }
    }
    fun onSignInEvent(event: SignInEvent){
        when (event){
            is SignInEvent.SignIn -> signIn(event)
        }
    }
    fun onCheckSessionEvent(event: CheckSessionEvent){
        when (event){
            is CheckSessionEvent.CheckSession -> checkSession(event)
        }
    }
    private fun signUp(event: SignUpEvent.SignUp){
        if (_signUpState.value.isLoading) return
        _signUpState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            authRepository.
            signUp(event.email,event.password,event.confirmPassword)
                .onSuccess {response ->
                    _signUpState.update {
                        it.copy(isLoading = false, isSuccess = true,token=response.idToken)
                    }
                }
                .onFailure {e ->
                    _signUpState.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                }
        }
    }

    private fun signIn(event: SignInEvent.SignIn){
        if (_signInState.value.isLoading) return
        _signInState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            authRepository.
            signIn(event.email,event.password)
                .onSuccess {response ->
                    _signInState.update {
                        it.copy(isLoading = false, isSuccess = true,token=response.idToken)
                    }
                }
                .onFailure {e ->
                    _signInState.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                }
        }
    }

    private fun checkSession(event: CheckSessionEvent.CheckSession) {
        if (_checkSessionState.value.isLoading) return

        _checkSessionState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val response = authRepository.checkSession()

                response.onSuccess { user ->
                    _checkSessionState.update {
                        it.copy(isLoading = false, isSuccess = true, user = user)
                    }
                    println("Session check succeeded: $user")
                }.onFailure { e ->
                    _checkSessionState.update {
                        it.copy(isLoading = false, isFailure = true)
                    }
                    println("Session check failed: ${e.message}")
                }
            } catch (e: Exception) {
                // Handle exceptions if necessary
                println("Session check exception: ${e.message}")
                _checkSessionState.update {
                    it.copy(isLoading = false, isFailure = true)
                }
            }
        }
    }


}
