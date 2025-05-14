package com.example.guetteteskilometres.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guetteteskilometres.ui.theme.GuetteTesKilometresTheme
import com.example.guetteteskilometres.ui.theme.black
import com.example.guetteteskilometres.ui.theme.light

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCard(
    title: String,
    leftText: String,
    rightText: String?,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(5.dp),
                color = backgroundColor
            )
            .padding(10.dp)
            .combinedClickable(
                indication = ripple(bounded = true),
                interactionSource = remember { MutableInteractionSource() },
                onLongClick = onLongClick,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Divider(
                modifier = Modifier.padding(start = 10.dp),
                color = black
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = leftText
            )
            if (!rightText.isNullOrEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = rightText
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeCardPreview() {
    GuetteTesKilometresTheme {
        CustomCard(
            title = "100 kilomètres",
            leftText = "12 participants",
            rightText = "100 kms",
            backgroundColor = light,
            onClick = { },
            onLongClick = { }
        )
    }
}

@Preview
@Composable
private fun ParticipationCardPreview() {
    GuetteTesKilometresTheme {
        CustomCard(
            title = "Faustine",
            leftText = "1234 m",
            rightText = "",
            backgroundColor = light,
            onClick = { },
            onLongClick = { }
        )
    }
}