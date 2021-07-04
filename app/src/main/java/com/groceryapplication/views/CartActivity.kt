package com.groceryapplication.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.groceryapplication.R
import com.groceryapplication.Repository
import com.groceryapplication.adapter.CartAdapter
import com.groceryapplication.database.CartModel
import com.groceryapplication.database.Dao
import com.groceryapplication.database.Database
import com.groceryapplication.databinding.ActivityCartBinding
import com.groceryapplication.viewModel.ProductViewModel
import com.groceryapplication.viewModelFactory.CommonViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity(), CartAdapter.onItemClickListener {

    var binding:ActivityCartBinding?=null
    private lateinit var dao: Dao
    var viewModel:ProductViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_cart)
        init()
    }

    private  fun init()
    {

        //****************Intializing dao ****************//
        dao = Database.getDatabase(this).dao()
        val viewModelFactory= CommonViewModelFactory(application = application, repository = Repository(),dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ProductViewModel::class.java)
        binding!!.activity=this
        getCartData()






    }

    private  fun getCartData()
    {
        GlobalScope.launch(Dispatchers.IO) {

            val datalist = arrayListOf<CartModel>()

            val list = dao.getdata()

            for (i in list.indices) {
                datalist.add(list.get(i))

            }
            withContext(Dispatchers.Main) {
                //***************Setting adapter****************//
                val adapter = CartAdapter(this@CartActivity, datalist)
                adapter.setOnClickListener(this@CartActivity)
                binding!!.rvCart.adapter = adapter

                var totalPrice:Int?=0
                for( i in datalist.indices)
                {
                    val price=datalist[i].price.substring(1)
                    val newPrice=price.replace(",","")
                    val priceInt=newPrice.toInt()
                    val singleProductPrice=priceInt*datalist[i].quantity
                    totalPrice=totalPrice?.plus(singleProductPrice)
                }
                binding!!.tvTotal.text="₹${totalPrice.toString()}"





            }

        }
    }

    override fun onItemClickAdd(body: CartModel, quantity: Int) {
        val model=CartModel(body.name, body.image,body.description,quantity,body.price)
        model.productId=body.productId
        viewModel!!.saveData(model)
        getCartData()
    }

    override fun onItemClickDelete(body: CartModel) {
        val model=CartModel(body.name, body.image,body.description,0,body.price)
        model.productId=body.productId
        viewModel!!.deleteData(model)
        getCartData()
    }

    override fun onItemClickUpdate(body: CartModel, quantity: Int) {
        viewModel!!.updateData(quantity, body.productId!!)
        getCartData()
    }
}