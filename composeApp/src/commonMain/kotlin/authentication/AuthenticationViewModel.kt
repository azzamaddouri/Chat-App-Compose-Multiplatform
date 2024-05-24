package authentication

import firebase.AuthResponse
import firebase.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import onCompletion

class AuthenticationViewModel {

    fun validateEmail(email: String): Boolean {
        if (email == "") return false
        val emailRegex = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
        return emailRegex.matches(email)
    }

    private val API_KEY: String = "AIzaSyAoMlUjpPAkVjZLhoOI8phPhFPsa5DsOnU"

    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun signUpWithEmailAndPassword(
        email: String, password: String, onCompletion: onCompletion<AuthResponse>
    ) {
        val responseBody = httpClient
            .post("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=${API_KEY}") {
                header("Content-Type", "application/json")
                parameter("email", email)
                parameter("password", password)
                parameter("returnSecureToken", true)
            }
        if (responseBody.status.value in 200..299) {
            val response = Json { ignoreUnknownKeys = true }
                .decodeFromString<AuthResponse>(responseBody.bodyAsText())
            onCompletion.onSuccess(response)
        } else {
            onCompletion.onError(Exception(responseBody.bodyAsText()))
        }
    }

    suspend fun loginWithAuthResponse(
        email: String, password: String, onCompletion: onCompletion<AuthResponse>
    ) {
        val responseBody = httpClient
            .post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=${API_KEY}") {
                header("Content-Type", "application/json")
                parameter("email", email)
                parameter("password", password)
                parameter("returnSecureToken", true)
            }
        if (responseBody.status.value in 200..299) {
            val response = Json { ignoreUnknownKeys = true }
                .decodeFromString<AuthResponse>(responseBody.bodyAsText())
            onCompletion.onSuccess(response)
        } else {
            onCompletion.onError(Exception(responseBody.bodyAsText()))
        }
    }

    suspend fun getRefreshToken(
        refreshToken: String?, onCompletion: onCompletion<TokenResponse>
    ) {
        val responseBody = httpClient
            .post("https://securetoken.googleapis.com/v1/token?key=${API_KEY}") {
                header("Content-Type", "application/json")
                parameter("grant_type", "refresh_token")
                parameter("refresh_token", refreshToken)
            }
        if (responseBody.status.value in 200..299) {
            val response = Json { ignoreUnknownKeys = true }
                .decodeFromString<TokenResponse>(responseBody.bodyAsText())
            onCompletion.onSuccess(response)
        } else {
            onCompletion.onError(Exception(responseBody.bodyAsText()))
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        completion: onCompletion<String>
    ) {
        if (password == confirmPassword) {
            this.signUpWithEmailAndPassword(email, password, object : onCompletion<AuthResponse> {
                override fun onSuccess(T: AuthResponse) {
                    completion.onSuccess(T.idToken)
                }

                override fun onError(e: Exception) {
                    completion.onError(e)
                }
            })
        } else {
            completion.onError(Exception("Password doesn't match"))
        }
    }

    suspend fun loginWithString(
        email: String,
        password: String,
        completion: onCompletion<String>
    ) {
        this.loginWithAuthResponse(email, password, object : onCompletion<AuthResponse> {
            override fun onSuccess(T: AuthResponse) {
                completion.onSuccess(T.idToken)
            }

            override fun onError(e: Exception) {
                completion.onError(e)
            }
        })
    }
}
