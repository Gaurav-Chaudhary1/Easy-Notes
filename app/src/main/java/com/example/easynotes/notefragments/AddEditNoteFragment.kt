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
import com.example.easynotes.database.SQLiteDatabaseOpenHelper
import com.example.easynotes.databinding.FragmentAddEditNoteBinding

class AddEditNoteFragment : Fragment() {
    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: SQLiteDatabaseOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        db = SQLiteDatabaseOpenHelper(requireContext())  // Initialize here
        setStatusBarColor()
        onSaveButtonClicked()
        onBackButtonClicked()
        return binding.root
    }

    private fun onBackButtonClicked() {
        binding.addToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_addEditNoteFragment_to_notesFragment)
        }
    }

    private fun onSaveButtonClicked() {
        binding.addToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.saveButton -> {
                    val title = binding.etTitle.text.toString()
                    val description = binding.etSummary.text.toString()
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        db.insertData(title, description)
                        Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_addEditNoteFragment_to_notesFragment)
                    } else {
                        Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
