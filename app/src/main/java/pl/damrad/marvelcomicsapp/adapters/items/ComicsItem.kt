package pl.damrad.marvelcomicsapp.adapters.items

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ComicsItem(
    val title: String?,
    val author: String?,
    val description: String?,
    val imagePath: String?,
    val morePath: String?
) : Parcelable