package pl.damrad.marvelcomicsapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentDetailsBinding
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.room.model.Comics
import pl.damrad.marvelcomicsapp.viewmodels.FavoriteViewModel

class DetailsFragment : Fragment() {
    private var binding: FragmentDetailsBinding? = null
    private var comicsItem: ComicsItem? = null

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            comicsItem = bundle.getParcelable(Key.COMIC_BUNDLE)
        }
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bind = FragmentDetailsBinding.inflate(inflater, container, false)
        binding = bind
        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.detailImage?.let { ViewCompat.setTransitionName(it, Key.HERO_IMAGE) }

        setData()
        setToolbar()
    }

    private fun setToolbar() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        comicsItem?.morePath?.let { path ->
            val item = binding?.toolbar?.menu?.findItem(R.id.favoriteBtn)
            favoriteViewModel.getComicByDetailPath(path).observe(viewLifecycleOwner) { comic ->
                comic?.let {
                    item?.icon = getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                    favoriteViewModel.favoriteComicState.value = true
                } ?: run {
                    item?.icon = getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24)
                    favoriteViewModel.favoriteComicState.value = false
                }
            }
        }

        binding?.toolbar?.setOnMenuItemClickListener { item ->
            val comic = Comics(
                id = null,
                title = comicsItem?.title,
                author = comicsItem?.author,
                description = comicsItem?.description,
                imagePath = comicsItem?.imagePath,
                morePath = comicsItem?.morePath
            )

            when (item.itemId) {
                R.id.favoriteBtn -> {
                    when (favoriteViewModel.favoriteComicState.value) {
                        true -> {
                            comicsItem?.morePath?.let { favoriteViewModel.deleteComics(it) }
                        }
                        false -> {
                            favoriteViewModel.insertComics(comic)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setData() {
        comicsItem?.let { item ->
            binding?.detailImage?.load(item.imagePath) {
                crossfade(true)
            }
            binding?.comicsTitle?.text = item.title
            binding?.comicsAuthor?.text = item.author
            binding?.comicsDescription?.text = item.description
            binding?.moreBtn?.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.morePath))
                startActivity(browserIntent)
            }
        }

        val behaviour = binding?.let { BottomSheetBehavior.from(it.bottomSheetBehaviour) }
        behaviour?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}