package com.example.barbuddy

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class CocktailIngredients(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val available: String
)

@Entity
data class Recipes(
    @PrimaryKey
    val id: Int,
    val name: String,
    val method: String,
    val iceMethod: String,
    val liquorIngredients: String,
    val mixerIngredients: String?,
    val garnishIngredients: String?,
    val boozy: Int,
    val citrusy: Int,
    val frozen: Int,
    val fruity: Int,
    val sweet: Int,
    val tart: Int,
    val warm: Int,
)

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

    @Query("SELECT * FROM CocktailIngredients WHERE name = :name ")
    fun getIngredientByName(name: String): CocktailIngredients

    @Query("UPDATE CocktailIngredients SET available = :isAvailable WHERE name = :itemName")
    fun updateInventory(itemName:String, isAvailable: String)

    @Query("SELECT * FROM Recipes WHERE name = :name")
    fun getRecipeByName(name: String): Recipes

    @Query("SELECT * FROM Recipes ORDER BY name ASC")
    fun getAllRecipes(): List<Recipes>
}

@Database(entities = [CocktailIngredients::class, Recipes::class], version = 2, exportSchema = false)
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
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}