package pl.damrad.marvelcomicsapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentDetailsBinding
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.room.model.Comics
import pl.damrad.marvelcomicsapp.viewmodels.FavoriteViewModel
import kotlin.math.log

class DetailsFragment : Fragment() {
    private var binding: FragmentDetailsBinding? = null
    private var comicsItem: ComicsItem? = null

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var loggedEmail: String? = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comicsItem = arguments?.getParcelable(Key.COMIC_BUNDLE)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = FragmentDetailsBinding.inflate(inflater, container, false).apply {
            favoriteVM = favoriteViewModel
            item = comicsItem
            fragment = this@DetailsFragment
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
            loggedEmail?.let { email ->
                favoriteViewModel.getComicByDetailPath(path, email).observe(viewLifecycleOwner) { comic ->
                    comic?.let {
                        item?.icon = getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24)
                        favoriteViewModel.favoriteComicState.value = true
                    } ?: run {
                        item?.icon = getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24)
                        favoriteViewModel.favoriteComicState.value = false
                    }
                }
            }
        }

        binding?.toolbar?.setOnMenuItemClickListener { item ->
            val comic = Comics(
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
                            comicsItem?.morePath?.let { loggedEmail?.let { email -> favoriteViewModel.deleteComics(it, email) } }
                        }
                        false -> {
                            loggedEmail?.let { favoriteViewModel.insertComics(comic, it) }
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    fun moreDetailsOnClick(path: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(path))
        startActivity(browserIntent)
    }

    private fun setData() {
        val behaviour = binding?.let { BottomSheetBehavior.from(it.bottomSheetBehaviour) }
        behaviour?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}

@BindingAdapter("app:uncannyImageUrl")
fun loadDetailedImage(view: ImageView, imageUrl: String?) {
    view.load(imageUrl?.replace("portrait_uncanny", "detail")) {
        crossfade(true)
    }
}