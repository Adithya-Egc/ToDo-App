package com.adithyaegc.todoapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adithyaegc.todoapp.R
import com.adithyaegc.todoapp.data.models.ToDo
import com.adithyaegc.todoapp.data.viewmodel.ToDoViewModel
import com.adithyaegc.todoapp.databinding.FragmentUpdateBinding
import com.adithyaegc.todoapp.fragments.SharedViewModel


class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel: ToDoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        val title = binding.updateTitle.setText(args.safeargTodo.title)
        val description = binding.updateDescription.setText(args.safeargTodo.description)
        val priorityData = sharedViewModel.parsePriorityToInt(args.safeargTodo.priority)
        val priority = binding.updateSpinner.setSelection(priorityData)

        binding.updateSpinner.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> updateDatabase()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteItem(args.safeargTodo)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(
                requireContext(),
                "Successfully removed ${args.safeargTodo.title}",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Item?")
        builder.setMessage("Are you sure you want to delete ${args.safeargTodo.title}?")
        builder.create().show()


    }

    private fun updateDatabase() {
        val title = binding.updateTitle.text.toString()
        val description = binding.updateDescription.text.toString()
        val priority = binding.updateSpinner.selectedItem.toString()

        val validation = sharedViewModel.checkValidation(title, description)

        if (validation) {
            val updateItem = ToDo(
                args.safeargTodo.id,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            todoViewModel.update(updateItem)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}