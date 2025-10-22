package com.example.pomodori

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// NO DB STUFF IN THIS FILE

fun makeNewSession(): Session {
    return Session(
        0,
        name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        0,
        //LocalDate.now().toEpochDay().toInt(),
        phases = listOf() // Phases sind am Anfang leere Liste
    )
}

fun addFocusPhase(session: Session, durationSecs: Int): Session {
    val phase = Phase(
        id = 0,
        type = PhaseType.FocusTime,
        name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        description = "Add Description",
        iconResId = 0,
        durationSecs = durationSecs,
    )
    return session.copy(phases = session.phases + phase)
}

fun addPausePhase(session: Session, durationSecs: Int): Session {
    val phase = Phase(
        id = 0,
        type = PhaseType.PauseTime,
        name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        description = "Add Description",
        iconResId = 0,
        durationSecs = durationSecs,
    )
    return session.copy(phases = session.phases + phase)
}