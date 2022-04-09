package io.esalenko.wirextest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemoteKey(key: RemoteKeysEntity)

    @Query("SELECT * FROM remote_keys WHERE id=:id")
    fun getRemoteKeys(id: Int): RemoteKeysEntity

    @Query("DELETE FROM remote_keys")
    fun clear()
}
