package pl.damrad.marvelcomicsapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.room.model.Comics

@Dao
interface ComicsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(comics: Comics)

    @Query("DELETE FROM favorite_comics WHERE morePath=:morePath")
    suspend fun deleteComics(morePath: String)

    @Query("SELECT * FROM favorite_comics ORDER BY id ASC")
    fun getAllFavorites(): Flow<List<Comics>>

    @Query("SELECT * FROM favorite_comics WHERE morePath=:morePath")
    fun getComicByDetailPath(morePath: String): LiveData<ComicsItem?>
}