package com.tushant.passwordmanager.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tushant.passwordmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordMeter(passwordState: MutableState<String>) {
    val isShowPassword = remember { mutableStateOf(false) }

    fun visualTransformation() = if (isShowPassword.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    fun visualIcon() = if (isShowPassword.value) {
        R.drawable.hide_password
    } else {
        R.drawable.show_password
    }

    val securityState = remember {
        mutableStateOf(StrengthLevel.minLevel)
    }
    val calculator: CalculatorInterface = PasswordCalculator

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
                securityState.value = calculator.calculatePasswordLevel(it)
            },
            placeholder = { Text("Password") },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFFCCCCCC),
                unfocusedBorderColor = Color(0xFFCCCCCC),
                focusedLabelColor = Color(0xFFCCCCCC),
                unfocusedLabelColor = Color(0xFFCCCCCC),
            ),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = visualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        isShowPassword.value = !isShowPassword.value
                    }
                ) {
                    Icon(
                        painter = painterResource(id = visualIcon()),
                        contentDescription = "Show Password",
                        tint = Color(0xFFCCCCCC)
                    )
                }
            }
        )

        ConstraintLayout {
            val (text, box1, box2) = createRefs()
            Box(
                Modifier
                    .constrainAs(box1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth(fraction = 0.65f)
                    .height(6.dp)
                    .background(Color.LightGray)
            )

            Box(
                Modifier
                    .constrainAs(box2) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .height(6.dp)
                    .animateContentSize()

            ) {
                Text(
                    "",
                    modifier = Modifier
                        .fillMaxWidth(0.65f * securityState.value.percentage)
                        .background(securityState.value.color)
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(box1.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 8.dp),
                text = stringResource(id = securityState.value.textId),
                color = securityState.value.color
            )
        }
    }
}