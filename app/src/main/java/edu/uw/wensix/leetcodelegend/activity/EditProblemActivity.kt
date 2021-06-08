package edu.uw.wensix.leetcodelegend.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.R
import edu.uw.wensix.leetcodelegend.databinding.ActivityEditProblemBinding
import edu.uw.wensix.leetcodelegend.model.Problem
import java.text.SimpleDateFormat
import java.util.*

const val PROBLEM_PREF_KEY = "problem preference key"
const val TIME_KEY = "TIME_KEY"

fun navigateToEditProblemActivity(context: Context, time: Long) = with(context) {
//    val intent = Intent(context, EditProblemActivity::class.java).apply {
//        val bundle = Bundle().apply {
//            putParcelable(PROBLEM_KEY, problem)
//        }
//        putExtras(bundle)
//    }
    val intent = Intent(context, EditProblemActivity::class.java).apply {
        val bundle = Bundle().apply {
            putLong(TIME_KEY, time)
        }
        putExtras(bundle)
    }
    startActivity(intent)
}

class EditProblemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProblemBinding
    private val app by lazy { application as LLApplication }
    private val preferences by lazy { app.preferences }
    lateinit var createdProblem: Problem
    private val refreshProblemManager by lazy { app.refreshProblemManager }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProblemBinding.inflate(layoutInflater).apply { setContentView(root) }

        with(binding) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            reminderTag.setOnClickListener {
                val dpd = DatePickerDialog(this@EditProblemActivity, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    reminder.setText("" + mMonth + "/" + mDay + "/" + mYear)
                }, year, month, day)
                dpd.show()
            }

            Log.i("reminder", reminder.text.toString())

            submitBtn.setOnClickListener {
                //create a problem
                createdProblem = Problem(
                    questionNum.text.toString().toInt(),
                    questionNum.text.toString().toInt(),
                    questionName.text.toString(),
                    SimpleDateFormat("MM/dd/yyyy").format(Date()),
                    difficulty.text.toString(),
                    note.text.toString(),
                    intent.getLongExtra(TIME_KEY, 0).toInt()/1000,
                    reminder.text.toString()
                )
                Log.i("problem", createdProblem.toString())

                var gson = Gson()
                var json = gson.toJson(createdProblem)

                //add to preference
                preferences.edit {
                    putString(PROBLEM_PREF_KEY, json)
                }


                app.problemToReview = createdProblem
                refreshProblemManager.reviewProblem()

                navigateToPastProblemActivity(this@EditProblemActivity)

            }

            btnPastProblem.setOnClickListener { navigateToPastProblemActivity(this@EditProblemActivity) }

        }

    }

}
