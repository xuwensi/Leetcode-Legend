package edu.uw.wensix.leetcodelegend

import android.app.Application

import android.content.Context
import android.content.SharedPreferences

import edu.uw.wensix.leetcodelegend.manager.ProblemNotificationManager
import edu.uw.wensix.leetcodelegend.manager.RefreshProblemManager
import edu.uw.wensix.leetcodelegend.model.Problem
import edu.uw.wensix.leetcodelegend.repository.DataRepository

class LLApplication: Application() {

    lateinit var dataRepository: DataRepository

    var problemToReview: Problem? = null

    lateinit var preferences: SharedPreferences
    lateinit var notificationManager: ProblemNotificationManager
    lateinit var refreshProblemManager: RefreshProblemManager


    override fun onCreate() {
        super.onCreate()

        this.dataRepository = DataRepository()
        this.preferences = getSharedPreferences("LLApplication", Context.MODE_PRIVATE)
        this.notificationManager = ProblemNotificationManager(this)
        this.refreshProblemManager = RefreshProblemManager(this)
    }

}