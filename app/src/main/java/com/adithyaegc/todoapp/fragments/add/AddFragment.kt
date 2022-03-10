package com.adithyaegc.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adithyaegc.todoapp.R
import com.adithyaegc.todoapp.databinding.FragmentAddBinding
import com.adithyaegc.todoapp.fragments.SharedViewModel
import com.adithyaegc.todoapp.data.models.ToDo
import com.adithyaegc.todoapp.data.viewmodel.ToDoViewModel


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        binding.addSpinner.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {

        val title = binding.addTitle.text.toString()
        val description = binding.addDescription.text.toString()
        val priority = binding.addSpinner.selectedItem.toString()

        val validation = sharedViewModel.checkValidation(title, description)

        if (validation) {
            val insertData = ToDo(
                0,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            todoViewModel.insert(insertData)
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}