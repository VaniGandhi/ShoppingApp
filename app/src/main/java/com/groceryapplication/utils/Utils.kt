package com.groceryapplication.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.widget.Toast
import com.github.loadingview.LoadingView
import com.groceryapplication.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private var progressDialog: Dialog? = null
var loader: LoadingView? = null
var dialog: Dialog?=null



fun showProgress(context: Context) {
    try {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
        progressDialog = Dialog(context)
        progressDialog!!.setContentView(R.layout.layout_progress_dialog)
        progressDialog!!.setCancelable(false)

        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        loader = progressDialog?.findViewById(R.id.loadingView)
        loader?.start()

        progressDialog!!.show()
    } catch (e: Exception) {
        e.printStackTrace()

    }

}

fun hideProgress() {
    try {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
        loader?.stop()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun getError(throwable: Throwable):String

{
    if (throwable is UnknownHostException) {
        return  "No Internet Connection"
    } else if (throwable is SocketTimeoutException) {
        return  "Server is not responding. Please try again"
    } else if (throwable is ConnectException) {
        return  "Failed to connect server"
    } else {
        return  "something went wrong !! please try again"
    }
}
fun Context?.isInternetConnected() : Boolean{
    val mManager = (this?.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager)
    val networkInfo = mManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Context?.showToast(message: String, length: Int){
    Toast.makeText(this, message, length).show()
}

fun Context.showInternetOff(){
    this.showToast(this.getString(R.string.unable_to_connect_to_internet), Toast.LENGTH_LONG)
}