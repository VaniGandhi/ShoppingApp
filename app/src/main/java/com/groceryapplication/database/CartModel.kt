package com.groceryapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartData")
class CartModel ( val name:String,
                  var image:String,
                  var description:String,
                  var quantity:Int,
                  var price:String,

){

    @PrimaryKey
    var productId: Int = 0


}