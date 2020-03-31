package com.github.gibbrich.breakingnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.github.gibbrich.breakingnews.di.DI
import com.github.gibbrich.breakingnews.manager.INavigationManager
import com.github.gibbrich.breakingnews.ui.ArticleListFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationManager: INavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        DI.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()

        navigationManager.navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onPause() {
        super.onPause()

        navigationManager.navController = null
    }
}
