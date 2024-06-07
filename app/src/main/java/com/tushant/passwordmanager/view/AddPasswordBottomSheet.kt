package com.tushant.passwordmanager.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tushant.passwordmanager.model.Password
import com.tushant.passwordmanager.viewModel.PasswordViewModel
import kotlinx.coroutines.launch
import com.tushant.passwordmanager.R
import com.tushant.passwordmanager.utils.PasswordMeter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordBottomSheet(
    viewModel: PasswordViewModel,
    isOpen: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    var accountType by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isOpen.value) {
        if (!isOpen.value) {
            accountType = ""
            email = ""
            password.value = ""
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .imePadding()
            ) {
                OutlinedTextField(
                    value = accountType,
                    onValueChange = { accountType = it },
                    placeholder = { Text("Account Name") },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color(0xFFCCCCCC),
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLabelColor = Color(0xFFCCCCCC),
                        unfocusedLabelColor = Color(0xFFCCCCCC),
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Username/ Email") },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = Color(0xFFCCCCCC),
                        unfocusedBorderColor = Color(0xFFCCCCCC),
                        focusedLabelColor = Color(0xFFCCCCCC),
                        unfocusedLabelColor = Color(0xFFCCCCCC),
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                PasswordMeter(passwordState = password)
                Spacer(modifier = Modifier.padding(16.dp))
                Button(
                    onClick = {
                        if (accountType.isEmpty() || email.isEmpty() || password.value.isEmpty()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            scope.launch {
                                isOpen.value = false
                                val passwordData = Password(
                                    accountType = accountType,
                                    email = email,
                                    encryptedPassword = password.value
                                )
                                viewModel.insertPassword(passwordData)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Add New Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
