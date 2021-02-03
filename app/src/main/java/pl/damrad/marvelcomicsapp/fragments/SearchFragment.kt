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
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentSearchBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ComicsAdapter
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setSearchView()
        binding.searchInfo.text = getString(R.string.start_typing_to_find_a_particular_comics)
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.connectionState.observe(viewLifecycleOwner) {
            binding.connectionState.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    private fun setSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    loadingData(true)
                    mainViewModel.setOffsetTitle(0)
                    mainViewModel.getAllComicsByTitle(query.trim())
                } else {
                    loadingData(false)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun initRecyclerView() {
        adapter = ComicsAdapter() { item ->
            val bundle = Bundle()
            bundle.putParcelable(Key.COMIC_BUNDLE, item)
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }

        binding.recyclerView.adapter = adapter

        var isLoading = false
        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                adapter.setLoadingBar(true)
                mainViewModel.nextOffsetTitle()
                mainViewModel.getAllComicsByTitle(binding.searchView.query.toString().trim())
            }
        })

        mainViewModel.listOfComicsByTitle.observe(viewLifecycleOwner) {
            loadingData(false)
            adapter.setLoadingBar(false)

            binding.infoLayout.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE


            val list = ComicItemCreator.createComicItem(it)

            adapter.setList(list)
            if (list.isEmpty()) {
                binding.infoLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.searchInfo.text = getString(R.string.there_is_not_comic_book) + binding.searchView.query.trim() + getString(R.string.in_our_library)
            }
        }
    }

    fun loadingData(isLoading: Boolean) {
        if (isLoading) {
            binding.infoLayout.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.searchLoadingPB.visibility = View.VISIBLE
        } else {
            binding.infoLayout.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
            binding.searchLoadingPB.visibility = View.GONE
        }
    }
}