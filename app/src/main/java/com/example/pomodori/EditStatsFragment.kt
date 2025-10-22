package com.example.pomodori

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pomodori.databinding.FragmentPauseTimeBinding

class EditStatsFragment : Fragment() {

    lateinit var titleInputField: EditText
    lateinit var descriptionEdit: EditText
    lateinit var db: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phaseId = requireActivity().intent.getIntExtra("phaseId", 0)
        db = DatabaseHandler(requireContext())
        val phase = db.readPhase(phaseId)!!

        // finding the Input Field and getting the data from db
        titleInputField = view.findViewById(R.id.editTitle)
        descriptionEdit = view.findViewById(R.id.descriptionEdit)
        titleInputField.text = Editable.Factory.getInstance().newEditable(phase.name)
        descriptionEdit.text = Editable.Factory.getInstance().newEditable(phase.description)

        //finding the Button
        val saveButton = view.findViewById<Button>(R.id.saveBtn)

        saveButton.setOnClickListener {
            save(phaseId)
        }
    }

    private fun save(phaseId: Int) {
        // Get data from input fields
        val title = titleInputField.text.toString()
        val description = descriptionEdit.text.toString()
        db.updatePhaseDetails(phaseId, title, description)

        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_LONG).show()

        switchToView()
    }

    private fun switchToView() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.example.pomodori.R.id.fragmentContainerStats, StatViewFragment())
            .commit()
    }
}
