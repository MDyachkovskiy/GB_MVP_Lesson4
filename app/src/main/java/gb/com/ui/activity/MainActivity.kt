package gb.com.ui.activity

import android.os.Bundle
import com.github.terrakok.cicerone.androidx.AppNavigator
import gb.com.App
import gb.com.R
import gb.com.databinding.ActivityMainBinding
import gb.com.mvp.model.presenter.MainPresenter
import gb.com.mvp.view.IMainView
import gb.com.navigation.Screens
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), IMainView {

    private val navigatorHolder = App.instance.navigatorHolder
    private val router = App.instance.router
    private val navigator = AppNavigator(this, R.id.container)

    private lateinit var binding: ActivityMainBinding

    private val presenter by moxyPresenter {
        MainPresenter(router, Screens())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}