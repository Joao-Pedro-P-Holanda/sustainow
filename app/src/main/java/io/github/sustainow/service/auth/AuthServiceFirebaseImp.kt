package io.github.sustainow.service.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import io.github.sustainow.domain.model.User
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.exceptions.AuthenticationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

class AuthServiceFirebaseImp : AuthService() {
    private val auth: FirebaseAuth = Firebase.auth
    override var user: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)

    init {

        val listener =
            FirebaseAuth.AuthStateListener {
                    auth ->
                user.value = UserState.Loading
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    Log.i("AuthServiceFirebaseImp", "User is logged in")
                    user.value =
                        UserState.Logged(
                            User(
                                currentUser.uid,
                                currentUser.displayName ?: "",
                                currentUser.email ?: "",
                                currentUser.photoUrl.toString(),
                            ),
                        )
                } else {
                    Log.i("AuthServiceFirebaseImp", "User is not logged in")
                    user.value = UserState.NotLogged
                }
            }
        auth.addAuthStateListener(listener)
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ) = callbackFlow {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(true)
            } else {
                trySend(false)
            }
        }.await()
        awaitClose()
    }.catch {
        when (it) {
            is FirebaseAuthInvalidUserException -> {
                throw AuthenticationException.InvalidEmailException(
                    it.message ?: "Invalid email for login",
                    it,
                )
            }

            is FirebaseAuthInvalidCredentialsException -> {
                throw AuthenticationException.InvalidPasswordException(
                    it.message ?: "Invalid password for login",
                    it,
                )
            }

            is FirebaseTooManyRequestsException -> {
                throw AuthenticationException.TooManyRequestsException(
                    it.message ?: "Too many Login Requests",
                    it,
                )
            }

            else -> {
                throw AuthenticationException.UnknownException(
                    it.message ?: "Unknown error on signup",
                    it,
                )
            }
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ) = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(true)
            } else {
                trySend(false)
            }
        }.await()
        awaitClose()
    }.catch {
        when (it) {
            is FirebaseAuthWeakPasswordException -> {
                throw AuthenticationException.WeakPasswordException(
                    it.message ?: "Password too weak for signup",
                    it,
                )
            }

            is FirebaseAuthUserCollisionException -> {
                throw AuthenticationException.InvalidEmailException(
                    it.message ?: "Email not valid for signup",
                    it,
                )
            }

            else -> {
                throw AuthenticationException.UnknownException(
                    it.message ?: "Unknown error on signup",
                    it,
                )
            }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
