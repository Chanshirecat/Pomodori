package com.example.pomodori

fun formatMillis(milliseconds: Long): String {

    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

fun formatSecs(seconds: Long): String {

    return formatMillis(seconds * 1000)
}
