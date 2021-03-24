package pl.damrad.marvelcomicsapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.room.model.Comics

@Dao
interface ComicsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComics(comics: Comics)

    @Query("DELETE FROM favorite_comics WHERE morePath=:morePath AND loggedUser=:user")
    suspend fun deleteComics(morePath: String, user: String)

    @Query("SELECT * FROM favorite_comics WHERE loggedUser=:user ORDER BY id ASC")
    fun getAllFavorites(user: String): Flow<List<Comics>>

    @Query("SELECT * FROM favorite_comics WHERE morePath=:morePath AND loggedUser=:user")
    fun getComicByDetailPath(morePath: String, user: String): LiveData<ComicsItem?>
}