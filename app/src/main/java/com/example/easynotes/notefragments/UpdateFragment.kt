package com.example.easynotes.notefragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.easynotes.R
import com.example.easynotes.adapter.NotesRCAdapter
import com.example.easynotes.database.Note
import com.example.easynotes.database.SQLiteDatabaseOpenHelper
import com.example.easynotes.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var db: SQLiteDatabaseOpenHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentUpdateBinding.inflate(layoutInflater)

        //database class
        db = SQLiteDatabaseOpenHelper(requireContext())

        //getting note data from NotesAdapter
        noteId = arguments?.getInt("id") ?: -1

        if (noteId == -1){
            findNavController().popBackStack()
            return binding.root
        }

        //set title and description
        val note = db.getNoteByID(noteId)
        binding.etUpdateTitle.setText(note.title)
        binding.etUpdateSummary.setText(note.description)

        onUpdateSaveButtonClicked()
        onBackButtonClicked()
        setStatusBarColor()
        return binding.root
    }

    private fun onUpdateSaveButtonClicked() {
        binding.updateToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.saveButton -> {
                    val title = binding.etUpdateTitle.text.toString()
                    val description = binding.etUpdateSummary.text.toString()
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val updateNote = Note(noteId, title, description)
                        db.updateData(updateNote)
                        Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_updateFragment_to_notesFragment)
                    } else {
                        Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.updateToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_notesFragment)
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.black)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = 0 // Reset any previous flags to ensure the icons remain white
            }
        }
    }

}