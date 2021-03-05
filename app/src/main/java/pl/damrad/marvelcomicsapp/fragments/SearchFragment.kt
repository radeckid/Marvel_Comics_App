package pl.damrad.marvelcomicsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.PaginationScrollListener
import pl.damrad.marvelcomicsapp.databinding.FragmentSearchBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel

class SearchFragment : Fragment() {

    var binding: FragmentSearchBinding? = null
    private lateinit var adapter: ComicsAdapter
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = FragmentSearchBinding.inflate(inflater, container, false).apply {
            mainVM = mainViewModel
        }
        binding = bind
        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setSearchView()
    }

    private fun setSearchView() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    mainViewModel.progressBarState.value = true
                    mainViewModel.setOffsetTitle(0)
                    mainViewModel.getAllComicsByTitle(query.trim())
                } else {
                    mainViewModel.progressBarState.value = false
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    private fun initRecyclerView() {
        adapter = ComicsAdapter() { item ->
            val bundle = Bundle()
            bundle.putParcelable(Key.COMIC_BUNDLE, item)
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }

        binding?.recyclerView?.adapter = adapter

        var isLoading = false
        binding?.recyclerView?.addOnScrollListener(object : PaginationScrollListener(binding?.recyclerView?.layoutManager as LinearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                adapter.setLoadingBar(true)
                mainViewModel.nextOffsetTitle()
                mainViewModel.getAllComicsByTitle(binding?.searchView?.query.toString().trim())
            }
        })

        mainViewModel.listOfComicsByTitle.observe(viewLifecycleOwner) {
            mainViewModel.progressBarState.value = false
            adapter.setLoadingBar(false)

            val list = ComicItemCreator.createComicItem(it)

            adapter.setList(list)
            if (list.isEmpty()) {
                mainViewModel.infoSearchTextState.value = true
                binding?.searchInfo?.text = getString(R.string.there_is_not_comic_book_in_our_library, binding?.searchView?.query?.trim())
            } else {
                mainViewModel.infoSearchTextState.value = false
            }
        }
    }
}