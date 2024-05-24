package features.authentication.data.repository

import features.authentication.data.firebase.FirebaseAuth
import features.authentication.domain.AuthRepository
import features.authentication.domain.AuthResponse
import features.authentication.domain.TokenResponse

class AuthRepositoryImp(private val firebaseAuth:FirebaseAuth) : AuthRepository{
    override suspend fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<AuthResponse> {
        return runCatching {
            if (password == confirmPassword) {
                firebaseAuth.signUp(email, password)
            } else {
                throw IllegalArgumentException("Passwords do not match")
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            firebaseAuth.signIn(email, password)
        }
    }

    override suspend fun getRefreshToken(refreshToken: String?): Result<TokenResponse> {
        return runCatching {
            firebaseAuth.getRefreshToken(refreshToken)
        }
    }

}
