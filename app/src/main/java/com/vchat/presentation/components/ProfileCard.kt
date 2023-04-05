package com.vchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vchat.ui.theme.Primary

/**
 * Created by Fasil on 21/03/23.
 */
@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 250.dp, max = 300.dp)
            .clip(ShapeDefaults.ExtraLarge),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter =
                rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/59242329?v=4")
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.FillBounds,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Username",
                color = Color.Black,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(6.dp)
            )
            Text(
                text = "Long description about the user and other some information about the user",
                fontSize = 14.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(6.dp),
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(ShapeDefaults.Large)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(ShapeDefaults.Large)
                        .background(Primary)
                        .border(width = 1.dp, color = Color.Black, shape = ShapeDefaults.Large)
                        .padding(12.dp),
                    text = "Edit Profile",
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardPreview() {
    ProfileCard()
}