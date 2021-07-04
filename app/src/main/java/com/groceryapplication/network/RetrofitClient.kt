package com.groceryapplication.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient {
    init    {

        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(200, TimeUnit.MINUTES)
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())

        httpClient.addInterceptor(object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                var request: Request


                    request =  chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "text/plain").build()





                return chain.proceed(request)
            }
        })

        retrofit = Retrofit.Builder()
            .baseUrl("https://www.mocky.io/")
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }



    fun getapi(): ApiInterface {
        return retrofit?.create(ApiInterface::class.java)!!

    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val modified = response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()

            return modified
        }
    }

    companion object{
        private  var retrofit : Retrofit?=null
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance():RetrofitClient{
            if(instance==null){
                instance= RetrofitClient()
            }
            return instance as RetrofitClient


        }
    }
}