package com.viral.ai.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.viral.ai.model.ChatMessage
import java.io.File

object ExportHelper {
    fun exportToText(context: Context, messages: List<ChatMessage>) {
        val fileName = "chat_export_${System.currentTimeMillis()}.txt"
        val content = messages.joinToString("\n\n") { msg ->
            "${if (msg.isUser) "YOU" else "PAAVANI AI"}:\n${msg.text}"
        }

        val file = File(context.cacheDir, fileName)
        file.writeText(content)

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export Chat"))
    }
}
