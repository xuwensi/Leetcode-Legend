package edu.uw.wensix.leetcodelegend.activity

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

class PastProblemActivity : AppCompatActivity() {
    private lateinit var adapter: ProblemListAdapter
    private lateinit var llApp: LLApplication
    private lateinit var binding: ActivityPastProblemBinding
    private val dataRepo by lazy { llApp.dataRepository }
    private lateinit var problems: List<Problem>
    private var newProblems: MutableList<Problem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Past Problems"
        binding = ActivityPastProblemBinding.inflate(layoutInflater).apply { setContentView(root) }
        llApp = this.applicationContext as LLApplication

        with(binding) {
            problems = listOf()
            initProblem()
            readLocalJsonData()
//            loadData()

            swipeToRefreshLayout.setOnRefreshListener {
                initProblem()

                readLocalJsonData()
//                loadData()

                swipeToRefreshLayout.isRefreshing = false
            }

            icSearch.setOnClickListener { searchProblem() }

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

    private fun loadData() {
        lifecycleScope.launch {
            val inbox: Inbox = dataRepo.getProblem()
            problems = inbox.problems
            adapter.updateProblem(problems)
//            Log.i("problem", problems.toString())

//            }.onFailure {
//                Toast.makeText(this@PastProblemActivity, "Error fetching past problems", Toast.LENGTH_SHORT).show()
//            }

        }
    }

    //for testing and demo purpose
    private fun readLocalJsonData() {
        val gson = Gson()
        val inbox = gson.fromJson(problemsJsonString, Inbox::class.java)
        problems = inbox.problems
        Collections.sort(problems)
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
        "date": "02/20/2221",
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
