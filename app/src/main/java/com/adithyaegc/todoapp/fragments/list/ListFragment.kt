package com.adithyaegc.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adithyaegc.todoapp.R
import com.adithyaegc.todoapp.data.models.ToDo
import com.adithyaegc.todoapp.data.viewmodel.ToDoViewModel
import com.adithyaegc.todoapp.databinding.FragmentListBinding
import com.adithyaegc.todoapp.fragments.SharedViewModel
import com.adithyaegc.todoapp.fragments.list.adapter.ListAdapter
import com.adithyaegc.todoapp.util.hideKeyboard
import com.adithyaegc.todoapp.util.observeOnce
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: ListAdapter
    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)


        listAdapter = ListAdapter(requireContext())

        hideKeyboard(requireActivity())

        binding.rView.apply {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            swipeToDelete(binding.rView)
            itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }

        }

        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkDatabaseEmpty(data)
            listAdapter.setData(data)
        })

        sharedViewModel.dataVisibility.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })


        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataIv.visibility = View.VISIBLE
            binding.noDataTv.visibility = View.VISIBLE
        } else {
            binding.noDataIv.visibility = View.INVISIBLE
            binding.noDataTv.visibility = View.INVISIBLE
        }

    }

    private fun swipeToDelete(rv: RecyclerView) {
        val swipeToDelete: SwipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = listAdapter.listItems[viewHolder.adapterPosition]
                todoViewModel.deleteItem(itemToDelete)
                undoDeletedItem(viewHolder.itemView, itemToDelete)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun undoDeletedItem(view: View, deletedItem: ToDo) {
        val snackBar = Snackbar.make(
            view, "Deleted ${deletedItem.title}",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            todoViewModel.insert(deletedItem)
        }
        snackBar.show()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
//        if (query != null) {
//            searchThroughDatabase(query)
//        }
        return true;
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)

        }
        return true;
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        todoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner, Observer { list ->
            list.let {
                listAdapter.setData(it)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAllItems()
            R.id.menu_high_priority -> todoViewModel.sortByHighPriority.observe(
                viewLifecycleOwner,
                Observer { listAdapter.setData(it) })
            R.id.menu_low_priority -> todoViewModel.sortByLowPriority.observe(
                viewLifecycleOwner,
                Observer { listAdapter.setData(it) })
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllItems() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAllItems()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete all items?")
        builder.setMessage("Are you sure you want to delete all items.")
        builder.create().show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}