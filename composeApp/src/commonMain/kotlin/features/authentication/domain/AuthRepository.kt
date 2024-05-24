package features.authentication.domain


interface AuthRepository {
    suspend fun signUp(email: String,password: String,confirmPassword: String) : Result<AuthResponse>
    suspend fun signIn(email: String,password: String) : Result<AuthResponse>
    suspend fun getRefreshToken(refreshToken:String?) : Result<TokenResponse>

}