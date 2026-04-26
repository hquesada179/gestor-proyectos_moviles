package com.helbertquesada.gestor_proyectos_moviles.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

private const val WEB_CLIENT_ID =
    "155026580409-u8ti27460atb2ke820kng12hq7r77l6c.apps.googleusercontent.com"

sealed class GoogleSignInResult {
    data object Success : GoogleSignInResult()
    data object Cancelled : GoogleSignInResult()
    data class Error(val message: String) : GoogleSignInResult()
}

suspend fun signInWithGoogle(context: Context): GoogleSignInResult {
    return try {
        val credentialManager = CredentialManager.create(context)

        val googleSignInOption = GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleSignInOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        val idToken = when {
            credential is GoogleIdTokenCredential -> credential.idToken
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ->
                GoogleIdTokenCredential.createFrom(credential.data).idToken
            else -> return GoogleSignInResult.Error("Tipo de credencial no soportado")
        }

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()

        GoogleSignInResult.Success
    } catch (e: GetCredentialCancellationException) {
        GoogleSignInResult.Cancelled
    } catch (e: NoCredentialException) {
        GoogleSignInResult.Error("No hay cuentas de Google disponibles en este dispositivo")
    } catch (e: GetCredentialException) {
        GoogleSignInResult.Error("No se pudo iniciar sesión con Google")
    } catch (e: Exception) {
        GoogleSignInResult.Error("No se pudo iniciar sesión con Google")
    }
}
