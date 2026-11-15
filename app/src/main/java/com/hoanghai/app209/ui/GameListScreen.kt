package com.hoanghai.app209.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hoanghai.app209.data.GameInfo
import com.hoanghai.app209.data.GameRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(nav: NavController) {
    val ctx = LocalContext.current
    var games by remember { mutableStateOf<List<GameInfo>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        games = GameRepository.listInstalledGames(ctx)
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game đã cài (${games.size})") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { pad ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(Modifier.padding(pad)) {
                items(games) { g ->
                    ListItem(
                        headlineContent = { Text(g.label) },
                        supportingContent = { Text(g.packageName) },
                        leadingContent = {
                            g.icon?.let { Image(it, contentDescription = null, modifier = Modifier.size(40.dp)) }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
