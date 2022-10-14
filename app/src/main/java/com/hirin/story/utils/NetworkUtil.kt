package com.hirin.story.utils

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.hirin.story.BuildConfig
import com.hirin.story.utils.constant.type.SharedPrefsKeyEnum
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkUtil {
    fun buildClient(applicationContext: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val requestInterceptor = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            if (SharedPreferencesUtil[SharedPrefsKeyEnum.TOKEN.name, String::class.java, ""]!!.isNotEmpty())
                builder.addHeader("Authorization", "Bearer ${SharedPreferencesUtil[SharedPrefsKeyEnum.TOKEN.name, String::class.java, ""]}")
            val request = builder
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20L, TimeUnit.SECONDS)
        builder.readTimeout(20L, TimeUnit.SECONDS)
        builder.writeTimeout(20L, TimeUnit.SECONDS)
        builder.addInterceptor(requestInterceptor)
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }

    inline fun <reified T> buildService(baseUrl: String, okHttpClient: OkHttpClient): T {
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()

        return retrofit.create(T::class.java)
    }
}