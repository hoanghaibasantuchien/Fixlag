package com.hoanghai.app209.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.hoanghai.app209.util.SystemMonitor
import kotlinx.coroutines.*

class OverlayService : Service() {

    private lateinit var wm: WindowManager
    private lateinit var view: TextView
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startAsForeground()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        showOverlay()
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val fps = SystemMonitor.readFps()
                val temp = SystemMonitor.readTemperature()
                val ping = SystemMonitor.ping()
                withContext(Dispatchers.Main) {
                    view.text = "FPS $fps | ${temp.toInt()}°C | ${ping}ms"
                }
                delay(1000)
            }
        }
    }

    private fun showOverlay() {
        val type = if (Build.VERSION.SDK_INT >= 26)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 30; y = 100
        }

        view = TextView(this).apply {
            text = "FPS --"
            setTextColor(Color.GREEN)
            textSize = 14f
            setPadding(24, 12, 24, 12)
            setBackgroundColor(0x80000000.toInt())
        }
        wm.addView(view, params)
    }

    private fun startAsForeground() {
        val chId = "hoanghai_fg"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(
                NotificationChannel(chId, "HoangHai", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val noti = Notification.Builder(this, chId)
            .setContentTitle("HoangHai đang chạy")
            .setContentText("Overlay FPS đang bật")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
        startForeground(1, noti)
    }

    override fun onDestroy() {
        job?.cancel()
        if (::view.isInitialized) try { wm.removeView(view) } catch (_: Exception) {}
        super.onDestroy()
    }
}
