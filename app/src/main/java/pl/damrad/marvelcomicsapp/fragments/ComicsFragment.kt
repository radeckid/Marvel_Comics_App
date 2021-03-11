package pl.damrad.marvelcomicsapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.PaginationScrollListener
import pl.damrad.marvelcomicsapp.databinding.FragmentComicsBinding
import pl.damrad.marvelcomicsapp.other.ComicItemCreator
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.states.NetworkState
import pl.damrad.marvelcomicsapp.states.UIState
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel

class ComicsFragment : Fragment() {

    var binding: FragmentComicsBinding? = null

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var connectionBar: Snackbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = FragmentComicsBinding.inflate(inflater, container, false).apply {
            mainVM = mainViewModel
            fragment = this@ComicsFragment
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

        mainViewModel.progressBarState.value = true

        setToolbar()
        initRecyclerView()
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.connectionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Connected -> {
                    if (connectionBar.isShown) connectionBar.dismiss()
                }
                is NetworkState.Disconnected -> {
                    if (!connectionBar.isShown) connectionBar.show()
                }
                is NetworkState.Warning -> {
                    Toasty.warning(requireContext(), getString(R.string.something_went_wrong), Toasty.LENGTH_LONG).show()
                }
                is NetworkState.Timeout -> {
                    Toasty.warning(requireContext(), getString(R.string.connection_time_out), Toasty.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createSnackbar(view: View): Snackbar {
        return Snackbar
            .make(view, R.string.check_internet_connection, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.refresh) {
                mainViewModel.getAllComics()
            }
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.RED)
            .setTextColor(Color.WHITE)
    }

    fun onClickBug() {
        throw NullPointerException("Look mom null pointer in kotlin!")
    }

    private fun setToolbar() {
        binding?.toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
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
            mainViewModel.progressBarState.value = false
            adapter.setLoadingBar(false)
            val list = ComicItemCreator.createComicItem(it)
            adapter.addNewComicsToList(list)
        }
    }
}

