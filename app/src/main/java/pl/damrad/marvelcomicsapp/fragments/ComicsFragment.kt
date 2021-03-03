package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.PaginationScrollListener
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentComicsBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.retrofit.NoConnectivityException
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel
import java.io.IOException
import java.lang.NullPointerException

class ComicsFragment : Fragment() {

    private var _binding: FragmentComicsBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentComicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.comicsLoadingPB.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE

        initRecyclerView()
        connectionObserve()

        binding.crashFab.setOnClickListener {
            throw NullPointerException("Look mom null pointer in kotlin!")
        }
    }

    private fun connectionObserve() {
        mainViewModel.connectionState.observe(viewLifecycleOwner) {
            binding.connectionState.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        val adapter = ComicsAdapter() { item ->
            val bundle = Bundle()
            bundle.putParcelable(Key.COMIC_BUNDLE, item)
            findNavController().navigate(R.id.action_comicsFragment_to_detailsFragment, bundle)
        }

        binding.recyclerView.adapter = adapter


        mainViewModel.getAllComics()

        var isLoading = false
        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                adapter.setLoadingBar(true)
                mainViewModel.nextOffset()
                mainViewModel.getAllComics()
            }
        })

        mainViewModel.listOfComics.observe(viewLifecycleOwner) {
            isLoading = false
            adapter.setLoadingBar(false)

            binding.comicsLoadingPB.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            val list = ComicItemCreator.createComicItem(it)

            adapter.addNewComicsToList(list)
        }
    }
}

