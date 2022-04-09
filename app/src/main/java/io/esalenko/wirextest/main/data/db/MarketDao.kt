package io.esalenko.wirextest.main.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.esalenko.wirextest.main.data.model.MarketEntity

@Dao
interface MarketDao {

    @Query("SELECT * FROM market")
    fun pagingSource(): PagingSource<Int, MarketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMarkets(markets: List<MarketEntity>)

    @Query("DELETE FROM market")
    fun clear()

}
