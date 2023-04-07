package gb.com.mvp.presenter.converter

import android.content.Intent
import android.graphics.Bitmap

interface IImageConverterPresenter {
    fun convertToPng(bitmap: Bitmap)
    fun saveImage(bitmap: Bitmap)
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun openGallery()
    fun cancelConversion()
    fun onConvertClicked(bitmap: Bitmap)
}