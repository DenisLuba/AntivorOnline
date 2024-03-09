package delu.game.antivoronline.web

import android.Manifest
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

class DownloadListenerAntivor(
    private val activity: ComponentActivity
) : DownloadListener {
    override fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimetype: String?,
        contentLength: Long
    ) {
//        if (!verifyPermissions()) return
        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        with(request) {
            setMimeType(mimetype)
            val cookies: String = CookieManager.getInstance().getCookie(url)
            addRequestHeader("cookie", cookies)
            addRequestHeader("User-Agent", userAgent)
            setDescription("Downloading file...")
            setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype))
            val downloadManager: DownloadManager = activity.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            Toast.makeText(activity, "Downloading File", Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyPermissions(): Boolean {
        val writePermission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (writePermission == PackageManager.PERMISSION_GRANTED) return true

        ActivityCompat.requestPermissions(
            activity,
            PERMISSION_REQUEST,
            REQUEST_CODE_PERMISSION
        )
        return false
    }

    companion object {

        private const val REQUEST_CODE_PERMISSION = 10;
        private val PERMISSION_REQUEST = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}