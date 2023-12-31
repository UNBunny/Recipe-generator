package com.example.recipegenerator.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT dish FROM recipe")
    fun getDish(): MutableList<String>

    @Query("SELECT dishRecipe FROM recipe")
    fun getRecipe(): MutableList<String>

    @Insert
    fun insertItem(recipe: Recipe)

    @Query("DELETE FROM Recipe WHERE dish = :dishName")
    fun deleteRecipeByDishName(dishName: String)
}