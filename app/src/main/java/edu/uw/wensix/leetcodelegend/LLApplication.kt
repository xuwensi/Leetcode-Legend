package edu.uw.wensix.leetcodelegend

import android.app.Application

import android.content.Context
import android.content.SharedPreferences
import edu.uw.wensix.leetcodelegend.repository.DataRepository

class LLApplication: Application() {

    lateinit var dataRepository: DataRepository
    lateinit var problemToReview: Problem

    lateinit var preferences: SharedPreferences


    override fun onCreate() {
        super.onCreate()

        this.dataRepository = DataRepository()
        this.preferences = getSharedPreferences("LLApplication", Context.MODE_PRIVATE)

    }

}