package edu.uw.wensix.leetcodelegend.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.adapter.ProblemListAdapter
import edu.uw.wensix.leetcodelegend.databinding.ActivityPastProblemBinding
import edu.uw.wensix.leetcodelegend.model.Inbox
import edu.uw.wensix.leetcodelegend.model.Problem
import kotlinx.coroutines.launch

class PastProblemActivity : AppCompatActivity() {
    private lateinit var adapter: ProblemListAdapter
    private lateinit var llApp: LLApplication
    private lateinit var binding: ActivityPastProblemBinding
    private val dataRepo by lazy { llApp.dataRepository }
    private lateinit var problems: List<Problem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Past Problems"
        binding = ActivityPastProblemBinding.inflate(layoutInflater).apply { setContentView(root) }
        llApp = this.applicationContext as LLApplication

        with(binding) {
            problems = listOf()
            initProblem()
            loadData()

            swipeToRefreshLayout.setOnRefreshListener {
                initProblem()
                loadData()
                Log.i("problem", problems.toString())

                swipeToRefreshLayout.isRefreshing = false
            }
        }

    }

    private fun initProblem() {
        with(binding){
            adapter = ProblemListAdapter(problems)
            pastProblemList.adapter = adapter
            adapter.problemClickedListener = { problem ->

                navigateToProblemDetailActivity(this@PastProblemActivity, problem)
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val inbox: Inbox = dataRepo.getProblem()
            problems = inbox.problems
            adapter.updateProblem(problems)
            Log.i("problem", problems.toString())

//            }.onFailure {
//                Toast.makeText(this@PastProblemActivity, "Error fetching past problems", Toast.LENGTH_SHORT).show()
//            }

        }
    }

}
