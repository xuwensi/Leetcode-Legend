package edu.uw.wensix.leetcodelegend.manager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.model.Problem
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


private const val PROBLEM_REFRESH_WORK_TAG = "PROBLEM_REFRESH_WORK_TAG"

class RefreshProblemManager(context: Context) {

    private val llApp: LLApplication = context.applicationContext as LLApplication

    private val workManager: WorkManager = WorkManager.getInstance(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun reviewProblem() {

        var problem: Problem? = llApp.problemToReview
        val selectedProblem: Problem
        if (problem?.notifyDate != null) selectedProblem = problem else return

        val createdDate =
            LocalDate.parse(selectedProblem.date, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        val reviewDate =
            LocalDate.parse(selectedProblem.notifyDate, DateTimeFormatter.ofPattern("M/dd/yyyy"))
        val delay = ChronoUnit.DAYS.between(createdDate, reviewDate)

        val request = OneTimeWorkRequestBuilder<LeetcodeWorker>()
            .setInitialDelay(delay, TimeUnit.DAYS)
            .addTag(PROBLEM_REFRESH_WORK_TAG)
            .build()

        workManager.enqueue(request)
    }
}