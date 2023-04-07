package gb.com.mvp.presenter.converter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import gb.com.mvp.model.ImageModel
import gb.com.mvp.view.IImageConverterView
import gb.com.utility.PICK_IMAGE_REQUEST
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class ImageConverterPresenter(
    fragment: Fragment,
    private val model: ImageModel
    ): MvpPresenter<IImageConverterView>(), IImageConverterPresenter {

    private val context = fragment.context

    private var disposable: Disposable? = null
    private var isConverting = false

    override fun openGallery() {
        viewState.openGallery()
    }

    override fun convertToPng(bitmap: Bitmap) {
        viewState.showConversionDialog()
        disposable = model.convertToPng(bitmap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if(isConverting) {
                        viewState.dismissConversionDialog()
                        viewState.showImage(it)
                        viewState.showMessage("Image converted successfully")
                        saveImage(it)}
                    },
                {error ->
                    if(isConverting) {
                        viewState.dismissConversionDialog()
                        viewState.showError(error.message ?: "Unknown error")
                    }
                }
            )
    }

    override fun cancelConversion() {
        isConverting = false
        disposable?.dispose()
        viewState.dismissConversionDialog()
    }

    override fun saveImage(bitmap: Bitmap) {
        model.saveImage(context, bitmap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { displayName -> viewState.showMessage("Image saved successfully: $displayName")},
                {error -> viewState.showError(error.message ?: "Unknown error")}
            )
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
            data !=null && data.data != null) {
            val imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                viewState.showImage(bitmap)
                model.loadImage(imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { viewState.showImage(it)},
                        {error -> viewState.showError(error.message ?: "Failed to load image")},
                    )
            } catch (e: Exception) {
                e.message?.let { message ->
                    viewState.showError(message)
                }
                e.printStackTrace()
            }
        }
    }

    override fun onConvertClicked(bitmap: Bitmap) {
        isConverting = true
        convertToPng(bitmap)
    }
    }
