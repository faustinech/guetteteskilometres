package com.example.guetteteskilometres.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun CustomField(
    @StringRes idLabel: Int,
    value: String?,
    @StringRes idErrorMessage: Int?,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        label = { Text(text = stringResource(id = idLabel)) },
        value = value.orEmpty(),
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp,
                horizontal = 10.dp
            ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        isError = idErrorMessage != null,
        supportingText = {
            if (idErrorMessage != null) {
                Text(
                    text = stringResource(id = idErrorMessage),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    )
}