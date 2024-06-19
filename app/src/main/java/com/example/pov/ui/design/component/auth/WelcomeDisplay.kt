package com.example.pov.ui.design.component.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.pov.R

@Composable
fun WelcomeDisplay(
    modifier: Modifier = Modifier,
    headLine: String,
    headLineTextStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_extra_small))
        )
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
                .align(Alignment.CenterHorizontally)
        )
        PoVDivider(headLine = headLine, headLineTextStyle = headLineTextStyle)
    }
}