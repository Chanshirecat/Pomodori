package com.example.pomodori

enum class PhaseType {
    FocusTime,
    PauseTime
}

data class Phase(
    val id: Int,
    val type: PhaseType,
    val name: String,
    val description: String,
    val iconResId: Int,
    val durationSecs: Int
)

data class Session(
    val id: Int,
    val name: String,
    //epoch date
    val date: Int,
    val phases: List<Phase>
)
