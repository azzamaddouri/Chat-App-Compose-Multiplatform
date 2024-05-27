package features.authentication.presentation.view

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chatappkmp.composeapp.generated.resources.Res
import chatappkmp.composeapp.generated.resources.compose_multiplatform
import core.helpers.Helpers
import di.AppModule
import features.authentication.presentation.AuthViewModel
import features.authentication.presentation.event.SignInEvent
import features.authentication.presentation.event.SignUpEvent
import features.authentication.presentation.state.SignInState
import features.authentication.presentation.state.SignUpState
import features.tabs.presentation.view.TabsView
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.launch

class AuthView : Screen {
    @Composable
    override fun Content() {
        val viewModel = AuthViewModel(AppModule.AuthRepository)
        val signUpState by viewModel.signUpState.collectAsState()
        val signInState by viewModel.signInState.collectAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize().safeDrawingPadding()
        ){
            AuthScreenContent(
                signUpState=signUpState,
                signInState=signInState,
                onSignUpEvent = viewModel::onSignUpEvent,
                onSignInEvent = viewModel::onSignInEvent)
        }
    }
}

@Composable
private fun SignInContent(
    signInState: SignInState,
    onSignInEvent: (SignInEvent) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf(true) }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    val helpers = Helpers()
    val navigator = LocalNavigator.currentOrThrow

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sign In",
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

        Spacer(modifier = Modifier.height(32.dp))

        if (signInState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (email.isEmpty())
                        emailValid = false
                    else if (password.isEmpty())
                        passwordError = true

                    emailValid = helpers.validateEmail(email)

                    if (emailValid) {
                        coroutineScope.launch {
                            onSignInEvent(SignInEvent.SignIn(email = email, password = password))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }
        }

    }
    if (signInState.isSuccess) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false
                },
                title = { "Sign In Successful" },
                text = {
                    Column {
                        Text("Logged in successfully!")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Token: ${signInState.token}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false
                            navigator.push(TabsView())
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    } else if (signInState.isFailure) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false
                },
                title = { Text("Operation Failed") },
                text = {
                    Column {
                        Text("An error occurred during sign in")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }

}

@Composable
private fun SignUpContent(
    signUpState: SignUpState,
    onSignUpEvent: (SignUpEvent) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf(true) }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    var confirmPassword by remember { mutableStateOf("") }
    val confirmPasswordError by remember { mutableStateOf(false) }

    val helpers = Helpers()
    val navigator = LocalNavigator.currentOrThrow

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sign Up",
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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        if (signUpState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (email.isEmpty())
                        emailValid = false
                    else if (password.isEmpty())
                        passwordError = true

                    emailValid = helpers.validateEmail(email)

                    if (emailValid) {
                        coroutineScope.launch {
                            onSignUpEvent(
                                SignUpEvent.SignUp(
                                    email = email,
                                    password = password,
                                    confirmPassword = confirmPassword
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }

    }
    if (signUpState.isSuccess) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false
                },
                title = { Text("Account created successfully!") },
                text = {
                    Column {
                        Text("Account created successfully!")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Token: ${signUpState.token}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false
                            navigator.push(TabsView())
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    } else if (signUpState.isFailure ) {
        var dialogOpen by remember { mutableStateOf(true) }

        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    dialogOpen = false
                },
                title = { Text("Operation Failed") },
                text = {
                    Column {
                        Text("An error occurred during sign up")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpen = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }

}

@Composable
private fun AuthScreenContent(
    signUpState: SignUpState,
    signInState: SignInState,
    onSignUpEvent: (SignUpEvent) -> Unit,
    onSignInEvent: (SignInEvent) -> Unit
) {
    var signIn by remember { mutableStateOf(true) }

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
            modifier = Modifier.size(200.dp).padding(32.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))

        if (signIn) {
            SignInContent(signInState = signInState, onSignInEvent = onSignInEvent)
        } else {
            SignUpContent(signUpState = signUpState, onSignUpEvent = onSignUpEvent)
        }

        Spacer(modifier = Modifier.height(32.dp))

        ClickableText(
            text = AnnotatedString(if (signIn) "Create a new account" else "Log In to existing Account"),
            onClick = {
                signIn = !signIn
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


