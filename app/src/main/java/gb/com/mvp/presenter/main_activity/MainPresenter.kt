package gb.com.mvp.presenter.main_activity

import com.github.terrakok.cicerone.Router
import gb.com.mvp.view.IMainView
import gb.com.navigation.IScreens
import moxy.MvpPresenter

class MainPresenter(
    private val router: Router,
    private val screens: IScreens
): MvpPresenter<IMainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.imageConverterScreen())
    }
}