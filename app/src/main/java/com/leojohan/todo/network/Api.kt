package com.leojohan.todo.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object Api {

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    val tasksWebService: TaskWebService by lazy {
        retrofit.create(TaskWebService::class.java)
    }

    // constantes qui serviront à faire les requêtes
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1MTEsImV4cCI6MTY0NzkzNzY5M30.a7nyAahqHHqwsP_aRz7jn4Sd4whZ_NtCuJrks4BTmR4"

    // on construit une instance de parseur de JSON:
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
            jsonSerializer.asConverterFactory("application/json".toMediaType())

    // client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor { chain ->
                    // intercepteur qui ajoute le `header` d'authentification avec votre token:
                    val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $TOKEN")
                            .build()
                    chain.proceed(newRequest)
                }
                .build()
    }

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
}