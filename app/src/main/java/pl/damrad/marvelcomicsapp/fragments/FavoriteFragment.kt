package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.ComicsAdapter
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentFavoriteBinding
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.viewmodels.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private lateinit var adapter: ComicsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding = bind
        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecycler()
    }

    private fun setRecycler() {
        adapter = ComicsAdapter { item ->
            val bundle = Bundle()
            bundle.putParcelable(Key.COMIC_BUNDLE, item)
            findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
        }

        binding?.recyclerView?.adapter = adapter

        favoriteViewModel.getAllComics().observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }
    }
}