package com.example.pomodori


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class StatsActivity : AppCompatActivity() {
    val db = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        findViewById<RecyclerView>(R.id.sessionsRecycler).let {
            val sessions = db.readAllSessions()
            it.adapter = SessionsAdapter(sessions) { sessionId, clickedPhase ->

                val intentVariable = Intent(this, SingleStatActivity::class.java)
                intentVariable.putExtra("phaseId", clickedPhase.id)
                intentVariable.putExtra("sessionId", sessionId)

                startActivity(intentVariable)
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}