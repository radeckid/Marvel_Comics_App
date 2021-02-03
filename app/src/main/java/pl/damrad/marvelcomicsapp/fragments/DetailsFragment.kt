package pl.damrad.marvelcomicsapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.transition.TransitionInflater
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.activity.MainActivity
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.FragmentDetailsBinding
import pl.damrad.marvelcomicsapp.other.Key


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var comic: ComicsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            comic = bundle.getParcelable(Key.COMIC_BUNDLE)
        }
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.detailImage, Key.HERO_IMAGE)

        setData()
        setToolbar()
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData() {
        comic?.let { item ->
            binding.detailImage.load(item.imagePath) {
                crossfade(true)
            }
            binding.comicsTitle.text = item.title
            binding.comicsAuthor.text = item.author
            binding.comicsDescription.text = item.description
            binding.moreBtn.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.morePath))
                startActivity(browserIntent)
            }
        }

        val behaviour = BottomSheetBehavior.from(binding.bottomSheetBehaviour)
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }
}