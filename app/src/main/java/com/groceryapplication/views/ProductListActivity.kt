package com.groceryapplication.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.groceryapplication.R
import com.groceryapplication.Repository
import com.groceryapplication.adapter.CartAdapter
import com.groceryapplication.adapter.ProductAdapter
import com.groceryapplication.database.CartModel
import com.groceryapplication.database.Dao
import com.groceryapplication.database.Database
import com.groceryapplication.databinding.ActivityProductListBinding
import com.groceryapplication.model.ProductDetail
import com.groceryapplication.utils.Status
import com.groceryapplication.utils.hideProgress
import com.groceryapplication.utils.showProgress
import com.groceryapplication.viewModel.ProductViewModel
import com.groceryapplication.viewModelFactory.CommonViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListActivity : AppCompatActivity() , ProductAdapter.onItemClickListener{

    var binding:ActivityProductListBinding?=null
    var viewModel:ProductViewModel?=null
    private lateinit var dao: Dao
    val datalist = arrayListOf<CartModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        init()

    }

    private  fun init()
    {
        //****************Intializing dao ****************//
        dao = Database.getDatabase(this).dao()
        //****************Intializing viewModel ****************//
        val viewModelFactory= CommonViewModelFactory(application = application, repository = Repository(),dao)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ProductViewModel::class.java)
        binding!!.viewModel=viewModel
        binding!!.activity=this
        getCartData()

        setObsevers()

        binding!!.ivCart.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        })
    }
    private  fun setObsevers()
    {
        viewModel?.result?.observe(this, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideProgress()
                        var productlist=resource.data?.products!!

                        if(datalist.size>0)
                        {
                            for( i in productlist.indices)
                            {
                                for( j in datalist.indices)
                                {
                                    if(productlist[i].id==datalist[j].productId)
                                    {
                                        productlist[i].quantity=datalist[j].quantity
                                    }
                                }
                            }

                        }


                        val adapter=ProductAdapter(this, productlist)
                        adapter.setOnClickListener(this)
                        val layoutManager=GridLayoutManager(this, 2)
                        binding!!.rvProducts.layoutManager=layoutManager
                        binding!!.rvProducts.adapter=adapter




                    }
                    Status.LOADING -> {
                        showProgress(this)
                    }
                    Status.ERROR -> {
                        hideProgress()


                    }
                }
            }
        })

    }


    override fun onItemClickAdd(body: ProductDetail, quantity: Int) {
        val model=CartModel(body.name!!, body.image!!,body.description!!,quantity,body.price!!)
        model.productId=body.id!!
        viewModel!!.saveData(model)
    }

    override fun onItemClickDelete(body: ProductDetail) {
        val model=CartModel(body.name!!, body.image!!,body.description!!,0,body.price!!)
        model.productId=body.id!!
        viewModel!!.deleteData(model)

    }

    override fun onItemClickUpdate(body: ProductDetail, quantity: Int) {
        viewModel!!.updateData(quantity, body.id!!)
    }

    private  fun getCartData()
    {
        GlobalScope.launch(Dispatchers.IO) {



            val list = dao.getdata()

            for (i in list.indices) {
                datalist.add(list.get(i))

            }
            withContext(Dispatchers.Main) {

                viewModel!!.getProductList()






            }

        }
    }


}