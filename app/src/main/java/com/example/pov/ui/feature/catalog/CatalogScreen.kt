package com.example.pov.ui.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.pov.Greeting
import com.example.pov.R

@Composable
fun CatalogRoute(navHostController: NavHostController) {
    CatalogScreen(
        navHostController = navHostController
    )
}

@Composable
fun CatalogScreen(navHostController: NavHostController) {
    CatalogBody(
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun CatalogBody(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding_extra_small)
        ),
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        item { Greeting(name = "Catalog screen") }
        item {
            Card(modifier = Modifier.fillParentMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.storage),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = R.string.storage_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}