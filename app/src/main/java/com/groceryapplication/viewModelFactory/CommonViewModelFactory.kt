package com.groceryapplication.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.groceryapplication.Repository
import com.groceryapplication.database.Dao
import com.groceryapplication.viewModel.ProductViewModel
import java.lang.IllegalArgumentException

class CommonViewModelFactory(private  val application: Application, private  val repository: Repository,
                             private val dao: Dao
):
    ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductViewModel::class.java))
        {
            return  ProductViewModel(application,repository,dao) as T
        }
        throw  IllegalArgumentException("Class not found")
    }
}