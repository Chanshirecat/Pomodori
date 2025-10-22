package com.example.pomodori

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pomodori.databinding.FragmentStatViewBinding


class StatViewFragment : Fragment() {

    private lateinit var binding: FragmentStatViewBinding
    lateinit var db: DatabaseHandler

    private fun switchToEdit() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.example.pomodori.R.id.fragmentContainerStats, EditStatsFragment())
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phaseId = requireActivity().intent.getIntExtra("phaseId", 0)
        val sessionId = requireActivity().intent.getIntExtra("sessionId", 0)
        val phase = db.readPhase(phaseId)!!
        binding.editTitle.text = phase.name
        binding.time.text = phase.durationSecs.toString()
        binding.descriptionEdit.text = phase.description
        binding.editBtn.setOnClickListener {
            switchToEdit()
        }

        val deleteButton = binding.deleteBtn

        deleteButton.setOnClickListener {
            db.deletePhase(phaseId.toString())
            val session = db.readSession(sessionId)!!
            if (session.phases.isEmpty()){
                db.deleteSession(sessionId.toString())
            }
            requireActivity().finish()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        db = DatabaseHandler(requireContext())

    }
}
