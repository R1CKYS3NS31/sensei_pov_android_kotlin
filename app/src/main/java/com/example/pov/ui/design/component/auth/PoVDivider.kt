package com.example.pov.ui.design.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.example.pov.R

@Composable
fun PoVDivider(
    modifier: Modifier = Modifier,
    headLine: String,
    headLineTextStyle: TextStyle = MaterialTheme.typography.titleMedium
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_extra_small))
                .weight(1f)
        )
        OutlinedButton(
            enabled = false,
            onClick = { },
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(
                        id = R.dimen.padding_small
                    )
                )
        ) {
            Text(
                text = headLine,
                style = headLineTextStyle,
            )
        }
        Divider(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_extra_small))
                .weight(1f)
        )
    }
}