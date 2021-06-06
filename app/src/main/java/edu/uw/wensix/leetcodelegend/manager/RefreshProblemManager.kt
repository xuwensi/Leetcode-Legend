package edu.uw.wensix.leetcodelegend.manager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.work.*
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.model.Problem
import java.util.concurrent.TimeUnit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val PROBLEM_REFRESH_WORK_TAG = "PROBLEM_REFRESH_WORK_TAG"

class RefreshProblemManager(context: Context) {

    private val llApp: LLApplication = context.applicationContext as LLApplication

    private val workManager: WorkManager = WorkManager.getInstance(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshProblem() {

        var selectedProblem: Problem = llApp.problemToReview
        val currentDateTime = LocalDateTime.now()

        if(currentDateTime.format(DateTimeFormatter.ISO_DATE) = selectedProblem.date) {

            val request = OneTimeWorkRequestBuilder<LeetcodeWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag(PROBLEM_REFRESH_WORK_TAG)
                .build()

            workManager.enqueue(request)
        }
    }


//    fun stopPeriodicallyRefreshing() {
//        workManager.cancelAllWorkByTag(PROBLEM_REFRESH_WORK_TAG)
//    }
//
//    fun doWorkEveryTwoDay() {
//        val request = PeriodicWorkRequestBuilder<LeetcodeWorker>(2, TimeUnit.DAYS)
//            .setConstraints(
//                androidx.work.Constraints.Builder()
//                    .setRequiresBatteryNotLow(true)
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build()
//            )
//            .addTag(PROBLEM_REFRESH_WORK_TAG)
//            .build()
//    }

    private fun isProblemRefreshRunning(): Boolean {
        return workManager.getWorkInfosByTag(PROBLEM_REFRESH_WORK_TAG).get().any {
            when(it.state) {
                WorkInfo.State.RUNNING,
                WorkInfo.State.ENQUEUED -> true
                else -> false
            }
        }
    }




}