package pl.damrad.marvelcomicsapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.PaginationScrollListener
import pl.damrad.marvelcomicsapp.databinding.FragmentSearchBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.other.UIState
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel

class SearchFragment : Fragment() {

    var binding: FragmentSearchBinding? = null
    private lateinit var adapter: ComicsAdapter
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var connectionBar: Snackbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = FragmentSearchBinding.inflate(inflater, container, false).apply {
            mainVM = mainViewModel
            lifecycleOwner = viewLifecycleOwner
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
        connectionBar = createSnackbar(view)

        initRecyclerView()
        setSearchView()
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.connectionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Connected -> {
                    if (connectionBar.isShown) connectionBar.dismiss()
                }
                is UIState.Disconnected -> {
                    if (!connectionBar.isShown) connectionBar.show()
                }
                is UIState.Warning -> {
                    Toasty.warning(requireContext(), getString(R.string.something_went_wrong), Toasty.LENGTH_LONG).show()
                }
                is UIState.Timeout -> {
                    Toasty.warning(requireContext(), getString(R.string.connection_time_out), Toasty.LENGTH_LONG).show()
                }
            }
        }
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

    private fun createSnackbar(view: View): Snackbar {
        return Snackbar
            .make(view, R.string.check_internet_connection, Snackbar.LENGTH_INDEFINITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.RED)
            .setTextColor(Color.WHITE)
    }
}