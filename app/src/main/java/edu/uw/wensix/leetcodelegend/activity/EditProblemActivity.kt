package edu.uw.wensix.leetcodelegend.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import edu.uw.wensix.leetcodelegend.LLApplication
import edu.uw.wensix.leetcodelegend.R
import edu.uw.wensix.leetcodelegend.databinding.ActivityEditProblemBinding
import edu.uw.wensix.leetcodelegend.model.Problem


fun navigateToEditProblemActivity(context: Context) = with(context){
//    val intent = Intent(context, EditProblemActivity::class.java).apply {
//        val bundle = Bundle().apply {
//            putParcelable(PROBLEM_KEY, problem)
//        }
//        putExtras(bundle)
//    }
    val intent = Intent(context, EditProblemActivity::class.java).apply {
        val bundle = Bundle().apply {
            putString(PROBLEM_KEY, "yes")
        }
        putExtras(bundle)
    }
    startActivity(intent)
}




class EditProblemActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditProblemBinding
    private val app by lazy {application as LLApplication}
    private val preferences by lazy { app.preferences}



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProblemBinding.inflate(layoutInflater).apply { setContentView(root) }
        super.onCreate(savedInstanceState)
        with(binding) {
            binding.submitBtn.setOnClickListener {
                //create a problem

                var problem = Problem(binding.questionNum.toString().toInt(),binding.questionNum.toString().toInt(), binding.questionName.toString(),
                    "date",
                    binding.difficulty.toString(), binding.tag.toString(), binding.note.toString(), 0, binding.reminder.toString()
                    )

                var gson = Gson()
                var json = gson.toJson(problem)

                //add to preference
                preferences.edit {
                    putString("Problem ${binding.questionNum}", json)
                }
                            }
        }
        setContentView(R.layout.activity_edit_problem)
    }
}