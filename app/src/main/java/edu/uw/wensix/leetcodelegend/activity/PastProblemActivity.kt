package edu.uw.wensix.leetcodelegend.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.R
import edu.uw.wensix.leetcodelegend.databinding.ActivityPastProblemBinding
import edu.uw.wensix.leetcodelegend.model.Problem
import kotlinx.coroutines.launch

class PastProblemActivity : AppCompatActivity() {

    private lateinit var llApp: LLApplication
    private lateinit var binding: ActivityPastProblemBinding
    private val dataRepo by lazy { llApp.dataRepository }
    lateinit var problems: List<Problem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastProblemBinding.inflate(layoutInflater).apply { setContentView(root) }
        llApp = this.applicationContext as LLApplication

        loadData()

        
    }

    private fun loadData() {
        lifecycleScope.launch {
            problems = dataRepo.getProblem()
            with(binding) {

            }
        }
    }

}
