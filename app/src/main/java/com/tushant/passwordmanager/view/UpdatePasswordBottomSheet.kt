package com.tushant.passwordmanager.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tushant.passwordmanager.model.Password
import com.tushant.passwordmanager.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasswordBottomSheet(
    password: Password,
    onUpdateClick: (Password) -> Unit,
    onDeleteClick: (Password) -> Unit,
    isOpen: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var accountType by remember { mutableStateOf(password.accountType) }
    var email by remember { mutableStateOf(password.email) }
    var encryptedPassword by remember { mutableStateOf(password.encryptedPassword) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(isOpen.value) {
        if (!isOpen.value) {
            accountType = password.accountType
            email = password.email
            encryptedPassword = password.encryptedPassword
            passwordVisibility = false
            isEditing = false
        }
    }

    if (isOpen.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isOpen.value = false
            }
        ) {
            Column(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Text(
                    text = "Account Details",
                    color = Color(0xFF3F7DE3),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_bold)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                Text(
                    text = "Account Name",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
                CustomTextField(
                    value = accountType,
                    onValueChange = { accountType = it },
                    readOnly = !isEditing
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Username/ Email",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                )
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    readOnly = !isEditing
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Password",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                )
                CustomTextField(
                    value = encryptedPassword,
                    onValueChange = { encryptedPassword = it },
                    readOnly = !isEditing,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (passwordVisibility) R.drawable.show_password else R.drawable.hide_password,
                    onTrailingIconClick = { passwordVisibility = !passwordVisibility }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                if (isEditing) {
                                    val updatedPassword = password.copy(
                                        accountType = accountType,
                                        email = email,
                                        encryptedPassword = encryptedPassword
                                    )
                                    onUpdateClick(updatedPassword)
                                }
                                isEditing = !isEditing
                            }
                        },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) {
                        Text(
                            text = if (isEditing) "Update" else "Edit",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                onDeleteClick(password)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)
                    ) {
                        Text("Delete",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = null,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        trailingIcon = {
                       trailingIcon?.let {
                           IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                               Image(
                                   painter = painterResource(id = it),
                                   contentDescription = null,
                                   modifier = Modifier
                                       .size(24.dp)
                               )
                           }
                       }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
            cursorColor = if (readOnly) Color.Transparent else Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 18.sp,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}