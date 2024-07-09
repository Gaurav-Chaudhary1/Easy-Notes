package com.example.easynotes.notefragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.easynotes.R
import com.example.easynotes.adapter.NotesRCAdapter
import com.example.easynotes.database.SQLiteDatabaseOpenHelper
import com.example.easynotes.databinding.FragmentNotesBinding
import com.google.firebase.auth.FirebaseAuth

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var db: SQLiteDatabaseOpenHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesRCAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize SharedPreferences here
        sharedPreferences = requireActivity().getSharedPreferences("logIn", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        onLogOutButtonClicked()
        setStatusBarColor()
        onAddButtonClicked()
        setupNotesListRecyclerView()
        return binding.root
    }

    private fun setupNotesListRecyclerView() {
        db = SQLiteDatabaseOpenHelper(requireContext())
        notesAdapter = NotesRCAdapter(db.getAllData(), requireContext())
        recyclerView = binding.recyclerView
        recyclerView.adapter = notesAdapter
    }

    private fun onAddButtonClicked() {
        binding.faAddButton.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_addEditNoteFragment)
        }
    }

    private fun onLogOutButtonClicked() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.userLogOut -> {
                    showDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDialog(): Dialog {
        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure want to log out ?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", false)
                apply()
            }
            auth.signOut()
            findNavController().navigate(R.id.action_notesFragment_to_signInFragment)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create().apply { show() }
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

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllData())
    }
}
