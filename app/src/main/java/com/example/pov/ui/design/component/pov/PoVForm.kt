package com.example.pov.ui.design.component.pov

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.data.data.model.pov.PoV
import com.example.pov.R


@Composable
fun PoVForm(
    modifier: Modifier = Modifier,
    poV: PoV,
    onValueChange: (PoV) -> Unit,
    isError: Boolean
) {
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(id = R.dimen.padding_extra_small),
            horizontal = dimensionResource(id = R.dimen.padding_extra_small)
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_extra_small)
        )
    ) {
        PoVTitle(
            modifier = Modifier
                .fillMaxWidth(),
            title = poV.title,
            onValueChange = { onValueChange(poV.copy(title = it)) },
            isError = isError
        )
        HorizontalDivider()
        PoVPoints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            points = poV.points,
            onValueChange = { onValueChange(poV.copy(points = it)) },
            isError = isError,
        )
    }
}