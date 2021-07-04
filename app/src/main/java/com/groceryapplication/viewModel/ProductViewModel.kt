package com.groceryapplication.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groceryapplication.Repository
import com.groceryapplication.database.CartModel
import com.groceryapplication.database.Dao
import com.groceryapplication.model.ProductResponseModel
import com.groceryapplication.utils.Resource
import com.groceryapplication.utils.getError
import com.groceryapplication.utils.isInternetConnected
import com.groceryapplication.utils.showInternetOff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(var application: Application, var repository: Repository,private val dao: Dao) :

    ViewModel(){

    var result= MutableLiveData<Resource<ProductResponseModel>>()
    fun getProductList()
    {

        result.value= Resource.loading(data = null)

        viewModelScope.launch (Dispatchers.IO )
        {

            try{
                val response=repository.getProductList()
                withContext(Dispatchers.Main)
                {
                    if(response.code()==200 && response.isSuccessful)
                    {


                            if(response.body()!=null)
                            {
                                result.value= Resource.success(data =response.body()!!, message ="Product list")
                            }



                    }
                    else if(response.code()==204)
                    {
                        result.value=Resource.error(data = null, message = "Product list")
                    }
                    else
                    {
                        result.value=Resource.error(data = null, message = response.message())
                    }
                }
            }
            catch (throwable:Throwable)
            {
                withContext(Dispatchers.Main)
                {
                    result.value=Resource.error(data = null,message = getError(throwable))
                }
            }

        }
    }
    //******************Saving data in background thread**************//
    fun saveData(model: CartModel) {
        if (application.applicationContext.isInternetConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                dao.addProduct(model)

            }


        } else {
            application.applicationContext?.showInternetOff()
        }


    }

    fun updateData(quantity:Int, productId:Int)
    {
        if (application.applicationContext.isInternetConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                dao.update(quantity, productId)

            }


        } else {
            application.applicationContext?.showInternetOff()
        }
    }
    fun deleteData(model: CartModel) {
        if (application.applicationContext.isInternetConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                dao.delete(model)

            }


        } else {
            application.applicationContext?.showInternetOff()
        }


    }

}