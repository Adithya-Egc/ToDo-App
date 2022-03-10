package com.adithyaegc.todoapp.fragments.list.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adithyaegc.todoapp.R
import com.adithyaegc.todoapp.databinding.RowLayoutBinding
import com.adithyaegc.todoapp.data.models.Priority
import com.adithyaegc.todoapp.data.models.ToDo
import com.adithyaegc.todoapp.fragments.list.ListFragmentDirections

class ListAdapter(private val context: Context) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var listItems: List<ToDo> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.rowTitle.text = listItems[position].title
        holder.binding.rowDescription.text = listItems[position].description
        holder.binding.rowLayout.setOnClickListener {

            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(listItems[position])

            val navController = Navigation.findNavController(
                (holder.binding.root.context) as Activity,
                R.id.navHost
            )
            navController.navigate(action)
        }

        val priority = listItems[position].priority
        when (priority) {
            Priority.HIGH -> holder.binding.rowPriority.setCardBackgroundColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.red
                )
            )
            Priority.MEDIUM -> holder.binding.rowPriority.setCardBackgroundColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.yellow
                )
            )
            Priority.LOW -> holder.binding.rowPriority.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.green
                )
            )
        }
    }

    override fun getItemCount(): Int = listItems.size

    class MyViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(toDo: List<ToDo>) {
        val toDoDiffUtil = ToDoDiffUtil(listItems, toDo)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.listItems = toDo
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}