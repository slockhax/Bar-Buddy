package com.example.barbuddy

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class CocktailIngredients(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val available: String
)


//@Entity
//data class Recipes(
//    @PrimaryKey
//    val id : Int,
//    val name: String,
//    val ingredients: String,
//    val tags: String
//)


@Dao
interface IngredientDao {
    @Query("SELECT * FROM CocktailIngredients WHERE type = 'spirits' ")
    fun getSpirits(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'cordials' ")
    fun getCordials(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'mixers' ")
    fun getMixers(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'garnishes' ")
    fun getGarnishes(): List<CocktailIngredients>

    @Insert
    fun addIngredient(item: CocktailIngredients)

    @Update
    fun updateInventory(item: CocktailIngredients)
}

@Database(entities = [CocktailIngredients::class], version = 1, exportSchema = false)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun IngredientDao(): IngredientDao

    companion object {
        fun create(context: Context): MyAppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MyAppDatabase::class.java,
                "MainDB"
            )
                .createFromAsset("database/dataSource.db")
                .allowMainThreadQueries()
                .build()
        }
    }
}














