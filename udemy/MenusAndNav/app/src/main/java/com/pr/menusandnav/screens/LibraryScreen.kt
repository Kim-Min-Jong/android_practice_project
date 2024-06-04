package com.pr.menusandnav.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pr.menusandnav.R

data class Lib(
    @DrawableRes val icon: Int,
    val name: String
)

val libraries = listOf<Lib>(
    Lib(R.drawable.ic_account, "Account"),
    Lib(R.drawable.ic_subscribe, "Subcribe"),
    Lib(R.drawable.ic_browse, "Browse"),
    Lib(R.drawable.ic_music_library, "Library"),
    Lib(R.drawable.ic_music_video, "MV"),
)

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(libraries) { lib ->
            LibItem(lib = lib)
        }
    }
}

@Composable
fun LibItem(
    modifier: Modifier = Modifier,
    lib: Lib
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = lib.icon),
                contentDescription = null,
                modifier = modifier.padding(8.dp)
            )
            Text(text = lib.name)
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,modifier = modifier)
        }

    }
    Divider(color = Color.LightGray)
}
