package com.vchat.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vchat.R
import com.vchat.common.enums.InputType
import com.vchat.presentation.components.*
import com.vchat.ui.theme.Primary

/**
 * Created by Fasil on 08/03/23.
 */
@Composable
fun Login(
    error: State<String?>,
    loading: State<Boolean>,
    onLogin: State<Boolean>,
    onClickLogin: (String, String) -> Unit,
    resetError: () -> Unit,
    onClickForgotPassword: () -> Unit,
    onClickRegister: () -> Unit,
    navigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(12.dp)
    ) {

        val showErrorMessage = remember {
            mutableStateOf(false)
        }

        showErrorMessage.value = error.value != null

        if (showErrorMessage.value) {
            MessageDialog(message = error.value.toString()) { resetError() }
        }

        if (loading.value) {
            LoadingDialog()
        }

        if (onLogin.value) {
            navigateToHome()
        }

        val email = remember {
            mutableStateOf("")
        }

        val password = remember {
            mutableStateOf("")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Welcome Back!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = "Login", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))
        InputBox(
            leadingIconResource = R.drawable.ic_at,
            value = email,
            placeHolder = "Email",
            inputType = InputType.EMAIL,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(20.dp))
        InputBox(
            leadingIconResource = R.drawable.ic_lock,
            value = password,
            placeHolder = "Password",
            inputType = InputType.TEXT_PASSWORD,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Forgot Password?")
            }
        }, modifier = Modifier.clickable { onClickForgotPassword() })
        Spacer(modifier = Modifier.height(3.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            ArrowButton(text = "Login") {
                onClickLogin(email.value, password.value)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account?")
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Register")
                }
            }, modifier = Modifier.clickable { onClickRegister() })
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onLogin = remember {
        mutableStateOf(false)
    }
    Login(error, loading, onLogin, { _, _ -> }, {}, {}, {}, {})
}