package pl.damrad.marvelcomicsapp.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_comics", indices = [Index(value = ["morePath"], unique = true)])
data class Comics(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String?,
    val author: String?,
    val description: String?,
    val imagePath: String?,
    @ColumnInfo(name = "morePath") val morePath: String?
)
