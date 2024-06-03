package com.pr.menusandnav.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubscribeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Manage Subscription")
        // 카드 형식의 컴포저블
        Card(
            modifier = modifier.padding(8.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = modifier.padding(8.dp)
            ) {
                Column {
                    Text(text = "Musical")
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Free Tier")
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,

                        )
                    }
                }
                Divider(thickness = 1.dp, modifier = modifier.padding(horizontal = 8.dp))
                Row(
                    modifier = modifier.padding(vertical = 16.dp)
                ) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                    Text(text = "Get a Plan")
                }
            }
        }
    }
}
