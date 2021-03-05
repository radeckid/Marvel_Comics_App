package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.PaginationScrollListener
import pl.damrad.marvelcomicsapp.databinding.FragmentComicsBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

class ComicsFragment : Fragment() {

    var binding: FragmentComicsBinding? = null

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = FragmentComicsBinding.inflate(inflater, container, false).apply {
            mainVM = mainViewModel
            fragment = this@ComicsFragment
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

        setToolbar()
        initRecyclerView()
    }

     fun onClickBug(){
        throw NullPointerException("Look mom null pointer in kotlin!")
    }

    private fun setToolbar() {
        binding?.toolbar?.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.logoutBtn -> {
                    mainViewModel.signOut()
                    findNavController().navigate(R.id.action_comicsFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = ComicsAdapter() { item ->
            val bundle = Bundle()
            bundle.putParcelable(Key.COMIC_BUNDLE, item)
            findNavController().navigate(R.id.action_comicsFragment_to_detailsFragment, bundle)
        }

        binding?.recyclerView?.adapter = adapter


        mainViewModel.getAllComics()

        var isLoading = false
        binding?.recyclerView?.addOnScrollListener(object : PaginationScrollListener(binding?.recyclerView?.layoutManager as LinearLayoutManager) {
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
            val list = ComicItemCreator.createComicItem(it)
            adapter.addNewComicsToList(list)
        }
    }
}

