package com.vchat.presentation.explore

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.presentation.components.LoadingDialog
import com.vchat.presentation.components.MessageDialog
import com.vchat.ui.theme.Primary
import com.vchat.utils.requestPermissions
import kotlinx.coroutines.delay

/**
 * Created by Fasil on 03/04/23.
 */
@Composable
fun AddEditPost(
    post: State<PostEntity?>,
    error: State<String?>,
    loading: State<Boolean>,
    resetError: () -> Unit,
    onProductAddOrUpdated: State<Boolean>,
    onClickDone: (PostEntity, Uri?) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            selectedImageUri.value = it
        }
    )

    val postEntity = if (post.value != null) {
        post.value!!
    } else {
        PostEntity()
    }


    LaunchedEffect(onProductAddOrUpdated.value) {
        if (onProductAddOrUpdated.value) {
            delay(40)
            onBackPressed()
        }
    }

    val showErrorMessage = remember {
        mutableStateOf(false)
    }

    showErrorMessage.value = error.value != null

    if (showErrorMessage.value) {
        MessageDialog(message = error.value.orEmpty()) { resetError() }
    }

    if (loading.value) {
        LoadingDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        TopAppBar(backgroundColor = Color.White) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onBackPressed() }
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = if (post.value == null) "Add Post" else "Edit Post",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickDone(postEntity, selectedImageUri.value) }
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 1.dp, color = Color.Black, shape = ShapeDefaults.Small)
                    .clickable {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            imagePicker.launch("image/*")
                        } else {
                            requestPermissions(context as ComponentActivity)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri.value != null) {
                    Image(
                        rememberAsyncImagePainter(model = selectedImageUri.value),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(ShapeDefaults.Small),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    if (post.value == null) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Placeholder Image"
                        )
                    } else {
                        Image(
                            rememberAsyncImagePainter(model = post.value?.postImageUrl),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(ShapeDefaults.Small),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        val content = if (post.value == null) {
            remember {
                mutableStateOf( "")
            }
        } else {
            remember {
                mutableStateOf(post.value?.content ?: "")
            }
        }
        postEntity.content = content.value
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(2.dp)
            ) {
                TextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = 300.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black
                    ), placeholder = {
                        Text(text = "What's in your mind....")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AddEditPostPreview() {
    val post = remember {
        mutableStateOf(null)
    }
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onProductAddOrUpdated = remember {
        mutableStateOf(false)
    }
    AddEditPost(post, error, loading, {}, onProductAddOrUpdated, { _, _ -> }) {}
}