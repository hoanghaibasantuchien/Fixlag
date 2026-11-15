package com.hoanghai.app209.util

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShellExecutor {

    /** Chạy lệnh shell qua Shizuku (nếu có), ngược lại trả về null */
    fun run(cmd: String): String? {
        if (!Shizuku.pingBinder()) return null
        return try {
            val proc = Shizuku.newProcess(arrayOf("sh", "-c", cmd), null, null)
            val out = BufferedReader(InputStreamReader(proc.inputStream)).readText()
            proc.waitFor()
            out
        } catch (e: Exception) {
            e.message
        }
    }

    /** Ví dụ: đổi độ phân giải */
    fun setResolution(w: Int, h: Int) = run("wm size ${w}x$h")

    /** Ví dụ: đổi DPI */
    fun setDensity(dpi: Int) = run("wm density $dpi")

    /** Khôi phục mặc định */
    fun resetDisplay() = run("wm size reset; wm density reset")

    /** Giới hạn FPS (Android 12+) — cần device hỗ trợ */
    fun setPeakFps(fps: Int, pkg: String? = null): String? {
        val key = if (pkg != null) "game_mode_${pkg}" else "peak_refresh_rate"
        return run("settings put system $key $fps")
    }
}
