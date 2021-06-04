package edu.uw.wensix.leetcodelegend.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
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
                Log.i("problem", problems.toString())

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
            var keyword = searchBar.text.toString().lowercase()

            if (keyword == null || keyword.equals("")) {
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
        adapter.updateProblem(problems)
    }

}

private val problemsJsonString = """
    {
      "title" : "Leetcode Legend data",

      "problems": [
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
            "id": 5,
            "number": 5,
            "title": "Longest Palindromic Substring",
            "date": "01/23/2021",
            "difficulty": "medium",
            "note": "avoid unnecessary re-computation while validating palindromes. Consider the case ababa. If we already knew that bab is a palindrome, it is obvious that ababa must be a palindrome since the two left and right end letters are the same.",
            "durationSecond": "498",
            "notifyDate": "03/23/2021"
          }
        ]
  }
""".trimIndent()
