package com.vchat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Created by Fasil on 12/03/23.
 */
@Composable
fun RoundedRadioButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(start = 1.dp, top = 1.dp, bottom = 4.dp, end = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .widthIn(min = 100.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(),
                modifier = Modifier
                    .background(Color.White)
                    .size(30.dp)
            )
            Text(text = text, color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedRadioButtonPreview() {
    RoundedRadioButton("Radio Button",true) {}
}