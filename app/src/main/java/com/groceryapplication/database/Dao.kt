package com.groceryapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface Dao {
    @Insert(onConflict = REPLACE)
    fun addProduct(data: CartModel)

    @Query("UPDATE CartData SET quantity=:quantity WHERE productId = :id")
    fun update(quantity: Int?, id: Int)
    @Delete
    fun delete(model: CartModel)

    @Query("Select * FROM CartData")
    fun getdata() : List<CartModel>

}