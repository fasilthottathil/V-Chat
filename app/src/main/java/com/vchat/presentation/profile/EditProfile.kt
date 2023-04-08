package com.vchat.presentation.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.presentation.components.InputBox
import com.vchat.presentation.components.LoadingDialog
import com.vchat.presentation.components.MessageDialog
import com.vchat.ui.theme.Primary
import com.vchat.utils.requestPermissions
import kotlinx.coroutines.delay

/**
 * Created by Fasil on 08/04/23.
 */
@Composable
fun EditProfile(
    error: State<String?>,
    loading: State<Boolean>,
    resetError: () -> Unit,
    user: State<UserEntity>,
    onUpdateUser: State<Boolean>,
    onClickDone: (UserEntity, Uri?) -> Unit,
    onClickBack: () -> Unit
) {

    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            selectedImageUri.value = it
        }
    )

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

    val userEntity = if (user.value.id.isEmpty()) {
        UserEntity()
    } else {
        user.value
    }

    LaunchedEffect(onUpdateUser.value) {
        if (onUpdateUser.value) {
            delay(40)
            onClickBack.invoke()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(backgroundColor = Color.White) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickBack.invoke() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Edit Profile", fontSize = 16.sp, color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickDone(userEntity, selectedImageUri.value) }
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        val painter = rememberAsyncImagePainter(
            if (selectedImageUri.value == null) {
                user.value.profileUrl
            } else {
                selectedImageUri.value
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
                .background(Color.LightGray)
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
            contentScale = ContentScale.FillBounds,
        )
        Spacer(modifier = Modifier.height(12.dp))
        val userName = if (user.value.id.isEmpty()) {
            remember {
                mutableStateOf("")
            }
        } else {
            remember {
                mutableStateOf(userEntity.name)
            }
        }
        userEntity.name = userName.value
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            InputBox(
                leadingIconResource = com.vchat.R.drawable.ic_person_outline,
                value = userName,
                placeHolder = "Username"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val content = if (user.value.id.isEmpty()) {
            remember {
                mutableStateOf("")
            }
        } else {
            remember {
                mutableStateOf(userEntity.about)
            }
        }
        userEntity.about = content.value
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
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
fun EditProfilePreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onUpdateUser = remember {
        mutableStateOf(false)
    }
    val user = remember {
        mutableStateOf(UserEntity())
    }
    EditProfile(error, loading, {}, user, onUpdateUser, { _, _ -> }, {})
}