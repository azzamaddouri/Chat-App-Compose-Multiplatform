package features.authentication.data.repository

import comchatappfirebaseauthentication.User
import features.authentication.data.firebase.FirebaseAuth
import features.authentication.data.sqldelight.SqlDelight
import features.authentication.domain.AuthRepository
import features.authentication.domain.AuthResponse

class AuthRepositoryImp(private val firebaseAuth:FirebaseAuth,private val sqlDelight: SqlDelight) : AuthRepository{
    override suspend fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<AuthResponse> {
        return runCatching {
            if (password == confirmPassword) {
                firebaseAuth.signUp(email, password).let { authResponse ->
                    sqlDelight.storeUserDetails(authResponse)
                    authResponse
                }
            } else {
                throw IllegalArgumentException("Passwords do not match")
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            firebaseAuth.signIn(email, password).let { authResponse ->
                sqlDelight.storeUserDetails(authResponse)
                val users = sqlDelight.getAllUsers()
                print("Users are : ${users}")
                authResponse
            }
        }
    }

    override suspend fun checkSession(): Result<User> {
        try {
            // Implement your session checking logic here
            val users = sqlDelight.getAllUsers()
            for (user in users) {
                if (user != null) {
                    val tokenResponse = firebaseAuth.getRefreshToken(user.refreshToken)

                    return Result.success(
                        User(
                            idToken = tokenResponse.id_token,
                            email = user.email,
                            localId = tokenResponse.user_id,
                            refreshToken = tokenResponse.refresh_token,
                            name = user.email
                        )
                    )
                }
            }
            throw NoSuchElementException("No valid session found")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}
