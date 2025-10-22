package com.example.pomodori

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class PhasesAdapter(
    private val phases: List<Phase>,
    val onItemClicked: (Phase) -> Unit
) : RecyclerView.Adapter<PhasesAdapter.PhasesViewHolder>() {

    class PhasesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val phaseName: TextView = view.findViewById(R.id.phaseName)
        val phaseTime: TextView = view.findViewById(R.id.myTime)
        val phaseCard: CardView = view.findViewById(R.id.recyclerview_card_phases)

    }

    override fun onBindViewHolder(holder: PhasesViewHolder, position: Int) {
        val phase = phases[position]
        val context = holder.phaseCard.context
        holder.phaseName.text = phase.name
        holder.phaseTime.text = formatSecs(phase.durationSecs.toLong())
        val colorResourceId =
            if (phase.type == PhaseType.FocusTime) R.color.red_accent
            else R.color.green_accent
        holder.phaseTime.setTextColor(context.resources.getColor(colorResourceId, null))
        holder.phaseCard.setOnClickListener {
            onItemClicked(phase)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhasesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.phases_recyclerview_content, parent, false)
        return PhasesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return phases.count()
    }
}
