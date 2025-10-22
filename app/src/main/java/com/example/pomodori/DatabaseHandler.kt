package com.example.pomodori

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


private data class PhaseEntity(
    val id: Int,
    val type: String,
    val name: String,
    val description: String,
    val iconResId: Int,
    val durationSecs: Int,
    val sessionId: Int
)

private data class SessionEntity(
    val id: Int,
    val name: String,
    //epoch date
    val date: Int,
)

private fun phaseToEntity(sessionId: Int, phase: Phase): PhaseEntity {
    return PhaseEntity(
        phase.id,
        phase.type.toString(),
        phase.name,
        phase.description,
        phase.iconResId,
        phase.durationSecs,
        sessionId
    )
}

private fun sessionToEntity(session: Session): SessionEntity {
    return SessionEntity(
        session.id,
        session.name,
        session.date,
    )
}

private fun phaseFromEntity(phaseEntity: PhaseEntity): Phase {
    return Phase(
        phaseEntity.id,
        PhaseType.valueOf(phaseEntity.type),
        phaseEntity.name,
        phaseEntity.description,
        phaseEntity.iconResId,
        phaseEntity.durationSecs
    )
}

private fun sessionFromEntity(phases: List<Phase>, sessionEntity: SessionEntity): Session {
    return Session(
        sessionEntity.id,
        sessionEntity.name,
        sessionEntity.date,
        phases
    )
}

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, dbName, null, 1) {

    companion object DBConfiguration {
        private const val dbName = "DBPomodori"
        private const val phaseTableName = "phases"
        private const val sessionTableName = "sessions"
        private const val idColumnName = "id"
        private const val typeColumnName = "type"
        private const val nameColumnName = "name"
        private const val dateColumnName = "date"
        private const val descriptionColumnName = "description"
        private const val durationSecsColumnName = "durationSecs"
        private const val iconResIdColumnName = "iconResId"
        private const val sessionIdColumnName = "sessionID"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $sessionTableName ($idColumnName INTEGER PRIMARY KEY, $dateColumnName INTEGER, $nameColumnName TEXT);")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $phaseTableName($idColumnName INTEGER PRIMARY KEY, $typeColumnName TEXT, $nameColumnName TEXT, $descriptionColumnName TEXT, $iconResIdColumnName INTEGER, $durationSecsColumnName INTEGER, $sessionIdColumnName INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE $sessionTableName")
        db?.execSQL("DROP TABLE $phaseTableName")
    }

    fun storeSession(session: Session): Session {
        val db = this.writableDatabase
        val sessionEntity = sessionToEntity(session)
        val sessionValues = ContentValues().apply {
            put(nameColumnName, sessionEntity.name)
            put(dateColumnName, sessionEntity.date)
        }
        val sessionId = db.insert(sessionTableName, null, sessionValues).toInt()

        val newPhases = session.phases.map { storePhase(it, sessionId, db) }
        return session.copy(id = sessionId, phases = newPhases)
    }

    fun storePhase(phase: Phase, sessionId: Int, db: SQLiteDatabase): Phase {
        val phaseEntity = phaseToEntity(sessionId, phase)
        val phaseValues = ContentValues().apply {
            put(typeColumnName, phaseEntity.type)
            put(nameColumnName, phaseEntity.name)
            put(descriptionColumnName, phaseEntity.description)
            put(iconResIdColumnName, phaseEntity.iconResId)
            put(durationSecsColumnName, phaseEntity.durationSecs)
            put(sessionIdColumnName, phaseEntity.sessionId)
        }
        val newId = db.insert(phaseTableName, null, phaseValues).toInt()
        return phase.copy(id = newId)
    }

    fun readSession(sessionId: Int): Session? {
        val db = this.writableDatabase
        val sessionProjection = arrayOf(idColumnName, nameColumnName, dateColumnName)
        val sessionSelection = "$idColumnName = ?"
        val sessionSelectionArgs = arrayOf(sessionId.toString())
        val sessionCursor = db.query(
            sessionTableName,
            sessionProjection,
            sessionSelection,
            sessionSelectionArgs,
            null,
            null,
            null
        )


        var sessionEntity: SessionEntity? = null
        with(sessionCursor) {
            if (moveToNext()) {
                sessionEntity = SessionEntity(
                    getLong(getColumnIndexOrThrow(idColumnName)).toInt(),
                    getString(getColumnIndexOrThrow(nameColumnName)),
                    getInt(getColumnIndexOrThrow(dateColumnName))
                )
            }
        }
        sessionCursor.close()

        if (sessionEntity == null) return null

        val phases = getPhasesForSession(sessionId, db)
        return sessionFromEntity(phases, sessionEntity!!)
    }

    fun readPhase(phaseId: Int): Phase? {
        val db = this.readableDatabase
        val phaseProjection =
            arrayOf(
                idColumnName,
                typeColumnName,
                nameColumnName,
                descriptionColumnName,
                iconResIdColumnName,
                durationSecsColumnName,
                sessionIdColumnName
            )
        val phaseSelection = "$idColumnName = ?"
        val phaseSelectionArgs = arrayOf(phaseId.toString())
        val phaseCursor = db.query(
            phaseTableName,
            phaseProjection,
            phaseSelection,
            phaseSelectionArgs,
            null,
            null,
            null
        )


        var phaseEntity: PhaseEntity? = null
        with(phaseCursor) {
            if (moveToNext()) {
                phaseEntity = PhaseEntity(
                    id = getLong(getColumnIndexOrThrow(idColumnName)).toInt(),
                    type = getString(getColumnIndexOrThrow(typeColumnName)),
                    name = getString(getColumnIndexOrThrow(nameColumnName)),
                    description = getString(getColumnIndexOrThrow(descriptionColumnName)),
                    iconResId = getInt(getColumnIndexOrThrow(iconResIdColumnName)),
                    durationSecs = getInt(getColumnIndexOrThrow(durationSecsColumnName)),
                    sessionId = getInt(getColumnIndexOrThrow(sessionIdColumnName))
                )

            }
        }
        phaseCursor.close()

        if (phaseEntity == null) return null

        val phase = phaseFromEntity(phaseEntity!!)

        return phase
    }

    fun readAllSessions(): List<Session> {
        val db = this.readableDatabase
        val sessionProjection = arrayOf(
            idColumnName,
            nameColumnName,
            dateColumnName
        ) //projection --> names of the columns I want to have
        val sessionSelection = null
        val sessionSelectionArgs = null
        val sessionCursor = db.query(
            sessionTableName,
            sessionProjection,
            sessionSelection,
            sessionSelectionArgs,
            null,
            null,
            null
        )

        val allSessions = mutableListOf<Session>()
        with(sessionCursor) {
            while (moveToNext()) {
                val sessionEntity = SessionEntity(
                    getLong(getColumnIndexOrThrow(idColumnName)).toInt(),
                    getString(getColumnIndexOrThrow(nameColumnName)),
                    getInt(getColumnIndexOrThrow(dateColumnName))

                )

                val phases = getPhasesForSession(sessionEntity.id, db)
                allSessions.add(sessionFromEntity(phases, sessionEntity))
            }
        }
        sessionCursor.close()

        return allSessions
    }

    private fun getPhasesForSession(sessionId: Int, db: SQLiteDatabase): List<Phase> {

        val phaseProjection = arrayOf(
            idColumnName,
            typeColumnName,
            nameColumnName,
            descriptionColumnName,
            iconResIdColumnName,
            durationSecsColumnName,
            sessionIdColumnName
        )
        val phaseSelection = "$sessionIdColumnName = ?"
        val phaseSelectionArgs = arrayOf(sessionId.toString())
        val phaseCursor = db.query(
            phaseTableName, phaseProjection, phaseSelection, phaseSelectionArgs, null, null, null
        )

        val phaseEntities = mutableListOf<PhaseEntity>()
        with(phaseCursor) {
            while (moveToNext()) {
                phaseEntities.add(
                    PhaseEntity(
                        getLong(getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.idColumnName)).toInt(),
                        getString(getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.typeColumnName)),
                        getString((getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.nameColumnName))),
                        getString((getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.descriptionColumnName))),
                        getInt((getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.iconResIdColumnName))),
                        getInt(getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.durationSecsColumnName)),
                        getInt(getColumnIndexOrThrow(com.example.pomodori.DatabaseHandler.sessionIdColumnName))
                    )
                )
            }
        }
        phaseCursor.close()

        val phases = phaseEntities.map { phaseFromEntity(it) }

        return phases
    }

    fun updatePhase(phase: Phase, sessionId: Int, db: SQLiteDatabase) {
        if (phase.id == 0) throw Exception("You're trying to update a new phase! Use storePhase instead")
        val phaseEntity = phaseToEntity(sessionId, phase)
        val phaseValues = ContentValues().apply {
            put(typeColumnName, phaseEntity.type)
            put(nameColumnName, phaseEntity.name)
            put(descriptionColumnName, phaseEntity.description)
            put(iconResIdColumnName, phaseEntity.iconResId)
            put(durationSecsColumnName, phaseEntity.durationSecs)
            put(sessionIdColumnName, phaseEntity.sessionId)
        }
        db.update(phaseTableName, phaseValues, "$idColumnName = ?", arrayOf(phase.id.toString()))
    }

    fun updatePhaseDetails(phaseId: Int, name: String, description: String){
        val db = this.writableDatabase
        val phaseValues = ContentValues().apply {
            put(nameColumnName, name)
            put(descriptionColumnName, description)
        }
        db.update(phaseTableName, phaseValues, "$idColumnName = ?", arrayOf(phaseId.toString()))
    }

    fun updateSession(session: Session): Session {
        if (session.id == 0) throw Exception("You're trying to update a new session! Use storeSession instead")

        val db = this.writableDatabase
        val sessionEntity = sessionToEntity(session)
        val sessionValues = ContentValues().apply {
            put(nameColumnName, sessionEntity.name)
            put(dateColumnName, sessionEntity.date)
        }
        db.update(
            sessionTableName,
            sessionValues,
            "$idColumnName = ?",
            arrayOf(session.id.toString())
        )

        val newPhases = session.phases.map { phase ->
            if (phase.id == 0)
                storePhase(phase, session.id, db)
            else {
                updatePhase(phase, session.id, db)
                phase
            }
        }
        return session.copy(phases = newPhases)
    }

    fun deleteSession(id: String): Unit {
        val db = writableDatabase
        db.delete(sessionTableName, "$idColumnName = ?", arrayOf(id))
    }

    fun deletePhase(id: String): Unit {
        val db = writableDatabase
        db.delete(phaseTableName, "$idColumnName = ?", arrayOf(id))
    }
}