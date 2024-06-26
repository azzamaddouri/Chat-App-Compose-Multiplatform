package features.authentication.data.firebase

import features.authentication.domain.AuthResponse
import features.authentication.domain.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FirebaseAuth {
    private val API_KEY: String = Firebase.getAPIKey()

    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun signUp(email: String,password: String) : AuthResponse {
            val responseBody = httpClient
                .post("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=${API_KEY}") {
                    header("Content-Type", "application/json")
                    parameter("email", email)
                    parameter("password", password)
                    parameter("returnSecureToken", true)
                }
            if (responseBody.status.value in 200..299) {
                return  Json { ignoreUnknownKeys = true }
                    .decodeFromString<AuthResponse>(responseBody.bodyAsText())

            } else {
                throw Exception(responseBody.bodyAsText())
            }
    }
    suspend fun signIn(email: String,password: String) : AuthResponse {
            val responseBody = httpClient
                .post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=${API_KEY}") {
                    header("Content-Type", "application/json")
                    parameter("email", email)
                    parameter("password", password)
                    parameter("returnSecureToken", true)
                }
            if (responseBody.status.value in 200..299) {
            return Json { ignoreUnknownKeys = true }
                    .decodeFromString<AuthResponse>(responseBody.bodyAsText())

            } else {
                throw Exception(responseBody.bodyAsText())
            }
    }
    suspend fun getRefreshToken(refreshToken:String?) : TokenResponse {
            val responseBody = httpClient
                .post("https://securetoken.googleapis.com/v1/token?key=${API_KEY}") {
                    header("Content-Type", "application/json")
                    parameter("grant_type", "refresh_token")
                    parameter("refresh_token", refreshToken)
                }
            if (responseBody.status.value in 200..299) {
                return Json { ignoreUnknownKeys = true }
                    .decodeFromString<TokenResponse>(responseBody.bodyAsText())
            } else {
               throw Exception(responseBody.bodyAsText())
            }
    }
}