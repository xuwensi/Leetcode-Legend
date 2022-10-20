package edu.uw.wensix.leetcodelegend.manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.uw.wensix.leetcodelegend.LLApplication;
import edu.uw.wensix.leetcodelegend.R;
import edu.uw.wensix.leetcodelegend.activity.PastProblemActivity
import edu.uw.wensix.leetcodelegend.model.Problem;
import java.util.*
import kotlin.random.Random

private const val NEW_PROBLEM_CHANNEL_ID = "NEW_PROBLEM_CHANNEL_ID"

class ProblemNotificationManager(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val llApp: LLApplication = context.applicationContext as LLApplication

    init {
        // Initialize all channels
        initNotificationChannels()
    }

    fun publishReviewNotification() {
        val problem: Problem? = llApp.problemToReview
        val selectedProblem: Problem
        if (problem?.notifyDate != null) selectedProblem = problem else return

        // Define the intent or action you want when user taps on notification
        val intent = Intent(context, PastProblemActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build information you want the notification to show
        val builder = NotificationCompat.Builder(
            context,
            NEW_PROBLEM_CHANNEL_ID
        )    // channel id from creating the channel
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("Time to review ${selectedProblem.title} !!")
            .setContentText("It's a ${selectedProblem.difficulty} problem")
            .setContentIntent(pendingIntent)    // sets the action when user clicks on notification
            .setAutoCancel(true)    // This will dismiss the notification tap
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Tell the OS to publish the notification using the info
        with(notificationManager) {
            // notificationId is a unique int for each notification that you must define
            notify(selectedProblem.id, builder.build())
        }
    }

    private fun initNotificationChannels() {
        initNewProblemChannel()
    }

    private fun initNewProblemChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Info about the channel
            val name = context.getString(R.string.new_problem)
            val descriptionText = context.getString(R.string.new_problem_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // Create channel object
            val channel = NotificationChannel(NEW_PROBLEM_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Tell the Android OS to create a channel
            notificationManager.createNotificationChannel(channel)
        }
    }


}