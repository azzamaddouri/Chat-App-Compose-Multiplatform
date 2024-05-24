package features.authentication.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import features.tabs.TabsScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chatappkmp.composeapp.generated.resources.Res
import chatappkmp.composeapp.generated.resources.compose_multiplatform
import core.helpers.Helpers
import di.AppModule
import features.tabs.TabsScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = AuthViewModel(AppModule.AuthRepository)
        val state by viewModel.state.collectAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize().safeDrawingPadding()
        ){
            AuthScreenContent(state=state, onEvent = viewModel::onEvent)
        }
    }
}

@Composable
private fun AuthScreenContent(
    state:AuthState,
    onEvent: (AuthEvent)-> Unit){
    var title by remember { mutableStateOf("Sign In") }

    var email by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf(true) }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    var signIn by remember { mutableStateOf(true) }
    var confirmPassword by remember { mutableStateOf("") }
    val confirmPasswordError by remember { mutableStateOf(false) }

    var signInText by remember { mutableStateOf("Create a new account") }

    val helpers = Helpers()
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.compose_multiplatform),
            null,
            modifier = Modifier.size(200.dp).padding(32.dp))
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            if (signIn) "Sign In" else "Sign Up",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )

        OutlinedTextField(
            value = email,
            isError = !emailValid,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            isError = passwordError,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = !signIn,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(250))
        ) {
            OutlinedTextField(
                value = confirmPassword,
                isError = confirmPasswordError,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        if (state.isLoading){
            CircularProgressIndicator()
        }
        else{
            Button(
            onClick = {
                if (email.isEmpty())
                    emailValid = false
                else if (password.isEmpty())
                    passwordError = true

                when (signIn) {
                    true -> {
                        emailValid = helpers.validateEmail(email)
                        if (emailValid) {
                            coroutineScope.launch {
                                onEvent(AuthEvent.SignIn(email=email, password=password))
                            }
                        }
                    }
                    false -> {
                        emailValid = helpers.validateEmail(email)
                        if (emailValid) {
                            coroutineScope.launch {
                                onEvent(AuthEvent.SignUp(
                                    email=email,
                                    password=password,
                                    confirmPassword= confirmPassword
                                ))
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(text = if (signIn) "Sign In" else "Sign Up") }}

        Spacer(modifier = Modifier.height(32.dp))

        ClickableText(
            text = AnnotatedString(signInText),
            onClick = {
                if (signIn) {
                    signIn = false
                    title = "Sign In"
                    signInText = "Log In to existing Account"
                } else {
                    signIn = true
                    title = "Sign Up"
                    signInText = "Create a New Account"
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        if (state.isSuccess) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false // Dismiss the dialog
                },
                title = { Text(if (signIn) "Sign In Successful" else "Account created successfully!") },
                text = {
                    Column {
                        Text(if (signIn) "Logged in successfully!" else "Account created successfully!")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Token: ${state.token}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false // Dismiss the dialog
                            navigator.push(TabsScreen(TabsScreenViewModel())) // Example navigation
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    } else if (state.isFailure) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false // Dismiss the dialog
                },
                title = { Text("Operation Failed") },
                text = {
                    Column {
                        Text("An error occurred during ${if (signIn) "Sign In" else "Sign Up"}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false // Dismiss the dialog
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }

}


}
