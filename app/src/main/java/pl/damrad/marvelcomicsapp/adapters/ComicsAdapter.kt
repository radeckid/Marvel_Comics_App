package pl.damrad.marvelcomicsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
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
        with(comicsList[position]) {
            when (holder.itemViewType) {
                VIEW_ITEM -> {
                    val binding = (holder as ComicsViewHolder).binding
                    binding.comicsImage.load(this!!.imagePath?.replace("portrait_uncanny", "portrait_medium")) {
                        crossfade(true)
                    }

                    binding.comicsTitle.text = title
                    binding.comicsAuthor.text = author

                    val desc = if (description?.length!! > 150) {
                        "${description.subSequence(0, 150)}..."
                    } else {
                        description
                    }
                    binding.comicsDescription.text = desc
                    binding.root.setOnClickListener {
                        clickListener.invoke(comicsList[position]!!)
                    }
                }
                VIEW_LOADING -> {
                    val binding = (holder as ProgressViewHolder).binding
                    binding.progressBar.isIndeterminate = true
                }
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

    class ComicsViewHolder(val binding: ItemComicsBinding) : RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}