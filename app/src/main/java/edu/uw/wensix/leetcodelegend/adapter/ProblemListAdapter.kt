package edu.uw.wensix.leetcodelegend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.uw.wensix.leetcodelegend.databinding.ProblemItemBinding
import edu.uw.wensix.leetcodelegend.model.Problem

class ProblemListAdapter(
    private var listOfProblems: List<Problem>
    ): RecyclerView.Adapter<ProblemListAdapter.ProblemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemHolder {
        val binding = ProblemItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProblemHolder(binding)
    }

    override fun getItemCount(): Int =listOfProblems.size

    override fun onBindViewHolder(holder: ProblemHolder, position: Int) {
        val problem = listOfProblems[position]

        with(holder.binding) {
            problemId.text = problem.number.toString()
            problemTitle.text = problem.title
            problemDate.text = problem.date
        }
    }

    fun updateProblem(newList: List<Problem>) {
        this.listOfProblems = newList
    }

    class ProblemHolder(val binding: ProblemItemBinding): RecyclerView.ViewHolder(binding.root)
}
