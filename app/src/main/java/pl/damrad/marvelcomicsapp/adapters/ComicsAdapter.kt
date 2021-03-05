package pl.damrad.marvelcomicsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.databinding.ItemComicsBinding
import pl.damrad.marvelcomicsapp.databinding.ItemLoadingBinding

class ComicsAdapter(
    private var comicsList: ArrayList<ComicsItem?> = arrayListOf(),
    val clickListener: (ComicsItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 0
        private const val VIEW_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_ITEM -> {
                val binding = ItemComicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComicsViewHolder(binding)
            }
            VIEW_LOADING -> {
                val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProgressViewHolder(binding)
            }
            else -> {
                val binding = ItemComicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ComicsViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_ITEM -> {
                val binding = (holder as ComicsViewHolder).binding
                comicsList[position]?.let { item -> holder.bind(item) }
                binding.root.setOnClickListener {
                    comicsList[position]?.let { view -> clickListener.invoke(view) }
                }
            }
            else -> {
                (holder as ProgressViewHolder).binding.executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (comicsList[position] != null) VIEW_ITEM else VIEW_LOADING
    }

    fun addNewComicsToList(data: List<ComicsItem?>) {
        val array = ArrayList<ComicsItem?>()
        array.addAll(comicsList)
        array.addAll(data)
        comicsList = array
        notifyDataSetChanged()
    }

    fun setList(data: ArrayList<ComicsItem?>) {
        comicsList = data
        notifyDataSetChanged()
    }

    fun setLoadingBar(isLoading: Boolean) {
        if (isLoading) {
            comicsList.add(null)
        } else {
            comicsList.remove(null)
        }
    }

    override fun getItemCount(): Int = comicsList.size

    class ComicsViewHolder(val binding: ItemComicsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ComicsItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class ProgressViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}

@BindingAdapter("app:imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    view.load(imageUrl?.replace("portrait_uncanny", "portrait_medium")) {
        crossfade(true)
    }
}

@BindingAdapter("app:shortDesc")
fun shortDesc(view: TextView, description: String?) {
    description?.let{
        view.text = if (description.length > 150) {
            "${description.subSequence(0, 150)}..."
        } else {
            description
        }
    }
}

@BindingAdapter("app:shortAuthors")
fun shortAuthors(view: TextView, authors: String?) {
    authors?.let{
        view.text = if (authors.length > 48) {
            "${authors.subSequence(0, 48)}..."
        } else {
            authors
        }
    }
}