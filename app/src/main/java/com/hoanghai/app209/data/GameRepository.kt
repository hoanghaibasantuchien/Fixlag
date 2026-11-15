package com.hoanghai.app209.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo

object GameRepository {

    fun listInstalledGames(ctx: Context): List<GameInfo> {
        val pm = ctx.packageManager
        return pm.getInstalledApplications(0)
            .asSequence()
            .filter { info ->
                // Lọc app có category GAME
                (info.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                pm.getLaunchIntentForPackage(info.packageName) != null
            }
            .map { info ->
                GameInfo(
                    packageName = info.packageName,
                    label = pm.getApplicationLabel(info).toString(),
                    icon = pm.getApplicationIcon(info)
                )
            }
            .sortedBy { it.label.lowercase() }
            .toList()
    }

    fun launch(ctx: Context, pkg: String): Boolean {
        val i: Intent = ctx.packageManager.getLaunchIntentForPackage(pkg) ?: return false
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(i)
        return true
    }
}
