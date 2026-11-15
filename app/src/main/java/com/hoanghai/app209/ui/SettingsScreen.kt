package com.hoanghai.app209.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hoanghai.app209.shizuku.ShizukuManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Shizuku", style = MaterialTheme.typography.titleLarge)
            Text("Shizuku cho phép can thiệp hệ thống không cần root.",
                style = MaterialTheme.typography.bodyMedium)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Đã cài Shizuku:")
                Text(if (ShizukuManager.isBinderAvailable()) "Có" else "Không")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Đã cấp quyền:")
                Text(if (ShizukuManager.isPermissionGranted()) "Rồi" else "Chưa")
            }

            Button(onClick = { ShizukuManager.requestPermission() }, modifier = Modifier.fillMaxWidth()) {
                Text("Xin quyền Shizuku")
            }
            OutlinedButton(onClick = { ShizukuManager.openShizukuApp(ctx) },
                modifier = Modifier.fillMaxWidth()) {
                Text("Mở app Shizuku")
            }

            Divider(Modifier.padding(vertical = 16.dp))
            Text("Giới thiệu", style = MaterialTheme.typography.titleLarge)
            Text("HoangHai Booster v1.0")
            Text("Package: com.hoanghai.app209")
        }
    }
}
