package com.vchat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vchat.common.enums.PostDialogOption

/**
 * Created by Fasil on 02/04/23.
 */
@Composable
fun PostOptionDialog(userId: String?, postUserId: String, onOptionSelected: (PostDialogOption) -> Unit) {
    Dialog(onDismissRequest = {  }) {
        Column(
            modifier = Modifier
                .width(260.dp)
                .wrapContentHeight()
                .clip(ShapeDefaults.ExtraLarge)
                .background(Color.White)
                .border(width = 1.dp, shape = ShapeDefaults.ExtraLarge, color = Color.Black)
        ) {
            if (userId == postUserId) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.EDIT) }
                    .height(40.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Edit", fontSize = 16.sp, color = Color.Black)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.DELETE) }
                    .height(40.dp)
                    .border(width = 0.5.dp, color = Color.Black), contentAlignment = Alignment.Center) {
                    Text(text = "Delete", fontSize = 16.sp, color = Color.Black)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.VIEW_PROFILE) }
                    .height(40.dp), contentAlignment = Alignment.Center) {
                    Text(text = "View Profile", fontSize = 16.sp, color = Color.Black)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.CLOSE) }
                    .height(40.dp)
                    .border(width = 0.5.dp, color = Color.Black), contentAlignment = Alignment.Center) {
                    Text(text = "Close", fontSize = 16.sp, color = Color.Red)
                }
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.VIEW_PROFILE) }
                    .height(40.dp), contentAlignment = Alignment.Center) {
                    Text(text = "View Profile", fontSize = 16.sp, color = Color.Black)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.REPORT) }
                    .height(40.dp)
                    .border(width = 0.5.dp, color = Color.Black), contentAlignment = Alignment.Center) {
                    Text(text = "Report", fontSize = 16.sp, color = Color.Black)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(PostDialogOption.CLOSE) }
                    .height(40.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Close", fontSize = 16.sp, color = Color.Red)
                }
            }
        }
    }
}

@Preview
@Composable
fun PostOptionDialogPreview() {
    PostOptionDialog("", "1") { }
}