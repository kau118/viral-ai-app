package com.viral.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viral.ai.ui.theme.*

@Composable
fun MarkdownText(text: String, modifier: Modifier = Modifier) {
    val parts = text.split("```")
    
    Column(modifier = modifier) {
        parts.forEachIndexed { index, part ->
            if (index % 2 == 0) {
                renderTextContent(part)
            } else {
                CodeBlockItem(part)
            }
        }
    }
}

@Composable
private fun renderTextContent(content: String) {
    val lines = content.split("\n")
    var inTable = false
    val tableLines = mutableListOf<String>()

    lines.forEach { line ->
        val trimmed = line.trim()
        
        // Simple Table Detection
        if (trimmed.startsWith("|") && trimmed.endsWith("|")) {
            inTable = true
            tableLines.add(trimmed)
        } else {
            if (inTable) {
                MarkdownTable(tableLines)
                tableLines.clear()
                inTable = false
            }
            
            if (trimmed.startsWith("- ") || trimmed.startsWith("* ")) {
                BulletItem(trimmed.substring(2))
            } else if (trimmed.isNotEmpty()) {
                Text(
                    text = parseAnnotatedString(trimmed),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
    if (inTable) {
        MarkdownTable(tableLines)
    }
}

@Composable
fun MarkdownTable(lines: List<String>) {
    // Basic table renderer
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.02f))
    ) {
        lines.forEachIndexed { index, line ->
            val cells = line.split("|").filter { it.isNotEmpty() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (index == 0) Color.White.copy(alpha = 0.05f) else Color.Transparent)
                    .padding(8.dp)
            ) {
                cells.forEach { cell ->
                    Text(
                        text = cell.trim(),
                        modifier = Modifier.weight(1f),
                        style = if (index == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) 
                                else MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
            if (index < lines.size - 1) {
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun BulletItem(content: String) {
    Row(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
        Text("•", color = PrimaryPurple, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        Text(
            text = parseAnnotatedString(content),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Composable
fun CodeBlockItem(code: String) {
    val lines = code.trim().lines()
    val language = if (lines.isNotEmpty() && !lines[0].contains(" ")) lines[0] else "code"
    val content = if (language != "code" && lines.size > 1) lines.drop(1).joinToString("\n") else code.trim()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(language.uppercase(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Black)
            Text("COPY", color = PrimaryPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = content,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFFD4D4D4),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

private fun parseAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        val boldPattern = Regex("\\*\\*(.*?)\\*\\*")
        val italicPattern = Regex("\\*(.*?)\\*")
        
        append(text)
        
        boldPattern.findAll(text).forEach { match ->
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold, color = AccentPink),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
        
        italicPattern.findAll(text).forEach { match ->
            addStyle(
                style = SpanStyle(fontStyle = FontStyle.Italic),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
    }
}
