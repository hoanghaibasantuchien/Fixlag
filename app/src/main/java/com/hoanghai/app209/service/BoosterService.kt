package com.hoanghai.app209.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hoanghai.app209.util.ShellExecutor

/**
 * Service chạy các lệnh shell qua Shizuku.
 * Gọi trực tiếp từ Activity nếu không cần foreground.
 */
class BoosterService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SET_RES -> {
                val w = intent.getIntExtra("w", 1080)
                val h = intent.getIntExtra("h", 2400)
                ShellExecutor.setResolution(w, h)
            }
            ACTION_SET_DPI -> {
                val dpi = intent.getIntExtra("dpi", 420)
                ShellExecutor.setDensity(dpi)
            }
            ACTION_RESET -> ShellExecutor.resetDisplay()
        }
        stopSelf()
        return START_NOT_STICKY
    }

    companion object {
        const val ACTION_SET_RES = "hh.setres"
        const val ACTION_SET_DPI = "hh.setdpi"
        const val ACTION_RESET = "hh.reset"
    }
}
