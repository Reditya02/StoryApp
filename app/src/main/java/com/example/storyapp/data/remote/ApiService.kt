package com.example.storyapp.data.remote

import com.example.storyapp.data.entity.AddStoryResponse
import com.example.storyapp.data.entity.LoginResponse
import com.example.storyapp.data.entity.RegisterResponse
import com.example.storyapp.data.entity.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse

    @GET("stories")
    fun getStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 30,
        @Query("location") location: Int? = 1
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Call<AddStoryResponse>
}