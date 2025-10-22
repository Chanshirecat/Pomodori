package com.example.pomodori

import android.content.Context
import androidx.fragment.app.Fragment

abstract class SessionFragment(var session: Session) : Fragment() {
    protected lateinit var dbHandler: DatabaseHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHandler = DatabaseHandler(context)
    }

    protected fun writeSessionToDb() {
        session = if (session.id == 0)
            dbHandler.storeSession(session)
        else
            dbHandler.updateSession(session)
    }
}