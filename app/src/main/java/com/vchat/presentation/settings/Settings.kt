package com.vchat.presentation.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vchat.BuildConfig
import com.vchat.presentation.components.LoadingDialog
import com.vchat.presentation.components.MessageDialog
import com.vchat.ui.theme.Primary
import kotlinx.coroutines.delay

/**
 * Created by Fasil on 08/04/23.
 */
@Composable
fun Settings(
    error: State<String?>,
    loading: State<Boolean>,
    onDeleteAccount: State<Boolean>,
    onLogout: State<Boolean>,
    resetError: () -> Unit,
    editProfile: () -> Unit,
    deleteAccount: () -> Unit,
    logout: () -> Unit,
    restartApp: (Context) -> Unit,
    onClickBack: () -> Unit
) {

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

    val context = LocalContext.current.applicationContext

    val showLogoutDialog = remember {
        mutableStateOf(false)
    }

    val showDeleteAccountDialog = remember {
        mutableStateOf(false)
    }

    if (showDeleteAccountDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text(text = "Delete Account") },
            text = { Text(text = "Do you want to delete account?") },
            confirmButton = {
                Row(modifier = Modifier.padding(6.dp)) {
                    Text(
                        text = "Cancel", modifier = Modifier.clickable {
                            showDeleteAccountDialog.value = false
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ok", modifier = Modifier.clickable {
                            showDeleteAccountDialog.value = false
                            deleteAccount.invoke()
                        }
                    )
                }
            }
        )
    }

    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text(text = "Logout") },
            text = { Text(text = "Do you want to logout app?") },
            confirmButton = {
                Row(modifier = Modifier.padding(6.dp)) {
                    Text(
                        text = "Cancel", modifier = Modifier.clickable {
                            showLogoutDialog.value = false
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ok", modifier = Modifier.clickable {
                            showLogoutDialog.value = false
                            logout.invoke()
                        }
                    )
                }
            }
        )
    }

    LaunchedEffect(onDeleteAccount.value) {
        if (onDeleteAccount.value) {
            delay(40)
            restartApp(context)
        }
    }

    LaunchedEffect(onLogout.value) {
        if (onLogout.value) {
            delay(40)
            restartApp(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
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
            Text(text = "Settings", color = Color.Black, fontSize = 16.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { editProfile.invoke() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Edit Profile")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "About Us")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.ContactSupport, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Contact Us")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
                .clickable { showDeleteAccountDialog.value = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Delete Account")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { showLogoutDialog.value = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Logout", color = Color.Red)
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "App Version : ${BuildConfig.VERSION_NAME}")
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onDelete = remember {
        mutableStateOf(false)
    }
    val onLogout = remember {
        mutableStateOf(false)
    }
    Settings(error, loading, onDelete, onLogout, {},{},{}, {}, {}, {})
}