package com.vchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vchat.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.vchat.ui.theme.Primary


/**
 * Created by Fasil on 11/03/23.
 */
@Composable
fun ArrowButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(Color.Black)
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Row(
            Modifier
                .background(color = Color.White)
                .widthIn(min = 90.dp, max = 160.dp)
                .heightIn(min = 45.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArrowButtonPreview() {
    ArrowButton(text = "Button") {}
}