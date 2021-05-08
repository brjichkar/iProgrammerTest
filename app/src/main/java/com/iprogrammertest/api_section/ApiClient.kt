
package com.iprogrammertest.api_section

import com.iprogrammertest.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    private val baseUrl = BuildConfig.BASE_URL
    private lateinit var retrofit: Retrofit

    /**
     *  @Function : okHttpClient()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get okHttpClient client to trigger api call
     */
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     *  @Function : getClient()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get retrofit client to trigger api call
     */
    fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

}
