package com.vchat.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vchat.R
import com.vchat.common.enums.InputType

/**
 * Created by Fasil on 11/03/23.
 */
@Composable
fun InputBox(
    placeHolder: String = "",
    @DrawableRes leadingIconResource: Int?,
    value: MutableState<String>,
    inputType: InputType = InputType.TEXT,
    imeAction: ImeAction = ImeAction.Next
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(start = 1.dp, end = 4.dp, top = 1.dp, bottom = 4.dp)
    ) {
        TextField(
            value = value.value,
            onValueChange = {
                value.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = Color.White),
            placeholder = {
                Text(text = placeHolder)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black
            ),
            leadingIcon = {
                leadingIconResource?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                    )
                }
            }, visualTransformation = when (inputType) {
                InputType.TEXT_PASSWORD -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = when (inputType) {
                    InputType.TEXT -> KeyboardType.Text
                    InputType.EMAIL -> KeyboardType.Email
                    InputType.TEXT_PASSWORD -> KeyboardType.Password
                    InputType.NUMBER_PASSWORD -> KeyboardType.NumberPassword
                },
                imeAction = imeAction
            ), maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputBoxPreview() {
    InputBox(
        value = remember { mutableStateOf("Text Field") },
        leadingIconResource = R.drawable.ic_at
    )
}