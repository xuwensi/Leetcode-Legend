package edu.uw.wensix.leetcodelegend.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import edu.uw.wensix.leetcodelegend.R
import edu.uw.wensix.leetcodelegend.databinding.ActivityProblemDetailBinding
import edu.uw.wensix.leetcodelegend.model.Problem

const val PROBLEM_KEY = "problem"

fun navigateToProblemDetailActivity(context: Context, problem: Problem) = with(context) {

    val intent = Intent(context, ProblemDetailActivity::class.java).apply {
        val bundle = Bundle().apply {
            putParcelable(PROBLEM_KEY, problem)
        }
        putExtras(bundle)
    }
    startActivity(intent)
}

class ProblemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProblemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityProblemDetailBinding.inflate(layoutInflater).apply { setContentView(root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding) {
            val problem: Problem? = intent.getParcelableExtra(PROBLEM_KEY)
            if (problem != null) {
                title = problem.title
                problemTitle.text = problem.title
                problemId.text = root.context.getString(R.string.prob_detail_id, problem.number)
                problemDifficulty.text =
                    root.context.getString(R.string.prob_detail_difficulty, problem.difficulty)
                problemDateCompleted.text =
                    root.context.getString(R.string.prob_detail_date_completed, problem.date)
                problemTimeTaken.text =
                    root.context.getString(
                        R.string.prob_detail_duration,
                        problem.durationSecond.toString()
                    )

                if (problem.notifyDate == null) {
                    problemReviewDate.text =
                        root.context.getString(R.string.prob_detail_review_date, "None")
                } else {
                    problemReviewDate.text =
                        root.context.getString(R.string.prob_detail_review_date, problem.notifyDate)
                }

                problemNote.text =
                    root.context.getString(R.string.prob_detail_note, problem.note)
            }

            btnProblemReview.setOnClickListener { navigateToTimerActivity(this@ProblemDetailActivity) }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}