package com.example.easynotes.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easynotes.R
import com.example.easynotes.database.Note
import com.example.easynotes.database.SQLiteDatabaseOpenHelper
import com.example.easynotes.databinding.ListViewBinding

class NotesRCAdapter(private var notes: List<Note>, context: Context):
    RecyclerView.Adapter<NotesRCAdapter.NotesViewHolder>() {

    private var db: SQLiteDatabaseOpenHelper = SQLiteDatabaseOpenHelper(context)

    class NotesViewHolder(val binding: ListViewBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(ListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]

        holder.binding.apply {
            tvTitle.text = note.title
            tvDescription.text = note.description
        }

        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure want to delete ?")
            builder.setPositiveButton("Yes"){dialog, id->
                db.deleteData(note.id)
                refreshData(db.getAllData())
                Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("No"){dialog, id ->
                dialog.dismiss()
            }
            builder.create().show()
            true
        }

        holder.itemView.setOnClickListener {

            val bundle = Bundle().apply {
                putInt("id",note.id)
            }

            holder.itemView.findNavController().navigate(R.id.action_notesFragment_to_updateFragment, bundle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(noteList: List<Note>) {
        notes = noteList
        notifyDataSetChanged()
    }
}