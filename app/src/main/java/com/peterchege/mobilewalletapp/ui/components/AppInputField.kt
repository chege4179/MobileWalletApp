package com.peterchege.mobilewalletapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterchege.mobilewalletapp.core.util.isNotNull
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInputField(
    label: String,
    inputValue: String,
    onChangeInputValue: (String) -> Unit,
    errorMessage: String? = null,
    showVisibilityOption: Boolean = false,
    contentDes: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    val colors = TextFieldDefaults.colors()
        .copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.onPrimary,
            focusedPlaceholderColor = colorScheme.onPrimaryContainer,
            unfocusedPlaceholderColor = colorScheme.onPrimaryContainer,
            errorLabelColor = Color.Red,
            errorLeadingIconColor = Color.Red,
            errorTextColor = Color.Red,
            errorTrailingIconColor = Color.Red,
            focusedTrailingIconColor = colorScheme.onPrimaryContainer,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = Color.Red,
            errorPlaceholderColor = Color.Red,
            focusedTextColor = colorScheme.onPrimaryContainer
        )

    var passwordIsVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val counter = remember {
        mutableIntStateOf(5)
    }



    Column {
        Text(
            text = label,
            style = TextStyle(
                color =  colorScheme.onPrimary,
                fontSize = 14.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        )

        BasicTextField(
            value = inputValue,
            cursorBrush = SolidColor(colorScheme.primary),
            textStyle = TextStyle(
                fontSize = 17.sp,
                textAlign = TextAlign.Left,
                color =  colorScheme.onPrimary
            ),
            visualTransformation = if (showVisibilityOption) {
                if (passwordIsVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            } else {
                VisualTransformation.None
            },
            onValueChange = onChangeInputValue,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 2.dp, bottom = 10.dp)
                .onFocusChanged { focusState ->

                },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            )
        ) {
            OutlinedTextFieldDefaults.DecorationBox(
                value = inputValue,
                enabled = true,
                singleLine = true,
                isError = errorMessage.isNotNull(),
                visualTransformation = if (showVisibilityOption) {
                    if (passwordIsVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }
                } else {
                    VisualTransformation.None
                },
                colors = colors,
                contentPadding = OutlinedTextFieldDefaults.contentPadding(
                    start = 12.dp,
                    end = 5.dp
                ),
                innerTextField = it,
                placeholder = {
                    Text(
                        text =label,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    )
                },
                trailingIcon = {
                    if (showVisibilityOption) {
                        PasswordVisibilityIcon(
                            passwordVisible = passwordIsVisible,
                            toggleVisibility = { passwordIsVisible = !passwordIsVisible },
                            contentDescription = contentDes
                        )
                    }

                },
                interactionSource = interactionSource,
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = true,
                        isError = errorMessage.isNotNull(),
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = RoundedCornerShape(8.dp),
                        focusedBorderThickness = 0.8.dp,
                        unfocusedBorderThickness = 0.4.dp,
                    )
                },
            )
        }
    }
}