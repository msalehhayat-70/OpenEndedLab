package com.example.openendedlab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewTaskActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_task)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val title = editTextTask.text.toString()
            if (title.isNotEmpty()) {
                saveTask(title)
            }
        }
    }

    private fun saveTask(title: String) {
        val userId = auth.currentUser?.uid ?: return
        val task = Task(title = title)

        firestore.collection("users").document(userId).collection("tasks")
            .add(task)
            .addOnSuccessListener {
                finish()
            }
    }
}