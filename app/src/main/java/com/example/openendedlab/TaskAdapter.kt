package com.example.openendedlab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskAdapter(private val taskList: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.title
        holder.completed.isChecked = task.isCompleted

        holder.completed.setOnCheckedChangeListener { _, isChecked ->
            updateTaskCompletion(task.id, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    private fun updateTaskCompletion(taskId: String, isCompleted: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).collection("tasks").document(taskId)
            .update("completed", isCompleted)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val completed: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
    }
}