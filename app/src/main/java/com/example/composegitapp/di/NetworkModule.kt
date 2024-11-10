package com.example.composegitapp.di

import android.annotation.SuppressLint
import com.example.composegitapp.network.HeaderInterceptor
import com.example.composegitapp.network.IGitHubApi
import com.example.composegitapp.preferences.IAppSettings
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module(includes = [])
class NetworkModule {

    @Provides
    fun provideGitHubApi(okHttpClient: OkHttpClient, appSettings: IAppSettings): IGitHubApi =
        createWebServiceApi(okHttpClient, appSettings)

    @Provides
    fun createOkHttpClient(logInterceptor: HttpLoggingInterceptor,headerInterceptor: HeaderInterceptor): OkHttpClient {
        val httpClient = getUnsafeOkHttpClient().apply {
            addInterceptor(logInterceptor)
            addInterceptor(headerInterceptor)
        }
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.writeTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(Level.BASIC)
        return logging
    }

    private inline fun <reified F> createWebServiceApi(
        okHttpClient: OkHttpClient,
        appSettings: IAppSettings
    ): F {
        val gson = GsonBuilder().create()
        val gsonFactory = GsonConverterFactory.create(gson)
        val retrofit = Retrofit.Builder()
            .baseUrl(appSettings.getCurrentApiUrl())
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)
            .build()
        return retrofit.create(F::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            return OkHttpClient.Builder().apply {
                val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) = Unit
                })

                val sslContext = SSLContext.getInstance("SSL").apply {
                    init(null, trustAllCerts, SecureRandom())
                }
                sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                hostnameVerifier { _, _ -> true }
            }
        } catch (e: Exception) {
            return OkHttpClient.Builder()
        }
    }
}