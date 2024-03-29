package edu.uw.wensix.leetcodelegend.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SortedList
import com.google.gson.Gson
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.adapter.ProblemListAdapter
import edu.uw.wensix.leetcodelegend.databinding.ActivityPastProblemBinding
import edu.uw.wensix.leetcodelegend.model.Inbox
import edu.uw.wensix.leetcodelegend.model.Problem
import kotlinx.coroutines.launch
import java.util.*

fun navigateToPastProblemActivity(context: Context) = with(context) {

    val intent = Intent(context, PastProblemActivity::class.java)

    startActivity(intent)
}

class PastProblemActivity : AppCompatActivity() {
    private lateinit var adapter: ProblemListAdapter
    private lateinit var llApp: LLApplication
    private lateinit var binding: ActivityPastProblemBinding
    private val dataRepo by lazy { llApp.dataRepository }
    private lateinit var problems: MutableList<Problem>
    private var newProblems: MutableList<Problem> = mutableListOf()
    private val preferences by lazy { llApp.preferences }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Past Problems"
        binding = ActivityPastProblemBinding.inflate(layoutInflater).apply { setContentView(root) }
        llApp = this.applicationContext as LLApplication

        with(binding) {
            problems = mutableListOf()
            initProblem()
            readLocalJsonData()

            swipeToRefreshLayout.setOnRefreshListener {
                initProblem()
                readLocalJsonData()
                swipeToRefreshLayout.isRefreshing = false
            }

            icSearch.setOnClickListener { searchProblem() }

            btnTimer.setOnClickListener { navigateToTimerActivity(this@PastProblemActivity) }
            timerText.setOnClickListener { navigateToTimerActivity(this@PastProblemActivity) }
            btnPerformance.setOnClickListener {
                val intent = Intent(this@PastProblemActivity, VisualActivity::class.java)
                startActivity(intent)
            }
            performanceText.setOnClickListener {
                val intent = Intent(this@PastProblemActivity, VisualActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun searchProblem() {
        if (problems.isEmpty()) {
            return
        }

        with(binding) {
            var keyword = ""
            if (searchBar.text != null) {
                keyword = searchBar.text.toString().lowercase()
            }

            if (keyword == "") {
                initProblem()
            } else {
                newProblems = mutableListOf()
                problems.forEach { problem ->
                    if (problem.title.lowercase().contains(keyword) || problem.number.toString()
                            .lowercase()
                            .contains(keyword)
                        || problem.date.lowercase().contains(keyword)
                    ) {
                        newProblems.add(problem)
                    }
                }
                initNewProblem()
            }
        }
    }

    private fun initProblem() {
        with(binding) {
            adapter = ProblemListAdapter(problems)
            pastProblemList.adapter = adapter
            adapter.problemClickedListener = { problem ->
                navigateToProblemDetailActivity(this@PastProblemActivity, problem)
            }
        }
    }

    private fun initNewProblem() {
        with(binding) {
            adapter = ProblemListAdapter(newProblems)
            pastProblemList.adapter = adapter
            adapter.problemClickedListener = { problem ->
                navigateToProblemDetailActivity(this@PastProblemActivity, problem)
            }
        }
    }

    //fetch from github
    private fun loadData() {
        lifecycleScope.launch {
            val problems: MutableList<Problem> = dataRepo.getProblem()
            adapter.updateProblem(problems)
        }
    }

    //for testing and demo purpose
    private fun readLocalJsonData() {
        val gson = Gson()
        val inbox = gson.fromJson(problemsJsonString, Inbox::class.java)
        problems = inbox.problems

        val insertedProblemString = preferences.getString(PROBLEM_PREF_KEY, "")
        if (insertedProblemString != "") {
            val toInsertProblem = gson.fromJson(insertedProblemString, Problem::class.java)
            problems.add(toInsertProblem)
        }
        problems.sort()
        adapter.updateProblem(problems)
    }

}

private val problemsJsonString = """
{
  "title" : "Leetcode Legend data",

  "problems": [
      {
        "id": 100,
        "number": 100,
        "title": "Same Tree",
        "date": "02/20/2021",
        "difficulty": "easy",
        "note": "The simplest strategy here is to use recursion. Check if p and q nodes are not None, and their values are equal. If all checks are OK, do the same for the child nodes recursively.",
        "durationSecond": "423",
        "notifyDate": null
      },
      {
        "id": 1,
        "number": 1,
        "title": "Two Sum",
        "date": "01/20/2021",
        "difficulty": "easy",
        "note": "Brute Force or Two-pass Hash Table",
        "durationSecond": "375",
        "notifyDate": null
      },
      
      {
        "id": 5,
        "number": 5,
        "title": "Longest Palindromic Substring",
        "date": "01/23/2021",
        "difficulty": "medium",
        "note": "avoid unnecessary re-computation while validating palindromes. Consider the case ababa. If we already knew that bab is a palindrome, it is obvious that ababa must be a palindrome since the two left and right end letters are the same.",
        "durationSecond": "498",
        "notifyDate": "03/23/2021"
      },
      {
        "id": 2,
        "number": 2,
        "title": "Add Two Numbers",
        "date": "01/22/2021",
        "difficulty": "medium",
        "note": "Keep track of the carry using a variable and simulate digits-by-digits sum starting from the head of list, which contains the least-significant digit.",
        "durationSecond": "498",
        "notifyDate": "03/22/2021"
      },
      {
        "id": 115,
        "number": 115,
        "title": "Distinct Subsequences",
        "date": "03/01/2021",
        "difficulty": "hard",
        "note": "A string's subsequence is a new string formed from the original string by deleting some (can be none) of the characters without disturbing the remaining characters' relative positions.",
        "durationSecond": "1225",
        "notifyDate": "04/01/2021"
      }
    ]
}
""".trimIndent()
