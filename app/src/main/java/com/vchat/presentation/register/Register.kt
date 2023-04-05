package com.vchat.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Register(
    error: State<String?>,
    loading: State<Boolean>,
    onRegistered: State<Boolean>,
    onClickRegister: (String, String, String, String) -> Unit,
    resetError: () -> Unit,
    onClickLogin: () -> Unit,
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

        val onRegister = remember {
            mutableStateOf(false)
        }

        showErrorMessage.value = error.value != null

        onRegister.value = onRegistered.value

        if (showErrorMessage.value) {
            MessageDialog(message = error.value.toString()) { resetError() }
        }

        if (loading.value) {
            LoadingDialog()
        }

        if (onRegister.value) {
            navigateToHome()
        }

        val username = remember {
            mutableStateOf("")
        }

        val email = remember {
            mutableStateOf("")
        }

        val password = remember {
            mutableStateOf("")
        }

        val gender = remember {
            mutableStateOf("Male")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle()) {
                    append("Welcome to ")
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("V Chat ")
                    }
                    append("App!")
                }
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))
        InputBox(
            leadingIconResource = R.drawable.ic_person_outline,
            value = username,
            placeHolder = "Username",
            inputType = InputType.TEXT,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(20.dp))
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
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            RoundedRadioButton("Male", gender.value == "Male") {
                gender.value = "Male"
            }
            Spacer(modifier = Modifier.width(10.dp))
            RoundedRadioButton("Female", gender.value == "Female") {
                gender.value = "Female"
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            ArrowButton(text = "Sign Up") {
                onClickRegister(username.value, email.value, password.value, gender.value)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account?")
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Login")
                }
            }, modifier = Modifier.clickable { onClickLogin() })
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onRegistered = remember {
        mutableStateOf(false)
    }
    Register(error, loading, onRegistered, { _, _, _, _ -> }, {}, {}, {})
}