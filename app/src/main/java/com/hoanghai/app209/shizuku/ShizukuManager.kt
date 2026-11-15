package com.hoanghai.app209.shizuku

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import rikka.shizuku.Shizuku

object ShizukuManager {

    private const val REQ_CODE = 1001
    private var ctx: Context? = null
    private var listener: ((Int) -> Unit)? = null

    private val permissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        listener?.invoke(grantResult)
    }

    fun init(context: Context) {
        ctx = context.applicationContext
        try {
            Shizuku.addRequestPermissionResultListener(permissionListener)
        } catch (_: Throwable) {}
    }

    fun isBinderAvailable(): Boolean = try { Shizuku.pingBinder() } catch (_: Throwable) { false }

    fun isPermissionGranted(): Boolean = try {
        isBinderAvailable() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    } catch (_: Throwable) { false }

    fun requestPermission(onResult: ((Boolean) -> Unit)? = null) {
        listener = { code -> onResult?.invoke(code == PackageManager.PERMISSION_GRANTED) }
        try {
            if (isBinderAvailable() && !isPermissionGranted()) {
                Shizuku.requestPermission(REQ_CODE)
            } else {
                onResult?.invoke(isPermissionGranted())
            }
        } catch (_: Throwable) { onResult?.invoke(false) }
    }

    fun openShizukuApp(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: Exception) {}
    }
}
