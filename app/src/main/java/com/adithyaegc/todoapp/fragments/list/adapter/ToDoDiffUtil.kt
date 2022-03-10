package com.adithyaegc.todoapp.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.adithyaegc.todoapp.data.models.ToDo

class ToDoDiffUtil(
    private val oldList: List<ToDo>,
    private val newList: List<ToDo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size


    override fun getNewListSize() = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority

    }
}