package com.hoanghai.app209.util

import java.io.File

object SystemMonitor {

    /** Đọc nhiệt độ CPU (°C) từ thermal_zone */
    fun readTemperature(): Float {
        val zones = (0..10).map { "/sys/class/thermal/thermal_zone$it/temp" }
        for (path in zones) {
            try {
                val raw = File(path).readText().trim().toInt()
                val c = if (raw > 1000) raw / 1000f else raw.toFloat()
                if (c in 10f..120f) return c
            } catch (_: Exception) {}
        }
        return -1f
    }

    /** Đọc FPS thô từ /sys/class/drm (chỉ một số máy hỗ trợ) */
    fun readFps(): Int {
        // Fallback đơn giản: đọc frame count trong drm debugfs
        try {
            val files = File("/sys/class/drm").listFiles { f -> f.name.startsWith("card0-") }
            files?.forEach { f ->
                val meas = File(f, "measure_refresh_rate")
                if (meas.exists()) return meas.readText().trim().toIntOrNull() ?: 0
            }
        } catch (_: Exception) {}
        return 0
    }

    /** Ping 1 host (ms) */
    fun ping(host: String = "8.8.8.8"): Long {
        return try {
            val p = Runtime.getRuntime().exec("ping -c 1 -W 1 $host")
            val out = p.inputStream.bufferedReader().readText()
            Regex("time=([\\d.]+)").find(out)?.groupValues?.get(1)?.toFloat()?.toLong() ?: -1L
        } catch (_: Exception) { -1L }
    }
}
