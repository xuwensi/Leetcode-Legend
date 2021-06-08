package edu.uw.wensix.leetcodelegend.repository

import edu.uw.wensix.leetcodelegend.model.Inbox
import edu.uw.wensix.leetcodelegend.model.Problem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class DataRepository {
    private val problemService= Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProblemService::class.java)

    suspend fun getProblem() = problemService.getProblem()
}

interface ProblemService{
    @GET("xuwensi/Leetcode-Legend/wensi/sample_data/past_problem.json")
    suspend fun getProblem(): MutableList<Problem>
}