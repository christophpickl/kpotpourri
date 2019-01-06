package com.github.christophpickl.kpotpourri.common.version

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.web.WebsiteOpener
import mu.KotlinLogging.logger
import java.net.URL
import java.text.MessageFormat
import javax.swing.JOptionPane

object VersionChecker {

    private val log = logger {}

    fun <V : Version<V>> checkAndShowDialog(
        currentVersion: V,
        urlOfLatestVersionFile: URL,
        downloadPattern: String
    ) {
        val result = check(currentVersion, urlOfLatestVersionFile)
        if (result is VersionCheckResult.VersionOutOfDate<V>) {
            val selected = JOptionPane.showOptionDialog(
                null,
                "There is a more recent version available to download.", "Version out of date",
                JOptionPane.OK_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                arrayOf("Download"),
                "Download"
            )
            if (selected == 0) {
                val downloadUrl = URL(MessageFormat.format(downloadPattern, result.latestVersion.toString()))
                WebsiteOpener.open(downloadUrl)
            }
        }
    }

    fun <V : Version<V>> check(
        currentVersion: V,
        urlOfLatestVersionFile: URL
    ): VersionCheckResult<V> {
        log.debug { "Checking latest version from URL: $urlOfLatestVersionFile" }
        val latestVersion = fetchLatestVersion(currentVersion, urlOfLatestVersionFile)
        log.debug { "Current: $currentVersion, latest: $latestVersion" }
        return if (currentVersion < latestVersion) {
            VersionCheckResult.VersionOutOfDate(currentVersion, latestVersion)
        } else {
            VersionCheckResult.VersionUpToDate(currentVersion)
        }
    }

    private fun <V : Version<V>> fetchLatestVersion(current: V, urlOfLatestVersionFile: URL): V {
        val content = urlOfLatestVersionFile.openStream().bufferedReader().use { it.readText() }
        return current.readOther(content)
            ?: throw KPotpourriException("Could not read version file content: [$content]")
    }

}

sealed class VersionCheckResult<V : Version<*>>(
    val currentVersion: V
) {
    class VersionUpToDate<V : Version<*>>(
        currentVersion: V
    ) : VersionCheckResult<V>(currentVersion)

    class VersionOutOfDate<V : Version<*>>(
        currentVersion: V,
        val latestVersion: V
    ) : VersionCheckResult<V>(currentVersion)
}
