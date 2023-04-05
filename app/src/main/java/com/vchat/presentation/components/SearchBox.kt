package com.vchat.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Created by Fasil on 19/03/23.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchBox(onSearch: (String) -> Unit) {
    val searchText = remember {
        mutableStateOf("")
    }
    val hintList = listOf(
        "Search \"music\"",
        "Search \"dance\"",
        "Search \"fight\"",
        "Search \"drama\"",
        "Search \"story\"",
        "Search \"life\"",
        "Search \"doodle art\""
    )
    val hintText = remember {
        mutableStateOf("Search \"fun\"")
    }
    LaunchedEffect(Unit) {
        while (true) {
            hintText.value = hintList.random()
            delay(4000)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(ShapeDefaults.Medium.copy(CornerSize(12.dp)))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = ShapeDefaults.Medium.copy(CornerSize(12.dp))
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText.value,
                onValueChange = {
                    it.also { searchText.value = it }
                    onSearch(searchText.value)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable { onSearch(searchText.value) }
                            .clip(CircleShape)
                    )
                },
                placeholder = {
                    AnimatedContent(
                        targetState = hintText.value,
                        transitionSpec = {
                            scaleIn() with scaleOut()
                        }
                    ) { text ->
                        Text(text = text, color = Color.Black)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun SearchBoxPreview() {
    SearchBox {}
}