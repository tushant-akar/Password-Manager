package com.tushant.passwordmanager.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tushant.passwordmanager.viewModel.PasswordViewModel
import com.tushant.passwordmanager.model.Password
import com.tushant.passwordmanager.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreen(
    modifier: Modifier = Modifier,
    viewModel: PasswordViewModel
) {
    val passwords by viewModel.passwords.observeAsState(emptyList())
    val showAddPasswordBottomSheet = rememberSaveable { mutableStateOf(false) }
    var showUpdatePasswordBottomSheet by remember { mutableStateOf<Password?>(null) }
    val isUpdateBottomSheetOpen = rememberSaveable { mutableStateOf(false) }

    Scaffold (
        modifier = modifier
            .background(Color(0xFFF3F5FA)),
        topBar = {
            TopAppBar(
                title = { Text(
                    text ="Password Manager",
                    fontFamily = FontFamily(Font(R.font.sf_pro_bold)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                ) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPasswordBottomSheet.value = true },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color(0xFF3F7DE3),
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Password",
                    tint = Color.White,
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(passwords) { password ->
                PasswordListItem(
                    password = password,
                    onClick = {
                        showUpdatePasswordBottomSheet = password
                        isUpdateBottomSheetOpen.value = true
                    }
                )
            }
        }

        AddPasswordBottomSheet(
            viewModel = viewModel,
            isOpen = showAddPasswordBottomSheet,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        showUpdatePasswordBottomSheet?.let { password ->
            UpdatePasswordBottomSheet(
                password = password,
                onUpdateClick = { updatedPassword ->
                    viewModel.updatePassword(updatedPassword)
                    showUpdatePasswordBottomSheet = null
                    isUpdateBottomSheetOpen.value = false
                },
                onDeleteClick = {
                    viewModel.deletePassword(it)
                    showUpdatePasswordBottomSheet = null
                    isUpdateBottomSheetOpen.value = false
                },
                isOpen = isUpdateBottomSheetOpen,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PasswordListItem(
    password: Password,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(8f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = password.accountType,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.sf_pro_bold)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }
                    Column(verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = maskString(password.encryptedPassword),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.sf_pro_bold)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC6C6C6)
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        }
    }
}

fun maskString(input: String): String {
    return "*".repeat(input.length)
}

