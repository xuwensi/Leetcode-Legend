package edu.uw.wensix.leetcodelegend

import android.app.Application
import androidx.lifecycle.lifecycleScope
import edu.uw.wensix.leetcodelegend.model.Problem
import edu.uw.wensix.leetcodelegend.repository.DataRepository
import kotlinx.coroutines.launch

class LLApplication: Application() {

    lateinit var dataRepository: DataRepository

    override fun onCreate() {
        super.onCreate()

        this.dataRepository = DataRepository()
    }

}