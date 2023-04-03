package gb.com.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import gb.com.ui.fragments.ImageConverterFragment

class Screens: IScreens {
    override fun imageConverterScreen() = FragmentScreen {
        ImageConverterFragment.newInstance()
    }
}