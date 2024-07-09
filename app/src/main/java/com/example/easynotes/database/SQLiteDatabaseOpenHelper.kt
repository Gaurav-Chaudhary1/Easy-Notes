package com.example.easynotes.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDatabaseOpenHelper(
    context: Context
) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "NOTES_DATABASE"
        private const val DATABASE_TABLE = "NOTES_DATABASE_TABLE"
        private const val DATABASE_ID = "_id"
        private const val DATABASE_TITLE = "TITLE"
        private const val DATABASE_DESCRIPTION = "DESCRIPTION"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val data = "CREATE TABLE $DATABASE_TABLE($DATABASE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DATABASE_TITLE TEXT, $DATABASE_DESCRIPTION TEXT)"
        db?.execSQL(data)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
        onCreate(db)
    }

    // Inserting data
    fun insertData(title: String, description: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(DATABASE_TITLE, title)
            put(DATABASE_DESCRIPTION, description)
        }
        db.insert(DATABASE_TABLE, null, contentValues)
        db.close()
    }

    // Getting all data
    @SuppressLint("Recycle")
    fun getAllData(): List<Note> {
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DATABASE_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DATABASE_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DATABASE_DESCRIPTION))

            val note = Note(id, title, description)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }

    // Deleting data
    fun deleteData(noteId: Int) {
        val db = writableDatabase
        db.delete(DATABASE_TABLE, "$DATABASE_ID = ?", arrayOf(noteId.toString()))
        db.close()
    }

    // Updating data
    fun updateData(note: Note) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(DATABASE_TITLE, note.title)
            put(DATABASE_DESCRIPTION, note.description)
        }
        db.update(DATABASE_TABLE, contentValues, "$DATABASE_ID = ?", arrayOf(note.id.toString()))
        db.close()
    }

    // Getting note by ID
    fun getNoteByID(noteID: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE WHERE $DATABASE_ID = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(noteID.toString()))

        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(DATABASE_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(DATABASE_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(DATABASE_DESCRIPTION))

        cursor.close()
        db.close()

        return Note(id, title, description)
    }
}
