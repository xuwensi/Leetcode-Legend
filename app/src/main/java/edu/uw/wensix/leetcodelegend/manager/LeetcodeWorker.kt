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

    private val dataRepository by lazy { dotifyApp.dataRepository }
    private val dotifyApp by lazy { context.applicationContext as LLApplication}
    private val songNotificationManager by lazy { dotifyApp.notificationManager }

    override suspend fun doWork(): Result {

        val allSongs = dataRepository.getProblem()


        songNotificationManager.publishNewSongNotification()

        return Result.success()
    }


}