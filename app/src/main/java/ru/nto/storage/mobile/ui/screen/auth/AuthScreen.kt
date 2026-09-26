package ru.nto.storage.mobile.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.nto.storage.mobile.R
import ru.nto.storage.mobile.core.TestIds

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is AuthAction.Open -> navController.navigate(action.destination) {
                    popUpTo(0)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.auth_title),
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontFamily.SansSerif,
            fontSize = 35.sp,
            fontStyle = FontStyle.Normal,
            letterSpacing = (-2.45).sp,
            fontWeight = FontWeight(400),
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.auth_title2),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            letterSpacing = (-1.4).sp,
            fontWeight = FontWeight(400)
        )
        Spacer(modifier = Modifier.height(85.dp))
        when (val currentState = state) {
            is AuthState.Data -> Content(viewModel, currentState)
            is AuthState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

// Поле фиксированной высоты (56dp), как в Figma:
// - без ошибки: один текст ("Логин"/значение), по центру блока;
// - с ошибкой: сверху маленький текст "Это поле должно быть заполненным!",
//   под ним — лейбл/значение. Высота блока не меняется между состояниями.
@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    fieldColor: Color,
    textColor: Color,
    errorText: String? = null,
    onFocusChange: (Boolean) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val showPlaceholder = value.isEmpty() && !isFocused

    val labelField = @Composable {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (showPlaceholder) {
                Text(
                    text = label,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 15.sp,
                    letterSpacing = (-1.05).sp,
                    fontWeight = FontWeight(400),
                    color = textColor
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 15.sp,
                    letterSpacing = (-1.05).sp,
                    fontWeight = FontWeight(400),
                    color = textColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        onFocusChange(focusState.isFocused)
                    }
            )
        }
    }

    Box(
        modifier
            .height(56.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(fieldColor)
            .padding(horizontal = 15.dp)
    ) {
        if (errorText != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = errorText,
                    color = textColor.copy(alpha = 0.6f),
                    fontWeight = FontWeight(400),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Normal,
                    letterSpacing = (-0.7).sp
                )
                labelField()
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
            ) {
                labelField()
            }
        }
    }
}

@Composable
private fun Content(
    viewModel: AuthViewModel,
    state: AuthState.Data
) {
    var inputText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var passwordText by remember { mutableStateOf("") }
    var showPasswordError by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.size(16.dp))

    AuthField(
        label = stringResource(R.string.auth_label2),
        value = inputText,
        onValueChange = {
            inputText = it
            showError = false
            viewModel.onIntent(AuthIntent.TextInput(it))
        },
        fieldColor = MaterialTheme.colorScheme.primaryContainer,
        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        errorText = if (showError) stringResource(R.string.auth_label2warn) else null,
        modifier = Modifier
            .testTag(TestIds.Auth.CODE_INPUT)
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.size(16.dp))

    AuthField(
        label = stringResource(R.string.auth_label),
        value = passwordText,
        onValueChange = {
            passwordText = it
            showPasswordError = false
        },
        fieldColor = MaterialTheme.colorScheme.primaryContainer,
        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        errorText = if (showPasswordError) stringResource(R.string.auth_label2warn) else null,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .testTag(TestIds.Auth.CODE_INPUT)
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.size(20.dp))
    Spacer(modifier = Modifier.size(16.dp))
    Button(
        modifier = Modifier
            .testTag(TestIds.Auth.SIGN_BUTTON)
            .fillMaxWidth()
            .widthIn(max = 364.dp),
        onClick = {
            when {
                inputText.isEmpty() -> showError = true
                passwordText.isEmpty() -> showPasswordError = true
                else -> viewModel.onIntent(AuthIntent.Send(inputText))
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        contentPadding = PaddingValues(15.dp)
    ) {
        Text(
            text = stringResource(R.string.auth_sign_in),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight(400),
            letterSpacing = (-1.05).sp
        )
    }
    if (state.error != null) {
        Text(
            modifier = Modifier.testTag(TestIds.Auth.ERROR),
            text = state.error,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red,
        )
    }
}