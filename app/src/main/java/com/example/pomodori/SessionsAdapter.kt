package com.example.pomodori

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class SessionsAdapter(
    private val sessions: List<Session>,
    private val onItemClicked: (Int, Phase) -> Unit
) :
    RecyclerView.Adapter<SessionsAdapter.SessionsViewHolder>() {

    class SessionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sessionName: TextView = view.findViewById(R.id.sessionName)

        val phasesRecycler: RecyclerView = view.findViewById(R.id.phasesRecycler)

    }

    override fun onBindViewHolder(holder: SessionsViewHolder, position: Int) {
        val session = sessions[position]
        holder.sessionName.text = session.name
        holder.phasesRecycler.adapter = PhasesAdapter(session.phases) { phase ->
            onItemClicked(session.id, phase)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.sessions_recyclerview_content, parent, false)
        return SessionsViewHolder(view)
    }


    override fun getItemCount(): Int {
        return sessions.count()
    }
}
