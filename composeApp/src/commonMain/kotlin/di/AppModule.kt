package di

import features.authentication.data.firebase.FirebaseAuth
import features.authentication.data.repository.AuthRepositoryImp
import features.authentication.domain.AuthRepository

object AppModule{
    val AuthRepository: AuthRepository by lazy {
        AuthRepositoryImp(firebaseAuth = FirebaseAuth())
    }
}