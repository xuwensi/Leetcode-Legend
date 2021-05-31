package edu.uw.wensix.leetcodelegend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Problem(
    val id: Int,
    val number: Int,
    val title: String,
    val date: String,
    val difficulty: String,
    val note: String,
    val durationSecond: Int,
    val notifyDate: String?,
):Parcelable
