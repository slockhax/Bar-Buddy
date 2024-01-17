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

@Entity
data class CocktailIngredients(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val available: String,
    val coreItem: String,
    val substitute: String?
)

@Entity
data class Recipes(
    @PrimaryKey
    val id: Int,
    val name: String,
    val method: String,
    val iceMethod: String,
    val ingredients: String,
    val garnish: String?,
    val descriptors: String,
    val craftable: Int
)

@Entity
data class Data(
    @PrimaryKey
    val id: Int,
    val dataKey: String,
    val value: String,
)

@Dao
interface IngredientDao {
    @Query("SELECT * FROM CocktailIngredients WHERE type = 'alcohol' " +
            "ORDER BY name COLLATE NOCASE")
    fun getAllSpirits(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'alcohol' AND coreItem = 'true' " +
            "ORDER BY name COLLATE NOCASE")
    fun getCoreSpirits(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'alcohol' AND coreItem = 'false' " +
            "ORDER BY name COLLATE NOCASE")
    fun getNonCoreSpirits(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'mixer' " +
            "ORDER BY name COLLATE NOCASE")
    fun getMixers(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE type = 'garnish' " +
            "ORDER BY name COLLATE NOCASE")
    fun getGarnishes(): List<CocktailIngredients>

    @Query("SELECT * FROM CocktailIngredients WHERE name = :name ")
    fun getIngredientByName(name: String): CocktailIngredients

    @Query("UPDATE CocktailIngredients SET available = :isAvailable WHERE name = :itemName")
    fun updateInventory(itemName:String, isAvailable: String)

    @Query("SELECT * FROM Recipes WHERE name = :name")
    fun getRecipeByName(name: String): Recipes

    @Query("SELECT * FROM Recipes ORDER BY name ASC")
    fun getAllRecipes(): List<Recipes>

    // TODO : Filter needs to account for similar names  e.g.  vodka -> vodka, citron
    @Query("SELECT * FROM Recipes " +
            "WHERE descriptors LIKE '%' || :descriptor || '%' " +
            "AND ingredients LIKE '%' || :ingredient || '%' " +
            "AND craftable IN ('1', :craftable) " +
            "ORDER BY name ASC")
    fun getFilteredRecipes(descriptor: String, ingredient: String, craftable: Int): List<Recipes>

    @Query("SELECT * FROM Recipes WHERE INGREDIENTS LIKE '%' || :ingredientName || '%'")
    fun getRecipesByIngredient(ingredientName: String): List<Recipes>

    @Query("UPDATE Recipes SET craftable = :isCraftable WHERE name = :recipeName")
    fun updateCraftableRecipe(recipeName: String, isCraftable: Int)

    @Query("SELECT * FROM Data WHERE dataKey = 'descriptors'")
    fun getFiltersList(): Data

    @Insert
    fun addIngredient(newItem: CocktailIngredients)
}

@Database(
    entities = [CocktailIngredients::class, Recipes::class, Data::class],
    version = 2,
    exportSchema = false)
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
//                .fallbackToDestructiveMigration()
                .build()
        }
    }
}