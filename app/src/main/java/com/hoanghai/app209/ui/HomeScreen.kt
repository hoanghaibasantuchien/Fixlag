package com.hoanghai.app209.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hoanghai.app209.service.OverlayService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    val ctx = LocalContext.current
    var overlayOn by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HoangHai Booster") },
                actions = {
                    IconButton(onClick = { nav.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Cài đặt")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).padding(16.dp).verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Trạng thái", style = MaterialTheme.typography.titleLarge)

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, null)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text("FPS Overlay", style = MaterialTheme.typography.titleMedium)
                            Text("Hiển thị FPS + nhiệt độ khi chơi game",
                                style = MaterialTheme.typography.bodySmall)
                        }
                        Switch(checked = overlayOn, onCheckedChange = { on ->
                            if (on && !Settings.canDrawOverlays(ctx)) {
                                ctx.startActivity(Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${ctx.packageName}")))
                                return@Switch
                            }
                            overlayOn = on
                            val svc = Intent(ctx, OverlayService::class.java)
                            if (on) ctx.startService(svc) else ctx.stopService(svc)
                        })
                    }
                }
            }

            Text("Công cụ", style = MaterialTheme.typography.titleLarge)

            Button(onClick = { nav.navigate(Routes.GAMES) },
                modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.SportsEsports, null)
                Spacer(Modifier.width(8.dp))
                Text("Danh sách game")
            }

            Button(onClick = {
                ctx.startActivity(Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${ctx.packageName}")))
            }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Layers, null)
                Spacer(Modifier.width(8.dp))
                Text("Cấp quyền Overlay")
            }
        }
    }
}
