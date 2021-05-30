package edu.uw.wensix.leetcodelegend.repository

import edu.uw.wensix.leetcodelegend.model.Problem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//https://raw.githubusercontent.com/xuwensi/Leetcode-Legend/main/sample_data/past_problem.json

class DataRepository {
    private val problemService= Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProblemService::class.java)

    suspend fun getProblem() = problemService.getProblem()
}

interface ProblemService{
    @GET("xuwensi/Leetcode-Legend/main/sample_data/past_problem.json")
    suspend fun getProblem(): List<Problem>
}