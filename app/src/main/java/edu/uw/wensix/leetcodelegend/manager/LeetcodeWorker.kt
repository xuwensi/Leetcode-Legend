package edu.uw.wensix.leetcodelegend.manager


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import edu.uw.wensix.leetcodelegend.LLApplication
import kotlin.random.Random

class LeetcodeWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    private val llApp by lazy { context.applicationContext as LLApplication }
    private val problemNotificationManager by lazy { llApp.notificationManager }

    override suspend fun doWork(): Result {

        problemNotificationManager.publishReviewNotification()

        return Result.success()
    }


}