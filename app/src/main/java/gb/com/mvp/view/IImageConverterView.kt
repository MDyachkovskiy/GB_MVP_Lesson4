package gb.com.mvp.view

import android.graphics.Bitmap
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IImageConverterView: MvpView {
    fun showImage(bitmap: Bitmap)
    fun showMessage(message: String)
    fun showError(message: String)
    fun openGallery()
}