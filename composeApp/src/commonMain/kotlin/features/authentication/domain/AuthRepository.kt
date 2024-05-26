package features.authentication.domain

import comchatappfirebaseauthentication.User


interface AuthRepository {
    suspend fun signUp(email: String,password: String,confirmPassword: String) : Result<AuthResponse>
    suspend fun signIn(email: String,password: String) : Result<AuthResponse>
    suspend fun checkSession() : Result<User>

}