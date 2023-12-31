package com.example.recipegenerator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "dish") val dish: String?,
    @ColumnInfo(name = "dishRecipe") val dishRecipe: String?
)
