package pl.damrad.marvelcomicsapp.adapters.items

import android.os.Parcel
import android.os.Parcelable

class ComicsItem(
    val title: String?,
    val author: String?,
    val description: String?,
    val imagePath: String?,
    val morePath: String?
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(description)
        parcel.writeString(imagePath)
        parcel.writeString(morePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComicsItem> {
        override fun createFromParcel(parcel: Parcel): ComicsItem {
            return ComicsItem(parcel)
        }

        override fun newArray(size: Int): Array<ComicsItem?> {
            return arrayOfNulls(size)
        }
    }
}
