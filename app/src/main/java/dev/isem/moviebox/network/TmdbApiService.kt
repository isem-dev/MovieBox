package dev.isem.moviebox.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TmdbApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") apiKey: String): PostersResult

    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") apiKey: String): MoviesResult

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieProperty

}

object TmdbApi {
    val retrofitService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}