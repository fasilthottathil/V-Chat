package com.vchat.presentation.passwordReset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vchat.R
import com.vchat.common.enums.InputType
import com.vchat.presentation.components.ArrowButton
import com.vchat.presentation.components.InputBox
import com.vchat.presentation.components.LoadingDialog
import com.vchat.presentation.components.MessageDialog
import com.vchat.ui.theme.Primary

/**
 * Created by Fasil on 14/03/23.
 */
@Composable
fun ResetPassword(
    error: State<String?>,
    loading: State<Boolean>,
    onResetEmailSend: State<Boolean>,
    onClickReset: (String) -> Unit,
    resetError: () -> Unit,
    navigateBack: () -> Unit
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

        val showPasswordEmailSendDialog = remember {
            mutableStateOf(false)
        }

        showErrorMessage.value = error.value != null

        showPasswordEmailSendDialog.value = onResetEmailSend.value

        if (loading.value) {
            LoadingDialog()
        }

        if (showErrorMessage.value) {
            MessageDialog(message = error.value.toString()) { resetError() }
        }

        val email = remember {
            mutableStateOf("")
        }

        if (showPasswordEmailSendDialog.value) {
            MessageDialog(message = "Password Reset email is sent to : ${email.value}") {
                navigateBack()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Reset Password", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))
        InputBox(
            leadingIconResource = R.drawable.ic_at,
            value = email,
            placeHolder = "Email",
            inputType = InputType.EMAIL,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            ArrowButton(text = "Reset") {
                onClickReset(email.value)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onResetEmailSend = remember {
        mutableStateOf(false)
    }
    ResetPassword(error, loading, onResetEmailSend, {}, {}, {})
}